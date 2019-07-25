package com.maicard.common.base;

/**
 * 支持进行inviters查询的查询类
 *
 *
 * @author NetSnake
 * @date 2017-06-28
 */
public abstract class InviterSupportCriteria extends Criteria {

	private static final long serialVersionUID = -5341750870840114346L;
	
	protected long[] inviters;
	
	public long[] getInviters() {
		return inviters;
	}
	public void setInviters(long... inviters) {
		this.inviters = inviters;
	}
}
