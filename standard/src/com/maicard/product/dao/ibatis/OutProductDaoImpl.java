package com.maicard.product.dao.ibatis;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.product.criteria.OutProductCriteria;
import com.maicard.product.dao.OutProductDao;
import com.maicard.product.domain.OutProduct;

@Repository
public class OutProductDaoImpl extends BaseDao implements OutProductDao {


	public List<OutProduct> list(OutProductCriteria outProductCriteria) throws DataAccessException {
		Assert.notNull(outProductCriteria, "outProductCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.OutProduct.list", outProductCriteria);
	}
	
}
