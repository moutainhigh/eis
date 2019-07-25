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

    <title>${systemName}-权限管理</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">

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
    <script src="/theme/${theme}/js/sweetalert.min.js"></script>
		<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
	  <style>
	  .sweet-alert button.cancel {
    background-color: #D0D0D0!important;
}
		  ul{
			  list-style: none;
		  }
		  /* 页面标题 */
		  .title {
			  background: #00a1e9;
			  color: #ffffff;
			  width: 200px;
			  height: 54px;
			  font-size: 25px;
			  line-height: 54px;
			  text-align: center;
			  margin-bottom: 0;
		  }
		  /* /页面标题 */
		  /* table外层div,及table样式 */
		  .content {
			  width: 100%;
			  border: 1px solid #f5f5f5;
			  position: relative;
			  border-radius: 5px;
			  background-color: #eee;
    		 margin-bottom: 26px;
		  }
		  .content table tr td {
			  height: 75px;
			  padding-left: 30px;
			  
		  }
		  /* /table外层div,及table样式 */

		  /* 下拉框及input通用 */
		  #queryForm select , #queryForm input[type='text'] {
			  height: 35px;
			  border: 1px #efefef solid;
			  outline: none;
			  min-width: 260px
		  }
		  /* /下拉框及input通用 */

		  .shijian {
			  border: 1px #efefef solid;
			  display: inline-block;
		  }
		  .clr {
			  color: red;
			  margin: 0 5px;
			  font-size: 16px;
		  }

		  /* 导出数据按钮 */
		  .apply-cash ,.educe-now , .educe-all{
			  margin-left: 10px;
		  }
		  /* /导出数据按钮 */

		  /* 查询及小计 */
		  .query , .show-total {
			  margin-left: 10px !important;
		  }
		  /* /查询及小计 */

		  select[name=bankName] {
			 
			  width: 350px;
		  }
		  .shoukuan {
			  position: relative;
		  }

		  input[name=fuzzyBankAccountName] {
			  width:350px;
		  }
		  /*商户订单号
		  */
		  .order-id {
			  width: 350px;
		  }
		  input, select {
		    border-radius: 5px;
		    padding: 2px 4px;
		    border: 1px solid #ddd;
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
    top: 12%;
    background-color: #fff;
    padding: 28px;
    width: 644px;
    left: 50%;
    border-radius: 5px;
    margin-left: -322px;
    padding-top: 0;
    padding-left: 0;
    padding-right: 0;
    overflow: hidden;
}
.increasebox span.tdtitle {
    width: 190px;
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
	  </style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>权限控制管理</span></h2>
			<div class="content">
				<form id="queryForm"  >
					<table width="100%" cellspacing="0" cellpadding="0" border="0" >
						<tr>
							<td>
								权限匹配的对象代码：
								<input type="text" name="objectTypeCode" id="objectTypeCode" class="" value="">
								<button   type="submit"  class="btn btn-primary query"  onclick="query()"  onSubmit="return false;">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<span class="glyphicon glyphicon-search"></span>
									查询&nbsp;&nbsp;&nbsp;&nbsp;
								</button>
							</td>
						</tr>
					</table>
				</form>
			</div> 
			<p><c:if test="${isShow != null }">
				<span class="addmoban Jurisdiction">+ 新增权限</span>
			</c:if >
			</p>
			<div class="table-responsive">
				 <table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>权限ID</th>
		                  <th>权限名</th>
		                  <th>权限说明</th>
		                  <th>权限匹配对象代码</th>
		                  <th>对应操作</th>
		                  <th>状态</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
					<tbody>
					<c:forEach var="i" items="${rows}">
						<tr>
							<td>${i.privilegeId}</td>
							<td>${i.privilegeName}</td>
							<td>${i.privilegeDesc}</td>
							<td>${i.objectTypeCode}</td>
							<td>${i.operateCode}</td>
							<td><spring:message code="Status.${i.currentStatus}" text="${i.currentStatus}" /></td>
							<td style="position:relative;">
								<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
								<ul style="position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:40px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
									<a href="<c:url value='/partnerPrivilege/get/${i.privilegeId}.shtml' />"><li>查看</li></a>
									<c:if test="${isShow != null }"><span><li class="delete" data-id="${i.privilegeId}" data-name="${i.privilegeName}" style="cursor: pointer;">删除</li></span></c:if>
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
   	<!-- 新增权限 -->
	<div class="increase addincrease" style="display:none;">
		<div class="increasebox">
			<h3>新增权限</h3> 
			<form action="" id="addpartnerForm">
				<p><span class="tdtitle">权限名称：</span><input class="bigwidth calendarInput" name='privilegeName' type="text"  /></p>
				<p id="supplementp"><span class="tdtitle" style="vertical-align: top;">详细说明：</span><input type="text" class="bigwidth calendarInput" name="privilegeDesc" id="privilegeDesc"/></p>
				
				<p><span class="tdtitle">对对象的操作：</span>
					<select class="bigwidth" name="operateCode">
						<option value="r" selected>r - 列出查看等只读通配操作</option>
						<option value="w">w - 读写通配操作</option>
						<option value="*">* - 具备完全权限的所有操作</option>
						<c:forEach var="a" items="${operateCode}">
							<option value="${a.code}">${a.code} - ${a.name} </option>
						</c:forEach>
					</select>
				</p>
				<p><span class="tdtitle">权限操作二级代码：</span><input type="text" name="subOperateCode" class="bigwidth" value="*"/>
				</p>
				<p><span class="tdtitle">权限匹配对象代码：</span>
					<select class="bigwidth" name="objectTypeCode">
						<c:forEach var="a" items="${objectTypes}">
							<option value="${a}">${a} </option>
						</c:forEach>
					</select>
				</p>
				<p><span class="tdtitle">权限匹配对象ID列表：</span><input class="bigwidth calendarInput" name='objectList' type="text"  /></p>
				<p><span class="tdtitle">权限匹配对象中的属性匹配：</span><input class="bigwidth calendarInput" name='objectAttributePattern' type="text"  /></p>
				<p><span class="tdtitle" style="vertical-align: top;">关联角色：</span>
						<span id="role" style="width:363px;display:inline-block;"><c:forEach var="a" items="${role}">
							<input type="checkbox" name="role" value="${a.roleId}"/><span style="margin: 0 15px 0 4px;">${a.roleName}</span>
						</c:forEach></span>
				</p>
				<p><span class="tdtitle">匹配方法：</span>
					<input type="text" name="mathPattern" placeholder="请输入“ * ”号，或者不填写" class="bigwidth calendarInput"/>
					<span style="display: block;margin-left: 193px;color: red;font-size: 12px;">注：“ * ”号表示全部，空表示严格按照list匹配</span>
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
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
  </body>
  <script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	})

	  $("input[name=fuzzyBankAccountName]").focus(function(){
			$(".queries").css("display","block");
	  });

	  // 删除
   $('.delete').on('click',function(){
    var Id = $(this).attr('data-id');
    var Name = $(this).attr('data-name');
    var dataarry = {'privilegeId':Id,'privilegeName':Name};
    swal({
		title: "您确定要删除"+Name+"吗？", 
		type: "",
		showCancelButton: true,
		closeOnConfirm: false,
		confirmButtonText: "确认",
		confirmButtonColor: "#ec6c62"
	}, function() {
      $.ajax( {  
         type : "POST",  
         url : "/partnerPrivilege/delete/"+Id+".json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) {  
            //$('.outpopup').removeClass('none');
            swal('权限名'+Name+'删除成功！')    
         },  
         error : function() {  
            //$('.outpopup').removeClass('none');
            swal('权限名'+Name+'删除失败！')   
         }  
        }); 
        }); 
   })
   // 新增
$('.Jurisdiction').on('click',function(){
	$('.increase .calendarInput').val('');
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
	$('#role input').each(function(){
		if ($(this).is(':checked')) {
			$(this).addClass('nullvinput');
		}else{
			$(this).removeClass('nullvinput');
		}
	}); 
	if( $('.nullvinput').length && $('.nullvinput').length>0){
		$('#role').removeClass('nullv');
	}else{
		$('#role').addClass('nullv');
	}
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
         url : "/partnerPrivilege/create.json",  
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
         	$('.loadbox').css('display','none'); 
         	$('#load > p > span').text('0'); 
            swal(title+'失败！')  
         }  
        });  
}

  </script>
</html>
