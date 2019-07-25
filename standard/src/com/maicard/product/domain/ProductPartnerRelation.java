package com.maicard.product.domain;


import com.maicard.common.domain.EisObject;
import com.maicard.product.domain.ProductPartnerRelation;

/**
 * 产品与合作伙伴关系
 * 对于金融行业而言，定义推介产品与合作经理人之间的关系
 * 即，某个产品的合作方销售人员是谁。
 * 
 * 对于点卡行业而言，定义了某个合作伙伴可以提交哪些产品并拥有分成权限
 * 以及该渠道的特定参数，如超时时间、重试次数和无法处理时的策略
 * 
 * 
 *
 * @author NetSnake
 * @date 2013-9-24 
 */
public class ProductPartnerRelation extends EisObject{

	private static final long serialVersionUID = 8245349073774880083L;

	private int productPartnerRelationId;

	private int productId;

	private int partnerId;
	
	private int weight; //显示优先级
	
	private int ttl;	//超时时间
	
	private int maxRetry;	//最大重试次数
	
	private String failPolicy;	//无法处理时的策略

	public ProductPartnerRelation() {
	}



	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}



	public int getProductPartnerRelationId() {
		return productPartnerRelationId;
	}



	public void setProductPartnerRelationId(int productPartnerRelationId) {
		this.productPartnerRelationId = productPartnerRelationId;
	}



	public int getPartnerId() {
		return partnerId;
	}



	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}



	public int getWeight() {
		return weight;
	}



	public void setWeight(int weight) {
		this.weight = weight;
	}



	public int getTtl() {
		return ttl;
	}



	public void setTtl(int ttl) {
		this.ttl = ttl;
	}



	public int getMaxRetry() {
		return maxRetry;
	}



	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}



	public String getFailPolicy() {
		return failPolicy;
	}



	public void setFailPolicy(String failPolicy) {
		this.failPolicy = failPolicy;
	}




	
}
