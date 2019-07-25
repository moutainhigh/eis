<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<c:if test="${paging.totalPage>1}">
<script type="text/javascript">
		function goPage(page) {
						var url = document.location.href;
			var totalPage=${paging.totalPage};
			var currentPage=${paging.currentPage};
			page=page==0?1:page>totalPage?totalPage:page;
			if(page==currentPage)return;
			document.location.href=url.indexOf('_'+currentPage+'.html')==-1?url+'_'+page+'.html':url.replace(currentPage+'.html',page+'.html');
		}
</script>
<div style="text-align: center;font-family: '微软雅黑';background: none;">
	<a href="javascript:goPage('${paging.firstPage}');">首页</a> | 
	<a href="javascript:goPage('${paging.currentPage-1}');">上一页</a> | 
	<c:forEach var="page" begin="${paging.displayFirstPage}" end="${paging.displayLastPage}">
		<c:choose>
			<c:when test="${page == paging.currentPage}">
				<b style="color: red;">${page}</b> |
			</c:when>
			<c:otherwise>
				<a href="javascript:goPage('${page}');">${page}</a> |
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<a href="javascript:goPage('${paging.currentPage+1}');">下一页</a> | 
	<a href="javascript:goPage('${paging.totalPage}');">尾页</a>
	</div>
</c:if>