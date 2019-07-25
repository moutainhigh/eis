package com.maicard.wpt.controller.common;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.MessageService;
import com.maicard.mb.service.UserMessageService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
/**
 * 用户的系统消息控制器
 * @author Pengzhenggang
 * @data 2016-4-26
 */
@Controller
@RequestMapping("/userMessage")
public class UserMessageController extends BaseController{
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private MessageService messageService;
	/**
	 * 消息显示
	 * @param request
	 * @param response
	 * @param map
	 * @param messageCriteria
	 * @return
	 */
	@RequestMapping(value="/list" ,method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, MessageCriteria messageCriteria ){
		final String view = "userMessage/list";
		long ownerId = (long) map.get("ownerId");
		int totalSizeNum = 0;
		
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			map.put("totalSizeNum", totalSizeNum);
			return CommonStandard.frontMessageView;
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if (frontUser == null) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
			map.put("totalSizeNum", totalSizeNum);
			return CommonStandard.frontMessageView;
		}
		List<UserMessage> userMessageList = new ArrayList<UserMessage>();
		logger.debug("用户消息设置:"+frontUser.getExtraValue("allowOtherToSelf"));
		if(frontUser.getExtraValue("allowOtherToSelf")==null || frontUser.getExtraValue("allowOtherToSelf").equals("Y")){
			messageCriteria.setReceiverId(frontUser.getUuid());
			if(messageCriteria.getCurrentStatus() == null || messageCriteria.getCurrentStatus().length < 1){
				messageCriteria.setCurrentStatus(MessageStatus.unread.id);			
			}
			messageCriteria.setOwnerId(ownerId);
			userMessageList = userMessageService.list(messageCriteria);
			if (userMessageList == null || userMessageList.size() < 1) {
				logger.debug("用户[" + frontUser.getUuid() + "]没有新的系统消息");
				map.put("totalSizeNum", totalSizeNum);
//				return CommonStandard.frontMessageView;
			}
		}
//		String messageExtraType = userMessageList.get(0).getMessageExtraType();
//		logger.debug("messageExtraType : " + messageExtraType);
		//已读消息
		messageCriteria = new MessageCriteria();
		messageCriteria.setReceiverId(frontUser.getUuid());
		messageCriteria.setCurrentStatus(MessageStatus.readed.id);			
		messageCriteria.setOwnerId(ownerId);
		List<UserMessage> userMessageRead = userMessageService.list(messageCriteria);
		//已发送
		messageCriteria = new MessageCriteria();
		messageCriteria.setSenderId(frontUser.getUuid());
		messageCriteria.setCurrentStatus(MessageStatus.sent.id);			
		messageCriteria.setOwnerId(ownerId);
		List<UserMessage> userMessageSent = userMessageService.list(messageCriteria);
		
//		ArrayList<UserMessage> messageList = new ArrayList<UserMessage>();
		/*for (UserMessage userMessage : userMessageList) {
			messageList.add(userMessage);
		}*/
		map.put("userMessage", userMessageList);
		map.put("userMessageSent", userMessageSent);
		map.put("userMessageRead", userMessageRead);
		int userMessageListSize = userMessageList==null?0:userMessageList.size();
		totalSizeNum = userMessageListSize+userMessageSent.size()+userMessageRead.size();
		map.put("totalSizeNum", totalSizeNum);
		logger.debug("未读消息数量："+userMessageListSize+"已读消息数量："+userMessageRead.size()+"已发送消息数量："+userMessageSent.size()+"#总数量"+totalSizeNum);
		return view;
	}

	/**
	 * 创建新的消息
	 * 
	 * NetSnake,2016-05-28
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			UserMessage userMessage){
		final String view = "userMessage/create";
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.id){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.id, "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		
		userMessage.setOwnerId(ownerId);
		userMessage.setSenderId(frontUser.getUuid());
		userMessage.setSendTime(new Date());
		int rs = userMessageService.insert(userMessage);	
		logger.debug("用户提交新消息:" + userMessage + ",新增结果:" + rs);
		if(rs == 1){
			map.put("message", new EisMessage(OperateResult.success.id, "提交成功"));
		} else{
			map.put("message", new EisMessage(OperateResult.failed.id, "提交失败"));
		}
		return view;
	}

	/**
	 * 消息删除
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		logger.debug("进入消息删除");
		final String view = "userMessage/list";
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if (frontUser == null) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		String messageId = request.getParameter("messageId");
		if (messageId == null) {
			logger.debug("用户[" + frontUser.getUuid() + "]要删除的消息id");
			return view;
		}

		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setReceiverId(frontUser.getUuid());
		messageCriteria.setMessageId(messageId);
		messageCriteria.setOwnerId(ownerId);
		List<UserMessage> userMessageLists = userMessageService.list(messageCriteria);
		if (userMessageLists == null || userMessageLists.size() < 1) {
			logger.debug("没找到用户[" + frontUser.getUuid() + "]的消息[" + messageId + "]");
			return CommonStandard.frontMessageView;
		}
		logger.debug("userMessageLists : " + userMessageLists.size());

		/**
		 * 消息的删除不完整
		 */
		int rs = userMessageService.delete(messageId);	//删除操作

		map.put("delNum", rs);
		if(rs==1){
			map.put("message", new EisMessage(OperateResult.success.id,"删除成功"));
		}else{
			map.put("message", new EisMessage(OperateResult.failed.id,"删除失败"));
		}
		return CommonStandard.frontMessageView;
	}
	/**
	 * 点击已读
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value="clickRead", method=RequestMethod.GET)
	public String clickRead(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		final String view = "userMessage/clickRead";
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if (frontUser == null) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		String messageId = request.getParameter("messageId");
		if (messageId == null) {
			logger.debug("用户[" + frontUser.getUuid() + "]要删除的消息id");
			return view;
		}

		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setReceiverId(frontUser.getUuid());
		messageCriteria.setMessageId(messageId);
		messageCriteria.setOwnerId(ownerId);
		messageCriteria.setCurrentStatus(MessageStatus.unread.id);
		List<UserMessage> userMessageLists = userMessageService.list(messageCriteria);
		if (userMessageLists == null || userMessageLists.size() < 1) {
			logger.debug("没找到用户[" + frontUser.getUuid() + "]的消息[" + messageId + "]");
			return CommonStandard.frontMessageView;
		}
		logger.debug("userMessageLists : " + userMessageLists.size());

		UserMessage userMessage = userMessageLists.get(0);
		logger.debug("userMessage信息："+userMessage+"#"+userMessage.getMessageId());
		userMessage.setCurrentStatus(MessageStatus.readed.id);
		userMessage.setReceiveTime(new Date());
		int rs = userMessageService.update(userMessage);
		logger.debug("点击已读 : " + rs);
		if(rs==1){
			map.put("message", new EisMessage(OperateResult.success.id,"删除成功"));
		}else{
			map.put("message", new EisMessage(OperateResult.failed.id,"删除失败"));
		}
		return view;
	}
	/**
	 * 标记已读
	 */
	@RequestMapping(value="markRead", method=RequestMethod.GET)
	public String markRead(HttpServletRequest request, HttpServletResponse response, ModelMap map, MessageCriteria messageCriteria,
			@RequestParam("messageIds") String[] messageIds){
		final String view = "userMessage/markRead";
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if (frontUser == null) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		if (messageIds == null || messageIds.length < 1) {
			map.put("message", new EisMessage(EisError.dataError.id,"您未选需要标记为已读的信息，请先选中需要标记为已读的信息"));
			return CommonStandard.frontMessageView;
		}

		for (int i = 0; i < messageIds.length; i++) {
			messageCriteria.setMessageId(messageIds[i]);
			messageCriteria.setReceiverId(frontUser.getUuid());
			messageCriteria.setCurrentStatus(MessageStatus.unread.id);
			messageCriteria.setOwnerId(ownerId);
			List<UserMessage> MessageLists = userMessageService.list(messageCriteria);
			if (MessageLists == null || MessageLists.size() < 1) {
				logger.error("没找到需要标记为已读的信息");
				return view;
			}
			for (UserMessage userMessage : MessageLists) {
				userMessage.setCurrentStatus(MessageStatus.readed.id);
				int rs = userMessageService.update(userMessage);
				if (rs != 1) {
					map.put("message", new EisMessage(OperateResult.failed.id, "标记失败"));
					return view;
				}
			}
		}
		map.put("message", new EisMessage(OperateResult.success.id, "标记成功"));
		return view;
	}
}
