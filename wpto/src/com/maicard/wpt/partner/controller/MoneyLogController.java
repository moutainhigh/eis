package com.maicard.wpt.partner.controller;


import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.MoneyLogCriteria;
import com.maicard.money.domain.MoneyLog;
import com.maicard.money.service.MoneyLogService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyMemory;
import com.maicard.standard.SecurityStandard.UserTypes;


@Controller
@RequestMapping("/moneyLog")
public class MoneyLogController extends BaseController{

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private NotifyService notifyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private MoneyLogService moneyLogService;
	@Resource
	private AuthorizeService authorizeService;

	private int rowsPerPage = 10;
	private final SimpleDateFormat csvFileSdf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);
	//private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, MoneyLogCriteria moneyLogCriteria) throws Exception {
		final String view = "common/moneyLog/index";
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		moneyLogCriteria.setOwnerId(partner.getOwnerId());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1); 

		String toUserName = moneyLogCriteria.getToUserName();

		map.put("moneyMemoryList", MoneyMemory.values());
		if(StringUtils.isNotBlank(toUserName)){
			if(StringUtils.isNumeric(toUserName.trim())){
				User payFromUser = null;
				if(moneyLogCriteria.getToAccountType() == UserTypes.partner.getId()){
					payFromUser = frontUserService.select(Long.parseLong(toUserName.trim()));
				} else {
					payFromUser = frontUserService.select(Long.parseLong(toUserName.trim()));

				}
				if(payFromUser == null){
					logger.warn("找不到UUID=" + toUserName + "的用户，查询用户类型是:" + moneyLogCriteria.getToAccountType());
					return view;
				}
				if(payFromUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + toUserName + "对应的用户，其ownerid[" + payFromUser.getOwnerId() + "]与指定的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				moneyLogCriteria.setToAccount(Long.parseLong(toUserName.trim()));
				logger.info("查询的用户名是:" + toUserName + ",UUID=" + toUserName.trim());
			} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(toUserName.trim());
				
				List<User> userList = null;
				if(moneyLogCriteria.getToAccountType() == UserTypes.partner.getId()){
					userList = partnerService.list(frontUserCriteria);
				} else {
					userList = frontUserService.list(frontUserCriteria);

				}
				if(userList == null || userList.size() < 1){
					logger.warn("找不到昵称=" + toUserName + "的用户,查询用户类型:" + moneyLogCriteria.getToAccountType());
					UserCriteria frontUserCriteria1 = new UserCriteria();
					frontUserCriteria1.setUsername(toUserName.trim());
					userList = frontUserService.list(frontUserCriteria1 );
					if(userList == null || userList.size() < 1){
						logger.warn("找不到用户名=" + toUserName + "的用户,查询用户类型:" + moneyLogCriteria.getToAccountType());
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if(userList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + toUserName + "对应的用户，其ownerid[" + userList.get(0).getOwnerId() + "]与指定的ownerId[" + toUserName + "]不匹配");
					return view;
				}
				moneyLogCriteria.setToAccount(userList.get(0).getUuid());
				logger.info("查询的人名是:" + toUserName + ",UUID=" + userList.get(0).getUuid());

			}
		}


		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(moneyLogCriteria, partner);
		}



		int totalRows = moneyLogService.count(moneyLogCriteria);
		map.put("total", totalRows);

		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}

		Paging paging = new Paging(rows);
		moneyLogCriteria.setPaging(paging);
		moneyLogCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		
		int downloadMode = ServletRequestUtils.getIntParameter(request, "download", 0);
		List<MoneyLog> moneyLogList = null;
		if(downloadMode == 2){
			/*Calendar calendar=Calendar.getInstance();   
		    calendar.setTime(new Date()); 	//当前时间
		    int today = calendar.get(Calendar.DAY_OF_MONTH); //当前日期
			calendar.set(Calendar.DAY_OF_MONTH, today - 2);//让日期加-2   两天之前的日期
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			moneyLogCriteria.setStartTime(sdf.format(calendar.getTime()));
			
			//下载所有数据
			payList = payService.list(moneyLogCriteria);*/
		} else {
			moneyLogList = moneyLogService.listOnPage(moneyLogCriteria);
		}
		
		List<MoneyLog> moneyLogList2 = new ArrayList<MoneyLog>();
		if(moneyLogList != null && moneyLogList.size() > 0){
			for(MoneyLog moneyLog : moneyLogList){
				if(moneyLog.getToAccount() > 0){
					User toAccountUser = partnerService.select(moneyLog.getToAccount());
					if(toAccountUser != null){
						moneyLog.setExtraValue("toUserName", toAccountUser.getNickName() == null ? toAccountUser.getUsername() : toAccountUser.getNickName());
					}
				}
				
				
				
				moneyLogList2.add(moneyLog);

			}
		}
		authorizeService.writeOperate(partner, moneyLogList2);

		map.put("rows",moneyLogList2);
		logger.debug("下载：" + downloadMode);
		if(downloadMode > 0 && moneyLogList2 != null){
			//下载CSV
			logger.debug("下载CSV");
			String fileName = csvFileSdf.format(new Date()) + (RandomUtils.nextInt(10000000) + 10000000) + ".csv";
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			response.setContentType("application/oct-stream");
			PrintWriter output = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("系统订单号, 入口订单号, 出口订单号, 渠道, 对应购买交易, 付款人, 订单金额, 成功金额, 发起时间, 结束时间, 状态\n");
			
			String csv = sb.toString();
			logger.debug("输出CSV:" + csv);
			output.write(csv);
			output.close();
			return null;
		}
		
		
		
		return view;
	}

	

	

}
