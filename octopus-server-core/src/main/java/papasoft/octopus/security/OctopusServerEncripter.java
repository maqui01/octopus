/**
 * 
 */
package papasoft.octopus.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import papasoft.octopus.context.OctopusContext;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.session.SessionData;
import papasoft.octopus.utils.CryptoUtils;

/**
 * @author maqui
 *
 */
public class OctopusServerEncripter {

	private PrivateKey privateKey = null;

	private static OctopusServerEncripter instance = null;
	
	public static OctopusServerEncripter getInstance() {
		if (instance == null) {
			instance = new OctopusServerEncripter();
			instance.init();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	private void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	private Cipher getCipherEncriptor(Integer sessionId, int opMode) throws OctopusException {
		try {
			Cipher cipher = null;
			if (sessionId == 0) {
				cipher = Cipher.getInstance("RSA", "BC");
				cipher.init(opMode, getPrivateKey());
			} else {
				cipher = Cipher.getInstance("AES", "BC");
				SessionData sessionData = OctopusContext.getCtx().getSessionManager().getSessionData(sessionId);
				cipher.init(opMode, sessionData.getSecretKey());
			}
			return cipher;
		} catch (Throwable e) {
			LogManager.logError("Can't create octopus decriptor", e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param sessionId
	 * @return
	 * @throws OctopusException
	 */
	public Cipher getCipherDecriptor(Integer sessionId) throws OctopusException {
		return getCipherEncriptor(sessionId, Cipher.DECRYPT_MODE);
	}
	
	/**
	 * 
	 * @param sessionId
	 * @return
	 * @throws OctopusException
	 */
	public Cipher getCipherEncriptor(Integer sessionId) throws OctopusException {
		if (sessionId == 0) {
			return null;
		}
		return getCipherEncriptor(sessionId, Cipher.ENCRYPT_MODE);
	}

	/**
	 * @return the privateKey
	 * @throws IOException 
	 */
	public PrivateKey getPrivateKey() throws IOException {
		if (this.privateKey == null) {
			this.privateKey = CryptoUtils.readPrivateKeyFromFile();
		}
		return this.privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @param data
	 * @return
	 * @throws OctopusException 
	 */
	public SecretKey decryptSecretKey(byte[] data) throws OctopusException {
		if (data == null) {
			return null;
		}
		
		ByteArrayInputStream b = new ByteArrayInputStream(data);
		InputStream is = null;
		Cipher cipher = getCipherDecriptor(0);
		if (cipher == null) {
			is = b;
		} else {
			is = new CipherInputStream(b, cipher);
		}
		ObjectInputStream o = null;
		try {
			o = new ObjectInputStream(is);
	        return (SecretKey) o.readObject();
		} catch (Exception e) {
			throw new OctopusException("Error building message object", e);
		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {}
			}
		}
	}


}
