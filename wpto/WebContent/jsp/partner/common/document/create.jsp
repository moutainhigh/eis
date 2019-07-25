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

    <title>${systemName}-${title}</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	
    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	 <link href="/theme/${theme}/style/documentCreate.css" rel="stylesheet">
	 <link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
	<!-- <script src="/theme/${theme}/js/lib/jquery.js" type="text/javascript"></script> -->
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" charset="utf-8">
        window.UEDITOR_HOME_URL = location.protocol + '//'+ document.domain + (location.port ? (":" + location.port):"") + "/ueditor/";
		//alert(window.UEDITOR_HOME_URL);
    </script>
	<script type="text/javascript" src="/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="/ueditor/ueditor.all.min.js"></script>
	<script src="/theme/${theme}/js/addComment.js" type="text/javascript"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="/theme/${theme}/style/documentCreateUpdate.css">
  </head>
  <style type="text/css">
	input,select{
		border-radius: 5px;
		border:1px solid #ddd;
		padding: 3px;
	}
   .submit input[type="submit"]{
   	padding: 7px 20px;
   	background-color: #0F8A0E;
   	color: #fff;
   	border: 2px solid #0F8A0E;
   }
   .submit input[type="button"]{
   	padding: 10px 21px;
   	margin-left:20px;
   	background-color: #E4E9F0;
   }
  </style>
  <body onbeforeunload="return '请先确认是否保存再关闭窗口！'">
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
		<div class="row">
			<div class="fullScreenBg" style="display:none;">	
			</div>
			<div class="message" style="display:none;">
				<span class="text"></span>
				<div class="submit">确定</div>
			</div>
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>${title}</span></h2>
			<div class="table-responsive">	
				<form:form id="documentForm"  commandName="document" name="documentForm" action="/document/create.shtml" enctype="multipart/form-data" onsubmit="return false">
				<input type="hidden" value="${document.udid}" name="udid">
				<input type="hidden" name="workflowInstanceId" value="${workflowInstanceId}"/>
				 <table class="table table-striped">
		            <thead style="display:none;">
		                <tr>
		                  <th style="width:20%;text-align:right;"></th>
		                  <th style="width:80%;text-align:left;"></th>		                  
		                </tr>
		            </thead>
					<tbody>
					
					<tr>
						<td>*文档类型</td>
						<td>
							<input type="hidden" id="documentTypeId" name="documentTypeId" value="${documentType.documentTypeId}"/>
							<spring:message code="DocumentTypeId.${document.documentTypeId}" />				
						</td>							
					</tr>
					<tr>
						<td>*链接优先展现形式</td>
						<td>
							<select id="displayTypeId" name="displayTypeId" required="true">
								<c:forEach var="displayType" items="${displayTypeList }">
									<option value="${displayType.displayTypeId}">${displayType.displayTypeName}</option>
								</c:forEach>
							</select>						
						</td>							
					</tr>
					
					<%@include file="/WEB-INF/jsp/common/document/documentCreateUpdate.jsp"%>
					
					<tr class="content">
						<td>*内容</td>
						<td>
							<textarea name="content" id="myEditor"></textarea>
							<script type="text/javascript">
							 UE.getEditor('myEditor');
								//1.2.4以后可以使用一下代码实例化编辑器
								//UE.getEditor('myEditor')
							</script>											
						</td>							
					</tr>

					<tr>
						<td style="width:20%;text-align:right;">前台页面显示顺序</td>
						<td style="width:80%;text-align:left;">
							<form:input size="50"  path="displayIndex"/> 
							<form:errors path="displayIndex" cssClass="errorMessage"/>
						</td>							
					</tr>

					<tr>
						<td>*发布栏目</td>
						<td>
							<ul id="browser" class="filetree" style="background:#F9F9F9">								
							<c:forEach var="node" items="${nodeTree}" >
								<li>
									<c:set var="parentNode" value="${node}" scope="request"/>
									<c:import url="/WEB-INF/jsp/common/include/documentTreeList.jsp" />
								</li>
							</c:forEach>								
							</ul>
						</td>							
					</tr>

					<tr>
						<td>同步显示位置</td>
						<td>
							<ul id="browserSync" class="filetree" style="background:#F9F9F9">								
							<c:forEach var="nodeSync" items="${nodeTree}" >
								<li>
									<c:set var="parentNodeSync" value="${nodeSync}" scope="request"/>
									<c:import url="/WEB-INF/jsp/common/include/documentTreeListSync.jsp" />
								</li>
							</c:forEach>								
							</ul>					
						</td>							
					</tr>

					<!-- <tr id="lastTr">
						<td>

						</td>
					</tr> -->
					</tbody>
				</table>
				<div class="submit"><input type="submit" value="保存文档" class="addPro"><input type="button" value="预览文章" onClick="updateDoc()"></div>
				
				</form:form>
			</div>
        </div>
      </div>
    </div>
	<div id="colModal" class="ModalIframe colModal black" hidden="hidden">	
		<div class="modalDrag colModalDrag">
			<div class="modalHeader">
				<a href="javascript:void(0);" class="close" onclick="off()">×</a>
				<span>产品列表</span>
			</div>
			<div id="modalBody" class="modalBody" style="height: 452px;">
				
			</div>
			<div class="modalFooter" id="modalFooter">
				<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
				<a href="javascript:void(0);" class="btn submit" onclick="butt()">确定</a>
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
	<script src="/theme/${theme}/js/lib/jquery.cookie.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.treeview.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.form.3.5.1.js" type="text/javascript"></script>
	<!--添加图片-->
	<script src="/theme/${theme}/js/addComment.js" type="text/javascript"></script>
	<script>
	//一次只能添加一张产品小图
	
	</script>
	<script>
	//添加文档
	var url="/document/create.json";
	$(function(){
		$("#documentForm").submit(function(e){
			e.preventDefault();
			if ($('#tags').val().indexOf('，')>-1) {
				alert('标签内容请输入英文逗号！');
				$('#tags').focus();
			}else{
			$(this).ajaxSubmit({
				type:"POST",
				url:url,
				dataType:"json",
				data:$("#documentForm").serialize(),				
				success:function(data) {						
					$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
				},
				error:function(XMLResponse){
					alert("操作失败:" + XMLResponse.responseText);
				},
				
			});};
		})
		$(".message .submit").click(function(){
			$(".fullScreenBg").hide();$(".message").hide();
		})
	})
	</script>
	<script>
		function writeDoc(OpenWindow,a,b,c){
		OpenWindow.document.write("<TITLE>"+a+"</TITLE>");
		OpenWindow.document.write("<BODY BGCOLOR=#ffffff align=center>");
		OpenWindow.document.write("<h1 align='center'>"+b+"</h1>");
		OpenWindow.document.write(""+c+"");
		OpenWindow.document.write("</BODY>");
		OpenWindow.document.write("</HTML>");
		OpenWindow.document.close();  
	}
	function updateDoc(){		
			if(!$("#documentForm")[0].checkValidity()||UE.getEditor('myEditor').getContent()==""||$("input[name=defaultNodeId]:checked").val()==undefined){
				alert("请输入*处必填信息！");
				return false;
			}  		
			var w=window.open("",'NewWindow','scrollbars=yes' ,'height=700','width=300','left=10','top=10','toolbar=yes','menubar=yes','resizable=yes','status=yes','z-look=yes','titlebar=yes','bReplace=true');
			$("#documentForm").ajaxSubmit(
				{
					type:"POST",
					url:url,
					dataType:"json",
					data:$("#documentForm").serialize(),
					async:false,
					success:function(data) {	
						if(data.message&&data.message.operateCode!=102008){
							//$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]")
							writeDoc(w,"提示信息",data.message.message,data.message.operateCode); 
							return;
						};
						if(data.document&&data.document.udid){
							w.location="/document/preview/"+data.document.udid+".shtml";  							
							/*w.document.write('<span class="src">'+src+'</span>'+'<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"><\/script>'+'<script type="text/javascript" src="/theme/${theme}/js/windowOpenOnload.js"> <\/script>'); */
							if($("form input[name='udid']").length==0){
								$("form").append('<input type="hidden" value="'+data.document.udid+'" name="udid">');
							}							
							url="/document/update.json";
							needPreview=true;
							src=data.document.udid;
						}
					},
					error:function(XMLResponse){
						console.log("error");
						w.location="/";
						var error=XMLResponse.responseText;
						setTimeout(writeError(w,error),3000);
												
					},
				}			
			);
	}
	function writeError(w,error){
		w.document.open();
		w.document.write('<span class="error">'+error+'</span>'+'<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"><\/script>'+'<script type="text/javascript" src="/theme/${theme}/js/windowOpenOnload.js"> <\/script>');
		w.document.close();
	}
	</script>
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
	//栏目
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
	
	<script>
	//产品列表样式
		function setStyle(){
			$("#colModal").show();
		}
		function butt(){
			$("#colModal").hide();
			var productCode = $("#iframeBg")[0].contentWindow.$('input[name="activityProduct"]:checked').val();
			$("#productCode").val(productCode);			
		}
		function off(){
			$("#colModal").hide();
		}
	</script>
	<script>
	//产品编码
		$(function(){
			var popup="";
			var activity="";
			if($("#documentTypeId").val() == 171005){
				$.ajax({
					type:"GET",
					url:"/document/list/product.json?documentTypeId="+171005,
					dataType:"json",
					async:false,
					success: function(message){
						popup+="<iframe src='/product.shtml?mini=true&currentStatus=100001&currentStatus=130004' frameborder=0 id='iframeBg'>";
					}
				})
				activity+=popup;
				var ac = document.getElementById('modalBody');
				ac.innerHTML = activity;
			} else if($("#documentTypeId").val() == 171004){
				$.ajax({
					type:"GET",
					url:"/document/list/product.json?documentTypeId="+171004,
					dataType:"json",
					async:false,
					success: function(message){
						popup+="<iframe src='/product.shtml?mini=true&currentStatus=100001&currentStatus=130004' frameborder=0 id='iframeBg'>";
					}
				})
				activity+=popup;
				var ac = document.getElementById('modalBody');
				ac.innerHTML = activity;
			}
		})
	</script>
  </body>
</html>
