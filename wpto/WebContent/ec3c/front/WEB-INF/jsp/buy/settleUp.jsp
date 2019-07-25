<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName} 购物结算</title>
<link rel="stylesheet" type="text/css" href="/theme/ec1/style/main.css">
<link rel="stylesheet" type="text/css" href="/theme/ec1/style/pay.css">

<link href="/theme/ec1/style/com.css" rel="stylesheet" />
<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
<link href="/theme/ec1/style/web.css" rel="stylesheet" />
<link href="/theme/ec1/style/productlist.css" rel="stylesheet" />

<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />

<script  type="text/javascript" src="/theme/ec1/js/jquery-1.7.1.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="/theme/ec1/js/respond.src.js"></script>
<script  type="text/javascript" src="/theme/ec1/js/common.js"></script>


<script>
function refresh(){
					location.reload();
				}
function changeType(){
 location.href="/buy/settleUp.shtml?orderId="+${order.cartId}+"&payTypeId=2&title=";
}
</script>
</head>
<body>
   <%@include file="/WEB-INF/jsp/include/head.jsp" %>
   <div id="scanCode"></div>
	<c:choose>
  <c:when test="${!empty message}">   
      <c:if test="${message.operateCode==102167}">
          <input type="hidden" value="${message.content}" id="qrcode_url"/>  
		  <script  type="text/javascript" src="/theme/ec1/js/jquery.qrcode.min.js"></script>
		  <div class="content">
			<p>订单提交成功，请您尽快付款！订单号：${order.cartId}</p>
			<p class="fo-13">请您在提交订单24小时内完成支付，否则订单会自动取消。<span class="box_container_right">应付金额<span class="orange fo-20">￥${order.money.chargeMoney}元</span></span></p>
			<div class="box_QRcode">
			<p class="fo-20 textIn20">微信支付</p>
			<!--<p class="textAlignCenter red">二维码已过期，<a href="#" class="blue" onclick="refresh()">刷新</a>页面重新获取二维码</p>
			<p class="textAlignCenter">距离二维码过期还剩<span class="red">40</span>秒，过期后请刷新页面重新获取二维码。</p>-->
			<p class="textAlignCenter"><div id="code"></div></p>
			<p class="textAlignCenter">请用<span class="orange">微信</span>扫一扫</p>
			<p class="textAlignCenter">扫描二维码进行支付</p>
			<p class="textIn20"><a href="#" class="orange" onclick="changeType()">〈 选用微信进行支付</a></p>
			</div>
		  </div>
		  <script>
				$("#code").qrcode({      
								  width: 182, //宽度 
								  height:182, //高度 
								  text: "${message.content}"
								});
			
		  </script>
      </c:if> 
	  <c:if test="${message.operateCode == 102124}">
			<script>
			     $(function(){
					 window.location.href = '${message.message}';
				 })
			</script>
	  </c:if> 
	  <c:if test="${message.operateCode == 102169}">
			<script>
			     $(function(){
					 $('#scanCode').append('<iframe src="${message.message}" style="width:100%;height:500px;"></iframe>');
				 })
			</script>
	  </c:if>
  </c:when> 
  <c:otherwise>
        <script>
		   alert("系统繁忙！");
	    </script>
  </c:otherwise> 
</c:choose>
 	<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
	
	
</body>
</html>
