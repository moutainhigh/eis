package com.maicard.site.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.FileSign;
import com.maicard.common.util.HttpUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.MessageService;
import com.maicard.site.criteria.StaticizeCriteria;
import com.maicard.site.dao.StaticizeDao;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Staticize;
import com.maicard.site.service.StaticizeService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import static com.maicard.standard.CommonStandard.DEFAULT_PAGE_SUFFIX;

public class StaticizeServiceImpl extends BaseService implements StaticizeService{

	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;

	@Resource
	private StaticizeDao staticizeDao;


	private String globalSiteStaticPathPrefix;

	private String siteDynamicUrl;

	private final String dirIndexFileName = "index" + DEFAULT_PAGE_SUFFIX;


	@PostConstruct
	public void init(){
		globalSiteStaticPathPrefix = configService.getValue(DataName.globalSiteStaticPathPrefix.toString(),0);
		if(globalSiteStaticPathPrefix == null){
			logger.warn("找不到系统全局配置[globalSiteStaticPathPrefix]");
		}



	}

	//静态化某个栏目的页面，不包含栏目下面的文章
	public void staticize(Node node){
		logger.debug("执行站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的首页静态化");
		if(globalSiteStaticPathPrefix == null || globalSiteStaticPathPrefix.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}

		siteDynamicUrl = configService.getValue(DataName.siteDynamicUrl.toString(), node.getOwnerId());
		if(siteDynamicUrl == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[siteDynamicUrl]");
		}

		String url = null;
		if(node.getPath() == null){
			throw new RequiredObjectIsNullException("节点[" + node.getNodeId() + "]路径为空，无法静态化");
		}


		String fileName = getFileDest(node);

		if(node.getPath().equals(".") || node.getPath().equals("")){
			//首页
			url = siteDynamicUrl;
		} else {
			url = siteDynamicUrl + "/content/" + node.getPath().replaceAll("/$", "") + "/" + dirIndexFileName;
		}

		File file = new File(fileName);
		File destDir = file.getParentFile();
		if(!destDir.exists()){
			logger.debug("指定路径不存在，创建目录:" + destDir.getAbsolutePath());
			try {
				FileUtils.forceMkdir(destDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		logger.debug("尝试请求站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的栏目[" + node.getPath() + "]页面 :" + url);
		String content = HttpUtils.sendData(url);
		if(content == null){
			logger.error("无法获取页面:" + url + ",请求返回空");
			return;
		}

		Date staticizeTime = new Date();
		content = writeToken(content, new SimpleDateFormat(CommonStandard.defaultDateFormat).format((staticizeTime)));

		String fileSign = null;
		try {
			fileSign = FileSign.getFileSign(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long fileSize = content.length();
		logger.debug("尝试创建站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的栏目[" + node.getPath() + "]的静态文件:" + fileName + ",文件大小:" + fileSize + ",文件签名:" + fileSign);

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), CommonStandard.DEFAULT_ENCODING));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Staticize staticize = new Staticize(node.getOwnerId());
		staticize.setObjectType(ObjectType.node.name());
		staticize.setObjectId(node.getNodeId());
		staticize.setUrl(url);
		staticize.setFileName(fileName);
		staticize.setFileSign(fileSign);
		staticize.setFileSize(fileSize);
		staticize.setStaticizeTime(staticizeTime);
		replace(staticize);
		try {
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override 
	public int replace(Staticize staticize) {
		if(staticize.getStaticizeId() > 0){
			return update(staticize);
		}
		if(staticize.getObjectType() == null  || staticize.getObjectId() < 1){
			logger.error("尝试更新的静态化数据，没有objectType和objectId");
			return -1;
		}
		StaticizeCriteria staticizeCriteria = new StaticizeCriteria(staticize.getOwnerId());
		List<Staticize> staticizeList = list(staticizeCriteria);
		if(staticizeList == null || staticizeList.size() < 1){
			logger.debug("根据条件[" + staticizeCriteria + "]找不到任何数据");
			return insert(staticize);
		} else if(staticizeList.size() != 1){
			logger.error("根据条件[" + staticizeCriteria + "]找到的数据不是1，是:" + staticizeList.size());
			return -1;
		}
		staticize.setStaticizeId(staticizeList.get(0).getStaticizeId());

		return update(staticize);
	}



	private int insert(Staticize staticize) {
		return staticizeDao.insert(staticize);
	}

	private int update(Staticize staticize) {
		return staticizeDao.update(staticize);
	}

	@Override
	public List<Staticize> list(StaticizeCriteria staticizeCriteria) {
		return staticizeDao.list(staticizeCriteria);
	}

	@Override
	@Async
	public void staticize(Document document, long waitSec) {

		logger.debug("执行文章[" + document.getUdid() + "/" + document.getDocumentCode()+ "]的静态化，等待时间:" + waitSec);
		if(waitSec > 0){
			try {
				Thread.sleep(waitSec * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(globalSiteStaticPathPrefix == null || globalSiteStaticPathPrefix.equals("")){
			throw new RequiredObjectIsNullException("未指定静态化目标存储路径");
		}


		Node node = document.getDefaultNode();
		if(node == null){
			throw new RequiredObjectIsNullException("尝试静态化的文章[" + document.getUdid() + "/" + document.getDocumentCode()+ "]没有默认节点对象");
		}

		siteDynamicUrl = configService.getValue(DataName.siteDynamicUrl.toString(), node.getOwnerId());
		if(siteDynamicUrl == null){
			throw new RequiredObjectIsNullException("找不到系统全局配置[siteDynamicUrl]");
		}

		String url = null;
		if(node.getPath() == null){
			throw new RequiredObjectIsNullException("节点[" + node.getNodeId() + "]路径为空，无法静态化");
		}



		url = siteDynamicUrl + "/content";

		if(node.getPath().equals(".") || node.getPath().equals("")){
			//首页
			url = url + "/" + document.getDocumentCode() + DEFAULT_PAGE_SUFFIX;
		} else {
			url = url + "/" + node.getPath().replaceAll("/$", "")  + "/" + document.getDocumentCode() + DEFAULT_PAGE_SUFFIX;
		}

		String fileName = getFileDest(document);
		File file = new File(fileName);

		File destDir = file.getParentFile();
		if(!destDir.exists()){
			logger.debug("指定路径不存在，创建目录:" + destDir.getAbsolutePath());
			try {
				FileUtils.forceMkdir(destDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		logger.debug("尝试请求站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的文章[" +  document.getUdid() + "/" + document.getDocumentCode() + "]页面 :" + url);
		String content = HttpUtils.sendData(url);
		if(content == null){
			logger.error("无法获取页面:" + url + ",请求返回空");
			return;
		}

		Date staticizeTime = new Date();
		content = writeToken(content, new SimpleDateFormat(CommonStandard.defaultDateFormat).format((staticizeTime)));
		String fileSign = null;
		try {
			fileSign = FileSign.getFileSign(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long fileSize = content.length();
		logger.debug("尝试创建站点[" + node.getSiteCode() + "/" + node.getOwnerId() + "]的文章[" +  document.getUdid() + "/" + document.getDocumentCode() + "]的静态文件:" + fileName + ",文件大小:" + fileSize + ",文件签名:" + fileSign);

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), CommonStandard.DEFAULT_ENCODING));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Staticize staticize = new Staticize(document.getOwnerId());
		staticize.setObjectType(ObjectType.document.name());
		staticize.setUrl(url);
		staticize.setObjectId(document.getUdid());
		staticize.setFileName(fileName);
		staticize.setFileSign(fileSign);
		staticize.setFileSize(fileSize);
		staticize.setStaticizeTime(staticizeTime);
		replace(staticize);
		try {
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * 根据指定栏目，生成它的静态化文件路径和名称
	 */
	private String getFileDest(Node node){

		String path = null;

		path = globalSiteStaticPathPrefix.replaceAll("/$", "") + File.separator + node.getSiteCode();

		if(node.getPath().equals(".") || node.getPath().equals("")){

		} else {
			path = path +  File.separator + "content" + File.separator + node.getPath();
		}

		String fileName = path + File.separator + dirIndexFileName;

		return fileName;


	}

	/**
	 * 根据指定文档，生成它的静态化文件路径和名称
	 */
	private String getFileDest(Document document){

		Node node = document.getDefaultNode();
		if(node == null){
			throw new RequiredObjectIsNullException("尝试静态化的文章[" + document.getUdid() + "/" + document.getDocumentCode()+ "]没有默认节点对象");
		}

		String path = null;

		path = globalSiteStaticPathPrefix.replaceAll("/$", "") + File.separator + node.getSiteCode() + File.separator + "content";

		if(node.getPath().equals(".") || node.getPath().equals("")){
			//首页
		} else {
			path = path + File.separator + node.getPath();
		}
		String fileName = path + File.separator + document.getDocumentCode() + DEFAULT_PAGE_SUFFIX; 

		return fileName;


	}

	private String writeToken(String content, String tokenValue) {
		try{
			org.jsoup.nodes.Document d = Jsoup.parse(content);
			Element meta = d.select("head").first();
			meta.append(" <meta name=\"" + CommonStandard.EIS_STATICIZE_TOKEN + "\" content=\"" + tokenValue + "\">");
			logger.debug("为静态页面加入静态标记:" + CommonStandard.EIS_STATICIZE_TOKEN + ":" + tokenValue);
			content = d.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public int deleteStaticizeFile(Node node) {
		String fileName = getFileDest(node);
		File file = new File(fileName);
		boolean deleted = FileUtils.deleteQuietly(file);

		logger.debug("删除栏目[" + node.getNodeId() + "/" + node.getName() + "]的静态化文件:" + fileName + ",是否删除成功:" + deleted);
		//无论是否删除成功，都发送删除消息
		Node n2 = node.clone();
		n2.setSyncFlag(0);
		messageService.sendJmsDataSyncMessage(null, "staticizeService", "deleteStaticizeFile", n2);
		File parentDir = file.getParentFile();
		if(parentDir.isDirectory()){
			String[] fileList = parentDir.list();
			if(fileList == null || fileList.length < 1){
				boolean deleteDir = FileUtils.deleteQuietly(parentDir);
				logger.debug("删除栏目[" + node.getNodeId() + "/" + node.getName() + "]的静态化文件:" + fileName + ",对应目录[" + parentDir.getAbsolutePath() + "]为空，删除目录,是否删除成功:" + deleteDir);

			}

		}
		if(!deleted){
			return -1;
		}
		StaticizeCriteria staticizeCriteria = new StaticizeCriteria(node.getOwnerId());
		staticizeCriteria.setObjectType(ObjectType.node.name());
		staticizeCriteria.setObjectId(node.getNodeId());
		delete(staticizeCriteria);
		return 1;
	}

	@Override
	public int deleteStaticizeFile(Document document) {
		String fileName = getFileDest(document);
		File file = new File(fileName);

		boolean deleted = FileUtils.deleteQuietly(file);
		logger.debug("删除文章[" + document.getUdid() + "/" + document.getDocumentCode() + "]的静态化文件:" + fileName + ",是否删除成功:" + deleted);
		//无论是否删除成功，都发送删除消息
		Document d2 = document.clone();
		d2.setSyncFlag(0);
		messageService.sendJmsDataSyncMessage(null, "staticizeService", "deleteStaticizeFile", d2);
		File parentDir = file.getParentFile();
		if(parentDir.isDirectory()){
			String[] fileList = parentDir.list();
			if(fileList == null || fileList.length < 1){
				boolean deleteDir = FileUtils.deleteQuietly(parentDir);
				logger.debug("文章[" + document.getUdid() + "/" + document.getDocumentCode() + "]的静态化文件:" + fileName + ",对应目录[" + parentDir.getAbsolutePath() + "]为空，删除目录,是否删除成功:" + deleteDir);

			}

		}
		if(!deleted){
			return -1;
		}
		StaticizeCriteria staticizeCriteria = new StaticizeCriteria(document.getOwnerId());
		staticizeCriteria.setObjectType(ObjectType.document.name());
		staticizeCriteria.setObjectId(document.getUdid());
		delete(staticizeCriteria);
		return 1;

	}

	@Override
	public int delete(StaticizeCriteria staticizeCriteria) {
		Assert.notNull(staticizeCriteria,"尝试条件删除的staticizeCriteria不能为空");
		Assert.notNull(staticizeCriteria.getObjectType(),"尝试条件删除的staticizeCriteria，对象类型不能为空");
		Assert.isTrue(staticizeCriteria.getObjectId() > 0,"尝试条件删除的staticizeCriteria，对象ID不能为空");
		List<Staticize> staticizeList = list(staticizeCriteria);
		if(staticizeList == null || staticizeList.size() < 1){
			return -1;
		}
		int count = 0;
		for(Staticize staticize : staticizeList){
			int rs = staticizeDao.delete(staticize.getStaticizeId());
			if(rs == 1){
				count++;
			}
		}
		return count;
	}
}
