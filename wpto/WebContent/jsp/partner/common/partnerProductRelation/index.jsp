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
		<title>${systemName}-产品列表</title>
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
				width:33%;
				padding-top: 0;
				padding-bottom: 0;
			}
			#queryForm td input[type='text'],
			#queryForm td select{
				width: 70%!important;
			}
			span.tdtitle {
			width: 100px;
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
.subtotaltr.none{
	display: none;
}
/*新增*/
.addmoban{
	cursor: pointer;
}
.increase {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 9999;
    background-color: rgba(51,51,51,0.5);
    width: 100%;
    height: 100%;
}
.increasebox {
        position: absolute;
    top: 22%;
    background-color: #fff;
    padding: 28px;
    width: 600px;
    left: 50%;
    border-radius: 5px;
    margin-left: -300px;
    padding-top: 0;
    padding-left: 0;
    padding-right: 0;
    overflow: hidden;
}
.increasebox span.tdtitle {
    width: 140px;
    display: inline-block;
    text-align: right;
}
.increasebox h3{
	    font-size: 22px;
    margin-top: 0;
    text-align: center;
    margin-bottom: 38px;
    background-color: #eee;
    padding: 15px;
    border-bottom: 2px solid #ddd;
}
.increasebox .shijian{
	margin-left: -5px;
}
.increasebox select{
	margin-left: -5px;
}
.increasebox input,select,textarea {
    border-radius: 5px;
    border: 1px solid #909090;
    padding: 3px 5px;
    background-color: #F7F7F7;
        min-width: 170px;
}
.increasebox p{
	margin: 0 0 20px;
	padding: 0 18px;
}
.confirm {
	cursor: pointer;
}
.btnp .closespan{
	    display: inline-block;
    padding: 7px 27px;
    background-color: #B5B5B5!important;
    border: none;
    border-radius: 5px;
    color: #fff;
    font-size: 17px;
    margin-left: 25px;
    cursor: pointer;

}
.increasebox .bigwidth{
	width: 363px!important;
}
.toolbtns span.remove{
	cursor: pointer;
}
/*进度条*/
.increasebox .loadbox{
	position: absolute;top: 0;left: 0;
	width: 100%;
	height: 100%;    background-color: rgba(53, 61, 72, 0.72);
}
#caseBlanche {
  /*background-color : #C2C2C2;*/
  height : 140px;
  width : 150px;
  padding-top : 10px;
  float : left;
  position : relative;
  top: 50%;
  left: 50%;
  margin-left: -75px;
  margin-top: -70px;
}
#rond {
  height  : 100px;
  width : 100px;
  border : 1px solid #fff;
  border-radius : 50%;
  position :absolute;
      top: 50%;
    left: 50%;
    animation: rond 2s infinite;
    -webkit-animation: rond 2s infinite;
    margin-top: -50px;
    margin-left: -50px;
}

@keyframes rond {
  0% {transform : rotate(0deg);}
  100% {transform : rotate(360deg);}
}

@-webkit-keyframes rond {
  0% {-webkit-transform : rotate(0deg);}
  100% {-webkit-transform : rotate(360deg);}
}
#test {
  height : 10px;
  width : 10px;
  position : absolute;
  background-color : #fff;
  border-radius : 50%;
  top : 0px;
  left : 10px;
  margin: 5px;
}
#caseBlanche #load {
  color : #fff;
  font-family : calibri;
  text-align : center;
  position : absolute;
  top : 59px;
  left :0;
  width: 100%;
}

	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>产品列表</span></h2>
			<div class="content" style="overflow:hidden;">
				<form:form modelAttribute="shareConfigCriteria" action="" id="queryForm" method="get">
					<fieldset>
						     <table width="100%"  >
							<tr>  
								<td style="padding-bottom: 15px;">
									<span class="tdtitle">用户名：</span>
									<input type="text" class="calendarInput"  name="username" value="<%=request.getParameter("username")==null?"":request.getParameter("username")%>"/>
								</td>
								<td style="padding-bottom: 15px;">
									<span class="tdtitle">用户UUID：</span>
									<form:input path="shareUuid"/>
								</td>
								<td style="padding-bottom: 15px;">
									<span class="tdtitle">商品名称：</span>
									<input type="text" class="calendarInput"  name="productName" value="<%=request.getParameter("productName")==null?"":request.getParameter("productName")%>"/>
								</td>
							</tr>
							<tr>  
								<td>
									<span class="tdtitle">商品ID：</span>
									<form:input path="objectId"/>
								</td>
								<td>
									<span class="tdtitle">商品代码：</span>
									<input type="text" class="calendarInput"  name="productCode" value="<%=request.getParameter("productCode")==null?"":request.getParameter("productCode")%>"/>
								</td>
								<td> 
									<span class="tdtitle">状态：</span>
						
						           <% 
										String invitersV=request.getParameter("currentStatus");  
										request.setAttribute("invitersV",invitersV); 
									%>
									<select name="currentStatus" id="currentStatus" value="${invitersV}">
										<c:forEach var="s" items="${statusList}">
											<option value="${s}"><spring:message code="Status.${s}" /></option>
										</c:forEach>
									</select>  
								</td>
							</tr>
							<tr>
								<td colspan="3" style="text-align:center;">
									<button class="btn btn-primary" id="chaxun" style="width: 426px;margin-left:106px;margin-top: 24px;"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
										&nbsp;&nbsp;&nbsp;&nbsp;
										<span class="glyphicon glyphicon-search"></span>
										查询
										&nbsp;&nbsp;&nbsp;&nbsp;
									</button>
								</td>
							</tr>
						</table>

					</fieldset>
				</form:form>
				 
			</div>
			<p data-show="true">
				
			<c:if test="${!empty isShow}"> 
				<span class="addmoban addproduct" style="margin-top:0; margin-left: 7px;">+ 增加产品</span>
			</c:if> 
		</p>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>商户名称[商户ID]</th>
		                  <th>产品名称</th>
		                  <th>产品ID</th>
						  <th>产品代码</th>
		                  <th>状态</th>
		                 <td>操作</td>
                		</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${shareConfig}">
						<tr class="trbody">
	                    <td>${i.operate.username}[${i.shareUuid}]</td>
	                    <td>${i.operate.productName}</td>
	                    <td>${i.objectId}</td>
	                    <td>${i.operate.productCode}</td>
						<td><spring:message code="Status.${i.currentStatus}" /></td>
						<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position: absolute; width: 57px; padding-right: 10px; padding-left: 10px; margin-left: -28.5px; top: 30px;left:50%;line-height: 26px;    list-style: none; z-index: 100;  border: 1px solid rgb(221, 221, 221);  border-radius: 5px; background-color: rgb(255, 255, 255);display: none;" class="toolbtns">
									<c:if test="${!empty isShow}">
										<li><span dataurl="/partnerProductRelation/delete/${i.shareConfigId}.json" class="remove">删除</span></li>
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
		<!-- 增加产品 -->
	<div class="increase addincrease" style="display:none;">
		<div class="increasebox">
			<h3>添加产品</h3> 
			<form action="" id="addpartnerForm">
				
				<p><span class="tdtitle">选择产品：</span>
					<select class="bigwidth productsel" name="productId">
						<c:forEach var="a" items="${product}">
							<option value="${a.productId}">${a.productName} </option>
						</c:forEach>
					</select>
				</p>
				<p><span class="tdtitle">选择商户：</span>
					<select class="bigwidth usersel" name="uuid">
						<c:forEach var="a" items="${partner}">
							<option value="${a.uuid}">${a.username} </option>
						</c:forEach>
					</select>
				</p>
				<p style="text-align: center;margin-top: 35px;" class="btnp"><span class="confirm querenbtn">确 认</span><span class="closespan">取 消</span></p>
			</form>
			<!-- 进度条 -->
			<div class="loadbox" style="display: none;">
				<div id="caseBlanche">
				  <div id="rond">
				    <div id="test"></div>
				  </div>
				  <div id="load">
				    <p>已加载 <span></span> %</p>
				  </div>
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
				console.log(zhi+','+zhixz);
			}else{
				$(this).removeAttr('selected');
				console.log(zhi+','+zhixz);
			}
		})
	})
// 删除
$('.toolbtns span.remove').on('click',function(){
	var shareConfigId = $(this).attr('dataurl');
swal({
		title: "您确定要删除吗？", 
		type: "",
		showCancelButton: true,
		closeOnConfirm: false,
		confirmButtonText: "确认",
		confirmButtonColor: "#ec6c62"
	}, function() {
		$.ajax({
			url: shareConfigId,
			type: "POST"
		}).done(function(data) {
			
			swal("操作成功!", "已成功删除数据！", "");
		}).error(function(data) {
			swal("OMG", "删除操作失败了!", "");
		});
	});


	



})
// 添加产品
$('.addproduct').on('click',function(){
	$('.increase .calendarInput').val('');
	$('.increase').css('display','block');
	
})
$('.btnp .closespan').on('click',function(){
	$(this).parent().parent().parent().parent().css('display','none');
})

$('.confirm ').on('click',function(){
	var title = $(this).parents('.increasebox').find('h3').text();
	var  productId = $('.productsel option:selected').attr('value');
	var uuid = $('.usersel option:selected').attr('value');
	ajaxfunction(title,uuid,productId);
});  



//提交
function ajaxfunction(title,uuid,productId){
  var dataarry = {'shareUuid':uuid,'objectId':productId};
  
  $.ajax( {  
         type : "POST",  
         url : "/partnerProductRelation/create.json",  
         dataType:"json",
         data:dataarry, 
         beforeSend:function(){
         	$('.loadbox').css('display','block'); 
         	for (var i = 0; i < 101; i++) {
         		$('#load > p > span').text(i);
         	};
         },
         success : function(data) {  
         	
         	$('.increase').css('display','none');
         	setTimeout(swal(title+data.message.message),1500);
            
         },
         complete:function(){
         	$('.loadbox').css('display','none');  
         	$('#load > p > span').text('0');
         },
         error : function() { 
          $('.increase').css('display','none');
            swal(title+'失败！')  
         }  
        });  
};


	</script>
	<!-- 导出数据 -->  
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
	</script>
  </body>
</html>
