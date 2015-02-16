/**
 * 
 */
package papasoft.octopus.message;

/**
 * @author maqui
 *
 */
public class ServerToClientMessage extends Message {

	private static final long serialVersionUID = 8003710886639094163L;
	private String result;
	private Boolean moreData;
	private Integer messagesQuantity;
	
	public ServerToClientMessage() {
		super();
		this.result = MessageResult.RESULT_OK;
		this.moreData = false;
		this.messagesQuantity = 0;
	}
	
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the moreData
	 */
	public Boolean getMoreData() {
		return moreData;
	}
	/**
	 * @param moreData the moreData to set
	 */
	public void setMoreData(Boolean moreData) {
		this.moreData = moreData;
	}

	/**
	 * @return the messagesQuantity
	 */
	public Integer getMessagesQuantity() {
		return messagesQuantity;
	}

	/**
	 * @param messagesQuantity the messagesQuantity to set
	 */
	public void setMessagesQuantity(Integer messagesQuantity) {
		this.messagesQuantity = messagesQuantity;
	}
	
	
}
