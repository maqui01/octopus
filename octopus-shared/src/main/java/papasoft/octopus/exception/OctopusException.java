/**
 * 
 */
package papasoft.octopus.exception;

import papasoft.octopus.message.MessageResult;


/**
 * @author maqui
 *
 */
public class OctopusException extends Exception {

	private static final long serialVersionUID = 7685351453762148354L;
	
	private String errorCode;

	/**
	 * 
	 */
	public OctopusException() {}

	/**
	 * @param message
	 */
	public OctopusException(String message) {
		super(message);
		this.errorCode = MessageResult.GENERIC_SERVER_ERROR;
	}
	
	/**
	 * @param message
	 */
	public OctopusException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	/**
	 * @param message
	 */
	public OctopusException(String message, Throwable th, String errorCode) {
		super(message, th);
		this.errorCode = errorCode;
	}

	/**
	 * @param th
	 */
	public OctopusException(Throwable th) {
		super(th);
	}

	/**
	 * @param message
	 * @param th
	 */
	public OctopusException(String message, Throwable th) {
		super(message, th);
	}

	/**
	 * @param ex
	 * @param serverDatabaseError
	 */
	public OctopusException(Throwable ex, String errorCode) {
		super(ex);
		this.errorCode = errorCode;
	}

	/**
	 * @return the data
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param data the data to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
