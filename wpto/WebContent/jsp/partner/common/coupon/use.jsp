<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${systemName}-${title}</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	
    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet">
    
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
		<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
		<script src="/theme/${theme}/js/jquery-1.11.3.min.js"></script>
		<script src="/theme/${theme}/js/sweetalert.min.js"></script> 
		<script src="/theme/${theme}/js/jquery.validate.min.js"></script>
		<script src="/theme/${theme}/js/couponuse.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<style>
		#yourformid{margin-top: 200px;text-align: center;}
		span.recive{font-size: 18px;color: #635454;}
		#receiveCode{width: 230px;height: 28px;border: 1px solid #A3A3A3;outline: 0;padding: 3px 5px;font-size: 12px;font-family: "微软雅黑";}
		#bnt{margin-left:10px;outline: 0;height: 28px;}
		label.error{font-size: 16px;color: #ff6400;margin: 5px -26px;display: block;font-weight: normal;}

	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header">${title}</h2>
			<form action="coupon/get.json" method="post" onSubmit="return false;" id="yourformid">
				<span class="recive">领取码</span>：<input type="text"  name="receiveCode" id="receiveCode"/>
				<input type="submit" value="提交" id="bnt"/>
			</form>
			<div id="consss"></div>
        </div>
      </div>
    </div>
    <div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
		<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
	</div>
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
    
  </body>
</html>
