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
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/ui/bindphone.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script  src="/js/ui/commonui.js"></script>
<title>手机绑定</title>
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
  <div id="usernamezh" style="display:block;margin:50px 5px 5px 68px;  font-size:14px;color:#ff6d00;"></div><div id="dengji" style="margin:15px 5px 5px 68px;"></div>
<div class="mobile_form">
                                <p style="margin-bottom: 0px;">
 手机号：
                                    <input type="text" maxlength="11" id="mobile" name="mobile">
                                    <input type="button" style="_margin-top:0"  value="免费获取手机校验码" id="btn" >
                                </p>
                                <p style="font-size: 12px;margin:0 0 0 60px; color:#d5d5d5" class="grey">请输入正确的11位手机号码</p>

                                <p>
                                    校验码：
                                    <input type="text" maxlength="6" id="confirm_code" name="confirm_code" class="default_value">
                                </p>
                                <p>
                                    <input type="button" name="bind" id="bind" class="submit_subscribe" value="提交手机绑定">
                                </p>

                            </div>
  <div class="mobile_form2" style="display:none">您的手机已绑定成功</div>                          
    <div class="clear"></div>
  </div>
  <div class="clear"></div>
</div>
<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>
