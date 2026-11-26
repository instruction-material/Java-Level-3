# Tested Gradebook Service

Treat all names and scores as fictional examples. Keep grade calculation in gradebook.Gradebook and console output in runner.Main. checks.ContractTests calls the service directly.

Validate the student ID, assignment ID, and finite score from zero through 100 before storing anything. A second submission replaces that assignment's previous score for the student.

Compute the mean of submitted scores by multiplying each score by that assignment's weight and dividing their sum by the total weight. An unsubmitted assignment contributes no weight. A student with no submissions has an empty mean. The demonstration prints 87.50 B. The learner completes submit and mean.

From the extracted project root, use Java 21:

```sh
javac --release 21 -Xlint:all -Werror -d out @sources.txt
java -cp out checks.ContractTests
java -cp out runner.Main
```
