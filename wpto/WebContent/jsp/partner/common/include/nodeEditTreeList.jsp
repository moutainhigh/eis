<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>

	<span class="parentNode folder" style="width:100%; position:relative;">
		<i class="fa blue fa-folder-open-o"></i>
		<c:set var="check" value="false"/>
		<c:forEach var="includeNodeSet" items="${node.includeNodeSet}">
			<c:if test="${requestScope.parentNode.nodeId == includeNodeSet.nodeId}"> 
				<input type="checkbox" name="includeNodeSet.nodeId" value="${requestScope.parentNode.nodeId}" checked="checked"/>
				<c:set var="check" value="true"/>
			</c:if>
			
		</c:forEach>
		<c:if test="${check==false}">
			<input type="checkbox" name="includeNodeSet.nodeId" value="${requestScope.parentNode.nodeId}"/>
		</c:if>
		<c:out value="${requestScope.parentNode.name}"></c:out>
		
		<span class="contentTypeRows">显示环境:
			<select name="includeNodeSet.contentType">
				<option value="">请选择</option>
				<c:set var="select1" value="0" />
				<c:if test="${!empty node.includeNodeSet}">
					<c:forEach var="includeNodeSets" items="${node.includeNodeSet}">
						<c:if test="${requestScope.parentNode.nodeId == includeNodeSets.nodeId}"> 
							<c:forEach var="q" items="${contextType}">
								<c:if test="${includeNodeSets.contextType eq q }">
									<option selected="selected" value="${q}"><spring:message code="ContextType.${q}"/></option>
								</c:if>
								<c:if test="${!(includeNodeSets.contextType eq q)}">
								<option value="${q}"><spring:message code="ContextType.${q}"/></option>
								</c:if>
								<c:set var="select1" value="${includeNodeSets.nodeId}" />
							</c:forEach>
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${select1 != requestScope.parentNode.nodeId}">
					<c:forEach var="q" items="${contextType}">
						<option value="${q}"><spring:message code="ContextType.${q}"/></option>
					</c:forEach>
				</c:if>
			</select>
		</span>
		
		<span class="contentTypeRows">获取文章数量:
			<c:set var="rowsNum" value="0" />
			<c:if test="${!empty node.includeNodeSet}">
				<c:forEach var="includeNodeSet" items="${node.includeNodeSet}">
					<c:if test="${requestScope.parentNode.nodeId == includeNodeSet.nodeId}"> 
						<input name="includeNodeSet.rows" value="${includeNodeSet.rows}"/>
						<c:set var="rowsNum" value="${includeNodeSet.nodeId}"/>
					</c:if>
					
				</c:forEach>
			</c:if>
			<c:if test="${requestScope.parentNode.nodeId != rowsNum}">
				<input name="includeNodeSet.rows" />
			</c:if>
		</span>
	</span>	
	
	<ul>
		<c:forEach var="childNode" items="${requestScope.parentNode.subNodeList}">
			<li>
			<c:choose>
			<c:when test="${!empty childNode.subNodeList}">			
				<c:set var="parentNode" value="${childNode}" scope="request"/>
				<c:import url="/WEB-INF/jsp/common/include/nodeEditTreeList.jsp" />
			</c:when>
			<c:otherwise>
				<span class="subNode file" style="width:100%; position:relative;">
					<i class="fa blue fa-file-text-o"></i>
					<c:set var="checked" value="false"/>
					<c:forEach var="includeNodeSet" items="${node.includeNodeSet}">
						<c:if test="${childNode.nodeId == includeNodeSet.nodeId}"> 
							<input type="checkbox" name="includeNodeSet.nodeId" value="${childNode.nodeId}" checked="checked">
							<c:set var="checked" value="true"/>
						</c:if>
					</c:forEach>
					<c:if test="${checked==false}">
						<input class="check subNodeId" type="checkbox" name="includeNodeSet.nodeId" value="${childNode.nodeId}"/>
					</c:if>
					<c:out value="${childNode.name}"></c:out>
					
					<!--<span style="text-align:right;">-->
						<span class="contentTypeRows">显示环境:
							<select name="includeNodeSet.contentType">
								<option value="">请选择</option>
								<c:set var="select2" value="0" />
								<c:if test="${!empty node.includeNodeSet}">
									<c:forEach var="includeNodeSet" items="${node.includeNodeSet}">
										<c:if test="${childNode.nodeId == includeNodeSet.nodeId}"> 
											<c:forEach var="q" items="${contextType}">
												<c:if test="${includeNodeSet.contextType eq q}">
													<option selected="selected" value="${q}"><spring:message code="ContextType.${q}"/></option>
												</c:if>
												<c:if test="${!(includeNodeSet.contextType eq q)}">
													<option value="${q}"><spring:message code="ContextType.${q}"/></option>
												</c:if>
												<c:set var="select2" value="${includeNodeSet.nodeId}" />
											</c:forEach>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${select2 != childNode.nodeId}">
									<c:forEach var="q" items="${contextType}">
										<option value="${q}"><spring:message code="ContextType.${q}"/></option>
									</c:forEach>
								</c:if>
							</select>
						</span>
						
						<span class="contentTypeRows">获取文章数量:
							<c:set var="rowsNums" value="0" />
							<c:if test="${!empty node.includeNodeSet}">
								<c:forEach var="includeNodeSet" items="${node.includeNodeSet}">
									<c:if test="${childNode.nodeId == includeNodeSet.nodeId}"> 
										<input name="includeNodeSet.rows" value="${includeNodeSet.rows}"/>
										<c:set var="rowsNums" value="${includeNodeSet.nodeId}"/>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${childNode.nodeId != rowsNums}">
								<input name="includeNodeSet.rows" />
							</c:if>
						</span>
					<!--</span>-->
					
				</span>
			</c:otherwise>
			</c:choose>
			</li>
		</c:forEach>
	</ul>