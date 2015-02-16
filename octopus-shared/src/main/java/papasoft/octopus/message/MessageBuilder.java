/**
 * 
 */
package papasoft.octopus.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import papasoft.octopus.exception.OctopusException;

/**
 * @author maqui
 *
 */
public class MessageBuilder {

	/**
	 * @param data
	 * @return
	 * @throws OctopusException 
	 */
	public static Message buildMessage(byte[] data, Cipher cipher) throws OctopusException {
		if (data == null) {
			return null;
		}
		
		ByteArrayInputStream b = new ByteArrayInputStream(data);
		InputStream is = null;
		if (cipher == null) {
			is = b;
		} else {
			is = new CipherInputStream(b, cipher);
		}
		ObjectInputStream o = null;
		try {
			o = new ObjectInputStream(is);
	        return (Message) o.readObject();
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

	/**
	 * 
	 * @param message
	 * @return
	 * @throws OctopusException
	 */
	public static byte[] buildByteArray(Message message, Cipher cipher) throws OctopusException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = null;
        try {
        	OutputStream os = null;
    		if (cipher == null) {
    			os = b;
    		} else {
    			os = new CipherOutputStream(b, cipher);
    		}
            o = new ObjectOutputStream(os);
			o.writeObject(message);
			o.flush();
			o.close();
	        return b.toByteArray();
		} catch (IOException e) {
			throw new OctopusException("Error building bytearray from message", e);
		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {}
			}
		}
	}
	
}
