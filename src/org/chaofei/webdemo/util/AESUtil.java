package org.chaofei.webdemo.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.chaofei.webdemo.constant.Constant;

import it.sauronsoftware.base64.Base64;

public class AESUtil {
	private Cipher encryCipher;
	private Cipher decryCipher;
	private static AESUtil instance;
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	private static final String KEY = Constant.EMAIL_URL;

	public AESUtil() {
		try {
			byte[] keybytes = sha256Encrypt(KEY);
		    SecureRandom random = new SecureRandom();  
		    DESKeySpec keySpec = new DESKeySpec(keybytes);  
		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
		    SecretKey secretKey = keyFactory.generateSecret(keySpec);  
		    encryCipher = Cipher.getInstance("DES");
		    decryCipher = Cipher.getInstance("DES");
		    encryCipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
		    decryCipher.init(Cipher.DECRYPT_MODE, secretKey, random);  
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static AESUtil getInstance() {
		if (null == instance) {
			instance = new AESUtil();
		}
		return instance;
	}

	private byte[] sha256Encrypt(String info) throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		sha256.update(info.getBytes(Charset.defaultCharset()));
		return sha256.digest();
	}

	public String encrypt(String msg) {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			byte[] encBytes = encryCipher.doFinal(msg.getBytes(Charset.defaultCharset()));
			return new String(Base64.encode(encBytes), Charset.defaultCharset());
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return "";
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return "";
		} finally {
			writeLock.unlock();
		}
	}
	
	public String decrypt(String msg) {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			byte[] decBytes = decryCipher.doFinal(Base64.decode(msg.getBytes(Charset.defaultCharset())));
			return new String(decBytes, Charset.defaultCharset());
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return "";
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return "";
		} finally {
			writeLock.unlock();
		}
	}
}
