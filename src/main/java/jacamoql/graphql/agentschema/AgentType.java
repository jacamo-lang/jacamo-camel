package jacamoql.graphql.agentschema;

import java.util.ArrayList;
import java.util.List;

public class AgentType {
    private String name;
    private List<String> focus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFocus() {
        if( focus == null ) { focus = new ArrayList<String>(); }
        return focus;
    }

    public void setFocus(List<String> focus) {
        this.focus = focus;
    }
}