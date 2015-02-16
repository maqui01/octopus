/**
 * 
 */
package papasoft.octopus.server.message;

import java.util.ArrayList;
import java.util.List;

import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class DummyMessage extends AbstractMessage {

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.handler.MessageHandler#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		ServerToClientMessage responseMessage = new ServerToClientMessage();
		responseMessage.putData(0, "Hola Octopus Mobile");
		List<ServerToClientMessage> messages = new ArrayList<ServerToClientMessage>();
		messages.add(responseMessage);
		return messages;
	}

}
