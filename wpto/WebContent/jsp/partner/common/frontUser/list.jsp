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
    <link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
	  <style>
		  input[name='username'] {
			  margin-left: 35px;
		  }
		  #queryForm input{
		  	border-radius: 5px;
		  	border:1px solid #ddd;
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
			<h2 class="sub-header"><span>${title}</span></h2>
			<div class="content">
				<form id="queryForm">
				<table width="100%">
					<tr>
						<td align="left" width="50%">
							注册日期：
							<span class="shijian">
								<input size="20" placeholder="开始日期" id="d11" class="calendarInput" name="createTimeBegin" type="text" value="<%=request.getParameter("createTimeBegin")==null?"":request.getParameter("createTimeBegin")%>" style="border:none;"/><img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
							<span class="shijian">
								<input size="20" placeholder="结束日期"  id="d12" class="calendarInput" name="createTimeEnd" type="text" value="<%=request.getParameter("createTimeEnd")==null?"":request.getParameter("createTimeEnd")%>" style="border:none;"/><img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
						</td>
						<td width="50%">
							UUID：<input type="text"  size="20" name="uuid" value="<%=request.getParameter("uuid")==null?"0":request.getParameter("uuid")%>">
							
						</td>


					</tr>
					<tr>
						<td>
							用户名：
							<input class="queryForm-btn-wt408" type="text" size="20" name="username" value="<%=request.getParameter("username")==null?"":request.getParameter("username")%>" style="margin-left:0;">
						</td>
						<td>
							昵称：
							<input type="text" size="30" name="nickName" value="<%=request.getParameter("nickName")==null?"":request.getParameter("nickName")%>">
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align:center;">
							<button class="btn btn-primary queryForm-btn-wt110 query" type="submit" value="查询"  onClick="query()" onSubmit="return false;" style="margin-left:0;">
								<span class="glyphicon glyphicon-search"></span>
								查询
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
		                  <th>用户UUID</th>
		                  <th>昵称</th>
		                  <th>性别</th>
		                  <th>注册时间</th>
		                  <th>地区</th>
		                  <th>状态</th>
		                  <if test="${isPlatformGenericPartner == true }"><th>渠道</th></if>
						  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.uuid}</td>
							<td>${i.nickName}</td>
							<td><c:if test="${i.gender == 1}">男</c:if><c:if test="${i.gender == 2}">女</c:if></td>
                			<td><fmt:formatDate value="${i.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                			<td>
                			<c:if test="${!empty $i.userConfigMap.get('country').dataValue }">
                				${i.userConfigMap.get('country').dataValue}
                			</c:if>
                			</td>
							<td><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></td>		
							<if test="${isPlatformGenericPartner == true }"><td>${i.userConfigMap.get('channel').dataValue}</td></if>
							
							<td><a href="#" onclick="delUser('${i.operate.del}','${i.username}','${i.nickName}')">删除</a></td>
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
  function delUser(url,username,nickName){
	  //alert(a+" --- "+b +"    " + c);
	  swal({ 
			title: "您确定要删除用户[" + nickName + "]及其所有关联数据吗？此操作不可撤销！",
			showCancelButton: true, 
			cancelButtonText:"取消",
			closeOnConfirm: true, 
			confirmButtonText: "确定", 
			confirmButtonColor: "rgb(90, 120, 158)" 
		}, function() { 
			$.ajax({
				type:"POST",
				url:url+'.json',
				data:'username='+username,
				dataType:'json',
				success:function(msg){
					//alert(JSON.stringify(msg));
					if(msg.message.operateCode==102008){
						swal({
							title:msg.message.message,
							showConfirmButton:true,
							confirmButtonColor: "#428bca" 
						},function(){
							window.location.reload();
						});
					}else{
						swal({
							title:msg.message.message,
							showConfirmButton:true,
							confirmButtonColor: "#428bca" 
						},function(){
							window.location.reload();
						});
					}
				}
			}) 
		});
  }
  </script>
</html>
