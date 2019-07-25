package com.maicard.wpt.partner.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.maicard.money.criteria.MoneyCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 管理用户的资金账户
 *
 *
 * @author NetSnake
 * @date 2017年4月30日
 *
 */
@Controller
@RequestMapping("/money")
public class MoneyController extends BaseController{

	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private NotifyService notifyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private AuthorizeService authorizeService;

	private int rowsPerPage = 10;

	

	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, MoneyCriteria moneyCriteria) throws Exception {
		final String view = "common/money/index";
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
		moneyCriteria.setOwnerId(partner.getOwnerId());
		moneyCriteria.setUserTypeId(partner.getUserTypeId());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1); 

		String payFromUserName = null;
		payFromUserName = ServletRequestUtils.getStringParameter(request, "username");

		if(StringUtils.isNotBlank(payFromUserName)){
			if(StringUtils.isNumeric(payFromUserName.trim())){
				User frontUser = frontUserService.select(Long.parseLong(payFromUserName.trim()));
				User partnerUser = partnerService.select(Long.parseLong(payFromUserName.trim()));
				if(frontUser == null && partnerUser == null){
					logger.warn("找不到UUID=" + payFromUserName + "用户");
					return view;
				}
				if((frontUser!=null && frontUser.getOwnerId() != partner.getOwnerId())||(partnerUser!=null && partnerUser.getOwnerId() != partner.getOwnerId())){
					logger.warn("UUID=" + payFromUserName + "对应的用户，其ownerid与指定的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				moneyCriteria.setUuid(Long.parseLong(payFromUserName.trim()));
				logger.info("查询的用户名是:" + payFromUserName + ",UUID=" + payFromUserName.trim());
			} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(payFromUserName.trim());
				List<User> frontUserList = frontUserService.list(frontUserCriteria);
				
				UserCriteria partnerCriteria = new UserCriteria();
				partnerCriteria.setNickName(payFromUserName.trim());
				List<User> partnerUserList = partnerService.list(partnerCriteria );
				if((frontUserList == null || frontUserList.size() < 1)&&(partnerUserList == null || partnerUserList.size() < 1)){
					logger.warn("找不到昵称=" + payFromUserName + "用户");
					UserCriteria frontUserCriteria1 = new UserCriteria();
					frontUserCriteria1.setUsername(payFromUserName.trim());
					frontUserList = frontUserService.list(frontUserCriteria1 );
					partnerCriteria.setNickName(null);
					partnerCriteria.setUsername(payFromUserName.trim());
					partnerUserList = partnerService.list(partnerCriteria);
					if((frontUserList == null || frontUserList.size() < 1)&&(partnerUserList == null || partnerUserList.size() < 1)){
						logger.warn("找不到用户名=" + payFromUserName + "的用户");
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if((frontUserList != null && frontUserList.size()>0 && frontUserList.get(0).getOwnerId() != partner.getOwnerId())
						|| (partnerUserList != null && partnerUserList.size()>0 && partnerUserList.get(0).getOwnerId() != partner.getOwnerId())){
					logger.warn("UUID=" + payFromUserName + "对应的前端用户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与指定的ownerId[" + payFromUserName + "]不匹配");
					return view;
				}
				if (frontUserList != null && frontUserList.size()>0) {
					moneyCriteria.setUuid(frontUserList.get(0).getUuid());
					logger.info("查询的发件人名是:" + payFromUserName + ",UUID=" + frontUserList.get(0).getUuid());
				}else {
					moneyCriteria.setUuid(partnerUserList.get(0).getUuid());
					logger.info("查询的发件人名是:" + payFromUserName + ",UUID=" + partnerUserList.get(0).getUuid());
				}

			}
		}
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(moneyCriteria, partner);
		}




		Paging paging = new Paging(rows);
		moneyCriteria.setPaging(paging);
		moneyCriteria.getPaging().setCurrentPage(page);
		
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
		List<Money> moneyList = moneyService.listOnPage(moneyCriteria);
		

		int totalRows = moneyService.count(moneyCriteria);
		map.put("total", totalRows);

		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		
		List<Money> moneyList2 = new ArrayList<Money>();
		if(moneyList != null && moneyList.size() > 0){
			for(Money m1 : moneyList){
				Money money = m1.clone();
				if(money.getUuid() > 0){
					User toAccountUser = partnerService.select(money.getUuid());
					if(toAccountUser == null) {
						toAccountUser = frontUserService.select(money.getUuid());
					}
					if(toAccountUser != null){
						money.setOperateValue("username", toAccountUser.getNickName() == null ? toAccountUser.getUsername() : toAccountUser.getNickName());
					}
				}
				
				
				
				moneyList2.add(money);

			}
		}
		authorizeService.writeOperate(partner, moneyList2);

		map.put("rows",moneyList2);
		
		
		
		return view;
	}

	

	

}
