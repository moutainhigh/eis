package com.maicard.test;

import java.beans.*;
import java.lang.reflect.Method;

import com.maicard.common.pe.IntArrayPropertyEditor;
import com.maicard.product.criteria.ItemCriteria;

public class ReflectTest3 {

	public static void main(String[] argv){



		ItemCriteria itemCriteria = new ItemCriteria();
		PropertyEditorManager.registerEditor(int[].class, IntArrayPropertyEditor.class);
		StringBuffer sb = new StringBuffer();
		BeanInfo bif = null;
		try {
			bif = Introspector.getBeanInfo(ItemCriteria.class);
			PropertyDescriptor pds[] = bif.getPropertyDescriptors();
			for(PropertyDescriptor pd:pds){
				//	System.out.println(pd.getName());
				if(pd.getName().equals("currentStatus")){
					Method writeMethod = pd.getWriteMethod();
					PropertyEditor pe = PropertyEditorManager.findEditor(pd.getPropertyType());
					System.out.println("pe=" + pe);
					if(pe != null ){
						pe.setAsText("100");
						writeMethod.invoke(itemCriteria, pe.getValue());
					}
					//System.out.println(pd.getPropertyType().getName() + "/" + pd.getPropertyType().getSimpleName());
					 
					/*for(Class<?> p :writeMethod.getParameterTypes()){
						System.out.println("PARA=>" + p.isArray());//.isArray());
					}
					if(writeMethod.getParameterTypes() != null && writeMethod.getParameterTypes().length > 0 
							&& writeMethod.getParameterTypes()[0].isArray()){
						//Method method2 = itemCriteria.getClass().getMethod("setCurrentStatus", int[].class);
						Object objs = Array.newInstance(int.class, 2);
						Array.set(objs, 0, 1);
						Array.set(objs, 1, 2);
						writeMethod.invoke(itemCriteria, objs);

					}*/

				}	
				Object value = pd.getReadMethod().invoke(itemCriteria);
				if(value != null){
					if(pd.getName().equals("currentStatus")){
						int[] test = (int[])value;
						for(int x : test){
							System.out.println(x);
						}
					}
					//		System.out.println(pd.getName() + "=>" + value.toString());
					//	sb.append("&" + pd.getName() + "=" + value.toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println( sb.toString() );
	}

}
