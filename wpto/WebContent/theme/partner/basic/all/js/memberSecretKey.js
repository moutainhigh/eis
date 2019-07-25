$(document).ready(function(){
	loadTable();
	
	$("#createKey").find(".makePassword").click(function(){
		var type = $("#createKey").find("select[name='secretKeyType']").val();
		var key = makeKey(type);
		$(this).prev().val(key);
	});
	
	$("#updateKey").find(".makePassword").click(function(){
		var type = $("#updateKey").find("span[name='secretKeyType']").text();
		var key = makeKey(type);
		$(this).prev().val(key);
	});
	
	
});

function makeKey(type){
	var str = "";
	if(type == "common"){
		str = makeCommonKey(8);
	}else if(type == "MD5"){
		str = makeMD5Key(8);
	}else if(type == "DES3" || type == "3DES" ||  type == "SHA-256"){
		str = make3DesKey();
	}
	return str;
}

//查询事件
function queryAgain(){
	var _obj = $("#queryForm");
	var memberNo = _obj.find("#memberNo").val().trim();
	var productType = _obj.find("#productType option:selected").val().trim();
	var secretKeyType = _obj.find("#secretKeyType option:selected").val().trim();
	var status = _obj.find("#status option:selected").val().trim();
	
	//参数拼接
	var param = {
	        "memberNo": memberNo,
	        "productType": productType,
	        "secretKeyType": secretKeyType,
	        "status": status,
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
		,ajax_url: '/memberSecretKey/listSecretKey.json'
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
		              {key: 'memberNo',remind: '会员号',text: '会员号'}, 
		              {key : 'productType',remind : '产品类型',text : '产品类型',
		            	  template: function(nodeData, rowData){
						        return getProductType(nodeData);
						  }
		              },
		              {key: 'secretKeyType',remind: '秘钥类型',text: '秘钥类型',
		            	  template: function(nodeData, rowData){
						        return getSecretKeyType(nodeData);
						  }
		              },
		              {key: 'dataValue',remind: '秘钥',text: '秘钥'},
		              {key: 'status',remind: '状态',text: '状态',
		            	  template: function(nodeData, rowData){
						        return getSecretKeyStatus(nodeData);
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
							var str = "<div style=\"position:relative;\"><span onclick=\"showTools(this)\" class=\"tools\" style=\"right:5px;cursor: pointer;\"><img src=\"/theme/basic/images/tools.png\"></span>";
							str += "<ul style=\"position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:13px;left:-27px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);\" class=\"toolbtns\">";
							str += "<li><a href=\"javascript:void(0)\" onclick=\"showUpdateStatus('"+rowData.id+"')\">更新状态</a></li>";
							str += "<li><a href=\"javascript:void(0)\" onclick=\"showUpdate('"+rowData.id+"')\">更新秘钥</a></li>";
							str += "<li><a href=\"javascript:void(0)\" onclick=\"deleteKey('"+rowData.id+"')\">删除秘钥</a></li>";
							str += "</ur></div>";
					        return str;
					    }
		              }
		      ]
	});
}

//重新编辑申请单
function getKey(id){
	var data;
	$.ajax({
		type:"GET",
		url:"/memberSecretKey/get.json",
		dataType:"json",
		data:{'id':id},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				data = result.data;
			}
		},
		error:function(){
			
		}		
	});
	return data;
}


//提交秘钥
function submitKey(){
	//参数验证
	var flag = true;
	var _obj = $("#createKey");
	_obj.find(".notNull").each(function(){
		if($(this).val().trim() == ""){
			flag = false;
			_obj.find(".error").html($(this).attr("data-warn")+"不能为空");
			return false;
		}
	});
	
	if(!flag){
		return;
	}
	_obj.find(".error").html("");
	
	//参数拼接
	var param = {};
	_obj.find(".needSubmit").each(function(){
		var name = $(this).attr("name");
		var v = $(this).val();
		param[name]=v;
	});
	
	$.ajax({
		type:"POST",
		url:"/memberSecretKey/save.json",
		dataType:"json",
		data:JSON.stringify(param),
		headers : {
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				queryAgain();
			}else{
				alert(result.msg);
			}
			closeDiv('createKey');
		},
		error:function(){
			
		}		
	});
}

//提交更新秘钥
function submitUpdateKey(){
	var _obj = $("#updateKey");
	var id = _obj.find("#keyId").text();
	var dataValue = _obj.find("input[name='dataValue']").val();
	if(dataValue == ""){
		_obj.find(".error").html(_obj.find("input[name='dataValue']").attr("data-warn")+"不能为空");
		return;
	}
	_obj.find(".error").html("");
	$.ajax({
		type:"POST",
		url:"/memberSecretKey/updateKey.json",
		dataType:"json",
		data:{'id':id,'dataValue':dataValue},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				queryAgain();
			}else{
				alert(result.msg);
			}
			closeDiv('updateKey');
		},
		error:function(){
			
		}		
	});
}

//提交更新秘钥
function deleteKey(id){
	$.confirm({
	    title: '提示',
	    content: '确定删除秘钥信息？',
	    confirmButton: '确定',
	    cancelButton: '取消',
	    confirm: function(){
	    	submitDeleteKey(id);
	    },
	    cancel: function(){
	    	
	    }
	});
	
}

function submitDeleteKey(id){
	$.ajax({
		type:"POST",
		url:"/memberSecretKey/delete.json",
		dataType:"json",
		data:{'id':id},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				queryAgain();
			}else{
				alert(result.msg);
			}
		},
		error:function(){
			
		}		
	});
}

//提交更新秘钥
function submitUpdateKeyStatus(){
	var _obj = $("#updateKeyStatus");
	var id = _obj.find("#keyId").text();
	var status = _obj.find(".status").val();
	if(status == ""){
		alert("状态不能为空");
		return;
	}
	$.ajax({
		type:"POST",
		url:"/memberSecretKey/updateKeyStatus.json",
		dataType:"json",
		data:{'id':id,'status':status},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				queryAgain();
			}else{
				alert(result.msg);
			}
			closeDiv('updateKeyStatus');
		},
		error:function(){
			
		}		
	});
}

function showCreate(){
	//重置信息
	var _obj = $("#createKey");
	_obj.find(".notNull").each(function(){
		$(this).val("");
	});
	showDiv('createKey');
}

function showUpdate(id){
	var data = getKey(id);
	if(data == null || data == undefined){
		alert("数据获取失败");
		return;
	}
	//数据渲染
	var _obj = $("#updateKey");
	_obj.find(".rowValue").each(function(){
		var key = $(this).attr("name");
		var v = $(data).attr(key);
		if(key == "productType"){
			v = getProductType(v);
		}
		if(key == "secretKeyType"){
			v = getSecretKeyType(v);
		}
		$(this).text(v);
	});
	_obj.find("input[name='dataValue']").val(data.dataValue);
	showDiv('updateKey');
}

function showUpdateStatus(id){
	var data = getKey(id);
	if(data == null || data == undefined){
		alert("数据获取失败");
		return;
	}
	//数据渲染
	var _obj = $("#updateKeyStatus");
	_obj.find(".rowValue").each(function(){
		var key = $(this).attr("name");
		var v = $(data).attr(key);
		if(name == "productType"){
			v = getProductType(v);
		}
		if(name == "secretKeyType"){
			v = getSecretKeyType(v);
		}
		$(this).text(v);
	});
	_obj.find(".status").val(data.dataValue);
	showDiv('updateKeyStatus');
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
