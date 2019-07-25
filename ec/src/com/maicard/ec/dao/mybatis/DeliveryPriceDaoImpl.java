package com.maicard.ec.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;





import com.maicard.ec.criteria.DeliveryPriceCriteria;
import com.maicard.ec.dao.DeliveryPriceDao;
import com.maicard.ec.domain.DeliveryPrice;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class DeliveryPriceDaoImpl extends BaseDao implements DeliveryPriceDao {


	@Override
	public int insert(DeliveryPrice deliveryPrice) {
		return 	 getSqlSessionTemplate().insert("com.maicard.ec.sql.DeliveryPrice.insert", deliveryPrice);
	}

	@Override
	public int update(DeliveryPrice deliveryPrice) {
		return getSqlSessionTemplate().update("com.maicard.ec.sql.DeliveryPrice.update", deliveryPrice);
	}

	@Override
	public int delete(long deliveryPriceId) {
		return getSqlSessionTemplate().delete("com.maicard.ec.sql.DeliveryPrice.delete", deliveryPriceId);
	}

	@Override
	public List<DeliveryPrice> list(DeliveryPriceCriteria deliveryPriceCriteria) {
		Assert.notNull(deliveryPriceCriteria, "deliveryPriceCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.ec.sql.DeliveryPrice.list", deliveryPriceCriteria);	
	}

	@Override
	public List<DeliveryPrice> listOnPage(DeliveryPriceCriteria deliveryPriceCriteria) {
		Assert.notNull(deliveryPriceCriteria, "deliveryPriceCriteria must not be null");
		Assert.notNull(deliveryPriceCriteria.getPaging(), "paging must not be null");

		int totalResults = count(deliveryPriceCriteria);
		Paging paging = deliveryPriceCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.ec.sql.DeliveryPrice.list", deliveryPriceCriteria, rowBounds);
	}

	@Override
	public int count(DeliveryPriceCriteria deliveryPriceCriteria) {
		Assert.notNull(deliveryPriceCriteria, "deliveryPriceCriteria must not be null");		
		return getSqlSessionTemplate().selectOne("com.maicard.ec.sql.DeliveryPrice.count", deliveryPriceCriteria);	
	}

	@Override
	public DeliveryPrice select(long deliveryPriceId) {
		return getSqlSessionTemplate().selectOne("com.maicard.ec.sql.DeliveryPrice.select", deliveryPriceId);	
	}



}
