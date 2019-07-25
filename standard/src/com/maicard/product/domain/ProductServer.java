package com.maicard.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.Server;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductServer extends Server {

	private static final long serialVersionUID = 1L;

	
	private long productId;
	
	
	
	private String serverUrl;
	
	
	private int weight = 0;

	private long activity = 0;

	private String outServerCode;
	
	private int regionId;
	

	

	public ProductServer() {
	}

	

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public long getActivity() {
		return activity;
	}

	public void setActivity(long activity) {
		this.activity = activity;
	}

	
	



	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	
	@Override
	public ProductServer clone() {
		try{
			return (ProductServer)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}



	public String getServerUrl() {
		return serverUrl;
	}



	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}



	public String getOutServerCode() {
		return outServerCode;
	}



	public void setOutServerCode(String outServerCode) {
		this.outServerCode = outServerCode;
	}



	public long getProductId() {
		return productId;
	}



	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	
}
