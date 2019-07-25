package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ProductPartnerRelationCriteria;
import com.maicard.product.dao.ProductPartnerRelationDao;
import com.maicard.product.domain.ProductPartnerRelation;

@Repository
public class ProductPartnerRelationDaoImpl extends BaseDao implements ProductPartnerRelationDao {

	public int insert(ProductPartnerRelation productPartnerRelation) throws DataAccessException {
		return getSqlSessionTemplate().insert("ProductPartnerRelation.insert", productPartnerRelation);
	}

	public int update(ProductPartnerRelation productPartnerRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("ProductPartnerRelation.update", productPartnerRelation);
	}

	public int delete(int productPartnerRelationId) throws DataAccessException {
		return getSqlSessionTemplate().delete("ProductPartnerRelation.delete", new Integer(productPartnerRelationId));
	}

	public ProductPartnerRelation select(int productPartnerRelationId) throws DataAccessException {
		return (ProductPartnerRelation) getSqlSessionTemplate().selectOne("ProductPartnerRelation.select", new Integer(productPartnerRelationId));
	}

	public List<ProductPartnerRelation> list(ProductPartnerRelationCriteria productPartnerRelationCriteria) throws DataAccessException {
		Assert.notNull(productPartnerRelationCriteria, "productPartnerRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("ProductPartnerRelation.list", productPartnerRelationCriteria);
	}

	public List<ProductPartnerRelation> listOnPage(ProductPartnerRelationCriteria productPartnerRelationCriteria) throws DataAccessException {
		Assert.notNull(productPartnerRelationCriteria, "productPartnerRelationCriteria must not be null");
		Assert.notNull(productPartnerRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(productPartnerRelationCriteria);
		Paging paging = productPartnerRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("ProductPartnerRelation.list", productPartnerRelationCriteria, rowBounds);
	}

	public int count(ProductPartnerRelationCriteria productPartnerRelationCriteria) throws DataAccessException {
		Assert.notNull(productPartnerRelationCriteria, "productPartnerRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("ProductPartnerRelation.count", productPartnerRelationCriteria);
	}

}
