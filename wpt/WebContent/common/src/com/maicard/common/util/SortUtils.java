package com.maicard.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class SortUtils {

	protected static final Logger logger = LoggerFactory.getLogger(SortUtils.class);

	public static void sort(List<?> objectList, String sortField, String sortOrder){
		if(objectList == null || objectList.size() < 1){
			logger.error("尝试排序的对象列表不能为空");
			return;		
		}
		CustomComparator customComparator = new CustomComparator(sortField, sortOrder);
		Collections.sort(objectList, customComparator);

	}


}

class CustomComparator implements Comparator<Object>{

	private String sortField;
	private String sortOrder;

	protected static final Logger logger = LoggerFactory.getLogger(CustomComparator.class);


	public CustomComparator(String sortField, String sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Object o1, Object o2) {
		if(this.sortField == null){
			logger.error("无法排序因为未指定排序字段");
			return 0;
		}

		String getMethodName = "get" + StringUtils.capitalize(this.sortField);
		Object result1 = null;
		Object result2 = null;
		try {
			Method method = o1.getClass().getMethod(getMethodName, new Class<?>[]{});
			if(method == null){
				logger.info("找不到要排序字段[" + this.sortField + "]的GET方法:" + getMethodName);
				getMethodName = "getExtraValue";
				method = o1.getClass().getMethod(getMethodName, String.class);
				if(method == null){
					logger.info("找不到要排序字段[" + this.sortField + "]的扩展方法:" + getMethodName);
					return 0;
				} else {
					result1 = method.invoke(o1, this.sortField);
					result2 = method.invoke(o2, this.sortField);
				}
			} else {
				result1 = method.invoke(o1, new Object[]{});
				result2 = method.invoke(o2, new Object[]{});
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		} 
		if(result1 == null || result2 == null){
			logger.warn("执行排序字段[" + this.sortField + "]的GET方法:" + getMethodName + "，返回的数据为空");
			return 0;
		}
		int compareResult = 0;

		/*if(NumericUtils.isIntNumber(result1.toString())){
			//按照数字进行比对
			compareResult = Long.compare(Long.parseLong(result1.toString()), Long.parseLong(result2.toString()));

		} else 	if(NumericUtils.isFloatNumber(result1.toString())){
			//按照数字进行比对
			compareResult =  Double.compare(Double.parseDouble(result1.toString()), Double.parseDouble(result2.toString()));
		} else {*/
		//按照字符串排序
		Collator collator = Collator.getInstance();
		CollationKey key1 = collator.getCollationKey(result1.toString());
		CollationKey key2 = collator.getCollationKey(result2.toString());

		compareResult =  key1.compareTo(key2);
		//}
		if(this.sortOrder != null && this.sortOrder.equalsIgnoreCase("DESC")){
			return - compareResult;
		} 
		return compareResult;

	}

}
