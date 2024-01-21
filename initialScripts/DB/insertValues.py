import mysql.connector

CONST_QUERY = "insert into song values "


def insert_into_db(query):
    query = f'{CONST_QUERY}{query};'
    print(query)

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
