folder = "D:/musicStreaming/Music-Streaming-App/database/"

oldFile = f"{folder}TempQuery.txt"
newFile = f'{folder}ArtistEntry.sql'

genre = f'{folder}genre.txt'

CONST_ARTIST_QUERY = "insert into artist (name,profilePicUrl,country,genre) values "

CONST_GENRE_QUERY = "insert ignore into genre (genre) values "

genreList = {}

with open(genre, 'r', encoding='utf-8') as rd:
    for line in rd:
        key = line.strip().split(',')[0]
        value = line.strip().split(',')[1].replace('"', '')
        genreList[key] = value

with open(newFile, 'a', encoding='utf-8') as wf:
    wf.write(CONST_ARTIST_QUERY + "\n")
    with open(oldFile, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()

            try:
                if not line.startswith("INSERT"):
                    if "Unknown" not in line:
                        query = line

                        line = line.removesuffix(',')
                        genre = line.split(',')[-1].replace("'", '').removesuffix(')')
                        genre = genre.removesuffix(');')

                        for key, value in genreList.items():
                            if value in line:
                                line = line.replace(value, key)

                                if line.endswith(";"):
                                    line = line.removesuffix(';')
                                    line = line + ','
                                    wf.write(line + '\n')
                                    print(line)
                                else:
                                    wf.write(line + ',' + '\n')
                                    print(line + ',')
            except:
                continue
