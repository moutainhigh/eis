<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>



<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /><title>
	七彩商城
</title>
	<link href="/theme/ec1/style/com.css" rel="stylesheet" />
	<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
	<link href="/theme/ec1/style/web.css" rel="stylesheet" />
	
	<link href="/theme/ec1/style/InputFrom.css" rel="stylesheet" />
</head>
<body>
    <form name="form1" method="post"  id="form1">

        <div class="Top">
            <div class="Top_Left">
                <a href="/content/user/register.shtml" class="Logo"></a><span class="Top_Title">欢迎注册</span>
            </div>
        </div>
        <div class="Wrap1000">
            <div class="InputWrap">
                <span class="InputWrap_Tip">我已经注册，现在就&nbsp;&nbsp;&nbsp;<a href="/content/user/login.shtml">登录</a></span>
                <div class="InputForm">
                    <table>
                        <tr>
                            <th>手机号：<input type="hidden" name="hidCheckCode" id="hidCheckCode" />
                            </th>
                            <td>
                                <input name="username" type="text" maxlength="11" id="username" class="InputWidth270" PlaceHolder="请填写手机号" /></td>
                        </tr>
                        <tr>
                            <th>附加码：</th>
                            <td>
                                <input name="txtExtCode" type="text" maxlength="5" id="txtExtCode" class="InputWidth180" />
                                <img id="imgCode" title="点击更换验证码" class="imgExtCode" onclick="reLoadExtCode()" src="/captcha" align="absmiddle" style="height:35px;width:60px;border-width:0px;" />
                            </td>
                        </tr>
                        <tr>
                            <th>验证码：</th>
                            <td>
                                <input name="smsRegisterSign" type="text" maxlength="6" id="smsRegisterSign" class="InputWidth180" PlaceHolder="输入短信验证码" />
                                <input type="button" id="btnSendExtCode" class="btnExtCode" onclick="checkCode()" value="获取验证码" /></td>
                        </tr>
                        <tr>
                            <th>密码：</th>
                            <td>
                                <input name="userPassword" type="password" maxlength="30" id="userPassword" class="InputWidth270" PlaceHolder="设置登录密码" /></td>
                        </tr>
                        
                        <tr>
                            <td colspan="2">
                                <span id="lblNote" style="color: gray; display: none;">验证码已发出，请注意查收短信。如果再<span style="color: Red;" id="spTime">120</span>秒内没有收到验证码，请重新获取</span>

                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td>
                                <input type="button" name="btnRegister" value="立即注册" onclick="return Validate();" id="btnRegister" class="btnRed" class="btnRed" /></td>
                        </tr>
                    </table>
                </div>
            </div>
            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>
        <script src="/theme/ec1/js/JQuery.js" type="text/javascript"></script>
        <!--<script src="/theme/ec1/js/com.js"></script>-->
        <script type="text/javascript">
function getRootPath(){
	var CurWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = CurWwwPath.indexOf(pathName);
	var localhostPath = CurWwwPath.substring(0,pos);
	return(localhostPath);
}

            function Validate() {
                var username = $("#username");
                var smsRegisterSign = $("#smsRegisterSign");
                var userPassword = $("#userPassword");
                
                if (smsRegisterSign.val() == "") {
                    alert("请输入短信验证码");
                    return false;
                }
                if (userPassword.val() == "") {
                    alert("请输入登录密码");
                    return false;
                }
				
                $.ajax({
                	type:"POST",
                	url:"/user.json",
                	data:{username:username.val(),smsRegisterSign:smsRegisterSign.val(),userPassword:userPassword.val()},
                	async:true,
                	success:function(data){
                	if(data.message.operateCode == 102008){
                		alert('注册成功');
                		//console.log(data.redirectUrl)
                		location.href=getRootPath()+"/"+unescape(data.redirectUrl);
                	}else{
                		alert(data.message.message);
                		//location.href = getRootPath();
                	}
                	}
                });
                return true;
            }

            function checkCode() {
                var mobile = $("#username").val();
                if (!mobile) {
                    alert("请输入手机号");
                    return false;
                }
                if (!/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/.test(mobile)) {
                    alert("手机格式错误");
                    return false;
                }

            var extCode = $("#txtExtCode").val();
             if (!extCode) {                alert("请输入页面验证码");
                 return false;             }

                var down;
                var time = 120;
              $.ajax({
             		type:"POST",
                    url: "/user/registerByPhone/"+mobile+".json",//../MSite/Service/AjaxSendCodeService.aspx", 
                    data: "doType=sendextcode&mobile=" + mobile + "&IsLoginAccount=true&extCode=" + extCode,
                    cache: false,
                    dataType: "json",
                    async: false,
                    success: function (data) {
                    	//alert(JSON.stringify(data));
                    	//alert(data.message.operateCode)
                    	//alert(data.HasError); // undefind
                    	//alert(data.Message);// undefind
                    	//alert(data);
                        if (data.HasError) {
                            reLoadExtCode();
                            alert(data.Message);
                           return;
                        }

                        down = setInterval(function () {
                            DownTime();
                        }, 1000);
                       $("#lblNote").show();
                       
                    },
                   
                });
            
            	

                function DownTime() {
                    time = time - 1;
                    if (parseInt(time) < 0) {
                        clearInterval(down);
                        time = 120;
                        $("#spTime").text("120");
                        $("#btnSendExtCode").removeAttr('disabled');
                        $("#btnSendExtCode").removeClass().addClass("btnExtCode");
                        $("#lblNote").hide();
                    } else {
                        $("#spTime").html(time);
                        $("#btnSendExtCode").attr('disabled', 'true');
                        $("#btnSendExtCode").removeClass().addClass("btnGray");
                        $("#lblNote").show();
                    }
                }
                return false;
            }

            function reLoadExtCode() {
                var ran = Math.round((Math.random()) * 100000000);
                $("#imgCode").attr("src", "/captcha?ran=" + ran);
            }
        </script>
    </form>
</body>
</html>
