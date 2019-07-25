package com.maicard.mb.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.EisTopicCriteria;
import com.maicard.mb.dao.EisTopicDao;
import com.maicard.mb.domain.EisTopic;

@Repository
public class EisTopicDaoImpl extends BaseDao implements EisTopicDao {

	public int insert(EisTopic eisTopic) throws DataAccessException {
		int ugid = Integer.parseInt("" + getSqlSessionTemplate().insert("EisTopic.insert", eisTopic));
		return ugid;

	}

	public int update(EisTopic eisTopic) throws DataAccessException {
		return getSqlSessionTemplate().update("EisTopic.update", eisTopic);
	}

	public int delete(int ugid) throws DataAccessException {
		return getSqlSessionTemplate().delete("EisTopic.delete", new Integer(ugid));
	}

	

	public EisTopic select(int ugid) throws DataAccessException {
		return (EisTopic) getSqlSessionTemplate().selectOne("EisTopic.select", new Integer(ugid));
	}

	public List<EisTopic> list(EisTopicCriteria topicCriteria) throws DataAccessException {
		Assert.notNull(topicCriteria, "topicCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("EisTopic.list", topicCriteria);
	}

	public List<EisTopic> listOnPage(EisTopicCriteria topicCriteria) throws DataAccessException {
		Assert.notNull(topicCriteria, "topicCriteria must not be null");
		Assert.notNull(topicCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(topicCriteria);
		Paging paging = topicCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("EisTopic.list", topicCriteria, rowBounds);
	}
	
	public int count(EisTopicCriteria topicCriteria) throws DataAccessException {
		Assert.notNull(topicCriteria, "topicCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("EisTopic.count", topicCriteria)).intValue();
	}
}
