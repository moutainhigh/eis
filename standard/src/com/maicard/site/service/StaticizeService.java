package com.maicard.site.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.maicard.site.criteria.StaticizeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Staticize;

public interface StaticizeService {
	
	/**
	 * 静态化一个栏目首页
	 */
	void staticize(Node node);
	
	/**
	 * 删除一个栏目的静态文件
	 */
	int deleteStaticizeFile(Node node);


	/**
	 * 若对应的Staticize对象已存在则更新
	 * 如果不存在则新增
	 */
	int replace(Staticize staticize);

	/**
	 * 静态化一篇文章，等待waitSec秒后执行，以确保前端数据已同步
	 * @param document
	 */
	@Async
	void staticize(Document document, long waitSec);
	
	/**
	 * 删除一篇文章的静态文件
	 */
	int deleteStaticizeFile(Document document);

	List<Staticize> list(StaticizeCriteria staticizeCriteria);

	/**
	 * 根据条件删除所有匹配的数据
	 */
	int delete(StaticizeCriteria staticizeCriteria);
	
}
