from __future__ import annotations

import os
import hashlib
import pathlib
import re
import sqlite3
import subprocess
import sys
import time
from dataclasses import dataclass

import numpy as np
from cocoindex.ops.sentence_transformers import SentenceTransformerEmbedder


PROJECT_ROOT = pathlib.Path(__file__).resolve().parents[2]
DATA_DIR = pathlib.Path(os.environ.get("KYOKU_RAG_DATA_DIR", PROJECT_ROOT / "tools" / "rag" / "data"))
SQLITE_PATH = pathlib.Path(os.environ.get("KYOKU_RAG_DB", DATA_DIR / "kyoku_project_knowledge.sqlite"))
EMBEDDING_MODEL = os.environ.get("KYOKU_RAG_EMBEDDING_MODEL", "sentence-transformers/all-MiniLM-L6-v2")
INDEX_SCRIPT_PATH = PROJECT_ROOT / "tools" / "rag" / "kyoku_project_index.py"
REFRESH_STAMP_PATH = pathlib.Path(os.environ.get("KYOKU_RAG_REFRESH_STAMP", DATA_DIR / "auto_refresh.stamp"))
REFRESH_LOCK_PATH = pathlib.Path(os.environ.get("KYOKU_RAG_REFRESH_LOCK", DATA_DIR / "auto_refresh.lock"))
SOURCE_FINGERPRINT_PATH = pathlib.Path(
    os.environ.get("KYOKU_RAG_SOURCE_FINGERPRINT", DATA_DIR / "source_fingerprint.sha256")
)
REFRESH_MIN_INTERVAL_SECONDS = int(os.environ.get("KYOKU_RAG_REFRESH_MIN_INTERVAL_SECONDS", "300"))
REFRESH_TIMEOUT_SECONDS = int(os.environ.get("KYOKU_RAG_REFRESH_TIMEOUT_SECONDS", "600"))
REFRESH_LOCK_WAIT_SECONDS = int(os.environ.get("KYOKU_RAG_REFRESH_LOCK_WAIT_SECONDS", "60"))

SOURCE_ROOTS: tuple[str, ...] = (
    "Kyoku",
    "activity",
    "auth",
    "config-server",
    "content",
    "discovery",
    "file",
    "gateway",
    "notification",
    "playlist",
    "search",
    "user",
    "validator",
    "kyoku-docker",
    "DataManipulationScripts",
)

INCLUDED_FILE_SUFFIXES = (
    ".kt",
    ".kts",
    ".java",
    ".swift",
    ".xml",
    ".sq",
    ".sql",
    ".md",
    ".yml",
    ".yaml",
    ".properties",
    ".toml",
    ".json",
    ".gradle",
    ".sh",
    ".bat",
    ".py",
)
EXCLUDED_DIR_NAMES = {
    "build",
    "out",
    "bin",
    "data",
    ".gradle",
    ".kotlin",
    ".idea",
    ".vscode",
    ".venv",
    "__pycache__",
    "node_modules",
    "DerivedData",
    "Pods",
    "xcuserdata",
    "proxy",
}
EXCLUDED_FILE_NAMES = {
    ".DS_Store",
    ".env",
    "local.properties",
    "credentials.properties",
    "google-services.json",
    "application.yml",
    "application.yaml",
}
EXCLUDED_FILE_SUFFIXES = (".jks", ".keystore", ".p12", ".pem")

_embedder: SentenceTransformerEmbedder | None = None


@dataclass
class SearchResult:
    score: float
    module: str
    path: str
    start_line: int
    end_line: int
    language: str
    text: str


def _get_embedder() -> SentenceTransformerEmbedder:
    global _embedder
    if _embedder is None:
        _embedder = SentenceTransformerEmbedder(EMBEDDING_MODEL)
    return _embedder


def _auto_refresh_enabled() -> bool:
    value = os.environ.get("KYOKU_RAG_AUTO_REFRESH", "1").strip().lower()
    return value not in {"0", "false", "no", "off"}


def _is_indexed_source_file(path: pathlib.Path) -> bool:
    if path.name in EXCLUDED_FILE_NAMES or path.name.endswith(EXCLUDED_FILE_SUFFIXES):
        return False
    if path.name.startswith(".env"):
        return False
    if not path.name.endswith(INCLUDED_FILE_SUFFIXES):
        return False

    try:
        relative_parts = path.relative_to(PROJECT_ROOT).parts
    except ValueError:
        relative_parts = path.parts

    return not any(part.startswith(".") or part in EXCLUDED_DIR_NAMES for part in relative_parts)


def _indexed_source_files() -> list[pathlib.Path]:
    paths: list[pathlib.Path] = []
    for source_root in SOURCE_ROOTS:
        root = PROJECT_ROOT / source_root
        if not root.exists():
            continue
        for current_root, dir_names, file_names in os.walk(root):
            dir_names[:] = sorted(name for name in dir_names if not name.startswith(".") and name not in EXCLUDED_DIR_NAMES)
            current_path = pathlib.Path(current_root)
            for file_name in sorted(file_names):
                path = current_path / file_name
                if not _is_indexed_source_file(path):
                    continue
                paths.append(path)
    return paths


def _source_fingerprint() -> str:
    digest = hashlib.sha256()
    for path in _indexed_source_files():
        try:
            stat = path.stat()
            relative_path = path.relative_to(PROJECT_ROOT).as_posix()
        except FileNotFoundError:
            continue
        digest.update(f"{relative_path}\0{stat.st_size}\0{stat.st_mtime_ns}\n".encode("utf-8"))
    return digest.hexdigest()


def _stored_source_fingerprint() -> str | None:
    try:
        return SOURCE_FINGERPRINT_PATH.read_text(encoding="utf-8").strip()
    except FileNotFoundError:
        return None


def _record_source_fingerprint() -> None:
    DATA_DIR.mkdir(parents=True, exist_ok=True)
    SOURCE_FINGERPRINT_PATH.write_text(_source_fingerprint(), encoding="utf-8")


def _source_inventory_changed() -> bool:
    return _source_fingerprint() != _stored_source_fingerprint()


def _last_refresh_mtime_ns() -> int:
    latest = 0
    for path in (REFRESH_STAMP_PATH, SQLITE_PATH):
        try:
            latest = max(latest, path.stat().st_mtime_ns)
        except FileNotFoundError:
            continue
    return latest


def _needs_index_refresh() -> bool:
    if not SQLITE_PATH.exists():
        return True

    last_refresh_ns = _last_refresh_mtime_ns()
    if _source_inventory_changed():
        return True

    if REFRESH_MIN_INTERVAL_SECONDS <= 0:
        return False

    return time.time_ns() - last_refresh_ns > REFRESH_MIN_INTERVAL_SECONDS * 1_000_000_000


def _acquire_refresh_lock() -> int | None:
    DATA_DIR.mkdir(parents=True, exist_ok=True)
    deadline = time.monotonic() + REFRESH_LOCK_WAIT_SECONDS
    while True:
        try:
            fd = os.open(REFRESH_LOCK_PATH, os.O_CREAT | os.O_EXCL | os.O_WRONLY)
            os.write(fd, str(os.getpid()).encode("utf-8"))
            return fd
        except FileExistsError:
            try:
                lock_age_seconds = time.time() - REFRESH_LOCK_PATH.stat().st_mtime
                if lock_age_seconds > REFRESH_TIMEOUT_SECONDS:
                    REFRESH_LOCK_PATH.unlink(missing_ok=True)
                    continue
            except FileNotFoundError:
                continue

            if time.monotonic() >= deadline:
                return None
            time.sleep(0.25)


def _release_refresh_lock(fd: int | None) -> None:
    if fd is not None:
        os.close(fd)
    REFRESH_LOCK_PATH.unlink(missing_ok=True)


def _run_index_refresh() -> None:
    fd = _acquire_refresh_lock()
    if fd is None:
        return

    try:
        result = subprocess.run(
            [sys.executable, str(INDEX_SCRIPT_PATH)],
            cwd=PROJECT_ROOT,
            env=os.environ.copy(),
            capture_output=True,
            text=True,
            timeout=REFRESH_TIMEOUT_SECONDS,
            check=False,
        )
        if result.returncode != 0:
            details = (result.stderr or result.stdout).strip()
            raise RuntimeError(f"CocoIndex refresh failed with exit code {result.returncode}: {details}")
        _record_source_fingerprint()
        REFRESH_STAMP_PATH.write_text(str(time.time_ns()), encoding="utf-8")
    finally:
        _release_refresh_lock(fd)


def _ensure_index_fresh() -> None:
    if not _auto_refresh_enabled() or not _needs_index_refresh():
        return

    try:
        _run_index_refresh()
    except Exception:
        if not SQLITE_PATH.exists():
            raise


def _decode_embedding(value: object) -> np.ndarray:
    if isinstance(value, memoryview):
        value = value.tobytes()
    if isinstance(value, bytes):
        return np.frombuffer(value, dtype=np.float32)
    if isinstance(value, str):
        return np.fromstring(value.strip("[]"), sep=",", dtype=np.float32)
    raise TypeError(f"Unsupported embedding storage type: {type(value).__name__}")


def _keyword_terms(query: str) -> list[str]:
    return [term for term in re.findall(r"[a-zA-Z0-9_]{3,}", query.lower()) if term]


def _keyword_bonus(query_terms: list[str], path: str, module: str, text: str) -> float:
    haystack = f"{module}\n{path}\n{text}".lower()
    matches = sum(1 for term in query_terms if term in haystack)
    return min(matches * 0.035, 0.35)


async def search_project_knowledge(query: str, module: str | None = None, limit: int = 8) -> list[SearchResult]:
    _ensure_index_fresh()

    if not SQLITE_PATH.exists():
        raise FileNotFoundError(
            f"Knowledge index not found at {SQLITE_PATH}. Run `tools/rag/.venv/bin/python tools/rag/kyoku_project_index.py` first."
        )

    clean_query = query.strip()
    if not clean_query:
        return []

    limit = max(1, min(limit, 25))
    query_embedding = await _get_embedder().embed(clean_query)
    query_embedding = np.asarray(query_embedding, dtype=np.float32)
    query_norm = float(np.linalg.norm(query_embedding)) or 1.0
    query_terms = _keyword_terms(clean_query)

    sql = "SELECT module, path, language, start_line, end_line, text, embedding FROM project_knowledge_chunks"
    params: list[str] = []
    if module:
        module_filter = module.strip().strip("/")
        sql += " WHERE module LIKE ? OR path LIKE ?"
        params.extend([f"%{module_filter}%", f"{module_filter}/%"])

    results: list[SearchResult] = []
    with sqlite3.connect(SQLITE_PATH) as conn:
        for row in conn.execute(sql, params):
            row_module, path, language, start_line, end_line, text, embedding_blob = row
            embedding = _decode_embedding(embedding_blob)
            embedding_norm = float(np.linalg.norm(embedding)) or 1.0
            vector_score = float(np.dot(query_embedding, embedding) / (query_norm * embedding_norm))
            score = vector_score + _keyword_bonus(query_terms, path, row_module, text)
            results.append(
                SearchResult(
                    score=score,
                    module=row_module,
                    path=path,
                    start_line=start_line,
                    end_line=end_line,
                    language=language,
                    text=text,
                )
            )

    results.sort(key=lambda item: item.score, reverse=True)
    return results[:limit]


def format_results(query: str, results: list[SearchResult]) -> str:
    if not results:
        return f"No Kyoku project knowledge results found for: {query}"

    lines = [f"Kyoku project knowledge results for: {query}", ""]
    for index, result in enumerate(results, start=1):
        location = f"{result.path}:{result.start_line}-{result.end_line}"
        snippet = result.text.strip()
        if len(snippet) > 1_500:
            snippet = snippet[:1_500].rstrip() + "..."
        lines.extend(
            [
                f"{index}. score={result.score:.3f} module={result.module} `{location}` language={result.language}",
                "```",
                snippet,
                "```",
                "",
            ]
        )
    return "\n".join(lines).rstrip()
