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
		<title>${systemName}-支付订单</title>
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
			#queryForm td{
				width:50%;
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
			<h2 class="sub-header"><span>支付订单</span></h2>
			<div class="content" style="overflow:hidden;">
				<form id="queryForm">
				<table width="100%"  >
					<tr>
						<td align="left" >
							<span class="tdtitle">时间：</span>
							<span class="shijian">
								<input size="20" placeholder="开始时间" id="d11" class="calendarInput" name="startTimeBegin" type="text" value="<%=request.getParameter("startTimeBegin")==null?"":request.getParameter("startTimeBegin")%>" />

								<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){start()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">

							</span>
							<span class="shijian">
								<input size="20" placeholder="结束时间"  id="d12" class="calendarInput" name="startTimeEnd" type="text" value="<%=request.getParameter("startTimeEnd")==null?"":request.getParameter("startTimeEnd")%>"/>

								<img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){end()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
						</td>
						<td >
							<span class="tdtitle">系统订单号：</span>
							<input type="text"  name="transactionId" value="<%=request.getParameter("transactionId")==null?"":request.getParameter("transactionId")%>"/>
							
						</td>
					</tr>
					<tr>
						
						<td><span class="tdtitle">入口订单号：</span>
							<input type="text" class="" name="inOrderId" value="<%=request.getParameter("inOrderId")==null?"":request.getParameter("inOrderId")%>">
						</td>
						<td >
							<span class="tdtitle">出口订单号：</span>
							<input type="text"   name="outOrderId" value="<%=request.getParameter("outOrderId")==null?"":request.getParameter("outOrderId")%>">
						</td>
					</tr>
					<tr>
						<td >
							<span  class="tdtitle">付款人类型：</span>
							<select name=payFromAccountType id="payFromAccountType" value="${payFromAccountType}">
								<option value="121002">商户</option>
								<option value="121003">前端用户</option>
								
							</select>					
						</td>
						<td >
							<span class="tdtitle">付款人：</span>
							<input type="text" class="" name="payFromUserName"  value="<%=request.getParameter("payFromUserName")==null?"":request.getParameter("payFromUserName")%>">
						</td>
						
					</tr>
					<tr >
						<td ><span class="tdtitle">识别码：</span>
						<input type="text" name="identify" value="<%=request.getParameter("identify")==null?"":request.getParameter("identify")%>"></td>
						<td ><span class="tdtitle">支付类型:</span>
						<select name="payTypeId" id="payTypeId" value="${payTypeId}">
								<option value="0">全部</option>
						
								<c:forEach var="s" items="${payTypeList}">
									<option value="${s.payTypeId}">${s.name}</option>
								</c:forEach>
						</select>	
						</td>
					
					</tr>
					<tr>
						<td >
							<span class="tdtitle">状态：</span>
							<% 
								String invitersV=request.getParameter("currentStatus");  
								request.setAttribute("invitersV",invitersV); 
							%>
							<select name="currentStatus" id="currentStatus" value="${invitersV}">
								<option value="">全部状态</option>
								<c:forEach var="s" items="${statusList}">
									<option value="${s}"><spring:message code="Status.${s}" /></option>
								</c:forEach>
							</select>
							<!-- <select name="currentStatus" id="currentStatus">
								<option value="">全部状态</option>
								<c:forEach var="s" items="${statusList}">
									<option value="${s}"><spring:message code="Status.${s}" /></option>
								</c:forEach>
							</select> -->
						</td>  
						<td>
							<button class="btn btn-primary" style="width: 426px;margin-left:106px;"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="glyphicon glyphicon-search"></span>
								查询
								&nbsp;&nbsp;&nbsp;&nbsp;
							</button>
						</td>
					</tr>
					
				</table>
				</form>			
			</div>
			<p>
			<c:if test="${!empty CSVDownload}">
			<button class="btn educe-now" name="download" value="1" style="background-color: #B0BDD0;">↓ 导出当页数据</button>
			<button style="background-color: #5C6F87; color: #fff;" class="btn educe-all" name="download" value="2">↓ 导出所有数据</button>
			</c:if>
			</p>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>系统订单号</th>
		                  <th>入口订单号</th>
						  <th>付款方式</th>
		                  <th>付款人</th>
		                  <th>订单金额</th>
		                  <th>成功金额</th>
		                  <th>手续费</th>
		                  <th>账户余额</th>
		                  <th>发起时间</th>
		                  <th>结束时间</th>
		                  <th>状态</th>
		                  <th>操作</th>
                		</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
	                    <td>
	                    	${i.transactionId}
							<!-- <c:choose>
								<c:when test="${fn:length(i.transactionId) > 8}">
									<span id="xt${i.transactionId}" onClick="show('${i.transactionId}','xt','8')">${fn:substring(i.transactionId, 0, 8)}...</span>
								</c:when>
								<c:otherwise>   -->
							   		
							  <!--  	</c:otherwise> 
							</c:choose> -->
						</td>
	                    <td>${i.inOrderId}
							<!-- <c:choose>
								<c:when test="${fn:length(i.inOrderId) > 8}">
									<span id="rk${i.inOrderId}" onClick="show('${i.inOrderId}','rk', '8')">${fn:substring(i.inOrderId, 0, 8)}...</span>
								</c:when>
								<c:otherwise>  
							   		
							   	</c:otherwise> 
							</c:choose> -->
						</td>
	                   
						<td>${i.data.payTypeName}</td>
						<!-- <c:set var="refBuyTransactionId" value="${fn:replace(i.refBuyTransactionId, ',', '<br/>')}" />
	                    <td><a href="/order/get/${i.refBuyTransactionId}.shtml" title="${i.refBuyTransactionId}" style="display:inline-block;text-decoration:none;color:#222;">${refBuyTransactionId} </a></td>-->
	                  	<td>
	                  		<span title="${i.data.fromAccountName}#${i.payFromAccount}">
	                  		<c:choose>  
							    <c:when test="${fn:length(i.data.fromAccountName) > 7}">  
							        <span id="fk${i.transactionId}" onClick="showfk('${i.transactionId}','fk' ,'7','${i.data.fromAccountName}')">${fn:substring(i.data.fromAccountName, 0, 7)}...</span>
							    </c:when>  
							   <c:otherwise>  
							   		${i.data.fromAccountName}
							   	</c:otherwise>  
							</c:choose>  
							</span>
	                  	</td>
	                  	
	                  	
	                  	<td><fmt:formatNumber value="${i.faceMoney}" pattern="#0.00"/></td>
	                  	<td><fmt:formatNumber value="${i.realMoney}" pattern="#0.00"/></td>
	                  	<td><fmt:formatNumber value="${i.commission}" pattern="#0.000"/></td>
	                  	<td><fmt:formatNumber value="${i.balance}" pattern="#0.00"/></td>
						<td>
							<span><fmt:formatDate value='${i.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/></span>
							<!-- <span id="start${i.transactionId}" onClick="showsj('${i.transactionId}','start' ,'<fmt:formatDate value='${i.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>')"><fmt:formatDate value="${i.startTime}" pattern="yyyy-MM-dd"/></span> -->
						</td>
						<td>
							<span><fmt:formatDate value='${i.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/></span>
							<%--<span id="end${i.transactionId}" onClick="showsj('${i.transactionId}','end' ,'<fmt:formatDate value='${i.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>')"><fmt:formatDate value="${i.endTime}" pattern="yyyy-MM-dd"/></span>--%>
						</td>
	                  	<td>
	                  		<font
	                  		<c:choose>
	                  			<c:when test="${i.currentStatus==710010}">
	                  				 color="#006600"	                  			
	                  			</c:when>
	                  			<c:otherwise>
	                  				color="#000000"
	                  			</c:otherwise>
	                  		</c:choose>
							>
	                  		<spring:message code="Status.${i.currentStatus}" /></font></td>
	                   	<td style="position:relative;">
							<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position:absolute; width: 78px; padding-right: 10px; padding-left: 10px; margin-left: -36px; top:38px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
								<c:if test="${i.operate.get != null }">
									<a href="/pay/get/${i.transactionId}.shtml">
										<li class="materialSelect">
											查看
										</li>
									</a>
									<a href="javascript: void(0)"  onclick="retransmission('${i.transactionId}')">
										重发通知
									</a>
								</c:if>
								
								<c:if test="${i.operate.isShow != null}">
									<a href="javascript: void(0)" onclick="Reconfirm('${i.transactionId}')">
										再次确认
									</a>
								</c:if>
								<c:if test="${i.operate.refund != null }">
									<a href="/pay/refund.json?transactionId=${i.transactionId}">
										<li>
											退款
										</li>
									</a>
								</c:if>
								<c:if test="${i.operate.book != null }">
									<a href="javascript: void(0)" onclick="bookKeeping('${i.transactionId}')">
										账户补帐
									</a>
								</c:if>
							</ul>
	                   	</td>
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
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
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
	<!-- 导出数据 -->
	<script>
	$('.educe-now').on('click',function(){
		location.href = window.location.href+'&download=1';
	});
	$('.educe-all').on('click',function(){
		location.href = window.location.href+'&download=2';
	});
	</script>
<script>

		//重发通知
		function retransmission(a) {
			$.ajax({
				type:"GET",
				url:"/pay/notify/"+a+".json",
				dataType:"json",
				success:function(data){
					var backnum = data.message.operateCode;
					var backmessage = data.message.message;
					var alerttext = '';
					if(backnum==102008){
						alerttext=backmessage+'发送成功！';
					}else{
						alerttext=backmessage;
					}
					swal(alerttext);
				}
			})
		}
		//再次确认
		function Reconfirm(a) {
			var dataarry = {'transactionId':a};
			$.ajax({
				type:"GET",
				url:"/pay/confirm"+".json",
				dataType:"json",
				data:dataarry,
				success:function(data){
					var backnum = data.message.operateCode;
					var backmessage = data.message.message;
					var alerttext = '';
					// if(backnum==102008){
					// 	alerttext=backmessage+'发送成功！';
					// }else{
					// 	alerttext=backmessage;
					// }
					swal('再次确认成功！');
				},
				error:function(){
					swal('再次确认失败！');
				}
			})
		}
		//重新申请对账
		function bookKeeping(a) {
			var dataarry = {'payId':a};
			$.ajax({
				type:"POST",
				url:"/pay/bookKeeping"+".json",
				dataType:"json",
				data:dataarry,
				success:function(data){
					var backnum = data.message.operateCode;
					var backmessage = data.message.message;
					var alerttext = '';
					 if(backnum==102008){
					 	alerttext=backmessage;
					 }else{
					 	alerttext="操作失败"+backmessage;
					 }
					swal(alerttext);
				},
				error:function(){
					swal('再次确认失败！');
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
	// 时间
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
			$('input#d11').val(startv)
		}else{
			return;
		}
	}
	function end(){
		y=parseInt($dp.cal.getP('y'));
		M=parseInt($dp.cal.getP('M'));
		d=parseInt($dp.cal.getP('d'));
		if (y==tyear && M==tmonth && d==tday) {
			$('input#d12').val(endv)
		}else{
			return;
		}
	}
	</script>
  </body>
</html>
