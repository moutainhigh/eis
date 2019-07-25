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
<title>${systemName}-银行账号</title>  
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
	background-color: #FFF;
}
.table-striped>tbody>tr:nth-of-type(odd) {
    background-color: #f9f9f9;
}
.table>tbody>tr>td {
	border-top: 1px solid #ddd;
}
.tdtitle{
	width: 70px;
	display: inline-block;
	text-align: right;
}
</style>
</head>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header"><span>银行账号</span></h2>
				<div class="content" style="overflow: hidden;">
					<form id="queryForm">
						<input type="hidden" name="op" id="op" value="share">
						<table width="100%">   
							<!-- <tr>
								<td align="left" width="50%">充值资金（元）：
								<input type="text" name="chargeMoney" value="" placeholder=""style="margin-right:10px;width: 350px;"/>
								<input type="text" name="chargemoney" value="" placeholder="最大数额" /> 
								</td>
								<td align="left" width="50%">收入资金（元）：
								<input type="text" name="incomingMoney" value="" placeholder=""style="margin-right:10px;width: 350px;"/>
								<input type="text" name="charge_money" value="" placeholder="最大数额" />
								</td>
							</tr> -->
							<tr>
								<td>
									<span class="tdtitle">所属用户：</span>
									<input type="text" name="username" value=""/>

								</td>
								<td>
									<span class="tdtitle">银行：</span>
									<input type="text" name="bankName"value="${bankAccountCriteria.bankName}"/>
								</td>
							</tr>
							<tr>
								<td>
									<span class="tdtitle">省份：</span>
									<input type="text" name="province"value="${bankAccountCriteria.province}"/>
								</td>
								<td>
									<span class="tdtitle">地市：</span>
									<input type="text" name="city"value="${bankAccountCriteria.city}"/>
								</td>
							</tr>
							<tr>
								<td>
									<span class="tdtitle">开户行：</span>
									<input type="text" name="issueBank"value="${bankAccountCriteria.issueBank}"/>
								</td>
								<td>
									<span class="tdtitle">账号类型：</span>
									<select name="bankAccountType" id="bankAccountType" xuzh='${bankAccountCriteria.bankAccountType}'>
										<option value="">所有</option>
										<option value="COMPANY">公司</option>
										<option value="PERSONAL">个人</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									<span class="tdtitle">开户人：</span>
									<input type="text" name="bankAccountName"value="${bankAccountCriteria.bankAccountName}"/>
								</td>
								<td>
									<span class="tdtitle">银行账号：</span>
									<input type="text" name="bankAccountNumber"value="${bankAccountCriteria.bankAccountNumber}"/>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align:center;padding-bottom: 0;padding-top: 17px;">
									<button class="btn btn-primary query" type="submit" value="查询" onClick="query()" onSubmit="return false;">
										&nbsp;&nbsp;&nbsp;&nbsp; <span class="glyphicon glyphicon-search"></span> 查询 &nbsp;&nbsp;&nbsp;&nbsp;
									</button>
								</td>
								<!-- <td align="left" width="50%">保证资金（元）：
								<input type="text" name="marginMoney" value="" placeholder=""style="margin-right:10px;width: 350px;"/>
								
								</td> -->
							</tr>  
							<tr>
								
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
								<th style="width: 150px;">所属用户</th>
								<th>银行</th>
								<th>省份</th>
								<th>地市</th>
								<th>开户行</th>
								<th>帐号类型</th>
								<th>开户人</th>
								<th>银行帐号</th>								
								<c:if test="${isPlatformGenericPartner == true }">
									<th>出款通道</th>			
								</c:if>					
								<th width="5%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td>${i.operate.username}[${i.uuid}]</td>
									<td>${i.bankName}</td>
									<td>${i.province}</td>
									<td>${i.city}</td>
									<td>${i.issueBank}</td>
									<td><c:if test="${i.bankAccountType=='COMPANY'}">公司</c:if><c:if test="${i.bankAccountType=='PERSONAL'}">个人</c:if></td>
									<td>${i.bankAccountName}</td>
									<td>${i.bankAccountNumber}</td>
									<c:if test="${isPlatformGenericPartner == true }">
										<td>${i.withdrawMethodId}-${i.operate.withdrawMethodName}</td>
									</c:if>
									<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
										<ul
											style="position: absolute; padding-right: 10px; padding-left: 10px; left: 50%; margin-left: -25px; top: 28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
											class="toolbtns">
											<input type="hidden" id="uuid" value="${i.uuid}" />
											
												<li class="remove" style="cursor: pointer;" bankAccountId="${i.bankAccountId}">删除</li>
											
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
	<script  type="text/javascript" src="/theme/${theme}/js/selectCity.js"></script>
	<script>
	$("#citys").citySelect({
    url:"/theme/${theme}/js/city.min.js",  
    prov:'北京', //省份 
    city:'东城区', //城市 
    dist:'', //区县 
    nodata:"none" //当子集无数据时，隐藏select 
    });
		//订单号
		function show(id, b, c) {
			var str = $("#" + b + id).html();
			if (str.indexOf('.') < 0) {
				str = str.substring(0, c) + "...";
				$("#" + b + id).text(str);
			} else {
				$("#" + b + id).text(id);
			}
		}
		//完成时间
		function showsj(id, b, c) {
			var str = $("#" + b + id).html();
			if (str.length <= 10) {
				$("#" + b + id).text(c);
			} else {
				c = str.substring(0, 10);
				$("#" + b + id).text(c);
			}
		}
		//购买用户
		function showfk(id, b, c, d) {
			var str = $("#" + b + id).html();
			if (str.indexOf('.') < 0) {
				str = str.substring(0, c) + "...";
				$("#" + b + id).text(str);
			} else {
				$("#" + b + id).text(d);
			}
		}
		//操作
		$(".tools").click(function() {
			$(this).parent().parent().siblings().find(".toolbtns").hide();
			$(this).siblings(".toolbtns").toggle();
		})
		//兑奖
		function exchange(a) {
			$.ajax({
				type : "POST",
				url : "/exchange/update/" + a + ".json",
				dataType : "json",
				success : function(data) {
					swal(data.message.message);
				}
			})
		}
	</script>
	<script>
		$('#bankAccountType option').each(function(){
			var zhi = $(this).attr('value');
			var xuzh = $('#bankAccountType').attr('xuzh');
			if (zhi==xuzh) {
				$(this).attr('selected','selected');
				$(this).siblings().remove('selected')
			};
		})
		function send(transactionId, url) {
			$(".Modal").css("display", "block");
			$(".Modal .submit").click(function() {
				if ($("#deliveryNum").val() == "") {
					alert("快递单号不能为空！")
				} else {
					$.ajax({
						type : "POST",
						url : url + ".json",
						data : {
							outDeliveryOrderId : $("#deliveryNum").val(),
							deliveryCompanyName : $("#companyName").val(),
							//transactionId:$(".transactionId").val(),
							transactionId : transactionId,
						},
						dataType : "json",
						success : function(data) {
							if (data.message.operateCode == 102008) {
								$(".Modal").css("display", "none");
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
		$(".Modal .cancel").click(function() {
			$(".Modal").css("display", "none");
		})
		$(".Modal .close").click(function() {
			$(".Modal").css("display", "none");
		})

		function updateExpress(transactionId, deliveryCompanyName,
				deliveryOrderId, url) {
			//alert(transactionId+", "+deliveryCompanyName+", "+deliveryOrderId+", "+url);
			$("#updateCompanyName").val(deliveryCompanyName);
			$("#updateDeliveryNum").val(deliveryOrderId);
			$(".updateModal").css("display", "block");
			$(".updateModal .submit")
					.click(
							function() {
								//alert("快递公司 ：" + $("#updateCompanyName").val() + "  快递单号：" + $("#updateDeliveryNum").val());
								if ($("#updateDeliveryNum").val() == "") {
									alert("快递单号不能为空！")
								} else {
									$
											.ajax({
												type : "POST",
												url : url + ".json",
												data : {
													outDeliveryOrderId : $(
															"#updateDeliveryNum")
															.val(),
													deliveryCompanyName : $(
															"#updateCompanyName")
															.val(),
													//transactionId:$(".transactionId").val(),
													transactionId : transactionId,
												},
												dataType : "json",
												success : function(data) {
													if (data.message.operateCode == 102008) {
														$(".Modal").css(
																"display",
																"none");
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
		$(".updateModal .cancel").click(function() {
			$(".updateModal").css("display", "none");
		})
		$(".updateModal .close").click(function() {
			$(".updateModal").css("display", "none");
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
$('.remove').on('click',function(){
	var bankAccountId = $(this).attr('bankAccountId');
	console.log(bankAccountId);
    if (confirm("确定要删除商品吗？")) {
      $.ajax({
      type:'post',
      url:'bankAccount/delete.json',
		dataType:'json',
      data:{
        bankAccountId:bankAccountId
      },
      success:function(data){
        window.location.reload();
      },
      error:function(data){
        alert(data.message.message);
      }
    })
    }
})
	</script>
</body>
</html>
