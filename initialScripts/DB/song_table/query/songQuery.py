import mysql.connector

CONST_QUERY = 'insert into song values '

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="music"
)

cursor = db.cursor()

with open('songQuery.sql', 'a', encoding='utf-8') as f:
    f.write(CONST_QUERY + '\n')
    cursor.execute('select * from song;')

    result = cursor.fetchall()
    for entry in result:
        entry = str(entry) + ','
        f.write(entry + '\n')
