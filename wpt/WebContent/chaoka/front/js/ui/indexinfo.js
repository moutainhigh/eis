
//注册相关
function isEmail(str) {
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(str);
}

/* 检查注册帐号 */
function onCheckAccount(obj) {
	//	[u4e00-u9fa5]
	$("#submit").attr("disable", true);

	if (obj.value == "") {

		$(obj).parent().next(".regok").text("请输入您的邮箱");

		$(obj).parent().next(".regok").css('color', '#ff0000');
		$(obj).parent().next(".regok").css({
			'background' : 'url(/style/cuo.png)',
			'background-repeat' : 'no-repeat',
			'padding-left' : '20px',
			'line-height' : '40px',
			'height' : '40px',
			'background-position' : '0% 50%'
		});
		$(obj).focus();
	} else if (!isEmail(obj.value)) {
		$(obj).parent().next(".regok").text("请输入正确的邮箱");
		$(obj).parent().next(".regok").css('color', '#ff0000');
		$(obj).parent().next(".regok").css({
			'background' : 'url(/style/cuo.png)',
			'background-repeat' : 'no-repeat',
			'padding-left' : '20px',
			'line-height' : '40px',
			'height' : '40px',
			'background-position' : '0% 50%'
		});
		$(obj).focus();
	} else {
		$.ajax({
			type : "GET",
			url : userHost + '/user/exist/' + obj.value + '.json',
			dataType : 'json',
			success : function (msg) {

				if (msg.message.operateCode == 102008) {
					$(obj).parent().next(".regok").text("帐号可以注册");
					$(obj).parent().next(".regok").css('color', '#70ad00');
					$(obj).parent().next(".regok").css({
						'background' : 'url(/style/dui.png)',
						'background-repeat' : 'no-repeat',
						'padding-left' : '20px',
						'line-height' : '40px',
						'height' : '40px',
						'background-position' : '0% 50%'
					});
					$("#submit").attr("disable", false);
				} else {
					$(obj).parent().next(".regok").text("帐号已被使用");
					$(obj).parent().next(".regok").css('color', '#ff0000');
					$(obj).parent().next(".regok").css({
						'background' : 'url(/style/cuo.png)',
						'background-repeat' : 'no-repeat',
						'padding-left' : '20px',
						'line-height' : '40px',
						'height' : '40px',
						'background-position' : '0% 50%'
					});
					$(obj).focus();
				}

			},
			error : function (xml, err) {
				jAlert("系统繁忙,请稍后再试", '提示信息');
			}
		});
	}
}

function onCheckEmpty(obj) {
	$("#submit").attr("disable", true);
	$userPassword = $("#registerForm input[id=userPassword]").val();
	$oncePassword = $("#registerForm input[id=oncePassword]").val();

	if ($(obj).val() == "" || $userPassword.length < 3) {

		$(obj).parent().next(".regok").text("密码不能为空且大于3位字符");

		$(obj).parent().next(".regok").css('color', '#ff0000');
		$(obj).parent().next(".regok").css({
			'background' : 'url(/style/cuo.png)',
			'background-repeat' : 'no-repeat',
			'padding-left' : '20px',
			'line-height' : '40px',
			'height' : '40px',
			'background-position' : '0% 50%'
		});
	} else {
		$("#submit").attr("disable", false);
		$(obj).parent().next(".regok").text("输入合法");
		$(obj).parent().next(".regok").css('color', '#70ad00');
		$(obj).parent().next(".regok").css({
			'background' : 'url(/style/dui.png)',
			'background-repeat' : 'no-repeat',
			'padding-left' : '20px',
			'line-height' : '40px',
			'height' : '40px',
			'background-position' : '0% 50%'
		});
	}

	if ($userPassword != "" && $oncePassword != "") {
		if ($userPassword != $oncePassword) {

			$("#oncePassword").parent().next(".regok").text("两次输入密码不一致");
			$("#oncePassword").parent().next(".regok").css('color', '#ff0000');
			$("#oncePassword").parent().next(".regok").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '40px',
				'height' : '40px',
				'background-position' : '0% 50%'
			});
			return false;
		}
	}
}

function onchangePatchca(obj) {
	var xmlR = Math.random(10000);
	$(obj).prev().attr("src", "/patchca.do?rd=" + xmlR);
}

$(document).ready(function () {
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
        async : false,
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
					afterHide : function (e) {
						location.reload();
					},
					modal : true
				});
			} else {

				$('#no_login').hide();
				$('#loginres').show();

				if (data.frontUser.authType == 2 ||data.frontUser.authType == 3) {

					$('.mobile_list').hide();
					$('.mobile_list2').show();
				} else {

					$('.mobile_list').show();
					$('.mobile_list2').hide();

				}
				
				if (data.frontUser.authType == 1 ||data.frontUser.authType == 3) {

					$('.email_list').hide();
					$('.email_list2').show();
				} else {

					$('.email_list').show();
					$('.email_list2').hide();

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
