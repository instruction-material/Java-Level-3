# Build a branching chatbot

Read a name and a favorite activity on two lines. Normalize the activity with strip and Locale.ROOT lowercase. Recognize reading and drawing, and give a fallback for other activities.

This reference passes the supplied contract checks.

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
