<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/jquery.spinner.css">
<!--link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/mycart.css"-->
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/settlement.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/iconfont.css">
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
<!--script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.spinner.js"></script-->
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/gouwuche.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../theme/${theme}/js/respond.src.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script>
	if(Cookie.getCookie("eis_username")==null){
        location.href = "/content/user/login.shtml";
    }
</script>
</head>
<body>
 	
 	 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>购物车</span><a class="list2" href="/"></a>
 </div>
   <div id="rongQi">
   	<c:if test="${empty itemMap}">
		<p  style="text-align:center;margin-top: 50px;">购物车没有商品哦~ <br><a href="/" style="color: #fff; margin-top: 23px; display: inline-block; padding: 6px 12px;border-radius: 5px; background-color: rgba(127, 164, 24, 0.7);"><i class="icon iconfont icon-arw-toLeft"></i>去购物</a></p>
   </c:if>
<c:forEach var="cartStr" items="${itemMap}" varStatus="status">
 <c:set var="str" value="${cartStr.value}"></c:set>   
    
	<div id="gouwuche_center">
		<div id="center_left">
		<div class="checkBox">
  		<input type="checkbox" id="checkboxInput${status.count}"   name="food" class="shifouxuanzhong" value="${str.transactionId}"/>
	  	<label for="checkboxInput${status.count}" id="checkBoxLabel"></label>
  	   </div>
		</div>
		<div id="center_center"> <a href="${str.itemDataMap.get('refUrl').dataValue}"><img src="${str.itemDataMap.get('productSmallImage').dataValue}" class="shiwu_img"></a>
			<div style="width:100%; height:24px;">            
				<div class="spinner" ><a class="decrease" onClick="decrease(this,'${str.transactionId}',event)">-</a><input type="text" class="spinner value passive" value="${str.count}" id="productcount" maxlength="2" oninput="changevalue(this.value,'${str.transactionId}',event)"><a class="increase" onClick="increase(this,'${str.transactionId}',event)">+</a>
				</div>
			</div>
		</div>
		<a href="${str.itemDataMap.get('refUrl').dataValue}">
		<div id="center_right">
			<p class="center_miaoshu oneline">${str.name}</p>
			<p class="xinxi"><span class="center_chandi">产地：${str.itemDataMap.get('productOrigin').dataValue} </span> </p>
            <p class="guige"><span class="center_jiage">规格：
            </span><span class="center_danwei">  ${str.itemDataMap.get('goodsSpec').dataValue}</span></p>
            <p class="danjia_dj">单价：<span class="danjia_style">￥<span class="danjia">${str.price.money}</span></span></p>
			<div class="xiaoji">
			   <span class="center_xiaoji">小计：<span style="color:#E04343">￥</span><span style="color:#E04343" class="sumtotal"><fmt:formatNumber value="${str.requestMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></span>
			   <span class="cart_del"><span id="edit" onclick="edit(this,event)">编辑</span><span class="cart-del-box" id="cart-del-box"><span onclick="deleteCart('${str.transactionId}',event,this)">删除</span> | <span id="complete" onclick="complete(this,event)">完成</span></span></span>
			</div>
			
		</div>
		</a>
	</div>
	
	<div style="width:100%; height:1px;background-color:#E3E3E3;"></div>
 </c:forEach>
	
	
</div>
<div id="gouwuche_bottom">
		<div class="zuoce">
			<div class="checkBox" id="quanxuan_checkBox">         
				<input type="checkbox"  id="quanxuan"  class="quanxuan"  style="display:none;" id="all" />
				<label for="quanxuan" id="checkBoxLabel2"></label>
			</div>
			<div class="shp-cart-info">
			<p><span>合计:</span><span class="orange">￥</span><span id="zong1" class="orange">0.00</span></p>
            <p style="font-size: 0.8em;">(不包含运费)</p>
            </div>
		</div>
		<div class="shanchu1">
			<div class="shanchu2 qujiesuan">
				<p id="balance" style="background-color: rgb(210, 52, 52);">去结算(0)</p>
			</div>
            <div class="delgwc" style="display:none">
				<p id="delgwc">删除</p>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>