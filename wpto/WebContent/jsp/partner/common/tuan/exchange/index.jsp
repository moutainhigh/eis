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
<link href="/theme/${theme}/style/query.css" rel="stylesheet">
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
@media ( max-width :768px) {
	.search_form td {
		display: block !important;
	}
}

.table>thead>tr>th {
	border-bottom: 0px;
	background-color: #f9f9f9;
}

.table-striped>tbody>tr:nth-of-type(odd) {
	background-color: #f9f9f9;
}
.table-striped>tbody>tr:nth-of-type(even) {
	background-color: #FFF;
}
.table>tbody>tr>td {
	border-top: 1px solid #ddd;
}
</style>
</head>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header"><span>${title}</span></h2>
				<div class="content" style="overflow: hidden;">
					<form id="queryForm">
						<table width="100%">
							<tr>
								<td align="left" width="50%">时&nbsp;&nbsp;&nbsp;间&nbsp;&nbsp;&nbsp;：<span class="shijian"><input size="20" id="d11" class="calendarInput" name="closeTimeBegin" type="text" value="" style="border: none;"placeholder="从"/> <img
										onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor: pointer;">
								</span><span class="shijian"> <input size="20" id="d12" class="calendarInput" name="closeTimeEnd" type="text" value="" style="border: none;" placeholder="到"/> <img
										onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor: pointer;">
								</span>
								</td>
								<td width="50%">用户名&nbsp;&nbsp;&nbsp;：<input type="text" name="username" />
									
								</td>
							</tr>
							<tr>
								<td>订单号&nbsp;&nbsp;&nbsp;：<input type="text" name="transactionId"></input></td>
								<td>中奖码&nbsp;&nbsp;&nbsp;：<input type="text" name="content"></input></td>
							</tr>
							<tr>
								<td>中奖状态：<select name="currentStatus">
										<option value="">请选择</option>
										<c:forEach items="${statusList}" var="s">
											<c:if test="${s > 710000}">
												<option value="${s}">
													<spring:message code="Status.${s}" />
												</option>
											</c:if>
										</c:forEach>
								</select></td>
								<td>扩展状态：<select name="extraStatus">
										<option value="-1">请选择</option>
										<c:forEach items="${statusList}" var="s">
											<c:if test="${s > 710000}">
												<option value="${s}">
													<spring:message code="Status.${s}" />
												</option>
											</c:if>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align:center;">
									<button class="btn btn-primary query" type="submit" value="查询" onClick="query()" onSubmit="return false;" style="margin-left:0">
										&nbsp;&nbsp;&nbsp;&nbsp; <span class="glyphicon glyphicon-search"></span> 查询 &nbsp;&nbsp;&nbsp;&nbsp;
									</button>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<!--<div class="smallTitle">
				<a href="">全部订单</a>
				<a href="" class="notAll">待付款（）</a>
				<a href="" class="notAll">待发货（）</a>
				<a href="" class="notAll">已发货（）</a>
				<a href="" class="notAll">已完成（）</a>
			</div>-->
				<div>
					<a class="completebtn" href="/exchange.shtml?extraStatus=710004">兑奖历史</a>
				</div>
				<div class="table-responsive">
					<table class="table table-striped" style="margin-top: 15px;">
						<thead>
							<tr>
								<th>订单号</th>
								<th style="width: 150px;">完成时间</th>
								<!--<th>产品编号</th>-->
								<th>产品名称</th>
								<th>产品概述</th>
								<th>是否兑奖</th>
								<th>购买份数</th>
								<th>订单金额</th>
								<th>实付款</th>
								<!--<th>支付方式</th>
						  <th>第三方订单号</th>-->
								<th>购买用户</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td><c:choose>
											<c:when test="${fn:length(i.transactionId) > 8}">
												<span id="xt${i.transactionId}" onClick="show('${i.transactionId}','xt','8')">${fn:substring(i.transactionId, 0, 8)}...</span>
											</c:when>
											<c:otherwise>  
											${i.transactionId}
										</c:otherwise>
										</c:choose></td>
									<td><span id="close${i.transactionId}" onClick="showsj('${i.transactionId}','close' ,'<fmt:formatDate value='${i.closeTime}' pattern='yyyy-MM-dd HH:mm:ss'/>')"><fmt:formatDate
												value="${i.closeTime}" pattern="yyyy-MM-dd" /></span></td>
									<td>${i.name}</td>
									<td><c:choose>
											<c:when test="${fn:length(i.content) > 16}">
												<span id="xt${i.content}" onClick="show('${i.content}','xt','16')">${fn:substring(i.content, 0, 16)}...</span>
											</c:when>
											<c:otherwise>  
											${i.content}
										</c:otherwise>
										</c:choose></td>
									<td>
										<c:choose>
											<c:when test="${i.currentStatus == 710010 && i.extraStatus == 710004}">
												已兑奖
											</c:when>
											<c:when test="${i.currentStatus == 710010 && i.extraStatus != 710004}">
												未兑奖
											</c:when>
											<c:otherwise>  
											不可兑奖
											</c:otherwise>
										</c:choose>
									</td>
									<td>${i.count}</td>
									<td>${i.requestMoney}</td>
									<td><c:choose>
											<c:when test="${i.currentStatus == 710010 || i.currentStatus == 710057 || i.currentStatus == 710011}">
										${i.requestMoney}
										</c:when>
											<c:otherwise>  
											0
										</c:otherwise>
										</c:choose></td>
									<td><span title="${i.chargeFromAccountName}"> <c:choose>
												<c:when test="${fn:length(i.chargeFromAccountName) > 7}">
													<span id="fk${i.transactionId}" onClick="showfk('${i.transactionId}','fk' ,'7','${i.chargeFromAccountName}')">${fn:substring(i.chargeFromAccountName, 0, 7)}...</span>
												</c:when>
												<c:otherwise>  
												${i.chargeFromAccountName}
											</c:otherwise>
											</c:choose>
									</span></td>
									<td><spring:message code="Status.${i.currentStatus}" /></td>
									<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
										<ul
											style="position: absolute; padding-right: 10px; padding-left: 10px; left: 50%; margin-left: -24px; top: 28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
											class="toolbtns">
											<c:choose>
												<c:when test="${i.extraStatus == 710004}">
													<li class="materialSelect">已兑奖</li>
												</c:when>
												<c:when test="${i.currentStatus == 710010}">
													<a href="#" onclick="exchange('${i.transactionId}')"><li class="materialSelect">兑奖</li></a>
												</c:when>
												<c:otherwise>
													<li class="materialSelect">无法兑奖</li>
												</c:otherwise>
											</c:choose>
										</ul></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="Pagination" style="text-align: center; width: 100%; background: #fff;">
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
		//订单号
		function show(id, b, c){
			var str = $("#"+b+id).html();
			if(str.indexOf('.') < 0){
				str=str.substring(0,c)+"...";
				$("#"+b+id).text(str);
			} else {
				$("#"+b+id).text(id);
			}
		}
		//完成时间
		function showsj(id,b,c){
			var str = $("#"+b+id).html();
			if(str.length <= 10){
				$("#"+b+id).text(c);
			} else {
				c=str.substring(0,10);
				$("#"+b+id).text(c);
			}
		}
		//购买用户
		function showfk(id, b, c, d){
			var str = $("#"+b+id).html();
			if(str.indexOf('.') < 0){
				str=str.substring(0,c)+"...";
				$("#"+b+id).text(str);
			} else {
				$("#"+b+id).text(d);
			}
		}
		//操作
		$(".tools").click(function(){
			$(this).parent().parent().siblings().find(".toolbtns").hide();
			$(this).siblings(".toolbtns").toggle();
		})
		//兑奖
		function exchange(a) {
			$.ajax({
				type:"POST",
				url:"/exchange/update/" + a + ".json",
				dataType:"json",
				success:function(data){
					swal(data.message.message);
				}
			})
		}
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
