# Project Coding Rules

- Reuse existing codebase functionality as much as possible before adding new code.
- Follow the current codebase coding patterns, naming, structure, and style.
- Follow established best practices for the language, framework, and layer being changed.
- Make changes according to the existing codebase structure, even when a larger refactor is required to do it correctly.
- When asked to implement something, never take shortcuts; implement the correct solution even if it may require a complete rewrite.
- Before starting a complete rewrite, stop and ask for approval. Concisely explain why the rewrite is necessary, its scope and impact, and the proposed approach, then wait for further instruction.
- If a change is unclear or there is not enough context, ask before editing. Do not make assumption-based changes.
- Do not touch code outside what needs to change for the current request or job.
- Do not change unrelated code even if it blocks verification or the current task; ask before touching anything outside the requested scope.
- Never write or add test cases.
- Never read, search, inspect, execute, or modify generated or external Gradle artifacts, including `Kyoku/gradlew`, `.gradle/`, generated build output, and external Android Gradle files. Source-controlled Gradle configuration files such as `*.gradle.kts` may be read and modified when required by the task.
- If you need to touch a file that has already been updated or is in progress, do not revert those changes; work around them because the user may be working in the same file.
- Do not revert any outside code without asking.
- Do not push anything to Git, ever.
- Ask permission before running any Git command.

# Plans

- Song streaming (signed URLs + nginx, songs moved out of MinIO): `plans/song-streaming-setup.md`. Read and follow this file only when the user talks about implementing song streaming, HLS/manifest/chunk auth, the media nginx, or migrating songs out of MinIO; otherwise ignore it.

# Infrastructure

- Cloudflare tunnel (`cloudflared`, tunnel `kyoku`): `kyoku-docker/cloudflare-tunnel.md`. Read this file first whenever starting or checking the local infra, or when a task involves the tunnel, public hostnames, exposing a service (e.g. MinIO/static files), or Cloudflare caching. Treat it as the source of truth for the current tunnel setup instead of assuming one.
