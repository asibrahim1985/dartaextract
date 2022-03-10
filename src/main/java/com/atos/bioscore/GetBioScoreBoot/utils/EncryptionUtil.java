package com.atos.bioscore.GetBioScoreBoot.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

	private static final String AES_BLOCK_MODE = "AES/ECB/PKCS5Padding";
	
	private static final String secKeyString = "xOk5lRxjJvj4iM/n7/SF9g==";
	
	public  void generateKey(int n) throws NoSuchAlgorithmException {
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(n);
	    SecretKey key = keyGenerator.generateKey();
		String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("*****************************");
		System.out.println(encodedKey);
		System.out.println("*****************************");
	}
	
	public String encryptTextAES(String input)
		      throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
		          NoSuchAlgorithmException, InvalidKeyException {
		    byte[] crypted = null;

		    SecretKeySpec skey = new SecretKeySpec(secKeyString.getBytes(), "AES");

		    var aesCipher = Cipher.getInstance(AES_BLOCK_MODE);
		    aesCipher.init(Cipher.ENCRYPT_MODE, skey);
		    crypted = aesCipher.doFinal(input.getBytes());
		    java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
		    return encoder.encodeToString(crypted);
	}
	
	public String decryptTextAES(String input)
		      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
		          BadPaddingException, IllegalBlockSizeException {
		    byte[] output = null;
		    java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
		    SecretKeySpec skey = new SecretKeySpec(secKeyString.getBytes(), "AES");
		    var aesCipher = Cipher.getInstance(AES_BLOCK_MODE);
		    aesCipher.init(Cipher.DECRYPT_MODE, skey);
		    output = aesCipher.doFinal(decoder.decode(input));
		    return new String(output);
		  }
}
