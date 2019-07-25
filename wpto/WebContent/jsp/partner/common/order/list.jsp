<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${systemName}-${title}</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
 	<script src="/theme/${theme}/js/pageQuery.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<script src="/theme/${theme}/js/sweetalert.min.js"></script>
	<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
	<style>

		.modalHeader{
			height:40px;
			background:#222;
			line-height:40px;
			color:#fff;
			font-weight:bold;
			padding-left:3%;
			border-radius: 8px 8px 0 0;
		}
		.Modal{
			position:fixed;
			top:40%;
			width:480px;
			left:50%;
			min-width:350px;
			margin-left:-240px;
			z-index:1500;
			background:#fff;
			border-radius:8px;
			border:#222 1px solid;
		}
		.updateModal{
			position:fixed;
			top:40%;
			width:480px;
			left:50%;
			min-width:350px;
			margin-left:-240px;
			z-index:1500;
			background:#fff;
			border-radius:8px;
			border:#222 1px solid;
		}
		.modalBody{
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
			background:#222;
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
		@media (max-width:768px){
			.search_form td{
				display:block !important;
			}
		}
	
		.table-responsive td{
			display:table-cell;
			vertical-align:middle;
			line-height:100%;
		}
		
		
		
		
		
		
		.smallTitle{
			width:100%;
			max-width:100%;
			overflow-x: auto;
			/*padding-top:23px;*/
			padding-bottom:7px;
		}
		.smallTitle .notAll{
			padding-left:18px;
		}
		.smallTitle a{
			color:#333 !important;
		}
		.smallTitle .cur{
			color:#337ab7;
		}
		
		
		.table-responsive td{
		   border-top:0px!important;
		   border-bottom:1px solid #ddd;
		   height: 60px;
	    }
		.table>thead>tr>th{
			border-bottom:0px;
			/*border-top:2px solid #ddd !important;*/
			/*background-color:#f9f9f9;*/
		}
		.table-striped>tbody>tr:nth-of-type(odd) {
			background-color: #f9f9f9!important;
		}
		
		.table>tbody>tr>td {
			  border-top: 0px solid #ddd;
		}
		
		
		/* 新增加 */
		/*.header_bc{
			background-color:#61CDB6;
			color:#fff;
		}*/
		.search_form_style2{
			border: 2px solid #fff;
			border-radius: 5px;
			padding-left: 15px;
		}
		#triangle-down {
			width: 0;
			height: 0;
			border-left: 10px solid transparent;
			border-right: 10px solid transparent;
			border-top: 10px solid #fff;
			margin-left: 48%;
		}
		
		td{
			padding-bottom: 20px;
		}
		td input,
		td select{
			border-radius: 5px;
			border:1px solid #ddd;
			padding: 5px 6px;
			width: 60%;
		}
		#queryForm{
			background-color: #eee;
    		border-radius: 5px;
    		padding: 25px;
		}
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="Modal" style="display:none;position:fixed;">
			
			<div class="modalHeader">
			<a class="close" >×</a>
			<span>快递信息</span>
			</div>
			<div class="modalBody">
			<form action="" style="margin:auto 0;">	
				<div class="form-item">
				<label for="company">快递公司:</label>
				<label>
					<select name="companyName" id="companyName">
						<c:forEach var="entry" items="${deliveryCompanyMap}" varStatus="status">  
							<option value="${entry.key}">${entry.value}</option>					 
						</c:forEach> 
					</select>
				</label>			
				</div>
				<div class="form-item"><label for="deliveryNum">快递单号:</label><input type="text" size="50"style="margin-left:3px;" id="deliveryNum" name="deliveryNum" value="" placeholder="请输入快递单号"/></div>
				
			</form>			
			</div>
			<div class="modalFooter">
			<input type="hidden" class="modalTransactionId" value=""/>
			<a href="javascript:void(0);" class="btn submit">确定</a>
			<a href="javascript:void(0);" class="btn cancel">取消</a>
			</div>
	</div>
	<div class="updateModal" style="display:none;position:fixed;">
		<div class="modalHeader">
			<a class="close" >×</a>
			<span>修改快递信息</span>
		</div>
		<div class="modalBody">
			<form action="" style="margin:auto 0;">	
				<div class="form-item">
					<label for="company">快递公司:</label>
					<label>
						<select name="companyName" id="updateCompanyName">
							<c:forEach var="entry" items="${deliveryCompanyMap}" varStatus="status">  
								<option value="${entry.key}">${entry.value}</option>					 
							</c:forEach> 
						</select>
					</label>			
				</div>
				<div class="form-item">
					<label for="updateDeliveryNum">快递单号:</label>
					<input type="text" size="50"style="margin-left:3px;"id="updateDeliveryNum" name="deliveryNum" value="" placeholder="请输入快递单号"/>
				</div>
			</form>			
		</div>
		<div class="modalFooter">
			<input type="hidden" class="modalTransactionId" value=""/>
			<a href="javascript:void(0);" class="btn submit">确定</a>
			<a href="javascript:void(0);" class="btn cancel">取消</a>
		</div>
	</div>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
	   <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main header_bc" style="padding-bottom:0;"> 
		<div class="search_form search_form_style2" style="overflow:hidden;">
				<h2 class="sub-header"><span>${title}</span></h2>
				<form id="queryForm">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="width:30%;">购买用户：<input type="text" style="" name="username" value=""></input></td>
						<td align="left" style="width:40%;">
						<span style="letter-spacing: 14px;">时间</span>：<span style="display:inline-block;"><input size="24" placeholder="从" id="d11" class="calendarInput" name="createTimeBegin" type="text" value='<fmt:formatDate value="${cartCriteria.createTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss" />' style="width:156px;" /><img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>
						<span style="display:inline-block;"><input size="24" placeholder="到"  id="d12" class="calendarInput" name="createTimeEnd" type="text" value='<fmt:formatDate value="${cartCriteria.createTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss" />'  style="width:156px;"/><img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>					
						</td>
						<td style="width:30%;">有无开具发票：<select name="requireInvoice" style="">
						<option value="0">全部</option>
						<option value="1">需要开具</option>							
						<option value="2">不需要开具</option>
						</select></td>
						
					</tr>
					<tr>
						<td style=""><span style="letter-spacing: 5px;">订单号</span>：<input type="text" style=""name="cartId" value="${cartCriteria.cartId}"></input></td>
						<td style="">产品编号：<input type="text" style=""name="productCode"></input></td>
						<td style=""><span style="letter-spacing: 7px;">产品名称</span>：<input type="text" style=""name="productName"></input></td>
					</tr>
					<tr>
						<td> 
							<span style="letter-spacing: 14px;">状态</span>：<select name="currentStatus" id="currentStatus">
								<option value="">全部状态</option>
								<c:forEach var="s" items="${currentStaus}">
									<option value="${s}"><spring:message code="Status.${s}" /></option>
								</c:forEach>
							</select>
							</select>

						</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td align="center" colspan="3"><input type="submit" value="查询"  onClick="query()" onSubmit="return false;" style="background-color: #5C6F87!important; color: #fff;padding: 9px; width: 257px;"/></td>
					</tr>

				</table>
				</form>			
		</div>
		<div class="" id="triangle-down"> 
				</div>
	  </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<!--<h2 class="sub-header">${title}</h2>
			<div class="search_form" style="overflow:hidden;">
				<form id="queryForm">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="display:inline-block;width:250px;padding-bottom:8px;">订单号[默认请设置为0]:<input type="text" style="width:80%;"name="cartId" value="${cartCriteria.cartId}"></input></td>
						<td style="display:inline-block;width:250px;padding-bottom:8px;">产品编号:<input type="text" style="width:80%;"name="productCode"></input></td>
						<td style="display:inline-block;width:250px;padding-bottom:8px;">产品名称:<input type="text" style="width:70%;"name="name"></input></td>
						<td style="display:inline-block;width:200px;padding-bottom:8px;">购买用户:<input type="text" style="width:70%;" name="username" value=""></input></td>
						<td align="left" style="display:inline-block;width:500px;max-width:80%;padding-bottom:8px;">
						时间：从
						<span style="border:1px solid #A4BED4;display:inline-block;"><input size="20"  id="d11" class="calendarInput" name="createTimeBegin" type="text" value='<fmt:formatDate value="${cartCriteria.createTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border:none;"/><img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>
						至
						<span style="border:1px solid #A4BED4;display:inline-block;"><input size="20"  id="d12" class="calendarInput" name="createTimeEnd" type="text" value='<fmt:formatDate value="${cartCriteria.createTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border:none;"/><img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>					
						</td>
						<td style="display:inline-block;width:200px;padding-bottom:8px;">有无开具发票:<select name="requireInvoice" style="width:40%;">
						<option value="0">全部</option>
						<option value="1">需要开具</option>							
						<option value="2">不需要开具</option>
						</select></td>
						<td align="center" width="7%"><input type="submit" value="查询"  onClick="query()" onSubmit="return false;"/></td>
					</tr>
				</table>
				</form>			
			</div>-->
			
			
		
			
			<div class="smallTitle">
				<a href="">全部订单</a>
				<!--<a href="" class="notAll">待付款（）</a>
				<a href="" class="notAll">待发货（）</a>
				<a href="" class="notAll">已发货（）</a>
				<a href="" class="notAll">已完成（）</a>-->
			</div>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>订单号</th>
		                  <th style="width:150px;">完成时间</th>
						  <!--<th>产品编号</th>-->
		                  <th>产品名称</th>
		                  <th>规格</th>
		                  <th>购买份数</th>
		                  <th>订单金额</th>
		                  <th>实付款</th>
						  <th>${coinName}</th>
		                  <th>运费</th>
		                  <!--<th>支付方式</th>
						  <th>第三方订单号</th>-->
						  <th>购买用户</th>
						  <th>状态</th>
						  <th>操作</th>
                		</tr>
		              </thead>
					<tbody>
					
					<c:forEach var="i" items="${rows}" varStatus="abc" >
						<c:forEach items="${i.miniItemList}" var="itemList"  varStatus="in">
						<tr>
							<c:if test="${in.index==0}">
								<td rowspan="${fn:length(i.miniItemList)}">
									${i.cartId}<!--${i.miniItemList[0].transactionId}-->
								</td>
							</c:if>
							<c:if test="${in.index==0}">
								<td rowspan="${fn:length(i.miniItemList)}">
									<fmt:formatDate value="${i.miniItemList[0].enterTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
							</c:if>
							<!--<td>${itemList.itemDataMap.productCode.dataValue}</td>-->
							<td>${itemList.name}</td>
							<td>${itemList.itemDataMap.goodsSpec.dataValue}</td>
							<td>${itemList.count}</td>
							<td><fmt:formatNumber value="${i.money.chargeMoney + i.money.giftMoney + i.money.coin}" maxFractionDigits="2"/></td>
							<td><fmt:formatNumber value="${i.money.chargeMoney}" maxFractionDigits="2"/>元 + <fmt:formatNumber value="${i.money.coin}" maxFractionDigits="2"/>${coinName}</td>
							<td><fmt:formatNumber value="${i.money.coin}" maxFractionDigits="2"/></td>
							<td>${itemList.itemDataMap.deliveryFee.dataValue}</td>
							<!--<td></td>
							<td></td>-->
						   <c:if test="${in.index==0}">
								<td rowspan="${fn:length(i.miniItemList)}">
									${i.data.userNickName}
								</td>
							</c:if>
							<td>
								<spring:message code="Status.${i.currentStatus}" />
							</td>
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<ul style="position:absolute; /*width: 78px;*/ padding-right: 10px; padding-left: 10px; margin-left: -36px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<input type="hidden" value="${itemList.transactionId}" class="transactionId"/>
									<input type="hidden" value="${itemList.itemDataMap.deliveryOrderId.dataValue}" class="outOrderId"/>
										<a href="/order/get/${i.cartId}.shtml">
											<li>订单详情</li>
										</a>
										<c:if test="${!empty itemList.operate.relate}">
											<a href="#" onClick="send('${itemList.transactionId}', '${itemList.operate.relate}')">
												<li>快递发布</li>
											</a>
										</c:if>
										<c:if test="${!empty itemList.operate.updateExpressInfo}">
											<a href="#" onClick="updateExpress('${itemList.transactionId}', '${itemList.itemDataMap.deliveryCompanyName.dataValue}','${itemList.itemDataMap.deliveryOrderId.dataValue}','${itemList.operate.updateExpressInfo}')">
												<li>修改快递信息</li>
											</a>
										</c:if>
								</ul>
							</td>
							
						</tr>
					
						</c:forEach>
					</c:forEach>
					
					</tbody>
				</table>
			</div>
			<div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
				<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
			</div>
			
        </div>
      </div>
    </div>
    
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
	</script>
	<script>
		function send(transactionId,url){
				$(".Modal").css("display","block");
				$(".Modal .submit").click(function(){
					if($("#deliveryNum").val()==""){
						alert("快递单号不能为空！")
					}else{
						$.ajax({ 
							type: "POST", 	
							url:url+".json",
							data: {
								outDeliveryOrderId: $("#deliveryNum").val(), 
								deliveryCompanyName: $("#companyName").val(), 
								//transactionId:$(".transactionId").val(),
								transactionId:transactionId,
							},
							dataType: "json",
							success: function(data){
								if (data.message.operateCode==102008) { 
									$(".Modal").css("display","none");
									alert(data.message.message);
								} else {
									alert(data.message.message);
								}
								location.reload();								
							},			     
						});
					}
				})
		}
		$(".Modal .cancel").click(function(){
			$(".Modal").css("display","none");
		})
		$(".Modal .close").click(function(){
			$(".Modal").css("display","none");
		})
		
		function updateExpress(transactionId, deliveryCompanyName, deliveryOrderId, url){
			//alert(transactionId+", "+deliveryCompanyName+", "+deliveryOrderId+", "+url);
			$("#updateCompanyName").val(deliveryCompanyName);
			$("#updateDeliveryNum").val(deliveryOrderId);
			$(".updateModal").css("display","block");
			$(".updateModal .submit").click(function(){
				//alert("快递公司 ：" + $("#updateCompanyName").val() + "  快递单号：" + $("#updateDeliveryNum").val());
				if($("#updateDeliveryNum").val()==""){
					alert("快递单号不能为空！")
				}else{
					$.ajax({ 
						type: "POST", 	
						url:url+".json",
						data: {
							outDeliveryOrderId: $("#updateDeliveryNum").val(), 
							deliveryCompanyName: $("#updateCompanyName").val(), 
							//transactionId:$(".transactionId").val(),
							transactionId:transactionId,
						},
						dataType: "json",
						success: function(data){
							if (data.message.operateCode==102008) { 
								$(".Modal").css("display","none");
								alert(data.message.message);
							} else {
								alert(data.message.message);
							}
							//location.reload();								
						},			     
					});
				}
				
			})
		}
		$(".updateModal .cancel").click(function(){
			$(".updateModal").css("display","none");
		})
		$(".updateModal .close").click(function(){
			$(".updateModal").css("display","none");
		})
	</script>
	<script>
		/*$(function(){
			$(".delivery").click(function(){
				var transactionId=$(this).siblings(".transactionId").val();
				$(".Modal").fadeIn();
				$(".Modal .modalHeader span").html("请输入订单["+transactionId+"]的配送信息");
				$(".modalTransactionId").val(transactionId);
			})
			$(".Modal .submit").click(function(){
				if($("#deliveryNum").val()==""){
					alert("快递单号不能为空！")
				}else{
					$.ajax({ 
						type: "POST", 	
						url: "/item/relate.json",
						data: {
							outDeliveryOrderId: $("#deliveryNum").val(), 
							deliveryCompanyName: $("#companyName").val(), 
							transactionId:$(".modalTransactionId").val(),
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
				}
			})
			$(".Modal .cancel").click(function(){
				$(".Modal").css("display","none");
			})
			$(".Modal .close").click(function(){
				$(".Modal").css("display","none");
			})
			/* var lenTr=$(".table-responsive tbody").find("tr").length;
			var i=0
			while(i<lenTr){
				var UUID=$(".table-responsive tbody").find(".uuid").eq(i).val();
				var index=i;
				var address="";
				$.ajax({ 
					type: "GET", 	
					url: "/addressBook.json?uuid="+UUID,
					dataType: "json",
					async:false,
					success: function(data){
						console.log(JSON.stringify(data));
						if(data.rows!=undefined){
							var lenRows=data.rows.length;
							for(var j=1;j<lenRows-1;j++){
								address += data.rows[j].address+"/"+data.rows[j].mobile+"<br>";
							}
							address += data.rows[lenRows-1].address+"/"+data.rows[lenRows-1].mobile;
							console.log(i);
							$(".table-responsive tbody").find("tr").eq(i).find("td").eq(2).html(address);
						}
					},	
					error:function(data){
						console.log(i);
						
					}
				});
				i++;
			} 
		})*/
	</script>
  </body>
</html>
