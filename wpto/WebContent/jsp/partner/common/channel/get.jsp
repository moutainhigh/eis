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

<!-- Custom styles for this workflow -->
<link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

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
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header">${title}</h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<colgroup width="800">
							<col width="100" />
							<col width="*" />
						</colgroup>
						<tr class="header">
							<td style="border: none;font-size: 18px" align="center"
								colspan="2">${partner.uuid}#渠道详情</td>
						</tr>
						<tr>
							<th>登录名</th>
							<td><c:out value="${partner.username}" /></td>
						</tr>
						<tr>
							<th>昵称</th>
							<td><c:out value="${partner.nickName}" /></td>
						</tr>
						<tr>
							<th>状态</th>
							<td><spring:message code="Status.${partner.currentStatus}" />
							</td>
						</tr>
						<tr>
							<th>本用户关联的角色</th>
							<td><c:forEach var="r" items="${partner.relatedRoleList}">
									<div>
										<a href="/partnerRole/get/${r.roleId }.shtml">${r.roleId}：${r.roleName}
										</a>
									</div>
								</c:forEach></td>
						</tr>
						<tr>
							<th>推广地址</th>
							<td><c:if test="${not empty frontInviteUrl}">
									<div>
										<a href="${frontInviteUrl}" target="_blank">${frontInviteUrl}</a>
									</div>
								</c:if> <c:if test="${empty frontInviteUrl}">
									<div style="color:red;">没有推广码</div>
								</c:if></td>
						</tr>
						<c:if test="${not empty qrCode}">
							<tr>
								<th>推广二维码</th>
								<td><c:if test="${not empty qrCode}">
										<div>
											<img src="${qrCode}" />
										</div>
									</c:if></td>
							</tr>
						</c:if>
						<tr>
							<td style="width:20%;text-align:right;">帐号关联</td>
							<td style="width:80%;text-align:left;">
							</td>							
						</tr>
						<c:forEach items="${gameProductList}" var="gameProduct">
							<tr class="gameType">
								<td style="width:20%;text-align:right;" data-id="${gameProduct.gameId}" id="gameId" >${gameProduct.gameName }</td>
								<td style="width:80%;text-align:left;">
									<!-- <input type="text" name="gameRoleName" id="gameRoleName"  placeholder="请输入您的游戏角色名称"> -->
								</td>
							</tr>
						</c:forEach>
						<div id="gameRoles" style="display:none;">
							<c:forEach items="${partnerGameNameList }" var="partnerGameName">
								<input id="${partnerGameName.gameId }" value="${partnerGameName.gameRoleName }">
							</c:forEach>
						</div>
					</table>
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
	<script src="/theme/${theme}/js/vendor/holder.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script src="/theme/${theme}/js/validateUtils.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			//给帐号赋值
			$(".gameType").each(function(){
				var $this = $(this);
				var gameId = $this.find("td:first").data("id");
				var val = $.trim($("#"+gameId).val());
				if(!ValidateUtils.isNull(val)){
					$this.find("td:last").html(val);
				}
			});
		})
	</script>
</body>
</html>
