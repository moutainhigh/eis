package com.maicard.mb.service.impl.gateway;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.MessageGatewayService;
import com.maicard.standard.DataName;
import com.maicard.standard.OperateResult;


@Service
public class SmtpGatewayImpl extends BaseService implements MessageGatewayService{

	@Resource
	private ConfigService configService;	

	String domainName = null;
	String mailSenderName = null;
	String mailSenderPassword = null;
	String smtpServer = null;
	int smtpPort = 25;

	private boolean serviceOk = false;

	@PostConstruct
	public void init(){
		//domainName = configService.getValue(DataName.siteDomain.toString(),0);
		mailSenderName = configService.getValue(DataName.mailSenderName.toString(),0);
		mailSenderPassword = configService.getValue(DataName.mailSenderPassword.toString(),0);
		smtpServer = configService.getValue(DataName.mailSmtpHost.toString(),0);
		smtpPort = configService.getIntValue(DataName.mailSmtpPort.toString(),0);

		if(StringUtils.isBlank(smtpServer)){
			logger.error("系统未配置邮件网关主机地址，邮件服务将不可用");
			return;
		}
		if(StringUtils.isBlank(mailSenderName)){
			logger.error("系统未配置邮件网关用户名，邮件服务将不可用");
			return;
		}		
		if(StringUtils.isBlank(mailSenderPassword)){
			logger.warn("系统未配置邮件网关主机地址，邮件服务可能工作异常");
		}
		if(smtpPort == 0){
			if(logger.isInfoEnabled()){
				logger.info("系统未配置邮件网关主机端口，使用默认端口25");
			}
			smtpPort = 25;
		}
		serviceOk = true;


	}


	@Override
	public boolean send(UserMessage message) throws Exception {
		if(!serviceOk){
			logger.error("SMTP网关服务不可用");
			return false;
		}
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

	private boolean sendEmail(String receiverName, String title, String content){
		Email email = new HtmlEmail();
		email.setHostName(smtpServer);
		email.setSmtpPort(smtpPort);
		if(smtpPort == 465) {
			 email.setSSL(true);
			 email.setSslSmtpPort(String.valueOf(smtpPort));
		}
		if(mailSenderName != null && mailSenderPassword != null){
			email.setAuthenticator(new DefaultAuthenticator(mailSenderName, mailSenderPassword));
		}
		email.setCharset("UTF-8");
		String senderName = mailSenderName;
		if(senderName.indexOf("@") <= 0){
			senderName += "@" + domainName;
		}
		logger.debug("邮件信息：receiverName："+receiverName+"mailSenderName:"+receiverName+"title:"+title+"content:"+content);
		String result = null;
		try{
			email.addTo(receiverName);
			email.setFrom(mailSenderName);
			email.setSubject(title);
			email.buildMimeMessage();			
			email.setMsg(content);
			result = email.sendMimeMessage();
		}catch(Exception e){
			logger.error("无法发送邮件到[" + receiverName + "]:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		if(logger.isDebugEnabled()){
			logger.debug("邮件[" + title + "]已发送至:" + receiverName + ",发送结果:" + result);
		}
		return true;
	}

	@Override
	public boolean send(String receiver, UserMessage message) {
		if(!serviceOk){
			logger.error("SMTP网关服务不可用");
			return false;
		}
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
		if(sendEmail(receiver, message.getTitle(), message.getContent())){
			message.setSenderStatus(OperateResult.success.getId());
			return true;
		}
		message.setSenderStatus(OperateResult.failed.getId());
		return false;
	}

}
