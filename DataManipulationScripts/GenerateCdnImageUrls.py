import os
import re
import csv
import unicodedata
import urllib.parse
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

# Public static host served by kyoku-static-nginx through the cloudflared tunnel
CDN_BASE_URL = os.getenv("STATIC_CDN_BASE_URL", "https://kyoku-image-cdn.poulastaa.dev").rstrip("/")

# Compressed images that were uploaded to MinIO (same layout as the bucket)
COMPRESSED_DATA_DIR = os.getenv("COMPRESSED_DATA_DIR", "/Users/poulastaad/personal/data/compressed")

# Content DB seed files, rewritten in place with full CDN urls
SQL_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "kyoku-docker", "mysql", "content", "sql")
GENRE_SQL = os.path.join(SQL_DIR, "04_insert_content_db_genre.sql")
ARTIST_SQL = os.path.join(SQL_DIR, "05_insert_content_db_artist.sql")
SONG_SQL = os.path.join(SQL_DIR, "06_insert_content_db_song.sql")

# Report of rows set to NULL (no image / url too long)
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "output")
REPORT_FILE = os.path.join(OUTPUT_DIR, "cdn_url_report.csv")

# Compressed folder -> bucket prefix (must match kyoku-docker/minio/static/setup-static-bucket.sh)
ARTIST_FOLDER, ARTIST_PREFIX = "artistCover", "artist"
GENRE_FOLDER, GENRE_PREFIX = "genreCover", "genre"
SONG_FOLDER, SONG_PREFIX = "songCover", "poster"

# Column limits from 01_content_db.sql
COVER_IMAGE_MAX_LENGTH = 200
POSTER_MAX_LENGTH = 300

# Matches the stored image path inside a seed row, e.g. "/artist/A%20Bazz.jpg"
ARTIST_ROW = re.compile(r'(\(\s*(\d+)\s*,\s*"(?:[^"\\]|\\.)*"\s*,\s*)("/artist/[^"]*"|NULL)(\s*\))')
GENRE_ROW = re.compile(r'(\(\s*(\d+)\s*,\s*"(?:[^"\\]|\\.)*"\s*,\s*)("/genre/[^"]*"|NULL)(\s*\))')
SONG_ROW = re.compile(r'(\(\s*(\d+)\s*,\s*"(?:[^"\\]|\\.)*"\s*,\s*)("/song/[^"]*"|NULL)(\s*,)')

IMAGE_NUMBER = re.compile(r"_(\d+)$")


def nfc(value):
    return unicodedata.normalize("NFC", value)


def fix_mojibake(value):
    """Undo UTF-8 text that was decoded as cp1252 / latin-1 (e.g. 'Ã¡' -> 'á')"""
    for encoding in ("cp1252", "latin-1"):
        try:
            return value.encode(encoding).decode("utf-8")
        except (UnicodeEncodeError, UnicodeDecodeError):
            continue
    return value


def name_candidates(stem):
    """Possible on disk names for a stored name (raw, mojibake fixed, nbsp / trailing dot cleaned)"""
    candidates = []
    for base in (stem, fix_mojibake(stem)):
        for value in (
                base,
                base.replace("\xa0", " "),
                base.rstrip(". "),
                base.replace("\xa0", " ").rstrip(". ").strip(),
        ):
            value = nfc(value)
            if value not in candidates:
                candidates.append(value)
    return candidates


def stored_stem(stored_value, prefix):
    """'/artist/A%20Bazz.jpg' -> 'A Bazz'"""
    path = urllib.parse.unquote(stored_value.strip('"'))
    return os.path.splitext(path[len(f"/{prefix}/"):])[0]


def cdn_url(prefix, *parts):
    encoded = "/".join(urllib.parse.quote(nfc(part), safe="") for part in parts)
    return f"{CDN_BASE_URL}/{prefix}/{encoded}"


def build_folder_index(folder):
    """Folder per entity (artist / genre): normalized folder name -> (folder name, primary image file)"""
    root = os.path.join(COMPRESSED_DATA_DIR, folder)
    index = {}
    for entry in os.listdir(root):
        entry_path = os.path.join(root, entry)
        if not os.path.isdir(entry_path):
            continue

        images = [f for f in os.listdir(entry_path) if f != ".DS_Store" and not f.endswith(".part")]
        if not images:
            continue

        # Primary image = lowest numbered file (_1 is missing for some entries)
        def sort_key(file_name):
            match = IMAGE_NUMBER.search(os.path.splitext(file_name)[0])
            return (int(match.group(1)) if match else 10 ** 9, file_name)

        index[nfc(entry)] = (entry, sorted(images, key=sort_key)[0])
    return index


def build_file_index(folder):
    """Flat folder (song posters): normalized file stem -> file name"""
    root = os.path.join(COMPRESSED_DATA_DIR, folder)
    return {
        nfc(os.path.splitext(f)[0]): f
        for f in os.listdir(root)
        if f != ".DS_Store" and not f.endswith(".part")
    }


def resolve_folder_entity(stored_value, prefix, folder_prefix, index):
    for candidate in name_candidates(stored_stem(stored_value, prefix)):
        if candidate in index:
            entry, image = index[candidate]
            return cdn_url(folder_prefix, entry, image)
    return None


def resolve_song_poster(stored_value, index):
    for candidate in name_candidates(stored_stem(stored_value, "song")):
        if candidate in index:
            return cdn_url(SONG_PREFIX, index[candidate])
    return None


def sql_string(value):
    return "NULL" if value is None else '"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"'


def process_seed_file(sql_file, row_pattern, resolver, max_length, table, column, report_rows):
    """Resolve every row of a seed file. Returns the rewritten sql"""
    with open(sql_file, encoding="utf-8") as f:
        content = f.read()

    stats = {"resolved": 0, "missing": 0, "null": 0}

    def replace(match):
        row_id, stored_value = int(match.group(2)), match.group(3)

        if stored_value == "NULL":
            stats["null"] += 1
            return match.group(0)

        url = resolver(stored_value)
        if url is not None and len(url) > max_length:
            report_rows.append([table, row_id, stored_value, "url_too_long", url])
            url = None

        if url is None:
            stats["missing"] += 1
            report_rows.append([table, row_id, stored_value, "no_image", ""])
        else:
            stats["resolved"] += 1

        return match.group(1) + sql_string(url) + match.group(4)

    rewritten = row_pattern.sub(replace, content)
    print(f"{table}.{column}: resolved {stats['resolved']}, no image -> NULL {stats['missing']}, "
          f"already NULL {stats['null']}")
    return rewritten


def generate():
    for folder in (ARTIST_FOLDER, GENRE_FOLDER, SONG_FOLDER):
        if not os.path.isdir(os.path.join(COMPRESSED_DATA_DIR, folder)):
            print(f"❌ Error: {os.path.join(COMPRESSED_DATA_DIR, folder)} does not exist")
            return

    artist_index = build_folder_index(ARTIST_FOLDER)
    genre_index = build_folder_index(GENRE_FOLDER)
    song_index = build_file_index(SONG_FOLDER)

    report_rows = []

    artist_sql = process_seed_file(
        ARTIST_SQL, ARTIST_ROW,
        lambda value: resolve_folder_entity(value, "artist", ARTIST_PREFIX, artist_index),
        COVER_IMAGE_MAX_LENGTH, "Artist", "cover_image", report_rows,
    )
    genre_sql = process_seed_file(
        GENRE_SQL, GENRE_ROW,
        lambda value: resolve_folder_entity(value, "genre", GENRE_PREFIX, genre_index),
        COVER_IMAGE_MAX_LENGTH, "Genre", "cover_image", report_rows,
    )
    song_sql = process_seed_file(
        SONG_SQL, SONG_ROW,
        lambda value: resolve_song_poster(value, song_index),
        POSTER_MAX_LENGTH, "Song", "poster", report_rows,
    )

    for sql_file, content in ((ARTIST_SQL, artist_sql), (GENRE_SQL, genre_sql), (SONG_SQL, song_sql)):
        with open(sql_file, "w", encoding="utf-8") as f:
            f.write(content)

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    with open(REPORT_FILE, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["table", "id", "old_value", "status", "detail"])
        writer.writerows(report_rows)

    # Summary
    print("\n" + "=" * 50)
    print(f"Seed files rewritten: {GENRE_SQL}, {ARTIST_SQL}, {SONG_SQL}")
    print(f"Report: {REPORT_FILE} ({len(report_rows)} rows set to NULL)")
    print("=" * 50)


if __name__ == "__main__":
    print("CDN Image URL Generator")
    print("=" * 30)
    print(f"CDN base url: {CDN_BASE_URL}")
    print(f"Compressed images: {COMPRESSED_DATA_DIR}")

    generate()
