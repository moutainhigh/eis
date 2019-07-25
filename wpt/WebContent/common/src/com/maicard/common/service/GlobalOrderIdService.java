package com.maicard.common.service;

import java.util.Date;



public interface GlobalOrderIdService {

	/**
	 * 查询一个订单号是否已存在，仅对保存过的有效
	 * @param orderId
	 * @return
	 */
	boolean exist(String orderId);
	
	/**
	 * 插入一条订单号
	 * @param globalOrderId
	 */
	void insert(String globalOrderId);

	/**
	 * 不指定条件，自动生成一个类型为其他的订单号
	 * @return
	 */
	String generate();

	/**
	 * 根据订单号，从订单中的时间部分，返回对应的时间
	 * @param transactionId
	 * @return
	 */
	Date getDateByTransactionId(String transactionId);

	/**
	 * 生成一个指定类型的订单号
	 * @param transactionTypeId
	 * @return
	 */
	String generate(int transactionTypeId);

	/**
	 * 生成一个指定类型和订单时间的订单号
	 * @param transactionTypeId
	 * @param orderDate
	 * @return
	 */
	String generate(int transactionTypeId, Date orderDate);

}
