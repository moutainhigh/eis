package com.maicard.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import com.maicard.standard.CommonStandard;
/**
 * 全局为Spring日期格式做转换
 * Controller中不再需要用initBinder进行配置
 * 需要在Spring配置文件中设置convertService
 *
 *
 * @author NetSnake
 * @date 2016年1月29日
 *
 */
public class DefaultDateFormatConverter  implements Converter<String, Date> {

	SimpleDateFormat dateFormat = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	private final String dateOnlyFormat = "yyyy-MM-dd";
	@Override    
	public Date convert(String source) {    
		if(StringUtils.isBlank(source)){
			return null;
		}
		dateFormat.setLenient(false); 
		if(source.length() < CommonStandard.defaultDateFormat.length() && source.length() >= dateOnlyFormat.length()){
			source = source.substring(0,dateOnlyFormat.length());
			try {
				return new SimpleDateFormat(dateOnlyFormat).parse(source);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
			}
			return null;
		}
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}   
		return null;

	}   
}
