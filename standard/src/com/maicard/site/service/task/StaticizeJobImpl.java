package com.maicard.site.service.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.FileSign;
import com.maicard.common.util.HttpUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.PayTypeService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.dao.ProductDao;
import com.maicard.product.domain.Product;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.dao.NodeDao;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Staticize;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.StaticizeJob;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.ServiceStatus;
import com.maicard.standard.SiteStandard;
import com.maicard.standard.CommonStandard.DataFetchMode;
import com.maicard.standard.DataName;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.SiteStandard.SitePath;
import com.maicard.standard.SiteStandard.VirtualDocument;


@Service
@Deprecated		//已放弃使用
public class StaticizeJobImpl extends BaseService implements StaticizeJob {

	@Resource
	private ConfigService configService;
	@Resource
	private DocumentService documentService;
	@Resource
	private NodeDao nodeDao;
	@Resource
	private PayTypeService payTypeService;
	@Resource
	private ProductDao productDao;



	private String globalSiteStaticPathPrefix;
	private String nodeStaticPath;
	private String productStaticPath;

	private String siteDynamicUrl;
	private String productDynamicUrl;
	private String nodeDynamicUrl;
	private String iframeDynamicUrl;

	private final String dirIndexFileName = "index" + CommonStandard.DEFAULT_PAGE_SUFFIX;

	private boolean running = false;

	@PostConstruct
	public void init(){
		globalSiteStaticPathPrefix = configService.getValue(DataName.globalSiteStaticPathPrefix.toString(),0);
		if(globalSiteStaticPathPrefix == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[globalSiteStaticPathPrefix]");
		}

		nodeStaticPath = configService.getValue("nodeStaticPath",0);
		if(nodeStaticPath == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[nodeStaticPath]");
		}
		productStaticPath = configService.getValue("productStaticPath",0);
		if(productStaticPath == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[productStaticPath]");
		}

		productDynamicUrl = configService.getValue("productDynamicUrl",0);
		if(productDynamicUrl == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[productDynamicUrl]");
		}
		nodeDynamicUrl = configService.getValue("nodeDynamicUrl",0);
		if(nodeDynamicUrl == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[nodeDynamicUrl]");
		}
		iframeDynamicUrl = configService.getValue("iframeDynamicUrl",0);
		if(iframeDynamicUrl == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[iframeDynamicUrl]");
		}

	}


	//静态化所有节点和文章
	public String staticizeAllNode() throws Exception{
		logger.debug("静态化所有常规节点和文章");
		StringBuffer report = new StringBuffer();
		if(nodeStaticPath == null || nodeStaticPath.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}
		File destDir = new File(nodeStaticPath);
		if(!destDir.exists()){
			report.append("指定路径不存在，创建目录[" + nodeStaticPath);
			destDir.mkdirs();
		}

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Node> nodeList = nodeDao.list(nodeCriteria);
		report.append("获取到" + (nodeList == null ? -1 : nodeList.size()) + "个节点");
		if(nodeList == null || nodeList.size() < 1){
			logger.debug("节点列表为空，无需进行节点和文章静态化");
			report.append("节点列表为空，无需进行节点和文章静态化");	
			return report.toString();
		}
		for(Node node : nodeList){
			if(node.getPath().equals("")){
				//不静态化首页
				//indexStaticize();
				continue;
			}
			boolean ignore = false;
			if(CommonStandard.ignoreStaticizeNode != null){
				for(String ignoreNode : CommonStandard.ignoreStaticizeNode){
					if(node.getPath().equals(ignoreNode)){
						ignore = true;
						break;
					}
				}
			}
			if(!ignore){
				report.append(staticizeNode(node));
			}
		}
		report.append(cleanAllNodeDir());
		return report.toString();

	}

	//静态化所有产品
	public String staticizeAllProduct() throws Exception{
		StringBuffer report = new StringBuffer();
		if(productStaticPath == null || productStaticPath.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}
		File destDir = new File(productStaticPath);
		if(!destDir.exists()){
			logger.debug("指定路径不存在，创建目录[" + productStaticPath);
			destDir.mkdirs();
		}

		ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setCurrentStatus(BasicStatus.normal.getId());
		productCriteria.setSupplierId(0);
		productCriteria.setProductLevel(1);
		productCriteria.setDataFetchMode(DataFetchMode.simple.toString());
		List<Product> productList = productDao.list(productCriteria);
		logger.debug("获取到内部一级产品:" + (productList == null ? -1 : productList.size()) + "个");
		if(productList == null || productList.size() < 1){
			logger.debug("内部一级产品列表为空，无需进行产品静态化");
			report.append("内部一级产品列表为空，无需进行产品静态化");	
			return report.toString();
		}



		for(Product product : productList){
			if(product == null){
				report.append("产品为空，无法静态化");
				continue;
			}
			staticizeProduct(product);
		}
		report.append(removeInvalidProduct());
		return report.toString();
	}

	//静态化支付节点
	public String payStaticize() throws Exception{
		StringBuffer report = new StringBuffer();
		if(nodeStaticPath == null || nodeStaticPath.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}
		File destDir = new File(nodeStaticPath);
		if(!destDir.exists()){
			report.append("指定路径不存在，创建目录[" + nodeStaticPath);
			destDir.mkdirs();
		}

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setPath("pay");
		List<Node> nodeList = nodeDao.list(nodeCriteria);
		if(nodeList == null || nodeList.size() != 1){
			logger.error("找不到pay节点");
			report.append("找不到pay节点");	
			return report.toString();
		}
		Node payNode = nodeList.get(0);
		if(payNode == null){

		}

		logger.debug("尝试请求:" + nodeDynamicUrl + "/" + dirIndexFileName);
		String content = HttpUtils.sendData(nodeDynamicUrl + "/" + payNode.getPath() + "/" + dirIndexFileName);
		String fileName = null;
		if(SitePath.createIndexFile.getName().equals("true")){//使用目录+index的形式
			File nodeDir = new File(nodeStaticPath + File.separator + payNode.getPath());
			if(!nodeDir.exists()){
				logger.debug("指定路径不存在，创建目录[" + nodeStaticPath + File.separator + payNode.getPath());
				report.append("指定路径不存在，创建目录[" + nodeStaticPath + File.separator + payNode.getPath());
				nodeDir.mkdirs();
			}
			fileName = nodeStaticPath + File.separator + payNode.getPath() + File.separator + dirIndexFileName;

		} else {
			fileName = nodeStaticPath + File.separator + payNode.getPath() + CommonStandard.DEFAULT_PAGE_SUFFIX;
		}
		logger.debug("尝试创建节点[" + payNode.getNodeId() + "]的index文件:" + fileName);
		report.append("尝试创建节点[" + payNode.getNodeId() + "]的index文件:" + fileName);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
		bw.write(content);
		bw.close();

		//查找所有正常状态的支付类型
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria();
		payTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		if(payTypeList != null && payTypeList.size() > 0){
			File documentDir = new File(nodeStaticPath + File.separator + payNode.getPath() + File.separator + SitePath.documentPrefix.getName());
			if(!documentDir.exists()){
				logger.debug("指定路径不存在，创建目录[" + nodeStaticPath + File.separator + payNode.getPath() + File.separator + SitePath.documentPrefix.getName());
				report.append("指定路径不存在，创建目录[" + nodeStaticPath + File.separator + payNode.getPath() + File.separator + SitePath.documentPrefix.getName());
				documentDir.mkdirs();
			}
			int added = 0;
			int deleted = 0;
			int ignored = 0;

			File[] _oldFileList = documentDir.listFiles();

			for(PayType payType : payTypeList){
				if(payType.getCurrentStatus() == BasicStatus.normal.getId()){
					String pageUrl = nodeDynamicUrl + "/" + payNode.getPath() + "/" + payType.getPayTypeId() + CommonStandard.DEFAULT_PAGE_SUFFIX;
					logger.info("尝试请求:" + pageUrl);
					content = HttpUtils.sendData(pageUrl);
					fileName = nodeStaticPath + File.separator + payNode.getPath() + File.separator + SitePath.documentPrefix.getName()  + payType.getPayTypeId() + CommonStandard.DEFAULT_PAGE_SUFFIX;

					logger.debug("尝试创建document[" + payType.getName() + "/" + payType.getPayTypeId() + "]的静态文件:" + fileName);
					report.append("尝试创建document[" + payType.getName() + "/" + payType.getPayTypeId() + "]的静态文件:" + fileName);
					bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
					bw.write(content);
					bw.close();
					added++;
				}

			}//XXX
			//删除已不存在的页面
			for(File oldFile : _oldFileList){
				logger.info("比对已存在的文件:" + oldFile.getName());
				boolean delete = true;
				for(PayType payType : payTypeList){
					if(oldFile.getName().equals(payType.getPayTypeId() + CommonStandard.DEFAULT_PAGE_SUFFIX)){
						delete = false;
						break;
					}
					if(SitePath.createIndexFile.getName().equals("true") && oldFile.getName().equals(dirIndexFileName)){
						delete = false;
						break;
					}
				}
				if(delete){
					logger.debug("删除不存在的页面:" + oldFile.getName());
					report.append("删除不存在的页面:" + oldFile.getName() + "\n");
					oldFile.delete();
					deleted++;
				}
			}
			logger.debug("完成节点[" + payNode.getPath() + "[" + payNode.getNodeId() + "]下文章的静态化，共更新/增加了[" + added + "]个文章HTML，有[" + ignored + "]个不需要更新，删除了[" + deleted + "]个文章HTML");
			report.append("完成节点[" + payNode.getPath() + "[" + payNode.getNodeId() + "]下文章的静态化，共更新/增加了[" + added + "]个文章HTML，有[" + ignored + "]个不需要更新，删除了[" + deleted + "]个文章HTML\n");
		}

		//比对不存在的节点目录或文件
		File[] _oldFileList = destDir.listFiles();
		if(SitePath.createIndexFile.getName().equals("true")){//使用目录+index的形式
			for(File oldFile : _oldFileList){
				logger.debug("比对已存在的文件夹:" + oldFile.getName());
				boolean delete = true;
				for(PayType payType : payTypeList){
					if(oldFile.getName().equals(payType.getPayTypeId())){
						delete = false;
					}
				}
				if(delete){
					oldFile.delete();
				}
			}		
		} else { // 使用节点.shtml形式
			for(File oldFile : _oldFileList){
				logger.info("比对已存在的文件:" + oldFile.getName());
				boolean delete = true;
				for(PayType payType : payTypeList){
					if(oldFile.getName().equals(payType.getPayTypeId())){
						delete = false;
					}
				}
				if(delete){
					oldFile.delete();
				}
			}	
		}

		return report.toString();

	}


	//静态化某个栏目
	public void indexStaticize(Node node) throws Exception{
		logger.debug("执行站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的首页静态化");
		if(globalSiteStaticPathPrefix == null || globalSiteStaticPathPrefix.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}
		String path = globalSiteStaticPathPrefix.replaceAll("/$", "") + File.pathSeparator + node.getSiteCode();
		File destDir = new File(path);
		if(!destDir.exists()){
			logger.debug("指定路径不存在，创建目录:" + globalSiteStaticPathPrefix);
			FileUtils.forceMkdir(destDir);
		}

		siteDynamicUrl = configService.getValue(DataName.siteDynamicUrl.toString(), node.getOwnerId());
		if(siteDynamicUrl == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[siteDynamicUrl]");
		}

		String url = null;
		if(node.getPath() == null){
			throw new RequiredObjectIsNullException("节点[" + node.getNodeId() + "]路径为空，无法静态化");
		}


		if(node.getPath().equals(".") || node.getPath().equals("")){
			//首页
			url = siteDynamicUrl;
		} else {
			url = siteDynamicUrl + "/" + node.getPath() + dirIndexFileName;
		}

		logger.debug("尝试请求站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的栏目[" + node.getPath() + "]页面 :" + url);
		String content = HttpUtils.sendData(url);
		String fileName = globalSiteStaticPathPrefix + File.separator + dirIndexFileName;
		String fileSign = FileSign.getFileSign(content.getBytes());
		long fileSize = content.length();
		logger.debug("尝试创建站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的栏目[" + node.getPath() + "]的静态文件:" + fileName + ",文件大小:" + fileSize + ",文件签名:" + fileSign);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), CommonStandard.DEFAULT_ENCODING));
		Staticize staticize = new Staticize(node.getOwnerId());
		staticize.setObjectType(ObjectType.node.name());
		staticize.setObjectId(node.getNodeId());
		staticize.setFileName(fileName);
		staticize.setFileSign(content);
		staticize.setFileSize(fileSize);
		bw.write(content);
		bw.close();

	}



	@Override
	@Async
	public EisMessage start() {
		logger.error("静态化任务被调用");
		if(running){
			logger.debug("任务正在执行中");
			return new EisMessage(OperateResult.success.getId(), "任务正在执行中");	
		}
		start(ObjectType.node.name(),null);
		start(ObjectType.product.name(),null);	
		return new EisMessage(OperateResult.success.getId(), "服务已完成");

	}

	@Override
	public EisMessage stop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage status() {
		if(running){
			return new EisMessage(ServiceStatus.opening.getId(),"服务正在运行");
		}
		return new EisMessage(ServiceStatus.closed.getId(),"服务未运行");
	}


	@Override
	@Async
	public EisMessage start(String objectType, int... objectIds) {
		running = true;
		long start = new Date().getTime();
		if(objectType == null){
			logger.debug("未指定静态化对象");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(), "未指定静态化对象");
		}
		if(objectType.equalsIgnoreCase(ObjectType.node.toString())){
			if(objectIds == null || objectIds.length == 0){
				logger.debug("指定静态化所有节点");
				try {
					staticizeAllNode();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				logger.debug("指定静态化[" + objectIds.length + "]个节点");
				for(int nodeId : objectIds){
					Node node = nodeDao.select(nodeId);
					if(node != null){
						try {
							logger.debug(staticizeNode(node));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			cleanAllNodeDir();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(objectType.equalsIgnoreCase(ObjectType.product.toString())){
			if(objectIds == null || objectIds.length == 0 || objectIds[0] == 0){
				logger.debug("指定静态化所有产品");
				try {
					staticizeAllProduct();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				logger.debug("指定静态化[" + objectIds.length + "]个产品");
				for(int id : objectIds){
					Product product = productDao.select(id);
					if(product != null){
						try {
							staticizeProduct(product);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			removeInvalidProduct();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("静态化节点任务完成，耗时[" + ((new Date().getTime() - start) / 1000) + "]秒");
		running = false;
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	private String staticizeNode(Node node) throws Exception{
		if(node != null && node.getPath().equals("")){
			logger.debug("指定的静态化节点是首页节点");
			//	indexStaticize();
			return null;
		}
		if(node != null && node.getPath().equals("pay")){
			logger.debug("指定的静态化节点是支付节点");
			payStaticize();
			return null;
		}
		StringBuffer report = new StringBuffer();

		if(node == null){
			report.append("节点为空，无法静态化");
			report.append(cleanAllNodeDir());
			return report.toString();
		}


		logger.debug("尝试请求:" + nodeDynamicUrl + "/" + node.getPath() + "/" + dirIndexFileName);
		String content = 		HttpUtils.sendData(nodeDynamicUrl + "/" + node.getPath() + "/" + dirIndexFileName);
		String fileName = null;
		if(SitePath.createIndexFile.getName().equals("true")){//使用目录+index的形式
			File nodeDir = new File(nodeStaticPath + File.separator + node.getPath());
			if(!nodeDir.exists()){
				report.append("指定路径不存在，创建目录[" + nodeStaticPath + File.separator + node.getPath());
				nodeDir.mkdirs();
			}
			fileName = nodeStaticPath + File.separator + node.getPath() + File.separator + dirIndexFileName;

		} else {
			fileName = nodeStaticPath + File.separator + node.getPath() + CommonStandard.DEFAULT_PAGE_SUFFIX;
		}
		report.append("尝试创建节点[" + node.getNodeId() + "]的index文件:" + fileName);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
		bw.write(content);
		bw.close();

		//查找当前节点关联的所有文章
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(SiteStandard.DocumentStatus.published.getId());
		documentCriteria.setNodePath(node.getPath());
		List<Document> documentList = documentService.list(documentCriteria);
		if(documentList != null && documentList.size() > 0){

			File documentDir = new File(nodeStaticPath + File.separator + node.getPath() + File.separator + SitePath.documentPrefix.getName());
			if(!documentDir.exists()){
				report.append("指定路径不存在，创建目录[" + nodeStaticPath + File.separator + node.getPath() + File.separator + SitePath.documentPrefix.getName());
				documentDir.mkdirs();
			}
			int added = 0;
			int deleted = 0;
			int ignored = 0;
			List<String> validDocumentNames = new ArrayList<String>();
			for(VirtualDocument v : VirtualDocument.values()){
				validDocumentNames.add(v.toString());
			}
			if(SitePath.createIndexFile.getName().equals("true")){
				validDocumentNames.add("index");
			}

			for(Document document : documentList){
				if(document.getCurrentStatus() == DocumentStatus.published.getId()){
					String pageUrl = nodeDynamicUrl + "/" + node.getPath() + "/" + document.getDocumentCode() + CommonStandard.DEFAULT_PAGE_SUFFIX;
					report.append("尝试请求:" + pageUrl + "\n");
					content = HttpUtils.sendData(pageUrl);
					if(content == null || content.length() < 1){
						report.append("请求的文件[" + pageUrl + "]返回为空，跳过\n");
						continue;
					}
					validDocumentNames.add(document.getDocumentCode());
					fileName = nodeStaticPath + File.separator + node.getPath() + File.separator + SitePath.documentPrefix.getName()  + document.getDocumentCode() + CommonStandard.DEFAULT_PAGE_SUFFIX;

					report.append("尝试创建document[" + document.getDocumentCode() + "]的静态文件:" + fileName + "\n");
					bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
					bw.write(content);
					bw.close();
					added++;
				}

			}

			deleted = cleanFilesInDir(documentDir.getAbsolutePath(), validDocumentNames);

			report.append("完成节点[" + node.getPath() + "[" + node.getNodeId() + "]下文章的静态化，共更新/增加了[" + added + "]个文章HTML，有[" + ignored + "]个不需要更新，删除了[" + deleted + "]个文章HTML\n");

		}
		for(VirtualDocument vd : VirtualDocument.values()){
			String pageUrl = nodeDynamicUrl + "/" + node.getPath() + "/" + vd.toString() + CommonStandard.DEFAULT_PAGE_SUFFIX;
			report.append("尝试请求:" + pageUrl + "\n");
			content = HttpUtils.sendData(pageUrl);
			fileName = nodeStaticPath + File.separator + node.getPath() + File.separator + SitePath.documentPrefix.getName()  + vd.toString() + CommonStandard.DEFAULT_PAGE_SUFFIX;

			report.append("尝试创建特殊文档document[" + vd.toString() + "]的静态文件:" + fileName + "\n");
			bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
			bw.write(content);
			bw.close();

		}

		return report.toString();
	}

	private String staticizeProduct(Product product) throws Exception {
		StringBuffer report = new StringBuffer();

		File destDir = new File(productStaticPath);
		if(!destDir.exists()){
			logger.debug("指定路径不存在，创建目录[" + productStaticPath);
			destDir.mkdirs();
		}
		String content  = null; 
		String fileName = null;

		if(SitePath.createIndexFile.getName().equals("true")){//使用目录+index的形式
			logger.debug("尝试请求:" + productDynamicUrl + "/" + product.getProductCode() + "/" + dirIndexFileName);
			content =  HttpUtils.sendData( productDynamicUrl + "/" + product.getProductCode() + "/" + dirIndexFileName);
			File productDir = new File(productStaticPath + File.separator + product.getProductCode());
			if(!productDir.exists()){
				logger.debug("指定路径不存在，创建目录[" + productStaticPath + File.separator + product.getProductCode());
				report.append("指定路径不存在，创建目录[" + productStaticPath + File.separator + product.getProductCode());
				productDir.mkdirs();
			}
			fileName = productStaticPath + File.separator + product.getProductCode() + File.separator + dirIndexFileName;

		} else {
			logger.debug("尝试请求:" + productDynamicUrl + "/" + product.getProductCode());
			content = HttpUtils.sendData(productDynamicUrl + "/" + product.getProductCode());
			fileName = productStaticPath + File.separator + product.getProductCode() + CommonStandard.DEFAULT_PAGE_SUFFIX;
		}
		logger.debug("尝试创建节点[" + product.getProductId() + "]的index文件:" + fileName);
		report.append("尝试创建节点[" + product.getProductId() + "]的index文件:" + fileName);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
		bw.write(content);
		bw.close();

		//查找当前产品下的子产品
		ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setCurrentStatus(BasicStatus.normal.getId());
		productCriteria.setSupplierId(0);
		productCriteria.setProductLevel(2);
		productCriteria.setParentProductId(product.getProductId());
		productCriteria.setDataFetchMode(DataFetchMode.simple.toString());
		List<Product> subProductList = productDao.list(productCriteria);
		if(subProductList != null && subProductList.size() > 0){

			File subProductDir = new File(productStaticPath + File.separator + product.getProductCode() + File.separator + SitePath.documentPrefix.getName());
			if(!subProductDir.exists()){
				logger.debug("指定路径不存在，创建目录[" + productStaticPath + File.separator + product.getProductCode() + File.separator + SitePath.documentPrefix.getName());
				report.append("指定路径不存在，创建目录[" + productStaticPath + File.separator + product.getProductCode() + File.separator + SitePath.documentPrefix.getName());
				subProductDir.mkdirs();
			}
			int added = 0;
			int deleted = 0;
			int ignored = 0;
			//比对已存在文件与文档的时间
			File[] _oldFileList = subProductDir.listFiles();
			/*for(File oldFile : _oldFileList){
				for(Product subProduct : subProductList){
					//logger.debug("file time:" + oldFile.lastModified() + ", product time:" + product.getLastModified().getTime());
					if(oldFile.getName().equals(subProduct.getProductCode() + Constants.CommonStandard.DEFAULT_PAGE_SUFFIX)){
						if(oldFile.lastModified() >= subProduct.getLastModified().getTime()){
							//不需要生成新的文件
							subProduct.setCurrentStatus(Constants.BasicStatus.disable.getId());
							ignored++;
							break;
						}
					}
				}

			}*/
			for(Product subProduct : subProductList){
				if(subProduct.getCurrentStatus() == BasicStatus.normal.getId() && subProduct.getExtraStatus() == ServiceStatus.opening.getId()){
					String pageUrl = productDynamicUrl + "/" + product.getProductCode() + "/" + subProduct.getProductCode() + CommonStandard.DEFAULT_PAGE_SUFFIX;
					logger.info("尝试请求:" + pageUrl);
					content = HttpUtils.sendData(pageUrl);
					fileName = productStaticPath + File.separator + product.getProductCode() + File.separator + SitePath.documentPrefix.getName()  + subProduct.getProductCode() + CommonStandard.DEFAULT_PAGE_SUFFIX;

					logger.debug("尝试创建document[" + subProduct.getProductCode() + "]的静态文件:" + fileName);
					report.append("尝试创建document[" + subProduct.getProductCode() + "]的静态文件:" + fileName);
					bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
					bw.write(content);
					bw.close();
					added++;
				}

			}
			//删除已不存在的页面
			for(File oldFile : _oldFileList){
				logger.info("比对已存在的文件:" + oldFile.getName());
				boolean delete = true;
				for(Product subProduct : subProductList){
					if(oldFile.getName().equals(subProduct.getProductCode() + CommonStandard.DEFAULT_PAGE_SUFFIX)){
						delete = false;
						break;
					}
					if(SitePath.createIndexFile.getName().equals("true") && oldFile.getName().equals(dirIndexFileName)){
						delete = false;
						break;
					}
				}
				if(delete){
					logger.debug("删除不存在的页面:" + oldFile.getName());
					report.append("删除不存在的页面:" + oldFile.getName() + "\n");
					oldFile.delete();
					deleted++;
				}
			}
			logger.debug("完成节点[" + product.getProductCode() + "[" + product.getProductId() + "]下文章的静态化，共更新/增加了[" + added + "]个文章HTML，有[" + ignored + "]个不需要更新，删除了[" + deleted + "]个文章HTML");
			report.append("完成节点[" + product.getProductCode() + "[" + product.getProductId() + "]下文章的静态化，共更新/增加了[" + added + "]个文章HTML，有[" + ignored + "]个不需要更新，删除了[" + deleted + "]个文章HTML\n");
		}
		return report.toString();
	}


	//清理所有节点的目录，尝试删除那些已经不存在的节点目录
	private String cleanAllNodeDir() throws Exception{
		StringBuffer report = new StringBuffer();
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Node> nodeList = nodeDao.list(nodeCriteria);
		report.append("获取到" + (nodeList == null ? -1 : nodeList.size()) + "个节点");
		if(nodeList == null || nodeList.size() < 1){
			report.append("节点列表为空，无需进行节点和文章静态化");	
			return report.toString();
		}
		//比对不存在的节点目录或文件
		File destDir = new File(nodeStaticPath);
		if(!destDir.exists()){
			report.append("指定静态化路径不存在[" + nodeStaticPath + "]\n");
			return report.toString();
		}
		File[] _oldFileList = destDir.listFiles();
		if(SitePath.createIndexFile.getName().equals("true")){//使用目录+index的形式
			for(File oldFile : _oldFileList){
				logger.debug("比对已存在的文件夹:" + oldFile.getName());
				boolean delete = true;
				for(Node node : nodeList){
					if(oldFile.getName().equals(node.getPath()) && oldFile.isDirectory()){
						delete = false;
						DocumentCriteria documentCriteria = new DocumentCriteria();
						documentCriteria.setCurrentStatus(SiteStandard.DocumentStatus.published.getId());
						documentCriteria.setNodePath(node.getPath());
						List<Document> documentList = documentService.list(documentCriteria);
						if(documentList != null && documentList.size() > 0){

							File documentDir = new File(nodeStaticPath + File.separator + node.getPath() + File.separator + SitePath.documentPrefix.getName());

							if(!documentDir.exists()){
								continue;
							}

							File[] _oldDocumentList = documentDir.listFiles();
							for(File _oldDocument : _oldDocumentList){
								boolean deleteDocument = true;
								for(Document document : documentList){	
									if(_oldDocument.getName().equals(document.getDocumentCode() + CommonStandard.DEFAULT_PAGE_SUFFIX) && _oldDocument.isFile()){
										deleteDocument = false;
										break;
									}
									if(_oldDocument.getName().equals(dirIndexFileName)){
										deleteDocument = false;
										break;
									}
									if(_oldDocument.isDirectory()){
										deleteDocument = false;
										break;
									}
								}
								for(VirtualDocument vd : VirtualDocument.values()){
									if(_oldDocument.getName().equals(vd.toString() + CommonStandard.DEFAULT_PAGE_SUFFIX)){
										deleteDocument = false;
										break;
									}
								}
								if(deleteDocument){
									logger.debug("删除目录[" + node.getPath() + "]下已不应存在的文档HTML:" + _oldDocument.getName());
									report.append("删除目录[" + node.getPath() + "]下已不应存在的文档HTML:" + _oldDocument.getName());
									_oldDocument.delete();
								}


							}
						}
					}
				}
				for(VirtualDocument vd : VirtualDocument.values()){
					if(oldFile.getName().equals(vd.toString() + CommonStandard.DEFAULT_PAGE_SUFFIX)){
						delete = false;
						break;
					}
				}
				if(delete){
					if(oldFile.isFile()){
						oldFile.delete();
					} else {
						File[] subFiles = oldFile.listFiles();
						for(File subFile : subFiles){
							if(subFile.delete()){
								report.append("成功删除子文件[" + subFile.getName() + "]\n");
							} else {
								report.append("无法删除子文件[" + subFile.getName() + "]\n");
							}
						}
						if(oldFile.delete()){
							report.append("成功删除节点目录[" + oldFile.getName() + "]\n");
						} else {						
							report.append("无法删除节点目录[" + oldFile.getName() + "]\n");
						}
					}
				}
			}		
		} else { // 使用节点.shtml形式
			for(File oldFile : _oldFileList){
				report.append("比对已存在的文件:" + oldFile.getName() + "\n");
				boolean delete = true;
				for(Node node : nodeList){
					if(oldFile.getName().equals(node.getPath() + CommonStandard.DEFAULT_PAGE_SUFFIX)){
						report.append("文件[" + oldFile.getName() + "]无需删除\n");
						delete = false;
					}
				}
				if(delete){
					report.append("删除文件[" + oldFile.getName() + "]\n");
					if(oldFile.delete()){
						report.append("成功删除节点文件[" + oldFile.getName() + "]\n");
					} else {						
						report.append("无法删除节点文件[" + oldFile.getName() + "]\n");
					}
				}
			}	
		}

		return report.toString();
	}

	private String removeInvalidProduct() throws Exception {
		StringBuffer report = new StringBuffer();
		if(productStaticPath == null || productStaticPath.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}
		File destDir = new File(productStaticPath);
		if(!destDir.exists()){
			logger.debug("指定路径不存在，创建目录[" + productStaticPath);
			destDir.mkdirs();
		}

		ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setCurrentStatus(BasicStatus.normal.getId());
		productCriteria.setSupplierId(0);
		productCriteria.setProductLevel(1);
		productCriteria.setDataFetchMode(DataFetchMode.simple.toString());
		List<Product> productList = productDao.list(productCriteria);
		//比对不存在的节点目录或文件
		File[] _oldFileList = destDir.listFiles();
		if(SitePath.createIndexFile.getName().equals("true")){//使用目录+index的形式
			for(File oldFile : _oldFileList){
				logger.debug("比对已存在的文件夹:" + oldFile.getName());
				boolean delete = true;
				for(Product product : productList){
					if(oldFile.getName().equals(product.getProductCode()) && oldFile.isDirectory()){
						delete = false;
					}
				}
				if(delete){
					if(oldFile.isFile()){
						oldFile.delete();
					} else {
						File[] subFiles = oldFile.listFiles();
						for(File subFile : subFiles){
							if(subFile.delete()){
								report.append("成功删除子文件[" + subFile.getName() + "]\n");
							} else {
								report.append("无法删除子文件[" + subFile.getName() + "]\n");
							}
						}
						if(oldFile.delete()){
							report.append("成功删除节点目录[" + oldFile.getName() + "]\n");
						} else {						
							report.append("无法删除节点目录[" + oldFile.getName() + "]\n");
						}
					}
				}
			}		
		} else { // 使用.shtml形式
			for(File oldFile : _oldFileList){
				logger.info("比对已存在的文件:" + oldFile.getName());
				boolean delete = true;
				for(Product product : productList){
					if(oldFile.getName().equals(product.getProductCode() + CommonStandard.DEFAULT_PAGE_SUFFIX)){
						logger.debug("文件[" + oldFile.getName() + "]无需删除");
						delete = false;
					}
				}
				if(delete){
					logger.debug("删除文件[" + oldFile.getName() + "]");
					if(oldFile.delete()){
						report.append("成功删除节点文件[" + oldFile.getName() + "]");
					} else {						
						report.append("无法删除节点文件[" + oldFile.getName() + "]");
					}
				}
			}	
		}

		return report.toString();
	}

	//在指定目录中比对文件，只保留合法的文件，删除其他文件
	private int cleanFilesInDir(String destDir, List<String> validFileNames){
		File dir = new File(destDir);
		if(!dir.exists()){
			logger.debug("指定路径[" + destDir + "]不存在，忽略本次操作");
			return -1;
		}

		File[] _oldFileList = dir.listFiles();
		logger.debug("尝试清理目录[" + destDir + "]下的文件，应当存在的合法文件有[" + (validFileNames == null ? -1 :  validFileNames.size()) + "]个，实际存在的文件有[" + _oldFileList.length + "]个");
		int deleteCount = 0;
		for(File oldFile : _oldFileList){
			logger.debug("比对已存在的文件:" + oldFile.getName());
			if(oldFile.isDirectory()){
				logger.debug(oldFile.getName() + "是文件夹，忽略本次操作");
				continue;
			}
			boolean delete = true;
			for(String validFileName : validFileNames){
				if(oldFile.getName().equals(validFileName + CommonStandard.DEFAULT_PAGE_SUFFIX)){
					delete = false;
				}
			}
			if(delete){
				if(oldFile.isFile()){
					oldFile.delete();
					deleteCount++;
				} 
			}
		}		
		return deleteCount;
	}





}
