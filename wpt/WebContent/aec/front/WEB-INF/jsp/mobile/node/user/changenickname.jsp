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
<title>昵称修改</title>
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/changenickname.css" rel="stylesheet" type="text/css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/updatenickname.min.js"></script> 
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>昵称修改</span>
  </div>
<!--  <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %> -->
 <form  method="POST"  name="member_upNickForm" id="member_upNickForm"   onsubmit="return false;">
 <ul>
 
  <li class="being">昵称：${frontUser.nickName} </li>
  <li>
     <input type="text" placeholder="输入新的昵称" class="new_name" id="nickName"  name="nickName"/>
   </li>
 </ul>
  <input type="hidden"  id="uuid"  name="uuid" value="${frontUser.uuid} " class="prompt_text"/>
 <div class="btn_submit"><input  type="submit" value="保存" class="bluebutton"/></div>
 </form>
</body>
</html>