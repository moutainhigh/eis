package com.maicard.wpt.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.maicard.wpt.domain.ScanCodeInfo;


/**
 * 对WeixinMsg中附加的ScanCode对象，跟数据库中存储的文本格式，进行转换
 * 
 *
 * @author NetSnake
 * @date 2015年10月5日
 * 
 */
public class ScanCodeTypeHandler implements TypeHandler<ScanCodeInfo>{

	@Override
	public ScanCodeInfo getResult(ResultSet arg0, String arg1) throws SQLException {
		String dbContent = arg0.getString(arg1);
		if(dbContent == null){
			return null;
		}
		String[] data = dbContent.split("#");
		if(data == null || data.length < 2){
			return null;
		}
		ScanCodeInfo scanCodeInfo = new ScanCodeInfo();
		scanCodeInfo.setScanType(data[0]);
		scanCodeInfo.setScanResult(data[1]);
		return scanCodeInfo;
	}

	@Override
	public ScanCodeInfo getResult(ResultSet arg0, int arg1) throws SQLException {
		String dbContent = arg0.getString(arg1);
		if(dbContent == null){
			return null;
		}
		String[] data = dbContent.split("#");
		if(data == null || data.length < 2){
			return null;
		}
		ScanCodeInfo scanCodeInfo = new ScanCodeInfo();
		scanCodeInfo.setScanType(data[0]);
		scanCodeInfo.setScanResult(data[1]);
		return scanCodeInfo;
	}

	@Override
	public ScanCodeInfo getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		String dbContent = arg0.getString(arg1);
		if(dbContent == null){
			return null;
		}
		String[] data = dbContent.split("#");
		if(data == null || data.length < 2){
			return null;
		}
		ScanCodeInfo scanCodeInfo = new ScanCodeInfo();
		scanCodeInfo.setScanType(data[0]);
		scanCodeInfo.setScanResult(data[1]);
		return scanCodeInfo;
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, ScanCodeInfo scanCodeInfo,
			JdbcType arg3) throws SQLException {
		if(scanCodeInfo == null){
			arg0.setString(arg1, null);
			return;
		}
		arg0.setString(arg1, scanCodeInfo.getScanType() + "#" + scanCodeInfo.getScanResult());
		return;
	}

}
