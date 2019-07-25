package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.dao.ProductDao;
import com.maicard.product.domain.Product;
import com.maicard.standard.CommonStandard;

@Repository
public class ProductDaoImpl extends BaseDao implements ProductDao {
	
	private final String cacheName = CommonStandard.cacheNameProduct;

	public int insert(Product product) throws DataAccessException {
		product.setProductCode(product.getProductCode().trim());
		return (Integer)getSqlSessionTemplate().insert("com.maicard.product.sql.Product.insert", product);
	}

	@CacheEvict(value = cacheName, key = "'Product#' + #product.productId")
	public int update(Product product) throws DataAccessException {
		product.setProductCode(product.getProductCode().trim());
		return getSqlSessionTemplate().update("com.maicard.product.sql.Product.update", product);
	}
	
	@Override
	@CacheEvict(value = cacheName, key = "'Product#' + #product.productId")
	public int updateNoNull(Product product) {
		return getSqlSessionTemplate().update("com.maicard.product.sql.Product.updateNoNull", product);

	}

	@CacheEvict(value = cacheName, key = "'Product#' + #productId")
	public int delete(long productId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.Product.delete", productId);
	}

	public Product select(long productId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库中获取产品[" + productId + "]");
		}
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Product.select", productId);
	}

	public List<Product> list(ProductCriteria productCriteria) throws DataAccessException {
		Assert.notNull(productCriteria, "productCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Product.list", productCriteria);
	}

	public List<Product> listOnPage(ProductCriteria productCriteria) throws DataAccessException {
		Assert.notNull(productCriteria, "productCriteria must not be null");
		Assert.notNull(productCriteria.getPaging(), "paging must not be null");

		int totalResults = count(productCriteria);
		Paging paging = productCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Product.list", productCriteria, rowBounds);
	}

	public int count(ProductCriteria productCriteria) throws DataAccessException {
		Assert.notNull(productCriteria, "productCriteria must not be null");

		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Product.count", productCriteria);
	}

	@Override
	public List<Long> listPkByNodeIds(ProductCriteria productCriteria) {
		Assert.notNull(productCriteria, "productCriteria must not be null");	
		Assert.notNull(productCriteria.getPaging(), "paging must not be null");	
		Paging paging = productCriteria.getPaging();
/*		if(productCriteria.getNodeIds() == null) { //其他视作取来自一个节点的文档
			int totalResults = count(productCriteria);
			paging.setTotalResults(totalResults);
			return getSession().selectList("Product.listPkByNodeId", productCriteria, paging.getFirstResult(), paging.getMaxResults());
			
		} else if(productCriteria.getNodeIds().length == 2){	//处理同时发布自两个节点的文档
			//logger.info("同时获取节点" + documentCriteria.getNodeIds()[0] + "," + documentCriteria.getNodeIds()[1] + "的内容");
			int totalResults = count(productCriteria);
			paging.setTotalResults(totalResults);
			return getSession().selectList("Product.listPkBy2NodeIds", productCriteria, paging.getFirstResult(), paging.getMaxResults());
		} 
		//productCriteria.setNodeId(productCriteria.getNodeIds()[0]);
*/		int totalResults = count(productCriteria);
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Product.listPkByNodeIds", productCriteria, rowBounds);
	}

	@Override
	public List<Long> listPk(ProductCriteria productCriteria) {
		Assert.notNull(productCriteria, "productCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Product.listPk", productCriteria);
	}

	@Override
	public List<Long> listPkOnPage(ProductCriteria productCriteria) {
		Assert.notNull(productCriteria, "productCriteria must not be null");
		Assert.notNull(productCriteria.getPaging(), "paging must not be null");

		int totalResults = count(productCriteria);
		Paging paging = productCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Product.listPk", productCriteria, rowBounds);
	}

	@Override
	public long getMaxId(ProductCriteria productCriteria)
			throws DataAccessException {
		Assert.notNull(productCriteria, "productCriteria must not be null");
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Product.getMaxId", productCriteria);
	}

	@Override
	public void updateAmount(Product product) {
		if(product == null){
			throw new RequiredObjectIsNullException("需要更新数量的产品是空");
		}
		if(product.getProductId() < 1){
			throw new RequiredObjectIsNullException("需要更新数量的产品ID是空");
		}
		if(product.getSoldCount() < 1){
			return;
		}
		getSqlSessionTemplate().update("com.maicard.product.sql.Product.updateAmount", product);
	}

	@Override
	@CacheEvict(value = cacheName, key = "'Product#' + #productId")
	public int forceDeleteAllAndRelation(long productId) {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.Product.forceDeleteAllAndRelation", productId);
	}





}
