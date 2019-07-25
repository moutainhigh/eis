package com.maicard.common.domain;

import java.io.Serializable;

import com.maicard.standard.CommonStandard;

/**
 * 用于管理系统中，自定义显示的字段
 *
 *
 * @author NetSnake
 * @date 2016年6月9日
 *
 */
public class Column implements Serializable{


	private static final long serialVersionUID = 7511190096209876848L;

	private int weight;
	
	private String columnType = CommonStandard.COLUMN_TYPE_NATIVE;
	
	private String columnName;
	
	private String useMessagePrefix;			//使用message中的字段名前缀
	
	private String format;			//格式，比如时间
	
	private int currentStatus;			//状态

	public Column(){
		
	}
	public Column(String columnName, String columnType, String useMessagePrefix, int weight, int currentStatus) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.useMessagePrefix = useMessagePrefix;
		this.weight = weight;
		this.currentStatus = currentStatus;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	
	public int getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getUseMessagePrefix() {
		return useMessagePrefix;
	}
	public void setUseMessagePrefix(String useMessagePrefix) {
		this.useMessagePrefix = useMessagePrefix;
	}
	

}
