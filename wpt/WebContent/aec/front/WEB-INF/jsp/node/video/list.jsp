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
<link rel="stylesheet" type="text/css" href="../../../../css/newCss/index.css">
<link rel="stylesheet" type="text/css" href="../../../../css/newCss/videolist.css">
<script type="text/javascript" src="../../../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../../js/respond.src.js"></script>
<script type="text/javascript" src="../../../../js/common.js"></script>
<script  type="text/javascript" src="../../../../js/jump.js"></script>
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
	<div class="page_title fo-14"><ul class="wid-80"><li><a href="/">首页</a></li><li><a href="/content/videolist/index.shtml">精彩视频</a></li><li><a href="#" class="orange">${pageTitle}</a></li></ul></div>
	<div class="box_container padbtm50">
		<div class="flotage">
		<a onclick="conceal()"><img src="../../../image/erweima.jpg"/></a>
		<a href="#wid-100"><img src="../../../image/up.jpg"/></a>
	    </div>
	    <div style="display:block" class="two-dimension" id="two-dimension">
		<img src="../../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	    </div>
		<ul class="list_jianbie wid-80 martop30">
			   <c:forEach var="document" items="${newsList}" >
			    <li>
			    <a href="${document.viewUrl}">
			       <div style="position: relative;" >
			    		<img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../../image/listImgDefault.png" class="box-img lazy"/>
			            <div style="z-index: 1;width:53px;height: 53px;top:50%; left:50%;margin:-26px 0px 0px -26px;position: absolute!important ;background-image: url(../../../image/play.png);" ></div>
			    	</div>
			    	<!-- <p class="time_bar"></p>  --> 
				   <p class="box_title">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
				   </a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	
</body>
</html>