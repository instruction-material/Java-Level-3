# Build a singly linked list

Implement the supplied ListContract with a sentinel head and a size. Insert and remove at both ends. Removing from an empty list leaves it empty. A valid zero-based index reads the matching value, including a stored null. Reject every other index with IndexOutOfBoundsException. Clearing resets the sentinel and size, and the list can be reused.

The sample first builds [A, B, C], removes the front and back, and clears the list. For the optional find task, implement firstIndex by walking the data nodes in order. Return -1 for a miss and compare values with Objects.equals.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror *.java
java ContractTests
java Main
```

Compile Main.java, LinkedList.java, ListContract.java and ContractTests.java. Use `java ContractTests find` and `java Main find` for the optional search.
