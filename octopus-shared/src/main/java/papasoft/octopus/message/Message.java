/**
 * 
 */
package papasoft.octopus.message;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * @author maqui
 *
 */
public abstract class Message implements Serializable {

	private static final long serialVersionUID = 145769126284790296L;
	private HashMap<Integer, Serializable> body;
	
	public Message() {
		this.body = new HashMap<Integer, Serializable>();
	}

	/**
	 * @return the body
	 */
	protected HashMap<Integer, Serializable> getBody() {
		return body;
	}
	
	/**
	 * Puts data in the message at @param key
	 * @param key
	 * @param data
	 */
	public void putData(Integer key, Serializable data) {
		getBody().put(key, data);
	}
	
	/**
	 * Retrieves data at @param key 
	 * @param key
	 * @return
	 */
	public Serializable getData(Integer key) {
		return getBody().get(key);
	}
	
	/**
	 * Clears data at @param key
	 * @param key
	 */
	public void clearData(Integer key) {
		getBody().remove(key);
	}
	
	/**
	 * 
	 */
	public void resetMessage() {
		this.body = new HashMap<Integer, Serializable>();
	}
	
	/**
	 * 
	 * @return
	 */
	protected Integer getBodySize() {
		return this.body.size();
	}

	/**
	 * @return
	 */
	public String getBodyToString() {
		StringBuilder builder = new StringBuilder();
		String delimiter = " | ";
		if (this.getBody() != null && this.getBodySize() > 0) {
			String loopDelim = "";
			for (Integer key : this.getBody().keySet()) {
				builder.append(loopDelim);
				Object value = this.getData(key);
				builder.append(key);
				builder.append("=");
				builder.append( getStringRepresentation(value) );
				loopDelim = delimiter;
			}	
			return builder.toString();
		}
		return " ";
	}

	/**
	 * @param value
	 * @return
	 */
	private String getStringRepresentation(Object value) {
		if (value != null) {
			if (value.getClass().isAssignableFrom(Integer.class) || value.getClass().isAssignableFrom(Double.class) ||
					value.getClass().isAssignableFrom(String.class) || value.getClass().isAssignableFrom(Date.class)) {
				return value.toString();
			} else {
				return "Object";
			}
		}
		return "null";
	}
}
