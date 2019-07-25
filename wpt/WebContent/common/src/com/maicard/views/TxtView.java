package com.maicard.views;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;



public class TxtView extends AbstractView {

	public TxtView()
	{
	}


	@SuppressWarnings("rawtypes")
	protected void renderMergedOutputModel(Map map, HttpServletRequest arg1,
			HttpServletResponse response) throws Exception {    

		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control",
				"no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");	
		StringBuffer output = new StringBuffer();
		for(Object key : map.keySet()){
			output.append(key.toString());
			output.append(":");
			try{
				BeanInfo bif = Introspector.getBeanInfo(map.get(key).getClass());
				PropertyDescriptor pds[] = bif.getPropertyDescriptors();

				for(PropertyDescriptor pd:pds){
					// System.out.println(pd.getName());
					Method method = pd.getReadMethod();
					if(method.getName().startsWith("getClass")){
						continue;
					}

					Object result = method.invoke(map.get(key));
					if(result != null){
						System.out.println(pd.getName() + ":" + result.toString());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			output.append(map.get(key).toString());
		}
		response.getWriter().write(output.toString());
	}
}
