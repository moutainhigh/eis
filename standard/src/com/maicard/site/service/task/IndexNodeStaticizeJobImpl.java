/**
 * 
 */
package com.maicard.site.service.task;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.site.service.IndexNodeStaticizeJob;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.StaticizeJob;
/**
 * 对首页节点（及其文档进行更新）
 * 供Quarts引擎调用
 * 
 *
 * @author NetSnake
 * @date 2013-10-23
 */
@Service
@Deprecated		//已放弃使用
public class IndexNodeStaticizeJobImpl extends BaseService implements IndexNodeStaticizeJob {

	@Resource
	private StaticizeJob staticizeJob;
	
	@Resource 
	private NodeService nodeService;


	private void startJob() {
		if(logger.isDebugEnabled()){
			logger.debug("首页节点静态化工作开始执行...");
		}
		//FIXME 没有指定有效的siteCode和ownerId
		staticizeJob.start("node", nodeService.getDefaultNode(null,0).getNodeId());

	}

	@Override
	public EisMessage start() {
		startJob();
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

}
