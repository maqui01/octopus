/**
 * 
 */
package papasoft.octopus.server.handler;

import java.util.HashMap;
import java.util.List;

import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class ResponseCacheManager {
	
	private static ResponseCacheManager instance = null;
	
	public static ResponseCacheManager getInstance() {
		if (instance == null) {
			instance = new ResponseCacheManager();
		}
		return instance;
	}

	private HashMap<Long, ResponseCacheEntry> cache = new HashMap<Long, ResponseCacheEntry>();
	
	/**
	 * 
	 * @param userId
	 * @param messageType
	 * @param messageId
	 * @param responseMessages
	 */
	public void cacheResponse(Long userId, Integer messageType, Long messageId, List<ServerToClientMessage> responseMessages) {
		if (messageId != null && messageId != 0L) {
			ResponseCacheEntry cacheEntry = new ResponseCacheEntry();
			cacheEntry.setMessageType(messageType);
			cacheEntry.setMessageId(messageId);
			cacheEntry.setResponseMessages(responseMessages);
			cache.put(userId, cacheEntry);
		}
	}
	
	/**
	 * 
	 * @param userId
	 * @param messageType
	 * @param messageId
	 * @return
	 */
	public List<ServerToClientMessage> retrieveCachedResponse(Long userId, Integer messageType, Long messageId) {
		ResponseCacheEntry cacheEntry = cache.get(userId);
		if (cacheEntry != null && !cacheEntry.getMessageId().equals(0) && cacheEntry.getMessageId().equals(messageId) && cacheEntry.getMessageType().equals(messageType)) {
			LogManager.logDebug("CacheManager entry found: user=" + userId + "; messageType=" + messageType + "; messageId=" + messageId);
			return cacheEntry.getResponseMessages();
		}
		return null;
	}
	
	private class ResponseCacheEntry {
		
		private Integer messageType;
		
		private Long messageId;
		
		private List<ServerToClientMessage> responseMessages;

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

		/**
		 * @return the responseMessages
		 */
		public List<ServerToClientMessage> getResponseMessages() {
			return responseMessages;
		}

		/**
		 * @param responseMessages the responseMessages to set
		 */
		public void setResponseMessages(List<ServerToClientMessage> responseMessages) {
			this.responseMessages = responseMessages;
		}
	}
}
