/**
 * 
 */
package papasoft.octopus.config;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author maqui
 *
 */
public class ConfigurationManager {

	private static final String DEFAULT_SERVER_PORT = "4662";
	private static final String PARAM_NAME_SERVER_PORT = "server_port";
	
	private Properties properties;
	private Properties fixedValues;
	private HashMap<String, String> applicationParameters;
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getParameterAsString(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}
	
	/**
	 * Returns configured server port
	 * @return
	 */
	public Integer getServerPortValue() {
		return Integer.decode(this.properties.getProperty(PARAM_NAME_SERVER_PORT, DEFAULT_SERVER_PORT));
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the fixedValues
	 */
	public Properties getFixedValues() {
		return fixedValues;
	}

	/**
	 * @param fixedValues the fixedValues to set
	 */
	public void setFixedValues(Properties fixedValues) {
		this.fixedValues = fixedValues;
	}

	/**
	 * @return the applicationParameters
	 */
	public HashMap<String, String> getApplicationParameters() {
		return applicationParameters;
	}

	/**
	 * @param applicationParameters the applicationParameters to set
	 */
	public void setApplicationParameters(HashMap<String, String> applicationParameters) {
		this.applicationParameters = applicationParameters;
	}
}
