package com.maicard.site.service.spider;


import java.util.List;



import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.service.SpiderService;

@Service
public class BbsSpiderSeviceImpl extends BaseService implements SpiderService {

	@Override
	public List<Document> list(DocumentCriteria documentCriteria) {
		return null;
		/*String host = "bbs.yeele.cn";
		int port = 80;
		String urlPrefix = "http://bbs.yeele.cn/";
		String url = urlPrefix + "search.php?search_id=active_topics";
		String result = HttpUtils.sendData(host, port, url);
		//System.out.println(result);
		String numberString = "rightside pagination";
		int offset = result.indexOf(numberString);
		int length = offset + numberString.length() + 30;
		String countResult = result.substring(offset, length);
		Pattern pattern = Pattern.compile("得到 \\d+ 个符合结果");
		Matcher m = pattern.matcher(countResult);
		int totalRows = 0;
		if(m.find()){
			String count =  m.group();
			count = count.replaceAll("得到", "");
			count = count.replaceAll("个符合结果", "");
			count = count.trim();
			//logger.info("BBS link count:" + count);
			totalRows = Integer.parseInt(count);
			
		}
		List<Document> documentList = new ArrayList<Document>();
		if(totalRows > 0){
			String startString = "row bg1";
			String endString = "fieldset class=\"display-options\"";
			String content = null;
			try{
				content = result.substring(result.indexOf(startString), result.indexOf(endString));
			}catch(Exception e){
				e.printStackTrace();
			}
			if(content == null){
				logger.error("未发现需要的字符");
				return null;
			}
			String linkString = "<a href=\"./viewtopic.php";
			pattern = Pattern.compile(linkString);
			m = pattern.matcher(content);
			while(m.find()){
				//logger.info("Start:" + m.start());
				Pattern p2 = Pattern.compile("</a>");
				Matcher m2 = p2.matcher(content.substring(m.start()));
				if(m2.find()){
					//logger.info("End:" + m2.end());
					String link = content.substring(m.start(), m.start() + m2.end());
					if(link.indexOf("topictitle") > 0){
						try{
						int offset1 = link.indexOf("\" class=\"topictitle\">");
						String urlStr = link.substring(11, offset1);
						urlStr = urlStr.replaceAll("&amp;", "&");
						String title = link.substring(offset1 + 21, link.length() -4);
						//logger.info("link=" + urlPrefix + urlStr + ", title=" + title);
						Document document = new Document();
						document.setTitle(title);
						document.setContent(urlPrefix + urlStr);
						documentList.add(document);
						}catch(Exception e2){
							e2.printStackTrace();
						}
						
					}
				}
			}
			
			
		}
		logger.info("BBS文档:" + documentList.size());
		return documentList;*/
	}
	
	public static void main(String[] argv){
		BbsSpiderSeviceImpl s = new BbsSpiderSeviceImpl();
		s.list(new DocumentCriteria());
	}

}
