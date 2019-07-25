package com.maicard.security.dao.ibatis;

import java.util.Date;
import java.util.List;


import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserOnlineLogCriteria;
import com.maicard.security.dao.UserOnlineLogDao;
import com.maicard.security.domain.UserOnlineLog;

@Repository
public class UserOnlineLogDaoImpl extends BaseDao implements UserOnlineLogDao {


	public int insert(UserOnlineLog userOnlineLog) throws DataAccessException {
		int rs =getSqlSessionTemplate().insert("UserOnlineLog.insert", userOnlineLog);
		return rs;
	}

	public int update(UserOnlineLog userOnlineLog) throws DataAccessException {
		int rs = getSqlSessionTemplate().update("UserOnlineLog.update", userOnlineLog);
		
		return rs;

	}

	public int delete(int userOnlineLogId) throws DataAccessException {


		return getSqlSessionTemplate().delete("UserOnlineLog.delete", new Integer(userOnlineLogId));


	}

	public UserOnlineLog select(int userOnlineLogId) throws DataAccessException {
		return (UserOnlineLog) getSqlSessionTemplate().selectOne("UserOnlineLog.select", new Integer(userOnlineLogId));
	}


	public List<UserOnlineLog> list(UserOnlineLogCriteria userOnlineLogCriteria) throws DataAccessException {
		Assert.notNull(userOnlineLogCriteria, "userOnlineLogCriteria must not be null");

		return getSqlSessionTemplate().selectList("UserOnlineLog.list", userOnlineLogCriteria);
	}


	public List<UserOnlineLog> listOnPage(UserOnlineLogCriteria userOnlineLogCriteria) throws DataAccessException {
		Assert.notNull(userOnlineLogCriteria, "userOnlineLogCriteria must not be null");
		Assert.notNull(userOnlineLogCriteria.getPaging(), "paging must not be null");

		int totalResults = count(userOnlineLogCriteria);
		Paging paging = userOnlineLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("UserOnlineLog.list", userOnlineLogCriteria, rowBounds);
	}

	public int count(UserOnlineLogCriteria userOnlineLogCriteria) throws DataAccessException {
		Assert.notNull(userOnlineLogCriteria, "userOnlineLogCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("UserOnlineLog.count", userOnlineLogCriteria)).intValue();
	}
	
	
	@Override
	public int updateSameUserAndOnlineTimeLog(UserOnlineLog userOnlineLog) {
		return getSqlSessionTemplate().update("UserOnlineLog.updateSameUserAndOnlineTimeLog", userOnlineLog);
	}

	@Override
	public UserOnlineLog getLastOnlineLog(long uuid) {
		return getSqlSessionTemplate().selectOne("UserOnlineLog.getLastOnlineLog", uuid);
	}

	@Override
	public long getTotalOnlineTime(UserOnlineLogCriteria userOnlineLogCriteria) {
		Assert.notNull(userOnlineLogCriteria, "userOnlineLogCriteria must not be null");
		if(userOnlineLogCriteria.getQueryBeginTime() == null){
			logger.debug("未设置查询起始时间，设置为24小时前");
			userOnlineLogCriteria.setQueryBeginTime(DateUtils.addHours(new Date(), -24));
		}
		try{
			return getSqlSessionTemplate().selectOne("UserOnlineLog.getTotalOnlineTime", userOnlineLogCriteria);
		}catch(Exception e){
			//e.printStackTrace();
		}
		return 0;
	}

	

}
