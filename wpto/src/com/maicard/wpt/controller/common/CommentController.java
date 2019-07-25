package com.maicard.wpt.controller.common;



import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.maicard.common.base.BaseController;
import com.maicard.common.base.UUIDFilenameGenerator;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.CommentConfig;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 用于用户查看、提交各类评论
 *
 * @author NetSnake
 * @date 2015年4月23
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController extends BaseController {
	
	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private CommentService commentService;

	@Resource
	private ConfigService configService;

	@Resource
	private CertifyService certifyService;	

	@Resource
	private ProductService productService;

	@Resource
	protected DocumentService documentService;
	private String documentUploadSaveDir;

	private int rowsPerPage = 10;


	@PostConstruct
	public void init(){
		documentUploadSaveDir = applicationContextService.getDataDir();

		rowsPerPage = configService.getIntValue(DataName.frontRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE; 
		}

	}

	@RequestMapping(value = "/index", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map, CommentCriteria commentCriteria) {
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		final String view = "comment/index";
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		commentCriteria.setOwnerId(frontUser.getOwnerId());
		commentCriteria.setUuid(frontUser.getUuid());
		

		int totalRows = commentService.count(commentCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		commentCriteria.setPaging(paging);
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));
		List<Comment> commentList = commentService.listOnPage(commentCriteria);
		/*if(commentList != null && commentList.size() > 0){
			for(Comment comment : commentList){
				afterFetch(comment);
			}
		}*/
		map.put("commentList", commentList);
		return view;
	}

	@RequestMapping(value = "/commentListOnPage", method=RequestMethod.GET)
	public String commentListOnPage(HttpServletRequest request, HttpServletResponse response, ModelMap map, CommentCriteria commentCriteria) {
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		final String view = "comment/index";
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return view;

		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		commentCriteria.setOwnerId(ownerId);
		commentCriteria.setCurrentStatus(141002);
		String flag = request.getParameter("flag");
		if (StringUtils.isNotBlank(flag)) {
			commentCriteria.setUuid(frontUser.getUuid());
		}
		boolean reload  = ServletRequestUtils.getBooleanParameter(request, "reload", false);
		if(reload){
			long commentId = ServletRequestUtils.getLongParameter(request, "commentId",-1);
			long objectId = ServletRequestUtils.getLongParameter(request, "object_id",-1);
			String objectType = "";
			try {
				objectType = ServletRequestUtils.getStringParameter(request, "object_type");
			} catch (ServletRequestBindingException e) {
				e.printStackTrace();
			}
			Comment comment = commentService.select(commentId);
			if(comment==null){
				logger.error("commentId为"+commentId+"相关评论已删除");
			}
			else{
				commentCriteria.setObjectId(objectId);
				commentCriteria.setObjectType(objectType);
				commentCriteria.setOwnerId(comment.getOwnerId());
				logger.debug("rows:"+rows+"#page:"+page+"#reload"+reload+"#commentId:"+commentId+"#objectId:"+objectId+"#ownerId:"+comment.getOwnerId());
			}
		}
		int totalRows = commentService.count(commentCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		commentCriteria.setPaging(paging);
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));
		List<Comment> commentList = commentService.listOnPage(commentCriteria);
		map.put("commentList", commentList);
		return view;
	}
	/*private void afterFetch(Comment comment) {
		if(comment.getObjectType().equals(ObjectType.product.getCode())){
			Product product = productService.select(comment.getObjectId());
			if(product == null){
				logger.error("找不到评论[" + comment.getCommentId() + "]对应的产品:" + comment.getObjectId());
				;
			} else {

				comment.setExtraValue(DataName.productName.toString(), product.getProductName());
				String image =  product.getExtraValue(DataName.productSmallImage.toString());
				if(image == null){
					logger.warn("找不到评论[" + comment.getCommentId() + "]对应的产品[" + comment.getObjectId() + "]的产品小图数据");

				} else{
					comment.setExtraValue(DataName.productSmallImage.toString(), image);
					logger.debug("把产品[" + comment.getObjectId() + "]对应小图[" + image + "]放入评论["  + comment.getCommentId() + "]数据");
				}


			}


		}

	}*/

	/**
	 * 提交一个评论
	 */
	@RequestMapping(value = "/submit", method=RequestMethod.POST)
	public String submit(HttpServletRequest request, HttpServletResponse response, ModelMap map, Comment comment) {
		final String view = "comment/submit";

		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		comment.setOwnerId(frontUser.getOwnerId());
		comment.setUuid(frontUser.getUuid());
		if(StringUtils.isBlank(comment.getObjectType())){
			logger.error("评论对象不允许为空");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，评论对象不允许为空"));
			return CommonStandard.frontMessageView;
		}
		
		if(comment.getObjectId() < 1){
			logger.error("评论对象ID不允许为0");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，请指定一个要评论的对象"));
			return CommonStandard.frontMessageView;
		}
		if(StringUtils.isBlank(comment.getContent())){
			logger.error("评论内容不允许为空");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，请提交您的评论内容"));
			return CommonStandard.frontMessageView;
		}

//		CommentConfig 是针对某种对象或某一个对象的评价配置
		CommentConfig commentConfig = commentService.getCommentConfig(comment);
		if(commentConfig == null){
			logger.warn("找不到针对对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论配置，将不允许发布评论");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "对不起，暂时不允许发表评论"));
			return CommonStandard.frontMessageView;
		}
		comment.setCommentConfigId(commentConfig.getCommentConfigId());
		comment.setCreateTime(new Date());

		if(commentConfig.getExtraDataDefine() == null || commentConfig.getExtraDataDefine().size() < 1){
			logger.info("commentConfig[" + commentConfig.getCommentConfigId() + "]未定义扩展数据配置");
		} else {
			if (request instanceof MultipartHttpServletRequest) {
				logger.info("请求中带有附件，使用文件上传处理.");
				try {
					this.fileUpload(request, "create", comment, commentConfig);
				} catch (IOException e) {
					map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "对不起,上传失败"));
					return CommonStandard.frontMessageView;
				}
			}
			for (String key : commentConfig.getExtraDataDefine().keySet()) {
				logger.debug("尝试获取扩展数据:" + key);
				String valueDefine = commentConfig.getExtraDataDefine().get(key);
				if(valueDefine != null && !valueDefine.equalsIgnoreCase("string")){
					logger.debug("扩展数据的定义不应当直接输入:" + valueDefine);
					continue;
				}
				String documentDataStr = null;
				documentDataStr = ServletRequestUtils.getStringParameter(request, key, null);

				if (StringUtils.isBlank(documentDataStr)) {
					logger.debug("数据规范[" + key+ "]没有提交数据");
					continue;
				}
				logger.debug("数据规范[" + key + "]提交的数据是[" + documentDataStr + "]");


			
				logger.debug("尝试插入自定义评论数据[" + key + "]，数据内容:[" 	+ documentDataStr + "]");
				comment.setExtraValue(key, documentDataStr);
			}
		}
		//放入用户昵称和头像到评论数据
		comment.setExtraValue(DataName.userRealName.toString(), frontUser.getNickName() == null ? frontUser.getUsername() : frontUser.getNickName());
		comment.setExtraValue(DataName.userHeadPic.toString(), frontUser.getExtraValue(DataName.userHeadPic.toString()));
		if("product".equals(comment.getObjectType())){
			Document document = new Document();
			if(comment.getObjectId()<1){
				map.put("message", new EisMessage(OperateResult.failed.getId(), "评论对象编码不正确"));
				return view;
			}
			document = documentService.select((int)comment.getObjectId());
			Product productByDocument = productService.getProductByDocument(document);
			if(productByDocument==null){
				map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，找不到文档对应的产品无法评论"));
				return view;
			}
			comment.setObjectId(productByDocument.getProductId());
		}
		//提交对评论的回复
		String replyTo="";
		try {
			replyTo = ServletRequestUtils.getStringParameter(request, "replyTo");
		} catch (ServletRequestBindingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.debug("回复评论人#"+replyTo);
		comment.setExtraValue("replyTo", replyTo);
		String relatedCommentId="";
		try {
			relatedCommentId = ServletRequestUtils.getStringParameter(request, "relatedCommentId");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		logger.debug("回复评论ID为#"+relatedCommentId+"#的评论");
//		comment.setRelatedCommentId(Long.parseLong(obj.toString()));

		EisMessage rs = commentService.insert(comment);
		logger.debug("用户[" + frontUser.getUuid() + "]针对对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论插入结果:" + rs);
		if (rs.getOperateCode() == OperateResult.success.id) {
			map.put("message",  new EisMessage(OperateResult.success.getId(),"您的评论已提交"));
		} else {
			if(rs.getOperateCode() == EisError.dataDuplicate.id){
				map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，您已经提交了评论，不能再次评论"));
			} else if(rs.getOperateCode() == EisError.subscribeCountError.id){
				map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，您必须已经购买该产品才能评论"));
			} else  if(rs.getOperateCode() == EisError.haveDirtyWord.id){
				map.put("message", rs);
			} else {
				map.put("message", rs);
			}
		}
		return view;
	}

	/**
	 * 查看一个评论
	 */
	@RequestMapping(value = "/detail/{commentId}", method=RequestMethod.POST)
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("commentId") long commentId) {
		final String view = "comment/detail";

		return view;
	}

	/**
	 * 删除一个评论
	 */
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		final String view = "comment/delete";

		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		long commentId = ServletRequestUtils.getLongParameter(request, "commentId", 0);
		if(commentId == 0){
			logger.info("删除评论请求未提交commentId");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "请提交要删除的评论"));
			return CommonStandard.frontMessageView;
		}

		Comment comment = commentService.select(commentId);
		if(comment == null){
			logger.info("找不到要删除的评论[" + commentId + "]");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要删除的评论"));
			return CommonStandard.frontMessageView;
		}
		if(comment.getUuid() != frontUser.getUuid()){
			logger.info("要删除的评论[" + commentId + "],其uuid[" + comment.getUuid() + "]与当前用户[" + frontUser.getUuid() + "]不一致");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要删除的评论"));
			return CommonStandard.frontMessageView;
		}
		if(comment.getOwnerId() != frontUser.getOwnerId()){
			logger.info("要删除的评论[" + commentId + "],ownerId[" + comment.getOwnerId() + "]与当前用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到要删除的评论"));
			return CommonStandard.frontMessageView;
		}
		String canDelete = comment.getExtraValue("canDelete");
		if(canDelete == null || canDelete.equalsIgnoreCase("false")){
			logger.info("要删除的评论[" + commentId + "]被配置为"+canDelete+"不可删除");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "评论不允许删除"));
			return CommonStandard.frontMessageView;
		}



		int rs = commentService.delete(commentId);
		logger.debug("用户[" + frontUser.getUuid() + "]删除评论[" + comment + "]，结果:" + rs);
		if (rs == 1) {
			map.put("message",  new EisMessage(OperateResult.success.getId(),"您的评论已删除"));
		} else {			
			map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，评论删除失败"));
		}
		return view;
	}

	// 仅限录音文件上传
	@SuppressWarnings("rawtypes")
	private int fileUpload(HttpServletRequest request, String mode, Comment comment, CommentConfig commentConfig) throws IOException  {

		// 从Spring容器中获取对应的上传文件目录
		// String fileUploadSavePath =
		// ((FileSystemResource)this.getApplicationContext().getBean("uploadSaveDir")).getPath();
		// logger.info("Spring容器中的上传文件目录在:" + fileUploadSavePath);
		// logger.info("尝试为文档[udid="+udid+"]在[" + mode + "]模式下上传附件");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Iterator its = multiRequest.getFileNames();
		int i = 0;
		int addCount = 0;
		int updateCount = 0;
		int ignoreCount = 0;
		while (its.hasNext()) {
			CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile((String) its.next());
			if (file.getSize() == 0) {
				if (i == 0) {
					continue;
				}
				break;
			}
			String key = file.getName();
			
			
			if(commentConfig.getExtraDataDefine() == null || commentConfig.getExtraDataDefine().get(key) == null){// 不支持的上传文件名
				logger.debug("不支持的上传文件:" + file.getName());
				ignoreCount++;
				break;
			}

			/*
			 * 如果当前为编辑模式,则查找是否有对应的已存在数据 如果有已存在的数据并且本次要上传,则直接更新对应的文件
			 */
			String fileUploadSavePath = null;

			fileUploadSavePath = documentUploadSaveDir;
			logger.debug("评论上传文件的原始文件名："+file.getOriginalFilename());
			String fileName =  CommonStandard.EXTRA_DATA_OPEN_PATH + File.separator + UUIDFilenameGenerator.generateWithDatePath(file.getOriginalFilename()+".wav");
			
			String fileDest = "";
			String existDocumentData = null;
			if (mode.equals("update") && comment.getData() != null) {
				for (String d2 : comment.getData().keySet()) {
					if (d2.equals(fileName)) {
						existDocumentData = d2;
						break;
					}
				}
				if (existDocumentData != null) {
					fileDest = fileUploadSavePath + File.separator + existDocumentData;
					File _oldFile = new File(fileDest);
					_oldFile.delete();
					updateCount++;
				} else {
					fileDest = fileUploadSavePath + File.separator + fileName;
				}

			} else {
				fileDest = fileUploadSavePath + File.separator + fileName;

			}
			logger.info("documentUploadSaveDir:" + documentUploadSaveDir + ",fileUploadSavePath:" + fileUploadSavePath + ",fileName:" + fileName);

			/*InputStream inputStream = file.getInputStream();
			try {
				   OutputStream os = new FileOutputStream(fileDest);
				   int bytesRead = 0;
				   byte[] buffer = new byte[8192];
				   while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				    os.write(buffer, 0, bytesRead);
				   }
				   os.close();
				   inputStream.close();
				  } catch (Exception e) {
				   e.printStackTrace();
			}*/
			File destDir = new File(fileDest).getParentFile();
			if(!destDir.exists()){
				logger.info("目标目录不存在，创建:" + fileUploadSavePath);
				FileUtils.forceMkdir(destDir);
			}
			logger.info("保存数据文件[" + file.getOriginalFilename() + "]到:" + fileDest);
			File dest = new File(fileDest);
			try {
				file.transferTo(dest);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				//continue;
			}
			/*if (mode.equals("edit") && existDocumentData != null) {
					// 已经更新了已存在附件的文件,无需其他操作
					continue;
				}*/

			comment.setExtraValue(key, fileName);
			addCount++;

		}
		int totalAffected = addCount + updateCount;
		String message = "完成附件上传,新增 " + addCount + " 个,更新 " + updateCount + " 个, 跳过 " + ignoreCount + " 个。";
		logger.info(message);
		return totalAffected;
	}

}
