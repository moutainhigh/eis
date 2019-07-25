package com.maicard.aspect.common;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.common.base.DataBaseContextHolder;
import com.maicard.common.service.ConfigService;
import com.maicard.standard.DataName;
import com.maicard.standard.DataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 根据业务需要动态注入不同的数据源
 * @author NetSnake
 * @date 2013-3-2
 */
@Aspect
public class DynamicDataSourceAspect extends BaseService {

	@Resource
	private ConfigService configService;

	boolean useSplitUserDbSource = false;
	boolean useSplitLogDbSource = false;




	@PostConstruct
	public void init(){
		useSplitUserDbSource = configService.getBooleanValue(DataName.useSplitUserDbSource.toString(),0);
		useSplitLogDbSource = configService.getBooleanValue(DataName.useSplitLogDbSource.toString(),0);

	}

	@Before("execution(* com.maicard..*.dao.ibatis.*.*(..)) || execution(* com.maicard..*.dao.mybatis.*.*(..)) || execution(* com.maicard..*.dao.mapper..*.*(..)) ")
	public void checkDbSource(JoinPoint joinPoint) throws Throwable{
		/*if(joinPoint.getTarget().getClass().getName().endsWith("ItemLogDaoImpl")
				|| joinPoint.getTarget().getClass().getName().endsWith("MoneyLogDaoImpl")){
			if(useSplitLogDbSource){
				if(logger.isDebugEnabled()){
					logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的数据源是:" + DataBaseContextHolder.getDbSource() + ",将其设置为日志数据源");
				}
				setLogDataSource();
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的数据源是:" + DataBaseContextHolder.getDbSource() + ",将其设置为普通数据源");
				}
				setNormalDataSource();
			}
			return;
		} else	if(joinPoint.getTarget().getClass().getName().startsWith("com.maicard.security.dao.ibatis") 
				|| joinPoint.getTarget().getClass().getName().startsWith("com.maicard.common.dao.ibatis.GlobalUniqueDaoImpl")
				|| joinPoint.getTarget().getClass().getName().equals("com.maicard.stat.dao.ibatis.UserIpStatDaoImpl")
				){
			if(useSplitUserDbSource){
				//logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的数据源是:" + DataBaseContextHolder.getDbSource() + ",将其设置为用户数据源");
				setUserDataSource();
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的数据源是:" + DataBaseContextHolder.getDbSource() + ",将其设置为日志数据源");
				}
				setNormalDataSource();
			}
			return;
		} else	*/

		final String moneyAccountDaoPrefix = "com.maicard.account.dao";
		boolean moneyDs = false;
		if(joinPoint.getTarget().getClass().getName().startsWith(moneyAccountDaoPrefix)){
			moneyDs = true;
		} else {
			Class<?>[] interfaces = joinPoint.getTarget().getClass().getInterfaces();
			if(interfaces != null && interfaces.length > 0){
				for(Class<?> clazz : interfaces){
					if(clazz.getName().startsWith(moneyAccountDaoPrefix)){
						moneyDs = true;
						break;
					}
					//logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的接口是:" + clazz.getName());
				}
			}
		}
		//logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的实际类是:" + ClassUtils.getUserClass(joinPoint.getTarget()));
		/*joinPoint.getTarget().getClass().getName().startsWith("com.maicard.money.dao") 
				|| */
				
		if(moneyDs){			
			if(logger.isDebugEnabled()){
				logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的数据源是:" + DataBaseContextHolder.getDbSource() + ",将其设置为资金数据源");
			}
			setMoneyDataSource();
			return;
		}else {
			if(logger.isDebugEnabled()){
				logger.debug("当前[" + joinPoint.getTarget().getClass().getName() + "]的数据源是:" + DataBaseContextHolder.getDbSource() + ",将其设置为普通数据源");
			}
			setNormalDataSource();
		}
	}
	private void setNormalDataSource() {
		if(logger.isDebugEnabled()){
			logger.debug("将数据源设置为:" + DataSource.normal.toString() + "DataSource");
		}
		DataBaseContextHolder.setDbSource(DataSource.normal.toString() + "DataSource");		
	}

	/*private void setUserDataSource() {
		if(logger.isDebugEnabled()){
			logger.debug("将数据源设置为:" + DataSource.user.toString() + "DataSource");
		}
		DataBaseContextHolder.setDbSource(DataSource.user.toString() + "DataSource");		
	}*/

	private void setMoneyDataSource() {
		if(logger.isDebugEnabled()){
			logger.debug("将数据源设置为:" + DataSource.money.toString() + "DataSource");
		}
		DataBaseContextHolder.setDbSource(DataSource.money.toString() + "DataSource");		
	}

	/*
	private void setLogDataSource(){
		if(logger.isDebugEnabled()){
			logger.debug("将数据源设置为:" + DataSource.log.toString()  + "DataSource");
		}
		DataBaseContextHolder.setDbSource(DataSource.log.toString() + "DataSource");	
	}*/

}
