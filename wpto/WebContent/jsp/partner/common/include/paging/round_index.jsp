<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<script>
$(function(){		
	var currentIndex = ${contentPaging.currentPage};
	var url=location.href;
	$(".orient-right").click(function(){
		if(currentIndex === ${contentPaging.totalPage}){
			currentIndex = 1;
		}else{
			currentIndex ++;
		}	
		if(url.indexOf("?") ===-1){
			url += "?page="+currentIndex;
		}else if(url.indexOf('page=') !==-1){
			url = url.replace(/page=[0-9]+/, 'page=' + currentIndex);
		} else {
			url += "&page="+currentIndex			
		}
		location.href = url;
	})
	$(".orient-left").click(function(){
		if(currentIndex === 1){
			currentIndex = ${contentPaging.totalPage};
		}else{
			currentIndex --;
		}		
		if(url.indexOf("?") ===-1){
			url += "?page="+currentIndex;
		}else if(url.indexOf('page=') !==-1){
			url = url.replace(/page=[0-9]+/, 'page=' + currentIndex);
		} else {
			url += "&page="+currentIndex			
		}
		location.href = url;
	})	
})
</script>
		<img src="/theme/${theme}/images/left_list_arrow.png" class="orient-left"/>
		<img src="/theme/${theme}/images/right_list_arrow.png" class="orient-right"/>
		<ul class="paginations">
        <c:forEach var="page" begin="${contentPaging.displayFirstPage}" end="${contentPaging.displayLastPage}">
            <c:choose>
                <c:when test="${page == contentPaging.currentPage}" >
                    <li><div class="item current"></div></li>
                </c:when>
                <c:otherwise>
                    <li><div class="item"></div></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
		</ul>
