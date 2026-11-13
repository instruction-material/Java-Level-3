# Structural checkpoints

| Stage | Command/result | Source change | Retained behavior evidence |
| --- | --- | --- | --- |
| Baseline | Record base output | None | Driver, malformed row, client API |
| Value objects | Record focused/core result | Code and quantity validation | Error order, value equality |
| Owned collection | Record base/core result | Ordered immutable snapshot | Input/getter mutation, map lookup |
| Transport boundary | Record base/core result | RequestCodec | Missing/extra/invalid/round-trip rows |
| Optional merge | Record all result separately | New line-grouping feature | First occurrence order, overflow, unchanged original |

Record a recoverable source checkpoint after each green structural step. A deliberately failing new case identifies the next step; retain the last green baseline independently.
