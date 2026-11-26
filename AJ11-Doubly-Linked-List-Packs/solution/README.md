# Build a doubly linked list

Use two sentinels, one before and one after the data. Update both directions of each link. Insertion and removal at either end take constant time. Set the size only when a node count changes. Access by index validates the position and walks through the data, leaving the list unchanged.

Add reverseValues to return data from the tail backward. Null is a legitimate data value, and sentinel nodes are excluded. Reset the entire structure when clearing so it can accept new entries immediately.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror *.java
java ContractTests
java Main
```

Compile Main.java, DoublyLinkedList.java, ListContract.java and ContractTests.java together.
