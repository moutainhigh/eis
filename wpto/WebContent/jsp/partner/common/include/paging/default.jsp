<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>

<c:if test="${contentPaging.totalPage>1}">
<script type="text/javascript">
		function goPage(page) {
			var url = document.location.href;
            console.log(url)
            console.log('asdad')
			var totalPage=${contentPaging.totalPage};
            console.log(totalPage)
			var currentPage=${contentPaging.currentPage};
			page = page==0 ? 1: page>totalPage ? totalPage:page;
            console.log(url);
			if(page==currentPage)return;
			if(url.indexOf('?') == -1){
				//没有任何查询字符串
				url = url + '?page=' + page;
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
<div class="page">
        <span class="current-data" style="float:left;">
            每页显示
            <input type="number" class="rowsPerPage"  form="queryForm"  value="${contentPaging.rowsPerPage}" name="rows">
            行
        </span>
        <a href="javascript:goPage('${contentPaging.firstPage}');" class="current">首页</a>
        <a href="javascript:goPage('${contentPaging.currentPage-1}');" class="current">上一页</a>

        <c:forEach var="page" begin="${contentPaging.displayFirstPage}" end="${contentPaging.displayLastPage}">
            <c:choose>
                <c:when test="${page == contentPaging.currentPage}" >
                    <a  class="current active"  >${page}</a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:goPage('${page}');" >${page}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <a href="javascript:goPage('${contentPaging.currentPage+1}');" class="current">下一页</a>
        <a href="javascript:goPage('${contentPaging.totalPage}');" class="current">尾页</a>
        <div class="choosePage">
            共${total}条数据，共${contentPaging.totalPage}页，前往
            <input type="text" size="3" class="inputPage" name="page" form="queryForm" value="${page}"/>页
            <input type="submit" class="submit" value="go" size="2" onsubmit="return false;">
        </div>
</div>
</c:if>