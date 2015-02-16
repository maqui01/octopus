/**
 * 
 */
package papasoft.octopus.server.message;

import java.util.ArrayList;
import java.util.List;

import papasoft.octopus.message.ClientToServerMessage;
import papasoft.octopus.message.ServerToClientMessage;
import papasoft.octopus.module.Module;

/**
 * @author maqui
 *
 */
public abstract class AbstractMessage {

	private ClientToServerMessage incomingMessage;
	private Module module;

	/**
	 * @return the incomingMessage
	 */
	public ClientToServerMessage getIncomingMessage() {
		return incomingMessage;
	}

	/**
	 * @param incomingMessage the incomingMessage to set
	 */
	public void setIncomingMessage(ClientToServerMessage incomingMessage) {
		this.incomingMessage = incomingMessage;
	}	
	
	public abstract List<ServerToClientMessage> handleMessage();

	/**
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(Module module) {
		this.module = module;
	}
	
	/**
	 * @param returnMessage
	 * @return
	 */
	protected List<ServerToClientMessage> singleMessage(
			ServerToClientMessage returnMessage) {
		List<ServerToClientMessage> messages = new ArrayList<ServerToClientMessage>();
		messages.add(returnMessage);
		return messages;
	}
	
	/**
	 * 
	 * @param error
	 * @return
	 */
	protected List<ServerToClientMessage> errorMessage(String errorCode) {
		ServerToClientMessage message = new ServerToClientMessage();
		message.setResult(errorCode);
		return singleMessage(message);
	}
}
