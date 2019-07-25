package com.maicard.site.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maicard.annotation.InputLevel;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.util.NumericUtils;
import com.maicard.serializer.ExtraDataMapSerializer;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.SiteStandard;
import com.maicard.views.JsonFilterView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Document  extends OpEisObject implements Cloneable {

	private static final long serialVersionUID = 1L;
	

	@JsonView(JsonFilterView.Partner.class)
	private long parentUdid;


	@JsonView(JsonFilterView.Partner.class)
	private int udid;

	@InputLevel(JsonFilterView.Partner.class)
	private String documentCode;

	@InputLevel(JsonFilterView.Partner.class)
	private String title;

	private String content;

	@InputLevel(JsonFilterView.Partner.class)
	private String author;

	@JsonView(JsonFilterView.Partner.class)
	private long publisherId;

	@JsonView(JsonFilterView.Partner.class)
	private Date createTime;

	@InputLevel(JsonFilterView.Partner.class)
	private Date publishTime;

	@JsonView(JsonFilterView.Partner.class)
	@InputLevel(JsonFilterView.Partner.class)
	private Date validTime;

	@JsonView(JsonFilterView.Partner.class)
	private int documentTypeId;

	@JsonView(JsonFilterView.Partner.class)
	@InputLevel(JsonFilterView.Partner.class)
	private String tags;

	@JsonView(JsonFilterView.Partner.class)
	private int flag;

	@JsonView(JsonFilterView.Partner.class)
	private int languageId;

	@JsonView(JsonFilterView.Partner.class)
	private String mimeType;	

	@JsonView(JsonFilterView.Partner.class)
	private int displayIndex;

	@JsonView(JsonFilterView.Partner.class)
	private int displayTypeId;

	@JsonView(JsonFilterView.Partner.class)
	private int alwaysOnTop;

	@JsonView(JsonFilterView.Partner.class)
	@InputLevel(JsonFilterView.Partner.class)
	private String redirectTo;

	@JsonView(JsonFilterView.Partner.class)
	@InputLevel(JsonFilterView.Partner.class)
	private int templateId;

	@JsonView(JsonFilterView.Partner.class)
	private Date lastModified;
	
	@JsonIgnore
	private Object param;
	
	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * 浏览该文档所需的级别
	 */
	@JsonView(JsonFilterView.Partner.class)
	private int viewLevel;

	/**
	 * 文档层级
	 */
	@JsonView(JsonFilterView.Partner.class)
	private int level;



	//statusName、languageName、documentTypeName、publisher等并不存在于document表中，而是查询自其他服务

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@JsonView(JsonFilterView.Partner.class)
	private Node defaultNode;

	@JsonView(JsonFilterView.Partner.class)
	private List<Node> relatedNodeList;

	@JsonSerialize(using = ExtraDataMapSerializer.class) 
	private Map<String,DocumentData> documentDataMap;



	private String publisher;

	//private String documentTypeName;
	
	private String viewUrl;

	@JsonView(JsonFilterView.Partner.class)
	private String documentTypeCode;

	@JsonView(JsonFilterView.Partner.class)
	private String displayTypeName;

	private int pages;




	public Document() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title != null){
			this.title = title.trim();
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if(author != null){
			this.author = author.trim();
		}
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		if(tags != null)
			this.tags = tags.trim();
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)udid;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Document other = (Document) obj;
		if (udid != other.udid)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"udid=" + "'" + udid + "'," + 
				"documentCode=" + "'" + documentCode + "'," + 
				"currentStatus=" + "'" + currentStatus + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 

				")";
	}
	
	@Override
	public String getViewUrl(){
		if(this.viewUrl != null){
			return this.viewUrl;
		}
		if(this.defaultNode != null){
			this.viewUrl = "/" + SiteStandard.SitePath.contentPrefix  + this.defaultNode.getPath() + "/" + this.documentCode + ".shtml";
			return this.viewUrl;
		}
		if(this.getRelatedNodeList() == null || this.getRelatedNodeList().size() < 1){
			return null;
		}
		String url = null;
		for(Node node : this.getRelatedNodeList()){
			if(node.getCurrentStatus() == BasicStatus.relation.getId()){
				url = node.getPath();
				break;
			}
		}
		if(url == null){
			return null;
		}
		this.viewUrl = "/" + SiteStandard.SitePath.contentPrefix + url + "/" + this.documentCode + ".shtml";
		return this.viewUrl;
	}

	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getUdid() {
		return udid;
	}

	public void setUdid(int udid) {
		this.udid = udid;
	}

	public long getParentUdid() {
		return parentUdid;
	}

	public void setParentUdid(long parentUdid) {
		this.parentUdid = parentUdid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}


	public int getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}

	public int getAlwaysOnTop() {
		return alwaysOnTop;
	}

	public void setAlwaysOnTop(int alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}

	public int getDisplayTypeId() {
		return displayTypeId;
	}

	public void setDisplayTypeId(int displayTypeId) {
		this.displayTypeId = displayTypeId;
	}

	public String getDisplayTypeName() {
		return displayTypeName;
	}

	public void setDisplayTypeName(String displayTypeName) {
		this.displayTypeName = displayTypeName;
	}

	public List<Node> getRelatedNodeList() {
		return relatedNodeList;
	}

	public void setRelatedNodeList(List<Node> relatedNodeList) {
		this.relatedNodeList = relatedNodeList;
	}

	public Node getDefaultNode() {
		return defaultNode;
	}

	public void setDefaultNode(Node defaultNode) {
		this.defaultNode = defaultNode;
	}



	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Map<String, DocumentData> getDocumentDataMap() {
		if(documentDataMap == null){
			documentDataMap = new HashMap<String,DocumentData>();
		}
		return documentDataMap;
	}

	public void setDocumentDataMap(Map<String, DocumentData> documentDataMap) {
		this.documentDataMap = documentDataMap;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getRedirectTo() {
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	/*public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}*/

	@Override
	public Document clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Document)in.readObject();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getDesc(int length) {
		if(length < 1){
			length = DocumentCriteria.DEFAULT_DESC_LENGTH;
		}
		String desc = null;
		if(this.documentDataMap != null && this.documentDataMap.get(DataName.documentBrief.toString()) != null && StringUtils.isNotBlank(this.documentDataMap.get(DataName.documentBrief.toString()).getDataValue())){
			desc = this.documentDataMap.get(DataName.documentBrief.toString()).getDataValue();
		} else {
			desc = this.content;
		}
		if(desc == null){
			return null;
		}
		if(desc.length() <= length){
			return desc;
		}
		return desc.substring(0,length);
	}
	
	@Override
	public void setExtraValue(String dataCode, String dataValue) {
		if(this.documentDataMap == null){
			this.documentDataMap = new HashMap<String, DocumentData>();
		}
		if(StringUtils.isBlank(dataValue)) {
			//是删除
			if(this.documentDataMap.containsKey(dataCode)) {
				this.documentDataMap.remove(dataCode);
			}
			return;
		}
		this.documentDataMap.put(dataCode, new DocumentData(dataCode,dataValue));
	}
	
	@Override
	public String getExtraValue(String dataCode) {
		if(this.documentDataMap == null){
			return null;
		}
		if(!this.documentDataMap.containsKey(dataCode)){
			return null;
		}
		if(this.documentDataMap.get(dataCode).getDataValue() != null){
			return this.documentDataMap.get(dataCode).getDataValue().trim();
		}
		return null;
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.documentDataMap == null){
			return false;
		}
		if(!this.documentDataMap.containsKey(dataCode)){
			return false;
		}
		if(this.documentDataMap.get(dataCode).getDataValue() != null && this.documentDataMap.get(dataCode).getDataValue().trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}



	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.documentDataMap == null){
			return 0;
		}
		if(!this.documentDataMap.containsKey(dataCode)){
			return 0;
		}
		if(this.documentDataMap.get(dataCode).getDataValue() != null && NumericUtils.isNumeric(documentDataMap.get(dataCode).getDataValue())){
			return Long.parseLong(documentDataMap.get(dataCode).getDataValue().trim());
		}
		return 0;
	}

	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.documentDataMap == null){
			return 0;
		}
		if(!this.documentDataMap.containsKey(dataCode)){
			return 0;
		}
		if(this.documentDataMap.get(dataCode).getDataValue() != null && NumericUtils.isNumeric(documentDataMap.get(dataCode).getDataValue())){
			return Float.parseFloat(documentDataMap.get(dataCode).getDataValue().trim());
		}
		return 0;
	}

	public int getViewLevel() {
		return viewLevel;
	}

	public void setViewLevel(int viewLevel) {
		this.viewLevel = viewLevel;
	}

	
	@Override
	public long getId() {
		return this.udid;
	}
	
	@Override
	public String getName() {
		return getTitle();
	}

	@Override
	public void setName(String name) {
		setTitle(name);
	}

}
