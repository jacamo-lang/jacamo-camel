package jacamoql.domain.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import jacamo.infra.JaCaMoLauncher;
import jacamoql.domain.service.AgentService;
import jacamoql.graphql.agentschema.AgentType;
import jason.asSemantics.Agent;

@Component
public class AgentFacade {

    @Autowired
    AgentService agentService;

    public AgentType getAgent(String name) {
        if(name != null) {
            Agent agent = agentService.getAgentByName(name);
            String agName = agent.getTS().getUserAgArch().getAgName();
            AgentType retAgent = new AgentType();
            retAgent.setName(agName);
            return retAgent;
        }
        return null;
    }

    public List<AgentType> getAllAgents(String workspace) {
        List<AgentType> retArray = new ArrayList<AgentType>();

        for(String arch: JaCaMoLauncher.getRunner().getAgs().keySet()){
            AgentType agent = new AgentType();
            agent.setName(JaCaMoLauncher.getRunner().getAgs().get(arch).getAgName());
            retArray.add(agent);
        }
        return retArray;
    }

    public AgentType createAgent() {
        return null;
    }
}