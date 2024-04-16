import mysql.connector

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()

CONST_QUERY = 'Insert ignore into SongArtistRelation values '

with open('SongArtistRelation.sql', 'r') as entrys:
    for entry in entrys:
        entry = entry.strip().removesuffix(',') + ';'
        try:
            cursor.execute(CONST_QUERY + entry)
            db.commit()
        except mysql.connector.Error as e:
            print(str(e))
            with open('err.txt', 'a') as es:
                es.write(CONST_QUERY + entry + '\n')
