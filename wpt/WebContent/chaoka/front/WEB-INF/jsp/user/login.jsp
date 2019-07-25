<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE>${systemName}</TITLE>
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
<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<LINK rel=stylesheet type=text/css href="/style/list.css">
<LINK rel=stylesheet type=text/css href="/style/Footer.css">
<title>用户登录 | ${systemName }</title>
</head>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">
<script  src="/js/ui/commonui.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>

<script  src="/js/ui/reg.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<body>

<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
<div id="mainbody">
          <div class="leftbar">
                <%@include file="/WEB-INF/jsp/include/common_reinfo.jsp"%>
        </div>   
              <div class="rightbar">
        
              
                <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 用户登录</span></div>
                    
               
                <div class="registerbody">
                    <!--<div class="welcomes">欢迎注册${systemName}，请在下面输入您的信息</div>-->
                         <div class="clear"></div>
             <ul class="tab">
    <li class="curr"><a href="">用户登录</a></li>
   
</ul><div class="clear"></div>


 <form  method="post" name="Useral" id="Useral">
<script>



document.onkeydown = function(e){ 
var ev = document.all ? window.event : e; 
if(ev.keyCode==13) { 
 loginuser();
} 
} 


</script>
<table width="500" height="200" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right">&nbsp;</td>
    <td><span class="notifyOk"  style=" font-size:12px; display:none"></span></td>
  </tr>
  <tr>
    <td width="42%" height="40" align="right" style=" font-size:12px">请输入您的${systemName}账号：</td>
    <td width="58%" height="40"><input type="text"  name="username" id="username" size="40"  style="height:25px; width:220px;" /></td>
  </tr>
  <tr>
    <td height="40" align="right"  style=" font-size:12px">输入您在${systemName}的密码：</td>
    <td height="40"><input type="password" name="userPassword" id="userPassword" size="40" style="height:25px;width:220px;" /></td>
  </tr>
  <tr>
    <td colspan="2" align="center"><span class="agree"  style=" font-size:12px">
      <input type="checkbox" id="userRememberName" name="userRememberName" checked="checked" />
请记住我的帐号</span>&nbsp;&nbsp;<span ><a href="/content/user/register.shtml" style="font-size:12px;color:#0070fe;">还没注册账号？</a></span></td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="left">
      <input type="button" id="loginres"  onclick="loginuser();" value="登录" style="	background:url(/style/bottonbg.gif) no-repeat;
	text-align:center;
	width:74px;
	height:25px;font-family:'微软雅黑', Verdana, sans-serif;
	line-height:27px; font-weight:bold; color:#FFFFFF;
	border:0px" />
   </td>
  </tr>
</table></form>
 </div>
    <div class="clear"></div>
    </div>
        <div class="clear"></div>
         </div>
        
 <%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>


