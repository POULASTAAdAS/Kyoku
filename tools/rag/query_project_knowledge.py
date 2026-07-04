from __future__ import annotations

import argparse
import asyncio

from knowledge_search import format_results, search_project_knowledge


async def main() -> None:
    parser = argparse.ArgumentParser(description="Query the local Kyoku project knowledge index.")
    parser.add_argument("query", help="Question or search text")
    parser.add_argument(
        "--module",
        help="Optional module filter, for example frontend, backend-auth, backend-user, docker, data-scripts",
    )
    parser.add_argument("--limit", type=int, default=8, help="Maximum number of chunks to return")
    args = parser.parse_args()

    results = await search_project_knowledge(args.query, module=args.module, limit=args.limit)
    print(format_results(args.query, results))


if __name__ == "__main__":
    asyncio.run(main())
