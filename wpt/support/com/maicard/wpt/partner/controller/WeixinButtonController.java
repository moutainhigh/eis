package com.maicard.wpt.partner.controller;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.Paging;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard;
import com.maicard.standard.CommonStandard.DataFetchMode;
import com.maicard.standard.SecurityStandard.UserExtraType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.criteria.WeixinButtonCriteria;
import com.maicard.wpt.domain.WeixinButton;
import com.maicard.wpt.service.WeixinButtonService;
import com.maicard.wpt.service.WeixinPlatformService;

/**
 * 管理当前合作伙伴的微信菜单
 * 
 * 
 * @author NetSnake
 * @date 2017-01-23
 */

@Controller
@RequestMapping("/weixinButton")
public class WeixinButtonController extends BaseController {
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ProductService ProductService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private PartnerService partnerService;	
	@Resource
	private ShareConfigService shareConfigService;	
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private PartnerRoleService partnerRoleService;
	@Resource
	private WeixinButtonService weixinButtonService;
	
	@Resource
	private WeixinPlatformService weixinPlatformService;

	private int rowsPerPage = 100;


	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, WeixinButtonCriteria weixinButtonCriteria) throws Exception {
		final String view = "wpt/weixinButton/index";

		////////////////////////////标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
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
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		//返回当前用户的菜单
		weixinButtonCriteria.setOwnerId(ownerId);
		weixinButtonCriteria.setUuid(partner.getUuid());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		int totalRows = weixinButtonService.count(weixinButtonCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		Paging paging = new Paging(rows);
		weixinButtonCriteria.setPaging(paging);
		weixinButtonCriteria.getPaging().setCurrentPage(page);
		logger.debug("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<WeixinButton> weixinButtonList = weixinButtonService.listOnPage(weixinButtonCriteria);

		map.put("total", totalRows);
		map.put("rows",weixinButtonList);
		return view;
	}

	@RequestMapping(value="/get" + "/{uuid}")		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("uuid") long uuid) throws Exception {
		final String view = "common/partner/get";
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}

		User partner = partnerService.select(uuid);
		if(partner == null){
			logger.error("找不到指定的用户:" + uuid);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"用户#" + uuid + "不存在"));
			return CommonStandard.partnerMessageView;
		}
		if(partner.getOwnerId() != ownerId){
			logger.error("指定的用户:" + uuid + "，其ownerId[" + partner.getOwnerId() + "]与当前会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"用户#" + uuid + "不存在"));
			return CommonStandard.partnerMessageView;
		}

		map.put("partner", partner);
		return view;
	}

	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String editSelf(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User currentPartner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(currentPartner == null){
			throw new UserNotFoundInRequestException();
		}	
		map.put("partner", currentPartner);
		return CommonStandard.partnerMessageView;
	}



	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User currentPartner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(currentPartner == null){
			throw new UserNotFoundInRequestException();

		}
		UserCriteria partnerCriteria=new UserCriteria();
		partnerCriteria.setDataFetchMode(DataFetchMode.normal.toString());
		partnerCriteria.setLevel(2);
		partnerCriteria.setOwnerId(currentPartner.getOwnerId());
		List<User> partnerList = partnerService.list(partnerCriteria);
		/*角色列表*/
		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setOwnerId(currentPartner.getOwnerId());
		List<Role> partnerRoleList = partnerRoleService.list(roleCriteria);

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.user.toString());
		dataDefineCriteria.setObjectId(UserTypes.partner.getId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		logger.debug("DataDefine数量 ：" + dataDefineList.size());
		if (dataDefineList == null || dataDefineList.size() < 1) {
			logger.info("没有可选自定义字段");
			map.put("message", "没有可选自定义字段");
		} else {
			//FIXME 去重复
			for (int i = 0; i < dataDefineList.size(); i++) {
				for (int j = dataDefineList.size() -1; j > i; j--) {
					if (dataDefineList.get(i).getDataDescription().equals(dataDefineList.get(j).getDataDescription())) {
						dataDefineList.remove(j);
					}
				}
			}
			logger.debug("dataDefine个数2 ： " + dataDefineList.size());
			map.put("dataDefine", dataDefineList);
		}

		//		HashMap<String, Integer> userTypeMaps = new HashMap<String, Integer>();
		//		for (UserTypes userTypes : UserTypes.values()) {
		//			userTypeMaps.put(userTypes.getName(), userTypes.getId());
		//			logger.debug("枚举 : " + userTypes.getName() + "   " + userTypes.getId());
		//		}
		//		map.put("userTypes", userTypeMaps);
		map.put("statusCodeList", SecurityStandard.UserStatus.values());
		map.put("userExtraTypeList",UserExtraType.values());
		map.put("partner", new User());
		map.put("partnerRoleList", partnerRoleList);
		map.put("partnerList", partnerList);

		return "common/partner/create";
	}

	/**
	 * 把本地微信菜单同步到微信
	 * 
	 *
	 * @author GHOST
	 * @date 2018-01-28
	 */
	@RequestMapping(value="/relate", method=RequestMethod.POST)	
	public String sync(HttpServletRequest request, HttpServletResponse response, ModelMap map, WeixinButtonCriteria weixinButtonCriteria) throws Exception {
		final String view = CommonStandard.partnerMessageView;

		////////////////////////////标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
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
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		weixinButtonCriteria.setOwnerId(ownerId);
		weixinButtonCriteria.setUuid(partner.getUuid());

		int totalRows = weixinButtonService.count(weixinButtonCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		
		List<WeixinButton> weixinButtonList = weixinButtonService.list(weixinButtonCriteria);
		
		weixinButtonList = weixinButtonService.generateTree(weixinButtonList);
		//把克隆的菜单设置到对应的公众号
		weixinPlatformService.createWeixinButton(weixinButtonList, null, partner.getUuid(), ownerId);

		map.put("total", totalRows);
		map.put("rows",weixinButtonList);

		return view;
	}


	@RequestMapping(value="/create", method=RequestMethod.POST)	
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, User child) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		child.setOwnerId(ownerId);
		child.setUserTypeId(SecurityStandard.UserTypes.partner.getId());
		child.setUserExtraTypeId(1);
		String[] type1 = request.getParameterValues("roleId");  
		List<Role> relatedRoleList = new ArrayList<Role>();

		if (type1!=null){

			for (int i = 0; i < type1.length; i++) {  
				Role role=new Role();
				role.setRoleId(Integer.parseInt(type1[i]));
				relatedRoleList.add(role);
			}   
			child.setRelatedRoleList(relatedRoleList);
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		//		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		dataDefineCriteria.setObjectType(ObjectType.user.toString());
		dataDefineCriteria.setObjectId(child.getUserTypeId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if (dataDefineList == null || dataDefineList.size() == 0) {
			logger.info("当前账户类型[" + child.getUserTypeId() + "]没有自定义字段.");
		} else {
			logger.debug("当前账户类型有[" + dataDefineList.size() + "]个自定义数据规范");

			for (DataDefine dataDefine : dataDefineList) {
				logger.debug("尝试获取数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据");
				String dataStr = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
				if (dataStr == null || dataStr.equals("")) {
					logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]没有提交数据");
					continue;
				}
				logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]提交的数据是[" + dataStr + "]");

				UserData userData = new UserData();
				userData.setDataDefineId(dataDefine.getDataDefineId());
				userData.setDataCode(dataDefine.getDataCode());
				userData.setCurrentStatus(BasicStatus.normal.getId());
				userData.setDataValue(dataStr);
				if (child.getUserConfigMap() == null) {
					child.setUserConfigMap(new HashMap<String, UserData>());
				}
				logger.debug("尝试插入自定义账户数据[" + userData.getDataCode() + "/" + userData.getDataDefineId() + "]，数据内容:[" + userData.getDataValue() + "]");
				child.getUserConfigMap().put(userData.getDataCode(), userData);
			}
		}
		child.setRelatedRoleList(relatedRoleList);
		long headUuid = ServletRequestUtils.getLongParameter(request, "headUuid", 0);
		child.setHeadUuid(headUuid);
		processPartnerData(request, child);
		logger.info("请求写入新的Partner[" + child.getUsername() + "],parentUuid=[" + child.getParentUuid() + "]");
		child.setCreateTime(new Date());
		EisMessage message = null;


		if(partnerService.insert(child) == 1 ){
			message = new EisMessage(OperateResult.success.getId(),"操作完成");		
		} else {
			message = new EisMessage(OperateResult.failed.getId(),"操作失败");
		}

		map.put("message", message);		
		return view;
	}
	@RequestMapping(value="/update" + "/{uuid}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("uuid")int uuid) throws Exception {

		//得到当前上级headUuid
		User thisPartner = partnerService.select(uuid);
		//得到headUuid的用户名


		ProductCriteria ProductCriteria = new ProductCriteria();
		ProductCriteria.setCurrentStatus(BasicStatus.normal.getId());

		List<Product> ProductList = ProductService.list(ProductCriteria);

		ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria();

		shareConfigCriteria.setShareUuid(uuid);
		/*	SimpleDateFormat  fmt = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
		thisPartner.setCreateTime(fmt.parse(fmt.format(thisPartner.getCreateTime())));*/
		List<ShareConfig> shareConfigList = shareConfigService.list(shareConfigCriteria);
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(siteDomainRelation == null){
			throw new Exception("找不到[" + request.getServerName() + "]对应的站点关系");
		}
		float  maxChannelSharePercent=configService.getFloatValue("maxChannelSharePercent",siteDomainRelation.getOwnerId());
		if( maxChannelSharePercent==0){

			maxChannelSharePercent=0.6f;
		}
		//得到所有partner的用户名
		UserCriteria partnerCriteria=new UserCriteria();

		partnerCriteria.setLevel(2);
		List<User> partnerList = partnerService.list(partnerCriteria);

		for(User allPartner:partnerList){

			if(allPartner.getHeadUuid()==thisPartner.getHeadUuid())	{

				if(thisPartner.getHeadUuid()!=0){
					User thisParentPartner = partnerService.select(thisPartner.getHeadUuid());
					allPartner.setUsername(thisParentPartner.getUsername());
					allPartner.setUuid(thisPartner.getHeadUuid());
					//得到相对应的用户名
				}


			}		

		}
		/*角色列表*/
		RoleCriteria roleCriteria = new RoleCriteria();
		List<Role> partnerAllRoleList = partnerRoleService.list(roleCriteria);

		UserRoleRelationCriteria partnerRoleRelationCriteria = new UserRoleRelationCriteria();
		partnerRoleRelationCriteria.setUuid(uuid);
		map.put("statusCodeList", SecurityStandard.UserStatus.values());
		map.put("userExtraTypeList",UserExtraType.values());
		map.put("partner", thisPartner);	

		map.put("maxChannelSharePercent", maxChannelSharePercent);		
		map.put("ProductList", ProductList);
		map.put("shareConfigList", shareConfigList);
		map.put("partnerAllRoleList", partnerAllRoleList);
		map.put("partnerList", partnerList);

		//logger.error(shareConfigList);
		return "partner/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)	
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@ModelAttribute("partner") User child) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		List<Role> relatedRoleList = new ArrayList<Role>();
		String[] type1 = request.getParameterValues("roleId");  
		if (type1!=null){

			for (int i = 0; i < type1.length; i++) {  
				Role role=new Role();
				role.setRoleId(Integer.parseInt(type1[i]));
				relatedRoleList.add(role);
			}   
			child.setRelatedRoleList(relatedRoleList);
		}

		long headUuid = ServletRequestUtils.getLongParameter(request, "headUuid", 0);
		child.setHeadUuid(headUuid);
		processPartnerData(request, child);
		EisMessage message = null;
		if(partnerService.update(child) > 0){
			message = new EisMessage(OperateResult.success.getId(),"操作完成");		
		} else {
			message = new EisMessage(OperateResult.failed.getId(),"操作失败");
		}

		map.put("message", message);		
		return view;
	}
	private void processPartnerData(HttpServletRequest request, User partner){
		//先从系统中获取当前partner的所有配置
		User _oldPartner = partnerService.select(partner.getUuid());
		if(_oldPartner == null){
			logger.error("在系统中找不到需要处理的Partner[" + partner + "]");
			return;
		}
		HashMap<String, UserData>userConfigMap = new HashMap<String, UserData>();
		if(_oldPartner.getUserConfigMap() == null){
			logger.debug("将要处理的Partner[" + partner + "]没有已存在的用户数据");
		} else {
			userConfigMap = _oldPartner.getUserConfigMap();
		}
		//尝试从请求中获取所有自定义数据的值
		DataDefineCriteria userDataDefinePolicyCriteria = new DataDefineCriteria();
		userDataDefinePolicyCriteria.setObjectId(UserTypes.partner.getId());
		userDataDefinePolicyCriteria.setObjectExtraId(partner.getUserExtraTypeId());
		List<DataDefine> userDataDefinePolicyList = dataDefineService.list(userDataDefinePolicyCriteria);
		if(userDataDefinePolicyList != null){
			for(DataDefine userDataDefinePolicy: userDataDefinePolicyList){
				if(userDataDefinePolicy.getInputLevel() == null){
					logger.debug("数据[" + userDataDefinePolicy.getDataCode() + "]的输入级别为空，跳过数据.");
					continue;
				}
				if(StringUtils.isBlank(userDataDefinePolicy.getInputLevel()) || userDataDefinePolicy.getInputLevel().equals(InputLevel.platform.name()) || userDataDefinePolicy.getInputLevel().equals(InputLevel.user.name())){

					String data = request.getParameter(userDataDefinePolicy.getDataCode());
					logger.debug("尝试获取数据规范[" + userDataDefinePolicy.getDataCode() + "]的数据:" + data);
					if(StringUtils.isBlank(data)){
						continue;
					}
					UserData ud = new UserData();
					ud.setDataDefineId(userDataDefinePolicy.getDataDefineId());
					ud.setDataValue(data);
					ud.setDataCode(userDataDefinePolicy.getDataCode());
					userConfigMap.put(ud.getDataCode(), ud);
				} else {
					logger.debug("数据[" + userDataDefinePolicy.getDataCode() + "]的输入级别不是用户或平台级别输入，跳过.");
					continue;
				}

			}
		}
		partner.setUserConfigMap(userConfigMap);

		try{
			logger.debug("更新的合作伙伴[" + partner.getUserTypeId() + "/" + partner.getUserExtraTypeId() + "]，该用户类型有" + (userDataDefinePolicyList == null ? 0 : userDataDefinePolicyList.size()) + "条数据规范，从请求中得到了[" + (partner.getUserConfigMap() == null ? 0 : partner.getUserConfigMap().size()) + "]条数据");
		}catch(Exception e){}
	}




}
