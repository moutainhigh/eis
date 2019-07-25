
function isEmail(str){
       var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
       return reg.test(str);
}
/* 注册相关 */
/* 检查帐号是否已注册 */
function onCheckAccount(obj){
//	[u4e00-u9fa5] 
	if(obj.value == ""){
		$('#ajaxInfo').text("请输入您的邮箱");
		return false;
	} else if(!isEmail(obj.value)){
		$('#ajaxInfo').text("请输入正确的邮箱");
		return false;
	} else {
		$.ajax({
		   type:"GET",
		   url:'/register.do?action=checkExist&username='+obj.value,
		   dataType:'json',
		   success: function(msg){
			   
			   if(msg.operateCode == 102008){
					$('#ajaxInfo').text("帐号可以注册");
					$('#ajaxInfo').css("color","green");
					
					return true;
			   } else {
					$('#ajaxInfo').text("帐号已被使用");
					$('#ajaxInfo').css("color","red");
					return false;
			   }
			
		   },
		   error: function(xml, err) {
				jAlert("系统繁忙,请稍后再试",'提示信息');
				return false;
			}
		});
	}
}

function registerSuccess(responseText, statusText)  { 
	//alert(responseText.substr(responseText.length -1, responseText.length));
	if(responseText.substr(0,1) == '{' && responseText.substr(responseText.length -1, responseText.length) == '}'){		
		var response = eval("("+responseText+")");
		
			if(response.operateCode == 102008){//成功
				$('#ajaxInfo').text('注册成功');	
				$('#ajaxInfo').css("color","green");
				var bizId = $('#goUrl').attr('alt');
				//alert(bizId);
				if(bizId != null && bizId != '#'){
					frontUser = "login";
					goBiz(bizId);
				} else {
					if(response.message.substr(0,1) == '/' || response.message.substr(0,4) == 'http'){
						window.location.href=response.message;
					} else {
						window.location.reload();
					}
				}				
			} else {
				$('#ajaxInfo').text(response.message);
				$('#ajaxInfo').css("color","red");
			}
	} else {
		$('#ajaxInfo').text('抱歉，系统异常!');
		$('#ajaxInfo').css("color","red");
	}
}


function registerError(responseText, statusText)  {
	if(responseText.substr(0,1) == '{' && responseText.substr(responseText.length -1, responseText.length) == '}'){		
		var response = eval("("+responseText+")");
		$('#ajaxInfo').text(responseText.message);		
	} else {
		alert("系统繁忙，请稍后再试: " + responseText);
	}
}

function openRegisterDialog(){
		$('#registerDialog').dialog('open');
}

/* 登录相关 */
function loginSuccess(responseText, statusText)  { 
	//alert(responseText.substr(responseText.length -1, responseText.length));
	if(responseText.substr(0,1) == '{' && responseText.substr(responseText.length -1, responseText.length) == '}'){		
		var response = eval("("+responseText+")");
			//alert(response.operateCode);
			if(response.operateCode == 175001){//成功
				$('#ajaxInfo').text('登录成功');
				var bizId = $('#goUrl').attr('alt');
				//alert(bizId);
				if(bizId != null && bizId != '#'){
					frontUser = "login";
					goBiz(bizId);
				} else {
					window.location.reload();
				}
				
			} else {
				$('#ajaxInfo').text(response.message);
			}
	} else {
		$('#ajaxInfo').text('抱歉，系统异常!');
	}
}


function loginError(responseText, statusText)  {
	if(responseText.substr(0,1) == '{' && responseText.substr(responseText.length -1, responseText.length) == '}'){		
		var response = eval("("+responseText+")");
		$('#ajaxInfo').text(responseText.message);
		alert(responseText.message);
	} else {
		$('#ajaxInfo').text("系统繁忙，请稍后再试");	
		alert("系统内部错误，请稍后再试或联系客服。");
	}
}

function logout(){
	//$('#result').load("/passport.do?action=jsonLogout");
	var result = $.ajax({
		url:"/passport.do?action=jsonLogout"
	});
	//alert(result);
	setTimeout("window.location.reload()", 1000);
	
}

function goBizz(bizId){
			
			var url = "/business.do?business=" + bizId;
	$('#goUrl').attr('href',url);
	$('#goUrl').attr('alt',bizId);
	if(frontUser == "" || frontUser == null){
		alert("请您先登录，然后才能开始游戏。");
		return;
	}
	$.get("/business.do?action=businessStatus&business=" + bizId, function businessStatus(response){
		if(response.operateCode == 122001){
			alert("您选择的游戏尚未开放，请耐心等待。");
			return;
		}
		if(response.operateCode == 122003){
			alert("您选择的游戏处于系统维护中，请耐心等待。");
			return;
		}
		if(response.operateCode == 122004){
			alert("您选择的游戏已关闭，请选择其他游戏。");
			return;
		}		
		if(response.operateCode == 122002){
			$.get("/business.do?action=getBusinessServer&business=" + bizId, function businessServer(response){
				
				$('#serverChooseDialog').empty();
				$('#serverChooseDialog').css({"width": "600px","min-height": "0px", "overflow-y":"auto",height: "200px"}); 
				
				var html = "<ul class='dialogServerUl'>";
				for( var j = 0; j < response.length; j++){
					if(response[j].extraStatus == 122002){
						html += "<li class='dialogServerLi'><a";
						if(response[j].businessSeverActivity > 800){
							html += " class='hot'";
						} else 	if(response[j].businessSeverActivity < 100){
							html += " class='idle'";
						} else {
							html += " class='normal'";
						}
						html += " href='/business.do?business=" + bizId + "&server=" + response[j].businessServerId + "'>[" + response[j].businessServerName + "]</a></li>";
					} else if(response[j].extraStatus == 122001){
						html += "<li class='dialogServerLi'><a class='disable' href='javascript:alert(\"该服即将开放，敬请期待！\");'>[" + response[j].businessServerName + "]</a></li>";	
					} else if(response[j].extraStatus == 122003){
						html += "<li class='dialogServerLi'><a class='disable' href='javascript:alert(\"该服维护中，请稍后再试。\");'>[" + response[j].businessServerName + "]</a></li>";
						
					} else if(response[j].extraStatus == 122004){
						html += "<li class='dialogServerLi'><a class='disable' href='javascript:alert(\"该服已关闭，请选择其他服。\");'>[" + response[j].businessServerName + "]</a></li>";
						
					}
				}
				html += "</ul>";
				html2="<div style='color:#000000;background:#eeeeee;'>"+html+"</div>";
          
                var box1 = new Boxy("<div style='color:#ff0000;'>"+html2+"</div>" //显示内容
        ,
                {
                    title: "请选择服务器", //对话框标题
                    modal: true, //是否为模式窗口
                   
                    closeText: "关闭",   //关闭功能按钮的标题文字
                    draggable: true //是否可以拖动
                });
                box1.resize(590, 300);  //设置对话框的大小
				return;
				
			},"json");
		} else {
				alert("您选择的游戏状态异常，请选择其他游戏。");
				return;
			
		}
		},"json"
	);
			
           

		}
function goBiz(bizId){	
	$('#loginDialog').dialog("close");
	
	var url = "/business.do?business=" + bizId;
	$('#goUrl').attr('href',url);
	$('#goUrl').attr('alt',bizId);
	if(frontUser == "" || frontUser == null){
		alert("请您先登录，然后才能开始游戏。");
		return;
	}
	$.get("/business.do?action=businessStatus&business=" + bizId, function businessStatus(response){
		if(response.operateCode == 122001){
			alert("您选择的游戏尚未开放，请耐心等待。");
			return;
		}
		if(response.operateCode == 122003){
			alert("您选择的游戏处于系统维护中，请耐心等待。");
			return;
		}
		if(response.operateCode == 122004){
			alert("您选择的游戏已关闭，请选择其他游戏。");
			return;
		}		
		if(response.operateCode == 122002){
			$.get("/business.do?action=getBusinessServer&business=" + bizId, function businessServer(response){
				
				$('#serverChooseDialog').empty();
				$('#serverChooseDialog').css({"width": "600px","min-height": "0px", "overflow-y":"auto",height: "200px"}); 
				
				var html = "<ul class='dialogServerUl'>";
				for( var j = 0; j < response.length; j++){
					if(response[j].extraStatus == 122002){
						html += "<li class='dialogServerLi'><a";
						if(response[j].businessSeverActivity > 800){
							html += " class='hot'";
						} else 	if(response[j].businessSeverActivity < 100){
							html += " class='idle'";
						} else {
							html += " class='normal'";
						}
						html += " href='/business.do?business=" + bizId + "&server=" + response[j].businessServerId + "'>[" + response[j].businessServerName + "]</a></li>";
					} else if(response[j].extraStatus == 122001){
						html += "<li class='dialogServerLi'><a class='disable' href='javascript:alert(\"该服即将开放，敬请期待！\");'>[" + response[j].businessServerName + "]</a></li>";	
					} else if(response[j].extraStatus == 122003){
						html += "<li class='dialogServerLi'><a class='disable' href='javascript:alert(\"该服维护中，请稍后再试。\");'>[" + response[j].businessServerName + "]</a></li>";
						
					} else if(response[j].extraStatus == 122004){
						html += "<li class='dialogServerLi'><a class='disable' href='javascript:alert(\"该服已关闭，请选择其他服。\");'>[" + response[j].businessServerName + "]</a></li>";
						
					}
				}
				html += "</ul>";
				$('#serverChooseDialog').html(html);
				//alert(html);
				$('#serverChooseDialog').dialog({
					modal: true,
					autoOpen: true,
					resizable: false,
					width: 600,
					height:230
				});
				return;
				
			},"json");
		} else {
				alert("您选择的游戏状态异常，请选择其他游戏。");
				return;
			
		}
		},"json"
	);
	
	
}


