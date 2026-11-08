"""
FixSongMasterPlaylistUrls.py

Rewrites song.master_playlist in the CONTENT seed SQL
(kyoku-docker/mysql/content/sql/06_insert_content_db_song.sql) from the old MinIO form

    /song/<song_dir>/<file>_master.m3u8

to the on-disk form served by kyoku-media-nginx (signed URL prefix + this value):

    master/<song_dir>/<file>_master.m3u8

Every rewritten value is resolved against the real file under /Volumes/DATA/master:
the stored value is tried as-is, then percent-decoded (e.g. "10%2B2" -> "10+2"),
each also in NFD / NFC form. Values that cannot be resolved are left untouched and recorded.

Default mode is DRY-RUN (nothing is written). Pass --apply to rewrite the SQL file.
Unresolved rows are written to output/song_master_playlist_url_failures.jsonl and a summary
to output/song_master_playlist_url_report.json.

Usage:
    python3 FixSongMasterPlaylistUrls.py            # dry-run
    python3 FixSongMasterPlaylistUrls.py --apply    # rewrite the SQL file
"""

import argparse
import json
import os
import re
import sys
import tempfile
import time
import unicodedata
import urllib.parse
from datetime import datetime

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SQL_DIR = os.path.join(BASE_DIR, "..", "kyoku-docker", "mysql", "content", "sql")
SONG_SQL = os.path.join(SQL_DIR, "06_insert_content_db_song.sql")

DATA_ROOT = "/Volumes/DATA"
MASTER_DIR = "master"

OUTPUT_DIR = os.path.join(BASE_DIR, "output")
FAILURES_FILE = os.path.join(OUTPUT_DIR, "song_master_playlist_url_failures.jsonl")
REPORT_FILE = os.path.join(OUTPUT_DIR, "song_master_playlist_url_report.json")

# Column limit from 01_content_db.sql (song.master_playlist)
MASTER_PLAYLIST_MAX_LENGTH = 300

# (<id> , "<title>" , <poster> , "<master_playlist>")  -- captures id and the master_playlist literal
SONG_ROW = re.compile(
    r'(\(\s*(\d+)\s*,\s*"(?:[^"\\]|\\.)*"\s*,\s*(?:"(?:[^"\\]|\\.)*"|(?i:NULL))\s*,\s*)"((?:[^"\\]|\\.)*)"(\s*\))'
)

# master/<song_dir>/<file>.m3u8 (same shape SongStreamService accepts)
TARGET_SHAPE = re.compile(r"^master/[^/]+/[^/]+\.m3u8$")


def sql_unescape(value):
    return re.sub(r"\\(.)", r"\1", value)


def sql_escape(value):
    return value.replace("\\", "\\\\").replace('"', '\\"')


def candidates(rest):
    """Possible on-disk relative paths (under master/) for a stored '/song/<rest>' value"""
    seen = []
    for base in (rest, urllib.parse.unquote(rest)):
        for form in (None, "NFD", "NFC"):
            value = unicodedata.normalize(form, base) if form else base
            if value not in seen:
                seen.append(value)
    return seen


def resolve(stored):
    """Returns (new_value, None) or (None, reason)"""
    if TARGET_SHAPE.match(stored):
        return stored, None  # already migrated (idempotent re-run)

    if not stored.startswith("/song/"):
        return None, "unexpected_prefix"

    rest = stored[len("/song/"):]
    for candidate in candidates(rest):
        if os.path.isfile(os.path.join(DATA_ROOT, MASTER_DIR, candidate)):
            new_value = f"{MASTER_DIR}/{candidate}"
            if not TARGET_SHAPE.match(new_value):
                return None, "unexpected_shape"
            if len(new_value) > MASTER_PLAYLIST_MAX_LENGTH:
                return None, "too_long"
            return new_value, None

    return None, "file_missing_on_disk"


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--apply", action="store_true", help="rewrite the SQL file (default: dry-run)")
    args = parser.parse_args()

    if not os.path.isdir(os.path.join(DATA_ROOT, MASTER_DIR)):
        sys.exit(f"Data root not mounted: {os.path.join(DATA_ROOT, MASTER_DIR)}")

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    started = time.time()
    mode = "APPLY" if args.apply else "DRY-RUN"
    print(f"Song master_playlist URL migration [{mode}] sql={os.path.normpath(SONG_SQL)}")

    with open(SONG_SQL, encoding="utf-8") as f:
        sql = f.read()

    stats = {"rows_seen": 0, "rows_rewritten": 0, "rows_already_ok": 0, "rows_decoded": 0, "failures": 0}
    failures = []

    def replace(match):
        prefix, song_id, literal, suffix = match.group(1), match.group(2), match.group(3), match.group(4)
        stats["rows_seen"] += 1
        stored = sql_unescape(literal)

        new_value, reason = resolve(stored)
        if reason:
            stats["failures"] += 1
            failures.append({
                "id": int(song_id),
                "stored": stored,
                "reason": reason,
                "ts": datetime.now().isoformat(timespec="seconds"),
            })
            return match.group(0)

        if new_value == stored:
            stats["rows_already_ok"] += 1
            return match.group(0)

        stats["rows_rewritten"] += 1
        if new_value[len(MASTER_DIR) + 1:] != stored[len("/song/"):]:
            stats["rows_decoded"] += 1

        if stats["rows_seen"] % 5000 == 0:
            print(f"{stats['rows_seen']:,} rows | rewritten {stats['rows_rewritten']:,} | fail {stats['failures']}")

        return f'{prefix}"{sql_escape(new_value)}"{suffix}'

    new_sql = SONG_ROW.sub(replace, sql)

    with open(FAILURES_FILE, "w", encoding="utf-8") as f:
        for item in failures:
            f.write(json.dumps(item, ensure_ascii=False) + "\n")

    if args.apply and new_sql != sql:
        fd, tmp = tempfile.mkstemp(dir=os.path.dirname(SONG_SQL), prefix=".tmp_song_")
        with os.fdopen(fd, "w", encoding="utf-8") as f:
            f.write(new_sql)
        os.replace(tmp, SONG_SQL)

    report = {
        "mode": mode,
        "sql_file": os.path.normpath(SONG_SQL),
        **stats,
        "duration_seconds": round(time.time() - started, 1),
        "finished_at": datetime.now().isoformat(timespec="seconds"),
    }
    with open(REPORT_FILE, "w", encoding="utf-8") as f:
        json.dump(report, f, indent=2)

    print("=" * 70)
    for key, value in report.items():
        print(f"{key:>22}: {value}")
    print("=" * 70)
    for item in failures:
        print(f"[FAIL] id={item['id']} :: {item['reason']} :: {item['stored']}")

    sys.exit(1 if failures else 0)


if __name__ == "__main__":
    main()
