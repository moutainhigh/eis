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
								<input type="text" name="username" id="username" placeholder="请输入登录名" maxlength="45">
							</td>							
						</tr>
						<tr>
							<td style="width:20%;text-align:right;">用户昵称[支持中文]</td>
							<td style="width:80%;text-align:left;">
								<input type="text" name="nickName" id="nickName" placeholder="请输入用户昵称" maxlength="45">
							</td>							
						</tr>
						<tr>
							<td style="width:20%;text-align:right;">用户密码</td>
							<td style="width:80%;text-align:left;">
								<input type="password" name="userPassword" id="userPassword" placeholder="请输入用户密码" maxlength="255">
							</td>							
						</tr>	
						<tr>
							<td style="width:20%;text-align:right;">再输入一遍密码</td>
							<td style="width:80%;text-align:left;">
								<input type="password"  name="userPassword2" id="userPassword2" placeholder="请再输入一遍密码用户密码" maxlength="255"/>
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
						<tr>
							<td style="width:20%;text-align:right;">帐号关联</td>
							<td style="width:80%;text-align:left;">
								
							</td>							
						</tr>
						<c:forEach items="${gameProductList}" var="gameProduct">
							<tr class="gameType">
								<td style="width:20%;text-align:right;" data-id="${gameProduct.gameId}" data-gamecode="${gameProduct.gameCode}"  id="gameId" >${gameProduct.gameName }</td>
								<td style="width:80%;text-align:left;">
									<input type="text" name="gameRoleName" class="gameRoleName" data-id="${gameProduct.gameId}" data-gamecode="${gameProduct.gameCode}" id="gameRoleName" placeholder="请输入您的游戏角色名称">
								</td>
							</tr>
						</c:forEach>
						
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
					<div class="submit"><input type="button" value="新增用户" class="addPro" id="addPro"></div>
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
	
	<script src="/theme/${theme}/js/validateUtils.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/layer/layer.js" type="text/javascript"></script>
	<script>
	$(function(){
		//校验是否存在登录名称
		$("#username").keyup(function(){
			var $this = $(this);
			var val = $.trim($this.val());
			if(!ValidateUtils.isNull(val)){
				$.ajax({
					type:"POST",
					dataType:"json",
					url:"/channel/hasLoginName.json",
					data:{
						username:val
					},
					success:function(data){
						if(data.message.operateCode != 102008){
							tips(data.message.message,$this,false);
						}
					}
				});
			}
		});
		
		//查询游戏角色帐号是否存在
		$(".gameRoleName").keyup(function(){
			var $this = $(this);
			//游戏帐号角色名称
			var gameAccount = $.trim($this.val());
			//游戏类型
			var gameCode = $.trim($this.data("gamecode"));
			//游戏id
			var gameId = $.trim($this.data("id"));
			if(!ValidateUtils.isNull(gameAccount) && !ValidateUtils.isNull(gameCode)){
				$.ajax({
					type:"POST",
					dataType:"json",
					url:"/channel/postProcess.json",
					data:{
						gameAccount:gameAccount,
						gameCode:gameCode,
						gameId:gameId
					},
					success:function(data){
						if(data.message.operateCode != 102008){
							tips(data.message.message,$this,false);
						}
					}
				});
			}
		});
		
		$("#addPro").click(function(){
			//登录名
			var $username = $("#username");
			var username = $.trim($username.val());
			//用户名称
			var $nickName = $("#nickName");
			var nickName = $.trim($nickName.val());
			//用户密码
			var $userPassword = $("#userPassword");
			var userPassword = $.trim($userPassword.val());
			//确认密码
			var $userPassword2 = $("#userPassword2");
			var userPassword2 = $.trim($userPassword2.val());
			//用户状态
			var $currentStatus = $("#currentStatus");
			var currentStatus = $.trim($currentStatus.find("option:selected").val());
			//关系
			var $gameType = $(".gameType");
			var gameTypeData = new Array();
			$gameType.each(function(){
				var $this = $(this);
				//游戏id
				var gameId =  $.trim($this.find("#gameId").data("id"));
				//游戏代码
				var gameCode = $.trim($this.find("#gameId").data("gamecode"));
				//角色名称
				var gameRoleName = $.trim($this.find("#gameRoleName").val());
				gameTypeData.push({
					gameId:gameId,
					gameCode:gameCode,
					gameRoleName:gameRoleName
				});
			});
			if(ValidateUtils.isNull(username)){
				tips($username.attr("placeholder"),$username,true);
				return ;
			}
			if(ValidateUtils.isNull(nickName)){
				tips($nickName.attr("placeholder"),$nickName,true);
				return ;
			}
			if(ValidateUtils.isNull(userPassword)){
				tips($userPassword.attr("placeholder"),$userPassword,true);
				return ;
			}
			if(ValidateUtils.isNull(userPassword2)){
				tips($userPassword2.attr("placeholder"),$userPassword2,true);
				return ;
			}
			if(userPassword2 != userPassword){
				tips("两次密码不一致!",$userPassword2,true);
				return ;
			}
			if(ValidateUtils.isNull(currentStatus)){
				tips($currentStatus.attr("placeholder"),$currentStatus,true);
				return ;
			}
			var datas = {
				username:username,
				nickName:nickName,
				userPassword:userPassword,
				currentStatus:currentStatus,
				gameTypeData:gameTypeData
			}
			$.ajax({
				type:"POST",
				contentType: "application/json; charset=utf-8",
				dataType:"json",
				data:JSON.stringify(datas),
				url:"/channel/createPartner.json",
				success:function(data){
					if(data.message.operateCode == 102008){
						layer.msg(data.message.message,{time:1000},function(){
							self.location = document.referrer;
						});
					}else{
						layer.msg(data.message.message,{time:1000},function(){
							location.reload();
						});
						layer.msg(data.message.message);
					}
				}
			})
		});
	})
	//消息提示
	function tips(msg,obj,isFocus){
		layer.tips(msg, obj, {tips:[2,'#f00']});
		if(isFocus){
			obj.focus();
		}
	}
	</script>
  </body>
</html>
