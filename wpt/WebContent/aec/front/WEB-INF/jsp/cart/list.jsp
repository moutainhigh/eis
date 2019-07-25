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
<link rel="stylesheet" type="text/css" href="../css/jquery.spinner.css">
<link rel="stylesheet" type="text/css" href="../css/mycart.css">
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script  type="text/javascript" src="../js/jquery.spinner.js"></script>
<!-- <script  type="text/javascript" src="../js/gouwuche.min.js"></script> -->
<script  type="text/javascript" src="../js/common.js"></script>
<script  type="text/javascript" src="../js/gouwuche.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
if(Cookie.getCookie("eis_username")==null){
        location.href = "/content/user/login.shtml";
    }
</script>
</head>
<body>
 	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50 minH555">
     
      <div class="cartNum po_re">
	      <img src="../image/cart_big.png"/>
	      <span class="po_ab num">0</span>我的购物车
	  </div>
	  <ul class="cartlist" id="rongQi">
	     <c:forEach var="cartStr" items="${itemMap}" varStatus="status">
            <c:set var="str" value="${cartStr.value}"></c:set> 
	     		 <li>
		   <img src="${str.itemDataMap.get('productSmallImage').dataValue}">
		   <p class="fo-14">${str.name}</p>
		   <p class="fo-14">￥${str.price.money}&nbsp;&nbsp;&nbsp;&nbsp;规格：${str.itemDataMap.get('goodsSpec').dataValue}</p>
		  <div class="box_container martop10 divBottom">
		   <div class="spinner" ><button class="decrease" onclick="decrease(this,'${str.transactionId}')">-</button><input type="text" class="spinner value passive" value="${str.count}" id="productcount" maxlength="2" onblur="changevalue(this.value,'${str.transactionId}')"  ><button class="increase" onClick="increase(this,'${str.transactionId}')">+</button>
            </div>
			<div class="box_container_right orange fo-12 total subtotal">小计：￥<span class="sumtotal"><fmt:formatNumber value="${str.requestMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></div>
		  </div>
		  <div class="checkBox">
			<div class="checkboxFive">
				<input type="checkbox" id="checkboxFiveInput${str.transactionId}" name="food"  value="${str.transactionId}"/>
				<label for="checkboxFiveInput${str.transactionId}"></label>
			</div>
		  </div>
		
		 </li>
		  </c:forEach>
	  </ul>
	  	<div id="gouwuche_bottom">
	     <div class="box_left_cart">
		 <div class="checkBox" id="quanxuan_checkBox">
		 <input type="checkbox"  id="quanxuan"  class="quanxuan" />
	  	 <label for="quanxuan" id="checkBoxLabel2"></label>
		</div>
		<p style="height:7px; margin-left:55px;">
		<a href="#" class="select_all">全选</a>
		<a href="#" id="delgwc" class="btn_delete">删除</a></p>
		
	    </div>
	     <div class="box_right_cart">
		 
		
		 <span class="box_container_left" style="margin-right:20px;">共选商品<span class="orange num">0</span>件</span>
		 <span>总计(不含运费):￥<span style="color:#ff6400" id="zong1">0</span></span>
		 <a href="#" class="btnSettle box_container_right marleft10" id="balance">结算</a>
		
		
	    </div>
    </div>



   </div>
   <%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>