package com.maicard.money.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.JsonUtils;
import com.maicard.money.criteria.MoneyLogCriteria;
import com.maicard.money.dao.MoneyLogDao;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.MoneyLog;
import com.maicard.money.service.MoneyLogService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.MoneyType;

@Service
public class MoneyLogServiceImpl extends BaseService implements MoneyLogService {

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private MoneyLogDao moneyLogDao;

	@Resource
	private PartnerService partnerService;

	@Resource
	private FrontUserService frontUserService;

	final String QUEUE_NAME = "MONEY_LOG";

	/**
	 * 保存REDIS缓存中的资金变化到数据库的任务是否已经执行
	 */
	static boolean saveTaskIsRunning = false;

	/**
	 * 保存REDIS缓存中的资金变化到数据库的任务每次休眠秒数
	 */
	static int saveTaskIdelSeconds = 5;


	public int insert(MoneyLog moneyLog) {
		if(moneyLog == null){
			logger.error("尝试插入的moneyLog为空");
			return -1;
		}
		if(moneyLog.getEnterTime() == null){
			moneyLog.setEnterTime(new Date());
		}


		//把该资金记录放到REDIS中，由某个负责日志入库的节点批量入库

		long score = new Date().getTime();

		if(logger.isDebugEnabled())logger.debug("向资金日志队列:" + QUEUE_NAME + "]放入新的资金日志，score=" + score);
		boolean addSuccess = centerDataService.addToZQueue(QUEUE_NAME, JsonUtils.toStringFull(moneyLog), score);
		if(addSuccess){
			return 1;
		} else {
			return 0;
		}
		/*try{
			return moneyLogDao.insert(moneyLog);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;*/
	}



	public List<MoneyLog> list(MoneyLogCriteria moneyLogCriteria) {
		return moneyLogDao.list(moneyLogCriteria);
	}

	public List<MoneyLog> listOnPage(MoneyLogCriteria moneyLogCriteria) {
		return moneyLogDao.listOnPage(moneyLogCriteria);
	}

	public int count(MoneyLogCriteria moneyLogCriteria) {
		return moneyLogDao.count(moneyLogCriteria);
	}



	@Override
	@Async
	public void insert(String op, String memory, Money moneyBefore, Money moneyAfter) {
		if(StringUtils.isBlank(op)){
			logger.error("请求记录日志的操作为空,无法新增资金日志");
			return;
		}
		if(moneyBefore == null){
			logger.error("请求记录日志的money对象为空");
			return;
		}
		long uuid = moneyBefore.getUuid();
		if(uuid == 0){
			logger.error("请求记录日志的money对象没有uuid");
			return;
		}

		//int userType = UserUtils.getUserType(uuid);
		/*User user = null;
		long inviter = 0;
		if(userType == UserTypes.partner.getId()){
			//这是一个商户
			user = partnerService.select(uuid);
			if(user != null){
				if(user.getInviter() > 0){
					inviter = user.getInviter();
				} else if(user.getParentUuid() > 0){
					inviter = user.getParentUuid();
				} else {
					inviter = user.getUuid();
				}
			}
		} else {
			user = frontUserService.select(uuid);
			if(user != null){
				if(user.getInviter() > 0){
					inviter = user.getInviter();
				} 
			}
		}*/




		BeanInfo bif = null;
		try {
			bif = Introspector.getBeanInfo(Money.class);

			PropertyDescriptor pds[] = bif.getPropertyDescriptors();
			for(PropertyDescriptor pd:pds){
				if(pd.getPropertyType().getName().equals("float")){
					float value = (Float)pd.getReadMethod().invoke(moneyBefore);
					if(value != 0){
						MoneyLog moneyLog = new MoneyLog();
						if(moneyLog.getChangeTime() < 1){
							moneyLog.setChangeTime(new Date().getTime());
						}
						logger.debug("新增资金变动记录:用户[" + moneyBefore.getUuid() + "][" + op + "]" + value + "[" + pd.getName() + "],余额时间戳:" + moneyLog.getChangeTime());
						moneyLog.setAmount(value);
						moneyLog.setOp(op);
						moneyLog.setMemory(memory);;
						moneyLog.setMoneyAfter(moneyAfter);
						//moneyLog.setExtraValue("operate",operateDescription);
						moneyLog.setToAccount(moneyBefore.getUuid());
						MoneyType mt = MoneyType.chargeMoney.findByCode(pd.getName());
						if(mt == null){
							logger.error("找不到对应的资金类型:" + pd.getName());
							continue;
						}
						moneyLog.setMoneyTypeId(mt.getId());
						moneyLog.setExtraValue("moneyTypeName", mt.getName());	
						moneyLog.setOwnerId(moneyBefore.getOwnerId());
						insert(moneyLog);
						moneyLog = null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * 把缓存中的资金变化日志写入数据库
	 * @throws InterruptedException 
	 */

	public void save() throws InterruptedException{
		if(saveTaskIsRunning){
			logger.info("资金变动日志入库已运行，不再执行");
			return;
		}
		saveTaskIsRunning = true;
		logger.info("启动资金变动日志入库任务");

		new Thread(new Runnable(){

			@Override
			public void run() {
				ObjectMapper om = JsonUtils.getInstance();
				while(true){
					try {
						Thread.sleep(saveTaskIdelSeconds * 1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					Set<String> set = centerDataService.pushSetFromZQueue(QUEUE_NAME, true, 0, 10);
					if(logger.isDebugEnabled())logger.debug("从缓存中获取到的资金日志数量是:" + set.size() + "条");
					if(set.size() < 1){
						continue;
					}
					try {
						for(String value : set){
							MoneyLog moneyLog = om.readValue(value, MoneyLog.class);

							if(moneyLog != null){
								moneyLogDao.insert(moneyLog);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						//saveTaskIsRunning = false;

					}

				}				
			}

		}).start();


	}

}
