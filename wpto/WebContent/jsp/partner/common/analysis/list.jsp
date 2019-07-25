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
		<style>
			@media (min-width:768px){
				.dateFrom{width:34% !important;}
				.dateTo{width:34% !important;}
			}
			input[name='username'] {
				margin-left: 32px;
			}
			input[name="nickName"] {
				margin-left: 7px;
			}
			.table-responsive{
				padding-top: 20px;
			}
			.out{
				/*border-bottom: 40px #ccc solid;/*上边框宽度等于表格第一行行高*/
			    width: 80px;/*让容器宽度为0*/
			    height:40px;/*让容器高度为0*/
			    /*border-left: 80px red solid;/*左边框宽度等于表格第一行第一格宽度*/
			    position:relative;/*让里面的两个子容器绝对定位*/
			    margin-left: auto;
			    margin-right: auto;
			    margin-top: -45px;
			}
			b{
			font-style: normal;
			    display:block;
			    position:absolute;
			    top: 5px;
			    /*left: -75px;*/
			    width:35px;
			}
			em{
				font-style: normal;
			    /*display:block;*/
			    position:absolute;
			    width: 35px;
			    top: 15px;
			    /*left: -40px;*/
			}
			.spr{
				width: 30px;
				background: rgb(255, 48, 83); 
				color: rgb(255, 255, 255);
				display: -webkit-inline-box;
				text-align: -webkit-center;
			}
			.sph{
				width: 30px;
				background: rgb(221, 221, 221); 
				color: rgb(51, 51, 51);
				display: -webkit-inline-box;
				text-align: -webkit-center;
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
			<h2 class="title">${title}</h2>
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
		                <tr>
		                	<th></th>
		                	<th>12除余+1</th>
		                	<th>56除余+1</th>
		                	<th>110除余+1</th>
		                </tr>
	                </thead>
	                <tbody>
		                <tr>
		                	<th style="vertical-align: middle; text-align: center;">大/小数量</th>
		                	<td>
		                		<canvas id="myCanvas1" width="80px" height="40px">
								</canvas>
			                	<div class="out"> 
									<b>${big12}</b> 
									<em>${small12}</em> 
								</div>
		                	</td>
		                	<td>
								<canvas id="myCanvas2" width="80px" height="40px">
								</canvas>
			                	<div class="out"> 
									<b>${big56}</b> 
									<em>${small56}</em> 
								</div>
							</td>
		                	<td>
								<canvas id="myCanvas3" width="80px" height="40px">
								</canvas>
			                	<div class="out"> 
									<b>${big110}</b> 
									<em>${small110}</em> 
								</div>
							</td>
		                </tr>
		                <tr>
		                	<th style="vertical-align: middle; text-align: center;">开大比例</th>
		                	<td><fmt:formatNumber type="percent" value="${bili12}" /></td>
		                	<td><fmt:formatNumber type="percent" value="${bili56}" /></td>
		                	<td><fmt:formatNumber type="percent" value="${bili110}" /></td>
		                </tr>
		        	</tbody>
				</table>
				<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>开奖时间</th>
		                  <th>开奖号码</th>
		                  <th>12除余+1</th>
		                  <th>56除余+1</th>
		                  <th>110除余+1</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}" varStatus="status">
						<tr>
							<td><fmt:formatDate value="${i.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			                <td>${i.drawNo}</td>
			                
		                	<c:if test="${i.except12 > 6}">
		                		<td><span class="spr">${i.except12}</span></td>
		                	</c:if>
		                	<c:if test="${i.except12 <= 6}">
		                		<td><span class="sph">${i.except12}</span></td>
		                	</c:if>
		                	
		                	<c:if test="${i.except56 > 28}">
		                		<td><span class="spr">${i.except56}</span></td>
		                	</c:if>
		                	<c:if test="${i.except56 <= 28}">
		                		<td><span class="sph">${i.except56}</span></td>
		                	</c:if>
		                	
		                	<c:if test="${i.except110 > 55}">
		                		<td><span class="spr">${i.except110}</span></td>
		                	</c:if>
		                	<c:if test="${i.except110 <= 55}">
		                		<td><span class="sph">${i.except110}</span></td>
		                	</c:if>
		                	
						</tr>
					</c:forEach>
					</tbody>
				</table>
				
				
			</div>
			<div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
				<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
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
  <script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
  </script>
   <script type="text/javascript">
   for (var i=1; i <= 3; i++){
		var c=document.getElementById("myCanvas"+i);
		var ctx = c.getContext("2d"); 
			  
		// canvas表格斜线  
		ctx.beginPath();
		ctx.moveTo(0,40);
		ctx.lineTo(80,0);
		ctx.stroke();
   }
   /*var c=document.getElementById("myCanvas");
	//var c = document.getElementsByClassName("myCanvas");
	var ctx = c.getContext("2d"); 
  
  // canvas表格斜线  
	function aa(){  
      ctx.beginPath();
      ctx.moveTo(0,40);
      ctx.lineTo(80,0);
      ctx.stroke();
  	}  
  	aa();  
  	$(window).resize(function() {//当窗口大小发生变化时，重新画线  
      aa();  
  	})*/
  </script>
</html>
