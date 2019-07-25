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
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
	  <link href="/theme/${theme}/style/query.css" rel="stylesheet">

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
		  input[name='senderName']{
				width: 392px !important;
		  }
		  .query {
			  margin-left: 71px !important;
			  max-width: 427px!important;
			  width:100%!important;
		  }
		  @media(max-width: 1200px){
		  	.query{
		  		margin-left: 0!important;
		  		max-width: 100%!important;
		  	}
		  }
	  </style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>${title}</span></h2>
			<div class="content">
				<form id="queryForm">
				<table width="100%">
					<tr>
						<td align="left" width="50%">
							时间：
							<span class="shijian">
								<input size="20" placeholder="开始时间"  id="d11" class="calendarInput" name="sendTimeBegin" type="text" value="" style="border:none;"/><img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
							<span class="shijian">
								<input size="20" placeholder="结束时间"  id="d12" class="calendarInput" name="sendTimeEnd" type="text" value="" style="border:none;"/><img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
						</td>
						<td width="50%">
							发件人ID：<input type="text" size="20" name="senderId" value="0">
							
						</td>

					</tr>
					<tr>
						<td>发件人名：<input class="queryForm-btn-wt408" type="text" size="20" name="senderName" value=""></td>
						<td>原始类型：<input type="text" size="20" name="originalType" value=""></td>
					</tr>
					<tr>
						<td>识别码：<input class="queryForm-btn-wt408" type="text" size="30" name="identify" value=""></td>
						<td>
							<button class="btn btn-primary query queryForm-btn-wt110"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="glyphicon glyphicon-search"></span>
								查询
								&nbsp;&nbsp;&nbsp;&nbsp;
							</button>
						</td>
					</tr>
				</table>
				</form>			
			</div>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>时间</th>
		                  <th>发信人</th>
		                  <th>用户ID</th>
		                  <th>内容</th>
		                  <th>识别码</th>
		                  <th>原始类型</th>
		                  <th>操作</th>
                		</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td><fmt:formatDate value="${i.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                    <td>${i.senderName}</td>
	                  	<td>${i.senderId}</td>
	                  	<td>${i.content}</td>
	                  	<td>${i.identify}</td>
	                  	<td>${i.originalType}</td>
	                   	<td style="position:relative;">
							<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position:absolute; width: 50px; padding-right: 10px; padding-left: 10px; margin-left: -24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
								<a href="${i.operate.get}"><li class="materialSelect">查看</li></a>
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
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
  </script>
</html>
