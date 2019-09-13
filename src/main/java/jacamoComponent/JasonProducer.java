package jacamoComponent;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultProducer;

import jacamo.infra.JaCaMoLauncher;
import jason.infra.centralised.CentralisedAgArch;



public class JasonProducer extends DefaultProducer {
	private final static Logger logger = Logger.getLogger(JasonProducer.class.getName());
	private JasonEndpoint endpoint;

	private static int idCounter = 1;
	private static ArrayList<String> customUsedIds = new ArrayList<String>();

	public JasonProducer(JasonEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
		logger.setLevel(Level.INFO);
	}

	public void process(Exchange exchange) throws Exception {
		String performative = endpoint.getPerformative();
		if(performative == null) {
			performative = (String) exchange.getProperty("performative", "tell"); // valor default
		}

		String source = endpoint.getSource("camel");
		if(source == null) {
			source = (String) exchange.getProperty("source"); // valor default
		}

		String msgId = endpoint.getMsgId();
		if (msgId == null) {
			msgId = (String) exchange.getProperty("msgId");
		}
		if(msgId == null) {
			msgId = "<auto>"+Integer.toString(idCounter);
			idCounter += 1;
		}else {
			if(customUsedIds.contains(msgId))
				logger.info(msgId+" was already used, using it anyway.");
			customUsedIds.add(msgId);
		}


		String irt = endpoint.getInReplyTo();
		if(irt == null) {
			irt = (String) exchange.getProperty("inReplyTo");
		}

		String agName = null;
		try {
			agName = endpoint.getEndpointUri().split("\\?")[0].split("\\/")[2];
		}catch (Exception e){
			logger.warning(e.toString());
		}

		jason.asSemantics.Message jasonMessage = null;

		String content = endpoint.getContent();
		if(content == null) {
			content = exchange.getIn().getBody().toString();

//			if(exchange.getIn().getBody() instanceof List) {
//				System.out.println("eh lista");
//				Set<Term> l = new HashSet<Term>((List) exchange.getIn().getBody());
//				jasonMessage = new jason.asSemantics.Message(performative, source, agName, l, msgId);
//			}else {
//				content = exchange.getIn().getBody().toString();
//				jasonMessage = new jason.asSemantics.Message(performative, source, agName, content, msgId);
//			}
		}
		jasonMessage = new jason.asSemantics.Message(performative, source, agName, content, msgId);


//		logger.info("Message id "+msgId+", from "+source+", to "+performative+" "+agName+" "+content);
		jasonMessage.setInReplyTo(irt);

		logger.info("Jason message: " + jasonMessage);
//		logger.info("In message "+exchange.getIn().getHeaders());
//		logger.info("In message "+exchange.getIn().getBody());
//		logger.info("Out message "+exchange.getOut().getHeaders());
//		logger.info("Out message "+exchange.getOut().getBody());

		CentralisedAgArch agent = null;
		try {
			agent = JaCaMoLauncher.getRunner().getAg(agName); // acha o agente
		}catch (Exception e) {
			logger.warning(e.toString());
		}

    	if (agent != null) {
    		agent.receiveMsg(jasonMessage); // entrega mensagem para mb do agente
    		logger.info("Agent received");
    	}else {
    		logger.warning("Agent " + agName + " not found.");
    	}
	}

	public void start() throws Exception { }

	public void stop() throws Exception { }

	public boolean isSingleton() { return false; }

	public Endpoint getEndpoint() { return endpoint; }

	public Exchange createExchange() { return null; }

	public Exchange createExchange(ExchangePattern pattern) { return null; }

	public Exchange createExchange(Exchange exchange) { return null; }

}
