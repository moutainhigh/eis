<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../css/mobile/main.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../js/mobile/common.js"></script>
<style>
#wrapper_1 {
    width: 100%;
    height: auto;
    margin-top: 40px;
    overflow: hidden;
}
.cont{
	padding:15px 10px;
	text-align:justify;
}
</style>
</head>
<body>
<div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>${pageTitle}文件</span><a class="list1" onclick="vanish()"></a>
 </div>
<div id="wrapper_1">
     <%@include file="/WEB-INF/jsp/include/BtnList.jsp" %>
<div class="cont">${document.content}</div>
</div>
<script>
	function  vanish(){
	   if(document.getElementById("nav_list").style.display=="none"){
			document.getElementById("nav_list").style.display="block";
		   }
		else{
			document.getElementById("nav_list").style.display="none";
			}
	}
</script>
</body>
</html>