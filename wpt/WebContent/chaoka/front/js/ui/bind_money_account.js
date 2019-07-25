
/**
 * 实名认证
 */
function isEmail(str) {
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(str);
}

$(document).ready(function () {

	$("#bind").click(function () {

		if ($("#bankAccountName").val() == "") {

			jAlert("请填写收款人姓名！", '提示信息');

			$("#bankAccountName").focus();
			return false;
		}
		if ($("#bankAccountNumber").val() == "") {

			jAlert("请填写银行卡号/微信号/支付宝账号！", '提示信息');

			$("#bankAccountNumber").focus();
			return false;
		}
		var formData = new FormData($("#bankAccountForm")[0]);

		$.ajax({
			type : "POST",
			url : userHost + '/user/bindMoneyAccount.json',
			data : formData,
			dataType : 'json',
			//encType: 'multipart/form-data',
			contentType: false,
			processData: false,
			success : function (data) {
				console.log("请求绑定银行卡:" + JSON.stringify(data));
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



});
