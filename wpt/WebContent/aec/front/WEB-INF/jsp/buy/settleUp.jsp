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
<link rel="stylesheet" type="text/css" href="../css/pay.css">
<script  type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../js/common.js"></script>
<script  type="text/javascript" src="../js/jquery.qrcode.min.js"></script>
<script  type="text/javascript" src="../js/scancode.min.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

var timer;
function refresh(){
					location.reload();
				}
function changeType(){
 location.href="/buy/settleUp.shtml?orderId="+${order.cartId}+"&payTypeId=2&title=";
}
$(document).ready(function(){
	timer=setInterval("autoQuery('${payTransactionId}')",3e3);
})
function toUtf8(str) {    
    var out, i, len, c;    
    out = "";    
    len = str.length;    
    for(i = 0; i < len; i++) {    
        c = str.charCodeAt(i);    
        if ((c >= 0x0001) && (c <= 0x007F)) {    
            out += str.charAt(i);    
        } else if (c > 0x07FF) {    
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));    
            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));    
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));    
        } else {    
            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));    
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));    
        }    
    }    
    return out;    
} 
</script>
</head>
<body>
    <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	    <c:choose> 
  <c:when test="${!empty message}">   
      <c:if test="${message.operateCode==102167}">
    <div class="content">
	  <div class="box_container_left">   
        <p>订单提交成功，请您尽快付款！订单号：${order.cartId}</p>
		<p class="fo-13 martop10">请您在提交订单
		<c:choose>
			   <c:when test="${!empty order.ttl}">
                   <c:if test="${order.ttl >= 3600}"><fmt:formatNumber type='number' value='${order.ttl/3600}'  maxFractionDigits="0"></fmt:formatNumber>小时</c:if>
				   <c:if test="${order.ttl < 3600}"><fmt:formatNumber type='number' value='${order.ttl/60}'  maxFractionDigits="0"></fmt:formatNumber>分钟</c:if>
               </c:when>
				<c:otherwise>24小时</c:otherwise>
			</c:choose>
			内完成支付，否则订单会自动取消。</p>
		
		</div>
		<div class="box_container_right" style="text-align:right"><p>应付金额<span class="orange">￥<fmt:formatNumber value="${order.money.chargeMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>元</span></p></div>
		<div class="box_QRcode  martop10">
		  <p class="fo-20 textIn20">微信支付</p>
		  <!--<p class="textAlignCenter red">二维码已过期，<a href="#" class="blue" onclick="refresh()">刷新</a>页面重新获取二维码</p>
		  <p class="textAlignCenter">距离二维码过期还剩<span class="red">40</span>秒，过期后请刷新页面重新获取二维码。</p>--> 
		  <p class="textAlignCenter"><div id="code"></div></p>
		  <p class="textAlignCenter">请用<span class="orange">微信</span>扫一扫</p>
		  <p class="textAlignCenter">扫描二维码进行支付</p>
		  <!-- <p class="textIn20"><a href="#" class="orange" onclick="changeType()">〈 选用支付宝进行支付</a></p> -->
		</div>
	</div>
          ${message.content}
          <input type="hidden" value="${message.content}" id="qrcode_url"/>
		  <script>
				$("#code").qrcode({      
     
								  width: 182, //宽度 
								  height:182, //高度 
								  text: "${message.content}"
								});
			
		  </script>
      </c:if> 
	  <c:if test="${message.operateCode==102169}">
	    <div class="content">
	  <div class="box_container_left">
        <p>订单提交成功，请您尽快付款！订单号：${order.cartId}</p>
		<p class="fo-13 martop10">请您在提交订单
		<c:choose>
			   <c:when test="${!empty order.ttl}">
                   <c:if test="${order.ttl >= 3600}"><fmt:formatNumber type='number' value='${order.ttl/3600}'  maxFractionDigits="0"></fmt:formatNumber>小时</c:if>
				   <c:if test="${order.ttl < 3600}"><fmt:formatNumber type='number' value='${order.ttl/60}'  maxFractionDigits="0"></fmt:formatNumber>分钟</c:if>
               </c:when>
				<c:otherwise>24小时</c:otherwise>
			</c:choose>
			内完成支付，否则订单会自动取消。</p>
		
		</div>
		<div class="box_container_right" style="text-align:right"><p>应付金额<span class="orange">￥<fmt:formatNumber value="${order.money.chargeMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>元</span></p></div>
		<div class="box_QRcode  martop10">
		  <p class="fo-20 textIn20">支付宝支付</p>
		  <!--<p class="textAlignCenter red">二维码已过期，<a href="#" class="blue" onclick="refresh()">刷新</a>页面重新获取二维码</p>
		  <p class="textAlignCenter">距离二维码过期还剩<span class="red">40</span>秒，过期后请刷新页面重新获取二维码。</p>-->
		  <p class="textAlignCenter"><div id="code"></div></p>
		  <p class="textAlignCenter">请用<span class="orange">支付宝</span>扫一扫</p>
		  <p class="textAlignCenter">扫描二维码进行支付</p>
		  <!-- <p class="textIn20"><a href="#" class="orange" onclick="changeType()">〈 选用支付宝进行支付</a></p> -->
		</div>
	</div>
          
          <input type="text" value="${message.content}" id="qrcode_url"/>  

		  <script>   
				alert("${message.message}");
				var str="${message.message}";
				$("#code").qrcode({       
								  width: 182, //宽度 
								  height:182, //高度 
								  text: toUtf8(str)
								});
			    
		  </script>
      </c:if> 
	 <c:if test="${message.operateCode!=102167&&message.operateCode!=102169}">
	     <script>
		  //alert("${message.message}"+"");
		  alert(${message.operateCode})
		  if(${message.operateCode==710010}){
			  location.href="/content/notice/paySuccess.shtml" ;
		  }
		 </script>
      </c:if> 
  </c:when> 
  <c:otherwise>   
        <script>
		   alert("系统繁忙！");
	    </script>
  </c:otherwise> 
</c:choose>
 	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	
	
</body>
</html>
 
