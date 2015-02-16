/**
 * 
 */
package papasoft.octopus.module;


/**
 * @author maqui
 *
 */
public class DummyModule extends Module {

	private static final Integer DUMMY_MODULE_ID = 0;

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.Module#getModuleId()
	 */
	@Override
	public Integer getModuleId() {
		return DUMMY_MODULE_ID;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.Module#getLoggerName()
	 */
	@Override
	public String getLoggerName() {
		return "";
	}

}
