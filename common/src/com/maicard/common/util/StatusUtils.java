package com.maicard.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.standard.EisError;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.ts.SettlementStatus;

public class StatusUtils {

	protected static final Logger logger = LoggerFactory.getLogger(HttpMethods.class);

	public static List<Integer> getAllStatusValue(String... valueTypes){


		List<Integer> statusList = new ArrayList<Integer>();

		for(TransactionStatus ts : TransactionStatus.values()){
			if(ts.id > 0){
				statusList.add(ts.id);
			}
		}
		for(EisError error :  EisError.values()){
			if(error.id > 0){

				statusList.add(error.id);
			}
		}

		return statusList;
	}

	public static List<Integer> getStatusList(String... statusCollection) {
		logger.info("放入状态枚举:" + Arrays.toString(statusCollection));
		List<Integer> statusList = 			new ArrayList<>();
		for(String statusValue : statusCollection){
			if(statusValue.equalsIgnoreCase("SettelmentStatus")){
				for (SettlementStatus settlementStatus : SettlementStatus.values()) {
					if(settlementStatus.id > 0){
						statusList.add(settlementStatus.id);
					}
				}
			}
		}		
		return statusList;
	}
}
