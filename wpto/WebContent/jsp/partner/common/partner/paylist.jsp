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

		<title>${systemName}-支付方式列表</title>

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
			<h2 class="sub-header"><span>用户支付方式列表</span></h2>
		
			<div class="table-responsive">
				<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>用户UUID</th>
		                  <th>用户名</th>
		                  <th>支付方式</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.uuid}</td>
							<td>${i.username}</td>
							<td class="mode">
								<c:forEach var="r" items="${i.payType}" varStatus="varStatus">
						             <span class="name">${varStatus.index+1}.${r.name}</span>
						       	</c:forEach>
							</td>
							<td>
								<a href="<c:url value='/partnerPayTypeRelation/get/${i.uuid}.shtml'/>" title="修改" style="background-image:url(/theme/basic/images/tools.png);width:14px;height:14px;display:inline-block;"></a>
							</td>
								<!--<a href="/partner/get/${i.uuid}.shtml">查看</a>-->
							
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
	$('.mode').each(function(){
		console.log($.trim($(this).text().length));
		if ($.trim($(this).text().length)=='17') {
			$(this).text('用户未开启任何支付方式');
		};
	})
  </script>
</html>
