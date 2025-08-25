import os
from minio import Minio
from minio.error import S3Error
from pathlib import Path
import mimetypes
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

# MinIO configuration from environment variables
MINIO_ENDPOINT = "localhost:1080"
MINIO_ACCESS_KEY = os.getenv("MINIO_FILE_ROOT_USER")
MINIO_SECRET_KEY = os.getenv("MINIO_FILE_ROOT_PASSWORD")
BUCKET_NAME = "song"
UPLOAD_FOLDER = "/"

# Local directory containing images
LOCAL_IMAGE_DIR = r"G:\rawSongPoster"

# Supported image extensions
IMAGE_EXTENSIONS = {'.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp', '.tiff', '.tif'}


def create_minio_client():
    """Create and return MinIO client"""
    try:
        client = Minio(
            MINIO_ENDPOINT,
            access_key=MINIO_ACCESS_KEY,
            secret_key=MINIO_SECRET_KEY,
            secure=False  # Set to True if using HTTPS
        )
        return client
    except Exception as e:
        print(f"Error creating MinIO client: {e}")
        return None


def ensure_bucket_exists(client, bucket_name):
    """Create bucket if it doesn't exist"""
    try:
        if not client.bucket_exists(bucket_name):
            client.make_bucket(bucket_name)
            print(f"Created bucket: {bucket_name}")
        else:
            print(f"Bucket {bucket_name} already exists")
        return True
    except S3Error as e:
        print(f"Error with bucket operations: {e}")
        return False


def get_content_type(file_path):
    """Get MIME type for the file"""
    content_type, _ = mimetypes.guess_type(file_path)
    return content_type or 'application/octet-stream'


def upload_images():
    """Main function to upload all images"""
    # Validate required environment variables
    if not MINIO_ACCESS_KEY or not MINIO_SECRET_KEY:
        print("❌ Error: MINIO_FILE_ROOT_USER and MINIO_FILE_ROOT_PASSWORD must be set in .env file")
        return

    # Create MinIO client
    client = create_minio_client()
    if not client:
        return

    # Ensure bucket exists
    if not ensure_bucket_exists(client, BUCKET_NAME):
        return

    # Check if local directory exists
    if not os.path.exists(LOCAL_IMAGE_DIR):
        print(f"Directory {LOCAL_IMAGE_DIR} does not exist!")
        return

    # Get all image files
    image_files = []
    for root, dirs, files in os.walk(LOCAL_IMAGE_DIR):
        for file in files:
            if Path(file).suffix.lower() in IMAGE_EXTENSIONS:
                image_files.append(os.path.join(root, file))

    if not image_files:
        print(f"No image files found in {LOCAL_IMAGE_DIR}")
        return

    print(f"Found {len(image_files)} image files to upload")

    # Upload each image
    successful_uploads = 0
    failed_uploads = 0

    for local_file_path in image_files:
        try:
            # Get relative path from the base directory
            rel_path = os.path.relpath(local_file_path, LOCAL_IMAGE_DIR)
            # Create object name with song/ prefix
            object_name = UPLOAD_FOLDER + rel_path.replace('\\', '/')

            # Get content type
            content_type = get_content_type(local_file_path)

            # Upload file
            client.fput_object(
                bucket_name=BUCKET_NAME,
                object_name=object_name,
                file_path=local_file_path,
                content_type=content_type
            )

            print(f"✓ Uploaded: {rel_path} -> {object_name}")
            successful_uploads += 1

        except Exception as e:
            print(f"✗ Failed to upload {rel_path}: {e}")
            failed_uploads += 1

    # Summary
    print("\n" + "=" * 50)
    print(f"Upload Summary:")
    print(f"Successful uploads: {successful_uploads}")
    print(f"Failed uploads: {failed_uploads}")
    print(f"Total files processed: {len(image_files)}")
    print("=" * 50)


if __name__ == "__main__":
    print("MinIO Image Uploader")
    print("=" * 30)

    # Check if .env file exists
    if not os.path.exists('.env'):
        print("⚠️  Warning: .env file not found in current directory")

    print(f"Using MinIO endpoint: {MINIO_ENDPOINT}")
    print(f"Using bucket: {BUCKET_NAME}")
    print(f"Upload folder: {UPLOAD_FOLDER}")

    # Start upload process
    upload_images()
