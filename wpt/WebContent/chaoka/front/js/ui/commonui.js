
$(document).ready(function () {

	$("#header_HyperLink2").click(function () {
		$.ajax({
			type : "GET",
			url : userHost + '/user.json',
			dataType : 'json',
            async:false,
			success : function (data) {

				if (data.frontUser == "" || data.frontUser == null) {

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

					jAlert('您已经登录!', '提示信息');

				}

			},
			error : function (data) {

				jAlert("系统繁忙,请稍后再试", '提示信息');
				return false;
			}
		});
	});

});

//隐藏loading
function hideloading(ids) {

	var loading = new ol.loading({
			id : ids
		}); //生成一个overlay对象，遮罩会遮住有此id的元素
	loading.hide(); //显示遮罩

	$('.ol_loading').fadeOut();
	$('.ol_loading').prev().css('display', 'none');
	//alert("hi");
	// $(".ol_loading_mask null_mask").remove();
	//$('.ol_loading_mask null_mask').fadeOut();
	return true;

}

//显示loading
function showloading(ids) {

	var loading = new ol.loading({
			id : ids
		}); //生成一个overlay对象，遮罩会遮住有此id的元素

	loading.show();
	return true;

}
//登录相关
function loginuser() {

	$.ajax({
		type : "POST",
		url : userHost + '/user/login.json',
		data : {
			username : $("#Useral input[id=username]").val(),
			userPassword : $("#Useral input[id=userPassword]").val(),
            userRememberName : $("#Useral input[id=userRememberName]").val()
		},
		dataType : 'json',

		success : function (data) {


			$(".notifyOk").fadeIn(1000, function () { $('.notifyOk').text(data.message.message);});

			if (data.message.operateCode == operateResultSuccess) { //成功

				//JAlert('登录成功','提示信息');
				$('.notifyOk').text('登录成功');
				window.location.href = '/';
			} else {
				$('.notifyOk').show();
				$('.notifyOk').text(data.message.message);
			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

}

function login() {

	$.ajax({
		type : "POST",
		url : userHost + '/user/login.json',
		data : {
			username : $("#User input[id=username]").val(),
			userPassword : $("#User input[id=userPassword]").val(),
            userRememberName : $("#User input[id=userRememberName]").val()
		},
		dataType : 'json',

		success : function (data) {


			//alert(data.message.message);

	$(".loginOk").fadeIn(10000, function () { 	$(".loginOk").css("visibility", "visible");});
	$(".loginOk").fadeIn(1000, function () { $('.loginOk').text(data.message.message);});
			if (data.message.operateCode == operateResultSuccess) { //成功

				$('.notifyOk').text('登录成功');

				window.location.reload();

				//	$('#no_login').hide();
				//	$('#loginres').show();

				////var html = '';
				//if (data.frontUser.nickName != null) {
				//		html += '您好,' + data.frontUser.nickName;
				//	} else {
				//		html += '您好,' + data.frontUser.username;
				//	}
				//$('#info').text(html);

			}

		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});

}

function logout() {
	//$('#result').load("/passport.do?action=jsonLogout");
	var result = $.ajax({
			type : "GET",
			url : userHost + "/user/logout.json"
		});
	//alert(result);
	setTimeout("location.href='/'", "1000");

}
//alert(JsonUti.convertToString(data));
//json解析弹出

var JsonUti = {

	//定义换行符

	n : "\n",

	//定义制表符

	t : "\t",

	//转换String

	convertToString : function (obj) {

		return JsonUti.__writeObj(obj, 1);

	},

	//写对象

	__writeObj : function (obj //对象
	, level //层次（基数为1）
	, isInArray) { //此对象是否在一个集合内

		//如果为空，直接输出null

		if (obj == null) {

			return "null";

		}

		//为普通类型，直接输出值

		if (obj.constructor == Number || obj.constructor == Date || obj.constructor == String || obj.constructor == Boolean) {

			var v = obj.toString();

			var tab = isInArray ? JsonUti.__repeatStr(JsonUti.t, level - 1) : "";

			if (obj.constructor == String || obj.constructor == Date) {

				//时间格式化只是单纯输出字符串，而不是Date对象

				return tab + ("\"" + v + "\"");

			} else if (obj.constructor == Boolean) {

				return tab + v.toLowerCase();

			} else {

				return tab + (v);

			}

		}

		//写Json对象，缓存字符串

		var currentObjStrings = [];

		//遍历属性

		for (var name in obj) {

			var temp = [];

			//格式化Tab

			var paddingTab = JsonUti.__repeatStr(JsonUti.t, level);

			temp.push(paddingTab);

			//写出属性名

			temp.push(name + " : ");

			var val = obj[name];

			if (val == null) {

				temp.push("null");

			} else {

				var c = val.constructor;

				if (c == Array) { //如果为集合，循环内部对象

					temp.push(JsonUti.n + paddingTab + "[" + JsonUti.n);

					var levelUp = level + 2; //层级+2


					var tempArrValue = []; //集合元素相关字符串缓存片段

					for (var i = 0; i < val.length; i++) {

						//递归写对象

						tempArrValue.push(JsonUti.__writeObj(val[i], levelUp, true));

					}

					temp.push(tempArrValue.join("," + JsonUti.n));

					temp.push(JsonUti.n + paddingTab + "]");

				} else if (c == Function) {

					temp.push("[Function]");

				} else {

					//递归写对象

					temp.push(JsonUti.__writeObj(val, level + 1));

				}

			}

			//加入当前对象“属性”字符串

			currentObjStrings.push(temp.join(""));

		}

		return (level > 1 && !isInArray ? JsonUti.n : "") //如果Json对象是内部，就要换行格式化

		 + JsonUti.__repeatStr(JsonUti.t, level - 1) + "{" + JsonUti.n //加层次Tab格式化

		 + currentObjStrings.join("," + JsonUti.n) //串联所有属性值

		 + JsonUti.n + JsonUti.__repeatStr(JsonUti.t, level - 1) + "}"; //封闭对象

	},

	__isArray : function (obj) {

		if (obj) {

			return obj.constructor == Array;

		}

		return false;

	},

	__repeatStr : function (str, times) {

		var newStr = [];

		if (times > 0) {

			for (var i = 0; i < times; i++) {

				newStr.push(str);

			}

		}

		return newStr.join("");

	}

};
