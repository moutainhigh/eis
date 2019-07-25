package com.maicard.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.maicard.site.domain.Document;

public class ReflectTest2 {

	public static void main(String[] argv){
		System.out.println("Start");
		Document obj = new Document();

		try{
			BeanInfo bif = Introspector.getBeanInfo(Document.class);
			PropertyDescriptor pds[] = bif.getPropertyDescriptors();
			for(PropertyDescriptor pd:pds){
				System.out.println(pd.getName() + ":" + pd.getPropertyType().getName());
				if(pd.getName().equals("currentStatus")){
					//		pd.get
					Method method = pd.getWriteMethod();
					String status = "100001";
					System.out.println("type:" + pd.getPropertyType().getName());
					Object result = null;
					if(pd.getPropertyType().getName().equals("int")){
						int xxx = Integer.parseInt(status);
						result = method.invoke(obj, xxx);
					} else {
						result = method.invoke(obj, (status));
					}
					System.out.println("反射执行document的方法[" + method.getName() + "],结果:" + result  + ":" + obj.getCurrentStatus());
				}
				/*Method method = pd.getReadMethod();
				if(method.getName().startsWith("getClass")){
					continue;
				}

				Object result = method.invoke(audit);
				if(result != null){
					System.out.println(pd.getName() + ":" + result.toString());
				}*/
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
