package jacamoComponent;

import jason.*;
import jason.asSyntax.*;
import jason.asSemantics.*;

public class createContext extends DefaultInternalAction {
	@Override
	public Object execute( TransitionSystem ts, Unifier un, Term[] args ) throws Exception {
		StringTerm name = (StringTerm)args[0];
		try {
			JacamoCamel.createContext(name.getString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
