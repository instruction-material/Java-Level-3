# Characterization Test Bug Fix

The price policy charges 250 cents below 5,000 cents and gives free shipping at 5,000 cents or above. Negative subtotals are invalid. The starter deliberately implements the old exclusive comparison.

Compile the starter, then run java -cp out checks.ContractTests characterize. It records the old observed result of 250 cents at exactly 5,000. Run checks.ContractTests with no arguments to reproduce the failing requirement. Repair Shipping.charge and rerun the default tests. The sample now prints 0 cents.

Compare the values 4,999, 5,000 and 5,001. Keep the characterization run as evidence from before the repair.

From the extracted project root, use Java 21:

```sh
javac --release 21 -Xlint:all -Werror -d out @sources.txt
java -cp out checks.ContractTests
java -cp out runner.Main
```
