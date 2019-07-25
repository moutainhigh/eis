package com.maicard.wpt.partner.controller;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.service.MessageService;
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
import com.maicard.security.service.UserDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DisplayLevel;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard;
import com.maicard.standard.SecurityStandard.UserExtraType;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 管理合作商户
 * 
 * 
 * @author NetSnake
 * @date 2017-9-26
 */

@Controller
@RequestMapping("/merchant")
public class MerchantController extends BaseController {
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ProductService ProductService;
	@Resource
	private MessageService messageService;
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
	private PartnerRoleService partnerRoleService;
	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;
	@Resource
	private CacheService cacheService;
	@Resource
	private UserDataService userDataService;

	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	//列出当前用户的下级经销商
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, UserCriteria partnerCriteria) throws Exception {
		final String view = "common/merchant/index";

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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////
		
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(partnerCriteria, partner);
		}
		


		if(authorizeService.havePrivilege(partner, ObjectType.merchant.name(), "w")){
			map.put("addUrl", "/merchant/create.shtml" );
		}
		
		//只能访问商户，不能访问内部用户
		partnerCriteria.setMerchant(true);
		
		long[] inviters = partnerCriteria.getInviters();
		logger.debug("inviters的值为"+Arrays.toString(inviters));
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		if (partnerCriteria.getUsername() !=null && partnerCriteria.getUsername().equals(""))
			partnerCriteria.setUsername(null);
		int totalRows = partnerService.count(partnerCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		partnerCriteria.setOwnerId(ownerId);
		partnerCriteria.setUserTypeId(121002);
		Paging paging = new Paging(rows);
		partnerCriteria.setPaging(paging);
		partnerCriteria.getPaging().setCurrentPage(page);
		logger.debug("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<User> partnerList = partnerService.listOnPage(partnerCriteria);

		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		map.put("userExtraTypeList",UserExtraType.values());
		map.put("total", totalRows);
		map.put("rows",partnerList);
		return view;
	}

	@RequestMapping(value="/get" + "/{uuid}")		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("uuid") long uuid) throws Exception {
		final String view = "common/merchant/get";
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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////
		if(!isPlatformGenericPartner){
			//不是内部用户，检查是不是访问自己或下级账户
			if(partner.getUuid() == uuid || partnerService.isValidSubUser(partner.getUuid(), uuid)){
				//用户合法访问
			} else {
				map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
				return CommonStandard.partnerMessageView;
			}
		}
		

		User selectUser = partnerService.select(uuid);
		
		
		//只允许查询extraType>0的用户类型，不允许查询=0的内部用户
		if(selectUser.getUserExtraTypeId() <= 0){
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "无权访问", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}
		User clone = selectUser.clone();
		Map<String, UserData> configMap = new HashMap<String,UserData>();
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
		dataDefineCriteria.setObjectType(ObjectType.user.name());
		dataDefineCriteria.setObjectId(UserTypes.partner.getId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList == null || dataDefineList.size() < 1){
			logger.error("ownerId=" + ownerId + "的系统没有针对合作伙伴的数据定义");
		} else {
			for(DataDefine dataDefine : dataDefineList){
				if(dataDefine.getDisplayLevel() == null ||dataDefine.getDisplayLevel().equals(DisplayLevel.partner.toString())|| dataDefine.getDisplayLevel().equals(DisplayLevel.user.toString()) || dataDefine.getDisplayLevel().equals(DisplayLevel.open.toString()) || isPlatformGenericPartner){
					UserData userData = new UserData(dataDefine);
					if (selectUser.getUserConfigMap().get(dataDefine.getDataCode())!= null) {
						userData = clone.getUserConfigMap().get(dataDefine.getDataCode());
					}
					if(!isPlatformGenericPartner && userData.getInputLevel() != null && (userData.getInputLevel().equals(InputLevel.system.name()) || userData.getInputLevel().equals(InputLevel.platform.name()))){	
							userData.setReadonly(true);
					}
					userData.setDataName(dataDefine.getDataName());
					configMap.put(dataDefine.getDataCode(), userData);
				}
			}
		}
		
		clone.setUserConfigMap((HashMap<String, UserData>) configMap);
		map.put("partner", clone);
		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setOwnerId(partner.getOwnerId());
		if (partner.getUserExtraTypeId()>0) {
			roleCriteria.setRoleLevel(partner.getUserExtraTypeId());
		}
		List<Role> partnerRoleList = partnerRoleService.list(roleCriteria);
		map.put("partnerRoleList", partnerRoleList);
		map.put("statusCodeList", SecurityStandard.UserStatus.values());
		map.put("userExtraTypeList",UserExtraType.values());
		/*UserDataCriteria userDataCriteria=new UserDataCriteria();
		userDataCriteria.setUuid(uuid);
		List<UserData> userDataList = userDataService.list(userDataCriteria);
		map.put("userDataList", userDataList);*/
		return view;
	}


	//显示自己的详细情况
	@RequestMapping(value="/detailSelf", method=RequestMethod.GET)
	public String detailSelf(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String view = "common/merchant/detail";

		User currentPartner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(currentPartner == null){
			throw new UserNotFoundInRequestException();

		}
		currentPartner = partnerService.select(currentPartner.getUuid());
		ProductCriteria ProductCriteria = new ProductCriteria();
		ProductCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Product> ProductList = ProductService.list(ProductCriteria);
		if(ProductList != null){
			map.put("ProductList", ProductList);
		}
		String inviteUrl = "";
		//inviteUrl = "http://www." + map.get(DataName.siteDomain.toString()) + "/content/";
		map.put("inviteUrl",inviteUrl);
		map.put("partner", currentPartner);
		return view;
	}

	/*@RequestMapping("/delete")	
	public String getDelete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {

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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////

		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			try{
				if(partnerService.delete(Integer.parseInt(ids[i])) > 0){
					successDeleteCount++;
				} 
			}catch(DataIntegrityViolationException forignKeyException ){
				String error  = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}
		}
		String messageContent = "成功删除[" + successDeleteCount + "]个.";
		if(!errors.equals("")){
			messageContent += errors;
		}
		logger.info(messageContent);
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));
		return CommonStandard.partnerMessageView;
	}	*/
	


	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		//////////////////////// 标准流程 ///////////////////////
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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////


		/*UserCriteria partnerCriteria=new UserCriteria();
		partnerCriteria.setDataFetchMode(DataFetchMode.normal.toString());
		partnerCriteria.setLevel(2);
		partnerCriteria.setOwnerId(partner.getOwnerId());
		List<User> partnerList = partnerService.list(partnerCriteria);*/
		/*角色列表*/
		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setOwnerId(partner.getOwnerId());
		roleCriteria.setRoleLevel(2);
		if (partner.getUserExtraTypeId()>0) {
			roleCriteria.setRoleLevel(partner.getUserExtraTypeId());
		}
		List<Role> partnerRoleList = partnerRoleService.list(roleCriteria);

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.user.toString());
		dataDefineCriteria.setObjectId(UserTypes.partner.getId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if (dataDefineList == null || dataDefineList.size() < 1) {
			logger.warn("合作伙伴类型没有可选自定义字段");

		} else {
			List<DataDefine> dataDefineList2 = new ArrayList<DataDefine>();
			for(DataDefine dataDefine : dataDefineList){
				if(dataDefine.getInputLevel() == null || dataDefine.getInputLevel().equals(InputLevel.partner.name())){

					dataDefineList2.add(dataDefine);
				}
				if(dataDefine.getInputLevel() == null || (dataDefine.getInputLevel().equals(InputLevel.platform.name()) && isPlatformGenericPartner)){

					dataDefineList2.add(dataDefine);
				}
			}
			map.put("configMap", dataDefineList2);
			dataDefineList = null;
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
		//map.put("partnerList", partnerList);

		return "common/merchant/create";
	}
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, User child) throws Exception {
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
		child.setOwnerId(ownerId);
		child.setUserTypeId(SecurityStandard.UserTypes.partner.getId());
		child.setUserExtraTypeId(2);
		child.setParentUuid(partner.getUuid());
		child.setInviter(partner.getUuid());
		String[] type1 = request.getParameterValues("roleId");  
		List<Role> relatedRoleList = new ArrayList<Role>();

		if (type1!=null){

			for (int i = 0; i < type1.length; i++) {  
				Role role=new Role();
				role.setRoleId(Integer.parseInt(type1[i]));
				relatedRoleList.add(role);
				logger.debug(type1[i]+"****************************************************************************");
			}   
			child.setRelatedRoleList(relatedRoleList);
		}else{
			partnerRoleRelationService.deleteByUuid(child.getUuid());
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
				//String dataStr = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
				String dataStr = ServletRequestUtils.getStringParameter(request,"data." + dataDefine.getDataCode());
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

		logger.debug(child.toString()+"****************************************************************************");
		logger.debug(child.getNickName()+child.getUsername()+"++++++++++++++"+child.getUserPassword()+"++++++++++++++"+child.getAuthKey()+"++++++++++++++");
		if(partnerService.insert(child) == 1 ){
			cacheService.delete(new CacheCriteria(CommonStandard.cacheNameUser, "Partner#"+partner.getUuid()));
			message = new EisMessage(OperateResult.success.getId(),"操作完成");		
		} else {
			message = new EisMessage(OperateResult.failed.getId(),"操作失败");
		}

		map.put("message", message);
		return view;
	}
	@RequestMapping(value="/update" + "/{uuid}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("uuid")int uuid) throws Exception {

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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);

		//////////////////////// 结束标准流程 ///////////////////////
		
		if(!isPlatformGenericPartner){
			//不是内部用户，检查是不是访问自己或下级账户
			if(partner.getUuid() == uuid || partnerService.isValidSubUser(partner.getUuid(), uuid)){
				//用户合法访问
			} else {
				map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
				return CommonStandard.partnerMessageView;
			}
		}
		//得到当前上级headUuid
		User thisPartner = partnerService.select(uuid);
		//得到headUuid的用户名
		
		String modelType = request.getRequestURI().split("/")[1].replaceAll("\\.\\w*$", "");

		//对于当前请求为/channel的，只允许查询extraType>0的用户类型，不允许查询=0的内部用户
		if(modelType.equalsIgnoreCase("channel") && thisPartner.getUserExtraTypeId() <= 0){
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "无权访问", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}


		ProductCriteria ProductCriteria = new ProductCriteria();
		ProductCriteria.setCurrentStatus(BasicStatus.normal.getId());

		List<Product> ProductList = ProductService.list(ProductCriteria);

		ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria();

		shareConfigCriteria.setShareUuid(uuid);
		/*	SimpleDateFormat  fmt = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
		thisPartner.setCreateTime(fmt.parse(fmt.format(thisPartner.getCreateTime())));*/
		List<ShareConfig> shareConfigList = shareConfigService.list(shareConfigCriteria);

		float  maxChannelSharePercent=configService.getFloatValue("maxChannelSharePercent", ownerId);
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
		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setRoleLevel(RoleCriteria.ROLE_LEVEL_MERCHANT);
		//所有可用的角色列表
		List<Role> partnerAllRoleList = partnerRoleService.list(roleCriteria);

		UserRoleRelationCriteria partnerRoleRelationCriteria = new UserRoleRelationCriteria();
		partnerRoleRelationCriteria.setUuid(uuid);

		List<UserRoleRelation> partnerRoleRelationList = partnerRoleRelationService.list(partnerRoleRelationCriteria);
		map.put("statusCodeList", SecurityStandard.UserStatus.values());
		map.put("userExtraTypeList",UserExtraType.values());
		map.put("partner", thisPartner);	

		map.put("maxChannelSharePercent", maxChannelSharePercent);		
		map.put("ProductList", ProductList);
		map.put("shareConfigList", shareConfigList);
		map.put("partnerRoleList", partnerRoleRelationList);
		map.put("partnerAllRoleList", partnerAllRoleList);
		map.put("partnerList", partnerList);

		//logger.error(shareConfigList);
		return "partner/update";
	}

	/**
	 * 显示商户自己的配置
	 * 
	 */
	@RequestMapping(value="/config" + "/{uuid}", method=RequestMethod.GET)
	public String getConfig(HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("uuid")long uuid) throws Exception {

		final String view = "common/merchant/config";

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());

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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);

		if(!isPlatformGenericPartner){
			//不是内部用户，检查是不是访问自己或下级账户
			if(partner.getUuid() == uuid || partnerService.isValidSubUser(partner.getUuid(), uuid)){
				//用户合法访问
			} else {
				map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
				return CommonStandard.partnerMessageView;
			}
		}

		User thisPartner = partnerService.select(uuid);
		if(thisPartner == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "找不到指定的用户", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		Map<String, UserData> configMap = new HashMap<String,UserData>();
		if(thisPartner.getUserConfigMap() == null || thisPartner.getUserConfigMap().size() < 1){
			//读取系统配置
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
			dataDefineCriteria.setObjectType(ObjectType.user.name());
			dataDefineCriteria.setObjectId(UserTypes.partner.getId());
			List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
			if(dataDefineList == null || dataDefineList.size() < 1){
				logger.error("ownerId=" + ownerId + "的系统没有针对合作伙伴的数据定义");
			} else {
				for(DataDefine dataDefine : dataDefineList){
					if(dataDefine.getDisplayLevel() == null || dataDefine.getDisplayLevel().equals(DisplayLevel.user.toString()) || dataDefine.getDisplayLevel().equals(DisplayLevel.open.toString()) || isPlatformGenericPartner){
						//添加一个新的数据
						UserData userData = new UserData(dataDefine);
						if(!isPlatformGenericPartner && userData.getInputLevel() != null && (userData.getInputLevel().equals(InputLevel.system.name()) || userData.getInputLevel().equals(InputLevel.platform.name()))){	
							//非平台内部用户，如果输入级别是platform或system，那么必须是只读
							userData.setReadonly(true);
						}
						logger.debug(userData.getDataValue()+"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						configMap.put(userData.getDataCode(), userData);
					}
				}
			}
		} else {
			for(UserData oldData : thisPartner.getUserConfigMap().values()){
				if(oldData.getDisplayLevel() == null || oldData.getDisplayLevel().equals(DisplayLevel.user.toString()) || oldData.getDisplayLevel().equals(DisplayLevel.open.toString()) || isPlatformGenericPartner){
					//添加一个新的数据
					UserData userData = new UserData(oldData);
					userData.setDataValue(oldData.getDataValue());
					if(!isPlatformGenericPartner && userData.getInputLevel() != null && (userData.getInputLevel().equals(InputLevel.system.name()) || userData.getInputLevel().equals(InputLevel.platform.name()))){	
						//非平台内部用户，如果输入级别是platform或system，那么必须是只读
						userData.setReadonly(true);
						logger.debug(oldData.getDataValue()+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					}
					configMap.put(userData.getDataCode(), userData);
				}
			}

		}
		//thisPartner.setUserConfigMap((HashMap<String, UserData>) configMap);
		User clone = thisPartner.clone();
		HashMap<String,UserData> userConfigMap = clone.getUserConfigMap();
		if (userConfigMap != null && userConfigMap.size() > 0) {
			for (String dataCode : configMap.keySet()) {
				if (userConfigMap.get(dataCode)!=null) {
					UserData userData = userConfigMap.get(dataCode);
					if (configMap.get(dataCode).isReadonly()) {
						userData.setReadonly(true);
					}
					configMap.put(dataCode, userData);
				}
			}
		}
		map.put("configMap", configMap);
		map.put("partner", thisPartner);
		return view;
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)	
	@AllowJsonOutput
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
		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		if(!isPlatformGenericPartner){
			//不是内部用户，检查是不是访问自己或下级账户
			if(partner.getUuid() == child.getUuid() || partnerService.isValidSubUser(partner.getUuid(), child.getUuid())){
				//用户合法访问
			} else {
				map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
				return CommonStandard.partnerMessageView;
			}
		}
		String modelType = request.getRequestURI().split("/")[1].replaceAll("\\.\\w*$", "");

		//对于当前请求为/channel的，只允许查询extraType>0的用户类型，不允许查询=0的内部用户
		if(modelType.equalsIgnoreCase("channel") && partner.getUserExtraTypeId() <= 0){
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "无权访问", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}
		String dataCode = ServletRequestUtils.getStringParameter(request, "updateDataCode");
		if(StringUtils.isNotBlank(dataCode)){
			//仅更新某个扩展属性
			dataCode=dataCode.substring(5);
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
			dataDefineCriteria.setObjectType(ObjectType.user.name());
			dataDefineCriteria.setObjectId(UserTypes.partner.getId());
			dataDefineCriteria.setDataCode(dataCode);
			logger.debug(ObjectType.partner.name()+"************"+dataCode+"******"+UserTypes.partner.getId());
			DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
			if(dataDefine == null){
				logger.error("找不到要更新的商户扩展数据定义:" + dataCode);
				map.put("message", new EisMessage(EisError.dataError.getId(), "找不到要更新的数据"+dataCode));
				return CommonStandard.partnerMessageView;
			}
			if(dataDefine.getInputLevel() != null){
				if(dataDefine.getInputLevel().equals(InputLevel.system.toString())){
					logger.error("不允许更新system级别的扩展数据:" + dataCode);
					map.put("message", new EisMessage(EisError.dataError.getId(), "找不到要更新的数据"));
					return CommonStandard.partnerMessageView;
				}
				if(!isPlatformGenericPartner && dataDefine.getInputLevel().equals(InputLevel.platform.toString())){
					logger.error("当前不是内部用户，不允许更新platform级别的扩展数据:" + dataCode);
					map.put("message", new EisMessage(EisError.dataError.getId(), "找不到要更新的数据"));
					return CommonStandard.partnerMessageView;
				}
			}
			
			String dataValue = ServletRequestUtils.getStringParameter(request,"data." + dataCode);
			logger.debug(dataValue+"*******************************************************************************");
			if(StringUtils.isBlank(dataValue)){
				logger.error("要求更新商户[" + child.getUuid() + "]的数据:" + dataCode + "但是未提交数据的值");
				map.put("message", new EisMessage(EisError.dataError.getId(), "请提交要更新的值"));
				return CommonStandard.partnerMessageView;
			}
			UserData userData = null;
			User user = partnerService.select(child.getUuid());
			if(user.getUserConfigMap() == null){
				user.setUserConfigMap(new HashMap<String,UserData>());
			}
			userData = user.getUserConfigMap().get(dataCode);

			if(userData == null){
				logger.warn("在被修改的商户[" + child.getUuid() + "]中没有要更新的属性:" + dataCode);
				userData = new UserData(dataDefine);
				userData.setUuid(child.getUuid());
				userData.setDataValue(dataValue);
				int rs = userDataService.insert(userData);
				if(rs == 1){
					messageService.sendJmsDataSyncMessage(null, "userDataService", "insert", userData);
				}
			} else {
				logger.debug(userData.getUserDataId()+"+++++++++++++++++++++++++++++");
				userData.setDataValue(dataValue);
				int rs = userDataService.update(userData);
				logger.debug(userData.toString()+"*******************************************************************************");
				if(rs == 1){
					messageService.sendJmsDataSyncMessage(null, "userDataService", "update", userData);
				}
			}
			cacheService.delete(new CacheCriteria(CommonStandard.cacheNameUser, "Partner#"+child.getUuid()));
			partner.getUserConfigMap().put(dataCode, userData);
			map.put("message", new EisMessage(OperateResult.success.id,"更新成功"));
			return CommonStandard.partnerMessageView;

		}
		String roleIds=ServletRequestUtils.getStringParameter(request, "roleId");
		if (StringUtils.isNotBlank(roleIds)){
			String[] type1 = roleIds.split(",");
			partnerRoleRelationService.deleteByUuid(child.getUuid());
			for (int i = 0; i < type1.length; i++) {  
				
				int roleId = Integer.parseInt(type1[i]);
				Role role = partnerRoleService.select(roleId);
				if(role == null){
					logger.error("找不到要设置的商户角色:" + roleId);
					continue;
				}
				if(role.getRoleLevel() != RoleCriteria.ROLE_LEVEL_MERCHANT){
					logger.error("要设置的商户角色:" + role + "不是商户角色");
					continue;
				}
				UserRoleRelation partnerRoleRelation = new UserRoleRelation();
				partnerRoleRelation.setUuid(child.getUuid());
				partnerRoleRelation.setRoleId(roleId);
				partnerRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				partnerRoleRelation.setOwnerId(ownerId);
				partnerRoleRelationService.insert(partnerRoleRelation );
			}   
		}
		
		
		long headUuid = ServletRequestUtils.getLongParameter(request, "headUuid", 0);
		child.setHeadUuid(headUuid);
		processPartnerData(request, child);
		EisMessage message = null;
		logger.debug(child.toString()+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if(partnerService.updateNoNull(child) > 0){
			logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			messageService.sendJmsDataSyncMessage(null, "partnerService", "insert", child);
			cacheService.delete(new CacheCriteria(CommonStandard.cacheNameUser, "Partner#"+child.getUuid()));
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
		logger.debug("更新的合作伙伴[" + partner.getUserTypeId() + "/" + partner.getUserExtraTypeId() + "]，该用户类型有" + (userDataDefinePolicyList == null ? 0 : userDataDefinePolicyList.size()) + "条数据规范，从请求中得到了[" + (partner.getUserConfigMap() == null ? 0 : partner.getUserConfigMap().size()) + "]条数据");
	}


	

}
