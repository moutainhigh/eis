package com.maicard.mb.service.impl.gateway;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.mb.domain.SmsProvider;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.SmsService;
import com.maicard.standard.OperateResult;

@Service
public class EmaySmsGatewayImpl extends BaseService implements  SmsService{

	@Resource
	private CacheService cacheService;
	@Resource
	private ConfigService configService;
	
	

	private static Map<String, SmsProvider> emayProviderCache = new HashMap<String, SmsProvider>();
	

	private boolean sendSms(String phone, String content, long ownerId) {


		Assert.notNull(phone,"尝试发送短信的手机号不能为空");
		Assert.notNull(content,"尝试发送的短信内容不能为空");
		Assert.isTrue(ownerId > 0,"尝试发送短信的ownerId不能为0");
		
		
		SmsProvider smsProvider = getProvider(ownerId);
		if(smsProvider == null){
			logger.error("无法发送短信因为易美网关未配置");
			return false;
		}
		



		content= smsProvider.prefixSign + content;
		try{ 
			logger.info("短信原文是:"+content+"\r\n");
			content=java.net.URLEncoder.encode(content,"UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		String url = null;
		
		if(smsProvider.username.startsWith("3SDK")){
			url = "http://sdk4http.eucp.b2m.cn:8080/sdkproxy/sendsms.action";
		} else {
			url = "http://sdk4rptws.eucp.b2m.cn:8080/sdkproxy/sendsms.action";
		}
		
		
		 url = url + "?cdkey="+ smsProvider.username +"&password="+ smsProvider.password +
				"&phone="+phone+"&message="+content+"&addserial=&seqid=0&smspriority=1";
		String result=HttpUtils.sendData(url);
		logger.info("短信url是："+url+"|下发短信的内容是"+content+"返回结果是:"+result);
		if (result.indexOf("<error>0</error>")>=0){
			return true;
		}
		return false;

	}




	private SmsProvider getProvider(long ownerId) {
		SmsProvider smsProvider = emayProviderCache.get(String.valueOf(ownerId));
		if(smsProvider != null){
			logger.debug("从缓存中返回ownerId=" + smsProvider.ownerId + "的易美网关配置");
			return smsProvider;
		}
		String username = configService.getValue("emaySmsUsername", ownerId);
		String password = configService.getValue("emaySmsPassword", ownerId);
		String signSource = configService.getValue("emaySmsSignSource", ownerId);

		if(StringUtils.isBlank(username)){
			logger.error("系统找不到emaySmsUsername配置，无法使用短信网网关");
			return null;
		}
		if(StringUtils.isBlank(password)){
			logger.error("系统找不到emaySmsPassword配置，无法使用短信网网关");
			return null;
		}
		if(StringUtils.isBlank(signSource)){
			logger.error("系统找不到emaySmsSignSource配置，无法使用短信网网关");
			return null;
		}
		smsProvider = new SmsProvider(username,password,signSource,ownerId);
		String url = null;
		if(smsProvider.username == null){
			logger.error("无法激活易美短信网关，没有配置数据");
			return null;
		}
		if(smsProvider.username.startsWith("3SDK")){
			url = "http://sdk4http.eucp.b2m.cn:8080/sdkproxy/regist.action?cdkey="+smsProvider.username+"&password="+smsProvider.password;
		} else {
			url = "http://sdk4rptws.eucp.b2m.cn:8080/sdkproxy/regist.action?cdkey="+smsProvider.username+"&password="+smsProvider.password;
		}
		String i=HttpUtils.sendData(url);
		logger.info("激活易美短信网关[" + smsProvider.username + "/" + smsProvider.password + "]的返回值是:"+i);
		logger.debug("初始化易美网关并放入缓存，返回ownerId=" + smsProvider.ownerId + "的易美网关配置");
		return smsProvider;

		
	}




	@PreDestroy
	public void destory(){
		if(emayProviderCache == null || emayProviderCache.size() < 1){
			logger.debug("当前系统中没有易美缓存");
			return;
		}
		for(SmsProvider smsProvider : emayProviderCache.values()){
			String url = null;
			if(smsProvider.username == null){
				logger.error("无法退出易美短信网关，没有配置数据");
				continue;
			}
			if(smsProvider.username.startsWith("3SDK")){
				url = "http://sdk4http.eucp.b2m.cn:8080/sdkproxy/logout.action?cdkey="+smsProvider.username+"&password="+smsProvider.password;
			} else {
				url = "http://sdk4rptws.eucp.b2m.cn:8080/sdkproxy/logout.action?cdkey="+smsProvider.username+"&password="+smsProvider.password;
			}
			String i=HttpUtils.sendData(url);
			logger.info("注销ownerId=" + smsProvider.ownerId + "的短信网关[" + smsProvider.username + "/" + smsProvider.password + "]的返回值是:" + i);
		}
	}




	@Override
	public boolean send(UserMessage message) throws Exception {
		if(message == null){
			logger.error("尝试发送的用户消息为空");
			return false;
		}
		if(message.getReceiverName() == null){
			logger.error("尝试发送的收件人为空");
			message.setSenderStatus(OperateResult.failed.getId());
			return false;
		}
		if(message.getContent() == null){
			logger.error("尝试发送的消息内容为空");
			message.setSenderStatus(OperateResult.failed.getId());
			return false;
		}	
		return send(message.getReceiverName(), message);
	}




	@Override
	public boolean send(String receiver, UserMessage message) {
		if(message == null){
			logger.error("尝试发送的用户消息为空");
			return false;
		}
		if(StringUtils.isBlank(receiver)){
			logger.error("尝试发送的收件人为空");
			message.setSenderStatus(OperateResult.failed.getId());
			return false;
		}
		if(message.getContent() == null){
			logger.error("尝试发送的消息内容为空");
			message.setSenderStatus(OperateResult.failed.getId());
			return false;
		}	
		if(sendSms(receiver, message.getContent(), message.getOwnerId())){
			message.setSenderStatus(OperateResult.success.getId());
			return true;
		}
		message.setSenderStatus(OperateResult.failed.getId());
		return false;			
	} 



}

