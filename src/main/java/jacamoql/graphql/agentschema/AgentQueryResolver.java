package jacamoql.graphql.agentschema;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jacamoql.domain.facade.AgentFacade;

@Component
public class AgentQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AgentFacade agentFacade;

    public AgentType agent(String name) {
        return agentFacade.getAgent(name);
    }

    public List<AgentType> agents(String workspace) {
        return agentFacade.getAllAgents(workspace);
    }
}