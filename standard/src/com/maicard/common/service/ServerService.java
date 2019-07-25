package com.maicard.common.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.maicard.annotation.ExecOnBothNode;
import com.maicard.common.domain.Server;

public interface ServerService {

	@Async
	@ExecOnBothNode
	void restart(Server server);

	@ExecOnBothNode
	int refresh(Server server);

	void run();

	Server select(String systemCode, int serverId);

	List<Server> list();

	@Async
	@ExecOnBothNode
	void updateSvn(Server server);

	@Async
	@ExecOnBothNode
	void install(Server server);

	/**
	 * 对服务器执行一条命令
	 * @param server
	 */
	@ExecOnBothNode
	void exec(Server server);

	/**
	 * 检查有没有中心缓存的命令并执行
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-25
	 */
	int checkCenterCmd(Server server);
}
