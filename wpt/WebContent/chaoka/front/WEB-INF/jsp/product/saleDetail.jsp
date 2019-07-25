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
<LINK rel=stylesheet type=text/css href="/style/Web.css">
<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<LINK rel=stylesheet type=text/css href="/style/list.css">
<LINK rel=stylesheet type=text/css href="/style/Footer.css">
<title>点卡寄售 | ${systemName }</title>
</head>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/commonui.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>
<script  src="/js/ui/saledetail.js"></script>
<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">

<body>
<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
<div id="mainbody">
  <div class="leftbar" style="height:auto">
    <%@include file="/WEB-INF/jsp/include/common_login.jsp"%>
    <%@include file="/WEB-INF/jsp/include/common_reinfo.jsp"%>
  </div>
  <div class="rightbar" >
    <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 寄售卡密 - ${product.productName }</span></div>
    <div class="productbody">
      <div class="productwelcome">${product.productName }</div>
      <div class="clear"></div>
      <div class="productmain">
        <form id="buyForm" action="" method="POST" >
          <ul>
         	<li><span class="leftspan">面值:</span><span style="color:#ff7a17;font-size:14px; font-weight:bold;text-decoration:line-through">${product.labelMoney }</span></li>
            <li><span class="leftspan">期望出售价格:</span><span style="color:#ff7a17; font-size:14px; font-weight:bold" class="currentprice">${product.buyMoney }</span></li>
            <li><span class="leftspan">最低出售价格:</span><span style="color:#ff7a17; font-size:14px; font-weight:bold" class="currentprice">              
            	<input id="minBuyMoney" type="text" value="${product.buyMoney }" name="minBuyMoney">
                <input id="buyMoney" type="hidden" value="${product.buyMoney }" name="buyMoney">
            </span>如果不能以高于或等于此价格成交，交易将失败，您的卡密不会被使用</li>
            <li class=""> <span class="leftspan">卡号:</span>
              <input id="productSerialNumber" type="text" value="" name="productSerialNumber">
            </li>
            <li class=""> <span class="leftspan">卡密:</span>
              <input id="productPassword" type="text" value="" name="productPassword">
            </li>
            
            
          </ul>
          <div style="margin:0 250px"><a onclick="javascript:productSale('${product.productCode}');" class="buySubmitButton" href="javascript:void(0);">购买</a></div>
        </form>
      </div>
    </div>
    <div class="clear"></div>
  </div>
  <div class="clear"></div>
</div>
<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>