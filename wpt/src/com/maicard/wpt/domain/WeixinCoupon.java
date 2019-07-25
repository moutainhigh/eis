package com.maicard.wpt.domain;

/**
 * 用于系统Coupon与微信卡券的转换
 *
 *
 * @author NetSnake
 * @date 2015年12月15日
 *
 */
public class WeixinCoupon {

	public WeixinCoupon(){

	}
	public WeixinCoupon(String couponCode, String code, String timestamp, String signature, String nonce_str, String openid, String outer_id) {
		this.cardId = couponCode;
		this.cardExt = new cardExt(code, timestamp, signature, nonce_str, openid, outer_id);
	}
	private  String cardId;
	private  cardExt cardExt;
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public cardExt getCardExt() {
		return cardExt;
	}
	public void setCardExt(cardExt cardExt) {
		this.cardExt = cardExt;
	}

}

class cardExt {
	private String code;
	private String signature;
	private String timestamp;

	//非必填
	private String nonce_str;
	private String openid;
	private String  outer_id;

	public cardExt(){

	}
	public cardExt(String code2, String timestamp2, String signature2, String nonce_str2, String openid2,
			String outer_id2) {
		if(code2 != null){
			this.code = code2;
		}
		this.timestamp = timestamp2;
		this.signature = signature2;
		this.nonce_str = nonce_str2;
		this.openid = openid2;
		this.outer_id = outer_id2;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getOuter_id() {
		return outer_id;
	}
	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
