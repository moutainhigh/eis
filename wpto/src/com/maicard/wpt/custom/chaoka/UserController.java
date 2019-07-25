package com.maicard.wpt.custom.chaoka;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.QrCodeUtils;
import com.maicard.common.util.StringTools;
import com.maicard.exception.EisException;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.domain.BankAccount;
import com.maicard.money.service.BankAccountService;
import com.maicard.security.domain.User;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.constants.chaoka.AuthLevel;
import com.maicard.wpt.constants.chaoka.BankEnums;
import com.maicard.wpt.controller.basic.AbstractUserController;

@Controller
@RequestMapping("/user")
public class UserController  extends AbstractUserController{	
	
	@Resource
	private BankAccountService bankAccountService;
	
	@Resource
	private ApplicationContextService applicationContextService;
	
	
	
	static String documentUploadSaveDir;
	
	@PostConstruct
	public void init(){
		super.init();
		documentUploadSaveDir = applicationContextService.getDataDir();

	}

	/**
	 * 显示实名认证界面
	 */
	@RequestMapping(value="/realNameAuth", method=RequestMethod.GET)
	public String getRealNameAuth(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null) {
			throw new EisException(EisError.userNotFoundInSession.id,"请先登录");
		}
		return "user/getRealNameAuth";
	}
	

	/**
	 * 请求实名认证
	 */
	@RequestMapping(value="/realNameAuth", method=RequestMethod.POST)
	public String submitRealNameAuth(HttpServletRequest request,HttpServletResponse response, ModelMap map, String realName, String idNumber) throws Exception {
		if(StringUtils.isBlank(realName)) {
			throw new EisException(EisError.PARAMETER_ERROR.id,"未输入必须的真实姓名");
		}
		if(StringUtils.isBlank(idNumber)) {
			throw new EisException(EisError.PARAMETER_ERROR.id,"未输入必须的身份证号");
		}
		
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null) {
			throw new EisException(EisError.userNotFoundInSession.id,"请先登录");
		}
		if(frontUser.getAuthType() >= AuthLevel.REAL_NAME.id) {
			logger.info("用户:{}已经完成了实名认证，不允许再次认证", frontUser.getUuid());
			throw new EisException(EisError.dataDuplicate.id,"已经实名认证用户不允许再次提交");
		}
		frontUser.setExtraValue(DataName.userRealName.name(),realName);
		frontUser.setExtraValue(DataName.userRealNameIdCardNumber.name(), idNumber);
		frontUser.setAuthType(AuthLevel.REAL_NAME.id);
		EisMessage rs = frontUserService.update(frontUser);
		if(rs.getOperateCode() == OperateResult.success.id) {
			map.put("message",new EisMessage(rs.getOperateCode(),"实名认证成功"));
		} else {
			map.put("message",new EisMessage(rs.getOperateCode(),"实名认证失败"));
		}
		return CommonStandard.frontMessageView;
	}

	
	/**
	 * 显示绑定收款账号界面
	 */
	@RequestMapping(value="/bindMoneyAccount", method=RequestMethod.GET)
	public String bindMoneyAccount(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null) {
			throw new EisException(EisError.userNotFoundInSession.id,"请先登录");
		}
		BankAccountCriteria bankAccountCriteria = new BankAccountCriteria(frontUser.getOwnerId());
		bankAccountCriteria.setUuid(frontUser.getUuid());
		List<BankAccount> bankAccountList = bankAccountService.list(bankAccountCriteria);
		map.put("bankAccountList",bankAccountList);
		
		return "user/bindMoneyAccount";
	}
	

	/**
	 * 请求绑定收款账号界面
	 */
	@RequestMapping(value="/bindMoneyAccount", method=RequestMethod.POST)
	public String submitBindMoneyAccount(HttpServletRequest request,HttpServletResponse response, ModelMap map, 
			BankAccount bankAccount) throws Exception {
		if(StringUtils.isBlank(bankAccount.getBankAccountName())) {
			throw new EisException(EisError.PARAMETER_ERROR.id,"未输入必须的银行卡户名");
		}
		if(StringUtils.isBlank(bankAccount.getBankAccountNumber())) {
			throw new EisException(EisError.PARAMETER_ERROR.id,"未输入必须的银行卡号");
		}
		BankEnums be = null;
		String bankCode = bankAccount.getBankCode();
		if(StringUtils.isBlank(bankCode)) {
			if(StringUtils.isBlank(bankAccount.getBankName())) {
				logger.error("未提供银行代码也未提供银行名字");
				throw new EisException(EisError.PARAMETER_ERROR.id,"请提供银行名称");
			} else {
				be = BankEnums.getByName(bankAccount.getBankName());
			}
			
		} else {
			 be = BankEnums.getByCode(bankCode);
			
		}
		if(be == null) {
			throw new EisException(EisError.UNSUPPORTED_BANK.id,"不支持的银行");
		}
		
		bankAccount.setBankName(be.bankName);
		bankAccount.setBankCode(be.name());
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null) {
			throw new EisException(EisError.userNotFoundInSession.id,"请先登录");
		}
		if(frontUser.getAuthType() < AuthLevel.REAL_NAME.id) {
			logger.info("用户:{}未完成实名认证，不允许提交收款账号", frontUser.getUuid());
			throw new EisException(EisError.ACCOUNT_ERROR.id,"请先完成实名认证");
		}
		bankAccount.setOwnerId(frontUser.getOwnerId());
		
		bankAccount.setCurrentStatus(BasicStatus.normal.id);
		
		
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("certifyUpload");
		if(file == null) {
			throw new EisException(EisError.PARAMETER_ERROR.id,"未输入必须的账号截图");
		}
		String fileUploadSavePath = documentUploadSaveDir + "/" + CommonStandard.EXTRA_DATA_LOGIN_PATH + File.separator;
		String suffix = StringTools.getFileExt(file.getOriginalFilename());
		String fileName = frontUser.getUuid() + "_money_account_certify." + suffix;
		String filePath = fileUploadSavePath + fileName;  
		logger.debug("上传资金账户附加认证文件："+file.getOriginalFilename() + ",保存到:" + fileUploadSavePath + "目录下的:" + fileName);
		File destDir = new File(filePath).getParentFile();
		if(!destDir.exists()){
			logger.info("目标目录不存在，创建:" + fileUploadSavePath);
			FileUtils.forceMkdir(destDir);
		}
		
		File dest = new File(filePath);
		if(dest.exists()) {
			dest.delete();
		}
		//保存  
		file.transferTo(dest);
		if(be == BankEnums.ALIPAY || be == BankEnums.WECHAT) {
			//如果是支付宝或者微信，检查是否上传了正确的二维码
			String url = QrCodeUtils.decode(filePath);
			if(url == null  || url.indexOf("://") < 0) {
				throw new EisException(EisError.PARAMETER_ERROR.id,"请提交正确的二维码图片");
			}
		}
		bankAccount.setCertifyFile(fileName);
		
		bankAccount.setUuid(frontUser.getUuid());
		int rs = bankAccountService.insert(bankAccount);
		if(rs == 1) {
			map.put("message",new EisMessage(OperateResult.success.id,"绑定收款账号成功"));
			
			
		} else {
			map.put("message",new EisMessage(OperateResult.failed.id,"绑定收款账号失败:" + rs));
		}
		return CommonStandard.frontMessageView;
	}
}


