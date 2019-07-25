<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<c:if test="${paging.totalPage>1}">
<div style="text-align: center;font-family: 黑体;background: none;">
	<a href="javascript:goPage('${paging.firstPage}');" style="color: #104964;">首页</a> | 
	<a href="javascript:goPage('${paging.currentPage-1}');" style="color: #104964;">上一页</a> | 
	<c:forEach var="page" begin="${paging.startPagingLink}" end="${paging.endPagingLink}">
		<c:choose>
			<c:when test="${page == paging.currentPage}">
				<b style="color: red;">${page}</b> |
			</c:when>
			<c:otherwise>
				<a href="javascript:goPage('${page}');" style="color: #104964;">${page}</a> |
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<a href="javascript:goPage('${paging.currentPage+1}');" style="color: #104964;">下一页</a> | 
	<a href="javascript:goPage('${paging.totalPage}');" style="color: #104964;">尾页</a>
</div>
</c:if>