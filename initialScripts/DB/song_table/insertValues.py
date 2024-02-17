import mysql.connector

CONST_QUERY = "insert into song values "


def insert_into_db(query):
    query = f'{CONST_QUERY}{query};'
    print(query)

    with open('query/insertQuery.sql', 'a') as wr:
        wr.write(f'{query}\n')
    wr.close()

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
        print((str(e)))

        with open('log/failed.txt', 'a') as f:
            f.write(f'{query}\n')
        f.close()
