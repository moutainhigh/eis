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

    <title>${systemName}登录</title>

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
	<script src="/theme/${theme}/js/jquery.md5.js" type="text/javascript"></script>
  </head>

  <body>
	<script language="javascript" type="text/javascript">
    
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
		
		var encCaptcha = $('#captchaWord').val();
		if(encCaptcha == null){
			alert("请输入验证码");
			return false;
		}
		encCaptcha=encCaptcha.toLowerCase(); 
		encCaptcha = $.md5(encCaptcha);
		//console.log(encCaptcha);
		$('#captchaWord').val(encCaptcha);
		
    }
	 function changeImg(){

		document.getElementById("identifyCode").src="/captcha.shtml?"+Math.random();

	}
	
    </script>
    <div class="container">

      <form id="loginForm" name="loginForm" class="form-signin" method="POST" action="/user/login.shtml" onSubmit="return test();">
        <p class="guanliname">${systemName}管理中心</p>
        <h2 class="form-signin-heading">登&nbsp;&nbsp;&nbsp;&nbsp;录</h2>
        <c:if test="${message != null}">
        	<div class="error-message">${message.message}[${message.operateCode}]</div>
        </c:if>
        <label for="inputEmail" class="sr-only">用户名</label>
        <input id="username" name="username" value="${userRememberName}" type="text"  class="form-control" placeholder="我的用户名" required autofocus>
        <label for="inputPassword" class="sr-only">密码</label>
        <input id="userpassword"  type="password" id="inputpass" class="form-control" placeholder="我的密码" >
		<input id="userPassword" name="userPassword" type="hidden" id="userPassword" class="form-control" placeholder="我的密码" required>
		<c:if test="${needCaptcha == 'true' }">
			<img src="/captcha.shtml" id="identifyCode" style="float:left;"/><a href="javascript:changeImg();" style="color: #337ab7;font-size: 12px;text-decoration: none;padding-top: 40px;padding-left: 30px;display: block;float: left;">看不清，换一张</a>
			<div style="clear:both;"></div>
			<div>
        	<input id="captchaWord" name="captchaWord"  type="text"  class="form-control" placeholder="请输入图片中的数字和字母" required autofocus>
        	</div>
        </c:if>
        <div class="checkbox">
          <label>
            <input type="checkbox" id="rememberMe" name="rememberMe" <c:if test="${rememberMe == 'true'}"> checked </c:if> value="true">记住帐号
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit" >登  录</button>
      </form>
	<p align="center" style=" margin-top: 10px;color: #969696;">请使用IE11以上或Firefox浏览器。</p>
    </div> <!-- /container -->

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    
  </body>
</html>
