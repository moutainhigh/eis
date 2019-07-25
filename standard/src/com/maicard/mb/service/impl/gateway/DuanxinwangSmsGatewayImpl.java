package com.maicard.mb.service.impl.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.domain.SmsProvider;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.SmsService;
import com.maicard.standard.OperateResult;

/**
 * 短信网网关
 *
 *
 * @author NetSnake
 * @date 2016年4月23日
 *
 */
@Service
public class DuanxinwangSmsGatewayImpl extends BaseService implements SmsService{

	@Resource
	private ConfigService configService;

	Map<Long, SmsProvider> dxwProviderCache = null;

	private boolean sendSms(String phone, String content, long ownerId) {

		Assert.notNull(phone,"尝试发送短信的手机号不能为空");
		Assert.notNull(content,"尝试发送的短信内容不能为空");
		Assert.isTrue(ownerId > 0,"尝试发送短信的ownerId不能为0");


		String username = configService.getValue("duanxinwangUsername", ownerId);
		String password = configService.getValue("duanxinwangPassword", ownerId);
		//signSource是发送短信的前缀
		String signSource = configService.getValue("duanxinwangSignSource", ownerId);

		if(StringUtils.isBlank(username)){
			logger.error("系统找不到duanxinwangUsername配置，无法使用短信网网关");
			return false;
		}
		try {
			username = java.net.URLEncoder.encode(username,"UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		if(StringUtils.isBlank(password)){
			logger.error("系统找不到duanxinwangPassword配置，无法使用短信网网关");
			return false;
		}
		if(StringUtils.isBlank(signSource)){
			logger.error("系统找不到duanxinwangSignSource配置，无法使用短信网网关");
			return false;
		}


		logger.info("准备通过短信网下发短信到" + phone + ",内容是"+content);
		//		String sign="【以先食材】";
		content= signSource + content;
		StringBuffer sb = new StringBuffer("http://web.duanxinwang.cc/asmx/smsservice.aspx?");
		sb.append("name=");
		sb.append(username);
		sb.append("&pwd=");
		sb.append(password);
		sb.append("&mobile="+phone);
		try {
			sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		sb.append("&stime=");
		try {
			sb.append("&sign="+URLEncoder.encode("","UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		sb.append("&type=pt&extno=");
		URL url = null;
		try {
			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		logger.info("短信url是："+url);
		String result = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			InputStream is =url.openStream();
			result = convertStreamToString(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("返回结果是:"+ result);
		if( result != null && result.indexOf("提交成功")>0)	{
			return true;
		}
		else{
			return false;
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

	public String convertStreamToString(InputStream is) {    
		StringBuilder sb1 = new StringBuilder();    
		byte[] bytes = new byte[4096];  
		int size = 0;  

		try {    
			while ((size = is.read(bytes)) > 0) {  
				String str = new String(bytes, 0, size, "UTF-8");  
				sb1.append(str);  
			}  
		} catch (IOException e) {    
			e.printStackTrace();    
		} finally {    
			try {    
				is.close();    
			} catch (IOException e) {    
				e.printStackTrace();    
			}    
		}    
		return sb1.toString();    
	}




}
