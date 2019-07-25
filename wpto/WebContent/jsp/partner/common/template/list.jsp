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

    <title>${systemName}-模版列表</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
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
	
  </head>
  <style type="text/css">
  .table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
    padding: 13px;}
   .delete{
   	width: 100%;height: 100%;
   	background-color: rgba(0,0,0,0.6);
   	position: fixed;top: 0;left: 0;
   	    z-index: 999;
   	padding: 15px;
   	display: none;
   }
   .delete .box{
   	position: absolute;width: 300px;
   	height: 150px;
   	top: 50%;left: 50%;
   	margin-left: -150px;margin-top: -75px;
   	border-radius: 5px;
   	background-color: #fff;
   	text-align: center;
   }
   .delete h3{
   	    margin-bottom: 15px;
    margin-top: 31px;
   }
   .delete span,.delete a{
   	display: inline-block;
    padding: 7px 15px;
    background-color: #C5C5C5;
    border-radius: 5px;
	margin: 15px;
	cursor: pointer;
   }
   .delete a{
        background-color: #5C6F87;
    color: #fff;
   }
    </style>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>模版列表</span></h2>
			<c:if test="${!empty addUrl}">
				<p><a href="${addUrl}.shtml" class="addmoban" >+ 添加模版</a></p>
			</c:if>	
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>模版ID</th>
		                  <th>模版名称</th>
		                  <th>模版文件</th>
		                  <th>状态</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.templateId}</td>
							<td>${i.templateName}</td>
							<td>${i.templateLocation}</td>
							<td><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></td>
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<c:if test="${!empty addUrl}">
								<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<c:if test="${i.operate.get != null }"><a href="${i.operate.get}.shtml"><li class="materialSelect">查看</li></a></c:if>
									<c:if test="${i.operate.del != null }"><li class="materialDelete" ahref="${i.operate.del}.shtml?idList=${i.templateId}" style="cursor: pointer;">删除</li></c:if>
									<c:if test="${i.operate.update != null }"><a href="${i.operate.update}.shtml"><li class="materialEdit">编辑</li></a></c:if>
								</ul>
								</c:if>	
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
		<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
	</div>
        </div>
      </div>
    </div>
    <div class="delete">
    	<div class="box">
    		<h3>确定删除模板吗？</h3>
    		<a href="#" class="confirm">确定</a><span class="cancel">取消</span>
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
  </body>
  <script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	});
	$('.materialDelete').on('click',function(){
		var href = $(this).attr('ahref');
		$('.delete').css('display','block');
		$('.delete a').attr('href',href);
	});
	$('.delete .confirm').on('click',function(){
		$('.delete').css('display','none');
	});
	$('.delete .cancel').on('click',function(){
		$('.delete').css('display','none');
	});
  </script>
</html>
