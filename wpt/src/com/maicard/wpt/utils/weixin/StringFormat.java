package com.maicard.wpt.utils.weixin;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.common.util.ClassUtils;
import com.maicard.standard.CommonStandard;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinRichTextMessage;

public class StringFormat {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static final Logger logger = LoggerFactory.getLogger(StringFormat.class);


	public static String stripCdata(String src){
		if(src == null){
			return src;
		}
		return src.replaceAll("<\\!\\[CDATA\\[","").replaceAll("\\]\\]>", "").trim();
	}

	public static String getFormattedText(byte[] bytes) {  
		int len = bytes.length;  
		StringBuilder buf = new StringBuilder(len * 2);  
		// 把密文转换成十六进制的字符串形式  
		for (int j = 0; j < len; j++) {  
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);  
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);  
		}  
		return buf.toString();  
	}  

	public static String formatMsg2Xml(WeixinRichTextMessage weixinRichTextMessage, String urlPrefix){

		String picUrl = weixinRichTextMessage.getPicUrl();
		if(picUrl != null && picUrl.startsWith("/")){
			//把相对地址改为绝对地址
			picUrl = urlPrefix + picUrl;		
		}
		String url = weixinRichTextMessage.getUrl();
		if(url != null && url.startsWith("/")){
			//把相对地址改为绝对地址
			url = urlPrefix + url;		
		}
		StringBuffer sb = new StringBuffer();  
		Date date = new Date();  
		sb.append("<xml><ToUserName><![CDATA[");  
		sb.append(weixinRichTextMessage.getTo());  
		sb.append("]]></ToUserName><FromUserName><![CDATA[");  
		sb.append(weixinRichTextMessage.getFrom());  
		sb.append("]]></FromUserName><CreateTime>");  
		sb.append(date.getTime());  
		if (StringUtils.isNotBlank(weixinRichTextMessage.getPicUrl()) && StringUtils.isNotBlank(weixinRichTextMessage.getUrl())) {
			sb.append("</CreateTime><MsgType><![CDATA[news]]></MsgType>");
			sb.append("<ArticleCount>1</ArticleCount>");  
			sb.append("<Articles>");  
			sb.append("<item>");  
			sb.append("<Title><![CDATA[" + weixinRichTextMessage.getTitle() + "]]></Title>");  
			sb.append("<Description><![CDATA[" + weixinRichTextMessage.getContent() + "]]></Description>");
			sb.append("<PicUrl><![CDATA[" +  picUrl + "]]></PicUrl>");
			sb.append("<Url><![CDATA[" + url + "]]></Url>");  
			sb.append("</item>");
			sb.append("</Articles>"); 
			sb.append("</xml>");  
		} else if(StringUtils.isBlank(weixinRichTextMessage.getContent()) && StringUtils.isNotBlank(weixinRichTextMessage.getPicUrl())){
			//如果只有图片地址，那么应该发送图片消息
			sb.append("</CreateTime><MsgType><![CDATA[image]]></MsgType><Image><MediaId><![CDATA[");  
			sb.append(weixinRichTextMessage.getPicUrl());  	//此处URL应该是mediaID
			sb.append("]]></MediaId></Image></xml>");
		} else{
			sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");  
			sb.append(weixinRichTextMessage.getContent());  
			sb.append("]]></Content></xml>");
		}


		/*sb.append("<item>");  
		sb.append("<Title><![CDATA[" + welcomeBaojieTitle + "]]></Title>");  
		sb.append("<Description><![CDATA[" + welcomeBaojieContent + "]]></Description>");  
		sb.append("<PicUrl><![CDATA[" + welcomeBaojiePicUrl + "]]></PicUrl>");  
		sb.append("<Url><![CDATA[" + welcomeBaojieUrl + "]]></Url>");  
		sb.append("</item>");  */

		return sb.toString();  
	}

	/**
	 * 转化成XML格式
	 */
	public static String formatText2Xml(String from, String to, String content) {
		if(StringUtils.isBlank(content)){
			logger.info("消息为空，无法格式化为微信XML格式");
			return content;
		}
		if(content.startsWith("<xml")){
			logger.info("消息已经是XML格式不再处理");
			return content;
		}
		StringBuffer sb = new StringBuffer();  
		Date date = new Date();  
		sb.append("<xml><ToUserName><![CDATA[");  
		sb.append(from);  
		sb.append("]]></ToUserName><FromUserName><![CDATA[");  
		sb.append(to);  
		sb.append("]]></FromUserName><CreateTime>");  
		sb.append(date.getTime());  
		sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");  
		sb.append(content);  
		//sb.append("]]></Content><FuncFlag>0</FuncFlag></xml>");
		sb.append("]]></Content></xml>");
		return sb.toString();  
	}


	public static WeixinMsg formatXml2WeixinMsg(String strXml)//解析xml
	{  
		WeixinMsg msg = null;  
		if (strXml.length() <= 0 || strXml == null)  
			return null;  
		// 将字符串转化为XML文档对象  
		Document document = null;
		try {
			document = DocumentHelper.parseText(strXml.replaceAll("&", "${26}"));
		} catch (DocumentException e) {
			//e.printStackTrace();
		}  
		if(document == null){
			logger.error("无法解析XML:" + strXml);
			return null;
		}
		// 获得文档的根节点  
		Element root = document.getRootElement();  
		// 遍历根节点下所有子节点  
		Iterator<?> iter = root.elementIterator();  
		// 遍历所有结点  
		msg = new WeixinMsg();  
		try {
			while(iter.hasNext()){  
				Element ele = (Element)iter.next(); 
				//logger.debug("设置属性[" + ele.getName() + "=>" + ele.getText() + "]");
				String value = ele.getText().trim();
				if(ele.getName().equalsIgnoreCase("EventKey") || ele.getName().equalsIgnoreCase("Content")){
					value = value.replaceAll("\\$\\{26\\}", "&");
				}
				ClassUtils.setAttribute(msg, StringUtils.uncapitalize(ele.getName()),value, CommonStandard.COLUMN_TYPE_NATIVE);
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;  
	}
	
	
}
