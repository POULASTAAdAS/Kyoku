import os
import subprocess

storageDir = "F:/music/"

_128 = storageDir + "128/"
_320 = storageDir + "320/"
master = storageDir + "master/"


def make_dirs(_128_folder_name, _320_folder_name, master_folder_name):
    if not os.path.exists(_128_folder_name):
        os.mkdir(_128_folder_name)

    if not os.path.exists(_320_folder_name):
        os.mkdir(_320_folder_name)

    if not os.path.exists(master_folder_name):
        os.mkdir(master_folder_name)


def create_master_playlist(song_path):
    only_name = os.path.basename(song_path).removesuffix(".mp3")

    _128_folder_name = f'{_128}{only_name}'
    _320_folder_name = f'{_320}{only_name}'
    master_folder_name = f'{master}{only_name}'
    print(_128_folder_name)

    try:
        make_dirs(_128_folder_name, _320_folder_name, master_folder_name)

        cmd = f'''ffmpeg -y -i {song_path} -c:a aac -b:a 128k -muxdelay 0 -f segment -sc_threshold 0 -segment_time 10 -segment_list "{_128_folder_name}/playlist{only_name}.m3u8" -segment_format mpegts "{_128_folder_name}/segment%d{only_name}.m4a" -c:a aac -b:a 320k -muxdelay 0 -f segment -sc_threshold 0 -segment_time 10 -segment_list "{_320_folder_name}/playlist{only_name}.m3u8" -segment_format mpegts "{_320_folder_name}/segment%d{only_name}.m4a"'''

        result = subprocess.run(cmd, check=True)

        master_playlist = f'{master_folder_name}/{only_name}_master.m3u8'

        if result.returncode == 0:
            playlist128 = f"{_128_folder_name}/playlist{only_name}.m3u8\n"
            playlist320 = f"{_320_folder_name}/playlist{only_name}.m3u8\n"

            with open(master_playlist, 'a') as m:
                m.write("#EXTM3U\n")
                m.write("#EXT-X-VERSION:3\n")
                m.write("#EXT-X-STREAM-INF:BANDWIDTH=128000\n")
                m.write(playlist128)
                m.write("#EXT-X-STREAM-INF:BANDWIDTH=320000\n")
                m.write(playlist320)
            m.close()

        return f"True_{master_playlist}"
    except Exception as e:
        print(str(e))
        return "False"
