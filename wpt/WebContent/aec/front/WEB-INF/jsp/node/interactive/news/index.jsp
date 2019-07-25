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
<link rel="stylesheet" type="text/css" href="../../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../../css/index.css">
<link rel="stylesheet" type="text/css" href="../../../css/newslist.css">
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js"></script>
<script type="text/javascript" src="../../../js/common.js"></script>
</head>
<body>
   <div class="wid-100" id="wid-100">
<%@include file="/WEB-INF/jsp/include/index_head.jsp" %>
		<div class="flotage">
		<a onclick="conceal()"><img src="../../../image/erweima.jpg"/></a>
		<a href="#wid-100"><img src="../../../image/up.jpg"/></a>
	</div>
	<div style="display:block;" class="two-dimension" id="two-dimension">
		<img src="../../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	</div>
	<div class="content">
	    <h3 class="martop30 orange spacing">新闻报道</h3>
		<ul class="wid-80 martop50">
		 <c:forEach items="${newsList}" var="list">
		   <a href="${list.viewUrl}">
		   <li>
		   <div class="box_left">
		     <img src="${list.documentDataMap.get('productSmallImage').dataValue}"  class="box_container_left img_little">
		     </div>
			 <div class="box_news">
			  <span class="fw lp2">${list.title}</span>
			   <p class="fo-14 "><span class="co_gray"><fmt:formatDate value="${list.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />   </span>
			  <div style="color:#000;line-height:40px;">${fn:substring(list.documentDataMap.get('documentBrief').dataValue,0,100)}${fn:length(list.documentDataMap.get('documentBrief').dataValue)>100?"......":""}</div>
			
			</div>
		   </li>
		   </a>
		   <div class="line martop20"></div>
		   <div class="kong"></div>
		   </c:forEach> 
		     
		  
		</ul>
	</div>
<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</div>
	<script type="text/javascript">
	function conceal(){
	 if(document.getElementById("two-dimension").style.display=="none"){
    document.getElementById("two-dimension").style.display="block";
   }
	else{
    document.getElementById("two-dimension").style.display="none";
    }
}
</script>
</body>
</html>