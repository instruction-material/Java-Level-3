# Refactoring record

For each step record the behavior promise, proposed seam, predicted result, changed files, exact command and observed result. Preserve the API, Entry, LegacyJournal, EntrySink, CompatibilityClient and ContractChecks.

## Required journal

1. Characterize the default implementation's input errors, output line, clock interval and ID bounds. State which fields naturally vary.
2. Extract the dependency calls one at a time. Keep the default construction and record call working after each change.
3. Add a constructor accepting Clock, IntSupplier and EntrySink. Preserve validation before consulting any dependency.
4. Use fixed and recording fakes to verify exact output and call order, then run both assertion settings.
5. Retain one failed output attempt. Explain what the core guarantees and why retry would be a separate behavior change.

## Optional capture normalization

Implement CaptureNormalizer independently of the journal refactor. Mask time and ID only; preserve label/count and reject malformed captures. Record a real default capture, its normalized approval and a changed-count counterexample. Explain why masking the whole line would hide defects.

## Optional failure exercise

Complete RetryingSink independently of the journal refactor. Verify success, one transient IOException, two failures, and a runtime failure with recording fakes. State the assumed no-commit-on-IOException sink contract. Explain why a real remote or partially writing sink needs idempotence/reconciliation before this policy is safe. Keep this intentional retry feature separate from the behavior-preserving core.
