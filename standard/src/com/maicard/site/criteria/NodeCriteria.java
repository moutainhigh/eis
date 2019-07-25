package com.maicard.site.criteria;



import org.apache.commons.lang.StringUtils;

import com.maicard.common.base.Criteria;

public class NodeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int nodeId;
	private String path;
	private String alias;
	private int parentNodeId;
	private int displayWeight;
	
	/**
	 * 栏目的分类
	 */
	private String category;
	
	/**
	 * 栏目的二级分类
	 */
	private String sort;
	
	private long minPrice = -1;
	
	private long maxPrice = -1;
	
	private String priceType = "money";
	
	/**
	 * 当需要与UserRelation进行关联时，关联类型
	 */
	private String userRelation;
	
	/**
	 * 该栏目是否应当出现在网站导航栏中，如电商的所有商品这种栏目
	 */
	private int nodeTypeId;
	private int rootNodeId;
	private String siteCode;
	private boolean includeChild;
	
	/**
	 * 允许浏览栏目的最大级别
	 */
	private int maxViewLevel;
	
	private String outId;
	
	private String[] tags;


	public NodeCriteria() {
	}

	public NodeCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}


	public int getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(int parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public int getDisplayWeight() {
		return displayWeight;
	}

	public void setDisplayWeight(int displayWeight) {
		this.displayWeight = displayWeight;
	}

	public int getNodeTypeId() {
		return nodeTypeId;
	}

	public void setNodeTypeId(int nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	public int getRootNodeId() {
		return rootNodeId;
	}

	public void setRootNodeId(int rootNodeId) {
		this.rootNodeId = rootNodeId;
	}

	public boolean isIncludeChild() {
		return includeChild;
	}

	public void setIncludeChild(boolean includeChild) {
		this.includeChild = includeChild;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if(StringUtils.isNotBlank(path)){
			this.path = path;
		}
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		if(StringUtils.isNotBlank(alias)){
			this.alias = alias;
		}
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		if(StringUtils.isNotBlank(siteCode)){
			this.siteCode = siteCode;
		}
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public int getMaxViewLevel() {
		return maxViewLevel;
	}

	public void setMaxViewLevel(int maxViewLevel) {
		this.maxViewLevel = maxViewLevel;
	}

	public String[] getTags() {
		
		return tags;
	}

	public void setTags(String... tags) {
		this.tags = tags;
	}

	
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
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

	public long getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(long minPrice) {
		this.minPrice = minPrice;
	}

	public long getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(long maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getUserRelation() {
		return userRelation;
	}

	public void setUserRelation(String userRelation) {
		this.userRelation = userRelation;
	}
	

}
