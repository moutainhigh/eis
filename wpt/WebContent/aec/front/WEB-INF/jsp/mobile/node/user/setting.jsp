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
<title>设置</title>
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/iconfont.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/setting.css" rel="stylesheet" type="text/css">
<style type="text/css">
	*{
		font-size: 16px;
	}
  .avatar-img{
    width:100%;
    height: 100%;
    border-radius:100%;
  }
</style>
</head>

<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>设置</span><!--<a class="home" href="/">-->
 </div>
<!--  <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %>  -->
 <ul>
 	
   <a href="/content/user/changephoto.shtml"><li><div class="box_left">头像</div><div id="head_icon_con" style="width: 50px;height:50px;">
        <c:choose>
            <c:when test="${empty frontUser.userConfigMap.userHeadPic.dataValue}">
                <img data-original="#" src="../../../theme/${theme}/images/touxiang.png" class="avatar-img" >
            </c:when>
            <c:otherwise>
                <img data-original="#" src="/file/client/${frontUser.userConfigMap.userHeadPic.dataValue}" class="avatar-img" >
            </c:otherwise>
        </c:choose>
    </div></li></a>
   <a href="changenickname.shtml"><li><div class="box_left">昵称</div><div class="box_right"><span class="btn_title">${frontUser.nickName}</span><i class="icon iconfont icon-miny"></i></div></li></a>
   <a href="/content/user/changemobile.shtml"><li><div class="box_left">用户名(手机号)</div><div class="box_right"><span class="btn_title">${frontUser.username}</span><i class="icon iconfont icon-miny"></i></div></li></a>
   <a href="/content/user/changepassword.shtml"><li><div class="box_left">密码</div><div class="box_right"><i class="icon iconfont icon-miny"></i></div></li></a>
   <a href="/addressBook/index.shtml"><li><div class="box_left">收货地址</div><div class="box_right"><i class="icon iconfont icon-miny"></i></div></li></a>
   <a href="/content/user/message.shtml"><li><div class="box_left">给我留言</div><div class="box_right"><i class="icon iconfont icon-miny"></i></div></li></a>
 </ul>
 <div class="btn_container"><input type="button" value="退出登录" class="bluebutton" onClick="logout();"/></div>
</body>
</html>