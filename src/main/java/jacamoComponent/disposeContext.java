package jacamoComponent;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

public class disposeContext extends DefaultInternalAction {
	@Override
	public Object execute( TransitionSystem ts, Unifier un, Term[] args ) throws Exception {
		StringTerm name = (StringTerm)args[0];
		try {
			JacamoCamel.disposeContext(name.getString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
