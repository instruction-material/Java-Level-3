# Clinic 16 review record

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

## Preserve a deliberate missing-profile policy

The old Greetings API accepts a nullable Profile. Null deliberately produces hello="Hello, guest" and channel="screen". A present profile produces "Hello, "+its exact name and its stored channel. A real profile named guest with channel email is still present and must retain email.

Profile is an immutable value. Validate name first: null throws NullPointerException("name"); blank, longer than 16 UTF-16 char units, or containing pipe/carriage-return/newline throws IllegalArgumentException("name"). Accepted spaces and Unicode are retained. Validate channel second: null throws NullPointerException("channel"), and anything except exact email or screen throws IllegalArgumentException("channel"). A partially populated or invalid profile fails during construction; it is never silently converted into a guest.

Trace both old methods for null and a present profile. Extract a label decision first while keeping the channel path stable. Then introduce an internal Recipient abstraction. The reference uses KnownRecipient for present data and GuestRecipient for the intentional default; Recipient.of is the one nullable boundary. GreetingService remains the same public facade.

The guest object's methods define the default policy. They do not eliminate the need to distinguish presence at an input boundary. Default behavior is appropriate only where absence is an accepted domain case. Keep invalid Profile construction loud. Use the exact old outputs to protect this distinction.

Core behavior checks already pass in the starter. Establish the source refactor with actual small changes, moved responsibility and an external unchanged client. Explain which repeated caller choices disappeared, where null is still interpreted, why guest is a policy rather than fabricated personal data, and one boundary where absence must instead be an error.

This final reference illustrates the described responsibility split. Retain the actual source checkpoints used in an attempt; passing behavior alone does not establish that sequence or its design judgment.
