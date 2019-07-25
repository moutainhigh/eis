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

    <title>${systemName}-产品详情</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this workflow -->
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
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>产品详情</span></h2>
			<div class="table-responsive">	
		<table class="table table-striped">
		<colgroup width="800">
			<col width="100"/>
			<col width="*"/>
		</colgroup>
		<tr class="header tabheader">
			<td style="border: none;font-size: 18px" align="left" colspan="2">${product.productId}#产品详情</td>
		</tr>	
		<tr>
			<th>产品代码</th>
			<td>
				<c:out value="${product.productCode}"/>
			</td>
		</tr>
		<tr>
			<th>产品名称</th>
			<td>
				<span>${product.productName}</span>
			</td>
		</tr>
		<tr>
			<th>初始库存</th>
			<td>
				<span>${product.initCount}</span>
			</td>
		</tr>	
		<tr>
			<th>创建时间</th>
			<td>
				<span><fmt:formatDate value='${product.createTime}' type="both"/></span>
			</td>
		</tr>
		<tr>
			<th>发布时间</th>
			<td>
				<span><fmt:formatDate value='${product.publishTime}' type="both"/></span>
			</td>
		</tr>
		<tr>
			<th>订单有效期</th>
			<td>
				${product.transactionTtl}
			</td>
		</tr>		
		<tr>
			<th>状态</th>
			<td>
				<spring:message code="Status.${product.currentStatus}" />
			</td>
		</tr>	
		<c:forEach var="r" items="${product.productDataMap}">
			<tr>
				<th>${r.value.dataName} [${r.value.dataCode}]</th>
				<td>
					${r.value.dataValue}
				</td>
			</tr>
		</c:forEach>
		<tr>
			<th>市场价格（元）</th>
			<td>${product.labelMoney}</td>
		</tr>
		<tr>
			<th>标准价格（元）</th>
			<td>
				<c:forEach var="price" items="${priceList}">
					<span style="margin-right:10px;">${price.priceType} [${price.priceId}]价格：${price.money}</span>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th>标签</th>
			<td>
			<c:forEach items="${tag}" var="a">
				<span style="margin-left:10px;">${a.tagName}</span>
			</c:forEach>
			</td>
		</tr>	
		<tr>
			<th>指定模板</th>
			<td></td>
		</tr>	
		<tr>
			<th>内容</th>
			<td>${product.content}</td>
		</tr>
		<tr>  
			<th>前台页面显示顺序</th>
			<td></td>
		</tr>
		<tr>
			<th>发布到的位置</th>
			<td>${node.showPosition.name}</td>
		</tr>
		<tr>
			<th>同步显示位置</th>
			<td>${node.synPosition.name}</td>
		</tr>
	</table>
	
	  
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
  </body>
</html>
