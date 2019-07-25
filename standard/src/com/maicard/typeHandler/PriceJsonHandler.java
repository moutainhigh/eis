package com.maicard.typeHandler;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.money.domain.Price;
import com.maicard.standard.CommonStandard;

/**
 * 在Price对象和JSON数据之间进行转换
 * 
 *
 * @author NetSnake
 * @date 2016-02-27
 *
 */
public class PriceJsonHandler extends BaseService implements TypeHandler<Price>{
	
	static ObjectMapper om = new ObjectMapper();
	
	static {
		om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
		//使用NON_DEFAULT避免一切默认和空值，最大限度的减少存储所需要的空间
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		om.setTimeZone(TimeZone.getDefault());
	}	

	@Override
	public Price getResult(ResultSet arg0, String arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	@Override
	public Price getResult(ResultSet arg0, int arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	@Override
	public Price getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Price price,
			JdbcType arg3) throws SQLException {
		if(price == null){
			arg0.setString(arg1, null);
			return;
		}
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		try {
			arg0.setString(arg1, om.writeValueAsString(price));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}		

		arg0.setString(arg1, null);


	}
	
	private Price convert(String text) {
		if(text == null || text.trim().equals("")){
			return null;
		}
		try{
			return om.readValue(text, Price.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("无法转换数据库内容到HashMap:" + e.getMessage());
		}		
		return null;	
		
	}
	
	/*public static void main(String[] argv){
		Price p = new Price();
		p.setMoney(1000);
		try {
			System.out.println(om.writeValueAsString(p));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
