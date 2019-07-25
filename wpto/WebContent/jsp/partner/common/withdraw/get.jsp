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

    <title>${systemName}-提现单详情</title>

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
			<h2 class="sub-header"><span>提现单详情</span></h2>
			<div class="table-responsive">	
	<table class="table tablefuji">
		<tr class="header tabheader" >
			<th colspan="2">${withdraw.data.withdrawUserName}[${withdraw.uuid}]用户提现单详情</th>
		</tr>	
		<tr>
			<th style="width: 200px;">提现交易ID：</th>
			<td>
				<c:out value="${withdraw.transactionId}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">入口订单号：</th>
			<td>
				<c:out value="${withdraw.inOrderId}"/>
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">提现类型：</th>
			<td>
				<c:out value="${withdraw.data.withdrawTpe}"/>
			</td>
		</tr>
		<c:if test="${isPlatformGenericPartner == true}">
		<tr>
			<th style="width: 200px;">提现方式：</th>
			<td>
				<c:out value="${withdraw.data.withdrawMethod}"/>
			</td>
		</tr>
		</c:if>
		<tr>
			<th style="width: 200px;">提现金额：</th>
			<td>
				<fmt:formatNumber value="${withdraw.withdrawMoney}" pattern="#0.0"/> 元
			</td>
		</tr>
		<tr>
			<th style="width: 200px;">批付数量：</th>
			<td>
				<c:out value="${withdraw.totalRequest}"/>
			</td>
		</tr>
		<tr>    
			<th>开始时间：</th>
			<td> 
				
				<fmt:formatDate value='${withdraw.beginTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			</td>
			
		</tr>
		<tr>    
			<th>完成时间：</th>
			<td> 
				<fmt:formatDate value='${withdraw.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			</td>
		</tr>
		<tr>
			<th>收款账户：</th>
			<td>
				${withdraw.bankAccount.bankAccountName}  ${withdraw.bankAccount.bankName} ${withdraw.bankAccount.bankAccountNumber}
			</td>
		</tr>
		<c:if test="${isPlatformGenericPartner == true}">
			<tr>
				<th>上游返回数据明细：</th>
				<td>
					${withdraw.data.rowResponse}
				</td>
			</tr>
		</c:if>
		
		<tr>
			<th>状  态：</th>
			<td>
				<spring:message code='Status.${withdraw.currentStatus}' />
			</td>
		</tr>  
		
	</table>
	<h3 style="font-size:16px;"><span class="looksp">查看子订单</span></h3>
	<table  class="table table-striped tableziji" style="display:none;">

		<c:if test="${!empty childTransaction}">
			
			<tr class="header tabheader">       
				<th style="text-align:center;font-size:15px;">提现交易ID</th>
				<th style="text-align:center;font-size:15px;">提现金额</th>
				<th style="text-align:center;font-size:15px;">收款账户</th>
				<c:if test="${isPlatformGenericPartner == true}">				
				<th style="text-align:center;font-size:15px;">提现方式</th>
				</c:if>
				<th style="text-align:center;font-size:15px;">状态</th>
			</tr>
			<c:forEach var="a" items="${childTransaction}" >
				
				<tr>
					<td>  
						<c:out value="${a.transactionId}"/>
					</td>
					<td>
						<fmt:formatNumber value="${a.withdrawMoney}" pattern="#0.0"/> 元
					</td>
					<td>
						${a.bankAccount.bankAccountName}  ${a.bankAccount.bankName} ${a.bankAccount.bankAccountNumber}
					</td>
					<c:if test="${isPlatformGenericPartner == true}">				
					<td>
						<c:out value="${a.data.withdrawMethod}"/>
					</td>
					</c:if>
					<td>
						<c:if test="${a.data.networkError == true }"><font color='red'>需人工核实</font> </c:if><spring:message code='Status.${a.currentStatus}' />
					</td>
				</tr>
			</c:forEach>
		</c:if>
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
     	if($('.tableziji tr').length==0){
     		$('.looksp').css('display','none');
     		$('.looksp').parent().text('该提现单没有子订单');
     	}
    	$('.looksp').on('click',function(){
    		$('.tableziji').toggle();
    		if($('.tableziji').css('display')=='none'){
    			$(this).text('查看子订单')
    		}else{
    			$(this).text('收起子订单')
    		}
    	})
    </script>
  </body>
</html>
