<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<TITLE>${systemName}</TITLE>
<meta http-equiv="x-ua-compatible" content="ie=7">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name=description 
content=炒卡网主营游戏充值,话费充值,影音娱乐,杀毒软件,软件序列号，在线教育，影票兑换码等产品。，高效快捷，实时充值,安全放心。7*24小时为您提供全面的支付服务。>
<META name=keywords 
content=炒卡网,jcard,炒卡网充值,炒卡网官网,充值,官网,qq卡,q币充值中心,qq充值中心,qq蓝钻,蓝钻豪华版,q币查询,点卡充值,游戏点卡,魔兽世界点卡,魔兽世界,大话西游3,剑网三,九阴真经,神仙传,神仙道,人人豆,>
<LINK rel="Shortcut Icon" href="/favicon.ico">
<LINK rel=Bookmark href="/favicon.ico">
<LINK rel=Stylesheet type=text/css href="/style/UCardTop.css">
<LINK rel=Stylesheet type=text/css href="/style/CarouselAD.css">
<LINK rel=stylesheet type=text/css href="/style/OldUcard.css">
<LINK rel=stylesheet type=text/css href="/style/Web.css">
<LINK rel=stylesheet type=text/css href="/style/list.css">
<LINK rel=stylesheet type=text/css href="/style/Footer.css">
<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<title>账户资料管家</title>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/editpassword.js"></script>
<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<script  src="/js/ui/commonui.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">
</head>

<body>
<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
<div id="mainbody">
  <div class="leftbar">
    <%@include file="/WEB-INF/jsp/include/common_login.jsp"%>
    <%@include file="/WEB-INF/jsp/include/common_info.jsp"%>
  </div>
  <div class="rightbar">
    <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 我的账户</span></div>
    <div id="usernamezh" style="display:block;margin:50px 5px 5px 50px; font-size:14px;color:#ff6d00;"></div>
    <div id="aqgj">
    <form  method="POST" action="/user/update/security.json" id="securityEdit" name="securityEdit">
      <input type="hidden" id="uuid" value="" name="uuid" >
       <input type="hidden" size="40"  value="" id="username" name="username">
 
     
        <p style="display:block;margin:0 5px 0 30px; font-size:14px;color:#ff6d00; display:inline">在这里修改我的密码和其他安全信息</p>
        <div class="clear"></div>
      <p style="display:block;margin:0 5px 0 30px; font-size:14px;color:#ff6d00; display:inline">如需修改其他资料，请前往<a href="/user/edit/data">账号资料管家</a></p>
      <div class="clear"></div>
      <p style="overflow:hidden"><span class="txtedit">旧密码：</span>
        <span class="inedit"><input type="password" size="40" value="" id="oldPassword" name="oldPassword"></span>
        <span class="notifyedit"></span></p>
           <div class="clear"></div>
      <p style="overflow:hidden"><span class="txtedit">新的密码：</span>
       <span class="inedit"><input type="password" size="40" value="" id="newPassword" name="newPassword" ></span><span class="notifyedit"></span>
      </p>
         <div class="clear"></div>
      <p><span class="txtedit">再次输入新密码：</span>
        <span class="inedit"><input type="password" size="40" value="" id="newPassword2" name="newPassword2" ></span><span class="notifyedit"></span>
      </p>
             <div class="clear"></div>
      <p style="margin-left:200px"><input type="submit"  id="denglu"  value="修改" /></p>
    </form>
    </div>
  </div>
</div>
<div class="clear"></div>
<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>
