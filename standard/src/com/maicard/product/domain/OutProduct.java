/**
 * 
 */
package com.maicard.product.domain;

import com.maicard.common.domain.EisObject;

/**
 * 外部系统产品编码与我方产品的对应
 * 
 *
 * @author NetSnake
 * @date 2013-9-6 
 */
public class OutProduct extends EisObject{
	

	private static final long serialVersionUID = -705978806757610008L;
	
	/**
	 * 我方内部产品ID
	 */
	private long internalProductId;

	/**
	 * 外部产品代码
	 */
	private String outProductCode;
	
	

	public String getOutProductCode() {
		return outProductCode;
	}
	public void setOutProductCode(String outProductCode) {
		this.outProductCode = outProductCode;
	}
	public long getInternalProductId() {
		return internalProductId;
	}
	public void setInternalProductId(long internalProductId) {
		this.internalProductId = internalProductId;
	}
	
	
	
	
}
