package com.maicard.common.domain;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraObjectAccess;

/**
 * 带有ExtraValueAccess标准实现的EisObject基础类
 * 
 *
 *
 * @author NetSnake
 * @date 2018-04-27
 */
public abstract class EOEisObject extends EisObject implements ExtraObjectAccess{


	private static final long serialVersionUID = 6017101980268386873L;

	final Logger logger = LoggerFactory.getLogger(getClass());

	protected Map<String,Object> data;
	
	@Override
	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String getExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return null;
		}
		if(this.data.containsKey(dataCode) && this.data.get(dataCode) != null){
			return this.data.get(dataCode).toString().trim();
		}
		return null;	
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return false;
		}
		if(this.data.get(dataCode) != null && this.data.get(dataCode).toString().trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}

	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return 0;
		}
		Object obj = this.data.get(dataCode);
		if(obj == null) {
			return 0;
		}
		return NumericUtils.parseLong(obj);
	}

	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return 0;
		}
		Object obj = this.data.get(dataCode);
		if(obj == null) {
			return 0;
		}
		return NumericUtils.parseFloat(obj);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T>T getObject(String dataCode){
		if(this.data == null || this.data.size() < 1){
			return null;
		}
		Object obj = this.data.get(dataCode);
		if(obj == null) {
			return null;
		}
		return (T)obj;
	}
	@Override
	public void setExtraValue(String dataCode, String dataValue) {

		if(this.data == null){
			this.data = new HashMap<String,Object>();
		}
		if(dataValue == null) {
			this.data.remove(dataCode);
		} else {
			this.data.put(dataCode, dataValue);	
		}
	}
	
	@Override
	public void setExtraObject(String dataCode, Object dataValue) {

		if(this.data == null){
			this.data = new HashMap<String,Object>();
		}
		if(dataValue == null) {
			this.data.remove(dataCode);
		} else {
			this.data.put(dataCode, dataValue);	
		}
	}

}
