<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>


	<span class="parentNode folder"><i class="fa blue fa-folder-open-o"></i> <c:out value="${requestScope.parentNode.name}"></c:out></span>	
	<ul>
	<c:forEach var="childNode" items="${requestScope.parentNode.subNodeList}">
		<li>
		<c:choose>
		<c:when test="${!empty childNode.subNodeList}">			
			<c:set var="parentNode" value="${childNode}" scope="request"/>
			<c:import url="/WEB-INF/jsp/common/include/documentTreeList.jsp" />
		</c:when>
		<c:otherwise>
			<span class="subNode file"><i class="fa blue fa-file-text-o"></i> <input class="check" type="radio" name="defaultNodeId" value="${childNode.nodeId}" <c:if test="${childNode.nodeId == document.defaultNode.nodeId}"> checked="checked"</c:if> /> <c:out value="${childNode.name}"></c:out></span>
		</c:otherwise>
		</c:choose>
		</li>
	</c:forEach>
	</ul>