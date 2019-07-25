package com.maicard.security.dao.ibatis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.security.criteria.OperateLogCriteria;
import com.maicard.security.dao.OperateLogDao;
import com.maicard.security.domain.OperateLog;
import com.maicard.standard.TablePartitionPolicy;

@Repository
public class OperateLogDaoImpl extends BaseDao implements OperateLogDao {

	private final String 	 defaultTableName = "operate_log";
	private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");


	public int insert(OperateLog operateLog)  throws Exception{
		getTableName(operateLog);
		return getSqlSessionTemplate().insert("com.maicard.security.sql.OperateLog.insert", operateLog);
	}

	public int update(OperateLog operateLog)  throws Exception {
		getTableName(operateLog);
		return getSqlSessionTemplate().update("com.maicard.security.sql.OperateLog.update", operateLog);
	}


	public List<OperateLog> list(OperateLogCriteria operateLogCriteria) throws Exception {
		Assert.notNull(operateLogCriteria, "operateLogCriteria must not be null");		
		getTableName(operateLogCriteria);
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.OperateLog.list", operateLogCriteria);
	}

	public List<OperateLog> listOnPage(OperateLogCriteria operateLogCriteria) throws Exception {
		Assert.notNull(operateLogCriteria, "operateLogCriteria must not be null");
		Assert.notNull(operateLogCriteria.getPaging(), "paging must not be null");
		getTableName(operateLogCriteria);
		int totalResults = count(operateLogCriteria);
		Paging paging = operateLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.security.sql.OperateLog.list", operateLogCriteria, rowBounds);
	}

	public int count(OperateLogCriteria operateLogCriteria) throws Exception {
		Assert.notNull(operateLogCriteria, "operateLogCriteria must not be null");
		getTableName(operateLogCriteria);
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.OperateLog.count", operateLogCriteria);
	}

	//得到最频繁操作的业务服务器ID
	@Override
	public int getFrequentObjectId (
			OperateLogCriteria operateLogCriteria) throws Exception{
		Assert.notNull(operateLogCriteria, "operateLogCriteria must not be null");
		getTableName(operateLogCriteria);
		try{
			Integer rs = getSqlSessionTemplate().selectOne("com.maicard.security.sql.OperateLog.getFrequentObjectId", operateLogCriteria);
			if(rs == null){
				return 0;
			}
			return rs;
		}catch(Exception e){
			logger.error("处理SQL时错误:" + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	//得到最后一次操作的业务服务器
	public int getRecentObjectId(
			OperateLogCriteria operateLogCriteria)  throws Exception{
		Assert.notNull(operateLogCriteria, "operateLogCriteria must not be null");
		getTableName(operateLogCriteria);
		try{
			Integer rs = getSqlSessionTemplate().selectOne("com.maicard.security.sql.OperateLog.getRecentObjectId", operateLogCriteria);
			if(rs == null){
				return 0;
			}
			return rs;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	private void getTableName(OperateLog operateLog){
		if(operateLog == null){
			throw new RequiredObjectIsNullException("operateLog为空");
		}
		if(operateLog.getOperateTime() == null){
			operateLog.setOperateTime(new Date());
		}
		if(TablePartitionPolicy.operateLog.getName() != null && !TablePartitionPolicy.operateLog.getName().equals("")){
			operateLog.setTableName(defaultTableName + "_" + new SimpleDateFormat(TablePartitionPolicy.operateLog.getName()).format(operateLog.getOperateTime()));
		} else {
			operateLog.setTableName(defaultTableName);		
		}
	}

	private void getTableName(OperateLogCriteria operateLogCriteria){
		if(operateLogCriteria == null){
			throw new RequiredObjectIsNullException("operateLogCriteria为空");
		}
		if(operateLogCriteria.getBeginTime() == null || operateLogCriteria.getBeginTime().equals("")){
			operateLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
			logger.debug("未指定设置查询开始时间，强制指定为:" + operateLogCriteria.getBeginTime());
		}
		if(TablePartitionPolicy.operateLog.getName() != null && !TablePartitionPolicy.operateLog.getName().equals("")){
			
			operateLogCriteria.setTableName(defaultTableName + "_" + fmt.format(operateLogCriteria.getBeginTime()).substring(5, 7));
		} else {
			operateLogCriteria.setTableName(defaultTableName);		
		}

		

	}

	@Override
	public int clearOldLog(OperateLogCriteria operateLogCriteria) {
		return getSqlSessionTemplate().delete("com.maicard.security.sql.OperateLog.clearOldLog", operateLogCriteria);
	}

	@Override
	public long findPeriod(OperateLogCriteria operateLogCriteria) {
		Assert.notNull(operateLogCriteria, "operateLogCriteria must not be null");
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.OperateLog.findPeriod", operateLogCriteria);
	}

}
