# Git Safety Rules

- Never run `git push` in this project from an assistant session.
- Never push Kyoku application code, config, or secrets changes unless the user explicitly requests a push in the same message.
- Do not commit changes unless the user explicitly requests a commit in the same message.
- If a Git operation is requested, inspect `git status`, `git diff`, and recent commits first, and stage only explicitly intended files.
- Local file edits are allowed when requested, but leave publishing to Git under the user's control.
