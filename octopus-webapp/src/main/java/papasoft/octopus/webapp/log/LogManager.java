/**
 * 
 */
package papasoft.octopus.webapp.log;

import org.apache.log4j.Logger;

/**
 * @author maqui
 *
 */
public class LogManager {

	private static final String DEFAULT_LOGGER_NAME = "papasoft.octopus.defaultLogger";
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Logger getLogger(String name) {
		return Logger.getLogger(name);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 * @param loggerName defined in configuration file
	 */
	public static void logDebug(Object message, Throwable th, String loggerName) {
		Logger.getLogger(loggerName).debug(message, th);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param loggerName defined in configuration file
	 */
	public static void logDebug(Object message, String loggerName) {
		Logger.getLogger(loggerName).debug(message);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 */
	public static void logDebug(Object message, Throwable th) {
		logDebug(message, th, DEFAULT_LOGGER_NAME);
	}
	
	/**
	 * 
	 * @param message to log
	 */
	public static void logDebug(Object message) {
		logDebug(message, DEFAULT_LOGGER_NAME);
	}

	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 * @param loggerName defined in configuration file
	 */
	public static void logError(Object message, Throwable th, String loggerName) {
		Logger.getLogger(loggerName).error(message, th);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param loggerName defined in configuration file
	 */
	public static void logError(Object message, String loggerName) {
		Logger.getLogger(loggerName).error(message);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 */
	public static void logError(Object message, Throwable th) {
		logError(message, th, DEFAULT_LOGGER_NAME);
	}
	
	/**
	 * 
	 * @param message to log
	 */
	public static void logError(Object message) {
		logError(message, DEFAULT_LOGGER_NAME);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 * @param loggerName defined in configuration file
	 */
	public static void logInfo(Object message, Throwable th, String loggerName) {
		Logger.getLogger(loggerName).info(message, th);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param loggerName defined in configuration file
	 */
	public static void logInfo(Object message, String loggerName) {
		Logger.getLogger(loggerName).info(message);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 */
	public static void logInfo(Object message, Throwable th) {
		logInfo(message, th, DEFAULT_LOGGER_NAME);
	}
	
	/**
	 * 
	 * @param message to log
	 */
	public static void logInfo(Object message) {
		logInfo(message, DEFAULT_LOGGER_NAME);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 * @param loggerName defined in configuration file
	 */
	public static void logWarn(Object message, Throwable th, String loggerName) {
		Logger.getLogger(loggerName).warn(message, th);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param loggerName defined in configuration file
	 */
	public static void logWarn(Object message, String loggerName) {
		Logger.getLogger(loggerName).warn(message);
	}
	
	/**
	 * 
	 * @param message to log
	 * @param Throwable to log
	 */
	public static void logWarn(Object message, Throwable th) {
		logWarn(message, th, DEFAULT_LOGGER_NAME);
	}
	
	/**
	 * 
	 * @param message to log
	 */
	public static void logWarn(Object message) {
		logWarn(message, DEFAULT_LOGGER_NAME);
	}
}
