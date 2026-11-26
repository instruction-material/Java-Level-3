# Build a Mad Lib

Read an animal, a verb, an adjective, and a noun as four complete lines. Insert the values into the supplied sentence pattern. Keep interior spaces and trim the ends of each input.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the optional headline task, run java ContractTests headline and java Main headline with the same four input lines.

Enter the following lines at the console.

```text
red fox
dance
curious
a lantern
```
