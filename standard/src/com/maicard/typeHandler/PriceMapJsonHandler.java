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
import com.maicard.common.util.JsonUtils;
import com.maicard.money.domain.Price;

/**
 * 在Price Map对象和JSON数据之间进行转换
 * 
 *
 * @author NetSnake
 * @date 2016-02-27
 *
 */
public class PriceMapJsonHandler extends BaseService implements TypeHandler<Map<String,Price>>{

	@Override
	public Map<String,Price> getResult(ResultSet arg0, String arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	@Override
	public Map<String,Price> getResult(ResultSet arg0, int arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	@Override
	public Map<String,Price> getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Map<String,Price> feeMap,
			JdbcType arg3) throws SQLException {
		if(feeMap == null){
			arg0.setString(arg1, null);
			return;
		}
		try {
			arg0.setString(arg1, JsonUtils.getInstance().writeValueAsString(feeMap));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}		

		arg0.setString(arg1, null);


	}
	
	private Map<String,Price> convert(String text) {
		if(text == null || text.trim().equals("")){
			return null;
		}
		try{
			return JsonUtils.getInstance().readValue(text, new TypeReference<HashMap<String,Price>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("无法转换数据库内容到HashMap:" + e.getMessage());
		}		
		return null;	
		
	}

}
