package com.maicard.site.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.OpEisObject;
import com.maicard.standard.SiteStandard;
import com.maicard.views.JsonFilterView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Node extends OpEisObject implements Cloneable{

	private static final long serialVersionUID = 1L;

	private int nodeId;

	/**
	 * 栏目的分类
	 */
	private String category;
	
	/**
	 * 栏目的二级分类
	 */
	private String sort;

	/**
	 * 与外部系统关联的ID
	 */
	@JsonView(JsonFilterView.Partner.class)
	private String outId;

	/**
	 * 节点的预览图地址
	 */	
	private String pic;
	
	/**
	 * 浏览该栏目所需的级别
	 */
	@JsonView(JsonFilterView.Partner.class)
	private int viewLevel;



	

	@JsonView(JsonFilterView.Partner.class)
	private int parentNodeId;


	/**
	 * 该栏目类型，如果是业务类型，则应当出现在网站导航栏中，如电商的所有商品这种栏目
	 */
	@JsonView(JsonFilterView.Partner.class)
	private int nodeTypeId;

	@JsonView(JsonFilterView.Partner.class)
	private int level;

	@JsonView(JsonFilterView.Partner.class)
	private String alias;

	@JsonView(JsonFilterView.Partner.class)
	private int displayWeight; 

	@JsonView(JsonFilterView.Partner.class)
	private int redirect;

	@JsonView(JsonFilterView.Partner.class)
	private String redirectTo;

	private String path;

	@JsonView(JsonFilterView.Partner.class)
	private String processClass;

	@JsonView(JsonFilterView.Partner.class)
	private int templateId;

	@JsonView(JsonFilterView.Partner.class)
	private String siteCode;

	@JsonView(JsonFilterView.Partner.class)
	private Date lastModified;

	@JsonView(JsonFilterView.Partner.class)
	private Set<IncludeNodeConfig> includeNodeSet;





	//非持久化属性	
	@JsonView(JsonFilterView.Partner.class)
	private String templateLocation;

	@JsonView(JsonFilterView.Partner.class)
	private List<Node> subNodeList;


	@JsonView(JsonFilterView.Partner.class)
	private String parentNodeName;

	@JsonView(JsonFilterView.Partner.class)
	private List<Node> nodePath;

	private String tags;

	/**
	 * 访问该栏目的URL，动态生成
	 */
	private String viewUrl;



	public Node() {
	}

	@Override
	public Node clone(){
		return (Node)super.clone();

	}



	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(int parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public int getNodeTypeId() {
		return nodeTypeId;
	}

	public void setNodeTypeId(int nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeId;

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
		final Node other = (Node) obj;
		if (nodeId != other.nodeId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"nodeId=" + "'" + nodeId + "'," + 
				"nodeTypeId=" + "'" + nodeTypeId + "'," + 
				"name=" + "'" + name + "'," + 
				"currentStatus=" + "'" + currentStatus + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}

	@Override
	public String getViewUrl(){
		if(this.viewUrl != null){
			return this.viewUrl;
		}
		this.viewUrl = "/" + SiteStandard.SitePath.contentPrefix  + this.path + "/index.shtml";
		return this.viewUrl;


	}

	public List<Node> getSubNodeList() {
		return subNodeList;
	}

	public void setSubNodeList(List<Node> subNodeList) {
		this.subNodeList = subNodeList;
	}


	public String getParentNodeName() {
		return parentNodeName;
	}

	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getRedirect() {
		return redirect;
	}

	public void setRedirect(int redirect) {
		this.redirect = redirect;
	}

	public int getDisplayWeight() {
		return displayWeight;
	}

	public void setDisplayWeight(int displayWeight) {
		this.displayWeight = displayWeight;
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public String getRedirectTo() {
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Node> getNodePath() {
		return nodePath;
	}

	public void setNodePath(List<Node> nodePath) {
		this.nodePath = nodePath;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getTemplateLocation() {
		return templateLocation;
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Set<IncludeNodeConfig> getIncludeNodeSet() {
		return includeNodeSet;
	}

	public void setIncludeNodeSet(Set<IncludeNodeConfig> includeNodeSet) {
		this.includeNodeSet = includeNodeSet;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getViewLevel() {
		return viewLevel;
	}

	public void setViewLevel(int viewLevel) {
		this.viewLevel = viewLevel;
	}
	
	@Override
	public long getId() {
		return this.nodeId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
