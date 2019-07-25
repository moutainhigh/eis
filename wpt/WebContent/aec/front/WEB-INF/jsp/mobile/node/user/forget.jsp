<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>以先</title>
<!--<link rel="stylesheet/less" href="/style/mobile/common.less" />
<script src="/js/less.min.js"></script>-->
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/list.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/forgetpassword.js?v=161201"></script> 
<script  type="text/javascript" src="/js/mobile/lazyload.js"></script>
</head>

<body>
 <div class="header" id="header">
      <a class="back" href="javascript:history.go(-1);"></a><span>找回密码</span>
    </div>
<!-- <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %>
 --><div class="login">
  <form  method="POST"  name="member_forgetForm" id="member_forgetForm"   onsubmit="return false;">
        <div class="lomex">                 
           <p> <input type="text" placeholder="请输入您的手机号" id="username" name="username"></p>
           
              <p> <input type="text" placeholder="手机验证码" id="phoneBindSign" name="phoneBindSign" ><input id="btnSendCode" type="button" value="点击获取" onclick="sendMessage()" /></p>
              
              <p><input type="password" placeholder="设置新密码" id="userPassword" name="userPassword" ></p>
              
              <p><input type="password" placeholder="确认新密码" id="userPassword2" name="userPassword2" ></p>
          
            <input type="submit" id="login" class="bluebutton"  value="提  交">
     
    
        </div>
        </form>
      
    </div>

</body>
</html>