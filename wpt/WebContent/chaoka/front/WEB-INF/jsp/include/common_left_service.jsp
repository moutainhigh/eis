<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!--service_center-->
<div class="clr"></div>
<div class="leftbar_bot1"></div>
<div class="clr4"></div>          
<div class="service_center fl">
	<div class="service_center_tit"><span class="leftbar_item_tit"></span></div>
	<div class="clr"></div>
	<div class="service_center_body fl">
		<div class="clr line"></div>
		<div class="service_cont">
			<ul>
			<li>在线客服：<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1158353505&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1158353505:41 &r=0.32167142592253184" alt="易乐游为您服务" title="易乐游为您服务"></a></li>
			<li>服务热线：010-58674566</li>
			<li>客服邮箱：<a href="mailto:service@yeele.cn">service@yeele.cn</a></li>
			</ul>
		</div>
		<div class="clr line"></div>
		<div class="faq fl">
			<ul>
			<li class="fl tit"><span class="fr"><a href="/service" target="_blank"></a></span><b class="fl">常见问题：</b></li>
			<c:forEach var="document" items="${faqList}">
			<li><a href="/${document.defaultNode.alias}/${document.udid}.html" target="_blank">${fn:substring(document.title ,0,14)}${fn:length(document.title)>14?"...":""}</a>
			</c:forEach>
			</ul>
		</div>
	</div>
</div>
<div class="clr"></div>