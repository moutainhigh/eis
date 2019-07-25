<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">

<title>Xml数据导入</title>

</head>

<body>

	<div id="wrapper">

		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.html">配置文件导入</a>
		</div>

		

		<div class="navbar-default sidebar" role="navigation">
			<div class="sidebar-nav navbar-collapse">
				<ul class="nav" id="side-menu">
					<li class="sidebar-search">
						<div class="input-group custom-search-form">
							<input type="text" class="form-control" placeholder="查询内容...">
							<span class="input-group-btn">
								<button class="btn btn-default" type="button">
									<i class="fa fa-search" style="padding: 3px 0 3px 0;"></i>
								</button>
							</span>
						</div> <!-- /input-group -->
					</li>
					<li><a href="customer.action" class="active"><i
							class="fa fa-edit fa-fw"></i> 配置文件导入</a></li>
				</ul>
			</div>
		</div>
		</nav>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">配置文件导入</h1>
					<div class="btn-group">
						<a type="button" class="btn btn-default" href="${insert}">导入</a>
						<a type="button" class="btn btn-default" href="index">返回</a>
					</div>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
			<table class="table table-striped">
				<caption>XML数据预览</caption>
				<thead>
					<tr >
						<c:forEach items="${names}" var="name">
							<th>${name}</th>
						</c:forEach>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ImportXMLDatas}" var="data">
						<tr align="center">
							<c:forEach items="${data}" var="item">
								<td>${item.value}</td>
							</c:forEach>
						</tr>	
					</c:forEach>
				</tbody>
			</table>
	<script>
		function preview(str){
			var form =document.forms[0];
			form.action=str;
			form.submit();
		}
	</script>


</body>

</html>
