package com.maicard.wpt.criteria;


import com.maicard.common.base.Criteria;
public class EntityShopCriteria extends Criteria implements Cloneable {

	
	private static final long serialVersionUID = -318209341289543819L;

	private long entityShopId;			//PK
	
	//上游关联ID
	private String upId;
	
	//下游关联ID，如微信
	private String downId;
    
    //属于哪个商户
	private long uuid;					
	
	//商户名，不含店名
    private String businessName;
    
    //店名，不含商户名
    private String branchName;
    
    
    //所在省、直辖市
    private String province;
    
    //所在城市
    private String city;
    
    //所在地区    
    private String district;
    
  
    
    //联系电话
    private String phone;
    
	//分类
    private String categories;
    
    //经度
    private String longitude;
    
    //纬度
    private String latitude;
    
   
  
    //人均价格
    private int avgPrice;
    
  
    //推荐
    private String recommend;
    
    
    
    
    //开放时间
    private String  openTime;
    
 
	

	

	
	public EntityShopCriteria() {
	}
	
	

	public long getEntityShopId() {
		return entityShopId;
	}



	public void setEntityShopId(long entityShopId) {
		this.entityShopId = entityShopId;
	}



	public long getUuid() {
		return uuid;
	}



	public void setUuid(long uuid) {
		this.uuid = uuid;
	}



	public String getBusinessName() {
		return businessName;
	}



	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}



	public String getBranchName() {
		return branchName;
	}



	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}



	public String getProvince() {
		return province;
	}



	public void setProvince(String province) {
		this.province = province;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getDistrict() {
		return district;
	}



	public void setDistrict(String district) {
		this.district = district;
	}





	public String getCategories() {
		return categories;
	}



	public void setCategories(String categories) {
		this.categories = categories;
	}



	public String getLongitude() {
		return longitude;
	}



	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}



	public String getLatitude() {
		return latitude;
	}



	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}





	public int getAvgPrice() {
		return avgPrice;
	}



	public void setAvgPrice(int avgPrice) {
		this.avgPrice = avgPrice;
	}




	public String getRecommend() {
		return recommend;
	}



	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}



	public String getOpenTime() {
		return openTime;
	}



	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}



	public String getUpId() {
		return upId;
	}



	public void setUpId(String upId) {
		this.upId = upId;
	}



	public String getDownId() {
		return downId;
	}



	public void setDownId(String downId) {
		this.downId = downId;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}
	

}
