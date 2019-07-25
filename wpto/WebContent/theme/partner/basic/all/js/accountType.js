//定义账户类型map  key=code  value=描述
//个人会员
var accountMap00 = new Map();
accountMap00.put("0001","个人基本账户");
accountMap00.put("0003","个人理财账户");
accountMap00.put("0005","个人保证金户");
accountMap00.put("0007","P2P个人专户");

var accountMap08 = new Map();
accountMap08.put("0801","商户基本账户");
accountMap08.put("0802","基金份额总户");
accountMap08.put("0803","商户理财账户");
accountMap08.put("0805","商户保证金户");
accountMap08.put("0807","商户押金账户");
accountMap08.put("0808","商户欠款账户");
accountMap08.put("0811","商户待清算账户");

var accountMap80 = new Map();
accountMap80.put("8000","备付渠道金存款");
accountMap80.put("8001","渠道交易应付账户");
accountMap80.put("8002","渠道交易应收账户");
accountMap80.put("8003","渠道资金应付账户");
accountMap80.put("8004","渠道资金应收账户");
accountMap80.put("8005","已核应付");
accountMap80.put("8006","已核应收");

var accountMap88 = new Map();
accountMap88.put("8803","付款待处理");
accountMap88.put("8805","退款待处理");
accountMap88.put("8807","拒付应付款");
accountMap88.put("8809","调账待处理");
accountMap88.put("8851","收单交易手续费收入");
accountMap88.put("8852","渠道手续费成本支出");
accountMap88.put("8853","会员充值手续费收入");
accountMap88.put("8855","会员转账手续费收入");
accountMap88.put("8857","会员提现手续费收入");
accountMap88.put("8858","代付手续费支出");
accountMap88.put("8859","代付手续费收入");
accountMap88.put("8861","备付金户利息收入");
accountMap88.put("8863","自有户利息收入");
accountMap88.put("8858","代付手续费支出");
accountMap88.put("8864","账户管理费支出");
accountMap88.put("8893","渠道迁入挂账");
accountMap88.put("8894","渠道清零挂账");
accountMap88.put("8811","平台测试款");
accountMap88.put("8865","资损赔付户");
accountMap88.put("8867","资损预付金户");
accountMap88.put("8871","待查入金");
accountMap88.put("8872","待查出金");
accountMap88.put("8873","投资收益");
accountMap88.put("8891","客户清零挂账");
accountMap88.put("8892","客户余额迁入挂账");
accountMap88.put("8874","汇兑损失");
accountMap88.put("8875","汇兑收入");
accountMap88.put("8877","外币付款待处理");
accountMap88.put("8879","风险准备金账户");
accountMap88.put("8881","赔付待处理账户");
accountMap88.put("8883","强扣资金账户");
accountMap88.put("8885","预收手续费账户");
accountMap88.put("8887","商户充值待认领账户");
accountMap88.put("8889","生活应用手续费收入");
accountMap88.put("8895","非备付金渠道过渡户");

var memberTypeMap = new Map();
memberTypeMap.put("00",accountMap00);
memberTypeMap.put("08",accountMap08);
memberTypeMap.put("80",accountMap80);
memberTypeMap.put("88",accountMap88);

//下面返回option列
function createOptionForAccountType(accountType){
	var map = new Map();
	if(accountType == undefined || accountType == ""){
		//返回全部
		memberTypeMap.each(function(k,v){
			v.each(function(key,value){
				map.put(key, value);
			});
		});
	}else{
		//返回特定类型
		map = memberTypeMap.get(accountType);
	}
	var opStr = "<option value=''>全部</option>";
	//下面生成option
	if(map.size() > 0){
		map.each(function(key,value){
			opStr += "<option value='"+key+"'>"+value+"</option>";
		});
		return opStr;
	}else{
		return "";
	}
	
}

