# Kyoku Project Knowledge Index

This directory contains the local CocoIndex-backed project knowledge setup used by OpenCode.

## What Is Indexed

The index uses one SQLite database for Kyoku frontend and backend knowledge. It covers:

- `Kyoku/` frontend Kotlin Multiplatform app code, shared modules, iOS app code, and Gradle build logic
- Backend services: `activity`, `auth`, `config-server`, `content`, `discovery`, `file`, `gateway`, `notification`, `playlist`, `search`, `user`, and `validator`
- Helpful local setup files under `kyoku-docker/`, including compose, SQL, scripts, and docs
- Data helper scripts under `DataManipulationScripts/`

It intentionally excludes generated folders, build outputs, Docker data, virtual environments, external dependencies, IDE folders, and sensitive/local files such as `.env`, `application.yml`, `local.properties`, credentials, keystores, signing files, and proxy config.

## Setup

```bash
/opt/homebrew/bin/python3.12 -m venv tools/rag/.venv
tools/rag/.venv/bin/python -m pip install -r tools/rag/requirements.txt
```

CocoIndex is not available for the macOS system Python 3.9 used by `/usr/bin/python3`; use Python 3.12 or newer.

## Build Or Refresh The Index

```bash
tools/rag/.venv/bin/python tools/rag/kyoku_project_index.py
```

CocoIndex stores incremental state under `tools/rag/data/cocoindex.lmdb` and the searchable SQLite target at `tools/rag/data/kyoku_project_knowledge.sqlite`.

## Auto Refresh

Local queries and the OpenCode MCP server refresh the index on demand before searching. A refresh runs when the SQLite target is missing, when the indexed source fingerprint changes because of an add, edit, delete, or rename, or when the refresh marker is older than `KYOKU_RAG_REFRESH_MIN_INTERVAL_SECONDS` as a periodic safety net.

The default OpenCode interval is 300 seconds. Set `KYOKU_RAG_AUTO_REFRESH=0` to disable auto-refresh, or change `KYOKU_RAG_REFRESH_MIN_INTERVAL_SECONDS` to tune the periodic refresh cadence.

## Query Locally

```bash
tools/rag/.venv/bin/python tools/rag/query_project_knowledge.py "Where is sign in handled?"
tools/rag/.venv/bin/python tools/rag/query_project_knowledge.py "How does auth token validation work?" --module backend-auth
tools/rag/.venv/bin/python tools/rag/query_project_knowledge.py "Which services are in docker compose?" --module docker
```

## OpenCode MCP

`opencode.json` starts `tools/rag/mcp_project_knowledge.py` as the `kyoku_project_knowledge` MCP server. Restart OpenCode after config changes.
