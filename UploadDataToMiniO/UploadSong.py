import os
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from minio import Minio
from minio.error import S3Error
from pathlib import Path
import mimetypes
from dotenv import load_dotenv
import time
import gc
import signal
import traceback

# Load environment variables from .env file
load_dotenv()

# MinIO configuration from environment variables
MINIO_ENDPOINT = "localhost:1082"
MINIO_ACCESS_KEY = os.getenv("MINIO_SONG_ROOT_USER")
MINIO_SECRET_KEY = os.getenv("MINIO_SONG_ROOT_PASSWORD")
BUCKET_NAME = "song"
UPLOAD_FOLDER = "/"

# Local directory containing files
LOCAL_DIR = r"G:\master"

# Configuration optimized for HLS music structure
MAX_WORKERS = 40  # Further reduced for stability
BATCH_SIZE = 200  # Smaller batches
RETRY_ATTEMPTS = 2
DIRECTORY_BATCH_SIZE = 80  # Smaller directory batches
UPLOAD_TIMEOUT = 180  # Seconds per file

# Expected file extensions for music streaming
MUSIC_EXTENSIONS = {'.m3u8', '.m4a', '.mp3', '.aac', '.ts'}

# Thread-safe counters
upload_stats = {
    'successful': 0,
    'failed': 0,
    'processed': 0,
    'scanned_files': 0,
    'scanned_dirs': 0,
    'skipped_files': 0,
    'current_directory': '',
    'last_activity': time.time(),
    'lock': threading.Lock()
}

# Global flag for graceful shutdown
shutdown_flag = threading.Event()


def signal_handler(signum, frame):
    """Handle Ctrl+C gracefully"""
    print(f"\n\n⚠️ Shutdown signal received. Finishing current operations...")
    shutdown_flag.set()


signal.signal(signal.SIGINT, signal_handler)


def create_minio_client():
    """Create and return MinIO client"""
    try:
        client = Minio(
            MINIO_ENDPOINT,
            access_key=MINIO_ACCESS_KEY,
            secret_key=MINIO_SECRET_KEY,
            secure=False
        )
        # Test connection with timeout
        client.bucket_exists(BUCKET_NAME)
        return client
    except Exception as e:
        print(f"❌ Error creating MinIO client: {e}")
        return None


def ensure_bucket_exists(client, bucket_name):
    """Create bucket if it doesn't exist"""
    try:
        if not client.bucket_exists(bucket_name):
            client.make_bucket(bucket_name)
            print(f"✅ Created bucket: {bucket_name}")
        else:
            print(f"✅ Bucket {bucket_name} already exists")
        return True
    except Exception as e:
        print(f"❌ Error with bucket operations: {e}")
        return False


def get_content_type(file_path):
    """Get MIME type for the file"""
    content_type, _ = mimetypes.guess_type(file_path)
    return content_type or 'application/octet-stream'


def is_music_file(file_path):
    """Check if file is a music/streaming file we want to upload"""
    ext = Path(file_path).suffix.lower()
    return ext in MUSIC_EXTENSIONS


def upload_single_file(file_info, client=None):
    """Upload a single file with enhanced error handling"""
    local_file_path, object_name = file_info

    try:
        if client is None:
            client = create_minio_client()
            if not client:
                return False, f"MinIO client creation failed"

        if shutdown_flag.is_set():
            return False, "Shutdown requested"

        # Skip non-music files
        if not is_music_file(local_file_path):
            return False, f"Skipped non-music file"

        # File existence and access checks
        if not os.path.exists(local_file_path):
            return False, f"File not found on disk"

        if not os.access(local_file_path, os.R_OK):
            return False, f"File permission denied"

        # Get file info
        try:
            file_size = os.path.getsize(local_file_path)
            if file_size == 0:
                return False, f"File is empty (0 bytes)"
        except Exception as e:
            return False, f"Cannot get file size: {e}"

        # Check if object already exists (optional - remove if you want to overwrite)
        # try:
        #     client.stat_object(BUCKET_NAME, object_name)
        #     return False, f"Object already exists in bucket"
        # except:
        #     pass  # Object doesn't exist, continue with upload

        # Upload with detailed error handling
        for attempt in range(RETRY_ATTEMPTS):
            try:
                content_type = get_content_type(local_file_path)

                start_time = time.time()
                client.fput_object(
                    bucket_name=BUCKET_NAME,
                    object_name=object_name,
                    file_path=local_file_path,
                    content_type=content_type
                )

                upload_time = time.time() - start_time
                return True, f"OK ({file_size:,} bytes in {upload_time:.2f}s)"

            except S3Error as s3_e:
                error_msg = f"S3 Error (attempt {attempt + 1}): {s3_e.code} - {s3_e.message}"
                if attempt == RETRY_ATTEMPTS - 1:
                    return False, error_msg
                time.sleep(1)

            except Exception as e:
                error_msg = f"Upload error (attempt {attempt + 1}): {str(e)[:150]}"
                if attempt == RETRY_ATTEMPTS - 1:
                    return False, error_msg
                time.sleep(0.5)

        return False, "Failed after all retry attempts"

    except Exception as e:
        return False, f"Unexpected error: {str(e)[:150]}"


def update_stats(success=False, scanned_file=False, scanned_dir=False, skipped=False, current_dir=None):
    """Thread-safe statistics update"""
    with upload_stats['lock']:
        upload_stats['last_activity'] = time.time()

        if scanned_file:
            upload_stats['scanned_files'] += 1
        if scanned_dir:
            upload_stats['scanned_dirs'] += 1
        if skipped:
            upload_stats['skipped_files'] += 1
        if current_dir:
            upload_stats['current_directory'] = current_dir
        if not any([scanned_file, scanned_dir, skipped]):  # Upload operation
            upload_stats['processed'] += 1
            if success:
                upload_stats['successful'] += 1
            else:
                upload_stats['failed'] += 1


def print_scan_progress():
    """Print current scan progress"""
    with upload_stats['lock']:
        scanned_files = upload_stats['scanned_files']
        scanned_dirs = upload_stats['scanned_dirs']
        current_dir = upload_stats['current_directory']
        last_activity = upload_stats['last_activity']

    # Show time since last activity
    inactive_time = time.time() - last_activity
    activity_indicator = "⏸️" if inactive_time > 10 else "🔄"

    display_dir = current_dir[-30:] if len(current_dir) > 30 else current_dir
    print(f"\r{activity_indicator} Scan: {scanned_dirs:,} albums | {scanned_files:,} files | {display_dir}", end='',
          flush=True)


def print_upload_progress():
    """Print current upload progress"""
    with upload_stats['lock']:
        processed = upload_stats['processed']
        successful = upload_stats['successful']
        failed = upload_stats['failed']
        scanned_files = upload_stats['scanned_files']
        skipped = upload_stats['skipped_files']

    success_rate = f"{(successful / processed * 100):.1f}%" if processed > 0 else "0%"
    print(
        f"\r🎵 Upload: {processed:,}/{scanned_files:,} | ✅ {successful:,} ({success_rate}) | ❌ {failed:,} | ⏭️ {skipped:,}",
        end='', flush=True)


def check_directory_health(directory_path):
    """Quick health check of a directory"""
    try:
        items = os.listdir(directory_path)
        return True, len(items)
    except PermissionError:
        return False, "Permission denied"
    except OSError as e:
        return False, f"OS Error: {e}"
    except Exception as e:
        return False, f"Error: {e}"


def scan_music_directory(directory):
    """Enhanced music directory scanner with health checks"""
    print(f"🎵 Starting enhanced music scan in: {directory}")

    start_time = time.time()
    last_progress_time = start_time
    last_health_check = start_time

    try:
        # Get all top-level directories
        print("🔍 Getting directory list...")
        all_items = os.listdir(directory)
        top_level_items = [item for item in all_items if os.path.isdir(os.path.join(directory, item))]
        print(f"📁 Found {len(top_level_items):,} album directories")

        # Process in smaller batches with health checks
        processed_dirs = 0

        for batch_start in range(0, len(top_level_items), DIRECTORY_BATCH_SIZE):
            if shutdown_flag.is_set():
                print(f"\n🛑 Shutdown requested")
                break

            batch_end = min(batch_start + DIRECTORY_BATCH_SIZE, len(top_level_items))
            current_batch = top_level_items[batch_start:batch_end]

            # Health check every 100 directories
            current_time = time.time()
            if current_time - last_health_check > 30:  # Every 30 seconds
                print(f"\n💓 Health check - processed {processed_dirs}/{len(top_level_items)} directories")
                last_health_check = current_time

            print(
                f"\n📦 Directory batch {batch_start // DIRECTORY_BATCH_SIZE + 1}: albums {batch_start + 1}-{batch_end}")

            for album_name in current_batch:
                if shutdown_flag.is_set():
                    break

                try:
                    album_path = os.path.join(directory, album_name)
                    update_stats(current_dir=album_name)
                    processed_dirs += 1

                    # Quick health check
                    healthy, info = check_directory_health(album_path)
                    if not healthy:
                        print(f"\n⚠️ Skipping unhealthy directory {album_name}: {info}")
                        continue

                    update_stats(scanned_dir=True)

                    # Scan album directory
                    album_items = os.listdir(album_path)

                    for item in album_items:
                        if shutdown_flag.is_set():
                            break

                        item_path = os.path.join(album_path, item)

                        try:
                            if os.path.isfile(item_path):
                                # Master playlist file
                                if is_music_file(item_path):
                                    rel_path = os.path.relpath(item_path, directory)
                                    object_name = UPLOAD_FOLDER + rel_path.replace('\\', '/')
                                    update_stats(scanned_file=True)
                                    yield (item_path, object_name)

                            elif os.path.isdir(item_path):
                                # Bitrate directory
                                update_stats(scanned_dir=True)

                                try:
                                    bitrate_files = os.listdir(item_path)
                                    for bitrate_file in bitrate_files:
                                        if shutdown_flag.is_set():
                                            break

                                        file_path = os.path.join(item_path, bitrate_file)
                                        if os.path.isfile(file_path) and is_music_file(file_path):
                                            rel_path = os.path.relpath(file_path, directory)
                                            object_name = UPLOAD_FOLDER + rel_path.replace('\\', '/')
                                            update_stats(scanned_file=True)
                                            yield (file_path, object_name)

                                except Exception as e:
                                    print(f"\n⚠️ Error in bitrate folder {item}: {e}")
                                    continue

                        except Exception as e:
                            print(f"\n⚠️ Error processing item {item}: {e}")
                            continue

                    # Update progress
                    current_time = time.time()
                    if current_time - last_progress_time > 3:  # Every 3 seconds
                        print_scan_progress()
                        last_progress_time = current_time

                except Exception as e:
                    print(f"\n❌ Error scanning album {album_name}: {e}")
                    traceback.print_exc()
                    continue

            # Small pause between batches
            if not shutdown_flag.is_set():
                time.sleep(0.1)  # Brief pause to prevent overwhelming the system

    except Exception as e:
        print(f"\n❌ Critical scanning error: {e}")
        traceback.print_exc()
        return

    with upload_stats['lock']:
        total_files = upload_stats['scanned_files']
        total_dirs = upload_stats['scanned_dirs']

    scan_duration = time.time() - start_time
    print(f"\n✅ Scan complete: {total_files:,} files in {total_dirs:,} directories ({scan_duration:.1f}s)")


def upload_batch(file_batch):
    """Upload batch with enhanced error handling"""
    if shutdown_flag.is_set():
        return

    try:
        client = create_minio_client()
        if not client:
            print(f"\n❌ Failed to create MinIO client for batch")
            return

        # Reduce workers for stability
        actual_workers = min(MAX_WORKERS, len(file_batch), 3)

        with ThreadPoolExecutor(max_workers=actual_workers) as executor:
            future_to_file = {
                executor.submit(upload_single_file, file_info, client): file_info
                for file_info in file_batch
            }

            completed = 0
            for future in as_completed(future_to_file):
                if shutdown_flag.is_set():
                    break

                file_info = future_to_file[future]
                completed += 1

                try:
                    success, message = future.result(timeout=UPLOAD_TIMEOUT)

                    if "Skipped non-music file" in message:
                        update_stats(skipped=True)
                    else:
                        update_stats(success)

                    # Show errors with more detail
                    if not success and "Skipped" not in message:
                        file_name = Path(file_info[1]).name
                        local_path = file_info[0]
                        print(f"\n❌ FAIL: {file_name}")
                        print(f"   📂 Path: {local_path}")
                        print(f"   🔍 Reason: {message}")

                        # Additional diagnostics
                        try:
                            if os.path.exists(local_path):
                                file_size = os.path.getsize(local_path)
                                print(f"   📊 Size: {file_size:,} bytes")
                            else:
                                print(f"   ⚠️ File doesn't exist!")
                        except Exception as diag_e:
                            print(f"   🔍 Diagnostic error: {diag_e}")

                    # Update progress
                    if completed % 10 == 0 or completed == len(file_batch):
                        print_upload_progress()

                except Exception as e:
                    update_stats(False)
                    print(f"\n❌ Exception: {e}")

    except Exception as e:
        print(f"\n❌ Batch upload error: {e}")
        traceback.print_exc()


def upload_files():
    """Main upload function with comprehensive monitoring"""
    start_time = time.time()
    last_heartbeat = start_time

    print(f"🔧 System check...")

    # Validate environment
    if not MINIO_ACCESS_KEY or not MINIO_SECRET_KEY:
        print("❌ Missing MinIO credentials in .env file")
        return

    # Test connection
    print("🔌 Testing MinIO connection...")
    test_client = create_minio_client()
    if not test_client:
        print("❌ Cannot connect to MinIO")
        return

    if not ensure_bucket_exists(test_client, BUCKET_NAME):
        print("❌ Cannot access bucket")
        return

    # Validate directory
    print("📂 Checking source directory...")
    if not os.path.exists(LOCAL_DIR):
        print(f"❌ Directory {LOCAL_DIR} does not exist!")
        return

    if not os.access(LOCAL_DIR, os.R_OK):
        print(f"❌ Directory {LOCAL_DIR} is not readable!")
        return

    print("✅ All checks passed, starting upload process...")
    print("=" * 70)

    batch_count = 0
    current_batch = []
    heartbeat_counter = 0

    try:
        for file_info in scan_music_directory(LOCAL_DIR):
            if shutdown_flag.is_set():
                print(f"\n🛑 Shutdown requested")
                break

            current_batch.append(file_info)
            heartbeat_counter += 1

            # Heartbeat every 100 files
            if heartbeat_counter % 100 == 0:
                current_time = time.time()
                print(
                    f"\n💓 Heartbeat: Found {heartbeat_counter:,} files, {current_time - last_heartbeat:.1f}s since last")
                last_heartbeat = current_time

            # Process batch
            if len(current_batch) >= BATCH_SIZE:
                batch_count += 1

                with upload_stats['lock']:
                    total_found = upload_stats['scanned_files']
                    processed = upload_stats['processed']

                print(
                    f"\n\n🎵 Batch {batch_count} ({len(current_batch)} files) | Found: {total_found:,} | Processed: {processed:,}")

                upload_batch(current_batch)

                if shutdown_flag.is_set():
                    break

                print(f"✅ Batch {batch_count} done")
                current_batch.clear()
                gc.collect()

        # Final batch
        if current_batch and not shutdown_flag.is_set():
            batch_count += 1
            print(f"\n🎵 Final batch {batch_count} ({len(current_batch)} files)")
            upload_batch(current_batch)

    except Exception as e:
        print(f"\n💥 Critical error: {e}")
        traceback.print_exc()

    # Statistics
    end_time = time.time()
    duration = end_time - start_time

    with upload_stats['lock']:
        stats = dict(upload_stats)

    print(f"\n\n" + "=" * 70)
    print(f"📊 FINAL RESULTS:")
    print(f"🔍 Files scanned: {stats['scanned_files']:,}")
    print(f"📤 Files processed: {stats['processed']:,}")
    print(f"✅ Successful: {stats['successful']:,}")
    print(f"❌ Failed: {stats['failed']:,}")
    print(f"⏭️ Skipped: {stats['skipped_files']:,}")
    success_rate = (stats['successful'] / stats['processed'] * 100) if stats['processed'] > 0 else 0
    print(f"📈 Success rate: {success_rate:.1f}%")
    print(f"📦 Batches processed: {batch_count}")
    print(f"⏱️ Total time: {duration / 60:.1f} minutes")
    print(f"🚀 Speed: {stats['processed'] / duration:.1f} files/second" if duration > 0 else "N/A")
    print("=" * 70)


if __name__ == "__main__":
    print("🎵 Enhanced MinIO Music Uploader v2")
    print("=" * 50)

    upload_files()
