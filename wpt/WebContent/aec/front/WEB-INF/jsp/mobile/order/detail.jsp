<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" style="background:#f5f5f5;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title>${systemName}-订单详情</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/iconfont.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/orderdetail.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/orderlist.css"/>

<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/order.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script>
function getDeliveryInfo(orderId){	
	$(".msg").show();
	//window.setTimeout('showIframe()',2000);
	$.ajax({ 
	type: "GET", 	
	url: "/deliveryOrder/traceByDeliveryOrder/"+orderId+".json",
	data:{returnUrl:location.href},
	dataType: "json",
	success: function(data){
		if(data.deliveryOrder){
			//console.log(data.deliveryOrder.traceData.traceUrl);
			url=data.deliveryOrder.traceData.traceUrl;
			location.href=url;
			//url+=(url.indexOf('?')!=-1?"&":"?")+"timeStamp="+(new Date()).getTime();
			//$(".iframe iframe").attr("src",url);
			//window.frames["kuaidi"].location.href=url;
			clear();
		}else if(data.message){
			$(".msg").hide();
			alert(data.message.message);
		}
	},				
	});	
}
function cancel(orderId){
	$.ajax({ 
	type: "GET", 	
	url: "/order/cancel/"+orderId+".json",
	dataType: "json",
	success: function(data){
		JSON.stringify(data);
	},				
	});	
}
function showIframe(){
	$(".iframeHeader").show();
	$(".iframe").show();
}
function clear() {   
   	$(".msg").hide();	
} 
function closeIframe(){
	$(".msg").hide();
	$(".iframe").hide();
	$(".iframeHeader").hide();
}
$(function(){
	//$(".iframe iframe").attr("src","");
})
</script>
<style>
	.msg{
		position:fixed;
		width:50%;
		top:30%;
		left:25%;
		text-align:center;
		padding:10px 0 10px;
		border-radius:8px;
		background:#222222;
		opacity:0.8;
		color:#ffffff;
		display:none;
	}
	@media (min-width:768px){
		.msg{
			position:fixed;
			width:300px;
			top:30%;
			left:50%;
			margin-left:-150px;
			text-align:center;
			padding:10px 0 10px;
			border-radius:8px;
			background:#222222;
			opacity:0.8;
			color:#ffffff;
			display:none;
		}
	}
	.iframe{
		position:fixed;
		z-index:100;
		width:100%;
		height:100%;
		display:none;
		left:0;
		top:0;
	}
	.iframeHeader{
		height:60px;
		position:fixed;
		z-index:120;
		width:100%;
		left:0;
		top:0;
		background: #fff;
		font-size: 16px;
		font-weight: bold;
		color: #ff6400;
		-moz-box-sizing: border-box;
		box-sizing: border-box;
		min-width: 300px;
		margin: 0 auto!important;
		border-bottom: 1px #c9c9c9 solid;
		line-height:60px;
		text-align:center;
		display:none;
	}
	.iframeHeader .back{
		background: rgba(0, 0, 0, 0) url("/image/mobile/header/left.png");
		display: block;
		height: 19px;
		left: 10px;
		top: 50%;
		width: 11px;
		position: absolute;
		margin-top: -9.8px;
		-webkit-background-size: 11px 19px;
		-moz-background-size: 11px 19px;
		-o-background-size: 11px 19px;
		background-size: 11px 19px;
	}
</style>
</head>
<body>
<div class="header" id="header">

	  <a class="back" href="javascript:history.go(-1);"></a><span >订单详情</span><a class="person" href="/"></a>
	</div> 
	<div id="wrapper_1">
	   <div class="box_title">
			<div class="wid90">
				<div class="box_left">
					订单状态：<span class="orange"><spring:message code="Status.${order.currentStatus}" /></span>
				</div>
			</div>
	  </div>
	  <!--<c:if test="${!empty deliveryOrder.memory}">
	  <c:if test="${order.currentStatus==710051||order.currentStatus==710050}">
	  <div class="box_title">
			<div class="wid90">
				物流信息
			</div>
	  </div>
	 <div class="line"></div> 
	  <ul class="order-status">	
		<a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')" style="display:block;">
		 <li class="wid90">
		    <div class="box_left">
				${deliveryOrder.memory}
			</div>
			<div class="box_center orange">
				<img src="../../../../image/more.png" style="margin-top:10px;">
			</div>
		</li> 
		</a>
	  </ul>
	  <div class="line"></div>
	  </c:if>
	  </c:if>-->
	  <!-- 地址 -->
	  <div class="order-address">
	  	<div class="icon">
	  		<i class="icon iconfont icon-ditu"></i>
	  	</div>
	  	<div class="text">
	  		<p>收货人：${deliveryOrder.contact}   <span style="float: right">${deliveryOrder.mobile}</span></p>
		 	<p>${deliveryOrder.country}${deliveryOrder.province}&nbsp;${deliveryOrder.city}&nbsp;${deliveryOrder.district}&nbsp;${deliveryOrder.address}</p>
	  	</div>
	  </div>
	  <!-- 商品 -->
	  <div class="product">
		<div class="box_title">
			<div class="wid90">
				商品清单
			</div>
	  </div>
	  <ul class="buy-list">
	  <c:set var="cm" value="0"></c:set>
		<c:forEach var="product" items="${order.miniItemList}" varStatus="i">
	    <li class="box_product">
	     	<a href="">
		   <!-- <div style="padding-bottom:10px;color:#96989a;">时间：<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div> -->
		   <div style="display: flex;">
	       <img src="${product.itemDataMap.get('productSmallImage').dataValue}"/> 
		   <div class="box_right2" style="flex: 1">
			  <div style="width: 75%;display: inline-block; box-sizing: border-box;
    vertical-align: top;"> <span class="ware_name">${product.name}</span></div>
			  <div style="width: 20%;display: inline-block;">
			  <div class="one_sprice"><span class="price_mark">￥${product.price.money}</span> </div>
			  <div class="divCenter"> <span class="ware_from">${product.itemDataMap.get('goodsSpec').dataValue}</span><span class="all_num">×${product.count}</span></div></div>
              
		   </div>
		   </div>
				</a>
		  </li> 
	       
		</c:forEach>
	  </ul>
	 <!--  <c:if test="${product.currentStatus==710051||product.currentStatus==710050}">
             <span><a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')" style="display:block;">${deliveryOrder.memory}点击查看物流信息</a></span>
       </c:if> -->
	   <c:set var="cm" value="${product.price.money * product.count + cm}"></c:set>
	  </div>
	  <div class="fapiao">
	     <div class="wid90">
		          <div class="box_left" style="float: left">
					发票抬头
				</div>
				<div class="box_right invoiceInfo" style="float:right;"></div>
					<script>	
						invoice=${!empty order.invoiceInfo?order.invoiceInfo:"{}"}; 
						console.log(invoice.title);						
						var title=invoice.title;
						if(title==undefined){
							title="无";
						}
						$(".invoiceInfo").html(title);							
					</script>				
			</div>
	  </div>
	   <ul class="order-status">
	    <li class="wid90">
		        <div class="box_left">
					商品总价
				</div>
				<div class="box_right cloeee">
					￥<span id="spzj"><fmt:formatNumber value="${cm+order.money.giftMoney+order.money.transitMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span>
				</div>
			
		</li>
		 <li class="wid90">
		        <div class="box_left ">
					运费（快递）
				</div>
				<div class="box_right cloeee">
					￥<span id="kdf"><fmt:formatNumber value="${!empty deliveryOrder.fee.money?deliveryOrder.fee.money:0.00}" pattern="#0.00"/></span>
				</div>
			
		</li>
		 <li class="wid90">
		        <div class="box_left ">
					代金券抵扣
				</div>
				<div class="box_right cloeee">
					￥<span id="djq"><fmt:formatNumber value="${!empty order.money.giftMoney?order.money.giftMoney:0.00}" pattern="#0.00"/></span>
				</div>
		</li>
		<li class="wid90">
		        <div class="box_left">
					<span style="font-size: 14px;">订单总价</span>
				</div>
				<div class="box_right orange ddzj">
					￥<span></span>
				</div>
		</li>
		
	  </ul>
	  <ul class="lastul">
		<li>订单编号：${order.cartId}</li>
		<li>创建时间：<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></li>
	  </ul>
	  
		<c:choose>
			   <c:when test="${order.currentStatus==710050}">
			    	  <div class="btn_group">
							<a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')" class="btn_a box_left">查看物流</a>
							<a href="javascript:ok('${order.transactionIds[0]}')" class="btn_b box_right">确认收货</a>
						</div>
			   </c:when>
			   <c:when test="${order.currentStatus==710017}">
			     
			   </c:when>
			   <c:when test="${order.currentStatus==710051}">
			     	  <div class="btn_group_gray">
							<a class="btn_c box_left">查看物流</a>
							<a class="btn_d box_right">确认收货</a>
						</div>
			   </c:when>
			   <c:when test="${order.currentStatus==710019}">
			   	  <div class="btn_group">
						<a href="javascript:cancel('${order.cartId}');" class="btn_a box_left">取消订单</a>
						<a href="#" class="btn_b box_right">立即支付</a>
				  </div>
			   </c:when>
			   <c:when test="${order.currentStatus==710010}">
			     	  <div class="btn_group">
							<a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')" class="btn_a box_left">查看物流</a>
							<a href="javascript:ok('${order.transactionIds[0]}')" class="btn_b box_right">确认收货</a>
						</div>
			   </c:when>
			    <c:when test="${order.currentStatus==710052}">
			    <c:forEach var="product" items="${order.transactionIds}" varStatus="i">
			       	   <div class="btn_group">
							<a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')" class="btn_a box_left">查看物流</a>
							<a href="/order/addComment.shtml?tid=${product}" class="btn_b box_right">评价</a>
						</div>
				  </c:forEach>
			   </c:when>
			   <c:when test="${order.currentStatus==710053}">
			        	  <div class="btn_group">
							<a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')" class="btn_a box_left">查看物流</a>
							<a href="${deliveryOrder.}" class="btn_b box_right">再次购买</a>
						</div>
			   </c:when>
			   <c:otherwise>
			   </c:otherwise>
			  </c:choose>

	</div>
	<div class="iframe">
		<iframe src='' height="100%" width="100%" frameborder="0" name="kuaidi"></iframe>
	</div>
	<div class="iframeHeader">
		<a class="back" href="javascript:closeIframe();"></a><span>物流信息</span>	
	</div>
	<div class="msg">
		正在处理，请稍等…
	</div>
</body>
<script>
$(function(){
	$('.ddzj span').text(parseFloat($('#spzj').text())+parseFloat($('#kdf').text())-parseFloat($('#djq').text()))
})
</script>
</html>