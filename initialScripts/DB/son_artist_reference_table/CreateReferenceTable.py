import mysql.connector

CONST_QUERY = 'insert ignore into songArtistReference (songId,artistId) values '

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()

with open('SongArtistReferenceEntry.sql', 'a', encoding='utf-8') as f:
    f.write(CONST_QUERY + '\n')
    cursor.execute('select id , artist from song;')
    songs = cursor.fetchall()

    for song in songs:
        songId = song[0]
        songArtistList = song[1].split(',')

        cursor.execute('select id , name from artist;')
        artistList = cursor.fetchall()

        for songArtist in songArtistList:
            for artist in artistList:
                artistId = artist[0]
                artistName = artist[1]

                if songArtist == artistName:
                    query = f'({songId},{artistId}),\n'
                    f.write(query)
                    print(query)
