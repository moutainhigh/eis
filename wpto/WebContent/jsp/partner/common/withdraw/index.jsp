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

    <title>${systemName}-出款订单</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
	  <script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
	  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	  <style>
		  ul{
			  list-style: none;
		  }
		  /* 页面标题 */
		  .title {
			  background: #00a1e9;
			  color: #ffffff;
			  width: 200px;
			  height: 54px;
			  font-size: 25px;
			  line-height: 54px;
			  text-align: center;
			  margin-bottom: 0;
		  }
		  /* /页面标题 */
		  /* table外层div,及table样式 */
		  .content {
			  width: 100%;
			  border: 1px solid #f5f5f5;
			  position: relative;
			  border-radius: 5px;
			  background-color: #eee;
    		 margin-bottom: 26px;
		  }
		  .content table tr td {
			  height: 50px;
			  padding-left: 30px;
			  
		  }
		  /* /table外层div,及table样式 */

		  /* 下拉框及input通用 */
		  #queryForm select , #queryForm input[type='text'] {
			  height: 35px;
			  border: 1px #BFBFBF solid;
			  outline: none;
		  }
		  /* /下拉框及input通用 */

		  .shijian {
			  border: 1px #efefef solid;
			  display: inline-block;
		  }
		  .clr {
			  color: red;
			  margin: 0 5px;
			  font-size: 16px;
		  }

		  /* 导出数据按钮 */
		  .apply-cash ,.educe-now , .educe-all{
			  margin-left: 10px;
		  }
		  /* /导出数据按钮 */

		  /* 查询及小计 */
		  .query , .show-total {
			  margin-left: 10px !important;
		  }
		  /* /查询及小计 */

		  select[name=bankName] {
			 
			  width: 390px;
		  }
		  .shoukuan {
			  position: relative;
		  }

		  input[name=fuzzyBankAccountName],select {
			  width:390px;
		  }
		  /*商户订单号
		  */
		  .order-id {  
			  width: 390px;
		  }
		  input, select {
		    border-radius: 5px;
		    padding: 2px 4px;
		    border: 1px solid #ddd!important;
		}
		.subtotaltr.none{
			display: none;
		}
		.sweet-alert h2{
			font-size: 17px;
		}
		tr td span:nth-child(1){
			width: 90px;
			display: inline-block;
			text-align: right;
		}
		.colorgreen{
			color: green!important;
		}
	  </style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>出款订单</span></h2>
			<div class="content">
				<p style=" margin: 0 0 10px;border-bottom: 2px solid #fff;
    padding-left: 43px; padding: 9px 43px;">
					<b>还可付款：</b><b class="clr"><fmt:formatNumber value="${money.transitMoney}" pattern="#0.00"/></b>元，<b>账户余额：</b><b class="clr"><fmt:formatNumber value="${money.incomingMoney}" pattern="#0.00"/></b>元
					<c:if test="${!empty addUrl }">
						<a href="${addUrl}" class="btn btn-primary apply-cash addmoban" style="padding:6px 11px!important;">申请出款</a>
					</c:if>
				</p>
				<form id="queryForm"  >
					<table width="100%" cellspacing="0" cellpadding="0" border="0" >
						<tr>
							<td align="left">
								<span >开始时间：</span>
								<span class="shijian" >
									<input size="20"  id="d11" class="calendarInput" name="beginTimeBegin" type="text" value='<fmt:formatDate value="${withdrawCriteria.beginTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border:none;"/>
									<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){start()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
								</span>
								<span>结束时间</span>：								
								<span class="shijian" >
									<input size="20"  id="d12" class="calendarInput" name="beginTimeEnd" type="text" value='<fmt:formatDate value="${withdrawCriteria.beginTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss" />' style="border:none;"/><img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){end()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
								</span>
							</td>
							<% 
							String invitersV=request.getParameter("inviters");  
							request.setAttribute("invitersV",invitersV); 
							%>
							<td>
								<span >商户：</span>
								<select name="inviters" value="${invitersV}">
									<option value="" >所有商户</option>
									<c:forEach var="s" items="${inviterList}">
										<option value="${s.uuid}" >${s.nickName}
									</c:forEach>
								</select> 
							</td>
						
						</tr>
						<tr>
							<td >
								<span>出款金额：</span>
								<input type="text" name="minMoney" class="order-id" placeholder="最小出款金额" value="0" style="width:135px;"><span style="font-size:12px;color:#959595;">最小金额 / 元</span>
								<input type="text" name="maxMoney" value="0" class="order-id" placeholder="最大出款金额" style="width:135px;margin-left:14px;"><span style="font-size:12px;color:#959595;">最大金额 / 元</span>
							</td>
							<td >
								<% 
								String bankNameV=request.getParameter("bankName");  
								request.setAttribute("bankNameV",bankNameV); 
								%>
								<span>银行：</span>
								<select name="bankName" >
									<option value="">所有银行</option>
									<c:forEach var="x" items="${bankNames}">
										<option value="${x}" >${x}</option>>
									</c:forEach>
								</select>
							</td>
							
							

						</tr>
						<tr>
							<td >
								<span>商户订单号：</span>
								<input type="text" name="inOrderId" class="order-id" value="${withdrawCriteria.inOrderId}">
							</td>
							<td>
								<span>系统订单号：</span>
								<input type="text" name="transactionId" class="order-id" value="${withdrawCriteria.transactionId}">
							</td>
							
						</tr>
						<tr>
							<td class="shoukuan">
								<span>收款人名称：</span>
								<input type="text" name="fuzzyBankAccountName" value="">
							</td>
							
							<td>
								<span>银行卡号：</span>
								<input name="bankAccountNumber" type="text" style="width:390px;"/>
							</td>
							
						</tr>
						<tr>
							<td>
								<% 
								String currentStatusV=request.getParameter("currentStatus");  
								request.setAttribute("currentStatusV",currentStatusV); 
								%>
								<span>出款状态：</span>
								<select name="currentStatus" value="${currentStatusV}">
									<option value="" >所有状态</option>
									<c:forEach var="s" items="${statusList}">
								  		<option value="${s}">
										<spring:message code="Status.${s}" />
									</c:forEach>
								</select> 
							</td>
							<td >
								<button   type="submit"  class="btn btn-primary query"  onclick="query()"  onSubmit="return false;" style="width:390px;margin-left:95px!important;">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<span class="glyphicon glyphicon-search" style="width:18px;"></span>
									查询&nbsp;&nbsp;&nbsp;&nbsp;
								</button>
							</td>
						</tr>
						<tr>
							<td style="height:20px;">
								<p>
									<input class="show-total" name="requireSummary" value="true" type="checkbox" <c:if test="${withdrawCriteria.requireSummary == true}"> checked </c:if>>
									显示小计
								</p>
							</td>
							<td></td>
						</tr>
					</table>
				</form>
			</div> 
			<p>
				<c:if test="${!empty CSVDownload}">
				<button class="btn btn-primary educe-now" name="download" value="1">导出当页数据</button>
				<button class="btn btn-primary educe-all" name="download" value="2">导出所有数据</button>
				</c:if>
			</p>
			<div class="table-responsive">
				 <table class="table table-striped">
		              <thead>
		                <tr>
		                  <c:if test="${isPlatformGenericPartner == true }"><th>出款用户</th></c:if>
		                  <th>出款交易ID</th>
		                  <th>入口订单号</th>
		                  <th>出款金额</th>
		                  <th>批付数量</th>
		                  <th>开始时间</th>
		                  <th>完成时间</th>
		                  <th>收款账户</th>
		                  <th>状态</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
		                  <c:if test="${isPlatformGenericPartner == true }"><td>${i.data.withdrawUserName}-${i.uuid}</td></c:if>
							<td>${i.transactionId}</td>
							<td>${i.inOrderId}</td>
							<td>${i.withdrawMoney}</td>
							<td>${i.totalRequest}</td>
							<td><fmt:formatDate value="${i.beginTime}" type="both"/></td>
							<td><fmt:formatDate value="${i.endTime}" type="both"/></td>
							<td>${i.bankAccount.bankName} ${i.bankAccount.bankAccountName} ${i.bankAccount.bankAccountNumber}</td>
							<td>
							<c:if test="${i.data.networkError == true }"><font color='red'>需人工核实</font> </c:if><span class="statusspan"><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></span></td>
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:40px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<a href="${i.operate.get}"><li class="materialSelect">查看</li></a>
									<li class="materialEdit" data-url="${i.operate.update}" data-transactionId="${i.transactionId}" style="cursor: pointer;">完成结算</li>

									<a href="javascript: void(0)"  onclick="retransmission('${i.transactionId}')">
										<li>重发通知</li>
									</a>
								<!-- 	<c:if test="${i.operate.get != null }">
									</c:if>
									<c:if test="${i.operate.update != null }">
									</c:if> -->
									<c:if test="${i.currentStatus=='710013'}">
										<a href="javascript: void(0)" onclick="Reconfirm('${i.transactionId}')">
											再次确认
										</a>
									</c:if>
								</ul>
							</td>
						</tr>
					</c:forEach>
						<tr class="subtotaltr" data-requireSummary="${requireSummary}">
							<td>本页小计</td>
							<td></td>
							<td></td>
							<td><fmt:formatNumber value="${totalWithdrawMoney}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber> </td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
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

	  $("input[name=fuzzyBankAccountName]").focus(function(){
			$(".queries").css("display","block");
	  })

	$('select').each(function(){
		var zhixz = $(this).attr('value');
		for (var i = 0; i < $(this).find('option').length; i++) {
			var zhi = $(this).find('option').eq(i).attr('value');
			if (zhi==zhixz) {
				$(this).find('option').eq(i).attr('selected','selected');
				// console.log(zhi+','+zhixz);
			}else{
				$(this).find('option').eq(i).removeAttr('selected');
				// console.log(zhi+','+zhixz);
			}
		};
	  
		})
	// 显示小计
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
	$('.materialEdit').on('click',function(){
		var url = $(this).attr('data-url');
		var transactionId = $(this).attr('data-transactionId');
		var dataarry = {'transactionId':transactionId}
		$.ajax({
			type:"POST",
			url:url,
			dataType:"json",
			data:dataarry,
			success:function(data) {
				swal('出款交易ID'+transactionId+'结算完成！');

			},
			error:function(){
				swal('出款交易ID'+transactionId+'结算失败！')
			}		
		})
	})
  </script>
  	<!-- 导出数据 -->  
	<script>
	function GetRequest(onenum) { 
		var url = location.search; //获取url中"?"符后的字串 
		var theRequest = new Object(); 
		if (url.indexOf("?") == 0) { 
			location.href = window.location.href+'&download='+onenum;
		} else{
			location.href = window.location.href+'?download='+onenum;
		}
		
	} 


	$('.educe-now').on('click',function(){
		GetRequest(1);
	});
	$('.educe-all').on('click',function(){
		GetRequest(2);
	});

	//完成结算标绿色
	$('.statusspan').each(function () {
		var text = $(this).text();
		if (text=='已结算') {
			$(this).parents('tr').addClass('colorgreen');
		}else{
			$(this).parents('tr').removeClass('colorgreen');
		}
	})
	//再次确认
	function Reconfirm(a) {
		var dataarry = {'transactionId': a};
		$.ajax({
			type: "GET",
			url: "/withdraw/confirm.json?confirmTransactionId=" + a,
			dataType: "json",
			data: dataarry,
			success: function (data) {
				swal('再次确认成功！');
			},
			error: function () {
				swal('再次确认失败！');
			}
		});
	}
	//重发通知
		function retransmission(a) {
			$.ajax({
				type:"GET",
				url:"/withdraw/notify.json?transactionId="+a,
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
</html>
