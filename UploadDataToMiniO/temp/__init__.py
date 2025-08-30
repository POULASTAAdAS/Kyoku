with open('106_insert_content_db_relation_song_country.sql', 'w') as w:
    w.write('''USE CONTENT;\n''')
    w.write('''INSERT
    IGNORE INTO SongCountry (song_id, country_id) VALUES    ''')
    w.write('''\n''')
    with open('results.csv', 'r') as f:
        lines = f.readlines()
        for line in lines:
            line = f'({line.strip()}),\n'
            w.write(line)
            print(line)
