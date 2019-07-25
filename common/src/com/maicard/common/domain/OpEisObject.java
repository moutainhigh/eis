package com.maicard.common.domain;

import com.maicard.method.Operatable;

/**
 * 具备可购买、可操作的基本对象
 * @author GHOST
 * @date 2018-10-23
 *
 */
public class OpEisObject extends EOEisObject implements Operatable{

	private static final long serialVersionUID = -2252702881312867383L;
	
	protected String name;
	
	protected String brief;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}
	
	public  String getViewUrl() {
		return null;
	}
	
	
	
	

}
