import mysql.connector

artistFile = 'D:/Kyoku/Music-Streaming-App/database/artist.csv'

query = 'select id , artist from song;'

CONST_QUERY = 'insert into songArtistRelation (songId , artistId) values \n'

try:
    db = mysql.connector.connect(
        host="localhost",
        user="root",
        password="password",
        database="music"
    )

    cursor = db.cursor()

    with open("SongArtistRelation.sql", 'a') as wr:
        wr.write(CONST_QUERY)
        with open(artistFile, 'r') as a:
            for one in a:
                list = one.strip().replace('"', '').split(',')
                for artist in list:
                    artist = artist.strip()

                    cursor.execute(f'select id from artist where name = "{artist}";')
                    artistId = cursor.fetchone()

                    if artistId is not None:
                        artistId = str(artistId[0])

                        cursor.execute(f'select id from song where artist like "%{artist}%";')
                        songIdList = cursor.fetchall()

                        for songId in songIdList:
                            songId = str(songId).replace("(", '').replace(")", '').replace(",", '').strip()

                            wr.write(f'({songId} , {artistId}),\n')
                            print(f'({songId} , {artistId}),')


except Exception as e:
    print((str(e)))
