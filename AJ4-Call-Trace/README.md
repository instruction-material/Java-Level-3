# Worked call-and-return trace

This complete example sums integers from 1 through n for 0 <= n <= 100. The empty sum is 0. A call to sum(4) descends through 4, 3, 2, 1 and 0. The returns are 0, 1, 3, 6 and 10. Each pending frame remembers one addition.

Use JDK 21:

    javac -Xlint:all Main.java ContractTests.java
    java ContractTests
    java Main

The checks print "Call-trace contracts: 105 passed"; the example prints 10. Compare the trace for n=4 with n=0 and n=1. Return to the separate Recursion Practice learner pack to implement its five methods.
