
function request(paras) {
	var url = location.href;
	var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
	var paraObj = {}
	for (i = 0; j = paraString[i]; i++) {
		paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
	}
	var returnValue = paraObj[paras.toLowerCase()];
	if (typeof(returnValue) == "undefined") {
		return "";
	} else {
		return returnValue;
	}
}

$(document).ready(function () {



	//读取绑定邮箱结果
	$.ajax({
		type : "POST",
		url : userHost + '/user/bindEmail/confirm/'+request("uuid")+'/'+request("sign")+'.json',

		dataType : 'json',

		success : function (data) {

			//if (data.message.operateCode == operateResultSuccess) {
		
			
				$('#jieguo').html(data.message.message);
			
			
			//}else{
			
			 
		    //$('#jieguo').text(data.message.message);
		
	

			
			//}
		},
		error : function (data) {

			jAlert("系统繁忙,请稍后再试", '提示信息');
			return false;
		}
	});



});
