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
<title>${systemName}-支付统计</title>
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
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
</head>
<style type="text/css">
	.subtotaltr.none{
		display: none;
	}
</style>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header"><span>支付统计(每5分钟更新)</span></h2>
				<div class="content">
					<form id="queryForm">
						<table width="100%">
							<tr>
								<td>
									
								</td>
								<td></td>
							</tr>
							<tr>
								<td align="left" style="width: 44%;">时间：<span class="shijian"> <input size="20" placeholder="开始时间" id="d11" class="calendarInput" name="queryBeginTime" type="text"
										value='<fmt:formatDate value="${payStatCriteria.queryBeginTime}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border: none;" /> <img
										onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor: pointer;">
								</span><span class="shijian"> <input size="20" placeholder="结束时间" id="d12" class="calendarInput" name="queryEndTime" type="text"
										value='<fmt:formatDate value="${payStatCriteria.queryEndTime}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border: none;" /> <img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor: pointer;">
								</span>
								</td>
								<td style="width: 52%;">渠道: 
								<% 
								String invitersV=request.getParameter("inviters");  
								request.setAttribute("invitersV",invitersV); 
								%>
								<select id="inviters" name="inviters" id="selecte" class="shortselect" value="${invitersV}">
									<option value="">所有渠道</option>
									<c:forEach items="${inviterList}" var="s" varStatus="vs">  
			         					<option value="${s.uuid}">${s.nickName}</option>
							 		</c:forEach>
								</select>

									<button class="btn btn-primary query" type="submit" value="查询" onClick="query()" onSubmit="return false;">
										&nbsp;&nbsp;&nbsp;&nbsp; <span class="glyphicon glyphicon-search"></span> 查询 &nbsp;&nbsp;&nbsp;&nbsp;
									</button>
								</td>
							</tr>
							<tr>
								<td><span class="payStat-radio-right"><input type="checkbox" <c:if test="${payStatCriteria.requireSummary == true}"> checked </c:if> name="requireSummary" value="true">显示小计</span>
									<span><input type="checkbox" <c:if test="${payStatCriteria.groupByDay == true}"> checked </c:if> name="groupByDay" value="true">按天统计</span></td>
								<td><span class="payStat-radio-right"><input type="checkbox" <c:if test="${payStatCriteria.groupByInviter == true}"> checked </c:if> name="groupByInviter" value="true">分渠道</span>
									<span><input type="checkbox" <c:if test="${payStatCriteria.groupByPayMethodId == true}"> checked </c:if> name="groupByPayMethodId" value="true">分支付方式</span></td>
							</tr>
						</table>
					</form>
				</div>
				<p><c:if test="${!empty CSVDownload}">
					<button class="btn btn-primary educe-now" name="download" value="1">导出当页数据</button>
					<button class="btn btn-primary educe-all" name="download" value="2">导出所有数据</button>
				</c:if>
				</p>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>统计时间</th>
								<th>付款人数</th>
								<th>成功付款人数</th>
								<th>发起付款金额</th>
								<th>成功付款金额</th>
								<th>毛利</th>
								<th>付费率</th>
								<th>ARPU值</th>
								<th>付款方式</th>
								<th>渠道</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${rows}">
								<tr>
									<td>${i.statTime}</td>
									<td>${i.totalCount}</td>
									<td>${i.successCount}</td>
									<td><fmt:formatNumber type="number" value="${i.totalMoney}" pattern="0.00" maxFractionDigits="2" /></td>
									<td><fmt:formatNumber type="number" value="${i.successMoney}" pattern="0.00" maxFractionDigits="2" /></td>
									<td>${i.profit}</td>
									<td class="pay-rate"><fmt:formatNumber type="number" value="${i.data.payRate * 100}" maxFractionDigits="1" />%</td>
									<td>${i.data.arpu}</td>
									<td>${i.data.payMethodName}</td>
									<td>${i.data.inviterName}</td>
								</tr>
							</c:forEach>
								<tr class="subtotaltr" style="background-color: #E4E9F0;" data-requireSummary="${requireSummary}">
									<td>本页小计</td>
									<td><fmt:formatNumber value="${totalCount}" pattern="#"/></td>
									<td><fmt:formatNumber value="${successCount}" pattern="#"/></td>
									<td><fmt:formatNumber value="${totalMoney}" pattern="#0.00"/></td>
									<td><fmt:formatNumber value="${successMoney }" pattern="#0.00"/></td>
									<td>${profit}</td>
									<td><fmt:formatNumber value="${payRate}" type="percent" /></td>
									<td><fmt:formatNumber value="${arpu}" /></td>
									<td></td>
									<td></td>
								</tr>
							
						</tbody>
					</table>
				</div>
				<div class="Pagination" style="text-align: center; width: 100%; background: #fff;">
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

$('#inviters option').each(function(){
	var zhi = $(this).attr('value');
	var zhixz = $(this).parent().attr('value');
	if (zhi==zhixz) {
		$(this).attr('selected','selected');
	}else{
		$(this).removeAttr('selected');
	}
})

	if($('.subtotaltr').attr('data-requireSummary')=='true'){
		$('.payStat-radio-right').css('display','block');
		$("input[name='requireSummary']").attr('checked','checked');
		$('.subtotaltr').removeClass('none');
	}else{
		$("input[name='requireSummary']").removeAttr('checked');
		$('.subtotaltr').addClass('none');
	}
	$("input[name='requireSummary']").on('click',function(){
		if($(this).is(':checked')){
			$('.subtotaltr').removeClass('none');
		}else{
			$('.subtotaltr').addClass('none');
		}
	})


		//重发通知
		function retransmission(a) {
			$.ajax({
				type : "GET",
				url : "/pay/notify/" + a + ".json",
				dataType : "json",
				success : function(data) {
					swal(data.message.message);
				}
			})
		}

		//根据付费率改变颜色
		function payRateColor() {
			var payRate = [];
			for (var i = 0; i < $(".pay-rate").length; i++) {
				payRate.push(parseInt($(".pay-rate")[i].innerHTML));
			}
			for (var c = 0; c < payRate.length; c++) {
				if (payRate[c] < 60 && 30 <= payRate[c]) {
					$('.pay-rate').eq(c).css("color", "#F5CC3E");
				} else if (payRate[c] < 30) {
					$('.pay-rate').eq(c).css("color", "red");
				}
			}
		}
		payRateColor();
	</script>
	<script>
		$(function() {
			var len = $(".table-responsive table td ").find("a").length;
			for (var i = 0; i < len; i++) {
				$(".table-responsive table td a")[i].ondragstart = dragstart;

			}
			$(".educe-now").click(function() {
				console.log(window.location.href);
			})
		})
		function dragstart() {
			return false;
		}
	</script>
	<script>
		$(".tools").click(function() {
			$(this).parent().parent().siblings().find(".toolbtns").hide();
			$(this).siblings(".toolbtns").toggle();
		})
	</script>
</body>
</html>
