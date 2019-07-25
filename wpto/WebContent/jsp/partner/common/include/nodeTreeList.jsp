<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>
	
	<span class="parentNode folder" style="width:100%;">
		<i class="fa blue fa-folder-open-o"></i>
		<input class="hehe" type="hidden"/>
		<c:out value="${requestScope.parentNode.name}"></c:out>
		
		<span id="xiangqing" style="position:relative; float:right; width:88%;">
		<span style="width:22%;">${requestScope.parentNode.path}(${requestScope.parentNode.nodeId})</span>
		<span style="width:32%;">${requestScope.parentNode.templateLocation}(${requestScope.parentNode.templateId})</span>
		<span style="width:22%;"><spring:message code="NodeProcessor.${requestScope.parentNode.processClass}" /></span>
		<span style="width:10%;"><spring:message code="Status.${requestScope.parentNode.currentStatus}" text="${requestScope.parentNode.currentStatus}" /></span>
		
		
		<span class="tools" style="width:10%; cursor: pointer;" ><img src="/theme/basic/images/tools.png"></span>
		<c:if test="${!empty isShow}">
		<ul style=" position: absolute; width:100px; left:90%; margin-left: -21px;top:20px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
			<a href="#" onclick="addNode(${requestScope.parentNode.nodeId})" ><li class="materialAdd" style="background:white">增加子栏目</li></a>
			<a href="#" onclick="delNode(${requestScope.parentNode.nodeId})"><li class="materialDelete" style="background:white">删除</li></a>
			<!--<a href="#" onclick="staticNode(${requestScope.parentNode.nodeId})"><li class="materialStatic" style="background:white">静态化</li></a>-->
			<a href="#" onclick="editNode(${requestScope.parentNode.nodeId})"><li class="materialEdit" style="background:white">编辑</li></a>
		</ul>
	</c:if>
		</span>
	</span>
	
	
	<ul>
	<c:forEach var="childNode" items="${requestScope.parentNode.subNodeList}" varStatus="xh">
	
		<li>
		<c:choose>
		<c:when test="${!empty childNode.subNodeList}">
			<c:set var="parentNode" value="${childNode}" scope="request"/>
			<c:import url="/WEB-INF/jsp/common/include/nodeTreeList.jsp" />
		</c:when>
		<c:otherwise>
		
			<span class="subNode file" style="width:100%">
				<i class="fa blue fa-file-text-o"></i> 
					<input class="hehe" type="hidden"/>
					<c:out value="${childNode.name}"></c:out>
					
						<span id="xiangqing" style="position:relative;width:88%; float:right;">
						<span style="width:22%;">${childNode.path}(${childNode.nodeId})</span>
						<span style="width:32%;">${childNode.templateLocation}(${childNode.templateId})</span>
						<span style="width:22%;"><spring:message code="NodeProcessor.${childNode.processClass}" /></span>
						<span style="width:10%"><spring:message code="Status.${childNode.currentStatus}" text="${childNode.currentStatus}" /></span>
						
						<span class="tools" style="cursor: pointer;width:10%;" ><img src="/theme/basic/images/tools.png"></span>
						<ul style=" position: absolute; width:100px; left:90%; margin-left: -21px; top:20px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
							<a href="#" onclick="addNode(${childNode.nodeId})"><li class="materialAdd" style="background:white">增加子栏目</li></a>
							<li class="materialDelete" style="background:white" ahref="${childNode.nodeId}">删除</li>
							<!--<a href="#" onclick="staticNode(${childNode.nodeId})"><li class="materialStatic" style="background:white">静态化</li></a>-->
							<a href="#" onclick="editNode(${childNode.nodeId})"><li class="materialEdit" style="background:white">编辑</li></a>
						</ul>
					</span>
						
			</span>
		</c:otherwise>
		</c:choose>
		</li>
	</c:forEach>
	</ul>
