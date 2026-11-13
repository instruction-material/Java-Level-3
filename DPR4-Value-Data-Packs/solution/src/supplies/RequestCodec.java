package supplies;

import java.util.ArrayList;
import java.util.StringJoiner;

/** Interpret and produce the old transport shape at this boundary. */
public final class RequestCodec {
    private RequestCodec() {}
    public static SupplyRequest decode(String row) {
        try {
            if (row == null) throw new IllegalArgumentException("row");
            String[] fields = row.split("\\|", -1);
            if (fields.length != 3 || !fields[1].matches("[012]"))
                throw new IllegalArgumentException("row");
            String[] entries = fields[2].isEmpty() ? new String[0] : fields[2].split(";", -1);
            ArrayList<RequestLine> lines = new ArrayList<>();
            for (String entry : entries) {
                String[] pair = entry.split(":", -1);
                if (pair.length != 2 || !pair[1].matches("[1-9][0-9]{0,2}"))
                    throw new IllegalArgumentException("row");
                lines.add(new RequestLine(new ItemCode(pair[0]), Integer.parseInt(pair[1])));
            }
            return SupplyRequest.fromLines(fields[0], ServiceLevel.fromCode(Integer.parseInt(fields[1])), lines);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("row");
        }
    }
    public static String encode(SupplyRequest request) {
        if (request == null) throw new IllegalArgumentException("request");
        StringJoiner body = new StringJoiner(";");
        for (RequestLine line : request.lines())
            body.add(line.code().value() + ":" + line.units());
        return request.destination() + "|" + request.serviceCode() + "|" + body;
    }
}
