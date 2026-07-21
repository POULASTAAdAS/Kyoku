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
- Never read, search, inspect, execute, or modify external or Android Gradle files, including `Kyoku/gradlew` and Gradle build files, under any circumstances.
- If you need to touch a file that has already been updated or is in progress, do not revert those changes; work around them because the user may be working in the same file.
- Do not revert any outside code without asking.
- Do not push anything to Git, ever.
- Ask permission before running any Git command.
