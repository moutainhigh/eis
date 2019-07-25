<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!--  common header nav, top include of page -->


<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>
<link rel="stylesheet" href="/style/boxy.css" type="text/css" />  
<!--  common header nav, top include of page -->

<DIV class=wrapper>
<DIV class=left1><A href="/" target=_top><IMG align=left src="/style/logo.gif" width=190 height=54></A></DIV>
<DIV class=topleft2>
<DIV class=mainNav_bg>
<UL class=mainNav>
  <LI><A id=header_HyperLink <c:choose> <c:when test='${""==node.alias}'>class=current</c:when></c:choose> href="/" target=_top>首页</A> </LI>
  <LI><A   id=header_HyperLink2 href="javascript:void(0);"  target=_top>登录</A></LI>
    <LI><A id=header_HyperLink3 href="/${contentPrefix}user/register${pageSuffix}" target=_top>实名注册</A></LI>
  <LI><A id=header_HyperLink4 <c:choose> <c:when test='${"product"==node.alias}'>class=current</c:when></c:choose>  href="/${contentPrefix}product/index${pageSuffix}" target=_top>产品大全</A></LI>
  <LI><A id="header_HyperLink5" <c:choose> <c:when test='${"pay"==node.alias}'>class=current</c:when></c:choose> href="/${contentPrefix}pay/index${pageSuffix}" target=_top>充值中心</A></LI>
   <LI><A id=header_HyperLink6 href="#" target=_top>家长监控</A></LI>
 


</UL></DIV></DIV>
<DIV class=topleft3>
<!--<span id=Lottery_apDiv_wt><IMG 
style="BORDER-RIGHT-WIDTH: 0px; WIDTH: 166px; BORDER-TOP-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; HEIGHT: 25px; BORDER-LEFT-WIDTH: 0px" 
align=left src="/style/apDiv_kf.gif"></span>-->
<!--<SPAN 
class=topheaderR><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=1158353505&amp;site=qq&amp;menu=yes"><img src="http://wpa.qq.com/pa?p=2:1158353505:41 &amp;r=0.32167142592253184" alt="炒卡网客服为您服务" title="炒卡网客服为您服务" border="0"></a></SPAN>--> 
</DIV></DIV>
<DIV class=cf></DIV>
<DIV class=wrapper_search>
<DIV id=ShowCategoryTree class=wrapper_searchleft1>

<DIV id=ZfMenu></DIV>
<!-- <DIV class=CategoryTree></DIV> 
<DIV id=apDiv2><IMG src="/style/here.gif" 
width=114 height=41></DIV>-->
</DIV>
<DIV class=wrapper_searchleft2>
<DIV class=wrapper_searchDL></DIV>
<DIV class=wrapper_searchDM><INPUT 
onkeydown="" id=header_txtSearchKey 
name=header$txtSearchKey> </DIV>
<DIV class=wrapper_searchDR><INPUT 
style="BORDER-RIGHT-WIDTH: 0px; WIDTH: 87px; BORDER-TOP-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; HEIGHT: 33px; BORDER-LEFT-WIDTH: 0px" 
id=header_btnSearch src="/style/index_29.gif" 
type=image name=header$btnSearch></DIV></DIV>
<DIV class=wrapper_searchleft3>
<DIV style="PADDING-LEFT: 10px">
<UL >
<c:forEach var="game" items="${allProductList}" end="3">
  <LI><A 
  href="/product/${game.productCode}/index${pageSuffix}" 
  target=_blank><FONT style="font-size:12px">${fn:substring(game.productName ,0,10)}</FONT></A></LI>
 </c:forEach></UL></DIV></DIV></DIV>