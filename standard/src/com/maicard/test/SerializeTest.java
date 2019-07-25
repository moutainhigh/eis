package com.maicard.test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.maicard.common.util.HttpUtils;
import com.maicard.site.domain.Document;
import com.maicard.standard.CommonStandard;

public class SerializeTest {
	public static void main(String[] argv){
		
		String fileName = "d:/test.dat";
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("gift_1", "0.1");
		data.put("gift_2", "0.5");
		try {
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			FileOutputStream ffos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(ffos);
			oos.writeObject(data);
			oos.flush();
			oos.close();
			//System.out.println(fos.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public static void test(Document s){
		s = null;
	}
}
