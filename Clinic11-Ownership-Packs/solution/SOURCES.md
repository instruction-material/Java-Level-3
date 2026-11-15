# Sources

This original grid-route exercise supplies the Refactoring Clinic 11 objectives: feature envy, Extract Class, Move Method, ownership, unchanged clients and characterized edge behavior. The coordinate domain and exact API/error promises are exercise-specific.

- [Extract Class, Martin Fowler](https://refactoring.com/catalog/extractClass.html): related data and behavior move behind a collaborating object.
- [Java 21 List API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html): ordered elements, null rejection and unmodifiable List.copyOf results.

LegacyPlanner is the frozen before design. RoutePlan in the reference is an example ownership boundary. The longest-leg and waypoint-preview exercises have independent editable files and runnable checks. No external library is required.
