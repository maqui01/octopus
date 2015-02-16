/**
 * 
 */
package papasoft.octopus.security;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author maqui
 *
 */
public class OctopusKeyPairGenerator {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		keyPairGenerator.initialize(2048, random);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pub;
		RSAPrivateKeySpec priv;
		try {
			pub = fact.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
			priv = fact.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);

			saveToFile("key/public.key", pub.getModulus(),
			  pub.getPublicExponent());
			saveToFile("key/private.key", priv.getModulus(),
			  priv.getPrivateExponent());
			
		} catch (InvalidKeySpecException e) {
			System.out.print("Couldn't create keypair");
		}
	}
	
	public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) {
		ObjectOutputStream oout = null;
		try {
			oout = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(fileName)));
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			System.out.print("Couldn't create keypair");
		} finally {
			if (oout != null)
				try {
					oout.close();
				} catch (IOException e) {
					//
				}
		}
	}
}
