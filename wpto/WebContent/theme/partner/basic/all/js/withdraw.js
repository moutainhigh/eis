var beginData;
var endData;
$(document).ready(function(){
	beginData = DateUtil.dateToStr("yyyy-MM-dd HH:mm:ss",DateUtil.dateAdd('d',-7,new Date()));
	endData = DateUtil.dateToStr("yyyy-MM-dd HH:mm:ss",new Date());
	$("#queryForm").find("input[name='beginTimeStart']").flatpickr({
        allowInput:false,
        enableTime:true,
        enableSeconds:true,
        time_24hr:true,
		defaultDate:DateUtil.dateAdd('d',-7,new Date())
	});

	$("#queryForm").find("input[name='beginTimeEnd']").flatpickr({
		allowInput:false,
		enableTime:true,
		enableSeconds:true,
		time_24hr:true,
		defaultDate:new Date()
	});

	$("#queryForm").find("select[name='merchantNo']").selectMania({
		size: 'small',
		themes: ['square','red'],
		placeholder: 'Please select me!',
		removable: true,
		search: true,
	});
    //loadSubTable();
	loadTable();
	var param = {
		"beginTimeStart": beginData,
		"beginTimeEnd": endData
	};
	queryStatistics(param);
});

//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().find("toolbtns").css("display","none");
	$(obj).next().toggle();
}


//查询事件
function queryWithdrawAgain(){
    var _obj = $("#queryForm");
    var beginTimeStart = _obj.find("input[name='beginTimeStart']").val();
    var beginTimeEnd = _obj.find("input[name='beginTimeEnd']").val();
    var merchantNo = _obj.find("input[name='merchantNo']").val();
    var inOrderId = _obj.find("input[name='inOrderId']").val();
    var transactionId = _obj.find("input[name='transactionId']").val();
    var bankAccountName = _obj.find("input[name='bankAccountName']").val();
    var bankAccountNumber = _obj.find("input[name='bankAccountNumber']").val();
    var currentStatus = _obj.find("select[name='currentStatus']").find("option:selected").val().trim();
	var bankName = _obj.find("select[name='bankName']").find("option:selected").val().trim();
    var param = {
        "beginTimeStart": beginTimeStart,
        "beginTimeEnd": beginTimeEnd,
        "uuid": merchantNo,
        "inOrderId": inOrderId,
        "transactionId": transactionId,
        "bankName": bankName,
        "bankAccountNumber": bankAccountNumber,
        "currentStatus": currentStatus,
        "beginTimeStart": beginTimeStart,
		"bankAccountName": bankAccountName,
        "pSize":10
    };
	$('#grid_table').GM('setQuery',param,true);
	queryStatistics(param);
}


function loadTable(){
	$('#grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/withdrawManager/list.json'
		,ajax_type: 'POST'
		,pageSize: 10
		,query: {"beginTimeStart": beginData,"beginTimeEnd": endData,}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,columnData: [
		              {key: 'merchantName',remind: '出款用户',text: '出款用户'},
		              {key: 'transactionId',remind: '系统订单号',text: '系统订单号'},
		              {key: 'inOrderId',remind: '商户订单号',text: '商户订单号'},
		              {key: 'withdrawMoney',remind: '出款金额',text: '出款金额'},
		              {key: 'totalRequest',remind: '批付数量',text: '批付数量',
		            	  template: function(nodeData, rowData){
		            		  	if(nodeData == undefined || nodeData == 0){
		            		  		return 1;
		            		  	}else{
		            		  		return nodeData;
		            		  	}
						  }
		              },
						  {key: 'beginTime',remind: '开始时间',text: '开始时间',
							  template: function(nodeData, rowData){
								  if(nodeData == undefined){
									  return '';
								  }else{
									  return formatDate(nodeData);
								  }
							  }
						  },
						{key: 'endTime',remind: '完成时间',text: '完成时间',
							template: function(nodeData, rowData){
								if(nodeData == undefined){
									return '';
								}else{
									return formatDate(nodeData);
								}
							}
						},
						{key: 'bankAccount',remind: '收款账户',text: '收款账户',
							template: function(nodeData, rowData){
								if(nodeData == undefined){
									return '';
								}else{
									var dataObj = JSON.parse(nodeData);
									var str = dataObj.bankName+" "+dataObj.bankAccountName+" "+dataObj.bankAccountNumber;
									return str;
								}
						}},
						{key: 'currentStatus',remind: '状态',text: '状态',
							template: function(nodeData, rowData){
								return getOldTradeStatus(nodeData);
							}
						},
                        {key: '',remind: '操作',text: '操作',
                            template: function(nodeData, rowData){
                                //根据状态判断
                                var str = "<div style=\"position:relative;\"><span onclick=\"showTools(this)\" class=\"tools\" style=\"right:5px;cursor: pointer;\"><img src=\"/theme/basic/images/tools.png\"></span>";
                                str += "<ul style=\"position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:13px;left:-27px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);\" class=\"toolbtns\">";
                                str += "<li><a href=\"javascript:void(0)\" onclick=\"queryWithdrawById('"+rowData.withdrawId+"')\">查看详细</a></li>";
								if(rowData.totalRequest > 0){
									str += "<li><a href=\"javascript:void(0)\" onclick=\"querySubAgain('"+rowData.transactionId+"')\">子订单</a></li>";
								}
								str += "<li><a href=\"javascript:void(0)\" onclick=\"finishBilling('"+rowData.transactionId+"')\">完成结算</a></li>";
								str += "<li><a href=\"javascript:void(0)\" onclick=\"retransmission('"+rowData.transactionId+"')\">重发通知</a></li>";
								if(rowData.currentStatus == '710013'){
									str += "<li><a href=\"javascript:void(0)\" onclick=\"Reconfirm('"+rowData.transactionId+"')\">再次确认</a></li>";
								}
								str += "</ur></div>";
                                return str;
                            }
                        }
					 ]
	});
}
var flag = false;
function querySubAgain(transactionId){
	if(!flag){
		flag = true;
		loadSubTable(transactionId);
	}else{
		//参数拼接
		var param = {
			"parentTransactionId": transactionId
		};
		$('#showSub').find("#showSubInfo").GM('setQuery',param,true);

	}

	openDiv("showSub");

}
function loadSubTable(transactionId){
	$('#showSub').find("#showSubInfo").GM({
		supportRemind: true
		,gridManagerName: 'test1'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/withdrawManager/listSub.json'
		,ajax_type: 'POST'
		,pageSize: 100
		,query: {"parentTransactionId": transactionId}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,responseHandler: function(result){
			$('#showSub').find(".page-toolbar").css("display","none");
		}
		,columnData: [
			{key: 'transactionId',remind: '系统订单号',text: '系统订单号'},
			{key: 'outOrderId',remind: '渠道订单号',text: '渠道订单号'},
			{key: 'withdrawMoney',remind: '出款金额',text: '出款金额'},
			{key: 'bankAccount',remind: '收款账户',text: '收款账户',
				template: function(nodeData, rowData){
					if(nodeData == undefined){
						return '';
					}else{
						var dataObj = JSON.parse(nodeData);
						var str = dataObj.bankName+" "+dataObj.bankAccountName+" "+dataObj.bankAccountNumber;
						return str;
					}
				}},
			{key: 'currentStatus',remind: '状态',text: '状态',
				template: function(nodeData, rowData){
					return getOldTradeStatus(nodeData);
				}
			},
			{key: 'data',remind: '上游数据',text: '上游数据',
				template: function(nodeData, rowData){
					if(nodeData == undefined){
						return '';
					}else{
						var dataObj = JSON.parse(nodeData);
						var str = dataObj.rowResponse;
						return str;
					}
				}
			}
		]
	});

}

//查询提现详细信息
function queryWithdrawById(withdrawId){
	//参数拼接
    $.ajax({
        type:"POST",
        url:"/withdrawManager/get.json",
        dataType:"json",
        data:withdrawId,
        headers : {
            'Content-Type' : 'application/json;charset=utf-8'
        },
        success:function(result) {
            loadDetailInfo(result.data);
        },
        error:function(){

        }
    });
}

function loadDetailInfo(data){
	if(data == undefined || data == ''){
		alert("没有获取到相关数据");
	}else{
		//开始渲染
		var _obj = $("#showWithdrawInfo");
		_obj.find(".rowValue").each(function(){
			var name = $(this).attr("name");
			var v = $(data).attr(name);
			if(name == 'beginTime' || name == 'endTime'){
				v = formatDate(v);
			}
			if(name == 'data'){
				v = JSON.parse(v).rowResponse;
			}
			if(name == 'bankAccount'){
				var dataObj = JSON.parse(v);
				v = dataObj.bankName+" "+dataObj.bankAccountName+" "+dataObj.bankAccountNumber;
			}
			if(name == 'currentStatus'){
				v = getOldTradeStatus(v);
			}
			$(this).html(v);
		});
		openDiv("showWithdrawInfo");
	}
}

//查询提现子订单
function queryStatistics(param){
 	//参数拼接
 	$.ajax({
		type:"POST",
		url:"/withdrawManager/statistics.json",
		dataType:"json",
 		data:param,

		success:function(result) {
			$("#totalCount").html(0);
			$("#totalWithdrawMoney").html(0);
			$("#totalSuccessMoney").html(0);
 			if(result.code == '00000'){
				var data = result.data;
				$("#totalCount").html(data.totalCount);
				$("#totalWithdrawMoney").html(data.totalWithdrawMoney);
				$("#totalSuccessMoney").html(data.totalSuccessMoney);
			}
		},
 		error:function(){

 		}
 	});
 }

function retransmission(a) {
	$.ajax({
		type:"GET",
		url:"/withdrawManager/notify.json?transactionId="+a,
		dataType:"json",
		success:function(data){
			var backnum = data.message.operateCode;
			var backmessage = data.message.message;
			var alerttext = '';
			if(backnum==102008){
				alerttext=backmessage+'发送成功！';
			}else{
				alerttext=backmessage;
			}
			swal(alerttext);
		}
	})
}

//再次确认
function Reconfirm(a) {
	var dataarry = {'transactionId': a};
	$.ajax({
		type: "GET",
		url: "/withdrawManager/confirm.json?confirmTransactionId=" + a,
		dataType: "json",
		data: dataarry,
		success: function (data) {
			swal('再次确认成功！');
		},
		error: function () {
			swal('再次确认失败！');
		}
	});
}

function finishBilling(transactionId) {
	var dataarry = {'transactionId':transactionId}
	$.ajax({
		type:"POST",
		url:"/withdrawManager/update.json",
		dataType:"json",
		data:dataarry,
		success:function(data) {
			swal('出款交易ID'+transactionId+'结算完成！');

		},
		error:function(){
			swal('出款交易ID'+transactionId+'结算失败！')
		}
	})
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

