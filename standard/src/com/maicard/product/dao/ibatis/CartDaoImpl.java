package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.dao.CartDao;
import com.maicard.product.domain.Cart;

@Repository
public class CartDaoImpl extends BaseDao implements CartDao {

	public int insert(Cart cart) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.product.sql.Cart.insert", cart);
	}

	public int update(Cart cart) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.product.sql.Cart.update", cart);
	}

	public int delete(long cartId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.Cart.delete", cartId);

	}

	public Cart select(long cartId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Cart.select", cartId);
	}

	public List<Cart> list(CartCriteria cartCriteria) throws DataAccessException {
		Assert.notNull(cartCriteria, "cartCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Cart.list", cartCriteria);
	}

	public List<Cart> listOnPage(CartCriteria cartCriteria) throws DataAccessException {
		Assert.notNull(cartCriteria, "cartCriteria must not be null");
		Assert.notNull(cartCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(cartCriteria);
		Paging paging = cartCriteria.getPaging();
		paging.setTotalResults(totalResults);
		logger.debug("查询条件页面，第{}页，共{}条数据，当前返回第{}到第{}条:" + paging.getCurrentPage(), paging.getTotalResults(), paging.getFirstResult(), paging.getMaxResults());
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		logger.debug("查询条件开始条数:" + rowBounds.getOffset() + ",返回条数限制:" + rowBounds.getLimit());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Cart.list", cartCriteria, rowBounds);
	}

	public int count(CartCriteria cartCriteria) throws DataAccessException {
		Assert.notNull(cartCriteria, "cartCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Cart.count", cartCriteria);
	}

	@Override
	public int updateNoNull(Cart cart) {
		return getSqlSessionTemplate().update("com.maicard.product.sql.Cart.updateNoNull", cart);
	}

}
