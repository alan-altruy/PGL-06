package be.ac.umons.g06.server;

import be.ac.umons.g06.server.model.ServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main Server Application
 */
@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
	/**
	 * Manager of the server
	 */
	@Autowired
	private ServerManager manager;
	/**
	 * Object of ConfigurableApplicationContext
	 */
	private static ConfigurableApplicationContext ctx;

	/**
	 * Main class of Server
	 * @param args
	 */
	public static void main(String[] args) {
		ctx = SpringApplication.run(ServerApplication.class, args);
//		int exitCode = SpringApplication.exit(ctx, () -> 0);
		//error code 0
//		System.exit(exitCode);
	}

	/**
	 * Run method of main app Server
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		manager.work();
	}

}
