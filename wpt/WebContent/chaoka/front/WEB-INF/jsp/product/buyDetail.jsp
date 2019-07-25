<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html>
<html>
<HEAD>
<TITLE>${systemName}</TITLE>
    <meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
<title>炒卡网充值| ${systemName }</title>
</head>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/commonui.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>
<script  src="/js/ui/buydetail.js"></script>

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
    <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - ${product.productName }</span></div>
    <div class="productbody">
      <div class="productwelcome">${product.productName }</div>
      <div class="clear"></div>
      <div class="productmain">
      
<form id="buyForm" action="" method="POST" class="form-horizontal" >
          <div id="productServerListJson" style="display:none">${regionJson}</div>
  <div class="form-group">
    <label for="productAccountName" class="leftspan">帐号:</label>
   <input id="productAccountName" type="text" class="form-control" value="" style="width:200px;" placeholder="请输入账号" name="productAccountName">
  </div>
  <div class="form-group">
    <label for="productRegion" class="leftspan">充值区域:</label>
    <select name="productRegion" id="productRegion" class="form-control" style="width:250px;">
                <option value="0" selected="true">请选择</option>
                <c:forEach var="data" items="${product.regionMap}">
                  <option value="${data.key}">${data.value.regionName}</option>
                </c:forEach>
              </select>
  </div>
  <div class="form-group" style="display:none" id="serverChoose">
    <label for="productServer" class="leftspan">充值服务器:</label>
     <select name="productServer" id="productServer" class="form-control" style="width:250px;">
      </select>
  </div>
   <div class="form-group">
    <label for="labelMoney" class="leftspan">价格:</label>
    <div style="float:left; margin-top:10px 0 0 10px;">
       <c:forEach var="data" items="${subProductMap}">
            <div >
  <label>
    <input type="radio" name="labelMoney" id="labelMoney" value="${data.key}"  alt="${data.value.buyMoney}">
   面值：<label style="text-decoration:line-through">${data.value.labelMoney}</label>元&nbsp;&nbsp;购买价：${data.value.buyMoney}元&nbsp;&nbsp;折扣：${data.value.rate}
  </label>
</div>
       </c:forEach> 
       </div>   
       <div class="clear"></div> 
  </div>
  
    <div class="form-group">
    <label for="availableCount" class="leftspan">当前可购买数量:</label>
    <span id="shuliang"  style="height:30px; line-height:30px">${product.availableCount }</span>
  </div>
  

   <div style="margin:0 250px"><a onclick="javascript:productBuy('${product.productCode}');" class="btn btn-primary" role="button" href="javascript:void(0);">购买</a></div>
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