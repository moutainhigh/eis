package com.maicard.wpt.domain;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.http.HttpClientPoolV3;
import com.maicard.wpt.constants.WeixinConfig;

public class WeixinAsyncMessage implements Runnable{

		protected final Logger logger = LoggerFactory.getLogger(getClass());



		private WeixinRichTextMessage weixinRichTextMessage;
		private String accessToken;
		private String urlPrefix;


		public WeixinAsyncMessage(WeixinRichTextMessage weixinRichTextMessage, String accessToken, String urlPrefix){
			this.weixinRichTextMessage = weixinRichTextMessage;
			this.accessToken = accessToken;
			this.urlPrefix = urlPrefix;
		}


		@Override
		public void run() {
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
			sb.append("{\"touser\":\"");  
			sb.append(weixinRichTextMessage.getTo()); 
			sb.append("\",\"msgtype\":\"news\",\"news\":{\"articles\":");
			sb.append("[{");
			sb.append("\"title\":\"" + weixinRichTextMessage.getTitle() + "\",");
			sb.append("\"description\":\"" + weixinRichTextMessage.getContent() + "\",");
			sb.append("\"url\":\"" + url + "\",");
			sb.append("\"picurl\":\"" + picUrl + "\"");
			sb.append("}]");
			sb.append("}}");
			int delaySec = weixinRichTextMessage.getDelaySec();
			if(delaySec > 0){
				logger.info("等待" + delaySec + "秒后发送图文消息[" + weixinRichTextMessage.getWeixinRichTextMessageId() + "]");
				try {
					Thread.sleep(delaySec * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.info("发送图文消息[" + weixinRichTextMessage.getWeixinRichTextMessageId() + "]:" + sb.toString());
			HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
			final String apiUrl = WeixinConfig.API_PREFIX + "/cgi-bin/message/custom/send?access_token=" + accessToken;

			int rs = 0;
			String result = null;
			try{
				PostMethod pm = new PostMethod(apiUrl);
				pm.setRequestEntity(new StringRequestEntity(sb.toString(),"","UTF-8"));
				rs = httpClient.executeMethod(pm);
				result = pm.getResponseBodyAsString();
			}catch(Exception e){
				e.printStackTrace();
			}
			logger.info("图文消息发送结果:" + rs + "/" + result);
		}

}
