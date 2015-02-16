/**
 * 
 */
package papasoft.octopus.domain;

import java.io.Serializable;

/**
 * @author maqui
 *
 */
public class SecretField implements Serializable {

	private static final long serialVersionUID = 8885307045612931934L;
	private String field;
	
	public SecretField(String field) {
		this.field = field;
	}

	/**
	 * @return the password
	 */
	public String getSecretField() {
		return field;
	}
	
}
