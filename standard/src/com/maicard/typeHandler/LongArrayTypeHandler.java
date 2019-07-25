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
 * 返回为long[]
 *
 *
 * @author NetSnake
 * @date 2017年3月13日
 *
 */
public class LongArrayTypeHandler implements TypeHandler<long[]>{

	@Override
	public long[] getResult(ResultSet arg0, String arg1) throws SQLException {
		return convert(arg0.getString(arg1));
	}

	private long[] convert(String data) {
		if(StringUtils.isBlank(data)){
			return null;
		}
		String[] datas = data.split(",");
		long[] array = new long[datas.length];
		
		for(int i = 0; i < datas.length; i++){
			array[i] = Long.parseLong(datas[i]);
		}
		return array;
	}

	@Override
	public  long[]  getResult(ResultSet arg0, int arg1) throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public  long[]  getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, long[] arg2,
			JdbcType arg3) throws SQLException {
		if(arg2 == null || arg2.length < 1){
			arg0.setString(arg1, null);
			return;
		}
		StringBuffer sb = new StringBuffer();
		for(long x : arg2){
			sb.append(x).append(",");
		}
		
		arg0.setString(arg1, sb.toString().replaceAll(",$", ""));


	}

}
