package com.maicard.test;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;



public class GcTest {
	public static void main(String[] argv){
		List<GarbageCollectorMXBean> instances  = ManagementFactory.getGarbageCollectorMXBeans();

		//遍历每个实例

		for(GarbageCollectorMXBean instance : instances){
			//返回垃圾收集器的名字
			System.out.printf("***%s: %s***%n","Name",instance.getName());
			//返回已发生的回收的总次数
			System.out.printf("%s: %s%n","CollectionCount",instance.getCollectionCount());
			//返回近似的累积回收时间
			System.out.printf("%s: %s%n","CollectionTime",instance.getCollectionTime());

		}
	}
}
