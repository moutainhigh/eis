package com.maicard.wpt.domain;

/**
 * 微信卡券产品数据结构
 * 与微信官方的数据结构对应
 * 与我方的数据结构无关
 *
 *
 * @author NetSnake
 * @date 2016年6月22日
 *
 */
public class WeixinCouponModel {
	
	private String card_type;
	
	private CouponModelCash cash;
	
	public WeixinCouponModel(){		
	}
	
	

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public CouponModelCash getCash() {
		if(cash == null){
			cash = new CouponModelCash();
		}
		return cash;
	}

	public void setCash(CouponModelCash cash) {
		this.cash = cash;
	}

	

}
