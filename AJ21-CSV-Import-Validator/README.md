# AJ21 CSV Import Validator

Create a CSV-style importer that accepts good rows and reports bad rows without corrupting accepted data.

## Requirements
- Read records from a comma-separated text file.
- Validate required fields, numeric fields, dates or statuses, and duplicate IDs.
- Store accepted records in a collection chosen for the required operations.
- Produce a rejected-row report that explains each failure.
- Preserve existing application state if a later row fails.

## Stretch
- Add a dry-run mode that validates a file without importing it.

