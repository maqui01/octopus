/**
 * 
 */
package papasoft.octopus.server.handler;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.List;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;

import papasoft.octopus.context.OctopusContext;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.ClientToServerMessage;
import papasoft.octopus.message.MessageBuilder;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;
import papasoft.octopus.module.Module;
import papasoft.octopus.security.OctopusServerEncripter;
import papasoft.octopus.session.SessionData;
import papasoft.octopus.session.SessionManager;

/**
 * @author maqui
 *
 */
public class TransactionHandler implements IDataHandler {	

	/* (non-Javadoc)
	 * @see org.xsocket.connection.IDataHandler#onData(org.xsocket.connection.INonBlockingConnection)
	 */
	@Override
	public boolean onData(INonBlockingConnection conn) throws IOException,
			BufferUnderflowException, ClosedChannelException,
			MaxReadSizeExceededException {
		
		ClientToServerMessage incomingMessage = null;
		try {
			if (conn.available() == -1) {
				return true;
			}
			
			conn.markReadPosition();
			
			Integer sessionId = conn.readInt();
			Integer messageSize = conn.readInt();

			byte[] data = conn.readBytesByLength(messageSize);
			
			conn.removeReadMark();
			if (sessionId != null && sessionId == SessionManager.NULL_SESSION_ID) {
				return processSessionIdMessage(conn, data);
			} else {
				SessionData session = OctopusContext.getCtx().getSessionManager().getSessionData(sessionId);
				if (session == null || session.isExpired()) {
					sendFatalSignal(conn, SessionManager.EXPIRED_SESSION_ID, incomingMessage);
					return true;
				}	
				
				incomingMessage = (ClientToServerMessage) MessageBuilder.buildMessage(data, OctopusServerEncripter.getInstance().getCipherDecriptor(sessionId));
				OctopusContext.getCtx().getAuditManager().auditIncomingMessage(incomingMessage, conn.getRemoteAddress().getHostAddress(), sessionId);
				
				boolean validMessage = validateMessage(conn, session.getId(), incomingMessage);
				if (!validMessage) {
					sendErrorMessage(conn, sessionId, incomingMessage, MessageResult.HEADER_INVALID_MODULE_OR_MESSAGE_TYPE);
					return true;
				}			
	
				/* Send response */
				return sendResponse(conn, sessionId, incomingMessage, session);
			}			
		} catch (BufferUnderflowException ex) {
			LogManager.logDebug("Buffer Underflow", ex);
	        conn.resetToReadMark();
	        return true;
		} catch (Throwable ex) {
			LogManager.logError("Error receiving message", ex);
			try {
				sendFatalSignal(conn, SessionManager.NULL_SESSION_ID, incomingMessage);
			} catch (Throwable e) {
				// Just return true if everything fails
			}
			return true;
		}
	}

	/**
	 * @param conn
	 * @param nullSessionId
	 * @throws IOException 
	 * @throws BufferOverflowException 
	 */
	private void sendFatalSignal(INonBlockingConnection conn,
			Integer signal, ClientToServerMessage incomingMessage) throws BufferOverflowException, IOException {
		OctopusContext.getCtx().getAuditManager().auditOutgoingFatal(incomingMessage, signal, conn.getRemoteAddress().getHostAddress());
		conn.write(signal);
	}

	/**
	 * @param conn
	 * @param sessionId
	 * @param incomingMessage
	 * @param session
	 * @return
	 * @throws IOException
	 * @throws OctopusException
	 */
	private boolean sendResponse(INonBlockingConnection conn,
			Integer sessionId, ClientToServerMessage incomingMessage,
			SessionData session) throws IOException, OctopusException {
		Integer responseSize = 0;
		conn.write(sessionId); // Write SessionId
		// If response is cached from previous requests, then don't process and send same response as before
		List<ServerToClientMessage> responseMessages = ResponseCacheManager.getInstance().retrieveCachedResponse(incomingMessage.getUserId(), incomingMessage.getMessageType(), incomingMessage.getMessageId());
		if (responseMessages == null) {
			responseMessages = handleMessage(incomingMessage);
			if (responseMessages != null && responseMessages.size() > 0 && responseMessages.get(0).getResult().equals(MessageResult.RESULT_OK)) {
				ResponseCacheManager.getInstance().cacheResponse(incomingMessage.getUserId(), incomingMessage.getMessageType(), incomingMessage.getMessageId(), responseMessages);
			}
		}
		if (responseMessages != null && responseMessages.size() > 0) {
			int i = 0;
			for (ServerToClientMessage responseMessage : responseMessages) {
				i++;
				if (i != responseMessages.size()) {
					responseMessage.setMoreData(true);
				}
				responseMessage.setMessagesQuantity(responseMessages.size());
				OctopusContext.getCtx().getAuditManager().auditOutgoingMessage(responseMessage, incomingMessage, conn.getRemoteAddress() != null ? conn.getRemoteAddress().getHostAddress() : "N/A", sessionId);
				responseSize += sendResponseMessage(conn, session.getId(), responseMessage);
			}
		} else {
			responseSize += sendErrorMessage(conn, sessionId, incomingMessage, MessageResult.MESSAGE_UNRESPONSE);
		}
		LogManager.logDebug("Inc: " + incomingMessage.toString() + " - Response size: " + responseSize);
		return true;
	}

	/**
	 * @param conn
	 * @param data
	 * @return
	 * @throws IOException
	 */
	private boolean processSessionIdMessage(INonBlockingConnection conn,
			byte[] data) throws IOException {
		try {
			SessionData session = OctopusContext.getCtx().getSessionManager().getSessionData();
			session.setSecretKey(OctopusServerEncripter.getInstance().decryptSecretKey(data));
			OctopusContext.getCtx().getAuditManager().auditSessionIdCreation(conn.getRemoteAddress().getHostAddress(), session.getId());
			conn.write(session.getId());
			return true;
		} catch (Throwable th) {
			LogManager.logError("Error creating new session Id", th);
			conn.write(SessionManager.NULL_SESSION_ID);
			return true;
		}
	}

	/**
	 * @param conn
	 * @param sessionId
	 * @param responseMessage
	 * @throws OctopusException
	 * @throws IOException
	 */
	private int sendResponseMessage(INonBlockingConnection conn,
			Integer sessionId, ServerToClientMessage responseMessage)
			throws OctopusException {
		
		byte[] responseData = MessageBuilder.buildByteArray(responseMessage, OctopusServerEncripter.getInstance().getCipherEncriptor(sessionId));
		
		try {
			conn.write(responseData.length); // Write TamañoMensaje
			conn.write(responseData, 0, responseData.length); // Write Mensaje
			return responseData.length;
		} catch (Exception e) {
			throw new OctopusException("Error sending message", e);
		} 
	}

	/**
	 * @param incomingMessage
	 * @return 
	 */
	private List<ServerToClientMessage> handleMessage(ClientToServerMessage incomingMessage) {
		Module module = OctopusContext.getCtx().getModule(incomingMessage.getModuleId());
		return module.handleMessage(incomingMessage);
	}

	/**
	 * @param conn 
	 * @param incomingMessage
	 * @return
	 * @throws OctopusException 
	 */
	private boolean validateMessage(INonBlockingConnection conn, Integer sessionId, ClientToServerMessage incomingMessage) throws OctopusException {
		
		// Then check if the module and messageType is OK
		Module module = OctopusContext.getCtx().getModule(incomingMessage.getModuleId());
		if (module != null && module.isValidMessage(incomingMessage.getMessageType())) {
			return true;
		}
		return false;
	}

	/**
	 * @param conn
	 * @param headerInvalidOrExpiredSession
	 * @throws OctopusException 
	 */
	private int sendErrorMessage(INonBlockingConnection conn, Integer sessionId, ClientToServerMessage incomingMessage,
			String errorCode) throws OctopusException {
		ServerToClientMessage errorMessage = new ServerToClientMessage();
		errorMessage.setResult(errorCode);
		OctopusContext.getCtx().getAuditManager().auditOutgoingMessage(errorMessage, incomingMessage, conn.getRemoteAddress() != null ? conn.getRemoteAddress().getHostAddress() : "N/A", sessionId);
		return sendResponseMessage(conn, sessionId, errorMessage);
	}

}
