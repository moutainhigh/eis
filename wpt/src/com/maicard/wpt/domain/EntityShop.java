package com.maicard.wpt.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.EisObject;
import com.maicard.views.JsonFilterView;
/**
 * 实体店
 * 用于商户实体店与微信门店的对接
 *
 *
 * @author NetSnake
 * @date 2016年1月8日
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityShop extends EisObject implements Cloneable {

	
	private static final long serialVersionUID = -318209341289543819L;

	@JsonView({JsonFilterView.Full.class})
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
    
    //详细地址
    private String address;
    
    //联系电话
    private String phone;
    
	//分类
    private String categories;
    
    //经度
    private String longitude;
    
    //纬度
    private String latitude;
    
    //照片列表
    private String photoList;
    
    //特色服务
    private String specialService;
  
    //人均价格
    private int avgPrice;
    
    //简介
    private String introduction;
    
    //推荐
    private String recommend;
    
    
    
    
    //开放时间
    private String  openTime;
    
 
	

	

	
	public EntityShop() {
	}
	
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("@");
		sb.append(Integer.toHexString(hashCode()));
		sb.append("(");
		sb.append("entityShopId=");
		sb.append(entityShopId);
		sb.append(',');
		sb.append("uuid=");
		sb.append(uuid);
		sb.append(',');
		sb.append("businessName=");
		sb.append(businessName);
		sb.append(',');
		sb.append("branchName=");
		sb.append(entityShopId);
		sb.append(',');
		sb.append("branchName=");
		sb.append(entityShopId);
		sb.append(',');
		sb.append(')');
		
		return sb.toString();
	}
	
	@Override
	public EntityShop clone() {
		try{
			return (EntityShop)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
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



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
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





	public String getSpecialService() {
		return specialService;
	}



	public void setSpecialService(String specialService) {
		this.specialService = specialService;
	}



	public int getAvgPrice() {
		return avgPrice;
	}



	public void setAvgPrice(int avgPrice) {
		this.avgPrice = avgPrice;
	}



	public String getIntroduction() {
		return introduction;
	}



	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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



	public String getPhotoList() {
		return photoList;
	}



	public void setPhotoList(String photoList) {
		this.photoList = photoList;
	}
	

}
