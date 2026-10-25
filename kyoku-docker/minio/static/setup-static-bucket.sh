#!/bin/sh
# Creates the public static bucket and uploads compressed images into it.
# Runs inside kyoku-static-setup (cgr.dev/chainguard/minio-client:latest-dev):
#   docker compose --profile static-setup run --rm kyoku-static-setup
#
# Source (mounted read-only at /source)   ->  Bucket path
#   artistCover/<Artist>/<Artist>_N.webp  ->  <bucket>/artist/<Artist>/<Artist>_N.webp
#   genreCover/<Genre>/<Genre>_N.webp     ->  <bucket>/genre/<Genre>/<Genre>_N.webp
#   songCover/<Song>.webp                 ->  <bucket>/poster/<Song>.webp
#
# Safe to re-run: mc mirror only uploads missing / changed objects.
set -eu

ALIAS="local"
BUCKET="${STATIC_BUCKET:-cdn}"
SOURCE_DIR="/source"

echo "================================================"
echo "        Kyoku Static Bucket Setup"
echo "================================================"

for folder in artistCover genreCover songCover; do
    if [ ! -d "$SOURCE_DIR/$folder" ]; then
        echo "[ERROR] Missing source folder: $SOURCE_DIR/$folder"
        exit 1
    fi
done

echo "[INFO] Connecting to $MINIO_ENDPOINT ..."
mc alias set "$ALIAS" "$MINIO_ENDPOINT" "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD" >/dev/null

echo "[INFO] Ensuring bucket: $BUCKET"
mc mb --ignore-existing "$ALIAS/$BUCKET"

# Anonymous read of single objects only. No s3:ListBucket, so the bucket content cannot be enumerated.
echo "[INFO] Applying public read-only (GetObject) policy"
POLICY_FILE="$(mktemp)"
cat > "$POLICY_FILE" <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": { "AWS": ["*"] },
      "Action": ["s3:GetObject"],
      "Resource": ["arn:aws:s3:::$BUCKET/*"]
    }
  ]
}
EOF
mc anonymous set-json "$POLICY_FILE" "$ALIAS/$BUCKET"
rm -f "$POLICY_FILE"

mirror_folder() {
    source_folder="$1"
    target_prefix="$2"

    echo "[INFO] Uploading $source_folder -> $BUCKET/$target_prefix/"
    mc mirror --overwrite --exclude ".DS_Store" --exclude "*.part" \
        "$SOURCE_DIR/$source_folder" "$ALIAS/$BUCKET/$target_prefix"
}

mirror_folder artistCover artist
mirror_folder genreCover genre
mirror_folder songCover poster

echo ""
echo "[INFO] Object count per prefix:"
for prefix in artist genre poster; do
    count="$(mc ls --recursive "$ALIAS/$BUCKET/$prefix/" | wc -l | tr -d ' ')"
    echo "  $prefix: $count"
done

echo "[SUCCESS] Static bucket ready: $BUCKET"
