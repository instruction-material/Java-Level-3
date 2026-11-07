// Factory Method: subclasses choose a rendering product for a shared workflow.
package creation;
import java.util.Objects;

public final class Exporters {
    private Exporters() {}
    public abstract static class Exporter {
        protected abstract Renderer createRenderer();
        public final String export(Report report) {
            Objects.requireNonNull(report, "report");
            Renderer product = Objects.requireNonNull(createRenderer(), "renderer");
            return Objects.requireNonNull(product.render(report), "rendered text");
        }
    }
    public static final class Plain extends Exporter {
        protected Renderer createRenderer() { return RendererFactory.forStyle(Style.PLAIN); }
    }
    public static final class Boxed extends Exporter {
        protected Renderer createRenderer() { return RendererFactory.forStyle(Style.BOXED); }
    }
}
