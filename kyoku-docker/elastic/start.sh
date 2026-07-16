#!/bin/bash

echo "================================================================================================"
echo "      Elasticsearch Artist Search Setup"
echo "================================================================================================"

# Navigate to the parent directory of this script
cd "$(dirname "$0")/.."

# Load environment variables from .env file
if [ ! -f ".env" ]; then
    echo "[ERROR] .env file not found in root directory."
    exit 1
fi

echo "[INFO] Loading environment variables..."
set -a
source .env
set +a

docker_compose() {
    if docker compose version >/dev/null 2>&1; then
        docker compose "$@"
    elif command -v docker-compose >/dev/null 2>&1; then
        docker-compose "$@"
    else
        echo "[ERROR] Docker Compose is not available."
        echo "[INFO] Install Docker Compose v2 or docker-compose v1."
        exit 1
    fi
}

echo "----------------------------------------"
echo "  Step 1: Waiting for Elasticsearch"
echo "----------------------------------------"
echo "[INFO] Waiting for Elasticsearch to be healthy..."
while true; do
    if docker_compose ps elasticsearch | grep -q "healthy"; then
        echo "[SUCCESS] Elasticsearch is healthy!"
        break
    fi
    echo "  ... still waiting for Elasticsearch ..."
    sleep 5
done
echo ""

echo "----------------------------------------"
echo "  Step 2: Setting up Kibana System User"
echo "----------------------------------------"
echo "[INFO] Setting kibana_system user password..."
curl -s -u "elastic:${ELASTIC_ROOT_PASSWORD}" \
    -X POST http://localhost:1200/_security/user/kibana_system/_password \
    -H "Content-Type: application/json" \
    -d "{\"password\": \"${KIBANA_SYSTEM_PASSWORD}\"}" > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "[WARNING] Could not set kibana_system password. Kibana may fail to start."
else
    echo "[SUCCESS] kibana_system password configured!"
fi
echo ""

echo "----------------------------------------"
echo "  Step 3: Waiting for content-primary"
echo "----------------------------------------"
echo "[INFO] Waiting for MySQL (content-primary) to be ready..."

# Connect directly inside the container on its internal port 3306
replica_attempts=0
while true; do
    replica_attempts=$((replica_attempts + 1))
    if docker exec content-primary \
        mysql -uroot -p"${MYSQL_ROOT_CONTENT_PASSWORD}" \
        -h127.0.0.1 -P3306 \
        -e "SELECT 1;" > /dev/null 2>&1; then
        echo "[SUCCESS] content-primary is ready!"
        break
    fi
    if [ "$replica_attempts" -ge 15 ]; then
        echo "[ERROR] Could not connect to content-primary after 15 attempts."
        exit 1
    fi
    echo "[INFO] Waiting for content-primary... attempt ${replica_attempts}/15"
    sleep 5
done
echo ""

echo "----------------------------------------"
echo "  Step 4: Building Elastic Setup Image"
echo "----------------------------------------"
docker_compose build elastic-setup
if [ $? -ne 0 ]; then
    echo "[ERROR] Failed to build elastic-setup image!"
    echo "[INFO] Check docker-compose.yml and elastic/Dockerfile."
    exit 1
fi
echo "[SUCCESS] Elastic setup image built!"
echo ""

echo "----------------------------------------"
echo "  Step 5: Running Setup and Import"
echo "----------------------------------------"
echo "[INFO] This will:"
echo "       1. Create the 'artists' index with mappings"
echo "       2. Import artist data from MySQL to Elasticsearch"
echo ""
docker_compose --profile setup run --rm elastic-setup

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Setup/Import failed!"
    echo "[INFO] Troubleshooting:"
    echo "      1. Check logs: docker compose logs elastic-setup"
    echo "      2. Verify Elasticsearch: docker compose logs elasticsearch"
    echo "      3. Verify MySQL: docker compose logs content-primary"
    echo ""
    exit 1
fi

echo ""
echo "[SUCCESS] Setup and import completed successfully!"
echo ""

echo "----------------------------------------"
echo "  Step 6: Verifying Import"
echo "----------------------------------------"
echo "[INFO] Checking document count in Elasticsearch..."

COUNT_RESPONSE=$(curl -s -u "elastic:${ELASTIC_ROOT_PASSWORD}" http://localhost:1200/artists/_count 2>/dev/null)
if [ $? -ne 0 ]; then
    echo "[WARNING] Could not query Elasticsearch for document count."
else
    COUNT_LINE=$(echo "$COUNT_RESPONSE" | grep -o '"count":[0-9]*')
    echo "[INFO] Document count response: ${COUNT_LINE}"
fi

echo ""
echo "================================================================================================"
echo "                         ELASTICSEARCH SETUP COMPLETE!"
echo "================================================================================================"
echo ""
echo "[INFO] Your Elasticsearch artist search is now configured."
echo "[INFO] Next steps:"
echo "       - Access Kibana at: http://localhost:1201"
echo "       - Login with: elastic / ${ELASTIC_ROOT_PASSWORD}"
echo "       - Explore the 'artists' index in Dev Tools"
echo ""
echo "[INFO] Connection details:"
echo "  Elasticsearch:    http://localhost:1200"
echo "  Kibana:           http://localhost:1201"
echo "  MySQL Primary:    localhost:1030"
echo ""
echo "[INFO] Useful commands:"
echo "  - View ES logs:      docker compose logs -f elasticsearch"
echo "  - View MySQL logs:   docker compose logs -f content-primary"
echo "  - Re-run import:     docker compose --profile setup run --rm elastic-setup"
echo ""
