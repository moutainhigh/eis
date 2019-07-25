package com.maicard.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.time.DateUtils;

import com.maicard.common.util.Crypt;
import com.maicard.common.util.Sslv3SocketFactory;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;
import com.maicard.product.domain.Item;






public class SortTest {

	public static void main(String[] argv){
/*		String[] promotions = new String[]{
			"coin#300#50", "coin#100#5", "coin#200#10", "coin#300#20"
		};
		
		List<ActivityReward> activityRewardList = new ArrayList<ActivityReward>();
		for(int i = 1; i <= promotions.length; i++){
			ActivityReward activityReward = new ActivityReward(i, promotions[i]);
			activityRewardList.add(activityReward);
		}
		
		for(ActivityReward itemLog : activityRewardList){
			System.out.println("rate:" + itemLog.getRewardRate());
		}
		System.out.println("------------------------------------");
		//排序
		Collections.sort(activityRewardList, new Comparator<ActivityReward>(){

			@Override
			public int compare(ActivityReward o1, ActivityReward o2) {
				if(o1.getRewardRate() >= o2.getRewardRate()){
					System.out.println(o1.getRewardRate() + ":" + o2.getRewardRate() + "===>" + 1);
					return 1;
				}
				System.out.println(o1.getRewardRate() + ":" + o2.getRewardRate() + "===>" + 0);
				return -1;
			}});
		
		System.out.println("------------------------------------");
		
		for(ActivityReward itemLog : activityRewardList){
			System.out.println("rate:" + itemLog.getRewardRate());
		}*/

	}


}