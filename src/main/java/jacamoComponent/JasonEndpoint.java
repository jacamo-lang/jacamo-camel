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

public class JasonEndpoint extends DefaultEndpoint {
	private final static Logger logger = Logger.getLogger(JasonEndpoint.class.getName());

	private String performative  = null;
	private String source   = null;
	private String receiver = null;
	private String content = null;
	private String msgId    = null;
	private String inReplyTo = null;

	public JasonEndpoint() {
	}

	public JasonEndpoint(String uri, JasonComponent component) {
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
//		logger.info("Generating exchange object from: " + jMessage);
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

//		logger.info("Message's properties: " + result.getProperties());
//		logger.info("Message's body      : " + result.getIn().getBody());
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
		JasonProducer producer = new JasonProducer(this);
		return producer;
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new JasonConsumer(this, processor);
	}

	public PollingConsumer createPollingConsumer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPerformative() {
		return performative;
	}
	public String getPerformative(String Default) {
		if(performative == null) {
			return Default;
		}
		return performative;
	}
	public void setPerformative(String performative) {
		this.performative = performative;
	}
	public String getSource() {
		return source;
	}
	public String getSource(String Default) {
		if(source == null) {
			return Default;
		}
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return this.content;
	}
	public void setContent(String c) {
		this.content = c;
	}

	public String getMsgId() {
		return this.msgId;
	}
	public void setMsgId(String id) {
		this.msgId = id;
	}

	public String getInReplyTo() {
		return this.inReplyTo;
	}
	public void setInReplyTo(String irt) {
		this.inReplyTo = irt;
	}

}
