// Signed-URL verification for kyoku-media-nginx.
//
// URL: /<expires>/<uid>/<token>/master/<song_dir>/<file>
// token = base64url( HMAC-SHA256(secret, "<expires>:<uid>:/master/<song_dir>/") ), no padding
//
// Must stay byte-for-byte identical to the content service signer.

var crypto = require('crypto');

var SECRET = process.env.MEDIA_TOKEN_SECRET || '';

var URI_PATTERN = /^\/(\d+)\/([0-9a-f]{16})\/([A-Za-z0-9_-]+)\/(master\/[^\/]+\/)/;

function hmacBase64Url(key, data) {
    return crypto.createHmac('sha256', key).update(data).digest('base64url');
}

// Constant-time compare: HMAC both values with a per-call key so the compared digests are
// fixed-length and unrelated to the inputs; the early-exit === then leaks nothing useful.
function timingSafeEqual(a, b) {
    var key = crypto.createHash('sha256').update(SECRET + ':cmp:' + Date.now()).digest('hex');
    return hmacBase64Url(key, a) === hmacBase64Url(key, b);
}

function verify(r) {
    if (SECRET.length === 0) {
        r.error('media_auth: MEDIA_TOKEN_SECRET is not set');
        return '0';
    }

    // r.uri is the decoded, normalized URI
    var m = r.uri.match(URI_PATTERN);
    if (!m) return '0';

    var expires = m[1];
    var uid = m[2];
    var token = m[3];
    var prefix = '/' + m[4]; // "/master/<song_dir>/"

    if (parseInt(expires, 10) < Math.floor(Date.now() / 1000)) return '0';

    var data = expires + ':' + uid + ':' + prefix;
    return timingSafeEqual(hmacBase64Url(SECRET, data), token) ? '1' : '0';
}

export default { verify };
