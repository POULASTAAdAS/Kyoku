import csv
import re


def load_csv_as_dict(file_path):
    data = {}
    with open(file_path, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            key = row.get("id") or None
            value = row.get("url") or None
            data[key] = value
    return data


# Example usage
file_path = "output.csv"
result = load_csv_as_dict(file_path)

with open("new_raw.txt", "w", encoding='utf-8') as w:
    with open('raw.txt' , 'r',encoding='utf-8') as r:
        lines = r.readlines()
        for line in lines:
            _id = line.split(',')[0].removeprefix('(').strip()
            
            try:
                url = result[_id]
            except KeyError:
                url = 'null'
            
            line = re.sub(r'"rawSongPoster/[^"]*"' , f'"{url}"' , line)
            print(line)
            w.write(line)
