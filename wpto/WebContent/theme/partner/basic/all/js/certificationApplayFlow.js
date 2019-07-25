$(document).ready(function(){
	loadTable();
	$("#opApplyFlow").click(function(){
		var parentObj = $("#applyFlowInfo");
		var id= parentObj.find("span[name='id']").text();
		var memberNo = parentObj.find("span[name='memberNo']").text();
		var merchantNo = parentObj.find("span[name='merchantNo']").text();
		showOpDiv(id,memberNo,merchantNo);
	});
	
	$("#submitAudit").click(function(){
		var parentObj = $("#updateFlowInfo");
		var id = parentObj.find("span[name='id']").text();
		var status = parentObj.find("select[name='applyFlowStatus'] option:selected").val().trim();
		var remark = parentObj.find("textarea[name='remark']").val();
		auditFlow(id,status,remark);
	});
	
	$(".preview_img").zoomify({
		scale:0.9
	});
	
	$(".preview_img").on("zoom-out.zoomify",function(){
		$("#content-info").css("overflow-y","auto");
		$(this).hide();
	});
	
	$(".preview").click(function(){
		$("#content-info").css("overflow-y","inherit");
		$(this).next().trigger("click");
	});
});


//查询事件
function queryAgain(){
	var merchantNo = $("#merchantNo").val().trim();
	var memberNo = $("#memberNo").val().trim();
	var applyFlowStatus = $("#applyFlowStatus option:selected").val().trim();
	
	//参数拼接
	var param = {
	        "merchantNo": merchantNo,
	        "memberNo": memberNo,
	        "applyFlowStatus": applyFlowStatus,
	        "pSize":10
	};
	
	$('#grid_table').GM('setQuery',param);
	
}

function loadTable(){
	$('#grid_table').GM({
		supportRemind: true
		,gridManagerName: 'test'
		,supportAjaxPage:true
		,supportSorting: false
		,ajax_url: '/certificationApplyFlow/getCertificationApplyFlow.json'
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
		,responseHandler: function(result){
			
	    }
		,columnData: [
		              {key: 'id',remind: '申请单号',text: '申请单号'},
		              {key: 'memberNo',remind: '会员号',text: '会员号'}, 
		              {key : 'merchantNo',remind : '商户号',text : '商户号'},
		              {key: 'enterpriseName',remind: '企业名称',text: '企业名称'},
		              {key: 'certificationType',remind: '认证类型',text: '认证类型',
		            	  template: function(nodeData, rowData){
						        return getCertificateType(nodeData);
						  }
		              },
		              {key: 'auditStatus',remind: '编辑状态',text: '编辑状态',
		            	  template: function(nodeData, rowData){
						        return getAuditStatus(nodeData);
						  }
		              },
		              {key: 'applyFlowStatus',remind: '审核状态',text: '审核状态',
		            	  template: function(nodeData, rowData){
						        return getApplyFlowStatus(nodeData);
						  }
		              },
		              {key: 'createdDate',remind: '创建时间',text: '创建时间',
		            	  template: function(nodeData, rowData){
						        return formatDate(nodeData);
						  }
		              },
		              {key: 'modifiedDate',remind: '更新时间',text: '更新时间',
		            	  template: function(nodeData, rowData){
						        return formatDate(nodeData);
						  }
		              },
		              {key: '',remind: '操作',text: '操作',
						template: function(nodeData, rowData){
							//根据状态判断
							var status = rowData.applyFlowStatus;
							var str = "<div style=\"position:relative;\"><span onclick=\"showTools(this)\" class=\"tools\" style=\"right:5px;cursor: pointer;\"><img src=\"/theme/basic/images/tools.png\"></span>";
							str += "<ul style=\"position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:13px;left:-27px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);\" class=\"toolbtns\">";
							str += "<li><a href=\"javascript:void(0)\" onclick=\"getFlow('"+rowData.id+"')\">查看详细</a></li>";
							str += "</ur></div>";
					        return str;
					    }
		              }
		      ]
	});
}

//重新编辑申请单
function getFlow(id){
	$.ajax({
		type:"GET",
		url:"/certificationApplyFlow/get.json",
		dataType:"json",
		data:{'id':id},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				loadData(result.data);
			}else{
				alert("系统异常，请稍后重试");
			}
		},
		error:function(){
			
		}		
	});
}

//加载详细数据
function loadData(data){
	if(data != null && data != ""){
		//提前情况所有数据
		var parentObj = $("#applyFlowInfo");
		parentObj.find(".rowValue").text("");
		var certificationType = data.certificationType;
		//切换类型
		
		parentObj.find(".single").css("display","none");
		parentObj.find("."+certificationType).css("display","block");
		var spanArr = parentObj.find(".share").find(".rowValue");
		var spanArrT = parentObj.find("."+certificationType).find(".rowValue");
		$(spanArr).each(function(){
			var name = $(this).attr("name");
			var v = $(data).attr(name);
			if($(this).hasClass('preview')){
				$(this).html("点击查看");
				$(this).next().attr("src",normalPreUrl($(data).attr("id"),v));
				return true;
			}
			//特殊情况处理
			if(name == 'enterpriseType'){
				v = getEnterpriseType(v);
			}else if(name == 'certificationType'){
				v = getCertificateType(v);
			}else if(name == 'idType'){
				v = getIdType(v);
			}else if(name == 'auditStatus'){
				v = getAuditStatus(v);
			}else if(name == 'applyFlowStatus'){
				v = getApplyFlowStatus(v);
			}else if(name == 'createdDate' || name == 'modifiedDate'){
				v = formatDate(v);
			}else if(name == 'idTermType'){
				if(v == 'LONG'){
					v = getTermType(v);
				}else{
					v = formatDate($(data).attr($(this).attr("data-dateStr")));
				}
			}
			
			$(this).text(v);
		});
		$(spanArrT).each(function(){
			var name = $(this).attr("name");
			var v = $(data).attr(name);
			if($(this).hasClass('preview')){
				$(this).html("点击查看");
				$(this).next().attr("src",normalPreUrl($(data).attr("id"),v));
				return true;
			}
			if(name == 'address'){
				v = $(data).attr("province") + " " + $(data).attr("city") +" "+ $(data).attr("address");
			}else if(name == 'blicTrcTermType' || name == 'licenseTermType'){
				if(v == 'LONG'){
					v = getTermType(v);
				}else{
					v = formatDate($(data).attr($(this).attr("data-dateStr")));
				}
			}
			$(this).text(v);
		});
		//判断是否展示操作按钮
		if(data.applyFlowStatus == 'WAIT'){
			parentObj.find("#opApplyFlow").css("display","block");
		}else{
			parentObj.find("#opApplyFlow").css("display","none");
		}
		showDiv("applyFlowInfo");
	}else{
		alert("数据不存在");
	}
}

//重新编辑申请单
function auditFlow(id,status,remark){
	$.ajax({
		type:"POST",
		url:"/certificationApplyFlow/auditApplyFlow.json",
		dataType:"json",
		data:{'id':id,'status':status,'remark':remark},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				closeDiv("updateFlowInfo");
				queryAgain();
			}else{
				alert("系统异常，请稍后重试");
			}
		},
		error:function(){
			
		}		
	});
}

//展示操作框
function showOpDiv(id,memberNo,merchantNo){
	var parentObj = $("#updateFlowInfo");
	parentObj.find("span[name='id']").text(id);
	parentObj.find("span[name='memberNo']").text(memberNo);
	parentObj.find("span[name='merchantNo']").text(memberNo);
	closeDiv("applyFlowInfo");
	showDiv("updateFlowInfo");
}

//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().parent().find(".toolbtns").css("display","none");
	$(obj).next().toggle();
}

//关闭详情div
function closeDiv(divId){
	$(".sweet-overlay").css("display","none");
	$("#"+divId).css("display","none");
}

//展示详情div
function showDiv(divId){
	$(".sweet-overlay").css("display","block");
	$("#"+divId).css("display","block");
}

//返回预览路径
function normalPreUrl(id,fileName){
	return "/previewController/previewNormal.json?id="+id+"&fileName="+fileName;
}