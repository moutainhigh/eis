package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.ConfigCriteria;
import com.maicard.common.dao.ConfigDao;
import com.maicard.common.domain.Config;
import com.maicard.common.util.Paging;

@Repository
public class ConfigDaoImpl extends BaseDao implements ConfigDao {
	

	public int insert(Config config) throws DataAccessException {
		return getSqlSessionTemplate().insert("Config.insert", config);
	}

	public int update(Config config) throws DataAccessException {
		return getSqlSessionTemplate().update("Config.update", config);
	}

	public int delete(int configId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Config.delete", new Integer(configId));
	}

	public Config select(int configId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("Config.select", new Integer(configId));
	}

	public List<Config> list(ConfigCriteria configCriteria) throws DataAccessException {
		Assert.notNull(configCriteria, "configCriteria must not be null");		
		return getSqlSessionTemplate().selectList("Config.list", configCriteria);
	}

	public List<Config> listOnPage(ConfigCriteria configCriteria) throws DataAccessException {
		Assert.notNull(configCriteria, "configCriteria must not be null");
		Assert.notNull(configCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(configCriteria);
		Paging paging = configCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("Config.list", configCriteria, rowBounds);
	}

	public int count(ConfigCriteria configCriteria) throws DataAccessException {
		Assert.notNull(configCriteria, "configCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("Config.count", configCriteria)).intValue();
	}

	@Override
	public Config selectByName(ConfigCriteria configCriteria ) {
		return getSqlSessionTemplate().selectOne("Config.selectByName", configCriteria);

	}

}
