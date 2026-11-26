# Event-Driven Library Queue

This fictional library reads a UTF-8 script from requests.txt. Define book stock first using stock title count. Then place request, return, and cancel events in order as verb member title. Each title has available copies, recorded loans, and a waiting list. An available copy goes to the requester. Otherwise add the member to the waiting list. A returned copy goes to the first waiting member if there is one. A duplicate request preserves the original state. A cancellation removes the named waiting member.

The service owns circulation state. The parser produces typed events, runner.Main controls the loop, and EventSink accepts each result. Compare the effect of collecting outcomes in memory with streaming them to a new file. To save a report, run java -cp out runner.Main requests.txt report.txt. Use a new report filename.

For the optional review, open the same workspace. Reproduce the mutable waiting-list issue with java -cp out checks.ContractTests extension. Change waiting to expose an immutable snapshot and rerun the whole suite. Task 2 core checks can pass before this optional change.

Compile from this folder with Java 21:

```sh
javac --release 21 -Xlint:all -Werror -d out @sources.txt
java -cp out checks.ContractTests
java -cp out runner.Main
```
