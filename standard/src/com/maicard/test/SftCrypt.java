package com.maicard.test;

import java.io.File;
import java.io.FileInputStream;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;

public class SftCrypt {
	public static void main(String[] argv){
		String src = "2714";
		try{
			FileInputStream is = new FileInputStream(new File("d:/sft_cert.cert"));
			X509Certificate x509Cert = X509Certificate.getInstance(is);
			PublicKey publicKey = x509Cert.getPublicKey();
			Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			byte[] middle = cipher.doFinal(src.getBytes());	
			System.out.println(Base64.encodeBase64String(middle));
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
