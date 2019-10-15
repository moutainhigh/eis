package com.maicard.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.standard.CommonStandard;
import com.maicard.standard.TablePartitionPolicy;

public class TablePartitionUtils {

	static final Logger logger = LoggerFactory.getLogger(TablePartitionUtils.class);
	static final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	public static String getTableMonth(String transactionId){
		
		if(StringUtils.isBlank(transactionId)) {
			logger.error("应用表月份时的订单号为空");
			return "";
		}
		
		
		String tableName = null;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date splitDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);
		Date orderDate = null;
		try {
			orderDate = new SimpleDateFormat(CommonStandard.orderIdDateFormat).parse(transactionId.substring(5, 5 + CommonStandard.orderIdDateFormat.length()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(orderDate == null){
			logger.error("无法解析订单号[" + transactionId + "]的日期，无法转换表月份");
			return "";
		}
		if (orderDate.after(splitDate)){		
			logger.debug("订单[" + transactionId + "]的日期位于分表时间点之后:" + sdf.format(splitDate) + ",不需要换表月份");
			return "";
		} 
		if (TablePartitionPolicy.itemHistory.toString().equals("MM")) {
			tableName = "_"	+ fmt.format(orderDate).substring(5, 7);
		}  else {
			tableName = "";
		}

		logger.debug("订单[" + transactionId + "]的表月份是:" + tableName);
		return tableName;

	}

	
	public static void main(String[] argv) {
		String id = "0112119101500383302";
		String m = getTableMonth(id);
		System.out.println(m);
	}
}
