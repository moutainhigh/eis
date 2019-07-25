package com.maicard.ec.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;





import com.maicard.ec.criteria.DeliveryOrderCriteria;
import com.maicard.ec.dao.DeliveryOrderDao;
import com.maicard.ec.domain.DeliveryOrder;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class DeliveryOrderDaoImpl extends BaseDao implements DeliveryOrderDao {


	@Override
	public int insert(DeliveryOrder deliveryOrder) {
		return 	 getSqlSessionTemplate().insert("DeliveryOrder.insert", deliveryOrder);
	}

	@Override
	public int update(DeliveryOrder deliveryOrder) {
		return getSqlSessionTemplate().update("DeliveryOrder.update", deliveryOrder);
	}

	@Override
	public int delete(long deliveryOrderId) {
		return getSqlSessionTemplate().delete("DeliveryOrder.delete", deliveryOrderId);
	}

	@Override
	public List<DeliveryOrder> list(DeliveryOrderCriteria deliveryOrderCriteria) {
		Assert.notNull(deliveryOrderCriteria, "deliveryOrderCriteria must not be null");		
		return getSqlSessionTemplate().selectList("DeliveryOrder.list", deliveryOrderCriteria);	
	}

	@Override
	public List<DeliveryOrder> listOnPage(DeliveryOrderCriteria deliveryOrderCriteria) {
		Assert.notNull(deliveryOrderCriteria, "deliveryOrderCriteria must not be null");
		Assert.notNull(deliveryOrderCriteria.getPaging(), "paging must not be null");

		int totalResults = count(deliveryOrderCriteria);
		Paging paging = deliveryOrderCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("DeliveryOrder.list", deliveryOrderCriteria, rowBounds);
	}

	@Override
	public int count(DeliveryOrderCriteria deliveryOrderCriteria) {
		Assert.notNull(deliveryOrderCriteria, "deliveryOrderCriteria must not be null");		
		return getSqlSessionTemplate().selectOne("DeliveryOrder.count", deliveryOrderCriteria);	
	}

	@Override
	public DeliveryOrder select(long deliveryOrderId) {
		return getSqlSessionTemplate().selectOne("DeliveryOrder.select", deliveryOrderId);	
	}



}
