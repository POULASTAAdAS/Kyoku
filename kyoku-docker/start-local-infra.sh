#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR"
CLOUDFLARED_CONFIG="${CLOUDFLARED_CONFIG:-/Users/poulastaad/.cloudflared/config.yml}"
CLOUDFLARED_TUNNEL="${CLOUDFLARED_TUNNEL:-}"
RUN_ELASTIC_SETUP=false
SKIP_TUNNEL=false

usage() {
  cat <<'EOF'
Usage: ./start-local-infra.sh [options]

Starts Kyoku local infrastructure only:
  - Docker dependencies from kyoku-docker/docker-compose.yml
  - Kyoku Cloudflare tunnel in a separate macOS Terminal window

It does not start any Spring backend service. Start config-server,
discovery, gateway, and the other backend services manually.

Options:
  --setup-elastic  Run the Elasticsearch artist index/import helper after Docker starts.
  --no-tunnel      Start Docker dependencies only.
  -h, --help       Show this help.

Environment overrides:
  CLOUDFLARED_CONFIG  Path to cloudflared config file.
  CLOUDFLARED_TUNNEL  Optional tunnel name/UUID argument for cloudflared run.
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --setup-elastic)
      RUN_ELASTIC_SETUP=true
      ;;
    --no-tunnel)
      SKIP_TUNNEL=true
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "[ERROR] Unknown option: $1" >&2
      usage >&2
      exit 1
      ;;
  esac
  shift
done

require_command() {
  local command_name="$1"

  if ! command -v "$command_name" >/dev/null 2>&1; then
    echo "[ERROR] Required command not found: $command_name" >&2
    exit 1
  fi
}

docker_compose() {
  if docker compose version >/dev/null 2>&1; then
    docker compose "$@"
  elif command -v docker-compose >/dev/null 2>&1; then
    docker-compose "$@"
  else
    echo "[ERROR] Docker Compose is not available." >&2
    echo "[INFO] Install Docker Compose v2 or docker-compose v1." >&2
    exit 1
  fi
}

start_cloudflared_terminal() {
  osascript - "$CLOUDFLARED_CONFIG" "$CLOUDFLARED_TUNNEL" <<'APPLESCRIPT'
on run argv
  set configPath to item 1 of argv
  set tunnelName to item 2 of argv
  set commandText to "cloudflared tunnel --config " & quoted form of configPath & " run"

  if tunnelName is not "" then
    set commandText to commandText & " " & quoted form of tunnelName
  end if

  tell application "Terminal"
    activate
    do script commandText
  end tell
end run
APPLESCRIPT
}

require_command docker

if [[ "$SKIP_TUNNEL" == false ]]; then
  require_command cloudflared
  require_command osascript

  if [[ ! -f "$CLOUDFLARED_CONFIG" ]]; then
    echo "[ERROR] cloudflared config not found: $CLOUDFLARED_CONFIG" >&2
    exit 1
  fi
fi

if [[ ! -f "$DOCKER_DIR/.env" ]]; then
  echo "[ERROR] Missing Docker environment file: $DOCKER_DIR/.env" >&2
  echo "[INFO] Create it from $DOCKER_DIR/.env.example and fill in local values." >&2
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "[ERROR] Docker is not running or is not reachable." >&2
  echo "[INFO] Start Docker Desktop, then run this script again." >&2
  exit 1
fi

echo "================================================================================================"
echo "  Kyoku Local Infrastructure"
echo "================================================================================================"
echo "[INFO] Docker compose file: $DOCKER_DIR/docker-compose.yml"
echo "[INFO] Starting Docker dependencies only. Backend services are not started by this script."
echo ""

(
  cd "$DOCKER_DIR"
  docker_compose config --quiet
  docker_compose up -d
)

echo ""
echo "[SUCCESS] Docker dependencies are starting/running."
echo "[INFO] Expected local dependency ports:"
echo "  MySQL: user=1000 playlist=1010 activity=1020 content=1030"
echo "  MongoDB: activity=1038"
echo "  Redis: gateway=1040"
echo "  MinIO: file=1080/1081 song=1082/1083"
echo "  RabbitMQ: amqp=1100 management=14000"
echo "  Elasticsearch/Kibana: 1200/1201"
echo ""

if [[ "$RUN_ELASTIC_SETUP" == true ]]; then
  echo "[INFO] Running Elasticsearch artist index/import setup..."
  (
    cd "$DOCKER_DIR"
    bash ./elastic/start.sh
  )
  echo ""
fi

if [[ "$SKIP_TUNNEL" == false ]]; then
  echo "[INFO] Opening Kyoku cloudflared tunnel in a separate Terminal window..."
  echo "[INFO] Tunnel target: kyoku-gateway.poulastaa.dev -> http://localhost:8080"
  start_cloudflared_terminal
  echo "[SUCCESS] cloudflared Terminal window opened. Leave it running."
else
  echo "[INFO] Skipped cloudflared tunnel startup."
fi

echo ""
echo "[INFO] Start backend services manually when ready: config-server :8888, discovery :8001, gateway :8080."
echo "[INFO] Stop Docker dependencies with: cd '$DOCKER_DIR' && docker compose down"
