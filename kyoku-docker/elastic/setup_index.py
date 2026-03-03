"""
Elasticsearch Artist Index Setup Script
Creates the artists index with proper mappings before data import.
"""
import os
import sys
import time
from elasticsearch import Elasticsearch
from elasticsearch.exceptions import ConnectionError, NotFoundError

# Configuration from environment
ES_HOST = os.getenv('ES_HOST', 'elasticsearch')
ES_PORT = int(os.getenv('ES_PORT', '9200'))
ES_USER = os.getenv('ES_USER', 'elastic')
ES_PASSWORD = os.getenv('ES_PASSWORD', 'changeme')
INDEX_NAME = 'artists'
EXPECTED_MIN_COUNT = 37000  # Minimum expected artist count


def wait_for_elasticsearch(max_retries=30, delay=5):
    """Wait for Elasticsearch to be available."""
    es_url = f"http://{ES_USER}:{ES_PASSWORD}@{ES_HOST}:{ES_PORT}"
    print(f"Waiting for Elasticsearch at {ES_HOST}:{ES_PORT}...")
    
    for attempt in range(max_retries):
        try:
            es = Elasticsearch(
                [f"http://{ES_HOST}:{ES_PORT}"],
                basic_auth=(ES_USER, ES_PASSWORD),
                verify_certs=False,
                request_timeout=10
            )
            if es.ping():
                print("Elasticsearch is available!")
                return es
        except Exception as e:
            print(f"Attempt {attempt + 1}/{max_retries}: Elasticsearch not ready ({str(e)})")
        
        time.sleep(delay)
    
    raise Exception("Elasticsearch failed to become available")


def check_existing_data(es):
    """Check if index exists and has sufficient data."""
    try:
        if not es.indices.exists(index=INDEX_NAME):
            print(f"Index '{INDEX_NAME}' does not exist.")
            return False, 0
        
        count_response = es.count(index=INDEX_NAME)
        doc_count = count_response['count']
        print(f"Index '{INDEX_NAME}' exists with {doc_count} documents.")
        
        if doc_count >= EXPECTED_MIN_COUNT:
            print(f"Data already imported (>= {EXPECTED_MIN_COUNT} docs). Skipping setup.")
            return True, doc_count
        else:
            print(f"Index exists but has insufficient documents ({doc_count}). Recreating...")
            return False, doc_count
            
    except Exception as e:
        print(f"Error checking index: {e}")
        return False, 0


def delete_index(es):
    """Delete existing index if present."""
    try:
        if es.indices.exists(index=INDEX_NAME):
            print(f"Deleting existing index '{INDEX_NAME}'...")
            es.indices.delete(index=INDEX_NAME)
            print("Index deleted successfully.")
            time.sleep(2)
    except Exception as e:
        print(f"Error deleting index: {e}")
        raise


def create_index_with_mapping(es):
    """Create the artists index with proper mappings."""
    print(f"Creating index '{INDEX_NAME}' with mappings...")
    
    index_body = {
        "settings": {
            "number_of_shards": 2,
            "number_of_replicas": 2,
            "refresh_interval": "-1",
            "analysis": {
                "analyzer": {
                    "artist_name_analyzer": {
                        "type": "custom",
                        "tokenizer": "standard",
                        "filter": ["lowercase", "artist_edge_ngram"]
                    }
                },
                "filter": {
                    "artist_edge_ngram": {
                        "type": "edge_ngram",
                        "min_gram": 2,
                        "max_gram": 20
                    }
                }
            }
        },
        "mappings": {
            "properties": {
                "id": {
                    "type": "keyword",
                    "store": True
                },
                "name": {
                    "type": "text",
                    "analyzer": "artist_name_analyzer",
                    "search_analyzer": "standard",
                    "fields": {
                        "keyword": {
                            "type": "keyword"
                        }
                    }
                },
                "coverImage": {
                    "type": "keyword",
                    "index": False,
                    "doc_values": False
                },
                "followers": {
                    "type": "long",
                    "doc_values": True
                },
                "countryCode": {
                    "type": "keyword"
                },
                "countryName": {
                    "type": "text",
                    "analyzer": "standard",
                    "fields": {
                        "keyword": {
                            "type": "keyword"
                        }
                    }
                }
            }
        }
    }
    
    try:
        es.indices.create(index=INDEX_NAME, body=index_body)
        print(f"Index '{INDEX_NAME}' created successfully!")
        return True
    except Exception as e:
        print(f"Error creating index: {e}")
        raise


def verify_index(es):
    """Verify index was created properly."""
    try:
        mapping = es.indices.get_mapping(index=INDEX_NAME)
        settings = es.indices.get_settings(index=INDEX_NAME)
        
        print("\nIndex Verification:")
        print(f"  - Index exists: {es.indices.exists(index=INDEX_NAME)}")
        print(f"  - Mappings configured: {INDEX_NAME in mapping}")
        print(f"  - Settings applied: {INDEX_NAME in settings}")
        
        return True
    except Exception as e:
        print(f"Verification failed: {e}")
        return False


def main():
    """Main entry point."""
    print("=" * 60)
    print("Elasticsearch Artist Index Setup")
    print("=" * 60)
    
    try:
        # Wait for ES
        es = wait_for_elasticsearch()
        
        # Check existing data
        has_data, count = check_existing_data(es)
        if has_data:
            print("\nSetup complete! Data already exists.")
            sys.exit(0)
        
        # Delete if partial
        if count > 0:
            delete_index(es)
        
        # Create index
        create_index_with_mapping(es)
        
        # Verify
        if verify_index(es):
            print("\n" + "=" * 60)
            print("Setup completed successfully!")
            print("=" * 60)
            sys.exit(0)
        else:
            sys.exit(1)
            
    except Exception as e:
        print(f"\nSetup failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
