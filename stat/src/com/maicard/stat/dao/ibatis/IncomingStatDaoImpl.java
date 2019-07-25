package com.maicard.stat.dao.ibatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.dao.IncomingStatDao;
import com.maicard.stat.domain.IncomingStat;

@Repository
public class IncomingStatDaoImpl extends BaseDao implements IncomingStatDao {


	@Override
	public List<IncomingStat> incomingStat(PayStatCriteria payStatCriteria) {
		getSqlSessionTemplate().selectOne("IncomingStat.iuda",payStatCriteria);
		return getSqlSessionTemplate().selectList("IncomingStat.incomingStat",payStatCriteria);
	}


}

