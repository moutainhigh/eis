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
									top.window.location=url.indexOf('_'+"page="+currentPage+'.shtml')==-1?url.replace("page="+currentPage,"page="+page):url.replace("page="+currentPage+'.shtml',"page="+page+'.shtml');
								}
						</script>
					 
			           <ul class="listbox"> 
						<a href="javascript:goPage('${paging.firstPage}');" class="current" style="width:40px;">首页</a> 
						<a href="javascript:goPage('${paging.currentPage-1}');" class="current prev"><li><</li></a>  
						<c:forEach var="page" begin="${paging.displayFirstPage}" end="${paging.displayLastPage}">
							<c:choose>
								<c:when test="${page == paging.currentPage}" >
									<a  class="current currentPage"><li>${page}</li></a> 
								</c:when>
								<c:otherwise>
								<a href="javascript:goPage('${page}');" ><li>${page}</li></a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<a href="javascript:goPage('${paging.currentPage+1}');" class="current next"><li>></li></a>   
						<a href="javascript:goPage('${paging.totalPage}');" class="current" style="width:40px;">尾页</a>
					   </ul>
			        
					</c:if>