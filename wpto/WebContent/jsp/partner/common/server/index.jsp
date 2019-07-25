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
<title>${systemName}-系统服务器管理</title>
<!-- Bootstrap core CSS -->
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
<link href="/theme/${theme}/style/query.css" rel="stylesheet">
<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
<script src="/theme/${theme}/js/pageQuery.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
	    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
		<script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
<style>
@media ( max-width :768px) {
	.search_form td {
		display: block !important;
	}
}

.table-responsive td {
	display: table-cell;
	vertical-align: middle;
	line-height: 100%;
}

.content table tr td:nth-child(2) {
	padding: 0;
}
/* 下拉菜单样式 */
select[name='currentStatus'] {
	margin-left: 42px;
}
/* /下拉菜单样式 */
input[name='inOrderId'] {
	width: 280px !important;
}
</style>
</head>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

				<h2 class="sub-header"><span>服务器管理</span></h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>系统代码</th>
								<th>服务器ID</th>
								<th>启动时间</th>
								<th>更新时间</th>
								<th>应用路径</th>
								<th>IP</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td>${i.systemCode}</td>
									<td>${i.systemServerId}</td>
									<td><fmt:formatDate value="${i.bootTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${i.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${i.contextPath}</td>
									<td>${i.ip}</td>
									<td><spring:message code="Status.${i.currentStatus}" /></td>
									<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
										<ul style="position: absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top: 28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
											<c:if test="${i.operate.update != null}">
												<li><a href="javascript:restart('${i.systemServerId}','update')" class="updatePage"> 更新页面 </a></li>
												<li ><a href="javascript:restart('${i.systemServerId}', 'install')" class="materialSelect">安装</a></li>
												<li><a href="javascript:restart('${i.systemServerId}', 'restart')" class="restart"> 重启 </a></li>

											</c:if>

										</ul>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="Pagination" style="text-align: center; width: 100%; background: #fff;">
					<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
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
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script>
		function show(id, b, c) {
			var str = $("#" + b + id).html();
			if (str.indexOf('.') < 0) {
				str = str.substring(0, c) + "...";
				$("#" + b + id).text(str);
			} else {
				$("#" + b + id).text(id);
			}
		}
		function showfk(id, b, c, d) {
			var str = $("#" + b + id).html();
			if (str.indexOf('.') < 0) {
				str = str.substring(0, c) + "...";
				$("#" + b + id).text(str);
			} else {
				$("#" + b + id).text(d);
			}
		}
		function showsj(id, b, c) {
			var str = $("#" + b + id).html();
			if (str.length <= 10) {
				$("#" + b + id).text(c);
			} else {
				c = str.substring(0, 10);
				$("#" + b + id).text(c);
			}
		}
	</script>
	<script>
		//重发通知
		function retransmission(a) {
			$.ajax({
				type : "GET",
				url : "/pay/notify/" + a + ".json",
				dataType : "json",
				success : function(data) {
					swal(data.message.message);
				}
			})
		}
	</script>
	<script>
		$(function() {
			var len = $(".table-responsive table td ").find("a").length;
			for (var i = 0; i < len; i++) {
				$(".table-responsive table td a")[i].ondragstart = dragstart;

			}
		})
		function dragstart() {
			return false;
		}
	</script>
	<script>
		$(".tools").click(function() {
			$(this).parent().parent().siblings().find(".toolbtns").hide();
			$(this).siblings(".toolbtns").toggle();
		})

		/* restart */
		function restart(systemServerId,mode){
			console.log(systemServerId,mode);
			$.ajax({
				type : "POST",
				url : "/server/update.json?serverId="+systemServerId+"&mode="+mode,
				dataType : "json",
				success : function(data) {
					swal(data.message.message)
				}
			})
		}


	</script>

</body>
</html>
