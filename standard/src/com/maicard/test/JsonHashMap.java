package com.maicard.test;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maicard.common.util.JsonUtils;

public class JsonHashMap {


	public static void main(String[] argv){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("清新", "tag");
		map.put("中国风", "tag");
		map.put("简介", "tag");
		map.put("红色", "tag");
		try {
			System.out.println(JsonUtils.getInstance().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


}