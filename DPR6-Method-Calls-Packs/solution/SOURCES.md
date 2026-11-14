# Sources and provenance

This is an original Java21 teaching exercise, authored for the method-call module. It replaces the old pricing-strategy placeholder with dispatch API migration; the old placeholder is retained in repository history, not represented as the original project for this topic.

The syllabus's fourteen historical technique names are retained. The worked code and explanations are original.

- Martin Fowler, Introduce Parameter Object: https://refactoring.com/catalog/introduceParameterObject.html
- Martin Fowler, Preserve Whole Object: https://refactoring.com/catalog/preserveWholeObject.html
- Martin Fowler, Separate Query from Modifier: https://refactoring.com/catalog/separateQueryFromModifier.html
- Martin Fowler, Replace Constructor with Factory Function: https://refactoring.com/catalog/replaceConstructorWithFactoryFunction.html
- Java SE21 Optional API: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html

The three Fowler examples clarify distinct boundaries; no book example is copied into this exercise. Optional is used for a legitimate missing return value and is itself non-null. No dependency downloads are required.
