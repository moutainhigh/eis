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

    <title>${systemName}严格验证登录</title>

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
    /* function test(){
    	RSAUtils.setMaxDigits(200);  
        var key = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
        var oldPass = $('#userPassword').val();
        //alert(oldPass);
        var encrypedPwd = RSAUtils.encryptedString(key, oldPass); 
        //$('loginForm').attr('userPassword', encryptedPwd);
        console.log('crypt:' + encrypedPwd);
    	//document.form.submit();
		$.ajax({ 
		    type: "POST", 	
			url: "/user/login.json",
			data: {
				username: $("#username").val(), 
				userPassword: encrypedPwd, 
			},
			dataType: "json",
			success: function(data){
				if (data.message.operateCode==102008) { 
					alert("登录成功");
					window.location.href='/';
				} else {
					alert("出现错误：" + data.message.message);
					window.location.href=''
				}  
			},	
			error: function(data){
				alert(data);
			}
		});
    } */
	function test(){
    	var exponent = "${publicKeyExponent}";
    	var modulus = "${publicKeyModulus}";
    	if(exponent == null || modulus == null){
    		alert("系统数据异常，无法初始化安全控件!");
    		return;
    	}
    	RSAUtils.setMaxDigits(200);  
        var key = new RSAUtils.getKeyPair(exponent, "", modulus);  
    	
        var oldPass = $('#userpassword').val();
        //alert(oldPass);
        var encrypedPwd = RSAUtils.encryptedString(key, oldPass); 
        //$('loginForm').attr('userPassword', encryptedPwd);
		$('#userPassword').val(encrypedPwd);    	
    }
    </script>
    <div class="container">
		 <h2 style="font-size: 18px;text-align: center;line-height: 30px;">您当前想要访问的资源需要再次确认您的身份<br/>请在下面输入您的
		 <c:if test="${strictAuthType == 'payPassword'}">支付密码</c:if>
		 <c:if test="${strictAuthType == 'userPassword'}">登录密码</c:if>
		 
		 </h2>
      <form id="loginForm" name="loginForm" class="form-signin" method="POST" action="/user//secAuth/login.shtml" onSubmit="return test();">
		
        <c:if test="${message != null}">
        	<div class="error-message">${message.message}[${message.operateCode}]</div>
        </c:if>
        <label for="inputPassword" class="sr-only">密码</label>
        <input id="userpassword"  type="password" id="inputpass" class="form-control" placeholder="密码" >
		<input id="userPassword" name="userPassword" type="hidden" id="userPassword" class="form-control" placeholder="密码" required>
		<input id="data" name="data" type="hidden" value="${data}" >
		<input id="returnUrl" name="returnUrl" type="hidden" value="${returnUrl}" >
		
        <button class="btn btn-lg btn-primary btn-block" type="submit" >确认</button>
      </form>
	<p align="center" style='margin-top: 10px;color: #9E9E9E;'>请使用IE11以上版本的浏览器。</p>
    </div> <!-- /container -->

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    
  </body>
</html>
