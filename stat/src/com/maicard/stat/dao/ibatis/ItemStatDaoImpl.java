package com.maicard.stat.dao.ibatis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.standard.CommonStandard;
import com.maicard.stat.criteria.ItemStatCriteria;
import com.maicard.stat.dao.ItemStatDao;
import com.maicard.stat.domain.ItemStat;

@Repository
public class ItemStatDaoImpl  extends BaseDao implements ItemStatDao {
	
	private final String defaultOrderBy = "stat_time DESC";
	private final String defaultTableName = "item";
	@Override
	public int gather(ItemStatCriteria statCriteria) throws Exception{
		Assert.notNull(statCriteria, "statCriteria must not be null");
		int rs = ((Integer)getSqlSessionTemplate().selectOne("ItemStat.gather", statCriteria)).intValue();
		return rs;
	}

	  @Override
	  public int change_Inviter(ItemStatCriteria statCriteria) throws Exception {
		  Assert.notNull(statCriteria, "frontUserItemStatCriteria must not be null");
		  return getSqlSessionTemplate().selectOne("ItemStat.changeInviter", statCriteria); 
	  }
	@Override
	public List<ItemStat> listOnPage(ItemStatCriteria statCriteria) throws Exception {
		Assert.notNull(statCriteria, "frontUserItemStatCriteria must not be null");
		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals("")){
			statCriteria.setBeginTime(  new SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addWeeks(new Date(),-1))); 
		}
		if(statCriteria.getOrderBy() == null){
			statCriteria.setOrderBy(defaultOrderBy);
		}
		
		int totalResults = count(statCriteria);
		Paging paging = statCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		
		if(statCriteria.getStatTimeMode() == null || statCriteria.getStatTimeMode().equals("day")){
			List<ItemStat> list = getSqlSessionTemplate().selectList("ItemStat.listByDay", statCriteria, rowBounds);
			/*for(int i = 0 ; i < list.size(); i++){
				logger.error("XXXXXXXXXXXXXXXXXXXXXX" + i + ":" + list.get(i).getStatTime());
			}*/
			return list;
		}
		else
		return getSqlSessionTemplate().selectList("ItemStat.listByHour", statCriteria, rowBounds);

	}
	@Override
	public List<ItemStat> listByInviter(ItemStatCriteria statCriteria) throws Exception {
		Assert.notNull(statCriteria, "frontUserItemStatCriteria must not be null");
		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals("")){
			statCriteria.setBeginTime(new SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addWeeks(new Date(),-1))); 
		}
		if(statCriteria.getOrderBy() == null){
			statCriteria.setOrderBy(defaultOrderBy);
		}
		
		int totalResults = countByInviter(statCriteria);
		Paging paging = statCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("ItemStat.listByInviter", statCriteria, rowBounds);

	}	
	@Override
	public int count(ItemStatCriteria statCriteria) throws Exception {
		if(statCriteria.getStatTimeMode() == null)
			return getSqlSessionTemplate().selectOne("ItemStat.count", statCriteria);
		else{
		  if (statCriteria.getStatTimeMode().equals("day"))
			return getSqlSessionTemplate().selectOne("ItemStat.bydaycount", statCriteria);			
		else
		  return getSqlSessionTemplate().selectOne("ItemStat.byhourcount", statCriteria);
		}
	}
	@Override
	public int countByInviter(ItemStatCriteria statCriteria) throws Exception {
		Assert.notNull(statCriteria, "frontUserItemStatCriteria must not be null");
		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals("")){
			statCriteria.setBeginTime(new SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addWeeks(new Date(),-1))+"00"); 
		}
		if(statCriteria.getOrderBy() == null){
			statCriteria.setOrderBy(defaultOrderBy);
		}
			
		return getSqlSessionTemplate().selectOne("ItemStat.countByInviter", statCriteria);
	}

	@Override
	public void moveToHistory(ItemCriteria itemCriteria) {
		if(itemCriteria == null){
			throw new RequiredAttributeIsNullException("未指定清理item的条件");
		}
		
		if(itemCriteria.getTableName() == null || itemCriteria.getTableName().equals(defaultTableName)){
			throw new RequiredAttributeIsNullException("清理item的表名错误:" + itemCriteria.getTableName());
		}
		getSqlSessionTemplate().selectOne("ItemStat.moveToHistory",itemCriteria);			
	}

	@Override
	public float getJWCharge() {
		return 	getSqlSessionTemplate().selectOne("ItemStat.getJWCharge");			

	}

	@Override
	public List<Item> getJWShengdaTakeCard(ItemStatCriteria statCriteria) {
		
		return 	getSqlSessionTemplate().selectList("ItemStat.getJWShengdaTakeCard",statCriteria);			
	}


	/*

	class Sort implements Comparator<ItemStat>{

		@Override
		public int compare(ItemStat item1, ItemStat item2) {
			//logger.info("Compare:" + item1.getStatTime() + " ====" + item2.getStatTime());
			try{
				DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH");
				if(df.parse(item1.getStatTime()).getTime() > df.parse(item2.getStatTime()).getTime()){
					return -1;
				} else {
					return 1;
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			return 1;
		}

	}*/



}
