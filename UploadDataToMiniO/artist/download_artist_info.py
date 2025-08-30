import requests
from bs4 import BeautifulSoup
import re
from datetime import datetime
import time
import json
import os

# Configuration
artist_name_file = "entry.txt"  # Input file with artist names
sql_output_file = "artistinfo.sql"  # SQL output file

# Headers for requests
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}


def create_sql_file():
    """Create the SQL file and write the table creation statement"""
    with open(sql_output_file, 'w', encoding='utf-8') as f:
        f.write("-- Artist Information Database\n")
        f.write(f"-- Generated on: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n\n")

        f.write("-- Create table\n")
        f.write("""CREATE TABLE IF NOT EXISTS ArtistInfo
                   (
                       name
                       TEXT
                       UNIQUE
                       NOT
                       NULL,
                       biography
                       TEXT
                       DEFAULT
                       NULL,
                       birth_date
                       DATE
                       DEFAULT
                       NULL,
                       monthly_listeners
                       BIGINT
                       UNSIGNED
                       DEFAULT
                       0
                   );

                """)

        f.write("-- Artist data\n")

    print(f"SQL file '{sql_output_file}' initialized successfully.")


def escape_sql_string(text):
    """Escape strings for SQL insertion"""
    if text is None:
        return "NULL"

    # Replace single quotes with two single quotes for SQL escaping
    escaped = text.replace("'", "''")
    return f"'{escaped}'"


def search_wikipedia(artist_name):
    """Search Wikipedia for artist information"""
    try:
        # Search Wikipedia
        search_url = f"https://en.wikipedia.org/w/api.php"
        search_params = {
            'action': 'query',
            'format': 'json',
            'list': 'search',
            'srsearch': f'{artist_name} musician singer artist',
            'srlimit': 1
        }

        response = requests.get(search_url, params=search_params, headers=headers)

        if response.status_code == 200:
            data = response.json()
            if data['query']['search']:
                page_title = data['query']['search'][0]['title']

                # Get page content
                content_params = {
                    'action': 'query',
                    'format': 'json',
                    'titles': page_title,
                    'prop': 'extracts|pageprops',
                    'exintro': True,
                    'explaintext': True,
                    'exsectionformat': 'plain'
                }

                content_response = requests.get(search_url, params=content_params, headers=headers)
                if content_response.status_code == 200:
                    content_data = content_response.json()
                    pages = content_data['query']['pages']
                    for page_id in pages:
                        if 'extract' in pages[page_id]:
                            return pages[page_id]['extract'][:1000]  # Limit to 1000 chars

    except Exception as e:
        print(f"Error searching Wikipedia for {artist_name}: {e}")

    return None


def extract_birth_date_from_text(text):
    """Extract birth date from biography text"""
    if not text:
        return None

    # Common date patterns
    patterns = [
        r'born\s+(\d{1,2}\s+\w+\s+\d{4})',  # born 15 January 1990
        r'born\s+(\w+\s+\d{1,2},\s+\d{4})',  # born January 15, 1990
        r'\(born\s+(\d{4})\)',  # (born 1990)
        r'Born:\s+(\d{1,2}\s+\w+\s+\d{4})',  # Born: 15 January 1990
        r'\((\d{1,2}\s+\w+\s+\d{4})\s*[-–]',  # (15 January 1990 -
        r'\((\w+\s+\d{1,2},\s+\d{4})\s*[-–]',  # (January 15, 1990 -
    ]

    for pattern in patterns:
        match = re.search(pattern, text, re.IGNORECASE)
        if match:
            date_str = match.group(1)

            # Try to parse the date
            date_formats = [
                '%d %B %Y',  # 15 January 1990
                '%B %d, %Y',  # January 15, 1990
                '%d %b %Y',  # 15 Jan 1990
                '%b %d, %Y',  # Jan 15, 1990
                '%Y'  # 1990
            ]

            for fmt in date_formats:
                try:
                    date_obj = datetime.strptime(date_str, fmt)
                    return date_obj.strftime('%Y-%m-%d')
                except:
                    continue

    return None


def search_spotify_info(artist_name):
    """Search for Spotify monthly listeners (simulated - would need API in production)"""
    try:
        # Note: This is a simplified example. In production, you'd use Spotify API
        # For now, we'll search for the information on the web
        search_url = f'https://www.bing.com/search?q={artist_name}+spotify+monthly+listeners'

        response = requests.get(search_url, headers=headers)

        if response.status_code == 200:
            soup = BeautifulSoup(response.text, 'html.parser')
            text = soup.get_text()

            # Look for patterns like "X monthly listeners" or "X million monthly listeners"
            patterns = [
                r'(\d+(?:,\d+)*(?:\.\d+)?)\s*million\s*monthly\s*listeners',
                r'(\d+(?:,\d+)*)\s*monthly\s*listeners'
            ]

            for pattern in patterns:
                match = re.search(pattern, text, re.IGNORECASE)
                if match:
                    listeners_str = match.group(1).replace(',', '')

                    if 'million' in match.group(0).lower():
                        listeners = int(float(listeners_str) * 1000000)
                    else:
                        listeners = int(listeners_str)

                    return listeners

    except Exception as e:
        print(f"Error searching Spotify info for {artist_name}: {e}")

    return 0


def get_artist_info(artist_name):
    """Get all information for an artist"""
    print(f"\nFetching information for: {artist_name}")

    info = {
        'name': artist_name,
        'biography': None,
        'birth_date': None,
        'monthly_listeners': 0
    }

    # Get biography from Wikipedia
    biography = search_wikipedia(artist_name)
    if biography:
        info['biography'] = biography
        print(f"  ✓ Found biography ({len(biography)} chars)")

        # Try to extract birth date from biography
        birth_date = extract_birth_date_from_text(biography)
        if birth_date:
            info['birth_date'] = birth_date
            print(f"  ✓ Found birth date: {birth_date}")
        else:
            print(f"  ✗ Birth date not found")
    else:
        print(f"  ✗ Biography not found")

    # Get monthly listeners
    listeners = search_spotify_info(artist_name)
    if listeners > 0:
        info['monthly_listeners'] = listeners
        print(f"  ✓ Found monthly listeners: {listeners:,}")
    else:
        print(f"  ✗ Monthly listeners not found")

    # Add a small delay to avoid rate limiting
    time.sleep(1)

    return info


def write_to_sql_file(artist_info):
    """Write artist information to SQL file"""
    try:
        with open(sql_output_file, 'a', encoding='utf-8') as f:
            # Create INSERT statement
            sql_statement = f"""INSERT OR REPLACE INTO ArtistInfo (name, biography, birth_date, monthly_listeners)
VALUES ({escape_sql_string(artist_info['name'])}, {escape_sql_string(artist_info['biography'])}, {escape_sql_string(artist_info['birth_date'])}, {artist_info['monthly_listeners']});

"""
            f.write(sql_statement)

        print(f"  ✓ Written to SQL file: {artist_info['name']}")

    except Exception as e:
        print(f"  ✗ Error writing to SQL file: {e}")


def display_final_stats(processed_artists):
    """Display statistics about the processed artists"""
    total = len(processed_artists)
    with_bio = sum(1 for artist in processed_artists if artist['biography'])
    with_birth = sum(1 for artist in processed_artists if artist['birth_date'])
    with_listeners = sum(1 for artist in processed_artists if artist['monthly_listeners'] > 0)

    print("\n" + "=" * 50)
    print("PROCESSING STATISTICS")
    print("=" * 50)
    print(f"Total artists processed: {total}")
    print(
        f"Artists with biography: {with_bio} ({with_bio / total * 100:.1f}%)" if total > 0 else "Artists with biography: 0")
    print(
        f"Artists with birth date: {with_birth} ({with_birth / total * 100:.1f}%)" if total > 0 else "Artists with birth date: 0")
    print(
        f"Artists with monthly listeners: {with_listeners} ({with_listeners / total * 100:.1f}%)" if total > 0 else "Artists with monthly listeners: 0")
    print(f"\nSQL file saved as: {sql_output_file}")


def main():
    """Main function to orchestrate the scraping process"""
    print("=" * 50)
    print("ARTIST INFORMATION SCRAPER")
    print("=" * 50)

    # Create SQL file with table creation
    create_sql_file()

    # Read artist names from file
    try:
        with open(artist_name_file, 'r', encoding='utf-8') as f:
            artist_names = [line.strip() for line in f if line.strip()]

        print(f"\nFound {len(artist_names)} artists to process")
        print("-" * 50)

        processed_artists = []

        # Process each artist
        for i, artist_name in enumerate(artist_names, 1):
            print(f"\n[{i}/{len(artist_names)}] Processing: {artist_name}")

            # Get artist information
            artist_info = get_artist_info(artist_name)
            processed_artists.append(artist_info)

            # Write to SQL file
            write_to_sql_file(artist_info)

            # Progress indicator
            if i % 10 == 0:
                print(f"\n>>> Progress: {i}/{len(artist_names)} artists processed")

        # Display final statistics
        display_final_stats(processed_artists)

        print("\n" + "=" * 50)
        print("SCRAPING COMPLETED SUCCESSFULLY!")
        print(f"All data has been written to '{sql_output_file}'")
        print("=" * 50)

    except FileNotFoundError:
        print(f"Error: Artist name file '{artist_name_file}' not found!")
        print("Please create a file with artist names (one per line)")
    except Exception as e:
        print(f"Error: {e}")


if __name__ == "__main__":
    main()
