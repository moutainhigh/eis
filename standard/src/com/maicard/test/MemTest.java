package com.maicard.test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.math.BigDecimal;
import java.util.List;


public class MemTest {
	public static void main(String[] argv){
		long heapUsed = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 / 1024;
		long heapTotal = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / 1024 /1024;
		BigDecimal heapUsage = new BigDecimal((float)heapUsed / (float)heapTotal);
		heapUsage = heapUsage.setScale(2, BigDecimal.ROUND_UP);	
		
		long permUsed = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed() / 1024 / 1024;
		long permTotal = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax() / 1024 /1024;
		BigDecimal permUsage = new BigDecimal((float)permUsed / (float)permTotal);
		permUsage = permUsage.setScale(2, BigDecimal.ROUND_UP);	
		
		System.out.println(
				"Memory[HEAP:" + heapUsed +	"M/" + heapTotal + "M/" + heapUsage + ", PERM:" +	permUsed  +	"M/" + permTotal + "M/" + permUsage + "]");

	}

}
