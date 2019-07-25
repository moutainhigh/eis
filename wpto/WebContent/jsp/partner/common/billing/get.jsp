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

    <title>${systemName}-结算单详情</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
	<script src="/theme/${theme}/js/sweetalert.min.js"></script>
	<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<script>
		$(function(){
			var length=$("tbody tr").length;
			for(var i=1;i<length;i++){
				$("tbody tr").eq(i).find("th").eq(0).css("text-align","right");
				$("tbody tr").eq(i).find("td").eq(0).css({"text-align":"left","paddingLeft":"3%"});
			}			
		})
	</script>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>结算单详情</span></h2>
			<div class="table-responsive">	
				 <table class="table table-striped">
		<tr class="header tabheader" >
			<th colspan="2">${billing.operate.username}用户结算单详情</th>
		</tr>	
		<tr>
			<th style="width: 200px;">结算ID：</th>
			<td class="billingId">
				<c:out value="${billing.billingId}"/>
			</td>
		</tr>
		<tr>    
			<th>结算周期：</th>
			<td> 
				
				<fmt:formatDate value='${billing.billingBeginTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			到
				<fmt:formatDate value='${billing.billingEndTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			</td>
		</tr>
		<tr>
			<th>交易金额：</th>
			<td>
				<fmt:formatNumber value="${billing.faceMoney}" pattern="#0.0"/> 元
			</td>
		</tr>
		<tr>
			<th>结算金额：</th>
			<td>
				<span class="jiesuane"><fmt:formatNumber value="${billing.realMoney}" pattern="#0.0"/></span> 元
			</td>
		</tr>
		<tr>
			<th>实际到账金额：</th>
			<td>
				<span class="daozhange"><fmt:formatNumber value="${billing.arriveMoney}" pattern="#0.0"/></span> 元
			</td>
		</tr>
		<tr>
			<th>差额：</th>
			<td> 
				<span class="chae"></span> 元
			</td>
		</tr>
		<tr>
			<th>结算比例：</th>
			<td>
				<fmt:formatNumber value="${billing.commission}"  type="percent"/>
			</td>
		</tr>
		<tr>
			<th>状态：</th>
			<td>
				<spring:message code='Status.${billing.currentStatus}' />
			</td>
		</tr>
		<tr>
			<th>操作人：</th>
			<td>
				<c:out value="${billing.operate.operator}"/>
			</td>
		</tr>
		<tr>
			<th>操作时间：</th>
			<td>
				<fmt:formatDate value='${billing.billingHandlerTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
			</td>
		</tr>
		<tr>
			<th>操作说明：</th>
			<td>
				<div><c:out value="${billing.data.memory}"/></div>
			</td>
		</tr>
		<c:if test="${billing.currentStatus==730001}">
		<tr>
			<th><span class="completespan">完成收款</span></th>
			<td></td>
		</tr>
		</c:if>
	</table>
	</div>
        </div>
      </div>
    </div>
    <style type="text/css">
    .completespan{
    	cursor: pointer;
    	display: inline-block;
    	padding: 5px 18px;
    	background-color: #476284;
    	color: #fff;
    	border-radius: 4px;
    	cursor: pointer;
    	font-weight: normal;
    }
    .completeout{
    	width: 100%;
    	height: 100%;
    	background-color: rgba(0,0,0,0.5);
    	position: fixed;top: 0;left: 0;
    	z-index: 9999;
    	display: none;
    }
    .completeout .complete{
    	width: 300px;height: 150px;
    	background-color: #F5F5F5;
    	border-radius: 5px;
    	text-align: center;
    	position: absolute;
	    top: 50%;
	    left: 50%;
	    margin-left: -150px;
	    margin-top: -75px;
    }
    .completeout .complete h3{
    	font-size: 16px;
    	text-align: center;
    	    margin: 25px 0 20px;
    }
    .completeout .complete input[type="text"]{
    	width: 70%;
    	border: 1px solid #8A8A8A;
	    border-radius: 3px;
	    padding: 2px 4px;
    }
    .confirm,.closes{
    	display: inline-block;
    	padding: 5px 18px;
    	background-color: #476284;
    	color: #fff;
    	border-radius: 4px;
    	cursor: pointer;
    	margin-top: 14px;
    }
    .closes{
    	background-color: #A9A9A9;
    	margin-left: 15px;
    }
    </style>
    <!-- 完成收款弹框 -->
    <div class="completeout">
    	<div class="complete">
    		<h3>请输入实际到账金额</h3>
    		<input type="text" placeholder="如：100.98" class="arriveMoney"/> 元
    		<p><span class="confirm"  billingId="${billing.billingId}">确 定</span><span class="closes">取 消</span></p>
    		  
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
    	$('.chae').text($('.jiesuane').text()-$('.daozhange').text());


    	$('.confirm').on('click',function(){
    		var billingId = $(this).attr('billingId');
    		var arriveMoney = $('.arriveMoney').val();
    		var reg = /^[1-9]+(.[0-9]{0,2})?$/;
    		var dataarry = {'billingId':billingId,'arriveMoney':arriveMoney,'mode':'arrive'};
    		if (!reg.test(arriveMoney)) {
    			alert('请输入正确金额（必须大于零且小数点后最多两位数）')
    		}else{
	    		$.ajax( {  
			         type : "POST",  
			         url : "/billing/update.json",  
			         dataType:"json",
			         data:dataarry, 
			         success : function(data) {  
			         	$('.completeout').css('display','none');
			         	swal(data.message.message);
		         	
			            
			         },
			         error : function(data) {  
			         	$('.completeout').css('display','none');
			            swal(data.message.message);
			         }  
	        	});  
	    	}
    	})
    	$('.closes').on('click',function(){
    		$(this).parents('.completeout').css('display','none');
    	})
    	$('.completespan').on('click',function(){
    		$('.completeout').css('display','block');
    	})
    </script>
  </body>
</html>
