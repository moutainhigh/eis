package com.maicard.site.service.spider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.util.TagUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.service.SpiderService;

/**
 * 抓取糗事百科这个网站的段子
 *
 * @author NetSnake
 * @date 2015年1月10日 
 */


@Service
public class QiushibaikeSpiderImpl extends BaseService implements SpiderService {

	private final String host = "www.qiushibaike.com";
	private final int port = 80;
	private final String listArticleUrl = "http://www.qiushibaike.com/text/";
	private final String lastArticleIdFile = "qiushibaike_lastid.dat";


	@Override
	public List<Document> list(DocumentCriteria documentCriteria) {
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		int lastArticleId = readLastId();
		int currentMaxArticleId = 0;
		
		
		String result = null;
		try{
			result = HttpUtilsV3.getData(httpClient, listArticleUrl,null);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(result == null){
			logger.error("无法获取URL:" + listArticleUrl);
			return null;
		}
		String beginString = "<div id=\"content-left\" class=\"col1\">";
		int startOffset = result.indexOf(beginString);
		if(startOffset < 0){
			logger.error("找不到字符串：" + beginString + ",无法解析");
			return null;
		}
		String endString = "<div class=\"pagebar clearfix\">";
		int endOffset = result.indexOf(endString);
		if(endOffset < 0){
			logger.error("找不到字符串：" + endString + ",无法解析");
			return null;
		}
		String pageData = result.substring(startOffset + beginString.length(), endOffset);


		int offset1 = -1;
		int offset2 = -1;
		//缩小文档范围
		String code1 = "<div class=\"article block untagged mb15\" id='";
		String code2 = "<div class=\"stats clearfix\">";
		//文档ID的范围
		String code3 = "qiushi_tag_";
		String code4 = "'>";
		//正文范围
		String code5 = "<div class=\"content\" title=\"";
		String code6 = "</div>";
		
		List<Document> documentList = new ArrayList<Document>();
		while(pageData.indexOf(code1) >= 0){
			offset1 = pageData.indexOf(code1);

			offset2 = pageData.indexOf(code2);

			String data1 = pageData.substring(offset1 + code1.length(), offset2);

			//logger.info("得到一篇段子:" + data1);
			pageData = pageData.substring(offset2 + code2.length());

			//获取ID和标题
			offset1  = data1.indexOf(code3);
			if(offset1 < 0){
				logger.error("找不到段子的标题数据:" + code3);
				continue;
			} 
			offset2  = data1.indexOf(code4);
			if(offset2 < 0){
				logger.error("找不到段子的标题数据:" + code4);
				continue;
			}
			String subData = TagUtils.removeHtml(data1.substring(offset1 + code3.length(), offset2));
			//logger.info("段子数据:" + subData);
			
			//logger.info("段子ID:" + subData);
			int articleId = Integer.parseInt(subData);
			if(articleId <= lastArticleId){
				logger.info("段子" + articleId + "]已经被处理过，忽略");
				continue;
			}
			if(articleId > currentMaxArticleId){
				currentMaxArticleId = articleId;
			}
			//获取内容
			offset1  = data1.indexOf(code5);
			logger.info("code5 offset=" + offset1);
			if(offset1 < 0){
				logger.error("找不到段子的内容数据:" + code5);
				continue;
			} 
			String subData2 = data1.substring(offset1 + code5.length());
			offset2  = subData2.indexOf(code6);
			logger.info("code6 offset=" + offset2);

			if(offset2 < 0){
				logger.error("找不到段子的内容数据:" + code6);
				continue;
			}
			try{
				subData2 = StringUtils.split(subData2.substring(0, offset2), ">")[1].trim();
			}catch(Exception e){
				e.printStackTrace();
				logger.error("无法解析段子内容");
				continue;
			}
			logger.info("段子内容:" + subData2);
			
			String title = null;
			if(subData2.length() > 10){
				title = subData2.substring(0,10);
			} else {
				title = subData2;
			}
			Document document = new Document();
			document.setTitle(title);
			document.setContent(subData2);
			documentList.add(document);






		}
		if(documentList != null && documentList.size() > 0 && currentMaxArticleId > lastArticleId){
			writeLastId(currentMaxArticleId);
		}
		return documentList;
	}

	private int readLastId() {
		try{
			/*InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(lastArticleIdFile);
			if(in == null){
				logger.error("无法读取最后ID文件:" + Thread.currentThread().getContextClassLoader().getResource("").toString() + "" + lastArticleIdFile);
				return 0;
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
			byte[] data = new byte[BUFFER_SIZE];  
			int count = -1;  
			while((count = in.read(data,0,BUFFER_SIZE)) != -1)  {
				outStream.write(data, 0, count);  
			}	        
			data = null; 	        */
			File file = new File(lastArticleIdFile + "");
			int id = Integer.parseInt(FileUtils.readFileToString(file).trim());
			logger.info("读取到的最后一个ID是:" + id);
			return id;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		return 0;
	}

	private void writeLastId(int id) {
	//	String path = Thread.currentThread().getContextClassLoader().getResource("").toURI(). + File.pathSeparator + lastArticleIdFile;
		String path = ""+lastArticleIdFile;
		try {
			FileUtils.write(new File(path), ""+id, false);
			logger.info("将最后文章ID[" + id + "]写入文件[" + path + "]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv){
		QiushibaikeSpiderImpl n = new QiushibaikeSpiderImpl();
		n.list(new DocumentCriteria());
	}

}
