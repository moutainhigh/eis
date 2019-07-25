package com.maicard.common.service;

import java.util.List;

import com.maicard.common.domain.EisObject;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.criteria.ExtraDataCriteria;



public interface ExtraDataService {

	/**
	 * 插入一条扩展数据
	 * @param extraData
	 * @return 插入成功返回1
	 */
	int insert(ExtraData extraData);

	/**
	 * 更新一条有主键的扩展数据
	 * @param extraData
	 * @return 更新成功返回1
	 */
	int update(ExtraData extraData);

	/**
	 * 根据主键删除一个扩展数据
	 * @param extraDataId
	 * @return 删除成功返回1
	 */
	int delete(long extraDataId);
	
	/**
	 * 根据主键返回一条扩展数据
	 * @param 主键extraDataId
	 * @return 扩展数据
	 */
	ExtraData select(long extraDataId, String tableName);
		
	/**
	 * 根据条件返回所有符合条件的扩展数据
	 * @param extraDataCriteria
	 * @return
	 */
	List<ExtraData> list(ExtraDataCriteria extraDataCriteria);

	/**
	 * 根据条件返回所有符合条件的扩展数据<br>
	 * 但会根据paging限定返回条数
	 * @param extraDataCriteria
	 * @return
	 */
	List<ExtraData> listOnPage(ExtraDataCriteria extraDataCriteria);
	
	/**
	 * 根据条件计算应返回数据的数量
	 * @param extraDataCriteria
	 * @return
	 */
	int count(ExtraDataCriteria extraDataCriteria);

	
	/**
	 * 根据条件删除所有符合条件的数据<br>
	 * ！慎用！<br>
	 * 如果条件设置不当可能删除所有数据
	 * @param extraDataCriteria
	 * @return
	 */
	int deleteByObjectId(ExtraDataCriteria extraDataCriteria);

	/**
	 * 同步一个EisObject中的扩展数据集合<br>
	 * 包括新增、更新和删除
	 * @param eisObject
	 * @return
	 */
	int sync(EisObject eisObject);

	





}
