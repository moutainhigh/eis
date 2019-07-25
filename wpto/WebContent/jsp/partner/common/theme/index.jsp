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

    <title>${systemName}-前端主题列表</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this theme -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->	
	<style>
		.main .row .item{
			margin:5% 5% 0 5%;
			margin-top:15%;
			border-top-left-radius:12px;
			border-top-right-radius:12px;
			overflow:hidden;
			position:relative;
		}
		.main .row .itemLabel{
			margin:0 5% 5% 5%;
			height:40px;
			line-height:40px;
			padding-left:5%;
			padding-right:5%;
			cursor:pointer;
			border:1px solid #ddd;
			border-top:none;
			border-bottom-left-radius:12px;
			border-bottom-right-radius:12px;
			
		}
		.main .row .itemLabel span:first-child{
			float:left;
		}		
		.main .row .item img{
			border-top-left-radius:12px;
			border-top-right-radius:12px;
			border:1px solid #ddd;
			
		}
		.detail{
			position:absolute;
			background:#333;
			opacity:0.5;
			height:0;
			width:100%;
			bottom:0;
			left:0;
			z-index:10;
			
			
		}
		.preview{
			width:45%;
			left:50%;
			top:35%;
			height:39px;
			border-radius:5px;
			margin-left:-22.5%;
			background:#428bca;
			position:absolute;
			z-index:20;
			color:#fff;
			text-align:center;
			line-height:39px;
			font-size:16px;
			display:none;
			cursor:pointer;
		}
		.tools img{
			cursor:pointer;
		}
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>主题列表</span></h2>
			<c:if test="${!empty addUrl }">
				<a href="${addUrl}.shtml">添加主题</a>
			</c:if>	
			<div class="row">
			<c:forEach var="i" items="${rows}">				
				<div class="col-sm-3  col-md-3 col-lg-2 col-xs-6">
					<div class="zoomIn" style="">
						<div class="image item">
							<img src="/theme/${theme}/images/defaultImage.png" style="width:100%;height:100%;"/>				
						</div>	
						<div class="itemLabel" style="position:relative;">
							<span style="float:left;overflow: hidden;text-overflow:ellipsis;display:block;width:90%;white-space: nowrap;" title=${i.themeName}>${i.themeId}-${i.themeName}</span>
							<span class="tools" style="position:absolute;right:8px;"><img src="/theme/${theme}/images/tools.png"/></span>
							<div class="clear:both;"></div>
							<ul class="toolbtns" style="position:absolute;right:0px;width:110px;line-height:30px;top:30px;list-style:none;z-index: 100;background-color: #fff;border: #ddd 1px solid;border-radius: 5px;padding-right:10px;text-align:right;display:none;">
								<c:if test="${i.operate.get!= null }"><a href="${i.operate.get}.shtml"><li class="materialSelect">查看</li></a></c:if>
								<c:if test="${i.operate.del != null }"><a href="${i.operate.del}.shtml?idList=${i.themeId}"><li class="materialDelete">删除</li></a></c:if>
								<c:if test="${i.operate.update != null }"><a href="${i.operate.update}.shtml"><li class="materialEdit">编辑</li></a></c:if>
							</ul>
						</div>
					</div>
				</div>	
				</c:forEach>
			</div>
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
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
  </body>
  <script>
	$(".tools").click(function(){
		$(this).parent().parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
	var w=$(".item").width();
	$(".item").css("height",1.4*w);	
	$(window).resize(function(){
		var w=$(".item").width();
		$(".item").css("height",1.4*w);	
	})
	$(".item").mouseenter(function(){
		$(this).children(".detail").animate({"height":"100%"},500);
		$(this).children(".preview").fadeIn(1000);
	})	
	$(".item").mouseleave(function(){
		$(this).children(".detail").animate({"height":0},10);
		$(this).children(".preview").fadeOut(10);
	})
	$(".preview").click(function(){
		var sceneId=$(this).children(".sceneId").html();
		window.open("/scene/preview/"+sceneId+".shtml");
	})
  </script>
</html>
