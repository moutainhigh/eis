<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE HTML>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <title>活动详情</title>
    <link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
    <link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/tuan3.css?v=1701161443">
    <link rel="stylesheet" href="../../css/mobile/jquery.spinner.css?v=02201432" />
    <script type="text/javascript"  src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
    <script type="text/javascript"  src="../../../theme/${theme}/js/mobile/common.min.js"></script>
    <script type="text/javascript"  src="../../../theme/${theme}/js/mobile/jquery.spinner.js"></script>
    <script type="text/javascript"  src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript"  src="../../../../../theme/${theme}/js/mobile/lazyload.js"></script>
	 <script type="text/javascript"  src="../../../../../theme/${theme}/js/mobile/productdetail.js"></script>
<script type="text/javascript">
function countDown(time,id,curTime){
	
	var day_elem = $(id).find('.day');
	var hour_elem = $(id).find('.hour');
	var minute_elem = $(id).find('.minute');
	var second_elem = $(id).find('.second');	
	var end_time = new Date(time.replace(/-/g,'/')).getTime();//月份是实际月份-1
	sys_second = (end_time-curTime)/1000;
	var timer = setInterval(function(){
		if (sys_second > 1) {
			sys_second -= 1;
			var day = Math.floor((sys_second / 3600) / 24);
			var hour = Math.floor((sys_second / 3600) % 24);
			var minute = Math.floor((sys_second / 60) % 60);
			var second = Math.floor(sys_second % 60);
			day_elem && $(day_elem).text(day);//计算天
			$(hour_elem).text(hour<10?"0"+hour:hour);//计算小时
			$(minute_elem).text(minute<10?"0"+minute:minute);//计算分钟
			$(second_elem).text(second<10?"0"+second:second);//计算秒杀
		} else {
			 //location.reload();
			 clearInterval(timer);
			 $("#btn-container").html("<a class='btn_buy2' >活动已结束</a>")
		}
	}, 1000);
}

</script>
</head>
<body>
     <%@include file="/WEB-INF/jsp/include/header.jsp" %>
	<div class="con">
		<div class="content">
			<div class="box_container"><span class="ware_name">${fn:substring(document.title ,0,12)}${fn:length(document.title)>12?"..":""}</span></div>
			<div class="box_container_a">
				<div class="box_left">
					<div class="box_line">以先推荐售价：<span class="new_sprice">￥${document.documentDataMap.get('activityPrice').dataValue}</span><!--<span class="old_sprice">￥86.5</span>--></div>
					<div class="box_line">市场产品售价：<span class="old_sprice">￥${document.documentDataMap.get('marketPrice').dataValue}</span></div>
					<div class="box_line"><span class="ware_where">产地：${document.documentDataMap.get("productOrigin").dataValue}</span></div>
					<div class="box_line"><span class="">规格：${document.documentDataMap.get("goodsSpec").dataValue}</span></div>
					
				</div>
			</div>
            <input type="hidden" id="documentCode" value="${document.documentCode}"/>
			<input type="hidden" id="productCode" value="${document.documentDataMap.get('productCode').dataValue}"/>
			<div class="box_container colockbox" id="colockbox1"> 活动倒计时：<span class="data_space day">00</span>天<span class="data_space hour">00</span>时<span class="data_space minute">00</span>分<span class="data_space second">00</span>秒 </div>
			<div class="box_container"> <span class="text_left">购&nbsp;买&nbsp;份&nbsp;数：</span> 
			<span class="line_container">
				<progress max="100" value="${document.documentDataMap.get('soldCount').dataValue*100/(document.documentDataMap.get('availableCount').dataValue+document.documentDataMap.get('soldCount').dataValue)}">
				<ie style="width:${document.documentDataMap.get('soldCount').dataValue*100/(document.documentDataMap.get('availableCount').dataValue+document.documentDataMap.get('soldCount').dataValue)}%;"></ie>
			   </progress>
			 </span> 
				<span class="text_font">&nbsp;
				<span class="data_space">
				${document.documentDataMap.get('soldCount').dataValue}
				</span>
				<span class="data_space0">
				/${document.documentDataMap.get('availableCount').dataValue + document.documentDataMap.get('soldCount').dataValue}
				</span>
				</span>				
			</div>
			<div class="box_container_z">
			<fmt:parseDate value="${document.documentDataMap.activityBeginTime.dataValue}" var="beginTime"/>
			<fmt:parseDate value="${document.documentDataMap.activityEndTime.dataValue}" var="endTime"/>			
			<div class="box_line"> 活动时间：<fmt:formatDate value="${beginTime}" type="date"/>—<fmt:formatDate value="${endTime}" type="date"/></div>
				<div class="box_line"><span class="ware_where"> 发货规则：活动结束后三天内陆续原产地发货</span></div>
				<div class="box_line"><span class="ware_where">发货地：${document.documentDataMap.deliveryFromArea.dataValue}</span></div>
				<div class="box_line1"><span class="ware_where">配送：<span class="ware_fo">${document.documentDataMap.get("deliveryDesc").dataValue}</span></span></div>
			</div> 
			<c:if test="${result.operateCode!=500080&&result.operateCode!=500072}">
			<c:set var="curTime" value="<%=new java.util.Date().getTime()%>"/>	
			<script>
				countDown("${document.documentDataMap.get('activityEndTime').dataValue}","#colockbox1",${curTime});	
			</script>  
			</c:if> 
			    
</div>
	<div id="data_a"><a href="#" id="checkJsApi"></a></div>
</div>
<div class="fixDiv">
     <a href="/cart.shtml">
     <div class="left">
	    <img src="../../../../image/mobile/footer/cart.png">
		<span class="${empty result.attachment.cartaCount?'':'cartIcon'}">${empty result.attachment.cartaCount?'':result.attachment.cartaCount}</span>
	 </div>
	 </a>
	 <div class="right" id="btn-container">
	 	    <c:choose>
      			<c:when test="${result.operateCode==500080||result.operateCode==500072}">
					<a class="btn_buy2" >${result.message}</a>
	  			</c:when> 			
				<c:when test="${document.documentDataMap.get('availableCount').dataValue==0&&result.operateCode!=500080&&result.operateCode!=500072}">
					<a class="btn_buy2" >已售罄</a>
	  			</c:when>  
				<c:otherwise> 
				<c:if test="${!empty result.attachment.productCount&&result.attachment.productCount!=0&&result.attachment.productCount!=null}">
				<div class="spinner" >
					<a class="decrease" onClick="decrease(this,'${result.attachment.transaId}')">-</a>
					<input type="text" class="spinner value passive" value="${empty result.attachment.productCount?0:result.attachment.productCount}" id="productcount" maxlength="2" oninput="changevalue(this.value,'${result.attachment.transaId}')"  >
					<a class="increase" onClick="increase(this,'${result.attachment.transaId}')">+</a>
				</div>
				</c:if>
				<c:if test="${empty result.attachment.productCount||result.attachment.productCount==0||result.attachment.productCount==null}">	
				 <a class="btn_buy" id="buyNow">加入购物车</a>
				</c:if>
	            </c:otherwise>
            </c:choose>
	</div>
</div>
	<div class="detail_con cpxq" >
	   <div class="detail_con_text">
       ${document.content}
	       <!-- <div class="linellae"></div>	
			<div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
			<div class="linellae1"></div> -->
	   </div>
      </div>
	<div class="goods_comm">商品评论</div>
	<div class="detail_con yhpj" >
       <ul class="buy-list wid90">
	   <c:forEach var="comment" items="${commentList}">
		<c:forEach var="i" items="${comment}" varStatus="status">
		<c:if test="${status.index==0}">
	    <li>
		</c:if>
		<c:if test="${status.index!=0}">
		<li style="margin-left:8%;">
		</c:if>
		   <div class="ware_img">
			<c:choose>
			  <c:when test="${!empty i.data.userHeadPic}">
				<c:if test="${fn:indexOf(i.data.userHeadPic,'http://')!=-1}">
					<img src="${i.data.userHeadPic}">
				</c:if>
				<c:if test="${fn:indexOf(i.data.userHeadPic,'http://')==-1}">
					<img src="/static/userUploadDir/${i.data.userHeadPic}">
				</c:if>
			  </c:when>
			  <c:otherwise>
				<img src="../../image/header.png">
			  </c:otherwise>
			</c:choose>		   
		   </div>
		   <div class="box_right2">
			  <div> <span class="ware_names"><!-- ${i.data.userRealName} --><c:set var="l" value="${fn:length(i.data.userRealName)}"></c:set>			
				${fn:substring(i.data.userRealName,0,1)}***${fn:substring(i.data.userRealName,l-1,l)}
			  </span></div>
			  <div class="divCenter"> <span class="ware_from">${i.title}${i.content}</span></div>
              <div class="one_sprice"><span class="price_mark"><fmt:formatDate value="${i.publishTime}"  type="both"/></span> <span class="fare orange" style="float:right;display:none;">删除</span></div>
			  </div>
		</li>
		 </c:forEach>
		</c:forEach>
		<c:if test="${fn:length(commentList)==0||empty commentList}">
		   <p style="margin-top: 10px;text-align:center;">此商品暂无评价</p>
		</c:if>
	  </ul>

	  	       <!--  <div class="linellae"></div>	
			    <div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
			    <div class="linellae1"></div> -->
      </div>
</body>
</html>