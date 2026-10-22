import os
import csv
import shutil
from pathlib import Path
from multiprocessing import Pool, cpu_count
from PIL import Image, ImageSequence

# Source directory containing raw images (read only, never modified)
RAW_DATA_DIR = "/Users/poulastaad/personal/data/raw"

# Destination directory for compressed images
COMPRESSED_DATA_DIR = "/Users/poulastaad/personal/data/compressed"

# Raw folder name -> compressed folder name
FOLDER_MAPPING = {
    "rawSongPoster": "songCover",
    "rawArtistCover": "artistCover",
    "rwaGenreCover": "genreCover",
}

# WebP encoder settings
WEBP_QUALITY = 90
WEBP_METHOD = 6  # 0 (fast) - 6 (slowest, smallest output)
WEBP_EXTENSION = ".webp"

# Report of skipped / fallback files, written at the root of COMPRESSED_DATA_DIR
REPORT_FILE = os.path.join(COMPRESSED_DATA_DIR, "compression_report.csv")

# Magic byte signatures of real image formats
IMAGE_SIGNATURES = [
    (b"\xff\xd8\xff", "jpeg"),
    (b"\x89PNG\r\n\x1a\n", "png"),
    (b"GIF87a", "gif"),
    (b"GIF89a", "gif"),
    (b"BM", "bmp"),
    (b"II*\x00", "tiff"),
    (b"MM\x00*", "tiff"),
]

STATUS_CONVERTED = "converted"
STATUS_EXISTS = "already_exists"
STATUS_COPIED = "copied_original"
STATUS_SKIPPED = "skipped_not_image"
STATUS_FAILED = "failed"


def detect_image_type(file_path):
    """Detect the real image type from file content, None if the file is not an image"""
    with open(file_path, "rb") as f:
        header = f.read(512)

    if not header:
        return None

    for signature, image_type in IMAGE_SIGNATURES:
        if header.startswith(signature):
            return image_type

    if header[:4] == b"RIFF" and header[8:12] == b"WEBP":
        return "webp"

    # SVG is a text based image, but must not be confused with html / xml error pages
    text = header.decode("utf-8", errors="ignore").lower()
    if "<svg" in text and "<html" not in text:
        return "svg"

    return None


def convert_to_webp(source_path, target_path):
    """Encode source image as WebP, keeping animation, transparency, ICC profile and EXIF"""
    with Image.open(source_path) as image:
        save_kwargs = {
            "format": "WEBP",
            "quality": WEBP_QUALITY,
            "method": WEBP_METHOD,
        }
        if image.info.get("icc_profile"):
            save_kwargs["icc_profile"] = image.info["icc_profile"]
        if image.info.get("exif"):
            save_kwargs["exif"] = image.info["exif"]

        if getattr(image, "is_animated", False):
            frames = [normalize_mode(frame.copy()) for frame in ImageSequence.Iterator(image)]
            save_kwargs["save_all"] = True
            save_kwargs["append_images"] = frames[1:]
            save_kwargs["duration"] = [frame.info.get("duration", 100) for frame in ImageSequence.Iterator(image)]
            save_kwargs["loop"] = image.info.get("loop", 0)
            frames[0].save(target_path, **save_kwargs)
        else:
            image.load()
            normalize_mode(image).save(target_path, **save_kwargs)


def normalize_mode(image):
    """Convert image to a mode supported by the WebP encoder"""
    if image.mode in ("RGB", "RGBA"):
        return image
    has_alpha = image.mode in ("LA", "PA") or (image.mode == "P" and "transparency" in image.info)
    return image.convert("RGBA" if has_alpha else "RGB")


def process_file(task):
    """Compress a single file, falling back to copying the original if it is a real image"""
    source_path, rel_path, target_dir = task
    target_webp = os.path.join(target_dir, str(Path(rel_path).with_suffix(WEBP_EXTENSION)))
    target_original = os.path.join(target_dir, rel_path)

    try:
        if os.path.exists(target_webp) or os.path.exists(target_original):
            return rel_path, STATUS_EXISTS, ""

        image_type = detect_image_type(source_path)
        if image_type is None:
            return rel_path, STATUS_SKIPPED, "content is not an image"

        os.makedirs(os.path.dirname(target_webp), exist_ok=True)
        temp_path = target_webp + ".part"

        try:
            convert_to_webp(source_path, temp_path)
            os.replace(temp_path, target_webp)
            return rel_path, STATUS_CONVERTED, image_type
        except Exception as e:
            if os.path.exists(temp_path):
                os.remove(temp_path)
            shutil.copy2(source_path, target_original)
            return rel_path, STATUS_COPIED, f"{image_type}: {e}"

    except Exception as e:
        return rel_path, STATUS_FAILED, str(e)


def collect_tasks():
    """Walk raw folders and build (source, relative path, target dir) tasks"""
    tasks = []
    for raw_folder, compressed_folder in FOLDER_MAPPING.items():
        source_dir = os.path.join(RAW_DATA_DIR, raw_folder)
        target_dir = os.path.join(COMPRESSED_DATA_DIR, compressed_folder)

        if not os.path.exists(source_dir):
            print(f"⚠️  Directory {source_dir} does not exist, skipping")
            continue

        for root, dirs, files in os.walk(source_dir):
            for file in files:
                if file == ".DS_Store":
                    continue
                source_path = os.path.join(root, file)
                rel_path = os.path.relpath(source_path, source_dir)
                tasks.append((source_path, rel_path, target_dir))
    return tasks


def compress_images():
    """Main function to compress all images"""
    if os.path.abspath(COMPRESSED_DATA_DIR).startswith(os.path.abspath(RAW_DATA_DIR) + os.sep):
        print("❌ Error: COMPRESSED_DATA_DIR must not be inside RAW_DATA_DIR")
        return

    tasks = collect_tasks()
    if not tasks:
        print(f"No files found in {RAW_DATA_DIR}")
        return

    print(f"Found {len(tasks)} files to process")
    os.makedirs(COMPRESSED_DATA_DIR, exist_ok=True)

    counts = {STATUS_CONVERTED: 0, STATUS_EXISTS: 0, STATUS_COPIED: 0, STATUS_SKIPPED: 0, STATUS_FAILED: 0}
    report_rows = []

    with Pool(cpu_count()) as pool:
        for index, (task, result) in enumerate(zip(tasks, pool.imap(process_file, tasks, chunksize=32)), start=1):
            rel_path, status, detail = result
            counts[status] += 1

            if status in (STATUS_COPIED, STATUS_SKIPPED, STATUS_FAILED):
                report_rows.append([os.path.basename(task[2]), rel_path, status, detail])
                print(f"✗ {status}: {rel_path} ({detail})")

            if index % 1000 == 0:
                print(f"Processed {index}/{len(tasks)}")

    with open(REPORT_FILE, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["folder", "path", "status", "detail"])
        writer.writerows(report_rows)

    # Summary
    print("\n" + "=" * 50)
    print("Compression Summary:")
    print(f"Converted to WebP: {counts[STATUS_CONVERTED]}")
    print(f"Already existed: {counts[STATUS_EXISTS]}")
    print(f"Copied original (conversion failed): {counts[STATUS_COPIED]}")
    print(f"Skipped (not an image): {counts[STATUS_SKIPPED]}")
    print(f"Failed: {counts[STATUS_FAILED]}")
    print(f"Total files processed: {len(tasks)}")
    print(f"Report: {REPORT_FILE}")
    print("=" * 50)


if __name__ == "__main__":
    print("WebP Image Compressor")
    print("=" * 30)
    print(f"Source: {RAW_DATA_DIR}")
    print(f"Destination: {COMPRESSED_DATA_DIR}")
    print(f"WebP quality: {WEBP_QUALITY}")

    compress_images()
