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

	<title>${systemName}-产品分组数据</title>

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
		input[name='username'] {
	margin-left: 35px;
}

#queryForm input {
	border-radius: 5px;
	border: 1px solid #ddd;
}
</style>
</head>

<body>

	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<%	String str_date1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()); //将日期时间格式化 %>

	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>

			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header">
					<span>产品分组数据</span>
				</h2>
				<c:if test="${!empty addUrl}">
					<p><a href="${addUrl}.shtml" class="addmoban">+ 添加分组数据</a></p>
					<p><a href="#" class="addmoban" id="createMaxObjectId">+ 新增objectId</a></p>
				</c:if>
				<div class="content">
					<form id="queryForm">
						<table width="100%">


							<tr>
								<td colspan="2" style="text-align: center;">
									<button class="btn btn-primary queryForm-btn-wt110 query" type="submit" value="查询" onClick="query()" onSubmit="return false;"
									 style="margin-left: 0;">
										<span class="glyphicon glyphicon-search"></span> 查询
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
								<th>分组ID</th>
								<th>主键</th>
								<th>分组说明</th>
								<th>级别</th>
								<th>分组键</th>
								<th>分组值</th>
								<th>对象类型</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td>${i.objectId}</td>
									<td>${i.id}</td>
									<td>${i.groupDesc}</td>
									<td>${i.level}</td>
									<td>${i.groupKey }</td>
									<td>${i.groupValue }</td>
									<td>${i.objectType} # ${i.groupTarget}</td>
									<td>
										<spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" />
									</td>

									<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
										<ul style="position: absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top: 28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
										 class="toolbtns">
											<a href="./productGroup/get/${i.id}.shtml">
												<li class="materialtCompile ">查看</li>
											</a>

											<c:if test="${!empty i.operate.update }">
												<a href="./productGroup/update/${i.id}.shtml">
													<li class="materialtCompile ">编辑</li>
												</a>
											</c:if>


											<c:if test="${!empty i.operate.delete }">

												<li class="materialDelete" ahref="${i.id}">删除</li>

											</c:if>
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

	<div class="delete">
		<div class="box">
			<h3>确定删除吗？</h3>
			<span class="confirm">确定</span><span class="cancel">取消</span>
		</div>
	</div>
	<div class="Pagination" style="text-align: center; width: 100%; background: #fff;">
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

	$(function () {
		$('body').on("click", ".tools", function () {
			$(this).parent().parent().siblings().find(".toolbtns").hide();
			$(this).siblings(".toolbtns").toggle();
		})
		
		$("#createMaxObjectId").on('click', function(){
			$.ajax({
				type: "POST",
				url: "/productGroup/create/objectId.json",
				dataType: "json",
				success: function (data) {
					swal("新生成的objectId是:" + data.maxObjectId + "\n请牢记这个数字");
				}
			})
		});

		$('.materialDelete').on('click',function(){
			var href = $(this).attr('ahref');
			$('.delete').css('display','block');
			$('.delete .confirm').attr('href',href);
		});
		$('.delete .confirm').on('click', function () {
			$('.delete').css('display', 'none');
			del($(this).attr('href'));
		});
		$('.delete .cancel').on('click', function () {
			$('.delete').css('display', 'none');
		});
		function del(a) {
			$.ajax({
				type: "GET",
				url: "/productGroup/delete.json?idList=" + a,
				dataType: "json",
				success: function (data) {
					swal(data.message.message);
					location.reload();
				}
			})
		}
	});
</script>

</html>