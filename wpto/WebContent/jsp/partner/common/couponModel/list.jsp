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
			<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>

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
		border-radius:5px;
		border: 2px solid #B5494E;
	}
	.buttonColor{
		background-color:#fff;
		color:#B5494E;
		border-radius:5px;
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
		/*border-top:2px solid #ddd !important;
		background-color:#f9f9f9;*/
	}
	.table-striped>tbody>tr:nth-of-type(even) {
		background-color: #f9f9f9;
	}
	.table-striped>tbody>tr:nth-of-type(odd) {
		background-color: #FFF;
	}
	.tankuang input{
	    font-family: inherit;
	    font-size: inherit;
	    line-height: inherit;
	    border-radius: 3px;
	    border: 1px solid #B5B5B5;
	    width: 210px;
	        padding: 2px 3px;
	}	
	.tankuang button{
		padding: 5px 18px;
		background-color: #37485D;
		color: #fff;
		border: none;
		border-radius: 4px;
		    margin-top: 18px;
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
		height:45px;
	}
	.tankuang table select{
		height:30px;
	}
	.tankuang{
		    width: 500px;
    height: 415px;
    border: 1px solid #ddd;
    position: absolute;
    left: 50%;
    top: 50%;
    z-index: 99!important;
    margin-top: -223px;
    margin-left: -250px;
     display: none; 
    z-index: 9999999;
    box-shadow: 0 0 10px #D4D4D4;
    border-radius: 8px;
        background-color: #fff;
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
	.smallTitle	>a.btn-primary{
		background-color: #96A5B9!important;
		border:none!important;
	}
	iframe{
		z-index: 999999999999999999;
	}	
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<!-- 添加储值券 -->
	<div id="columns" class="tankuang">
		<h2 style="
		text-align: center;
	    font-size: 16px;
	    background-color: #eee;
	    margin: 0;
	    line-height: 51px;
	    margin-bottom: 15px;
	    color: #B5494E;
	    font-weight: bold;
	        border-bottom: 2px solid #37485D;">添加储值券</h2>
	        <img src="/theme/${theme}/images/close.png" style="position: absolute; top: 10px;  right: 8px; width: 31px;cursor: pointer;" class="imgclose">
	        <!-- <form id="coupontable"> -->
		<table width="100%" style="background-color:#FFFFFF;">
			<tbody>
				<tr>
					<td class="identification">编号：</td>
					<td><input type="text" name="couponCode" id="couponCode" /></td>
				</tr>
				<tr>
					<td class="identification">名称：</td>
					<td><input type="text" name="couponModelName" id="couponModelName"/></td>
				</tr>
				<tr>
					<td class="identification">开始时间：</td>
					<td>
					<input size="20"  id="d11" class="calendarInput" name="validTimeBegin" type="text"/>
					<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
					</td>
				</tr>
				<tr>
					<td class="identification">结束时间：</td>
					<td>
						<input size="20" id="d12" class="calendarInput" name="validTimeEnd" type="text" />
						<img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
					</td>
				</tr>
				<tr>
					<td class="identification">金额：</td>
					<td><input type="number" name="giftMoney" id="giftMoney"/></td>
				</tr>
				<tr>
					<td class="identification">发行数量：</td>
					<td><input type="text" name="maxKeepCount" id="maxKeepCount"/></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center;"><button class="addcoupon">提交</button></td>
				</tr>
			</tbody>
		</table>
		<!-- </form> -->
	</div>
	
	
	<div class="hidebg"></div> <!-- 背景层 -->
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>${title}</span></h2>
			<div class="table-responsive content" style="background-color:transparent;">
				<div class="smallTitle">
					<a href="" class="btn btn-primary queryForm-btn-wt110">储值券列表</a>
					<a href="" class="btn btn-primary queryForm-btn-wt110">储值券充值情况</a>
					<a href="" class="btn btn-primary queryForm-btn-wt110">储值券消费记录</a>
					<span class="buttonWithoutColor buttonColor addCouponModel" style="float:right;cursor: pointer;">+ 添加储值券</span>
				</div>
				 <table class="table table-striped">
		              <thead>
						<tr>
							<th>代金券编号</th>
							<th>名称</th>
							<th>开始时间</th>
							<th>截止时间</th>
							<th>有效期</th>
							<th>发行数量</th>
							<th>已使用数量</th>
							<th>操作</th>
						</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.couponCode}</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><a href="">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="">删除</a></td>
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
			$(".tankuang").css("display","block");
		})
		$(".imgclose").click(function(){
			$(".tankuang").css("display","none");
		})
		// 提交代金券
		$('.addcoupon').on('click',function () {
			//alert('11')
			$('table input').each(function () {
				var val = $(this).val();
				if (val=='') {
					$(this).addClass('nullV');
				}else{
					$(this).removeClass('nullV')
				}
			})
			if ($('.nullV').length&&$('.nullV').length>0) {
					var tsv = $('.nullV').eq(0).parents('tr').find('.identification').text();
					alert(tsv+'不能为空');
					$('.nullV').eq(0).focus();
				}
				else{
					var couponCode = $('#couponCode').val();
					var couponModelName = $('#couponModelName').val();
					var validTimeBegin = $('#d11').val();
					var validTimeEnd = $('#d12').val();
					var giftMoney = $('#giftMoney').val();
					var maxKeepCount = $('#maxKeepCount').val();
			$.ajax({
	            type:"POST",
	            url: '/couponModel/create.shtml',
	            data:{
	            	'couponCode':couponCode,
	            	'couponModelName':couponModelName,
	            	'validTimeBegin':validTimeBegin,
	            	'validTimeEnd':validTimeEnd,
	            	'giftMoney':giftMoney,
	            	'maxKeepCount':maxKeepCount
	            },
	            dataType:'json', 
	            success:function (data) {
	                alert("成功");
	                location.reload();
	            },
	            error:function (data) {
	                alert("系统繁忙,请稍后再试");
	                return false;
	            }
	        });}
		})

		
	})	
	</script>
  </body>
</html>
