import json
import os
import subprocess

from mutagen.mp3 import MP3

from DB.createMasterPlaylist import create_master_playlist
from DB.insertValues import insert_into_db

CONST_APP_NAME = '"Kyoku"'

songDir = "E:/22-24/"

songs = os.listdir(songDir)
rootDir = "F:/songs/"

_128 = rootDir + "128/"
_320 = rootDir + "320/"
cover_image_storage_dir = rootDir + "coverPhoto/"
master = rootDir + "master/"

_id = 1
CONST_DEFAULT_COVER_IMAGE_PATH = "F:/songs/default_cover_image.jpg"

CONST_SONG_MAP = {
    'id': str(_id),
    'coverImage': CONST_DEFAULT_COVER_IMAGE_PATH,
    'masterPlaylistPath': CONST_APP_NAME,
    'totalTime': CONST_APP_NAME,
    'title': CONST_APP_NAME,
    'artist': CONST_APP_NAME,
    'album': CONST_APP_NAME,
    'genre': CONST_APP_NAME,
    'composer': CONST_APP_NAME,
    'publisher': CONST_APP_NAME,
    'album_artist': CONST_APP_NAME,
    'description': CONST_APP_NAME,
    'track': CONST_APP_NAME,
    'year': CONST_APP_NAME
}


def extract_total_time_and_year(song_path):
    audio = MP3(song_path)

    time = audio.info.length * 1000
    year = audio.get("TDRC")

    return {
        "totalTime": str(time),
        "year": f'"{year}"'
    }


def extract_cover_image(song_path):
    path = f'{cover_image_storage_dir}{os.path.basename(song_path).replace(".mp3", ".jpeg")}'
    try:
        cover_image_cmd = f'ffmpeg -i {song_path} -an -c:v copy "{path}" -y'
        status = subprocess.run(cover_image_cmd, capture_output=True)
        if status.returncode == 0:
            return path
        else:
            return CONST_DEFAULT_COVER_IMAGE_PATH
    except Exception:
        return CONST_DEFAULT_COVER_IMAGE_PATH


def create_single_query(song_path):
    tags_map = CONST_SONG_MAP

    # get coverImage
    tags_map["coverImage"] = f'"{extract_cover_image(song_path)}"'.strip()

    audio_year = extract_total_time_and_year(song_path)

    # get time and year
    tags_map["year"] = audio_year["year"]
    tags_map["totalTime"] = audio_year["totalTime"]

    # create masterPlaylist
    result = create_master_playlist(song_path)

    if "True" in result:
        tags_map["id"] = str(_id)
        tags_map["masterPlaylistPath"] = f'"{result.replace("True_", "")}"'

        command = [
            'ffprobe',
            '-v', 'quiet',
            '-print_format', 'json',
            '-show_format',
            '-show_streams',
            song_path
        ]

        try:
            result = subprocess.run(command, capture_output=True, text=True)

            if result.returncode == 0:
                metadata = json.loads(result.stdout)
                tags = metadata.get('format', {}).get('tags', {})

                for key, value in tags.items():
                    if key == 'title':
                        tags_map['title'] = f'"{value}"'

                    elif key == 'artist':
                        tags_map['artist'] = f'"{value}"'

                    elif key == 'album':
                        tags_map['album'] = f'"{value}"'

                    elif key == 'genre':
                        tags_map['genre'] = f'"{value}"'

                    elif key == 'composer':
                        tags_map['composer'] = f'"{value}"'

                    elif key == 'publisher':
                        tags_map['publisher'] = f'"{value}"'

                    elif key == 'album_artist':
                        tags_map['album_artist'] = f'"{value}"'

                    elif key == 'description':
                        tags_map['description'] = f'"{value}"'

                    elif key == 'track':
                        tags_map['track'] = f'"track {str(value)}"'

            return f'({", ".join(tags_map.values())})'
        except Exception:
            return ""


def make_dir():
    if not os.path.exists(_128):
        os.mkdir(path=_128)

    if not os.path.exists(_320):
        os.mkdir(path=_320)

    if not os.path.exists(cover_image_storage_dir):
        os.mkdir(path=cover_image_storage_dir)

    if not os.path.exists(master):
        os.mkdir(path=master)


if __name__ == '__main__':
    make_dir()

    # get query_list
    for song in songs:
        song = os.path.join(songDir, song)
        single_query = create_single_query(song)
        _id += 1

        # insert into db
        insert_into_db(single_query)
