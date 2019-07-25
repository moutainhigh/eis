
    $(document).ready(function () {
		initPage();		
		$(".moreAddress").click(function(){			
			if($(".moreAddCont").css("display")=="block"){
				$(".moreAddCont").slideUp(500);
				$(this).text(">>更多收货地址");
			}else{
				$(".moreAddCont").slideDown(500);
				$(this).text("<<收起收货地址");
			}			
		})	
		$("input[id='useCoupon']").click(function(){
			$(this)[0].checked=true;
		})
		$(".useCouponBtn").click(function(){			
			if($(".useCoupon").css("display")=="block"){
				$(".useCoupon").slideUp(500);
				$(".useCoupon").next().hide();
				$("span.useCouponBtn").text("（+点击展开）");
			}else{
				$(".useCoupon").slideDown(500);
				$(".useCoupon").next().show();
				$("span.useCouponBtn").text("（-点击收回）");
			}			
		})		
		$(".needInvoiceBtn").click(function(){
			if($(".needInvoice").css("display")=="block"){
				$(".needInvoice").slideUp(500);
				$(".needInvoice").next().hide();
				$("span.needInvoiceBtn").text("（+点击展开）");
			}else{
				$(".needInvoice").slideDown(500);
				$(".needInvoice").next().show();
				$("span.needInvoiceBtn").text("（-点击收回）");
			}			
		})		
		$(".addCoupon").click(function(){
			var giftCardNumber=$(this).parent().parent().parent().siblings().find(".addCouponCode").val();
			if(giftCardNumber.length==0){
			    malert("请输入代金券编号",1500);     
				return false;
			}
			$.ajax({
				type: "POST",
				url: '/buy/setCoupon.json',
				data: {
					couponCode:"MONEY_COUPON",
					couponSerialNumber: giftCardNumber.replace(/\ +/g,"").substr(0,8),
					couponPassword:giftCardNumber.replace(/\ +/g,"").substr(8, 16),
					orderId:$("#orderId").val()
				},
				dataType: 'json',
				success: function (data) {               
					switch (data.message.operateCode) {
						case 102008:
							malert("充值成功，充值金额为"+data.coupon.giftMoney.giftMoney+"元",1500);
							var currentGiftMoney=parseFloat($(".currentGiftMoney").html())+parseFloat(data.coupon.giftMoney.giftMoney);
							$(".currentGiftMoney").html(currentGiftMoney);
							var productMoney=parseFloat($("#productMoney").html());
							if(currentGiftMoney<productMoney){
								$("#couponMoney").html(currentGiftMoney);
							}else{
								$("#couponMoney").html(productMoney);
							}
							setTotalMoney();
							break;
						case operateResultFailed:
							alert(data.message.message);
							$(".addCouponCode").val("");
							break;
						default:
							alert(data.message.message);
							$(".addCouponCode").val("");
					}
				},
				error: function (data) {
					alert("系统繁忙,请稍后再试");
					return false;
				}
			});
		});
		$("input[id='invoiceTitle']").blur(function(){
			if($("input[id='invoiceTitle']").val().length==0){
				return false;
			}
			$.ajax({
				type: "POST",
				url: '/buy/setInvoice.json',
				data: {
					title: $("input[id='invoiceTitle']").val(),
					orderId: $("input[id='orderId']").val()
				},
				dataType: 'json',
				success: function (data) {
					 alert(data.message.message);
				},
				error: function (data) {			
					alert("系统繁忙,请稍后再试");
					return false;
				}
			});
		})
		$("input[name='addressBookId']").click(function(){
		
				var openIDStr="";
				var param={};
				//var arr=[];
			var foods = document.getElementsByName("tid");
			for ( var i = 0; i < foods.length; i++) {				
				var str=foods[i].value+",";
				openIDStr+=str;
				//arr.push(foods[i].value);
			}
			openIDStr.substring(0,openIDStr.length-1);
			param["orderId"]=$("#orderId").val();
			param["addressBookId"]=$("input[name='addressBookId']:checked").val();
			param["tid"]=openIDStr;
			$.ajax({
				type: "POST",
				url: '/buy/countDeliveryFee.json',
				data:param,
				dataType: 'json',
				success: function (data) {  
				      var fee=parseFloat(data.deliveryOrder.fee.money);
					  $("#fee").html(fee.toFixed(2));
						setTotalMoney();       
				},
				error: function (data) {
					alert("系统繁忙,请稍后再试");
					return false;
				}
			});
		})		
		$("form").submit(function(e){			
			e.preventDefault();		    
			var iptRadio=$("input[name='addressBookId']:checked").val();
			if(!iptRadio)
			{
				malert("请添加或者选择地址","1000");
				return false;
			}
			else{		
			    var mLoading=new MLoading();
				$("form input[type=submit]").attr("disabled","disabled"); 
			    $("form input[type=submit]").css("backgroundColor","#ccc");
				var payType=$("input[name='checkit']:checked").val();
				$(this).ajaxSubmit({
					type : "POST",
					url :  '/buy/settleUp.json',
					async:false,
					dataType : 'json',
					success : function (data) {
						if(data.message.operateCode == 102168){
							mLoading.beRemoved();							
							wx.ready(function(){
							wx.chooseWXPay({
							timestamp: data.message.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
							nonceStr: data.message.messageId, // 支付签名随机串，不长于 32 位
							package: 'prepay_id='+data.message.message, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
							signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
							paySign: data.message.sign, // 支付签名
							success: function (res) {
								if(res.errMsg=='chooseWXPay:ok'){
									malert("支付成功！",1000);
									location.href="/order/index.shtml"; 
								}
							},
							fail:function (res) {
								if(res.errMsg=='chooseWXPay:fail'){
									$(".waitMsg").remove();
									malert("微信支付失败！","1000")
								}
							},
							cancel:function (res) {
								if(res.errMsg=='chooseWXPay:cancel'){
									 					
								}
							},
							});
							})
							wx.error(function(res){
							});				
						}
						else if(data.message.operateCode == 102169){
							mLoading.beRemoved();
							window.location.href=data.message.message;
						}
						else{
							 mLoading.beRemoved();
							 malert(data.message.message+"",1000);				
						}			
					}
				});
				return false;
			}		   
		  });  
    })
	function setTotalMoney(){
		var totalNum=parseFloat($("#productMoney").html())+parseFloat($("#fee").html())-parseFloat($("#couponMoney").html());
		totalNum=totalNum.toFixed(2);
		$("#totalMoney").html(totalNum);
	}
	function addWaitMsg(){
		var html='';
		html+='<div class="waitMsg">正在处理，请稍候…</div>';
		$("body").append(html);
	}
	function initPage(){
		setTotalMoney();
	}