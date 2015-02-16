/**
 * 
 */
package papasoft.octopus.module;

import java.util.HashMap;
import java.util.List;

import papasoft.octopus.message.ClientToServerMessage;
import papasoft.octopus.message.ServerToClientMessage;
import papasoft.octopus.minterface.ModuleInterface;
import papasoft.octopus.module.batch.BatchProcess;
import papasoft.octopus.server.message.AbstractMessage;

/**
 * @author maqui
 *
 */
public abstract class Module {
	
	private Integer moduleId;
	
	private HashMap<Integer, AbstractMessage> messages;
	
	private ModuleInterface moduleInterface;
	
	private List<BatchProcess> batchProcesses;
	
	public abstract String getLoggerName();
	
	/**
	 * 
	 * @param messageType
	 * @return
	 */
	public Boolean isValidMessage(Integer messageType) {
		if (this.messages != null && this.messages.containsKey(messageType)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param incomingMessage
	 * @return
	 */
	public List<ServerToClientMessage> handleMessage(
			ClientToServerMessage incomingMessage) {
		
		AbstractMessage handler = this.messages.get(incomingMessage.getMessageType());
		handler.setIncomingMessage(incomingMessage);
		handler.setModule(this);
		
		return handler.handleMessage();
	}

	/**
	 * @return the messages
	 */
	public HashMap<Integer, AbstractMessage> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(HashMap<Integer, AbstractMessage> messages) {
		this.messages = messages;
	}

	/**
	 * @return the moduleId
	 */
	public Integer getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the dataInterface
	 */
	protected ModuleInterface getModuleInterface() {
		return moduleInterface;
	}

	/**
	 * @param dataInterface the dataInterface to set
	 */
	public void setModuleInterface(ModuleInterface moduleInterface) {
		this.moduleInterface = moduleInterface;
	}

	/**
	 * @return the batchProcesses
	 */
	public List<BatchProcess> getBatchProcesses() {
		return batchProcesses;
	}

	/**
	 * @param batchProcesses the batchProcesses to set
	 */
	public void setBatchProcesses(List<BatchProcess> batchProcesses) {
		this.batchProcesses = batchProcesses;
	}	
	
}
