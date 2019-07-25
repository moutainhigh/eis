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

					hideloading('frontUserEdit');
					jAlert(data.message.message, '提示信息', function (r) {

						location.reload();
					});

					tryTime = 1;
				} else {
					if (tryTime > 9) {

						hideloading('frontUserEdit');

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

	//修改资料提交
	$('#frontUserEdit').submit(function () {

		var obj = $("#frontUserEdit input[id=nickName]");
		//$username = $("#frontUserEdit input[id=username]").val();
		$nickName = $("#frontUserEdit input[id=nickName]").val();
		$userKeyStore = $("#frontUserEdit input[id=userKeyStore]").val();
		if ($nickName == "") {
			$(obj).parent().next(".notifyedit").text("请输入您的昵称");
			$(obj).parent().next(".notifyedit").css('color', '#ff0000');
			$(obj).parent().next(".notifyedit").css({
				'background' : 'url(/style/cuo.png)',
				'background-repeat' : 'no-repeat',
				'padding-left' : '20px',
				'line-height' : '30px',
				'height' : '30px',
				'background-position' : '0% 50%'
			});
			return false;
		}
		var submitOptions = {
			type : "POST",
			success : registerSuccess,
			error : registerError
		}
		$('#ajaxInfo').text('正在处理中');
		$('#frontUserEdit').ajaxSubmit(submitOptions);
		return false;

	});
	var tryTime = 1;
	function registerSuccess(responseText, statusText) {
		//alert(responseText.substr(responseText.length -1, responseText.length));

         var responseText=eval("("+responseText+")");
		if (responseText.message.operateCode == operateResultAccept) { //成功

			showloading('zlgj');
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

						hideloading('zlgj');

						jAlert('修改成功！', '提示信息', function (r) {
						
						
						
							window.location.reload();
						

						});

					} else if (data.message.operateCode == operateResultAccept) {

						if (tryTime > 9) {

							jAlert('出现异常,请稍后再试！', '提示信息');
							
							window.location.reload();
				

						}
								
						reajax(acontent);

					}

				},
				error : function (data) {

					jAlert("系统繁忙,请稍后再试", '提示信息');
					return false;
				}
			});

		} else {
			$('#ajaxInfo').text(responseText.message.message);
			$('#ajaxInfo').css("color", "red");
		}

	}

	function registerError(responseText, statusText) {

		jAlert("系统繁忙,请稍后再试", '提示信息');

	}
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
					afterHide: function(e) { location.reload();},
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
				//编辑用户资料
				$(".inedit").eq(0).text(data.frontUser.username);
				$('#frontUserEdit input[id=username]').attr('value', data.frontUser.username);
				$('#frontUserEdit input[id=nickName]').attr('value', data.frontUser.nickName);
				$('#frontUserEdit input[id=userKeyStore]').attr('value', data.frontUser.username);
				$('#frontUserEdit input[id=uuid]').attr('value', data.frontUser.uuid);
				$('#frontUserEdit input[id=userTypeId]').attr('value', data.frontUser.userTypeId);
				$('#frontUserEdit input[id=userExtraTypeId]').attr('value', data.frontUser.userExtraTypeId);
				$('#frontUserEdit input[id=authType]').attr('value', data.frontUser.authType);
				$('#frontUserEdit input[id=createTime]').attr('value', data.frontUser.createTime);
				$('#frontUserEdit input[id=lastLoginTimestamp]').attr('value', data.frontUser.lastLoginTimestamp);
				$('#frontUserEdit input[id=lastLoginIp]').attr('value', data.frontUser.lastLoginIp);
				$('#frontUserEdit input[id=parentUuid]').attr('value', data.frontUser.parentUuid);
				$('#frontUserEdit input[id=rootUuid]').attr('value', data.frontUser.rootUuid);
				$('#frontUserEdit input[id=address]').attr('value', data.frontUser.address);
				$('#frontUserEdit input[id=postcode]').attr('value', data.frontUser.postcode);
				$('#frontUserEdit input[id=level]').attr('value', data.frontUser.level);
				$('#frontUserEdit input[id=from_uuid]').attr('value', data.frontUser.from_uuid);
				$('#frontUserEdit input[id=inviteByUuid]').attr('value', data.frontUser.inviteByUuid);

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');

			return false;
		}
	});

});
