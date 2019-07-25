package com.maicard.wpt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.security.domain.User;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.OperateResult;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.service.ScanCodeService;

/**
 * 针对微信的扫一扫二维码或条形码
 * 来与上游厂商的优惠券等业务对接
 *
 *
 * @author NetSnake
 * @date 2016年1月14日
 *
 */
@Service
public class ScanCodeServiceImpl extends BaseService implements ScanCodeService{

	@Resource
	private  ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;


	@Override
	public Object getScanResult(User user, Object msg, String identify) {
		logger.info("收到用户[" + user.getUuid() + "/" + user.getNickName() + "]扫码请求，对象:" + (msg == null ? "空" : msg.getClass().getName()));
		if(msg == null){
			return null;
		}

		if(msg instanceof WeixinMsg){
			return getWeixinScanResult(user, msg, identify);			
		}
		logger.warn("不支持的扫码对象:" + msg.getClass().getName());
		return null;
	}

	private Object getWeixinScanResult(User user, Object msg, String identify) {
		if(StringUtils.isBlank(identify)){
			identify = "default";
		}

		WeixinMsg weixinMsg = (WeixinMsg)msg;

		if(weixinMsg.getScanCodeInfo() == null){
			logger.error("传入的weixinMsg没有扫描对象ScanCodeInfo");
			return null;
		}
		if(StringUtils.isBlank(weixinMsg.getScanCodeInfo().getScanResult())){
			logger.error("传入的weixinMsg没有扫描对象ScanCodeInfo");
			return null;
		}
		if(weixinMsg.getScanCodeInfo().getScanType() != null && weixinMsg.getScanCodeInfo().getScanType().equals("barcode")){
			//条形码
			if(StringUtils.isBlank(weixinMsg.getScanCodeInfo().getScanResult())){
				logger.error("传入的扫描对象，扫描结果为空");
				return null;
			}
			String[] data = weixinMsg.getScanCodeInfo().getScanResult().split(",");
			if(data == null || data.length < 2){
				logger.error("传入的扫描对象，扫描结果为空或格式异常:" + weixinMsg.getScanCodeInfo().getScanResult());
				return null;
			}
			//String codeEncode = data[0];
			//扫描条码的数值
			ActivityCriteria activityCriteria = new ActivityCriteria();
			activityCriteria.setActivityType("scanCode");
			activityCriteria.setCurrentStatus(BasicStatus.normal.getId());
			activityCriteria.setActivityIdentify(identify);
			List<Activity> activityList = activityService.list(activityCriteria);
			if(activityList == null || activityList.size() < 1){
				logger.info("系统中没有类型为优惠券兑换且识别代码是[" + identify + "]的活动");
				return null;
			}
			String resultMsg = "";
			for(Activity activity : activityList){
				ActivityProcessor activityProcessor = null;
				Object bean = applicationContextService.getBean(activity.getProcessor());
				if(bean == null || !(bean instanceof ActivityProcessor)){
					logger.error("找不到" + activity.getActivityId() + "#活动指定的bean:" + activity.getProcessor());
					continue;
				}
				activityProcessor = (ActivityProcessor)bean;
				EisMessage executeResult = activityProcessor.execute(null, activity, user, msg);
				if(executeResult != null && executeResult.getOperateCode() == OperateResult.success.getId()){
					resultMsg += executeResult.getMessage() + ",";
				}
			}
			if(StringUtils.isBlank(resultMsg)){
				return new EisMessage(OperateResult.failed.getId(),"扫码活动失败");
			} else {
				resultMsg = resultMsg.replaceAll(",$", "");
				return new EisMessage(OperateResult.success.getId(),resultMsg);
			}

		} else {
			logger.error("不支持的扫码类型:" + weixinMsg.getScanCodeInfo().getScanType());
			return null;
		}
	}


}
