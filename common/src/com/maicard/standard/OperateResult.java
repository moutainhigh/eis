package com.maicard.standard;

/**
 * 
 * 操作结果枚举
 *
 * @author GHOST
 * @date 2012-12-08
 */
public enum OperateResult {

	/**
	 * 未知
	 */
	unknown(0),
	
	/**
	 * 接受
	 */
	accept(102005),
	
	/**
	 * 拒绝
	 */
	deny(102006),
	
	/**
	 * 失败
	 */
	failed(102007),
	
	/**
	 * 成功
	 */
	success(102008),
	
	/**
	 * 等待处理
	 */
	waiting(102009);

	public final int id;

	private OperateResult(int id){
		this.id = id;
	}
	public int getId() {
		return id;
	}


	@Override
	public String toString(){
		return name();
	}
	public OperateResult findById(int id) {
		for(OperateResult value: OperateResult.values()){
			if(value.getId() == id){
				return value;
			}
		}
		return unknown;
	}	
}
