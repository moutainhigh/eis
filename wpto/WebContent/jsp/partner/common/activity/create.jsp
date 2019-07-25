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

    <title>${systemName}-新建<spring:message code="ActivityType.${activity.activityType}"/>活动</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

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

  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header">新建<spring:message code="ActivityType.${activity.activityType}"/></h2>
			<div class="table-responsive">	
	<form:form commandName="activity" name="activityForm" action="/activity/create.shtml">

		<table class="table table-striped">

		<colgroup width="800">
			<col width="100"/>
			<col width="*"/>
		</colgroup>
		<tr class="header">
			<td style="border: none;font-size: 18px" align="center" colspan="2">${title}</td>
		</tr>
		<tr>
			<th>活动类型</th>
			<td>
				<form:hidden path="activityType" value="${activity.activityType}"/>
				<spring:message code="ActivityType.${activity.activityType}"/>
			</td>
		</tr>
		<tr>
			<th>名称</th>
			<td>
				<form:input path="activityName"/>
				<form:errors path="activityName" cssClass="errorMessage"/>
			</td>
		</tr>
		<tr>
			<th>代码[只支持英文和数字]</th>
			<td>
				<form:input path="activityCode"/>
				<form:errors path="activityCode" cssClass="errorMessage"/>
			</td>
		</tr>

		<tr>
			<th>处理程序</th>
			<td>
				<select id="processor" name="processor">
				<c:forEach var="p" items="${activityProcessorList}">
					<option id="processor" name="processor" value="${p}"><spring:message code="ActivityProcessor.${p}" /></option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>开始时间</th>
			<td>
				<input id="d01" class="calendarInput" name="beginTime" type="text" value="<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %>" style="border:none;"/><img onclick="WdatePicker({el:'d01',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>
				<form:errors path="beginTime" cssClass="errorMessage"/>
			</td>
		</tr>
		<tr>
			<th>结束时间</th>
			<td>
				<input id="d02" class="calendarInput" name="endTime" type="text" value="<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %>" style="border:none;"/><img onclick="WdatePicker({el:'d02',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>
				<form:errors path="endTime" cssClass="errorMessage"/>
			</td>
		</tr>
		<c:if test="${!empty activity.data }">
		<tr>
			<th>扩展配置</th>
			<td>
				<c:forEach var="extraData" items="${activity.data}">
					<spring:message code="DataName.${extraData.key}" /><input name="data.${extraData.key}" type="text" width="18" height="20" />
					<br/>
				</c:forEach>
			</td>
		</tr>

		</c:if>
		<tr>
			<th>状态</th>
			<td>
				<form:errors path="currentStatus" cssClass="errorMessage"/>
				<select id="currentStatus" name="currentStatus">
				<c:forEach var="status" items="${statusCodeList}">
					<option id="currentStatus"  value="${status}"><spring:message code="Status.${status}" /> </option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr id="lastTr"> <td align="center" colspan="2"><input type="submit" value="确认" onclick="btnSubmit(this)"/></td></tr>
		</table>
	</form:form>
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
