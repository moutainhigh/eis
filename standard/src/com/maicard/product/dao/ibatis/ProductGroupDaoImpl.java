package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.dao.ProductGroupDao;
import com.maicard.product.domain.ProductGroup;

@Repository
public class ProductGroupDaoImpl extends BaseDao implements ProductGroupDao {

	public int insert(ProductGroup productGroup) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.product.sql.ProductGroup.insert", productGroup);
	}

	public int update(ProductGroup productGroup) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.product.sql.ProductGroup.update", productGroup);

	}

	public int delete(long id) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.ProductGroup.delete", id);
	}

	public ProductGroup select(long id) throws DataAccessException {
		return  getSqlSessionTemplate().selectOne("com.maicard.product.sql.ProductGroup.select", id);
	}

	public List<ProductGroup> list(ProductGroupCriteria productGroupCriteria) throws DataAccessException {
		Assert.notNull(productGroupCriteria, "productGroupCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ProductGroup.list", productGroupCriteria);
	}
	
	@Override
	public List<ProductGroup> listNextGroup(ProductGroupCriteria productGroupCriteria) throws DataAccessException {
		Assert.notNull(productGroupCriteria, "productGroupCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ProductGroup.listNextGroup", productGroupCriteria);
	}

	public List<ProductGroup> listOnPage(ProductGroupCriteria productGroupCriteria) throws DataAccessException {
		Assert.notNull(productGroupCriteria, "productGroupCriteria must not be null");
		Assert.notNull(productGroupCriteria.getPaging(), "paging must not be null");

		int totalResults = count(productGroupCriteria);
		Paging paging = productGroupCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ProductGroup.list", productGroupCriteria,rowBounds);
	}

	public int count(ProductGroupCriteria productGroupCriteria) throws DataAccessException {
		Assert.notNull(productGroupCriteria, "productGroupCriteria must not be null");

		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.ProductGroup.count", productGroupCriteria);
	}

	@Override
	public long readMaxObjectId(long ownerId) {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.ProductGroup.getMaxObjectId", ownerId);

	}




}
