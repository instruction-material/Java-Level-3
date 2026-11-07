import java.util.List;
import boundary.OrderDraft;
import boundary.PriceAdapter;
import boundary.LegacyPrice;
import boundary.QuoteFacade;

public class Main {
    public static void main(String[] args) {
        OrderDraft draft = new OrderDraft("Class order", List.of(new OrderDraft.Line("book", 6)));
        QuoteFacade facade = new QuoteFacade(new PriceAdapter(new LegacyPrice.Standard()));
        System.out.print(facade.quote(draft).render());
    }
}
