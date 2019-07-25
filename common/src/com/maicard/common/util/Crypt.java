package com.maicard.common.util;


import java.security.MessageDigest;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;





public class Crypt {
	SecretKey desKey;
	SecretKey aesKey;

	public String rsaPublicKeyFile, rsaPrivateKeyFile;
	public String aesKeyFile;
	/*byte[] des3keyBytes = {0x11, 0x22, 0x4F, 0x58, (byte)0x88, 0x10, 0x40, 0x38
            , 0x28, 0x25, 0x79, 0x51, (byte)0xCB, (byte)0xDD, 0x55, 0x66
            , 0x77, 0x29, 0x74, (byte)0x98, 0x30, 0x40, 0x36, (byte)0xE2};*/
	private static final String HEX_CHARS = "0123456789abcdef";

	private static final String DEFAULT_ENCODING = "UTF-8";



	public void setDesKey(String key){
		byte[] staticKey = key.getBytes();
		try {
			SecretKeyFactory keyfact = SecretKeyFactory.getInstance("DES");
			DESKeySpec dks = new DESKeySpec(staticKey); 
			desKey = keyfact.generateSecret(dks);
		}catch(Exception e){
			e.printStackTrace();
		}		   
	}
	public void setDes3Key(String key){
		byte[] staticKey = key.getBytes();
		try {

			desKey =  new SecretKeySpec(staticKey, "DESede");  
		}catch(Exception e){
			e.printStackTrace();
		}		   
	}
	public void setAesKey(String key){
		byte[] staticKey = key.getBytes();
		try {

			aesKey =  new SecretKeySpec(staticKey, "AES");  
		}catch(Exception e){
			e.printStackTrace();
		}		   
	}

	public String desEncrypt(String src) {
		String str = "";
		try{ 
			Cipher c1 = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, desKey);
			byte[] encoded = c1.doFinal(src.getBytes());
			for(int i=0;i<encoded.length;i++)
			{
				str += byteToHex(encoded[i]);
			}
			return str;	   

		}catch(Exception e){
			return null;

		}

	}

	public String desDecrypt(String dst) {
		try{ 

			Cipher c1 = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, desKey);
			byte[] ByteRequest = hexStringToByte(dst);
			byte[] decoded = c1.doFinal(ByteRequest);
			return new String(decoded);

		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}

	public String des3Encrypt(String src) {
		return des3Encrypt(src, DEFAULT_ENCODING);

	}	
	
	public String des3Encrypt(String src, String encoding) {
		String str = "";
		try{ 
			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, desKey);
			byte[] encoded = c1.doFinal(src.getBytes(encoding));
			for(int i=0;i<encoded.length;i++){
				str += byteToHex(encoded[i]);
			}
			return str;	   

		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}	

	public String des3Decrypt(String dst){
		return des3Decrypt(dst, DEFAULT_ENCODING);
	}
	public String des3Decrypt(String dst, String encoding) {
		try{ 

			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, desKey);
			byte[] ByteRequest = hexStringToByte(dst);
			byte[] decoded = c1.doFinal(ByteRequest);
			return new String(decoded, encoding);

		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}

	public String des3EncryptBase64(String src) {
		try{ 
			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, desKey);
			byte[] encoded = c1.doFinal(src.getBytes());
			String dst = new Base64().encodeToString(encoded);
			return dst.replaceAll("\r", "").replaceAll("\n", "").trim();

		}catch(Exception e){
			return null;

		}

	}

	public String des3DecryptBase64(String dst) {
		//SecretKey des3key = new SecretKeySpec(des3keyBytes, "DESede");
		try{ 

			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, desKey);
			byte[] ByteRequest =  new Base64().decode(dst);//new BASE64Decoder().decodeBuffer(dst);

			byte[] decoded = c1.doFinal(ByteRequest);
			return new String(decoded);

		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}




	public boolean aesGenKey(){
		try{
			KeyGenerator kg= KeyGenerator.getInstance("AES/ECB/PKCS5Padding");			
			kg.init(128);
			SecretKey key = kg.generateKey();
			byte[] keyRaw = key.getEncoded();
			System.out.println(keyRaw);

			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public String aesEncrypt(String src) {
		String str = "";
		try{ 
			//SecretKeySpec  key= new SecretKeySpec(this.aeskeyBytes, "AES");
			/*byte[] test = key.getEncoded();
			for(int i = 0; i < test.length; i++){
				System.out.print(test[i]);
			}
			System.out.print("\n\n");*/
			Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encoded = c1.doFinal(src.getBytes(DEFAULT_ENCODING));
			for(int i=0;i<encoded.length;i++)	{
				str += byteToHex(encoded[i]);
			}
			return str;	   

		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}
	
	public static String passwordEncode(String src){
		return DigestUtils.sha256Hex(DigestUtils.md5Hex(src));
	}
	
	//上一版本的密码加密方式，慎用
	public static String legacyPasswordEncode(String src){
		try{ 
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(src.getBytes());
			byte[] dest = sha.digest(src.getBytes());
			String enc = "";
			for(int i=0;i<dest.length;i++){
				enc += byteToHex(dest[i]);
			}
			return enc;

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public byte[] aesEncryptWithBin(String src) {
		try{ 

			Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encoded = c1.doFinal(src.getBytes(DEFAULT_ENCODING));
			return encoded;


		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}

	public String aesDecrypt(String dst) {
		try{ 
			//SecretKeySpec  key= new SecretKeySpec(this.aeskeyBytes, "AES");
			Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] ByteRequest = hexStringToByte(dst);
			byte[] decoded = c1.doFinal(ByteRequest);
			return new String(decoded, DEFAULT_ENCODING);

		}catch(Exception e){
			e.printStackTrace();
			return null;

		}

	}

	public static byte[] hexStringToByte(String hex) {  
		int len = (hex.length() / 2);  
		byte[] result = new byte[len];  
		char[] achar = hex.toCharArray();  
		for (int i = 0; i < len; i++) {  
			int pos = i * 2;  
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));  
		}  
		return result;  
	}  

	private static byte toByte(char c) {  
		byte b = (byte) "0123456789ABCDEF".indexOf(c);  
		return b;  
	}  

	public static String byteToHex(byte b)		   {
		char Digest[] = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[]ch = new char[2];
		ch[0] = Digest[(b>>>4&0X0F)];
		ch[1] = Digest[b&0X0F];
		return new String(ch);
	}

	public static String base64Encode(String src){
		return new String(new Base64().encode(src.getBytes())).replaceAll("\r\n", "").replaceAll("\n", "");

	}
	public static String base64Decode(String src){
		return new String(new Base64().decode(src.getBytes())).replaceAll("\r\n", "").replaceAll("\n", "");

	}

	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
			sb.append(HEX_CHARS.charAt(b[i] & 0x0F));
		}
		return sb.toString();
	}

	public static byte[] toByteArray(String s) {
		byte[] buf = new byte[s.length() / 2];
		int j = 0;
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) ((Character.digit(s.charAt(j++), 16) << 4) | Character
					.digit(s.charAt(j++), 16));
		}
		return buf;
	}

	public static void main(String[] argv){
		System.out.println(System.getProperty("java.home"));
		try{
			//KeyGenerator   kg   =   KeyGenerator.getInstance("AES");   //获取密匙生成器  
			//kg.init(128);   //初始化  
			//SecretKey   key   =   kg.generateKey();   //生成密匙，可用多种方法来保存密匙
			//byte[] keyRaw = key.getEncoded();
			//for(int i=0; i< keyRaw.length;i++){
			//	System.out.print(Crypt.byteToHex(keyRaw[i]));
			//}
			//System.out.println("");
			Crypt c = new Crypt();
			String src = "test";
			c.setAesKey("1234567890123456");
			String dst = c.aesEncrypt(src);
			System.out.println("EncResult:" + dst);
			System.out.println("Result:" + c.aesDecrypt(dst));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String shortMd5(String src){
		if(src == null || src.length() != 32){
			return null;
		}
		String result = "";
		for(int i = 0; i < src.length(); i++){
			if(i % 2 == 0){
				result += src.charAt(i);
			}
		}
		return result;
	}
	public static byte[] base64Encode(byte[] sourceBytes) {
		return new Base64().encode(sourceBytes);
		//String dest =  new String(new Base64().encode(sourceBytes)).replaceAll("\r\n", "").replaceAll("\n", "");
		//return dest.getBytes();
	}

	public static byte[] base64Decode(byte[] sourceBytes) {
		return new Base64().decode(sourceBytes);
	}




}
