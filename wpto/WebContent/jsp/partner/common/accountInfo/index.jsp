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
		<title>${systemName}-账户管理</title>
		<!-- Bootstrap core CSS -->
		<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

		<!-- Custom styles for this template -->
		<link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
		<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
	    <link href="/theme/${theme}/style/query.css" rel="stylesheet">
	    <link href="/theme/${theme}/all/style/page/page.css" rel="stylesheet">
	    <link href="/theme/${theme}/all/style/GridManager.css" rel="stylesheet">
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

			.flowDetailInfo{
				display: none;
				width:600px;
				background-color:white;
				padding: 17px;
				border-radius: 5px;
				text-align: center;
				position: fixed;
				left: 50%;
				top: 80px;
				margin-left: -300px;
				z-index: 99999;
				border:1px solid;
				min-height:600px;
				height:610px;
				
			}
			.flowTitle{
				text-align: left;
				font-size: 20px;
				border-bottom: 1px solid;
			}
			
			.flowInfo{
				text-align: left;
				font-size: 16px;
			}
			.flowInfo .rowTitle{
				width: 20%;
				text-align: right;
				display: inline-block;
			}
			.flowInfo .rowValue{
				display: inline-block;
				width:75%;
				text-align: left;
				float:right;
			}
		</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>账户管理</span></h2>
			<div class="content" style="overflow:hidden;">
				<form id="queryForm">
					<table width="100%"  >
						<tr>
							<td align="left">
								<span class="tdtitle">商户号：</span>
								<input type="text"  id="merchantNo" name="merchantNo" value=""/>
							</td>
							<td>
								<span class="tdtitle">会员号：</span>
								<input type="text"  id="memberNo" name="memberNo" value=""/>
							</td>
						</tr>
						<tr>
							<td align="left">
								<span class="tdtitle">账户号：</span>
								<input type="text" class="" name="accountNo" id="accountNo"  value="">
							</td>
							<td>
								<span  class="tdtitle">账户类型：</span>
								<select name="accountType" id="accountType">
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<span  class="tdtitle">账户状态：</span>
								<select name="accountStatus" id="accountStatus">
									<option value="">全部</option>
									<option value="OPEN">帐户开立</option>
									<option value="NORM">帐户正常</option>
									<option value="S_PAY">帐户止付</option>
									<option value="FREZ">帐户冻结</option>
        							<option value="CLOS">帐户关闭</option>
       							</select>
							</td>
							<td>
								<span class="btn btn-primary" style="width: 426px;margin-left:106px;"  value="查询"  onClick="queryAgain()">
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
				<p class="notiship">
				 	<a href="javascript:void(0)" class="addmoban" onclick="showCreate()">
				 	+ 新增账户
				 	</a>
				 </p>
				<table id="grid_table" class="table table-striped">
		              
		              
				</table>
			</div>
			
        </div>		
      </div>
	<!-- 悬浮框展示 -->
	</div>
    <div class="sweet-overlay" tabindex="-1" style="display: none;"></div>
    
        <div class="flowDetailInfo" id="createAccountInfo" style="height:300px;min-height:0px;">
    	<img onclick="closeDiv('createAccountInfo')" src="/theme/${theme}/all/images/post_close.png" style="float:right;cursor:pointer;">
    	<p class="flowTitle">
    		新增账户
    	</p>
    	<div style="overflow-y:auto;height:250px;">
	    	<p class="flowInfo">
	    		<span class="rowTitle">会员号:</span>
	    		<input name="memberNo" id="memberNoCreate" style="width: 50%;">
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">账户类型:</span>
	    		<select name="accountType" id="accountTypeCreate" style="width: 50%;">					 
					
				</select>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">账户名称:</span>
	    		<input name="accountName" id="accountNameCreate" style="width: 50%;">
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">账户状态:</span>
	    		<select name="accountStatus" id="accountStatusCreate" style="width: 50%;">					 
					<option value="NORM">帐户正常</option>
					<option value="OPEN">帐户开立</option>
					<option value="S_PAY">帐户止付</option>
					<option value="FREZ">帐户冻结</option>
					<option value="CLOS">帐户关闭</option>
				</select>
	    	</p>
	    	<p class="flowInfo submit" style="text-align:center;">
	    		<input value="新增账户" class="submitinput" type="submit" onclick="createAccount()">
	    	</p>
	    	
	    </div>
    </div>
    <div class="flowDetailInfo" id="flowDetailInfo">
    	<img onclick="closeDiv()" src="/theme/${theme}/all/images/post_close.png" style="float:right;cursor:pointer;">
    	<p class="flowTitle">
    		账户流水详细信息
    	</p>
    	<div style="overflow-y:auto;height:510px;">
	    	<p class="flowInfo">
	    		<span class="rowTitle">流水id:</span>
	    		<span class="rowValue" id="flow_id"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">账户:</span>
	    		<span class="rowValue" id="account_no"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">账户类型:</span>
	    		<span class="rowValue" id="account_type"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">会员id:</span>
	    		<span class="rowValue" id="member_no"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">来源账户:</span>
	    		<span class="rowValue" id="org_account_no"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">来源会员:</span>
	    		<span class="rowValue" id="org_member_no"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">订单:</span>
	    		<span class="rowValue" id="transaction_id"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">银行订单:</span>
	    		<span class="rowValue" id="bank_transaction_id"></span>
	    	</p>
<!-- 	    	<p class="flowInfo">
	    		<span class="rowTitle">交易类型:</span>
	    		<span class="rowValue" id="trade_type"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">交易子类型:</span>
	    		<span class="rowValue" id="sub_trade_type"></span>
	    	</p> -->
	    	<p class="flowInfo">
	    		<span class="rowTitle">资金类型:</span>
	    		<span class="rowValue" id="fund_type"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">交易金额:</span>
	    		<span class="rowValue" id="trade_amount"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">完成后金额:</span>
	    		<span class="rowValue" id="finish_amount"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">订单交易金额:</span>
	    		<span class="rowValue" id="order_amount"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">货币类型:</span>
	    		<span class="rowValue" id="currency"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">账目类型:</span>
	    		<span class="rowValue" id="account_category"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">资金操作类型:</span>
	    		<span class="rowValue" id="opt_type"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">交易状态:</span>
	    		<span class="rowValue" id="trade_status"></span>
	    	</p>
	    	<!-- <p class="flowInfo">
	    		<span class="rowTitle">清算状态:</span>
	    		<span class="rowValue" id="clear_status"></span>
	    	</p> -->
	    	<!-- <p class="flowInfo">
	    		<span class="rowTitle">清算类型:</span>
	    		<span class="rowValue" id="clear_type"></span>
	    	</p> -->
	    	<p class="flowInfo">
	    		<span class="rowTitle">交易描述:</span>
	    		<span class="rowValue" id="trade_subject"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">创建时间:</span>
	    		<span class="rowValue" id="created_date"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">完成时间:</span>
	    		<span class="rowValue" id="success_date"></span>
	    	</p>
	    	<p class="flowInfo">
	    		<span class="rowTitle">更新时间:</span>
	    		<span class="rowValue" id="modified_date"></span>
	    	</p>
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
    <script src="/theme/${theme}/js/util.js"></script>
    <script src="/theme/${theme}/all/js/GridManager.js"></script>
    <script src="/theme/${theme}/all/js/Map.js"></script>
    <script src="/theme/${theme}/all/js/accountType.js"></script>
    <script src="/theme/${theme}/all/js/accountInfo.js"></script>
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
