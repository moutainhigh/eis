<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../css/main.css">
<link rel="stylesheet" type="text/css" href="../css/orderlist.css">
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/order.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
function abolish(orderId){
    var r=confirm("确定要取消吗?");
    console.log(orderId);
	if(r==false){
		return;
	}
    $.ajax({
        url:"/order/cancel/"+orderId+".json",
        type:"get",
        dataType:'json',
        success:function(data){
            //ajax返回的数据
            //console.log(data.message.operateCode);
            //console.log(operateResultSuccess);
            
            if(data.message.operateCode != operateResultSuccess){

                alert(data.message.message);
            }
            else if (data.message.operateCode == operateResultSuccess) { //成功
                alert("取消成功!");
                //location.reload();
            }
			location.reload();
        }
    });
}
function getDeliveryInfo(orderId){
	$(".msg").show();
	$.ajax({ 
	type: "GET", 	
	url: "/deliveryOrder/traceByDeliveryOrder/"+orderId+".json",
	data:{returnUrl:location.href},
	dataType: "json",
	success: function(data){
		if(data.deliveryOrder){
			url=data.deliveryOrder.traceData.traceUrl;
			url+=(url.indexOf('?')!=-1?"&":"?")+"timeStamp="+(new Date()).getTime();
			location.href=url;
		}else if(data.message){
			$(".msg").hide();
			alert(data.message.message);
		}
	},				
	});	
}
function getDeliveryInfo(orderId){
	$(".msg").show();
	$.ajax({ 
	type: "GET", 	
	url: "/deliveryOrder/traceByDeliveryOrder/"+orderId+".json",
	data:{returnUrl:location.href},
	dataType: "json",
	success: function(data){
		if(data.deliveryOrder){
			url=data.deliveryOrder.traceData.traceUrl;
			location.href=url;
		}else if(data.message){
			$(".msg").hide();
			alert(data.message.message);
		}
	},				
	});	
}

/*function coupon(){
	$.ajax({ 
	type: "post", 	
	url: "/order/fetchCoupon.shtml",
	data:{},
	dataType: "text",
	success: function(data){
		alert("成功");
		alert(data);
	},
	error: function(XMLHttpRequest, textStatus, errorThrown) {
		alert(XMLHttpRequest.status);
		alert(XMLHttpRequest.readyState);
		alert(textStatus);
	},		
	});	
}*/
var clickCount = 0;
function coupon(){
	if (clickCount > 0) {
           alert("您已经点击过了。");
           return ;
     }
	$.ajax({ 
	type: "post", 	
	url: "/order/fetchCoupon.shtml",
	data:{},
	dataType: "text",
	success:function(data){
		if(data){
			alert(data);
		} else {
			alert("充值失败");
		}
	}
	})
	clickCount++; //点击统计累加
}

</script>
</head>
<body>
  
<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50 padbtm50">
     <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox">
	   <div><a href="#">账户中心</a>><a href="#">我的订单</a></div>
	   <div class="BoxOrder martop10">
	      <ul class="btnsOrder1">
		     <li><a href="/order/index.shtml">全部订单</a></li>
			 <li><a href="/order/index.shtml?currentStatus=710050">待收货</a></li>
			 <li><a href="/order/index.shtml?currentStatus=710019">待付款</a></li>
			 <li><a href="/order/index.shtml?currentStatus=710052">待评价</a></li>
		  </ul>
		  <div class="box_order_title">
		  <ul class="btnsOrder2">
		     <li class="li1">商品</li>
			 <!--<li class="li2">单价(元)</li>-->
			 <li class="li2">件数</li>
			 <li class="li3">单价(元)</li>
			 <li class="li4">交易状态</li>
			 
			
		  </ul>
		  <div class="operation">交易操作</div>
		  </div>
		  <c:forEach var="order" items="${orderList}">
		   <c:if test="${order.currentStatus!=710056}">
		  <ul class="orderlist fo-12">
			  <!--<span>${order.cartId}</span>-->
			  <div class="orderNum">订单编号：${order.cartId}&nbsp;&nbsp;<fmt:formatDate value="${order.createTime}" type="both"/></div>
			  <div class="box_order">
			  <div class="box_left">
			   <c:forEach var="product" items="${order.miniItemList}" varStatus="i">
			   <div class="boxUl">
			     <li class="li1">
			    <div class="productlist">
				 <a href="${product.itemDataMap.refUrl.dataValue}">
				   <img src="${product.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left"/>
				   <div class="box_container_left">
				      <p>${product.name}  <span class="box_container_right"></span></p>
					  <p>${product.content}</p>
				   </div>
				   </a>
				 <!--<span>单价：${product.price.money}</span>-->
				</div>
				</li>
				<li class="li2"><p>${product.count}</p></li>
			    <li class="li3 orange">
			     <p>￥<fmt:formatNumber value="${product.price.money}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></p>
			    </li>
			    <li class="li4">
			     <p><spring:message code="Status.${order.currentStatus}" /></p>
			    </li>
			  </div>
				</c:forEach>
			</div>
			 <div class="operation1">
			   <c:choose>
			  	 <c:when test="${order.currentStatus==710050}">
			    <c:forEach var="product" items="${order.miniItemList}" varStatus="i">
			    <c:if test="${i.index==0}">
				<div class="btn-group-order">
			    <a href=""><p class="orange" onclick="ok('${product.transactionId}')">确认收货</p></a>
			     <a href="javascript:getDeliveryInfo(${order.deliveryOrderId});"><p class="orange">查看物流</p></a>
				 </div>
				</c:if>
				 </c:forEach>
			   </c:when>
			     <c:when test="${order.currentStatus==710017}">
			    <c:forEach var="product" items="${order.miniItemList}" varStatus="i">
				
			      <div class="btn-group-order">
		           <a href="${product.itemDataMap.refUrl.dataValue}" class="orange"><p>再次购买</p></a>
				   </div>
			
				</c:forEach>
				</c:when>
			    <c:when test="${order.currentStatus==710051}">
			   <c:forEach var="product" items="${order.miniItemList}" varStatus="i">
			     <c:if test="${i.index==0}">
			     <div class="btn-group-order">
						<p style="color:#ababab;">确认收货</p>
				   </div>
				</c:if>
				</c:forEach> 
                </c:when>
                 <c:when test="${order.currentStatus==710017}">
			  	 <a href="javascript:cancel(${order.cartId})">
				 <span style="float: right; margin-right: 10px;">删除订单</span>
				</a> 
			   </c:when> 
			   <c:when test="${order.currentStatus==710019}">
			   <div class="btn-group-order">
			      <a href="/buy/settleUp.shtml?orderId=${order.cartId}"><p class="orange">去付款</p></a>
				  <a><p class="orange" onclick="abolish(${order.cartId})">取消订单</p></a>
				 </div>
			   </c:when>
			   <c:when test="${order.currentStatus==710010}">
			   <div class="btn-group-order">
			     <a href="javascript:getDeliveryInfo(${order.deliveryOrderId});"><p class="orange">查看物流</p></a>
				 </div>	   
			   </c:when>
			    <c:when test="${order.currentStatus==710052}">
				 <c:forEach var="product" items="${order.miniItemList}" varStatus="i">
				 <c:if test="${product.currentStatus==710052}">
				<div class="btn-group-order">
			        <p ><a href="/order/addComment.shtml?tid=${product.transactionId}" class="orange">评价</a></p> 
					<a href="javascript:getDeliveryInfo(${order.deliveryOrderId});"><p class="orange">查看物流</p></a>
					 
					 <c:forEach items="${isCoupon}" var="coupon">
					  <c:if test="${product.itemId==coupon.key&&coupon.value==true}">
						<a href="javascript:coupon();"><p class="orange">优惠券</p></a>	
					  </c:if>
					</c:forEach>
				</div>
				</c:if>
				<c:if test="${product.currentStatus==710053}">
				 <div class="btn-group-order">
			         <p><a style="color:#ababab;">已评价</a></p> 
					<a href="javascript:getDeliveryInfo(${order.deliveryOrderId});"><p class="orange">查看物流</p></a>
					</div>
				</c:if>
				</c:forEach>
				</c:when>
			   <c:when test="${order.currentStatus==710053}">
				   <div class="btn-group-order">
			         <p><a style="color:#ababab;">已评价</a></p> 
					<a href="javascript:getDeliveryInfo(${order.deliveryOrderId});"><p class="orange">查看物流</p></a>
					</div>
			   </c:when>
			   <c:otherwise>
			    <div class="btn-group-order">
			    <span style="margin-right: 10px;"> <spring:message code="Status.${order.currentStatus}"/></span> 
				</div>
			   </c:otherwise>
			  </c:choose>
			 </div>
			</div>
			 <div class="orderNum">
			    <c:set var="totalM" value="${!empty order.data.deliveryFee?order.data.deliveryFee:0}"/>
			    <span>实付款:￥<fmt:formatNumber value="${order.money.chargeMoney + totalM}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>(运费￥<fmt:formatNumber value="${!empty order.data.deliveryFee?order.data.deliveryFee:0.00}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>)</span>
				<a class="orange" style="float:right" href="/order/get.shtml?cartId=${order.cartId}">查看订单详情</a>
			 </div>
		  </ul>
		  </c:if>
		  </c:forEach> 
		 <%@include file="/WEB-INF/jsp/include/page.jsp" %>
	   </div>
	   
	 </div>
  </div>
  <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>