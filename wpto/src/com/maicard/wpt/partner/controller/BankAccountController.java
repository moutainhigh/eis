package com.maicard.wpt.partner.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.domain.BankAccount;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.money.service.BankAccountService;
import com.maicard.money.service.WithdrawMethodService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 银行帐号管理
 *
 *
 * @author NetSnake
 * @date 2017-08-10
 */
@Controller
@RequestMapping("/bankAccount")
public class BankAccountController extends BaseController{


	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private BankAccountService bankAccountService;

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private PartnerService partnerService;
	
	@Resource
	private WithdrawMethodService withdrawMethodService;


	private int rowsPerPage = 10;

	@PostConstruct
	public void init() {
		rowsPerPage = configService.getIntValue(
				DataName.partnerRowsPerPage.toString(), 0);
		if (rowsPerPage < 1) {
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}

	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, BankAccountCriteria bankAccountCriteria) throws Exception {
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		final String view = "common/bankAccount/index";
		bankAccountCriteria.setOwnerId(partner.getOwnerId());
		
		String username = ServletRequestUtils.getStringParameter(request, "username");

		if(StringUtils.isNotBlank(username)){
			if(StringUtils.isNumeric(username.trim())){
				User payFromUser = null;
				if(bankAccountCriteria.getUserTypeId() == UserTypes.frontUser.getId()){
					payFromUser = frontUserService.select(Long.parseLong(username.trim()));
				} else {
					payFromUser = partnerService.select(Long.parseLong(username.trim()));
				}
				if(payFromUser == null){
					logger.warn("找不到UUID=" + username + "的用户，查询用户类型是:" + bankAccountCriteria.getUserTypeId());
					return view;
				}
				if(payFromUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + username + "对应的用户，其ownerid[" + payFromUser.getOwnerId() + "]与指定的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				bankAccountCriteria.setUuid(Long.parseLong(username.trim()));
				logger.info("查询的用户名是:" + username + ",UUID=" + username.trim());
			} else {
				UserCriteria userCriteria = new UserCriteria();
				userCriteria.setNickName(username.trim());
				
				List<User> userList = null;
				
				if(bankAccountCriteria.getUserTypeId() == UserTypes.frontUser.getId()){
					userList = frontUserService.list(userCriteria);
				} else {
					userList = partnerService.list(userCriteria);
				}
				if(userList == null || userList.size() < 1){
					logger.warn("找不到昵称=" + username + "的用户,查询用户类型:" + bankAccountCriteria.getUserTypeId());
					UserCriteria frontUserCriteria1 = new UserCriteria();
					frontUserCriteria1.setUsername(username.trim());
					userList = frontUserService.list(frontUserCriteria1 );
					if(userList == null || userList.size() < 1){
						logger.warn("找不到用户名=" + username + "的用户,查询用户类型:" + bankAccountCriteria.getUserTypeId());
						userCriteria.setUsername(null);
						userCriteria.setNickName(null);
						return view;
					}
				}
				if(userList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + username + "对应的用户，其ownerid[" + userList.get(0).getOwnerId() + "]与指定的ownerId[" + username + "]不匹配");
					return view;
				}
				bankAccountCriteria.setUuid(userList.get(0).getUuid());
				logger.info("查询的人名是:" + username + ",UUID=" + userList.get(0).getUuid());

			}
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);

		map.put("isPlatformGenericPartner",isPlatformGenericPartner);

		
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(bankAccountCriteria, partner);
		}
		
		int rows = ServletRequestUtils.getIntParameter(request, "rows",				rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = bankAccountService.count(bankAccountCriteria);
		map.put("total", totalRows);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		
		Paging paging = new Paging(rows);
		bankAccountCriteria.setPaging(paging);
		bankAccountCriteria.getPaging().setCurrentPage(page);
		List<BankAccount> bankAccountList = bankAccountService.listOnPage(bankAccountCriteria);

		String objectType = "bankAccount";
		if(bankAccountList.size() > 0){
			for(BankAccount bankAccount : bankAccountList){
				bankAccount.setOperateValue("get", "./" + objectType + "/" + Operate.get.name() + "/" + bankAccount.getBankAccountId());
				bankAccount.setOperateValue("delete", "./" + objectType + "/" + Operate.delete.name() + "/" + bankAccount.getBankAccountId());
				if(bankAccount.getUuid() > 0){
					User user = null;
					if(bankAccount.getUserTypeId() == UserTypes.frontUser.id){
						user = frontUserService.select(bankAccount.getUuid());
					} else {
						user = partnerService.select(bankAccount.getUuid());
					}
					if(user != null){
						bankAccount.setOperateValue("username", user.getNickName() == null ? user.getUsername() : user.getUsername());
					}

				}
				if(bankAccount.getWithdrawMethodId() > 0){
					WithdrawMethod withdrawMethod = withdrawMethodService.select(bankAccount.getWithdrawMethodId());
					if(withdrawMethod != null){
						bankAccount.setOperateValue("withdrawMethodName", withdrawMethod.getName());
					}
				}
			}
		}

		
		map.put("rows",bankAccountList);
		return view;
	}


	@RequestMapping(value="/get" + "/{bankAccountId}", method=RequestMethod.GET )			
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("bankAccountId") int bankAccountId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		BankAccount bankAccount = bankAccountService.select(bankAccountId);
		if(bankAccount == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + bankAccountId + "的银行帐号对象");			
		}
		if(bankAccount.getOwnerId() != partner.getOwnerId()){
			throw new ObjectNotFoundByIdException("找不到ID=" + bankAccountId + "的银行帐号对象");			
		}
		
		map.put("bankAccount", bankAccount);
		return "common/bankAccount/get";
	}


	@RequestMapping(value="/update/{bankAccountId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@PathVariable("bankAccountId") int bankAccountId) throws Exception {
		long ownerId = 0;

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;		
		}

		BankAccount bankAccount = bankAccountService.select(bankAccountId);
		if(bankAccount == null){
			throw new ParentObjectNotFoundException("找不到指定的银行帐号[" + bankAccountId + "]");				
		}
		if(bankAccount.getOwnerId() != ownerId){
			throw new ParentObjectNotFoundException("找不到指定的银行帐号[" + bankAccountId + "]");				
		}


		return "common/bankAccount/update";
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception{
		final String view = "common/payMethod/create";
		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
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
		

		

		
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, BankAccount bankAccount ) throws Exception{
		final String view = CommonStandard.partnerMessageView;
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		/*if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.accessDenied.getId(),"没有权限"));
			return CommonStandard.partnerMessageView;
		}*/
		if(!authorizeService.havePrivilege(partner, "bankAccount", "w")){
			logger.error("用户"+partner.getNickName()+"没有新增银行帐号的权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "没有新增银行帐号的权限"));
			return CommonStandard.partnerMessageView;
		}

		bankAccount.setCurrentStatus(BasicStatus.normal.getId());
		bankAccount.setOwnerId(ownerId);
		
		if(!isPlatformGenericPartner){
			//是商户，那需要把新增银行账号的uuid设置为自己
			bankAccount.setUuid(partner.getUuid());
		} else {
			//FIXME
			bankAccount.setUuid(partner.getUuid());
		}

		
		
		EisMessage message = null;
		if (bankAccountService.insert(bankAccount) == 1) {
			message = new EisMessage(OperateResult.success.getId(), "操作完成");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "操作失败");
		}
		map.put("message", message);
		return view;
	}

	

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@AllowJsonOutput
	public String delete(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, String bankAccountId ) throws Exception{
		final String view = CommonStandard.partnerMessageView;
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		
		BankAccount bankAccount = bankAccountService.select(NumericUtils.parseInt(bankAccountId));

		if(bankAccount == null){
			logger.error("找不到指定的银行账户:" + bankAccountId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"找不到指定的银行账户"));
			return CommonStandard.partnerMessageView;
		}
		
		if(!isPlatformGenericPartner){

			if(partner.getUuid() == bankAccount.getUuid() || partnerService.isValidSubUser(partner.getUuid(), bankAccount.getUuid())){
				
			} else {
				logger.error("商户:" + partner.getUuid() + "尝试删除的银行帐号不属于它自身:" + bankAccount);
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"找不到指定的银行账户"));
				return CommonStandard.partnerMessageView;
			}
		}
		
		EisMessage message = null;
		if (bankAccountService.delete(bankAccount.getBankAccountId()) > 0) {
			message = new EisMessage(OperateResult.success.getId(), "删除成功");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "删除失败");
		}

		map.put("message", message);
		return view;
	}
	
	
}
