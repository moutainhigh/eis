package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.LocationCriteria;
import com.maicard.common.dao.LocationDao;
import com.maicard.common.domain.Location;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredAttributeIsNullException;

@Repository
public class LocationDaoImpl extends BaseDao implements LocationDao {


	public int insert(Location location) throws Exception {
		if(location == null){
			return 0;
		}
		return (Integer)getSqlSessionTemplate().insert("Location.insert", location);
	}

	public int update(Location location) throws Exception {
		if(location == null){
			return 0;
		}
		int rs = getSqlSessionTemplate().update("Location.update", location);
		return rs;


	}


	public Location select(LocationCriteria locationCriteria) throws Exception {
		Assert.notNull(locationCriteria, "userConfigCriteria must not be null");
		//Assert.notNull(locationCriteria.getLocationId(), "userConfigCriteria config id must not be null");
		return (Location) getSqlSessionTemplate().selectOne("Location.select", locationCriteria);
	}


	public List<Location> list(LocationCriteria locationCriteria) throws Exception {
		Assert.notNull(locationCriteria, "userConfigCriteria must not be null");
		//logger.debug("查找数据表:" + locationCriteria.getTableName());
		return getSqlSessionTemplate().selectList("Location.list", locationCriteria);
	}


	public List<Location> listOnPage(LocationCriteria locationCriteria) throws Exception {
		Assert.notNull(locationCriteria, "userConfigCriteria must not be null");
		Assert.notNull(locationCriteria.getPaging(), "paging must not be null");

		int totalResults = count(locationCriteria);
		Paging paging = locationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("Location.list", locationCriteria, rowBounds);
	}

	public int count(LocationCriteria locationCriteria) throws Exception {
		Assert.notNull(locationCriteria, "userConfigCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("Location.count", locationCriteria)).intValue();
	}


	@Override
	public int delete(LocationCriteria locationCriteria) {
		Assert.notNull(locationCriteria, "userConfigCriteria must not be null.");
		if(StringUtils.isBlank(locationCriteria.getObjectType()) && locationCriteria.getObjectId() == 0){
			throw new RequiredAttributeIsNullException("尝试删除location，但未指定删除对象类型或ID");
		}		
		List<Integer> idList = getSqlSessionTemplate().selectList("Location.listPk", locationCriteria);
		int rs = 0;
		if(idList == null || idList.size() < 1){
		} else {
			SqlSession session = getSqlSessionTemplate().getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			try{
				for(Integer id : idList){
					session.delete("Location.delete", id);
					rs++;
				}
			}catch(Exception e){
				logger.error("在批量删除用户数据时失败:" + e.getMessage());
				e.printStackTrace();
			}
			session.commit();
			session.close();
			session = null;
			idList = null;
		}
		if(logger.isDebugEnabled()){
			logger.debug("根据条件将要删除的用户数据有[" + (idList == null ? "-1" : idList.size()) + "]个,实际删除数据有[" + rs + "]个");
		}
		return rs;
	}

	@Override
	public int updateBatch(List<Location> flushLocationList) {
		if(flushLocationList == null || flushLocationList.size() < 1){
			logger.debug("批量执行的位置信息为空");
			return 0;
		}
		SqlSession session = getSqlSessionTemplate().getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		int count = 0;
		try{
			for(Location location : flushLocationList){
				if(StringUtils.isBlank(location.getObjectType()) ||  location.getObjectId() < 1){
					logger.warn("位置信息[" + location + "]没有objectType或objectId，将不进行更新");
					continue;
				}
				if(session.update("Location.update", location) == 1){
					count++;
				}
			}
		}catch(Exception e){
			logger.error("在批量更新位置信息时失败:" + e.getMessage());
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("共" + flushLocationList.size() + "条位置数据需更新，更新了" + count + "条");
		}
		session.commit();
		session.close();
		session = null;
		return count;
	}





}
