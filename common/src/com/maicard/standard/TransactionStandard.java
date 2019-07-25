package com.maicard.standard;


public class TransactionStandard {
	


	//交易状态
	public static enum TransactionStatus{
		unknown(0,"未知"),
		needProcess(710001,"需要处理"),
		accept(710002,"接受但还未成功"),
		//XXX marginMoneyNotEnough(710002;	//保证金不足;16;交易状态
		closed(710004,"已关闭"),//或未开放;16;交易状态
		auctionWaitingResult(710005,"等待竞拍结果"),
		auctionSuccess(710007,"竞拍成功"),
		success(710010,"交易成功"),
		failed(710011,"交易失败"),
		//completed()			= 710012;	//交易已处理;16;交易状态	
		inProcess(710013,"交易处理中"),
		timeout(710017,"交易超时"),
		inCart(710018,"暂存在购物车"),
		waitingPay(710019,"交易等待付款"),
		newOrder(710021,"新订单"),
		notOpen(710022,"未开放"),
		moneyUpdateFailed(710023,"资金更新失败"),
		systemException(710024,"系统异常"),
		release(710025,"需返回原状态"),
		waitingNotify(710026,"等待异步处理结果"),
		needNotProcess(710027,"不需要进行处理"),
		halfComplete(710028,"部分完成"),
		needReValidate(710029,"需再次验证"),
		forceClose(710030,"强行关闭"),
		successBiggerThanLabel(710031,"成功总额大于标签价格"), 
		forceSuccess(710032,"强制成功"),
		rollback(710033,"需要回滚之前的交易"),
		errorToSuccess(710034,"由失败转为成功"),
		validated(710037,"已完成所有验证"),
		needLastMatch(710038,"需要最后一次匹配"),
		waitingConfirm(710039,"等待验证结果"), 
		waitingOtherResult(710040,"等待其他返回结果"), 
		waitingMatch(710041,"等待匹配"), 
		forceMoveFrozenToRequest(710042,"强行将冻结金额改为请求金额"), 
		onlyLooseMatch(710043,"只可用于宽松匹配"), 
		releaseAndFrozen(710044,"释放并暂停使用"), 
		forceRegionMatch(710045,"只可用于同区域匹配"), 
		needBackupNodeProcess(710046,"需要备份节点处理"),
		deliveryConfirmed(710047,"已确认收货"),
		delayProcess(710048,"延迟处理"), 
		bookingSuccess(710049,"预定成功"),
		delivering(710050,"发货中"),
		preDelivery(710051,"准备发货"),
		waitingComment(710052,"待评价"),
		commentClosed(710053,"评价已完成"), 
		refunding(710054,"退款中"),
		refunded(710055,"已退款"),
		waitingMinusMoney(710056,"等待扣款"),
		waitPostSuccess(710057,"已付款和扣款，执行结束操作");
		
		
		public final int id;
		public final String name;
		private TransactionStatus(int id, String name){
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
		public static TransactionStatus findById(int id){
			for(TransactionStatus value: TransactionStatus.values()){
				if(value.getId() == id){
					return value;
				}
			}
			return unknown;
		}
	}

	//交易类型
	public static enum TransactionType{
		unknown(0,"未知"),
		userMessage(9,"消息"),//非交易类型，用来生成用户消息的唯一ID messageId
		other(10,"其他交易"),
		pay(11,"付款"),
		buy(12,"消费"),
		sale(13,"出售"),
		match(14,"匹配"),
		stock(15,"库存"),
		withdraw(16,"提现"), 
		exchange(17,"兑换"),
		coupon(18,"优惠券"),
		booking(19,"预定"),
		refund(20,"退款"),
		charge(21,"充值");
		

		public final int id;
		public final String name;
		private TransactionType(int id, String name){
			this.id = id;
			this.name = name;
		}
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
		public String getCode() {
			return name();
		}
		@Override
		public String toString(){
			return this.name + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
		}	
		
		public TransactionType findById(int id){
			for(TransactionType value: TransactionType.values()){
				if(value.getId() == id){
					return value;
				}
			}
			return unknown;
		}
	}
	
	/**
	 * 返回认为是交易成功的所有状态
	 * @return
	 */
	public static int[] getSuccessStatus(){
		return new int[]{
				TransactionStatus.success.id,
				TransactionStatus.commentClosed.id,
				TransactionStatus.delivering.id,
				TransactionStatus.deliveryConfirmed.id,
				TransactionStatus.waitingComment.id,
				TransactionStatus.preDelivery.id,
		};
	}
	
}
