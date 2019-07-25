<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html >
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}-确认订单buy</title>
<link rel="stylesheet" href="../../../theme/${theme}/css/mobile/jquery.spinner.css" />
<link rel="stylesheet" href="../../../theme/${theme}/css/mobile/orderConfirm.css" />
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css" />
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.spinner.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.slides.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/selectDeliveryAddress.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>

<script>
	wx.config({
		debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId: '${appId}', // 必填，公众号的唯一
			timestamp: '${timeStamp}', // 必填，生成签名的时间戳
			nonceStr: '${nonceStr}', // 必填，生成签名的随机串
			signature: '${signature}',// 必填，签名，见附录1
			jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone','openLocation','getLocation','scanQRCode','chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});	
</script>
</head>
<body>
<div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>确认订单</span><a class="list2" href="/"></a>
 </div>
 <form enctype="multipart/form-data" method="post">
 <div class="wrapper_1">
 <input id="orderId" value="${orderId}" type="hidden" name="orderId"/>
 <c:choose>
 <c:when test="${fn:length(addressBookList)>0}">
 <div class="address"> 
	  <ul> 
	  <c:forEach var="i" items="${addressBookList}" begin="0" end="0" varStatus="status">
		<li>
			<input name="addressBookId" value="${i.addressBookId}" type="radio" checked="checked" id="defaultRadio">
		    <div class="addInfo">
				<p style=""><c:if test="${i.currentStatus==100003}"><span class="default_icon">[默认]</span></c:if>${i.province} ${i.city} ${i.address}</p> 
				<p class="mt10">${i.contact} ${i.mobile}</p>
			</div>
		</li>
		<div class="moreAddress">>>更多收货地址</div>
	  </c:forEach>
	  </ul>
	  <ul class="moreAddCont">
	  <c:forEach var="i" items="${addressBookList}" begin="1" varStatus="status">
		<li style="">
			<input name="addressBookId" value="${i.addressBookId}" type="radio">
			<div class="addInfo">
		    <p style=""><c:if test="${i.currentStatus==100003}"><span class="default_icon">[默认]</span></c:if>${i.province} ${i.city} ${i.address}</p> 
			<p class="mt10">${i.contact} ${i.mobile}</p>
			</div>
		 </li>
	  </c:forEach>	
	  <a id="addLink" href="/addressBook/create.shtml">+新增地址</a>
	 </ul>	 
  </div>
</c:when>
 <c:otherwise>
   <a href="/addressBook/create.shtml" class="block">
   <div class="btn_add">
      请添加收货地址
	  <i class="icon_right"></i>
   </div>
   </a>
   <div class="line"></div>
 </c:otherwise>
</c:choose> 
	<div class="wid90">
		<ul class="box-xy  scxy" style="display: block;">
		<c:forEach items="${cart}" var="i" varStatus="status">
		<c:set value="${i.value}" var="str"/>
	    <li>
			<input type="hidden"  name="tid"  value="${str.transactionId}"/>
			<div class="ware_img"><img src="${str.itemDataMap.get('productSmallImage').dataValue}"> </div>
			<div class="box_right2">			
			<p class="center_miaoshu oneline">${str.name}</p>
			<p class="xinxi"><span class="center_chandi">产地：${str.itemDataMap.get('productOrigin').dataValue} </span> </p>
            <p class="guige"><span class="center_jiage">规格：
            </span><span class="center_danwei">  ${str.itemDataMap.get('goodsSpec').dataValue}</span></p>
            <p class="danjia_dj">单价：<span class="danjia_style">￥<span class="danjia">${str.price.money}</span></span></p>			
		   </div> 
		</li>
		</c:forEach>
	  </ul>
	</div>
	<div class="line"></div>
	<div class="wid90 martop20 pay">
       <div class="left">
	      支付方式
	  </div>
	</div>	
	 <c:forEach var="payType" items="${payTypeList}" varStatus="status">
	<div class="wid90 martop20 pay">
	  <div class="left">
			<input type="radio" value="${payType.payTypeId}" name="payTypeId" checked>
			<img src="/image/mobile/weixin.png" style="margin-right:5px;"/>${payType.name}
	  </div>
	</div>
	</c:forEach>
	<!--<c:forEach var="payType" items="${payTypeList}" varStatus="status" begin="1" end="1">
	<div class="wid90 martop20 pay">
	  <div class="left">
	      <input type="radio" value="${payType.payTypeId}" name="payTypeId" checked>
	      <img src="/image/mobile/zhifubao.png" style="margin-right:5px;"/>支付宝支付
	  </div>
   </div>
   </c:forEach>-->
   <div class="line martop20" ></div>	
   <ul class="order-status">
	    <li class="wid90">
			<div class="left orange">
				<input type="checkbox" id="useCoupon"  class="useCouponBtn"/>
			</div>
		    <div class="left">
				使用代金券<span class="useCouponBtn orange fo12">（+点击展开）</span>
			</div>				
		</li>		
	</ul>   
     <ul class="order-status useCoupon">
		 <div class="wid90">
				<div class="right  box_r martop10">
					<p><input type="text" class="yanzheng addCouponCode" placeholder="请输入代金券验证码"></input></p>					
				</div>
				<div class="right  box_r martop10">
				    <div class="left">
			           <p style="color:#A3A3A3;" class="martop10" >现有代金券总金额:<span class="orange">￥<span class="currentGiftMoney">
			           
			           </span></p>
			        </div>
					 <div class="right orange">
			         <p class="martop20 orange"> <a href="#" class="orangeButton1 fo12 addCoupon">确认添加</a></p>
			        </div>
				</div>
		</div>
	  </ul>
	  <div class="line hidden" ></div>
	  	<ul class="order-status">
	  		<li class="wid90">
				<div class="left orange">
					<input type="checkbox"  class="needInvoiceBtn" id="needInvoiceBtn"/>
				</div>
		        <div class="left">
					 开具发票<span class="needInvoiceBtn orange fo12 hidden">（+点击展开）</span>
				</div>
			</li>
		</ul>
		<ul class="order-status needInvoice">
			<div class="wid90">
				<div class="right  box_r">
					<p style="color:#A3A3A3;"><input type="text" class="invoiceTitle" id="invoiceTitle" placeholder="发票抬头：例如，北京盛世家和科技有限公司"></input></p>
				</div>
			</div>		
	  	</ul>
	  	<div class="line hidden"></div>     	
	   <ul class="order-status">
	    <li class="wid90">
		        <div class="left">
					商品金额总计
				</div>
				<div class="right orange">
					￥<span id="productMoney">${totalMoney}</span>
				</div>	
		</li>
		<div class="line "></div>
		 <li class="wid90">
		        <div class="left">
					运费总计
				</div>
				<div class="right orange">
					￥<span id="fee"><fmt:formatNumber value="${empty deliveryOrder.fee.money?0.0:deliveryOrder.fee.money}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span>
				</div>
		</li>
		<div class="line "></div>
		 <li class="wid90">
		        <div class="left">
					使用代金券抵扣
				</div>
				<div class="right orange useGiftMoney">
					￥<span id="couponMoney"><c:choose><c:when test="${!empty money.giftMoney}"><c:if test="${money.giftMoney<totalMoney}">${money.giftMoney}</c:if><c:if test="${totalMoney<=money.giftMoney}">${totalMoney}</c:if></c:when><c:otherwise>0.00</c:otherwise></c:choose></span>
				</div>
		</li>
	  </ul>
	  <div class="btn_group">
	    <a  class="btn_a left">实付款:<span class="orange">￥<span id="totalMoney"></span></span></a>
		<input class="btn_b right submitOrder" style="color:#fff;" type="submit" value="立即支付"/>
	  </div>
 </div>
 </form>
</body>
</html>