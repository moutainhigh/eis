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

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
	<script src="/theme/${theme}/js/pageQuery.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header">${title}</h2>
			<div class="search_form">
				<form id="queryForm">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td>UUID:<input type="text" size="20" name="uuid" value=""></input></td>
						<td>用户名:<input type="text" size="20" name="username" value=""></input></td>
						<td>省份:<input type="text" size="20" name="province" value=""></input></td>
						<td>城市:<input type="text" size="20" name="city" value=""></input></td>
						<td>收件人:<input type="text" size="20" name="contact" value=""></input></td>
						<td>手机号:<input type="text" size="20" name="mobile" value=""></input></td>
						<td>识别码:<input type="text" size="10" name="identify" value=""></input></td>
						<td align="center" width="7%"><input   type="submit" value="查询"  onClick="query()" onSubmit="return false;"/></td>
						</tr>
				</table>
				</form>
			
			</div>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>地址本ID</th>
		                  <th>用户</th>
		                  <th>省份</th>
		                  <th>城市</th>
		                  <th>地区</th>
		                  <th>地址</th>
		                  <th>联系人</th>
		                  <th>电话</th>
		                  <th>识别码</th>
		                  <th>状态</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.addressBookId}</td>
							<td>${i.uuid}</td>
							<td>${i.province}</td>
							<td>${i.city}</td>
							<td>${i.district}</td>
							<td>${i.address}</td>
							<td>${i.contact}</td>
							<td>${i.mobile}/${i.phone}</td>
							<td>${i.identify}</td>
							<td><spring:message code="Status.${i.currentStatus}"/></td>
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
</html>
