<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

<c:if test="${paging.lastPage>1}">
<script type="text/javascript">
		function goPage(page) {
			var url = document.location.href;
			var totalPage=${paging.lastPage};
			var currentPage=${paging.currentPage};
			page = page==0 ? 1: page>totalPage ? totalPage:page;
            
			if(page==currentPage)return;
			if(url.indexOf('?') == -1){
				//没有任何查询字符串
				url = url + '?page=' + page;
				console.log(url);
			} else {
				//已经有页码
				if(url.indexOf('page=') >= 0){
					url = url.replace(/page=[0-9]+/, 'page=' + page);
				} else {
					url = url + "&page="+page ;
				}  
                if(url.indexOf('&page=') >= 0){
                    url = url+page;
                    url = url.replace(/page=[0-9]+/, 'page=' + page);
                }
			}
            console.log(url);
			document.location.href=url;
		}
		function showData(data){
            var url = document.location.href;
            if(data<=0) {
                location.reload();
			}else{
                if(url.indexOf('rows=') >= 0){
                    url = url.replace(/rows=[0-9]+/, 'rows=' + data);
                } else {
                    url = url + "&rows=" + data;
                }
			}

            document.location.href=url;
		}
		$(function(){
			$(".submit").click(function (){
                var page=$(".inputPage").val(),
                    totalData = $(".rowsPerPage").val();
                showData(totalData);
                goPage(page);
            });
			$(".inputPage").change(function(){
				var page=$(".inputPage").val();
				goPage(page);
			});
			$(".rowsPerPage").change(function(){
                var totalData = $(".rowsPerPage").val();
                showData(totalData);
            });

		})
</script>

<div id="page">
      
        <a href="javascript:goPage('${paging.firstPage}');">首页</a>
        <a href="javascript:goPage('${paging.currentPage-1}');" >上一页</a>
        <c:choose>
        <c:when test="${paging.lastPage<3&&paging.currentPage==1}">
			<a href="javascript:goPage('${paging.currentPage}');"class="current">${paging.currentPage}</a>
			<a href="javascript:goPage('${paging.currentPage+1}');">${paging.currentPage+1}</a>
    	</c:when>
    	<c:when test="${paging.lastPage<3&&paging.currentPage==2}">
    		<a href="javascript:goPage('${paging.currentPage-1}');">${paging.currentPage-1}</a>
			<a href="javascript:goPage('${paging.currentPage}');"class="current">${paging.currentPage}</a>
    	</c:when>
        <c:when test="${paging.currentPage==1}">
        	<a href="javascript:goPage('${paging.currentPage}');" class="current">${paging.currentPage}</a>
			<a href="javascript:goPage('${paging.currentPage+1}');">${paging.currentPage+1}</a>
			<a href="javascript:goPage('${paging.currentPage+2}');">${paging.currentPage+2}</a>
    	</c:when>
    	<c:when test="${paging.currentPage==paging.lastPage}">
        	<a href="javascript:goPage('${paging.currentPage-2}');">${paging.currentPage-2}</a>
			<a href="javascript:goPage('${paging.currentPage-1}');">${paging.currentPage-1}</a>
			<a href="javascript:goPage('${paging.currentPage}');"class="current">${paging.currentPage}</a>
    	</c:when>
    	
    	<c:otherwise>  
    		<a href="javascript:goPage('${paging.currentPage-1}');">${paging.currentPage-1}</a>
			<a href="javascript:goPage('${paging.currentPage}');"class="current">${paging.currentPage}</a>
			<a href="javascript:goPage('${paging.currentPage+1}');">${paging.currentPage+1}</a>
    	</c:otherwise>
    	</c:choose>
        <a href="javascript:goPage('${paging.currentPage+1}');">下一页</a>  
        <a href="javascript:goPage('${paging.lastPage}');">尾页</a>
        <span>共${paging.lastPage}页</span>
</div>  
</c:if>