package jacamoComponent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultProducer;

import jason.asSemantics.Event;
import jason.asSemantics.Intention;
import jason.asSyntax.Literal;
import jason.asSyntax.Trigger;
import jason.asSyntax.Trigger.TEOperator;
import jason.asSyntax.Trigger.TEType;

public class ArtifactProducer extends DefaultProducer {
	private final static Logger logger = Logger.getLogger(ArtifactProducer.class.getName());
	private ArtifactEndpoint endpoint;
	private String artName;

	// list containing obsProperties mapped with artifact name
	public static Map<String, Collection<Literal>> observableProperties = new HashMap<String, Collection<Literal>>();
	public static Map<String, Collection<Literal>> signals = new HashMap<String, Collection<Literal>>();

	public ArtifactProducer(ArtifactEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
		artName = endpoint.getEndpointUri().split("\\?")[0].split("\\/")[2];
		if(!endpoint.getIsSignal())
			ArtifactProducer.observableProperties.putIfAbsent(artName, new ArrayList<Literal>());

		logger.setLevel(Level.FINE);
	}

	public void process(Exchange exchange) throws Exception {

		String content = null;
		if(endpoint.getProperty() != null)
			content = endpoint.getProperty();
		else
			content = exchange.getIn().getBody().toString();

		Literal property = Literal.parseLiteral(content);

		property.addAnnot(Literal.parseLiteral("artifact_name("+artName+")"));
		property.addAnnot(Literal.parseLiteral("percept_type(obs_prop)"));
		String updateMessage = null;
		if(!endpoint.getIsSignal()) {
			updateMessage = "Observable property added: "+property;
			for (Literal s: ArtifactProducer.observableProperties.get(artName)){
				if(s.getFunctor().equals(property.getFunctor()) && s.getArity() == property.getArity()) {
					ArtifactProducer.observableProperties.get(artName).remove(s);
					updateMessage = "Observable property updated: "+property;
					break;
				}
			}
			ArtifactProducer.observableProperties.get(artName).add(property);
			for(CamelAgArch arch: JacamoCamel.getArchList()) {
				if(arch.hasFocusOn(artName)){
					arch.wake();
				}
			}
		} else {
			Event signal = new Event(new Trigger(TEOperator.add, TEType.belief, property), new Intention());
			updateMessage = "New signal: "+signal.getTrigger();
			for(CamelAgArch arch: JacamoCamel.getArchList()) {
				if(arch.hasFocusOn(artName)){
					arch.getTS().updateEvents(signal);
					arch.wake();
				}
			}
		}
		logger.info(updateMessage);
	}

	public static Map<String, Collection<Literal>> getObservableProperties(){
		return ArtifactProducer.observableProperties;
	}

	public static Map<String, Collection<Literal>> getSignals(){
		return ArtifactProducer.signals;
	}

	public static void clearSignals(String workspace) {
		ArtifactProducer.signals.get(workspace).clear();
	}

	public void start() throws Exception { }

	public void stop() throws Exception { }

	public boolean isSingleton() { return false; }

	public Endpoint getEndpoint() {	return endpoint; }

	public Exchange createExchange() { return null;	}

	public Exchange createExchange(ExchangePattern pattern) { return null; }

	public Exchange createExchange(Exchange exchange) { return null; }

}
