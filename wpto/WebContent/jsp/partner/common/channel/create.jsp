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
		.treeview, .treeview ul { 
			padding: 0;
			margin: 0;
			list-style: none;
			background(url('/theme/${theme}/images/lineDottedVert.png'))
		}

		.treeview ul {
			margin-top: 4px;
		}
		.treeview li { 
			margin: 0;
			padding: 3px 0pt 3px 16px;
		}
		.treeview li { background: url('/theme/${theme}/images/treeview/treeview-default-line.png') 0 0 no-repeat; }
		.treeview li.collapsable, .treeview li.expandable { background-position: 0 -176px; }

		.treeview .expandable-hitarea { background-position: -80px -3px; }

		.treeview li.last { background-position: 0 -1766px }
		.treeview li.lastCollapsable, .treeview li.lastExpandable { background-image: url('/theme/${theme}/images/treeview/treeview-default.png'); }  
		.treeview li.lastCollapsable { background-position: 0 -111px }
		.treeview li.lastExpandable { background-position: -32px -67px }

		.treeview div.lastCollapsable-hitarea, .treeview div.lastExpandable-hitarea { background-position: 0; }
		.filetree li { padding: 3px 0 2px 16px; }
		.filetree span.folder, .filetree span.file { padding: 1px 0 1px 16px; display: block; }
		.treeview .hitarea {
			background: url('/theme/${theme}/images/treeview/treeview-default.png') -64px -25px no-repeat;
			height: 16px;
			width: 16px;
			margin-left: -16px;
			float: left;
			cursor: pointer;
		}
		html .hitarea {
			display: inline;
			float:none;
		}

		.treeview li { 
			margin: 0;
			padding: 3px 0pt 3px 16px;
		}
		#treecontrol { margin: 1em 0; display: none; }

		.treeview .hover { cursor: pointer; }

		.treeview li { background: url('/theme/${theme}/images/treeview/treeview-default-line.png') 0 0 no-repeat; }
		.treeview li.collapsable, .treeview li.expandable { background-position: 0 -176px; }

		.treeview .expandable-hitarea { background-position: -80px -3px; }

		.treeview li.last { background-position: 0 -1766px }
		.treeview li.lastCollapsable, .treeview li.lastExpandable { background-image: url('/theme/${theme}/images/treeview/treeview-default.png'); }  
		.treeview li.lastCollapsable { background-position: 0 -111px }
		.treeview li.lastExpandable { background-position: -32px -67px }

		.treeview div.lastCollapsable-hitarea, .treeview div.lastExpandable-hitarea { background-position: 0; }

		.treeview-red li { background-image: url('/theme/${theme}/images/treeview/treeview-red-line.gif'); }
		.treeview-red .hitarea, .treeview-red li.lastCollapsable, .treeview-red li.lastExpandable { background-image: url('/theme/${theme}/images/treeview/treeview-red.gif'); } 

		.treeview-black li { background-image: url('/theme/${theme}/images/treeview/treeview-black-line.gif'); }
		.treeview-black .hitarea, .treeview-black li.lastCollapsable, .treeview-black li.lastExpandable { background-image: url('/theme/${theme}/images/treeview/treeview-black.gif'); }  

		.treeview-gray li { background-image: url('/theme/${theme}/images/treeview/treeview-gray-line.gif'); }
		.treeview-gray .hitarea, .treeview-gray li.lastCollapsable, .treeview-gray li.lastExpandable { background-image: url('/theme/${theme}/images/treeview/treeview-gray.gif'); } 

		.treeview-famfamfam li { background-image: url('/theme/${theme}/images/treeview/treeview-famfamfam-line.gif'); }
		.treeview-famfamfam .hitarea, .treeview-famfamfam li.lastCollapsable, .treeview-famfamfam li.lastExpandable { background-image: url('/theme/${theme}/images/treeview/treeview-famfamfam.gif'); } 

		.treeview .placeholder {
			background: url('/theme/${theme}/images/treeview/ajax-loader.gif') 0 0 no-repeat;
			height: 16px;
			width: 16px;
			display: block;
		}

		.filetree li { padding: 3px 0 2px 16px; }
		.filetree span.folder, .filetree span.file { padding: 1px 0 1px 16px; display: block; }
		/* .filetree span.folder { background: url('/theme/${theme}/images/treeview/folder.gif') 0 no-repeat; }
		.filetree li.expandable span.folder { background: url('/theme/${theme}/images/treeview/folder-closed.gif') 0 no-repeat; } */
		/* .filetree span.file { background: url('/theme/${theme}/images/treeview/file.gif') 0 no-repeat; } */ 
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
		input,select{
		    border-radius: 5px;
		    border: 1px solid #ddd;
		    padding: 3px;
		    min-width: 200px;
		}
	</style>
  </head>
  <body>
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
			<h2 class="sub-header">${title}</h2>
			<div class="table-responsive">	
				<form:form id="partnerForm"  commandName="partner" name="partnerForm" enctype="multipart/form-data" action="/channel/create.shtml" method="post">
				 <table class="table table-striped">
		            <thead style="display:none;">
		                <tr>
		                  <th style="width:20%;text-align:right;"></th>
		                  <th style="width:80%;text-align:left;"></th>		                  
		                </tr>
		            </thead>
					<tbody>
						
						<!--tr>
							<td>用户类型</td>
							<td style="width:80%;text-align:left;">
								<select id="userTypeId" name="userTypeId" onchange="selectEvent(this.options[this.options.selectedIndex].value)">
									<option value=""  >请选择</option>
									<c:forEach var="userType" items="${userTypes}" >
										<option value="${userType.value}"  >${userType.key}</option>
									</c:forEach>
								</select>
							</td>							
						</tr-->
						
						<tr>
							<td style="width:20%;text-align:right;">登录名[只允许英文字母、数字和下划线]</td>
							<td style="width:80%;text-align:left;">
								<form:input path="username"/>
								<form:errors path="username" cssClass="errorMessage"/>
							</td>							
						</tr>
						<tr>
							<td style="width:20%;text-align:right;">用户昵称[支持中文]</td>
							<td style="width:80%;text-align:left;">
								<form:input path="nickName"/>
								<form:errors path="nickName" cssClass="errorMessage"/>
							</td>							
						</tr>
						<tr>
							<td style="width:20%;text-align:right;">用户密码</td>
							<td style="width:80%;text-align:left;">
								<form:password path="userPassword"/>
								<form:errors path="userPassword" cssClass="errorMessage"/>
							</td>							
						</tr>	
						<tr>
							<td style="width:20%;text-align:right;">再输入一遍密码</td>
							<td style="width:80%;text-align:left;">
								<input type="password"  name="userPassword2"/>
							</td>							
						</tr>	
						<tr style="display:none">
							<td>关联角色</td>
							<td>
								<c:forEach var="r" items="${partnerRoleList}">
									<c:if test="${r.roleName == '合作方管理员'}">
										<input type="radio" name="roleId" value="${r.roleId }" checked="checked" >&nbsp;${r.roleId}：${r.roleName}</div>&nbsp;&nbsp;&nbsp;&nbsp;
									</c:if>
								</c:forEach>						
							</td>							
						</tr>
						
						<tr>
							<td style="width:20%;text-align:right;">用户状态</td>
							<td style="width:80%;text-align:left;">
								<form:select path="currentStatus">					 
									<c:forEach items="${statusCodeList}" var="i">
										<option value="${i.id}" <c:if test="${i.id == 120001}"> selected </c:if> ><spring:message code="Status.${i.id}" /></option>
									</c:forEach>
								</form:select>
							</td>							
						</tr>
						
						<c:forEach items="${dataDefine}" var="d">
							<tr class="dataDefine">
								<td style="width:20%;text-align:right;">${d.dataDescription}</td>
								<td>
									<c:choose>
										<c:when test="${d.inputMethod == 'string'}">
											<input class="${d.dataCode}" type="text" name="${d.dataCode}" value="">
										</c:when>
										<c:when test="${d.inputMethod == 'input'}">
											<c:if test="${d.dataCode == 'userInviteCode'}">
												<input id="userInviteCode" type="text" name="${d.dataCode}" onblur="upperCase()" value="">&nbsp;&nbsp;
												<input id="butt" style="display:none" type="button" value="推广二维码">
											</c:if>
											<c:if test="${d.dataCode != 'userInviteCode'}">
												<input class="${d.dataCode}" type="text" name="${d.dataCode}" value="">
											</c:if>
										</c:when>
										
										<c:when test="${d.inputMethod == 'select'}">
											<input type="radio" name="${d.dataCode}" value="true">是
											<input type="radio" name="${d.dataCode}" value="false">否
										</c:when>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
						
					</tbody>
				</table>
				<div class="submit"><input type="submit" value="新增用户" class="addPro querenbtn"></div>
				</form:form>
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
	$(function(){
		$("#documentForm").submit(function(e){
				e.preventDefault();
				$(this).ajaxSubmit(
				 {
					type:"POST",
					url:"/document/create.json",
					dataType:"json",
					data:$("#documentForm").serialize(),				
					success:function(data) {						
						$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
					},
					error:function(XMLResponse){
						alert("操作失败:" + XMLResponse.responseText);
					},
					
				}			
				);
			})
			$(".message .submit").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
			})
	})
	</script>
	<script>
		function upperCase(){
			var x=document.getElementById("userInviteCode").value;
			if(x.length < 1 || x == null){
				$("#butt").hide();
			}else{
				$("#butt").show();
			}
			//document.getElementById("userInviteCode").value=x.toUpperCase()
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
		/*function selectEvent(val){
			if(val == 121002){
				$(".dataDefine").show();
			} else {
				$(".dataDefine").hide();
			}
		}*/
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
				reg=/^[0-9]*$/;
				var productName=$("#productName").val();
				var productCode=$("#productCode").val();
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
				}else if(!reg.test(productCode)){
					alert("产品代码必须为数字！")
				}else if(labelMoney==""){
					alert("请填写市场价格！");
				}else if(!reg.test(labelMoney)){
					alert("市场价格必须为数字！")
				}else if(standardMoney){
					alert("请填写至少一个标准价格！");
				}
				else if(!reg.test(standardMoneyNum)){
					alert("标准价格必须为数字！")
				}else if(UE.getEditor('myEditor').getContent()==""){
					alert("请输入内容！")
				}else if($("input[name=defaultNodeId]:checked").val()==undefined){
					alert("请选择发布位置！")
				}
				else{
					$(this).ajaxSubmit(
					 {
						type:"POST",
						url:"/product/create.json",
						dataType:"json",
						data:$("#productTypeForm").serialize(),				
						success:function(data) {						
							$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
						},
						error:function(XMLResponse){
							$(".fullScreenBg").show();$(".message").show();$(".message .text").html(XMLResponse.responseText);
							//alert("操作失败:" + XMLResponse.responseText);
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
  </body>
</html>
