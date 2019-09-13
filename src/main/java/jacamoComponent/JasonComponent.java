package jacamoComponent;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class JasonComponent extends DefaultComponent {

	public void setCamelContext(CamelContext camelContext) {
		super.setCamelContext(camelContext);

	}

	public CamelContext getCamelContext() {
		return super.getCamelContext();
	}

	public Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		JasonEndpoint endpoint = new JasonEndpoint(uri, this);
		setProperties(endpoint, parameters); // call setters
		return endpoint;
	}

	public boolean useRawUri() {
		return false;
	}

}
