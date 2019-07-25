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

		<title>${systemName}-商户管理</title>

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
		<style>
			@media (min-width:768px){
				.dateFrom{width:34% !important;}
				.dateTo{width:34% !important;}
			}
			input[name='username'] {
				/*margin-left: 32px;*/
			}
						
@media (max-width: 1200px){
	.content table tr td {
	    width: 100%!important;
	    display: inline-block;
	}
	.content table tr td input{
		width: 80%;
	}
}
@media (max-width: 768px){
	.content table tr td span.tabtitle{
		display: inline-block;
		width: 100%;
	}
	.content table tr td input{
		width: 100%;
	}
	.shijian{
		width:100%;
	}
	input[name="nickName"]{
		margin-left: 0;
	}
};

		</style>
  </head>
  <body>
  	
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<%	String str_date1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()); //将日期时间格式化 %>
	
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>商户管理</span></h2>

			
			<div class="content">
				<form id="queryForm">
					<table width="100%">
						
						<tr>
							<td >
								<span class="tabtitle">注册日期：</span>
								<span class="shijian">
									<input size="20"  id="d11" class="calendarInput" placeholder="开始日期" name="queryBeginTime" type="text" value='<fmt:formatDate value="${payStatCriteria.queryBeginTime}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border:none;"/>
									<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){start()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
								</span>
								<span class="shijian">
									<input size="20"placeholder="结束日期"  id="d12" class="calendarInput" name="queryEndTime" type="text" value='<fmt:formatDate value="${payStatCriteria.queryEndTime}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border:none;"/>
									<img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){end()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
								</span>
							</td>
							<td>
								<span class="tabtitle">UUID：</sapn><input type="text"   name="uuid" value="0"/>

								
							</td>
						</tr>
						<tr>
							<td>
								<span class="tabtitle">用户名：</span><input class="" type="text" name="username" value="">
							</td>
							<td>
								<span class="tabtitle">昵 称：</span><input type="text"   name="nickName" value="">
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align:center;">
								<button class="btn btn-primary" type="submit" value="查询"  onClick="query()"  onSubmit="return false;">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<span class="glyphicon glyphicon-search" ></span>
									查询&nbsp;&nbsp;&nbsp;&nbsp;
								</button>
							</td>
						</tr>
					</table>

				</form>			
			</div>
			<p>
				<c:if test="${!empty addUrl }">
					<a href="${addUrl }" class="addmoban btn btn-primary queryForm-btn-wt110">+ 添加商户</a>
				</c:if>
			</p>
			<div class="table-responsive">
				<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>用户UUID</th>
		                  <th>用户名</th>
		                  <th>昵称</th>
		                  <th>性别</th>
		                  <th>创建时间</th>
		                  <th>上次登录时间</th>
		                  <th>状态</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.uuid}</td>
							<td>${i.username}</td>
							<td>${i.nickName}</td>
							<td><c:if test="${i.gender == 1}">男</c:if><c:if test="${i.gender == 2}">女</c:if></td>
                			<td><fmt:formatDate value="${i.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                			<td><fmt:formatDate value="${i.lastLoginTimestamp}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></td>		
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<a href="/merchant/get/${i.uuid}.shtml"><li class="materialSelect">查看</li></a>
								</ul>
								<!--<a href="/partner/get/${i.uuid}.shtml">查看</a>-->
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
        </div>
      </div>
    </div>
    <div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
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
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
	var date = new Date();
	var tyear = date.getFullYear();
	var tmonth = date.getMonth()+1;
	var tday = date.getDate();
	var startv = tyear+'-'+tmonth+'-'+tday+' '+'00:00:00';
	var endv = tyear+'-'+tmonth+'-'+tday+' '+'23:59:59';
	function start(){
		y=parseInt($dp.cal.getP('y'));
		M=parseInt($dp.cal.getP('M'));
		d=parseInt($dp.cal.getP('d'));
		
		if (y==tyear && M==tmonth && d==tday) {
			$('input[name="queryBeginTime"]').val(startv)
		}else{
			return;
		}
	}
	function end(){
		y=parseInt($dp.cal.getP('y'));
		M=parseInt($dp.cal.getP('M'));
		d=parseInt($dp.cal.getP('d'));
		if (y==tyear && M==tmonth && d==tday) {
			$('input[name="queryEndTime"]').val(endv)
		}else{
			return;
		}
	}
  </script>
</html>
