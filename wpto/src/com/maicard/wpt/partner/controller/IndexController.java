package com.maicard.wpt.partner.controller;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.SecurityLevelService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.SecurityLevelUtils;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.product.service.ItemService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.stat.criteria.FrontUserStatCriteria;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.domain.FrontUserStat;
import com.maicard.stat.domain.PayStat;
import com.maicard.stat.service.FrontUserStatService;
import com.maicard.stat.service.PayStatService;
@Controller
@RequestMapping("/index")
public class IndexController extends BaseController{

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;

	@Resource
	private FrontUserStatService frontUserStatService;


	@Resource
	private ItemService itemService;
	
	@Resource
	private MoneyService moneyService;
	@Resource
	private PayStatService payStatService;
	
	@Resource
	private PartnerService partnerService;
	
	@Resource
	private UserMessageService userMessageService;

	@Resource
	private SecurityLevelService securityLevelService;

	@Value("${systemVersion}")
	private String systemVersion;

	private final String changePasswordUrl = "/user/update/userPassword" + CommonStandard.DEFAULT_PAGE_SUFFIX;
	private final SimpleDateFormat  sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	private static HashMap<String,Object> dashboardMap = new HashMap<String,Object>();

	private int securityLevelId;
	private SecurityLevel securityLevel;

	@PostConstruct
	public void init(){
		securityLevelId = SecurityLevelUtils.getSecurityLevel();
		securityLevel = securityLevelService.select(securityLevelId);
	}



	@RequestMapping(method=RequestMethod.GET)
	@IgnorePrivilegeCheck
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {		
		User partner =certifyService.getLoginedUser(request,response,UserTypes.partner.getId());
		if(partner == null){
			//为防止转发带上参数，不再使用Spring的redirect而采用response的redirect重定向
			response.sendRedirect(CommonStandard.PARTNER_LOGIN_URL);
			return null;
			//return "redirect:/user/login";
		} 
		//logger.debug("本地消息测试:" + applicationContextService.getLocaleMessage("Status.100001"));
		logger.debug("用户[" + partner.getUsername() + "]进入合作伙伴系统.");
		//检查密码是否过期
		boolean  passwordNeedChange = certifyService.passwordNeedChange(partner);
		if(passwordNeedChange){
			logger.debug("当前用户的密码需要进行更改,跳转到:" + changePasswordUrl);
			response.sendRedirect(changePasswordUrl);
			return null;
		}

		writeDashboard(map, partner);

		writeRecentUserMessage(map, partner);

		writeVersion(map);

		writeSecurityLevel(map);

		writePerformanceRate(map);

		writeSuccessOrder(map,partner);



		return "common/index/list";
	}


	private void writeVersion(ModelMap map) {
		map.put("systemVersion", systemVersion);		
	}

	private void writeSecurityLevel(ModelMap map) {
		map.put("securityLevel", securityLevel);
		map.put("securityLevelId", securityLevelId);		
	}

	private void writePerformanceRate(ModelMap map) {
		map.put("performanceRate", applicationContextService.getPerformanceRate());
	}


	private void writeRecentUserMessage(ModelMap map, User partner) {
		final int rows = 10;
		final int page = 1;
		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setOwnerId(partner.getOwnerId());
		messageCriteria.setInviter(partner.getOwnerId());
		messageCriteria.setSendTimeEnd(DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(new Date(),1),Calendar.DAY_OF_MONTH),-1));

		messageCriteria.setCurrentStatus(null);

		Paging paging = new Paging(rows);
		messageCriteria.setPaging(paging);
		messageCriteria.getPaging().setCurrentPage(page);

		List<UserMessage> messageList = userMessageService.listOnPage(messageCriteria);
		if(messageList == null || messageList.size() < 1){
			logger.debug("当前没有任何用户消息");
			return;
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
						/*						int activityId = Integer.parseInt(data[1]);
				Activity activity = activityService.select(activityId);
						if(activity.getOwnerId() != sysUser.getOwnerId()){
							logger.warn("消息[" + messageList.get(i).getMessageId() + "]对应的ownerId[" + messageList.get(i).getOwnerId() + "]与当前系统会话中的[" + sysUser.getOwnerId() + "]不一致");
						} else {
							messageList.get(i).setIdentify(activity.getActivityName());
						}*/
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
			messageList.get(i).getOperate().put("get", "./userMessage/get/"+ messageList.get(i).getMessageId());
		}

		map.put("userMessageList", messageList);

		messageCriteria = null;		
	}




	private void writeDashboard(ModelMap map, User partner) {		
		int registerUserToday = 0;
		if(dashboardMap.get("registerUserToday") == null || Calendar.getInstance().get(Calendar.MINUTE) % 10 == 0){
			//统计今日新注册用户
			UserCriteria frontUserCriteria = new UserCriteria(partner.getOwnerId());
			frontUserCriteria.setCreateTimeBegin(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
			registerUserToday = frontUserService.count(frontUserCriteria);
			logger.debug("查询今日[" + sdf.format(frontUserCriteria.getCreateTimeBegin()) + "]新增用户数:" + registerUserToday);
			dashboardMap.put("registerUserToday", registerUserToday);
		} else {
			registerUserToday = (Integer)dashboardMap.get("registerUserToday");
			logger.debug("从缓存中获取今日新增用户数:" + registerUserToday);
		}
		map.put("registerUserToday", registerUserToday);




	}


	private void writeSuccessOrder(ModelMap map, User partner) throws Exception{
		//统计今日成功订单数和总金额
		PayStatCriteria payStatCriteria = new PayStatCriteria(partner.getOwnerId());
		payStatCriteria.setQueryBeginTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
		payStatCriteria.setGroupByDay(true);
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(payStatCriteria, partner);
		}
		
		List<PayStat> payStatList = payStatService.listOnPage(payStatCriteria);
		double successMoneyToday = 0f;
		int successOrderToday = 0;
		if(payStatList != null && payStatList.size() > 0){
			successMoneyToday = payStatList.get(0).getSuccessMoney();
			successOrderToday = payStatList.get(0).getSuccessCount();
		}
		map.put("successMoneyToday", successMoneyToday);
		map.put("successOrderToday", successOrderToday);
		
		//统计今日注册用户
		FrontUserStatCriteria frontUserStatCriteria = new FrontUserStatCriteria(partner.getOwnerId());
		frontUserStatCriteria.setGroupByDay(true);
		frontUserStatCriteria.setQueryBeginTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(frontUserStatCriteria, partner);
		}
		List<FrontUserStat> frontUserStatList = frontUserStatService.listOnPage(frontUserStatCriteria);
		int registerUserToday = 0;
		if(frontUserStatList != null && frontUserStatList.size() > 0){
			registerUserToday = frontUserStatList.get(0).getRegisterCount();
		}
		map.put("registerUserToday", registerUserToday);
		
		//统计可提现金额
		Money money = moneyService.select(partner.getUuid(),partner.getOwnerId());
		map.put("money", money);
	}
	


}
