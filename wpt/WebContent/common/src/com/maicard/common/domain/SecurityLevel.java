package com.maicard.common.domain;

import java.util.HashMap;

public class SecurityLevel extends EisObject{
	
	private static final long serialVersionUID = -3509744174161406367L;

	private int level;
	
	private String name;
	
	private String description;
	
	private HashMap<String,String>data;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("@");
		sb.append(Integer.toHexString(hashCode()));
		sb.append("(");
		sb.append("level=");
		sb.append("'");
		sb.append(level);
		sb.append("',");
		sb.append("name=");
		sb.append("'");
		sb.append(name);
		sb.append("')");
		return sb.toString();
	}

}
