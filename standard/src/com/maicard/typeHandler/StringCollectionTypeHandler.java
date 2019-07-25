package com.maicard.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
public class StringCollectionTypeHandler implements TypeHandler<Collection<String>>{

	@Override
	public Collection<String> getResult(ResultSet arg0, String arg1) throws SQLException {
			
		return convert(arg0.getString(arg1));
	}

	private Collection<String> convert(String string) {
		String[] data = string.split(",");
		if(data == null || data.length < 1){
			return null;
		}
		Set<String> set = new HashSet<String>();
		for(String da : data){
			set.add(da.trim());
		}
		return set;
	}

	@Override
	public  Collection<String>  getResult(ResultSet arg0, int arg1) throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public  Collection<String> getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		return convert(arg0.getString(arg1));

	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Collection<String> arg2,
			JdbcType arg3) throws SQLException {
		if(arg2 == null){
			arg0.setString(arg1, null);
			return;
		}
		if(arg2 == null || arg2.size() < 1){
			arg0.setString(arg1, null);
			return;
		}
		StringBuffer sb = new StringBuffer();
		for(String x : arg2){
			sb.append(x).append(",");
		}
		
		arg0.setString(arg1, sb.toString().replaceAll(",$", ""));


	}

}
