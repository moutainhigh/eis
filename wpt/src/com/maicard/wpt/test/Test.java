package com.maicard.wpt.test;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.maicard.common.domain.Attribute;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.JsonUtils;

public class Test {
	public static void main(String[] argv) {
		DecimalFormat randFormat = new DecimalFormat("00000000");

		System.out.println(randFormat.format(1));
		
	}
}
