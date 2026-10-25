# Static Images Setup (MinIO + nginx cache + Cloudflare)

Public artist / genre / song poster images, served from `https://kyoku-static.poulastaa.dev`.

```
client -> Cloudflare edge cache -> tunnel "kyoku" -> kyoku-static-nginx :1084 (disk cache) -> kyoku-file-minio bucket "cdn"
```

Tunnel details: `cloudflare-tunnel.md`.

## Layout

| What                 | Where                                                       |
|----------------------|-------------------------------------------------------------|
| Source images        | `personal/data/compressed` (mounted read-only)              |
| MinIO data           | `personal/data/minio/file` (`MINIO_DATA_DIR`)               |
| nginx cache          | `personal/data/nginx/static-cache` (`NGINX_CACHE_DIR`)      |
| MinIO image          | `cgr.dev/chainguard/minio:latest-dev` (`minio/minio` no longer published) |
| Bucket               | `cdn`, anonymous `s3:GetObject` only                        |
| nginx config         | `nginx/static/static.conf.template`                         |
| Bucket setup script  | `minio/static/setup-static-bucket.sh`                       |
| DB url generator     | `DataManipulationScripts/GenerateCdnImageUrls.py`           |

| Compressed folder                      | Object / public path                         |
|----------------------------------------|----------------------------------------------|
| `artistCover/<Artist>/<Artist>_N.webp` | `/artist/<Artist>/<Artist>_N.webp`           |
| `genreCover/<Genre>/<Genre>_N.webp`    | `/genre/<Genre>/<Genre>_N.webp`              |
| `songCover/<Song>.webp`                | `/poster/<Song>.webp`                        |

The DB stores the full URL of the lowest numbered image per artist / genre (`_1` is missing for some).

Data size: ~8.2 GB compressed. MinIO keeps its own copy, plus up to 10 GB nginx cache.

## Steps

Run in order. Each step has a check; do not continue until it passes.

### 1. Environment

Add to `kyoku-docker/.env` (see `.env.example`):

```
MINIO_FILE_ROOT_USER=...
MINIO_FILE_ROOT_PASSWORD=...
MINIO_DATA_DIR=/Users/poulastaad/personal/data/minio
COMPRESSED_DATA_DIR=/Users/poulastaad/personal/data/compressed
NGINX_CACHE_DIR=/Users/poulastaad/personal/data/nginx/static-cache
STATIC_BUCKET=cdn
KYOKU_UID=502
KYOKU_GID=20
```

`KYOKU_UID` / `KYOKU_GID` = output of `id -u` / `id -g`.

**Existing MinIO data:** the volume moved from `./data/minio/file/upload-storage` to `personal/data/minio/file`.
If the old folder has data (e.g. the `static` bucket with logos), copy it first:
`cp -R kyoku-docker/data/minio/file/upload-storage/. ../data/minio/file/`.

Create the folders:

```bash
mkdir -p /Users/poulastaad/personal/data/minio/file /Users/poulastaad/personal/data/nginx/static-cache
```

Check: `docker compose config --quiet` prints nothing.

### 2. Start MinIO

```bash
cd kyoku-docker
docker compose up -d kyoku-file-minio
```

Check: `curl -f http://localhost:1080/minio/health/live` returns `200`, console opens at `http://localhost:1081`.

### 3. Create bucket and upload images

```bash
docker compose --profile static-setup run --rm kyoku-static-setup
```

Re-runnable: only missing objects are uploaded.

Check (end of the script output):

| prefix | expected objects |
|--------|------------------|
| artist | 32822            |
| genre  | 205              |
| poster | 69964            |

Check public access rules:

```bash
curl -s -o /dev/null -w "%{http_code}\n" "http://localhost:1080/cdn/genre/Jazz/Jazz_1.webp"   # 200
curl -s -o /dev/null -w "%{http_code}\n" "http://localhost:1080/cdn/"                        # 403 (no listing)
```

### 4. Start nginx cache

```bash
docker compose up -d kyoku-static-nginx
```

Check (run twice):

```bash
curl -I http://localhost:1084/genre/Jazz/Jazz_1.webp
```

First `X-Cache-Status: MISS`, then `HIT`. `Cache-Control: public, max-age=31536000, immutable`.
`curl -I http://localhost:1084/poster/does-not-exist.webp` → `404`.

### 5. Cloudflare

1. Tunnel ingress already added in `~/.cloudflared/config.yml` (and `/etc/cloudflared/config.yml`):
   `kyoku-static.poulastaa.dev -> http://localhost:1084`.
2. DNS record (once): `cloudflared tunnel route dns kyoku kyoku-static.poulastaa.dev`
3. Restart the tunnel (`cloudflared tunnel --config ~/.cloudflared/config.yml run kyoku`).
4. Cloudflare dashboard → `poulastaa.dev` → Caching → Cache Rules → Create rule:
   - When: `Hostname equals kyoku-static.poulastaa.dev`
   - Cache eligibility: **Eligible for cache**
   - Edge TTL: **Ignore cache-control header and use this TTL** → 1 month
   - Browser TTL: **Respect origin TTL**

Check (run twice):

```bash
curl -I https://kyoku-static.poulastaa.dev/genre/Jazz/Jazz_1.webp
```

Second call: `CF-Cache-Status: HIT`.

### 6. Database (last)

Only after step 5 passes.

1. Backup:

   ```bash
   docker exec content-primary sh -c 'mysqldump -uroot -p"$MYSQL_ROOT_PASSWORD" --single-transaction CONTENT' > content-backup-$(date +%F).sql
   ```

2. Generate URLs (reads compressed folder + seed SQL; writes `DataManipulationScripts/output/` and rewrites the seed files `04/05/06_insert_content_db_*.sql`):

   ```bash
   cd DataManipulationScripts
   python GenerateCdnImageUrls.py
   ```

   Expected: genre and song posters all resolve; ~31 artists → `NULL` (their raw images were not real images, see `compressed/compression_report.csv`). Details in `output/cdn_url_report.csv`.

3. Review the diff of the seed files and `output/update_content_db_cdn_urls.sql`.

4. Apply to the running DB (primary only, replicas follow):

   ```bash
   docker exec -i content-primary sh -c 'mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < DataManipulationScripts/output/update_content_db_cdn_urls.sql
   ```

5. Refresh data copied from the DB:
   - Elasticsearch artists: `docker compose --profile setup run --rm elastic-setup`
   - Redis cached objects with old paths: `GENRE` (content, 30 min TTL), `SONG:*` (playlist, 6 h TTL). Wait for TTL or delete:
     `docker exec gateway-redis redis-cli -a contentRedis --scan --pattern 'SONG:*' | xargs -r docker exec -i gateway-redis redis-cli -a contentRedis del` and the same for `GENRE`.

Check: pick a few rows

```sql
SELECT cover_image FROM CONTENT.Artist ORDER BY RAND() LIMIT 3;
SELECT poster FROM CONTENT.Song ORDER BY RAND() LIMIT 3;
```

and `curl -I` each URL → `200`.

## Notes

- Changing the domain later = bulk `UPDATE` (full URLs are stored). Re-run the generator with a new `STATIC_CDN_BASE_URL`.
- Never overwrite an object in `cdn`; caches keep the old one for up to a year. Upload under a new name.
- Cloudflare free plan CDN terms: fine for normal app images; revisit if image traffic becomes very large.
