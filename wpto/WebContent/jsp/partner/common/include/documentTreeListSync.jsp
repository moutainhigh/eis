<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>


	<span class="parentNode folder"><i class="fa blue fa-folder-open-o"></i> <input type="checkbox" class="parent" name="nodeId" value="${requestScope.parentNodeSync.nodeId}" /> <c:out value="${requestScope.parentNodeSync.name}"></c:out></span>	
	<ul>
	<c:forEach var="childNodeSync" items="${requestScope.parentNodeSync.subNodeList}">
		<li>
		<c:choose>
		<c:when test="${!empty childNodeSync.subNodeList}">			
			<c:set var="parentNodeSync" value="${childNodeSync}" scope="request"/>
			<c:import url="/WEB-INF/jsp/common/include/documentTreeListSync.jsp" />
		</c:when>
		<c:otherwise>
			<span class="subNode file"><i class="fa blue fa-file-text-o"></i> 
			<input class="check subNodeId" type="checkbox" name="nodeId" value="${childNodeSync.nodeId}" 
			<c:forEach var="related" items="${document.relatedNodeList}">
				<c:if test="${related.nodeId == childNodeSync.nodeId && related.currentStatus == 100001 }"> checked="checked" </c:if>
				<c:if test="${document.defaultNode.nodeId == childNodeSync.nodeId}"> disabled=true </c:if>
			</c:forEach>
			/> <c:out value="${childNodeSync.name}"></c:out></span>
		</c:otherwise>
		</c:choose>
		</li>
	</c:forEach>
	</ul>