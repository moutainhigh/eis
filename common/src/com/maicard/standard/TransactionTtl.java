package com.maicard.standard;


public enum TransactionTtl {
	
	productDefine("产品默认","product",0),
	hour("一小时","hour",3600),
	day("一天","day",86400),
	month("一个月","month",2592000);	
	
	
	private final String name;
	private final String code;
	private final int ttl;
	private TransactionTtl(String name, String code, int ttl){
		this.name = name;
		this.code = code;
		this.ttl = ttl;
	}
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public int getTtl() {
		return ttl;
	}
	public TransactionTtl findByTtl(int ttl){
		for(TransactionTtl value: TransactionTtl.values()){
			if(value.getTtl() == ttl){
				return value;
			}
		}
		return productDefine;
	}

}
