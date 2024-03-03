import mysql.connector

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()

try:
    cursor.execute("select id , genre from song")
    dbSongs = cursor.fetchall()

    cursor.execute("select id, name from genre")
    dbGenres = cursor.fetchall()

    with open('SongGenreRelationInsertion.sql', 'a') as f:
        f.write('Insert into SongGenreRelation values \n')
        for songId, songGenre in dbSongs:
            songGenre = songGenre.strip()
            for genreId, genre in dbGenres:
                genre = genre.strip()

                if songGenre == genre:
                    print(f"({songId},{genreId})")
                    f.write(f"({songId},{genreId}),\n")





except Exception as e:
    print(str(e))
