package com.maicard.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 数据库中存储逗号分割的字符串
 * 返回为String[]
 *
 *
 * @author NetSnake
 * @date 2017年3月13日
 *
 */
public class IntArrayTypeHandler implements TypeHandler<int[]>{

	@Override
	public int[] getResult(ResultSet arg0, String arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	private int[] convert(String data) {
		if(StringUtils.isBlank(data)){
			return null;
		}
		String[] datas = data.split(",");
		int[] array = new int[datas.length];
		
		for(int i = 0; i < datas.length; i++){
			array[i] = Integer.parseInt(datas[i]);
		}
		return array;
	}

	@Override
	public  int[]  getResult(ResultSet arg0, int arg1) throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public  int[]  getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, int[] arg2,
			JdbcType arg3) throws SQLException {
		if(arg2 == null || arg2.length < 1){
			arg0.setString(arg1, null);
			return;
		}
		StringBuffer sb = new StringBuffer();
		for(int x : arg2){
			sb.append(x).append(",");
		}
		
		arg0.setString(arg1, sb.toString().replaceAll(",$", ""));


	}

}
