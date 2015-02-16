/**
 * 
 */
package papasoft.octopus.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author maqui
 *
 */
public class CryptoUtils {

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static PrivateKey readPrivateKeyFromFile() throws IOException {
		ObjectInputStream oin = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			oin = new ObjectInputStream(new BufferedInputStream(classLoader.getResourceAsStream( "private.key" )));
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privKey = fact.generatePrivate(keySpec);
			return privKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			if (oin != null) {
				oin.close();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static PublicKey readPublicKeyFromFile() throws IOException {
		ObjectInputStream oin = null;
		try {
			oin = new ObjectInputStream(new BufferedInputStream(new FileInputStream("key/public.key")));
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			if (oin != null) {
				oin.close();
			}
		}
	}
}
