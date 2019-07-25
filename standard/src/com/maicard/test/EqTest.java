package com.maicard.test;

import java.util.LinkedHashSet;


public class EqTest {
	public static void main(String[] argv){
		LinkedHashSet<Integer> unprocessedNode = new LinkedHashSet<Integer>();
		int x = 1;
		unprocessedNode.add(x);
		
		int y = 2;
		unprocessedNode.add(y);
		
		System.out.println(unprocessedNode.size());
	}

}
