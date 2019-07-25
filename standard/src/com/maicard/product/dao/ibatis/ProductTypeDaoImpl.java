package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.dao.ProductTypeDao;
import com.maicard.product.domain.ProductType;

@Repository
public class ProductTypeDaoImpl extends BaseDao implements ProductTypeDao {

	public int insert(ProductType productType) throws DataAccessException {
		return getSqlSessionTemplate().insert("ProductType.insert", productType);
	}

	public int update(ProductType productType) throws DataAccessException {
		return getSqlSessionTemplate().update("ProductType.update", productType);
	}

	public int delete(int productTypeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("ProductType.delete", new Integer(productTypeId));
	}

	public ProductType select(int productTypeId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("ProductType.select", new Integer(productTypeId));
	}

	public List<ProductType> list(ProductTypeCriteria productTypeCriteria) throws DataAccessException {
		Assert.notNull(productTypeCriteria, "productTypeCriteria must not be null");	
		return getSqlSessionTemplate().selectList("ProductType.list", productTypeCriteria);
	}

	public List<ProductType> listOnPage(ProductTypeCriteria productTypeCriteria) throws DataAccessException {
		Assert.notNull(productTypeCriteria, "productTypeCriteria must not be null");
		Assert.notNull(productTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(productTypeCriteria);
		Paging paging = productTypeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("ProductType.list", productTypeCriteria, rowBounds);
	}

	public int count(ProductTypeCriteria productTypeCriteria) throws DataAccessException {
		Assert.notNull(productTypeCriteria, "productTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("ProductType.count", productTypeCriteria);
	}

}
