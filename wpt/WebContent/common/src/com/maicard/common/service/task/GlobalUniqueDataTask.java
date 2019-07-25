package com.maicard.common.service.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.GlobalUniqueCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.service.Task;

/**
 * 检查REDIS服务器中是否已设置所有的全局唯一约束数据
 * 如果没有则批量设置
 *
 *
 * @author NetSnake
 * @date 2016年4月24日
 *
 */

@Service
public class GlobalUniqueDataTask extends BaseService implements Task{
	
	@Resource
	private GlobalUniqueService globalUniqueService;

	
	

	@Override
	public EisMessage start() {
		return null;
	}

	@Override
	public EisMessage stop() {
		return null;
	}

	@Override
	public EisMessage status() {
		return null;
	}

	@Override
	public EisMessage start(String objectType, int... objectIds) {
		return null;
	}

	@Override
	public void run() {
		_start();
		
	}
	
	private void _start(){
		int distributedCount = globalUniqueService.getDistributedCount();
		
		if(distributedCount < 0){
			try {
				globalUniqueService.syncDbToDistributed();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			int dbCount = globalUniqueService.count(new GlobalUniqueCriteria());
			if(dbCount < distributedCount){
				//需要将中央缓存中的所有数据同步到本机数据库
				globalUniqueService.syncDistributedToDb();
			}
		}
		
	}

}
