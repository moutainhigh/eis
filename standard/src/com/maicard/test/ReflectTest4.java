package com.maicard.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.maicard.common.domain.Audit;
import com.maicard.common.domain.EisMessage;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.standard.CommonStandard;

public class ReflectTest4 {

	@SuppressWarnings("rawtypes")
	public static void main(String[] argv){
		Document document = new Document();
		Method[] methods = document.getClass().getMethods();
		StringBuffer message = new StringBuffer();
		//String value = "100001";
		Node value = new Node();
		for(Method method : methods){
			//System.out.println(method.getName());

			//if(method.getName().startsWith("setCurrentStatus")){
			Class[] types = method.getParameterTypes();
			for(Class clazz : types){
				System.out.println("parameter type:" + clazz.getName());
				//clazz.cast(value);
			}
			if(types.length != 1){
				System.out.println("方法[" + method.getName() + "]没有参数或有多个参数");
				continue;
			}
			Class paraClass = types[0];
			System.out.println("测试方法:" + method.getName() + ",参数类型:" + paraClass.getName());
			if(value.getClass().equals(paraClass)){		
				System.err.println("参数类型与提供的参数类型一致");
				try {
					method.invoke(document, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;

			}
			/*PropertyEditor pe = PropertyEditorManager.findEditor(paraClass);
			if(pe == null){
				System.out.println("找不到[" + paraClass + "]的属性编辑器");
				continue;
			}
			System.out.println("执行方法:" + method.getName());
			if(value.getClass().equals(paraClass)){				
				pe.setValue(value);				
			} else {
				pe.setAsText(value.toString());
			}
			try {
				method.invoke(document, pe.getValue());
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}	*/


		}
		System.out.println(document.getCurrentStatus());
	}

}
