$(document).ready(function(){
	loadTable();
	//为选择框填充初始数据
	var str = createOptionForAccountType("");
	$("#accountType").html(str);
	$("#memberNo").blur(function(){
		if($(this).val().trim() == ""){
			$("#accountType").html(str);
		}else{
			queryMemberInfoByNo($(this).val().trim(),"accountType");
		}
	});
	$("#memberNoCreate").blur(function(){
		if($(this).val().trim() == ""){
			$("#accountTypeCreate").html(str);
		}else{
			queryMemberInfoByNo($(this).val().trim(),"accountTypeCreate");
		}
	});
});



//查询流水详细信息
function queryMemberInfoByNo(memberNo,objId){
	//参数拼接
	$.ajax({
		type:"POST",
		url:"/memberInfo/getDetail.json",
		dataType:"json",
		data:memberNo,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
        async:false,
		success:function(result) {
			if(result.code == "00000"){
				if(result.data == ""){
					$("#"+objId).html(createOptionForAccountType(""));
				}else{
					$("#"+objId).html(createOptionForAccountType(result.data.memberType));
				}
			}
			//loadDetailInfo(result);
		},
		error:function(){
		}		
	});
}
//详细信息数据处理
function loadDetailInfo(result){
	if(result.code == "00000"){
		//数据处理
		var data = result.data;
		$("#member_no").html(data.memberNo);
		$("#merchant_no").html(data.merchantNo);
		$("#member_type").html(getMemberType(data.memberType));
		$("#alias_name").html(data.aliasName);
		$("#remark").html(data.remark);
		$("#member_status").html(getMemberStatus(data.memberStatus));
		openDiv("flowDetailInfo");
	}else{
		//无数据
		alert("无数据信息！");
	}
	
}

function createAccount(){
	
	var memberNo = $("#memberNoCreate").val().trim();
	var accountType = $("#accountTypeCreate option:selected").val().trim();
	var accountName = $("#accountNameCreate").val().trim();
	var accountStatus = $("#accountStatusCreate option:selected").val().trim();
	var accountInfo = {
		   "memberNo": memberNo,
		   "accountType": accountType,
		   "accountName": accountName,
		   "accountStatus": accountStatus
	};
	
	$.ajax({
		type:"POST",
		url:"/accountInfo/create.json",
		dataType:"json",
		data:JSON.stringify(accountInfo),
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			if(result.code == "00000"){
				swal("新增账户成功");
				closeDiv("createAccountInfo");
				$("#queryInfo").trigger("click");
			}else{
				swal("新增失败，"+result.msg);
			}
		},
		error:function(){
			swal("系统异常");
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

//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().find("toolbtns").css("display","none");
	$(obj).next().toggle();
}

function showCreate(){
	if($("#memberNoCreate").val().trim() == ""){
		$("#accountTypeCreate").html(createOptionForAccountType(""));
	}else{
		queryMemberInfoByNo($("#memberNoCreate").val().trim(),"accountTypeCreate");
	}
	openDiv("createAccountInfo");
}

//查询事件
function queryAgain(){
	var merchantNo = $("#merchantNo").val().trim();
	var memberNo = $("#memberNo").val().trim();
	var accountNo = $("#accountNo").val().trim();
	var accountType = $("#accountType option:selected").val().trim();
	var accountStatus = $("#accountStatus option:selected").val().trim();
	
	//参数拼接
	var param = {
	        "merchantNo": merchantNo,
	        "memberNo": memberNo,
	        "accountNo": accountNo,
	        "accountType": accountType,
	        "accountStatus": accountStatus
	};
	
	$('#grid_table').GM('setQuery',param);
	
}

function loadTable(){
	$('#grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
//			,disableCache:true
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/accountInfo/get.json'
		,ajax_type: 'POST'
		,pageSize: 10
		,query: {}
		,supportDrag:false
		,supportRemind:false
		,supportSorting:false
		,supportAutoOrder:false
		,height:'auto'
		,supportCheckbox:false
		,supportConfig:false
		,columnData: [
		              {key: 'accountNo',remind: '账户号',text: '账户号'},
		              {key: 'memberNo',remind: '会员号',text: '会员号'}, 
		              {key : 'merchantNo',remind : '商户号',text : '商户号'},
		              {key: 'accountNo',remind: '账户号',text: '账户号'},
		              {key: 'accountType',remind: '账户类型',text: '账户类型',
						template: function(nodeData, rowData){
					        return getAccountType(nodeData);
					    }
		              },
		              {key: 'totalAmount',remind: '总金额',text: '总金额',
						template: function(nodeData, rowData){
					        return amountFormat(nodeData);
					    }
		              },
		              {key: 'availableAmount',remind: '可用余额',text: '可用余额',
						template: function(nodeData, rowData){
					        return amountFormat(nodeData);
					    }
		              },
		              {key: 'freezeAmount',remind: '冻结金额',text: '冻结金额',
						template: function(nodeData, rowData){
					        return amountFormat(nodeData);
					    }
		              },
		              {key: 'riskAmount',remind: '风险金额',text: '风险金额',
						template: function(nodeData, rowData){
					        return amountFormat(nodeData);
					    }
		              },
		              {key: 'currency',remind: '货币类型',text: '货币类型'},
		              {key: 'accountStatus',remind: '账户状态',text: '账户状态',
							template: function(nodeData, rowData){
						        return getAccountStatus(nodeData);
						    }
			          },
		              {key: 'createdDate',remind: '时间',text: '时间',
						template: function(nodeData, rowData){
					        return formatDate(nodeData);
					    }
		              },
		              {key: '',remind: '操作',text: '操作',
						template: function(nodeData, rowData){
					        return formatDate(nodeData);
					    }
		              }
		      ]
	});
}