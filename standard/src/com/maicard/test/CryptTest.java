package com.maicard.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.ShortMd5;
import com.maicard.standard.DataName;


public class CryptTest {
	public static void main(String[] argv){
		String src = "zb299";
		System.out.println(Crypt.passwordEncode(src));
	//	System.out.println(DigestUtils.sha256Hex(src));
	//	System.out.println(DigestUtils.md5Hex(src));

		/*String[] src = new String[]{"root","Haiyouwh0","mq","vectAO"};
		System.out.println(src);
		Crypt crypt = new Crypt();
		String		key = "e748d1ae40583fbc7d05155e";
		System.out.println(key);
		crypt.setDes3Key(key);
		for(String s : src){
			String dst = 	crypt.des3Encrypt(s);
			System.out.println(dst + "=> " + crypt.des3Decrypt(dst));
		}*/


	}
}
