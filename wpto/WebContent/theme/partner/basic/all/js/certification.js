var cerMap = new Map();
var applyMap = new Map();
var applyFlowIdMap = new Map();
$(document).ready(function(){
	loadTable();
	//初始化时间
	$(".flatpickr").flatpickr({
		allowInput:false,
		defaultDate:new Date()
	});
	
	//初始化省市联动
	$(".distpicker").distpicker({
		  province: '请选择省',
		  city: '请选择市',
		  autoSelect:false
	});
	
	//为认证方式绑定点击事件
	$("#certificateType").change(function(){
		var v = $(this).find("option:selected").val();
		changeInfo(v);
	});
	
	$("#submit_aut").click(function(){
		$.confirm({
		    title: '提示',
		    content: '是否直接提交审核！提交后不可编辑。',
		    confirmButton: '提交审核',
		    cancelButton: '保存',
		    confirm: function(){
		    	if($("#submit_aut").data("update")){
		    		submitUpdateApply(true);
		    	}else{
		    		submitApply(true);
		    	}
		    },
		    cancel: function(){
		    	if($("#submit_aut").data("update")){
		    		submitUpdateApply(false);
		    	}else{
		    		submitApply(false);
		    	}
		    }
		});
		//submitApply();
	});
	
	//绑定焦点时间
	$(".certification_info").find(".needSubmit").focus(function(){
		$(this).parent().next().html("");
	});
	
	$(".uploadFile").change(function(){
		var formData = new FormData();
		formData.append("file",$(this)[0].files[0]);
		upload(formData,$(this));
	});
	
	
	$(".uploadFile_form").hover(function(){
		var flag = $(this).find(".preview").is(":visible");
		if(flag){
			//元素存在 展示预览
			$(this).next().css("display","block");
		}
	},function(){
		
	});
	
	
	$(".preview_mask").hover(function(){
		
	},function(){
		$(this).css("display","none");
	});
	
	$(".re_upload").click(function(){
		$(this).parent().prev().find(".uploadFile").trigger("click");
	});
	
	$(".preview_img").zoomify({
		scale:0.9
	});
	
	$(".preview_img").on("zoom-out.zoomify",function(){
		$(this).hide();
	});
	
	$(".mask-sm-box").click(function(){
		$(this).parent().next().trigger("click");
	});
	$(".mask-bg-box").click(function(){
		$(this).parent().next().trigger("click");
	});
});



//展示工具
function showTools(obj){
	$(obj).parent().parent().parent().find("toolbtns").css("display","none");
	$(obj).next().toggle();
}


//查询事件
function queryAgain(){
	var merchantNo = $("#merchantNo").val().trim();
	var memberNo = $("#memberNo").val().trim();
	var isRealname = $("#isRealname option:selected").val().trim();
	
	//参数拼接
	var param = {
	        "merchantNo": merchantNo,
	        "memberNo": memberNo,
	        "isRealname": isRealname,
	        "pSize":10
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
		,ajax_url: '/certification/getCertificationInfo.json'
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
			//计算汇总数据
			if(result.code == "00000"){
				loadCertApplyInfo(result.data);
			}
	    }
		,columnData: [
		              {key: 'merchantNo',remind: '商户号',text: '商户号'},
		              {key: 'memberNo',remind: '会员号',text: '会员号'}, 
		              {key : 'loginName',remind : '登录名',text : '登录名'},
		              {key: 'idName',remind: '姓名',text: '姓名'},
		              {key: 'idType',remind: '证件类型',text: '证件类型',
							template: function(nodeData, rowData){
						        return getIdType(nodeData);
							}
		              },
		              {key: 'idNo',remind: '证件号',text: '证件号'},
		              {key: 'useMobile',remind: '常用手机号',text: '常用手机号'},
		              {key: 'email',remind: '常用邮箱',text: '常用邮箱'},
		              {key: 'realname',remind: '是否实名',text: '是否实名',
							template: function(nodeData, rowData){
								if(nodeData == undefined || !nodeData ){
									return '未实名';
								}
						        return '已实名';
							}
		              },
		              {key: 'memberStatus',remind: '状态',text: '状态',
							template: function(nodeData, rowData){
						        return getMemberStatus(nodeData);
						    }
		              },
		              {key: 'createdDate',remind: '时间',text: '时间',
						template: function(nodeData, rowData){
					        return formatDate(nodeData);
					    }
		              },
		              {key: '',remind: '操作',text: '操作',
						template: function(nodeData, rowData){
							/*
							 * 返回操作列
							 * 如果没有申请单  和  认证信息 则展示认证
							 * 如果认证成功状态 则显示重新认证
							 * 如果认证成功 或者重新认证中 如果申请单是未提交 则继续编辑 如果是打回 则重新编辑
							 */ 
							//默认返回实名认证
							var str = "<a href=\"javascript:void(0)\" onclick=\"toApply('"+rowData.merchantNo+"','"+rowData.memberNo+"')\">实名认证</a>";
							var memberNo = rowData.memberNo;
							if(applyFlowIdMap.size() > 0){
								//查看申请单状态
								var applyStatus = applyMap.get(memberNo);
								if(applyStatus == 'NO_SUB'){
									return "<a href=\"javascript:void(0)\" onclick=\"toUpdate('"+applyFlowIdMap.get(rowData.memberNo)+"')\">继续编辑</a>";;
								}
								if(applyStatus == 'WAIT'){
									return '等待审核';
								}
								if(applyStatus == 'NO_PASS'){
									return "<a href=\"javascript:void(0)\" onclick=\"toUpdate('"+applyFlowIdMap.get(rowData.memberNo)+"')\">重新编辑</a>";;
								}
							}
							
							if(rowData.realname){
								return "<a href=\"javascript:void(0)\" onclick=\"toApply('"+rowData.merchantNo+"','"+rowData.memberNo+"')\">重新认证</a>";
							}
							return str;
					    }
		              }
		      ]
	});
}

function loadCertApplyInfo(data){
	//cerMap = new Map();
	applyMap = new Map();
	applyFlowIdMap = new Map();
	if(data == '' || data == null){
		return '';
	} else {
		var param = new Array();
		$(data).each(function(){
			param.push($(this).attr("memberNo"));
		});
		//queryCertificationInfo(param);
		queryApplyFlow(param);
	}
}

//查询制定人员得信息
function queryCertificationInfo(param){
	$.ajax({
		type:"POST",
		url:"/certification/getCertificationInfoByMemberNo.json",
		dataType:"json",
		data:JSON.stringify(param),
		async:false,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			if(result.code == '00000' && result.data != null && result.data != ''){
				$(result.data).each(function(){
					cerMap.put($(this).attr("memberNo"), $(this).attr("certificationStatus"));
				});
			}
		},
		error:function(){
			
		}		
	});
}

//查询指定人员申请单信息
function queryApplyFlow(param){
	$.ajax({
		type:"POST",
		url:"/certification/getApplyFlowByMemberNo.json",
		dataType:"json",
		data:JSON.stringify(param),
		async:false,
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			if(result.code == '00000' && result.data != null && result.data != ''){
				$(result.data).each(function(){
					applyMap.put($(this).attr("memberNo"), $(this).attr("applyFlowStatus"));
					applyFlowIdMap.put($(this).attr("memberNo"), $(this).attr("id"));
				});
			}
		},
		error:function(){
			
		}		
	});
}

//上传文件
function upload(formData,obj){
	$.ajax({
        type: "POST",
        url: "/upload/uploadFile.json",
        data: formData,
        async: false, 
        contentType: false,  
        processData: false,
        success: function (result) {
        	if(result.code == '00000'){
        		obj.next().val(result.data);
        		var preImg = obj.next().next();
        		preImg.attr("src",tempPreUrl(result.data));
        		preImg.css("display","block");
        		obj.parent().next().next().attr("src",tempPreUrl(result.data));
        	}else{
        		alert("系统错误，请重试");
        	}
        },
        error: function(data) {
            alert("error:"+data.responseText);
        }
    });
}

//验证参数
function Verification(classId){
	var flag = true;
	var notNullArr = $(".share .notNull");
	var notNullArrT = $("."+classId+" .notNull");
	
	var isPhoneArr = $(".share .isPhone");
	var isPhoneArrT = $("."+classId+" .isPhone");
	
	var isMailArr = $(".share .isMail");
	var isMailArrT = $("."+classId+" .isMail");
	
	var isIDarr = $("."+classId+" .isID");
	
	
	notNullArr.data("Verification",true);
	notNullArrT.data("Verification",true);
	isPhoneArr.data("Verification",true);
	isPhoneArrT.data("Verification",true);
	isMailArr.data("Verification",true);
	isMailArrT.data("Verification",true);
	
	//验证参数是否为空
	$(notNullArr).each(function(){
		if($(this).attr("name") == 'bankAccPermitImageName'){
			alert($(this).val().trim());
		}
		if($(this).val().trim() == ""){
			$(this).parent().next().html("不能为空！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	$(notNullArrT).each(function(){
		if($(this).val().trim() == ""){
			$(this).parent().next().html("不能为空！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	//验证是否是合法手机号
	$(isPhoneArr).each(function(){
		if(!jQuery.mCheck($(this).val().trim(),'mobile') && $(this).data("Verification")){
			$(this).parent().next().html("手机号格式不正确！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	$(isPhoneArrT).each(function(){
		if(!jQuery.mCheck($(this).val().trim(),'mobile') && $(this).data("Verification")){
			$(this).parent().next().html("手机号格式不正确！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	//验证邮箱格式是否正确
	$(isMailArr).each(function(){
		if(!jQuery.mCheck($(this).val().trim(),'email') && $(this).data("Verification")){
			$(this).parent().next().html("邮箱格式不正确！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	$(isMailArrT).each(function(){
		if(!jQuery.mCheck($(this).val().trim(),'email') && $(this).data("Verification")){
			$(this).parent().next().html("格式不正确！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	//验证身份证
	$(isIDarr).each(function(){
		if(!jQuery.isIdCard($(this).val().trim()) && $(this).data("Verification")){
			$(this).parent().next().html("格式不正确！");
			$(this).data("Verification",false);
			flag = false;
		}
	});
	
	return flag;
}

//拼接插入参数
function getSaveParam(classId){
	//根据认证类型获取不同参数
	var needSubmitArr = $(".share .needSubmit");
	var needSubmitArrT = $("."+classId+" .needSubmit");
	//拼接开始
	var param = {};
	//获取商户号 和  会员号
	var merchantNo = $("#merchantNo_label").data("merchantNo");
	var memberNo = $("#memberNo_label").data("memberNo");
	param['merchantNo'] = merchantNo;
	param['memberNo'] = memberNo;
	
	$(needSubmitArr).each(function(){
		var v = '';
		var tagName = $(this).prop("tagName");
		if(tagName == 'INPUT'){
			v = $(this).val().trim();
			if($(this).attr("type") == 'radio'){
				//如果此标签值为Long 则直接返回 否则将此标签和时间标签同时返回
				v = $(this).val();
				//如果被选中
				if($(this).prop('checked')){
					if(v != 'LONG'){
						var nextObj = $(this).next();
						param[nextObj.attr("name")]=nextObj.val();
					}
				}else{
					return true;
				}
			}
		}else if(tagName == 'SELECT'){
			v = $(this).find("option:selected").val().trim();
		}
		param[$(this).attr("name")]=v;
	});
	
	$(needSubmitArrT).each(function(){
		var v = '';
		var tagName = $(this).prop("tagName");
		if(tagName == 'INPUT'){
			v = $(this).val().trim();
			if($(this).attr("type") == 'radio'){
				//如果此标签值为Long 则直接返回 否则将此标签和时间标签同时返回
				v = $(this).val();
				//如果被选中
				if($(this).prop('checked')){
					if(v != 'LONG'){
						var nextObj = $(this).next();
						param[nextObj.attr("name")]=nextObj.val();
					}
				}else{
					return true;
				}
			}
		}else if(tagName == 'SELECT'){
			v = $(this).find("option:selected").val().trim();
		}
		param[$(this).attr("name")]=v;
	});
	return param;
}

//提交申请
function submitApply(submit){
	var v = $("#certificateType").find("option:selected").val();
	var flag = Verification(v);
	if(!flag){
		return;
	}
	//获取提交参数
	var param = getSaveParam(v);
	param['submitAudit'] = submit;
	$.ajax({
		type:"POST",
		url:"/certification/saveApplyFlow.json",
		dataType:"json",
		data:JSON.stringify(param),
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				queryAgain();
				cancle();
			}else{
				alert("系统异常，请稍后重试");
			}
		},
		error:function(){
			
		}		
	});
}

//提交申请
function submitUpdateApply(submit){
	var id = $("#submit_aut").data("applyFlowId");
	if(id == ""){
		return;
	}
	var v = $("#certificateType").find("option:selected").val();
	var flag = Verification(v);
	if(!flag){
		return;
	}
	//获取提交参数
	var param = getSaveParam(v);
	param['submitAudit'] = submit;
	param['id'] = id;
	$.ajax({
		type:"POST",
		url:"/certification/updateApplyFlow.json",
		dataType:"json",
		data:JSON.stringify(param),
		headers : {  
            'Content-Type' : 'application/json;charset=utf-8'  
        },
		success:function(result) {
			if(result.code == '00000'){
				alert("操作成功");
				queryAgain();
				cancle();
			}else{
				alert("系统异常，请稍后重试");
			}
		},
		error:function(){
			
		}		
	});
}

//跳转处理
function toApply(merchantNo,memberNo){
	//跳转至申请div
	$("#merchantNo_label").text(merchantNo);
	$("#merchantNo_label").data("merchantNo",merchantNo);
	$("#memberNo_label").text(memberNo);
	$("#memberNo_label").data("memberNo",memberNo);
	$("#cancle_z").css("display","block");
	reset();
	//根据认证方式 去展示认证内容
	var v = $("#certificateType").find("option:selected").val();
	changeInfo(v);
	$(".certification_info").css("display","block");
	$(".grid_info").css("display","none");
	$("#submit_aut").data("update",false);
}

//重新编辑申请单
function toUpdate(id){
	$.ajax({
		type:"GET",
		url:"/certificationApplyFlow/get.json",
		dataType:"json",
		data:{'id':id},
		async:false,
		success:function(result) {
			if(result.code == '00000'){
				setData(result.data);
			}else{
				alert("系统异常，请稍后重试");
			}
		},
		error:function(){
			
		}		
	});
}

//设置数据
function setData(data){
	reset();
	if(data != null && data != ''){
		//根据企业类型 进行初始值处理
		var certificationType = data.certificationType;
		//初始值进行处理
		var merchantNo = data.merchantNo;
		var memberNo = data.memberNo;
		var id = data.id;
		$("#merchantNo_label").data("merchantNo",merchantNo);
		$("#memberNo_label").data("memberNo",memberNo);
		$("#merchantNo_label").text(merchantNo);
		$("#memberNo_label").text(memberNo);
		$("#submit_aut").data("applyFlowId",data.id);
		$("#submit_aut").data("update",true);
		$("#certificateType").val(certificationType);
		changeInfo(certificationType);
		//下面对input框赋值
		var needSubmitArr = $(".share .needSubmit");
		var needSubmitArrT = $("."+certificationType+" .needSubmit");
		$(needSubmitArr).each(function(){
			var tagName = $(this).prop("tagName");
			var key = $(this).attr("name");
			if(tagName == 'INPUT'){
				if($(this).attr("type") == 'radio'){
					if($(this).val() == $(data).attr(key)){
						$(this).prop("checked",true);
						if($(this).val() != 'LONG'){
							var nextObj = $(this).next();
							var nextKey = nextObj.attr("name");
							nextObj.val($(data).attr(nextKey));
						}
					}else{
						return true;
					}
				}else{
					$(this).val($(data).attr(key));
					if($(this).hasClass("needPreview")){
						//预览
						$(this).next().attr("src",normalPreUrl(id,$(data).attr(key)));
						$(this).next().css("display","block");
						$(this).parent().parent().find(".preview_img").attr("src",normalPreUrl(id,$(data).attr(key)));
					}
				}
			}else if(tagName == 'SELECT'){
				$(this).val($(data).attr(key));
				$(this).change();
			}
		});
		
		$(needSubmitArrT).each(function(){
			var tagName = $(this).prop("tagName");
			var key = $(this).attr("name");
			if(tagName == 'INPUT'){
				if($(this).attr("type") == 'radio'){
					if($(this).val() == $(data).attr(key)){
						$(this).prop("checked",true);
						if($(this).val() != 'LONG'){
							var nextObj = $(this).next();
							var nextKey = nextObj.attr("name");
							nextObj.val($(data).attr(nextKey));
						}
					}else{
						return true;
					}
				}else{
					$(this).val($(data).attr(key));
					if($(this).hasClass("needPreview")){
						//预览
						$(this).next().attr("src",normalPreUrl(id,$(data).attr(key)));
						$(this).next().css("display","block");
						$(this).parent().parent().find(".preview_img").attr("src",normalPreUrl(id,$(data).attr(key)));
					}
				}
			}else if(tagName == 'SELECT'){
				$(this).val($(data).attr(key));
				$(this).change();
			}
		});
		$(".certification_info").css("display","block");
		$(".grid_info").css("display","none");
		$("#cancle_z").css("display","block");
	}else{
		alert("数据不存在");
	}
}

//重置所有信息
function reset(){
	//清理input  恢复select  时间选择   文件上传 radio恢复默认
	var _obj = $(".certification_info");
	_obj.find("input[type='text']").val("");
	_obj.find("input[type='file']").val("");
	_obj.find("input[type='hidden']").val("");
	var _select = _obj.find("select");
	$(_select).each(function(){
		$(this).children().eq(0).attr("selected",true);
	});
}

//取消认证
function cancle(){
	$(".certification_info").css("display","none");
	$(".grid_info").css("display","block");
	$("#cancle_z").css("display","none");
}



//切换认证内容
function changeInfo(label){
	$('.base_info:not(.share)').css("display","none");
	$(".certification_info ."+label).css("display","block");
}

//返回预览路径
function normalPreUrl(id,fileName){
	return "/previewController/previewNormal.json?id="+id+"&fileName="+fileName;
}

//返回临时预览路径
function tempPreUrl(fileName){
	return "/previewController/previewTemp.json?fileName="+fileName;
}
