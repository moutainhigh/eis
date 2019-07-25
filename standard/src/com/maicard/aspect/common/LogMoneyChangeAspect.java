package com.maicard.aspect.common;


import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyLogService;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 对资金变化进行记录
 * 
 * 
 * @author NetSnake 
 * @date 2013-6-23
 */
@Aspect
public class LogMoneyChangeAspect extends BaseService{
	
	/*@Resource
	private ConfigService configService;*/
	@Resource
	private MoneyLogService moneyLogService;	
	
	/*private boolean handlerMoney;
	
	private boolean logMoneyChangeOnBothNode;
	
	@PostConstruct
	public void init(){
		handlerMoney = configService.getBooleanValue(DataName.handlerMoney.toString(),0);
		logMoneyChangeOnBothNode = configService.getBooleanValue(DataName.LOG_MONEY_CHANGE_ON_BOTH_NODE.toString(), 0);
	}*/
	


	@Around("execution(public com.maicard.common.domain.EisMessage com.maicard.money.service.impl.MoneyServiceImpl.*(..))")
	//@Around("execution(public int com.maicard.money.dao.ibatis.MoneyDaoImpl.*(..))")
	public Object doAroundForMoneyChange(ProceedingJoinPoint joinPoint) throws Throwable{
		/*if(!handlerMoney && !logMoneyChangeOnBothNode){
			return joinPoint.proceed();
		}*/
		if(logger.isDebugEnabled())logger.debug("切入资金服务器资金变化：" + joinPoint.getTarget().getClass().getSimpleName() + ",method:" +joinPoint.getSignature().getName());
		String method = joinPoint.getSignature().getName();
		/*logger.debug("有[" + joinPoint.getArgs().length + "]个参数");
		if(joinPoint.getArgs().length > 0){
			for(Object obj : joinPoint.getArgs()){
				logger.debug(">>>>>>>>>>>" + obj.getClass().getName());
			}
		}*/
		Money m0 = null;
		try{
			m0 = (Money)joinPoint.getArgs()[0];
		}catch(Exception e){
			//e.printStackTrace();
		}
		if(m0 == null){
			//logger.error("joinPoint中第一个参数不是Money类型");
			return joinPoint.proceed();
		}
		Money moneyBefore = m0.clone();
		Object result =  joinPoint.proceed();
		if(result == null){
			return joinPoint.proceed();
		}
		if(!(result instanceof EisMessage)){
			return joinPoint.proceed();
		} 
		//执行完成后，money对象已发生改变
		Money moneyAfter = m0.clone();
		
		EisMessage rs = (EisMessage)result;
		logger.debug("资金服务返回代码是:" + rs);
		if(rs != null && rs.getOperateCode() == OperateResult.success.id){
			//进行日志记录	
			String op = null;
			String memory = null;
			if(method.equals("plus") || method.equalsIgnoreCase("charge")){//增加资金
				op =Operate.plus.code;
				memory = Operate.plus.getName();
			} else if(method.equals("minus")){//扣除资金
				op = Operate.minus.code;
				memory = Operate.minus.getName();
			} else if(method.equals("lock")){//锁定资金
				op = Operate.lock.code;
				memory = Operate.lock.getName();
			} else if(method.equals("unLock")){//解锁资金
				op = Operate.unLock.code;
				memory = Operate.unLock.getName();
			} 
			if(StringUtils.isNotBlank(moneyBefore.getMemo())){
				//如果当前资金对象设置了备注，则使用该备注作为说明
				memory = moneyBefore.getMemo();
			}

			logger.debug("新增资金变动记录[" + memory + "/" + op + "]");
			moneyLogService.insert(op,memory,moneyBefore, moneyAfter);
		}		
		return result;
	}
}
