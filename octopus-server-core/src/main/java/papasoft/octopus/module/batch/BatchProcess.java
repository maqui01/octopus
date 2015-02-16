/**
 * 
 */
package papasoft.octopus.module.batch;

import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.minterface.ModuleInterface;

/**
 * @author maqui
 *
 */
public abstract class BatchProcess implements Runnable {
	
	private String cronPattern;
	
	private ModuleInterface moduleInterface;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (init()) {
			LogManager.logInfo("Starting " + getProcessName(), getLoggerName());
			try {
				execute();
				LogManager.logInfo(getProcessName() + " ended", getLoggerName());
			} catch (Throwable th) {
				LogManager.logError(getProcessName() + " stopped with errors", th, getLoggerName());
			}
		}
	}
	
	protected abstract Boolean init();
	
	public abstract void execute() throws OctopusException;
	
	protected abstract String getLoggerName();
	
	public abstract String getProcessName();

	/**
	 * @return the cronPattern
	 */
	public String getCronPattern() {
		return cronPattern;
	}

	/**
	 * @param cronPattern the cronPattern to set
	 */
	public void setCronPattern(String cronPattern) {
		this.cronPattern = cronPattern;
	}

	/**
	 * @return the moduleInterface
	 */
	public ModuleInterface getModuleInterface() {
		return moduleInterface;
	}

	/**
	 * @param moduleInterface the moduleInterface to set
	 */
	public void setModuleInterface(ModuleInterface moduleInterface) {
		this.moduleInterface = moduleInterface;
	}

}
