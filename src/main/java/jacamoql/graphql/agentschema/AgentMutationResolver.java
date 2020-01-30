package jacamoql.graphql.agentschema;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jacamoql.domain.facade.AgentFacade;

@Component
public class AgentMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private AgentFacade agentFacade;

    public AgentType createAgent(String name) {
        return null;
    }
}