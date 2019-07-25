package com.maicard.wpt.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maicard.common.domain.EisObject;

//微信菜单的按钮
public class WeixinButton extends EisObject implements Cloneable {
	
	private static final long serialVersionUID = 8760836951280572840L;
	
	
	/**
	 * 主键
	 */
	private long weixinButtonId;
	
	/**
	 * 该微信按钮属于哪个用户
	 */
	private long uuid;
	/**
	 * 上级按钮ID
	 */
	private long parentButtonId;
	
	/**
	 * 属于哪个菜单
	 */
	//private long weixinMenuId;

	private String type;
	
	private String name;
	
	private String key;
	
	/**
	 * 情景菜单的tag
	 */
	private String tagId;
	
	
	@Override
	public WeixinButton clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (WeixinButton)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@JsonProperty("sub_button")
	private List<WeixinButton> subButton;
	
	@JsonProperty("media_id")
	private String mediaId;
	
	private String url;
		
	public WeixinButton(){
		
	}
	
	public WeixinButton(String type, String name, String key, String mediaId, String url){
		this.type = type;
		this.name = name;
		this.key = key;
		this.mediaId = mediaId;
		this.url = url;
	}
	
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"type=" + "'" + type + "'," + 
			"name=" + "'" + name + "'," + 
			"key=" + "'" + key + "'," + 
			"mediaId=" + "'" + mediaId + "'," + 
			"url=" + "'" + url + "'" + 
			")";
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


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getWeixinButtonId() {
		return weixinButtonId;
	}

	public void setWeixinButtonId(long weixinButtonId) {
		this.weixinButtonId = weixinButtonId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	/*public long getWeixinMenuId() {
		return weixinMenuId;
	}

	public void setWeixinMenuId(long weixinMenuId) {
		this.weixinMenuId = weixinMenuId;
	}
*/
	public long getParentButtonId() {
		return parentButtonId;
	}

	public void setParentButtonId(long parentButtonId) {
		this.parentButtonId = parentButtonId;
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

	public List<WeixinButton> getSubButton() {
		return subButton;
	}

	public void setSubButton(List<WeixinButton> subButton) {
		this.subButton = subButton;
	}

}
