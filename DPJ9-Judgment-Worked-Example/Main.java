import java.util.Locale;
import java.util.Objects;
import java.util.function.UnaryOperator;
public class Main {
    public static String line(String value){
        Objects.requireNonNull(value,"text");
        if(value.indexOf('\r')>=0 || value.indexOf('\n')>=0)throw new IllegalArgumentException("single-line text");
        return value;
    }
    public static final class Legacy {
        public static String banner(String text){return "NOTICE: "+line(text).trim().toUpperCase(Locale.ROOT);}
        public static String email(String text){return "NOTICE: "+line(text).trim().toUpperCase(Locale.ROOT);}
    }
    public static final class Direct {
        private static String format(String text){return "NOTICE: "+line(text).trim().toUpperCase(Locale.ROOT);}
        public static String banner(String text){return format(text);}
        public static String email(String text){return format(text);}
    }
    public static final class Pluggable {
        private final UnaryOperator<String> formatter;
        public Pluggable(UnaryOperator<String> formatter){this.formatter=Objects.requireNonNull(formatter,"formatter");}
        public String banner(String text){return line(formatter.apply(line(text)));}
        public String email(String text){return line(formatter.apply(line(text)));}
    }
    public static void main(String[] args){
        Pluggable injected=new Pluggable(text->"NOTICE: "+text.trim().toUpperCase(Locale.ROOT));
        System.out.println(Legacy.banner(" hello "));
        System.out.println(Direct.banner(" hello "));
        System.out.println(injected.banner(" hello "));
    }
}
