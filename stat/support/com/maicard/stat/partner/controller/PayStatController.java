package com.maicard.stat.partner.controller;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.SecurityStandard;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.domain.PayStat;
import com.maicard.stat.service.PayStatService;


@Controller
@RequestMapping("/payStat")
public class PayStatController extends BaseController {

	@Resource
	private PayStatService payStatService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayMethodService payMethodService;
	
	@Resource
	private PayTypeService payTypeService;

	private int rowsPerPage = 10;
	private final SimpleDateFormat csvFileSdf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);
	final private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	final private DecimalFormat df = new DecimalFormat("0.00");


	//统计支付数据
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			PayStatCriteria payStatCriteria
			) throws Exception {
		final String view = "stat/pay/index";


		User partner  = certifyService.getLoginedUser(request, response, SecurityStandard.UserTypes.partner.getId());
		if(partner == null ){
			throw new UserNotFoundInRequestException();
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		
		
		map.put("isPlatformGenericPartner", isPlatformGenericPartner);
		if (!isPlatformGenericPartner) {
			//非内部商户，不允许按照这两个条件进行查询,NetSnake,2017-10-31
			payStatCriteria.setGroupByPayMethodId(false);
			payStatCriteria.setPayMethodId(0);
			long[] submitInviters = payStatCriteria.getInviters();
			partnerService.setSubPartner(payStatCriteria, partner);

			if(submitInviters != null && submitInviters.length > 0){
				//外部商户要查询他的子账户
				ArrayList<Long> validInviters = new ArrayList<Long>();
				//对外部商户提交的子账户进行检查，是否在它的下级中
				for(long submitInviter : submitInviters){
					for(long allInviter : payStatCriteria.getInviters()){
						if(submitInviter == allInviter){
							validInviters.add(submitInviter);
							break;
						}
					}
				}
				if(validInviters.size() > 0){
					payStatCriteria.setInviters(NumericUtils.longList2Array(validInviters));
				} else {
					payStatCriteria.setInviters(new long[]{});
				}
			}
		}
		
		if(authorizeService.havePrivilege(partner, ObjectType.payStat.name(), "download")){
			map.put("CSVDownload", "download" );
		}
		/*long currentUuid;
		if(partner.getHeadUuid()>0){
			currentUuid=partner.getHeadUuid();
		}else{
			currentUuid=partner.getUuid();
		}*/
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//查询开始时间
		if(payStatCriteria.getQueryBeginTime() == null){
			//如果没设置起始时间，设置为一周前
			payStatCriteria.setQueryBeginTime(DateUtils.truncate(DateUtils.addDays(new Date(), -7),Calendar.DAY_OF_MONTH));
		}
		
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria(ownerId);
		
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		if(payTypeList.size() > 0){
			map.put("payTypeList", payTypeList);
		}
		List<User> inviterList  = new ArrayList<User>();
		UserCriteria userCriteria = new UserCriteria(ownerId);
		if(!isPlatformGenericPartner){
			partnerService.applyMoreDynmicData(partner);
			inviterList = partner.getProgeny();
		} else {
			inviterList = partnerService.list(userCriteria);
		}
		if(inviterList != null && inviterList.size() > 0){
			map.put("inviterList", inviterList);
		}


		logger.info("支付统计查询起止时间:" + sdf.format(payStatCriteria.getQueryBeginTime()) + " 到 " + (payStatCriteria.getQueryEndTime() == null ? "空" : sdf.format(payStatCriteria.getQueryEndTime())));
		int totalRows = payStatService.count(payStatCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		map.put("total", totalRows);

		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		payStatCriteria.setPaging(paging);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));		
		//payStatCriteria.setGroupByInviter(true);
		int downloadMode = ServletRequestUtils.getIntParameter(request, "download", 0);
		List<PayStat> payStatList = null;
		if (downloadMode == 2) {
			//下载所有数据
			payStatCriteria.setQueryBeginTime(null);
			payStatList = payStatService.list(payStatCriteria);
		} else {
			payStatList = payStatService.listOnPage(payStatCriteria);
		}
		
		
		
		int totalCount = 0;
		int successCount = 0;
		double totalMoney = 0;
		double successMoney = 0;
		
		if (downloadMode < 2 && payStatList != null && payStatList.size() > 0) {
			for(PayStat payStat : payStatList){
				if(payStat.getTotalCount() > 0){
					float rate = (float)payStat.getSuccessCount() / (float)payStat.getTotalCount();
					payStat.setExtraValue("payRate", df.format(rate));
				}
				if(payStat.getSuccessCount() > 0){
					payStat.setExtraValue("arpu", df.format(payStat.getSuccessMoney() / payStat.getSuccessCount()));
				}
				if(payStat.getInviter() > 0){
					User inviterPartner = partnerService.select(payStat.getInviter());
					if(inviterPartner != null){
						payStat.setExtraValue("inviterName", (inviterPartner.getNickName() == null ? inviterPartner.getUsername() : inviterPartner.getNickName()));
					} else {
						payStat.setExtraValue("inviterName", "未知");
					}
				} else {
					payStat.setExtraValue("inviterName", "所有");

				}
				//logger.info("XXX isPlatformGenericPartner=" + isPlatformGenericPartner + ",payMethodId=" + payStat.getPayMethodId());
				if(isPlatformGenericPartner && payStat.getPayMethodId() > 0){
					PayMethod payMethod = payMethodService.select(payStat.getPayMethodId());
					if(payMethod != null){
						payStat.setExtraValue("payMethodName", payMethod.getName());
					} else {
						payStat.setExtraValue("payMethodName", "未知");
					}
				} else {
					payStat.setExtraValue("payMethodName", "所有");
				}
				if(payStat.getPayTypeId() > 0){
					PayType payType = payTypeService.select(payStat.getPayTypeId());
					if(payType != null){
						payStat.setExtraValue("payTypeName", payType.getName());
					} else {
						payStat.setExtraValue("payTypeName", "未知");
					}
				} else {
					payStat.setExtraValue("payTypeName", "所有");
				}
				//if(payStatCriteria.isRequireSummary()){		
					//计算本页小计
					totalCount += payStat.getTotalCount();
					successCount += payStat.getSuccessCount();
					totalMoney += payStat.getTotalMoney();
					successMoney += payStat.getSuccessMoney();
				//}
			}
		}
		map.put("totalCount", totalCount);
		map.put("successCount", successCount);
		map.put("totalMoney", totalMoney);
		map.put("successMoney", successMoney);
		map.put("requireSummary", payStatCriteria.isRequireSummary());
		if(totalCount > 0){
			float rate = (float)successCount / (float)totalCount;
			map.put("payRate", df.format(rate));
		}
		//payRate
		if(successCount > 0){
			map.put("arpu", df.format(successMoney / successCount));
		}
		map.put("rows",payStatList);
		
		if (downloadMode > 0 && payStatList != null && authorizeService.havePrivilege(partner, ObjectType.payStat.name(), "download")) {
			logger.debug("下载模式 ： " + downloadMode + "   数据[" + payStatList.size() + "]条");
			//下载CSV
			String fileName = csvFileSdf.format(new Date()) + (RandomUtils.nextInt(10000000) + 10000000) + ".csv";
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			response.setContentType("application/oct-stream");
			PrintWriter output = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("统计时间,付款人数,成功付款人数,发起付款金额,成功付款金额,付费率,ARPU值,付款方式,渠道\n");
			for (PayStat payStat : payStatList) {
				float rate = 0;
				if(payStat.getTotalCount() > 0){
					rate = (float)payStat.getSuccessCount() / (float)payStat.getTotalCount();
					payStat.setExtraValue("payRate", df.format(rate));
				}
				String arpu = null;
				if(payStat.getSuccessCount() > 0){
					arpu = df.format(payStat.getSuccessMoney() / payStat.getSuccessCount());
				}
				String payMethodName;
				if(isPlatformGenericPartner && payStat.getPayMethodId() > 0){
					PayMethod payMethod = payMethodService.select(payStat.getPayMethodId());
					if(payMethod != null){
						payMethodName = payMethod.getName();
					} else {
						payMethodName = "未知";
					}
				} else {
					payMethodName = "所有";
				}
				String inviterName;
				if(payStat.getInviter() > 0){
					User inviterPartner = partnerService.select(payStat.getInviter());
					if(inviterPartner != null){
						inviterName = (inviterPartner.getNickName() == null ? inviterPartner.getUsername() : inviterPartner.getNickName());
					} else {
						inviterName = "未知";
					}
				} else {
					inviterName = "所有";
				}
				sb.append(payStat.getStatTime()).append(",").append(payStat.getTotalCount()).append(",").append(payStat.getSuccessCount()).append(",").append(payStat.getTotalMoney()).append(",").append(payStat.getSuccessMoney()).append(",").append(df.format(rate)).append(",").append(arpu).append(",").append(payMethodName).append(",").append(inviterName).append("\n");
			}
			String csv = sb.toString();
//			logger.debug("输出CSV:" + csv);
			output.write(csv);
			output.close();
			return null;
		}
		
		
		
		
		return view;
	}




	

}
