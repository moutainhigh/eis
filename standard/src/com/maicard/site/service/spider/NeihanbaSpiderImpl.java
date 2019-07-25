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
 * 抓取内涵8这个网站的段子
 *
 * @author NetSnake
 * @date 2015年1月9日 
 */


@Service
public class NeihanbaSpiderImpl extends BaseService implements SpiderService {

	private final String host = "m.neihan8.com";
	private final int port = 80;
	private final String listArticleUrl = "http://m.neihan8.com/article/";
	private final String lastArticleIdFile = "neihanba8_lastid.dat";


	@Override
	public List<Document> list(DocumentCriteria documentCriteria) {
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		int lastArticleId = readLastId();
		int currentMaxArticleId = 0;
		
		
		String result = null;
		try{
			result = new String(HttpUtilsV3.getByteData(httpClient, listArticleUrl ),"GBK");
		}catch(Exception e){
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(result == null){
			logger.error("无法获取URL:" + listArticleUrl);
			return null;
		}
		String beginString = " <ul id=\"conbox\">";
		int startOffset = result.indexOf(beginString);
		if(startOffset < 0){
			logger.error("找不到字符串：" + beginString + ",无法解析");
			return null;
		}
		String endString = "<div class=\"mt10 mb10\">";
		int endOffset = result.indexOf(endString);
		if(endOffset < 0){
			logger.error("找不到字符串：" + endString + ",无法解析");
			return null;
		}
		String pageData = result.substring(startOffset + beginString.length(), endOffset);


		int offset1 = -1;
		int offset2 = -1;
		String code1 = "<li>";
		String code2 = "</li>";
		String code3 = "<p class=\"title\"><a href=\'/article/";
		String code4 = "</a>";
		String code5 = "<div class=\"con\">";
		String code6 = "<div class=\"tool\">";
		String code7 = "</span></p>";
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
			String data2[] = StringUtils.split(subData, ".html'>");
			if(data2 == null || data2.length < 2){
				logger.error("无法解析段子的标题数据:" + subData);
				continue;
			}
			logger.info("段子ID:" + data2[0] + ",标题:" + data2[1]);
			int articleId = Integer.parseInt(data2[0]);
			if(articleId <= lastArticleId){
				logger.info("段子" + data2[0] + "]已经被处理过，忽略");
				continue;
			}
			if(articleId > currentMaxArticleId){
				currentMaxArticleId = articleId;
			}
			//获取内容
			offset1  = data1.indexOf(code5);
			if(offset1 < 0){
				logger.error("找不到段子的内容数据:" + code5);
				continue;
			} 
			offset2  = data1.indexOf(code6);
			if(offset2 < 0){
				logger.error("找不到段子的内容数据:" + code6);
				continue;
			}
			String subData2 = data1.substring(offset1 + code5.length(), offset2);

			offset1 = subData2.indexOf(code7);
			if(offset1 < 0){
				logger.error("无法解析段子的内容标记:" + code7);
				continue;
			}
			subData2 = TagUtils.removeHtml(subData2.substring(offset1 + code7.length()));


			logger.info("段子正文数据:" + subData2);

			Document document = new Document();
			document.setAuthor(data2[0]);
			document.setTitle(data2[1]);
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
		NeihanbaSpiderImpl n = new NeihanbaSpiderImpl();
		n.list(new DocumentCriteria());
	}

}
