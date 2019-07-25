package com.maicard.common.util;

import java.util.regex.Pattern;

import com.maicard.standard.CommonStandard;

public class InputFilter {

	private static final Pattern pattern = Pattern.compile(CommonStandard.BAD_CHAR_PATTERN);

	public static boolean isLegal(String input){
		return !pattern.matcher(input).find();
	}
	
	public static void main(String[] argv){
		String src = "abc\\%aaa";
		System.out.println(src);
		
		System.out.println(pattern.matcher(src).find());
	}
}
