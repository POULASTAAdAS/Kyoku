# Song Streaming Setup — Signed URLs + nginx

Status: **planned, not implemented.** Final design agreed on 2026-10-22.

## 1. Goal

Stream songs (HLS) to Android, iOS and desktop clients so that **every request is authenticated before any data is sent**:

1. master manifest (`master.m3u8`)
2. bitrate manifest (`128/playlist.m3u8`, `320/playlist.m3u8`)
3. every audio chunk

Song bytes must never pass through the JVM. Storage should hold as many songs as possible on the `DATA` drive.

## 2. Decisions and reasons

| Decision | Reason |
|---|---|
| Move songs **out of MinIO** into plain files on disk | Measured on `DATA` (exFAT, 128 KB allocation block): one 263 KB segment takes **768 KB** under MinIO (2 dirs + `xl.meta` + `part.1`) vs **384 KB** as a plain file, so plain files hold **~2× more**. |
| **Keep m4a/fMP4 (AAC)**, do not convert to `.ts` | The existing segments are already AAC in m4a. MPEG-TS adds ~10%+ container overhead for audio. All current HLS players support fMP4. |
| **One `.m4a` per bitrate per song** + `#EXT-X-BYTERANGE` | Removes the 128 KB block waste on small segment files (~20% today) and cuts file count ~25×. nginx serves byte ranges natively. |
| Auth = **microservice issues signed URL once per song; nginx checks the signature on every request** | Standard CDN token-auth pattern (CloudFront signed URLs / Cloudflare token auth). Every request is checked, with no JVM call per chunk. |
| **Rejected:** client sends JWT on every chunk to a microservice | iOS AVPlayer cannot attach custom headers to segment requests; the JWT would expire mid-song; 25+ JVM requests per play; nginx would need the HS256 JWT secret, which can forge logins. |
| Images (`static`, `genre`, `artist`, poster) **stay in MinIO** | Small store, served by existing `file` service `FileService` + cache. Out of scope. |

## 3. Architecture

```
                         (1) GET /api/v1/song/{id}/stream   Authorization: Bearer <JWT>
 Client ──────────────► gateway ──► song service  ── checks JWT + rights, returns signed URL
   │                                                                   │
   │  ◄────────────── { "url": "https://media.<domain>/s/{uid}/{exp}/{sig}/{songId}/master.m3u8" }
   │
   │ (2) GET master.m3u8, then 128/playlist.m3u8, then byte ranges of 128/audio.m4a
   ▼
 Cloudflare (cache BYPASS for /s/*) ──► cloudflared tunnel ──► nginx
                                                                 │ secure_link check on EVERY request
                                                                 │ 403 bad sig / 410 expired
                                                                 ▼
                                                    /Volumes/DATA/songs (mounted :ro)
```

- Only **nginx** is exposed through the tunnel for media. No direct exposure of storage.
- The signing secret is shared only by the song service and nginx. It is **not** the JWT secret.

## 4. Storage layout on `DATA`

```
/Volumes/DATA/songs/{songId}/master.m3u8
/Volumes/DATA/songs/{songId}/128/playlist.m3u8
/Volumes/DATA/songs/{songId}/128/audio.m4a
/Volumes/DATA/songs/{songId}/320/playlist.m3u8
/Volumes/DATA/songs/{songId}/320/audio.m4a
```

**All URIs inside manifests must be relative.** This is what carries the token to every request without rewriting files.

`master.m3u8`:
```m3u8
#EXTM3U
#EXT-X-VERSION:7
#EXT-X-STREAM-INF:BANDWIDTH=140000,CODECS="mp4a.40.2"
128/playlist.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=340000,CODECS="mp4a.40.2"
320/playlist.m3u8
```

`128/playlist.m3u8` (byte-range, single file):
```m3u8
#EXTM3U
#EXT-X-VERSION:7
#EXT-X-TARGETDURATION:10
#EXT-X-PLAYLIST-TYPE:VOD
#EXT-X-MAP:URI="audio.m4a",BYTERANGE="830@0"
#EXTINF:10.0,
#EXT-X-BYTERANGE:163840@830
audio.m4a
#EXTINF:10.0,
#EXT-X-BYTERANGE:163840@164670
audio.m4a
...
#EXT-X-ENDLIST
```

Generate with ffmpeg (per bitrate):
```bash
ffmpeg -i input.flac -vn -c:a aac -b:a 128k \
  -f hls -hls_time 10 -hls_playlist_type vod \
  -hls_segment_type fmp4 -hls_flags single_file \
  -hls_fmp4_init_filename audio.m4a \
  128/playlist.m3u8
```
(Check the produced file names; rename the single output to `audio.m4a` and update the playlist if ffmpeg names it differently.)

## 5. Signed URL format

```
/s/{uid}/{exp}/{sig}/{songId}/{file}
```

| Part | Meaning |
|---|---|
| `uid` | user id (numeric) — ties the URL to a user, useful for logs and abuse tracking |
| `exp` | expiry, Unix seconds |
| `sig` | `base64url( md5( "{exp}{uid}{songId} {secret}" ) )`, no padding |
| `songId` | folder name under `/Volumes/DATA/songs` |
| `file` | `master.m3u8` or `{128\|320}/{name}` |

The signature covers **user + song + expiry**, so one token unlocks every file of that one song for that one user until `exp`.

**Expiry:** song duration + buffer, default **1 hour**. Configure via property.

## 6. nginx

### 6.1 Server config — `kyoku-docker/nginx/media.conf`

```nginx
server {
    listen 80;
    server_name _;

    # secret kept outside the repo; file contains:  set $media_secret "....";
    include /etc/nginx/secrets/media_secret.conf;

    location ~ ^/s/(?<uid>\d+)/(?<exp>\d+)/(?<sig>[A-Za-z0-9_-]+)/(?<song>[A-Za-z0-9_-]+)/(?<file>master\.m3u8|(?:128|320)/[A-Za-z0-9_.-]+)$ {
        secure_link     $sig,$exp;
        secure_link_md5 "$exp$uid$song $media_secret";

        if ($secure_link = "")  { return 403; }   # signature mismatch
        if ($secure_link = "0") { return 410; }   # expired

        alias /data/songs/$song/$file;

        types {
            application/vnd.apple.mpegurl m3u8;
            audio/mp4                     m4a;
        }

        add_header Cache-Control "private, no-store" always;
        add_header Access-Control-Allow-Origin "*" always;   # tighten if web client exists
    }

    location / { return 404; }
}
```

Notes:
- The strict regex on `file` blocks `../` traversal and only allows expected files.
- `Range` requests (needed for `#EXT-X-BYTERANGE`) work automatically for static files.
- `secure_link` is built into the official `nginx` image.
- nginx `secure_link` only supports MD5. With a private secret + short expiry this is the standard setup. If HMAC-SHA256 is required later, use the njs module (`js_set` computing HMAC) with the same URL format.

### 6.2 docker-compose entry — `kyoku-docker/docker-compose.yml`

```yaml
  kyoku-media-nginx:
    image: nginx:1.27-alpine          # pin a version
    container_name: kyoku-media-nginx
    restart: unless-stopped
    ports:
      - "1090:80"
    volumes:
      - ./nginx/media.conf:/etc/nginx/conf.d/default.conf:ro
      - ./nginx/secrets:/etc/nginx/secrets:ro          # gitignored
      - /Volumes/DATA/songs:/data/songs:ro
```

- `nginx/secrets/` must be added to `kyoku-docker/.gitignore`.
- Port `1090` is a proposal; pick a free one in the existing port scheme.

### 6.3 cloudflared

- Add an ingress rule: `media.<domain>` → `http://kyoku-media-nginx:80` (or `http://localhost:1090`).
- See existing notes in `kyoku-docker/cloudflare-tunnel.md`.
- Cloudflare **Cache Rule: Bypass cache** for `media.<domain>/s/*`, so an expired URL can never be served from Cloudflare cache.
- Before relying on it, check the current Cloudflare terms about serving large volumes of audio through the free plan / tunnel.

## 7. Song service (Kotlin / Spring)

Add to the service that owns songs (follow existing package, naming, `Endpoints` and config patterns of that service; `file` service uses `Endpoints.BASE_URL = "/api/v1/file"` style).

### 7.1 Config properties

```yaml
media:
  base-url: https://media.<domain>
  secret: ${MEDIA_STREAM_SECRET}     # same value as nginx $media_secret
  ttl: 1h
```

### 7.2 Signer

```kotlin
@Component
class MediaUrlSigner(
    private val props: MediaProperties,
) {
    fun sign(uid: Long, songId: String): SignedStreamUrl {
        val exp = Instant.now().plus(props.ttl).epochSecond
        val md5 = MessageDigest.getInstance("MD5")
            .digest("$exp$uid$songId ${props.secret}".toByteArray(Charsets.UTF_8))
        val sig = Base64.getUrlEncoder().withoutPadding().encodeToString(md5)

        return SignedStreamUrl(
            url = "${props.baseUrl}/s/$uid/$exp/$sig/$songId/master.m3u8",
            expiresAt = exp,
        )
    }
}
```

`songId` must match `[A-Za-z0-9_-]+` (same as the nginx regex). Use the DB id or a slug.

### 7.3 Endpoint

```
GET /api/v1/song/{songId}/stream      (JWT required, via gateway)
200 { "url": "...master.m3u8", "expiresAt": 1792700000 }
403 user may not play this song
404 song not found
```

Logic: validate JWT → load song → check rights (subscription, region, ban…) → `signer.sign(userId, songId)`.

## 8. Client behaviour

- Call `/stream`, then hand `url` directly to the player (Media3 ExoPlayer / AVPlayer / desktop player). **No custom headers needed.**
- On **410** (expired) while playing, or when resuming after a long pause: call `/stream` again and reload at the current position.
- On **403**: treat as access denied; do not retry blindly.

## 9. Security properties and limits

| Property | Status |
|---|---|
| Every manifest and chunk request is authenticated | Yes — nginx `secure_link` |
| JVM handles audio bytes | No |
| Token tied to user + song + expiry | Yes |
| Shared URL usable by someone else | Until `exp` only — accepted trade-off; keep TTL short |
| Instant revocation (ban mid-song) | Not by default. Optional: nginx `auth_request` to a tiny check endpoint, with the answer cached ~30–60 s |
| Path traversal | Blocked by regex |
| Storage writes | None — `DATA` mounted read-only |

## 10. Migration from MinIO (drive is ~100% full)

Must be done **song by song**, since there is no space for a full copy:

1. Free some space first (e.g. delete one song from MinIO via `mc rm`).
2. For each song in MinIO bucket `song` (store `/Volumes/DATA/minio/song/upload-storage`):
   1. Export its objects with `mc cp --recursive` to a temp folder **on the internal disk**.
   2. Rebuild to the new layout (single `audio.m4a` per bitrate + byte-range playlists), ideally from the original source audio; otherwise remux existing segments with ffmpeg.
   3. Write to `/Volumes/DATA/songs/{songId}/`.
   4. Verify playback through nginx with a signed URL.
   5. `mc rm --recursive` that song from MinIO (frees ~2× what the copy used).
3. When the `song` bucket is empty, remove the `kyoku-song-minio` container and its compose entry.
4. The `file` MinIO store (images) is unchanged.

The migration script deletes data — run only with explicit approval, on a small batch first.

## 11. Later: move the check to the CDN edge

When traffic grows, a Cloudflare Worker can verify the same signature at the edge and cache chunks with a cache key that excludes `uid/exp/sig`. The URL format and Kotlin signer stay the same; nginx becomes the origin only.

## 12. Implementation checklist

- [ ] Create `/Volumes/DATA/songs/` layout and convert a test song
- [ ] `kyoku-docker/nginx/media.conf` + secret file + `.gitignore` entry
- [ ] `kyoku-media-nginx` compose service (read-only DATA mount)
- [ ] `MEDIA_STREAM_SECRET` in `.env` / `.env.example`
- [ ] `media` properties + `MediaUrlSigner` + `/stream` endpoint in the song-owning service
- [ ] Gateway route for `/api/v1/song/{id}/stream`
- [ ] cloudflared ingress for `media.<domain>` + Cloudflare cache bypass rule
- [ ] Client: play signed URL, handle 410 / 403
- [ ] Migration script (approval required), then retire `kyoku-song-minio`
