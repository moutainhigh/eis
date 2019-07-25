package com.maicard.test;

import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class py {
	public static String getPingYin(String src){
		char[] t1 = null;
		t1=src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4="";
		int t0=t1.length;
		try {
			for (int i=0;i<t0;i++)
			{
				//判断是否为汉字字符
				if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
				{
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4+=t2[0];
				}
				else
					t4+=java.lang.Character.toString(t1[i]);
			}
			//		       System.out.println(t4);
			return t4;
		}
		catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	//返回中文的首字母
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			}else {
				convert += word;
			}
		}
		return convert;
	}
	//将字符串转移为ASCII码
	public static String getCnASCII(String cnStr)
	{
		StringBuffer   strBuf   =   new   StringBuffer();
		byte[]   bGBK   =   cnStr.getBytes();
		for(int   i=0;i <bGBK.length;i++){
			strBuf.append(Integer.toHexString(bGBK[i]&0xff));
		}
		return strBuf.toString();
	}
	public static String makepy(int n){
		String str ="";
		int hightPos, lowPos,i; // 定义高低位
		for (i=0;i<n;i++){
			Random random = new Random();
			hightPos = (176 + Math.abs(random.nextInt(39)));//获取高位值
			lowPos = (161 + Math.abs(random.nextInt(93)));//获取低位值
			byte[] b = new byte[2];
			b[0] = (new Integer(hightPos).byteValue());
			b[1] = (new Integer(lowPos).byteValue());
			try
			{
				str =str+ new String(b,"GBK");//转成中文
			}
			catch (Exception e){}
		}
		return str;        
	}
	public static String getRandom(int min, int max)
	{
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return String.valueOf(s);
	}		
	public static void main(String[] args) {
		int i=0;
		for (i=0;i<5000;i++){
			String str=makepy(3);
			str=str+String.valueOf(getRandom(1980,2000));
			System.out.println(getPingYin(str));
			str="";
		}
	}
}
