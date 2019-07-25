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
		<title>${systemName}-标签管理</title>
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
		#queryForm td{
			padding-right: 30px;
		}
		.content table tr td{
			padding-bottom: 0!important;
		}
		#queryForm select, #queryForm input[type='text']{
			width: 70%;
		}
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
				width:25%;
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
			span.glyphicon.glyphicon-remove{
				position: absolute!important;
				top: -10px;
			    left: -11px;
			    color: #FFF;
			    background-color: rgba(92, 111, 135, 0.3);
			    border-radius: 100%;
			    padding: 4px;
			    opacity: 0;
			}
		.glyphicon-tags:before {
		    content: "\e042";
		    margin-right: 8px;
		    font-size: 15px;
		    color: #333;
		}
		ul#tagName{

		}
		ul#tagName li{
			position: relative;
			float: left;
		    margin-right: 30px;
		    list-style: none;
		    padding: 6px 21px;
		    background-color: #88C4DC;
		    border-radius: 5px;
		    color: #fff;
		    margin-left: 16px;
		    margin-bottom: 15px;
		}
		ul#tagName li a{
			color: #fff;
		}
		ul#tagName li:hover a{
			color: #333;
		}
		ul#tagName li:hover span{
			opacity: 1;
		}
		ul#tagName li span:hover{
			background-color: rgba(92, 111, 135, 0.54);
		}
		span.removechoose {
			float: right;
		    font-size: 14px;
		    background-color: #C7D0DE;
		    padding: 7px 13px;
		    border-radius: 5px;
		    border: 1px solid #96A8C3;
		    cursor: pointer;
		}
		ul#tagName li input[type="checkbox"]{
			display: none;
		}
		.removes{
			background-color: #C7D0DE;
		    padding: 7px 13px;
		    border-radius: 5px;
		    border: 1px solid #96A8C3;
		    cursor: pointer;
		    display: none;
		}
		</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>标签管理</span></h2>
			<div class="content" style="overflow:hidden;">
				<form id="queryForm">
				<table width="100%"  >
					<tr>
						<td><span>标签名：</span>
							<input type="text" name="tagName"/></td>
						<td>
							<span>对象类型：</span>
							 <% 
							String objectTypeV=request.getParameter("objectType");  
							request.setAttribute("objectTypeV",objectTypeV); 
							%>
							<select name="objectType" id="objectType" value="${objectTypeV}">
								<option value="">所有</option>
							<c:forEach var="i" items="${objectTypeList}">
								<option value="${i.value}">${i.key} ${i.value}</option>
							</c:forEach>
							</select>
						</td>
						<td><span>对象ID：</span>
							<input type="text" name="objectId"/></td>
						<td>
							<button class="btn btn-primary" style="width: 60%;margin-left:106px;"  type="button" value="查询" onClick="query_num()">
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
			<div class="table-responsive">	
				<h2 style="font-size: 18px;border-bottom: 1px solid;margin-bottom: 30px;"><span class="glyphicon glyphicon-tags"></span>我的标签 <span class="removechoose">删除标签</span></h2>
				<ul id="tagName" class="clearfix">
					<c:forEach var="i" items="${rows}">
						<li tagId="${i.tagId}"><span class="glyphicon glyphicon-remove remove" alt="删除标签"></span>
							<input type="checkbox" name="tagId" value="${i.tagId}" valuename="${i.tagName}">
							<a href="/tag/get/${i.tagId}.shtml" alt="查看标签">${i.tagName}</a>
						</li>
					</c:forEach>	
				</ul>
				<p style="text-align: center;margin-top: 30px;"><span class="removes">确定删除</span></p>
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
		$('#objectType option').each(function(){
			var zhi = $(this).attr('value');
			var zhixz = $(this).parent().attr('value');

			if (zhi==zhixz) {
				$(this).attr('selected','selected');
				//console.log(zhi+','+zhixz);
			}else{
				$(this).removeAttr('selected');
				//console.log(zhi+','+zhixz);
			}
		});
		$('.removechoose').on('click',function () {
			//$(this).text('请选择标签')
			$('ul#tagName li input').toggle();
			$('.removes').toggle();
			if ($('.removes').css('display') == 'none') {
				$(this).text('删除标签')
			}else{
				$(this).text('请选择标签')
			}
		})
		$('#tagName .remove').on('click',function (){
			var tagId = $(this).parent('li').attr('tagId');
			var tagli = $(this).parent('li');
			var name = $(this).next('a').text();
			//console.log(bankAccountId);
		    if (confirm("确定要删除 '"+name+"' 标签吗？")) {
		      $.ajax({
		      type:'post',
		      url:'/tag/delete.json',
				dataType:'json',
		      data:{
		        idList:tagId
		      },
		      success:function(data){
		        swal(data.message.message);
		        tagli.remove();
		      
		      },
		      error:function(data){
		        swal('系统繁忙请稍后再试！');
		      }
		    })
		    }
		})
		$('.removes').on('click',function (){
			var tags = '';
			var tagsname = '';
			if ($('ul#tagName li input[type="checkbox"]:checked').length &&$('ul#tagName li input[type="checkbox"]:checked').length>0 ) {
				$('ul#tagName li input[type="checkbox"]').each(function (){
					if ($(this).is(':checked')) {
						var tagv = $(this).attr('value'); var tagN = $(this).attr('valuename');
						tags += tagv+',';
						tagsname += tagN+',';
					}
				});
				if (confirm("确定要删除 '"+tagsname+"' 标签吗？")) {
			      $.ajax({
			      type:'post',
			      url:'/tag/delete.json',
					dataType:'json',
			      data:{
			        idList:tags
			      },
			      success:function(data){
			        swal(data.message.message);
			        tagli.remove();
			      
			      },
			      error:function(data){
			        swal('系统繁忙请稍后再试！');
			      }
			    })
		    }

			}else{
				swal('请勾选要删除的标签！')
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
