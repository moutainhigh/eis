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
		<title>${systemName}-资金明细</title>
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
		<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
		<style>

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
			/*.content table tr td:nth-child(2) {
				padding: 0;
			}*/
			/* 下拉菜单样式 */
			select[name='currentStatus'] {
				/*margin-left: 42px;*/
			}
			/* /下拉菜单样式 */
			input[name='inOrderId'] {
				/*width: 280px !important;*/
			}
			#queryForm td{
				width:50%;
			}
			span.tdtitle {
			width: 95px;
			display: inline-block;
			text-align: right;
			}
			@media(max-width: 1500px)and(min-width: 1200px){
				.shijian input {
				    width: 114px !important;
				}
			}
			@media(max-width: 1200px){
				span.tdtitle{
					text-align: left;
				}
				#queryForm td {
			    width: 100%;
			    display: inline-block;
			    padding-bottom: 15px;
			}
			#queryForm table,#queryForm tr,#queryForm tbody{
				display: block;
			}
			.btn-primary{
				margin-left: 0!important;max-width: 100%;
			}
			}
		#queryForm td input,#queryForm td select{
			width: 367px!important;
		}
		#queryForm td button{
			margin-left: 0!important;
		}
		#queryForm td .shijian input{
			width: 152px !important;
		}
		.content{
			padding: 30px 25px!important;
		}
		</style>
</head>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header"><span>资金明细</span></h2>
				<div class="content" style="overflow: hidden;">
					<form id="queryForm">
						<table width="100%">
							<tr>
								<td align="left" width="50%"><span class="tdtitle">时间：</span>从 <span class="shijian"> <input size="20" id="d11" class="calendarInput" name="startTime" type="text" value="" style="border: none;border: 1px #cecece solid;" /> <img
										onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){start()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor: pointer;">
								</span> 到 <span class="shijian"> <input size="20" id="d12" class="calendarInput" name="endTime" type="text" value="" style="border: none;border: 1px #cecece solid;"/> <img
										onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){end()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor: pointer;">
								</span>
								</td>
								<td><span class="tdtitle">资金说明：</span> 
									<select name="memory" id="memory" value="${memory}">
											<option value="">全部</option>
										<c:forEach var="s" items="${moneyMemoryList}">
											<option value="${s}">${s}</option>
										</c:forEach>	

								</select></td>
								
							</tr>
							
							<tr>
								
								<td><span class="tdtitle">付款人类型：</span> 
								<select name="toAccountType" id="toAccountType" value="${toAccountType}">
										<option value="121002">商户</option>
										<option value="121003">前端用户</option>

								</select></td>
								<td width="50%">
									<span class="tdtitle">用户名：</span>
									<input type="text" name="toUserName" value="${toUserName}" />
									
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align:center;padding-bottom:0;">
									<button class="btn btn-primary query" type="submit" value="查询" onClick="query()" onSubmit="return false;">
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
			</div>
				<div>
					<a href="/exchange.shtml?extraStatus=710004">兑奖历史</a>
				</div>-->
				<div class="table-responsive">
					<table class="table table-striped" style="margin-top: 15px;">
						<thead>
							<tr>
								<th style="width: 250px;">发生时间</th>
								<th>账户</th>
								<th>说明</th>
								<th>类型</th>
								<th>金额</th>
								<th>当期余额</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td><fmt:formatDate value='${i.enterTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
									<td>${i.data.toUserName}[ ${i.toAccount} ]</td>
									<td>${i.memory}</td>
									<td>${i.op}</td>
									<td>${i.amount}</td>									
									<td>[收入资金:${i.moneyAfter.incomingMoney}, 可提现资金:${i.moneyAfter.transitMoney}]</td>									
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
		// 时间
		var date = new Date();
	var tyear = date.getFullYear();
	var tmonth = date.getMonth()+1;
	var tday = date.getDate();
	var startv = tyear+'-'+tmonth+'-'+tday+' '+'00:00:00';
	var endv = tyear+'-'+tmonth+'-'+tday+' '+'23:59:59';
	function start(){
		y=parseInt($dp.cal.getP('y'));
		M=parseInt($dp.cal.getP('M'));
		d=parseInt($dp.cal.getP('d'));
		
		if (y==tyear && M==tmonth && d==tday) {
			$('input#d11').val(startv)
		}else{
			return;
		}
	}
	function end(){
		y=parseInt($dp.cal.getP('y'));
		M=parseInt($dp.cal.getP('M'));
		d=parseInt($dp.cal.getP('d'));
		if (y==tyear && M==tmonth && d==tday) {
			$('input#d12').val(endv)
		}else{
			return;
		}
	}

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
