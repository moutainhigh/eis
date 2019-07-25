package com.maicard.wpt.criteria;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.maicard.common.base.Criteria;

public class WeixinButtonCriteria extends Criteria implements Cloneable {

	private static final long serialVersionUID = -2619959274241034010L;

	private long parentButtonId;

	private long weixinMenuId;

	private String type;

	private String name;

	private String key;

	private String mediaId;

	private String url;

	private long uuid;

	private String tagId;
	
	
	@Override
	public WeixinButtonCriteria clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (WeixinButtonCriteria)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public WeixinButtonCriteria() {
	}

	public WeixinButtonCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getParentButtonId() {
		return parentButtonId;
	}

	public void setParentButtonId(long parentButtonId) {
		this.parentButtonId = parentButtonId;
	}

	public long getWeixinMenuId() {
		return weixinMenuId;
	}

	public void setWeixinMenuId(long weixinMenuId) {
		this.weixinMenuId = weixinMenuId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

}
