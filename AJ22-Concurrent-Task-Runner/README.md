# AJ22 Concurrent Task Runner

Build a bounded concurrent task runner with deterministic reporting.

## Requirements
- Model jobs with immutable input data.
- Execute jobs with a small `ExecutorService` pool.
- Record success, failure, duration, and error messages.
- Keep shared mutable state minimal and justified.
- Print the final report in deterministic order even if tasks finish out of order.

## Stretch
- Add cancellation for tasks that exceed a configured time limit.

