package jacamoComponent;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.SendProcessor;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.asSyntax.VarTerm;
import jason.asSyntax.parser.ParseException;

public class ArtifactConsumer extends DefaultConsumer {
	private final ArtifactEndpoint endpoint;

	private static ArrayList<String> artifactsNames = new ArrayList<String>();
	private static Map<String, ArtifactConsumer> consumers = new HashMap<String, ArtifactConsumer>();
	private final static Logger logger = Logger.getLogger(ArtifactConsumer.class.getName());

	private String artName;
	private String opName;
	private String[] args;
	private String argsString;
	private String[] returns;
	private String returnsString;

	public ArtifactConsumer(ArtifactEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
		this.setArtName(endpoint.getEndpointUri().split("\\?")[0].split("\\/")[2]);
		this.opName = endpoint.getOperation();
		this.argsString = endpoint.getArgs();
		if(!this.argsString.substring(1, this.argsString.length()-1).trim().isEmpty())
			this.args = this.argsString.substring(1, this.argsString.length()-1).trim().replace(" ", "").split(",");
		else
			this.args = new String[] {};
		this.returnsString = endpoint.getReturns();
		if(!this.returnsString.substring(1, this.returnsString.length()-1).trim().isEmpty())
			this.returns = this.returnsString.substring(1, this.returnsString.length()-1).trim().replace(" ", "").split(",");
		else
			this.returns = new String[] {};
		logger.setLevel(Level.INFO);
	}

	public void start() throws Exception {
		consumers.put(opName, this);
		artifactsNames.add(artName);
		super.start();
	}

	public void stop() throws Exception {
		consumers.remove(opName, this);
		artifactsNames.remove(artName);
		super.stop();

	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public static Map<String, ArtifactConsumer> getConsumers(){
		return ArtifactConsumer.consumers;
	}

	public void operate(CamelAgArch archInstance, ActionExec a) {
		Term[] arguments = a.getActionTerm().getTermsArray();

		String mod = (args.length>0 && returns.length>0)?", ":"";
		logger.info("Operation invoked: "+opName+" /"+(args.length + returns.length)+" - "+argsString+mod+returnsString);

		if(arguments.length != args.length + returns.length) {
			logger.warning("Expected number of arguments in the operation called: " + (args.length + returns.length) + ", got " + arguments.length);
			String receivedArgs = "";
			for(Term t : arguments) {
				receivedArgs += t.toString() + " ";
			}
			logger.warning("Received arguments: " + receivedArgs);
			return ;
		}

		Exchange exchange = new DefaultExchange(endpoint);

		for(int index = 0; index<args.length; index++) {
			exchange.setProperty(args[index], arguments[index].toString());
		}

		try {
			getProcessor().process(exchange);

			Unifier un = a.getIntention().peek().getUnif();
			a.setResult(true);
			for(int i=0; i<returns.length; i++) {
				Term returnTerm = null;
				String returnHeader = null;
				if(exchange.getIn().getHeader(returns[i]) != null)
					returnHeader = exchange.getIn().getHeader(returns[i]).toString();
				if(returnHeader == null) {
					a.setFailureReason(ASSyntax.createLiteral("unify_error", a.getActionTerm().getTerm(args.length + i)), "Exchange header"+returns[i]+" is null: ");
					a.setResult(false);
					break;
				}
				if(returnHeader.startsWith("[")){
					returnTerm = ListTermImpl.parseList(returnHeader);
				}else{
					returnTerm = Literal.parseLiteral(returnHeader);
				}
				logger.info("Unifying "+a.getActionTerm().getTerm(args.length + i)+" to "+returnHeader);
				if( !un.unifies(returnTerm, a.getActionTerm().getTerm(args.length + i)) ) {
					a.setFailureReason(ASSyntax.createLiteral("unify_error", a.getActionTerm().getTerm(args.length + i)), "Error unifying terms: " + a.getActionTerm().getTerm(args.length + i).toString() + " with " + returnHeader);
					a.setResult(false);
					break;
				}
			}

			archInstance.actionExecuted(a);
			logger.info("Processed successfully");
		} catch (Exception e) {
			logger.warning("Processed badly");
			a.setResult(false);
			a.setFailureReason(a.getActionTerm().copy(), "The exchange couldn't be processed correctly.");
			archInstance.actionExecuted(a);
			e.printStackTrace();
		}

	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getArtName() {
		return artName;
	}

	public void setArtName(String artName) {
		this.artName = artName;
	}

	public String[] getReturns() {
		return returns;
	}

	public void setReturns(String[] returns) {
		this.returns = returns;
	}

	public String getReturnsString() {
		return returnsString;
	}

	public void setReturnsString(String returnsString) {
		this.returnsString = returnsString;
	}

	public static ArrayList<String> getArtifactsNames() {
		return artifactsNames;
	}

}
