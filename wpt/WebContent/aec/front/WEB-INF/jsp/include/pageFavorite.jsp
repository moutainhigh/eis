<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<c:if test="${favPaging.totalPage>1}">
						<script type="text/javascript">
							function goPage(page) {
									var url = document.location.href;
									var totalPage=${favPaging.totalPage};
									var currentPage=${favPaging.currentPage};
									page=page==0?1:page>totalPage?totalPage:page;
									if(page==currentPage)return; 
									document.location.href=url.indexOf('_'+"favPage="+currentPage+'.shtml')==-1?url.replace("favPage="+currentPage,"favPage="+page):url.replace("favPage="+currentPage+'.shtml',"favPage="+page+'.shtml');
								}
						</script>
					 
			           <ul class="listbox"> 
						<a href="javascript:goPage('${favPaging.firstPage}');" class="current" style="width:40px;">首页</a> 
						<a href="javascript:goPage('${favPaging.currentPage-1}');" class="current prev"><li><</li></a>  
						<c:forEach var="page" begin="${favPaging.displayFirstPage}" end="${favPaging.displayLastPage}">
							<c:choose>
								<c:when test="${page == favPaging.currentPage}" >
									<a  class="current currentPage"><li>${page}</li></a> 
								</c:when>
								<c:otherwise>
								<a href="javascript:goPage('${page}');" ><li>${page}</li></a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<a href="javascript:goPage('${favPaging.currentPage+1}');" class="current next"><li>></li></a>   
						<a href="javascript:goPage('${favPaging.totalPage}');" class="current" style="width:40px;">尾页</a>
					   </ul>
			        
					</c:if>