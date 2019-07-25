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

<title>${systemName}-支付类型</title>

<!-- Bootstrap core CSS -->
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
<link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">
<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
<style>
.blue {
	color: #428bca;
}

.fa {
	font-size: 16px;
}

#edui1 {
	z-index: 9 !important;
}

.edui-editor-breadcrumb {
	text-align: left !important;
}

#browserSync {
	line-height: 12px !important;
}

#browser span {
	display: inline-block;
	line-height: 12px;
	padding-left: 5px;
	padding-right: 5px;
}

#browserSync span {
	display: inline-block;
	line-height: 12px;
	padding-left: 5px;
	padding-right: 3px;
}

#browser {
	line-height: 12px !important;
	background: #fff !important;
}

.treeview, .treeview ul {
	padding: 0;
	margin: 0;
	list-style: none;
	background
	(url('/theme/${theme
}

/
images
/lineDottedVert




.png




'))
}
.treeview ul {
	margin-top: 4px;
}

.treeview li {
	margin: 0;
	padding: 3px 0pt 3px 16px;
}

.treeview li {
	background:
		url('/theme/${theme}/images/treeview/treeview-default-line.png') 0 0
		no-repeat;
}

.treeview li.collapsable, .treeview li.expandable {
	background-position: 0 -176px;
}

.treeview .expandable-hitarea {
	background-position: -80px -3px;
}

.treeview li.last {
	background-position: 0 -1766px
}

.treeview li.lastCollapsable, .treeview li.lastExpandable {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-default.png');
}

.treeview li.lastCollapsable {
	background-position: 0 -111px
}

.treeview li.lastExpandable {
	background-position: -32px -67px
}

.treeview div.lastCollapsable-hitarea, .treeview div.lastExpandable-hitarea
	{
	background-position: 0;
}

.filetree li {
	padding: 3px 0 2px 16px;
}

.filetree span.folder, .filetree span.file {
	padding: 1px 0 1px 16px;
	display: block;
}

.treeview .hitarea {
	background: url('/theme/${theme}/images/treeview/treeview-default.png')
		-64px -25px no-repeat;
	height: 16px;
	width: 16px;
	margin-left: -16px;
	float: left;
	cursor: pointer;
}

html .hitarea {
	display: inline;
	float: none;
}

.treeview li {
	margin: 0;
	padding: 3px 0pt 3px 16px;
}

#treecontrol {
	margin: 1em 0;
	display: none;
}

.treeview .hover {
	cursor: pointer;
}

.treeview li {
	background:
		url('/theme/${theme}/images/treeview/treeview-default-line.png') 0 0
		no-repeat;
}

.treeview li.collapsable, .treeview li.expandable {
	background-position: 0 -176px;
}

.treeview .expandable-hitarea {
	background-position: -80px -3px;
}

.treeview li.last {
	background-position: 0 -1766px
}

.treeview li.lastCollapsable, .treeview li.lastExpandable {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-default.png');
}

.treeview li.lastCollapsable {
	background-position: 0 -111px
}

.treeview li.lastExpandable {
	background-position: -32px -67px
}

.treeview div.lastCollapsable-hitarea, .treeview div.lastExpandable-hitarea
	{
	background-position: 0;
}

.treeview-red li {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-red-line.gif');
}

.treeview-red .hitarea, .treeview-red li.lastCollapsable, .treeview-red li.lastExpandable
	{
	background-image:
		url('/theme/${theme}/images/treeview/treeview-red.gif');
}

.treeview-black li {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-black-line.gif');
}

.treeview-black .hitarea, .treeview-black li.lastCollapsable,
	.treeview-black li.lastExpandable {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-black.gif');
}

.treeview-gray li {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-gray-line.gif');
}

.treeview-gray .hitarea, .treeview-gray li.lastCollapsable,
	.treeview-gray li.lastExpandable {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-gray.gif');
}

.treeview-famfamfam li {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-famfamfam-line.gif');
}

.treeview-famfamfam .hitarea, .treeview-famfamfam li.lastCollapsable,
	.treeview-famfamfam li.lastExpandable {
	background-image:
		url('/theme/${theme}/images/treeview/treeview-famfamfam.gif');
}

.treeview .placeholder {
	background: url('/theme/${theme}/images/treeview/ajax-loader.gif') 0 0
		no-repeat;
	height: 16px;
	width: 16px;
	display: block;
}

.filetree li {
	padding: 3px 0 2px 16px;
}

.filetree span.folder, .filetree span.file {
	padding: 1px 0 1px 16px;
	display: block;
}
/* .filetree span.folder { background: url('/theme/${theme}/images/treeview/folder.gif') 0 no-repeat; }
		.filetree li.expandable span.folder { background: url('/theme/${theme}/images/treeview/folder-closed.gif') 0 no-repeat; } */
/* .filetree span.file { background: url('/theme/${theme}/images/treeview/file.gif') 0 no-repeat; } */
.fullScreenBg {
	position: fixed;
	background: #333;
	opacity: 0.7;
	z-index: 200;
	width: 100%;
	height: 100%;
}

.message {
	position: fixed;
	z-index: 300;
	background: #fff;
	height: 230px;
	width: 300px;
	left: 50%;
	top: 230px;
	margin-left: -150px;
	text-align: center;
	border-radius: 5px;
	overflow: hidden;
	padding-top: 30px;
}

.sidebar {
	z-index: 20;
}

.message .submit {
	border-radius: 5px;
	height: 40px;
	line-height: 40px;
	text-align: center;
	width: 100px;
	color: #fff;
	font-size: 20px;
	background: #333;
	display: inline-block;
	position: absolute;
	bottom: 20px;
	left: 50%;
	margin-left: -50px;
	cursor: pointer;
}

.message .text {
	display: inline-block;
	font-size: 18px;
}

.submit {
	width: 100%;
	text-align: center;
}

tr#kuozhantr {
	border-top: 2px solid #5C6F87;
}

button, input, select, textarea {
	border-radius: 5px;
	border: 1px solid #989898;
	padding: 1px 3px;
	background-color: #E6E6E6;
}

#kzsj {
	width: 100%;
	border: 1px solid #ddd;
	margin-bottom: 20px;
}

#kzsj tr {
	border-bottom: 1px solid #ddd;
}

td {
	padding: 16px 30px !important;
}

#kzsj>tbody>tr:nth-of-type(odd) {
	background-color: #f9f9f9;
}
</style>
</head>
<body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2 class="sub-header">
					<span>添加支付类型</span>
				</h2>
				<div class="table-responsive">
					<form id="partnerForm" name="partnerForm">
						<table class="table table-striped" style="">
							<thead style="display: none;">
								<tr>
									<td style="width: 20%; text-align: right;">
									</th>
									<td style="width: 80%; text-align: left;">
									</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="text-align: right;">提现类型名称</td>
									<td style="text-align: left;"><input class="notEmpty" name="withdrawTypeName" id="withdrawTypeName" /></td>
								</tr>
								<tr>
									<td style="text-align: right;">到账周期</td>
									<td style="text-align: left;">
										<select name="arrivePeriod" id="arrivePeriod">
											<c:forEach var="period" items="${billingPeriodList}">
												<option value="${period}">${period}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">提现手续费类型</td>
									<td style="text-align: left;">
										<select name="commissionType" id="commissionType">
												<option value="COMMISSION_TYPE_FIXED">按笔固定</option>
												<option value="COMMISSION_TYPE_RATE">按比例</option>
										</select>
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">手续费比例</td>
									<td style="text-align: left;">
										<input class="notEmpty" name="commission" id="commission" />
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">手续费扣除来源</td>
									<td style="text-align: left;">
										<select name="commissionChargeType" id="commissionChargeType">
											<option value="COMMISSION_CHARGE_TYPE_IN_WITHDRAW">
												<spring:message code='CommissionChargeType.COMMISSION_CHARGE_TYPE_IN_WITHDRAW'/>
                      						</option>
											<option value="COMMISSION_CHARGE_TYPE_IN_REMAIN_MONEY">
												<spring:message code='CommissionChargeType.COMMISSION_CHARGE_TYPE_IN_REMAIN_MONEY'/>
											</option>
										</select>
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">处理器</td>
									<td style="text-align: left;">
										<select name="processClass" id="processClass">
												<c:forEach var="i" items="${process}">
													<option value="${i.processName}">${i.processDesc}</option>
												</c:forEach>
										</select>
									</td>
								</tr>
								
								<tr>
									<td style="text-align: right;">开始时间</td>
									<td style="text-align: left;">
										<input name="withdrawBeginTime" id="withdrawBeginTime" placeholder="请输入开始时间，例：00:00:00"/>
									</td>
								</tr>
								
								<tr>
									<td style="text-align: right;">结束时间</td>
									<td style="text-align: left;"><input name="withdrawEndTime" id="withdrawEndTime" placeholder="请输入结束时间，例：23:59:59" /></td>
								</tr>
								
								<tr>
									<td style="text-align: right;">可提现次数</td>
									<td style="text-align: left;"><input class="notEmpty isNum" name="maxWithdrawCountInPeriod" id="maxWithdrawCountInPeriod" /></td>
								</tr>
								
								<tr>
									<td style="text-align: right;">周期内可提现总金额</td>
									<td style="text-align: left;"><input class="notEmpty isNum" name="maxWithdrawAmountInPeriod" id="maxWithdrawAmountInPeriod" /></td>
								</tr>
								
								<tr>
									<td style="text-align: right;">每笔提现最大金额</td>
									<td style="text-align: left;"><input class="notEmpty isNum" name="maxWithdrawAmountPerCount" id="maxWithdrawAmountPerCount" /></td>
								</tr>
								
								<tr>
									<td style="text-align: right;">每笔提现最小金额</td>
									<td style="text-align: left;"><input class="notEmpty isNum" name="minWithdrawAmountPerCount" id="minWithdrawAmountPerCount" /></td>
								</tr>
								
 								<c:forEach var="a" items="${dataDefine}">
									<tr>
										<td style="text-align: right;">${a.dataName}</td>
										<td style="text-align: left;"><input name="${a.dataCode}" id="${a.dataCode}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<div class="submit">
							<input type="submit" value="新增出款通道" class="submitinput">
						</div>
					</form>
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
	<script src="/theme/${theme}/js/lib/jquery.cookie.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.treeview.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.form.3.5.1.js" type="text/javascript"></script>
	
	<script>



	$(function(){
		var length=$("tbody tr").length;
		for(var i=0;i<length;i++){
			$("tbody tr").eq(i).find("td").eq(1).find('input').css({"max-width":"300px;","width":"50%"});
			$("tbody tr").eq(i).find("td").eq(1).find('select').css({"max-width":"300px;","width":"50%"});
			$("tbody tr").eq(i).find("td").eq(1).css({"text-align":"left","paddingLeft":"5%"});
		};
	});
	
	</script>
	<script>
		$(document).ready(function(){
			$("#browser").treeview();
			$("#browserSync").treeview(); 
		});



		$(function(){
			var m=$("#browser").find("input[name='defaultNodeId']").length;
			var checked;
			for(var i=0;i<m;i++){
				$('#browser input[name="defaultNodeId"]:eq('+i+')').click(function(){
					checked=$('#browser').find('input[name="defaultNodeId"]:checked').val();
					var index=$('#browser input[name="defaultNodeId"]').index(this);
					//alert($("#browserSync").find(".subNodeId").eq(index)[0].checked);
					 if($("#browserSync").find(".subNodeId").eq(index)[0].checked==true){
						alert("同步时不能选择跟发布位置一样的节点！");
						$("#browserSync").find(".subNodeId").eq(index).removeAttr("checked");
					} 
				})
			}
			
			$("#browserSync").find(".subNodeId").click(function(){
				if($(this).val()==checked){
					//alert("同步时不能选择跟发布位置一样的节点！")
					$(this).removeAttr("checked");
				}
			})
		});


		$(function(){

		$(".submitinput").on('click',function(e){
				e.preventDefault();
				//验证信息  先空 然后数字型  时间格式 空class名为notEmpty 数字class为isNum 时间格式为time
				var notEmptyInput = $("#partnerForm").find(".notEmpty");
				var verification = true;//默认验证通过
				$(notEmptyInput).each(function(){
					if($(this).val().trim() == ""){
						var alertV = $(this).parent().prev().text();
						alert(alertV+"不能为空！");
						verification = false;
						return false;
					}
				});
				if(!verification){
					return;
				}
				//验证是否是数字
				var isNumInput = $("#partnerForm").find(".isNum");
				var reg = /^([1-9][\d]{0,12}|0)(\.[\d]{1,4})?$/;
				$(isNumInput).each(function(){
					if(!reg.test($(this).val())){
						var alertV = $(this).parent().prev().text();
						alert(alertV+"必须为数字！");
						verification = false;
						return false;
					}
				});
				
				if(!verification){
					return;
				}
				
				//现在验证开始时间和结束时间格式合法性
				var timeInput = $("#partnerForm").find(".time");
				var regTime = /^(?:[01]\d|2[0-3])(?::[0-5]\d){2}$/;
				$(timeInput).each(function(){
					if(!regTime.test($(this).val())){
						var alertV = $(this).parent().prev().text();
						alert(alertV+"格式必须为:00:00:00！");
						verification = false;
						return false;
					}
				});
				
				if(!verification){
					return;
				}
				
				if(verification){
						$.ajax(
							 {  
								type:"POST",
								url:"/withdrawType/create.json",
								dataType:"json",
								data:$('#partnerForm').serialize(),
								success:function(data) {						
									swal('添加提现类型'+data.message.message);
								},
								error:function(XMLResponse){
									swal('添加提现类型失败!');
									//console.log(333);
								},
							}			
						);
				}
			});
		
	})  
	 </script>
	<script>
		$(function(){
			$(".hitarea").click(function(){
				if($(this).hasClass("collapsable-hitarea")){
						$(this).siblings("ul").slideDown();
						$(this).siblings(".folder").children("i").removeClass("fa-folder-o");
						$(this).siblings(".folder").children("i").addClass("fa-folder-open-o");
					}else if($(this).hasClass("expandable-hitarea")){
						$(this).siblings("ul").slideUp();
						$(this).siblings(".folder").children("i").addClass("fa-folder-o");
						$(this).siblings(".folder").children("i").removeClass("fa-folder-open-o");
					}
			})
			
		})			
	</script>


</body>
</html>
