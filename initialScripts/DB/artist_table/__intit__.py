# construct artist table

CONST_QUERY = "Insert into artist (name , profilePicUrl,Country,genre) values "

countryList = {
    "India": 1,
    "Pakistan": 2,
    "United Kingdom": 3,
    '"United Kingdom"': 3,
    "Canada": 4,
    '"United States"': 5
}

artistFilePath = "D:/musicStreaming/Music-Streaming-App/database/artistName.csv"

queryFile = 'ArtistInsertQuery.sql'

with open(queryFile, 'a') as w:
    w.write(f'{CONST_QUERY}\n')
    with open(artistFilePath, 'r') as f:
        for line in f:
            line = line.strip()
            lineArr = line.split(',')
            if lineArr[3] in countryList:
                lineArr[3] = countryList[lineArr[3]]
            lineArr[1] = lineArr[1].replace("'", '').replace('"', '')
            lineArr[2] = lineArr[2].replace("'", '').replace('"', '')
            lineArr[4] = lineArr[4].replace("'", '').replace('"', '')
            lineArr[5] = lineArr[5].replace("'", '').replace('"', '')

            query = f"('{lineArr[1]}','{lineArr[2]}',{lineArr[3]},{lineArr[4]}),\n"
            w.write(query)
