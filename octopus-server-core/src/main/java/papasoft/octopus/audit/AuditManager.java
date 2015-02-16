/**
 * 
 */
package papasoft.octopus.audit;

import java.util.Date;

import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.ClientToServerMessage;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class AuditManager {

	private AuditDao auditDao;

	/**
	 * @return the auditDao
	 */
	public AuditDao getAuditDao() {
		return auditDao;
	}

	/**
	 * @param auditDao the auditDao to set
	 */
	public void setAuditDao(AuditDao auditDao) {
		this.auditDao = auditDao;
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void auditIncomingMessage(ClientToServerMessage message, String remoteIp, Integer sessionId) {
		AuditEntry entry = new AuditEntry();
		entry.setDate(new Date());
		entry.setMessageType(message.getMessageType());
		entry.setModuleId(message.getModuleId());
		entry.setRemoteIp(remoteIp);
		entry.setBody(message.getBodyToString());
		entry.setSessionId(sessionId);
		entry.setType(AuditEntryType.ENTRY_INCOMING_MESSAGE);
		entry.setUserId(message.getUserId());
		entry.setDeviceId(message.getImei());
		entry.setMessageId(message.getMessageId());
		try {
			getAuditDao().save(entry);
		} catch (OctopusException e) {
			LogManager.logError("Error saving auditory", e);
		}
	}
	
	/**
	 * 
	 * @param remoteIp
	 * @param sessionId
	 * @throws OctopusException
	 */
	public void auditSessionIdCreation(String remoteIp, Integer sessionId) {
		AuditEntry entry = new AuditEntry();
		entry.setDate(new Date());
		entry.setRemoteIp(remoteIp);
		entry.setSessionId(sessionId);
		entry.setType(AuditEntryType.ENTRY_SESSION_ID_CREATION);
		try {
			getAuditDao().save(entry);
		} catch (OctopusException e) {
			LogManager.logError("Error saving auditory", e);
		}
	}
	
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void auditOutgoingMessage(ServerToClientMessage outgoingMessage, ClientToServerMessage incomingMessage, String remoteIp, Integer sessionId) {
		AuditEntry entry = new AuditEntry();
		entry.setDate(new Date());
		entry.setMessageType(incomingMessage.getMessageType());
		entry.setModuleId(incomingMessage.getModuleId());
		entry.setRemoteIp(remoteIp);
		entry.setBody("Result: " + outgoingMessage.getResult() + " | Msgs quant: " + outgoingMessage.getMessagesQuantity() + " | " + outgoingMessage.getBodyToString());
		entry.setSessionId(sessionId);
		entry.setType(AuditEntryType.ENTRY_OUTGOING_MESSAGE);
		entry.setUserId(incomingMessage.getUserId());
		entry.setDeviceId(incomingMessage.getImei());
		try {
			getAuditDao().save(entry);
		} catch (OctopusException e) {
			LogManager.logError("Error saving auditory", e);
		}
	}

	/**
	 * @param incomingMessage
	 * @param signal
	 * @param hostAddress
	 */
	public void auditOutgoingFatal(ClientToServerMessage incomingMessage,
			Integer signal, String remoteIp) {
		AuditEntry entry = new AuditEntry();
		entry.setDate(new Date());
		if (incomingMessage != null) {
			entry.setMessageType(incomingMessage.getMessageType());
			entry.setModuleId(incomingMessage.getModuleId());
			entry.setUserId(incomingMessage.getUserId());
			entry.setDeviceId(incomingMessage.getImei());
		}
		entry.setRemoteIp(remoteIp);
		entry.setBody("Fatal signal: " + signal.toString());
		entry.setSessionId(null);
		entry.setType(AuditEntryType.ENTRY_OUTGOING_FATAL);
		try {
			getAuditDao().save(entry);
		} catch (OctopusException e) {
			LogManager.logError("Error saving auditory", e);
		}
	}
}
