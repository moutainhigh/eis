package com.maicard.wpt.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.criteria.MessageTypeCriteria;
import com.maicard.mb.domain.MessageType;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.MessageTypeService;
import com.maicard.mb.service.SubscribeMessageService;
import com.maicard.mb.service.UserMessageSender;
import com.maicard.mb.service.UserMessageService;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/userMessage")
public class UserMessageController extends BaseController{

	@Resource
	private ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private MessageTypeService messageTypeService;
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private SubscribeMessageService subscribeMessageService;


	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@InitBinder
	@IgnorePrivilegeCheck
	public void initBinder(HttpServletRequest request, HttpServletResponse response, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, "sendTimeBegin", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
		binder.registerCustomEditor(Date.class, "sendTimeEnd", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));

	}




	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("messageCriteria") MessageCriteria messageCriteria){
		final String view = "common/userMessage/list";		
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		map.put("addUrl", "./userMessage/create");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		messageCriteria.setOwnerId(partner.getOwnerId());
		if(messageCriteria.getSendTimeBegin() != null){
			logger.info("查询开始时间是:" + sdf.format(messageCriteria.getSendTimeBegin()));
		}
		if(messageCriteria.getSendTimeEnd() != null){
			messageCriteria.setSendTimeEnd(DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(messageCriteria.getSendTimeEnd(),1),Calendar.DAY_OF_MONTH),-1));
			logger.info("查询结束时间是:" + sdf.format(messageCriteria.getSendTimeEnd()));
		}
		if(messageCriteria.getCurrentStatus() != null && messageCriteria.getCurrentStatus().length == 1 && messageCriteria.getCurrentStatus()[0] == 0){
			messageCriteria.setCurrentStatus(null);
		}
		String senderName = null;
		try {
			senderName = ServletRequestUtils.getStringParameter(request, "senderName");
		} catch (ServletRequestBindingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(senderName)){
			if(StringUtils.isNumeric(senderName.trim())){
				User frontUser = frontUserService.select(Long.parseLong(senderName.trim()));
				if(frontUser == null){
					logger.warn("找不到UUID=" + senderName + "的前端用户");
					return view;
				}
				if(frontUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + senderName + "对应的前端用户，其ownerid[" + frontUser.getOwnerId() + "]与指定的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				messageCriteria.setSenderId(Long.parseLong(senderName.trim()));
				logger.info("查询的用户名是:" + senderName + ",UUID=" + senderName.trim());
			} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(senderName.trim());
				List<User> frontUserList = frontUserService.list(frontUserCriteria);
				if(frontUserList == null || frontUserList.size() < 1){
					logger.warn("找不到昵称=" + senderName + "的前端用户");
					frontUserCriteria.setUsername(senderName.trim());
					frontUserList = frontUserService.list(frontUserCriteria);
					if(frontUserList == null || frontUserList.size() < 1){
						logger.warn("找不到用户名=" + senderName + "的前端用户");
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if(frontUserList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + senderName + "对应的前端用户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与指定的ownerId[" + senderName + "]不匹配");
					return view;
				}
				messageCriteria.setSenderId(frontUserList.get(0).getUuid());
				logger.info("查询的发件人名是:" + senderName + ",UUID=" + frontUserList.get(0).getUuid());

			}
		}

		int totalRows = userMessageService.count(messageCriteria);
		map.put("total", totalRows);
		map.put("title", "用户消息列表");
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		messageCriteria.setPaging(paging);
		messageCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");

		List<UserMessage> messageList = userMessageService.listOnPage(messageCriteria);
		if(messageList == null || messageList.size() < 1){
			map.put("message", new EisMessage(OperateResult.failed.id,"没有消息"));
			return view;
		} 

		for(int i = 0; i < messageList.size(); i++){
			if(messageList.get(i).getSenderId() > 0){
				User frontUser = frontUserService.select(messageList.get(i).getSenderId());
				if(frontUser != null){
					messageList.get(i).setSenderName(frontUser.getNickName());
				}
			}
			/*messageList.get(i).setCurrentStatusName(OperateResult.unknown.findById(messageList.get(i).getCurrentStatus()).getName());
			if(StringUtils.isBlank(messageList.get(i).getCurrentStatusName()) || messageList.get(i).getCurrentStatusName().equals("未知")){
				messageList.get(i).setCurrentStatusName(MessageStatus.deleted.findById(messageList.get(i).getCurrentStatus()).getName());
			}
			if(StringUtils.isBlank(messageList.get(i).getCurrentStatusName()) || messageList.get(i).getCurrentStatusName().equals("未知")){
				messageList.get(i).setCurrentStatusName(EisError.accessDenied.findById(messageList.get(i).getCurrentStatus()).getName());
			}*/
			if(messageList.get(i).getIdentify() != null){
				if(messageList.get(i).getIdentify().startsWith(ObjectType.activity.name().toLowerCase())){
					String[] data = messageList.get(i).getIdentify().split("#");
					if(data != null && data.length > 1 && StringUtils.isNumeric(data[1])){
						int activityId = Integer.parseInt(data[1]);
						Activity activity = activityService.select(activityId);
						if(activity.getOwnerId() != partner.getOwnerId()){
							logger.warn("消息[" + messageList.get(i).getMessageId() + "]对应的ownerId[" + messageList.get(i).getOwnerId() + "]与当前系统会话中的[" + partner.getOwnerId() + "]不一致");
						} else {
							messageList.get(i).setIdentify(activity.getActivityName());
						}
					}
				}
			}
			if(messageList.get(i).getAttachment() != null && messageList.get(i).getAttachment().size() > 0){
				String attachementText = "";
				if(messageList.get(i).getAttachment().get("Event") != null){
					attachementText += "Event:" + messageList.get(i).getAttachment().get("Event").toString() + ";";
				}
				if(messageList.get(i).getAttachment().get("MsgType") != null){

					attachementText += "MsgType:" + messageList.get(i).getAttachment().get("MsgType").toString() + ";";
				}

				if(messageList.get(i).getAttachment().get("EventKey") != null){
					attachementText += "EventKey:" + messageList.get(i).getAttachment().get("EventKey").toString() + ";";
				}
				if(messageList.get(i).getAttachment().get("ScanCodeInfo") != null){
					attachementText += "ScanCodeInfo:" + messageList.get(i).getAttachment().get("ScanCodeInfo").toString() + ";";
				}
				messageList.get(i).setAttachementText(attachementText);
			}
			messageList.get(i).setOperate(new HashMap<String,String>());
			messageList.get(i).getOperate().put("detail", "./userMessage/get/"+ messageList.get(i).getMessageId());
			messageList.get(i).getOperate().put("del", "./userMessage/delete");	
		}
		Map<String,String>identifyMap = generateIdentifyMap(partner.getOwnerId());
		List<Integer>statusList = new ArrayList<Integer>();
		for(OperateResult status : OperateResult.values()){
			statusList.add(status.id);
		}
		for(MessageStatus status : MessageStatus.values()){
			statusList.add(status.id);
		}
		statusList.add(EisError.accountNotExist.id);
		statusList.add(EisError.cardUsedBefore.id);
		map.put("statusList", statusList);

		logger.info("当前页面共有" + identifyMap.size() + "个识别串" );
		map.put("identifyMap", identifyMap);
		map.put("rows", messageList);

		messageCriteria = null;
		return view;
	}
	@RequestMapping(value="/get/{messageId}", method=RequestMethod.GET)
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String mode = ServletRequestUtils.getRequiredStringParameter(request, "mode");
		String messageType = ServletRequestUtils.getStringParameter(request, "mode", "short");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		map.put("mode",  mode);
		map.put("statusCodeList", BasicStatus.values());
		EisMessage eisMessage = null;
		if (mode.equals("edit")) {
			int messageId = ServletRequestUtils.getRequiredIntParameter(request, "messageId");
			if(messageType.equals("subscribe")){
				eisMessage = subscribeMessageService.select(messageId);
			} else {
				//FIXME eisMessage = shortMessageService.select(messageId);
			}			
		} else {
			eisMessage = new EisMessage();
			if(messageType.equals("subscribe")){
				//eisMessage.setMessageLevel(Constants.MessageLevel.subscribeMessage.id);
			} 
		} 
		map.put("message", eisMessage);
		return "common/message/" + mode;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getSendMessage(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		final String view = "common/userMessage/create";
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		MessageTypeCriteria messageTypeCriteria = new MessageTypeCriteria();
		messageTypeCriteria.setOwnerId(partner.getOwnerId());
		messageTypeCriteria.setCurrentStatus(BasicStatus.normal.id);
		List<MessageType> messageTypeList = messageTypeService.list(messageTypeCriteria);
		map.put("messageTypeList", messageTypeList);
		return view;
	}



	@RequestMapping(method=RequestMethod.POST)
	protected String onSubmit(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("toUser") String toUser,
			@RequestParam("messageTypeId") int messageTypeId			) throws Exception {

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		StringBuffer sb = new StringBuffer();
		for(String key : request.getParameterMap().keySet()){
			String[] valueArray = request.getParameterMap().get(key);
			String value = null;
			if(valueArray != null){
				if( valueArray.length > 1){
					for(String v : valueArray){				
						value+=v + ",";
					}
					value = value.substring(0, value.length() - 1);
				} else {
					value = valueArray[0];
				}
				if(key.equals("toUser") || key.equals("messageTypeId") ||  key.equals("content")){
					continue;
				}
				sb.append(key);
				sb.append("=");
				sb.append(value);
				sb.append("&");
			}
		}
		String content = sb.toString().replaceAll("&$", "");
		if(StringUtils.isBlank(toUser)){
			logger.warn("未提交要发送消息的用户名");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"未提交要发送消息的用户名"));
			return CommonStandard.backMessageView;
		}
		if(messageTypeId < 1){
			logger.warn("未提交要发送的消息类型");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"未提交要发送的消息类型"));
			return CommonStandard.backMessageView;
		}
		MessageType messageType = messageTypeService.select(messageTypeId);
		if(messageType == null){
			logger.error("找不到指定的发送消息类型:" + messageTypeId);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"错误的消息类型:" + messageTypeId));
			return CommonStandard.backMessageView;
		}
		if(messageType.getCurrentStatus() != BasicStatus.normal.id){
			logger.error("指定的消息类型状态异常:" + messageType.getCurrentStatus());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"错误的消息类型:" + messageTypeId));
			return CommonStandard.backMessageView;
		}
		if(messageType.getOwnerId() != partner.getOwnerId()){
			logger.error("指定的消息云ID[" + messageType.getOwnerId() + "]与当前系统会话中的ownerId:" + partner.getOwnerId() + "不匹配");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"错误的消息类型:" + messageTypeId));
			return CommonStandard.backMessageView;
		}
		if(StringUtils.isBlank(messageType.getProcessClass())){
			logger.error("指定的消息类型[" + messageTypeId + "]没有处理器数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统数据异常"));
			return CommonStandard.backMessageView;
		}

		UserMessageSender userMessageSender = null;
		Object object = applicationContextService.getBean(messageType.getProcessClass());
		if(object == null){
			logger.error("找不到消息类型[" + messageTypeId + "]指定的处理器:" + messageType.getProcessClass());
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统数据异常"));
			return CommonStandard.backMessageView;
		}	
		if(!(object instanceof UserMessageSender)){
			logger.error("消息类型[" + messageTypeId + "]指定的处理器:" + messageType.getProcessClass() + "不是UserMessageSender类型");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统数据异常"));
			return CommonStandard.backMessageView;
		}
		userMessageSender = (UserMessageSender)object;
		int sendResult = userMessageSender.send(toUser, content, partner.getOwnerId());
		logger.info("由处理器[" + messageType.getProcessClass() + "]发送消息结果:" + sendResult);
		if(sendResult > 0 && sendResult < 1000){
			map.put("message", new EisMessage(sendResult,"成功发送" + sendResult + "个消息"));
		} else {
			map.put("message", new EisMessage(sendResult,"发送失败:" + sendResult));
		}
		return CommonStandard.partnerMessageView;
	}


	private Map<String,String> generateIdentifyMap(long ownerId){
		Map<String,String> identifyMap = new HashMap<String,String>();
		List<String> identifyList = userMessageService.getUniqueIdentify(ownerId);
		if(identifyList == null || identifyList.size() < 1){
			return identifyMap;
		}
		for(String originalIdentify : identifyList){
			if(StringUtils.isBlank(originalIdentify)){
				continue;
			}
			if(originalIdentify.startsWith(ObjectType.activity.name().toLowerCase())){
				String[] data = originalIdentify.split("#");
				if(data != null && data.length > 1 && StringUtils.isNumeric(data[1])){
					int activityId = Integer.parseInt(data[1]);
					Activity activity = activityService.select(activityId);
					if(activity.getOwnerId() != ownerId){
						logger.warn("识别码[" + originalIdentify + "]对应的活动ownerId[" + activity.getOwnerId() + "]与当前系统会话中的[" + ownerId + "]不一致");
						continue;
					}
					identifyMap.put(originalIdentify,activity.getActivityName());
				}
			}
		}

		return identifyMap;
	}

}
