# Notebook architecture
Use Java 21. Keep the package tree, sources.txt and provider descriptor together.
This single-threaded in-memory program makes no network requests and persists no data across runs.

## Build and run
From this role folder:
```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
jar --create --file notebook.jar -C classes . -C . META-INF
java -cp notebook.jar ContractTests core
java -cp notebook.jar Main
```
The JAR command packages the UTF-8 META-INF/services/notes.plugins.TextPlugin descriptor. javac alone does not copy it. Main prints:
```text
[bracket, upper]
a:[HI]
[a=[HI]]
a:[Ready]
[a=[Ready]]
1
```
The untouched starter compiles and rejects the first unfinished repository lookup. Complete eight core methods: InMemoryNotes.find/all/save, PluginCatalog.discover/apply, NoteService.create/replace and NoteViewModel.refresh. Main and core checks work with optional NoteService.copy unfinished.

## Boundaries
Note is an immutable id/text value. Ids match [a-z][a-z0-9-]*; null is rejected. Text may be empty but excludes CR/LF. InMemoryNotes upserts by id, retains first-insertion order on replacement, and returns immutable snapshot lists. find rejects invalid ids and returns Optional.empty for absence.

PluginCatalog captures providers by their id at construction, rejects duplicate/invalid/null providers, and presents sorted immutable availableIds. A provider changing its id later does not change that captured key. apply validates text and copies requested ids, resolves every id before executing any provider, then applies in requested order, including repeated occurrences. Empty pipeline is identity. Unknown ids or invalid results fail. Exceptions propagate; the caller owns prior provider side effects. A copied request selection remains stable during provider execution.

discover eagerly iterates ServiceLoader.load(TextPlugin.class, explicitLoader) into this catalog. The included classpath providers are public implementations with public no-argument constructors. Discovery instantiates trusted code and provides no isolation. ServiceConfigurationError propagates. Empty discovery gives an empty catalog, never a hidden fallback. Runtime provider order does not select the pipeline: ids supplied to apply do. This pack uses a classpath JAR; it contains no module-info.java or plugin installation UI.

NoteService receives repository, catalog and event sink in its constructor. create requires a new id; replace requires an existing id. It validates/transforms before save, then publishes after save. Repository exceptions prevent sink delivery. A sink exception propagates after the note is committed; retries can encounter a duplicate. The busy guard rejects nested mutating calls during plugin/sink work and is released in finally. Read-only list calls remain allowed. Conforming repositories provide their declared operations without reentering application mutations. Storage and external observers have no shared transaction.

The repository is a port. The supplied adapter is in memory. The service depends on domain, ports and plugin API; Main is the composition root that selects infrastructure. Tests supply another repository through the same interface.

NoteViewModel stores immutable presentation rows. refresh reads the service and replaces state once rows are ready. add invokes create and then refresh on success. A failing notification can follow a committed note while leaving the view at its earlier snapshot; explicitly refresh after handling that failure. This presentation model illustrates MVVM responsibilities with explicit refresh. It provides no toolkit or automatic binding. For MVC, a controller would handle view input and select presentation updates while the domain/service boundary remains separate.

## Optional copy
After core, implement NoteService.copy. Validate both ids; require the source to exist and target to be absent; apply the requested pipeline to source text; save under target id and notify after commit. Leave source unchanged. Apply the same mutation guard and failure boundaries.
```sh
java -cp notebook.jar ContractTests
```
Keep the repository contract, plugin configuration/order trace, dependency diagram, notification-failure trace and presentation refresh evidence alongside the code. Explain a simpler direct design for a small fixed program.
