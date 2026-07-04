from __future__ import annotations

import hashlib
import os
import pathlib
from dataclasses import dataclass
from typing import Annotated, Iterator

import cocoindex as coco
from cocoindex.connectors import localfs, sqlite
from cocoindex.ops.sentence_transformers import SentenceTransformerEmbedder
from cocoindex.ops.text import RecursiveSplitter, detect_code_language
from cocoindex.resources.file import FileLike, PatternFilePathMatcher
from numpy.typing import NDArray


PROJECT_ROOT = pathlib.Path(__file__).resolve().parents[2]
DATA_DIR = pathlib.Path(os.environ.get("KYOKU_RAG_DATA_DIR", PROJECT_ROOT / "tools" / "rag" / "data"))
SQLITE_PATH = pathlib.Path(os.environ.get("KYOKU_RAG_DB", DATA_DIR / "kyoku_project_knowledge.sqlite"))
COCOINDEX_DB_PATH = pathlib.Path(os.environ.get("COCOINDEX_DB", DATA_DIR / "cocoindex.lmdb"))
EMBEDDING_MODEL = os.environ.get("KYOKU_RAG_EMBEDDING_MODEL", "sentence-transformers/all-MiniLM-L6-v2")

SQLITE_DB = coco.ContextKey[sqlite.ManagedConnection]("kyoku_project_knowledge_db")

_embedder = SentenceTransformerEmbedder(EMBEDDING_MODEL)
_splitter = RecursiveSplitter()

SOURCE_ROOTS: tuple[tuple[str, str], ...] = (
    ("frontend", "Kyoku"),
    ("backend-activity", "activity"),
    ("backend-auth", "auth"),
    ("backend-config-server", "config-server"),
    ("backend-content", "content"),
    ("backend-discovery", "discovery"),
    ("backend-file", "file"),
    ("backend-gateway", "gateway"),
    ("backend-notification", "notification"),
    ("backend-playlist", "playlist"),
    ("backend-search", "search"),
    ("backend-user", "user"),
    ("backend-validator", "validator"),
    ("docker", "kyoku-docker"),
    ("data-scripts", "DataManipulationScripts"),
)

INCLUDED_PATTERNS = [
    "**/*.kt",
    "**/*.kts",
    "**/*.java",
    "**/*.swift",
    "**/*.xml",
    "**/*.sq",
    "**/*.sql",
    "**/*.md",
    "**/*.yml",
    "**/*.yaml",
    "**/*.properties",
    "**/*.toml",
    "**/*.json",
    "**/*.gradle",
    "**/*.sh",
    "**/*.bat",
    "**/*.py",
]

EXCLUDED_PATTERNS = [
    "**/.*/**",
    "**/.DS_Store",
    "**/build/**",
    "**/out/**",
    "**/bin/**",
    "**/data/**",
    "**/.gradle/**",
    "**/.kotlin/**",
    "**/.idea/**",
    "**/.vscode/**",
    "**/.venv/**",
    "**/__pycache__/**",
    "**/node_modules/**",
    "**/DerivedData/**",
    "**/Pods/**",
    "**/xcuserdata/**",
    "**/.env",
    "**/.env.*",
    "**/local.properties",
    "**/credentials.properties",
    "**/google-services.json",
    "**/application.yml",
    "**/application.yaml",
    "**/proxy/**",
    "**/*.jks",
    "**/*.keystore",
    "**/*.p12",
    "**/*.pem",
]

MAX_FILE_BYTES = 350_000
CHUNK_SIZE = 1_800
CHUNK_OVERLAP = 250


@dataclass
class ProjectKnowledgeChunk:
    id: str
    module: str
    path: str
    language: str
    start_line: int
    end_line: int
    text: str
    embedding: Annotated[NDArray, _embedder]


@coco.lifespan
def coco_lifespan(builder: coco.EnvironmentBuilder) -> Iterator[None]:
    DATA_DIR.mkdir(parents=True, exist_ok=True)
    builder.settings.db_path = COCOINDEX_DB_PATH
    with sqlite.managed_connection(SQLITE_PATH, load_vec="auto") as conn:
        builder.provide(SQLITE_DB, conn)
        yield


def _source_roots() -> list[tuple[str, str, pathlib.Path]]:
    roots: list[tuple[str, str, pathlib.Path]] = []
    for module, relative_root in SOURCE_ROOTS:
        absolute_root = PROJECT_ROOT / relative_root
        if absolute_root.exists():
            roots.append((module, relative_root, absolute_root))
    return roots


def _language_for_path(path: str) -> str:
    if path.endswith((".sq", ".sql")):
        return "sql"
    if path.endswith((".yml", ".yaml")):
        return "yaml"
    if path.endswith(".properties"):
        return "properties"
    if path.endswith(".gradle"):
        return "kotlin"
    if path.endswith(".bat"):
        return "batch"
    detected = detect_code_language(filename=path)
    return detected or "text"


def _chunk_id(path: str, start: int, end: int, text: str) -> str:
    digest = hashlib.sha256(f"{path}:{start}:{end}:{text}".encode("utf-8")).hexdigest()
    return digest[:32]


def _repo_relative_path(source_relative_root: str, file: FileLike) -> str:
    file_path = pathlib.Path(file.file_path.path)
    if file_path.is_absolute():
        relative_to_source = file_path.relative_to(PROJECT_ROOT / source_relative_root)
    else:
        relative_to_source = file_path
    return (pathlib.PurePosixPath(source_relative_root) / relative_to_source).as_posix()


@coco.fn(memo=True)
async def process_file(
    file: FileLike,
    module: str,
    source_relative_root: str,
    table: sqlite.TableTarget[ProjectKnowledgeChunk],
) -> None:
    raw = await file.read()
    if len(raw) > MAX_FILE_BYTES:
        return

    text = raw.decode("utf-8", errors="replace")
    if not text.strip():
        return

    path = _repo_relative_path(source_relative_root, file)
    language = _language_for_path(path)

    try:
        chunks = _splitter.split(
            text,
            chunk_size=CHUNK_SIZE,
            chunk_overlap=CHUNK_OVERLAP,
            language=language,
        )
    except Exception:
        chunks = _splitter.split(
            text,
            chunk_size=CHUNK_SIZE,
            chunk_overlap=CHUNK_OVERLAP,
        )

    for chunk in chunks:
        chunk_text = chunk.text.strip()
        if not chunk_text:
            continue

        start_line = chunk.start.line
        end_line = chunk.end.line
        table.declare_row(
            row=ProjectKnowledgeChunk(
                id=_chunk_id(path, start_line, end_line, chunk_text),
                module=module,
                path=path,
                language=language,
                start_line=start_line,
                end_line=end_line,
                text=chunk_text,
                embedding=await _embedder.embed(f"{path}\n{chunk_text}"),
            ),
        )


@coco.fn
async def app_main() -> None:
    table = await sqlite.mount_table_target(
        SQLITE_DB,
        "project_knowledge_chunks",
        await sqlite.TableSchema.from_class(ProjectKnowledgeChunk, primary_key=["id"]),
    )

    matcher = PatternFilePathMatcher(
        included_patterns=INCLUDED_PATTERNS,
        excluded_patterns=EXCLUDED_PATTERNS,
    )

    for module, source_relative_root, absolute_root in _source_roots():
        files = localfs.walk_dir(
            absolute_root,
            recursive=True,
            path_matcher=matcher,
        )
        async for key, file in files.items():
            await coco.mount(
                coco.component_subpath("file", f"{module}/{key}"),
                process_file,
                file,
                module,
                source_relative_root,
                table,
            )


app = coco.App("KyokuProjectKnowledge", app_main)


if __name__ == "__main__":
    app.update_blocking(report_to_stdout=True)
