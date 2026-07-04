from __future__ import annotations

from mcp.server.fastmcp import FastMCP

from knowledge_search import format_results, search_project_knowledge


mcp = FastMCP("kyoku_project_knowledge")


@mcp.tool()
async def project_knowledge_search(query: str, module: str | None = None, limit: int = 8) -> str:
    """Search Kyoku project knowledge across frontend, backend services, Docker setup, and data scripts."""
    try:
        results = await search_project_knowledge(query=query, module=module, limit=limit)
        return format_results(query, results)
    except Exception as exc:
        return f"Kyoku project knowledge search failed: {exc}"


if __name__ == "__main__":
    mcp.run()
