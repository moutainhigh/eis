package com.maicard.common.dao.ibatis;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.QUserInfoCriteria;
import com.maicard.common.dao.QUserInfoDao;
import com.maicard.common.domain.QUserInfo;
import com.maicard.common.util.Paging;

@Repository
public class QUserInfoDaoImpl extends BaseDao implements QUserInfoDao {


	public int insert(QUserInfo qUserInfo)  throws Exception{
		return getSqlSessionTemplate().insert("QUserInfo.insert", qUserInfo);
	}

	public int update(QUserInfo qUserInfo)  throws Exception {
		return getSqlSessionTemplate().update("QUserInfo.update", qUserInfo);
	}


	public List<QUserInfo> list(QUserInfoCriteria qUserInfoCriteria) throws Exception {
		Assert.notNull(qUserInfoCriteria, "qUserInfoCriteria must not be null");		
		return getSqlSessionTemplate().selectList("QUserInfo.list", qUserInfoCriteria);
	}

	public List<QUserInfo> listOnPage(QUserInfoCriteria qUserInfoCriteria) throws Exception {
		Assert.notNull(qUserInfoCriteria, "qUserInfoCriteria must not be null");
		Assert.notNull(qUserInfoCriteria.getPaging(), "paging must not be null");
		int totalResults = count(qUserInfoCriteria);
		Paging paging = qUserInfoCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("QUserInfo.list", qUserInfoCriteria, rowBounds);
	}

	public int count(QUserInfoCriteria qUserInfoCriteria) throws Exception {
		Assert.notNull(qUserInfoCriteria, "qUserInfoCriteria must not be null");
		return getSqlSessionTemplate().selectOne("QUserInfo.count", qUserInfoCriteria);
	}

	


}
