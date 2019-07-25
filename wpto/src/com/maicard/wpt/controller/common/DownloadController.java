package com.maicard.wpt.controller.common;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.product.service.StockService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;


/**
 * 判断用户权限是否可以访问某个文件
 *
 *
 * @author NetSnake
 * @date 2015年11月16日
 *
 */
@Controller
@RequestMapping("/file")
public class DownloadController extends BaseController{


	@Resource
	private CertifyService certifyService;
	
	@Resource
	private DocumentService documentService;
	
	@Resource
	private NodeService nodeService;

	@Resource
	private UserRelationService userRelationService;
	
	@Resource
	private StockService stockService;

	private static final String REAL_DOWNLOAD_PREFIX = "/upload";

	@ResponseBody
	@IgnoreLoginCheck
	@RequestMapping
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			logger.debug("文件权限检查时没有frontUser");
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		
		String fileName = ServletRequestUtils.getStringParameter(request, "file");
		if(StringUtils.isBlank(fileName)) {
			logger.debug("文件权限检查时没有提交file参数");
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		
		logger.debug("检查用户:{}对文件:{}的访问权限", frontUser.getUuid(), fileName);
		String[] data = fileName.split("/");
		if(data.length < 3) {
			logger.debug("检查用户:{}对文件:{}的访问权限,但是文件长度错误", frontUser.getUuid(), fileName);
		}
		String reposidId = data[1];
		DocumentCriteria documentCriteria = new DocumentCriteria(frontUser.getOwnerId());
		documentCriteria.setMimeType(reposidId);
		List<Document> documentList = documentService.list(documentCriteria);
		if(documentList.size() < 1) {
			logger.debug("没有找到用户:{}要访问文件:{}的对应文档:reposidId={}", frontUser.getUuid(), fileName, reposidId);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		Document document = documentList.get(0);
		int result = stockService.checkPrivilege(document, frontUser);
		if(result == OperateResult.success.id) {
			String realFileName = REAL_DOWNLOAD_PREFIX + "/" + fileName;
			response.setHeader("X-Accel-Redirect", realFileName);
			return null;
		}
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return null;		
	}





}
