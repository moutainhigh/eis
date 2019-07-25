package com.maicard.test;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegTest {
	public static void main(String[] argv){
		String u =  "aaaa|bbbb|ccc";
		String[] a = u.split("|");
		for(String b : a){
			System.out.println(b);

		}

	}
}