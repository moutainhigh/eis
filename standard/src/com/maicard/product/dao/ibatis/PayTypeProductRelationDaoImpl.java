package com.maicard.product.dao.ibatis;

import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.product.criteria.PayTypeProductRelationCriteria;
import com.maicard.product.dao.PayTypeProductRelationDao;

@Repository
public class PayTypeProductRelationDaoImpl extends BaseDao implements PayTypeProductRelationDao {

	@Override
	public Integer getProductIdByPayTypeId( PayTypeProductRelationCriteria payTypeProductRelationCriteria) {	
		return (Integer)getSqlSessionTemplate().selectOne("PayTypeProductRelation.selectProductIdByPayTypeId", payTypeProductRelationCriteria);
	}

}
