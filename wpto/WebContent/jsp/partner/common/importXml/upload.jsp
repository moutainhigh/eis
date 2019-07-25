<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">

<title>Xml数据导入</title>


</head>
	<style>
		li {list-style-type:none;text-transform: 1px;font-size: 16px;padding-top: 10px;}
	</style>

<body>

	<div id="wrapper">

		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.html">配置文件导入</a>
		</div>

		

		<div class="navbar-default sidebar" role="navigation">
			<div class="sidebar-nav navbar-collapse">
				<ul class="nav" id="side-menu">
					<li class="sidebar-search">
						<div class="input-group custom-search-form">
							<input type="text" class="form-control" placeholder="查询内容...">
							<span class="input-group-btn">
								<button class="btn btn-default" type="button">
									<i class="fa fa-search" style="padding: 3px 0 3px 0;"></i>
								</button>
							</span>
						</div> 
					</li>
					<li><a href="customer.action" class="active"><i
							class="fa fa-edit fa-fw"></i> 配置文件导入</a></li>
				</ul>
			</div>
		</div>
		</nav>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">配置文件导入 {message}</h1>
				</div>
			</div>
			<form method="post"   enctype="multipart/form-data" >
				<label id="realBtn" class="btn btn-info">
			     <input  type="file" id="file" name="dataxml" class="mFileInput" style="left:-9999px;position:absolute;">
			     <span>请先选择XML文件</span>
			</label>
			<li>
				<a onclick="preview('buildingModel')" href="#" class="active">
					<i class="fa fa-edit fa-fw"></i>
					 建筑模型导入
				</a>
			</li>
			<li><a onclick="preview('equipmentModel.action')"  class="active"><i class="fa fa-edit fa-fw"></i> 装备模型导入</a></li>
			<li><a onclick="preview('toolModel.action')" href="javascript:void(0)" class="active"><i class="fa fa-edit fa-fw"></i> 道具模型导入</a></li>
			<li><a onclick="preview('dropModel.action')" href="javascript:void(0)" class="active"><i
							class="fa fa-edit fa-fw"></i> 掉落模型导入</a></li>
			<li><a onclick="preview('eventModel.action')" href="javascript:void(0)" class="active"><i
							class="fa fa-edit fa-fw"></i> 事件模型导入</a></li>
			<li><a onclick="preview('taskModel.action')" href="javascript:void(0)" class="active"><i
							class="fa fa-edit fa-fw"></i> 任务模型导入</a></li>
			<!-- 
				<li><a href="${pageContext.request.contextPath }/faerieModel.action" class="active"><i
								class="fa fa-edit fa-fw"></i> 精灵模型导入</a></li>
				<li><a href="${pageContext.request.contextPath }/faerieReinforceModel.action" class="active"><i
								class="fa fa-edit fa-fw"></i> 精灵强化模型导入</a></li>
			 -->
			<li><a onclick="preview('mapModel.action')" href="javascript:void(0)" class="active"><i class="fa fa-edit fa-fw"></i> 地图模型导入</a></li>
			<li><a onclick="preview('monsterModel.action')" href="javascript:void(0)" class="active"><i class="fa fa-edit fa-fw"></i> 怪物模型导入</a></li>
			<li><a onclick="preview('monsterCreatorModel.action')" href="javascript:void(0)" class="active"><i class="fa fa-edit fa-fw"></i> 怪物生成器模型导入</a></li>
			<li><a onclick="preview('skillModel.action')"  href="javascript:void(0)" class="active"><i
							class="fa fa-edit fa-fw"></i> 技能模型导入</a></li>

			</form>

	<script>
		function preview(str){
			var form =document.forms[0];
			form.action=str;
	          var file=document.getElementById("file");
	          if(file.innerHTML==''){
	                 alert("请先选择对应的文件");
	                 return false;
	           }
			form.submit();
		}
	</script>
</body>

</html>
