package com.maicard.standard;


public enum Operate {
		unknown(0,"unknown","未知"),
		create(102001,"create","新增"),
		delete(102002,"delete","删除"),
		update(102003,"update","更新"),
		updateLogin(102116,"updateLogin","更新登录信息"),
		get(102004,"get","查看"),
		relate(102005,"relate","关联"),
		active(102109,"active","用户激活"),
		login(102110,"login","用户登入"),
		logout(102111,"logout","用户退出"),
		jump(102124,"jump","跳转"), 
		postProcess(102113,"postProcess","后期处理"),
		updateAmount(102125,"updateAmount","更新数量"),
		close(102126,"close","关闭"),
		plus(102127,"plus","增加"),
		minus(102128,"minus","减少"),
		lock(102129,"lock","锁定"),
		unLock(102130,"unLock","解锁"),
		afterLock(102131,"afterLock","锁定后"),
		afterUnLock(102132,"afterUnLock","解锁后"),
		afterPlus(102133,"afterPlus","加上后"),
		afterMinus(102134,"afterMinus","减少后"),
		preview(102135,"preview","预览"),
		byTime(102136,"byTime","按时间轴"),
		byProduct(102137,"ByProduct","按产品"),
		byInviter(102138,"ByInviter","按渠道"),
		list(102139,"list","列表"), 
		//sendNotify(102140,"sendNotify","发送通知"),
		JmsDataSync(102141,"handlerJmsDataSync","更新本地数据"),
		flush(102142,"flush","刷新数据"),
		download(102143,"download","下载"), 
		findByUuid(102144,"findByUuid","根据UUID查找"), 
		findByUsername(102145,"findByUsername","根据用户名查找"),
		withdraw(102146,"withdraw","提现"),
		use(102147,"use","使用"),
		//userRegister(102148,"userRegister","用户注册"),
		userRoleInfo(102149,"userRoleInfo","用户角色信息"), 
		listProductServer(102150,"listProductServer","列出产品服务器"),
		exchange(102159,"exchange","兑换"), 
		prepare(102163,"prepare","预备"),
		join(102164,"join","加入"),
		updateNoNull(102165,"updateNoNull","仅更新非空数据"),
		clear(102166,"clear","清除所有关联数据"),
		scanCode(102167,"scanCode","扫码"),
		call(102168,"call","调用对应程序"),
		iframe(102169,"iframe","在iframe中显示结果"), 
		refund(102170,"refund","退款"), 
		addToCart(102171,"addToCart","加入购物车"),
		settleUp(102172,"settleUp","结算"),
		notify(102173,"notify","通知"),
		share(102174,"share","分润"),
		config(102175,"config","配置"),
		confirm(102176,"confirm","确认"), 
		post(102177,"post","POST提交");
	
		//ByPartner(102144,"ByPartner","按渠道"),
		//detailSelf(102145,"detailSelf","明细"),        
		//partner(102146,"partner","下级用户");
		public final int id;
		public  final String code;
		public final String name;
		
		private Operate(int id, String code, String name){
			this.id = id;
			this.code = code;
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}
		@Override
		public String toString(){
			return this.name + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
		}	
		public Operate findByCode(String code){
			for(Operate value: Operate.values()){
				if(value.code.equalsIgnoreCase(code)){
					return value;
				}
			}
			return unknown;
		}
		public Operate findById(int id){
			for(Operate value: Operate.values()){
				if(value.getId() == id){
					return value;
				}
			}
			return unknown;
		}
	}
