/**
 * 
 */
package papasoft.octopus.session;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import papasoft.octopus.log.LogManager;

/**
 * @author maqui
 *
 */
public class SessionManager {
	
	public static final Integer NULL_SESSION_ID = 0;
	public static final Integer EXPIRED_SESSION_ID = 1;
	private static final int SESSION_MAP_MAX_CAPACITY = 10000;

	private HashMap<Integer, SessionData> sessionMap;
	private Random rand;
	private Integer sessionTimeout;
	
	/**
	 * 
	 * @param sessionTimeout in minutes
	 */
	public SessionManager(Integer sessionTimeout) {
		this.sessionMap = new HashMap<Integer, SessionData>();
		rand = new Random();
		this.sessionTimeout = sessionTimeout;
		Thread thread = new Thread(new SessionPurgeThread());
		thread.start();
	}
	
	/**
	 * 
	 * @param SessionId
	 * @return
	 */
	public synchronized SessionData getSessionData(Integer sessionId) {
		if (this.sessionMap.containsKey(sessionId)) {
			SessionData sessionData = this.sessionMap.get(sessionId);
			if (sessionData.isExpired()) {
				return this.sessionMap.remove(sessionId);
			} else {
				sessionData.update();
				return sessionData;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized SessionData getSessionData() {
		if (this.sessionMap.size() > SESSION_MAP_MAX_CAPACITY) {
			purgeExpiredData();
			if (this.sessionMap.size() > SESSION_MAP_MAX_CAPACITY) { 
				return null;
			}
		}
		rand.setSeed(System.currentTimeMillis());
		Integer key = new Integer(rand.nextInt());
		while (this.sessionMap.containsKey(key) || key == NULL_SESSION_ID || key == EXPIRED_SESSION_ID) {
			key = new Integer(rand.nextInt());
		}
		SessionData sessionData = new SessionData(key, this.sessionTimeout);
		this.sessionMap.put(key, sessionData);
		
		return this.sessionMap.get(key);
	}

	/**
	 * @return the sessionTimeout
	 */
	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	/**
	 * @param sessionTimeout the sessionTimeout to set
	 */
	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * 
	 */
	private void purgeExpiredData() {
		Iterator<SessionData> it = getSessionMap().values().iterator();
		while (it.hasNext()) {
			SessionData data = it.next();
			if (data.isExpired()) {
				it.remove();
			}
		}
	}
	
	private class SessionPurgeThread implements Runnable {

		private static final int PURGE_TIME = 900000;

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Boolean stop = false;
			while (true && !stop) {
				purgeExpiredData();
				try {
					Thread.sleep(PURGE_TIME);
				} catch (InterruptedException e) {
					LogManager.logError("Error running backgroud session purge process, process stopped", e);
					stop = true;
				}
			}
		}
		
	}

	/**
	 * @return the sessionMap
	 */
	private HashMap<Integer, SessionData> getSessionMap() {
		return sessionMap;
	}
}
