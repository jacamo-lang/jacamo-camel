package jacamoComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;

import jason.asSemantics.Message;

public class ArtifactEndpoint extends DefaultEndpoint {
	private final static Logger logger = Logger.getLogger(ArtifactEndpoint.class.getName());

	private String operation = null;
	private String args = null;
	private String property = null;
	private Boolean isSignal = false;
	private String workspace = "main";
	private String returns = null;

	public ArtifactEndpoint() {
	}

	public ArtifactEndpoint(String uri, ArtifactComponent component) {
		super(uri, component);
		logger.setLevel(Level.INFO);
	}

	public boolean isSingleton() {
		return false;
	}

	public Exchange createExchange() {
		Exchange result = new DefaultExchange(this); // gera um exchange com referencia ao JasonEndpoint
		return result;
	}

	public Exchange createExchange(Message jMessage) {
		logger.info("Generating exchange object from: " + jMessage);
		// DefaultMessage inMessage = new DefaultMessage
		Exchange result = new DefaultExchange(this);

//		Map<String, Object> headers = new HashMap<String, Object>();
//		headers.putIfAbsent("source", jMessage.getSender());
//		headers.putIfAbsent("performative", jMessage.getIlForce());
//		headers.putIfAbsent("msgId", jMessage.getMsgId());
//		headers.putIfAbsent("inReplyTo", jMessage.getInReplyTo());
//
//		result.getIn().setHeaders(headers);

		result.getIn().setBody(jMessage.getPropCont().toString());

		result.setProperty("source", jMessage.getSender());
		result.setProperty("performative", jMessage.getIlForce());
		result.setProperty("msgId", jMessage.getMsgId());
		result.setProperty("inReplyTo", jMessage.getInReplyTo());

		logger.info("Message's properties: " + result.getProperties());
		logger.info("Message's body      : " + result.getIn().getBody());
		return result;
	}

	public Exchange createExchange(ExchangePattern pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	public Exchange createExchange(Exchange exchange) {
		// TODO Auto-generated method stub
		return null;
	}

	public Producer createProducer() throws Exception {
		ArtifactProducer producer = new ArtifactProducer(this);
		return producer;
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new ArtifactConsumer(this, processor);
	}

	public PollingConsumer createPollingConsumer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getArgs() {
		if(args!=null)
			return args;
		else
			return "()";
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Boolean getIsSignal() {
		return isSignal;
	}

	public void setIsSignal(Boolean isSignal) {
		this.isSignal = isSignal;
	}

	public void setIsSignal(String isSignal) {
		if(isSignal.equals("true"))
			setIsSignal(true);
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getReturns() {
		if(returns!=null)
			return returns;
		else
			return "()";
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

}
