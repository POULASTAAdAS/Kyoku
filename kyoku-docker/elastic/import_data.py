"""
Elasticsearch Artist Data Import Script
Imports artist data from MySQL to Elasticsearch using bulk operations.
"""
import os
import sys
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
INDEX_NAME = 'artists'
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


def fetch_artists_from_mysql(conn):
    """Fetch all artists with their country codes from MySQL."""
    query = """
        SELECT 
            a.id,
            a.name,
            a.cover_image,
            a.followers,
            GROUP_CONCAT(DISTINCT c.code) as country_codes,
            GROUP_CONCAT(DISTINCT c.country) as country_names
        FROM Artist a
        LEFT JOIN ArtistCountry ac ON a.id = ac.artist_id
        LEFT JOIN Country c ON ac.country_id = c.id
        GROUP BY a.id, a.name, a.cover_image, a.followers
        ORDER BY a.id
    """
    
    cursor = conn.cursor(dictionary=True, buffered=False)
    cursor.execute(query)
    
    return cursor


def transform_artist(row):
    """Transform a MySQL row into an Elasticsearch document."""
    # Pick first country code (in case of multiple countries)
    country_code = None
    if row['country_codes']:
        codes = [code.strip() for code in row['country_codes'].split(',') if code.strip()]
        if codes:
            country_code = codes[0]  # Take first country only
    
    # Pick first country name (in case of multiple countries)
    country_name = None
    if row['country_names']:
        names = [name.strip() for name in row['country_names'].split(',') if name.strip()]
        if names:
            country_name = names[0]  # Take first country only
    
    return {
        "_index": INDEX_NAME,
        "_id": str(row['id']),
        "_source": {
            "id": row['id'],
            "name": row['name'],
            "coverImage": row['cover_image'] if row['cover_image'] else None,
            "followers": int(row['followers']) if row['followers'] else 0,
            "countryCode": country_code,  # Single value, not array
            "countryName": country_name   # Single value, not array
        }
    }


def import_artists(es, conn):
    """Import artists using bulk operations."""
    print("\n" + "=" * 60)
    print("Starting Artist Import")
    print("=" * 60)

    # Fetch from MySQL
    print("Fetching artists from MySQL...")
    cursor = fetch_artists_from_mysql(conn)
    
    # Track progress
    total_imported = 0
    errors = []
    
    def generate_docs():
        nonlocal total_imported
        for row in cursor:
            doc = transform_artist(row)
            total_imported += 1
            if total_imported % 1000 == 0:
                print(f"  Processed {total_imported} artists...")
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
            for i, (ok, info) in enumerate(failed[:5]):
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
    print("Elasticsearch Artist Data Import")
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
        success, failed = import_artists(es, conn)
        
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
