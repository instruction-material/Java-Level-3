# AJ20 Type-Safe Plugin Registry

Design a registry for named commands or tools without falling back to raw `Object` values.

## Requirements
- Define a shared plugin or command interface.
- Register at least four implementations by name.
- Look up and execute implementations through the interface.
- Reject duplicate registrations and unknown names cleanly.
- Avoid raw types and unchecked casts in the main registry API.

## Stretch
- Add command metadata such as description, required arguments, and examples.

