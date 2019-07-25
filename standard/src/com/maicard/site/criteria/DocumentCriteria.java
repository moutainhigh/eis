package com.maicard.site.criteria;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.maicard.common.base.Criteria;

public class DocumentCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_DESC_LENGTH = 40;

	private int udid;

	private String[] documentCode;

	private int documentTypeId;

	private String documentTypeCode;
	
	private String noDocumentTypeCode;

	private String author;

	private String title;		//注意，title在数据库中为模糊查询
	
	private String publishTime;
	
	private Date publishTimeEnd;
	
	private Date publishTimeBegin;
	
	private String mimeType;	


	private int publisherId;

	private int currentUuid;

	private int displayTypeId;

	private long invalidTimestamp;

	private int anonymousReturnDocumentDataCutCount;

	private String[] nodePath;
	
	private String[] tags;
	
	private int flag;
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	private long ignoreReader;
	/**
	 * maxUdid用来指定返回文档的最大ID，返回文档的UDID不能大于这个
	 * 用来返回上一篇文档
	 */
	private long maxUdid;
	
	/**
	 * 允许浏览栏目的最大级别
	 */
	private int maxViewLevel;
	
	/**
	 * minUdid用来指定返回文档的最小ID，返回文档的UDID不能小于这个
	 * 用来返回下一篇文档
	 */
	private long minUdid;
	
	public DocumentCriteria() {
	}

	public DocumentCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public int getUdid() {
		return udid;
	}

	public void setUdid(int udid) {
		this.udid = udid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = (author.trim().equals("") ? null : author.trim());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(StringUtils.isBlank(title)){
			return;
		}
		this.title = title.trim();
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = (publishTime.trim().equals("") ? null : publishTime.trim());
	}

	public int getDisplayTypeId() {
		return displayTypeId;
	}

	public void setDisplayTypeId(int displayTypeId) {
		this.displayTypeId = displayTypeId;
	}

	public int getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}

	public int getCurrentUuid() {
		return currentUuid;
	}

	public void setCurrentUuid(int currentUuid) {
		this.currentUuid = currentUuid;
	}

	public long getInvalidTimestamp() {
		return invalidTimestamp;
	}

	public void setInvalidTimestamp(long invalidTimestamp) {
		this.invalidTimestamp = invalidTimestamp;
	}

	public int getAnonymousReturnDocumentDataCutCount() {
		return anonymousReturnDocumentDataCutCount;
	}

	public void setAnonymousReturnDocumentDataCutCount(
			int anonymousReturnDocumentDataCutCount) {
		this.anonymousReturnDocumentDataCutCount = anonymousReturnDocumentDataCutCount;
	}

	public String[] getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String... documentCode) {
		if(documentCode != null && documentCode.length > 0) {
			this.documentCode = documentCode;
		}
	}

	public String[] getNodePath() {
		return nodePath;
	}

	public void setNodePath(String... nodePath) {
		this.nodePath = nodePath;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		if(documentTypeCode != null && !documentTypeCode.trim().equals("")){
			this.documentTypeCode = documentTypeCode.trim();
		}
	}

	public long getIgnoreReader() {
		return ignoreReader;
	}

	public void setIgnoreReader(long ignoreReader) {
		this.ignoreReader = ignoreReader;
	}

	public Date getPublishTimeEnd() {
		return publishTimeEnd;
	}

	public void setPublishTimeEnd(Date publishTimeEnd) {
		this.publishTimeEnd = publishTimeEnd;
	}

	public Date getPublishTimeBegin() {
		return publishTimeBegin;
	}

	public void setPublishTimeBegin(Date publishTimeBegin) {
		this.publishTimeBegin = publishTimeBegin;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"udid=" + "'" + udid + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
			
				")";
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String... tags) {
		this.tags = tags;
	}
	
	

	public long getMaxUdid() {
		return maxUdid;
	}

	public void setMaxUdid(long maxUdid) {
		this.maxUdid = maxUdid;
	}

	public long getMinUdid() {
		return minUdid;
	}

	public void setMinUdid(long minUdid) {
		this.minUdid = minUdid;
	}

	public String getNoDocumentTypeCode() {
		return noDocumentTypeCode;
	}

	public void setNoDocumentTypeCode(String noDocumentTypeCode) {
		this.noDocumentTypeCode = noDocumentTypeCode;
	}

	public int getMaxViewLevel() {
		return maxViewLevel;
	}

	public void setMaxViewLevel(int maxViewLevel) {
		this.maxViewLevel = maxViewLevel;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
