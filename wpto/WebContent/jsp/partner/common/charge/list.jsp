<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">

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
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet"
	type="text/css" />
</head>
<style>
.table>thead>tr>th {
	border-bottom: 0px;
	border-top: 2px solid #ddd !important;
	background-color: #f9f9f9;
}

.table-striped>tbody>tr:nth-of-type(odd) {
	background-color: #FFF;
}

.table>tbody>tr>td {
	border-top: 0px solid #ddd;
}
</style>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>

	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="title">${title}</h2>
				<div class="content" style="overflow: hidden;">
					<table>
						<tr>
							<td>充值：<input id="faceMoney" type="text" name="faceMoney" placeholder="请输入充值金额"/>
							</td>
							<td>支付方式： <select id="payTypeId" name="payTypeId">
									<option value="0">请选择</option>
									<option value="1">微信扫码支付</option>
									<option value="2">支付宝扫码支付</option>
							</select>
							</td>
							<td>
								<input class="btn btn-primary query" type="button" value="充值" onClick="charge()"/>
							</td>
						</tr>
					</table>
					<div id="div" style="width: 300px; text-align: center;"></div>
				</div>


				<!--<div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
				<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
			</div>-->

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
</body>
<script>
	var id;
	function charge(){
		var faceMoney = $("#faceMoney").val();
		var payTypeId = $("#payTypeId").val();
		if(faceMoney == ""){
			swal("请输入充值金额");
		} else if(payTypeId <= 0) {
			swal("请选择支付方式");
		} else {
			$.ajax({
				type:"POST",
				url:"/charge.json?faceMoney=" + faceMoney + "&payTypeId=" + payTypeId,
				dataType:"json",
				success:function(data){
					var adress = data.message.message;
					$("#div").html('<p style="color:red; text-align:center;">请扫码完成支付</p><img src=' + adress + '>');
					id = data.transactionId;
					$("#transactionId").val(id);
				}
			});
			
		}
		var timesRun = 0;
		var interval = self.setInterval(function(){
			timesRun += 1;
			if(id == ""){
				swal("充值订单ID为空");
				clearInterval(interval);
				return;
			}
			
			if(timesRun > 10){
				alert(timesRun);
				clearInterval(interval);
				$.ajax({
					type:"GET",
					url:"/charge/get/" + id + ".json",
					dataType:"json",
					success:function(data){
						swal(data.message.message);
					}
				});
			} else {
				$.ajax({
					type:"GET",
					url:"/charge/get/" + id + ".json",
					dataType:"json",
					success:function(data){
						if(data.message.operateCode == 710010){
							swal(data.message.message);
							clearInterval(interval);
						}
					}
				});
			}
			
		},5000);
		
	}
	
	
</script>
</html>
