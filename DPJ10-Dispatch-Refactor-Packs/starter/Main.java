import dispatch.Composition;
import dispatch.domain.Request;
import dispatch.domain.Request.Channel;
import dispatch.domain.Request.Priority;
import dispatch.legacy.LegacyDesk;
import dispatch.ports.DeskApi;
import dispatch.ports.NoticeSink;
public class Main {
    public static void main(String[] args) {
        if(args.length>1 || (args.length==1 && !args[0].equals("base")))throw new IllegalArgumentException("use base or no arguments");
        NoticeSink sink=message->System.out.println("sent:"+message.id());
        DeskApi desk=args.length==1?new LegacyDesk(sink):Composition.create(sink);
        Request first=new Request("a","Hi",Channel.EMAIL,Priority.NORMAL,2);
        System.out.println(desk.preview(first).rendered());
        desk.send(first);
        System.out.println(desk.find("a").orElseThrow().cents());
        desk.send(new Request("b","Ready",Channel.SMS,Priority.URGENT,3));
        System.out.println(desk.all().stream().map(message->message.id()).toList());
    }
}
