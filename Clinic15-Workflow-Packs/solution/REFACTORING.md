# Clinic 15 review record

## Before changing source
Public call and exact input/output:
Source locations and concrete smell:
Behavior, validation order and data that must stay stable:
Chosen boundary and why it fits the problem:

## Actual small steps
Retain each source checkpoint or commit identity.
For each step: prediction, change, passing output and next risk.
First extraction:
Next responsibility move:
Final client and boundary checks:
Recoverable checkpoint or postponed change:

## Review
Before/after responsibility sketch:
One observed benefit and one tradeoff:
Standard, boundary and failure cases:
One tempting change that would alter behavior:
Pattern or technique justified by this case:

## Optional independent work
Transfer predictions, actual output and retained inputs:
Extension predictions, actual output and retained inputs:
What these observations establish and what needs further review:

## Reference notes

## Identify the common sequence

The working DocumentService duplicates a document workflow: validate rows, create a header, append each row in order and finish with a footer. Plain output for ["a","b"] is "begin\na\nb\nend\n"; marked output is "BEGIN\n* a\n* b\nEND\n". Every shown line ends with newline. Empty input still has its header and footer. An empty row remains an actual blank body row.

The facade validates kind non-null with NullPointerException("kind"), then the entire row list, then checks whether kind is exactly plain or marked. Unknown kinds throw IllegalArgumentException("kind"). Row validation rejects a null list with NullPointerException("rows") and more than six rows with IllegalArgumentException("rows"). Each row must be non-null (NullPointerException("row")), at most 16 UTF-16 char units and contain no pipe, carriage return or newline (IllegalArgumentException("row")). Empty rows and accepted spaces are preserved. Input order and caller lists remain unchanged.

Extract the repeated row loop into a helper first. Run baseline with both formats before adding variation. Then complete DocumentTemplate's final build method: validate all rows, call header once, line once per row in order, and footer once. Concatenate exactly those strings. Reject null hook results with NullPointerException("header"), ("line") or ("footer"). A hook runtime failure propagates and stops later hooks. Earlier callback effects have no rollback guarantee.

header and line are protected abstract hooks. The default footer is an empty string, permitting a deliberate omitted footer. PlainDocument and MarkedDocument implement their existing formats. Keep DocumentService.build(kind,rows) and new DocumentService() intact. The reference facade selects a format and delegates to the shared final skeleton; individual variants own their formatting.

Compile a custom template with no footer and check its output. Record a trace such as header, line:a, line:, footer and verify that invalid later rows cause no hooks. A final template method keeps order fixed while subclasses supply steps. A Strategy-based design can instead receive independent step collaborators when runtime assembly or multiple unrelated behaviors warrant it; compare that tradeoff rather than introducing both forms by default.

Retain both original outputs, the first shared-loop extraction, the final skeleton, an omitted-footer case, a failing body hook and an explanation of which duplication disappeared.

This final reference illustrates the described responsibility split. Retain the actual source checkpoints used in an attempt; passing behavior alone does not establish that sequence or its design judgment.
