package com.maicard.wpt.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.wpt.criteria.EntityShopCriteria;
import com.maicard.wpt.dao.EntityShopDao;
import com.maicard.wpt.domain.EntityShop;

public class EntityShopDaoImpl extends BaseDao implements EntityShopDao {
	@Override
	public int insert(EntityShop entityShop) throws DataAccessException {
		return getSqlSessionTemplate().insert("EntityShop.insert", entityShop);
	}

	@Override
	public int update(EntityShop entityShop) throws DataAccessException {
		return getSqlSessionTemplate().update("EntityShop.update", entityShop);
	}

	@Override
	public int delete(long entityShopId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Mapbox.delete", entityShopId);
	}

	@Override
	public EntityShop select(long entityShopId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("EntityShop.select",entityShopId);
	}

	@Override
	public List<EntityShop> list(EntityShopCriteria entityShopCriteria) throws DataAccessException {
		Assert.notNull(entityShopCriteria, "entityShopCriteria must not be null");
		return getSqlSessionTemplate().selectList("EntityShop.list", entityShopCriteria);
	}

	@Override
	public List<EntityShop> listOnPage(EntityShopCriteria entityShopCriteria) throws DataAccessException {
		Assert.notNull(entityShopCriteria, "entityShopCriteria must not be null");
		Assert.notNull(entityShopCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(entityShopCriteria);
		Paging paging = entityShopCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("EntityShop.list", entityShopCriteria, rowBounds);
	}

	@Override
	public int count(EntityShopCriteria entityShopCriteria) throws DataAccessException {
		Assert.notNull(entityShopCriteria, "entityShopCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("EntityShop.count",entityShopCriteria)).intValue();
	}
}
