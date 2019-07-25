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
		<link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">
		<link rel="stylesheet" href="/theme/${theme}/style/query.css">

		<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
		<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
		<script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
		<script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
		<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
		<script src="/theme/${theme}/js/sweetalert.min.js"></script>
		<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
	</head>
	<style>

		input[name='refTitle'] {
			width: 400px !important;
		}
		.query {
			margin-left: 72px !important;
			width:426px;
		}
		.shenhe,.shanchu{
			display: inline-block;
		    padding: 3px 15px;
		    border-radius: 15px;
		    background-color: #5e7592;
		    color: #fff;
		    text-align: center;
		    margin: 0 0 0 13px;
		    cursor: pointer;
		}
		.shanchu{
			background-color: #b0bdd0;
			color: #333;
		}
@media (max-width:1200px){
	#queryForm table,#queryForm tbody ,#queryForm tr{
		display: block;
	}
	.query{
		margin-left:0!important;
		max-width: 100%;
	}
}
		@media (max-width:768px){
			.search_form td{
				display:block !important;
			}
		}
		td img{
			max-width: 55px;
		}
		td img.face{
			width: 55px;height: 55px;
			border-radius: 100%;
			    border: 2px solid #E4E9F0;
		}
	</style>

	<body>
		<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
		<div class="Modal" style="display:none;position:fixed;">
			
			<div class="modalHeader">
			<a class="close" >×</a>
			<span>回复评论</span>
			</div>
			<div class="modalBody">
			<form action="" style="margin:auto 0;">	
				<div class="form-item">
				<label for="company">文章标题:</label>
				<label id="documentTitle">
					
				</label>			
				</div>
				<div class="form-item"><label id="huifu"></label>
					<div id="contentTextarea" contenteditable="true">
						
					</div>
				</div>			
				
			</form>			
			</div>
			<div class="modalFooter">
			<input type="hidden" class="modalTransactionId" value=""/>
			<a href="javascript:void(0);" class="btn submit">确定</a>
			<a href="javascript:void(0);" class="btn cancel">取消</a>
			</div>
		</div>
		<div class="container-fluid">
			<div class="row">
				<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<h2 class="sub-header"><span>${title}</span></h2>
					<div class="content">
				<form id="queryForm">
				<table width="100%" >
					<tbody >
					<tr>
						<td align="left" width="50%">
							时间：
							<span class="shijian">
								<input size="20"  id="d11" placeholder="开始时间" class="calendarInput" name="startTimeBegin" type="text" value="" style="border:none;"/>
								<img onclick="WdatePicker({el:'d11',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
							
							<span class="shijian">
								<input size="20"  id="d12" placeholder="结束时间" class="calendarInput" name="startTimeEnd" type="text" value="" style="border:none;"/>
								<img onclick="WdatePicker({el:'d12',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
							</span>
						</td>
						<td width="50%">
							用户昵称：<input type="text"  id="userRealName" name="userRealName" value="">
							
						</td>
					</tr>
					<tr>
						<td >
							文章标题：<input class="queryForm-btn-wt408" type="text"  id="refTitle" name="refTitle">
						</td>
						<td>
						<button class="btn btn-primary query"  type="submit" value="查询"  onClick="query()" onSubmit="return false;">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="glyphicon glyphicon-search"></span>
								查询
								&nbsp;&nbsp;&nbsp;&nbsp;
							</button>
						</td>
					</tr>

						<!--<td style="display:inline-block; width:23.5%; padding-bottom:8px; border:0">
							栏目：<input type="text" style="width:80%;" name="nodeId"></input>
						</td>-->
					</tbody>
				</table>
				</form>			
			</div>

					<div class="table-responsive">
						<table class="table table-striped" style="margin-bottom: 10px!important;">
							<thead>
								<tr>
									<th></th>
									<th>评论人</th>
									<th width="45%">评论内容</th>
									<th width="15%">文章标题</th>
									<th width="15%">评论时间</th>
									<th width="15%">审核状态</th>
									<th width="5%">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="i2" items="${rows}">
									<c:forEach var="i3" items="${i2}" varStatus="count">
										<c:if test="${fn:length(i2)<2}">
											<!--  -->
											<tr>
												<td><input type="checkbox" name="chb" value="${i3.commentId}"/></td>
												<td class="">
													<c:if test="${i3.data.userHeadPic != null}">
														<img class="face" src="/file/client/${i3.data.userHeadPic}" alt="头像"  />
													</c:if>
		  											<c:if test="${i3.data.userHeadPic == null}">
														<img class="face" src="/theme/basic/images/headericon.png" alt="头像" />
													</c:if>
													<p class="">${i3.data.userRealName}</p>
												</td>
								 				<td>
													<span class="comments">${i3.content}</span></td>
												<td class="">${i3.title}</td>
												<td><span class="times"><fmt:formatDate value="${i3.createTime}" type="both"/></span></td>
												<td class="${i3.currentStatus}"><spring:message code="Status.${i3.currentStatus}" /></td>
												<td style="position: relative;">
													<span class="tools" style="right:5px;cursor: pointer;">
													<img src="/theme/basic/images/tools.png"></span>
													<ul style="position:absolute; width: 96px; text-align: center; padding-right: 10px; padding-left: 10px; margin-left: -38px; top:50px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
														<c:if test="${!empty i3.operate.get }">
															<a href="#" onclick="reply('${i3.data.userRealName}','${i3.data.refTitle}','${i3.commentId}','${uuid}','${nickName}')">
																<li class="materialSelect">回复</li>
															</a>
														</c:if>
														<c:if test="${!empty i3.operate.relate}">
															<a href="#" onclick="shenhe('${i3.operate.relate}', ${i3.commentId})">
																<li>审核</li>
															</a>
														</c:if>
														<c:if test="${!empty i3.operate.clear}">
															<a href="#" onclick="quxiaoshenhe('${i3.operate.clear}', ${i3.commentId})">
																<li>取消审核</li>
															</a>
														</c:if>
														<c:if test="${!empty i3.operate.del }">
															<a href="${i3.operate.del}">
																<li class="materialSelect">删除</li>
															</a>
														</c:if>
													</ul>
												</td>
											</tr>
											<!--  -->
										</c:if>

										<c:if test="${fn:length(i2)>1}">
											<c:if test="${count.count<2}">
												<!--  -->
												<tr>
													<td><input type="checkbox" name="chb" value="${i3.commentId}"/></td>
													<td class="">
													<c:if test="${i3.data.userHeadPic != null}">
														<img class="face" src="/file/client/${i3.data.userHeadPic}" alt="头像"  />
													</c:if>
													<c:if test="${i3.data.userHeadPic == null}">
														<img class="face" src="/theme/basic/images/people.png" alt="头像" /><!-- http://www.yixian365.com/style/images/logo2.png -->
													</c:if>
													<p class="">${i3.data.userRealName}</p>
													</td>
													<td>
														<span class="comments">${i3.content}</span></td>
													<td class="">${i3.title}</td>
													<td><span class="times"><fmt:formatDate value="${i3.createTime}" type="both"/></span></td>
													<td><spring:message code="Status.${i3.currentStatus}" /></td>
													<td style="position: relative;">
														<span class="tools" style="right:5px;cursor: pointer;">
													<img src="/theme/basic/images/tools.png"></span>
														<ul style="position:absolute; width: 96px; text-align: center; padding-right: 10px; padding-left: 10px; margin-left: -38px; top:50px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
															<c:if test="${!empty i3.operate.get }">
																<a href="#" onclick="reply('${i3.data.userRealName}','${i3.data.refTitle}','${i3.commentId}','${uuid}','${nickName}')">
																	<li class="materialSelect">回复</li>
																</a>
															</c:if>
															<c:if test="${!empty i3.operate.relate}">
																<a href="#" onclick="shenhe('${i3.operate.relate}', ${i3.commentId})">
																	<li>审核</li>
																</a>
															</c:if>
															<c:if test="${!empty i3.operate.clear}">
																<a href="#" onclick="quxiaoshenhe('${i3.operate.clear}', ${i3.commentId})">
																	<li>取消审核</li>
																</a>
															</c:if>
															<c:if test="${!empty i3.operate.del }">
																<a href="${i3.operate.del}">
																	<li class="materialSelect">删除</li>
																</a>
															</c:if>
														</ul>
													</td>
												</tr>
												<!--  -->
											</c:if>

											<c:if test="${count.count>1}">
												<tr>
													<td><input type="checkbox" name="chb" value="${i3.commentId}"></td>
													<!-- <td class="cBB"></td> -->
													<td colspan="1" class="tT" width="30%">
														<c:if test="${i3.data.userHeadPic != null}">
														<img class="face" src="/file/client/${i3.data.userHeadPic}" alt="头像" />
													</c:if>
													<c:if test="${i3.data.userHeadPic == null}">
														<img class="face" src="/theme/basic/images/people.png" alt="头像" /><!-- http://www.yixian365.com/style/images/logo2.png -->
													</c:if>
														<span class="">${i3.data.userRealName}</span>
														
													</td>
													<td><span class="comments">${i3.content}</span></td>
													<td class="">${i3.title}</td>
													<td colspan="1"><span class="times"><fmt:formatDate value="${i3.createTime}" type="both"/></span></td>

													<td><spring:message code="Status.${i3.currentStatus}" /></td>
													<td style="position: relative;">
														<span class="tools" style="right:5px;cursor: pointer;">
													<img src="/theme/basic/images/tools.png"></span>
														<ul style="position:absolute; width: 96px; padding-right: 10px; padding-left: 10px; margin-left: -38px; top:50px; text-align: center; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
															<c:if test="${!empty i3.operate.get }">
																<a href="#" onclick="reply('${i3.data.userRealName}','${i3.data.refTitle}','${i3.commentId}','${uuid}','${nickName}')">
																	<li class="materialSelect">回复</li>
																</a>
															</c:if>
															<c:if test="${!empty i3.operate.relate}">
																<a href="#" onclick="shenhe('${i3.operate.relate}', ${i3.commentId})">
																	<li>审核</li>
																</a>
															</c:if>
															<c:if test="${!empty i3.operate.clear}">
																<a href="#" onclick="quxiaoshenhe('${i3.operate.clear}', ${i3.commentId})">
																	<li>取消审核</li>
																</a>
															</c:if>
															<c:if test="${!empty i3.operate.del }">
																<a href="${i3.operate.del}">
																	<li class="materialSelect">删除</li>
																</a>
															</c:if>
														</ul>
													</td>
												</tr>
											</c:if>
										</c:if>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						<p><span><input type="checkbox" class="allSel">全选</span><span class="shenhe">一键审核</span><span class="shanchu">一键删除</span></p>
					</div>
				</div>
			</div>
		</div>
		<div id="BgDiv"></div>
		<div class="Pagination" style="text-align:center;width:100%;background:#fff;">
			<%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
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
	</body>
	<script>
	//查询
	/*$("#queryForm").on('submit',function() {
		type:"POST",
		url:"comment.json",
		dataType:"json",
		success:function(data){
			alert(data.message.message);
		}		
	})*/
	//回复
	function reply(name, title, commentId, uuid, userRealName){
		$("#contentTextarea").empty();
		$("#documentTitle").text(title);
		$("#huifu").text("回复 "+name+" : ");
		$(".Modal").css("display","block");
		$(".Modal .submit").click(function(){
			if($("#contentTextarea").text()==""){
				alert("回复评论为空！");
			} else {
				$.ajax({
					type: "POST", 	
					url:"/comment/create.json",
					data: {
							commentId: commentId,
							content:$("#contentTextarea").text(),
							uuid:uuid,
							nickName:userRealName,
						},
					dataType: "json",
					success:function(data){
						if (data.message.operateCode==102008) { 
							$(".Modal").css("display","none");
							$("#contentTextarea").empty();
							alert(data.message.message);
						} else {
							alert(data.message.message);
						}
						location.reload();
					},	
				})
			}
		})
	}
	$(".Modal .cancel").click(function(){
		$("#contentTextarea").empty();
		$(".Modal").css("display","none");
	})
	$(".Modal .close").click(function(){
		$("#contentTextarea").empty();
		$(".Modal").css("display","none");
	})
	//审核
	function shenhe(relate, commentId){
		$.ajax({
			type:"POST",
			url:relate + ".json?idList=" + commentId,
			dataType:"json",
			success:function(msg){
				alert(msg.message.message);
				//location.reload();
			},
		})
	}
	$('.allSel').change(function(){
		if($(this).is(':checked')){
			$('input[name="chb"]').each(function(){
				$(this).prop('checked',true)
			})
		}else{
			$('input[name="chb"]').each(function(){
				$(this).prop('checked',false)
			})
		}
	});

	$('.shenhe').on('click',function(){
		var idLists = ''
		$('input[name="chb"]').each(function(){
			if($(this).is(':checked')){
				var idList = $(this).attr('value');
				idLists += idList+'-'
			}
		})
		idLists = idLists.substring(0,idLists.length-1);
		if(idLists !== ''){
			$.ajax({
				type:"POST",
				url:"/comment/relate.json?idList=" + idLists,
				dataType:"json",
				success:function(msg){
					alert(msg.message.message);
					location.reload();
				},
			})
		}else{
			alert('您还没有选择需要审核的评论！')
		}
	})
	$('.shanchu').on('click',function(){
		var idLists = ''
		$('input[name="chb"]').each(function(){
			if($(this).is(':checked')){
				var idList = $(this).attr('value');
				idLists += idList+'-'
			}
		})
		idLists = idLists.substring(0,idLists.length-1);
		if(idLists !== ''){
			$.ajax({
				type:"post",
				url:"/comment/delete.json?idList=" + idLists,
				dataType:"json",
				success:function(msg){
					alert(msg.message.message);
					location.reload();
				},
			})
		}else{
			alert('您还没有选择需要删除的评论！')
		}
	})
	//取消审核
	function quxiaoshenhe(clear, commentId){
		$.ajax({
			type:"POST",
			url:clear + ".json?idList=" + commentId,
			dataType:"json",
			success:function(msg){
				alert(msg.message.message);
				location.reload();
			},
		})
	}
	</script>
	<script>
		$(function() {
			$('body').on("click", ".tools", function() {
				$(this).parent().parent().siblings().find(".toolbtns").hide();
				$(this).siblings(".toolbtns").toggle();
			})
		});
	</script>
	<script>
//		$.ajax({
//			type: "GET",
//			url: "/comment.json",
//			dataType: "json",
//			success: function(data) {
//				if (data.rows != null) {
//					var n = data.rows.length;
//					//var htmlTab=writeHtml(n);
//					$(".table").append(htmlTab);
//					for (var i = 0; i < n; i++) {
//						$(".table tbody tr").eq(i).find("td").eq(0).html(i + 1);
//						$(".table tbody tr").eq(i).find("td").eq(1).html(data.rows[i].name);
//						$(".table tbody tr").eq(i).find("td").eq(2).html(data.rows[i].cacheSize);
//						$(".table tbody tr").eq(i).find("td").eq(3).html(data.rows[i].cacheCount);
//						var operateHtml = '';
//						operateHtml += "<span class='tools' style='right:5px;cursor: pointer;'><img src='/theme/basic/images/tools.png'></span>";
//						operateHtml += "<ul style='position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);' class='toolbtns'>";
//						if (data.rows[i].operate.get != null && data.rows[i].operate != null) {
//							operateHtml += "<a href='#' class='detail'><li>查看</li></a>";
//						}
//						if (data.rows[i].operate.del != null && data.rows[i].operate != null) {
//							operateHtml += "<a href='#' class='delete'><li>清除</li></a>";
//						}
//						if (data.rows[i].operate.update != null && data.rows[i].operate != null) {
//							operateHtml += "<a href='#' class='update'><li>修改</li></a>";
//						}
//						if (data.rows[i].operate.preview != null && data.rows[i].operate != null) {
//							operateHtml += "<a href='#' class='preview'><li>预览</li></a>";
//						}
//						operateHtml += "</ul>";
//						$(".table tbody tr").eq(i).find("td").eq(4).html(operateHtml);
//					}
//					$("body").on("click", ".delete", function() {
//						cacheName = $(this).parent().parent().siblings().eq(1).html();
//						//alert(cacheName);
//						$.ajax({
//							type: "POST",
//							url: "/cache/delete.json?idList=" + cacheName,
//							dataType: "json",
//							async: false,
//							success: function(data) {
//								alert(data.message.message);
//								if (data.rows != null) {
//									var n = data.rows.length;
//									var htmlTab = writeHtml(n);
//									$(".table").empty().append(htmlTab);
//									for (var i = 0; i < n; i++) {
//										$(".table tbody tr").eq(i).find("td").eq(0).html(i + 1);
//										$(".table tbody tr").eq(i).find("td").eq(1).html(data.rows[i].name);
//										$(".table tbody tr").eq(i).find("td").eq(2).html(data.rows[i].cacheSize);
//										$(".table tbody tr").eq(i).find("td").eq(3).html(data.rows[i].cacheCount);
//										var operateHtml = '';
//										operateHtml += "<span class='tools' style='right:5px;cursor: pointer;'><img src='/theme/basic/images/tools.png'></span>";
//										operateHtml += "<ul style='position:absolute; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px;display: none; background-color: rgb(255, 255, 255);' class='toolbtns'>";
//										if (data.rows[i].operate.get != null && data.rows[i].operate != null) {
//											operateHtml += "<a href='#' class='detail'><li>查看</li></a>";
//										}
//										if (data.rows[i].operate.del != null && data.rows[i].operate != null) {
//											operateHtml += "<a href='#' class='delete'><li>清除</li></a>";
//										}
//										if (data.rows[i].operate.update != null && data.rows[i].operate != null) {
//											operateHtml += "<a href='#' class='update'><li>修改</li></a>";
//										}
//										if (data.rows[i].operate.preview != null && data.rows[i].operate != null) {
//											operateHtml += "<a href='#' class='preview'><li>预览</li></a>";
//										}
//										operateHtml += "</ul>";
//										$(".table tbody tr").eq(i).find("td").eq(4).html(operateHtml);
//									}
//								}
//							},
//						});
//					})
////var result =confirm("确定吗?");
//if(result ==true)
//{
////处理
//}
//else
//{
////处理
//}
//
//				}
//			},
//		});
		function del(a) {
			
			var  statu=confirm("是否确认删除本条评论？")
			if(statu == true){
			
			$.ajax({
				type:"GET",
				url:"/document/delete.json?idList=" + a,
				dataType:"json",
				//data:$("form").eq(1).serialize();
				success:function(data){
					console.log(data);
					aler(data.message.message);
					location.reload();
				}
			})
		}
		}
		
		function get(a) {
			$.ajax({
				type:"GET",
				url:"/document/delete.json?idList=" + a,
				dataType:"json",
				success:function(data){
					swal(data.message.message);
					location.reload();
				}
			})
		}
	</script>

</html>