<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

<div class="register" id="registerDialog" name="registerDialog" style="display:none;" title="快速注册">
	<form name="registerForm"  id="registerForm" action="/register.do?action=register" method="POST" onSubmit="return false">
		<p id="registerResult" name="registerResult"></p>
		<p>输入常用邮箱：<input type="text" name="username" id="username" onBlur="onCheckAccount(this)"></p>
		<p><span>输入一个密码：</span> <input type="password" name="userPassword" id="userPassword"  value=""></p>
		<p class="submit" style="text-align:center;border-top:1px dashed #ff6d00; margin-top:5px;"><input type="button" id="registerSubmit" name="registerSubmit" value="快速注册"/></p>
	</form>
</div>
