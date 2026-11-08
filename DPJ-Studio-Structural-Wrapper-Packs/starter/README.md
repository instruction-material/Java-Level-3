# Structural Wrapper Refactor Studio

The working starter combines request counting, optional caching, uppercasing and
prefixing in one read method. Refactor those responsibilities into composable
Source wrappers while preserving the existing ReadDesk operations.

## Supplied contract

A key matches `[a-z][a-z0-9-]{0,15}`. Null key raises NullPointerException;
other invalid keys raise IllegalArgumentException before attempts or source calls.
Constructor source and prefix are non-null. Calls are sequential.

Every valid read increases the attempt count, including cache hits and failed
source calls. A cache hit returns its saved transformed string without calling
the source. A miss loads a non-null string, optionally uppercases it using
Locale.ROOT, then adds the unchanged prefix. Only successful results enter
the cache. Empty strings are valid cached results.

The cache belongs to that ReadDesk instance. Changes to the underlying source
stay invisible for an already cached key until clearCache; clearing preserves
the attempt count. Source exceptions propagate unchanged. A null source result
raises NullPointerException and is not cached.

The reference call order is Counting → optional Cache → Prefix → optional Upper
→ supplied Source. Counting sees every valid request. Prefix sits outside Upper
so a prefix of `tag:` stays lowercase. The Source interface expects validated
keys and non-null successful results.

| Input text | Prefix | Uppercase | Result |
| --- | --- | --- | --- |
| Hi | tag: | yes | tag:HI |
| Hi | tag: | no | tag:Hi |
| Straße | empty | yes | STRASSE |
| empty | tag: | yes | tag: |

## Build and observe

Use JDK 21:

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes Main base
java -cp classes Main
```

Both Main commands print:

```text
tag:HI
tag:HI
2
1
tag:HI
2
```

After two reads of one cached key, attempts = 2 while source calls = 1.
Clearing and reading again causes the second source call. The starter already
passes core. Keep the preserved LegacyReader and independent ContractTests
unchanged during the target refactor.

## Core work and review

1. Trace a cache miss, hit, source failure and clear using the supplied contract.
2. Extract just the load/transform helper first and rerun core. Compare that
   smaller intermediate with independently reusable wrapper objects.
3. Refactor counting, caching, prefix and uppercase into Source collaborators.
   Keep ReadDesk's constructor and public core operations unchanged.
4. Update sources.txt for added files. Run core after each coherent edit.
5. Explain why moving Counting inside Cache changes the metric even if returned
   text stays the same. Explain why moving Prefix inside Upper changes text.
6. Compare the helper-only design with four added wrapper classes, composition
   setup and cache ownership. Record a reason to retain or remove each wrapper.

The core checks cover every upper/cache combination and three prefixes, ordinary,
Unicode and empty text, invalid keys, stale cache values, failure identity, null
results and recovery. Passing output tests does not prove a justified structure;
inspect the actual dependency chain and DECISIONS.md.

## Optional selective invalidation

Implement `invalidate(key)`. Validate the key first; remove only its cached entry
and return whether it existed. A disabled cache returns false. Invalidation
performs no source call and does not change attempts. Removing an empty-string
entry returns true. Other cached keys remain unchanged.

```sh
java -cp classes ContractTests
```

The starter leaves this optional operation unfinished while core remains green.

Written transfer: compare Counting(Cache(Source)) with Cache(Counting(Source))
for reads a, a, clear, a. Both return the same three values. The outer counter
sees three requests; the inner counter sees two source misses. State which
measurement a caller wants and which wrapper owns its lifetime.

Extension review: demonstrate repeated invalidation, an absent key, empty text,
invalid key and the no-cache configuration without changing unrelated entries.
