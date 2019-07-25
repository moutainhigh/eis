var totalPage = 1;
$(document).ready(function(){
	loadTable();
});

//查询流水详细信息
function queryMemberInfoByNo(memberNo){
	
	//参数拼接
	$.ajax({
		type:"POST",
		url:"/memberInfo/getDetail.json",
		dataType:"json",
		data:memberNo,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			loadDetailInfo(result);
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

function createMember(){
	var merchantNo = $("#merchantNoCreate").val().trim();
	var memberType = $("#memberTypeCreate option:selected").val().trim();
	var aliasName = $("#aliasNameCreate").val().trim();
	var remark = $("#remarkCreate").val().trim();
	var memberStatus = $("#memberStatusCreate option:selected").val().trim();
	var memberName = $("#memberNameCreate").val().trim();
	
	var memberInfo = {
		   "merchantNo": merchantNo,
		   "memberType": memberType,
		   "aliasName": aliasName,
		   "remark": remark,
		   "memberStatus": memberStatus,
		   "memberName":memberName
	};
	
	$.ajax({
		type:"POST",
		url:"/memberInfo/create.json",
		dataType:"json",
		data:JSON.stringify(memberInfo),
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			var code = result.code;
			if(code == "00000"){
				swal("新增会员成功");
				closeDiv("createMember");
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

//查询事件
function queryAgain(){
	var merchantNo = $("#merchantNo").val().trim();
	var memberNo = $("#memberNo").val().trim();
	var memberType = $("#memberType option:selected").val().trim();
	var memberStatus = $("#memberStatus option:selected").val().trim();
	
	//参数拼接
	var param = {
	        "merchantNo": merchantNo,
	        "memberNo": memberNo,
	        "memberType": memberType,
	        "memberStatus": memberStatus
	};
	
	$('#grid_table').GM('setQuery',param);
	
}

function loadTable(){
	$('#grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/memberInfo/get.json'
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
		              {key: 'memberNo',remind: '会员号',text: '会员号'},
		              {key: 'merchantNo',remind: '商户号',text: '商户号'},
		              {key: 'memberName',remind: '会员名',text: '会员名'},
		              {key: 'memberType',remind: '会员类型',text: '会员类型',
		            	  template: function(nodeData, rowData){
						        return getMemberType(nodeData);
						  }
		              },
		              {key: 'memberStatus',remind: '会员状态',text: '会员状态',
		            	  template: function(nodeData, rowData){
						        return getMemberStatus(nodeData);
						  }
		              },
		              {key: 'remark',remind: '备注',text: '备注'},
		              {key: 'createdDate',remind: '时间',text: '时间',
		            	  template: function(nodeData, rowData){
						        return formatDate(nodeData);
						  }
		              },
		              {key: 'null',remind: '操作',text: '操作',
		            	  template: function(nodeData, rowData){
		            		  	var str = "<div style=\"position:relative;\"><span onclick=\"showTools(this)\" class=\"tools\" style=\"right:5px;cursor: pointer;\"><img src=\"/theme/basic/images/tools.png\"></span>";
								str += "<ul style=\"position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -53px; top:22px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);\" class=\"toolbtns\">";
								str += "<li><a href=\"javascript:void(0)\" onclick=\"queryMemberInfoByNo('"+rowData.memberNo+"')\">查看</a></li>";
								str += "<li><a href=\"javascript:void(0)\">修改</a></li>";
								str += "</ur></div>";
						        return str;
						  }
		              }
		            ]
	});
}

//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().parent().find(".toolbtns").css("display","none");
	$(obj).next().toggle();
}