package com.maicard.wpt.domain;

public class CouponModelBaseInfo {
	protected String logo_url;

	protected String code_type;

	protected String brand_name;

	protected String title;

	protected String sub_title;

	protected String color;

	protected String notice;

	protected String description;

	protected String sku;

	protected int quantity;

	protected CouponModelDateInfo dynamicDateInfo;
	
	protected String date_info;
	
	
	protected boolean use_custom_code;
	
	protected boolean bind_openid;
	
	protected String service_phone;
	
	protected String location_id_list;
	
	protected String source;
	
	protected String custom_url_name;
	
	protected String center_title;
	
	protected String center_sub_title;
	
	protected String center_url;
	
	protected String custom_url;
	
	protected String custom_url_sub_title;
	
	protected String promotion_url_name;
	
	protected String promotion_url;

	protected String promotion_url_sub_title;

	protected int get_limit;

	protected boolean can_share;

	protected boolean can_give_friend;


	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getCode_type() {
		return code_type;
	}

	public void setCode_type(String code_type) {
		this.code_type = code_type;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDate_info() {
		return date_info;
	}

	public void setDate_info(String date_info) {
		this.date_info = date_info;
	}

	public boolean getUse_custom_code() {
		return use_custom_code;
	}

	public void setUse_custom_code(boolean use_custom_code) {
		this.use_custom_code = use_custom_code;
	}

	public boolean getBind_openid() {
		return bind_openid;
	}

	public void setBind_openid(boolean bind_openid) {
		this.bind_openid = bind_openid;
	}

	public String getService_phone() {
		return service_phone;
	}

	public void setService_phone(String service_phone) {
		this.service_phone = service_phone;
	}

	public String getLocation_id_list() {
		return location_id_list;
	}

	public void setLocation_id_list(String location_id_list) {
		this.location_id_list = location_id_list;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCustom_url_name() {
		return custom_url_name;
	}

	public void setCustom_url_name(String custom_url_name) {
		this.custom_url_name = custom_url_name;
	}

	public String getCenter_title() {
		return center_title;
	}

	public void setCenter_title(String center_title) {
		this.center_title = center_title;
	}

	public String getCenter_sub_title() {
		return center_sub_title;
	}

	public void setCenter_sub_title(String center_sub_title) {
		this.center_sub_title = center_sub_title;
	}

	public String getCenter_url() {
		return center_url;
	}

	public void setCenter_url(String center_url) {
		this.center_url = center_url;
	}

	public String getCustom_url() {
		return custom_url;
	}

	public void setCustom_url(String custom_url) {
		this.custom_url = custom_url;
	}

	public String getCustom_url_sub_title() {
		return custom_url_sub_title;
	}

	public void setCustom_url_sub_title(String custom_url_sub_title) {
		this.custom_url_sub_title = custom_url_sub_title;
	}

	public String getPromotion_url_name() {
		return promotion_url_name;
	}

	public void setPromotion_url_name(String promotion_url_name) {
		this.promotion_url_name = promotion_url_name;
	}

	public String getPromotion_url() {
		return promotion_url;
	}

	public void setPromotion_url(String promotion_url) {
		this.promotion_url = promotion_url;
	}

	public String getPromotion_url_sub_title() {
		return promotion_url_sub_title;
	}

	public void setPromotion_url_sub_title(String promotion_url_sub_title) {
		this.promotion_url_sub_title = promotion_url_sub_title;
	}

	public int getGet_limit() {
		return get_limit;
	}

	public void setGet_limit(int get_limit) {
		this.get_limit = get_limit;
	}

	public boolean isCan_share() {
		return can_share;
	}

	public void setCan_share(boolean can_share) {
		this.can_share = can_share;
	}

	public boolean isCan_give_friend() {
		return can_give_friend;
	}

	public void setCan_give_friend(boolean can_give_friend) {
		this.can_give_friend = can_give_friend;
	}

	public CouponModelDateInfo getDynamicDateInfo() {
		if(dynamicDateInfo == null){
			dynamicDateInfo = new CouponModelDateInfo();
		}
		return dynamicDateInfo;
	}

	public void setDynamicDateInfo(CouponModelDateInfo dynamicDateInfo) {
		this.dynamicDateInfo = dynamicDateInfo;
	}

}
