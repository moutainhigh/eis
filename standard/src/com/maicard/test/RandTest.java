package com.maicard.test;

import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

public class RandTest {
	enum operateType{delete,create};

	public static void main(String[] argv){
		//int rand = (new Random().nextInt(89) + 10);
		for(int i = 0; i < 10; i ++){
			System.out.println(RandomUtils.nextInt(9));
		}
		
	}
}
