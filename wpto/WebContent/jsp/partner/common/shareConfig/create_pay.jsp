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

    <title>${systemName}-添加分成配置</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	 <link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="/theme/${theme}/js/sweetalert.min.js"></script>
<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
	<style>
		.blue{color:#428bca;}
		.fa{font-size:16px;}
		#edui1{z-index:9!important;}
		.edui-editor-breadcrumb{
			text-align:left !important;
		}
		#browserSync{line-height:12px !important;}
		#browser span{display:inline-block;line-height:12px;padding-left:5px;padding-right:5px;}
		#browserSync span{display:inline-block;line-height:12px;padding-left:5px;padding-right:3px;}
		#browser{line-height:12px !important;background:#fff !important;}
		
		html .hitarea {
			display: inline;
			float:none;
		}

	.fullScreenBg{
			position:fixed;
			background:#333;
			opacity:0.7;
			z-index:200;
			width:100%;
			height:100%;
		}
		.message{
			position:fixed;
			z-index:300;
			background:#fff;
			height:230px;
			width:300px;
			left:50%;
			top:230px;
			margin-left: -150px;
			text-align:center;
			border-radius:5px;
			overflow:hidden;
			padding-top:30px;
		}
		.sidebar{
			z-index:20;
		}
		.message .submit{
			border-radius:5px;
			height:40px;
			line-height:40px;
			text-align:center;
			width:100px;
			color:#fff;
			font-size:20px;
			background:#333;
			display:inline-block;
			position:absolute;
			bottom:20px;
			left:50%;
			margin-left:-50px;
			cursor:pointer;
		}
		.message .text{
			display:inline-block;
			font-size:18px;		
		}
		.submit{width:100%;text-align:center;}
		tr#kuozhantr{
			border-top: 2px solid #5C6F87;
		}
		button, input, select, textarea {
		    border-radius: 5px;
		    border: 1px solid #989898;
		    padding: 1px 3px;
		    background-color: #E6E6E6;
		}
		#kzsj{
			width: 100%;
			border:1px solid #ddd;
			margin-bottom: 20px;
		}
		#kzsj tr{
			border-bottom: 1px solid #ddd;
		}
		td{
			padding:16px 30px!important;
		}
		#kzsj>tbody>tr:nth-of-type(odd) {
		    background-color: #f9f9f9;
		}
		.product.none{
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
			<h2 class="sub-header"><span>添加分成配置</span></h2>
			<div class="table-responsive">	
				<form id="partnerForm"  name="partnerForm" >
				 <table class="table table-striped" style="">
		            <thead style="display:none;">
		                <tr>
		                  <td style="width:20%;text-align:right;"></th>
		                  <td style="width:80%;text-align:left;"></th>
		                </tr>
		            </thead>  
					<tbody>
						<tr>
							<td  style="text-align:right;width:20%;">分成名称</td>
							<td style="text-align:left;">
								<input name="shareConfigName" type="text" id="shareConfigName"/>
							</td>							
						</tr>
						<tr>
							<td  style="text-align:right;">分成对象类型</td>
							<td style="text-align:left;">
								<select name="objectType" id="objectType">
									<c:forEach var="a" items="${objectType}">
										<option value="${a}">
											<spring:message code='ObjectType.${a}'/>
										</option>
									</c:forEach>
								</select>
							</td>			 				
						</tr>
						
						<tr>
							<td  style="text-align:right;">分成对象</td>
							<td style="text-align:left;">
								<select name="shareUuid" id="shareUuid">
									<c:forEach var="a" items="${pay}">
										<option name="pay" value="${a.uuid}" style="display:none;">
											${a.username}
										</option>
									</c:forEach>
									<c:forEach var="a" items="${product}">
										<option name="product" value="${a.uuid}" style="display:none;">
											${a.username}
										</option>
									</c:forEach>
									<c:forEach var="a" items="${channel}">
										<option name="channel" value="${a.payMethodId}" style="display:none;">
											${a.name}
										</option>
									</c:forEach>
								</select>
							</td>							
						</tr>
						
					
						<tr class="product">
							<td  style="text-align:right;">分成产品</td>
							<td style="text-align:left;">
								<select name="objectId" id="objectId">
									<option value="0">---请选择产品---</option>
									<c:forEach var="a" items="${productList}">
										<option value="${a.productId}">${a.productName}</option>
									</c:forEach>
								</select>
							</td>							
						</tr>
						<tr>
							<td  style="text-align:right;">分成比例</td>
							<td style="text-align:left;">
								<input name="sharePercent" id="sharePercent" type="text"/>%
							</td>							
						</tr>
						<tr>
							<td  style="text-align:right;">支付卡类型</td>
							<td style="text-align:left;">
								<select name="payCardType" id="payCardType">
									<option value="UN">
										<spring:message code='PayCardType.UN'/>
									</option>
									<option value="DE">
										<spring:message code='PayCardType.DE'/>
									</option>
									<option value="CR">
										<spring:message code='PayCardType.CR'/>
									</option>
								</select>
							</td>							
						</tr>
						<tr>
							<td  style="text-align:right;">资金方向</td>
							<td style="text-align:left;">
								<input type="radio" checked value="plus" name="moneyDirect">增加资金
								<input type="radio" value="minus" name="moneyDirect"style="margin-left: 15px;">减少资金
							</td>							
						</tr>
						<tr>
							<td style="text-align:right;">付费类型用户</td>
							<td style="text-align:left;">
								<input type="radio" checked value="0" name="chargeType">预付费
								<input type="radio" value="1" name="chargeType" style="margin-left: 15px;">后付费
							</td>							
						</tr>
						<tr>
							<td  style="text-align:right;">区间的结算起始金额</td>
							<td style="text-align:left;">
								<input class="amountMoney" name="beginMoney" id="beginMoney" type="text"/>
							</td>							
						</tr>	
						<tr>
							<td  style="text-align:right;">区间的结算结束金额</td>
							<td style="text-align:left;">
								<input class="amountMoney" name="endMoney" id="endMoney" type="text"/>
							</td>							
						</tr>	
						<tr>
							<td  style="text-align:right;">最大重试次数</td>
							<td style="text-align:left;">
								<input class="amountMoney" name="maxRetry" id="maxRetry" type="text"/>
								<input name="isCreate" type="text" value="isCreate" style="display:none;"/>
							</td>							
						</tr>		
					</tbody>
				</table>

				<div class="submit"><input type="submit" value="新增分成配置" class="submitinput"></div>
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

	//执行展示分成对象代码
	function changeObjectInfo(){
		var objectType = $("#objectType option:selected").val();
		$("#shareUuid").find("option").css("display","none");
		$("#shareUuid").find("option[name='"+objectType+"']").css("display","block");
		$("#shareUuid").find("option").attr("selected",false);
		//默认选中切换后得第一个
		$("#shareUuid").find("option[name='"+objectType+"']").eq(0).attr("selected",true);
		
	}
	changeObjectInfo();
	
	//$('#objectType').find("option[value='product']").attr('checked','checked');
	$('#objectType').change(function(){
		changeObjectInfo();
		if ($('#objectType option:checked').attr('value')=='product') {
			$('.product').removeClass('none');
		}else{
			$('.product').addClass('none');
			
			$('.product select option').each(function(){
				$(this).removeAttr('selected');
			});
			$('.product select').find("option[index='0']").attr('selected',true);
		};
	})


	$(function(){
		
		var length=$("tbody tr").length;
		for(var i=0;i<length;i++){
			$("tbody tr").eq(i).find("td").eq(1).find("input[type='text']").css({"max-width":"300px;","width":"50%"});
			$("tbody tr").eq(i).find("td").eq(1).find('select').css({"max-width":"300px;","width":"50%"});
			$("tbody tr").eq(i).find("td").eq(1).css({"text-align":"left","paddingLeft":"5%"});
		};
});
	
	</script>

	
	<script>


		$(function(){

		$(".submitinput").on('click',function(e){
				e.preventDefault();
				var verification = true;//默认所有验证通过
				var reg = /^((\d|[123456789]\d)(\.\d+)?|100)$/;//0到100
				$("input[type='text']").each(function(){
					if ($(this).val()=='') {
						$(this).addClass('daempty');
					}else{
						$(this).removeClass('daempty');
					};
				});
				
				//验证输入框是否有空
				if ($('.daempty').length && $('.daempty').length>1) {
					var alertV = $('table.table-striped').find('.daempty').eq(0).parents('tr').find('td').eq(0).text();
					alert(alertV+'不能为空！');
					$('table.table-striped').find('.daempty').eq(0).focus();
					verification = false;
				}
				
				if(verification && !reg.test($("#sharePercent").val())){
					var alertV = $("#sharePercent").parent().prev().text();
					alert(alertV+"不能在0-100范围外！");
					$("#sharePercent").focus();
					verification = false;
				}
				
				//验证结算起始金额和结束金额不能为空 并且结束金额要大于起始金额
				if(verification){
					$(".amountMoney").each(function(){
						if($(this).val()<0){
							var alertV = $(this).parent().prev().text();
							alert(alertV+'不能小于0！');
							$(this).focus();
							verification = false;
						}
					});
				}
				
				var beginMoney = $("#beginMoney").val();
				var endMoney = $("#endMoney").val();
				if(verification && endMoney <= beginMoney){
					alert('结算结束金额不能小于等于结算起始金额！');
					$("#endMoney").focus();
					verification = false;
				}
				
				if(verification){
					$.ajax(
							 {  
								type:"POST",
								url:"/shareConfig/create.json",
								dataType:"json",
								data:$('#partnerForm').serialize(),
								success:function(data) {						
									swal(data.message.message);
								},
								error:function(XMLResponse){
									swal('添加分成配置失败!');
									console.log(333);
							},
						}			
					);
				}
				
			});
		
	})  
	 </script>
 
	
  </body>
</html>
