/**
 * 
 */
package com.maicard.site.service.task;

import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.site.service.BatchStaticizeNodeService;
import com.maicard.site.service.StaticizeJob;
import com.maicard.standard.DataName;
import com.maicard.standard.Operate;
import com.maicard.standard.ServiceStatus;

/**
 * 
 *
 * @author NetSnake
 * @date 2013-9-18 
 */
@Service
@Deprecated		//已放弃使用
public class BatchStaticizeNodeServiceImpl extends BaseService implements BatchStaticizeNodeService {

	@Resource
	private StaticizeJob staticizeJob;
	
	@Resource
	private ConfigService configService;

	private LinkedHashSet<Integer> unprocessedNode;
	
	private boolean handlerBatchStaticize  = false;
	
	@PostConstruct
	public void init(){
		handlerBatchStaticize = configService.getBooleanValue(DataName.handlerBatchStaticize.toString(),0);
	}


	@Override
	public  Integer[] accessUnprocessedNode(String mode, int nodeId){
		if(mode.equals(Operate.create.getCode())){
			if(unprocessedNode == null){
				unprocessedNode = new LinkedHashSet <Integer>();
			}
			unprocessedNode.add(nodeId);	
			return null;
		} 
		if(mode.equals(Operate.flush.getCode())){
			if(unprocessedNode == null){
				return null;
			}
			Integer[] needProcessNodeId = unprocessedNode.toArray(new Integer[unprocessedNode.size()]);
			unprocessedNode.clear();
			return needProcessNodeId;
		}
		return null;
	}

	private void startJob() {
		if(logger.isDebugEnabled()){
			logger.debug("批量静态化任务开始执行...");
		}
		if(unprocessedNode == null || unprocessedNode.size() == 0){
			if(logger.isDebugEnabled()){
				logger.debug("需要静态化的节点是空");
			}
			return;
		}

		EisMessage status = staticizeJob.status();
		if(status == null || status.getOperateCode() != ServiceStatus.closed.getId()){
			logger.info("静态化任务状态异常或正在执行中.下次再试[" + (status == null ? "null" : status.getOperateCode()) + "]");	
			return;
		}
		Integer[] needProcessedId = accessUnprocessedNode(Operate.flush.getCode(),0);
		if(needProcessedId == null || needProcessedId.length == 0){
			if(logger.isDebugEnabled()){
				logger.debug("尝试获取的静态化的节点数组是空");
			}
			return;
		}

		if(logger.isDebugEnabled()){
			logger.debug("尝试获取的静态化的节点数组有[" + needProcessedId.length + "]个");
		}
		if(logger.isDebugEnabled()){
			logger.debug("尝试获取的静态化的节点数组有[" + needProcessedId.length + "]个");
		}
		int[] nodeIds = new int[needProcessedId.length];
		for(int i =0 ; i < needProcessedId.length; i++){
			nodeIds[i] = needProcessedId[i];
		}
		staticizeJob.start("node", nodeIds);

	}

	@Override
	public EisMessage start() {
		if(handlerBatchStaticize){
			logger.debug("本节点负责批量静态化，尝试执行批量静态化");
			startJob();
		} else {
			logger.debug("本节点不负责批量静态化，不执行批量静态化");		
		}
		return null;
	}

	@Override
	public EisMessage stop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage status() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage start(String objectType, int... objectIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
