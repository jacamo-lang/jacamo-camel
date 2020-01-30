import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class ParseList implements Processor {
@SuppressWarnings({ "unchecked", "rawtypes" })
@Override
public void process(Exchange exchange) throws Exception {
		ArrayList<String> returnList = new ArrayList<String>();
		// LinkedCaseInsensitiveMap<String> body = (LinkedCaseInsensitiveMap<String>) exchange.getIn().getBody();
		ArrayList<LinkedCaseInsensitiveMap> body = (ArrayList<LinkedCaseInsensitiveMap>) exchange.getIn().getBody();

        for(LinkedCaseInsensitiveMap termo: body) {
        	ArrayList vals = new ArrayList(termo.values());
        	String result = "";
        	result += "value(";
        	result += vals.get(0)+", ";
        	result += vals.get(1)+", ";
        	result += vals.get(2);
        	result += ")";
        	returnList.add(result);
        }
        exchange.getIn().setBody(returnList);
    }
}
