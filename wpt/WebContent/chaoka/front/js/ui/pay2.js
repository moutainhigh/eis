//充值列表页step1.jsp
//ajax多次调用函数
var tryTime = 1;
function reajax(orderId) {

	$.ajax({
		type : "GET",
		url : userHost + '/pay/query/' + orderId + '.json',
		dataType : 'json',

		success : function (data) {

			if (data.message.operateCode != transInProcess) {

				hideloading('chargeForm');
				jAlert(data.message.message + data.message.operateCode, '提示信息', function (r) {

					location.reload();
				});

				tryTime = 1;
			} else {
				if (tryTime > 24) {

					hideloading('chargeForm');

					jAlert('系统正在为您处理中,请稍后查询！', '提示信息', function (r) {

						location.reload();
					});

				}

				window.setTimeout(function () {
					reajax(orderId);
				}, tryTime * 1000);

				tryTime = ++tryTime;

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

}
$(document).ready(function () {

	$('#chargeForm').submit(function () {

		if (validAmount.length > 1) { //点卡充值
			var cardChecked = false;
			$.each($(".card_fee"), function (i, n) {
				if (n.checked) {
					cardChecked = true;
				}
				//alert(n.value);
			});
			if (!cardChecked) {
				jAlert('请选择点卡面值。', '提示信息');

				return false;
			}
			if ($('#card_serialnumber').val().length != cardSerialnumberLength) {

				jAlert('卡号必须是' + cardSerialnumberLength + '位.', '提示信息');

				return false;
			}
			if ($('#card_password').val().length != cardPasswordLength) {
				jAlert('密码必须是' + cardPasswordLength + '位.', '提示信息');

				return false;
			}
		} else { //网银充值
			//if (payTypeId != 10) {
            if (inputType != "account"){
				var bankSelected = false;
				$.each($('.bank'), function (i, n) {
					if (n.checked) {
						bankSelected = true;
					}
				});

				if (!bankSelected) {
					jAlert('请选择银行', '提示信息');

					return false;
				}
			}
			var totalfree = $('#total_fee').val();
			
			if ($('#total_fee').val() == null) {
				jAlert('请输入要充值的金额', '提示信息');

				return false;
			}
			
			
			if (parseFloat(totalfree) < 1) {

				jAlert('请输入有效的金额', '提示信息');

				return false;
			}
			
			if (!checkPrice(totalfree)) { //不是数字isNaN返回true
				jAlert('请输入有效的金额', '提示信息');
				return false;
			}

		}

		$.ajax({
			type : "GET",
			url : userHost + '/user.json',

			dataType : 'json',

			success : function (data) {

				if (data.frontUser == null||data.frontUser == undefined ) { //成功
					Boxy.load(userHost + "/content/user/loginMini.shtml", //确认提示信息

					{
						title : "请先登录",
						closeText : "关闭",
						draggable : true,
						afterShow : function (e) {
							$('#Useral input[id=username]').focus();
						},
						//afterHide: function(e) { location.reload();},
						modal : true
					});
				} else {

					var submitOptions = {
						success : registerSuccess,
						error : registerError
					}
					//$('#ajaxInfo').text('正在处理中');
					$('#chargeForm').ajaxSubmit(submitOptions);
					return false;

				}

			},
			error : function (data) {

				jAlert("系统繁忙,请稍后再试", '提示信息');
				return false;
			}
		});

	});

	function registerSuccess(responseText, statusText) {
		//alert(responseText.substr(responseText.length -1, responseText.length));
var responseText=eval("("+responseText+")");

		//alert(responseText.message.operateCode);
		if (responseText.message.operateCode == operateJump) {
			
			//alert(responseText.message.message);

			$('html').append(responseText.message.message);

		}
		if (responseText.message.operateCode == transFailed) {

			jAlert(responseText.message.message, '提示信息');
			return false;

		}
        if (responseText.message.operateCode == transSuccess) {

            jAlert(responseText.message.message, '提示信息');
            return false;

        }


		if (responseText.message.operateCode == transInProcess) {

			$.ajax({
				type : "GET",
				url : userHost + '/pay/query/' + responseText.message.message + '.json',

				dataType : 'json',

				success : function (data) {

					if (data.message.operateCode != transInProcess) {

						hideloading('chargeForm');

						jAlert(data.message.message, '提示信息');

						location.reload();
						//setTimeout("location.href='/pay'", "2000");
					} else {
						showloading('chargeForm');
						reajax(responseText.message.message);
					}

				},
				error : function (data) {

					jAlert("系统繁忙,请稍后再试", '提示信息');
					return false;
				}
			});
		}

	}

	function registerError(responseText, statusText) {

		jAlert("系统繁忙,请稍后再试", '提示信息');

	}
	//读取用户资金信息
	$.ajax({
		type : "GET",
		url : userHost + '/user/money.json',

		dataType : 'json',

		success : function (data) {},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});
	$.ajax({
		type : "GET",
		url : userHost + '/user.json', async:false,

		dataType : 'json',

		success : function (data) {

		if (data.frontUser == null || data.frontUser == undefined) { //成功
				$('#no_login').show();
				$('#loginres').hide();
				Boxy.load(userHost + "/content/user/loginMini.shtml", //确认提示信息

				{
					title : "请先登录",
					closeText : "关闭",
					draggable : true,
					afterShow : function (e) {
						$('#Useral input[id=username]').focus();
					},
					//afterHide: function(e) { location.reload();},
					modal : true
				});
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
				if (data.frontUser.nickName != null|| data.frontUser.nickName != undefined) {
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
				//编辑用户资料

				$('#securityEdit input[id=username]').attr('value', data.frontUser.username);
				$('#securityEdit input[id=nickName]').attr('value', data.frontUser.nickName);
				$('#securityEdit input[id=userKeyStore]').attr('value', data.frontUser.username);
				$('#securityEdit input[id=uuid]').attr('value', data.frontUser.uuid);
				$('#securityEdit input[id=userTypeId]').attr('value', data.frontUser.userTypeId);
				$('#securityEdit input[id=userExtraTypeId]').attr('value', data.frontUser.userExtraTypeId);
				$('#securityEdit input[id=authType]').attr('value', data.frontUser.authType);
				$('#securityEdit input[id=createTime]').attr('value', data.frontUser.createTime);
				$('#securityEdit input[id=lastLoginTimestamp]').attr('value', data.frontUser.lastLoginTimestamp);
				$('#securityEdit input[id=lastLoginIp]').attr('value', data.frontUser.lastLoginIp);
				$('#securityEdit input[id=parentUuid]').attr('value', data.frontUser.parentUuid);
				$('#securityEdit input[id=rootUuid]').attr('value', data.frontUser.rootUuid);
				$('#securityEdit input[id=address]').attr('value', data.frontUser.address);
				$('#securityEdit input[id=postcode]').attr('value', data.frontUser.postcode);
				$('#securityEdit input[id=level]').attr('value', data.frontUser.level);
				$('#securityEdit input[id=from_uuid]').attr('value', data.frontUser.from_uuid);
				$('#securityEdit input[id=inviteByUuid]').attr('value', data.frontUser.inviteByUuid);

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

	var inputType = $("#inputType").val();
	var validAmount = $("#validAmount").val();
	var cardSerialnumberLength = $("#cardSerialnumberLength").val();
	var cardPasswordLength = $("#cardPasswordLength").val();
	var payTypeId = $("#payTypeId").val();
	$("#selectAmount").hide();
	$("#accountlist").hide();
	$("#banklist").hide();
	switch (inputType) {
	case "card":
		$("#selectAmount").show();
		if (validAmount.length > 1) {
			var amountRadio = validAmount.split(',');
			//alert(amountRadio.length);
			var amountHtml = "";
			for (var i = 0; i < amountRadio.length; i++) {
				//amountHtml += amountRa
				amountHtml += "<input type='radio' id='card_fee' name='card_fee' class='card_free' value='" + amountRadio[i] + "'/>" + amountRadio[i] + "元";
			}
			amountHtml += "<p><span>请输入充值卡卡号：</span><input type=\"text\" name=\"card_serialnumber\" class=\"inptu_cz\" id=\"card_serialnumber\" value=\"\" /><span class=\"notice\"></span> </p><p><span>请输入充值卡密码：</span><input type=\"text\" class=\"inptu_cz\" name=\"card_password\" id=\"card_password\" value=\"\" /><span class=\"notice\"></span></p>";
			//alert(amountHtml);
			$("#selectAmount").empty();
			$("#selectAmount").append(amountHtml);
		}
		//$("#accountlist").show();
		break

	case "account":
		$("#accountlist").show();
		break

	case "bank":
		$("#accountlist").show();
		$("#banklist").show();
		break

	}

});
//验证金额
function checkPrice(price){
  return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(price.toString());
}