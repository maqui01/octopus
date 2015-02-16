/**
 * 
 */
package papasoft.octopus.sales.module;

import papasoft.octopus.module.Module;

/**
 * @author maqui
 *
 */
public class SalesModule extends Module {
	
	private static final String LOGGER_NAME = "papasoft.octopus.salesModuleLogger";
	
	/**
	 * 
	 * @return
	 */
	public SalesInterface getSalesInterface() {
		return (SalesInterface) getModuleInterface();
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.Module#getLoggerName()
	 */
	@Override
	public String getLoggerName() {
		return LOGGER_NAME;
	}
}
