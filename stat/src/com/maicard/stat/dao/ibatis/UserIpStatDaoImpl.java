package com.maicard.stat.dao.ibatis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;
import com.maicard.stat.criteria.UserIpStatCriteria;
import com.maicard.stat.dao.UserIpStatDao;
import com.maicard.stat.domain.UserIpStat;


@Repository
public class UserIpStatDaoImpl  extends BaseDao implements UserIpStatDao {
	
	
	@Override
	public List<UserIpStat> listOnPage(UserIpStatCriteria statCriteria) throws Exception {
		Assert.notNull(statCriteria, "frontUserUserIpStatCriteria must not be null");
		Date date = new Date();   
        SimpleDateFormat sf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);   
        String nowDate = sf.format(date);   
       

        Calendar cal = Calendar.getInstance();   
        cal.setTime(sf.parse(nowDate));   
        cal.add(Calendar.DAY_OF_YEAR, +1);   
       // String nextDate_1 = sf.format(cal.getTime());   
   
        //通过秒获取下一天日期   
        long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒   
        date.setTime(time * 1000);//毫秒   
        String nextDate_2 = sf.format(date).toString();   
     


		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals("")){
			statCriteria.setBeginTime(nowDate); 
		}
		if(statCriteria.getEndTime() == null || statCriteria.getEndTime().equals("")){
			statCriteria.setEndTime(nextDate_2); 
		}
		
		int totalResults = count(statCriteria);
		Paging paging = statCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		
		if(statCriteria.getMode()!="0"){
			
			return getSqlSessionTemplate().selectList("UserIpStat.listByIp", statCriteria, rowBounds);
			
		}else{
			
			return getSqlSessionTemplate().selectList("UserIpStat.listByIpAll", statCriteria, rowBounds);
		}
		
		

	}
	
	@Override
	public int count(UserIpStatCriteria statCriteria) throws Exception {
		
		Date date = new Date();   
        SimpleDateFormat sf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);   
        String nowDate = sf.format(date);   
      
        Calendar cal = Calendar.getInstance();   
        cal.setTime(sf.parse(nowDate));   
        cal.add(Calendar.DAY_OF_YEAR, +1);   
    //    String nextDate_1 = sf.format(cal.getTime());   
   
        //通过秒获取下一天日期   
        long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒   
        date.setTime(time * 1000);//毫秒   
        String nextDate_2 = sf.format(date).toString();   
      

		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals("")){
			statCriteria.setBeginTime(nowDate); 
		}
		if(statCriteria.getEndTime() == null || statCriteria.getEndTime().equals("")){
			statCriteria.setEndTime(nextDate_2); 
		}
if(statCriteria.getMode()!="0"){
			
			return getSqlSessionTemplate().selectOne("UserIpStat.byipcount", statCriteria);
			
		}else{
			
			return getSqlSessionTemplate().selectOne("UserIpStat.byipallcount", statCriteria);
		}

		
	}
	
}
