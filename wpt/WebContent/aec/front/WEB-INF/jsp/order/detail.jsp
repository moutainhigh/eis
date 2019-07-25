<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../css/main.css">
<!--<link rel="stylesheet" type="text/css" href="../css/header.css">-->
<link rel="stylesheet" type="text/css" href="../css/orderdetail.css">
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script  type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
	$(function(){
		$(".iframeHeader .close").on('click',function(){
			console.log("close");
			$(".iframe").hide();
			$(".iframeHeader").hide();
			$(".fullBg").hide();				
		})		
	})
		
	</script>
	<script type="text/javascript">
		$(function() {
			if ($(".marleft50>.orange").html != "发货中") {
				$(".btn_apprise").css("display", "none");
			}

			//发票信息处理
			if((${!empty order.invoiceInfo})){
			var title_top = ${order.invoiceInfo}
					$("#fa_title_top").text("发票抬头：" + title_top.title);

//			if (title_top.indexOf("content") >= 1) {
//				$("#fa_content").text("发票内容：" + title_top.content);
//			} else {
//				$("#fa_content").text("发票内容：");
//			}

			}



		})
		function getDeliveryInfo(orderId){	
			$.ajax({ 
			type: "GET", 	
			url: "/deliveryOrder/traceByDeliveryOrder/"+orderId+".json",
			data:{returnUrl:location.href,displayType:"html"},
			dataType: "json",
			success: function(data){
				console.log(JSON.stringify(data));
				if(data.deliveryOrder){
					url=data.deliveryOrder.traceData.traceUrl;
					url+=(url.indexOf('?')!=-1?"&":"?")+"timeStamp="+(new Date()).getTime();
					$(".iframe iframe").attr("src",url);
					$(".iframe").show();
					$(".fullBg").show();
					$(".iframeHeader").show();
					//window.frames["kuaidi"].location.href=url;
				}else if(data.message){
					alert(data.message.message);
				}
			},				
			});	
		}
	</script>
	<style>
		.iframe{
			position:fixed;
			z-index:100;
			width:500px;
			height:400px;
			margin-left:-250px;
			margin-top:-200px;
			display:none;
			left:50%;
			top:50%;
			background:rgba(250,250,250,0.8);
		}
		.fullBg{
			background:#222222;
			opacity:0.5;
			width:100%;
			height:100%;
			position:fixed;
			z-index:90;
			top:0;
			left:0;
			display:none;
		}
		.iframeHeader{
			height:60px;
			position:absolute;
			z-index:120;
			width:100%;
			left:0;
			top:0;
			background: #4690cd;
			font-size: 16px;
			font-weight: bold;
			color: #ffffff;
			-moz-box-sizing: border-box;
			box-sizing: border-box;
			margin: 0 auto!important;
			line-height:60px;
			text-align:center;
			overflow:hidden;
			display:none;
		}
		.iframeHeader img.close{
			width:35px;
			top:12.5px;	
			position:absolute;
			right:15px;
			cursor:pointer;
		}
		.rel{
			height:100%;
			width:100%;
			position:relative;
		}
	</style>
</head>
<body>
<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="content">
	 <p class="btn_page martop30"><a href="/">首页</a>><a href="/order/index.shtml">我的订单</a>><a href="">订单详情</a></p>
	 <div class="martop10"><span>订单号:${order.cartId}</span><span class="marleft50">状态：<span class="orange"><spring:message code="Status.${order.currentStatus}" /></span></span><a href="#" class="btn_apprise">去评价</a></div>
	 <!--<div class="flowstep martop30">
			<ul class="flowstep-5">
                <li>
                    <div class="step-done">
						<div class="step-yes"></div>
						 <div class="step-name">拍下商品</div>
                        <div class="step-time">
                            <div class="step-time-wraper"><fmt:formatDate value="${order.createTime}" type="both"/></div>
                        </div>
					</div>      
				</li>
				<li>
                    <div class="step-done">
						<div class="step-yes"></div>
						 <div class="step-name">支付成功</div>
                        <div class="step-time">
                            <div class="step-time-wraper"><fmt:formatDate value="${deliveryOrder.createTime}" type="both"/></div>
                        </div>
                    </div>       
				</li>
            	<li>
                    <div class="step-done">
                       
						<div class="step-no"></div>
						 <div class="step-name">卖家发货</div>
                        <div class="step-time">
                            <div class="step-time-wraper"></div>
                        </div>
                    </div>     
				</li>
            	<li class="step-done">
                   
					<div class="step-no"></div>
					 <div class="step-name">确认收货</div>
					 <div class="step-time">
                            <div class="step-time-wraper"></div>
                        </div>
                </li>
         </ul>
      </div>-->
	  <div class="flowstep martop30">
			<ul class="flowstep-5">
                <li>
                    <div class="step-done">
						 <c:choose>
						<c:when test="${order.currentStatus==710019}">
						<div class="step-yes"></div>
						</c:when>
						<c:when test="${order.currentStatus==710017}">
						<div class="step-no"></div>
						</c:when>
						<c:otherwise>
						 <div class="step-yes"></div>
						</c:otherwise>
						</c:choose>
						 <div class="step-name">拍下商品</div>
                        <div class="step-time">
                            <div class="step-time-wraper"><fmt:formatDate value="${order.createTime}" type="both"/></div>
                        </div>
					</div>      
				</li>
				<li>
                    <div class="step-done">
					   <c:choose>
						<c:when test="${order.currentStatus==710019}">
						<div class="step-no"></div>
						</c:when>
						<c:when test="${order.currentStatus==710017}">
						<div class="step-no"></div>
						</c:when>
						<c:otherwise>
						 <div class="step-yes"></div>
						</c:otherwise>
						</c:choose>
						 <div class="step-name">支付成功</div>
                        <div class="step-time">
                            <div class="step-time-wraper"><fmt:formatDate value="${deliveryOrder.createTime}" type="both"/></div>
                        </div>
                    </div>       
				</li>
            	<li>
                    <div class="step-done">  
						<c:choose>
						<c:when test="${order.currentStatus==710050}">
							<div class="step-yes"></div>
						</c:when>
						<c:when test="${order.currentStatus==710052}">
							<div class="step-yes"></div>
						</c:when>
						<c:when test="${order.currentStatus==710053}">
							<div class="step-yes"></div>
						</c:when>
						<c:otherwise>
							<div class="step-no"></div>
						</c:otherwise>
						</c:choose>
						 <div class="step-name">卖家发货</div>
                        <div class="step-time">
                            <div class="step-time-wraper"></div>
                        </div>
                    </div>     
				</li>
            	<li class="step-done">
					<c:choose>
						<c:when test="${order.currentStatus==710010}">
							<div class="step-yes"></div>
						</c:when>
						<c:when test="${order.currentStatus==710052}">
							<div class="step-yes"></div>
						</c:when>
						<c:when test="${order.currentStatus==710053}">
							<div class="step-yes"></div>
						</c:when>
						<c:otherwise>
							<div class="step-no"></div>
						</c:otherwise>
						</c:choose>
					 <div class="step-name">确认收货</div>
					 <div class="step-time">
                            <div class="step-time-wraper"></div>
                        </div>
                </li>
         </ul>
      </div>

	  <div class="martop30">
		  <span>承运公司：
		  	<span class="orange">
		  		<c:set var="chengyun" value="${deliveryOrder.memory}"/>
				${fn:substring(chengyun,5,9)}
		  	</span>
	  </span></div>
	 <!--<div class="package-status martop30">

		
	    <div class="box_noReceive">
		   以上信息已经过优化处理，如需查看快递公司原文信息，请在官网查询
		</div>
	</div>-->
	<div class="martop30">订单信息：</div>
	<div class="orderInfo martop30">
	   <ul>
	   <span>收货人信息</span>
	     <li>收货人：${deliveryOrder.contact}</li>
		 <li>地址：${deliveryOrder.province} ${deliveryOrder.city} ${deliveryOrder.address}</li>
		 <li>联系方式：${deliveryOrder.mobile}</li>
	   </ul>
	    <ul>
	   <span>支付方式</span>
	   <li>微信支付</li>
	   </ul>
	     <ul>
	    <span>发票信息</span> 
	   <li>发票类型：纸质发票</li>
	   <li id="fa_title_top">发票抬头：<i class="invoiceInfo"></i><script>	
						invoice=${!empty order.invoiceInfo?order.invoiceInfo:"{}"}; 
						console.log(invoice.title);						
						var title=invoice.title;
						if(title==undefined){
							title="无";
						}
						$(".invoiceInfo").html(title);							
					</script></li>
	   <li>发票内容：明细</li>
	   </ul> 
	   <ul style="margin-bottom:30px;">
	   <span>商品清单</span>
	   <p></p>
	   </ul>
	   <ul id="producTitle">  
            <li class="li0">商品</li>
            <li class="li1">单价</li>
			<li class="li2">数量</li>
			<li class="li3">小计</li>
         </ul>
		<c:forEach var="product" items="${order.miniItemList}" varStatus="index">
         <ul id="productList">
            
           <li class="li0">
			   <a href="${product.itemDataMap.refUrl.dataValue}">
			   		<img src="${product.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left">
			   </a>
			   <a href="${product.itemDataMap.refUrl.dataValue}">
			   		<span class="box_container_left"  style="font-size:13px;">${product.name}</span>
			   </a>
		   </li>
          
			 
			  <li class="li1">
			   <p>RMB：￥${product.price.money}</p>
			   
		   </li>
		   <li class="li2">${product.count}</li>
           <li class="li3">
			   <p>￥<fmt:formatNumber value="${product.price.money*product.count}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></p>
		   </li>
			<li class="li4"><a href="javascript:getDeliveryInfo('${deliveryOrder.deliveryOrderId}')">查看物流</a></li>
         </ul>
		 </c:forEach>
		<div class="orderInfo1">
		    <p><span class="box_left">商品数量：</span><span class="box_right orange">${order.totalGoods}</span></p>
			<p><span class="box_left">商品金额总计：</span><span class="box_right orange">￥<fmt:formatNumber value="${order.money.chargeMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></p>
			<p><span class="box_left">运费：</span><span class="box_right orange">￥<fmt:formatNumber value="${empty deliveryOrder.fee.money?0:deliveryOrder.fee.money}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></p>
			<p class="line"></p>
			<c:set var="totalMoney" value="${empty deliveryOrder.fee.money?0:deliveryOrder.fee.money}"/>
			<p><span class="box_left">应付金额：</span><span class="box_right">￥:<span style="color: red;"><fmt:formatNumber value="${order.money.chargeMoney + totalMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></span></p>
		 </div>
	</div>
	</div>
	<div class="iframe">
		<div class="rel">
			<iframe src='' height="100%" width="100%" frameborder="0" name="kuaidi"></iframe>
			<div class="iframeHeader">
				快递信息<img src="/image/close.png" class="close">
			</div>
		</div>
	</div>
	<div class="fullBg"></div>
	<div class="empty"></div>
	  <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</body>
</html>