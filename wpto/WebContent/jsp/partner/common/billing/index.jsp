<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
		<title>${systemName}-结算单列表</title>
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
		<script src="/theme/${theme}/js/util.js"></script>
		<script src="/theme/${theme}/js/commons-dates.js"></script>
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
				padding-top: 0;
				padding-bottom: 0;
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
    top: 20%;
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
.increasebox input[type="text"],select,textarea {
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
/*进度条*/
.increasebox .loadbox{
	position: absolute;top: 0;left: 0;
	width: 100%;
	height: 100%; background-color: rgba(53, 61, 72, 0.72);
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
			<h2 class="sub-header"><span>结算单列表</span></h2>
			<div class="content" style="overflow:hidden;">
				<form:form modelAttribute="billingCriteria" action="" id="queryForm" method="get">
					<fieldset>
						     <table width="100%"  >
							<tr>  
								<td align="left" style="padding-bottom: 15px;">
									<span class="tdtitle">时间：</span>
									<span class="shijian">  
										<input size="20" placeholder="开始时间" id="d11" class="calendarInput" name="billingBeginTimeBegin" type="text" value='<fmt:formatDate value="${billingCriteria.billingBeginTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss" />' />

										<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){start()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
									</span>
									<span class="shijian">
										<input size="20" placeholder="结束时间"  id="d12" class="calendarInput" name="billingEndTimeEnd" type="text" value='<fmt:formatDate value="${billingCriteria.billingEndTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss" />'/>

										<img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){end()}})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
									</span>
								</td>  
								<td style="padding-bottom: 15px;">
									<span class="tdtitle">用户名：</span>
									<input type="text" class="calendarInput"  name="username" value="<%=request.getParameter("username")==null?"":request.getParameter("username")%>"/>
									
								</td>
							</tr>
							<tr>  
								<td> 
									<span class="tdtitle">状态：</span>
									<!-- <div id="optionv" style="display: none;">
									<c:forEach var="s" items="${statusList}">
										<span><spring:message code="Status.${s}" /></span>
									</c:forEach>
									</div>
									 <form:select path="currentStatus" value="" multiple="1" style="    background-color: #fff;">  
						               <form:options items="${statusList}"/>
						               
						             </form:select>      -->
									 <% 
									String invitersV=request.getParameter("currentStatus");  
									request.setAttribute("invitersV",invitersV); 
									%>
									<select name="currentStatus" id="currentStatus" value="${invitersV}">
										<option value="">全部状态</option>
										<c:forEach var="s" items="${statusList}">
											<option value="${s}"><spring:message code="Status.${s}" /></option>
										</c:forEach>
									</select>
										

								</td>
								<td>
									<button class="btn btn-primary" id="chaxun" style="width: 426px;margin-left:106px;"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
										&nbsp;&nbsp;&nbsp;&nbsp;
										<span class="glyphicon glyphicon-search"></span>
										查询
										&nbsp;&nbsp;&nbsp;&nbsp;
									</button>
								</td>
							</tr>
							
							<tr >
								<td>
									<span class="payStat-radio-right" style="margin-left: 56px;"><input type="checkbox" name="requireSummary" value="true">显示小计</span>
								</td>
							</tr>
									
						</table>

					</fieldset>
				</form:form>
				 
			</div>
			<p data-show="true">
			<c:if test="${!empty CSVDownload}">
			<button class="btn educe-now" name="download" value="1" style="background-color: #B0BDD0;">↓ 导出当页数据</button>
			<button style="background-color: #5C6F87; color: #fff;margin-left: 7px;" class="btn educe-all" name="download" value="2">↓ 导出所有数据</button>
			</c:if>
			<c:if test="${!empty isShow}">
				<span class="addmoban addaddmoban" style="margin-top:0; margin-left: 7px;">+ 新增结算单</span><span class="addmoban suppladdmoban" style="margin-top:0; margin-left: 11px;">补结算</span>
			</c:if>
		</p>
			<div class="table-responsive">			
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>结算单ID</th>
		                  <th>对应用户或通道</th>
		                  <th>生成时间</th>
		                  <th>结算周期</th>
		                  <th>支付卡类型</th>
						  <th>交易金额</th>
		                  <th>结算金额</th>
		                  <th>期初余额1</th>
		                  <th>期初差额</th>
		                  <th>手续费</th>
		                  <th>结算比例</th>
		                  <th>状态</th>
		                  <th>备注</th>
		                 <th>操作</th>
                		</tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${billingList}">
						<tr class="trbody">
	                    <td>${i.billingId}</td>
	                    <td>${i.operate.username}</td>
	                    <td><fmt:formatDate value='${i.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
	                    <td><fmt:formatDate value='${i.billingBeginTime}' pattern='yyyy-MM-dd HH:mm:ss'/> 到 <br><fmt:formatDate value='${i.billingEndTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
						<td>
						<spring:message code='PayCardType.${i.data.payCardType}'/>
						</td>
						<td><fmt:formatNumber value="${i.faceMoney}" pattern="#0.00"/></td>
						<td><fmt:formatNumber value="${i.realMoney}" pattern="#0.00"/></td>
						<td><fmt:formatNumber value="${i.beginBalance}" pattern="#0.00"/></td>
						<td><fmt:formatNumber value="${i.beginBalance - i.realMoney}" pattern="#0.00"/></td>
						<td><fmt:formatNumber value="${i.faceMoney - i.realMoney}" pattern="#0.00"/></td>
						<td><fmt:formatNumber value="${i.commission}"  type="percent" maxFractionDigits="2" minFractionDigits="2" /></td>
						<td class="statusspan" style="min-width: 80px;"><spring:message code="Status.${i.currentStatus}" /></td>
						<td><span style="max-width: 150px; word-break: break-all;display: inline-block;">${i.data.memory}</span></td>
						<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position: absolute; width: 57px; padding-right: 10px; padding-left: 10px; margin-left: -9px; top: 30px;line-height: 26px;    list-style: none; z-index: 100;  border: 1px solid rgb(221, 221, 221);  border-radius: 5px; background-color: rgb(255, 255, 255);display: none;" class="toolbtns">
								<li><a href="<c:url value='/billing/get/${i.billingId}.shtml'/>" class="updatePage"> 查看 </a></li>
							</ul>
						</td>
						</tr>
					</c:forEach>
						<tr class="subtotaltr" style="background-color: #E4E9F0;" data-requireSummary="${requireSummary}">
							<td>本页小计</td>
							<td></td>
							<td></td>
							
							<td><fmt:formatNumber value="${totalFaceMoney }" pattern="#0.00"/></td>
							<td><fmt:formatNumber value="${totalRealMoney}" pattern="#0.00"/></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
				<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
			</div>
        </div>		
      </div>
		
    </div>
	<!-- 增加结算单 -->
	<!-- 补结算 -->
	<div class="increase addincrease" style="display:none;">
		<div class="increasebox">
			<h3></h3> 
			<form action="" id="addpartnerForm">
				<p><span class="tdtitle">时间：</span>
					<input type="hidden" name="billingType" value="">
					<input size="20" placeholder="开始时间" id="d22" class="calendarInput" name="billingBeginTime" type="text" value=""  style="margin-left:-5px;"/>
					<img onclick="WdatePicker({el:'d22',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
				
					<input size="20" placeholder="结束时间"  id="d23" class="calendarInput" name="billingEndTime" type="text" value=""/>
					<img onclick="WdatePicker({el:'d23',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
					<a class="yesterday" onclick="yesterdayFull()">昨日全天</a>
				</p>
				<p><h4 class="billingTypeInfo" style="font-size: 14px; text-align: center; display: block;color: red; margin-top: -23px;    margin-bottom: 22px;">注：补结算的金额将立即进入商户可结算资金！</h4></p>
				<p class="faceMoney"><span class="tdtitle">结算金额（元）：</span><input class="bigwidth calendarInput" name='faceMoney' type="text"  /></p>
				<p><span class="tdtitle">实际完成金额（元）：</span><input class="bigwidth calendarInput" name='realMoney' type="text"  /></p>
				<p><span class="tdtitle">结算比例（1为100%）：</span><input class="bigwidth calendarInput"  name='commission' type="text"  /></p>
				
				<p><span class="tdtitle">选择商户：</span>
					<select class="bigwidth" name="uuid">
						<c:forEach var="a" items="${partner}">
							<option value="${a.uuid}">${a.nickName}/${a.username}/${a.uuid}</option>
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
		});


	})
// 获取时间
var currentdate="";
        function getNowDate(){
            var date = new Date();
            var seperator1 = "-";
            var seperator2 = ":";
            var month = date.getMonth() + 1;
            var strDate = date.getDate()-1;
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            };
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            };
            currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " ";
            if($('#d11').val().length<1){
            	$('#d11').val(currentdate+' 00:00:00');
        	}
        	if($('#d12').val().length<1){
        		$('#d12').val(currentdate+' 23:59:59');
        	}
            
        }

getNowDate();
// 新增
$('.addaddmoban').on('click',function(){
	$("input[name='billingType']").val("addBilling");
	$('.increase h3').text('新增结算单');
	$('.increase h4').css('display','none');
	$('.increase .calendarInput').val('');
	$('.increase #supplementp').remove();
	$('.increase #kouchup').remove();
	$('.increase p.accountType').remove();
	$('.increase').css('display','block');
	
})
// 补
$('.suppladdmoban').on('click',function(){
	$("input[name='billingType']").val("patchBilling");
	$('.increase h3').text('补结算');
	$('.increase h4').css('display','block');
	$('.increase .calendarInput').val('');
	$('.increase').css('display','block');
	$("input[name='billingBeginTime']").val(getFormatDate(new Date()));
	$("input[name='billingEndTime']").val(getFormatDate(new Date()));
	if($('#supplementp').length && $('#supplementp').length>0){
		return;   
	}else{
		$('.increase .btnp').before('<p id="kouchup" class="transit"><span class="tdtitle">从商户未结算资金中扣除：</span><input class="calendarInput"  name="fromIncomingMoney" type="checkbox"  value="1" /></p><p id="supplementp"><span class="tdtitle" style="vertical-align: top;">补结算说明：</span><textarea class="bigwidth calendarInput" name="supplement" id="supplement" style="min-height:80px;"></textarea></p>');
		$('.increase .faceMoney').before('<p class="accountType" xmlns="http://www.w3.org/1999/html"><span class="tdtitle">入款账户：</span>'+
		'<input type="radio" value="transit"  name="accountType" checked="checked" /><span type="margin-right:20px;">余额基本户账户(transit)&nbsp;&nbsp;<span><input type="radio" value="incoming"  name="accountType" /><span>待结算账户(incoming)</span></p>');
		$("input[name='accountType']").change(function(){
			var accountType = $("input[name='accountType']:checked").val();
			if(accountType=="incoming"){
				$('.increase h4.billingTypeInfo').text("注：补结算的金额将立即进入商户待结算资金账户!")
				$('p.transit').hide();
			}else {
				$('.increase h4.billingTypeInfo').text("注：补结算的金额将立即进入商户可结算资金!")
				$('p.transit').show();
			}
		});
	}
});
$('.btnp .closespan').on('click',function(){
	$(this).parent().parent().parent().parent().css('display','none');
})

$('.confirm ').on('click',function(){
	var title = $(this).parent().parent().parent().find('h3').text();
	$(".addincrease .calendarInput").each(function(){
		if($(this).val().length==0){
			$(this).addClass('nullv');
		}else{
			$(this).removeClass('nullv');
		}

	})
	$('.loadbox').css('display','block');
		ajaxfunction(title);
	/* if($('.nullv').length>0){
		var tst = $('.nullv').eq(0).parent().find('.tdtitle').text();
		alert(tst+'不能为空！');
		$('.nullv').eq(0).focus();
	}else{
		//$(this).parent().parent().parent().parent().css('display','none');
		$('.loadbox').css('display','block');
		ajaxfunction(title);
	}*/
})  

$('.statusspan').each(function () {
	var text = $(this).text();
	if (text=='已结算') {
		$(this).parents('tr').addClass('colorgreen');
	}else{
		$(this).parents('tr').removeClass('colorgreen');
	}
})





//提交
function ajaxfunction(title){
	$.ajax( {
         type : "POST",  
         url : "/billing/create.json",  
         dataType:"json",
         data:$('#addpartnerForm').serialize(), 
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
         	$('.increase').css('display','none'); 
         	$('#load > p > span').text('0');
         },
         error : function() {  
            swal(title+'失败！')  
         }  
        });  
}



yesterdayFull = function () {
	$("input[name='billingBeginTime']").val(getSmpFormatDate2(new Date(new Date().getTime() - 24*60*60*1000),true));
	$("input[name='billingEndTime']").val(getSmpFormatDate(new Date(new Date().getTime() - 24*60*60*1000),true));
};


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
	});
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
