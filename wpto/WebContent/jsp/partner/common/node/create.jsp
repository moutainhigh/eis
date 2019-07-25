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

    <title>${systemName}-新建栏目</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
	
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
  </head>
  <style>
		.blue{color:#428bca;}
		.fa{font-size:16px;}
		#edui1{z-index:9!important;}
		#browser{line-height:12px !important;}
		#browser span{display:inline-block;line-height:12px;padding-left:5px;padding-right:5px;}
		#browser span #xiangqing{padding-left:0px;padding-right:0px;margin-right:-5px;}
		#browser span #xiangqing span{/*padding-left:0px;padding-right:0px;margin-left:0px;margin-right:-5px;*/text-align:center;}
		#browserSync span{display:inline-block;line-height:12px;padding-left:5px;padding-right:3px;}
		#browserSync{line-height:12px !important;background:#fff !important;}
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
		
		.contentTypeRows{
			float: right;
			
			margin-right:15%;
		}
		input[type="text"]{
			max-width: 300px;
		    width: 100%;
		    border-radius: 5px;
		    border: 1px solid #ddd;
		    padding: 3px 5px;
		}
        button, input, select, textarea {
		    border-radius: 5px;
		    border: 1px solid #ddd;
		    padding: 3px;
}
@media(min-width: 1200px){
	#browser{
				margin-left: 40px;
			}
}
		@media(max-width: 1200px){
			table,tbody,tr,td{
				display: block;
				width:100%;
			}
			tr,td{
				border-top: 1px solid #ddd;
			}
			#browser{
				margin-left: 0!important;
			}
		}
	</style>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>${title}</span></h2>
			<div class="table-responsive">	
	<form:form commandName="node" name="nodeForm" action="/node/create.shtml">
		
		<table class="table table-striped">
		
		<colgroup width="800">
			<col width="100"/>
			<col width="*"/>
		</colgroup>		
		<tr class="header tabheader">
			<td align="left" colspan="2">${title}</td>
		</tr>
		<tr>
			<th>上级栏目</th>
			<td>
				<form:hidden path="parentNodeId" value="${node.parentNodeId}"/>
				<c:if test="${empty node.parentNodeName}">
					系统
				</c:if>
				${node.parentNodeName}
			</td>
		</tr>
		<tr>
			<th>*名称[中文]</th>
			<td>
				<form:input path="name"/>
				<form:errors path="name" cssClass="errorMessage"/>
			</td>
		</tr>
		<tr>
			<th>*路径代码[只支持英文、数字和下划线]</th>
			<td>
				<form:input path="alias"/>
				<form:errors path="alias" cssClass="errorMessage"/>
			</td>
		</tr>
			
		<tr>
			<th>*处理程序</th>
			<td>
				<select id="processClass" name="processClass">
				<c:forEach var="p" items="${nodeProcessorList}">
					<option id="processor" name="processor" value="${p}"><spring:message code="NodeProcessor.${p}" /></option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>*模版</th>
			<td>
				<select id="templateId" name="templateId">
				<c:forEach var="t" items="${templateList}">
					<option id="templateId" name="templateId" value="${t.templateId}">${t.templateName}[${t.templateId}]</option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>*自动包含子栏目</th>
			<td style="text-align:left;">
				
					
					<!--<input type="checkbox" checked="checked" value="">栏目ID:${t.nodeId},获取文章数量:${t.rows},显示环境:${t.contextType}</input><br/>-->
					<ul id="browser" class="filetree" style="background:#F9F9F9;">
					
					<c:forEach var="node" items="${nodeTree}" varStatus="in">
						<li>
							<c:set var="parentNode" value="${node}" scope="request"/>
							<c:import url="/WEB-INF/jsp/common/include/nodeEditTreeList.jsp" />
						</li>
					</c:forEach>
					
					</ul>
			</td>
		</tr>
		<tr>
			<th>*多站点代码[如不确定，请勿修改]</th>
			<td>
				<select id="siteCode" name="siteCode">
				<c:forEach var="t" items="${siteCodeList}">
					<option id="siteCode" name="siteCode" value="${t}">${t}</option>
				</c:forEach>
				</select>	
			</td>
		</tr>
		
		<tr>
			<th>*状态</th>
			<td>
				<form:errors path="currentStatus" cssClass="errorMessage"/>
				<select id="currentStatus" name="currentStatus">
				<c:forEach var="status" items="${statusCodeList}">
					<option id="currentStatus"  value="${status.id}" <c:if test="${status.id == 100001}"> selected </c:if> ><spring:message code="Status.${status.id}" text="${status.name}"/></option>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr id="lastTr"> <td align="center" colspan="2"><input class="querenbtn" type="submit" value="确 认" onclick="btnSubmit(this)"/></td></tr>
		</table>
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
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script src="/theme/${theme}/js/jquery.treeview.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.form.3.5.1.js" type="text/javascript"></script>
  </body>
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
</html>
