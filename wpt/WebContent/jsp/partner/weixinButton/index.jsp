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
		<title>${systemName}-微信菜单</title>
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
#queryForm select, #queryForm input[type='text']{
	width: 370px;
}
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>微信菜单</span></h2>
			<div class="content" style="overflow:hidden;">
				<form id="queryForm">
						   <table width="100%"  >
							<tr>  
								<td>
									<span class="tdtitle">菜单名称：</span>
									<input type="text" class="calendarInput"  name="name" value="<%=request.getParameter("name")==null?"":request.getParameter("name")%>"/>
								</td>
								<td>
									<span class="tdtitle">跳转地址：</span>
									<input type="text" class="calendarInput"  name="menuUrl" value="<%=request.getParameter("menuUrl")==null?"":request.getParameter("menuUrl")%>"/>
								</td>
								<td style="text-align:center;">
									<button class="btn btn-primary" id="chaxun" style="width: 300px;"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
										&nbsp;&nbsp;&nbsp;&nbsp;
										<span class="glyphicon glyphicon-search"></span>
										查询
										&nbsp;&nbsp;&nbsp;&nbsp;
									</button>
								</td>
								</tr>
								<tr>
								
							</tr>
						</table>
				</form>
			</div>
			<p data-show="true">
			<!-- <c:if test="${!empty isShow}">
			</c:if> -->
			<span class="addmoban addaddmoban" style="margin-top:0; margin-left: 7px;">+ 新增菜单</span>
			</p>
			<div class="table-responsive">	
				 <table class="table table-striped">
		              <thead>
		               <tr>
		                  <th>菜单ID</th>
		                  <th>菜单名称</th>
		                  <th>跳转地址</th>
						  <th>父级菜单</th>
		                  <th>菜单级别</th>
		                  <th>KEY</th>
		                  <th>状态</th>
		                 <td>操作</td>
                		</tr>
		              </thead>
		              <% 
	                String nameV=request.getParameter("name");  
	                request.setAttribute("nameV",nameV); 
	                String menuUrlV=request.getParameter("menuUrl");  
	                request.setAttribute("menuUrlV",menuUrlV); 
	                %>
		              <c:if test="${nameV==null && menuUrlV==null || menuUrlV=='' && nameV==''}">
					<tbody class="menuall">
					<c:forEach var="i" items="${menu}">
						<tr class="trbody">
	                    <td>${i.weixinButtonId}</td>
	                    <td>${i.name}</td>
						<td><a href="<c:url value='${i.menuUrl}'/>"><c:url value='${i.menuUrl}'/></a></td>
						<td>${i.parentMenuName}</td>
						<td>${i.menuLevel}</td>
						<td>${i.key}</td>
						<td><spring:message code="Status.${i.currentStatus}" /></td>
						<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position: absolute; width: 57px; padding-right: 10px; padding-left: 10px;left: 50%; margin-left: -27px; top: 30px;line-height: 26px; list-style: none; z-index: 100;  border: 1px solid rgb(221, 221, 221);  border-radius: 5px; background-color: rgb(255, 255, 255);display: none;" class="toolbtns">
								<li><a href="<c:url value='/partnerMenu/get/${i.weixinButtonId}.shtml'/>" class="updatePage">修改</a></li>
							</ul>
						</td>
						</tr>
					</c:forEach>
				</tbody>
				</c:if>
				<tbody class="menusing">
					
	                
					<c:forEach var="i" items="${menuList}">
					<c:if test="${nameV==i.name || menuUrlV==i.menuUrl}">
						<tr class="trbody">
	                    <td>${i.weixinButtonId}</td>
	                    <td>${i.name}</td>
						<td><a href="<c:url value='${i.menuUrl}'/>"><c:url value='${i.menuUrl}'/></a></td>
						<td>${i.parentMenuName}</td>
						<td>${i.menuLevel}</td>
						<td>${i.key}</td>
						<td><spring:message code="Status.${i.currentStatus}" /></td>
						<td style="position: relative;"><span class="tools" style="right: 5px; cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
							<ul style="position: absolute; width: 57px; padding-right: 10px; padding-left: 10px;left: 50%; margin-left: -27px; top: 30px;line-height: 26px; list-style: none; z-index: 100;  border: 1px solid rgb(221, 221, 221);  border-radius: 5px; background-color: rgb(255, 255, 255);display: none;" class="toolbtns">
								<li><a href="<c:url value='/partnerMenu/get/${i.weixinButtonId}.shtml'/>" class="updatePage">修改</a></li>
							</ul>
						</td>
						</tr>
						</c:if>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
				<c:if test="${nameV==null && menuUrlV==null || menuUrlV=='' && nameV==''}">
				<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
			</c:if>
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
				<p><span class="tdtitle">菜单名称：</span><input class="bigwidth calendarInput" name='name' type="text"  /></p>
				<p><span class="tdtitle">是否为顶级菜单：</span>
					<input class="" name='isTop' type="radio" value="true" checked/><span style="margin-right:20px;">是</span>
					<input class="" name='isTop' type="radio"value="false" /><span>否</span>
				</p>
				<p><span class="tdtitle">跳转链接：</span><input class="bigwidth calendarInput"  name='menuUrl' type="text" placeholder="若为顶级菜单请输入'#'号" /></p>
				<p><span class="tdtitle">父级菜单：</span>
					<select class="bigwidth" name="parentMenuId">
						<c:forEach var="a" items="${menuList}">
	                    <c:if test="${a.menuUrl=='#'}">
	                    <option value="${a.weixinButtonId}">${a.name} </option>
	                    </c:if>
	                    </c:forEach>   
					</select>
				</p>
				<p><span class="tdtitle">键值：</span><input class="bigwidth calendarInput"  name='key' type="text" placeholder="显示图标等" /></p>
				<p><span class="tdtitle" style="vertical-align: top;">关联角色：</span>
					<span  style="display:inline-block; width:375px;">
					<c:forEach var="s" items="${roleList}">
						<span style="display:inline-block;margin-right: 8px;"><input type="checkbox" name="roleIds" value="${s.roleId}"/><span>${s.roleName}</span></span>
					</c:forEach>
					</span>
				</p>
				<p><span class="tdtitle">状 态：</span>
					<select name="currentStatus" id="currentStatus" class="bigwidth">
						<c:forEach var="s" items="${statusList}">
							<option value="${s}"><spring:message code="Status.${s}" /></option>
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
				    <p>加载中···</p>
				  </div>
				</div>
			</div>  
		</div>
	</div>
	<!-- 搜索——全部菜单 -->
	<!-- <ul id="menuSearch" style="display: none;">
		<c:forEach var="a" items="${menuList}">
		<li data-url="${a.menuUrl}">${a.name}</li>
		</c:forEach>
	</ul> -->


    
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
		



$('input[name="isTop"]').each(function(){
	if ($(this).is(':checked')) {
		if ($(this).next('span').text()=='是') {
			$("select[name='parentMenuName']").parent().css('display','none');
			$("select[name='parentMenuName']").append("<option value='0' class='wu' checked>无</option");
		}else{
			$("select[name='parentMenuName']").parent().css('display','block');
			$("select[name='parentMenuName'] option.wu").remove();
		};
	};
})
$('input[name="isTop"]').on('click',function(){
	if ($(this).next('span').text()=='是') {
		$("select[name='parentMenuName']").parent().css('display','none');
		$("select[name='parentMenuName']").append("<option value='0' class='wu' checked>无</option");
	}else{
		$("select[name='parentMenuName']").parent().css('display','block');
		$("select[name='parentMenuName'] option.wu").remove();
	};
})
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

// 新增
$('.addaddmoban').on('click',function(){
	$('.increase h3').text('新增菜单');
	$('.increase .calendarInput').val('');
	$('.increase #supplementp').remove();
	$('.increase').css('display','block');
	
})

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
	if($('.nullv').length>0){
		var tst = $('.nullv').eq(0).parent().find('.tdtitle').text();
		alert(tst+'不能为空！');
		$('.nullv').eq(0).focus();
	}else{
		//$(this).parent().parent().parent().parent().css('display','none');
		$('.loadbox').css('display','block');
		ajaxfunction(title);
	}
})  







//提交
function ajaxfunction(title){
  
  $.ajax( {  
         type : "POST",  
         url : "/partnerMenu/create.json",  
         dataType:"json",
         data:$('#addpartnerForm').serialize(), 
         beforeSend:function(){
         	$('.loadbox').css('display','block'); 
         	// for (var i = 0; i < 101; i++) {
         		
         	// };
         	//$('#load > p > span').text('加载中···');
         },
         success : function(data) {  
         	
         	$('.increase').css('display','none');
         	swal(title+'成功！');
            
         },
         complete:function(){
         	$('.loadbox').css('display','none'); 
         	$('.increase').css('display','none'); 
         	//$('#load > p > span').text('0');
         },
         error : function() {  
            swal(title+'失败！')  
         }  
        });  
}





	// 本页小计
	// $('.subtotal').each(function(){
	// 	var index = $(this).index();
	// 	var nums=0;
	// 	for(var i=0;i<$('.trbody').length;i++){
	// 		var num = parseFloat($('.trbody').eq(i).find('td').eq(index).text());
	// 		nums += num;
	// 	}
	// 	$(this).text(nums);
	// })
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
	})
	</script>
  </body>
</html>
