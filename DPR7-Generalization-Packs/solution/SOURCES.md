# Source and technique notes

The three case studies and their code, frozen before designs, inputs and checks are authored for this course. The technique names and category follow Refactoring.Guru:
https://refactoring.guru/refactoring/techniques/dealing-with-generalization

Java standard-library semantics used here are defined by the Java 21 API:
https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html
https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Math.html#addExact(long,long)

The preserved scope is the stated domain API and supplied source clients. Legacy class hierarchy, reflection and serialized object shape are not retained contracts.

Java 21 sealed-class declaration and permitted-subclass rules:
https://docs.oracle.com/en/java/javase/21/language/sealed-classes-and-interfaces.html
