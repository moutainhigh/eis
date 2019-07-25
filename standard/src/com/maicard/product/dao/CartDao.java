package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.domain.Cart;

public interface CartDao {

	int insert(Cart cart) throws DataAccessException;

	int update(Cart cart) throws DataAccessException;

	int delete(long cartId) throws DataAccessException;

	Cart select(long cartId) throws DataAccessException;

	List<Cart> list(CartCriteria cartCriteria) throws DataAccessException;
	
	List<Cart> listOnPage(CartCriteria cartCriteria) throws DataAccessException;
	
	int count(CartCriteria cartCriteria) throws DataAccessException;

	int updateNoNull(Cart cart);

}
