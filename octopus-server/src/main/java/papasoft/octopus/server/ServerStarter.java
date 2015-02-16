/**
 * 
 */
package papasoft.octopus.server;

import it.sauronsoftware.cron4j.Scheduler;

import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import papasoft.octopus.context.OctopusContext;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.module.Module;
import papasoft.octopus.module.batch.BatchProcess;
import papasoft.octopus.server.batch.ServerBatchProcess;
import papasoft.octopus.server.handler.TransactionHandler;

/**
 * @author maqui
 *
 */
public class ServerStarter {
	
	public static final String SERVER_LOGGER_NAME = "papasoft.octopus.defaultLogger";
	private Integer serverPort;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerStarter server = new ServerStarter();
		server.start();
	}

	/**
	 * 
	 */
	private void start() {
		initializeServer();
		try {
			startServer();
		} catch (OctopusException e) {
			LogManager.logError("Fatal error during server runtime. Server STOPPED", e, SERVER_LOGGER_NAME);
		}
	}

	private void startServer() throws OctopusException {
		try {
			IServer server = new Server(this.serverPort, new TransactionHandler());
			server.run();
		} catch (Exception e) {
			LogManager.logError("Error running server", e, SERVER_LOGGER_NAME);
			throw new OctopusException("Error running server", e);
		}
	}

	private void initializeServer() {
		LogManager.logInfo("Server starting");
		try {
			this.serverPort = OctopusContext.getCtx().getConfigurationManager().getServerPortValue();
			startBatchProcesses();
		} catch (Exception e) {
			LogManager.logError("Error starting server", e, SERVER_LOGGER_NAME);
			LogManager.logInfo("Server failed to start", SERVER_LOGGER_NAME);
			return;
		}
		
		LogManager.logInfo("Server started", SERVER_LOGGER_NAME);
	}

	/**
	 * 
	 */
	private void startBatchProcesses() {
		Scheduler scheduler = new Scheduler();
		for ( ServerBatchProcess process : OctopusContext.getCtx().getBatchProcesses() ) {
			scheduler.schedule(process.getCronPattern(), process);
		}
		for (Module module : OctopusContext.getCtx().getModules()) {
			if (module.getBatchProcesses() != null) {
				for (BatchProcess process : module.getBatchProcesses()) {
					scheduler.schedule(process.getCronPattern(), process);
				}
			}
		}
		scheduler.start();
	}

}
