package com.maicard.typeHandler;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maicard.common.base.BaseService;
import com.maicard.common.util.JsonUtils;

/**
 * 使用jackson把<String,Object>类型的Hash表以文本形式存放在数据库中
 * 读出时，把JSON文本转换为<String,Object>类型的Hash表
 * 
 * 用于一些简单的基本配置键值对存放，并能在数据库直接进行修改
 *
 *
 * @author NetSnake
 * @date 2015年11月29日
 *
 */
public class ObjectHashMapTypeHandler extends BaseService implements TypeHandler<Map<String,Object>>{

	@Override
	public Map<String,Object> getResult(ResultSet arg0, String arg1) throws SQLException {
		String text = arg0.getString(arg1);
		return text2Map(text);
	}

	

	@Override
	public Map<String,Object> getResult(ResultSet arg0, int arg1) throws SQLException {
		String text = arg0.getString(arg1);
		return text2Map(text);
	}

	@Override
	public Map<String,Object> getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		String text = arg0.getString(arg1);
		return text2Map(text);
		
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Map<String,Object> arg2,
			JdbcType arg3) throws SQLException {
		if(arg2 == null){
			arg0.setString(arg1, null);
			return;
		}
		try {
			arg0.setString(arg1, JsonUtils.getInstance().writeValueAsString(arg2));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}		

		arg0.setString(arg1, null);


	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> text2Map(String text) {
		if(text == null || text.trim().equals("")){
			return null;
		}
		try{
			Object object = JsonUtils.getInstance().readValue(text, new TypeReference<Map<String,Object>>(){});
			return (Map<String,Object>)object;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("无法转换数据库内容到Map<String,Object>:" + e.getMessage());
		}		
		return null;	
	}

}
