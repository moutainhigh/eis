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
<script  src="/js/ui/indexinfo.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script  src="/js/ui/commonui.js"></script>
<title>我的账户</title>
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
    <div id="usernamezh" style="display:block;margin:50px 5px 5px 68px;  font-size:14px;color:#ff6d00;"></div>
    <div id="dengji" style="margin:15px 5px 5px 68px;"></div>
    <ul id="accountinfo">
      <li>充值资金:
        <div id="chargeMoney2" style="display:inline; color:#ff6d00;"> ${money.chargeMoney}</div>
        元 </li>
      <li>收入资金:
        <div id="incomingMoney" style="display:inline; color:#ff6d00;"> ${money.incomingMoney}</div>
        元 </li>
      <li>冻结资金:
        <div id="frozenMoney" style="display:inline; color:#ff6d00;"> ${money.frozenMoney}</div>
        元 </li>
      <li>定金:
        <div id="marginMoney" style="display:inline; color:#ff6d00;"> ${money.marginMoney}</div>
        元 </li>
      <li>金币:
        <div id="coin" style="display:inline; color:#ff6d00;"> ${money.coin}</div>
        元 </li>
      <li>积分:
        <div id="point" style="display:inline; color:#ff6d00;"> ${money.point}</div>
        元 </li>
    </ul>
    <div class="clear"></div>
    <div  class="hengpai">
      <div class="mobile_list" style="display:none;color:#ff6d00;"><a href="/content/user/bindPhone.shtml" >您的手机未绑定，现在就绑定 </a></div>
      <div class="mobile_list2" style="display:none;color:#ff6d00;">您的手机已绑定</div>
       <div class="help" style="color:#ff6d00;">绑定手机有什么好处</div>
    </div>
    <div class="hengpai">
      <div class="email_list" style="display:none;color:#ff6d00;"><a href="/content/user/bindEmail.shtml" >您的邮箱未绑定，现在就绑定</a></div>
      <div class="email_list2" style="display:none;color:#ff6d00;">您的邮箱已绑定</div>
       <div class="help" style="color:#ff6d00;">绑定邮箱有什么好处</div>
    </div>
    <div class="clear"></div>
  </div>
  <div class="clear"></div>
</div>
<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>
