# Sources and design basis

This original workshop-registration exercise implements the DPR10 Capstone Refactoring Studio objectives: choose a salvageable application, diagnose risks, sequence safe transformations, evaluate pattern use and review the resulting architecture. The workshop API, examples, fixtures and optional tasks are course-specific contracts, not claims about an external registration system.

- [Martin Fowler: Extract Class](https://refactoring.com/catalog/extractClass.html) illustrates moving related data and behavior behind a collaborating object.
- [Java 21 List API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html) defines ordered lists and the unmodifiable, null-rejecting List.copyOf factory used by snapshots.
- [Java 21 LinkedHashMap API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/LinkedHashMap.html) specifies encounter order for the catalog representation.

The starter preserves the working before design in WorkshopDesk. The reference is one example, with per-session ownership and rendering separated. Optional trace comparison and rename preview have independent entry points. No third-party library is required.
