package jacamoComponent;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

import jason.asSemantics.Message;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.ObjectTermImpl;
import jason.asSyntax.Term;
import jason.asSyntax.VarTerm;

public class JasonConsumer extends DefaultConsumer {
	private final JasonEndpoint endpoint;

	private static HashMap<String, JasonConsumer> consumers = new HashMap<String, JasonConsumer>();
	private final static Logger logger = Logger.getLogger(JasonConsumer.class.getName());

	private String address; // "agente" que o Jason vhe

	public JasonConsumer(JasonEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
		this.address = endpoint.getEndpointUri().split("\\?")[0].split("\\/")[2];
		consumers.put(address, this);
		logger.setLevel(Level.FINE);
	}

	public void start() throws Exception {
		consumers.put(address, this);
		super.start();
	}

	public void stop() throws Exception {
		consumers.remove(address, this);
		super.stop();

	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public static HashMap<String, JasonConsumer> getConsumers(){
		return JasonConsumer.consumers;
	}

	public void receiveMessage(Message m) {
		logger.info("Message received: "+m);

		if(endpoint.getSource() != null && !endpoint.getSource().equals(m.getSender())) {
			System.err.println("This route is not to receive this source: " + m.getSender() + ", only: " + endpoint.getSource());
			return ;
		}
		if(endpoint.getPerformative() != null && !endpoint.getPerformative().equals(m.getIlForce())) {
			System.err.println("This route is not to receive this performative: " + m.getIlForce() + ", only: " + endpoint.getPerformative());
			return ;
		}


		if(endpoint.getInReplyTo() != null && !endpoint.getInReplyTo().equals(m.getInReplyTo())) {
			System.err.println("This route is not to receive this reply: " + m.getInReplyTo() + ", only: " + endpoint.getInReplyTo());
			return ;
		}
		// YOU CAN'T DEFINE A MSGID IN JASON APPARENTLY
		// if(endpoint.getMsgId() != null && !endpoint.getMsgId().equals(m.getMsgId())) {
		// 	System.err.println("This route is not to receive this id: " + m.getMsgId() + ", only: " + endpoint.getMsgId());
		// 	return ;
		// }

		Exchange exchange = endpoint.createExchange(m);

		if(endpoint.getContent() != null) {
			Unifier u = new Unifier();

			// Needs to be applicable to atom and lists as well
			// from list to (atom, literal, list)
			Term contentLit = null;

			if(m.getPropCont() instanceof ListTerm) {
				contentLit = ListTermImpl.parseList(endpoint.getContent());
			}else {
				contentLit = Literal.parseLiteral(endpoint.getContent());
			}

			logger.info("Unifying "+contentLit.toString()+" with "+m.getPropCont().toString());
			if(u.unifies((Term)m.getPropCont(), contentLit)) {
				for(VarTerm t: u) {
					exchange.setProperty(t.toString(), u.get(t));
				}
			}else {
				logger.warning("Unification failed.");
				return;
			}
		 }
//		logger.info("Exchange created for processing: "+exchange);
		try {
			getProcessor().process(exchange);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
