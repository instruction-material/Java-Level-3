# AJ19 Command Parser Test Harness

Write a command parser that can be tested without running the full application.

## Requirements
- Parse commands such as `add "task name" 2026-05-01 high`, `done 3`, `list`, and `quit`.
- Return structured command objects or a typed parse result instead of raw strings.
- Reject malformed commands without changing application state.
- Track useful error messages for missing fields, invalid IDs, and unknown commands.
- Include a parser-only test harness with valid and invalid commands.

## Stretch
- Add quoted-string parsing with escaped quotes.
- Add line-numbered script execution from a text file.

