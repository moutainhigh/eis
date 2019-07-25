

$(document).ready(function () {

	$.ajax({
		type : "GET",
		//url : 'http://api.chaoka.cn/user.jsonp',
		url : userHost + '/user.json',
        async:false,
		dataType : 'json',
		//dataType : 'json',
		// dataType: "jsonp",
		//jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		//  jsonpCallback:"jsonpback",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据


		success : function (data) {

			//alert(data.frontUser);s

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

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

});
