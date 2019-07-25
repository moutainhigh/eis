package com.maicard.wpt.utils.weixin;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.maicard.mb.domain.UserMessage;
import com.maicard.wpt.domain.WeixinMsg;

public class WeixinMessageConverter {

	public static UserMessage convert(WeixinMsg weixinMsg, String identify){
		if(weixinMsg == null){
			return null;
		}
		UserMessage userMessage = new UserMessage();
		userMessage.setOwnerId(weixinMsg.getOwnerId());
		if(weixinMsg.getMsgId() == 0){
			userMessage.setMessageId(UUID.randomUUID().toString());
		} else {
			userMessage.setMessageId(weixinMsg.getMsgId()+"");
		}
		Date msgTime = null;
		if(weixinMsg.getCreateTime() < 1){
			msgTime = new Date();
		} else {
			msgTime = new Date(weixinMsg.getCreateTime() * 1000);
		}
		String identifyString = null;
		if(identify == null){
			identifyString = identify;
		} else {
			identifyString = weixinMsg.getEventKey();
		}
		userMessage.setSendTime(msgTime);
		userMessage.setReceiveTime(new Date());
		userMessage.setTitle(weixinMsg.getLabel());
		userMessage.setContent(weixinMsg.getContent());
		userMessage.setReceiverName(weixinMsg.getToUserName());
		userMessage.setSenderName(weixinMsg.getFromUserName());
		userMessage.setIdentify(identifyString);
		userMessage.setInviter(weixinMsg.getInviter());
		userMessage.setOwnerId(weixinMsg.getOwnerId());
		String messageType = "微信";
		if(StringUtils.isNotBlank(weixinMsg.getEvent())){
			if(weixinMsg.getEvent().equalsIgnoreCase("scancode_waitmsg")){
				messageType += "扫码并等待结果";
			} else if(weixinMsg.getEvent().equalsIgnoreCase("scancode_push")){
				messageType += "扫码推送";
			} else if(weixinMsg.getEvent().equalsIgnoreCase("scancode_ajax")){
				messageType += "外部扫码事件";
			} else 	if(weixinMsg.getEvent().equalsIgnoreCase("unsubscribe")){
				messageType += "取消关注";
			} else 	if(weixinMsg.getEvent().equalsIgnoreCase("subscribe")){
				messageType += "关注";
			}  else if(weixinMsg.getEvent().equalsIgnoreCase("view")){
				messageType += "点击菜单";
			} else if(weixinMsg.getEvent().equalsIgnoreCase("pic_photo_or_album")){
				messageType += "拍照或选择照片";
			} else if(weixinMsg.getEvent().equalsIgnoreCase("pic_sysphoto")){
				messageType += "拍照事件";
			} else if(weixinMsg.getEvent().equalsIgnoreCase("location_select")){
				messageType += "发送位置";
			} else if(weixinMsg.getEvent().equalsIgnoreCase("TEMPLATESENDJOBFINISH")){
				messageType += "模版消息发送结束";
			}else {
				messageType +=weixinMsg.getEvent() + "事件";
			}
		}
		if(weixinMsg.getMsgType().equalsIgnoreCase("text")){
			messageType += "文本";
		}
		if(weixinMsg.getMsgType().equalsIgnoreCase("image")){
			messageType += "图片";
			userMessage.setContent(weixinMsg.getPicUrl());
		}
		
		userMessage.setOriginalType(messageType);
		userMessage.setAttachment(new HashMap<String,Object>());
		//	userMessage.getAttachment().put("Ticket", weixinMsg.getTicket());
		if(weixinMsg.getEvent() != null){
			userMessage.getAttachment().put("Event", weixinMsg.getEvent());
		}
		if(weixinMsg.getEventKey() != null){
			userMessage.getAttachment().put("EventKey", weixinMsg.getEventKey());
		}
		if(weixinMsg.getMsgType() != null){
			userMessage.getAttachment().put("MsgType", weixinMsg.getMsgType());
		}

		if(weixinMsg.getScanCodeInfo() != null){
			userMessage.getAttachment().put("ScanCodeInfo", weixinMsg.getScanCodeInfo().getScanType() + "#" + weixinMsg.getScanCodeInfo().getScanResult());
		}
		return userMessage;
	}

}
