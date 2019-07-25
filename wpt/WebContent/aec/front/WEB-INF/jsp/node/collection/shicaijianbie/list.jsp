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
<link rel="stylesheet" type="text/css" href="../../../../css/newCss/main.css">
<!-- <link rel="stylesheet" type="text/css" href="../../../../css/index.css"> -->
<link rel="stylesheet" type="text/css" href="../../../../css/videolist.css">
<script type="text/javascript" src="../../../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../../js/respond.src.js"></script>
<script type="text/javascript" src="../../../../js/common.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
</head>
<body id="wid-100">
	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="page_title fo-14"><ul class="wid-80"><li><a href="/">首页</a></li><li><a href="/content/shicaixinyang/index.shtml">食材溯源</a></li><li><a href="#" class="orange">${pageTitle}</a></li></ul></div>
	<div class="box_container padbtm50">
		<ul class="list7 martop45 wid-80">
			<c:forEach var="document" items="${newsList}">
			    <li>
			    <a href="${document.viewUrl}">
			       <p><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"></p>
				   <p class="box_title" style="font-size: 15px;">${fn:substring(document.title ,0,17)}${fn:length(document.title)>17?"...":""}</p>
				  <!-- <p>${document.documentDataMap.get('documentBrief').dataValue}</p>-->
				   </a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>