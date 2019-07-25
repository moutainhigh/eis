
//注册相关
function isEmail(str) {
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(str);
}
//验证手机
function checkSubmitMobil() {

	if ($("#mobile").val() == "") {

		jAlert("手机号码不能为空！", '提示信息');

		$("#mobile").focus();
		return false;
	} else

		if (!$("#mobile").val().match(/^(((1[3,8,7][0-9]{1})|(15[0-9]{1}))+\d{8})$/)) {

			jAlert("手机号码格式不正确！请重新输入！", '提示信息');

			$("#mobile").focus();
			return false;
		} else {
			return true;
		}
}
/* sh */
var wait = 60;
function time(o, q) {

	if (q == 1) {

		wait = 0;
		document.getElementById("btn").removeAttribute("disabled");
		document.getElementById("btn").value = "免费获取验证码";
		return false;
	}

	if (wait == 0) {

		document.getElementById("btn").removeAttribute("disabled");
		document.getElementById("btn").value = "免费获取验证码";
		wait = 60;
	} else {

		document.getElementById("btn").setAttribute("disabled", true);
		document.getElementById("btn").value = "重新发送(" + wait + ")";
		wait--;
		setTimeout(function () {
			time(o, "")
		},
			1000)
	}
}
$(document).ready(function () {

	$("#btn").click(function () {

		if (checkSubmitMobil() != false) {
			time(this, "");

			var mobile = $("#mobile").val();
			$.ajax({
				type : "POST",
				url : userHost + '/user/bindPhone/submit/' + mobile + '.json',
				dataType : 'json',
				success : function (data) {

					if (data.message.operateCode == operateResultSuccess) {

						var crmob = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
						var acrmob = "校验码短信已发送至：" + crmob;

						jAlert(acrmob, '提示信息');

						return false;

					} else {

						jAlert(data.message.message, '提示信息', function (r) {

							time(this, 1);

						});

						//$("input[id='btn']").attr("disabled", true);


						//$("#btn").removeAttribute("disabled");
						return false;

					}

				},
				error : function (xml, err) {
					jAlert("系统繁忙,请稍后再试", '提示信息');
				}
			});

		}

	});

	$("#bind").click(function () {

		if ($("#confirm_code").val() == "") {

			jAlert("校验码不能为空！", '提示信息');

			$("#confirm_code").focus();
			return false;
		}

		$.ajax({
			type : "POST",
			url : userHost + '/user/bindPhone/confirm/' + $("#confirm_code").val() + '.json',
			dataType : 'json',
			success : function (data) {

				if (data.message.operateCode == operateResultSuccess) {

					jAlert(data.message.message, '提示信息');

					location.reload();

				} else {

					jAlert(data.message.message, '提示信息', function (r) {

						time(this, 1);

					});
					return false;

				}

			},
			error : function (xml, err) {
				jAlert("系统繁忙,请稍后再试", '提示信息');
			}
		});
	})

	//读取用户资金信息
	$.ajax({
		type : "GET",
		url : userHost + '/user/money.json',

		dataType : 'json',

		success : function (data) {

			if (data.money != null) {

				$('#chargeMoney2').text(data.money.chargeMoney);
				$('#incomingMoney').text(data.money.incomingMoney);
				$('#frozenMoney').text(data.money.frozenMoney);
				$('#marginMoney').text(data.money.marginMoney);
				$('#coin').text(data.money.coin);
				$('#point').text(data.money.point);

			}
		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

	$.ajax({
		type : "GET",

		url : userHost + '/user.json',

		dataType : 'json',
        async : false,
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
					afterHide : function (e) {
						location.href="/";
					},
					modal : true
				});
			} else {

				$('#no_login').hide();
				$('#loginres').show();
				if (data.frontUser.authType == 2||data.frontUser.authType == 3) {
		
					$('.mobile_form').hide();
					$('.mobile_form2').show();
				}else{
				    $('.mobile_form').show();
					$('.mobile_form2').hide();
				
				}
                if(data.frontUser.phone!=null){

                    $('#mobile').attr("value",data.frontUser.phone);
                }
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

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

});
