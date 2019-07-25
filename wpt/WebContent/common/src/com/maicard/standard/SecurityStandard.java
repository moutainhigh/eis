package com.maicard.standard;

public interface SecurityStandard {


	public enum CryptMode {
		none(1,"none", "无加密模式"),
		des(2,"des","DES加密"),
		des3(3,"des3", "DES3加密"),
		aes(4,"aes", "AES加密"),
		rsa(5,"rsa","RSA加密");

		private final int id;
		private final String code;
		private final String name;

		private CryptMode(int id, String code, String name){
			this.id = id;
			this.code = code;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

	}

	public enum UserTypes {
		sysUser(121001, "系统用户"),
		partner(121002,"合作伙伴"),
		frontUser(121003,"终端用户");

		public final int id;
		public final String name;
		private UserTypes(int id, String name){
			this.id = id;
			this.name = name;
		}
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		@Override
		public String toString(){
			return this.name + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
		}

		public UserTypes findById(int id){
			for(UserTypes userTypeStandard: UserTypes.values()){
				if(userTypeStandard.getId() == id){
					return userTypeStandard;
				}
			}
			return null;
		}
	}

	//扩展用户类型
	public enum UserExtraType {
		promotionChannel(1, "推广渠道"),
		accountChargeSupplier(2,"帐号直充供应者"),
		payChannel(3,"支付或卡密回收通道"),
		siteCooporation(4,"网站联合运营"),
		cardSalePartner(5,"销卡请求者"),
		accountChargeRequester(6,"帐号充值请求者"),
		deliveryPartner(9,"快递合作商"),
		tencentUser(11,"腾讯开放API注册用户");

		public final int id;
		public final String name;
		private UserExtraType(int id, String name){
			this.id = id;
			this.name = name;
		}
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		@Override
		public String toString(){
			return this.name + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
		}

		public UserExtraType findById(int id){
			for(UserExtraType userType: UserExtraType.values()){
				if(userType.getId() == id){
					return userType;
				}
			}
			return null;
		}
	}

	//用户状态
	public enum UserStatus{
		unknown(0,"未知"),
		normal(120001,"正常"),
		disabled(120002,"禁用"),
		locked(120003,"锁定"),
		unactive(120004,"未激活"),
		autoCreate(120005,"自动创建"),
		inQuery(120006,"查询中"),
		needQuery(120007,"需要查询"), 
		needSetPassword(120008,"需要设置密码"),
		authorized(120009,"已通过认证或授权"),
		hidden(120010,"隐藏");

		public final int id;
		public final String name;
		private UserStatus(int id, String name){
			this.id = id;
			this.name = name;
		}
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		@Override
		public String toString(){
			return this.name + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
		}	
		public UserStatus findById(int id){
			for(UserStatus value: UserStatus.values()){
				if(value.getId() == id){
					return value;
				}
			}
			return unknown;
		}
	}

	//用户扩展状态
	public enum UserExtraStatus{
		unknown(0,"unknown"),
		online(123001,"online"),
		offline(123002,"offline"),
		invisible(123003,"invisible");

		public final int id;
		public final String code;
		private UserExtraStatus(int id, String code){
			this.id = id;
			this.code = code;
		}
		public int getId() {
			return id;
		}

		public String getCode() {
			return code;
		}
		@Override
		public String toString(){
			return this.code + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
		}	
		public UserExtraStatus findById(int id){
			for(UserExtraStatus value: UserExtraStatus.values()){
				if(value.getId() == id){
					return value;
				}
			}
			return unknown;
		}
	}
	//用户级别方案类型
	public static enum UserLevelCondition{
		current, upgrade, downgrade;
	}


	//用户级别方案优惠名称
	public static enum UserLevelPromotion{
		givePointForPay,
		givePointForBuy;
	}
	
}
