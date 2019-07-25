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
<link href="../../../theme/${theme}/css/mobile/register.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/footer.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.md5.js"></script>
<script src="../../../theme/${theme}/js/mobile/inputpassword.js"></script>
</head>

<body>
<div class="header" id="header">
     <a class="back" href="javascript:history.go(-1);"></a><span>完善资料</span><a class="list1" href="/"></a>
</div>
<div class="login">

      <h2>设置密码</h2>
  <form  method="POST"  name="member_regForm" id="member_regForm"   onsubmit="return false;">
        <div class="lomex">                 
           <p> <input type="password" placeholder="密码不能少于6位" id="userPassword" name="userPassword"></p>
            <input type="hidden" id="username" name="username" value="${username}">
         
            <div class="box_input"><input type="submit" id="login" class="btnzda writetext"  value="保存" name="login"></div>

        </div>
        </form>
      
    </div>

</body>
</html>