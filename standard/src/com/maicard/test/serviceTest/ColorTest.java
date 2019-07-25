package com.maicard.test.serviceTest;

import java.awt.Color;

import com.maicard.common.util.Crypt;

public class ColorTest {
	public static void main(String[] argv){
		//System.out.println(new Color(16,73,100).getRGB());
		String key = "ALla2lll";
		Crypt c = new Crypt();
		c.setDesKey(key);
		
		//System.out.println(c.desEncrypt("action=login&username=netsnakecn@163.com&password=test111"));
		System.out.println(Crypt.passwordEncode("test111"));
	}
}
