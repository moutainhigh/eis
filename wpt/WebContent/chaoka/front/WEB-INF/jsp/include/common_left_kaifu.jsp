<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<div class="new_game_server fl">
	<div class="new_game_server_tit" style="border-top:none;"><span class="leftbar_item_tit"></span></div>
        <div class="clr"></div>
        <div class="new_game_server_body">
                <ul class="open_service_info_list">
	               	<c:forEach var="document" items="${kaifuList}">
				<li><a href="/${document.defaultNode.alias}/${document.udid}.html" class="link" target="_blank">
					<span class="open_service_info_list_date" <c:if test="${document.currentStatus == 130005}">style="color:#FF3D00"</c:if>><fmt:formatDate value="${document.validTime}" pattern="MM/dd HH:mm"/></span> 
	         			<span class="open_service_info_list_name" <c:if test="${document.currentStatus == 130005}">style="color:#FF3D00"</c:if>>${fn:substring(document.title ,0,12)}${fn:length(document.title)>12?"...":""}</span> 
				</a></li>
	               	</c:forEach>
                 </ul>
        </div>
</div>
			<div class="clr"></div>
			<div class="leftbar_bot1"></div>
<div class="clr4"></div>