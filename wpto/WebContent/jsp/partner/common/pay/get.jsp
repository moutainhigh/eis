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

    <title>${systemName}-支付订单详情</title>

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
			for(var i=1;i<length;i++){
				$(".tablefuji tbody tr").eq(i).find("th").eq(0).css("text-align","right");
				$(".tablefuji tbody tr").eq(i).find("td").eq(0).css({"text-align":"left","paddingLeft":"3%"});
			}			
		})
	</script>
  </head>
  <style type="text/css">
	.looksp{
		display: inline-block;
	    padding: 8px 12px;
	    border: 2px solid #7E92AB;
	    border-radius: 5px;
	    cursor: pointer;
	    background-color: #E4E9F0;
	}
	.tablefuji>tbody>tr:nth-of-type(odd) {
    background-color: #f9f9f9;
}
  </style>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>支付订单详情</span></h2>
			<div class="table-responsive">	
	<table class="table tablefuji">
		<tr class="header tabheader" >
			<th colspan="2">支付订单详情</th>
		</tr>	
		<tr>
			<th style="width: 200px;">系统订单号：</th>
			<td>
				<c:out value="${pay.transactionId}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">入口订单号：</th>
			<td>
				<c:out value="${pay.inOrderId}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">出口订单号：</th>
			<td>
				<c:out value="${pay.outOrderId}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">商户：</th>
			<td>
				<c:out value="${pay.description}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">对应购买交易：</th>
			<td>
				<c:out value="${pay.refBuyTransactionId}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">付款人：</th>
			<td>
				<c:out value="${pay.data.fromAccountName}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">付款人类型：</th>
			<td>
				<c:forEach items="${userType}"  var="a">
					<c:if test="${a.userTypeId==pay.payFromAccountType}">
						${a.userTypeName}
					</c:if>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">收款人：</th>
			<td>
				<c:out value="${pay.data.toAccountName}"/>
			</td>
		</tr>
		
		<tr>
			<th style="width: 200px;">收款人类型：</th>
			<td>
				<c:forEach items="${userType}"  var="a">
					<c:if test="${a.userTypeId==pay.payToAccountType}">
						${a.userTypeName}
					</c:if>
				</c:forEach>
			</td>
		</tr>
		<c:if test="${isPlatformGenericPartner==true}">
			<tr>
				<th style="width: 200px;">支付方式：</th>
				<td>
					<c:out value="${pay.operate.payMethod}"/>
				</td>
			</tr>
		</c:if>
		<tr>
			<th style="width: 200px;">支付类型：</th>
			<td>
				<c:out value="${pay.data.payTypeName}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">订单金额：</th>
			<td>
				<fmt:formatNumber value="${pay.faceMoney}" pattern="#0.00"/> 元
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">成功金额：</th>
			<td>
				<fmt:formatNumber value="${pay.realMoney}" pattern="#0.00"/> 元
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">交易卡类型：</th>
			<td>
				<spring:message code='PayCardType.${pay.payCardType}'/>
			</td>
		</tr>
		<tr>    
			<th>发起时间：</th>
			<td> 
				<fmt:formatDate value='${pay.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			</td>
			
		</tr>
		<tr>    
			<th>结束时间：</th>
			<td> 
				<fmt:formatDate value='${pay.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			</td>
		</tr>
		<tr>
			<th>状  态：</th>
			<td>
				<spring:message code='Status.${pay.currentStatus}' />
			</td>
		</tr>  
		
	</table>
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
    <script type="text/javascript">

    </script>
  </body>
</html>
