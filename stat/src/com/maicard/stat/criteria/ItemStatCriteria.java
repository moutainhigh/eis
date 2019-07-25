package com.maicard.stat.criteria;


public class ItemStatCriteria extends StatCriteria {

	private static final long serialVersionUID = 1L;
	
	private int[] productId;
	private String[] region;	
	private String[] server;
	private long[] inviter;
    private String username;
    private int extraStatus;
    private String uuids;
	public ItemStatCriteria() {
	}


	public int[] getProductId() {
		return productId;
	}


	public void setProductId(int... productId) {
		this.productId = productId;
	}


	public String[] getRegion() {		
		return region;
	}


	public void setRegion(String... region) {		
		this.region = region;
	}


	public String[] getServer() {
		return server;
	}


	public void setServer(String... server) {
		this.server = server;
	}


	public long[] getInviter() {
		return inviter;
	}


	public void setInviter(long... inviter) {
		this.inviter = inviter;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public int getExtraStatus() {
		return extraStatus;
	}


	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}


	public String getUuids() {
		return uuids;
	}


	public void setUuids(String uuids) {
		this.uuids = uuids;
	}



}
