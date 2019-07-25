<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${systemName}管理中心-修改密码</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/user_login.css" rel="stylesheet">
	<link href="/theme/${theme}/style/footer.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <script src="/theme/${theme}/js/security.js" type="text/javascript"></script>  
    <script src="/theme/${theme}/js/jquery-1.11.3.min.js" type="text/javascript"></script>  
     <script src="/theme/${theme}/js/jquery.form.js" type="text/javascript"></script> 
    <script src="/theme/${theme}/js/jquery.form_ie8.js" type="text/javascript"></script>
  </head>

  <body>
	<script language="javascript" type="text/javascript">
    
	function check(){
        var newPass1 = $('#userpassword').val();
		var newPass2 = $('#userpassword2').val();
		var oldPass = $('#oldPassword').val();
		if(newPass1 != newPass2){
			alert("两次输入的新密码不一致，请重新输入");
			return false;
		}
    	var exponent = "${publicKeyExponent}";
    	var modulus = "${publicKeyModulus}";
    	if(exponent == null || modulus == null){
    		alert("系统数据异常，无法初始化安全控件!");
    		return;
    	}
    	RSAUtils.setMaxDigits(200);  
        var key = new RSAUtils.getKeyPair(exponent, "", modulus);  
        var encrypedPwd = RSAUtils.encryptedString(key, newPass1); 
        var encrypedPwd2 = RSAUtils.encryptedString(key, newPass2); 
        var encryptedOldPwd =  RSAUtils.encryptedString(key, oldPass); 
        //$('loginForm').attr('userPassword', encryptedPwd);
		$('#userPassword').val(encrypedPwd);    	
		$('#userPassword2').val(encrypedPwd2);    	
		$('#oldPassword').val(encryptedOldPwd);    	
   }
    </script>
    <div class="container">

      <form id="loginForm" name="loginForm" class="form-signin" method="POST" action="/user/update/userPassword.shtml" onSubmit="return check();">
        <h2 class="form-signin-heading">${systemName}管理中心-修改密码</h2>
        <c:if test="${message != null}">
        	<div class="error-message">${message.message}[${message.operateCode}]</div>
        </c:if>
        <label for="inputEmail" class="sr-only">原密码</label>
        <input id="oldPassword" name="oldPassword" value="" type="password"  class="form-control" placeholder="原密码" required autofocus>
        <label for="inputPassword" class="sr-only">新密码</label>
        <input id="userpassword"  type="password" id="inputpass" class="form-control" placeholder="新密码" >
		<label for="inputPassword" class="sr-only">再次输入新密码</label>
        <input id="userpassword2"  type="password" class="form-control" placeholder="再次输入新密码" >
        
        <input id="userPassword" name="userPassword" type="hidden">
        <input id="userPassword2" name="userPassword2" type="hidden">
        
        <button class="btn btn-lg btn-primary btn-block" type="submit" >修改</button>
      </form>
	<p align="center">请使用IE11以上版本的浏览器。</p>
    </div> <!-- /container -->

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    
  </body>
</html>
