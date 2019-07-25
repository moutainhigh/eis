package com.maicard.method;

import java.util.Map;

/**
 * 拥有扩展数据如Map<String,String>或自定义扩展数据如<String,ProductData>的实体类 <br/>
 * 可以实现该接口以跳过Map的判断，提供更方便、快捷的访问方法
 *
 *
 * @author NetSnake
 * @date 2016年6月10日
 *
 */
public interface ExtraObjectAccess {

	String getExtraValue(String dataCode);
	
	boolean getBooleanExtraValue(String dataCode);

	long getLongExtraValue(String dataCode);

	float getFloatExtraValue(String dataCode);
	
	void setExtraValue(String dataCode, String dataValue);

	<T>T getObject(String dataCode);

	void setExtraObject(String dataCode, Object dataValue);

	Map<String, Object> getData();

	void setData(Map<String, Object> data);




}
