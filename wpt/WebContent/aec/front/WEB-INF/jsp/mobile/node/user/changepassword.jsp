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
<title>${systemName}</title>
<!--<link rel="stylesheet/less" href="/style/mobile/common.less" />
<script src="/js/less.min.js"></script>-->
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/changepassword.css" rel="stylesheet" type="text/css"> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/updatepassword.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>

<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>设置</span>
  </div>
<!-- <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %> -->
<div class="login">

   <!--    <h2>修改密码</h2> -->
  <form  method="POST"  name="member_upForm" id="member_upForm"   onsubmit="return false;">
        <div class="lomex">                 
           <p> <input type="text" placeholder="请输入您的手机号" id="username" name="username"></p>
           
               <p><input type="password" placeholder="请输入原密码" id="oldPassword" name="oldPassword" ></p>
              
              <p><input type="password" placeholder="设置新密码" id="newPassword" name="newPassword" ></p>
              
              <p><input type="password" placeholder="确认新密码" id="newPassword2" name="newPassword2" ></p>
          
           
                   
    
        </div>
        
       <div class="btn_submit"><input type="submit" id="login" class="bluebutton"  value="保  存"></div>
       </form>
    </div>

</body>
</html>