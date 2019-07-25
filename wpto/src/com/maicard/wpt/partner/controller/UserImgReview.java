package com.maicard.wpt.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.criteria.ExtraDataCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.ExtraDataService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.ActivityLog;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 用户上传图片的管理
 * @author Pengzhengggang
 * @data 2016-12-9
 */
@Controller
@RequestMapping("/photo")
public class UserImgReview extends BaseController {
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ActivityLogService activityLogService;
	@Resource
	private ExtraDataService extraDataService;
	@Resource
	private ActivityService activityService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private UserMessageService userMessageService;
	
	private int rowsPerPage = 10;
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	private final String DEFAULT_SMS_VALIDATE_MESSAGE = "恭喜您上传的照片已通过审核，您上传照片的编号是${index}号。";
	@PostConstruct
	public void init() {
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(), 0);
		if (rowsPerPage < 1) {
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}
	/**
	 * 用户上传的图片列表
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			ActivityLogCriteria activityLogCriteria) throws Exception{
		final String view = "common/photo/list";
		logger.debug("图片管理");
		map.put("title", "图片列表");
		// 登录检查
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if (partner == null) {
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return view;
		}
		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.id, "您尚未登录，请先登录"));
			return view;
		}
		//结束登陆检查
		
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		ActivityCriteria activityCriteria = new ActivityCriteria(ownerId);
		activityCriteria.setActivityCode("baojie201702");
		activityCriteria.setOwnerId(ownerId);
		List<Activity> activityList = activityService.list(activityCriteria);
		if (activityList == null || activityList.size() < 1) {
			map.put("message", "没找到上传照片的活动");
			return view;
		}
		Activity activity = activityList.get(0);
		
		activityLogCriteria.setActivityId(activity.getActivityId());
		if(activity.getBeginTime() == null){
			activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		} else {
			activityLogCriteria.setBeginTime(activity.getBeginTime());
		}
		activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
		activityLogCriteria.setOwnerId(activity.getOwnerId());
		int totalRows = activityLogService.count(activityLogCriteria);
		if(totalRows < 1){
			logger.info("当前返回的数据条数是0");
			map.put("message", "当前返回的数据条数是0");
			return view;
		}
		
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		activityLogCriteria.setPaging(paging);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		List<ActivityLog> joinList = activityLogService.listOnPage(activityLogCriteria);
		logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。 joinList :　" + joinList.size());
		
		if(joinList == null || joinList.size() < 1){
			map.put("message", "数据为空");
			return view;
		}
		for(ActivityLog activityLog : joinList){
			User joinUser = frontUserService.select(activityLog.getUuid());
			ExtraDataCriteria extraDataCriteria = new ExtraDataCriteria();
			
			ExtraDataCriteria extraDataCriteria1 = new ExtraDataCriteria();
			if(joinUser == null){
				
			} else {
				DataDefine dataDefine = dataDefineService.select("userUpLoadImage");
				//增加心得的字段
				DataDefine dataDefine1 = dataDefineService.select("userUploadSummary");
				
				activityLog.setExtraValue("joinUser", joinUser.getUsername());
				extraDataCriteria.setUuid(joinUser.getUuid());
				extraDataCriteria.setTableName("activity_data");
				extraDataCriteria.setObjectType(ObjectType.activity.name());
				extraDataCriteria.setObjectId(activity.getActivityId());
				
				extraDataCriteria1.setUuid(joinUser.getUuid());
				extraDataCriteria1.setTableName("activity_data");
				extraDataCriteria1.setObjectType(ObjectType.activity.name());
				extraDataCriteria1.setObjectId(activity.getActivityId());
				if (dataDefine != null && dataDefine1 != null) {
					extraDataCriteria.setDataDefineId(dataDefine.getDataDefineId());
					
					extraDataCriteria1.setDataDefineId(dataDefine1.getDataDefineId());
				}
				List<ExtraData> extraDataList = extraDataService.list(extraDataCriteria);
				List<ExtraData> extraDataList1 = extraDataService.list(extraDataCriteria1);
				extraDataList.addAll(extraDataList1);
				logger.debug("extraDataListSize : " + extraDataList.size());
				activityLog.setExtraDataList(extraDataList);
			}
			
		}
		ArrayList<ActivityLog> activityLogListClone = new ArrayList<ActivityLog>();
		for (ActivityLog activityLog : joinList) {
			ActivityLog activityLogClone = (ActivityLog) activityLog.clone();
			activityLogClone.setOperate(new HashMap<String,String>());
			// 当状态是141001待审核时，显示审核按钮；状态是141002已发布时，显示取取消审核按钮
			if (activityLogClone.getCurrentStatus() == CommentCriteria.STATUS_WAIT_EXAMINE) {
				activityLogClone.getOperate().put("relate", "./photo/" + Operate.relate.name());
			} else if(activityLogClone.getCurrentStatus() == CommentCriteria.STATUS_PUBLISHED){
				activityLogClone.getOperate().put("clear", "./photo/" + Operate.clear.name());
			}
			activityLogListClone.add(activityLogClone);
		}
		
		map.put("path", "static/userFile");
		map.put("joinUserList", activityLogListClone);
		map.put("rows", joinList.size());
		return view;
	}
	
	@RequestMapping(value="/relate", method=RequestMethod.POST)
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		// 登录检查
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if (partner == null) {
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}
		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.id, "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//结束登陆检查
		int uuid = 0;
		String uuidStr = request.getParameter("uuid");
		if (NumericUtils.isIntNumber(uuidStr)) {
			uuid = Integer.parseInt(uuidStr);
		}
		if (uuid < 1) {
			map.put("message", "请提交正确的参与者信息");
			return CommonStandard.partnerMessageView;
		}
		User frontUser = frontUserService.select(uuid);
		if (frontUser == null) {
			map.put("message", "没有找到参与者[" + uuid + "]");
			return CommonStandard.partnerMessageView;
		}
		int activityId = 0;
		String activityIdStr = request.getParameter("activityId");
		if (NumericUtils.isIntNumber(activityIdStr)) {
			activityId = Integer.parseInt(activityIdStr);
		}
		if (activityId < 0) {
			map.put("message", "请提交正确的活动id");
			return CommonStandard.partnerMessageView;
		}
		Activity activity = activityService.select(activityId);
		if (activity == null) {
			map.put("message", "没有找到ID为[" + activityId + "]的活动");
			return CommonStandard.partnerMessageView;
		}
		
		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		if(activity.getBeginTime() == null){
			activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		} else {
			activityLogCriteria.setBeginTime(activity.getBeginTime());
		}
		activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
		activityLogCriteria.setUuid(uuid);
		activityLogCriteria.setOwnerId(activity.getOwnerId());
		int totalRows = activityLogService.count(activityLogCriteria);
		if(totalRows < 1){
			logger.info("当前返回的数据条数是0");
			map.put("message", "当前返回的数据条数是0");
			return CommonStandard.partnerMessageView;
		}
		List<ActivityLog> ActivityLogList = activityLogService.list(activityLogCriteria);
		ActivityLog activityLog = ActivityLogList.get(0);
		ActivityLog activityLogClone = (ActivityLog) activityLog.clone();
		if (activityLogClone.getCurrentStatus() != CommentCriteria.STATUS_PUBLISHED) {
			logger.debug("用户[" + frontUser.getUuid() + "]上传的照片的状态[" + activityLogClone.getCurrentStatus() + "]没有审核，设置为已审核，并更新");
			activityLogClone.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
			int rs = activityLogService.update(activityLogClone);
			if (rs == 1) {
//				map.put("message", new EisMessage(OperateResult.success.id, "审核通过"));
				//下发短信
				String smsTemplate = configService.getValue(DataName.registerSmsValidateMessage.toString(), ownerId);
				if(smsTemplate == null){
					smsTemplate = DEFAULT_SMS_VALIDATE_MESSAGE;
				}
				String shortMsg = smsTemplate.replaceAll("\\$\\{index\\}", String.valueOf(activityLog.getIndex()));
				
				String phone = activityLog.getExtraValue("phone");

				if (phone != null) {
					UserMessage sms = new UserMessage(ownerId);
					sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
					sms.setContent(shortMsg);
					sms.setReceiverName(phone);
					sms.setCurrentStatus(MessageStatus.queue.id);
					int rss = userMessageService.send(sms);
					logger.debug("短信下发到[手机" + phone + ",内容:" + shortMsg + "]，消息服务返回的是:" + rss);
					if(rs < 1){
						map.put("message", new EisMessage(OperateResult.failed.id, "短信下发失败"));
						return CommonStandard.partnerMessageView;		
					}
					map.put("message", new EisMessage(OperateResult.success.id,"审核成功，短信下发成功！"));
				} else {
					map.put("message", new EisMessage(OperateResult.success.id,"审核成功，但没找到编号为[" + activityLog.getIndex() + "]号的手机号，短信下发失败"));
				}
			} else {
				map.put("message", new EisMessage(OperateResult.failed.id, "审核失败"));
			}
		} else {
			logger.debug("用户[" + frontUser.getUuid() + "]上传的照片的状态[" + activityLogClone.getCurrentStatus() + "]为已审核，不做更新");
			map.put("message", new EisMessage(OperateResult.success.id, "照片状态为已发布状态，不做更新"));
		}
		return CommonStandard.partnerMessageView;
	}
	
	@RequestMapping(value="/clear", method=RequestMethod.POST)
	public String clear(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		////////////////////////////标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if (partner == null) {
		// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id, "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}
		
		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.id, "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		int uuid = 0;
		String uuidStr = request.getParameter("uuid");
		if (NumericUtils.isIntNumber(uuidStr)) {
			uuid = Integer.parseInt(uuidStr);
		}
		if (uuid < 1) {
			map.put("message", "请提交正确的参与者信息");
			return CommonStandard.partnerMessageView;
		}
		User frontUser = frontUserService.select(uuid);
		if (frontUser == null) {
			map.put("message", "没有找到参与者[" + uuid + "]");
			return CommonStandard.partnerMessageView;
		}
		int activityId = 0;
		String activityIdStr = request.getParameter("activityId");
		if (NumericUtils.isIntNumber(activityIdStr)) {
			activityId = Integer.parseInt(activityIdStr);
		}
		if (activityId < 0) {
			map.put("message", "请提交正确的活动id");
			return CommonStandard.partnerMessageView;
		}
		Activity activity = activityService.select(activityId);
		if (activity == null) {
			map.put("message", "没有找到ID为[" + activityId + "]的活动");
			return CommonStandard.partnerMessageView;
		}
		
		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		if(activity.getBeginTime() == null){
			activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		} else {
			activityLogCriteria.setBeginTime(activity.getBeginTime());
		}
		activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
		activityLogCriteria.setUuid(uuid);
		activityLogCriteria.setOwnerId(activity.getOwnerId());
		int totalRows = activityLogService.count(activityLogCriteria);
		if(totalRows < 1){
			logger.info("当前返回的数据条数是0");
			map.put("message", "当前返回的数据条数是0");
			return CommonStandard.partnerMessageView;
		}
		List<ActivityLog> ActivityLogList = activityLogService.list(activityLogCriteria);
		ActivityLog activityLog = ActivityLogList.get(0);
		ActivityLog activityLogClone = (ActivityLog) activityLog.clone();
		if (activityLogClone.getCurrentStatus() == CommentCriteria.STATUS_PUBLISHED) {
			logger.debug("用户[" + frontUser.getUuid() + "]上传的照片的状态[" + activityLogClone.getCurrentStatus() + "]已发布，取消审核，变更为待审核");
			activityLogClone.setCurrentStatus(CommentCriteria.STATUS_WAIT_EXAMINE);
			int rs = activityLogService.update(activityLogClone);
			if (rs == 1) {
				map.put("message", new EisMessage(OperateResult.success.id, "取消审核成功"));
			} else {
				map.put("message", new EisMessage(OperateResult.failed.id, "取消审核失败"));
			}
		} else {
			logger.debug("用户[" + frontUser.getUuid() + "]上传的照片的状态[" + activityLogClone.getCurrentStatus() + "]为待审核，不做取消审核");
			map.put("message", new EisMessage(OperateResult.success.id, "照片状态为待审核状态，不做更新"));
		}
		return CommonStandard.partnerMessageView;
	}

}
