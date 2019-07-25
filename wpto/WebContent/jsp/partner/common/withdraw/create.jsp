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

    <title>${systemName}-申请提现</title>

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
	  <script src="/theme/${theme}/js/city.js"></script>
	  <script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
	  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>  
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	  <style>
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
		  #withdrawMoney,#bankAccountId,#withdrawType,#withdrawTypeId {
			  border: 1px solid #ddd;
			  width:32%; 
			  height: 30px;
			  line-height: 30px;
			  margin-top: 10px;
			  outline: none;
		  }
		  #widthdrawAffirm {
			  margin: 0 auto;
			  margin-top: 40px;
			  width: 100px;
			  margin-left: 117px;
		  }
		  .content table tr td {
			  height: 64px;
			  padding-left: 30px;
		  }
		  #queryForm select , #queryForm input[type='text'] {
			  height: 35px;
			  border: 1px solid #efefef ;
			  outline: none;
			  border-radius: 5px;
			  width:32%; 
		  }  

		  .tixian {
			  border: 1px solid #efefef;
			  width: 1100px;
			  border-radius: 0 5px 5px;
			  background-color: #eee;
		  }
		  .tixian>p {
			  margin: 30px 0;
			  display: block;
			  /*width: 30%;*/
			  margin-left: 30px;
		  }
		  /*新增*/
.addmoban{
	cursor: pointer;
	    margin-left: 31px;
    margin-top: 21px;
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
.increasebox input,select,textarea {
    border-radius: 5px;
    border: 1px solid #909090;
    padding: 3px 5px;
    background-color: #F7F7F7;
        min-width: 182px;
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
	  </style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
		  <div class="row">
			  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
			  <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				  <h2 class="sub-header"><span>申请提现</span></h2>
				  <div class="withdraw-form">
					  <div class="tixian">
					  	<c:if test="${allowNewWithdrawBankAccount==true}">
					    <span class="addmoban addaddmoban">+ 新增提现银行卡</span>
					    </c:if>
						  <p><span>请选择提现类型：</span>
							  <select name="withdrawTypeId" id="withdrawTypeId" >
								  <c:forEach var="type" items="${withdrawTypeList}">
									  <option  value="${type.withdrawTypeId}">
										  ${type.withdrawTypeName}
									  </option>
								  </c:forEach>

							  </select>
						  </p>
						  <p><span>请输入提现金额：</span>
							  <input type="number"  name="withdrawMoney" id="withdrawMoney">
						  </p>
						  <p><span>请选择提现账户：</span>
							  <select name="bankAccountId" id="bankAccountId">
								  <c:forEach var="ba" items="${bankAccountList}">
									  <option value="${ba.bankAccountId}">${ba.bankName} ${ba.bankAccountName} ${ba.bankAccountNumber}</option>
								  </c:forEach>
							  </select>
						  </p>
						  <p><input type="submit" id="widthdrawAffirm" class="btn btn-primary btn-block" value="确认提现"></p>
					  </div>


				  </div>

			  </div>

		  </div>
    </div>
    <div class="Pagination" style="text-align:center;width:100%;background:#fff;">	
		<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
	</div>
	<div class="increase addincrease" style="display: none;">
		<div class="increasebox">
			<h3>新增提现银行卡</h3> 
			<form action="" id="addpartnerForm">
				<p><span class="tdtitle">银行名称：</span><input class="bigwidth calendarInput" name="bankName" type="text"></p>
				<p><span class="tdtitle">选择省市：</span>
					    <select id="province" name="province"></select>  
					    <select id="city" name="city" ></select>  
					    <script class="resources library" src="area.js" type="text/javascript"></script>
					    <script type="text/javascript">_init_area();</script>
				</p>
				<p><span class="tdtitle">开户行：</span><input class="bigwidth calendarInput" name="issueBank" type="text"></p>
				<p><span class="tdtitle">开户人姓名：</span><input class="bigwidth calendarInput" name="bankAccountName" type="text"></p>
				<p><span class="tdtitle">银行账号：</span><input class="bigwidth calendarInput" name="bankAccountNumber" type="text"></p>
				<p style="text-align: center;margin-top: 35px;" class="btnp"><span class="confirm querenbtn">确 认</span><span class="closespan">取 消</span></p>
			</form>
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
// 新增
$('.addaddmoban').on('click',function(){
	$('.increase').css('display','block');
	
});
$('.btnp .closespan').on('click',function(){
	$(this).parent().parent().parent().parent().css('display','none');
});
$('.confirm ').on('click',function(){
	var title = $(this).parent().parent().parent().find('h3').text();
	$(".addincrease .calendarInput").each(function(){
		if($(this).val().length==0){
			$(this).addClass('nullv');
		}else{
			$(this).removeClass('nullv');
		}

	});
	if($('#s_province').find('option:selected').attr('value')=='省份') {
		$('#s_province').addClass('nullv');
		}else{
			$('#s_province').removeClass('nullv');
		};
	if($('#s_city option:selected').attr('value')=='地级市') {
		$('#s_city').addClass('nullv');
		}else{
			$('#s_city').removeClass('nullv');
		};	
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

// 新增银行接口
function ajaxfunction(title){
  
  $.ajax( {  
         type : "POST",  
         url : "/bankAccount/create.json",  
         dataType:"json",
         data:$('#addpartnerForm').serialize(), 
         success : function(data) {  
         	swal(data.message.message);
         },
         error : function(data) {  
            swal(data.message.message)  
         }  
        });  
}  





	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	});
	$("#widthdrawAffirm").click(function(){
        $.ajax({
            type: 'POST',
            url: "/withdraw/create.json?withdrawTypeId=" + $("#withdrawTypeId").val() + "&withdrawMoney="+$("#withdrawMoney").val()+"&bankAccountId="+$("#bankAccountId").val(),
            success: function(data){
                if(data.message.operateCode==102008){
					alert("提现成功！");
				}else{
					alert(data.message.message+'['+data.message.operateCode+']');
				}
			},
			error:function(data){
				alert("提现失败！")
			}
        });
	})
  </script>
</html>
