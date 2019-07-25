<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<script type="text/javascript">
function changeDialogTab(i){
	$('.dialogTab').css("display","none");
	$('.dialogMenu').css("background-color","#f5f5f5");
	$('#dialogTab' + i).css("display","block");
	$('#dialogMenu' + i).css("background-color","#fefefe");
	

}

/* 登录相关 */
var url = "/business.do?business=" + bizId;



</script>


<div  id="loginDialog" name="loginDialog" style="display:none; " title="您尚未登录，请先登录或注册">
	<p class="title" style="background-color:#eeeeee;">
		<a href="javascript:changeDialogTab(1)" id="dialogMenu1" class="dialogMenu"style="background-color:#fefefe;">登录易乐游</a>
		<a href="javascript:changeDialogTab(2)" id="dialogMenu2" class="dialogMenu"  >注册新帐号</a>
	</p>
	<div id="dialogTab1" class="dialogTab">
		<p id="loginResult" name="loginResult"></p>
		<form name="login" name="loginForm" id="loginForm" action="/passport.do?action=login" method="POST" onClick="return false;">
		<p><span class="inputName">用户名:</span><input type="text"   id="username"  name="username" value="${eis_username}" size="20"/></p>
		<p><span class="inputName">密 码:</span><input type="password"  id="userPassword" name="userPassword" size="20" /></p>
		<p class="rem"><span><a href="#">忘记密码</a></span><input type="checkbox" name="remember_username" <c:if test="${eis_username != null}"> checked</c:if>/> 记住账号</p>			
		<p class="submit"><input type="submit" id="loginButton" name="loginButton" value="登录" /></p>
		</form>	
	</div>
	<div id="dialogTab2" class="dialogTab" style="display:none">
		<form name="registerForm"  id="registerForm" action="/register.do?action=register" method="POST" onSubmit="return false">
		<p id="registerResult" name="registerResult"></p>
		<p>输入常用邮箱：<input type="text" name="username" id="regUsername" onBlur="onCheckAccount(this)"></p>
		<p><span>输入一个密码：</span> <input type="password" name="userPassword" id="regUserPassword"  value=""></p>		
		<p><input type="checkbox" checked="true" readonly="true" />我已阅读并同意：<a href="/about/188.html" target="_blank">易乐游用户协议</a></p>
		<p class="submit" style="text-align:center;border-top:1px dashed #ff6d00; margin-top:5px;"><input type="button" id="registerSubmit" name="registerSubmit" value="快速注册"/></p>
	</form>
	</div>
	
	
</div>

