
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

    <script type="text/javascript" src="/theme/ec1/js/JQuery.js"></script>
    <script type="text/javascript">
    function getRootPath(){
	var CurWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = CurWwwPath.indexOf(pathName);
	var localhostPath = CurWwwPath.substring(0,pos);
	return(localhostPath);
}
        function reLoadExtCode() {

            var ran = Math.round((Math.random()) * 100000000);
            $("#imgCode").attr("src", "/captcha?ran=" + ran);
        }
        function CheckInput() {

            var username = $("#username");
            var userPassword = $("#userPassword");

            var extCode = $("#txtExtCode").val();
            if (username.val() == "") {
                alert("请输入用户名");
                return false;
            }
            if (userPassword.val() == "") {
                alert("请输入登录密码");
                return false;
            }

            if (!extCode) {
                alert("请输入附加码");
                return false;
            }
			
			$.ajax({
                	type:"POST",
                	url:"/user/login.json",
                	data:{username:username.val(),userPassword:userPassword.val()},
                	async:true,
                	success:function(data){
                	 console.log(JSON.stringify(data));
                		//alert(data.redirectUrl);
                	if(data.message.operateCode == 102008){
                        console.log(data);
                		alert('登录成功');
                		// location.href=unescape(data.redirectUrl);
                        window.location.href = "/";
                	}else{
                		alert(data.message.message);
                		//location.href = getRootPath();
                	}
                	}
                });
               
            return true;
        }

    </script>
</head>
<body>
    <!--<form name="form1" method="post" action="/user.json" id="form1">-->
<div>
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwULLTEzMTU5NzgxNDRkZOrmPw4lojy2q7s0OH2EZE/t6ycy" />
</div>

<div>

	<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="75ABFA0E" />
</div>
        <div class="Top">
            <div class="Top_Left">
                <a href="/content/user/login.shtml" class="Logo"></a><span class="Top_Title">欢迎登录</span>
            </div>
        </div>
        <div class="Wrap1000">
            <div class="InputWrap">
                <span class="InputWrap_Tip"><a href="/content/user/register.shtml">立即注册</a></span>
                <div class="InputForm">
                    <table>
                        <tr>
                            <th>账号：</th>
                            <td>
                                <input name="username" type="text" id="username" placeholder="用户名/手机号" class="InputWidth270" maxlength="11" /></td>
                        </tr>
                        <tr>
                            <th>密码：</th>
                            <td>
                                <input name="userPassword" type="password" id="userPassword" placeholder="请输入密码" class="InputWidth270" />&nbsp;&nbsp;&nbsp;
                                <a class="ForgetPwd" href="FindBackPwd.aspx">忘记密码&nbsp;？</a></td>
                        </tr>
                        <tr>
                            <th>附加码：</th>
                            <td>
                                <input name="txtExtCode" type="text" id="txtExtCode" placeholder="输入右侧附加码" class="InputWidth180" maxlength="6" />
                                <img id="imgCode" class="imgExtCode" onclick="reLoadExtCode()" src="/captcha" align="absmiddle" style="height:35px;width:60px;border-width:0px;" /></td>
                        </tr>
                        <tr>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <th></th>
                            <td>
                                <input type="submit" name="btnSubmit" value="登录" onclick="return CheckInput();" id="btnSubmit" class="btnRed" />
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>
   <!-- </form>-->
</body>
</html>
