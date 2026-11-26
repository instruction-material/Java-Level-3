# Street Searcher

Implement each unfinished method as its stage begins. Keep using the same workspace.

The included roads.txt contains fictional place names, coordinates and directed road connections created for this course. Each record contains source latitude and longitude, destination latitude and longitude, miles, and source--destination. Separate the fields with whitespace. Empty lines and lines beginning with # are ignored. Miles are finite and nonnegative. A repeated directed road may repeat its weight. Conflicting weights or coordinates cause a line-numbered error.

Compile each saved checkpoint in Java 21 with `javac --release 21 -Xlint:all -Werror *.java`.

1. Implement the graph in StreetGraph.java and run `java ContractTests graph`.
2. Implement the record loader in RoadFile.java and run `java ContractTests load`.
3. Implement Dijkstra in RouteFinder.java and run `java ContractTests route`. Use `java ContractTests` to run all three. Then run `java Main roads.txt Harbor Station`. The route has three stops and costs 5.00 miles. `java Main roads.txt Station Harbor` prints that the reverse route is unavailable.
4. For optional closures, implement the filter in RouteFinder.java. Run `java ContractTests avoid` and `java Main roads.txt Harbor Station Market`. The remaining route uses the direct road for 8.00 miles.
