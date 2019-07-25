package com.maicard.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagUtils {
	public static String removeHtml(String src){
		final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式  
	    final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式  
	    final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	    final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符  
	    Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
        Matcher m_script = p_script.matcher(src);  
        src = m_script.replaceAll(""); // 过滤script标签  
  
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
        Matcher m_style = p_style.matcher(src);  
        src = m_style.replaceAll(""); // 过滤style标签  
  
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(src);  
        src = m_html.replaceAll(""); // 过滤html标签  
  
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(src);  
        src = m_space.replaceAll(""); // 过滤空格回车标签  
        return src.trim(); // 返回文本字符串  
	}
}
