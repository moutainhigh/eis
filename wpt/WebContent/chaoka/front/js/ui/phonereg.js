//ajax多次调用函数
	var tryTime = 1;
	function reajax(orderId) {

		$.ajax({
			type : "GET",
				data : {
				content : orderId

			},

			url : userHost + '/message/get/reply/' + orderId + '.json',
		
			dataType : 'json',

			success : function (data) {

				if (data.message.operateCode == operateResultSuccess) {

					hideloading('registerForm');
					jAlert(data.message.message, '提示信息', function (r) {

						location.reload();
					});

					tryTime = 1;
				} else {
					if (tryTime > 9) {

						hideloading('registerForm');

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


/* 检查注册帐号 */
function onCheckAccount(obj) {
	//	[u4e00-u9fa5]
	$("#submit").attr("disable", true);

	if (obj.value == "") {

		$(obj).parent().next(".regok").text("请输入您的手机");

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
	} else if (!obj.value.match(/^(((1[3,8,7][0-9]{1})|(15[0-9]{1}))+\d{8})$/)) {
		$(obj).parent().next(".regok").text("请输入正确的手机号码");
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
			type : "POST",
			url : userHost + '/user/exist.json',
			dataType : 'json',
            data : {
                username : obj.value

            },
			success : function (msg) {

				if (msg == true) {
					$(obj).parent().next(".regok").text("手机可以注册");
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
					$(obj).parent().next(".regok").text("手机已被使用");
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
function onCheckNum(obj) {
	$("#submit").attr("disable", true);
		
	$idnumber = $("#registerForm input[id=idnumber]").val();
	 if ($idnumber == "" ){
		

		$(obj).parent().next(".regok").text("身份证不能为空");

		$(obj).parent().next(".regok").css('color', '#ff0000');
		$(obj).parent().next(".regok").css({
			'background' : 'url(/style/cuo.png)',
			'background-repeat' : 'no-repeat',
			'padding-left' : '20px',
			'line-height' : '40px',
			'height' : '40px',
			'background-position' : '0% 50%'
		});
	
		
		}else{
			
			$(obj).parent().next(".regok").text("合法输入");
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

function onCheckTrue(obj) {
		$("#submit").attr("disable", true);
		$truename = $("#registerForm input[id=truename]").val();

	 if ($truename == "" ){
		

		$(obj).parent().next(".regok").text("真实姓名不能为空");

		$(obj).parent().next(".regok").css('color', '#ff0000');
		$(obj).parent().next(".regok").css({
			'background' : 'url(/style/cuo.png)',
			'background-repeat' : 'no-repeat',
			'padding-left' : '20px',
			'line-height' : '40px',
			'height' : '40px',
			'background-position' : '0% 50%'
		});
	
		
		}else{
			
			$(obj).parent().next(".regok").text("合法输入");
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


$(document).ready(function () {
	//$("input").val() = "";
	//$('#registerForm input[id=username]').focus();
	$('#rmcp').hide();
	$('#registerForm').submit(function () {

		//检查用户名
$truename = $("#registerForm input[id=truename]").val();

$idnumber = $("#registerForm input[id=idnumber]").val();
		var obj = $("#registerForm input[id=username]");

		if (obj.val() == "") {
			$(obj).parent().next().text("请输入您的手机");
			$(obj).parent().next().css('color', '#ff0000');
			$(obj).parent().next(".regok").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '40px',
				'height' : '40px',
				'background-position' : '0% 50%'
			});
			return false;
		}
if ($truename == "") {
			$("#registerForm input[id=truename]").parent().next().text("请输入您的真实姓名");
			$("#registerForm input[id=truename]").parent().next().css('color', '#ff0000');
			$("#registerForm input[id=truename]").parent().next(".regok").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '40px',
				'height' : '40px',
				'background-position' : '0% 50%'
			});
			return false;
		}
		if ($idnumber == "") {
			$("#registerForm input[id=idnumber]").parent().next().text("请输入您的身份证号码");
			$("#registerForm input[id=idnumber]").parent().next().css('color', '#ff0000');
			$("#registerForm input[id=idnumber]").parent().next(".regok").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '40px',
				'height' : '40px',
				'background-position' : '0% 50%'
			});
			return false;
		}
		$userPassword = $("#registerForm input[id=userPassword]").val();
		$oncePassword = $("#registerForm input[id=oncePassword]").val();

		if ($userPassword == "") {
			$("#registerForm input[id=userPassword]").parent().next().text("请输入密码");
			$("#registerForm input[id=userPassword]").parent().next().css('color', '#ff0000');
			$("#registerForm input[id=userPassword]").parent().next(".regok").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '40px',
				'height' : '40px',
				'background-position' : '0% 50%'
			});
			return false;
		}

		if ($userPassword != $oncePassword) {

			$("#registerForm input[id=oncePassword]").parent().next().text("两次输入密码不一致");
			$("#registerForm input[id=oncePassword]").parent().next().css('color', '#ff0000');
			$("#registerForm input[id=oncePassword]").parent().next(".regok").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '40px',
				'height' : '40px',
				'background-position' : '0% 50%'
			});
			return false;
		}

		//	if($("#eis_patchca").val() == ""){
		//			$("#eis_patchca").parent().next().text("请输入验证码");
		//			$("#eis_patchca").parent().next().css('color','#ff0000');
		//			return false;
		//		}


		//alert($("#license").attr('checked'));
		if ($("#license").attr('checked') == null) {

			jAlert('要注册炒卡网，您必须接受用户协议', '提示信息');

			return false;

		}
		var submitOptions = {
			success : registerSuccess,
			error : registerError
		}
		
	
		$('#registerForm').ajaxSubmit(submitOptions);
		return false;
	});
})

var tryTime = 1;
function registerSuccess(responseText, statusText) {
    //alert(responseText.substr(responseText.length -1, responseText.length));

    var responseText = eval("(" + responseText + ")");
    switch (responseText.message.operateCode) {
        case operateResultSuccess:

            jAlert('注册成功！', '提示信息', function (r) {


                location.href='/';


            });
            break

        case operateResultFailed:
            jAlert(responseText.message.message, '提示信息', function (r) {

                window.location.reload();


            });
            break

        case errorObjectAlreadyExist:
            jAlert(responseText.message.message, '提示信息');

            break

    }

    /*	if (responseText.message.operateCode == operateResultAccept) { //成功

     showloading('registerForm');

     var acontent = responseText.message.messageId;

     if (acontent == null) {

     jAlert('出现异常,请稍后再试！', '提示信息');

     window.location.reload();

     }

     $.ajax({
     type : "GET",
     data : {
     content : acontent

     },
     url : userHost + '/message/get/reply/' + acontent + '.json',
     dataType : 'json',
     success : function (data) {


     if (data.message.operateCode == operateResultSuccess) {

     hideloading('registerForms');

     jAlert('注册成功！', '提示信息', function (r) {

     //$('#notifyOk').hide();

     Boxy.load(userHost + "/content/user/loginMini.shtml", //确认提示信息

     {
     title : "注册后请先登录",
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


     });

     } else if (data.message.operateCode == operateResultAccept) {

     if (tryTime > 9) {

     jAlert('出现异常,请稍后再试！', '提示信息');

     window.location.reload();

     }
     reajax(acontent);

     }else{

     jAlert(data.message.message, '提示信息', function (r) {

     hideloading('registerForm');

     });

     }

     },
     error : function (data) {

     jAlert("系统繁忙,请稍后再试", '提示信息');
     return false;
     }
     });

     } else {
     jAlert('注册成功请回首页登陆！', '提示信息', function (r) {

     //$('#notifyOk').hide();
     location.href='/';


     });
     }*/

}

function registerError(responseText, statusText) {

	jAlert("系统繁忙,请稍后再试", '提示信息');

}
