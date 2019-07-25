var channelMap = new Map();
var shareMap = new Map();

$(document).ready(function(){
	$("#tablist").find("li").click(function(){
		$(".tab-pane").css("display","none");
		if($(this).attr("data-query")){
			if($(this).attr("id") == "memberTab"){
				loadMemberTable();
			}else if($(this).attr("id") == "channelTab"){
				loadTable();
			}
		}
		$(this).attr("data-query",true);
		$("#"+$(this).attr("name")).css("display","block");
	});
	
	//初始化时间
	$("#memberDiv").find("#memberReportMonthTime").flatpickr({
		allowInput:false,
		defaultDate:new Date(),
		onChange:function(dateObject, dateString){
			$("#memberDiv").find("#memberReportMonthTime").val(DateUtil.dateToStr("yyyy-MM",dateObject));
		}
	});
	
	$("#memberDiv").find("#memberReportTime").flatpickr({
		allowInput:false,
		defaultDate:new Date()
	});
	
	$("#memberDiv").find("input[name='reportTime']").click(function(){
		var _obj = $("#memberDiv");
		var _dayObj = _obj.find("#memberReportTime");
		var _monthObj = _obj.find("#memberReportMonthTime");
		_obj.find("input[name='reportTime']").not(this).attr("checked",false);
		if($(this).val() == "day"){
			_dayObj.css("display","inline");
			_monthObj.css("display","none");
		}else if($(this).val() == "month"){
			_dayObj.css("display","none");
			_monthObj.css("display","inline");
			_monthObj.val(_monthObj.val().substr(0,7));
		}
	});
	
	
	//初始化时间
	$("#channelDiv").find("#channelReportMonthTime").flatpickr({
		allowInput:false,
		defaultDate:new Date(),
		onChange:function(dateObject, dateString){
			$("#channelDiv").find("#channelReportMonthTime").val(DateUtil.dateToStr("yyyy-MM",dateObject));
		}
	});
	
	$("#channelDiv").find("#channelReportTime").flatpickr({
		allowInput:false,
		defaultDate:new Date()
	});
	
	$("#channelDiv").find("input[name='reportTime']").click(function(){
		var _obj = $("#channelDiv");
		var _dayObj = _obj.find("#channelReportTime");
		var _monthObj = _obj.find("#channelReportMonthTime");
		_obj.find("input[name='reportTime']").not(this).attr("checked",false);
		if($(this).val() == "day"){
			_dayObj.css("display","inline");
			_monthObj.css("display","none");
		}else if($(this).val() == "month"){
			_dayObj.css("display","none");
			_monthObj.css("display","inline");
			_monthObj.val(_monthObj.val().substr(0,7));
		}
	});
	
	queryPayChannelInfo();
	$("#tablist").find("li").eq(0).trigger("click");
});




//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().find("toolbtns").css("display","none");
	$(obj).next().toggle();
}

//查询事件
function queryMemberAgain(){
	
	var memberNo = $("#memberNo").val().trim();
	//参数拼接
	var param = '';
	var parentObj = $("#memberDiv");
	var obj = parentObj.find("input[name='reportTime']:checked");
	if(obj.val() == 'day'){
		var reportTime = parentObj.find("#memberReportTime").val().trim();
		param = {
			    "reportTime": reportTime,
			    "memberNo": memberNo,
			    "pSize":"10"
		};
	}else if(obj.val() == 'month'){
		var reportMonthTime = parentObj.find("#memberReportMonthTime").val().trim();
		param = {
			    "reportMonthTime": reportMonthTime,
			    "memberNo": memberNo,
			    "pSize":"10"
		};
	}
	$('#member_grid_table').GM('setQuery',param,true);
	
}

//查询事件
function queryChannelAgain(){
	var payChannelId = $("#payChannelId").val().trim();
	//参数拼接
	var param = '';
	var parentObj = $("#channelDiv");
	var obj = parentObj.find("input[name='reportTime']:checked");
	if(obj.val() == 'day'){
		var reportTime = parentObj.find("#channelReportTime").val().trim();
		param = {
			    "reportTime": reportTime,
			    "payChannel": payChannelId,
			    "pSize":"10"
		};
	}else if(obj.val() == 'month'){
		var reportMonthTime = parentObj.find("#channelReportMonthTime").val().trim();
		param = {
			    "reportMonthTime": reportMonthTime,
			    "payChannel": payChannelId,
			    "pSize":"10"
		};
	}
	
	$('#channel_grid_table').GM('setQuery',param,true);
	
}

function loadTable(){
	$('#channel_grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/accountFlow/getReportByChannel.json'
		,ajax_type: 'POST'
		,pageSize: 10
		,query: {'reportTime':$("#channelReportTime").val().trim()}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,responseHandler: function(result){
			//计算汇总数据
			if(result.code == "00000"){
				loadChannelSummary(result.data);
			}
	    }
		,columnData: [
		              {key: 'payChannelId',remind: '支付通道',text: '支付通道',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return nodeData+"/"+channelMap.get(nodeData);
						  }
		              },
		              {key: 'cardType',remind: '卡类型',text: '卡类型',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return getCardType(nodeData);
						  }
		              },
		              {key: 'feeRate',remind: '费率',text: '费率',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'successTraCount',remind: '成功笔数',text: '成功笔数',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'orderTotalAmount',remind: '交易总金额',text: '交易总金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "0.0";
		            		  	}else{
		            		  		return amountFormat(nodeData);
		            		  	}
						  }
		              },
		              {key: '',remind: '手续费支出',text: '手续费支出',
		            	  template: function(nodeData, rowData){
		            		  	var v = rowData.orderTotalAmount * (1 - rowData.feeRate);
		            		  	if(isNaN(v)){
		            		  		return 0.0;
		            		  	}else{
		            		  		return amountFormat(v);
		            		  	}
		            	  }
		              }
		            ]
	});
}
//处理通道汇总信息
function loadChannelSummary(data){
	var channelReportCountV = 0;
	var channelTotalMoney = 0;
	var channelExpenditure = 0;
	var channelExpenditureTemp = 0;
	if(data != null && data != ''){
		$(data).each(function(){
			channelReportCountV += $(this).attr("successTraCount");
			channelTotalMoney += $(this).attr("orderTotalAmount");
			channelExpenditureTemp = $(this).attr("orderTotalAmount") * (1 -  $(this).attr("feeRate"));
			if(!isNaN(channelExpenditureTemp)){
				channelExpenditure += channelExpenditureTemp;
			}
		});
	}
	
	$("#channel_reportCount").html(channelReportCountV);
	$("#channel_totalMoney").html(amountFormat(channelTotalMoney));
	$("#channel_expenditure").html(amountFormat(channelExpenditure));
}

function loadMemberTable(){
	
	$('#member_grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/accountFlow/getReportByMember.json'
		,ajax_type: 'POST'
		,pageSize: 10
		,query: {'reportTime':$("#memberReportTime").val().trim()}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,responseHandler: function(result){
			//计算汇总数据
			if(result.code == "00000"){
				loadMemberSummary(result.data);
			}
	    }
		,columnData: [
		              {key: 'memberNo',remind: '会员号',text: '会员号'},
		              {key: 'memberName',remind: '会员名',text: '会员名'},
		              {key: 'payChannelId',remind: '支付通道',text: '支付通道',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return nodeData+"/"+channelMap.get(nodeData);
						  }
		              },
		              {key: 'cardType',remind: '卡类型',text: '卡类型',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return getCardType(nodeData);
						  }
		              },
		              {key: 'feeRate',remind: '费率',text: '费率',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'successTraCount',remind: '成功笔数',text: '成功笔数',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'orderTotalAmount',remind: '交易总金额',text: '交易总金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0.0;
		            		  	}else{
		            		  		return amountFormat(nodeData);
		            		  	}
						   }
		              },
		              {key: 'null1',remind: '商户手续费收入',text: '商户手续费收入',
		            	  template: function(nodeData, rowData){
		            		  	var v = rowData.orderTotalAmount * (1 - rowData.feeRate);
		            		  	if(isNaN(v)){
		            		  		return 0.0;
		            		  	}else{
		            		  		return amountFormat(v);
		            		  	}
		            	}
		              },
		              {key: 'null2',remind: '通道手续费支出',text: '通道手续费支出',
		            	  template: function(nodeData, rowData){
		            		    var bankRate = shareMap.get(rowData.payChannelId+rowData.cardType);
		            		    var v = rowData.orderTotalAmount * (1 - bankRate);
		            		  	if(isNaN(v)){
		            		  		return 0.0;
		            		  	}else{
		            		  		return amountFormat(v);
		            		  	}
		            	  }
		              },
		              {key: 'null3',remind: '收益',text: '收益',
		            	  template: function(nodeData, rowData){
		            		  	var channelV = rowData.orderTotalAmount * (1 - shareMap.get(rowData.payChannelId+rowData.cardType));
		            		  	var memberV = rowData.orderTotalAmount * (1 - rowData.feeRate);
		            		  	isNaN(channelV)?channelV=0.0:channelV=channelV;
		            		  	isNaN(memberV)?memberV=0.0:memberV=memberV;
		            		  	var v = memberV - channelV;
		            		  	if(isNaN(v)){
		            		  		return 0.0;
		            		  	}else{
		            		  		return amountFormat(v);
		            		  	}
		            	  }
		              }
		            ]
	});
}
//处理商家汇总信息
function loadMemberSummary(data){
	var memberReportCountV = 0;
	var memberTotalMoney = 0;
	var memberIncome = 0;
	var memberIncomeTemp = 0;
	var memberExpenditure = 0;
	var memberExpenditureTemp = 0;
	if(data != null && data != ''){
		$(data).each(function(){
			memberReportCountV += $(this).attr("successTraCount");
			memberTotalMoney += $(this).attr("orderTotalAmount");
			memberIncomeTemp = $(this).attr("orderTotalAmount") * (1 -  $(this).attr("feeRate"));
			if(!isNaN(memberIncomeTemp)){
				memberIncome += memberIncomeTemp;
			}
			memberExpenditureTemp = $(this).attr("orderTotalAmount") * (1 - shareMap.get($(this).attr("payChannelId")+$(this).attr("cardType")));
			if(!isNaN(memberExpenditureTemp)){
				memberExpenditure += memberExpenditureTemp;
			}
		});
	}
	
	$("#member_reportCount").html(memberReportCountV);
	$("#member_totalMoney").html(amountFormat(memberTotalMoney));
	$("#member_income").html(amountFormat(memberIncome));
	$("#member_expenditure").html(amountFormat(memberExpenditure));
	$("#member_get").html(amountFormat(memberIncome-memberExpenditure));
}

//查询通道信息
function queryPayChannelInfo(){
	$.ajax({
		type:"POST",
		url:"/accountFlow/getPayChannelInfo.json",
		dataType:"json",
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				var channelData = result.data.payMethodData;
				var shareData = result.data.shareConfigData;
				$(channelData).each(function(){
					channelMap.put($(this).attr("payMethodId"), $(this).attr("name"));
				});
				
				$(shareData).each(function(){
					shareMap.put($(this).attr("shareUuid")+$(this).attr("payCardType"), $(this).attr("sharePercent"));
				});
			}
		},
		error:function(){
			
		}		
	});
}