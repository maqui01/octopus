/**
 * 
 */
package papasoft.octopus.message;


/**
 * @author maqui
 *
 */
public class ClientToServerMessage extends Message {
	
	private static final long serialVersionUID = -5489770751336750123L;
	private Integer moduleId;
	private Integer messageType;
	private Long userId;
	private String imei;
	private Long messageId = 0L;
	
	public ClientToServerMessage() {
		super();
	}
	
	/**
	 * @return the module
	 */
	public Integer getModuleId() {
		return moduleId;
	}
	/**
	 * @param module the module to set
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	/**
	 * @return the messageType
	 */
	public Integer getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return the username
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param username the username to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ModId=" + getModuleId() + " MsgType=" + getMessageType() + " User=" + getUserId() + " Fields=" + getBodySize();
	}

	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the messageId
	 */
	public Long getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
}
