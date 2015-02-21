package papasoft.octopus.webapp.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import papasoft.octopus.server.starter.ServerStarter;

/**
 * 
 * @author maqui
 *
 */
public class ServerContextListener implements ServletContextListener {

	private ServerStarter starter;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//NOTHING
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		starter = new ServerStarter();
		starter.start();
	}

}
