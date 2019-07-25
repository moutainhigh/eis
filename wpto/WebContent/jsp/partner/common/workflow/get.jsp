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
			<col width="100"/>
			<col width="*"/>
		</colgroup>
		<tr class="header">
			<td style="border: none;font-size: 18px" align="center" colspan="2">${workflow.workflowId}#工作流详情</td>
		</tr>	
		<tr>
			<th>名称</th>
			<td>
				<c:out value="${workflow.workflowName}"/>
			</td>
		</tr>
		<tr>
			<th>说明</th>
			<td>
				<c:out value="${workflow.workflowDesc}"/>
			</td>
		</tr>
		<tr>
			<th>操作对象</th>
			<td>
				<spring:message code="Entity.${workflow.targetObjectType}"/>
			</td>
		</tr>
		
		<tr>
			<th>状态</th>
			<td>
				<spring:message code="Status.${workflow.currentStatus}"/>
			</td>
		</tr>		
		
		<tr>
			<th>工作步骤</th>
			<td>
				<table>
				 <thead>
		                <tr>
		                  <th>步骤</th>
		                  <th>下一步骤</th>
		                  <th>步骤名称</th>
		                  <th>权重</th>
		                  <th>工作结果</th>		                  
		                  <th>状态</th>		                  
		                </tr>
		              </thead>
				<c:forEach var="i" items="${workflow.routeList }">
					<tr>
							<td>${i.step}</td>
							<td>${i.nextStep}</td>
							<td>${i.routeName}</td>
							<td>${i.priority}</td>
							<td>
								<spring:message code="Operate.${i.targetObjectOperateCode}"/>   <spring:message code="Entity.${i.targetObjectType}"/>
								把<spring:message code="Entity.${i.targetObjectType}"/>的<spring:message code="Entity.${i.targetObjectType}.currentStatus"/>设置为<spring:message code="Status.130005"/>
							</td>
							<td><spring:message code="Status.${i.currentStatus}"/></td>
					</tr>
				</c:forEach>
				</table>
			</td>
		</tr>	
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
  </body>
</html>
