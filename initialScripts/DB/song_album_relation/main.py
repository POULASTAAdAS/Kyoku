import mysql.connector

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()


def getArtistIdList(_artists):
    cursor.execute(f'select id from artist where name = "{_artists}"')
    _id = cursor.fetchone()
    if _id:
        return int(_id[0])


cursor.execute("select id , name from album")
albums = cursor.fetchall()

cursor.execute("SELECT DISTINCT id ,album, album_artist FROM song where album != 'empty' order by album")
songs = cursor.fetchall()

try:
    with open('SongAlbumArtistRelation.sql', 'a') as f:
        f.write('Insert into SongAlbumArtistRelation (songId , artistId , albumId ) values \n')
        for songId, album, artists in songs:

            for albumId, albumName in albums:
                if album == albumName:

                    artistList = artists.split(',')
                    for artist in artistList:
                        artistId = getArtistIdList(artist)
                        if artistId:
                            print(f'({albumId},{songId},{artistId}),')
                            f.write(f'({songId},{artistId},{albumId}),\n')

except Exception as e:
    print(str(e))
