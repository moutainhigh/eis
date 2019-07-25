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

<script type="text/javascript" src="../../../js/pull-down list.js"></script>
<style type="text/css">
li{
	color:#9D9D9F;
	list-style:none;
	font-family: "Microsoft YaHei" !important;
}
.box_left1 {
  width: 56.5%;
  height: 500px;
  overflow: hidden;
  margin: 0 auto;
}	
</style>
</head>
<body>
<div class="wid-100">
	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="box_left1">
		<ul>
    		<li><p>以先共为您搜到相关结果0个</p></li>
        	<li><p style="height:26px;"></p></li>
    		<li><p style="font-size:24px">抱歉，没有找到与<span style="color:#FF6C00">"${inputResult }"</span> 相关的内容</li>
            <li><p style="height:5px;"></p></li>
    		<li><p>建议：</p></li>
        	<li><p>检查输入是否正确，</p></li>
        	<li><p>或者重新输入其他关键词。</p></li>
    	</ul>  
	</div>
    <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>	
</div>
</body>
</html>