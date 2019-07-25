package com.maicard.wpt.service;

import java.util.List;

import com.maicard.common.domain.EisMessage;
import com.maicard.wpt.criteria.WeixinButtonCriteria;
import com.maicard.wpt.domain.WeixinButton;

public interface WeixinButtonService {
	int insert(WeixinButton weixinButton);

	int update(WeixinButton weixinButton);

	int delete(long weixinButtonId);
	
	WeixinButton select(long weixinButtonId);
	
	List<WeixinButton> list(WeixinButtonCriteria weixinButtonCriteria);

	List<WeixinButton> listOnPage(WeixinButtonCriteria weixinButtonCriteria);

	int count(WeixinButtonCriteria weixinButtonCriteria);

	EisMessage clone(WeixinButtonCriteria weixinButtonCriteria);

	/**
	 * 根据提供的菜单，生成一个菜单-子菜单树形结构
	 * @param weixinButtonList
	 * @return
	 */
	List<WeixinButton> generateTree(List<WeixinButton> weixinButtonList);


}
