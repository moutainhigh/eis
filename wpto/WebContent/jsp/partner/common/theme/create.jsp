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

    <title>${systemName}-新增模版</title>

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
	<script>
		$(function(){
			var length=$("tbody tr").length;
			for(var i=0;i<length;i++){
				$("tbody tr").eq(i).find("th").eq(0).css("text-align","right");
				$("tbody tr").eq(i).find("td").eq(0).css({"text-align":"left","paddingLeft":"5%"});
			}			
		})
	</script>
	<style>
		.fullScreenBg{
			position:fixed;
			background:#333;
			opacity:0.7;
			z-index:200;
			width:100%;
			height:100%;
		}
		.message{
			position:fixed;
			z-index:300;
			background:#fff;
			height:230px;
			width:300px;
			left:50%;
			top:230px;
			margin-left: -150px;
			text-align:center;
			border-radius:5px;
			overflow:hidden;
			padding-top:30px;
		}
		.sidebar{
			z-index:20;
		}
		.message .submit{
			border-radius:5px;
			height:40px;
			line-height:40px;
			text-align:center;
			width:100px;
			color:#fff;
			font-size:20px;
			background:#333;
			display:inline-block;
			position:absolute;
			bottom:20px;
			left:50%;
			margin-left:-50px;
			cursor:pointer;
		}
		.message .text{
			display:inline-block;
			font-size:18px;		
		}
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <div class="fullScreenBg" style="display:none;">	
			</div>
			<div class="message" style="display:none;">
				<span class="text"></span>
				<div class="submit">确定</div>
			</div>
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header">${title}</h2>
			<div class="table-responsive">	
	<form:form commandName="template" name="templateForm" id="templateForm" action="/template/create.shtml" onsubmit="return false">
	<form:hidden path="templateId"/>
				 
		<table class="table table-striped">
		
		<colgroup width="800">
			<col width="100"/>
			<col width="*"/>
		</colgroup>		
		<tr class="header">
			<td style="border: none;font-size: 18px" align="center" colspan="2">新增模版</td>
		</tr>
		<tr>
			<th>名称</th>
			<td>
				<form:input path="templateName"/>
				<form:errors path="templateName" cssClass="errorMessage"/>
			</td>
		</tr>
		<tr>
			<th>级别</th>
			<td>
				<form:input path="suggestLevel"/>
				<form:errors path="suggestLevel" cssClass="errorMessage"/>
			</td>
		</tr>
		<tr>
			<th>语言</th>
			<td>
				<form:errors path="languageId" cssClass="errorMessage"/>
				<select id="languageId" name="languageId">
				<c:forEach var="language" items="${languageList}">
					<option id="languageId"  value="${language.languageId}"
					<c:if test="${template.languageId==language.languageId}">selected </c:if>
					>${language.languageName}</option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>路径</th>
			<td>
				<form:input path="templateLocation"/>
				<form:errors path="templateLocation" cssClass="errorMessage"/>
			</td>
		</tr>
		<tr>
			<th>状态</th>
			<td>
				<form:errors path="currentStatus" cssClass="errorMessage"/>
				<select id="currentStatus" name="currentStatus">
				<c:forEach var="status" items="${statusCodeList}">
					<option id="currentStatus"  value="${status.id}"
					<c:if test="${template.currentStatus == status.id }">
						selected 
					</c:if>
					>
					<spring:message code="Status.${status.id}" text="${status.name}"/></option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr id="lastTr"> 
			<td align="center" colspan="2">
				<input type="submit" value="确认"/>
			</td>
		</tr>
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
	<script src="/theme/${theme}/js/jquery.form.3.5.1.js" type="text/javascript"></script>
	<script>
	$(function(){
		$("#templateForm").submit(function(e){
				e.preventDefault();
				$(this).ajaxSubmit(
				 {
					type:"POST",
					url:"/template/create.json",
					dataType:"json",
					data:$("#templateForm").serialize(),				
					success:function(data) {						
						$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
					},
					error:function(XMLResponse){
						alert("操作失败:" + XMLResponse.responseText);
					},
					
				});
			})
			$(".message .submit").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
			})
			
		
	})
			
	</script>
  </body>
</html>
