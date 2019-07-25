package com.maicard.wpt.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 评论管理
 * 
 * @author Pengzhenggang
 * @data 2016-5-11
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private CommentService commentService;
	@Resource
	private ProductService productService;
	@Resource
	private NodeService nodeService;
	@Resource
	private DocumentService documentService;
	@Resource
	private FrontUserService frontUserService;

	private int rowsPerPage = 10;

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init() {
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(), 0);
		if (rowsPerPage < 1) {
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}

	/**
	 * 评论列表
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @param commentCriteria
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			CommentCriteria commentCriteria) throws Exception {
		final String view = "common/comment/list";
		logger.debug("评论管理");
		map.put("title", "评论列表");
		// 登录检查
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
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
			return view;
		}
		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return view;
		}

		int totalRows = 0;
		String userRealName = request.getParameter("userRealName"); // 昵称
		String refTitle = request.getParameter("refTitle"); // 标题
		String startTimeBegin = request.getParameter("startTimeBegin"); // 开始时间
		String startTimeEnd = request.getParameter("startTimeEnd"); // 结束时间

		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		// 结束登录检查
		// 通过昵称查找评论
		List<Comment> commentLists1 = null;
		List<Comment> commentList = new ArrayList<Comment>();
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setOwnerId(partner.getOwnerId());
		if (userRealName != null && !userRealName.isEmpty()) {
			logger.debug("昵称 ：" + userRealName);
			frontUserCriteria.setNickName(userRealName);
			List<User> frontUserLists = frontUserService.list(frontUserCriteria);
			if (frontUserLists != null && frontUserLists.size() > 0 && frontUserLists.size() < 2) {
				logger.debug("通过昵称查找到[" + frontUserLists.size() + "]个用户");
				commentCriteria.setUuid(frontUserLists.get(0).getUuid());
				
				// 通过标题查找评论
				if (refTitle != null && !refTitle.isEmpty()) {
					List<Comment> tempList = new ArrayList<Comment>();
					DocumentCriteria documentCriteria = new DocumentCriteria();
					documentCriteria.setOwnerId(ownerId);
					documentCriteria.setTitle(refTitle);
					List<Document> documentLists = documentService.list(documentCriteria);
					if (documentLists != null && documentLists.size() > 0) {
						for (Document document : documentLists) {
							commentCriteria.setObjectId(document.getUdid());
							commentCriteria.setObjectType(ObjectType.document.toString());
							commentCriteria.setOwnerId(ownerId);
							totalRows += commentService.count(commentCriteria);
							logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。");
							List<Comment> commentLists = commentService.list(commentCriteria);
							if (commentLists != null && !commentLists.isEmpty()) {
								for (Comment comment : commentLists) {
									tempList.add(comment);
								}
							}
						}
					}
					commentList = tempList;
				} else {

					// 通过时间段查找评论
					if (startTimeBegin != null && !startTimeBegin.isEmpty() && startTimeEnd != null
							&& !startTimeEnd.isEmpty()) {
						logger.debug("查询的评论开始时间:" + startTimeBegin);
						logger.debug("查询的评论结束时间:" + startTimeEnd);
						commentCriteria.setCreateTimeBegin(sdf.parse(startTimeBegin));
						commentCriteria.setCreateTimeEnd(sdf.parse(startTimeEnd));
					}

					commentCriteria.setOwnerId(ownerId);

					Paging paging = new Paging(rows);
					paging.setCurrentPage(page);
					commentCriteria.setPaging(paging);
					// int totalRows = 0;
					// List<Comment> commentList = null;

					totalRows = commentService.count(commentCriteria);
					commentList = commentService.listOnPage(commentCriteria);
					logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。");

				}
			} else if (frontUserLists != null && frontUserLists.size() > 1) {
				logger.debug("通过昵称查找到[" + frontUserLists.size() + "]个用户");
				for (User user : frontUserLists) {
					commentCriteria.setUuid(user.getUuid());
					commentCriteria.setOwnerId(ownerId);

					totalRows += commentService.count(commentCriteria);
					commentLists1 = commentService.list(commentCriteria);
					if (commentLists1 != null && commentLists1.size() > 0) {
						commentList.addAll(commentLists1);
					}

				}
				logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。");
			}
		} else {

			// 通过标题查找评论
			if (refTitle != null && !refTitle.isEmpty()) {
				List<Comment> tempList = new ArrayList<Comment>();
				DocumentCriteria documentCriteria = new DocumentCriteria();
				documentCriteria.setOwnerId(ownerId);
				documentCriteria.setTitle(refTitle);
				List<Document> documentLists = documentService.list(documentCriteria);
				if (documentLists != null && documentLists.size() > 0) {
					for (Document document : documentLists) {
						commentCriteria.setObjectId(document.getUdid());
						commentCriteria.setObjectType(ObjectType.document.toString());
						commentCriteria.setOwnerId(ownerId);
						totalRows += commentService.count(commentCriteria);
						logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。");
						List<Comment> commentLists = commentService.list(commentCriteria);
						if (commentLists != null && !commentLists.isEmpty()) {
							for (Comment comment : commentLists) {
								tempList.add(comment);
							}
						}
					}
				}
				commentList = tempList;
			} else {

				// 通过时间段查找评论
				if (startTimeBegin != null && !startTimeBegin.isEmpty() && startTimeEnd != null
						&& !startTimeEnd.isEmpty()) {
					logger.debug("查询的评论开始时间:" + startTimeBegin);
					logger.debug("查询的评论结束时间:" + startTimeEnd);
					commentCriteria.setCreateTimeBegin(sdf.parse(startTimeBegin));
					commentCriteria.setCreateTimeEnd(sdf.parse(startTimeEnd));
				}

				commentCriteria.setOwnerId(ownerId);

				Paging paging = new Paging(rows);
				paging.setCurrentPage(page);
				commentCriteria.setPaging(paging);
				// int totalRows = 0;
				// List<Comment> commentList = null;

				totalRows = commentService.count(commentCriteria);
				commentList = commentService.listOnPage(commentCriteria);
				logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。");

			}
		}
		map.put("total", totalRows);
		if (totalRows < 1) {
			logger.debug("当前返回数据行数是0");
			return view;
		}

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setOwnerId(ownerId);
		//nodeCriteria.setSiteCode("yixian");
		List<Node> nodeList = nodeService.list(nodeCriteria);
		if (nodeList == null || nodeList.size() < 1) {
			logger.debug("栏目为空");
			map.put("message", new EisMessage(EisError.dataError.getId(), "栏目为空！"));
			return CommonStandard.partnerMessageView;
		}
		map.put("node", nodeList);
		// List<Comment> commentList =
		// commentService.listOnPage(commentCriteria);
		if (commentList == null || commentList.size() < 1) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "没有评论信息"));
			return view;
		}
		ArrayList<Comment> comment2List = new ArrayList<Comment>();
		for (Comment comment : commentList) {
			Comment comment2 = (Comment) comment.clone();
			comment2.setOperate(new HashMap<String, String>());
			comment2.getOperate().put("get", "./comment/" + Operate.get.name() + "/" + comment2.getCommentId());
			comment2.getOperate().put("update",
					"./comment/" + Operate.update.name() + "/" + comment2.getCommentId());
			comment2.getOperate().put("del", "./comment/" + Operate.delete.name() + "/" + comment2.getCommentId());
			/*
			 * logger.debug("类型1 : " + comment2.getObjectType() + "  类型2 ： " +
			 * ObjectType.product.toString()); HashMap<String, String> dataMap =
			 * new HashMap<String, String>(); if
			 * (comment2.getObjectType().equals(ObjectType.product.toString()))
			 * { logger.debug("XXX1"); Product product =
			 * productService.select(comment2.getObjectId()); if (product ==
			 * null) { throw new ObjectNotFoundByIdException("找不到被评论的Product对象["
			 * + comment2.getObjectId() + "]"); } dataMap.put("name",
			 * product.getProductName()); comment2.setData(dataMap);
			 * logger.debug("XXX2"); } else { dataMap.put("name",
			 * comment2.getObjectId() + ""); comment2.setData(dataMap); }
			 * 
			 * logger.debug("评论状态 ： " + comment2.getCurrentStatusName());
			 */
			// 当状态是141001待审核时，显示审核按钮；状态是141002已发布时，显示取取消审核按钮
			if (comment2.getCurrentStatus() == CommentCriteria.STATUS_WAIT_EXAMINE) {
				comment2.getOperate().put("relate", "./comment/" + Operate.relate.name());
			} else if (comment2.getCurrentStatus() == CommentCriteria.STATUS_PUBLISHED) {
				comment2.getOperate().put("clear", "./comment/" + Operate.clear.name());
			}
			comment2List.add(comment2);
			logger.debug("一条评论的标题"+comment2.getTitle());
		}
		logger.debug("系统共有[" + comment2List.size() + "]条评论");
		if (commentCriteria.getPaging() != null) {
			// 计算并放入分页
			map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		}
		List<List<Comment>> commentList2 = commentService.sort(comment2List);
		map.put("uuid", partner.getUuid());
		map.put("nickName", partner.getNickName());
		map.put("rows", commentList2);
		return view;
	}

	/**
	 * 查看
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @param commentCriteria
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get/{commentId}", method = RequestMethod.POST)
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("commentId") Integer commentId) throws Exception {
		final String view = "common/comment/get";
		logger.debug("要查看的评论 ：" + commentId);
		// 登录检查
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
			return view;
		}
		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return view;
		}

		if (commentId == 0) {
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "没有指定要获取的评论ID[commentId == 0"));
			return CommonStandard.partnerMessageView;
		}
		Comment comment = commentService.select(commentId);
		if (comment == null) {
			logger.warn("找不到要查看的评论[" + commentId + "]");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要查看的评论"));
			return CommonStandard.partnerMessageView;
		}
		if (comment.getOwnerId() != partner.getOwnerId()) {
			logger.info("要查看的评论[" + commentId + "],ownerId[" + comment.getOwnerId() + "]与当前用户[" + partner.getUuid()
					+ "]的ownerId[" + partner.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要查看的评论"));
			return CommonStandard.partnerMessageView;
		}
		logger.debug("comment扩展:" + comment.getData());
		Comment comment2 = comment.clone();
		if (comment2.getObjectType().equals(ObjectType.product.toString())) {
			logger.debug("XXX1");
			Product product = productService.select(comment2.getObjectId());
			if (product == null) {
				throw new ObjectNotFoundByIdException("找不到被评论的Product对象[" + comment2.getObjectId() + "]");
			}
			comment2.getData().put("name", product.getProductName());
			logger.debug("XXX2");
		} else {
			comment2.getData().put("name", comment2.getObjectId() + "");
		}
		logger.debug("扩展数据 ： " + comment2.getData());

		map.put("objectType", comment2.getObjectType());
		map.put("comment", comment2);
		return view;
	}

	/**
	 * 删除评论
	 */
	@RequestMapping(value = "/delete/{commentId}", method = RequestMethod.GET)
	@AllowJsonOutput
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("commentId") Integer commentId) throws Exception {
		logger.debug("要删除的评论 : " + commentId);
		// 登录检查
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

		if (commentId == 0) {
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "没有指定要获取的评论ID[commentId==0]"));
			return CommonStandard.partnerMessageView;
		}

		Comment comment = commentService.select(commentId);
		if (comment == null) {
			logger.warn("找不到要删除的评论[" + commentId + "]");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要删除的评论"));
			return CommonStandard.partnerMessageView;
		}
		if (comment.getOwnerId() != partner.getOwnerId()) {
			logger.info("要删除的评论[" + commentId + "],ownerId[" + comment.getOwnerId() + "]与当前用户[" + partner.getUuid()
					+ "]的ownerId[" + partner.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要删除的评论"));
			return CommonStandard.partnerMessageView;
		}

		int rs = commentService.delete(commentId);
		logger.debug("用户[" + partner.getUuid() + "]删除评论[" + comment + "]，结果:" + rs);
		if (rs == 1) {
			map.put("message", new EisMessage(OperateResult.success.getId(), "评论已删除"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，评论删除失败"));
		}
		return CommonStandard.partnerMessageView;
	}

	/**
	 * 批量删除
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@AllowJsonOutput
	public String deleteList(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		String[] ids = idList.split("-");
		// 登录检查
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
		int successDeleteCount = 0;
		String errors = "";
		for (int i = 0; i < ids.length; i++) {
			int deleteId = Integer.parseInt(ids[i]);
			Comment comment = commentService.select(deleteId);
			if (comment == null) {
				logger.warn("找不到要删除的评论，ID=" + deleteId);
				continue;
			}

			if (comment.getOwnerId() != partner.getOwnerId()) {
				logger.warn("要删除的评论，ownerId[" + comment.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try {
				int commentId = Integer.parseInt(ids[i]);
				if (commentService.delete(commentId) > 0) {
					successDeleteCount++;

				}
			} catch (Exception e) {
				String error = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}
		}
		String messageContent = "成功删除[" + successDeleteCount + "]个.";
		if (!errors.equals("")) {
			messageContent += errors;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(), messageContent));
		return CommonStandard.partnerMessageView;
	}

	// 对某个评论点赞或无用
	public String useful() {
		return null;

	}

	// 回复评论
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		//////////////////////////// 标准检查流程 ///////////////////////////////////
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
		String commentIdStr = request.getParameter("commentId");
		String content = request.getParameter("content");
		String uuidStr = request.getParameter("uuid");
		String userRealName = request.getParameter("nickName");
		int commentId = 0;
		if (NumericUtils.isNumeric(commentIdStr)) {
			commentId = Integer.parseInt(commentIdStr);
		}
		Comment comment = commentService.select(commentId);
		if (comment == null) {
			logger.error("没找到要回复评论的ID " + commentId);
			map.put("message", new EisMessage(OperateResult.failed.getId(), "没找到要评论的ID" + commentId));
			return CommonStandard.partnerMessageView;
		}

		if (StringUtils.isEmpty(content)) {
			logger.error("回复内容为空 ");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "回复内容为空"));
			return CommonStandard.partnerMessageView;
		}
		int uuid = 0;
		if (NumericUtils.isNumeric(uuidStr)) {
			uuid = Integer.parseInt(uuidStr);
		}
		if (StringUtils.isEmpty(userRealName)) {
			logger.debug("回复用户空 ");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "回复用户空"));
			return CommonStandard.partnerMessageView;
		}

		Comment newComment = new Comment();
		newComment.setCommentId(0);
		newComment.setCommentConfigId(comment.getCommentConfigId());
		newComment.setRootCommentId(commentId);
		newComment.setUuid(uuid);
		newComment.setCreateTime(new Date());
		newComment.setObjectType(comment.getObjectType());
		newComment.setObjectId(comment.getObjectId());
		newComment.setContent(content);
		newComment.setExtraValue("userRealName", userRealName);
		newComment.setExtraValue("unique", "true");
		newComment.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
		newComment.setVersion(comment.getVersion());
		newComment.setOwnerId(comment.getOwnerId());
		EisMessage rs = commentService.insert(newComment);
		if (rs.getOperateCode() == OperateResult.success.id) {

			map.put("message", new EisMessage(OperateResult.success.getId(), "回复成功"));
		} else {
			map.put("message", rs);
		}
		return CommonStandard.partnerMessageView;
	}

	// 审核评论
	@RequestMapping(value = "/relate", method = RequestMethod.POST)
	@AllowJsonOutput
	public String auditPass(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		//////////////////////////// 标准检查流程 ///////////////////////////////////
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

		if (idList == null || idList.length() < 1) {
			logger.error("您提交要审核的评论的id [" + idList + "]为空");
			map.put("message", new EisMessage(EisError.dataError.getId(), "您提交要审核的评论的id [" + idList + "]为空"));
			return CommonStandard.partnerMessageView;
		}

		String[] ids = idList.split("-");
		for (int i = 0; i < ids.length; i++) {
			int auditCommentId = Integer.parseInt(ids[i]);
			Comment comment = commentService.select(auditCommentId);
			if (comment == null) {
				logger.error("没找到要审核的评论[" + auditCommentId + "]");
				map.put("message", new EisMessage(EisError.dataError.getId(), "没找到要审核的评论[" + auditCommentId + "]"));
				return CommonStandard.partnerMessageView;
			}
			Comment comment2 = comment.clone();
			if (comment2.getCurrentStatus() != CommentCriteria.STATUS_PUBLISHED) {
				logger.debug("用户[" + comment2.getUuid() + "]的评论的状态[" + comment2.getCurrentStatus() + "]没有审核，设置为已审核，并更新");
				comment2.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
				int rs = commentService.update(comment2);
				if (rs == 1) {
					map.put("message", new EisMessage(OperateResult.success.getId(), "审核通过"));
				} else {
					map.put("message", new EisMessage(OperateResult.failed.getId(), "审核失败"));
				}
			} else {
				logger.debug("用户[" + comment2.getUuid() + "]的评论的状态[" + comment2.getCurrentStatus() + "]为已审核，不做更新");
				map.put("message", new EisMessage(OperateResult.success.getId(), "评论状态为已发布状态，不做更新"));
			}
		}
		return CommonStandard.partnerMessageView;
	}

	// 取消审核评论
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	@AllowJsonOutput
	public String cancelAudit(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		//////////////////////////// 标准检查流程 ///////////////////////////////////
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
		if (idList == null || idList.length() < 1) {
			logger.error("您提交要取消审核的评论的id [" + idList + "]为空");
			map.put("message", new EisMessage(EisError.dataError.getId(), "您提交要取消审核的评论的id [" + idList + "]为空"));
			return CommonStandard.partnerMessageView;
		}

		String[] ids = idList.split("-");
		for (int i = 0; i < ids.length; i++) {
			int auditCommentId = Integer.parseInt(ids[i]);
			Comment comment = commentService.select(auditCommentId);
			if (comment == null) {
				logger.error("没找到要取消审核的评论[" + auditCommentId + "]");
				map.put("message", new EisMessage(EisError.dataError.getId(), "没找到要取消审核的评论[" + auditCommentId + "]"));
				return CommonStandard.partnerMessageView;
			}
			Comment comment2 = comment.clone();
			if (comment2.getCurrentStatus() == CommentCriteria.STATUS_PUBLISHED) {
				logger.debug(
						"用户[" + comment2.getUuid() + "]的评论的状态[" + comment2.getCurrentStatus() + "]已发布，取消审核，变更为待审核");
				comment2.setCurrentStatus(CommentCriteria.STATUS_WAIT_EXAMINE);
				int rs = commentService.update(comment2);
				if (rs == 1) {
					map.put("message", new EisMessage(OperateResult.success.getId(), "取消审核成功"));
				} else {
					map.put("message", new EisMessage(OperateResult.failed.getId(), "取消审核失败"));
				}
			} else {
				logger.debug("用户[" + comment2.getUuid() + "]的评论的状态[" + comment2.getCurrentStatus() + "]为待审核，不做取消审核");
				map.put("message", new EisMessage(OperateResult.success.getId(), "评论状态为待审核状态，不做取消审核更新"));
			}
		}
		return CommonStandard.partnerMessageView;
	}
}
