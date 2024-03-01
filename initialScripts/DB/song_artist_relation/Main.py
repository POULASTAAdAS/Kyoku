import mysql.connector

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()

cursor.execute("select id , name from artist")
resultArtist = cursor.fetchall()

try:
    cursor.execute("select id , artist from song")
    result = cursor.fetchall()

    with open("songArtist.sql", 'a') as f:
        f.write(f'Insert into SongArtistRelation (songId , artistId) values \n')

        for songId, names in result:
            names = names.split(',')

            for artistId, name in resultArtist:
                if name in names:
                    print(f'({songId} , {artistId}),')
                    f.write(f'({songId} , {artistId}),\n')


except Exception as e:
    print(str(e))
