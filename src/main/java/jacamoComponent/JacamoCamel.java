package jacamoComponent;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ExplicitCamelContextNameStrategy;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.RoutesDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import jacamo.project.JaCaMoProject;
import jacamo.platform.DefaultPlatformImpl;

import jason.infra.centralised.BaseCentralisedMAS;
import jason.mas2j.AgentParameters;
import jason.mas2j.ClassParameters;


public class JacamoCamel extends DefaultPlatformImpl{
	private final static Logger logger = Logger.getLogger(JacamoCamel.class.getName());

	private static JaCaMoProject masProject;
	private static List<CamelAgArch> archList = new ArrayList<CamelAgArch>();
	private static List<CamelContext> contexts = new ArrayList<CamelContext>();

	@Override
	public void init(String[] args) throws Exception{
		masProject = project;

		logger.setLevel(Level.FINE);
		List<AgentParameters> lags = new ArrayList<AgentParameters>();

		BaseCentralisedMAS.getRunner().getRuntimeServices().registerDefaultAgArch(CamelAgArch.class.getName());

		if(args.length > 0) {
			for(String fileName: args) {
				createContext(fileName);
			}
		}
	}

	@Override
	public void stop() {
		try {
			for(CamelContext context: contexts)
				context.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.stop();
	}

	public static void createContext(String fileName) throws Exception{
		String contextFile = null;
		if(fileName.contains(" --")) {
			contextFile = fileName.split(" --")[1];
			fileName = fileName.split(" --")[0];
		}

		if(fileName.endsWith(".xml")) {
			try {
				logger.info("Loading route from: " + fileName);
				JndiRegistry registry=null;

				if (contextFile != null) {
					try {
						ApplicationContext applicationContext = new FileSystemXmlApplicationContext(contextFile);
						logger.info("Loading context configurations from: " + contextFile);
						String[] beanNames=applicationContext.getBeanDefinitionNames();
						if (beanNames != null) {
							Map<String,String> enviroment= new HashMap<String,String>();
							enviroment.put("java.naming.factory.initial", "org.apache.camel.util.jndi.CamelInitialContextFactory");
							registry= new JndiRegistry(enviroment);
							for (String name : beanNames) {
								registry.bind(name,applicationContext.getBean(name));
							}
						}
					}catch (Exception e){
						logger.warning("No " + contextFile + " in " + System.getProperty("user.dir"));
						logger.warning(e.toString());
					}
				}

				CamelContext context = null;
				if(registry != null) {
					context = new DefaultCamelContext(registry);
				}else {
					context = new DefaultCamelContext();
				}

				InputStream is = new FileInputStream(fileName);
				RoutesDefinition routes = context.loadRoutesDefinition(is);
				context.addRouteDefinitions(routes.getRoutes());

				context.setNameStrategy(new ExplicitCamelContextNameStrategy(fileName));

				contexts.add(context);
				context.start();
				logger.info("Route successfully loaded and context successfully created.");
			}catch (IllegalArgumentException e){
				logger.warning("No " + fileName + " in " + System.getProperty("user.dir"));
				logger.warning(e.toString());
			}
		}else {
			logger.warning("Invalid file format. Only .xml supported");
		}
	}

	public static void disposeContext(String contextName) {
		for(CamelContext context: contexts) {
			if(context.getName().equals(contextName)) {
				try {
					contexts.remove(context);
					context.stop();
					logger.info("Context from file "+contextName+" was disposed.");
					return;
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		throw new NoSuchElementException();
	}

	public static JaCaMoProject getProject() {
		return JacamoCamel.masProject;
	}

	public static List<CamelAgArch> getArchList() {
		return JacamoCamel.archList;
	}

	public static void insertArchList(CamelAgArch arch) {
		JacamoCamel.archList.add(arch);
	}
}
