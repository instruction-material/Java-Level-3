# Model the bakery with nested classes

Build Bakery from the supplied input fields, and keep worker, product and bakery data separate. Bakery.Worker is an inner class with an enclosing bakery and a badge that uses its name. Bakery.Product is a nested record, which can be created without a bakery. Copy the ingredient list and require a positive batch size. Type all monetary amounts in cents.

The no-argument run uses two sample records. Run `java Main input` to read the fields printed below from standard input, one value per line: bakery name, address, worker name, role, hourly cents, product name, flavor, comma-separated ingredients, and batch size.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror *.java
java ContractTests
java Main
```

Include Bakery.java when compiling. For the optional batch calculation, run `java ContractTests batches` and `java Main batches`.
