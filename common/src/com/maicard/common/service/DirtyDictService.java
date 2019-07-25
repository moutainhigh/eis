package com.maicard.common.service;

public interface DirtyDictService {
	
	boolean isDirty(String word);

	String replace(String userMessage);

	String check(String sentence);

}
