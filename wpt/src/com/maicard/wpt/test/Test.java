package com.maicard.wpt.test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.maicard.common.domain.Attribute;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.JsonUtils;
import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.domain.PayMethod;

public class Test {
	public static void main(String[] argv) {
		
		List<PayMethod> payMethodList = new ArrayList<PayMethod>();
		
		PayMethod p1 = new PayMethod();
		p1.setName("T1");
		p1.setPayTypeId(1);
		p1.setCurrentStatus(100002);
		
		payMethodList.add(p1);
		
		
		PayMethod p2 = new PayMethod();
		p2.setName("T2");
		p2.setPayTypeId(2);
		p2.setCurrentStatus(100001);
		
		payMethodList.add(p2);
		
		System.out.println(JSON.toJSONString(payMethodList));
		PayMethodCriteria payMethodCriteria = new PayMethodCriteria();
		payMethodCriteria.setCurrentStatus(100001);
		payMethodCriteria.setPayTypeId(2);
	try {
			List<PayMethod> list2 = ClassUtils.search(payMethodList, payMethodCriteria);
			System.out.println("过滤后的:" + list2.size() + "======>" + JSON.toJSONString(list2));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
