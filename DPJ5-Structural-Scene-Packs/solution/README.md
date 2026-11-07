# Structural scene contracts

Java 21. Keep all fourteen files and src/scene together. Rendering returns pure exercise text, not SVG/JSON or a GUI. Main reads no input and creates no output file.

Graphic.render returns non-null text. Labels match [A-Za-z][A-Za-z0-9_]{0,19}; geometry sizes are1..100. Null values cause NullPointerException and invalid non-null fields cause IllegalArgumentException. Circle/Box geometry and injected Renderer notation vary independently (Bridge). Plain outputs circle(name,r), box(name,wxh), group[children joined by semicolon]. Compact outputs C:name:r, B:name:w:h, G(children joined by pipe). These are literal strings without spaces or final LF.

Group (Composite) snapshots its non-null child list, retains child identities/order, renders each occurrence once, and delegates the collected strings to its renderer. Empty groups are valid. Children can use their own renderers. The graph is assumed acyclic and bounded; arbitrary cyclic/custom graphs have no termination guarantee. Its list is unmodifiable; child identity does not imply immutability of an arbitrary supplied Graphic.

Frame (Decorator) retains the wrapped Graphic and returns "[" + one wrapped render + "]". Nested frames add nested brackets. Framing a group differs from grouping framed children. Null rendered text is rejected.

LazyGraphic (Proxy) calls its supplied loader only on first render, caches a successfully obtained non-null Graphic, and delegates every render to that same product. Construction does not load. A loading exception/null propagates and leaves it unloaded; a later separate render may try loading again. There is no automatic retry within one call. A rendering exception leaves a successfully loaded product cached. This proxy is single-threaded and owns no external resource or disposal operation.

GlyphPool (Flyweight) shares immutable Glyph(symbol,tone) values within one pool. Symbols are A..Z and Tone is INK or ACCENT, so at most52keys exist. Repeated get for the same key returns the same reference. Separate pools can return equal but distinct objects. Placement holds extrinsic coordinates0..1000 independently; position is excluded from the pool key. Pool mutation is single-threaded.

Core: implement Circle.render, Group.render, Frame.render, LazyGraphic.render and GlyphPool.get. Optional Placement.shift creates a fresh placement with checked coordinate addition and validates the resulting bounds; it preserves the original and shares the immutable glyph.

    javac --release 21 -Xlint:all -Werror -d classes @sources.txt
    java -cp classes ContractTests core
    java -cp classes Main
    java -cp classes ContractTests

The untouched starter compiles and names the first unfinished method. Core/Main must work with shift unfinished. Main prints:
    group[circle(moon,2);[box(tile,3x4)];C:sun:1]
    A:INK@1,2
    A:INK@5,6
    true

Keep a geometry/notation comparison, group identity/ownership sketch, decorator order example, lazy-load state trace and intrinsic/extrinsic state inventory before consulting the reference.
