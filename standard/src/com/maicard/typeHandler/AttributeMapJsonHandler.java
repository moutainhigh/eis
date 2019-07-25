package com.maicard.typeHandler;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Attribute;
import com.maicard.common.util.JsonUtils;

/**
 * 在Attribute Map对象和JSON数据之间进行转换
 * 
 *
 * @author NetSnake
 * @date 2016-06-14
 *
 */
public class AttributeMapJsonHandler extends BaseService implements TypeHandler<Map<String,Attribute>>{

	@Override
	public Map<String,Attribute> getResult(ResultSet arg0, String arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	@Override
	public Map<String,Attribute> getResult(ResultSet arg0, int arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	@Override
	public Map<String,Attribute> getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Map<String,Attribute> attributeMap,
			JdbcType arg3) throws SQLException {
		if(attributeMap == null){
			arg0.setString(arg1, null);
			return;
		}
		try {
			arg0.setString(arg1, JsonUtils.getInstance().writeValueAsString(attributeMap));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}		

		arg0.setString(arg1, null);


	}
	
	private Map<String,Attribute> convert(String text) {
		if(text == null || text.trim().equals("")){
			return null;
		}
		try{
			return JsonUtils.getInstance().readValue(text, new TypeReference<HashMap<String,Attribute>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("无法转换数据库内容到HashMap:" + e.getMessage());
		}		
		return null;	
		
	}

}
