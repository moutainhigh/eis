package com.maicard.stat.domain;

import com.maicard.common.domain.EisObject;

public class UserIpStat extends EisObject {
	
	private static final long serialVersionUID = -7774736003596755447L;

	private Integer successCount;

	private Integer totalCount;

	private Float successMoney;
	
	private Float totalMoney;		
	
	private Integer productId;
	
	private String region;
	
	private String server;
	
	private String statTime;
		
	private Long inviter;
	
	////////////////////////////////////////////	
	private Float countRate;
	private Float moneyRate;
	private String productName;
	private String inviterName;
    private int level;

	public Integer getSuccessCount() {
		return successCount == null ? 0 : successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getTotalCount() {
		return totalCount == null ? 0 : totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Float getSuccessMoney() {
		return successMoney == null ? 0f : successMoney;
	}

	public void setSuccessMoney(Float successMoney) {
		this.successMoney = successMoney;
	}

	public Float getTotalMoney() {
		return totalMoney == null ? 0f : totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getProductId() {
		return productId == null ? 0 : productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Long getInviter() {
		return inviter == null ? 0 : inviter;
	}

	public void setInviter(Long inviter) {
		this.inviter = inviter;
	}

	public Float getCountRate() {
		return countRate;
	}

	public void setCountRate(Float countRate) {
		this.countRate = countRate;
	}

	public Float getMoneyRate() {
		return moneyRate;
	}

	public void setMoneyRate(Float moneyRate) {
		this.moneyRate = moneyRate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInviterName() {
		return inviterName;
	}

	public void setInviterName(String inviterName) {
		this.inviterName = inviterName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
