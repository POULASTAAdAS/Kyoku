import os
from minio import Minio
from urllib.parse import quote
from dotenv import load_dotenv
import re

load_dotenv()
MINIO_ENDPOINT = "localhost:1080"
MINIO_ACCESS_KEY = os.getenv("MINIO_FILE_ROOT_USER")
MINIO_SECRET_KEY = os.getenv("MINIO_FILE_ROOT_PASSWORD")

client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_ACCESS_KEY,
    secret_key=MINIO_SECRET_KEY,
    secure=False
)


def get_artist_folder_url(artist_name, bucket_name="artist"):
    try:
        # Verify folder exists
        prefix = f"{artist_name}/"
        objects = client.list_objects(bucket_name, prefix=prefix)

        folder_exists = any(True for _ in objects)

        if folder_exists:
            # Create URL-safe version
            encoded_artist = quote(artist_name)
            folder_url = f"/{bucket_name}/{encoded_artist}.jpg"
            return folder_url
        else:
            print(f"Artist folder '{artist_name}' not found")
            return None

    except Exception as e:
        print(f"Error: {e}")
        return None


def extract_artist_name_from_line(line):
    """Extract the artist name from the SQL line"""
    # Pattern to match: (id, "artist_name", "path")
    match = re.search(r'\(\s*\d+\s*,\s*"([^"]+)"\s*,', line)
    if match:
        return match.group(1)
    return None


def find_best_matching_name(artist_name, available_names):
    """Find the best matching name from available names"""
    # First try exact match
    if artist_name in available_names:
        return artist_name

    # Create a list of potential matches with their scores
    matches = []

    for name in available_names:
        # Skip very short names unless they're exact matches
        if len(name) <= 2 and name != artist_name:
            continue

        # Check if name is contained in artist_name
        if name in artist_name:
            # Prefer matches that are closer to word boundaries
            # and longer matches
            score = len(name) * 100

            # Bonus if it matches at word boundaries
            import re
            if re.search(r'\b' + re.escape(name) + r'\b', artist_name, re.IGNORECASE):
                score += 50

            matches.append((name, score))

    # Return the best match (highest score)
    if matches:
        return max(matches, key=lambda x: x[1])[0]

    return None


if __name__ == "__main__":
    # Load available names
    with open('name.txt', 'r', encoding='utf-8') as f:
        names = [name.strip() for name in f.readlines()]

    # Remove very short names that might cause false matches (optional)
    # Uncomment the next line if you want to filter out single character names
    # names = [name for name in names if len(name) > 1]

    with open('problem.txt', 'w', encoding='utf-8') as p:
        with open('artist.sql', 'w', encoding='utf-8') as w:
            w.write('USE CONTENT;\n')
            w.write('INSERT IGNORE INTO ARTIST (id,`name`,cover_image) VALUES\n')

            with open('artist.txt', 'r', encoding='utf-8') as a:
                lines = a.readlines()

                for line in lines:
                    # Extract artist name from the SQL line
                    artist_name = extract_artist_name_from_line(line)

                    if artist_name:
                        # Find best matching name
                        matched_name = find_best_matching_name(artist_name, names)

                        if matched_name:
                            url = get_artist_folder_url(matched_name)

                            # Handle case when artist folder is not found
                            if url is None:
                                url = "null"

                            # Replace the URL in the line
                            line = re.sub('/rawArtistCover/[^"]*', url, line)

                            print(f"Matched '{artist_name}' -> '{matched_name}' -> {url}")
                            w.write(line)
                        else:
                            print(f"No match found for '{artist_name}' - setting to null")
                            # Set URL to null for unmatched artists
                            line = re.sub('/rawArtistCover/[^"]*', 'null', line)
                            w.write(line)
                    else:
                        print(f"Could not extract artist name from line: {line.strip()}")
                        p.write(line)
