# Build a branching chatbot

Read a name and a favorite activity on two lines. Normalize the activity with strip and Locale.ROOT lowercase. Recognize reading and drawing, and give a fallback for other activities.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

Enter the following lines at the console.

```text
Mira
DRAWING
```
