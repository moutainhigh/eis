package com.maicard.wpt.controller.common;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.Paging;
import com.maicard.product.service.ProductService;
import com.maicard.product.service.StockService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.processor.PostUserRelationProcessor;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/userRelation")
public class UserRelationController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private DocumentService documentService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;

	@Resource
	private ProductService productService;

	@Resource
	private StockService stockService;

	@Resource
	private UserRelationService userRelationService;




	private int rowsPerPage = 10;

	//允许用户自行提交的对象
	static final String[] validSubmitObject = new String[] {ObjectType.node.name(), ObjectType.document.name()};

	//当建立或删除一个关联后执行后续操作的程序名称
	static final String postUserRelationProcessor = "postUserRelationProcessor";


	@PostConstruct
	public void init(){

		rowsPerPage = configService.getIntValue(DataName.frontRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE; 
		}

	}

	@RequestMapping(value="/index",method=RequestMethod.GET )
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map, UserRelationCriteria userRelationCriteria) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		final String view = "userRelation/index";
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		userRelationCriteria.setUuid(frontUser.getUuid());
		Paging paging = null;
		if(StringUtils.isBlank(userRelationCriteria.getObjectType())){
			logger.error("未提供列出的对象名");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "提交数据异常"));
			return view;
		}


		paging = new Paging(rows);
		paging.setCurrentPage(page);

		paging.setCurrentPage(page);

		userRelationCriteria.setPaging(paging);
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		if(userRelationList == null ||  userRelationList.size() <  1){
			return view;	
		}
		map.put("userRelationList", userRelationList);
		/*if(userRelationCriteria.getObjectType().equals(ObjectType.document.name())){
			List<Document> documentList = new ArrayList<Document>();
			for(UserRelation userRelation : userRelationList){
				Document document = documentService.select((int)userRelation.getObjectId());
				if(document != null){
					if(document.getDocumentDataMap() != null && document.getDocumentDataMap().get(DataName.productCode.toString()) != null){
						String productCode = document.getDocumentDataMap().get(DataName.productCode.toString()).getDataValue();
						if(productCode == null){
							logger.warn("产品文档[" + document.getUdid() + "]未定义扩展数据productCode的值");
							continue;
						}

						document.setFlag(1);

						Product product = productService.select(productCode, frontUser.getOwnerId());
						if(product == null){
							logger.warn("找不到产品文档[" + document.getUdid() + "]所定义的产品[" + productCode + "]");
							continue;
						}
						if(product.getProductDataMap() != null && product.getProductDataMap().size() > 0){
							for(ProductData pd : product.getProductDataMap().values()){
								DocumentData dd = new DocumentData();
								dd.setDataCode(pd.getDataCode());
								dd.setDataValue(pd.getDataValue());
								document.getDocumentDataMap().put(dd.getDataCode(), dd);
								logger.info("根据产品[" + product.getProductId() + "]为文档[" + document.getUdid() + "]生成附加数据[" + dd.getDataCode() + "=>" + dd.getDataValue());
							}	
						}
						PointExchangeCriteria pointExchangeCriteria = new PointExchangeCriteria();
						pointExchangeCriteria.setObjectType(ObjectType.product.name());
						pointExchangeCriteria.setObjectId(product.getProductId());
						pointExchangeCriteria.setCurrentStatus(BasicStatus.normal.getId());
						List<PointExchange> pointExchangeList = pointExchangeService.list(pointExchangeCriteria);
						if(pointExchangeList == null || pointExchangeList.size() < 1){
							logger.info("找不到产品ID=" + product.getProductId() + "的积分兑换规则");
						} else {
							PointExchange pointExchange = pointExchangeList.get(0);
							logger.info("经计算，产品[" + product.getProductId() + "]的积分兑换规则是[" + pointExchange + "]");
							String price = "";
							if(pointExchange.getCoin() > 0){
								price += "coin:" + pointExchange.getCoin() + ";";
							}
							if(pointExchange.getPoint() > 0){
								price += "point:" + pointExchange.getPoint();
							}
							price = price.replaceAll(";$","");
							DocumentData dd = new DocumentData();
							dd.setDataCode(DataName.productBuyMoney.toString());
							dd.setDataValue(price);
							logger.info("根据产品[" + product.getProductId() + "]为文档[" + document.getUdid() + "]生成productBuyMoney:" + price);
							document.getDocumentDataMap().put(DataName.productBuyMoney.toString(), dd);
						}
					}
					documentList.add(document);
				}
			}
			map.put("documentList", documentList);
		}*/
		return view;	

	}


	//提交一个关联
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, UserRelation userRelation) throws Exception {
		logger.debug("进入提交关联方法");
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行关注"));
			return CommonStandard.frontMessageView;
		}

		if(userRelation.getObjectId() <= 0) {
			logger.error("增加关联但是未提交objectId");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id));
			return CommonStandard.frontMessageView;
		}

		final String view = "userRelation/create";

		userRelation.setOwnerId(frontUser.getOwnerId());
		String relationType = ServletRequestUtils.getStringParameter(request, "type");
		//只允许提交这两种关联类型
		if(relationType != null && relationType.equalsIgnoreCase(UserRelationCriteria.RELATION_TYPE_FAVORITE)){
			relationType = UserRelationCriteria.RELATION_TYPE_FAVORITE;

		} else {
			relationType = UserRelationCriteria.RELATION_TYPE_PRAISE;
		}
		userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setRelationType(relationType);
		boolean isValidObject = false;
		for(String ot : validSubmitObject){
			if(userRelation.getObjectType().equalsIgnoreCase(ot)){
				isValidObject =true;
				break;
			}
		}
		if(!isValidObject){
			logger.error("提交需要关联的对象名称[" + userRelation.getObjectType() + "]不合法");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "提交数据异常"));
			return view;
		}

		//关注或收藏对象如果是文章，也查找该文章是否有小图

		//要关注的对象
		OpEisObject targetObject = stockService.getTargetObject(userRelation.getObjectType(), userRelation.getObjectId());

		if(targetObject == null){
			logger.error("找不到用户:{}要关注的对象:{}/{}", frontUser.getUuid(), userRelation.getObjectType(), userRelation.getObjectId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId()));
			return view;		
		}
		userRelation.setExtraValue(DataName.refUrl.toString(), targetObject.getViewUrl());
		userRelation.setExtraValue(DataName.refTitle.toString(), targetObject.getName());
		if(targetObject.getName() != null){
			userRelation.setExtraValue(DataName.refBrief.toString(), targetObject.getBrief() );
		}
		logger.debug("放入用户[" + frontUser.getUuid() + "]关注的文章[" + targetObject.getId() + "]链接地址:" + targetObject.getViewUrl());

		String thumbnail = targetObject.getExtraValue(DataName.thumbnail.toString());
		if(StringUtils.isNotBlank(thumbnail)){
			logger.debug("放入用户[" + frontUser.getUuid() + "]关注的文章[" + targetObject.getId() + "]缩略图:" + thumbnail);
			userRelation.setExtraValue(DataName.thumbnail.toString(), thumbnail);
		}
		userRelation.setCurrentStatus(BasicStatus.normal.getId());
		if(StringUtils.isBlank(userRelation.getRelationLimit())){
			userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		}
		if(userRelation.getRelationLimit().equalsIgnoreCase(UserRelationCriteria.RELATION_LIMIT_UNIQUE) 
				|| userRelation.getRelationLimit().equalsIgnoreCase(UserRelationCriteria.RELATION_LIMIT_GLOBAL_UNIQUE)){
			UserRelationCriteria userRelationCriteria = new UserRelationCriteria(userRelation.getOwnerId());
			userRelationCriteria.setUuid(userRelation.getUuid());
			userRelationCriteria.setObjectType(userRelation.getObjectType());
			userRelationCriteria.setObjectId(userRelation.getObjectId());
			userRelationCriteria.setRelationType(userRelation.getRelationType());
			int alreadyRelatedCount = userRelationService.count(userRelationCriteria);
			if(alreadyRelatedCount > 0){
				logger.error("用户[" + userRelation.getUuid() + "]已关注对象:" +  userRelation.getObjectType() + "#" + userRelation.getObjectId() + ",类型:" + userRelationCriteria.getRelationType() + ",且关注限制为:" + userRelation.getRelationLimit() + ",不再关注");
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(),"您已操作，不能再次操作"));
				return view;
			}
		}
		int rs = userRelationService.insert(userRelation);
		if(rs == 1){
			PostUserRelationProcessor processor = applicationContextService.getBeanGeneric(postUserRelationProcessor);
			if(processor != null) {
				processor.postProcess(frontUser, userRelation, Operate.create.name);
			}
			map.put("message", new EisMessage(OperateResult.success.getId(),"操作成功"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"操作失败"));
		}
		return view;	
	}

	//删除一个关联
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map, UserRelationCriteria userRelationCriteria) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再操作"));
			return CommonStandard.frontMessageView;
		}

		final String view = "userRelation/delete";


		long userRelationId = ServletRequestUtils.getLongParameter(request,"userRelationId");
		int rs = 0;

		UserRelation userRelation = null;
		if(userRelationId > 0){
			userRelation = userRelationService.select(userRelationId);
			if(userRelation == null) {
				logger.warn("找不到用户:{}请求删除的关联对象:{}", frontUser.getUuid(), userRelationId);
			} else {
				rs = userRelationService.delete(userRelationId);
				if(rs == 1) {
					PostUserRelationProcessor processor = applicationContextService.getBeanGeneric(postUserRelationProcessor);
					if(processor != null) {
						processor.postProcess(frontUser, userRelation, Operate.delete.name);
					}
				}

			}
		} else {		
			userRelationCriteria.setOwnerId(frontUser.getOwnerId());
			userRelationCriteria.setUuid(frontUser.getUuid());
			if(StringUtils.isBlank(userRelationCriteria.getObjectType())){
				logger.error("未提交需要删除的对象名称");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "提交数据异常"));
				return view;
			}
			if(userRelationCriteria.getObjectId() <= 0){
				logger.error("未提交需要删除的对象ID");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "提交数据异常"));
				return view;
			}
			if(StringUtils.isBlank(userRelationCriteria.getRelationType())){
				logger.error("未提交需要删除的关联类型");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "提交数据异常"));
				return view;
			}
			List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
			if(userRelationList.size() < 1) {
				logger.warn("找不到用户:{}请求删除的关联对象:{}", frontUser.getUuid(), JsonUtils.toStringFull(userRelationCriteria));
			} else {
				PostUserRelationProcessor processor = applicationContextService.getBeanGeneric(postUserRelationProcessor);

				for(UserRelation ur : userRelationList) {
					rs = userRelationService.delete(ur.getUserRelationId());
					if(rs == 1 && processor != null) {
						processor.postProcess(frontUser, ur, Operate.delete.name);
					}

				}
			}
		}
		if(rs == 1){

			map.put("message", new EisMessage(OperateResult.success.getId(),"取消成功"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"取消失败"));
		}
		return view;	
	}


}
