package com.maicard.common.util;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;


public class CryptKeyUtils {
	
	private static final String HEX_CHARS = "0123456789abcdef";


	static{  
		try{
			System.loadLibrary("CloudCrypt");  
		}catch(Throwable t){
			//t.printStackTrace();
		}
	} 

	static Logger logger = Logger.getLogger(CryptKeyUtils.class.getName());

	private static final String digestKey = "jeyRofsyefhi_Biocsub";
	private static String masterKey = null;

	public static String readDes3Key() throws Exception{	

		String mac =  _readMac();
		/*if(masterKey == null){
			try{
				masterKey = readKeyNative();
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
		if(mac == null){
			throw new Exception("System access error!");
		}*/
		String key = DigestUtils.md5Hex(mac + digestKey).substring(0, 24);
		System.out.println("读取到本机信息:" + mac  + ",,返回对应密钥:" + key);
		return key;
	}
	
	public static String readAesKey() throws Exception{	

		String mac =  _readMac();
		/*if(masterKey == null){
			try{
				masterKey = readKeyNative();
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
		if(mac == null){
			throw new Exception("System access error!");
		}*/
		String key = DigestUtils.sha256Hex(DigestUtils.md5Hex(mac + digestKey)).substring(0, 16);
		//System.out.println("读取到本机信息:" + mac  + ",,返回对应AES密钥:" + key);
		return key;
	}

	private static String _readMac(){
		try{
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				if(ni.getName().equals("lo")){
					continue;
				}
				if(ni.getHardwareAddress() != null && ni.getHardwareAddress().length > 0){
					return toHexString(ni.getHardwareAddress());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
			sb.append(HEX_CHARS.charAt(b[i] & 0x0F));
		}
		return sb.toString();
	}

	public static native String readKeyNative();

	public static void main(String argv[]){
		try {
			readDes3Key();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

