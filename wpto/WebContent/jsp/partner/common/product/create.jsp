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

    <title>${systemName}-新建产品-${productType.productTypeName}</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
	<link href="/theme/${theme}/style/productCreateUpdate.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
	<!--<link href="/theme/${theme}/style/webuploader.css" rel="stylesheet" type="text/css"/>-->

    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8">
        window.UEDITOR_HOME_URL = location.protocol + '//'+ document.domain + (location.port ? (":" + location.port):"") + "/ueditor/";
		//alert(window.UEDITOR_HOME_URL);
    </script>
	<script type="text/javascript" src="/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="/ueditor/ueditor.all.min.js"></script>
	<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="/theme/${theme}/js/sweetalert.min.js"></script>
		<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
    <!--<script src="/theme/${theme}/js/webuploader.js"></script>-->
  </head>
  <style type="text/css">
  button, input, select, textarea {
    font-family: inherit;
    font-size: inherit;
    line-height: inherit;
    border-radius: 5px;
    border: 1px solid #ddd;
    padding: 2px 5px;
}
.img_and_info{
	height: 88px;
}
.img_and_info video{
	width: 100%;
	max-height: 100%;
}
@media(max-width: 1300px){
	table,tbody,tr{
		display: block;
		width: 100%;
	}
	td{
		display: inline-block;
		float: left;
	}
}
/*.colorred{
	background-color: #F10500;
	border:none;
}*/
.btn_addPic{
 display: inline-block;
}

/*进度条*/
.increasebox {
        position: fixed;
    top: 0;
    background-color: #fff;
    padding: 28px;
    width: 100%;
    height: 100%;
    left: 0;
    border-radius: 5px;
    /* margin-left: -300px; */
    padding-top: 0;
    padding-left: 0;
    padding-right: 0;
    overflow: hidden;
    z-index: 99999;
        background-color: rgba(53, 61, 72, 0.69);
        display: none;
}
.increasebox .loadbox{
	position: absolute;top: 0;left: 0;
	width: 100%;
	height: 100%; 
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
  <body>
<!-- 进度条 -->
<div class="increasebox">
<div class="loadbox">
	<div id="caseBlanche">
	  <div id="rond">
	    <div id="test"></div>
	  </div>
	  <div id="load">
	    <p><span style="font-size:16px;">0%</span></p>
	  </div>
	</div>
</div>  
</div>
	<%@include file="/WEB-INF/jsp/common/product/productCreateUpdate.jsp" %>



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
	<script src="/theme/${theme}/js/addComment.js" type="text/javascript"></script>
	
	<script>
		var dom=document.getElementsByTagName('form')[0];
		

		var bar = $('.bar');
        var percent = $('.percent');
		$(function(){
			var length=$("tbody tr").length;
			for(var i=0;i<length;i++){
				$("tbody tr").eq(i).find("td").eq(0).css("text-align","right");
				$("tbody tr").eq(i).find("td").eq(1).css({"text-align":"left","paddingLeft":"5%"});
			}	


			// $('.filePrew').on('change', function(){
	  //       var fileName = $(this).val();
	  //       if(fileName){
	  //           console.info(fileName);
	  //       }
	  //   });
		
		})
		function selectEvent(val)
		{
			$.ajax({
			   type:"POST",
			   url:'/productType/get/'+val + ".json",
			   dataType:'json',
			   success: function(msg){
				   var json=msg;
				   var html="";
				   var curTr=$(".selectTr");
				   for(var data in msg.productType.dataDefineMap)
				   {
						//alert(msg.productType.dataDefineMap[data].inputLevel);
						if(msg.productType.dataDefineMap[data].inputLevel=='platform'){
							var name=msg.productType.dataDefineMap[data].dataDescription;
							var element="";
							
							switch (msg.productType.dataDefineMap[data].inputMethod) {
								case 'file':
									element="<input type='file' name='"+ msg.productType.dataDefineMap[data].dataCode+"'/>";
									break;
								case 'textarea':
									element="<textarea rows='5' cols='130' id='"+ msg.productType.dataDefineMap[data].dataCode+"' name='"+ msg.productType.dataDefineMap[data].dataCode+"'></textarea>";
									break;
								case 'select':
									element = "<select name='" + msg.productType.dataDefineMap[data].dataCode+ "'>";
									var opts = msg.productType.dataDefineMap[data].validDataEnum.split(",");
									for(var i=0; i < opts.length; i++){
										if(msg.productType.dataDefineMap[data].defaultValue == opts[i]){
											element += "<option name='" + msg.productType.dataDefineMap[data].dataCode+ "' value='" + opts[i] + "' selected>";
										} else {
											element += "<option name='" + msg.productType.dataDefineMap[data].dataCode+ "' value='" + opts[i] + "'>";
										}
										element += (opts[i]);
										element +=  "</option>";
									}
									element += "</select>";
									break;
								default:
									element="<input size='50'  type='text' name='"+ msg.productType.dataDefineMap[data].dataCode+"' />";
				
							}
							
							html+='<tr  class="selectTr"><td style="text-align:right;">'+name+'</td><td style="text-align:left;padding-left:5%;">'+element+'</td></tr>';
							  
						}	
									
				   }
			
				   $("#documentchangetype").html(msg.productType.productTypeDescription);		
						  
					if(curTr.length>0)curTr.remove();
					
					$("#lastTr").before(html);
					/* var tuanHtml="";
					tuanHtml+='<span class="tuanPrice">';
					tuanHtml+='<span style="padding-left:15px;padding-right:5px;font-weight:bold;">团购价格:</span>';
					tuanHtml+='<span>${moneyName}：<input name="PRICE_TUAN_PRICE.money" size="10" id="PRICE_TUAN_PRICE.money" type="text" value="" style="margin-left:5px;margin-right:10px;"/></span>';
					tuanHtml+='<span>${coinName}：<input name="PRICE_TUAN_PRICE.coin" size="10" id="PRICE_TUAN_PRICE.coin" type="text" value="" style="margin-left:5px;margin-right:10px;"/></span>';
					tuanHtml+='<span>${pointName}：<input name="PRICE_TUAN_PRICE.point" size="10" id="PRICE_TUAN_PRICE.point" type="text" value="" style="margin-left:5px;margin-right:10px;"/></span>';
					tuanHtml+='<span>${scoreName}：<input name="PRICE_TUAN_PRICE.score" size="10" id="PRICE_TUAN_PRICE.score" type="text" value="" style="margin-left:5px;margin-right:10px;"/></span>';
					tuanHtml+='</span>';
					$("select[name='supportTuan']").change(function(){
						if($("select[name='supportTuan']").val()=="true"){
							$(this).after(tuanHtml);
						}else{
							$(".tuanPrice").remove();
						}
					}) */
			   },
			   error: function(xml, err) {
					alert("出错了,请稍后再试!");
				}
			});
		}
	</script>
	<script>
		$(function(){
		$('#productCode').parents('tr').remove();
		// $('#productTypeForm input').each(function(){
		// 	$(this).parent().append('<span class="tishispan"></span>');
		// 	$('.tishispan').css({'color':'red','font-size':'12px'});
		// });
		$("#productTypeForm").submit(function(e){
				e.preventDefault();
				var reg=/^[0-9]*$/;
				//reg=/^\d*\.{0,1}\d{0,2}$/;
				var zhongWenYingWenShuZi=/^[\u4E00-\u9FA5A-Za-z0-9\.\*\/\+]+$/;
				var YingWenShuZi=/^\w+$/;
				var partnerName = $.trim($("#partnerName").val());
				var goodsSpec = $("#goodsSpec").val();
				var deliveryFromArea = $("#fhdz .city option:selected").text(); 
				var deliveryCompanyName = $("#deliveryCompanyName").val();
				var productOrigin = $("#productOrigin").val(); 
				var deliveryPriceListFile = $("#deliveryPriceListFile").val();
				//var availableCount = $("#availableCount").val();
				var productName=$("#productName").val();
				// var productCode=$("#productCode").val();
				/*var transactionTtl=$("#transactionTtl").val();
				var labelMoney=$("#labelMoney").val();
				var money=$("#PRICE_PRICE_STANDARDmoney").val();
				var coin=$("#PRICE_PRICE_STANDARDcoin").val();
				var point=$("#PRICE_PRICE_STANDARDpoint").val();
				var score=$("#PRICE_PRICE_STANDARDscore").val();
				var standardMoney=(money=="")&&(coin=="")&&(point=="")&&(score=="");
				standardMoneyNum=money+point+coin+score;*/
				var initCount = $("#initCount").val();
				var alerttext=null;
				$('#productTypeForm input').each(function(){

				if($(this).length && $(this).length>0){
					
					var requiredV = $(this).attr('required');//requiredV==true必填条件
					var idname = $(this).attr('id');
					//$(this).attr('type')=='text'测试
					if($(this).attr('type')=='text'){
						//库存
						if($(this).val().length==0){
							$(this).addClass('must');
							$(this).removeClass('incorrect');
							$(this).parent().find($('.tishispan')).text('内容不能为空');
						}else{
							$(this).removeClass('must');
							
							if(idname=='initCount'){
								if(initCount == -1){
									$(this).removeClass('incorrect');
									return;
								}else if(!reg.test(initCount) ){
									//alert('123');
									alerttext =="库存必须为数字！";
									$(this).addClass('incorrect');
									// $(this).parent().find($('.tishispan')).text('库存必须为数字！');
								}else if(initCount < -1){
									alerttext =="库存最小可填-1";
									$(this).addClass('incorrect');
									//$(this).parent().find($('.tishispan')).text('库存最小可填-1');
								}
						}else if(idname =='deliveryCompanyName'){
							if(!zhongWenYingWenShuZi.test(deliveryCompanyName)){
								// alert("产品合作快递输入内容不合法！");
								$(this).addClass('incorrect');
							}else{
								$(this).removeClass('incorrect');
							}
						
						}
						// else if(idname =='productCode'){
						// if(!YingWenShuZi.test(productCode)){
						// 		// alert("产品编码输入内容不合法！");
						// 		$(this).addClass('incorrect');
						// 	}else{
						// 		$(this).removeClass('incorrect');
						// 	}
						// }
						else if(idname =='partnerName'){
							if(!zhongWenYingWenShuZi.test(partnerName)){
								// alert("产品合作方输入内容不合法！");
								$(this).addClass('incorrect');
							}else{
								$(this).removeClass('incorrect');
							}
						}  
						// else if(idname =='productName'){
						// if(!zhongWenYingWenShuZi.test(productName)){
						// 		//alert("产品名称输入内容不合法！");
						// 		$(this).addClass('incorrect');
						// 	}else{
						// 		$(this).removeClass('incorrect');
						// 	}
						// }
						else if(idname =='goodsSpec'){
							if(!zhongWenYingWenShuZi.test(goodsSpec)){
								// alert("规格输入产品规格不合法！");
								$(this).addClass('incorrect');
							}else{
								$(this).removeClass('incorrect');
							}
						}
						}//else不为空时
					}//是否必须填写

					
				}//如果存在
				else{return false}
				})//遍历结束

				// 判断付费用户的文件是否可以填空的
				// $('#subscribeGallery').each(function(){
				// 	var ulLength = $(this).parent().parent().parent().find('.pageContent li');
				// 	console.log(ulLength.length);
				// 	if (ulLength.length>0) {
				// 		$(this).removeClass('must');
				// 	}else{
				// 		$(this).addClass('must');
				// 	};

				// })
				function For(){
				for(var i=0;i<$('#productTypeForm input').length;i++ ){
						$('#productTypeForm input').eq(i).attr('da-index',i);
						// console.log(i);
						$('#productTypeForm input').eq(i).removeClass('colorred');
					}
				}
				For();
				$('#browser  input').each(function(){
					if($(this).is(':checked')){
						$(this).addClass('inputgx');
					}
					if($('#browser .inputgx').length>0){
						$(this).parents('#browser').removeClass('must');
					}else{
						$(this).parents('#browser').addClass('must');
						$(this).parents('#browser').attr('da-index',$('#browser  input').eq(0).attr('da-index'));
					}
				})
				// $('#browserSync input').each(function(){
				// 	if($(this).is(':checked')){
				// 		$(this).addClass('inputgx');
				// 	}
				// 	if($('#browserSync .inputgx').length>0){
				// 		$(this).parents('#browserSync').removeClass('must');
				// 	}else{
				// 		$(this).parents('#browserSync').addClass('must');
				// 		$(this).parents('#browserSync').attr('da-index',$('#browserSync  input').eq(0).attr('da-index'));
				// 	}
				// })
				var jinggao = $('.must').eq(0).parents('tr').find('td').eq(0).text();
				var cuowu = $('.incorrect').eq(0).parents('tr').find('td').eq(0).text();
				// if($('#warning').text()=='产品编码已存在,请重新输入'){
					
				// 	$('#productCode').addClass('warning');
				// 	$('#productCode').focus();
				// }else{
				// 	$('#productCode').removeClass('warning');
				// }
			
				// else{
				// 	alert(jinggao+"输入内容不能为空!");
				// 	$('.must').eq(0).focus().addClass('colorred');
					//$('.must').eq(0).addClass('focusclass');
					//$('.incorrect').eq(0).removeClass('focusclass');
				// }
				
				
				// alert(alerttext);
				
				// if(initCount == ""){
				// 	alert("请输入库存！");
				// }else if(!reg.test(initCount)){
				// 	alert("库存必须为数字！");
				// }else if(initCount < -1){
				// 	alert("库存最小可填-1")
				// }	

				// else if(deliveryCompanyName == ""){
				// 	alert("请输入产品合作快递！");
				// }else if(!zhongWenYingWenShuZi.test(deliveryCompanyName)){
				// 	alert("产品合作快递输入内容不合法！");

				// }else if(deliveryPriceListFile == ""){
				// 	alert("请上传快递费报价单！");
				// }
				// else if(productCode == ""){
				// 	alert("请输入产品编码！");
				// }else if(!zhongWenYingWenShuZi.test(productCode)){
				// 	alert("产品编码输入内容不合法！");
				// }

				// else if(partnerName == ""){
				// 	alert("请输入产品合作方！");
				// }else if(!zhongWenYingWenShuZi.test(partnerName)){
				// 	alert("产品合作方输入内容不合法！");
				// }

				// else if($("#productTypeId").val()==""){
				// 	alert("请选择产品类型！");
				// }

				// else if(productName == ""){
				// 	alert("请输入产品名称！");
				// }else if(!zhongWenYingWenShuZi.test(productName)){
				// 	alert("产品名称输入内容不合法！");
				// }

				// else if(goodsSpec == ""){
				// 	alert("请输入产品规格！");
				// }else if(!zhongWenYingWenShuZi.test(goodsSpec)){
				// 	alert("规格输入产品规格不合法！");
				// }	
				/*else if(transactionTtl==""||transactionTtl==0){
					alert("请填写订单有效期（秒）并且不能超过172800秒！");
				}else if(transactionTtl>172800){
					alert("订单有效期（秒）不能超过172800秒 ！");
				}else if(labelMoney==""){
					alert("请填写市场价格！");
				}else if(!reg.test(labelMoney)){
					alert("市场价格必须为数字！")
				}else if(standardMoney){
					alert("请填写至少一个标准价格！");
				}
				else if(!reg.test(standardMoneyNum)){
					alert("标准价格必须为数字！")
				} else if(UE.getEditor('editor').getContent()==""){
					alert("请输入内容！")
				} else if($("input[name=defaultNodeId]:checked").val()==undefined){
					alert("请选择发布位置！")
				}*/
				// var AAAAA="";
				//  $('#productTypeForm input').each(function(){
				//  	var bbb = $(this).parents('tr').find('td').eq(0).text()+',';
				//  	AAAAA+=bbb;
				//  })
				//  console.log(AAAAA);
				if($('#productTypeForm .must').length==0 && $('#productTypeForm .incorrect').length==0 && $('.warning').length==0){
						if ($("#vipFree").length && $("#vipFree").length>0) {
					alert('vipFree');
						if ($("#vipFree").val()!=='0' && $("#vipFree").val()!=='1' && $("#vipFree").val()!=='2') {
							alert('vip免费请输入数字0、1或2');
							$("#vipFree").addClass('xinzengclass');
							$('#vipFree').focus();
							}else{
								$("#vipFree").removeClass('xinzengclass');
							}
						};
						if($("#tags").length && $("#tags").length>0){
							//alert('tags');
							if($('#tags').val().indexOf('，')>-1) {
								alert('标签内容请输入英文逗号！');
								$("#tags").addClass('xinzengclass');
								$('#tags').focus();
							}else{
								$("#tags").removeClass('xinzengclass');
							}
						}
						if ($('.xinzengclass').length==0) {

						//alert('tong');
					$(this).ajaxSubmit(  
						{
							type:"POST",
							url:"/product/create.json",
							dataType:"json",
							data:$("#productTypeForm").serialize(),		
							beforeSend:function(){
					         	$('.increasebox').css('display','block');

					         	// for (var i = 0; i < 101; i++) {
					         		
					         	// };
					         	// $('#load > p > span').text("加载中···");
					         },
							 uploadProgress: function(event, position, total, percentComplete) {//上传的过程
								//position 已上传了多少
								//total 总大小
								//percentVal已上传的百分数
								var percentVal = percentComplete + '%';
								// console.log(percentVal, position, total);
								$('#load > p > span').text(percentVal);
							 },
					         success : function(data) {  
					         	
					         	$('.increasebox').css('display','none');
					         	swal(data.message.message+"["+data.message.operateCode+"]");
					         	$('.showSweetAlert .confirm').on('click',function(){
					         		window.location.reload();
					         	})  
					            
					         },
					         complete:function(){
					         	//$('.loadbox').css('display','none'); 
					         	$('.increasebox').css('display','none'); 
					         	$('#load > p > span').text('0');
					         },
					         error : function(data) {  
					         	$('.increasebox').css('display','none');
					            swal("操作失败");
					         }  		
							// success:function(data) {
							// 	//alert(JSON.stringify(data));
							// 	//$(".fullScreenBg").show();$(".message").show();
							// 	//$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
								
							// },
							// error:function(XMLResponse){
							// 	//$(".fullScreenBg").show();$(".message").show();$(".message .text").html(XMLResponse.responseText);
							// 	//alert("操作失败:" + XMLResponse.responseText);
							// 	//alert("失败");
							// 	//$('.focusclass').focus();
								
							// },
							
						}			
					);}
				 }else{
					 //alert('123');
				 		if ($('.must').length && $('.must').length>0) {
							var mustIndex = parseInt($('.must').eq(0).attr('da-index'));
						}else{
							var mustIndex = 1000;
						}
						if ($('.incorrect').length && $('.incorrect').length>0) {
							var incorrectIndex = parseInt($('.incorrect').eq(0).attr('da-index'));
						}else{
							var incorrectIndex = 1000;
						}
						if($('.warning').length==1){
							alert('产品编码已经存在，请重新输入！')
						}else{
							if(mustIndex<incorrectIndex){
								alert(jinggao+"输入内容不能为空！");
								//$('.must').eq(0).addClass('focusclass');
								$('.must').eq(0).focus();
								console.log(incorrectIndex,mustIndex)
							}else{
								alert(cuowu+'输入内容不合法！');
								$('.incorrect').eq(0).focus();
								console.log(incorrectIndex,mustIndex)
								//$('.incorrect').eq(0).addClass('focusclass');
								//$('.must').eq(0).removeClass('focusclass');
								// console.log(alerttext);
							}
						}
						
				 }
				 

			})//提交结束


			$(".message .submit").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
			})

		
	})
	</script>
	<script>
		$(document).ready(function(){
				$("#browser").treeview();
				$("#browserSync").treeview(); 
			});
			$(function(){
				var m=$("#browser").find("input[name='defaultNodeId']").length;
				var checked;
				for(var i=0;i<m;i++){
					$('#browser input[name="defaultNodeId"]:eq('+i+')').click(function(){
						checked=$('#browser').find('input[name="defaultNodeId"]:checked').val();
						var index=$('#browser input[name="defaultNodeId"]').index(this);
						//alert($("#browserSync").find(".subNodeId").eq(index)[0].checked);
						 if($("#browserSync").find(".subNodeId").eq(index)[0].checked==true){
							alert("同步时不能选择跟发布位置一样的节点！")
							$("#browserSync").find(".subNodeId").eq(index).removeAttr("checked");
						} 
					})
				}
				
				$("#browserSync").find(".subNodeId").click(function(){
					if($(this).val()==checked){
						alert("同步时不能选择跟发布位置一样的节点！")
						$(this).removeAttr("checked");
					}
				})
				$(".hitarea").click(function(){
					if($(this).hasClass("collapsable-hitarea")){
						$(this).siblings("ul").slideDown();
						$(this).siblings(".folder").children("i").removeClass("fa-folder-o");
						$(this).siblings(".folder").children("i").addClass("fa-folder-open-o");
					}else if($(this).hasClass("expandable-hitarea")){
						$(this).siblings("ul").slideUp();
						$(this).siblings(".folder").children("i").addClass("fa-folder-o");
						$(this).siblings(".folder").children("i").removeClass("fa-folder-open-o");
					}
				})
			}) 	
	</script>
	<script type="text/javascript">
		$(function(){
			function tdclear(){
				$('tr').each(function(){
					$(this).addClass('clearfix');
				})
				$('#refUrl').parents('tr').empty();
			}
			tdclear();
		})
		// 轮播图
		// select
			// $('#displayTypeId').change(function(){
			// 	if ($('#displayTypeId option:selected').attr('value')=='176005') {
			// 		if ($('.displayIndextr').length>0 && $('.displayIndextr').length) {
			// 		return false;
			// 	}else{
			// 			$(this).parents('tr').after('<tr class="displayIndextr clearfix"><td  style="width:20%;text-align:right;" class="productLeft">在轮播图中的展现顺序：</td><td style="text-align:left;width:80%;padding-left:5%;"class="productRight clearfix"><input type="number" value="1" max="10" min="1" name="displayIndex"/><span>（最小为1，最大为10）</span></td></tr>');
			// 		}
			// 	}else{
			// 		$('.displayIndextr').remove();
			// 	}
			// })

		// $('#productCode').blur(function(){
		// 	var productCodeValue = $(this).val();
		// 	var data = {'productCode':productCodeValue};
		// 	$.ajax({
		// 		type:"POST",
		// 		url:"/product/notify.json",
		// 		dataType:"json",
		// 		data:data,			
		// 		success:function(data) {
		// 			$('#warning').remove();
		// 			$('body').append("<span style='display:none;' id='warning'>"+data.warning+"</span>")
		// 		},  
		// 		error:function(){
					
		// 		}  
		// 	})
		// })
	</script>
  </body>
</html>
