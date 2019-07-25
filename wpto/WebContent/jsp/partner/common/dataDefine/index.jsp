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
			@media (min-width:768px){
				.dateFrom{width:34% !important;}
				.dateTo{width:34% !important;}
			}
			input[name='username'] {
				/*margin-left: 32px;*/
			}
						
@media (max-width: 1200px){
	.content table tr td {
	    width: 100%!important;
	    display: inline-block;
	}
	.content table tr td input{
		width: 80%;
	}
}
@media (max-width: 768px){
	.content table tr td span.tabtitle{
		display: inline-block;
		width: 100%;
	}
	.content table tr td input{
		width: 100%;
	}
	.shijian{
		width:100%;
	}
	input[name="nickName"]{
		margin-left: 0;
	}
};
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
    top: 90px;
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
		</style>
  </head>
  <body>
  	
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
	<%	String str_date1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()); //将日期时间格式化 %>
	
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>扩展数据</span></h2>

			
			<div class="content">
				<form id="queryForm">
				<!--<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td align="left" >
						注册日期：从
						<span style="border:1px solid #A4BED4;display:inline-block;"><input size="20"  id="d11" class="calendarInput" name="createTimeBegin" type="text" value="" style="border:none;"/><img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>
						到
						<span style="border:1px solid #A4BED4;display:inline-block;"><input size="20"  id="d12" class="calendarInput" name="createTimeEnd" type="text" value="" style="border:none;"/><img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;"></span>					
						</td>
						<td>UUID:<input type="text" size="20" name="uuid" value="0"></input></td>
						<td>用户名:<input type="text" size="20" name="username" value=""></input></td>
						<td>昵称:<input type="text" size="30" name="nickName" value=""></input></td>
						<td align="center" width="7%"><input   type="submit" value="查询"  onClick="query()" onSubmit="return false;"/></td>
						</tr>
				</table>-->
					<table width="100%">
						
						<tr>
							<td>
								<span class="tabtitle">数据代码：</span><input class="" type="text" name="dataCode" value="">
							</td>
							<td>
								<span class="tabtitle">数据定义类型：</span><input type="text"   name="objectType" value="">
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align:center;">
								<button class="btn btn-primary" type="submit" value="查询"  onClick="query()"  onSubmit="return false;">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<span class="glyphicon glyphicon-search" ></span>
									查询&nbsp;&nbsp;&nbsp;&nbsp;
								</button>
							</td>
						</tr>
					</table>

				</form>			
			</div>
			<p>
				<span class="addmoban btn btn-primary queryForm-btn-wt110">新增数据</span>
			</p>
			<div class="table-responsive">
				<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>扩展数据ID</th>
		                  <th>数据代码</th>
		                  <th>数据名称</th>
		                  <th>数据定义类型</th>
		                  <th>状态</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.dataDefineId}</td>
							<td>${i.dataCode}</td>
							<td>${i.dataName}</td>
							<td>${i.objectType }</td>
							<td><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></td>		
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<a href="<c:url value='/dataDefine/get/${i.dataDefineId}.shtml' />"><li class="materialSelect">查看</li></a>
								</ul>  
							
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
        </div>
      </div>
    </div>
	<!-- 新增数据 -->
	<div class="increase addincrease" style="display:none;">
		<div class="increasebox">
			<h3>新增数据</h3> 
		
			<form action="" id="addpartnerForm">
			
				<p><span class="tdtitle">数据代码：</span><input class="bigwidth calendarInput" name='dataCode' type="text"  /></p>
				<p><span class="tdtitle">数据类型：</span>
					<select name="dataType" id="dataType" class="bigwidth">
			          <c:forEach items="${dataType}" var="i">
			            <option value="${i}" >${i}</option>
			          </c:forEach> 
			        </select>
				</p>
				<p><span class="tdtitle">数据名称：</span><input class="bigwidth calendarInput"  name='dataName' type="text"  /></p>
				<p><span class="tdtitle">数据说明：</span><input class="bigwidth calendarInput"  name='dataDescription' type="text"  /></p>

				<p><span class="tdtitle">是否必须输入：</span>
					<select name="required" id="required" class="bigwidth">
			            <option value="1" >是</option>
			            <option value="0" >否</option>
			        </select>
				</p>
				<p><span class="tdtitle">允许在哪里级别显示：</span>
					<select name="displayLevel" id="displayLevel" class="bigwidth">
			          <c:forEach items="${displayLevel}" var="i">
			            <option value="${i}" >${i}</option>
			          </c:forEach> 
			        </select>
				</p>
				<p><span class="tdtitle">用户输入方式：</span><input class="bigwidth calendarInput"  name='inputMethod' type="text"  /></p>
				<p><span class="tdtitle">允许在哪里级别输入：</span>
					<select name="inputLevel" id="inputLevel" class="bigwidth">
			          <c:forEach items="${inputLevel}" var="i">
			            <option value="${i}" >${i}</option>
			          </c:forEach> 
			        </select>
				</p>
				<p><span class="tdtitle">数据定义对应的类型：</span>
					<select name="objectType" id="objectType" class="bigwidth">
			          <c:forEach items="${objectType}" var="i">
			            <option value="${i}" >${i}</option>
			          </c:forEach> 
			        </select>
				</p>
				<p class="objectIdp"><span class="tdtitle">数据定义Id：</span>
					<select name="objectId" id="objectId" class="bigwidth">
						<option value="0">---请选择---</option>
			          <c:forEach items="${userType}" var="i">
			            <option value="${i.userTypeId}" class="user">${i.userTypeName}</option>
			          </c:forEach>
			          <c:forEach items="${documentType}" var="i">
			            <option value="${i.documentTypeId}" class="document">${i.documentTypeName}</option>
			          </c:forEach>
			          <c:forEach items="${productType}" var="i">
			            <option value="${i.productTypeId}" class="product">${i.productTypeName}</option>
			          </c:forEach> 
			        </select>
				</p>
				<p><span class="tdtitle">比较时的条件操作符：</span><input class="bigwidth calendarInput"  name='compareMode' type="text"  /></p>
				<p><span class="tdtitle">状态：</span>
					<select name="currentStatus" id="currentStatus" class="bigwidth">
						<c:forEach items="${statusList}" var="i">
				           <option value="${i}" ><spring:message code="Status.${i}" /></option>
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
  </body>
  <script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})
$('#objectType').change(function(){
	var xzzhi = $(this).find('option:selected').attr('value');
		$('#objectId option').each(function(){
			if ($(this).hasClass(xzzhi)) {
				$(this).css('display','block');
			}else{
				$(this).css('display','none');
			}
		})
		if (xzzhi=='business'||xzzhi=='activity'||xzzhi=='payMethod'||xzzhi=='withdrawMethod') {
			$('.objectIdp').css('display','none');
			$('#objectId').find('option').eq(0).attr('selected','selected');
		}else{
			$('.objectIdp').css('display','block');
		};
	
})

$('#objectType').each(function(){
	var xzzhi = $(this).find('option:selected').attr('value');
		$('#objectId option').each(function(){
			if ($(this).hasClass(xzzhi)) {
				$(this).css('display','block');
			}else{
				$(this).css('display','none');
			}  
		})
		if (xzzhi=='business'||xzzhi=='activity'||xzzhi=='payMethod'||xzzhi=='withdrawMethod') {
			$('.objectIdp').css('display','none');
			$('#objectId').find('option').eq(0).attr('selected','selected');
		}else{
			$('.objectIdp').css('display','block');
		};
	
	
})


$('.addmoban').on('click',function(){
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

function ajaxfunction(){
  
  $.ajax( {  
         type : "POST",  
         url : "/dataDefine/create.json",  
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
         	setTimeout(swal(data.message.message),1500);
            
         },
         complete:function(){
         	$('.loadbox').css('display','none'); 
         	$('.increase').css('display','none'); 
         	$('#load > p > span').text('0');
         },
         error : function() {  
            swal(data.message.message)  
         }  
        });  
}
  </script>
</html>
