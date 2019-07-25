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

    <title>${systemName}管理中心</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<script>
	$(function(){
		$('body').on("click",".tools",function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
		})
	});
	</script>
	
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>缓存管理</span></h2>
			<div class="table-responsive">
				<table class="table table-striped">                            
				<thead>
				 <tr>
				 	<th style="width:17%;">编号</th>
				 	<th style="width:17%;">缓存名</th>
				 	<th style="width:17%;">缓存大小</th>
				 	<th style="width:17%;">缓存数</th><th style="width:32%;">操作</th>
				 </tr>
				</thead>
				<tbody>
					<c:forEach var="a" items="${rows}">
					<tr>
						<td>${a.index}</td>
						<td>${a.name}</td>
						<td>${a.cacheSize}</td>
						<td>${a.cacheCount}</td>
						<td style="position:relative;">
							<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
								<a href="#" class="deletea"><li>清除</li></a>
								
							</ul>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
        </div>
      </div>
    </div>
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
    <script>
	
	$(".deletea").on("click",function(){
			var cacheName=$(this).parent().parent().siblings().eq(1).html();
			alert(cacheName);
			$.ajax({ 
				type: "POST", 	
				url: "/cache/delete.json?idList="+cacheName,
				dataType: "json",
				async:false,
				success: function(data){
					alert(data.message.message);
				}		     
			}); 		
	})			
						
	</script>
	</body>
</html>
