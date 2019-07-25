package com.maicard.product.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.exception.EisException;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;

public interface CartService {

	//int insert(Item item, boolean useLabelMoney, boolean isFreeProduct);
	
	Item select(long uuid, String transactionId);

	void delete(long uuid, String transactionId) throws Exception;


	void clear(long uuid) throws Exception;

	HashMap<String, Item> map(ItemCriteria itemCriteria);

	List<Cart> list(CartCriteria cartCriteria);

	List<Item> list(ItemCriteria itemCriteria);
	
	int count(CartCriteria cartCriteria);


	int count(long uuid, int status);

	
	int insert(Cart cart);

	Cart select(long cartId);

	int createNewCartId();

	int updateNoNull(Cart cart);

	/**
	 * 根据条件得到用户当前的购物车
	 * 如果没有就创建一个新的
	 * 
	 */
	Cart getCurrentCart(long uuid, String priceType, String orderType, String identify, long ownerId,boolean createNewCart);


	Cart add(Item item, boolean createNewCart, String identify, int cartId) throws EisException;

	int update(Cart cart);

	void update(Item item, int changeCount) throws Exception;

	List<Cart> listOnPage(CartCriteria cartCriteria);

	int recycle(Cart order);

	int delete(long cartId);

	void finish(Cart cart);

	







}
