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

    <title>${systemName}-${title}</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
	  <link href="/theme/${theme}/style/query.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
	<style>
	.hidebg{
			height:100%;
			width:100%;
			background-color:#333;
			position: fixed;
			z-index:1100;
			filter:alpha(opacity=70); /* IE浏览器下设置透明度为60% */
			opacity:0.7;  /* 非IE浏览器下设置透明度为60% */
			display:none;"
		}
	.smallTitle{
		width:100%;
		max-width100%;
		overflow-x: auto;
		padding-top:8px;
		padding-bottom:10px;
	}
	.buttonWithoutColor{   
		padding:5px 10px; 
		border-radius:2px;
		border: 1px solid #337ab7;
	}
	.buttonColor{
		background-color:#337ab7;
		color:#FFFFFF;
		border-radius:2px;
	}
	.smallTitle a img{
		margin-right:5px; margin-top:-1px; width:15px; height:15px;
	}
	/* 表格 */
	.table-responsive td{
		border-top:0px!important;
		border-bottom:1px solid #ddd;
		height: 60px;
	 }
	.table>thead>tr>th{
		border-bottom:0px;
		border-top:2px solid #ddd !important;
		background-color:#f9f9f9;
	}
	.table-striped>tbody>tr:nth-of-type(even) {
		background-color: #f9f9f9;
	}
	.table-striped>tbody>tr:nth-of-type(odd) {
		background-color: #FFF;
	}
		
		
	.tankuang tr th{
		text-align:center;
		height:40px;
	}
	.tankuang tr .identification{
		text-align:right;
		padding:0px 15px;
	}
	.tankuang tr td{
		height:50px;
	}
	.tankuang table select{
		height:30px;
	}
	.tankuang{
		width:500px;
		border:1px solid #ddd;
		left:32%;
		position: absolute;
		z-index: 1200;
		top: 10%;
		display:none;	
	}
	@media (max-width:968px){
		.tankuang{
			width:80%;
			left:30%;
			margin-left:-20%;
		}
	}
		
	.tankuang span{
		color:red;
	}
		
		
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="title">${title}</h2>
			
			<div class="content" style="overflow:hidden;">
				<form id="queryForm">
				<table width="100%"  >
					<tr>
						<td width="50%">
							兑换券编号：
							<input type="text"  name="couponCode">
						</td>
						<td width="50%">
							用户：
							<input type="text" name="id">
							<button class="btn btn-primary query"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="glyphicon glyphicon-search"></span>
								查询
								&nbsp;&nbsp;&nbsp;&nbsp;
							</button>
						</td>
					</tr>
					<tr>
						<td >兑换券序列号：
							<input type="text" name="couponSerialNumber">
						</td>
						<td >
							兑换券密码：
							<input type="text" name="couponPassword">
						</td>
					</tr>
					<tr>
						<td >
							状态：
							<select name="currentStatus" >
								<option value="">全部状态</option>
								<c:forEach var="s" items="${statusList}">
									<option value="${s}">${s} <spring:message code="Status.${s}" />
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
				</form>			
			</div>
			<!-- 按钮触发模态框 -->
			<button id="ad" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">购买卡券</button>
			<!-- 模态框（Modal） -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="myModalLabel">
								购买兑换券
							</h4>
						</div>
						<div class="modal-body">
							
						</div>
						<!-- 
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭
							</button>
							<button type="button" class="btn btn-primary">
								购买
							</button>
						</div>
						 -->
					</div>
				</div>
			</div>

			<div class="table-responsive">当前可用余额:${money.chargeMoney}元
				 <table class="table table-striped">
		              <thead>
						<tr>
							<th>兑换券编号</th>
							<th>名称</th>
							<th>兑换券序列号</th>
							<th>兑换券密码</th>
							<th>渠道</th>
							<th>用户</th>
							<th>状态</th>
							<th><!-- 操作 --></th>
						</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.couponCode}</td>
							<td>${i.couponModelName}</td>
							<td>${i.couponSerialNumber}</td>
							<td>${i.couponPassword}</td>
							<td>
								${i.data.inviterName}
							</td>
							<td>
								<c:if test="${i.data.nickName != null}">
									${i.uuid}/${i.data.nickName}
								</c:if>
								<c:if test="${i.data.nickName == null}">
									${i.uuid}
								</c:if>
							</td>
							<td><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></td>
							<td><!-- <a href="">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="">删除</a> --></td>
						</tr>
					</c:forEach>
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
	
	<script>
	$(function(){
		$(".addCouponModel").click(function(){
			$(".tankuang").show();
			$(".hidebg").show();
		})
		$(".close").click(function(){
			$(".hidebg").css("display","none");
			$(".tankuang").css("display","none");
		})
		
	})	
	</script>
	<script>
	$("#ad").click(function(){
		var html=""
		$.ajax({
			type:"GET",
			url:"/couponModel.json",
			dataType:"json",
			success:function(data){
				html+="<table class='table table-striped'>",
				html+="<thead><tr><th>代金券编号</th><th>名称</th><th>购买数量</th><th>操作</th></tr></thead>";
				html+="<tbody>"
				for(var i=0; i < data.rows.length; i++){
					html+="<tr><td>"+data.rows[i].couponCode+"</td><td>"+data.rows[i].couponModelName+"</td><td><input id="+data.rows[i].couponCode+" type='text' value='0'/></td><td><a href='#' onClick='buy(\""+data.rows[i].couponCode+"\")'>购买</a></td></tr>";
				}
				html+="</tbody>";
				html+="</table>";
				$(".modal-body").html(html);
			}
		})
	})
	
	function buy(a){
		var count = $("#"+a).val();
		$.ajax({
			type:"POST",
			url:"/coupon/plus.json?count=" + count + "&couponCode=" + a,
			dataType:"json",
			success:function(data){
				alert(data.message.message);
			}
		})
	}
	</script>
  </body>
</html>
