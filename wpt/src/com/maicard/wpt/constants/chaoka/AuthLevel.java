package com.maicard.wpt.constants.chaoka;

public enum AuthLevel {
	REAL_NAME(3);
	
	public final int id;

	private AuthLevel(int id){
		this.id = id;
	}
}
