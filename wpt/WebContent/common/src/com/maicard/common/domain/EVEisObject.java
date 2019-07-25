package com.maicard.common.domain;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraValueAccess;

/**
 * 带有ExtraValueAccess标准实现的EisObject基础类
 * 
 *
 *
 * @author NetSnake
 * @date 2018-04-27
 */
public abstract class EVEisObject extends EisObject implements ExtraValueAccess{


	private static final long serialVersionUID = 6017101980268386873L;

	final Logger logger = LoggerFactory.getLogger(getClass());

	protected Map<String,String> data;
	
	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String getExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return null;
		}
		if(this.data.containsKey(dataCode)){
			String value = this.data.get(dataCode);
			if(value == null) {
				return null;
			}
			return value.trim();
		}
		return null;	
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return false;
		}
		if(this.data.get(dataCode) != null && this.data.get(dataCode).trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}

	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return 0;
		}
		if(this.data.containsKey(dataCode) && NumericUtils.isNumeric(this.data.get(dataCode))){
			return Long.parseLong(this.data.get(dataCode));
		}
		return 0;
	}

	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return 0;
		}
		if(this.data.containsKey(dataCode) && NumericUtils.isNumeric(this.data.get(dataCode))){
			return Float.parseFloat(this.data.get(dataCode));
		}
		return 0;
	}

	@Override
	public void setExtraValue(String dataCode, String dataValue) {
		if(dataCode == null || dataValue == null) {
			return;
		}
		if(this.data == null){
			this.data = new HashMap<String,String>();
		}
		this.data.put(dataCode, dataValue);		
	}

}
