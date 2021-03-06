<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" style="background:#f5f5f5;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/orderlist.css"/>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/orderlist.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="/content/user/pcenter.shtml?favPage=1&favRows=8"></a><span>我的订单</span><a class="list2" href="/"></a>
 </div>
<div class="wrapper_1">
      <ul class="ui-btn-group">
	    <li><a href="/order/index.shtml?page=1&rows=10" class="current">全部订单</a></li>
	    <li><a href="/order/index.shtml?currentStatus=710019&page=1&rows=10">待付款</a></li>
		<li><a href="/order/index.shtml?currentStatus=710050&page=1&rows=10">待收货</a></li>
		<li><a href="/order/index.shtml?currentStatus=710052&page=1&rows=10">待评价</a></li>
	  </ul>
 <c:forEach var="order" items="${orderList}" varStatus="in">
      <c:if test="${order.currentStatus!=710056}">
	  <div class="box_order">
	   <div class="orderInfo">
	     <div class="left">订单编号:${order.cartId}</div>
		 <div class="right"><spring:message code="Status.${order.currentStatus}"/></div>
	    </div>
		<div class="line"></div>
		<c:forEach var="product" items="${order.miniItemList}" varStatus="i">
	     <div class="box_product">
		   <div style="padding-bottom:10px;color:#96989a;">时间：<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
	       <div class="ware_img"><img src="${product.itemDataMap.get('productSmallImage').dataValue}"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_name">${product.name}</span></div>
			  <div class="divCenter"> <span class="ware_from">${product.itemDataMap.get('goodsSpec').dataValue}</span><span class="all_num">×${product.count}</span></div>
              <div class="one_sprice"><span class="price_mark">￥${product.price.money}</span> </div>
		   </div>
		   <div style="clear:both;"></div>
			    <c:choose>
				<c:when test="${order.currentStatus==710050}">
			   <div class="btn-group">
			   	 <a href="javascript:confirmReceived('${product.transactionId}')" class="qufukuan">确认收货</a>
			     <a href="javascript:getDeliveryInfo('${order.deliveryOrderId}');" class="quxiaodingdan">查看物流</a>
			   </div>
			   </c:when>
			   <c:when test="${order.currentStatus==710051}">
			   <div class="btn-group">
			     <a href="javascript:void(0);" class="querenshouhuo1">查看物流</a>				 
				 <a href="javascript:void(0);" class="querenshouhuo1">确认收货</a>
			   </div>
			   </c:when>
			     <c:when test="${order.currentStatus==710019}">
			   <div class="btn-group">
			     <a href="/buy/settleUp.shtml?orderId=${order.cartId}" class="qufukuan">去付款</a>
				 <!--a href="#" class="quxiaodingdan">取消订单</a-->
			   </div>
			   </c:when>
			   <c:when test="${order.currentStatus==710010}">
			   <div class="btn-group">
					<a href="javascript:getDeliveryInfo('${order.deliveryOrderId}');" class="quxiaodingdan">查看物流</a>	
					<a href="javascript:void(0);" class="qufukuan">确认收货</a>
			   </div>
			   </c:when>
			    <c:when test="${order.currentStatus==710052}">
			   <div class="btn-group">
			     <a href="${product.itemDataMap.refUrl.dataValue}" class="quxiaodingdan">再次购买</a>
				 <a href="/order/addComment.shtml?tid=${product.transactionId}" class="qufukuan">评价</a>
			   </div>
			   </c:when>
			   <c:when test="${order.currentStatus==710053}">
			   <div class="btn-group">
			     <a href="${product.itemDataMap.refUrl.dataValue}" class="quxiaodingdan">再次购买</a>
			   </div>
			   </c:when> 
			    <c:when test="${order.currentStatus==710017}">
			  	 <a href="javascript:cancel(${order.cartId})">
				 <span style="float: right; margin-right: 10px;">删除订单</span>
				</a> 
			   </c:when> 
			   <c:otherwise >
				 <span style="float: right; margin-right: 10px;"> <spring:message code="Status.${product.currentStatus}"/></span>
			   </c:otherwise>			  
			  </c:choose>	
		  </div> 
		  <div class="line"></div>
		</c:forEach>
		<c:set var="totalM" value="${!empty order.data.deliveryFee?order.data.deliveryFee:0}"/>
		  <div class="wid90 box_money">
			 <span class="orange"><a href="/order/get.shtml?cartId=${order.cartId}">订单详情>></a></span>
		     <!-- <span class="fare">(运费：<span style="color:#ff6400;">￥<fmt:formatNumber value="${!empty order.data.deliveryFee?order.data.deliveryFee:0.00}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span>)</span> -->实付款:<span class="orange">￥<fmt:formatNumber value="${chargeMoney[in.index] + totalM}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span>
		  </div>
		  <div class="line"></div>	
		 </c:if>
 </c:forEach>		  
      <%@include file="/WEB-INF/jsp/include/page.jsp" %>
	  </div>
	<div class="msg">
		正在处理，请稍等…
	</div> 
	<%@include file="/WEB-INF/jsp/include/footer.jsp" %>   
</body>
</html>