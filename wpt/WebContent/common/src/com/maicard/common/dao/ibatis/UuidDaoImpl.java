package com.maicard.common.dao.ibatis;

import java.util.Calendar;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.UserUniqueCriteria;
import com.maicard.common.dao.UuidDao;
import com.maicard.common.domain.UserUnique;
import com.maicard.common.domain.Uuid;

@Repository
public class UuidDaoImpl extends BaseDao implements UuidDao {
	
	
	public long insert(Uuid uuid) throws DataAccessException {
		int rs = getSqlSessionTemplate().insert("Uuid.insert", uuid);
			
		if(rs == 1){
			long cleanUuid = uuid.getUuid() - 10;
			if(Calendar.getInstance().get(Calendar.MINUTE) % 5 == 0 && Calendar.getInstance().get(Calendar.SECOND) % 5 == 0 ){
				getSqlSessionTemplate().insert("Uuid.clean", cleanUuid);
			} 
			return uuid.getUuid();
		}
		return 0;
	}

	@Override
	public long getMaxIdForUser(UserUniqueCriteria userUniqueCriteria) {
		Assert.notNull(userUniqueCriteria,"获取用户最大唯一数的条件不能为空");
		Assert.isTrue(userUniqueCriteria.getOwnerId() > 0, "获取用户最大唯一数的ownerId条件不能为空");
		return 	getSqlSessionTemplate().selectOne("Uuid.getMaxIdForUser", userUniqueCriteria);

	}

	@Override
	public int insert(UserUnique userUniqueId) {
		return getSqlSessionTemplate().insert("Uuid.insertForUser", userUniqueId);
	}
	
	


}
