package com.maicard.wpt.partner.controller;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.MoneyBalanceCriteria;
import com.maicard.money.domain.MoneyBalance;
import com.maicard.money.service.MoneyBalanceComputer;
import com.maicard.money.service.MoneyBalanceService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 管理整个通道的资金平衡表
 *
 *
 * @author NetSnake
 * @date 2017年11月5日
 *
 */
@Controller
@RequestMapping("/moneyBalance")
public class MoneyBalanceController extends BaseController{

	@Resource
	private ApplicationContextService applicationContextService;
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
	private MoneyBalanceService moneyBalanceService;
	@Resource
	private AuthorizeService authorizeService;

	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, MoneyBalanceCriteria moneyBalanceCriteria) throws Exception {
		final String view = "common/moneyBalance/index";
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
		moneyBalanceCriteria.setOwnerId(partner.getOwnerId());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1); 

		
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			//非内部用户不允许使用本功能
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"无权访问"));
			return CommonStandard.partnerMessageView;
		}




		Paging paging = new Paging(rows);
		moneyBalanceCriteria.setPaging(paging);
		moneyBalanceCriteria.getPaging().setCurrentPage(page);
		
		/*//通过用户名查询
		String username = ServletRequestUtils.getStringParameter(request, "username");
		if (StringUtils.isNotBlank(username)) {
			UserCriteria partnerCriteria = new UserCriteria();
			partnerCriteria.setUsername(username);
			partnerCriteria.setOwnerId(ownerId);
			List<User> list = partnerService.list(partnerCriteria);
			if (list != null && list.size() > 0) {
				moneyCriteria.setUuid(list.get(0).getUuid());
			}else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setUsername(username);
				frontUserCriteria.setOwnerId(ownerId);
				List<User> frontUserList = frontUserService.list(frontUserCriteria );
				if (frontUserList != null && frontUserList.size() > 0) {
					moneyCriteria.setUuid(frontUserList.get(0).getUuid());
				}
			}
		}*/
		List<MoneyBalance> moneyList = moneyBalanceService.listOnPage(moneyBalanceCriteria);
		

		int totalRows = moneyBalanceService.count(moneyBalanceCriteria);
		map.put("total", totalRows);

		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		
		if(authorizeService.havePrivilege(partner, ObjectType.moneyBalance.name(), "w")){
			map.put("addUrl", "/moneyBalance/create.json");
		}
		
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		
		if(moneyList.size() > 0){
			for(MoneyBalance moneyBalance : moneyList){
				if(moneyBalance.getUuid() > 0){
					User toAccountUser = partnerService.select(moneyBalance.getUuid());
					if(toAccountUser == null) {
						toAccountUser = frontUserService.select(moneyBalance.getUuid());
					}
					if(toAccountUser != null){
						moneyBalance.setOperateValue("username", toAccountUser.getNickName() == null ? toAccountUser.getUsername() : toAccountUser.getNickName());
					}
				}
			}
		}

		map.put("rows",moneyList);
		
		
		
		return view;
	}
	
	/**
	 * 请求生成当前时间的资金平衡表
	 * @param request
	 * @param response
	 * @param map
	 * @param child
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, MoneyBalanceCriteria moneyBalanceCriteria) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		String username = ServletRequestUtils.getStringParameter(request, "username");
		logger.debug(username+"********************************************************************************");
		if(partner == null){
			throw new UserNotFoundInRequestException();
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
		//////////////////////// 结束标准流程 ///////////////////////
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);

		if(!isPlatformGenericPartner){
			//非内部用户不允许使用本功能
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"无权访问"));
			return CommonStandard.partnerMessageView;
		}


		moneyBalanceCriteria.setOwnerId(ownerId);
		
		String[] beanNames = applicationContextService.getBeanNamesForType(MoneyBalanceComputer.class);
		if(beanNames == null || beanNames.length < 1){
			logger.error("系统中没有类型为MoneyBalanceComputer的bean");
			map.put("message", new EisMessage(EisError.beanNotFound.id, "系统没有配置任何资金平衡计算器"));
			return view;
		}
		//获取第一个资金平衡计算器，理论上来讲，一个平台只应该有一个资金平衡计算器
		MoneyBalanceComputer computer = applicationContextService.getBeanGeneric(beanNames[0]);
		int rs = computer.computer(moneyBalanceCriteria);
		if(rs == OperateResult.success.id){
			map.put("message", new EisMessage(rs, "计算完成"));
		} else {
			map.put("message", new EisMessage(rs, "资金平衡计算失败"));

		}
		return view;
	}
	

	

}
