package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class MenuCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int menuId;
	
	public MenuCriteria() {
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

}
