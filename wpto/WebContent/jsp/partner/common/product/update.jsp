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

    <title>${systemName}-${title}1</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	 <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	<link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
	<link href="/theme/${theme}/style/productCreateUpdate.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8">
        window.UEDITOR_HOME_URL = location.protocol + '//'+ document.domain + (location.port ? (":" + location.port):"") + "/ueditor/";
		//alert(window.UEDITOR_HOME_URL);
    </script>
	<script type="text/javascript" src="/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="/ueditor/ueditor.all.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/product/productCreateUpdate.jsp" %>
    <!-- Bootstrap core JavaScript ================================================== -->
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
		$(function(){
			var length=$("tbody tr").length;
			for(var i=0;i<length;i++){
				$("tbody tr").eq(i).find("td").eq(0).css("text-align","right");
				$("tbody tr").eq(i).find("td").eq(1).css({"text-align":"left","paddingLeft":"5%"});
			}			
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
		}) 
		$(function(){
		$("#productTypeForm").submit(function(e){
				e.preventDefault();
				//reg=/^[0-9]*$/;
				reg=/^\d*\.{0,1}\d{0,2}$/;
				var productName=$("#productName").val();
				var productCode=$("#productCode").val();
				var transactionTtl=$("#transactionTtl").val();
				var labelMoney=$("#labelMoney").val();
				var money=$("#PRICE_PRICE_STANDARDmoney").val();
				var coin=$("#PRICE_PRICE_STANDARDcoin").val();
				var point=$("#PRICE_PRICE_STANDARDpoint").val();
				var score=$("#PRICE_PRICE_STANDARDscore").val();
				var standardMoney=(money=="")&&(coin=="")&&(point=="")&&(score=="");
				standardMoneyNum=money+point+coin+score;
				if($("#productTypeId").val()==""){
					alert("请选择产品类型！");
				}else if(productName==""){
					alert("请填写产品名称！");
				}else if(productCode==""){
					alert("请填写产品代码！");
				}else if(transactionTtl==""||transactionTtl==0){
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
				/*else if(!reg.test(standardMoneyNum)){
					alert("标准价格必须为数字！")
				} else if(UE.getEditor('editor').getContent()==""){
					alert("请输入内容！")
				} */else if($("input[name=defaultNodeId]:checked").val()==undefined){
					alert("请选择发布位置！")
				}
				else{
					
					alert($("#productTypeForm").serialize());
					$(this).ajaxSubmit(
						{
							type:"POST",
							url:"/product/update.json",
							dataType:"json",
							data:$("#productTypeForm").serialize(),				
							success:function(data) {	
								alert(JSON.stringify(data));
								$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
							},
							error:function(XMLResponse){
								$(".fullScreenBg").show();$(".message").show();$(".message .text").html(XMLResponse.responseText);
								//alert("操作失败:" + XMLResponse.responseText);
								alert("失败")
							},
							
						}			
					);
				}
			})
			$(".message .submit").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
			})
		
	})
	</script>
	<script>
		$(function(){
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
	<script>
		//删除图片
		function delPic(names, imgName){
			var na = "showPic_"+names;
			var len=$("#pageContent_"+names).find("img").length;
			if(names == "productGallery"){
				var css = document.getElementById(imgName).style.border;
				if(css == "1px solid red" ){
					document.getElementById(imgName).style.border='0px none';
					document.getElementById('btn_delPic_'+imgName).innerHTML='确认删除';
				}else{
					document.getElementById(imgName).style.border='1px solid red';
					document.getElementById('btn_delPic_'+imgName).innerHTML='取消删除';
				}
				var val = "";
				$("#uploaded_productGallery").find("div").each(function(index,element){
					var id = $(element).attr("id");
					var css = document.getElementById(id).style.border;
					if(css != "1px solid red"){
						val+=id+",";
					}
				});
				var newVal = val.substring(0,val.length-1);
				if(len == 0){
					$("#"+names).val(newVal);
				}
			} else {
				var css = document.getElementById(na).style.border;
				if(css == "1px solid red" ){
					if(len == 0){
						$("#"+names).val("");
					}
					$("#"+na).css("border","0px none");
					$("#btn_delPic_"+names).html("确认删除");
				}else{
					if(len == 0){
						$("#"+names).val("DELETE");
					}
					$("#"+na).css("border","1px solid red");
					$("#btn_delPic_"+names).html("取消删除");
				}
			}
		}
	</script>
  </body>
</html>
