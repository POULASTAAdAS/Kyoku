# construct genre table

CONST_QUERY = 'Insert into genre (name) values '

genreList = []

genreFilePath = "D:/musicStreaming/Music-Streaming-App/database/genre.csv"

with open(genreFilePath, 'r') as f:
    for line in f:
        line = line.strip()
        name = line.split(',')[1]
        name = name.replace('"', '').strip()

        if name not in genreList:
            genreList.append(name)

with open("TempGenre.sql", 'a') as f:
    f.write(f'{CONST_QUERY}\n')

    for name in genreList:
        f.write(f"('{name}'),\n")
