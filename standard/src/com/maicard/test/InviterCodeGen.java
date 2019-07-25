package com.maicard.test;



import com.maicard.common.util.ShortMd5;

public class InviterCodeGen {
	public static void main(String[] argv){		
		String uuid = "303052";
		System.out.println( "p" + ShortMd5.encode(uuid));
	}
}
