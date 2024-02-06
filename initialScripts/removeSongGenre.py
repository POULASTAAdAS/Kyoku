import json
import os
import subprocess

count = 0

songsRootDir = "E:/19-21/"

listOfSongs = os.listdir(songsRootDir)

for song in listOfSongs:
    fileFromRootPath = os.path.join(songsRootDir, song)
    try:
        command = [
            'ffprobe',
            '-v', 'quiet',
            '-print_format', 'json',
            '-show_format',
            '-show_streams',
            fileFromRootPath
        ]

        result = subprocess.run(command, capture_output=True, text=True)

        if result.returncode == 0:
            metadata = json.loads(result.stdout)
            tags = metadata.get('format', {}).get('tags', {})

            if "New Punjabi" in tags["album"]:
                os.remove(fileFromRootPath)
                print("New Punjabi:         " + fileFromRootPath)

            if "Punjabi" in tags["album"]:
                os.remove(fileFromRootPath)
                print("New Punjabi:         " + fileFromRootPath)




    except:
        continue
