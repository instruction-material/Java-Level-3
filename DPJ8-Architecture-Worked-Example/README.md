# Explicit application composition
Use Java 21:
```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
Main prints:
```text
1:HI
[1=HI]
2:READY
[1=HI]
[1=HI, 2=READY]
```
Memory supplies the Store port; Service receives that port, a text processor and an observer.
write upserts a positive-id, non-null single-line Item, applying the processor before save and
notifying afterward. Duplicate ids replace in their original insertion position.
Notification failure leaves storage committed. Reentrant mutation is rejected and the guard
is always released. List and screen values are immutable snapshots. ViewModel refresh is
explicit; the stale screen after the second write illustrates that boundary.
This complete example uses an explicitly injected processor. The assigned notebook pack adds
actual ServiceLoader discovery, provider metadata, named pipelines and separate create/replace.
There is no UI toolkit, database, asynchronous dispatcher or automatic binding.
