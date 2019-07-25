package com.maicard.common.domain;

import java.io.Serializable;

import com.maicard.standard.CommonStandard;

/**
 * 用于在工作流过程中，控制对象的属性
 * 如何操作对象的这些属性
 *
 *
 * @author NetSnake
 * @date 2016年6月9日
 *
 */
public class Attribute implements Serializable{


	private static final long serialVersionUID = 8363142954880669295L;

	private String columnType = CommonStandard.COLUMN_TYPE_NATIVE;
	

	private String columnName;

	private boolean readonly;			//是否不可写入

	private int hidden = 0;			//是否显示：0显示，1不显示，作为隐藏字段，2不输出

	private boolean required;			//是否必须输入
	
	
	private String inputMethod;			//输入方式

	private String[] validValue;			//有效的值，如果为空则为任意值

	private int currentStatus;			//状态
	
	private String useMessagePrefix;			//使用message中的字段名前缀

	private int weight;					//数字越大越靠前

	public Attribute(){

	}

	public Attribute(String type, String name, String inputMethod, boolean readonly, int hidden, boolean required, String... validValue){
		this.columnType = type;
		this.columnName = name;
		this.inputMethod = inputMethod;
		this.useMessagePrefix = CommonStandard.DEFAULT_MESSAGE_PREFIX;
		this.readonly = readonly;
		this.hidden = hidden;
		this.required = required;
		this.validValue = validValue;
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

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String[] getValidValue() {
		return validValue;
	}

	public void setValidValue(String... validValue) {
		this.validValue = validValue;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getInputMethod() {
		return inputMethod;
	}

	public void setInputMethod(String inputMethod) {
		this.inputMethod = inputMethod;
	}

	public String getUseMessagePrefix() {
		return useMessagePrefix;
	}

	public void setUseMessagePrefix(String useMessagePrefix) {
		this.useMessagePrefix = useMessagePrefix;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHidden() {
		return hidden;
	}

	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
}
