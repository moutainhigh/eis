<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml" style="background-color: #e0fdf4;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title></title>
   <link href="/style/mobile/bootstrap/css/bootstrap.min.css" rel="stylesheet">
   <link href="/style/mobile/my_order.css" rel="stylesheet">   
   <link href="/style/mobile/head.css" rel="stylesheet" type="text/css"/>
   <script src="/js/jquery.min.js"></script>
   <script src="/style/mobile/bootstrap/js/bootstrap.min.js"></script>   
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<!--[if lt IE 9]>
<script src="/js/respond.min.js"></script>
<![endif]--> 	

<script type="text/javascript">
		//获取设备渲染像素宽度
		var iWidth = document.documentElement.clientWidth;
		document.getElementsByTagName('html')[0].style.fontSize = iWidth/10 + 'px';	
		$(window).resize(function(){
			var iWidth = document.documentElement.clientWidth;
			document.getElementsByTagName('html')[0].style.fontSize = iWidth/10+'px';
		})
</script>
<script>
$(function(){
	var h1=$(".container").width();
	$(".pay2").css("width",h1+30+'px');
})
 wx.config({
			debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId: '${appid}', // 必填，公众号的唯一
			timestamp: '${timestamp}', // 必填，生成签名的时间戳
			nonceStr: '${nonceStr}', // 必填，生成签名的随机串
			signature: '${signature}',// 必填，签名，见附录1
			jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone','openLocation','getLocation','scanQRCode','chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
$(function(){
	var price=$(".price").html();
	price=parseInt(price);
	$(".price").html(price);

    var priceSu=$(".priceSum").val();
    priceSu=parseInt(priceSu);
    $(".priceSum").val(priceSu);
    
    
    var total_input=$(".total_input").val();
    total_input=parseFloat(total_input);
    $(".total_input").val(total_input);
})
$(function(){
	var jump=$(".jump").val();
	//alert(jump);
	if(jump!=undefined){
		$.ajax({
			type : "GET",
			url :  jump,
			async:false,
			dataType : 'json',
			success : function (data) {
				//alert(JSON.stringify(data));
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
							if(res.errMsg=='chooseWXPay:ok'){$(".orderState").html("<img src='/style/mobile/images/sales/success.png' style='width:0.4rem;'>支付已成功").css("color","#18b501");$(".orderID").show();$(".orderName").show();$(".backLink").html("<a href='/content/notice/birandecuxiao.shtml'>返回内购页</a>")}
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
			},
		})
	}
})

	</script>
	<script>
		$(function(){
			$(".dfk_title ul li").click(function(){
				$(this).parent().children("li").removeClass("active");
				$(this).addClass("active");			
			})	
			
			$(".left_ph1").click(function(){
				$(this).toggleClass("xx");
				$(this).toggleClass("selectedlist");			
			})
			
			$(".pay_img").click(function(){
				$(this).toggleClass("xx");
				$(this).toggleClass("selectedlist");
				if($(this).hasClass("xx")){
					$(".left_ph1").removeClass("selectedlist");		
					$(".left_ph1").addClass("xx");		
				}else{
					$(".left_ph1").addClass("selectedlist");		
					$(".left_ph1").removeClass("xx");						
					
				}
			})
			
		})
	
	
	function plus(transactionId){
			var oldCount = parseInt($('#item_' + transactionId + ' #name').val());
			oldCount++;
			
			$.ajax({
		        type : "POST",
		        url :  '/cart/update.json',
		        async:true,
		        data:{
		        	count: oldCount,
		        	transactionId:transactionId
		        },
		        dataType : 'json',

		        success : function (data) {
		        	if(data.item != null){
		        		var count = data.item.count;
		        	//	$('#item_' + transactionId + ' #name').value=parseInt(count);
		        		$('#item_' + transactionId + ' #name').val(parseInt(count));
		        		var allOrder = $("dd[id^='item']");
		        		var totalMoney1 = 0;
						var totalMoney2 = 0;
						var totalMoney3 = 0;
						var amount=0;
		        		if(allOrder != null && allOrder.length > 0){
		        			for(var i = 0; i < allOrder.length; i++){
		        				var orderElement = $(allOrder[i]);
								var a=new Array(allOrder.length);
								if($(allOrder[i]).find(".left_ph1").hasClass("selectedlist")){a[i]=1;}else{a[i]=0;transactionId=null;}
								//alert(a);
				        		totalMoney1 += (parseFloat(orderElement.find('#dj1').val()) * orderElement.find('#name').val()*a[i]);
								totalMoney2 += (parseFloat(orderElement.find('#dj2').val()) * orderElement.find('#name').val()*a[i]);
								totalMoney3 += (parseFloat(orderElement.find('#dj3').val()) * orderElement.find('#name').val()*a[i]);
								amount+=(parseInt(orderElement.find('#name').val())*a[i]);
        					}
		        		}
						document.getElementById("total2_1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("total2_2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("total2_3").value = parseFloat(totalMoney3).toFixed(2);
		        		document.getElementById("price1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("price2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("price3").value = parseFloat(totalMoney3).toFixed(2);
						document.getElementById("amount").value = parseInt(amount);	
		        	} else {
		        		alert(data.message.operateCode + "\n" + data.message.message);
		        	}
				//	window.location.reload(); 
		        }
			}
			);
		}
		
		
		function selectlist(){
			var allOrder = $("dd[id^='item']");
			var oldCount = parseInt($(allOrder[0]).val());
			var totalMoney1 = 0;
			var totalMoney2 = 0;
			var totalMoney3 = 0;
			var amount=0;
		    if(allOrder != null && allOrder.length > 0){
		        for(var i = 0; i < allOrder.length; i++){
		        	var orderElement = $(allOrder[i]);
					var a=new Array(allOrder.length);
					if($(allOrder[i]).find(".left_ph1").hasClass("selectedlist")){a[i]=1;}else{a[i]=0;}
					totalMoney1 += (parseFloat(orderElement.find('#dj1').val()) * orderElement.find('#name').val()*a[i]);
					totalMoney2 += (parseFloat(orderElement.find('#dj2').val()) * orderElement.find('#name').val()*a[i]);
				    totalMoney3 += (parseFloat(orderElement.find('#dj3').val()) * orderElement.find('#name').val()*a[i]);
					amount+=(parseInt(orderElement.find('#name').val())*a[i]);
        		}
		    }
						document.getElementById("total2_1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("total2_2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("total2_3").value = parseFloat(totalMoney3).toFixed(2);
		        		document.getElementById("price1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("price2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("price3").value = parseFloat(totalMoney3).toFixed(2);
						document.getElementById("amount").value = parseInt(amount);		
		}
		
		function minus(transactionId){
			var oldCount = parseInt($('#item_' + transactionId + ' #name').val());
			if (oldCount>=1){
				oldCount--;
			}
			else{
				oldCount=0;
			}
			var reloadPage = false;
			if(oldCount == 0){
				if(!confirm("是否删除该商品？")){
					return;
				}
				reloadPage = true;
			}
			$.ajax({
		        type : "POST",
		        url :  '/cart/update.json',
		        async:true,
		        data:{
		        	count: oldCount,
		        	transactionId:transactionId
		        },
		        dataType : 'json',

		        success : function (data) {
		        	if(data.item != null){
		        		var count = data.item.count;
		        	//	$('#item_' + transactionId + ' #name').value=parseInt(count);
		        		$('#item_' + transactionId + ' #name').val(parseInt(count));
		        		var allOrder = $("dd[id^='item']");
		        		var totalMoney1 = 0;
						var totalMoney2 = 0;
						var totalMoney3 = 0;
						var amount=0;
		        		if(allOrder != null && allOrder.length > 0){
		        			for(var i = 0; i < allOrder.length; i++){
		        				var orderElement = $(allOrder[i]);
				        		//totalMoney += (orderElement.find('#dj').val() * count);
								//totalMoney += (parseInt(orderElement.find('#dj').val()) * orderElement.find('#name').val());
								//amount+=parseInt(orderElement.find('#name').val());
								var a=new Array(allOrder.length);
								if($(allOrder[i]).find(".left_ph1").hasClass("selectedlist")){a[i]=1;}else{a[i]=0;transactionId[i]=null;}
								//alert(a);
				        		totalMoney1 += (parseFloat(orderElement.find('#dj1').val()) * orderElement.find('#name').val()*a[i]);
								totalMoney2 += (parseFloat(orderElement.find('#dj2').val()) * orderElement.find('#name').val()*a[i]);
				        		totalMoney3 += (parseFloat(orderElement.find('#dj3').val()) * orderElement.find('#name').val()*a[i]);					
								amount+=(parseInt(orderElement.find('#name').val())*a[i]);
        					}
		        		}
					
		        		document.getElementById("total2_1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("total2_2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("total2_3").value = parseFloat(totalMoney3).toFixed(2);
		        		document.getElementById("price1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("price2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("price3").value = parseFloat(totalMoney3).toFixed(2);
						document.getElementById("amount").value = parseInt(amount);
		        	} else {
		        		alert(data.message.operateCode + "\n" + data.message.message);
		        	}
		        	if(reloadPage){
						window.location.reload(); 
		        	}
		        }
			}
			);
		}

function check(){ 
		
		var allOrder = $("dd[id^='item']");
		var a=new Array(allOrder.length);
		for(var i = 0; i < allOrder.length; i++){
		if($(allOrder[i]).find(".left_ph1").hasClass("selectedlist")){
			$(allOrder[i]).find(".orderId").val($(allOrder[i]).find(".orderId").val());
			
		}
		else{ $(allOrder[i]).find(".orderId").val(null)}
		}
		
	}
		
$(function(){
	
	var allOrder = $("dd[id^='item']");
	
	for(var i = 0; i < allOrder.length; i++){
		var orderElement = $(allOrder[i]);
		if(parseFloat(orderElement.find('#dj1').val())!=0&&parseFloat(orderElement.find('#dj2').val())==0&&parseFloat(orderElement.find('#dj3').val())==0){
			orderElement.find('#red1').css("display","block");
			orderElement.find('#plus1').css("display","none");
			
		}
		if(parseFloat(orderElement.find('#dj2').val())!=0&&parseFloat(orderElement.find('#dj1').val())==0&&parseFloat(orderElement.find('#dj3').val())==0){
			orderElement.find('#red2').css("display","block");
			orderElement.find('#plus1').css("display","none");	
						
		}
		if(parseFloat(orderElement.find('#dj3').val())!=0&&parseFloat(orderElement.find('#dj1').val())==0&&parseFloat(orderElement.find('#dj2').val())==0){
			orderElement.find('#red3').css("display","block");
			orderElement.find('#plus1').css("display","none");	
			
		}		
		if(parseFloat(orderElement.find('#dj1').val())!=0&&parseFloat(orderElement.find('#dj2').val())!=0&&parseFloat(orderElement.find('#dj3').val())==0){
			orderElement.find('#red1').css("display","block");
			orderElement.find('#plus1').css("display","inline-block");
			orderElement.find('#red2').css("display","inline-block");
			
		}
		
	}
	
})	

$(function(){
	
			var allOrder = $("dd[id^='item']");
			var oldCount = parseInt($(allOrder[0]).val());
			var totalMoney1 = 0;
			var totalMoney2 = 0;
			var totalMoney3 = 0;
			var amount=0;
		    if(allOrder != null && allOrder.length > 0){
		        for(var i = 0; i < allOrder.length; i++){
		        	var orderElement = $(allOrder[i]);
					var a=new Array(allOrder.length);
					if($(allOrder[i]).find(".left_ph1").hasClass("selectedlist")){a[i]=1;}else{a[i]=0;}
					totalMoney1 += (parseFloat(orderElement.find('#dj1').val()) * orderElement.find('#name').val()*a[i]);
					totalMoney2 += (parseFloat(orderElement.find('#dj2').val()) * orderElement.find('#name').val()*a[i]);
				    totalMoney3 += (parseFloat(orderElement.find('#dj3').val()) * orderElement.find('#name').val()*a[i]);
					amount+=(parseInt(orderElement.find('#name').val())*a[i]);
        		}
		    }
						document.getElementById("total2_1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("total2_2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("total2_3").value = parseFloat(totalMoney3).toFixed(2);
		        		document.getElementById("price1").value = parseFloat(totalMoney1).toFixed(2);
						document.getElementById("price2").value = parseFloat(totalMoney2).toFixed(2);
						document.getElementById("price3").value = parseFloat(totalMoney3).toFixed(2);
						document.getElementById("amount").value = parseInt(amount);		
})	
</script>
<style>
	.orderImg{width:3rem;margin-top:3rem;}
	body,.dfk_title{background-color: #e0fdf4;}
	.labelMoney{ float: right;position: relative;right: 0.88rem;}
	.labelMoney .price{text-decoration: line-through;color: #807e7e;position:relative!important;}
	.body_row3 {width: 100%;font-size: 0.45rem;color: #eb4f38;display: inline-block;float: right;
}
</style>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="head" style="z-index:999;">
			<span class="arrow_left" style="position:absolute;left:0px;"><a href="javascript:history.go(-1);"><img src="/style/mobile/images/arrow_left.png" style="margin-bottom:0.45rem;"></a></span>
			<span class="text_middle"><span>我的订单</span></span>
		</div>
		<div class="cont" style="padding-top:1.254rem;">
			<div class="dfk_title">				
				<c:choose>
					<c:when test="${cart != null}">
					<form action="/buy/delivery.shtml" onsubmit="return check()">
					<dl >
					<c:set var="totalCount" value="0" />
					<c:set var="totalMoney" value="0" />
					
					<c:forEach var="it" items="${cart}" begin="0" end="9">
						<c:set var="totalMoney" value="${totalMoney + (it.value.requestMoney + it.value.frozenMoney + it.value.successMoney + it.value.inMoney) * it.value.count}"/>
						<c:set var="totalCount" value="${totalCount + it.value.count}"/>
						<dd class="cont_dfk" id="item_${it.value.transactionId}">
							<div class="left">
								<a href="javascript:selectlist()"><span class="left_ph1 normal1 selectedlist" ><!--<img src="/style/mobile/images/xx.png">--></span></a>
								<span class="left_ph2"><a href="javascript:void()"><img src="${it.value.itemDataMap.get('productSmallImage').dataValue }"></a></span>
							</div>
							<div class="right" style="position: relative;">
								<input type="hidden" name="orderId" class="orderId" value="${it.value.transactionId }"/>
								<div class="right_title">${it.value.name }</div>								
								<div class="body_row2">
									<div class="body_rLeft" style="display: inline-block; border-radius: 2px;">
										<a href="javascript:minus('${it.value.transactionId}')" id="minus" style="position: relative; left: 8px;">-</a>
										<input type="text" value="${it.value.count}" id="name">
										<a href="javascript:plus('${it.value.transactionId}')" id="plus">+</a>
									</div>
									<span id="plus1" class="red">+</span> 
									<span class="red " id="red2"style="display:none;">
										U宝 <input type="text" value="${it.value.frozenMoney}" id="dj2" readonly="readonly">　
									</span>  
									<span class="red" id="red3"style="display:none;" >
										U币 <input type="text" value="${it.value.successMoney}" id="dj3" readonly="readonly">　
									</span>　
									<div  class="labelMoney">
										￥<span class="price">${it.value.labelMoney }</span>
									</div>																		
								</div>
								<div class="body_row3">
									<span class="red " id="red1"style="display:none;" >　
										￥<input class="priceSum" type="text" value="${it.value.requestMoney}" id="dj1" readonly="readonly">　
									</span> 
								</div>
							</div>							
						</dd>	
						
					</c:forEach>
						<!--<dd class="choose" style="border-bottom:60px solid #fff;">
							<span class="gray">已选择
								<input type="text" value="${totalCount}" id="amount" readonly="readonly"/>
								件商品
							</span>
							　应付：<span class="red" id="two" style="display:block;line-height:20px;float:right;">
								<span class="rmb">RMB
									<input type="text" value="${totalMoney}" id="price1" readonly="readonly"style="border:none;width: 50px;width: 80px; text-align: center;height: 95%;"/>
									<br/>
						</dd>-->
						<dd class="pay2">														
							<a href="javascript:selectlist()"><span class="pay_img selectedlist"></span></a>
							<span class="pay_left">全选</span>										
							<!--<div type="submit" value="结算" class="pay_right" id="submit">结算(<span id="amount" readonly="readonly" >${totalCount}</span>)</div>-->
							<input type="submit" value="结算(${totalCount})" class="pay_right" id="submit">
							<div class="pay_middle">
								<span class="trans">运费：包邮</span></br>
								<span class="total red">合计：<span class="rmb">
									<!--<input  type="text" value="${totalMoney}" id="total2_1" class="total_input" readonly="readonly"> 元</span>　-->
									<fmt:formatNumber  type="number" value="${totalMoney}"  pattern="0" maxFractionDigits="0"/> 　								
							</div>								
						</dd>					
					</dl>	
					</form>
					</c:when>
					<c:otherwise>
						<dl style="text-align:center;line-height:2;">
						<img src="/style/mobile/images/kdd.png" class="orderImg"><br/>
						<c:if test="${jump != null}">
							<span  class="orderState" style="margin-top:0.5rem;">等待支付，请稍等！</span><br/>
							<span style="display:none;" class="orderID">订单号：${item.transactionId}</span><br/>
							<span style="display:none;" class="orderName">商品名：${item.name}</span><br/>
							<span class="backLink" style="display:inline-block;margin-top:0.3rem;"></span>
							<input class="jump" type="hidden" value='${fn:replace(jump,".shtml",".json")}'/>
						</c:if>
						</dl>	
					</c:otherwise>
				</c:choose>											
			</div>			
		</div>
	</div>
</div>
</body>
</html>