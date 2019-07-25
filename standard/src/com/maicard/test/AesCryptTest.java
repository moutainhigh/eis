package com.maicard.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.springframework.util.Assert;

import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.ShortMd5;
import com.maicard.standard.DataName;


public class AesCryptTest {
	public static void main(String[] argv){
		String[] src = new String[]{"true","Haiyouwh0","mq","vectAO"};
		System.out.println(src);
		Crypt crypt = new Crypt();
		String		key = "1321ff3ed6d28ef0";
		System.out.println(key);
		crypt.setAesKey(key);
		for(String s : src){
			String dst = 	crypt.aesEncrypt(s);
			System.out.println(dst + "=> " + crypt.aesDecrypt(dst));
		}


	}
}
