import mysql.connector

CONST_QUERY = "insert into genre value "

_id = 1

with open("D:/musicStreaming/Music-Streaming-App/database/genre.txt", 'r', encoding="utf-8") as f:
    for line in f:
        line = line.strip()

        if not line.startswith('"'):
            line = f'"{line}"'

        query = f'{CONST_QUERY}({_id},{line});'

        try:
            db = mysql.connector.connect(
                host="localhost",
                user="root",
                password="password",
                database="music"
            )

            cursor = db.cursor()

            cursor.execute(query)
            db.commit()

            _id += 1

        except Exception as e:
            print(str(e))
