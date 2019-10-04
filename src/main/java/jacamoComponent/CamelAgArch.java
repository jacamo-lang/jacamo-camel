package jacamoComponent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import jaca.CAgentArch;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Event;
import jason.asSemantics.Message;
import jason.asSyntax.Literal;
import jason.ReceiverNotFoundException;
import jason.architecture.AgArch;

public class CamelAgArch extends AgArch {

	/*   CARTAGO COMPONENT   */

	private List<String> focusedArtifacts;

	@Override
	public void init() throws Exception {
		focusedArtifacts = new ArrayList<String>();
		JacamoCamel.insertArchList(this);
	}

	@Override
	public Collection<Literal> perceive(){
		Collection<Literal> perceptions = super.perceive();
		if(perceptions == null)
			perceptions = new ArrayList<Literal>();

		for(String artifact: focusedArtifacts) {
			Collection<Literal> percepts = ArtifactProducer.getObservableProperties().get(artifact);
			if(percepts != null)
			perceptions.addAll(percepts);
		}

		return perceptions;
	}

	@Override
	public void act(ActionExec a)  {
		String functor = a.getActionTerm().getFunctor();
		if(functor.equals("focus") && a.getActionTerm().getTerm(0)!=null) {
			if(!ArtifactConsumer.getArtifactsNames().contains(a.getActionTerm().getTerm(0).toString())){
				super.act(a);
				return;
			}
			focusedArtifacts.add(a.getActionTerm().getTerm(0).toString());
			a.setResult(true);
			actionExecuted(a);
		}else if(ArtifactConsumer.getConsumers().containsKey(functor)) {
			ArtifactConsumer consumer = ArtifactConsumer.getConsumers().get(functor);

			String failureReason = null;

			if(!hasFocusOn(consumer.getArtName())) {
				failureReason = "Agent must focus the artifact "+consumer.getArtName();
			}

			a.setResult(failureReason == null);
			if(a.getResult() == true) {
				consumer.operate(this, a); // wait for the op to finish
			}else {
				a.setFailureReason(a.getActionTerm().copy(), failureReason);
				actionExecuted(a);
			}
		}else {
			System.out.println("No operation registered: "+a.getActionTerm().getFunctor());
			super.act(a);
		}
	}

	public void signalize(Event event) {
		this.getTS().updateEvents(event);
	}

	public boolean hasFocusOn(String artName) {
		return focusedArtifacts.contains(artName);
	}

	protected CAgentArch getCartagoArch() {
    AgArch arch = getTS().getUserAgArch().getFirstAgArch();
    while (arch != null) {
      if (arch instanceof CAgentArch) {
        return (CAgentArch)arch;
      }
      arch = arch.getNextAgArch();
    }
    return null;
  }


		/*   JASON COMPONENT   */
		@Override
		public void sendMsg(Message m) throws Exception {
			try {
				super.sendMsg(m);
				return;
			} catch (ReceiverNotFoundException e) {
		  	try {
					if (JasonConsumer.getConsumers().containsKey(m.getReceiver())) {
						JasonConsumer.getConsumers().get(m.getReceiver()).receiveMessage(m);
		        return;
					}
		    } catch (Exception e2) { }
				throw e;
			}
		}

}
