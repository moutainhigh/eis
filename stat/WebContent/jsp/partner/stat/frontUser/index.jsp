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

		<title>${systemName}-前端用户统计</title>

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
			@media (max-width:768px){
				.search_form td{
					display:block !important;
				}
			}

			.table-responsive td{
				display:table-cell;
				vertical-align:middle;
				line-height:100%;
			}
		</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>${title}</span></h2>
				<div class="content" >
					<form id="queryForm">
					<table width="100%" >
						<tr>
							<td>
								
							</td>
							<td></td>
						</tr>
						<tr>
							<td align="left" style="width: 44%;">
								时间：从
								<span class="shijian">
									<input id="d11" class="calendarInput" name="queryBeginTime" type="text" value="" style="border:none;"/>
									<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
								</span>
								到
								<span class="shijian">
									<input id="d12" class="calendarInput" name="queryEndTime" type="text" value="" style="border:none;"/>
									<img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
								</span>
							</td>
							<td style="width: 52%;">渠道:
								<select name="inviters" style="width:50%;">
									<option value="">所有渠道</option>
									<c:forEach var="s" items="${inviterList}">
										<option value="${s.uuid}" >${s.nickName}
									</c:forEach>
								</select>
								<button class="btn btn-primary query"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<span class="glyphicon glyphicon-search"></span>
									查询
									&nbsp;&nbsp;&nbsp;&nbsp;
								</button>
							</td>
						</tr>
						<tr>
							<td >
								<span class="payStat-radio-right">
									<input type="checkbox" <c:if test="${frontUserStatCriteria.requireSummary == true}"> checked </c:if>name="requireSummary" value="true">显示小计
								</span>
								<span>
									<input type="checkbox" <c:if test="${frontUserStatCriteria.groupByDay == true}"> checked </c:if>name="groupByDay" value="true">按天统计
								</span>
							</td>

							<td >
								<span class="payStat-radio-right">
									<input type="checkbox" <c:if test="${frontUserStatCriteria.groupByInviter == true}"> checked </c:if>name="groupByInviter" value="true">分渠道
								</span>
							</td>
						</tr>
					</table>
				</form>			
			</div>
			<p>
				<button class="btn btn-primary educe-now" name="download" value="1">导出当页数据</button>
				<button class="btn btn-primary educe-all" name="download" value="2">导出所有数据</button>
			</p>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>统计时间</th>
		                  <th>注册人数</th>
		                  <th>激活人数</th>
		                  <th>登录人数</th>
		                  <th>渠道</th>
                		</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
	                    <td>${i.statTime}</td>
	                    <td>${i.registerCount}</td>
	                    <td>${i.activeCount}</td>						
	                  	<td>${i.loginCount}</td>
	                  	<td>${i.data.inviterName}</td>
						</tr>
					</c:forEach>
					<c:if test="${frontUserStatCriteria.requireSummary == true}">
					<tr>
		                  <td>本页小计</td>
		                  <td>${totalCount}</td>
		                  <td>${successCount}</td>
		                  <td>${totalMoney}</td>
		                  <td>${successMoney }</td>
		                  <td></td>
		                  <td></td>
                	</tr>
                	</c:if>	
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
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script>
		//重发通知
		function retransmission(a) {
			$.ajax({
				type:"GET",
				url:"/pay/notify/" + a + ".json",
				dataType:"json",
				success:function(data){
					swal(data.message.message);
				}
			})
		}
	</script>
	<script>
	$(function(){
		var len=$(".table-responsive table td ").find("a").length;
		for(var i=0;i<len;i++){
			$(".table-responsive table td a")[i].ondragstart=dragstart;
			
		}
	})
	function dragstart(){return false;}
	</script>
	<script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
	</script>
  </body>
</html>
