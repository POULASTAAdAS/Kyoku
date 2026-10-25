"""
Elasticsearch Song Data Import Script
Imports song data from MySQL to Elasticsearch using bulk operations.
"""
import os
import sys
import json
import time
from elasticsearch import Elasticsearch, helpers
from elasticsearch.exceptions import ConnectionError
import mysql.connector
from mysql.connector import Error

# Configuration
ES_HOST = os.getenv('ES_HOST', 'elasticsearch')
ES_PORT = int(os.getenv('ES_PORT', '9200'))
ES_USER = os.getenv('ES_USER', 'elastic')
ES_PASSWORD = os.getenv('ES_PASSWORD', 'changeme')
MYSQL_HOST = os.getenv('MYSQL_HOST', 'content-proxysql')
MYSQL_PORT = int(os.getenv('MYSQL_PORT', '6033'))
MYSQL_USER = os.getenv('MYSQL_USER', 'root')
MYSQL_PASSWORD = os.getenv('MYSQL_PASSWORD', 'changeme')
INDEX_NAME = 'songs'
BULK_CHUNK_SIZE = 500  # Documents per bulk request


def wait_for_elasticsearch(max_retries=30, delay=5):
    """Wait for Elasticsearch to be available."""
    print(f"Connecting to Elasticsearch at {ES_HOST}:{ES_PORT}...")

    for attempt in range(max_retries):
        try:
            es = Elasticsearch(
                [f"http://{ES_HOST}:{ES_PORT}"],
                basic_auth=(ES_USER, ES_PASSWORD),
                verify_certs=False,
                request_timeout=10
            )
            if es.ping():
                print("Elasticsearch connected!")
                return es
        except Exception as e:
            print(f"Attempt {attempt + 1}/{max_retries}: {str(e)}")

        time.sleep(delay)

    raise Exception("Failed to connect to Elasticsearch")


def wait_for_mysql(max_retries=30, delay=5):
    """Wait for MySQL to be available."""
    print(f"Connecting to MySQL at {MYSQL_HOST}:{MYSQL_PORT}...")

    for attempt in range(max_retries):
        try:
            conn = mysql.connector.connect(
                host=MYSQL_HOST,
                port=MYSQL_PORT,
                user=MYSQL_USER,
                password=MYSQL_PASSWORD,
                database="CONTENT",
                connection_timeout=5
            )
            if conn.is_connected():
                print("MySQL connected!")
                return conn
        except Error as e:
            print(f"Attempt {attempt + 1}/{max_retries}: MySQL not ready ({str(e)})")

        time.sleep(delay)

    raise Exception("Failed to connect to MySQL")


def fetch_songs_from_mysql(conn):
    """Fetch all songs with their info, country, artists, album and genres from MySQL.

    Related rows come back as JSON arrays of {id, name}, so names that contain commas stay intact.
    Each sub query is correlated on the song id, so one song never multiplies another relation's rows.
    """
    query = """
        SELECT
            s.id,
            s.title,
            s.poster,
            si.release_year,
            si.popularity,
            (
                SELECT JSON_ARRAYAGG(JSON_OBJECT('code', c.code, 'name', c.country))
                FROM SongCountry sc
                JOIN Country c ON c.id = sc.country_id
                WHERE sc.song_id = s.id
            ) AS countries,
            (
                SELECT JSON_ARRAYAGG(JSON_OBJECT('id', a.id, 'name', a.name))
                FROM SongArtist sa
                JOIN Artist a ON a.id = sa.artist_id
                WHERE sa.song_id = s.id
            ) AS artists,
            (
                SELECT JSON_ARRAYAGG(JSON_OBJECT('id', al.id, 'name', al.name))
                FROM SongAlbum sal
                JOIN Album al ON al.id = sal.album_id
                WHERE sal.song_id = s.id
            ) AS albums,
            (
                SELECT JSON_ARRAYAGG(JSON_OBJECT('id', g.id, 'name', g.name))
                FROM SongGenre sg
                JOIN Genre g ON g.id = sg.genre_id
                WHERE sg.song_id = s.id
            ) AS genres
        FROM Song s
        LEFT JOIN SongInfo si ON si.song_id = s.id
        ORDER BY s.id
    """

    cursor = conn.cursor(dictionary=True, buffered=False)
    cursor.execute(query)

    return cursor


def parse_json_list(value):
    """MySQL JSON_ARRAYAGG result -> list of dicts (None / empty when the song has no relation)."""
    if not value:
        return []
    if isinstance(value, (bytes, bytearray)):
        value = value.decode('utf-8')
    return json.loads(value) if isinstance(value, str) else value


def references(value):
    """JSON list -> [{id, name}] sorted by id, so documents are stable between imports."""
    items = [{"id": int(item['id']), "name": item['name']} for item in parse_json_list(value)]
    return sorted(items, key=lambda item: item['id'])


def transform_song(row):
    """Transform a MySQL row into an Elasticsearch document."""
    # Pick first country (in case of multiple countries); code and name come from the same row
    countries = sorted(parse_json_list(row['countries']), key=lambda item: item['code'] or '')
    country = countries[0] if countries else None

    # A song belongs to at most one album
    albums = references(row['albums'])

    return {
        "_index": INDEX_NAME,
        "_id": str(row['id']),
        "_source": {
            "id": row['id'],
            "title": row['title'],
            "poster": row['poster'] if row['poster'] else None,
            "popularity": int(row['popularity']) if row['popularity'] else 0,
            "releaseYear": row['release_year'],
            "countryCode": country['code'] if country else None,  # Single value, not array
            "countryName": country['name'] if country else None,  # Single value, not array
            "artists": references(row['artists']),
            "album": albums[0] if albums else None,
            "genres": references(row['genres'])
        }
    }


def import_songs(es, conn):
    """Import songs using bulk operations."""
    print("\n" + "=" * 60)
    print("Starting Song Import")
    print("=" * 60)

    # Fetch from MySQL
    print("Fetching songs from MySQL...")
    cursor = fetch_songs_from_mysql(conn)

    # Track progress
    total_imported = 0

    def generate_docs():
        nonlocal total_imported
        for row in cursor:
            doc = transform_song(row)
            total_imported += 1
            if total_imported % 1000 == 0:
                print(f"  Processed {total_imported} songs...")
            yield doc

    # Bulk import
    print(f"Importing with bulk operations (chunk size: {BULK_CHUNK_SIZE})...")
    start_time = time.time()

    try:
        success, failed = helpers.bulk(
            es,
            generate_docs(),
            chunk_size=BULK_CHUNK_SIZE,
            raise_on_error=False,
            raise_on_exception=False
        )

        elapsed = time.time() - start_time

        print(f"\nImport completed in {elapsed:.2f} seconds!")
        print(f"  - Successfully imported: {success} documents")

        if failed:
            print(f"  - Failed: {len(failed)} documents")
            for i, info in enumerate(failed[:5]):
                print(f"    Error {i+1}: {info}")

        # Refresh index
        print("Refreshing index...")
        es.indices.refresh(index=INDEX_NAME)

        return success, len(failed)

    except Exception as e:
        print(f"\nImport failed: {e}")
        raise
    finally:
        cursor.close()


def main():
    """Main entry point."""
    print("=" * 60)
    print("Elasticsearch Song Data Import")
    print("=" * 60)
    print(f"Source: MySQL ({MYSQL_HOST}:{MYSQL_PORT})")
    print(f"Destination: Elasticsearch ({ES_HOST}:{ES_PORT})")
    print(f"Index: {INDEX_NAME}")
    print("=" * 60)

    es = None
    conn = None

    try:
        # Connect to services
        es = wait_for_elasticsearch()
        conn = wait_for_mysql()

        # Import data
        success, failed = import_songs(es, conn)

        if success > 0 and failed == 0:
            print("\n" + "=" * 60)
            print("IMPORT COMPLETED SUCCESSFULLY!")
            print("=" * 60)
            sys.exit(0)
        elif success > 0:
            print(f"\nImport completed with {failed} errors.")
            sys.exit(0)  # Partial success is still acceptable
        else:
            print("\nImport failed - no documents imported.")
            sys.exit(1)

    except Exception as e:
        print(f"\nFATAL ERROR: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)

    finally:
        if conn and conn.is_connected():
            conn.close()
            print("MySQL connection closed.")


if __name__ == "__main__":
    main()
