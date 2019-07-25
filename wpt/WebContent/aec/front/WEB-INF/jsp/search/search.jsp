<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../../css/pull-down list.css" type="text/css" />
<script type="text/javascript"src="../../../js/pull-down list.js"></script>

</head>
<body>
     
<div style="margin-left:200px;width:300px;height:50px;">
	<form action="search/list.shtml" method="post" >
		<input type="text" id="shuRu2" name="keywords" autocomplete="off" onkeyup="doAjax()" > <input type="submit" value="搜索" /> 	
		<ul id="ulId" class="xiaLa">                                                                             
		</ul>
	</form>
</div>


</body>
</html>