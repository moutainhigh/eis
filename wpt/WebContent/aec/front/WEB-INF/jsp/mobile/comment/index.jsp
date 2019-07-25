<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/center.css"/>

<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<body>
   <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span >我的评论</span></a>
	</div>
	<div class="wrapper_1">
	<div class="content">
	  <ul>	
	  <c:forEach var="str" items="${commentList}" varStatus="status">
			   <li>
                        <div class="afbox">
                            <div class="zuic">
                            <a href="${str.data.refUrl}"><img src="${str.data.refImage}"></a>
                            </div>
                            <div class="zhtw">                           
                                <h3 class="plain1"><a href="${str.data.refUrl}">${str.data.refTitle}</a><!-- <div class="like"><img src="../../../../image/mobile/header/preview_unlike_icon1.png"><span>${!empty str.praiseCount?str.praiseCount:0}</span></div> --></h3>
								<h3 class="plain1 mt15"><a href="${str.data.refUrl}">${str.content}</a></h3>
                               <c:if test="${!empty commention.data.get('productGallery')}">
                                <ul class="img-list">
									<c:forEach var="img" items="${fn:split(commention.data.get('productGallery'),',')}">
										<li><img src="/static/userUploadDir/${img}"></li>
									</c:forEach>									
								</ul>
								</c:if>
                                
                                 <div class="other"><div><fmt:formatDate value="${str.createTime}"  type="both"/></div>   </div>
                            </div>
                        </div>
                      
                    </li>
		</c:forEach>
			</ul>
			<div style="clear:both;"></div>
	 </div>
	 </div>
	 <%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>