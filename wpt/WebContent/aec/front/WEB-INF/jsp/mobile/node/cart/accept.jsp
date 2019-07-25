<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html >
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title></title>
   <link href="/style/mobile/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  
   <link href="/style/mobile/select_delivery_address.css" rel="stylesheet">
  <link href="/style/mobile/head.css" rel="stylesheet" type="text/css"/>

<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.form.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/style/mobile/bootstrap/js/bootstrap.min.js"></script>
<!--[if lt IE 9]>
<script src="/js/respond.min.js"></script>
<![endif]--> 	
<script>

		//获取设备渲染像素宽度
		var iWidth = document.documentElement.clientWidth;
		document.getElementsByTagName('html')[0].style.fontSize = iWidth/10 + 'px';	
</script>
<script>
 wx.config({
			debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId: '${appid}', // 必填，公众号的唯一
			timestamp: '${timestamp}', // 必填，生成签名的时间戳
			nonceStr: '${nonceStr}', // 必填，生成签名的随机串
			signature: '${signature}',// 必填，签名，见附录1
			jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone','openLocation','getLocation','scanQRCode','chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});		
		

$(document).ready(function(){
  $("form").submit(function(e){
    e.preventDefault();
    $(this).ajaxSubmit({
        type : "POST",
        url :  '/pointExchange/cart/settleUp.json',
        async:false,
        dataType : 'json',

        success : function (data) {
        	if(data.message.operateCode == 102124){
				wx.ready(function(){
				wx.chooseWXPay({
				timestamp: data.message.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
				nonceStr: data.message.messageId, // 支付签名随机串，不长于 32 位
				package: 'prepay_id='+data.message.message, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
				signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
				paySign: data.message.sign, // 支付签名
				success: function (res) {
					// 支付成功后的回调函数
					//alert(JSON.stringify(res));					
					//alert("1timestamp:" + data.message.timestamp + "\n" + "nonceStr:" + data.message.messageId + "\npackage:" + 'prepay_id='+data.message.message + "\npaySign:" + data.message.sign);
					if(res.errMsg=='chooseWXPay:ok'){alert("微信支付成功！")}
				},
				fail:function (res) {
					// 支付失败后的回调函数
					//alert(JSON.stringify(res));
					//alert("2timestamp:" + data.message.timestamp + "\n" + "nonceStr:" + data.message.messageId + "\npackage:" + 'prepay_id='+data.message.message + "\npaySign:" + data.message.sign);
					if(res.errMsg=='chooseWXPay:fail'){alert("微信支付失败！")}
				},
				cancel:function (res) {
					// 支付取消后的回调函数
					//alert(JSON.stringify(res));
					//alert("2timestamp:" + data.message.timestamp + "\n" + "nonceStr:" + data.message.messageId + "\npackage:" + 'prepay_id='+data.message.message + "\npaySign:" + data.message.sign);
					if(res.errMsg=='chooseWXPay:cancel'){alert("用户取消操作！")}
				},
				});
				})
				wx.error(function(res){
				alert("支付失败!")
				// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

				});
				
			}
			else{
				alert(data.message.message);				
			}
			
        }
	}
	);
  });  
});
		$(function(){
			if(${totalMoney}!=0&&${totalCoin}==0&&${totalPoint}==0){$(".rmb").css("display","block");$(".ubao").css("display","none");$(".ubi").css("display","none");$(".gxz").css("lineHeight","90px");$(".total .red").css("lineHeight","90px");$(".gxz .red").css("lineHeight","90px");}	
			if(${totalMoney}==0&&${totalCoin}!=0&&${totalPoint}==0){$(".rmb").css("display","none");$(".ubao").css("display","block");$(".ubi").css("display","none");$(".gxz").css("lineHeight","90px");$(".total .red").css("lineHeight","90px");$(".gxz .red").css("lineHeight","90px");}
			if(${totalMoney}==0&&${totalCoin}==0&&${totalPoint}!=0){$(".rmb").css("display","none");$(".ubao").css("display","none");$(".ubi").css("display","block");$(".gxz").css("lineHeight","90px");$(".total .red").css("lineHeight","90px");$(".gxz .red").css("lineHeight","90px");}
			if(${totalMoney}!=0&&${totalCoin}!=0&&${totalPoint}==0){$(".rmb").css("display","block");$(".ubao").css("display","block");$(".ubi").css("display","none");$(".gxz").css("height","60px");$(".gxz").css("lineHeight","60px");$(".js").css("height","60px");$(".js").css("lineHeight","60px");$(".totalsub").css("lineHeight","60px");$(".trans").css("lineHeight","60px");$("dl").css("marginBottom","60px");}
			if(${totalMoney}!=0&&${totalCoin}!=0&&${totalPoint}!=0){$(".rmb").css("display","block");$(".ubao").css("display","block");$(".ubi").css("display","block");}
		})
		
</script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="head" style="z-index:999;">
			<span class="arrow_left" style="position:absolute;left:0px;    padding-top: 0.20rem;"><a href="javascript:history.go(-1);"><img src="/style/mobile/images/arrow_left.png" style="margin-bottom:0.45rem;"></a></span>
			<span class="text_middle"><span>付款</span></span>
		</div>
		<div class="cont">
			<div class="dfk_title">
						<dl >
							<form action="/buy/settleUp.shtml">

						<c:set var="totalCount" value="0" />												
						<c:forEach var="it" items="${cart}">
							<c:set var="totalCount" value="${totalCount + it.value.count}"/>
							<input type="hidden" name="orderId" value="${it.value.transactionId}" />
							<dd class="cont_fk">
								<div class="left" id="left">
									<img src="${it.value.itemDataMap.get('productSmallImage').dataValue}">
								</div>
								<div class="right">
										<span class="product">${it.value.name}</span>	
								</div>							
							</dd>
						</c:forEach>
							
							
							
							<div class="gxz">
								<span class="gray">共选择${totalCount}件商品</span>　应付：<span class="red" style="line-height:30px;display: block; width: 100px; float: right;"> <span class="rmb">${totalMoney}RMB</span><span id="br1" style="display:none;"><br/></span><span class="ubao">${totalCoin}U宝</span><span id="br2" style="display:none;"><br/></span><span class="ubi">${totalPoint}U币</span></span>
							</div>
							<dd class="js">	
								<div class="container">
								<div class="row">
								<input type="submit" value="结算" class="pay_right"></input>
													
								<div class="pay_middle" style="overflow:hidden;">
									<span class="total" style="overflow:hidden;display:block;float:left;"><span class="red"style="line-height:30px; width: 100px; display:block;float:right"><span class="rmb">${totalMoney}RMB</span><span class="ubao">${totalCoin}U宝</span><span class="ubi">${totalPoint}U币</span></span><span class="totalsub"style="display:block;float:right;line-height:90px;">合计：</span></span>
									<span class="trans" style="display:block;float:left;line-height:90px;">运费:　0</span>
								</div>
								</div>
								</div>
							</dd>
							</form>
						</dl>
			</div>
			
		</div>
		
	</div>
</div>
</body>
</html>