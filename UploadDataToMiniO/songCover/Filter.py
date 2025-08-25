import re
import os
import csv
from minio import Minio
from urllib.parse import quote
from dotenv import load_dotenv

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


def get_song_folder_url(song_name, bucket_name="song"):
    try:
        object_name = song_name
        try:
            client.stat_object(bucket_name, object_name)
            encoded_song = quote(song_name)
            folder_url = f"/{bucket_name}/{encoded_song}"
            return folder_url
        except:
            return None
    except Exception as e:
        print(f"Error: {e}")
        return None


with open('problem.txt', 'w', encoding='utf-8') as p:
    with open('output.csv', 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['id', 'url'])

        with open('raw.txt', 'r', encoding='utf-8') as f:
            lines = f.readlines()
            for line in lines:
                try:
                    parts = line.split('"')
                    image_path = parts[3]
                    filename = image_path.split('/')[-1]

                    print(filename)

                    _id = line.split(',')[0].removeprefix('(').strip()
                    url = get_song_folder_url(filename)

                    print(f'{_id},{url}')
                    writer.writerow([_id, url])

                except (IndexError, AttributeError) as e:
                    print(f"Error processing line: {line.strip()}")
                    p.write(line)
