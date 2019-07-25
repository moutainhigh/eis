package com.maicard.test;

import java.lang.reflect.Method;

import com.maicard.common.domain.Audit;
import com.maicard.site.domain.Document;

public class ReflectTest {

	public static void main(String[] argv){
		Document document = new Document();
		Method[] methods = document.getClass().getMethods();
		StringBuffer message = new StringBuffer();
		String value = "100001";
		for(Method method : methods){
			//System.out.println(method.getName());
			
			if(method.getName().startsWith("setCurrentStatus")){
				Class[] types = method.getParameterTypes();
				for(Class clazz : types){
					System.out.println("parameter type:" + clazz.getName());
					//clazz.cast(value);
				}
				System.out.println("Start");
			try{
					Object result = method.invoke(document, new Object[]{"100001"});
					if(result != null){
						message.append(result.toString());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		System.out.println(document.getCurrentStatus());
	}

}
