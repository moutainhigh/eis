$(document).ready(function(){
	loadTable();
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
	var memberStatus = $("#memberStatus option:selected").val().trim();
	
	//参数拼接
	var param = {
	        "merchantNo": merchantNo,
	        "memberNo": memberNo,
	        "memberStatus": memberStatus,
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
		,ajax_url: '/merchantBaseInfo/getMerchantBaseInfo.json'
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
		              {key: 'isRealname',remind: '是否实名',text: '是否实名',
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
					        return formatDate(nodeData);
					    }
		              }
		      ]
	});
}