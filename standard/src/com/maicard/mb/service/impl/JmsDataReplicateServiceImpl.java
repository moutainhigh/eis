/**
 * 
 */
package com.maicard.mb.service.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.EisObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.JmsDataReplicateService;
import com.maicard.site.service.SiteReplicator;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import org.springframework.scheduling.annotation.Async;

/**
 * 
 *
 * @author NetSnake
 * @date 2013-10-17
 */
public class JmsDataReplicateServiceImpl extends BaseService implements JmsDataReplicateService {

	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private ConfigService configService;

	private boolean handlerJmsDataSyncToLocal = false;

	@PostConstruct
	public void init(){
		handlerJmsDataSyncToLocal = configService.getBooleanValue(DataName.handlerJmsDataSyncToLocal.toString(),0);
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	@Async
	public void operate(EisMessage eisMessage) {
		if(handlerJmsDataSyncToLocal){
			String beanName = null;
			String methodName = null;
			Object parameters = null;
			try{
				beanName = eisMessage.getAttachment().get("updateSlaveBeanName").toString();
				methodName = eisMessage.getAttachment().get("updateSlaveMethodName").toString();
				parameters = eisMessage.getAttachment().get("updateSlaveParamaters");

			}catch(Exception e){}
			if(beanName == null){
				logger.error("消息操作模式是更新副本，但未指定更新的bean名称");
				eisMessage = null;
				return;
			}
			if(methodName == null){
				logger.error("消息操作模式是更新副本，但未指定更新的bean方法");
				eisMessage = null;
				return;
			}
			Object bean = applicationContextService.getBean(beanName);
			if(bean == null){
				logger.warn("本节点中找不到指定的bean[" + beanName + "]");
				eisMessage = null;
				return;
			}
			if(logger.isDebugEnabled()){
				logger.debug("接收到JMS数据复制请求:bean[" + beanName + "]类型:" + bean.getClass().getName() + "." + methodName + "(" + parameters + ")");
			}
			Object object = getValidSiteReplicationObject(parameters);
			if(object != null){
				eisMessage.getAttachment().put("updateSlaveParamaters",object);
				String[] siteReplicatores = applicationContextService.getBeanNamesForType(SiteReplicator.class);
				if(siteReplicatores == null || siteReplicatores.length < 1){
					logger.info("系统中找不到站点复制转换器bean[SiteReplicator]");
					eisMessage = null;
					return;
				}
				for(String bn : siteReplicatores){
					SiteReplicator  siteReplicatorBean = null;
					try{
						siteReplicatorBean = (SiteReplicator)applicationContextService.getBean(bn);
					}catch(Exception e){
						logger.error("无法将Bean[" + bn + "]转换为SiteReplicator类型");
					}
					if(siteReplicatorBean != null){
						siteReplicatorBean.convert(object);
					}				
				}
				Method[] methods = bean.getClass().getMethods();
				for(Method method : methods){
					//logger.debug("尝试匹配方法[" + methodName + "]===[" + method.getName() + "]");
					if(method.getName().equals(methodName)){
						Class[] types = method.getParameterTypes();
						if(types.length != 1){
							logger.error("bean[" + beanName + "]方法[" + methodName + "]支持的参数不是1，目前只支持一个参数的调用");
							break;
						}
						if(parameters.getClass().equals(Integer.class) && types[0].getName().equals("int")){
							if(logger.isDebugEnabled()){
								logger.debug("尝试调用服务[" +beanName + "]执行方法[" + methodName + "],整形参数[" + parameters + "]");
							}
							try {
								method.invoke(bean, parameters);
							} catch(Exception e) {
								logger.error("在调用服务[" +beanName + "]执行方法[" + methodName + "],整形参数[" + parameters + "]时出错:" + e.getMessage());
							}
							break;
						}
						if(object.getClass().equals(types[0])){
							if(logger.isDebugEnabled()){
								logger.debug("尝试调用服务[" +beanName + "]执行方法[" + methodName + "],参数[" + object + "]");
							}
							try {
								method.invoke(bean, object);
								if(logger.isDebugEnabled()){
									logger.debug("成功调用服务[" +beanName + "]执行方法[" + methodName + "],参数[" + object + "]，参数是否为EisObject:" + (object instanceof EisObject));
								}
								
							} catch (Exception e) {
								logger.error("在调用服务[" +beanName + "]执行方法[" + methodName + "],参数[" + object + "]时出错:" + e.getMessage());
							}
							break;
						} 
					}
				}
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("JMS数据复制不支持此对象类型:" + parameters.getClass().getName() + "，只支持SiteReplicationObject中指定的对象的复制");
				}
			}
		} else {
			if(logger.isDebugEnabled()){
				logger.debug("本节点不负责将JMS的数据更新到本地，忽略消息");
			}
		}
		eisMessage = null;
		return;

	}

	@Override
	public Object getValidSiteReplicationObject(Object parameters) {
		if(parameters == null){
			return null;
		}
		if(parameters.getClass().equals(Integer.class)){
			return parameters;
		}
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
		/*for(SiteReplicationObject siteReplicationObject : SiteReplicationObject.values()){
			Object object = null;
			String textData = null;
			try{
				textData = om.writeValueAsString(parameters);
		//FIXME		object = om.readValue(textData, siteReplicationObject.getClazz());
			}catch(Exception e){}
			if(object == null){
				textData = null;
			} else {
				textData = null;
				om = null;
				return object;

			}
		}*/
		return null;
	}


}
