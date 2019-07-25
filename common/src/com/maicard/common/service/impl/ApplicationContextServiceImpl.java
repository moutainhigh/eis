package com.maicard.common.service.impl;


import java.lang.annotation.Annotation;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;

@Service
public class ApplicationContextServiceImpl extends BaseService implements ApplicationContextService,ApplicationContextAware,ServletContextAware {

	@Override
	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getBean(String beanName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMemoryInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocaleMessage(String messageCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void directResponseException(HttpServletRequest request, HttpServletResponse response, Throwable t)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getThreadCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalStartedThreadCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPerformanceInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getBeanNamesForType(@SuppressWarnings("rawtypes") Class clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNginxConnection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getDatabaseWaiting(String dataSource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDatabaseActive(String dataSource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDatabaseMaxActive(String dataSource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Annotation findAnnotationOnBean(String beanName, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getPerformanceRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T getBeanGeneric(String beanName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return null;
	}

	@Override
	public String getDataDir() {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
