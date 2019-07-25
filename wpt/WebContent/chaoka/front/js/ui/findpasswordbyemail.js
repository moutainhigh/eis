
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

		$(obj).parent().next(".regok").text("输入正确");
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
}

$(document).ready(function () {
	//$("input").val() = "";
	//$('#registerForm input[id=username]').focus();
	$('#rmcp').hide(); //左侧隐藏菜单热门产品
	$('#findemailForm').submit(function () {

		//检查用户名


		//alert($("#license").attr('checked'));

		var submitOptions = {
			success : registerSuccess,
			error : registerError
		}

		$('#findemailForm').ajaxSubmit(submitOptions);
		return false;
	});
});

function registerSuccess(responseText, statusText) {
	//alert(responseText.substr(responseText.length -1, responseText.length));

	var responseText = eval("(" + responseText + ")");

	if (responseText.message.operateCode == operateResultSuccess) { //成功


		jAlert(responseText.message.message, '提示信息', function (r) {

			window.location.reload();

		});

	} else {
		jAlert(responseText.message.message, '提示信息');

		return false;
	}

}

function registerError(responseText, statusText) {

	jAlert("系统繁忙,请稍后再试", '提示信息');

}
