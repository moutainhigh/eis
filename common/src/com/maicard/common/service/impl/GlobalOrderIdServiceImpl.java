package com.maicard.common.service.impl;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.GlobalOrderIdCriteria;
import com.maicard.common.dao.GlobalOrderIdDao;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.TransactionStandard.TransactionType;

@Service
public class GlobalOrderIdServiceImpl extends BaseService implements GlobalOrderIdService {


	@Resource
	private GlobalOrderIdDao globalOrderIdDao;
	@Resource
	private ConfigService configService;

	@Value("${systemServerId}")
	private int serverId;
	
	@Resource
	private GlobalUniqueService globalUniqueService;


	private boolean transactionSaveGeneratedOrderId;

	private static ThreadLocal<SimpleDateFormat> orderIdFormaterHolder
	= new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat(CommonStandard.orderIdDateFormat);
		}
	};
	



	@PostConstruct
	public void init(){
		transactionSaveGeneratedOrderId = configService.getBooleanValue(DataName.transactionSaveGeneratedOrderId.toString(),0);
	}

	@Override
	public void insert(String globalOrderId) {
		globalOrderIdDao.insert(globalOrderId);

	}/*

	private String save(String orderId){
		int retry = 3;
		int i = 0;
		while(i < retry){
			try{

				insert(orderId);
				return orderId;
			}catch(Exception e){

			}
			i++;
		}
		return null;
	}*/


	@Override
	public String generate() {
		return generate(TransactionType.other.getId());
	}

	//唯一单号由三位服务器ID和两位交易类型ID作为前缀
	@Override
	public String generate(int transactionTypeId){
		return generate(transactionTypeId, new Date());
	}

	//唯一单号由三位服务器ID和两位交易类型ID作为前缀
	@Override
	public String generate(int transactionTypeId, Date orderDate){
		if(orderDate == null){
			orderDate = new Date();
		}
		DecimalFormat serverFormat = new DecimalFormat("000");
		DecimalFormat transactionTypeFormat = new DecimalFormat("00");
		DecimalFormat randFormat = new DecimalFormat("00000000");

		String prefix = serverFormat.format(serverId) + transactionTypeFormat.format(transactionTypeId);
		//String orderId = prefix + orderIdFormaterHolder.get().format(orderDate)  + (RandomUtils.nextInt(88888888) + 10000000);
		String orderId = prefix + orderIdFormaterHolder.get().format(orderDate)  + randFormat.format(globalUniqueService.incrOrderSequence(1));
		if(transactionSaveGeneratedOrderId){
			insert(orderId);
		}
		return orderId;
	}


	@Override
	public boolean exist(String orderId){
		GlobalOrderIdCriteria globalOrderIdCriteria = new  GlobalOrderIdCriteria();
		globalOrderIdCriteria.setOrderId(orderId);
		return globalOrderIdDao.exist(globalOrderIdCriteria);
	}

	@Override
	public Date getDateByTransactionId(String transactionId){
		if(transactionId == null || transactionId.equals("") || transactionId.length() < 23){
			return null;
		}
		try{
			return orderIdFormaterHolder.get().parse(transactionId.substring(5,19));
		}catch(Exception e){}
		return null;
	}
}
