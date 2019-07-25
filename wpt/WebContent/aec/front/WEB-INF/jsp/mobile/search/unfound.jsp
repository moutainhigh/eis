<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<style>
  .box_left1{
	  width:85%;
	  margin:0 auto;
	  margin-top:50px;
  }
</style>
</head>
<body>
<div class="wid-100">
   <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>搜索结果</span><a class="list2" href="/"></a>
  </div>
	<div class="box_left1">
		<ul>
    		<li><p>以先共为您搜到相关结果0个</p></li>
        	<li><p style="height:26px;"></p></li>
    		<li><p style="font-size:20px">抱歉，没有找到与<span style="color:#FF6C00">"${inputResult }"</span> 相关的网页</li>
            <li><p style="height:5px;"></p></li>
    		<li><p>建议：</p></li>
        	<li><p>检查输入是否正确，</p></li>
        	<li><p>或者重新输入其他关键词。</p></li>
    	</ul>  
	</div>
	<div>
	<div style="position:absolute;width:100%;height:auto;bottom:50px;">
    <%@include file="/WEB-INF/jsp/include/footer.jsp" %>	
	</div>
</div>
</body>
</html>