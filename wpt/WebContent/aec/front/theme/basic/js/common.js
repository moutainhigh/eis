var errorNoRelatedNode = 500001;
var errorNoRelatedNode2 = 500001;
var errorNoWorkflow = 500002;
var errorObjectNotEditable = 500003;
var errorObjectIsNull = 500004;
var errorAuthFailed = 500005;
var errorAccessDenied = 500006;
var errorUnsupportedFileName = 500008;
var errorBillAlreadyExist = 500009;
var errorChargeToAccountNotExist = 500010;
var errorBillUpdateFailed = 500011;
var errorMoneyUpdateFailed = 500012;
var errorDataUpdateFailed = 500013;
var errorVersionUnSupported = 500014;
var errorUnknownError = 500015;
var errorVerifyError = 500016;
var errorCanNotParseObject = 500017;
var errorObjectAlreadyExist = 500018;
var errorAccountLocked = 500019;
var errorParentAccountLocked = 500020;
var errorBillCreateFailed = 500021;
var errorBillNotExist = 500022;
var errorDataFormatError = 500023;
var errorAccountError = 500024;
var errorAccountNotExist = 500025;
var errorMoneyRangeError = 500026;
var errorActiveSignNotFound = 500027;
var errorMailBindSignNotFound = 500028;
var errorPhoneBindSignNotFound = 500029;
var errorFindPasswordSignNotFound = 500030;
var errorUserNotFoundInSession = 500031;
var errorRequiredDataNotFound = 500032;
var errorSystemInMaintenance = 500033;
var errorSystemUpgrade = 500034;
var errorBillDuplicate = 500035;
var errorDataError = 500036;
var errorNoMatchedPrivilege = 500037;
var errorFlowWorkRouteCountError = 500038;
var errorChargeToPeerError = 500039;
var errorPaySuccessButChargeToPeerError = 500040;
var errorMailAlreadyBinded = 500041;
var errorUserNotFoundInRequest = 500042;
var errorMailBindSignNotFoundInRequest = 500043;
var errorMailBindSignNotFoundInSystem = 500044;
var errorMailBindSignNotMatch = 500045;
var errorDataUniqueConflict = 500046;
var errorDataGlobalUniqueConflict = 500047;
var errorFindPasswordSignNotFoundInSystem = 500048;
var errorTransactionObjectNotMatch = 500049;
var errorMoneyAccountNotExist = 500050;
var errorMoneyNotEnough = 500051;
var errorCartIsNull = 500052;
var errorCartIsEmpty = 500053;
var errorTransactionProcessorNotFound = 500053;
var errorAddToCartFail = 500054;
var errorPayMethodIsNull = 500055;
var errorPayProcessorIsNull = 500056;
var errorSupplierNotFound = 500057;
var errorUserNotFoundInSystem = 500058;
var errorDataVerifyFail = 500059;
var activityClosed = 500072;
var activityMissedShot = 500073;
var activityLimited = 500074;
var captchaTimeOut = 500086;
var transSuccess = 710010;
var transFailed = 710011;
var transCompleted = 710012;
var transInProcess = 710013;
var transCompletedPaySuccessChargeFailed = 710014;
var transCompletedButUpdateFailed = 710015;
var transCompletedPaySuccessChargeNotProcess = 710016;
var transTimeout = 710017;
var transInCart = 710018;
var transWaitingPay = 710019;
var transCartHalfComplete = 710020;
var transNewOrder = 710021;
var operateResultAccept = 102005;
var operateResultDeny = 102006;
var operateResultFailed = 102007;
var operateResultSuccess = 102008;
var operateStatusNew = 102009;
var operateJump = 102124;
var authSuccess = 175001;
var userFromSuggest = 120005;
var systemName = "以先";
var loginUrl = "/user/login";
var registerSubmitUrl = "/user/register";
var userHost = "";
var payHost = "";
var apiHost = "";
var suffix = '.shtml';
var Cookie = {
	setCookie: function(a, b, c, d, e) {
		document.cookie = a + "=" + escape(b) + ((c) ? ";expires=" + c.toGMTString() : "") + ((d) ? ";path=" + d : "") + ((e) ? ";domain=" + e : "")
	},
	getCookie: function(a) {
		var b = document.cookie.match(new RegExp("(^| )" + a + "=([^;]*)(;|$)"));
		if (b != null) return decodeURIComponent(b[2]).replace(/^\"|\"$/g, '');
		return null
	},
	delCookie: function(a) {
		var d = new Date();
		var b;
		d.setTime(d.getTime() - 10000);
		var c = document.domain;
		var e = new Array();
		e = c.split(".");
		if (e.length >= 3 && e[1] != "com") {
			e.splice(0, 1);
			b = e.join(".")
		} else {
			b = c
		}
		this.setCookie(a, Cookie.getCookie(a), d, "/", b);
		this.setCookie("eis_passport_f", Cookie.getCookie("eis_passport_f"), d, "/", b)
	}
};
function logout() {
	var a = $.ajax({
		type: "GET",
		url: "/user/logout.json"
	});
	setTimeout("location.href='/content/user/login.shtml'", "1000")
}
function collection(b) {
	if (Cookie.getCookie("eis_username") == null) {
		alert("您还没有登录");
		location.href = "/content/user/login.shtml"
	} else {
		$.ajax({
			type: "POST",
			url: '/userRelation/add.json',
			data: {
				objectId: b,
				objectType: 'document',
				relationType: 'favorite',
			},
			dataType: 'json',
			success: function(a) {
				switch (a.message.operateCode) {
				case 102008:
					$("#fav").attr("class", "star_icon check");
					location.reload();
					break;
				default:
					alert(a.message.message);
					break
				}
			},
			error: function(a) {
				alert("系统繁忙,请稍后再试");
				return false
			}
		})
	}
}
function uncollection(b) {
	if (Cookie.getCookie("eis_username") == null) {
		alert("您还没有登录");
		location.href = "/content/user/login.shtml"
	} else {
		$.ajax({
			type: "POST",
			url: '/userRelation/delete.json',
			data: {
				objectId: b,
				objectType: 'document',
				relationType: 'favorite',
			},
			dataType: 'json',
			success: function(a) {
				switch (a.message.operateCode) {
				case 102008:
					$("#fav").attr("class", "star_icon no_check");
					location.reload();
					break
				}
			},
			error: function(a) {
				alert("系统繁忙,请稍后再试");
				return false
			}
		})
	}
}
function like(b) {
	if (Cookie.getCookie("eis_username") == null) {
		alert("您还没有登录");
		location.href = "/content/user/login.shtml"
	} else {
		$.ajax({
			type: "POST",
			url: '/userRelation/add.json',
			data: {
				objectId: b,
				objectType: 'document',
				relationType: 'praise',
			},
			dataType: 'json',
			success: function(a) {
				switch (a.message.operateCode) {
				case 102008:
					$("#fav").attr("class", "preview_icon check");
					location.reload();
					break
				}
			},
			error: function(a) {
				alert("系统繁忙,请稍后再试");
				return false
			}
		})
	}
}
function unlike(b) {
	if (Cookie.getCookie("eis_username") == null) {
		alert("您还没有登录");
		location.href = "/content/user/login.shtml"
	} else {
		$.ajax({
			type: "POST",
			url: '/userRelation/delete.json',
			data: {
				objectId: b,
				objectType: 'document',
				relationType: 'praise',
			},
			dataType: 'json',
			success: function(a) {
				switch (a.message.operateCode) {
				case 102008:
					$("#fav").attr("class", "preview_icon no_check");
					location.reload();
					break
				}
			},
			error: function(a) {
				alert("系统繁忙,请稍后再试");
				return false
			}
		})
	}
}
function conceal() {
	if (document.getElementById("two-dimension").style.display == "none") {
		document.getElementById("two-dimension").style.display = "block"
	} else {
		document.getElementById("two-dimension").style.display = "none"
	}
}
$(function() {
	init();
	var p=null;
	var b = decodeURI(Cookie.getCookie("eis_username"));
	if (Cookie.getCookie("eis_username") != null) {
		$(".zhanghu_res").html("<a href='/content/user/pcenter.shtml?favPage=1&favRows=8'>进入我的账户</a>  <a href='#' onClick='logout();'>退出</a>");
		$(".lHidden").html('')
	}
	 $("#nav_bar>ul>li").hover(function(){
		 if($(this).hasClass("brand")){
			 //$("#nav_bar_cont").hide();
			 $("#nav_bar_cont").css("height","0px");
			 $(this).children("p").fadeIn("400").end().siblings().children("p").hide();
			 return false;
		 }
		 $(this).children("ol").clone().appendTo($("#nav_bar_cont").empty());
		 //$("#nav_bar_cont").show()
		 $("#nav_bar_cont").css("height","40px");
		 $(this).children("p").fadeIn("400").end().siblings().children("p").hide();
	 },function(){
	 
	 });
	 $("#nav_bar_cont,#nav_bar>ul>li").hover(function(){
		clearTimeout(p);
	 },function(){
		 clearTimeout(p);
		 p=setTimeout(function(){
             $("#nav_bar_cont").css("height","0px");
             $("#nav_bar>li p").hide();
            },400)
	 })
	$("img.lazy").lazyload({
		effect: "fadeIn"
	});
	$(".search_show").click(function() {
		if ($("#search_show").is(":visible") == false) {
			$("#search_show").fadeIn()
		} else {
			$("#search_show").css("display", "none")
		}
	});
	$('.menu a').each(function() {
		if ($($(this))[0].href == String(window.location)) {
			$(this).addClass('sel')
		}
	});
	$(window).on("scroll",function(){
                var nH=parseInt($(window).scrollTop());
                if(nH>112){
					$(".scroll_h").css({"display":"block"});
					$("#nav_bar_cont").css({"position":"fixed","top":"50px"})
				}
				else{
					$(".scroll_h").css({"display":"none"});
					$("#nav_bar_cont").css({"position":"absolute","top":"112px"})
				}
    })
	function init(){
		var nH=parseInt($(window).scrollTop());
        if(nH>112){
					$(".scroll_h").css({"display":"block"});
					$("#nav_bar_cont").css({"position":"fixed","top":"50px"})
				}
				else{
					$(".scroll_h").css({"display":"none"});
					$("#nav_bar_cont").css({"position":"absolute","top":"112px"})
				}
	}
})
function Timer(time,id,curTime){
	this.day_elem = $(id).find('.day');
	this.hour_elem = $(id).find('.hour');
	this.minute_elem = $(id).find('.minute');
	this.second_elem = $(id).find('.second');	
	curTime=new Date(curTime.replace(/-/g,'/')).getTime();
	this.end_time = new Date(time.replace(/-/g,'/')).getTime();//月份是实际月份-1
	this.sys_second = (this.end_time-curTime)/1000;
	this.parentContainer=$(id);
}
Timer.prototype.start=function(){
	    var self=this;
		var t=setInterval(function(){
		if (self.sys_second >= 1) {
			self.sys_second -= 1;
			self.day = Math.floor((self.sys_second / 3600) / 24);
			self.hour = Math.floor((self.sys_second / 3600) % 24);
			self.minute = Math.floor((self.sys_second / 60) % 60);
			self.second = Math.floor(self.sys_second % 60);
			self.day_elem && $(self.day_elem).text(self.day);//计算天
			$(self.hour_elem).text(self.hour<10?"0"+self.hour:self.hour);//计算小时
			$(self.minute_elem).text(self.minute<10?"0"+self.minute:self.minute);//计算分钟
			$(self.second_elem).text(self.second<10?"0"+self.second:self.second);//计算秒杀 

		} else {
			  self.parentContainer.parent().find(".fr_a").html('立即购').removeClass('fr_a1').addClass('fr_a2');
			  self.parentContainer.remove();
			  clearInterval(t);
			  //location.reload();
		}}, 1000);
	}
