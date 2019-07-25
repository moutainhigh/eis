package com.maicard.common.pe;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.maicard.standard.CommonStandard;


/**
 * 用于执行反射功能时<br/>
 * 把String类型转换为Date类型<br/>
 * 使用<br/>
 * PropertyEditorManager.registerEditor(Date.class, StringDatePropertyEditor.class);
 *
 *
 * @author NetSnake
 * @date 2016-07-07
 *
 */
public class StringDatePropertyEditor extends PropertyEditorSupport  {
	private  Date date;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	@Override
	public void setAsText(String str){
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Date getValue(){
		return date;
	}


}
