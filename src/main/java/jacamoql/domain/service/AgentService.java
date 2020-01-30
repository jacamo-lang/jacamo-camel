package jacamoql.domain.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jacamo.infra.JaCaMoLauncher;
import jacamoql.GraphqlPlatform;
import jason.asSemantics.Agent;

@Service
public class AgentService {
    public Agent getAgentByName(String name) {
        return JaCaMoLauncher.getRunner().getAg(name).getTS().getAg();
    }

    public List<Agent> getAllAgents() {
        List<Agent> agentArray = new ArrayList<Agent>();
        for(String arch: JaCaMoLauncher.getRunner().getAgs().keySet()){
            agentArray.add(JaCaMoLauncher.getRunner().getAgs().get(arch).getTS().getAg());
        }
        return agentArray;
    }

    public List<String> getAgentFocus(String agentName) {
        // GraphqlPlatform.getProject().getAg(agentName)
        return null;
    }
}