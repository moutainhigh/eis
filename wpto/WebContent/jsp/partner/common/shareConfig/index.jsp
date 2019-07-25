<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<title>${systemName}-上架管理</title>

<!-- Bootstrap core CSS -->
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">

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
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<script>
	function addShareConfig(productId) {
		alert(productId);
			swal({
				title : "您确定要关闭吗？",
				type : "warning",
				showCancelButton : true,
				closeOnConfirm : false,
				confirmButtonText : "确认",
				confirmButtonColor : "#ec6c62"
			}, function() {
				$.ajax({
					type : "POST",
					url : "/shareConfig/update.json?productId=" + productId
							,
					dataType : "json",
					success : function(data) {
						swal({
							title : data.message.message
						}, function() {
							window.location.reload();
						});
					}
				});
			});

	}
</script>
</head>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header"><span>上架管理</span></h2>
				<c:if test="${!empty addUrl }">
					<a href="${addUrl}.shtml" class="addmoban">+ 添加上架商品</a>
				</c:if>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>上架ID</th>
								<th>上架商户</th>
								<th>分成比例</th>
								<th>产品名称</th>
								<th>产品代码</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td>${i.operate.shareConfigId}</td>
									<td>${i.operate.shareUser}</td>
									<td>${i.operate.sharePercent}</td>
									<td><img
										src="/static/${i.productDataMap.productSmallImage.dataValue}" width="100" height="50"
										alt="${i.productName}">${i.productName}</td>
									<td>${i.productCode}</td>
									<td><spring:message code="Status.${i.currentStatus}"
											text="${i.currentStatus}" /></td>
									<td style="position: relative;"><span class="tools"
										style="right: 5px; cursor: pointer;"><img
											src="/theme/basic/images/tools.png"></span>
										<ul
											style="position: absolute; padding-right: 10px; padding-left: 10px; left: 50%; margin-left: -24px; top: 28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
											class="toolbtns">
											<c:if test="${i.operate.get != null }">
												<a href="${i.operate.get}.shtml"><li
													class="materialSelect">查看</li></a>
											</c:if>
											<c:if test="${i.operate.update != null }">
												<li class="materialEdit" style="cursor: pointer"
													onclick="addShareConfig('${i.productId}')">上架</li>
											</c:if>
											<c:if test="${i.operate.del != null }">
												<li class="materialDelete" style="cursor: pointer"
													onclick="delShareConfig('${i.productId}')">下架</li>
											</c:if>
										</ul></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="Pagination"
		style="text-align: center; width: 100%; background: #fff;">
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
	$(".tools").click(function() {
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
</script>
</html>
