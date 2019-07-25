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

<title>${systemName}-分成配置</title>

<!-- Bootstrap core CSS -->
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">

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
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<script>
	function ShareConfig(url,text) {
		//alert(productId);

			swal({
				title : "您确定要"+text+"分成配置吗？",
				showCancelButton : true,
				closeOnConfirm : false,
				confirmButtonText : "确认",
				confirmButtonColor : "#ec6c62"
			}, function() {
				$.ajax({
					type : "POST",
					url : url,
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
				<h2 class="sub-header"><span>分成配置</span></h2>
				<div class="content" style="overflow:hidden;">
        <form id="queryForm">
        <table width="100%"  >
        	<tr>
				<td>
					<span>商户号：</span>
					<input class="" type="text" name="shareUuid" id="shareUuid" value="">
				</td>
				<td>
					<span>支付卡类型：</span>
					<select name="payCardType" id="payCardType">
						<option value="">全部</option>
						<option value="UN">
							<spring:message code='PayCardType.UN'/>
						</option>
						<option value="DE">
							<spring:message code='PayCardType.DE'/>
						</option>
						<option value="CR">
							<spring:message code='PayCardType.CR'/>
						</option>
					</select>
				</td>
			</tr>
        	<tr>
        	
        	<td colspan="2"><span>分成对象：</span>
              <select name="objectType" id="objectType">
                  <option value="pay" selected="selected">商户支付结算(pay)</option>
              	  <option value="channel">支付通道结算(channel)</option>
				  <option value="product">product</option>
			  </select>
            </td>
          	<td colspan="2">
              <button class="btn btn-primary" style="width: 250px"  type="submit" value="查询" onClick="queryShareConfig()">
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
				<c:if test="${!empty addUrl }">
					<a href="${addUrl}" class="addmoban">+ 添加分成配置</a>
				</c:if>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>分成配置ID</th>
								<th>分成对象</th>
								<th>分成对象类型</th>
								<th>支付卡类型</th>
								<th>分成比例</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td>${i.shareConfigId}</td>
									<td>${i.operate.shareUser}</td>
									<td>
										<spring:message code='ObjectType.${i.objectType}'/>
									</td>
									<td>
										<spring:message code='PayCardType.${i.payCardType}'/>
									</td>
									<td><fmt:formatNumber  value='${i.sharePercent}' type="percent"   maxFractionDigits="3" minFractionDigits="3" /></td>
									  
									<td><spring:message code="Status.${i.currentStatus}"
											text="${i.currentStatus}" /></td>
									<td style="position: relative;"><span class="tools"
										style="right: 5px; cursor: pointer;"><img
											src="/theme/basic/images/tools.png"></span>
										<ul
											style="position: absolute; padding-right: 10px; padding-left: 10px; left: 50%; margin-left: -24px; top: 28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);"
											class="toolbtns">
												<a href="<c:url value='shareConfig/get/${i.shareConfigId}.shtml' />"><li
													class="materialSelect">查看</li></a>
											<c:if test="${i.objectType =='product' }">
												<li class="materialEdit" style="cursor: pointer"
													onclick="ShareConfig('/shareConfig/create.json?productId=' + ${i.objectId},$(this).text())">上架</li>
												<li class="materialDelete" style="cursor: pointer"
													onclick="ShareConfig('/shareConfig/delete.json?idList=' + ${i.shareConfigId},$(this).text())">下架</li>
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
	function queryShareConfig(){
		//验证信息准确性
		var shareUuidVal = $("#shareUuid").val();
		if(shareUuidVal.trim() == ""){
			$("#shareUuid").val("0");
		}
		query();
	}
</script>
</html>
