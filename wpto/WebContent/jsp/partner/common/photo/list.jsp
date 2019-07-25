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

<title>${systemName}-${title}</title>

<!-- Bootstrap core CSS -->
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet"
	type="text/css" />
</head>
<style>
.com-title a {
	padding: 8px;
	display: inline-block;
	color: #333;
}

span {
	margin: 0;
	padding: 0;
}

table {
	width: 100%;
	font-size: 12px;
}

thead {
	background: #ccc;
}

tr {
	text-align: center;
}

tr th {
	border-top: 1px solid #333;
	padding: 8px;
	font-size: 14px;
	text-align: center;
}

tr td {
	height: 80px;
	border-bottom: 1px solid #ccc;
	font-size: 14px;
}

.tupian img {
	width: 20%;
}

tbody {
	border-top: 1px solid #333;
}

.modalFooter a {
	color: #fff;
	border-radius: 5px;
	margin-left: 5%;
}

.modalFooter .submit {
	background: #222;
}

.modalFooter .cancel {
	background: #d0d0d0;
}

@media ( max-width :768px) {
	.Modal {
		width: 80%;
		left: 50%;
		margin-left: -40%;
	}
}

@media ( max-width :768px) {
	.search_form td {
		display: block !important;
	}
}
</style>

<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>

	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header">${title}</h2>

				<div class="search_form" style="overflow: hidden;"></div>


				<div class="table-responsive">
					<table border="0" cellspacing="0" cellpadding="5">
						<thead>
							<tr>
								<th width="10%">图片编号</th>
								<th width="10%">上传时间</th>
								<th width="10%">联系方式</th>
								<th width="40%">图片列表</th>
								<th width="10%">心得列表</th>
								<th width="10%">审核状态</th>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i2" items="${joinUserList}">

								<tr>
									<td>${i2.index}</td>
									<td>${i2.logTime}</td>
									<td>${i2.data.phone}</td>
									<td class="tupian"><img
										src="${path}/${i2.extraDataList[0].dataValue}" alt="">
										<img
										src="${path}/${i2.extraDataList[1].dataValue}" alt="">
										</td>
										<td>${i2.extraDataList[2].dataValue}</td>
									<td><spring:message code="Status.${i2.currentStatus}" />
									</td>
									<td style="position: relative;"><span class="tools"
										style="right: 5px; cursor: pointer;"> <img
											src="/theme/basic/images/tools.png">
									</span>
										<ul
											style="position: absolute; width: 96px; padding-right: 10px; padding-left: 10px; margin-left: -38px; top: 50px; text-align: center; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
											class="toolbtns">
											<c:if test="${!empty i2.operate.relate }">
												<a href="#"
													onclick="shenhe('${i2.uuid}','${i2.operate.relate}', '${i2.activityId}')">
													<li>审核</li>
												</a>
											</c:if>
											<c:if test="${!empty i2.operate.clear }">
												<a href="#"
													onclick="quxiaoshenhe('${i2.uuid}','${i2.operate.clear}', '${i2.activityId}')">
													<li>取消审核</li>
												</a>
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
	<div id="BgDiv"></div>
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
	$(function() {
		$('.tools').on("click", function() {
			$(this).parent().parent().siblings().find(".toolbtns").hide();
			$(this).siblings(".toolbtns").toggle();
		})
	});

	//审核
	function shenhe(uuid, status, activityId) {
		$.ajax({
			type : "POST",
			url : status + ".json?uuid=" + uuid + "&activityId=" + activityId,
			dataType : "json",
			success : function(msg) {
				alert(msg.message.message);
				location.reload();
			},
		})
	}
	//取消审核
	function quxiaoshenhe(uuid, status, activityId) {
		$.ajax({
			type : "POST",
			url : status + ".json?uuid=" + uuid + "&activityId=" + activityId,
			dataType : "json",
			success : function(msg) {
				alert(msg.message.message);
				location.reload();
			},
		})
	}
</script>
<script>
	
</script>

</html>