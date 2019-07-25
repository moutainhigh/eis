package com.maicard.wpt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.wpt.dao.WeixinMsgDao;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.service.WeixinMsgService;
import com.maicard.wpt.utils.weixin.WeixinMessageConverter;

@Service
public class WeixinMsgServiceImpl extends BaseService implements WeixinMsgService {
	@Resource
	private WeixinMsgDao weixinMsgDao;
	
	@Resource
	private FrontUserService frontUserService;
	
	@Resource
	private UserMessageService userMessageService;
	@Override
	public int insert(WeixinMsg message){
		if(message == null){
			return -1;
		}
		if(message.getEvent() != null && message.getEvent().toLowerCase().startsWith("scancode") && message.getScanCodeInfo() != null){
			message.setContent(message.getScanCodeInfo().getScanType() + "#" + message.getScanCodeInfo().getScanResult());
		}
		return weixinMsgDao.insert(message);
	}
	@Override
	public void insertAsUserMessage(WeixinMsg weixinMsg, String identify, int status) {
		if(weixinMsg == null){
			logger.error("尝试保存的微信消息为空");
			return;
		}
		UserMessage userMessage = WeixinMessageConverter.convert(weixinMsg, identify);
		if(userMessage == null){
			logger.error("无法将微信消息转换为EIS用户消息");
			return;
		}
		userMessage.setCurrentStatus(status);
		if(userMessage.getSenderName() != null){
			//查找对应的用户
			UserCriteria frontUserCriteria = new UserCriteria();
			frontUserCriteria.setUsername(userMessage.getSenderName());
			List<User> frontUserList = 	frontUserService.list(frontUserCriteria);
			if(frontUserList == null || frontUserList.size() <= 0){
				logger.warn("找不到名为[" + frontUserCriteria.getUsername() + "]的前端用户");
			} else if(frontUserList.size() != 1){
				logger.error("名为[" + frontUserCriteria.getUsername() + "]的前端用户有" + frontUserList.size() + "个");
			} else {
				logger.info("找到了微信消息的发件人[" + frontUserCriteria.getUsername() + "]对应前端用户" + frontUserList.get(0).getUuid());
				userMessage.setSenderId(frontUserList.get(0).getUuid());
			}
		}
		userMessageService.insert(userMessage);
		
	}
}
