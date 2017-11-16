package org.chaofei.webdemo.util;

import org.junit.Test;

import junit.framework.TestCase;

public class AESUtilTest {

	@Test
	public void testEncrypt() {
		AESUtil aesUtil = AESUtil.getInstance();
		String plainText = "chaofeibest@163.com";
		TestCase.assertEquals("NUKh2Y8qncptStuA/CeTDGMWZIxjbnwq", aesUtil.encrypt(plainText));
	}
	
	@Test
	public void testDecrypt() {
		AESUtil aesUtil = AESUtil.getInstance();
		String enString = "NUKh2Y8qncptStuA/CeTDGMWZIxjbnwq";
		TestCase.assertEquals("chaofeibest@163.com", aesUtil.decrypt(enString));
	}
}
