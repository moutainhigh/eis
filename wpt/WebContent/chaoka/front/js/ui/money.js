

$(document).ready(function () {

	$("tr").mouseover(function () {
		$("tr").mouseover(function () {
			$(this).addClass("mouseOver")
		}).mouseout(function () {
			$(this).removeClass("mouseOver")
		});
	})


	//读取用户资金信息
	$.ajax({
		type : "GET",
		url : userHost + '/user/money.json',

		dataType : 'json',

		success : function (data) {

			if (data.money != null || data.money != undefined) {

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
		url : userHost + '/user/payList.json',
		data : {
			rows : 10

		},
		dataType : 'json',

		success : function (data) {

			var responseText = data.payList;

			if (responseText != null) {

				var tTr = "";
				for (var i = 0; i < responseText.length; i++) {

					if (i % 2 == 0) {
						cc = "aa";
					} else {
						cc = "bb";
					}

					tTr += "<tr class='" + cc + "'><td >" + responseText[i].transactionId + "</td><td>" + responseText[i].startTime + "</td><td>" + responseText[i].requestMoney + "元</td><td>" + responseText[i].successMoney + "元</td><td>" + responseText[i].statusName + "</td></tr>";

				}
        
				$("table tr:eq(0)").after(tTr);
				var pagestr = "";
				for (var j = 1; j <= data.totalPage; j++) {
					
					pagestr += "<a href='javascript:void(0);' onclick='productPage("+j+");'>"+j+"</a>" + ' ';

				}
				$("#pageres").append(pagestr);

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});
	$.ajax({
		type : "GET",
		url : userHost + '/user/buyList.json',
		data : {
			rows : 5

		},
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
					afterHide : function (e) {
						location.reload();
					},
					modal : true
				});
			} else {

				$('#no_login').hide();
				$('#loginres').show();

				var html = '';
				var htmlzh = ''; //账户首页
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

});
function productPage(pageparam){

	$.ajax({
		type : "GET",
		url : userHost + '/user/payList.json',
		data : {
			rows : 10,
			page:pageparam

		},
		dataType : 'json',

		success : function (data) {

			var responseText = data.payList;

			if (responseText != null) {

				var tTr = "";
				for (var i = 0; i < responseText.length; i++) {

					if (i % 2 == 0) {
						cc = "aa";
					} else {
						cc = "bb";
					}

					tTr += "<tr class='" + cc + "'><td>" + responseText[i].transactionId + "</td><td>" + responseText[i].startTime + "</td><td>" + responseText[i].requestMoney + "元</td><td>" + responseText[i].successMoney + "元</td><td>" + responseText[i].statusName + "</td></tr>";

				}
               $("table tr:gt(0)").remove()	;			
			$("table tr:eq(0)").after(tTr);

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});



}