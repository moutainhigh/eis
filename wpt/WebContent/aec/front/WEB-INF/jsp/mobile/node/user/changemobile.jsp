<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<title>手机号修改</title>
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/changemobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../../theme/${theme}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/jquery.form.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/changemobile.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>手机号修改</span>
  </div>
<!--  <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %> -->
<form  method="POST"  name="member_changeMoblieForm"  id="member_changeMoblieForm" onsubmit="return false;">
 
 <ul> 
  <li class="being">用户名(手机号)：${frontUser.username}</li>
 <input type="hidden"  id="uuid"  name="uuid" value="${frontUser.uuid} "/>
  <li><input type="text" placeholder="输入新手机号" class="new_mobile"  id="username"  name="username"  maxlength="11"  onkeydown="onlyNum();"/></li>
  
  <li><input type="text" placeholder="输入手机验证码" class="indentifycode" id="phoneBindSign" name="phoneBindSign"  maxlength="6"/>
  
  <input id="btnSendCode" type="button" class="btn_get" style="color:#00c9ff;" value="点击获取" onclick="sendMessage()" />
     </li>
    
 </ul>
  <div class="btn_submit"><input type="submit" name="tijiao" value="保存" class="bluebutton"/></div>
 </form>
</body>
</html>