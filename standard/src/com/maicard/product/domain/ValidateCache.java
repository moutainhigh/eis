/**
 * 
 */
package com.maicard.product.domain;

import java.io.Serializable;

/**
 * 
 *
 * @author NetSnake
 * @date 2013-9-5 
 */
public class ValidateCache implements Serializable{
	

	private static final long serialVersionUID = -2293468226432304773L;
	private String data1;
	private String data2;
	private String data3;

	public ValidateCache(){
		
	}
	
	public String getData1() {
		return data1;
	}
	public void setData1(String data1) {
		this.data1 = data1;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data2) {
		this.data2 = data2;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data3) {
		this.data3 = data3;
	}
	
	 

}
