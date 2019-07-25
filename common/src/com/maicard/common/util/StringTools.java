package com.maicard.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.maicard.standard.CommonStandard;

public class StringTools {
	

	/**
	 * 把element加入src，然后返回一个以,分割的字符串
	 * 如果src中已经有了element，则不会重复
	 * 
	 * @param src
	 * @param element
	 * @return
	 */
	public static String addElementNoDuplicate(String src, String element){
		if(src == null){
			return element;
		}
		if(element == null){
			return src;
		}
		StringBuilder sb = new StringBuilder();
		String[] data = src.split(",");
		if(data == null || data.length < 1){
			return element;
		}
		Set<String> dataSet = new HashSet<String>();
		for(String d : data){
			dataSet.add(d);
		}
		dataSet.add(element);
		for(String d : dataSet){
			sb.append(d).append(",");
		}
		return sb.toString().replaceAll(",$", "");
	}

	/**
	 * 把element加入src，然后返回一个以,分割的字符串
	 * 如果src中已经有了element，则使用value替换以#分割的值
	 * 
	 * @param src
	 * @param element
	 * @param value
	 * @return
	 */
	public static String addElementNoDuplicate(String src, String element, String value){
		String singalValue = element + "#" + value;
		if(src == null){
			return singalValue;
		}

		StringBuilder sb = new StringBuilder();
		String[] data = src.split(",");
		if(data == null || data.length < 1){
			return singalValue;
		}
		Set<String> dataSet = new HashSet<String>();
		boolean isReplace = false;
		for(String d : data){
			if(d.startsWith(element + "#")){
				isReplace = true;
				dataSet.add(singalValue);
			} else {
				dataSet.add(d);

			}
		}
		if(!isReplace){
			dataSet.add(singalValue);
		}
		for(String d : dataSet){
			sb.append(d).append(",");
		}
		return sb.toString().replaceAll(",$", "");
	}

	public static Set<String> getSetFromString(String[] src) {

		if(src == null || src.length < 1){
			return null;
		}
		Set<String> set = new HashSet<String>();
		for(String d : src){
			set.add(d.trim());
		}
		return set;
	}

	public static Set<String> getSetFromString(String src, String split) {
		Set<String> set = new HashSet<String>();

		if(StringUtils.isBlank(src)){
			return set;
		}

		String[] data = src.split(split);		

		if(data == null || data.length < 1){
			return set;
		}
		for(String d : data){
			set.add(d.trim());
		}
		return set;
	}
	
	public static Set<Integer> getIntSetFromString(String src, String split) {
		Set<Integer> set = new HashSet<Integer>();

		if(StringUtils.isBlank(src)){
			return set;
		}

		String[] data = src.split(split);		

		if(data == null || data.length < 1){
			return set;
		}
		for(String d : data){
			if(NumericUtils.isIntNumber(d)){
				set.add(NumericUtils.parseInt(d));
			}
		}
		return set;
	}

	public static boolean isMobilePhone(String src) {
		if(src == null){
			return false;
		}
		if(src.length() != 11){
			return false;
		}
		if(!src.startsWith("1")){
			return false;
		}
		if(!src.startsWith("13") && !src.startsWith("15") && !src.startsWith("17")){
			return false;
		}
		return true;
		
	}

	public static String mergeArray(String[] array) {
		if(array == null || array.length < 1){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String data : array){
			sb.append(data).append(",");
		}
		return sb.toString().replaceAll(",$", "");
	}
	
	public static String getFormattedTime(long time){
		if(time < 1510388920693L){
			return String.valueOf(time);
		}
		return new SimpleDateFormat(CommonStandard.defaultDateFormat).format(new Date(time));
	}

	public static String getFormattedTime(Date time) {
		if(time == null){
			return null;
		}
		return new SimpleDateFormat(CommonStandard.defaultDateFormat).format(time);
	}

	public static Date parseTime(String date){
		if(StringUtils.isBlank(date)){
			return null;
		}
		try{
			return new SimpleDateFormat(CommonStandard.defaultDateFormat).parse(date);
		}catch(Exception e){
			
		}
		return null;
	}
	/**
	 * 过滤掉源字符串中的所有EMOJI表情字符
	 * 
	 *
	 * @author GHOST
	 * @date 2018-03-20
	 */
	public static String filterEmoji(String src){
		Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
		Matcher matcher = emoji.matcher(src);
		
		return matcher.replaceAll("").trim();
	}
	
	/**
	 * 过滤掉源字符串中的所有中文字符
	 * 
	 *
	 * @author GHOST
	 * @date 2018-03-20
	 */
	public static String filterChinese(String src){
		Pattern pattern = Pattern.compile ("[(\\u4e00-\\u9fa5)]" ,Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
		Matcher matcher = pattern.matcher(src);
		return matcher.replaceAll("").trim();  
	}
	/*public static String concat(Object... src){
		StringBuffer sb = new StringBuffer();

		for(Object data : src){
			sb.append(data.toString());
		}
		return sb.toString();
	}*/

	/**
	 * 生成一个指定长度、纯数字的验证码
	 * @return
	 */
	public static String generateRandomCode(int size) {
		if(size < 1) {
			size = 6;
		}
		int basicNumber = (int)Math.pow(10, size);
		int basic2 = (int)Math.pow(10, size-1);
		System.out.println("basicNumber=" + basicNumber + ",basic2=" + basic2);
		Random random = new Random();
		String randomCode=String.valueOf(random.nextInt(basicNumber - 1)%(basicNumber - basic2) + basic2);
		return randomCode;
	}
	
	/**
	 * 返回文件的扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int offset = fileName.lastIndexOf(".");
		if(offset <= 0) {
			return "";
		} else {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}
	}

}
