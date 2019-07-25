<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html >
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title>以先</title>
<link href="../../../theme/${theme}/css/mobile/select_delivery_address.css" rel="stylesheet">
<script type="text/javascript" src="../../../theme/${theme}../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script> 
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
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
			appId: '${appId}', // 必填，公众号的唯一
			timestamp: '${timeStamp}', // 必填，生成签名的时间戳
			nonceStr: '${nonceStr}', // 必填，生成签名的随机串
			signature: '${signature}',// 必填，签名，见附录1
			jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone','openLocation','getLocation','scanQRCode','chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});		
	
</script>
<style>
	.orderImg{width:2rem;margin-top:3rem;margin-bottom:1.2rem;}
</style>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="cont">
			<div class="dfk_title">
				<dl style="text-align:center;line-height:2;">
					<!--img src="/style/mobile/images/kdd.png" class="orderImg"><br/-->
					<c:choose>
					<c:when test="${message.operateCode==102168}">
						<script>
							wx.ready(function(){
								wx.chooseWXPay({
								timestamp: '${timeStamp}', // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
								nonceStr: '${message.messageId}', // 支付签名随机串，不长于 32 位
								package: 'prepay_id='+'${message.message}', // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
								signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
								paySign: '${message.sign}', // 支付签名
								success: function (res) {
									// 支付成功后的回调函数
									if(res.errMsg=='chooseWXPay:ok'){$(".orderState").html("支付已成功").css("color","#18b501");$(".orderID").show();$(".orderName").show();$(".backLink").html('<a href="/order/index.shtml?page=1&rows=10"><span  style="display:inline-block;width:4rem;height:1rem;line-height:1rem;border:#ACADAF solid 1px;border-radius:0.2rem;color:#ACADAF;font-size:0.43rem;">返回</span></a>')}
								},
								fail:function (res) {
									// 支付失败后的回调函数
									if(res.errMsg=='chooseWXPay:fail'){alert("微信支付失败！");$(".orderState").html("微信支付失败！");$(".backLink").html('<a href="/order/index.shtml?page=1&rows=10"><span  style="display:inline-block;width:4rem;height:1rem;line-height:1rem;border:#ACADAF solid 1px;border-radius:0.2rem;color:#ACADAF;font-size:0.43rem;">返回</span></a>')}
								},
								cancel:function (res) {
									// 支付取消后的回调函数
									//alert(JSON.stringify(res));
									//alert("2timestamp:" + data.message.timestamp + "\n" + "nonceStr:" + data.message.messageId + "\npackage:" + 'prepay_id='+data.message.message + "\npaySign:" + data.message.sign);
									if(res.errMsg=='chooseWXPay:cancel'){
										 $(".orderState").html("您已取消支付操作！");$(".backLink").html('<a href="/order/index.shtml?page=1&rows=10"><span  style="display:inline-block;width:4rem;height:1rem;line-height:1rem;border:#ACADAF solid 1px;border-radius:0.2rem;color:#ACADAF;font-size:0.43rem;">返回</span></a>')
										}
								},
								});
							})
							wx.error(function(res){
							//alert("支付失败!")
							// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
							});
							/* function goBack(){
								location.href=history.back();
							} */
						</script>
						<span  class="orderState" style="font-size:0.6rem;margin-top:0.8rem;font-weight:bold;">等待支付，请稍候！</span><br/>
						<span class="backLink" style="margin-top:0.8rem;display:inline-block;"></span>
					</c:when>
					<c:otherwise>
						<span style="font-size:0.6rem;font-weight:bold;">${message.message}</span><br/>						
					</c:otherwise>
					</c:choose>
				</dl>
			</div>
			
		</div>
		
	</div>
</div>
</body>
</html>