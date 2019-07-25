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
	<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<script src="/theme/${theme}/js/sweetalert.min.js"></script>
	<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
</head>
<style>
	.modalHeader{
			height:40px;
			background: #5C6F87;;
			line-height:40px;
			color:#fff;
			font-weight:bold;
			padding-left:3%;
			border-radius: 8px 8px 0 0;
		}
		.Modal{
			position:absolute;
			height:auto!important; 
			height:300px; 
			min-height:300px;
			top:20%;
			width:500px;
			left:50%;
			margin-left:-150px;
			z-index:1500;
			background:#fff;
			border-radius:8px;
			border:#e4e4e4 1px solid;
		}
		.modalBody{
			height: 72%;
			padding-top:10px;
			padding-left:10px;
			padding-right:10px;
			padding-bottom:8px;
		}
		.modalFooter{
			text-align:right;
			padding-right:5%;
			padding-bottom:3%;
		}
		.modalFooter a{
			color:#fff;
			border-radius:5px;
			margin-left:5%;
		}
		.modalFooter .submit{
			background:#C34A4E;
		}
		.modalFooter .cancel{
			background:#d0d0d0;
		}
		@media (max-width:768px){
			.Modal{
				width:80%;
				left:50%;
				margin-left:-40%;
			}
		}
		.close{
			float: right;
			margin-right: 2%;
			font-size: 30px;
			font-weight: 300;
			line-height:40px;
			color: #fff;
			text-shadow: 0 1px 0 #fff;
			opacity:1;
		}
		.Modal a:hover{
			color:#fff;
		}		
		.smallTitle{
			width:100%;
			max-width100%;
			overflow-x: auto;
			padding-top:23px;
		}
		.buttonWithoutColor{   
			padding:5px 10px; 
			border-radius:2px;
			border: 1px solid #337ab7;
		}
		.buttonColor{
			background-color:#337ab7;
			color:#FFFFFF;
			border-radius:2px;
		}	
		.smallTitle a img{
			margin-right:5px; margin-top:-1px; width:15px; height:15px;
		}
		/* 表格 */
		.table-responsive td{
		   border-top:0px!important;
		   border-bottom:1px solid #ddd;
		   height: 50px;
	    }
	    .table{
	    	border:1px solid #ddd;
	    }
		.table-striped>tbody>tr:nth-of-type(even) {
			background-color: #f9f9f9;
		}
		.table-striped>tbody>tr:nth-of-type(odd) {
			background-color: #FFF;
		}
		.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th{
			vertical-align: middle;
		}
		
		
		
		
		.tankuang tr th{
			text-align:center;
			height:40px;
		}
		.tankuang tr .identification{
			text-align:right;
			padding:0px 15px;
		}
		.tankuang tr td{
			height:50px;
		}
		.tankuang table select{
			height:30px;
		}
		.tankuang{
			width:500px;
			border:1px solid #ddd;
			left:32%;
			position: absolute;
			z-index: 1200;
			top: 10%;
			display:none;	
		}
		@media (max-width:968px){
			.tankuang{
				width:80%;
				left:30%;
				margin-left:-20%;
			}
		}
		.hidebg{
			height:100%;
			width:100%;
			background-color:#333;
			position: fixed;
			z-index:1100;
			filter:alpha(opacity=70); /* IE浏览器下设置透明度为60% */
			opacity:0.7;  /* 非IE浏览器下设置透明度为60% */
			display:none;"
		}

/*新版搜索样式	*/	
		.wid1200{
			/*width:1200px;*/
			padding-bottom:80px;
			/*border:#efefef 1px solid;*/
			
		}
		.title{
			margin-top: 0;
			color: #5D5D5D;
			font-size: 27px;
			border-bottom: 1px dashed #5D5D5D;
			padding-bottom: 5px;
			margin-bottom: 29px;
		}
		.title span{
			border-left: 4px solid #5D5D5D;
    padding-left: 6px;
    display: inline-block;
    font-size: 20px;
		}
		.queryFormBox{
			/*width:1100px;
			border:1px solid #efefef;*/
			position:relative;
			background-color: #eee;
			border-radius: 5px;
			padding: 20px 20px 38px 20px;
		}
		.queryFormBox table tr td>input{
			height:32px;
			border:1px #e4e4e4 solid;
			margin-left:9px;
			margin-top:18px;
			outline:0;
			background-color: #fff;
    border-radius: 5px;
    padding:0 5px;
		}
		.queryFormBox select{  
			height:32px;
			border:1px #e4e4e4 solid;
			margin-left:9px;
			margin-top:18px;
			outline:0;
			border-radius: 5px;
		}
		.queryFormBox table tr td>div{
			border:1px #efefef solid;
			margin-top:18px;
			float:left;
		}
		.queryFormBox table tr td .dateBoxDiv{
			height:37px;
			border:1px #efefef solid;
			
			width:38px;
			float:left;
			text-align:center;
			line-height:37px;
		}
		.queryFormBox table tr td{
			height:64px;
			/*padding-left:30px;*/
			display:inline-block;width: 31%!important;
		}
		.queryFormBox table tr td span{			
			line-height:64px;
		}
		.queryFormBox table tr td:nth-child(1){
			width:313px;
		}
		.queryFormBox table tr:nth-child(1) td:nth-child(1) input{
			width:90px;
		}
		.queryFormBox table tr td:nth-child(2){
			width:315px;
		}
		.queryFormBox table tr:nth-child(1) td:nth-child(2) input{
			width:174px;
		}
		.queryFormBox table tr td:nth-child(3){
			width:313px;
		}
		.queryFormBox table tr:nth-child(1) td:nth-child(3) input{
			width:164px;
		}
		.infoPlus{
			color:#f06b77;
			font-size:12px;
			padding-left:8px;
		}
		.queryFormBox table tr:nth-child(2) td:nth-child(1) select{
			width:202px;
		}
		.queryFormBox table tr:nth-child(2) td:nth-child(2) select{
			width:230px;
		}
		.queryFormBox table tr:nth-child(2) td:nth-child(3) input{
			width:165px;
			outline:0;
			height:32px;
			border-radius: 5px;
		}
		.queryFormBox table tr:nth-child(2) td:nth-child(3) span{
			float:left;
		}
		.dateBoxArrow{
			
		}
		#queryBtn{
			/*background:#0F8A0E;*/
			color:#ffffff;
			text-align:center;
			font-size:18px;
			border:none;
			height:40px;
			width:140px;
			/*padding-left:40px;*/
		}
		
		.searchIcon{
			position:absolute;
			top:12px;
			left:30px;
		}
		.moreQueryBtn{
			position:absolute;
			height:30px;
			width:210px;
			left:50%;
			bottom:0;
			margin-left:-105px;
			text-align:center;
			line-height:30px;
			font-size:12px;
			color:#919191;
			background:#f5f5f5;
			border:1px solid #efefef;
		}
		.moreQueryBtn img{
			margin-right:8px;
		}
				
		@media(min-width: 1200px){
			.queryBox{
			position:absolute;
			right:30px;
			top:64px;}
		}
		@media(max-width: 1200px){
			.queryFormBox table tr td{
				width:100%!important;
			}
			.queryBox{
				position: relative;text-align: center;
			}
			

		}
		@media(max-width: 768px){
			#queryBtn{
				width:100%!important;
			}
		}
	</style>

<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="modalout">
		<div class="Modal">
			<div class="modalHeader">
				<a class="close">×</a>
				<span>添加文章</span>
			</div>
			<div class="modalBody">
				<form action="" style="margin:auto 0; height:auto!important; 
		height:200px; 
		min-height:200px;">
					<div style="font-size:16px;padding:10px 0 0 10px;">文章类型:</div>
					<div style="margin-left:100px; margin-top:-23px">
						<c:forEach var="docType" items="${documentTypeList}" varStatus="in">
							<span style="line-height: 30px; margin-right:20px;">
								<c:choose>
									<c:when test="${in.count == 1}">
										<input type="radio" name="documentType" value="${docType.documentTypeId}" checked="checked" />${docType.documentTypeName}
									</c:when>

									<c:otherwise>
										<input type="radio" name="documentType" value="${docType.documentTypeId}" />${docType.documentTypeName}
									</c:otherwise>
								</c:choose>
							</span>
							<br />
						</c:forEach>
					</div>

				</form>
			</div>
			<div class="modalFooter">
				<input type="hidden" class="modalProductId" value="" />
				<a href="javascript:void(0);" class="btn submit">确定</a>
				<a href="javascript:void(0);" class="btn cancel">取消</a>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="wid1200">
					<div class="title"><span>${title}</span></div>
					<div class="queryFormBox">
						<form id="queryForm">
							<table width="100%" cellspacing="0" cellpadding="0" border="0">
								<tr>
									<td><span>文章数字ID:</span><input type="text" name="udid" value="${documentCriteria.udid}"></input><span class="infoPlus">*默认请设置为0</span></td>
									<td><span>文章唯一代码:</span><input type="text" name="documentCode" value="${documentCriteria.documentCode}"></input></td>
									<td><span>标题:</span><input type="text" name="title" value="${documentCriteria.documentCode}"></input><span
										 class="infoPlus">*模糊查询</span></td>
								</tr>
								<tr>
									<td>
										<span>文章类型:</span>
										<select name="documentTypeId">
											<option value="0">全部类型</option>
											<c:forEach var="s" items="${documentTypeList}">
												<option value="${s.documentTypeId}" <c:if test="${s.documentTypeId == documentCriteria.documentTypeId}">
													selected </c:if> >${s.documentTypeName}</option>
											</c:forEach>
										</select>
									</td>
									<td>状态:
										<select name="currentStatus">
											<c:forEach var="s" items="${documentStatusList}">
												<option value="${s.id}">${s.id}[
													<spring:message code="Status.${s.id}" />]</option>
											</c:forEach>
										</select>
									</td>
									<td>
										<span>创建时间:</span>
										<div style="display:inline-block;"><input size="20" id="d11" class="calendarInput" name="createTime" type="text"
											 value="" style="border:none;" /></div>
										<div class="dateBoxDiv"><img class="dateBoxArrow" onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd'})" src="/theme/${theme}/images/datebox_arrow.png"
											 width="23" height="22" align="absmiddle" style="cursor:pointer;"></div>
									</td>
								</tr>

							</table>
							<div class="moreQueryBtn"><img src="/theme/${theme}/images/moreIcon.png">更多搜索条件点这里</div>
							<div class="queryBox" onClick="query()"><input class="btn btn-primary" id="queryBtn" type="submit" value="查询"
								 onSubmit="return false;" /><img src="/theme/${theme}/images/queryIcon.png" class="searchIcon"></div>
						</form>

					</div>
					<c:if test="${!empty addUrl}">
						<c:choose>
							<c:when test="${documentTypeList.size() == 1 }">
								<a class="addLink addmoban" href="${addUrl }?mini=true&documentTypeId=${documentTypeList[0].documentTypeId}">+
									添加文章</a>
							</c:when>
							<c:otherwise>
								<a class="addLink addmoban" href="#" onClick="addProduct()">+ 添加文章</a>
							</c:otherwise>
						</c:choose>
					</c:if>

					<div class="table-responsive" style="margin-top:12px;">
						<table class="table table-striped">
							<thead style="color: #333; border: none;border-bottom: 3px solid #0F8A0E;">
								<tr>
									<th>文章ID</th>
									<th>文章类型</th>
									<th>标题</th>
									<th>默认栏目</th>
									<th>链接地址</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="i" items="${rows}">
									<tr>
										<td>${i.udid}</td>
										<td>
											<spring:message code="DocumentTypeId.${i.documentTypeId}" />
										</td>
										<td>${i.title}</td>
										<td>${i.defaultNode.name}/${i.defaultNode.path}</td>
										<td><a href="${i.operate.get('preview')}" target="_blank">${i.operate.get('url')}</a></td>
										<td>
											<spring:message code="Status.${i.currentStatus}" />
										</td>
										<td style="position:relative;">
											<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
											<ul style="position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
											 class="toolbtns">
												<c:if test="${!empty i.operate.update }">
													<a href="${i.operate.update}.shtml">
														<li class="materialtCompile ">
															编辑
														</li>
													</a>
												</c:if>
												<c:if test="${!empty i.operate.depAccept }">
													<a href="#" onclick="depAccept(${i.id},${i.currentStatus},${i.documentDataMap.workflowInstanceId.dataValue})">
														<li class="materialApproval">
															审批
														</li>
													</a>
												</c:if>
												<c:if test="${!empty i.operate.published }">
													<a href="#" onclick="published(${i.id},${i.currentStatus},${i.documentDataMap.workflowInstanceId.dataValue})">
														<li class="materialIssue ">
															发布
														</li>
													</a>
												</c:if>
												<c:if test="${!empty i.operate.del }">

													<li class="materialDelete" ahref="${i.id}">
														删除
													</li>

												</c:if>
											</ul>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>

				<div class="Pagination" style="text-align:center;width:100%;background:#fff;">
					<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
				</div>
			</div>
		</div>
	</div>
	<div class="delete">
		<div class="box">
			<h3>确定删除文章吗？</h3>
			<span class="confirm">确定</span><span class="cancel">取消</span>
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
		//${addUrl }?mini=true&documentTypeId=123001
		//添加文章
		function addProduct() {
			$(".modalout").css("display", "block");
		}
		//提交
		$(".Modal .submit").click(function () {
			var value = $("input[name='documentType']:checked").val();

			location.href = "/document/create.shtml?mini=true&documentTypeId=" + value;


		})
		//取消
		$(".Modal .cancel").click(function () {
			$(".modalout").css("display", "none");
		})
		//关闭
		$(".Modal .close").click(function () {
			$("#contentTextarea").empty();
			$(".modalout").css("display", "none");
		})
	</script>
	<script>

		$(function () {
			$('body').on("click", ".tools", function () {
				$(this).parent().parent().siblings().find(".toolbtns").hide();
				$(this).siblings(".toolbtns").toggle();
			})
		});
	</script>
	<script>
		function depAccept(a, b, c) {
			$.ajax({
				type: "GET",
				url: "/document/update/status/" + a + ".json?udid=" + a + "&currentStatus=130004",
				dataType: "json",
				success: function (data) {
					swal(data.message.message);
					location.reload();
				}
			})
		}

		function published(a, b, c) {
			$.ajax({
				type: "GET",
				url: "/document/update/status/" + a + ".json?udid=" + a + "&currentStatus=130005",
				dataType: "json",
				success: function (data) {
					swal(data.message.message);
					location.reload();
				}
			})
		}
		$('.materialDelete').on('click', function () {
			var href = $(this).attr('ahref');
			$('.delete').css('display', 'block');
			$('.delete .confirm').attr('href', href);
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
				url: "/document/delete.json?idList=" + a,
				dataType: "json",
				success: function (data) {
					swal(data.message.message);
					location.reload();
				}
			})
		}
	</script>
</body>

</html>