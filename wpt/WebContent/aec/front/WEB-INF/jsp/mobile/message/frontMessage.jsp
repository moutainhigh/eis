<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title>提示</title>
   <script src="/js/jquery.min.js"></script>
<!--[if lt IE 9]>
<script src="/js/respond.min.js"></script>
<![endif]--> 	
<style>
	body{background:#f1f1f1;}
	.opCode{text-align:center;font-size:1.8em;font-weight:bold;color:#FF6400;margin-top:20px;}
	.msg{text-align:center;font-size:1.3em;margin-top:8px;}
	.oImg{text-align:center;margin-top:30px;}
	.oImg img{width:120px;height:auto;}
</style>
</head>
<body>
<div class="oImg"><img src="/image/mobile/logo.png" /></div>
<div class="opCode">${message.operateCode}</div>
<div class="msg">${message.message}</div>

</body>
</html>