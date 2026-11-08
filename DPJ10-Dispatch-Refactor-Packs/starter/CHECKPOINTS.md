# Runnable refactoring checkpoints
Keep LegacyDesk and ContractTests unchanged.

## 0. Characterize the runnable application
Compile all listed sources, run baseline/core and both Main modes. Save exact source/check output. Review preview purity, duplicates, snapshots, post-commit notification failure and nested send rejection.

## 1. Centralize duplicated computation
In target Desk.send only, replace the duplicated block from `String rendered;` through construction of `Dispatch result` with:
```java
Dispatch result=preview(request);
```
Retain the preceding duplicate check and following insertion/notification/finally order. Recompile and run baseline/core/Main. Target rate/rendering rules now have one location while storage and orchestration remain together. The native verifier builds and checks this actual intermediate source.

## 2. Introduce one boundary at a time
Choose a real pressure. Move map behavior behind a repository contract and rerun checks. Move computation behind a renderer contract and rerun checks. Inject collaborators and let Composition select adapters. Preserve validation, identity, effect order and guard. Keep each move in a separate recoverable commit.

The complete reference supplies these sources and controlled failure checks. Draw allowed dependencies: app -> domain/ports; infrastructure -> domain/ports; composition -> app/infrastructure. Compile app/domain/ports independently. Evaluate whether a private helper alone solves the pressure and explain the cost of each additional seam.

## 3. Review before extension
Run core's full table/failure cases. Compare defined target rule locations, changed files, test setup, added types and dependencies. Count retained legacy separately. Demonstrate another renderer/store, identify an omitted pattern and keep rollback targets.

## Optional. Add prefix-commit batching
Copy inputs before callbacks and route each item through existing send. Document which items remain after a later failure. Re-run core before the optional full harness.
