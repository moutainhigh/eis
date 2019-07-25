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

    <title>${systemName}-添加商户</title>

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
			position: fixed;
		    z-index: 300;
		    background: #fff;
		    height: 195px;
		    width: 300px;
		    left: 50%;
		    top: 287px;
		    margin-left: -150px;
		    text-align: center;
		    border-radius: 5px;
		    overflow: hidden;
		    padding-top: 51px;
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
			font-size:21px;		
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
	</style>
  </head>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
		<div class="row">
			<div class="fullScreenBg" style="display:none;opacity:1; background: rgba(0, 0, 0, 0.63);">	
			
			<div class="message" style="display:none;">
				<span class="text"></span>
				<p style="text-align:center;margin-top: 57px;"><a href="<c:url value='/merchant.shtml'/>"  class="completebtn" style=" font-size: 15px;padding: 10px 14px; margin-right: 12px;">返回商户列表</a><span class="querenbtn" style=" cursor: pointer;font-size: 15px;padding: 10px 14px;">继续添加商户</span></p>
			</div>
			</div>
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>添加商户</span></h2>
			<div class="table-responsive">	
				<form:form id="partnerForm"  commandName="partner" name="partnerForm" enctype="multipart/form-data" action="" method="post">
				 <table class="table table-striped" style="">
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
							
							<td style="text-align:right; width:25%;"><span>登录名</span>
								</td>
							<td style="text-align:left;">
								<form:input path="username"/><span style="color:red;font-size:12px;padding:0;margin-left:6px;">*只允许英文字母、数字和下划线</span>
								<form:errors path="username" cssClass="errorMessage"/>
							</td>							
						</tr>
						<tr>
							<td style="">用户昵称</td>
							<td style="text-align:left;">
								<form:input path="nickName"/><span style="color:red;font-size:12px;padding:0;margin-left:6px;">*支持中文</span>
								<form:errors path="nickName" cssClass="errorMessage"/>
							</td>							
						</tr>
						<tr>
							<td style="">用户密码</td>
							<td style="text-align:left;">
								<form:input path="userPassword"/>
								<input type="text"  name="userPassword2" id="userPassword2" placeholder="确认密码"style="margin-left:20px;"/>
								<form:errors path="userPassword" cssClass="errorMessage"/>
								<span class="makePassword" name="common" style="cursor:pointer;text-align:center;height: 24px;line-height: 25px;display: inline-block;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
									生成密码
								</span>
							</td>							
						</tr>	
						<tr>
							<td>二级密码</td>
							<td style="text-align:left;">
								<form:input path="authKey"/>
								<input type="text"  name="authKey2" id="authKey2" placeholder="确认密码"style="margin-left:20px;"/>
								<span class="makePassword" name="common" style="cursor:pointer;text-align:center;height: 24px;line-height: 25px;display: inline-block;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
									生成密码
								</span>
							</td>							
						</tr>	
							
						<tr>
							<td>关联角色</td>
							<td style="text-align:left;">
								<c:forEach var="r" items="${partnerRoleList}">
								<span class="jiaose"><input type="checkbox" name="roleId" value="${r.roleId }">&nbsp;${r.roleId}：<span style="padding-left:24px;display:inline-block;background-size: contain;background-repeat: no-repeat;">${r.roleName}</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
								</c:forEach>						
							</td>							
						</tr>
						
						<tr  style="border-bottom:2px solid #BDBDBD">
							<td style="">用户状态</td>
							<td style="text-align:left;">
								<form:select path="currentStatus">					 
									<c:forEach items="${statusCodeList}" var="i">
										<option value="${i.id}" <c:if test="${i.id == 120001}"> selected </c:if> ><spring:message code="Status.${i.id}" /></option>
									</c:forEach>
								</form:select>
								<span style="font-size:13px;color:red;margin-left:6px;">*如果不确定请选择 “ 正常 ”</span>
							</td>							
						</tr>
						
						<c:forEach items="${dataDefine}" var="d">
							<tr class="dataDefine">
								<td>${d.dataDescription}</td>
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
											
										</c:when>
										
										<c:when test="${d.inputMethod == 'select'}">
											<input type="radio" name="${d.dataCode}" value="true">是
											<input type="radio" name="${d.dataCode}" value="false">否
										</c:when>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
							<!-- 新增 -->
						<c:forEach var="a" items="${configMap}" varStatus="varStatus">
						<tr>
						
						<td>
							<span type="text" name="data.${a.dataCode}" id="data.${a.dataCode}"  style="border:none;min-width:174px;background-color:transparent;"  title="${a.dataDescription}">
								${a.dataName}[${a.dataCode}]
							</span>

						</td>
						<c:choose>
							
							<c:when test="${fn:contains(a,'dataValue')}">
								<td class="kzinputtd" style='text-align:left;'>
									<input name="data.${a.dataCode}" id="data.${a.dataCode}" value="${a.dataValue}" type="text" style="min-width:50%;" data-readonly="${a.readonly}" readonly data-required="${a.required}"/>
									
								</td>
								
							</c:when>
							
							<c:when test="${a.dataType=='boolean'}">
								<td class="tdradio" style='text-align:left;' data-readonly="${a.readonly}" data-required="${a.required}">
									<input type="radio" name="data.${a.dataCode}" id="data.${a.dataCode}" value="true" /><span style="margin-right:6px;">是</span>
									<input type="radio" name="data.${a.dataCode}" id="data.${a.dataCode}" value="false"/><span>否</span>
								</td>
							</c:when>
							<c:otherwise>
								<td class="kzinputtd" style='text-align:left;' >
									<input name="data.${a.dataCode}" id="data.${a.dataCode}"  type="text"  value=""  style="min-width:50%;" data-readonly="${a.readonly}" data-required="${a.required}"/>
									<c:if test="${a.dataCode == 'supplierLoginKey'}">
										<span class="makePassword" name="MD5" style="cursor:pointer;text-align:center;height: 24px;line-height: 25px;display: inline-block;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
											生成密码
										</span>
									</c:if>
									<c:if test="${a.dataCode == 'supplierChargeKey'}">
										<span class="makePassword" name="3DES" style="cursor:pointer;text-align:center;height: 24px;line-height: 25px;display: inline-block;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
											生成密码
										</span>
									</c:if>									
								</td>
								
							</c:otherwise>
						</c:choose>
						</tr> 
						</c:forEach>
						<!-- 结束 -->
					</tbody>
				</table>

				<div class="submit"><input type="submit" value="新增用户" class="submitinput"></div>
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
	<script src="/theme/${theme}/all/js/GenerationKey.js" type="text/javascript"></script>

	<script>



	$(function(){
		
		var length=$("tbody tr").length;
		for(var i=0;i<length;i++){
			$("tbody tr").eq(i).find("td").eq(0).css({"text-align":"right",'background-color': '#E4E9F0'});
			$("tbody tr").eq(i).find("td").eq(0).find('input').css({"text-align":"right",'width':'100%','min-width':'400px'});
			// $("tbody tr").eq(i).find("td").eq(1).css({"text-align":"left","paddingLeft":"5%"});
		};
		$('td span.jiaose').each(function(){
			var imgsrc = '/theme/basic/images/people.png';
			$(this).find('span').css('background-image','url('+imgsrc+')');
			console.log(imgsrc);
		});
		$('.kzinputtd').each(function(){
				console.log($(this).find("input[type='text']").attr('data-required'));
				if($(this).find("input[type='text']").attr('data-readonly') == 'false'){
					$(this).find("input[type='text']").removeAttr('readonly');
					
				}else{
					$(this).find("input[type='text']").attr('readonly','readonly');
					
				}
				if($(this).find("input[type='text']").attr('data-required') == 'true'){
					$(this).find("input[type='text']").addClass('required');
					
				}else{
					$(this).find("input[type='text']").removeClass('required');
					
				}
			})
		$('.tdradio').each(function(){
			if($(this).attr('data-readonly')=='false'){
				$(this).addClass('required');
			}
		});
		
		$(".makePassword").click(function(){
			var type = $(this).attr("name");
			var str = "";
			if(type == "common"){
				str = makeCommonKey(8);
			}else if(type == "MD5"){
				str = makeMD5Key(8);
			}else if(type == "3DES"){
				str = make3DesKey();
			}
			$(this).parent().find("input").val(str);
		});
		

	});
	
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
					//alert("同步时不能选择跟发布位置一样的节点！")
					$(this).removeAttr("checked");
				}
			})
		});


		$(function(){

		$(".submitinput").on('click',function(e){
				e.preventDefault();
				var reg=/^[\w]*$/;
				var reg2 =/^[a-zA-Z\u4e00-\u9fa5]{1}[a-zA-Z0-9\u4e00-\u9fa5]{3,9}$/;
				var username=$("#username").val();
				var nickName=$("#nickName").val();
				var userPassword=$("#userPassword").val();
				var userPassword2=$("#userPassword2").val();
				var authKey = $("#authKey").val();
				var authKey2 = $("#authKey2").val();
				console.log(userPassword);console.log(userPassword2);

				$('.jiaose input').each(function(){
						if($(this).is(":checked")){
							$(this).addClass('checkedbox');
						}
						
					});
				var checkedboxL = $('input.checkedbox').length;
				
				$('.tdradio').each(function(){
					if($(this).find('input').is(':checked')){
						$(this).find('input').addClass('radioinput');
					}else{
						$(this).find('input').removeClass('radioinput');
					}
					var radioinputL = $(this).find('input.radioinput').length;
					if(radioinputL>0){
					$(this).removeClass('required');
					}
					
				})
				 
				$('.required').each(function(){
					if ($(this).val()!=='') {
						$(this).removeClass('required');
					};
				})
				
				if(username==""){
					alert('请输入登录名！')
					$('#username').focus();
				}else if(!reg.exec(username)){
					alert("请输入正确格式登录名");
					$('#username').focus();
				}else if(nickName==""){
					alert("请输入用户昵称");
					$('#nickName').focus();
				}else if(userPassword==""){
					alert("请输入用户密码");
					$('#userPassword').focus();
				}else if(userPassword2==''){
					alert("请输入用户确认密码");
					$('#userPassword2').focus();
				}else if(userPassword2!==userPassword){
					alert("用户确认密码不正确");
					$('#userPassword2').focus();
				}else if(authKey==''){
					alert("请输入二级密码");
					$('#authKey').focus();
				}else if(authKey2==''){
					alert("请输入二级确认密码");
					$('#authKey2').focus();
				}else if(authKey2!==authKey){
					alert("二级确认密码不正确");
					$('#authKey2').focus();
				}else if(checkedboxL<1){
					alert("请选择关联角色");
					$("input[name='roleId']").eq(0).focus();
				}else if($('.required').length>0){
					console.log($('.required').length);
					var tcz = $('.required').eq(0).parents('tr').find('span').text();
					alert(tcz+'未填写');
					$('.required').eq(0).parents('tr').find('input').focus();
				}

				// }else if(productName==""){
				// 	alert("请填写产品名称！");
				// }else if(productCode==""){
				// 	alert("请填写产品代码！");
				// }else if(!reg.test(productCode)){
				// 	alert("产品代码必须为数字！")
				// }else if(labelMoney==""){
				// 	alert("请填写市场价格！");
				// }else if(!reg.test(labelMoney)){
				// 	alert("市场价格必须为数字！")
				// }else if(standardMoney){
				// 	alert("请填写至少一个标准价格！");
				// }
				// else if(!reg.test(standardMoneyNum)){
				// 	alert("标准价格必须为数字！")
				// }else if(UE.getEditor('myEditor').getContent()==""){
				// 	alert("请输入内容！")
				// }else if($("input[name=defaultNodeId]:checked").val()==undefined){
				// 	alert("请选择发布位置！")
				// }
				else{

					$.ajax(
					 {  
						type:"POST",
						url:"/merchant/create.json",
						dataType:"json",
						data:$('#partnerForm').serialize(),
						success:function(data) {						
							$(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
							//swal(data.message.message+"["+data.message.operateCode+"]");
						},
						error:function(XMLResponse){
							$(".fullScreenBg").show();$(".message").show();$(".message .text").html("操作失败:" +XMLResponse.responseText);
							//swal("操作失败:" + XMLResponse.responseText);
							//swal('失败');
							//console.log(333);
						},
					}			
					);
				}
			});
			$(".message .querenbtn").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
			});
		
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
