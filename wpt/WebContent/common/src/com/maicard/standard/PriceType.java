package com.maicard.standard;

public enum PriceType {
	CODE_TO_SCORE, SCORE_TO_PRODUCT, COIN_TO_PRODUCT,
	PRICE_STANDARD, //标准价格
	PRICE_TUAN, //团购价格
	PRICE_MEMBER, //会员价格
	PRICE_PROMOTION,	//优惠价格，可以与identify配合使用识别不同的优惠活动
	PRICE_SALE;//售出价格
}
