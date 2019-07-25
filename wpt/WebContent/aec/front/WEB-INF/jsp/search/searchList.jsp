<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../../css/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../css/index.css"/>
<script  type="text/javascript" src="../../../js/jquery.min.js"></script>
<script  type="text/javascript" src="../../../js/jump.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js">	</script>
<script type="text/javascript"  charset="utf-8" src="../../../js/common.js"></script>

<link rel="stylesheet" type="text/css" href="../../../css/main1.css" />
<link rel="stylesheet" type="text/css" href="../../../css/searchList.css" />
<script type="text/javascript" src="../../../js/pull-down list.js"></script>

<style type="text/css">
.highlight{
	color:#FF6C00;
}
a{
	text-decoration:none;
	color:#333333;
}
</style>
</head>
<body>

<div class="wid-100">
	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<!-- 搜索结果 -->
    <div class="box_left">
		<p class="martop30"><span class="fc">以先共为您搜索到相关结果${totalRecord }个</span></p>
		</div>
		<c:forEach items="${result.searchList }" var="result">
			<!-- 产品版式 -->
			<c:if test="${!empty result.unitPrice }">
				<div class="box_left">
					<p class="martop45 font-title" style="margin-top:10px !important;"><a href="${result.url }">${result.title }</a></p>
				   	<ul class="list4 martop45 bor" style="margin-top:5px !important;">
						<li style="margin:0 0;"><a href="${result.url }"> <img src="${result.simages }"/></a></li>
					  	<li style="width:70%; margin-left:2%; text-align:left; font-size:16px;">
					  		<div class="commodity" style="margin-left:15%;">
					  		${result.title } 
							<c:set value="${ fn:split(result.origin, '#') }" var="str1" />
					  		<p class="martop20">产地：<!--${result.origin }-->${str1[0] }&nbsp&nbsp&nbsp${str1[1] }</p>
					  		<p class="martop20">规格：${result.specifications }</p>
					  		<p class="martop20">单价：<span class="orange">￥${result.unitPrice }</span></p>
					  		<!--<input type="button" value="提交订单" class="butt martop10">-->
					  	</li>
				   </ul>
				   <div class="fc martop10">2016-05-19 11:33 &nbsp;&nbsp;&nbsp;[${result.plateName }]</div>
					<hr style="border: 1px #cfcfcf solid;" />
				</div>
			</c:if>
			<!-- 没有图片的非产品版式-->
			<c:if test="${empty result.unitPrice }">
	    		<c:if test="${empty result.simages2 }">
	    			<div class="box_left">
						<p class="martop25 font-title"><a href="${result.url }">${result.title }</a></p>
			    		<div class="text_message martop10">${result.content }<!--${fn:substring(result.content,0,50)}${fn:length(result.content)>50?"...":""}-->
			    		</div> 
						<div class="fc martop10">${result.createTime } &nbsp;&nbsp;&nbsp;[${result.plateName }]</div>
						<hr style="border: 1px #cfcfcf solid;" />
					</div>
	    		</c:if>
	    	</c:if>
	    	<!-- 有图片的非产品版式 -->
	    	<c:if test="${empty result.unitPrice }">
	    		<c:if test="${!empty result.simages2 }">
					<c:choose>
						<c:when test="${result.plateName == '火热预售' || result.plateName == '优选预售' }">
							<div class="box_left">
					<p class="martop45 font-title" style="margin-top:10px !important;"><a href="${result.url }">${result.title }</a></p>
				   	<ul class="list4 martop45 bor" style="margin-top:5px !important;">
						<li style="margin:0 0;"> <a href="${result.url }"><img src="${result.simages2 }"/></a></li>
					  	<li style="width:70%; margin-left:2%; text-align:left; font-size:16px;">
					  		<div class="commodity" style="margin-left:15%;">
					  		${result.title } 
							<c:set value="${ fn:split(result.origin, '#') }" var="str1" />
					  		<p class="martop20">产地：<!--${result.origin }-->${str1[0] }&nbsp&nbsp&nbsp${str1[1] }</p>
					  		<p class="martop20">规格：${result.specifications }</p>
					  		<p class="martop20">单价：<span class="orange">￥${result.unitPrice }</span></p>
					  		<!--<input type="button" value="提交订单" class="butt martop10">-->
					  	</li>
				   </ul>
				   <div class="fc martop10">2016-05-19 11:33 &nbsp;&nbsp;&nbsp;[${result.plateName }]</div>
					<hr style="border: 1px #cfcfcf solid;" />
				</div>
						</c:when>
						<c:otherwise>
							<div class="box_left">
						<p class="martop25 font-title" style="margin-top:10px !important;"><a href="${result.url }">${result.title } </a></p>
						<ul class="list4 martop45 wid-80" style="margin-top:5px !important;">
			    			<li style="margin:0 0;">
			       				<a href="${result.url }"><img src="${result.simages2 }"></a>
							</li>
			    			<li style="width:70%; margin-left:2%; text-align:left; font-size:16px;">
			    				<div class="message" style="margin-left:0; margin:0; padding:0; width:100%; padding-top:20px;">${result.content }<!--${fn:substring(result.content,0,50)}${fn:length(result.content)>50?"...":""}--></div> 
							</li>
						</ul>
						<div class="fc martop10">${result.createTime } &nbsp;&nbsp;&nbsp;[${result.plateName }]</div>
						<hr style="border: 1px #cfcfcf solid;" />
					</div>
						</c:otherwise>
					</c:choose>
	    		</c:if>
	    	</c:if>
		</c:forEach>
		
	</div>

    <!-- 分页 -->
    <div class="wid-80">
	<ul class="listbox martop45"> 
			 <!-- 上一页 -->
             <!--<li><a href="${pageContext.request.contextPath }/search/list.shtml?keywords=${inputResult }&currentPage=${dangQianYeHao-1 }" ><</a></li> -->
             <c:url value="${pageContext.request.contextPath }/search/list.shtml" var="shangyiye">
					<c:param name="keywords" value="${inputResult }"></c:param>
					<c:param name="currentPage" value="${dangQianYeHao-1 }"></c:param>
				</c:url>
             <li><a href="${shangyiye }" ><</a></li>
			 <!-- 当前页 -->
			 <c:forEach items="${pageList }" var="pl">
	 			<!--<li class="page"><a href="${pageContext.request.contextPath }/search/list.shtml?keywords=${inputResult }&currentPage=${pl+1 }">${pl+1 }</a></li>-->
				<c:url value="${pageContext.request.contextPath }/search/list.shtml" var="dangqianye">
					<c:param name="keywords" value="${inputResult }"></c:param>
					<c:param name="currentPage" value="${pl+1 }"></c:param>
				</c:url>
			   <li class="page"><a href="${dangqianye }">${pl+1 }</a></li>
			 </c:forEach>
			 <!-- <a href="#" class="especially"><li>.</li></a> 
			 <a href="#" class="especially"><li>.</li></a> 
			 <a href="#" class="especially"><li>.</li></a>  -->
			<!-- 下一页-->
             <!--<li><a href="${pageContext.request.contextPath }/search/list.shtml?keywords=${inputResult }&currentPage=${dangQianYeHao+1 }" >></a></li> -->
             <c:url value="${pageContext.request.contextPath }/search/list.shtml" var="xiayiye">
					<c:param name="keywords" value="${inputResult }"></c:param>
					<c:param name="currentPage" value="${dangQianYeHao+1 }"></c:param>
				</c:url>
             <li><a href="${xiayiye }" >></a></li>   
		</ul>
		</div>
		<div class="vacancy"></div>
	<footer>
	   <div class="footerBox">
    <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>	
</div>



</body>
</html>