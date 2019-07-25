package com.maicard.wpt.constants.chaoka;

public enum BankEnums {
	
	ALIPAY("支付宝"),
	WECHAT("微信"),
	ICBC("中国工商银行"),
	CCB("中国建设银行");
	
	
	public String bankName;
	
	private BankEnums(String name) {
		bankName = name;
	}
	
	public static BankEnums getByName(String name) {
		for(BankEnums be : BankEnums.values()) {
			if(be.bankName.equalsIgnoreCase(name)) {
				return be;
			}
		}
		return null;
	}
	
	public static BankEnums getByCode(String name) {
		for(BankEnums be : BankEnums.values()) {
			if(be.name().equalsIgnoreCase(name)) {
				return be;
			}
		}
		return null;
	}


}
