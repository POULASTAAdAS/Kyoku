import mysql.connector

folder = "D:/musicStreaming/Music-Streaming-App/database/query.txt"

CONST_QUERY = "insert into artist (name,profilePicUrl,country,preferedSpeakingLang) value "

with open(folder, 'r', encoding='utf=8') as f:
    for line in f:
        line = line.strip()

        if not line.startswith("INSERT"):
            if line.endswith(","):
                line = line.removesuffix(',')
                line = line + ';'
                query = CONST_QUERY + line

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

        except Exception as e:
            print(str(e))
