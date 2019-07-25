<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>
<!--  嵌入价格编辑部分 -->
<c:forEach items="${priceList}" var="price">
	<tr>
		<td><spring:message code="PriceType.${price.priceType}"/>价格</td>
		<td>
			市场价:<input type="text" name="marketPrice" size="5" value="${price.marketPrice}"> 
			${moneyName}:<input type="text" name="${price.priceType}.money" value="${price.money}" size="5"> 
			${coinName}:<input type="text" name="${price.priceType}.coin" value="${price.coin}" size="5"> 
			${pointName}:<input type="text" name="${price.priceType}.point" value="${price.point}" size="5"> 
			${scoreName}:<input type="text" name="${price.priceType}.score"  value="${price.score}" size="5">
		</td>
	</tr>
</c:forEach>
