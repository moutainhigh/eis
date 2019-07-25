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
<title>账户资料管家-支付记录</title>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/buylist.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<script  src="/js/ui/commonui.js"></script>
<style> 
#paylists tr:hover{ 
background-color:#bad7ff; //使用CSS伪类达到鼠标移入行变色的效果，比Jquery 的mouseover,hover 好用 
} 
 .mouseOver{background:green;font-size:12px;height:25px;} 
 .aa{background-color:#e4effc; color:#000000;font-size:12px;height:25px;}
 .bb{background-color:fff;color:#000000;font-size:12px;height:25px;}
</style> 

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
     <div id="usernamezh" style="display:block;margin:50px 5px 5px 68px;  font-size:14px;color:#ff6d00;"></div><div id="dengji" style="margin:15px 5px 5px 68px;"></div>
    <ul id="accountinfo">
 
      <li>充值资金:
        <div id="chargeMoney2" style="display:inline; color:#ff6d00;"> ${money.chargeMoney}</div>元
      </li>
      <li>收入资金:
        <div id="incomingMoney" style="display:inline; color:#ff6d00;"> ${money.incomingMoney}</div>元
      </li>
      <li>冻结资金:
        <div id="frozenMoney" style="display:inline; color:#ff6d00;"> ${money.frozenMoney}</div>元
      </li>
      <li>定金:
        <div id="marginMoney" style="display:inline; color:#ff6d00;"> ${money.marginMoney}</div>元
      </li>
      <li>金币:
        <div id="coin" style="display:inline; color:#ff6d00;"> ${money.coin}</div>个
      </li>
      <li>积分:
        <div id="point" style="display:inline; color:#ff6d00;"> ${money.point}</div>分.
      </li>
    </ul>
    <div class="clear"></div>
    <div  style="display:block;margin:0px 5px 5px 68px;  font-size:12px;color:#ff6d00;">
    <ul class="tabmoney">
    <li class="line"><a href="/content/user/payList.shtml">最近支付记录</a></li>
    <li class="curr"><a href="/content/user/buyList.shtml">最近购买记录</a></li>
    </ul>
    <div class="clear"></div>
<table width="670" border="0" cellspacing="1" cellpadding="0" id="paylists">
  <tr style="color:#FFFFFF;  font-size:12px; height:25px;">
    <th nowrap bgcolor="#003399">购买订单号</td>
    <th nowrap bgcolor="#003399">购买的产品</td>
    <th nowrap bgcolor="#003399">下单时间</td>

 <th nowrap bgcolor="#003399">当前状态</td>
  </tr>
<div id="listres"></div>
</table>
<div id="pageres2"></div>


</div><br>
<br>
    </div>
  </div>
</div>
<div class="clear"></div>
<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>
