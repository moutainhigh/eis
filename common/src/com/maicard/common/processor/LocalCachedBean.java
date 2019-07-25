package com.maicard.common.processor;

/**
 * 把数据缓存到内存中的service
 * 调用该命令需要清理并重新从数据库加载这些缓存
 * @author GHOST
 * @date 2018-10-25
 *
 */
public interface LocalCachedBean {

	void reloadCache();
}
