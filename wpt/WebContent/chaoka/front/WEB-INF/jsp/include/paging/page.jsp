<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

 <script type="text/javascript">
 		function goPage(page) {
						var url = "/${contentPrefix}product/index_"+page+"${pageSuffix}";
			
			document.location.href=url;
		}
	
</script>   
<c:if test="${productPaging.totalPage>1}">
<div style="text-align: center;margin-top:30px;font-family:"微软雅黑", Verdana, sans-serif;">
	<a href="javascript:goPage('${productPaging.firstPage}');" style="color: #104964;">首页</a> | 
	<a href="javascript:goPage('${productPaging.currentPage-1}');" style="color: #104964;">上一页</a> | 
	<c:forEach var="page" begin="${productPaging.startPagingLink}" end="${productPaging.endPagingLink}">
		<c:choose>
			<c:when test="${page == productPaging.currentPage}">
				<b style="color: red;">${page}</b> |
			</c:when>
			<c:otherwise>
				<a href="javascript:goPage('${page}');" style="color: #104964;">${page}</a> |
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<a href="javascript:goPage('${productPaging.currentPage+1}');" style="color: #104964;">下一页</a> | 
	<a href="javascript:goPage('${productPaging.totalPage}');" style="color: #104964;">尾页</a>
</div>
</c:if>
