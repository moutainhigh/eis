/**
 * 
 */
package com.maicard.site.service;

import com.maicard.common.service.EisJob;


/**
 * 节点的批量静态化
 *
 * @author NetSnake
 * @date 2013-9-18 
 */
public interface BatchStaticizeNodeService extends EisJob{

	Integer[] accessUnprocessedNode(String mode, int nodeId);

	

}
