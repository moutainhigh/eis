package com.maicard.site.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;

/**
 * 站点、文章静态化数据
 *
 *
 * @author NetSnake
 * @date 2016年5月27日
 *
 */
public class Staticize extends EisObject{

	private static final long serialVersionUID = 4159747610394857276L;

	private long staticizeId;
	
	//继承自父类，因此不需要在这里写
	//private String objectType;
	
	private long objectId;
	
	private String url;
	
	private String fileName;
	
	private String fileSign;
	
	private long fileSize;
	
	private Date staticizeTime;
	
	public Staticize(){}
	
	public Staticize(long ownerId){
		this.ownerId = ownerId;
	}

	public long getStaticizeId() {
		return staticizeId;
	}

	public void setStaticizeId(long staticizeId) {
		this.staticizeId = staticizeId;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSign() {
		return fileSign;
	}

	public void setFileSign(String fileSign) {
		this.fileSign = fileSign;
	}

	public Date getStaticizeTime() {
		return staticizeTime;
	}

	public void setStaticizeTime(Date staticizeTime) {
		this.staticizeTime = staticizeTime;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
