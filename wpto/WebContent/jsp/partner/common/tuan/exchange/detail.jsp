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

    <title>${systemName}-${title}</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	 <link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8">
        window.UEDITOR_HOME_URL = location.protocol + '//'+ document.domain + (location.port ? (":" + location.port):"") + "/ueditor/";
		//alert(window.UEDITOR_HOME_URL);
    </script>
	<script type="text/javascript" src="/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="/ueditor/ueditor.all.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<style>
		
		.fullScreenBg{
			position:fixed;
			background:#333;
			opacity:0.7;
			z-index:200;
			width:100%;
			height:100%;
		}
		.message{
			position:fixed;
			z-index:300;
			background:#fff;
			height:230px;
			width:300px;
			left:50%;
			top:230px;
			margin-left: -150px;
			text-align:center;
			border-radius:5px;
			overflow:hidden;
			padding-top:30px;
		}
		.sidebar{
			z-index:20;
		}
		.message .submit{
			border-radius:5px;
			height:40px;
			line-height:40px;
			text-align:center;
			width:100px;
			color:#fff;
			font-size:20px;
			background:#333;
			display:inline-block;
			position:absolute;
			bottom:20px;
			left:50%;
			margin-left:-50px;
			cursor:pointer;
		}
		.message .text{
			display:inline-block;
			font-size:18px;		
		}
		.submit{width:100%;text-align:center;}
		
		
		
		.table-responsive table tr .itemLeft{
			background-color:#EEEEEE;
			padding-right:25px;
			padding-top:20px;
		}
		.table-responsive table tr .itemRight{
			background-color:#FFFFFF;
			padding-top:18px;
			padding-left: 70px;
		}
		.table-responsive table tr{
			height:60px;
		}
		.table-responsive table tr td input{
			width:23%;
		}
		.table tbody tr{
			border-left:1px solid #ddd;
			border-right:1px solid #ddd;
			border-bottom:1px solid #ddd;
		}
		
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
		<div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header">yyyyyy${title}</h2>
			<!--<p style="padding-bottom:10px;font-weight:bold;display:inline-block;" id="documentchangetype">产品类型一旦确认后续将不能改变</p>-->
			<div class="table-responsive">	
				<table class="table table-striped" style="margin-top:15px;">
					 <!--<thead>
		                <tr>
							<th></th>
							<th></th>	
							<th></th>
		                </tr>
		            </thead>-->
					<tbody>
						<tr>
							<td style="width:80%;" class="itemLeft" colspan="2"><p style="text-align:left; padding-left:20%">订单号：${cart.cartId}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;下单时间：<fmt:formatDate value="${cart.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p></td>
							<td class="itemLeft" style="text-align:right"><p><spring:message code="Status.${cart.currentStatus}" /></p></td>
						</tr>
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>购买用户（昵称）：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2"><p>${userName}</p></td>
						</tr>
						
						<c:forEach var="item" items="${itemLists}" varStatus="status">
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>商品清单：</p></td>
							<td style="width:40%; text-align:left; border-left:1px solid #ddd;" class="itemRight">
								<p>购买产品：${item.productId}（${item.name}）</p>
								<img src="${item.itemDataMap.productSmallImage.dataValue}" />
								
							</td>
							<td style="width:40%; text-align:left; text-align:left;" class="itemRight">
								<p>名称：${item.name}</p>
								<p>规格：${item.itemDataMap.goodsSpec.dataValue}</p> 
								<p>单价：${item.price.money}</p> 
								<P>数量：${item.count} </P>
								<p>运费：${deliveryOder.fee.money}</p>
							</td>
						</tr>
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>配送信息：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2">
								<p>快递公司：${item.itemDataMap.deliveryCompanyName.dataValue}</p>
								<p>快递单号：${item.itemDataMap.deliveryOrderId.dataValue}</p>
							</td>
						</tr>
						</c:forEach>
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>收货地址：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2">
								<p>收货人：${deliveryOder.contact}</p> 
								<p>联系方式：${deliveryOder.mobile}</p> 
								<p>收货地址：${deliveryOder.country}&nbsp;${deliveryOder.province}&nbsp;${deliveryOder.city}&nbsp;${deliveryOder.district}&nbsp;${deliveryOder.address}</p> 
							</td>
						</tr>
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>抵扣信息：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2">
								<p>代金券抵扣：<c:choose>
													<c:when test="${!empty cart.money.giftMoney}">
														${cart.money.giftMoney}
													</c:when>
													<c:otherwise>
														0.00
													</c:otherwise>
												</c:choose>元</p>
								<p>优惠券优惠(暂无)：0元 </p>
							</td>
						</tr> 	
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>支付信息：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2"><p><spring:message code="Status.${cart.currentStatus}" /></p></td>
						</tr> 
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>发票信息：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2">
								<p id="fa_title_top">发票抬头：</p>
								<p id="fa_content">发票内容：</p>
							</td>
						</tr> 
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>物流信息：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2"></td>
						</tr>
						<tr>
							<td style="width:20%; text-align:right;" class="itemLeft"><p>价格清单：</p></td>
							<td style="width:80%; text-align:left; border-left:1px solid #ddd;" class="itemRight" colspan="2">
								<p>金额总计：<fmt:formatNumber value="${cart.money.chargeMoney + cart.money.giftMoney + cart.money.coin}" maxFractionDigits="2"/>元<p> 
								<p>（运费总计：
												<c:choose>
													<c:when test="${!empty deliveryOder.fee.money}">
														${deliveryOder.fee.money}
													</c:when>
													<c:otherwise>
														0.00
													</c:otherwise>
												</c:choose>元）</p>
								<p>代金券抵扣：<c:choose>
													<c:when test="${!empty cart.money.giftMoney}">
														${cart.money.giftMoney}
													</c:when>
													<c:otherwise>
														0.00
													</c:otherwise>
												</c:choose>元</p>
								<p>优惠券优惠(暂无)：0元</p>
								<p>${coinName}：
												<c:choose>
													<c:when test="${!empty cart.money.coin}">
														<fmt:formatNumber value="${cart.money.coin}" maxFractionDigits="2"/>
													</c:when>
													<c:otherwise>
														0.00
													</c:otherwise>
												</c:choose>${coinName}</p> 
								
								<p>实付款：<fmt:formatNumber value="${cart.money.chargeMoney}" maxFractionDigits="2"/>元 + <fmt:formatNumber value="${cart.money.coin}" maxFractionDigits="2"/>${coinName}</p>
							</td>
						</tr> 
						<tr>
							<td style="width:100%" class="itemLeft" colspan="3">
								<p><spring:message code="Status.${cart.currentStatus}" /></p>
								<p>下单时间：<fmt:formatDate value="${cart.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
							</td>
						</tr> 
					</tbody>
					<!--<div class="close">
						<input type="button" class="btn btn-primary" value="关闭查看" />
					<div>-->
				</table>
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
	<script src="/theme/${theme}/js/lib/jquery.cookie.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.treeview.js" type="text/javascript"></script>	
	<script src="/theme/${theme}/js/jquery.form.3.5.1.js" type="text/javascript"></script>
	<script type="text/javascript">
		//{"syncFlag":1,"ownerId":200008,"invoiceId":7,"uuid":101283861,"title":"北京盛世通宝科技有限公司"});
		var title_top = ${cart.invoiceInfo}
		$("#fa_title_top").text("发票抬头："+title_top.title);
		if(title_top.indexOf("content") >= 1){
			$("#fa_content").text("发票内容："+title_top.content);
		}else{
			$("#fa_content").text("发票内容：");
		}
		
	</script>
	
	
  </body>
</html>
