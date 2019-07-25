package com.maicard.wpt.criteria;


import com.maicard.common.base.Criteria;

/**
 *	微信富媒体查询条件
 *
 * @author NetSnake
 * @date 2016-01-11
 * 
 */
public class WeixinRichTextMessageCriteria  extends Criteria {
	
	public static final String TRIGGER_MODE_ON_SUBSCRIBE="ON_SUBSCRIBE";

	private static final long serialVersionUID = 6871794595799368163L;
	
	private int weight;

	private String trigger;

	private String to;		//发送给哪个openId

	private String from;		//发自谁，一般是自己的公众号openId

	private String title;		//标题



	private String content;		//内容

	private String picUrl;		

	private String url;		//点击本文本要跳转的地址


	
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
