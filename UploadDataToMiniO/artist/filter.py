import re

with open("artistinfo.sql", "r", encoding="utf-8") as f:
    content = f.read()

pattern = re.compile(
    r"\(\s*(\d+)\s*,\s*[\"']([^\"']*(?:\([^)]*\)[^\"']*)*)[\"']\s*,\s*[\"']([^\"']+)[\"']\s*\)",
    re.DOTALL
)

entries = []
for match in pattern.finditer(content):
    number = int(match.group(1))
    name = match.group(2).strip()
    image_path = match.group(3).strip()
    entries.append((number, name, image_path))

# Sort by number to maintain order
entries.sort(key=lambda x: x[0])

with open("parsed.txt", "w", encoding="utf-8") as f:
    for e in entries:
        line = f"({e[0]}, \"{e[1]}\", \"{e[2]}\"),\n"
        print(line)
