<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/about.css"/>
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../../js/mobile/lazyload.js"></script>
<script type="text/javascript">
			
</script>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>用户注册协议</span><a class="list2" href="/"></a>
 </div>
	<div id="wrapper_1">
		<div class="orange wid90">
		用户注册协议
		</div>
	    <div class="wid901">
	    	 ${document.content}
	    </div>

	 </div>

	  
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>

</html>    