var channelMap = new Map();
var shareMap = new Map();

$(document).ready(function(){
	$("#tablist").find("li").click(function(){
		$("#tablist").find("li").removeClass("active");
		$(this).addClass("active");
		$(".tab-pane").css("display","none");
		if($(this).attr("data-query")){
			if($(this).attr("id") == "checkTab"){
				loadCheckTable();
			}else if($(this).attr("id") == "mistakeTab"){
				loadMistakeTable();
			}
		}
		$(this).attr("data-query",true);
		$("#"+$(this).attr("name")).css("display","block");
	});
	
	//初始化时间
	$(".flatpickr").flatpickr({
		allowInput:false,
		defaultDate:DateUtil.dateAdd("d",-1,new Date())
	});
	//初始化时间
	$(".flatpickr_mistake_min").flatpickr({
		allowInput:false,
		enableTime:true,
		enableSeconds:true,
		time_24hr:true,
		defaultDate:minTimeOfDay(DateUtil.dateAdd("d",-1,new Date()))
	});
	
	//初始化时间
	$(".flatpickr_mistake_max").flatpickr({
		allowInput:false,
		enableTime:true,
		enableSeconds:true,
		time_24hr:true,
		defaultDate:maxTimeOfDay(new Date())
	});
	//queryPayChannelInfo();
	$("#tablist").find("li").eq(0).trigger("click");
});




//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().parent().find(".toolbtns").css("display","none");
	$(obj).next().toggle();
}

//查询事件
function queryCheckAgain(){
	var billDate = $("#checkTime").val().trim();
	var payChannel = $("#payChannel").val().trim();
	var status = $("#status option:selected").val().trim();
	//参数拼接
	var param = {
		"status":status,
	    "billDate": billDate,
	    "payChannel": payChannel,
	    "pSize":"10"
	};
	
	$('#check_grid_table').GM('setQuery',param,true);
	
}

//查询事件
function queryMistakeAgain(){
	var checkBatchNo = $("#batchNo").val().trim();
	var createdDateBegin = $("#mistakeStartTime").val().trim();
	var createdDateEnd = $("#mistakeEndTime").val().trim();
	var errorType = $("#errorType option:selected").val().trim();
	//参数拼接
	var param = {
	    "checkBatchNo": checkBatchNo,
	    "createdDateBegin": createdDateBegin,
	    "createdDateEnd": createdDateEnd,
	    "errorType":errorType,
	    "pSize":"10"
	};
	$('#mistake_grid_table').GM('setQuery',param,true);
}

function loadMistakeTable(){
	$('#mistake_grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/reconciliation/getMistake.json'
		,ajax_type: 'POST'
		,pageSize: 10
		,query: {'checkBatchNo':$("#batchNo").val().trim(),'createdDateBegin':$("#mistakeStartTime").val().trim(),'createdDateEnd':$("#mistakeEndTime").val().trim()}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,columnData: [
		              {key: 'checkBatchNo',remind: '批次号',text: '批次号',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'transactionId',remind: '交易号',text: '交易号',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'bankTrxNo',remind: '渠道交易号',text: '渠道交易号',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'orderAmount',remind: '平台订单金额',text: '平台订单金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'bankAmount',remind: '渠道订单金额',text: '渠道订单金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'feeAmount',remind: '平台手续费金额',text: '平台手续费金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'bankFee',remind: '渠道手续费金额',text: '渠道手续费金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'errorType',remind: '错误类型',text: '错误类型',
		            	  template: function(nodeData, rowData){
						        return getMistakeType(nodeData);
						  }
		              },
		              {key: 'handleStatus',remind: '处理状态',text: '处理状态',
		            	  template: function(nodeData, rowData){
		            		  return getMistakeHandleStatus(nodeData);
		            	  }
		              },
		              {key: 'null1',remind: '操作',text: '操作',
		            	  template: function(nodeData, rowData){
		            		  	var str = "<div style=\"position:relative;\"><span onclick=\"showTools(this)\" class=\"tools\" style=\"right:5px;cursor: pointer;\"><img src=\"/theme/basic/images/tools.png\"></span>";
								str += "<ul style=\"position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:13px;left:-27px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);\" class=\"toolbtns\">";
								str += "<li><a href=\"javascript:void(0)\" onclick=\"queryMistakeByNo('"+rowData.id+"')\">查看详细</a></li>";
								str += "</ur></div>";
						        return str;
						  }
		              }
		            ]
	});
}


function loadCheckTable(){
	$('#check_grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/reconciliation/get.json'
		,ajax_type: 'POST'
		,pageSize: 10
		,query: {'billDate':$("#checkTime").val().trim(),'status':$("#status option:selected").val().trim()}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,columnData: [
		              {key: 'batchNo',remind: '批次号',text: '批次号'},
		              {key: 'payChannel',remind: '通道',text: '通道'
		            	  //,
//		            	  template: function(nodeData, rowData){
//		            		  	if(nodeData == undefined){
//		            		  		return "";
//		            		  	}
//						        return nodeData+"/"+channelMap.get(nodeData);
//						  }
		              },
		              {key: 'tradeAmount',remind: '平台交易总金额',text: '平台交易总金额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return "";
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'tradeCount',remind: '平台交易笔数',text: '平台交易笔数',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'bankTradeAmount',remind: '渠道交易总额',text: '渠道交易总额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'bankFee',remind: '渠道手续费总额',text: '渠道手续费总额',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return amountFormat(nodeData);
						  }
		              },
		              {key: 'bankTradeCount',remind: '渠道交易笔数',text: '渠道交易笔数',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'mistakeCount',remind: '差错总笔数',text: '差错总笔数',
		            	  template: function(nodeData, rowData){
		            		  if(nodeData == undefined){
		            		  		return 0;
		            		  	}
						        return nodeData;
						  }
		              },
		              {key: 'status',remind: '状态',text: '状态',
		            	  template: function(nodeData, rowData){
		            		  if(nodeData == undefined){
		            		  		return 0;
		            		  }
		            		  if(nodeData == 'ERROR' || nodeData == 'FAIL'){
		            			  return "<span style=\"color:red;\">"+getBatchStatus(nodeData)+"</span>";
		            		  }
						      return getBatchStatus(nodeData);
		            	  }
		              },
		              {key: 'createdDate',remind: '时间',text: '时间',
		            	  template: function(nodeData, rowData){
						      return formatDate(nodeData);
		            	  }
		              },
		              {key: 'null1',remind: '操作',text: '操作',
		            	  template: function(nodeData, rowData){
		            		  	var str = "<div style=\"position:relative;\"><span onclick=\"showTools(this)\" class=\"tools\" style=\"right:5px;cursor: pointer;\"><img src=\"/theme/basic/images/tools.png\"></span>";
								str += "<ul style=\"position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:13px;left:-27px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);\" class=\"toolbtns\">";
								str += "<li><a href=\"javascript:void(0)\" onclick=\"queryCheckBatchByNo('"+rowData.batchNo+"')\">查看详细</a></li>";
								str += "<li><a href=\"javascript:void(0)\" onclick=\"showCheckMistake('"+rowData.batchNo+"',"+rowData.billDate+")\">差错信息</a></li>";
								if(rowData.status == 'INIT' || rowData.status == 'SUCCESS' || rowData.status == 'NOBILL'){
									str += "<li><a href=\"javascript:void(0)\" onclick=\"failCheckStatus('"+rowData.id+"',this)\">置为失败</li>";
								}
								str += "</ur></div>";
						        return str;
						  }
		              }
		            ]
	});
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

//查看差错信息
function showCheckMistake(batchNo,times){
	//为差错查询条件付初始值
	$("#batchNo").val(batchNo);
	var date = DateUtil.longToDate(times);
	$("#mistakeStartTime").val(DateUtil.dateToStr('yyyy-MM-dd HH:mm:ss',DateUtil.minTimeOfDay(date)));
	//$("#mistakeEndTime").val(DateUtil.dateToStr('yyyy-MM-dd HH:mm:ss',DateUtil.maxTimeOfDay(date)));
	$("#mistakeTab").attr("data-query",false);
	$("#mistakeTab").click();
}

//查询流水详细信息
function queryCheckBatchByNo(batchNo){
	//参数拼接
	$.ajax({
		type:"POST",
		url:"/reconciliation/getCheckDetail.json",
		dataType:"json",
		data:batchNo,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
        async:false,
		success:function(result) {
			if(result.code == "00000"){
				loadCheckData(result.data);
			}else{
				alert("暂无数据");
			}
			//loadDetailInfo(result);
		},
		error:function(){
		}		
	});
}

//渲染对账信息
function loadCheckData(data){
	if(data != null && data != ""){
		var _obj = $("#checkBatchInfo");
		_obj.find("#batch_no").html(data.batchNo);
		_obj.find("#pay_channel").html(data.payChannel);
		_obj.find("#mistake_count").html(data.mistakeCount);
		_obj.find("#unhandle_mistake_count").html(data.unhandleMistakeCount);
		_obj.find("#trade_count").html(data.tradeCount);
		_obj.find("#bank_trade_count").html(data.bankTradeCount);
		_obj.find("#trade_amount").html(amountFormat(data.tradeAmount)+" 元");
		_obj.find("#bank_trade_amount").html(amountFormat(data.bankTradeAmount)+" 元");
		_obj.find("#bank_fee").html(amountFormat(data.bankFee)+" 元");
		_obj.find("#org_check_file_path").html(data.orgCheckFilePath);
		if(data.orgCheckFilePath != undefined && data.orgCheckFilePath != ""){
			_obj.find("#org_check_file_path").append("<a href=\"/reconciliation/downloadCheckFile.json?id="+data.id+"\" id=\"downloadCheckFile\" target=\"_self\">下载</a>");
		}
		_obj.find("#status").html(getBatchStatus(data.status));
		_obj.find("#check_fail_msg").html(data.checkFailMsg);
		_obj.find("#bank_error_msg").text(data.bankErrorMsg);
		_obj.find("#modified_date").html(formatDate(data.modifiedDate));
		openDiv("checkBatchInfo");
	}else{
		alert("暂无数据");
	}
}

//查询差错详细信息
function queryMistakeByNo(id){
	//参数拼接
	$.ajax({
		type:"POST",
		url:"/reconciliation/getMistakeDetail.json",
		dataType:"json",
		data:id,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
        async:false,
		success:function(result) {
			if(result.code == "00000"){
				loadMistakeData(result.data);
			}else{
				alert("暂无数据");
			}
			//loadDetailInfo(result);
		},
		error:function(){
		}		
	});
}

//渲染差错信息
function loadMistakeData(data){
	if(data != null && data != ""){
		var _obj = $("#mistakeInfo");
		_obj.find("#check_batch_no").html(data.checkBatchNo);
		_obj.find("#transaction_id").html(data.transactionId);
		_obj.find("#bank_trx_no").html(data.bankTrxNo);
		_obj.find("#order_amount").html(amountFormat(data.orderAmount)+" 元");
		_obj.find("#bank_amount").html(amountFormat(data.bankAmount)+" 元");
		_obj.find("#fee_amount").html(amountFormat(data.feeAmount)+" 元");
		_obj.find("#trade_time").html(formatDate(data.tradeTime));
		_obj.find("#bank_trade_time").html(formatDate(data.bankTradeTime));
		_obj.find("#error_type").html(getMistakeType(data.errorType));
		_obj.find("#handle_status").html(getMistakeHandleStatus(data.handleStatus));
		_obj.find("#operator_name").html(data.operatorName);
		_obj.find("#modified_date").html(formatDate(data.modifiedDate));
		openDiv("mistakeInfo");
	}else{
		alert("暂无数据");
	}
}

//重置对账状态
function failCheckStatus(id,thisObj){
	$.ajax({
		type:"POST",
		url:"/reconciliation/failCheckStatus.json",
		dataType:"json",
		data:id,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
        async:false,
		success:function(result) {
			if(result.code == "00000"){
				queryCheckAgain();
			}else{
				alert(result.msg);
			}
			//loadDetailInfo(result);
		},
		error:function(){
		}		
	});
}

//重置对账状态
function reconciliationAgain(){
	$.ajax({
		type:"POST",
		url:"/reconciliation/reconciliationAgain.json",
		dataType:"json",
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
        async:false,
		success:function(result) {
			if(result.code == "00000"){
				alert("对账任务已执行，请稍后进行查询对账最新信息");
			}else{
				alert(result.msg);
			}
			//loadDetailInfo(result);
		},
		error:function(){
		}		
	});
}

function openDiv(divId){
	$("#"+divId).css("display","block");
	$(".sweet-overlay").css("display","block");
}

function closeDiv(divId){
	//隐藏狂
	$("#"+divId).css("display","none");
	$(".sweet-overlay").css("display","none");
}
