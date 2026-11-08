#!/usr/bin/env python3
"""
FixMasterPlaylistPaths.py

One-time migration for /Volumes/DATA/master: rewrites the variant-playlist
references inside each song's master .m3u8 from server-root-relative form

    master/<song_dir>/<bitrate>/playlist_<song>.m3u8

to playlist-relative form (required for HLS players + signed-URL serving):

    <bitrate>/playlist_<song>.m3u8

Only top-level master playlists are touched. Media playlists and segments are
never modified. The script is idempotent: already-fixed files are skipped.

Default mode is DRY-RUN (nothing is written). Pass --apply to actually rewrite.

Every failure is recorded to output/master_playlist_migration_failures.jsonl
(one JSON object per line) and a summary is written to
output/master_playlist_migration_report.json. Re-running overwrites both.

Usage:
    python3 FixMasterPlaylistPaths.py                 # dry-run, full scan
    python3 FixMasterPlaylistPaths.py --limit 50      # dry-run, first 50 dirs
    python3 FixMasterPlaylistPaths.py --apply         # real run
    python3 FixMasterPlaylistPaths.py --apply --root /Volumes/DATA/master
"""

import argparse
import json
import os
import sys
import time

DEFAULT_ROOT = "/Volumes/DATA/master"
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "output")
FAILURE_LOG = os.path.join(OUTPUT_DIR, "master_playlist_migration_failures.jsonl")
REPORT_FILE = os.path.join(OUTPUT_DIR, "master_playlist_migration_report.json")

PROGRESS_EVERY = 200  # directories


class Stats:
    def __init__(self):
        self.dirs_scanned = 0
        self.dirs_without_master = 0
        self.playlists_seen = 0
        self.playlists_fixed = 0
        self.playlists_already_ok = 0
        self.failures = 0


def record_failure(fh, stats, song_dir, file, reason, detail=None):
    stats.failures += 1
    entry = {
        "song_dir": song_dir,
        "file": file,
        "reason": reason,
        "detail": detail,
        "ts": time.strftime("%Y-%m-%dT%H:%M:%S"),
    }
    fh.write(json.dumps(entry, ensure_ascii=False) + "\n")
    fh.flush()
    print(f"\n[FAIL] {song_dir} :: {reason}" + (f" :: {detail}" if detail else ""))


def rewrite_lines(lines, dir_name):
    """Return (new_lines, changed, error_reason, error_detail).

    A URI line is any non-empty line not starting with '#'. Expected forms:
      master/<dir_name>/<rest>  -> rewritten to <rest>
      <rest> (already relative) -> kept
      master/<other_dir>/...    -> error (references a different song dir)
    """
    expected_prefix = f"master/{dir_name}/"
    new_lines = []
    changed = False

    for line in lines:
        stripped = line.strip()
        if not stripped or stripped.startswith("#"):
            new_lines.append(line)
            continue

        if stripped.startswith(expected_prefix):
            new_lines.append(stripped[len(expected_prefix):])
            changed = True
        elif stripped.startswith("master/"):
            return None, False, "prefix_mismatch", stripped
        elif stripped.startswith("/") or "://" in stripped:
            return None, False, "absolute_uri", stripped
        elif ".." in stripped:
            return None, False, "path_traversal_in_uri", stripped
        else:
            new_lines.append(stripped)  # already playlist-relative

    return new_lines, changed, None, None


def verify_targets(song_path, lines):
    """Every URI line must point at an existing file below the song dir."""
    for line in lines:
        stripped = line.strip()
        if not stripped or stripped.startswith("#"):
            continue
        target = os.path.join(song_path, *stripped.split("/"))
        if not os.path.isfile(target):
            return stripped
    return None


def atomic_write(path, lines):
    tmp = path + ".migtmp"
    with open(tmp, "w", encoding="utf-8", newline="\n") as f:
        f.write("\n".join(lines) + "\n")
    os.replace(tmp, path)


def process_song_dir(root, dir_name, apply_changes, stats, fail_fh):
    song_path = os.path.join(root, dir_name)

    try:
        entries = os.listdir(song_path)
    except OSError as e:
        record_failure(fail_fh, stats, dir_name, None, "unreadable_directory", str(e))
        return

    masters = [e for e in entries if e.lower().endswith(".m3u8")]
    if not masters:
        stats.dirs_without_master += 1
        record_failure(fail_fh, stats, dir_name, None, "no_master_playlist", None)
        return

    for master in masters:
        stats.playlists_seen += 1
        master_path = os.path.join(song_path, master)

        try:
            with open(master_path, "r", encoding="utf-8") as f:
                content = f.read()
        except (OSError, UnicodeDecodeError) as e:
            record_failure(fail_fh, stats, dir_name, master, "read_error", str(e))
            continue

        lines = content.splitlines()
        if not lines or lines[0].strip() != "#EXTM3U":
            record_failure(fail_fh, stats, dir_name, master, "not_a_playlist", None)
            continue

        new_lines, changed, err, detail = rewrite_lines(lines, dir_name)
        if err:
            record_failure(fail_fh, stats, dir_name, master, err, detail)
            continue

        missing = verify_targets(song_path, new_lines)
        if missing:
            record_failure(
                fail_fh, stats, dir_name, master, "referenced_file_missing", missing
            )
            continue

        if not changed:
            stats.playlists_already_ok += 1
            continue

        if apply_changes:
            try:
                atomic_write(master_path, new_lines)
            except OSError as e:
                record_failure(fail_fh, stats, dir_name, master, "write_error", str(e))
                continue

        stats.playlists_fixed += 1


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", default=DEFAULT_ROOT, help="master data root")
    parser.add_argument(
        "--apply", action="store_true", help="actually rewrite files (default: dry-run)"
    )
    parser.add_argument(
        "--limit", type=int, default=0, help="process only the first N song dirs"
    )
    args = parser.parse_args()

    if not os.path.isdir(args.root):
        print(f"Root does not exist or is not a directory: {args.root}")
        sys.exit(2)

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    mode = "APPLY" if args.apply else "DRY-RUN"
    print(f"Master playlist path migration [{mode}] root={args.root}")
    print(f"Failures  -> {FAILURE_LOG}")
    print(f"Report    -> {REPORT_FILE}")
    print("=" * 70)

    stats = Stats()
    start = time.time()

    with open(FAILURE_LOG, "w", encoding="utf-8") as fail_fh:
        # scandir streams entries; the volume is slow, do not pre-list everything
        with os.scandir(args.root) as it:
            for entry in it:
                if not entry.is_dir(follow_symlinks=False):
                    continue
                stats.dirs_scanned += 1

                process_song_dir(args.root, entry.name, args.apply, stats, fail_fh)

                if stats.dirs_scanned % PROGRESS_EVERY == 0:
                    elapsed = time.time() - start
                    rate = stats.dirs_scanned / elapsed if elapsed else 0
                    print(
                        f"\r{stats.dirs_scanned:,} dirs | fixed {stats.playlists_fixed:,} "
                        f"| ok {stats.playlists_already_ok:,} | fail {stats.failures:,} "
                        f"| {rate:.0f} dirs/s",
                        end="",
                        flush=True,
                    )

                if args.limit and stats.dirs_scanned >= args.limit:
                    print(f"\nStopping at --limit {args.limit}")
                    break

    duration = time.time() - start
    report = {
        "mode": mode,
        "root": args.root,
        "dirs_scanned": stats.dirs_scanned,
        "dirs_without_master": stats.dirs_without_master,
        "playlists_seen": stats.playlists_seen,
        "playlists_fixed": stats.playlists_fixed,
        "playlists_already_ok": stats.playlists_already_ok,
        "failures": stats.failures,
        "duration_seconds": round(duration, 1),
        "finished_at": time.strftime("%Y-%m-%dT%H:%M:%S"),
    }
    with open(REPORT_FILE, "w", encoding="utf-8") as f:
        json.dump(report, f, indent=2)

    print("\n" + "=" * 70)
    for k, v in report.items():
        print(f"{k:>22}: {v}")
    print("=" * 70)
    if stats.failures:
        print(f"!! {stats.failures} failure(s) recorded in {FAILURE_LOG}")
        print("!! Review every failure before giving the green light for impl.")
        sys.exit(1)
    print("No failures." + ("" if args.apply else " Re-run with --apply to rewrite."))


if __name__ == "__main__":
    main()
