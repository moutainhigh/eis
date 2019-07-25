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
		<title>${systemName}-报表管理</title>
		<!-- Bootstrap core CSS -->
		<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

		<!-- Custom styles for this template -->
		<link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
		<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
	    <link href="/theme/${theme}/style/query.css" rel="stylesheet">
	    <link href="/theme/${theme}/all/style/page/page.css" rel="stylesheet">
	    <link href="/theme/${theme}/all/style/GridManager.css" rel="stylesheet">
	    <link href="/theme/${theme}/all/style/flatpickr.min.css" rel="stylesheet">
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
			.content table tr td:nth-child(2) {
				padding: 0;
			}
			/* 下拉菜单样式 */
			select[name='currentStatus'] {
				/*margin-left: 42px;*/
			}
			/* /下拉菜单样式 */
			input[name='inOrderId'] {
				/*width: 280px !important;*/
			}
			
			span.tdtitle {
			width: 104px;
			display: inline-block;
			text-align: right;
			}
			@media(max-width: 1500px)and(min-width: 1200px){
				.shijian input {
				    width: 114px !important;
				}
			}
			@media(max-width: 1200px){
				span.tdtitle{
					text-align: left;
				}
				#queryForm td {
			    width: 100%;
			    display: inline-block;
			    padding-bottom: 15px;
			}
			#queryForm table,#queryForm tr,#queryForm tbody{
				display: block;
			}
			.btn-primary{
				margin-left: 0!important;max-width: 100%;
			}
		}
		
			
	 </style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<!-- <h2 class="sub-header"><span>报表管理</span></h2> -->
			<ul id="tablist" class="nav nav-tabs" role="tablist" style="margin-bottom:36px;">  
				<li role="presentation" data-query=false class="active" id="memberTab" name="memberDiv"><a href="javascript:void(0)" role="tab" data-toggle="tab" style="outline:none;">商户报表</a></li>  
				<li role="presentation" data-query=false id="channelTab" name="channelDiv"><a href="javascript:void(0)" role="tab" data-toggle="tab" style="outline:none;">通道报表</a></li>  
			</ul> 
			<div class="tab-content">
				<div id="memberDiv" class="tab-pane" style="display:none;">
					<div class="content" style="overflow:hidden;">
						<form id="queryForm">
							<table width="100%" class="queryArea">
								<tr>
									<td style="width:33%;padding:0;">
										<span class="tdtitle">会员号：</span>
										<input style="width:208px;" type="text"  id="memberNo" name="memberNo" value=""/>
									</td>
									<td style="width:33%;padding:0;">
										<span class="tdtitle">维度：</span>
										<input name="reportTime" value="day" class="che" type="checkbox" checked>
										按天统计
										<input name="reportTime" value="month" class="che" type="checkbox">
										按月统计
									</td>
									<td style="width:33%;padding:0;">
										<span class="tdtitle">时间：</span>
										<input style="width:208px;" type="text" data-dateFormat="yyyy-MM-dd" class="flatpickr" id="memberReportTime"  value="">
										<input style="width:208px;display:none;" type="text" data-dateFormat="yyyy-MM" class="flatpickr" id="memberReportMonthTime"  value="">
									</td>
								</tr>
								
								<tr>
									<td style="width:33%;padding:0;">
										
									</td>
									<td style="width:33%;padding:0;">
										
									</td>
									<td style="width:33%;padding-top:18px;text-align:center;">
										<span style="width:208px;"  class="btn btn-primary" style="width: 426px;margin-left:106px;"  value="查询"  onClick="queryMemberAgain()">
											&nbsp;&nbsp;&nbsp;&nbsp;
											<span class="glyphicon glyphicon-search"></span>
											查询
											&nbsp;&nbsp;&nbsp;&nbsp;
										</span>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div class="table-responsive">	
						<p id="data_statistics" class="data_statistics" style="font-size:18px;">
							总笔数：
							<font class="statistics" id="member_reportCount" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							笔&nbsp;&nbsp;
							总金额：
							<font class="statistics" id="member_totalMoney" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							元&nbsp;&nbsp;
							手续费总收入：
							<font class="statistics" id="member_income" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							元&nbsp;&nbsp;
							总支出：
							<font class="statistics" id="member_expenditure" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							元&nbsp;&nbsp;
							总收益：
							<font class="statistics" id="member_get" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							元
						</p>
						<table id="member_grid_table" class="table table-striped">
				              
				              
						</table>
					</div>
				</div>
				<div id="channelDiv" class="tab-pane" style="display:none;">
					<div class="content" style="overflow:hidden;">
						<form id="queryForm">
							<table width="100%" class="queryArea">
								<tr>
									<td style="width:33%;padding:0;">
										<span class="tdtitle">通道号：</span>
										<input style="width:208px;" type="text"  id="payChannelId" name="payChannelId" value=""/>
									</td>
									<td style="width:33%;padding:0;">
										<span class="tdtitle">维度：</span>
										<input name="reportTime" value="day" class="che" type="checkbox" checked>
										按天统计
										<input name="reportTime" value="month" class="che" type="checkbox">
										按月统计
									</td>
									<td style="width:33%;padding:0;">
										<span class="tdtitle">时间：</span>
										<input style="width:208px;" type="text" data-dateFormat="yyyy-MM-dd" class="flatpickr" id="channelReportTime"  value="">
										<input style="width:208px;display:none;" type="text" data-dateFormat="yyyy-MM" class="flatpickr" id="channelReportMonthTime"  value="">
									</td>
								</tr>
								<tr>
									<td style="width:33%;padding:0;">
										
									</td>
									<td style="width:33%;padding:0;">
										
									</td>
									<td style="width:33%;padding-top:18px;text-align:center;">
										<span style="width:208px;"  class="btn btn-primary" style="width: 426px;margin-left:106px;"  value="查询"  onClick="queryChannelAgain()">
											&nbsp;&nbsp;&nbsp;&nbsp;
											<span class="glyphicon glyphicon-search"></span>
											查询
											&nbsp;&nbsp;&nbsp;&nbsp;
										</span>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div class="table-responsive">	
						<p id="data_statistics" class="data_statistics" style="font-size:18px;">
							总笔数：
							<font class="statistics" id="channel_reportCount" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							笔&nbsp;&nbsp;
							总金额：
							<font class="statistics" id="channel_totalMoney" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							元&nbsp;&nbsp;
							通道总支出：
							<font class="statistics" id="channel_expenditure" style="font-size: 25px;font-weight: bold;color: green;">
								0
							</font>
							元
						</p>
						<table id="channel_grid_table" class="table table-striped">
				              
				              
						</table>
					</div>
				</div>
			</div>
			
			
			
		</div>		
      </div>
	<!-- 悬浮框展示 -->
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
    <script src="/theme/${theme}/js/util.js"></script>
    <script src="/theme/${theme}/all/js/Map.js"></script>
    <script src="/theme/${theme}/all/js/GridManager.js"></script>
    <script src="/theme/${theme}/all/js/report.js"></script>
    <script src="/theme/${theme}/all/js/flatpickr.js"></script>
    <script src="/theme/${theme}/all/js/DateUtil.js"></script>
	<script>
	$(document).ready(function(){
		$('#currentStatus option').each(function(){
			var zhi = $(this).attr('value');
			var zhixz = $(this).parent().attr('value');

			if (zhi==zhixz) {
				$(this).attr('selected','selected');
				//console.log(zhi+','+zhixz);
			}else{
				$(this).removeAttr('selected');
				//console.log(zhi+','+zhixz);
			}
		})
	})


		function show(id, b, c){
			var str = $("#"+b+id).html();
			if(str.indexOf('.') < 0){
				str=str.substring(0,c)+"...";
				$("#"+b+id).text(str);
			} else {
				$("#"+b+id).text(id);
			}
		}
		function showfk(id, b, c, d){
			var str = $("#"+b+id).html();
			if(str.indexOf('.') < 0){
				str=str.substring(0,c)+"...";
				$("#"+b+id).text(str);
			} else {
				$("#"+b+id).text(d);
			}
		}
		function showsj(id,b,c){
			var str = $("#"+b+id).html();
			if(str.length <= 10){
				$("#"+b+id).text(c);
			} else {
				c=str.substring(0,10);
				$("#"+b+id).text(c);
			}
		}
	</script>


  </body>
</html>
