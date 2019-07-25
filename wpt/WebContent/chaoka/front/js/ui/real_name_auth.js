
/**
 * 实名认证
 */
function isEmail(str) {
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(str);
}

$(document).ready(function () {

	$("#bind").click(function () {

		if ($("#realName").val() == "") {

			jAlert("请填写真实姓名！", '提示信息');

			$("#realName").focus();
			return false;
		}
		if ($("#idNumber").val() == "") {

			jAlert("请填写身份证号！", '提示信息');

			$("#idNumber").focus();
			return false;
		}

		$.ajax({
			type : "POST",
			url : userHost + '/user/realNameAuth.json',
			data : {
				realName : $("#realName").val(),
				idNumber : $("#idNumber").val()
			},
			dataType : 'json',
			success : function (data) {

				if (data.message.operateCode == operateResultSuccess) {

					jAlert(data.message.message, '提示信息');

					location.reload();

				} else {

					jAlert(data.message.message, '提示信息');
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
				Boxy.load("/user/login.html", //确认提示信息

				{
					title : "请先登录",
					closeText : "关闭",
					draggable : true,
					afterShow : function (e) {
						$('#Useral input[id=username]').focus();
					},
					afterHide : function (e) {
						location.href = "/";
					},
					modal : true
				});
			} else {

				$('#no_login').hide();
				$('#loginres').show();
				if (data.frontUser.authType == 1 || data.frontUser.authType == 3) {

					$('.mobile_form').hide();
					$('.mobile_form2').show();
				} else {
					$('.mobile_form').show();
					$('.mobile_form2').hide();

				}

                if(data.frontUser.email!=null){

                    $('#bindMailBox').attr("value",data.frontUser.email);
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
