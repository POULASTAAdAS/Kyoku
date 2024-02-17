import os

import mysql.connector

profileFolderPath = 'F:/songs/artist/'

CONST_QUERY = 'insert ignore into artist (name,profilePicUrl,country,genre) values '

profileFolder = os.listdir(profileFolderPath)

_id = 1

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()

while _id <= 2653:

    for folder in profileFolder:
        name = folder.strip()
        url = os.path.join(profileFolderPath, folder) + '/'

        cursor.execute(f'select name from artist where id = {_id};')

        result = cursor.fetchone()

        resultName = (str(result).replace(',)', '').
                      replace('(', '').
                      replace("'", '').
                      strip())

        if resultName == name:
            cursor.execute(f"update artist set profilePicUrl = '{url}' where id = {_id};")
            db.commit()
            print(url)
            break

    _id += 1
