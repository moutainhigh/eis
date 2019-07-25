package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ProductDataDao;
import com.maicard.product.domain.ProductData;

@Repository
public class ProductDataDaoImpl extends BaseDao implements ProductDataDao {

	
	public int insert(ProductData productData) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("ProductData.insert", productData);
	}

	public int update(ProductData productData) throws DataAccessException {
		return getSqlSessionTemplate().update("ProductData.update", productData);
	}

	public int delete(int productDataId) throws DataAccessException {
		return getSqlSessionTemplate().delete("ProductData.delete", new Integer(productDataId));
	}

	public ProductData select(int productDataId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("ProductData.select", productDataId);
	}

	@Override
	public List<Integer> listPk(ProductDataCriteria productDataCriteria) throws DataAccessException {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");		
		return getSqlSessionTemplate().selectList("ProductData.listPk", productDataCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(ProductDataCriteria productDataCriteria) throws DataAccessException {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");
		Assert.notNull(productDataCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(productDataCriteria);
		Paging paging = productDataCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("ProductData.listPk", productDataCriteria, rowBounds);
	}
	
	@Override
	public List<ProductData> list(ProductDataCriteria productDataCriteria) throws DataAccessException {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");

		return getSqlSessionTemplate().selectList("ProductData.list", productDataCriteria);
	}

	@Override
	public List<ProductData> listOnPage(ProductDataCriteria productDataCriteria) throws DataAccessException {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");
		Assert.notNull(productDataCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(productDataCriteria);
		Paging paging = productDataCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("ProductData.list", productDataCriteria, rowBounds);
	}


	public int count(ProductDataCriteria productDataCriteria) throws DataAccessException {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("ProductData.count", productDataCriteria);
	}


	@Override
	public List<Integer> listProductIdByCriteria(
			ProductDataCriteria productDataCriteria) {
		return getSqlSessionTemplate().selectList("ProductData.listProductIdByCriteria", productDataCriteria);
	}

}
