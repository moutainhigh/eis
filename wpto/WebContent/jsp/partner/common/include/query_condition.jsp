<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>
<c:if test="${!empty queryCondition}">
	<form id="queryForm">
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<c:forEach var="condition" items="${queryCondition}">
					<td align="right"  width="8%"><spring:message code="Criteria.${condition.key}"/>：</td>
					<td align="left"  width="18%">
					<c:choose>
						<c:when test="${!empty condition.value}">
							<select id="${condition.key}">
							<c:forEach var="queryValue" items="${condition.value}">
								<option value="${queryValue.key}"><spring:message code="${queryValue.value}"/></option>
							</c:forEach>
							</select>
						</c:when>
						<c:otherwise>
							<input id="${condition.key}" name="${condition.key}" />			
						</c:otherwise>
					</c:choose>
					</td>
				</c:forEach>
				<td align="center" width="7%"><input onClick="query()"  type="submit" value="查询" onSubmit="return false;"/></td>
				</tr>
	</table>
	</form>
</c:if>
<script>
function query(){
	var url = document.location.href.split('?')[0];
	url= url + $("#queryForm").serialize();  		
}
</script>