package com.maicard.wpt.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.EisObject;

/**
 *	微信富媒体即图文消息实体类
 *
 * @author NetSnake
 * @date 2015年12月9日
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeixinRichTextMessage  extends EisObject {


	private static final long serialVersionUID = -6950771308537850871L;



	private long weixinRichTextMessageId;
	
	//当有多个消息需要发送时，权重越大，越先发送
	private int weight;	
	
	private String trigger;

	private String to;		//发送给哪个openId

	private String from;		//发自谁，一般是自己的公众号openId

	private String title;		//标题



	private String content;		//内容

	private String picUrl;		

	private String url;		//点击本文本要跳转的地址

	private int delaySec;			//延迟多久发送

	public WeixinRichTextMessage(){

	}

	/**
	 * 
	 * @param to	发送给哪个公众号
	 * @param from	发自谁
	 * @param title	 标题
	 * @param content 内容
	 * @param picUrl 要加载显示的图片URL
	 * @param url 要跳转的URL
	 * @param delaySec 延迟发送秒数
	 * @param ownerId 云平台ID
	 */
	public WeixinRichTextMessage(String to, String from, String title, String content,
			String picUrl, String url, int delaySec, long ownerId) {
		this.to = to;
		this.from = from;
		this.title = title;
		this.content = content;
		this.picUrl = picUrl;
		this.url = url;
		this.delaySec = delaySec;
		this.ownerId = ownerId;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public int getDelaySec() {
		return delaySec;
	}


	public void setDelaySec(int delaySec) {
		this.delaySec = delaySec;
	}

	public long getWeixinRichTextMessageId() {
		return weixinRichTextMessageId;
	}

	public void setWeixinRichTextMessageId(long weixinRichTextMessageId) {
		this.weixinRichTextMessageId = weixinRichTextMessageId;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}


}
