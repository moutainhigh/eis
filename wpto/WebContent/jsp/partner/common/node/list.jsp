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

    <title>${systemName}-栏目列表</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">

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
	<script src="/theme/${theme}/js/sweetalert.min.js"></script> 
    <link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
  </head>
  <style>
		.blue{color:#428bca;}
		.fa{font-size:16px;}
		#edui1{z-index:9!important;}
		#browser{line-height:12px !important;}
		#browser span{display:inline-block;line-height:30px;padding-left:5px;padding-right:5px;}
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
		.filetree li { padding: 2px 0 2px 16px; }
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
		.main .page-header,.sub-header {
		margin-top: 0;
		color: #5D5D5D;
		font-size: 27px;
		border-bottom:1px dashed #5D5D5D;
		padding-bottom :5px;
		margin-bottom: 29px;
		}
		.main .page-header span,.sub-header span{
		border-left: 4px solid #5D5D5D;
		padding-left:6px;
		display: inline-block;
		font-size: 20px;
		}
	</style>
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
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main"style="margin-bottom: 100px;">
			<h2 class="sub-header"><span>栏目列表</span></h2>
			<div class="lmgl-lmlb">
				<span style="width:18%;display: inline-block; text-align: center;">栏目名称</span>
				<span style="width:18%;display: inline-block; text-align: center;">路径</span>
				<span style="width:26%;display: inline-block; text-align: center;">使用模版</span>
				<span style="width:18%;display: inline-block; text-align: center;">处理器</span>
				<span style="width:8%;display: inline-block; text-align: center;">状态</span>
				<span style="width:9%;display: inline-block; text-align: center;">操作</span>
			</div>					
			<div style="background:#F9F9F9;border: 1px solid #ddd;">
					<ul id="browser" class="filetree" style="background:#F9F9F9;">
					<c:forEach var="node" items="${nodeTree}" varStatus="xh">
					
						<li>
							<c:set var="parentNode" value="${node}" scope="request"/>
							<c:import url="/WEB-INF/jsp/common/include/nodeTreeList.jsp" />
						</li>
					</c:forEach>
					</ul>
						
					
			</div>
			<c:if test="${empty nodeTree}">
				<a href="#" onclick="addNode(0)" style="display: inline-block;    padding: 7px 12px;    background-color: #6982A0;    border-radius: 5px;    font-size: 16px;    color: #fff;    margin-top: 20px;">增加栏目</a>
			</c:if>	
			
        </div>
      </div>
    </div>
    <div class="delete">
    	<div class="box">
    		<h3>确定删除栏目吗？</h3>
    		<a href="#"  class="confirm">确定</a><span class="cancel">取消</span>
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
  </body>
	<script>

	$(function(){
        var len = $('.hehe').length;
        for(var i = 1;i<=len;i++){
			if(i%2==0){
				$('.hehe:eq('+(i-1)+')').val(i).parent('span').css({'background-color':'white', 'border-top': '1px solid #ddd','border-bottom': '1px solid #ddd'});				
			} else {
				$('.hehe:eq('+(i-1)+')').val(i);
			}
        }
	});
	
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
	//增加子栏目
	function addNode(a){
		//alert(a);
		window.location=("/node/create.shtml?parentNodeId=" + a);
	}
	//删除
	/*删除静态文件的按钮
	ajax post到/staticize/delete.json
	POST数据 objectType=node
	objectId=node.nodeId*/
	$('.materialDelete').on('click',function(){
		var href = $(this).attr('ahref');
		$('.delete').css('display','block');
		$('.delete .confirm').attr('href',href);
	});
	$('.delete .confirm').on('click',function(){
		$('.delete').css('display','none');
		delNode($(this).attr('href'));
	});
	$('.delete .cancel').on('click',function(){
		$('.delete').css('display','none');
	});

	function delNode(a){
		$.ajax({
			type:"POST",
			url:"/staticize/delete.json?objectType=node&id="+a,
			dataType:"json",
			success:function(data){
				alert(JSON.stringify(data));
				swal(data.message.message);
			}
		})
	}
	//静态化 ajax post到/staticize/create.json
//POST数据 objectType=node
//objectId=node.nodeId
//然后把结果显示给用户
	function staticNode(a){
		$.ajax({
			type:"POST",
			url:"/staticize/create.json?objectType=node&id="+a,
			dataType:"json",
			success:function(data){
				alert(JSON.stringify(data));
				//if(data.message.operateCode==102008){
					swal(data.message.message);
				//}else{
					
				//}
			}
		})
	}
	//编辑
	function editNode(a){
		window.location=("/node/update/"+a+".shtml?nodeId="+a);
		/*$.ajax({
			type:"GET",
			url:"/node/update/"+a+".shtml?nodeId="+a,
			dataType:"json",
			success:function(data){
				alert(JSON.stringify(data));
			}
		})*/
	}
	</script>
	<script>
	$(".tools").click(function(){
		$(this).parents().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").slideToggle();
	})
  </script>
</html>
