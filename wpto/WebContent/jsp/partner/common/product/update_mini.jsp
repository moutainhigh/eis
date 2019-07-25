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
	<script type="text/javascript" charset="utf-8">
        window.UEDITOR_HOME_URL = location.protocol + '//'+ document.domain + (location.port ? (":" + location.port):"") + "/ueditor/";
		//alert(window.UEDITOR_HOME_URL);
    </script>
	<script type="text/javascript" src="/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="/ueditor/ueditor.all.min.js"></script>
	<script src="/theme/${theme}/js/sweetalert.min.js"></script>
		<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<style>
		
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
		
		
		
		.table-responsive table tr .productLeft{
			background-color:#EEEEEE;
			padding-right:25px;
			padding-top:20px;
		}
		.table-responsive table tr .productRight{
			background-color:#FFFFFF;
			padding-top:18px;
			padding-left: 70px;
		}
		.table-responsive table tr{
			height:60px;
		}
		.table-responsive table tr td input{
			width:23%;
		}
		.table-responsive table tr td input[type="radio"],.table-responsive table tr td input[type="number"]{
			width:auto!important;
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
			<h2 class="sub-header"><span>${title}</span></h2>
			<!--<p style="padding-bottom:10px;font-weight:bold;display:inline-block;" id="documentchangetype">产品类型一旦确认后续将不能改变</p>-->
			<div class="table-responsive">	
				<form:form id="productForm"  commandName="product" name="productForm" action="/product/create.shtml" enctype="multipart/form-data" onsubmit="return false">
				<input type="hidden" name="workflowInstanceId" value="${workflowInstanceId}"/>
				<input type="hidden" name="productCode" value="${product.productCode}"/>
				<form:hidden path="productId"/>
				<form:hidden path="ownerId"/>
				
				 <table class="table table-striped">
		            <thead style="display:none;">
		                <tr>
		                  <th style="width:20%;text-align:right;"></th>
		                  <th style="width:80%;text-align:left;"></th>		                  
		                </tr>
		            </thead>
					<tbody>
						
				
				<c:forEach items="${validAttributeMap}" var="map">
						<tr>
							<td style="width:20%;text-align:right;" class="productLeft"><spring:message code="DataName.${map.value.columnName}"  />：</td>
												
								<c:choose>
									<c:when test="${map.value.inputMethod=='file'}"> 
										<c:choose>
											<c:when test="${map.value.columnType == 'extra'}">
												<td style="width:80%;text-align:left;" class="productRight"><input type="file" name="${map.value.columnName}" id="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}" /></td>
											</c:when>
											<c:otherwise>
												<td style="width:80%;text-align:left;" class="productRight"><input type="file" name="${map.value.columnName}" id="${map.value.columnName}" value="${product[map.value.columnName]}" /></td>
											</c:otherwise>
										</c:choose>
										<!--<td style="width:80%;text-align:left;" class="productRight"><input type="file" name="${map.value.columnName}" id="${map.value.columnName}"  /></td>-->
									</c:when>
									<c:when test="${map.value.inputMethod=='textarea'}">
										<c:choose>
											<c:when test="${map.value.columnType == 'extra'}">
												<td style="width:80%;text-align:left;" class="productRight"><textarea rows='5' cols='130' name="${map.value.columnName}" id="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}"   ></textarea></td>
											</c:when>
											<c:otherwise>
												<td style="width:80%;text-align:left;" class="productRight"><textarea rows='5' cols='130' name="${map.value.columnName}" id="${map.value.columnName}" value="${product[map.value.columnName]}"   ></textarea></td>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${map.value.inputMethod=='select'}">
										<td style="width:80%;text-align:left;" class="productRight">
											<c:choose>
												<c:when test="${map.value.columnType == 'extra'}">
													<select name="${map.value.columnName}" id="${map.value.columnName}">
														<c:forEach var="s" items="${map.value.validValue}">
															<option value="${s}"
															<c:if test="${s==product.productDataMap[map.value.columnName].dataValue}">selected </c:if>
															> <spring:message code="${map.value.useMessagePrefix}.${product.productDataMap[map.value.columnName].dataValue}" /> </option>
														</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<select name="${map.value.columnName}" id="${map.value.columnName}">
														<c:forEach var="s" items="${map.value.validValue}">
															<option value="${s}"
															<c:if test="${s==product[map.value.columnName]}">selected </c:if>
															> <spring:message code="${map.value.useMessagePrefix}.${s}" /> </option>
														</c:forEach>
													</select>
												</c:otherwise>
											</c:choose>
										</td>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${map.value.columnType == 'extra'}">
												<c:choose>
													<c:when test="${map.value.columnName == 'productCode'}">
														<td style="width:80%;text-align:left;" class="productRight"><input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}" /><label >${product.productDataMap[map.value.columnName].dataValue}</label><!--<input type="text" name="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}"  />--></td>
													</c:when>
													<c:otherwise>
														<td style="width:80%;text-align:left;" class="productRight"><input type="text" name="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}"  /></td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${map.value.columnName == 'productCode'}">
														<td style="width:80%;text-align:left;" class="productRight"><input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${product[map.value.columnName]}" /><label >${product[map.value.columnName]}</label><!--<input type="text" name="${map.value.columnName}" value="${product[map.value.columnName]}"  />--></td>
													</c:when>
													<c:otherwise>
														<td style="width:80%;text-align:left;" class="productRight"><input type="text" name="${map.value.columnName}" id="${map.value.columnName}" value="${product[map.value.columnName]}"  /></td>
													</c:otherwise>
												</c:choose>
											</c:otherwise>	
										</c:choose>
									</c:otherwise>
								</c:choose>
						</tr>
				</c:forEach>
						<!-- 下面是非动态、写死的输入 -->
								<tr>
									<td style="width:20%;text-align:right;">产品名称</td>
									<td style="width:80%;text-align:left;">
										<input name="productName" id="productName" type="text" value="${product.productName}" />
									</td>							
								</tr>
								<tr>
									<td style="width:20%;text-align:right;">钻石获得的倍数</td>
									<td style="width:80%;text-align:left;">
										<input name="rate" id="rate" type="text" value="${product.rate}" />
									</td>							
								</tr>
								<tr>
									<td style="width:20%;text-align:right;">市场价格</td>
									<td style="width:80%;text-align:left;">
										<input name="labelMoney" id="labelMoney" type="text" value="${product.labelMoney}" />
									</td>							
								</tr>
								<tr >
									<td style="width:20%;text-align:right;">标准价格</td>
									<td style="width:80%;text-align:left;">
										<span>${moneyName}：<input name="PRICE_PRICE_STANDARD.money" id="PRICE_PRICE_STANDARDmoney" type="text" value="${product.price.money}" style="margin-left:5px;margin-right:10px;"/></span>
										<span>${coinName}：<input name="PRICE_PRICE_STANDARD.coin" id="PRICE_PRICE_STANDARDcoin" type="text" value="${product.price.coin}" style="margin-left:5px;margin-right:10px;"/></span>
										<span>${pointName}：<input name="PRICE_PRICE_STANDARD.point" id="PRICE_PRICE_STANDARDpoint" type="text" value="${product.price.point}" style="margin-left:5px;margin-right:10px;"/></span>
										<span>${scoreName}：<input name="PRICE_PRICE_STANDARD.score" id="PRICE_PRICE_STANDARDscore" type="text" value="${product.price.score}" style="margin-left:5px;margin-right:10px;"/></span>
									</td>							
								</tr>
							<c:if test="${postProcess > 0}">
								<tr>
									<td style="font-size:16px; text-align:left" colspan="2">以下为产品文档内容</td>		
								</tr>
								<tr>
									<td>标签（多个请用英文逗号分开）</td>
									<td>
										<input name="tags" size="50"/>
									</td>							
								</tr>
								<tr>
									<td>指定模板</td>
									<td>
										<select name="templateId" id="templateId">
											<option value="0">不选择[将使用节点模版]</option>
											<c:forEach var="t" items="${templateList}">
												<option value="${t.templateId}">${t.templateName}[${t.templateId}]</option>
											</c:forEach>
										</select>						
									</td>							
								</tr>
											
								<tr class="content">
									<td>内容</td>
									<td>
										<script type="text/javascript">
											var editor = new UE.ui.Editor();
											textarea:'content'; //与textarea的name值保持一致
											editor.render('content');
											</script>
											<script type="text/plain" id="content" name="content">
											${document.content}
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
									<td>发布到的位置</td>
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
								<tr id="lastTr">
								<td>
								
								
								</td>
								</tr>
							</c:if>
				<%-- <tr>
					<td style="text-align:right;width:200px;">
						产品名称：
					</td>
					<td style="text-align:left;">
						${product.productName}
					</td>
				</tr> --%>
				
   				<tr>
   					<td style="text-align:right;">
   						展现形式：
   					</td>
   					<td style="text-align:left;">
   						<select name="displayTypeId" id="displayTypeId">
   							<option value="0"> -- 请选择 -- </option>
   							<option value="176005">轮播图</option>
   						</select>
   					</td>
   				</tr>
   				
					</tbody>
				</table>
				<div class="submit"><input type="submit" value="保存" class="addPro"></div>
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
	/*$(function(){
		$("#productForm").submit(function(e){
				e.preventDefault();
				$(this).ajaxSubmit(
				 {
					type:"POST",
					url:"/product/update.json",
					dataType:"json",
					data:$("#productForm").serialize(),				
					success:function(data) {
						$(".fullScreenBg").show();
						$(".message").show();
						$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
					},
					error:function(XMLResponse){
						alert("操作失败:" + XMLResponse.responseText);
					},
					
				}			
				);
			})
			$(".message .submit").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
				location.href = "http://yixian.mo4u.cn:8043/product.shtml?time=1466991698352";
			})
			
		
	})*/
	
	$(function(){
		// $("#productForm").submit(function(e){
		// 		e.preventDefault();
		// 		reg=/^[0-9]*$/;
		// 		zhongWenYingWenShuZi=/^[\u4E00-\u9FA5A-Za-z0-9]+$/;
		// 		var partnerName = $("#partnerName").val();
		// 		var goodsSpec = $("#goodsSpec").val();
		// 		var deliveryFromArea = $("#deliveryFromArea").val(); 
		// 		var productCode = $("#productCode").val();
		// 		var deliveryCompanyName = $("#deliveryCompanyName").val(); 
		// 		var productOrigin = $("#productOrigin").val(); 
		// 		var deliveryPriceListFile = $("#deliveryPriceListFile").val();
		// 		var availableCount = $("#availableCount").val();
		// 		var productName = $("#productName").val();
		// 		if(partnerName == ""){ 
		// 			alert("产品合作方不能为空！");
		// 		}else if(!zhongWenYingWenShuZi.test(partnerName)){
		// 			alert("产品合作方输入内容不合法！");
		// 		}else if(goodsSpec == ""){
		// 			alert("产品规格不能为空！");
		// 		}else if(!zhongWenYingWenShuZi.test(goodsSpec)){
		// 			alert("规格输入内容不合法！");
		// 		}else if($("#productTypeId").val()==""){
		// 			alert("产品类型不能为空！");
		// 		}else if(deliveryFromArea == ""){
		// 			alert("产品发货地不能为空！");
		// 		}else if(!zhongWenYingWenShuZi.test(deliveryFromArea)){
		// 			alert("产品发货地输入内容不合法！");
		// 		}else if(deliveryCompanyName == ""){
		// 			alert("产品合作快递不能为空！");
		// 		}else if(!zhongWenYingWenShuZi.test(deliveryCompanyName)){
		// 			alert("产品合作快递输入内容不合法！");
		// 		}else if(productOrigin == ""){
		// 			alert("产品产地不能为空！");
		// 		}else if(!zhongWenYingWenShuZi.test(productOrigin)){
		// 			alert("产品产地输入内容不合法！");
		// 		}else if(availableCount == ""){
		// 			alert("库存不能为空！");
		// 		}else if(!reg.test(availableCount)){
		// 			alert("库存必须为数字！");
		// 		}else if(availableCount < -1){
		// 			alert("库存最小为-1");
		// 		}else if(productName == ""){
		// 			alert("产品名称不能为空！");
		// 		}else if(!zhongWenYingWenShuZi.test(productName)){
		// 			alert("产品名称输入内容不合法！");
		// 		}
		// 		else{
					
		// 			// $(this).ajaxSubmit(
		// 			//  {
		// 			// 	type:"POST",
		// 			// 	url:"/product/update.json",
		// 			// 	dataType:"json",
		// 			// 	data:$("#productForm").serialize(),				
		// 			// 	success:function(data) {	
		// 			// 		//alert($("#productForm").serialize());
		// 			// 		// $(".fullScreenBg").show();
		// 			// 		// $(".message").show();
		// 			// 		// $(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
		// 			// 		swal(data.message.message+"["+data.message.operateCode+"]");
		// 			// 	},
		// 			// 	error:function(XMLResponse){
		// 			// 		// $(".fullScreenBg").show();$(".message").show();$(".message .text").html(XMLResponse.responseText);
		// 			// 		// alert("操作失败:" + XMLResponse.responseText);
		// 			// 		swal("操作失败:" + XMLResponse.responseText)

		// 			// 	},
						
		// 			// }			
		// 			// );
		// 		}
		// 	})

			$(".message .submit").click(function(){
				$(".fullScreenBg").hide();$(".message").hide();
				location.href = "http://yixian.mo4u.cn:8043/product.shtml?time=1466991698352";
			})
		
			$("#productForm").submit(function(e){
				e.preventDefault();
				$(this).ajaxSubmit(
					 {
						type:"POST",
						url:"/product/update.json",
						dataType:"json",
						data:$("#productForm").serialize(),				
						success:function(data) {	
						
							swal(data.message.message+"["+data.message.operateCode+"]");
						},
						error:function(XMLResponse){
							
							swal("操作失败:" + XMLResponse.responseText)

						}
						
					}			
					);


			});

			// select
			// $('#displayTypeId').change(function(){
			// 	if ($('#displayTypeId option:selected').attr('value')=='176005') {
			// 		if ($('.displayIndextr').length>0 && $('.displayIndextr').length) {
			// 		return false;
			// 	}else{
			// 			$(this).parents('tr').after('<tr class="displayIndextr"><td  style="text-align:right;">在轮播图中的展现顺序：</td><td style="text-align:left;"><input type="number" value="1" max="10" min="1" name="displayIndex"/><span>（最小为1，最大为10）</span></td></tr>');
			// 		}
			// 	}else{
			// 		$('.displayIndextr').remove();
			// 	}
			// })


		
		}) 
	</script>
	
  </body>
</html>
