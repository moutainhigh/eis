package com.maicard.mb.service.impl.gateway;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.SmsService;
import com.maicard.standard.OperateResult;
/**
 * 
 * @author henry
 * @date 2018/1/25
 * 阿里云有自己的短信模板,这里将title设置为随机验证码,将sign作为获取模板的标识
 */
public class AliYunSmsGatewayImpl extends BaseService implements SmsService{

	@Resource
	private ConfigService configService;
	
	//产品名称:云通信短信API产品,开发者无需替换
    private final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private final String domain = "dysmsapi.aliyuncs.com";
    
    private String accessKeyId = null;
    private String accessKeySecret = null;

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
		if(sendSms(receiver, message.getTitle(), message.getOwnerId(),message.getSign())){
			message.setSenderStatus(OperateResult.success.getId());
			return true;
		}
		message.setSenderStatus(OperateResult.failed.getId());
		return false;
	}

	private boolean sendSms(String phone, String content, long ownerId , String sign) {
		Assert.notNull(phone,"尝试发送短信的手机号不能为空");
		Assert.notNull(content,"尝试发送的短信内容不能为空");
		Assert.notNull(sign,"尝试发送的短信模板不能为空");
		Assert.isTrue(ownerId > 0,"尝试发送短信的ownerId不能为0");

        String aliYunSmsSignName = configService.getValue("aliYunSmsSignName", ownerId);
        String aliYunTemplate = configService.getValue("aliYunTemplate", ownerId);
        accessKeyId = configService.getValue("aliYunAccessKeyId", ownerId);
        accessKeySecret = configService.getValue("aliYunAccessKeySecret", ownerId);
        
        if(StringUtils.isBlank(aliYunSmsSignName)){
			logger.error("系统找不到aliYunSmsSignName配置，无法使用短信网网关");
			return false;
		}
        if(StringUtils.isBlank(aliYunTemplate)){
        	logger.error("系统找不到aliYunTemplate配置，无法使用短信网网关");
        	return false;
        }
        if(StringUtils.isBlank(accessKeyId)){
        	logger.error("系统找不到aliYunAccessKeyId配置，无法使用短信网网关");
        	return false;
        }
        if(StringUtils.isBlank(accessKeySecret)){
        	logger.error("系统找不到aliYunAccessKeySecret配置，无法使用短信网网关");
        	return false;
        }
        String templete = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
			Map map = mapper.readValue(aliYunTemplate, Map.class);
			if (map == null) {
				logger.error("aliyunTemplate转换成map失败");
				return false;
			}
			if (map.get(sign) == null) {
	        	logger.error("系统未配置类型为{}的模板",sign);
	        	return false;
			}
			templete = (String) map.get(sign);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(aliYunSmsSignName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templete);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        request.setTemplateParam("{\"code\":\""+content+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        try {
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			return true;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
		return false;
	}

}
