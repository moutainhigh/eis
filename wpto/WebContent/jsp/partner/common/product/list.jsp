<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${systemName}-产品列表</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
	<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
	
	<style> 
		.modalHeader{
			height:40px;
			background:#5C6F87;
			line-height:40px;
			color:#fff;
			font-weight:bold;
			padding-left:3%;
			border-radius: 8px 8px 0 0;
		}
		.Modal{
			position:fixed;
			height:auto!important; 
			height:300px; 
			min-height:300px;
			top:20%;
			width:500px;
			left:50%;
			min-width:500px;
			margin-left:-150px;
			z-index:1500;
			background:#fff;
			border-radius:8px;
			border:#222 1px solid;
		}
		.modalBody{
			height: 72%;
			padding-top:10px;
			padding-left:10px;
			padding-right:10px;
			padding-bottom:8px;
		}
		.modalFooter{
			text-align:right;
			padding-right:5%;
			padding-bottom:3%;
		}
		.modalFooter a{
			color:#fff;
			border-radius:5px;
			margin-left:5%;
		}
		.modalFooter .submit{
			background:#5C6F87;
		}
		.modalFooter .cancel{
			background:#d0d0d0;
		}
		@media (max-width:768px){
			.Modal{
				width:80%;
				left:50%;
				margin-left:-40%;
			}
		}
		.close{
			float: right;
			margin-right: 2%;
			font-size: 30px;
			font-weight: 300;
			line-height:40px;
			color: #fff;
			text-shadow: 0 1px 0 #fff;
			opacity:1;
		}
		.Modal a:hover{
			color:#fff;
		}		
		.smallTitle{
			width:100%;
			max-width100%;
			overflow-x: auto;
			padding-top:23px;
		}
		.buttonWithoutColor{   
			padding:5px 10px; 
			border-radius:2px;
			border: 1px solid #0F8A0E;
		}
		.buttonColor{
			background-color:#56616F;
			color:#FFFFFF;
			border-radius:2px;
		}	
		.smallTitle a img{
			margin-right:5px; margin-top:-1px; width:15px; height:15px;
		}
		/* 表格 */
		.table-responsive td{
		   border-top:0px!important;
		   border-bottom:1px solid #ddd;
		   height: 60px;
	    }
		.table>thead>tr>th{
			border-bottom:0px;
			/*border-top:2px solid #ddd !important;*/
			/*background-color:#f9f9f9;*/
			padding: 16px;
		}
		.table-striped>tbody>tr:nth-of-type(even) {
			background-color: #f9f9f9;
		}
		.table-striped>tbody>tr:nth-of-type(odd) {
			background-color: #FFF;
		}
		
		
		
		
		
		.tankuang tr th{
			text-align:center;
			height:40px;
		}
		.tankuang tr .identification{
			text-align:right;
			padding:0px 15px;
		}
		.tankuang tr td{
			height:50px;
		}
		.tankuang table select{
			height:30px;
		}
		.tankuang{
			width:500px;
			border:1px solid #ddd;
			left:32%;
			position: absolute;
			z-index: 1200;
			top: 10%;
			display:none;	
		}
		@media (max-width:968px){
			.tankuang{
				width:80%;
				left:30%;
				margin-left:-20%;
			}
		}
		.hidebg{
			height:100%;
			width:100%;
			background-color:#333;
			position: fixed;
			z-index:1100;
			filter:alpha(opacity=70); /* IE浏览器下设置透明度为60% */
			opacity:0.7;  /* 非IE浏览器下设置透明度为60% */
			display:none;"
		}
		#queryForm{
			background-color: #eee;
		    padding: 30px 10px;
		    border-radius: 5px;
		}
		#queryForm input,#queryForm select{
			border-radius: 5px;
			    border: 1px solid #E4E4E4;
    padding: 4px;
		}
		#queryForm td{
			margin:5px 0;
		}
		.delete{
   	width: 100%;height: 100%;
   	background-color: rgba(0,0,0,0.6);
   	position: fixed;top: 0;left: 0;
   	    z-index: 999;
   	padding: 15px;
   	display: none;
   }
   .delete .box{
   	position: absolute;width: 300px;
   	height: 150px;
   	top: 50%;left: 50%;
   	margin-left: -150px;margin-top: -75px;
   	border-radius: 5px;
   	background-color: #fff;
   	text-align: center;
   }
   .delete h4{
   	    margin-bottom: 15px;
    margin-top: 31px;
   }
   .delete span{
   	display: inline-block;
    padding: 5px 15px;
    background-color: #C5C5C5;
    border-radius: 5px;
	margin: 5px 10px;
	cursor: pointer;
   }
   .delete .confirm{
        background-color: #5C6F87;
    color: #fff;
   }
   .delete input{
   	    border-radius: 5px;
    border: 1px solid #333;
    padding: 2px 5px;
    width: 70%;
   }
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	
	
	<div class="Modal" style="display:none;">	
		<div class="modalHeader">
		<a class="close" >×</a>
		<span>添加产品</span>
		</div>
		<div class="modalBody">
		<form action="" style="margin:auto 0; height:auto!important; 
		height:200px; 
		min-height:200px;">
				<div style="font-size:20px;">产品类型:</div>
				<div style="margin-left:100px; margin-top:-23px">
					<c:forEach var="proType" items="${productType}" varStatus="in">
						<span style="line-height: 30px; margin-right:20px;">
						<c:choose>
							<c:when test="${in.count == 1}">
								<input type="radio" name="productType" value="${proType.productTypeId}" checked="checked"/>${proType.productTypeName}
							</c:when>
							
							<c:otherwise>
								<input type="radio" name="productType" value="${proType.productTypeId}"/>${proType.productTypeName}
							</c:otherwise>
						</c:choose>
						</span>
						<br/>
					</c:forEach>
				</div>
			
		</form>			
		</div>
		<div class="modalFooter">
			<input type="hidden" class="modalProductId" value=""/>
			<a href="javascript:void(0);" class="btn submit">确定</a>
			<a href="javascript:void(0);" class="btn cancel">取消</a>
		</div>
	</div>
	
	
	<!-- 显示列名 -->
	<div id="columns" class="tankuang">
		<table width="100%" style="background-color:#FFFFFF;">
			<thead>
				<tr>
				<th colspan="2" class="buttonColor">选择要显示的列名<img src="/theme/basic/images/close.png" class="close" style="width:20px; height:20px; float:right; margin-right:5px;" ></th>
			</tr>
			</thead>
			<tbody>
				<tr>
					<td class="identification checkboxStyle"><input type='checkbox' name="select" id="checkAll" onclick="selectAll()"></td>
					<td>全选</td>
				</tr>
				<c:forEach items="${systemDisplayColumns}" var="sdc">
					<tr>
						<td class="identification checkboxStyle"><input type='checkbox' name="subBox" value="${sdc.columnName}"></td>
						<td><spring:message code="DataName.${sdc.columnName}"  /><input type="hidden" id="currentStatus" value="${sdc.currentStatus}" /><td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="2" style="text-align:center"><a href="javascript:void(0)" class="buttonColor submitColumns" style="padding:5px 25px;">确认</a></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!--  删除 -->
	<div id="shanchu" class="tankuang" style="width:400px;">
		<table width="100%" style="background-color:#FFFFFF;">
			<thead>
				<tr>
				<th colspan="5" class="buttonColor">删除提示</th>
			</tr>
			</thead>
			<tbody>
				<tr>
					<td style="width:20%;"></td>
					<td style="width:15%;"></td>
					<td style="width:30%; text-align:center;" >确定要删除吗？</td>
					<td style="width:15%;"></td>
					<td style="width:20%;"></td>
				</tr>
				<tr>
					<td style="width:20%;"></td>
					<td style="width:15%; text-align:right;"><input type="button" value="确定" class="delSubmit" ></td>
					<td style="width:30%;"></td>
					<td style="width:15%;"><input type="button" value="取消" class="delClose" ></td>
					<td style="width:20%;"></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	
	<div class="hidebg"></div> <!-- 背景层 -->
    <div class="container-fluid" style="z-index:1000">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span><!-- ${title} -->产品管理列表</span></h2>

			<form id="queryForm">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" >
					<tr>
						<td>编号:<input type="text" style="width:80%;"name="productCode"></input></td>
						<td>名称:<input type="text" style="width:70%;" name="productName" value=""></input></td>
						<%-- <td style="display:inline-block;width:250px;">发布类型:<select name="productTypeName" style="width:70%;">
							<!--<option value="">全部状态</option>
							<c:forEach var="s" items="${statusList}">
								<option value="${s}">${s} <spring:message code="Status.${s}" />
							</c:forEach>-->
						</select></td>
						<td align="left" style="display:inline-block;width:260px;">
						创建时间:
						<span style="display:inline-block;"><input size="20"  id="d11" class="calendarInput" name="createTime" type="text" value="" style="border:none;"/><img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span></td>
						<td style="display:inline-block;width:200px;">产地:<input type="text" style="width:70%;"name="productOrigin"></input></td> --%>
						<td>发货地:<input type="text" style="width:70%;"name="deliveryFromArea"></input></td>
						<td align="center"><input class="btn btn-primary"   type="submit" style="width:100%;" value="查询"  onClick="query()" onSubmit="return false;"/></td>
						</tr>
				</table>
			</form>	
			
			<div class="smallTitle">
				<!-- <span style="line-height:32px; color:#37485D;font-size: 19px;">产品列表</span>
				 -->
				<c:if test="${!empty addUrl }">
					<c:choose>
					<c:when test="${productType.size() == 1}">
					<a href="${addUrl }?mini=true&productTypeId=${productType[0].productTypeId}" class="buttonWithoutColor addmoban buttonColor add"><img src="/theme/basic/images/jiahao.png" >添加产品</a>
					</c:when>
					<c:otherwise>
					<a href="#" class="buttonWithoutColor addmoban buttonColor add" onClick="addProduct()"><img src="/theme/basic/images/jiahao.png" >添加产品</a>
					</c:otherwise>
					</c:choose>
				</c:if>
				<a href="#" class="buttonWithoutColor displayColumns greenbtn" style=" margin-right:25px; ">显示列名</a>
			</div>
				
			<div class="table-responsive">
			<table>
				<tr>
				
				</tr>
			</table>
				<table class="table table-striped" style="margin-top:15px;">
		              <thead >
		                <tr>
		                <c:forEach var="column" items="${displayColumns}">		                
		                  <th><spring:message code="DataName.${column.columnName}" /></th>
		                </c:forEach>
						  <th>快递费报价单</th>
						  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
				
					<c:forEach var="i" items="${rows}">
						<tr>
						<c:forEach var="column" items="${displayColumns}">
						<td>
							<c:choose>
								<c:when test="${column.columnType == 'extra'}">
									<c:set var="dataValue" value="${i.productDataMap[column.columnName].dataValue}" />  
								</c:when>
								<c:otherwise>
									<c:set var="dataValue" value="${i[column.columnName]}" />								
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${column.useMessagePrefix != null}">
									<spring:message code="${column.useMessagePrefix}.${dataValue}" />
								</c:when>
								<c:otherwise>
									<c:choose>
									<c:when test="${column.format == 'date'}">
										<fmt:formatDate value="${dataValue}" pattern="yyyy-MM-dd HH:mm:ss"/>									
									</c:when>
									<c:otherwise>
										${dataValue}
									</c:otherwise>
									</c:choose>								
								</c:otherwise>
							</c:choose>
						</td>
						</c:forEach>
						<td><a href="${i.productDataMap.deliveryPriceListFile.dataValue}" >下载报价详情</a></td>
						<td style="position:relative;">
							<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position:absolute; padding-right: 10px; padding-left: 10px; margin-left: -18px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
								<input type="hidden" id="productId" value="${i.productId}" />
								<a href="${i.operate.update}.shtml?mini=true" class="edit">
									<li>查看</li>
								</a>
								<a href="<c:url value='/product/update/${i.productId}.shtml' />" class="edit">
									<li>编辑</li>
								</a>
								<li class="Inchange" availableCount="${i.availableCount}" productId="${i.productId}" style="cursor: pointer;">修改库存</li>
							</ul>
						</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>	
			</div>
        </div>
      </div>
    </div>
    <div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
		<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
	</div>
	  <div class="delete">
    	<div class="box">
    		<h4>修改库存</h4>
    		<p><input type="text" value=""/></p>
    		<span class="confirm">确定</span><span class="cancel">取消</span>
    	</div>
    </div>
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script>

	$('.Inchange').on('click',function(){
		var kucun = $(this).attr('availableCount');
		var productId = $(this).attr('productId')
		$('.delete').css('display','block');
		$('.delete').attr('productId',productId);
		$('.delete input').attr('value',kucun);
	});
	$('.delete .cancel').on('click',function(){
		$('.delete').css('display','none');
	})
	$('.confirm').on('click',function(){
		var productId = $(this).parents('.delete').attr('productId');
		if ($('.delete input')=='') {
			alert('库存不能为空！')
		}else{
			$.ajax({ 
				type: "POST", 	
				url: "/product/relate.json",
				data: {
					productId: productId, 
					availableCount: $('.delete input').val()
				},
				dataType: "json",
				success: function(data){
					if (data.message.operateCode==102008) { 
						$(".Modal").css("display","none");
						alert(data.message.message);
					} else {
						alert(data.message.message);
					};
					$('.delete').css('display','none');  
				},			     
			});
		}
	})
	//${addUrl }?mini=true&productTypeId=123001
		//添加产品
		function addProduct(){
			$(".Modal").css("display","block");
		}
		//提交
		$(".Modal .submit").click(function(){
			var value = $("input[name='productType']:checked").val();
			
				location.href = "/product/create.shtml?mini=true&productTypeId=" + value;
		})
		//取消
		$(".Modal .cancel").click(function(){
			$(".Modal").css("display","none");
		})
		//关闭
		$(".Modal .close").click(function(){
			$("#contentTextarea").empty();
			$(".Modal").css("display","none");
		})
	</script>
	<script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
	</script>
	<script>
		/*$(function(){
			$(".changeStore").click(function(){
				var productId=$(this).siblings(".productId").val();
				var productName=$(this).parent().siblings(".productName").html();
				var availableCount=$(this).parent().siblings(".availableCount").html();
				$(".Modal").fadeIn();
				$(".Modal .modalHeader span").html("请输入产品["+productName+"]的库存信息");
				$(".modalProductId").val(productId);
				$("#storeAmount").val(availableCount);
			})
			$(".Modal .submit").click(function(){
				var storeAmount=$("#storeAmount").val()
				if(storeAmount==""){
					alert("库存不能为空！")
				}else if(/^(\+|-)?\d+($|\.\d+$)/.test(storeAmount)){
					$.ajax({ 
						type: "POST", 	
						url: "/product/relate.json",
						data: {
							productId: $(".modalProductId").val(), 
							availableCount: $("#storeAmount").val(), 
							updateMode:$("input[name='changeMethod']:checked").val(),
						},
						dataType: "json",
						success: function(data){
							if (data.message.operateCode==102008) { 
								$(".Modal").css("display","none");
								alert(data.message.message);
							} else {
								alert(data.message.message);
							}  
						},			     
					});
				}else{
					alert("请输入数字类型数据（可包含负数）！");
				}
			})
			$(".Modal .cancel").click(function(){
				$(".Modal").css("display","none");
			})
			$(".Modal .close").click(function(){
				$(".Modal").css("display","none");
			})
		}) */
		
		
	</script>
	
	<script>
	<!-- 全选 -->
	function selectAll(){
		var checkAll = document.getElementById("checkAll");
		var subBox = document.getElementsByName("subBox");
		for(var i=0; i<subBox.length; i++){
			subBox[i].checked = checkAll.checked;
		}

	}
	
	<!-- 要显示的列提交 -->
	$(function(){
		$(".smallTitle .displayColumns").click(function(){    //显示列按钮点击
			$("#columns").fadeIn();   //选择列的弹出
			$(".hidebg").fadeIn();    //背景层弹出
			var obj = document.getElementsByName('subBox');
			//var checkAll = document.getElementById("checkAll");
			for(var i=0; i<obj.length; i++){
				var currentStatus = $("#currentStatus").val();
				if( currentStatus == 100003){
					obj[i].checked = true;
				}else{
					obj[i].checked = false;
				}
				/*if(obj[i].checked == true){
					checkAll.checked = true;
				}else{
					checkAll.checked = false;
				}*/
				
			}
			
		
			
		})
		$(".submitColumns").click(function(){
			var chk_value = new Array(); 
			$('input[name="subBox"]:checked').each(function(i){ 
				chk_value.push((i+1)+"#"+this.value);
			}); 
			//alert(chk_value);

			if(chk_value.length < 1){
				alert("请选择要显示的列！")
			}else{
				$.ajax({ 
					type: "POST", 
					traditional :true,						
					url: "/product/setViewColumns.json",
					data: {displayColumns:chk_value},
					dataType: "json",
					success: function(data){
						//alert(chk_value);
						$("#columns").css("display","none");
						$(".hidebg").css("display","none");
						window.location.reload();
						//alert("选择成功！！"); 
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert(XMLHttpRequest.status);
						alert(XMLHttpRequest.readyState);
						alert(textStatus);
					},
				});
			}
		})
		$("#columns .close").click(function(){   //关闭
			$("#columns").css("display","none");
			$(".hidebg").css("display","none");
		})
	})
	
	<!-- 删除 -->
	$(function(){
		
		$("#del").click(function(){
			$("#shanchu").fadeIn();   
			$(".hidebg").fadeIn();
		
		})
		
		$(".delSubmit").click(function(){
			$.ajax({ 
				type: "POST", 	
				url: "/product/delete.json",
				dataType: "json",
				data:{
					idList:$("#productId").val(),
				},
				success: function(data){
					//alert("删除成功");
					$("#shanchu").css("display","none");   
					$(".hidebg").css("display","none");
					window.location.reload();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);
				},     
			}); 
		
		})
		
		$(".delClose").click(function(){
			$("#shanchu").css("display","none");   
			$(".hidebg").css("display","none");
		
		})
	})
	</script>
  </body>
</html>
