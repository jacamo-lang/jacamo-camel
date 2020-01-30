package jacamoql;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jacamo.platform.DefaultPlatformImpl;
import jacamo.project.JaCaMoProject;

@SpringBootApplication
public class GraphqlPlatform extends DefaultPlatformImpl{
	private final static Logger logger = Logger.getLogger(GraphqlPlatform.class.getName());

	private static JaCaMoProject masProject;

	@Override
	public void init(String[] args) throws Exception{
		masProject = project;

		logger.setLevel(Level.FINE);

		logger.info("Starting platform and application.");
		SpringApplication.run(GraphqlPlatform.class, args);
	}

	@Override
	public void stop() {
		logger.info("Stopping platform");
		super.stop();
	}

	public static JaCaMoProject getProject() {
		return GraphqlPlatform.masProject;
	}
}