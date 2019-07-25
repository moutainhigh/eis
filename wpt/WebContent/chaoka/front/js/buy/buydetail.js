//消费购买详细页
//ajax多次调用函数
var tryTime = 1;
function reajax(orderId) {
	console.log("查询订单:" + orderId);
	$.ajax({
		type:"GET",
		url:userHost + '/order/get.json?cartId=' + orderId,

		dataType:'json',

		success:function (data) {
			console.log("查询订单:" + orderId + ",结果:" + JSON.stringify(data));
			if (data.order != null) {
				var status = data.order.currentStatus;
				if(status == 710010){
					var cardContent = data.order.data.cardData;
					/*if(data.itemList != null){
						for(var j = 0; j < data.itemList.length; j++){
							console.log("处理卡密:" + JSON.stringify(data.itemList[j]));
							if(data.itemList[j].content != null){
								cardContent += data.itemList[j].content;
								cardContent += "\n<br>";
							}
						}
					}*/
					jAlert('交易成功!<br>以下是您购买的商品<br>您也可以前往我的订单查看<br><br>' + cardContent, '提示信息', function (r) {

						hideloading('buyForm');
						tryTime = 1;

					});
				}
				//jAlert('交易成功', '提示信息');


				

				return false;

			} else {
				if (tryTime > 8) {

					jAlert('系统正在为您处理中,请稍后查询!', '提示信息', function (r) {

						hideloading('buyForm');

						tryTime = 1;

					});
					return false;

				}

				window.setTimeout(function () {
					reajax(orderId);
				}, tryTime * 1000);

				tryTime = ++tryTime;

			}

		},
		error:function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

}

function showSaleInput(){
	$("#saleInputLayer").css("display","block");
}

function productBuy(productCode) {


	$.ajax({
		type:"GET",
		url:userHost + '/user.json',
		dataType:'json',
		async:false,
		success:function (data) {

			if (data.frontUser == "" || data.frontUser == null) {

				Boxy.load(userHost + "/content/user/loginMini.shtml", //确认提示信息

						{
					title:"请先登录",
					closeText:"关闭",
					draggable:true,
					afterShow:function (e) {
						$('#Useral input[id=username]').focus();
					},
					afterHide:function (e) {
						location.reload();
					},
					modal:true
						});

			} else {
				var documentId=$('input:hidden[name="productId"]').val();
				var buyMoney=$('input:hidden[name="buyMoney"]').val();
				var count=$('input:text[name="buyCount"]').val();
				if(isNaN(count) || count < 1){
					count = 1;
				}
				var totalMoney = buyMoney * count;

				jConfirm('<div>你确定购买产品' + $(".productwelcome").text() + '吗?</div><br /><div>单价:' + buyMoney + '</div><br/><div>总价:' + totalMoney +'元</div>',
						'确认对话框', function (r) {



					if (r == true) {

						$.ajax({
							type:"POST",
							url:userHost + '/buy/add.json',
							data:{
								objectId:documentId,
								objectType:"document",
								count:count,
								totalMoney:totalMoney,
								createNewCart:true
							},
							dataType:'json',

							success:function (data) {
								console.log("添加购物车返回:" + JSON.stringify(data));
								var orderId = data.cartId;


								if (data.message.operateCode == 102008) {
									console.log("添加购物车成功，请求结算订单:" + orderId);
									//正在处理中
									// $(".ol_loading_mask null_mask").remove();

									$.ajax({
										type:"GET",
										url:userHost + '/buy/settleUp.json',
										data:{
											orderId:orderId,
											ignoreDelivery:true
										},
										dataType:'json',

										success:function (data) {
											console.log("请求结算返回:" + JSON.stringify(data));

											if (data.message.operateCode == 102005) {
												orderId = data.order.cartId;
												//查询结果
												jAlert('提交成功，正在查询订单信息![' + orderId + ']', '提示信息', function (r) {
													reajax(orderId);

													//hideloading('buyForm');
													tryTime = 1;
													
												});

												return false;


											}
											else {
												jAlert(data.message.message, '提示信息');

												return false;
												//showloading('buyForm');
												//reajax(orderId);
											}

										},
										error:function (data) {

											jAlert("系统繁忙,请稍后再试", '提示信息');
											return false;
										}
									});

								} else {
									jAlert(data.message.message, '提示信息');

									return false;
								}
							}
							,
							error : function (data) {

								jAlert("系统繁忙,请稍后再试", '提示信息');
								return false;
							}
						})

					}


				})
			}

		},


		error: function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	}
	)
	;

}

/**
 * 卖出一个点卡
 * @param productCode
 * @returns
 */
function productSale(productCode) {


	$.ajax({
		type:"GET",
		url:userHost + '/user.json',
		dataType:'json',
		async:false,
		success:function (data) {

			if (data.frontUser == "" || data.frontUser == null) {

				Boxy.load(userHost + "/content/user/loginMini.shtml", //确认提示信息

						{
					title:"请先登录",
					closeText:"关闭",
					draggable:true,
					afterShow:function (e) {
						$('#Useral input[id=username]').focus();
					},
					afterHide:function (e) {
						location.reload();
					},
					modal:true
						});

			} else {
				console.log("authType=" + data.frontUser.authType);
				if(isNaN(data.frontUser.authType) || data.frontUser.authType == undefined || data.frontUser.authType < 2){
					console.log("需要完善用户信息");
					//必须完善信息
					jConfirm('<div>您必须进一步完善信息才能寄售产品。<br/>点击确认前往完善您的信息，点击取消稍后再完善。</div>',
							'确认对话框', function (r) {
						if (r == true) {
							window.location = "/user.shtml";
							return;
						}
					});
					return false;
				}
				var documentId=$('input:hidden[name="productId"]').val();
				var saleMoney=$('input:hidden[name="saleMoney"]').val();
				var cardData=$('#saleCardInput').val();
				console.log("点卡寄售数据:" + cardData);
				if(cardData == null || cardData == ""){
					jAlert("请录入您的点卡数据", '提示信息');
					return false;
				}
				var cardArray = cardData.split("\n");
				var count = cardArray.length;
				var totalMoney = saleMoney * count;
				console.log("共提交了" + count + "条卡密");
				jConfirm('<div>你确定卖出' + count + '张' + $("#productName").text() +  '吗?</div><br /><div>单价:' + saleMoney + '</div><br/><div>总价:' + totalMoney +'元</div><br><div style="color:red">注意:该价钱并不代表最终成功售价，最终成交价由平台最终匹配确定</div>',
						'确认对话框', function (r) {

				

					if (r == true) {

						$.ajax({
							type:"POST",
							url:userHost + '/sale/add.json',
							data:{
								objectId:documentId,
								objectType:"document",
								count:count,
								totalMoney:totalMoney,
								cardData:cardData
							},
							dataType:'json',

							success:function (data) {
								console.log("添加购物车返回:" + JSON.stringify(data));
								var orderId = data.cartId;


								if (data.message.operateCode == 102008) {
									console.log("寄售成功，请求结算订单:" + orderId);
									//正在处理中
									jAlert("寄售已接收，请稍后前往订单中心查看", '提示信息');


								} else {
									jAlert(data.message.message, '提示信息');

									return false;
								}
							}
							,
							error : function (data) {

								jAlert("系统繁忙,请稍后再试", '提示信息');
								return false;
							}
						})

					}


				})
			}

		},


		error: function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	}
	)
	;

}

function selectCity(data)
{

	var option2 = '';
	var selectedIndex = $("#productRegion :selected").text();

	$("#productServer").empty();

	for (var i = 0; i < data.length; i++) {

		if (data[i].regionName == selectedIndex) {
			if (data[i].serverList != null) {


				for (var j = 0; j < data[i].serverList.length; j++) {


					$("#productServer_ui").show();
					if ($("#productRegion :selected").attr("value") == -1) {

						$("#productServer_ui").hide();
					}
					var serverNames = data[i].serverList[j].serverName;
					var serverIDs = data[i].serverList[j].serverId;
					option2 += "<option value=" + serverIDs + ">"
					+ serverNames + "</option>";

				}

			} else {

				$("#productServer_ui").hide();

			}

		}

	}

	$("#productServer").append(option2);

}

$(document).ready(function () {

	$("#productRegion").find("option[text='请选择']").attr("selected",true);	  
	$('#productRegion').bind('change',function(){

		if($("#productRegion").val()==0){


			jAlert("请选择充值区域", '提示信息');
			$(".serverChoose").hide();
			return false;



		}
		$productServerList = $("#productServerListJson").html();

		var jsonData = eval("(" + $productServerList + ")");
		var option1 = ''; 
		$.each(jsonData, function (index, indexItems) {

			if($("#productRegion").val()==index){


				$.each(indexItems.subRegionMap, function (index, indexItems) {



					option1 += "<option value=" + indexItems.regionId + ">"
					+ indexItems.regionName + "</option>";



				});	 


			}


		});	  





		if(option1!=null&&option1!=""){

			$("#serverChoose").show();

		}else{

			$("#serverChoose").hide();
		}

		$("#productServer option").remove();

		$("#productServer").append(option1);

	});

	//可购买数量等于-1
	$sl = $("#shuliang").text();
	if ($sl == '-1') {

		$("#shuliang").text("无限制");
	}




	//读取用户资金信息
	$.ajax({
		type:"GET",
		url:userHost + '/user/money.json',

		dataType:'json',

		success:function (data) {
		},
		error:function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});
	$.ajax({
		type:"GET",
		url:userHost + '/user.json',
		async:false,
		dataType:'json',

		success:function (data) {

			if (data.frontUser == null || data.frontUser == undefined) { //成功
				$('#no_login').show();
				$('#loginres').hide();
			} else {

				$('#no_login').hide();
				$('#loginres').show();

				var html = '';
				var htmlzh = '';
				//账户首页
				if (data.money == null || data.money == undefined) {

					$('#chargeMoney').text("0");

				} else {

					$('#chargeMoney').text(data.money.chargeMoney);
					$('#incomingMoney').text(data.money.incomingMoney);
					$('#frozenMoney').text(data.money.frozenMoney);
					$('#marginMoney').text(data.money.marginMoney);
					$('#coin').text(data.money.coin);
					$('#point').text(data.money.point);

				}
				if (data.frontUser.nickName != null || data.frontUser.nickName != undefined) {
					htmlzh += '欢迎回来,' + data.frontUser.nickName;
				} else {
					htmlzh += '欢迎回来,' + data.frontUser.username;
				}
				$('#usernamezh').text(htmlzh);
				if (data.frontUser.nickName != null) {
					html += '您好,' + data.frontUser.nickName;
				} else {
					html += '您好,' + data.frontUser.username;
				}
				$('#info').text(html);

			}
			//编辑用户资料


		},
		error:function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}


	});
});