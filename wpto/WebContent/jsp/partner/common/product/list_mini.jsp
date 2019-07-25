<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- ����3��meta��ǩ*����*������ǰ�棬�κ��������ݶ�*����*������� -->
<meta name="description" content="">
<meta name="author" content="">

<title>${systemName}-${title}11</title>

<!-- Bootstrap core CSS -->
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

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
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet"
	type="text/css" />
</head>
<body style="padding: 0px;">
	
	<table class="table table-striped">
		<thead>
			<tr>
				<th></th>
				<c:forEach var="column" items="${displayColumns}">
					<th><spring:message code="DataName.${column.columnName}" /></th>
				</c:forEach>
			</tr>
		</thead>
		
		<tbody>
			
			<c:forEach var="i" items="${rows}">
				<tr>
					<td>
						<input type="radio" name="activityProduct" value=" ${i.productCode}"/>
					</td>
					<c:forEach var="column" items="${displayColumns}">
						<td>
							<c:choose>
								<c:when test="${column.columnType == 'extra'}">
									<c:set var="dataValue" value="${i.productDataMap[column.columnName].dataValue}" />
								</c:when>
								<c:otherwise>
									<c:set var="dataValue" value="${i[column.columnName]}" />
								</c:otherwise>
							</c:choose>
							
							<c:choose>
								<c:when test="${column.useMessagePrefix != null}">
									<spring:message code="${column.useMessagePrefix}.${dataValue}" />
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${column.format == 'date'}">
											<fmt:formatDate value="${dataValue}" pattern="yyyy-MM-dd HH:mm:ss" />
										</c:when>
										<c:otherwise>
											${dataValue}
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="Pagination" style="text-align: center; width: 100%; background: #fff;">
		<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
	</div>
</body>
</html>