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
			<c:if test="${!empty addUrl }">
				<a href="${addUrl }">添加工作流</a>
			</c:if>
			<span class="addmoban addaddmoban">+ 新增提现银行卡</span>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>工作流ID</th>
		                  <th>名称</th>
		                  <th>说明</th>
		                  <th>操作对象</th>
		                  <th>状态</th>
		                  <th>操作</th>		                  
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.workflowId}</td>
							<td>${i.workflowName}</td>
							<td>${i.workflowDesc}</td>
							<td><spring:message code="Entity.${i.targetObjectType}"/></td>
							<td><spring:message code="Status.${i.currentStatus}"/></td>
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<c:if test="${i.operate.get != null }"><a href="${i.operate.get}"><li class="materialSelect">查看</li></a></c:if>
									<c:if test="${i.operate.del != null }"><a href="${i.operate.del}.shtml?idList=${i.workflowId}"><li class="materialDelete">删除</li></a></c:if>
									<c:if test="${i.operate.update != null }"><a href="${i.operate.update}.shtml"><li class="materialEdit">编辑</li></a></c:if>
								</ul>
								<!--<c:if test="${i.operate.get != null }"><a href="${i.operate.get}.shtml">查看</a></c:if>
								<c:if test="${i.operate.del != null }"><a href="${i.operate.del}.shtml?idList=${i.workflowId}">删除</a></c:if>
								<c:if test="${i.operate.update != null }"><a href="${i.operate.update}.shtml">编辑</a></c:if>-->
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
        </div>
      </div>
    </div>
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>

	<div class="increase addincrease" style="display: none;">
		<div class="increasebox">
			<h3>新增提现银行卡</h3>
			<form:form action="" id="workflowForm" commandName="workflow" name="workflowForm">
				<p><span class="tdtitle">工作流id：</span><input class="bigwidth calendarInput" name="workflowId" type="text"></p>
				<p><span class="tdtitle">名称：</span><input class="bigwidth calendarInput" name="workflowName" type="text"></p>
				<p><span class="tdtitle">说明：</span><input class="bigwidth calendarInput" name="workflowDesc" type="text"></p>
				<p><span class="tdtitle">状态：</span>
					<form:select path="currentStatus" >
						<c:forEach items="${statusCodeList}" var="i">
							<option value="${i.id}" <c:if test="${i.id == 100001}"> selected </c:if> ><spring:message code="Status.${i.id}" /></option>
						</c:forEach>
					</form:select>
					<%--<select id="province" name="province"></select>--%>
					<%--<select id="city" name="city" ></select>--%>
					<%--<script class="resources library" src="area.js" type="text/javascript"></script>--%>
					<%--<script type="text/javascript">_init_area();</script>--%>
				</p>
				<p><span class="tdtitle">开户行：</span><input class="bigwidth calendarInput" name="issueBank" type="text"></p>
				<p><span class="tdtitle">开户人姓名：</span><input class="bigwidth calendarInput" name="bankAccountName" type="text"></p>
				<p><span class="tdtitle">银行账号：</span><input class="bigwidth calendarInput" name="bankAccountNumber" type="text"></p>
				<p style="text-align: center;margin-top: 35px;" class="btnp"><span class="confirm querenbtn">确 认</span><span class="closespan">取 消</span></p>
			</form:form>
		</div>
	</div>    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
  </body>
  <script>
	  $('.addaddmoban').on('click',function(){
		  $('.increase').css('display','block');

	  });
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
  </script>
</html>
