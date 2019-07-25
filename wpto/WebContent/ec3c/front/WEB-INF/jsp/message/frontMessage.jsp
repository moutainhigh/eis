<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<script  type="text/javascript" src="/theme/ec1/js/jquery-1.7.1.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="/theme/ec1/js/respond.src.js"></script>
<script  type="text/javascript" src="/theme/ec1/js/common.js"></script>
<style>
	.wrap1000{
		margin:0 auto;
		width:1000px;
	}
	.center{
		text-align:center;
	}
	*{
		font-family:"Microsoft Yahei";
	}
	.head{
		margin-top:120px;
	}
	h1{
		font-size:30px;
		font-weight:bold;
		color:#FF6922;
	}
	body{
		text-align:center;
	}
</style>
<script>
	
</script>
</head>

<body>
    <div class="head center">
	   <h1>${message.operateCode}</h1>
    </div>
    <div class="content center">
		<p>${message.message}</p>
		<p>${message.content}</p>
    </div>
</body>
</html>
