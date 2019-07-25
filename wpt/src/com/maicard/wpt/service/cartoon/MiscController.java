package com.maicard.wpt.service.cartoon;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.domain.Node;
import com.maicard.site.service.NodeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

import static com.maicard.standard.CommonStandard.frontMessageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Cartoon项目的一些非标准交互
 * 
 * 
 * @author GHOST
 * @date 2018-10-25
 *
 */
@Controller
@RequestMapping("/misc")
public class MiscController extends BaseController {


	@Resource
	private CertifyService certifyService;

	@Resource
	private UserRelationService userRelationService;

	@Resource
	private NodeService nodeService;

	private int rowsPerPage = 10;

	/**
	 * 评分的上限
	 */
	static final int MAX_PARSE_SCORE = 10;



	/**
	 * 用户查看浏览记录
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-25
	 */
	@RequestMapping(value="/bookHistory",method=RequestMethod.GET )
	public String bookHistory(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return frontMessageView;
		}
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		userRelationCriteria.setPaging(paging);
		userRelationCriteria.setOrderBy(" last_use DESC");
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		List<Node> bookHistory = new ArrayList<Node>();
		if(userRelationList.size() > 0) {
			for(UserRelation userRelation : userRelationList) {
				Node node = nodeService.select((int)userRelation.getObjectId());
				if(node != null) {
					bookHistory.add(node);
				}
			}
		}
		map.put("subNodeList", bookHistory);
		return frontMessageView;
	}

	/**
	 * 用户要求删除某个记录
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-26
	 */
	@RequestMapping(value="/deleteHistory",method=RequestMethod.POST )
	public String deleteHistory(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return frontMessageView;
		}
		int nodeId = ServletRequestUtils.getIntParameter(request, "nodeId", 0);
		if(nodeId <= 0) {
			logger.error("用户：{}请求删除浏览记录但是没有提交nodeId", frontUser.getUuid());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId()));
			return frontMessageView;
		}
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectId(nodeId);
		Paging paging = new Paging(1);
		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		if(userRelationList.size() <= 0) {
			logger.warn("未找到用户：{}请求删除的浏览记录nodeId={}", frontUser.getUuid(), nodeId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId()));
			return frontMessageView;
		}
		//用户删除浏览记录，但是系统并不真正删除
		UserRelation userRelation = userRelationList.get(0);
		userRelation.setCurrentStatus(BasicStatus.deleted.id);
		int rs = userRelationService.update(userRelation);
		logger.info("用户：{}请求删除浏览记录nodeId={}，将其状态改为已删除，结果:{}", frontUser.getUuid(), nodeId, rs);
		map.put("message", new EisMessage(OperateResult.success.id));
		return frontMessageView;
	}

	/**
	 * 用户给某个栏目打分
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-29
	 */
	@RequestMapping(value="/praise",method=RequestMethod.POST )
	public String praise(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return frontMessageView;
		}
		float pariseScore = ServletRequestUtils.getIntParameter(request, "score", -1);
		int nodeId = ServletRequestUtils.getIntParameter(request, "nodeId", 0);
		if(nodeId <= 0) {
			logger.error("用户：{}请求评分但是没有提交nodeId", frontUser.getUuid());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId()));
			return frontMessageView;
		}
		if(pariseScore < 0) {
			logger.error("用户：{}请求对栏目:{}评分但是没有提交分数", frontUser.getUuid(), nodeId);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId()));
			return frontMessageView;
		}
		if(pariseScore > MAX_PARSE_SCORE) {
			logger.error("用户：{}请求对栏目:{}评分但分数错误:{}", frontUser.getUuid(), nodeId, pariseScore);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId()));
			return frontMessageView;
		}
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_PRAISE);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectId(nodeId);
		Paging paging = new Paging(1);
		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		if(userRelationList.size() > 0) {
			logger.warn("用户：{}已经有针对nodeId={}的评分:{}", frontUser.getUuid(), nodeId, userRelationList.get(0));
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId()));
			return frontMessageView;
		}
		UserRelation userRelation = new UserRelation(frontUser.getOwnerId());
		userRelation.setObjectType(ObjectType.node.name());
		userRelation.setObjectId(nodeId);
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setActivity((int) pariseScore * 100);
		userRelation.setCurrentStatus(BasicStatus.normal.id);
		int rs = userRelationService.insert(userRelation);
		logger.info("用户：{}给栏目:{}评分:{}，结果:{}", frontUser.getUuid(), nodeId, pariseScore, rs);
		map.put("message", new EisMessage(OperateResult.success.id));
		return frontMessageView;
	}

	/**
	 * 列出已收藏的栏目
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-29
	 */
	@RequestMapping(value="/listFavorite",method=RequestMethod.GET )
	public String listFavorite(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return frontMessageView;
		}
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		userRelationCriteria.setPaging(paging);
		userRelationCriteria.setOrderBy(" last_use DESC");
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		List<Node> bookHistory = new ArrayList<Node>();
		if(userRelationList.size() > 0) {
			for(UserRelation userRelation : userRelationList) {
				Node node = nodeService.select((int)userRelation.getObjectId());
				if(node != null) {
					bookHistory.add(node);
				}
			}
		}
		map.put("subNodeList", bookHistory);
		return frontMessageView;
	}

	/**
	 * 收藏某个栏目
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-29
	 */
	@RequestMapping(value="/addFavorite",method=RequestMethod.POST )
	public String addFavorite(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return frontMessageView;
		}
		int nodeId = ServletRequestUtils.getIntParameter(request, "nodeId", 0);
		if(nodeId <= 0) {
			logger.error("用户：{}请求收藏栏目但是没有提交nodeId", frontUser.getUuid());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId()));
			return frontMessageView;
		}

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectId(nodeId);
		Paging paging = new Paging(1);

		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		if(userRelationList.size() > 0) {
			UserRelation userRelation = userRelationList.get(0);
			userRelation.setCurrentStatus(BasicStatus.normal.id);
			int rs = userRelationService.update(userRelation);
			logger.warn("用户：{}已经收藏了栏目:{}，改为正常状态,结果:{}", frontUser.getUuid(), nodeId,rs);
		} else {
			UserRelation userRelation = new UserRelation(frontUser.getOwnerId());
			userRelation.setObjectType(ObjectType.node.name());
			userRelation.setObjectId(nodeId);
			userRelation.setUuid(frontUser.getUuid());
			userRelation.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
			userRelation.setCurrentStatus(BasicStatus.normal.id);
			int rs = userRelationService.insert(userRelation);
			logger.info("用户：{}收藏栏目:{}结果:{}", frontUser.getUuid(), nodeId, rs);
		}
		map.put("message", new EisMessage(OperateResult.success.id));
		return frontMessageView;
	}

	@RequestMapping(value="/deleteFavorite",method=RequestMethod.POST )
	public String deleteFavorite(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return frontMessageView;
		}
		int nodeId = ServletRequestUtils.getIntParameter(request, "nodeId", 0);
		if(nodeId <= 0) {
			logger.error("用户：{}请求取消收藏栏目但是没有提交nodeId", frontUser.getUuid());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId()));
			return frontMessageView;
		}

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectId(nodeId);
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		Paging paging = new Paging(1);

		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		if(userRelationList.size() <= 0) {
			logger.warn("用户：{}没有收藏栏目:{}", frontUser.getUuid(), nodeId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId()));
			return frontMessageView;
		}
		UserRelation userRelation = userRelationList.get(0);
		userRelation.setCurrentStatus(BasicStatus.deleted.id);
		int rs = userRelationService.update(userRelation);
		logger.info("用户：{}取消收藏栏目:{}结果:{}", frontUser.getUuid(), nodeId, rs);
		map.put("message", new EisMessage(OperateResult.success.id));
		return frontMessageView;
	}
	

}
