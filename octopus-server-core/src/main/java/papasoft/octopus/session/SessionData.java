/**
 * 
 */
package papasoft.octopus.session;

import javax.crypto.SecretKey;

/**
 * @author maqui
 *
 */
public class SessionData {
	
	private SecretKey secretKey;
	private long lastActivity;
	private Integer id;
	private Integer sessionTimeout;
	
	public SessionData(Integer id, Integer sessionTimeout) {
		this.lastActivity = System.currentTimeMillis();
		this.id = id;
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * @param sessionTimeout
	 * @return
	 */
	public boolean isExpired() {
		if (System.currentTimeMillis() - this.lastActivity - this.sessionTimeout * 60000 < 0) {
			return false;
		}
		return true;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the secretKey
	 */
	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(SecretKey secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * 
	 */
	public void update() {
		this.lastActivity = System.currentTimeMillis();
	}
	
	
}
