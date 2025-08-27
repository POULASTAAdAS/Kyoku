import re

with open("gen.txt", "r", encoding="utf-8") as f:
    content = f.read()

# Match: ( 'name', 'desc', NULL/'image', number )
pattern = re.compile(
    r"\(\s*'([^']+)'\s*,\s*'((?:[^']|'')*)'\s*,\s*(NULL|'[^']*')\s*,\s*(\d+)\s*\)",
    re.DOTALL
)

entries = []
for match in pattern.finditer(content):
    name = match.group(1).strip()
    description = match.group(2).replace("''", "'").strip()
    image = None if match.group(3) == "NULL" else match.group(3).strip("'")
    number = int(match.group(4))
    entries.append((name, description, image, number))

with open("parsed.txt", "w", encoding="utf-8") as f:
    for e in entries:
        print(e)
        e = f"({e[0]}, {e[1]}, {e[2]}, {e[3]}),\n"
        f.write(e)
