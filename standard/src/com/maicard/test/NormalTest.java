package com.maicard.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.StringUtils;

import com.maicard.common.domain.EisObject;
import com.maicard.site.domain.Document;
import com.maicard.standard.CommonStandard;

public class NormalTest {
	private final static int[] validMoney = new int[]{5,10,15,25,30,35,45,50,100,300,350,1000};


	static final String host = "api.weixin.qq.com";
	static final int port = 80; 

	public static void main(String[] argv){
		String src = "ypesyp|19690";
		System.out.println(DigestUtils.sha512Hex(src));
	}


}