<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
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
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/productdetail.css"/>
<link rel="stylesheet" href="../../../theme/${theme}/css/mobile/jquery.spinner.css?v=02201432" />
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/swiper.min.css"/>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.spinner.js"></script> 
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/productdetail.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<!-- 微信分享 -->
<!-- <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>   -->
</head>
<body>

     <%@include file="/WEB-INF/jsp/include/header.jsp" %>
     <div class="swiper-container">
	    <div class="swiper-wrapper">
         <c:if test="${!empty document.documentDataMap.get('productGallery').dataValue}">
			<c:forEach items="${document.documentDataMap.get('productGallery').dataValue.split(',')}" var="i">
				<div class="swiper-slide"><img src="/static/productFile/open/${i}" alt=""></div>
			</c:forEach>
          </c:if> 
          <!-- 测试 -->
          <!-- <div class="swiper-slide"><img src="../../../theme/${theme}/image/mobile/10.png" alt=""></div> -->
          </div>
	   <div class="swiper-pagination"></div>
	   </div>
	<div id="wrapper_1">
		
	   <div class="cont wid90">
      
	   <script language="javascript"> 
			var mySwiper = new Swiper('.swiper-container',{
			pagination : '.swiper-pagination',
			autoplay : 2000,
			paginationClickable :true
			})
		</script>
      <div class="con_a">
	  <div  class="ware_name">
	  	<span id="productTitle">${document.title}</span>
	  	<c:if test="${document.documentDataMap.get('availableCount').dataValue=='0'}">
	  	<span style="color:#ccc;">(已售罄)</span>
	  	</c:if>
		
		  <!-- <c:choose>
			<c:when test="${document.documentDataMap.get('availableCount').dataValue=='0'}">
				
			</c:when>
			<c:otherwise>
				<span style="flex: 1;">${document.title}</span>
				<span style="display: inline-block;width: 15%;text-align: center;color: #8A8A8A;">
			  		<i class="icon iconfont icon-share_gray"></i>
			  		<span id="share" style="display: inline-block;width: 100%;font-size: 12px;margin-top:-5px;">分享</span>
		  		</span>
			</c:otherwise>
		  </c:choose> -->
	  	
		
	  </div>
	    <div class="box_left">   
		  <p><span style="color:#ff6400" id="productprice">￥ ${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,'money:','')}</span></p>
		  <c:if test="${!empty document.documentDataMap.get('productMarketPrice').dataValue}">
		  <p>
			<span class="one_price"><span class="textTh">价格：￥ 110 ${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,'money:','')}</span>
		  </p>
		  </c:if>
		  <p style="display: flex;text-align;">
		  	<span style="flex: 1;text-align:left;">快递：8元</span>
		  	<span style="flex: 1;text-align:center;">月销：<c:if test="${empty document.documentDataMap.get('soldCount').dataValue}">0</c:if>${document.documentDataMap.get("soldCount").dataValue}笔</span>
			<span  style="flex: 1;text-align:right;">${document.documentDataMap.get("deliveryFromArea").dataValue}</span>
		  </p>
		  <div class="chanpincanshu">
			<p>产品参数<i class="icon iconfont icon-miny" style="float: right;font-size: 14px;"></i></p>
			<div class="span">
				<span>产地：${document.documentDataMap.get("productOrigin").dataValue}&nbsp&nbsp&nbsp</span>  
	          	<span>规格：${document.documentDataMap.get("goodsSpec").dataValue}&nbsp&nbsp&nbsp</span> 
	          	<span>保质期：136天</span>
			</div>
			
		  </div>
          
		 <div class="box_right">
          <div class="btn_group"> 
          <input type="hidden" id="documentCode" value="${document.documentCode}"/>
           <input type="hidden" id="productCode" value="${document.documentDataMap.get('productCode').dataValue}"/>
         </div>
        </div>
		
      </div>
	  </div>
	 <!--  <div class="detailbtns">
	     <div id="cpxq" class="btn_detail current" >产品详情</div>
		 <div id="yhpj" class="btn_detail">产品评价</div>
      </div>  -->
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
								<li>
									<!--<c:if test="${status.index==0}">
									<li>
									</c:if>
									<c:if test="${status.index!=0}">
									<li style="margin-left:8%;">
									</c:if>-->
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
												<img src="../../../theme/${theme}/image/header.png">
											</c:otherwise>
										</c:choose>		   
									</div>
									<div class="box_right2">
										<div> <span class="ware_names">
											 ${i.data.userRealName} 
											<!--<c:set var="l" value="${fn:length(i.data.userRealName)}"></c:set>			
											${fn:substring(i.data.userRealName,0,3)}***${fn:substring(i.data.userRealName,l-3,l)}-->
										</span></div>
										<div class="divCenter"> <span class="ware_from">${i.content}</span></div>
										<!--<div class="one_sprice"><span class="price_mark"><fmt:formatDate value="${i.publishTime}"  type="both"/></span> <span class="fare orange" style="float:right;display:none;">删除</span></div>-->
									</div>
								</li>
							</c:forEach>
						</c:forEach>
						<c:if test="${fn:length(commentList)==0||empty commentList}">
						<p style="text-align:center;text-align: center; font-size: 12px; color: #868686; padding: 10px 0;">此商品暂无评价</p>
						<ul class="commentFirst">
							<li>
								<p>
									<img src="../../../theme/${theme}/image/header.png">
									<span>仁66666</span>
								</p>
								<div>为了好评确认收货，结果确认错了，把给闺女买的没到货的确认了收货?腊肉收到了，挺满意，好吃就是买少了，本以为会和以前吃过的不一样，口感挺好，烟熏味挺大，炒着吃蒸着吃都好吃，上图给你看看有没有食欲呵呵?物流挺快服务很周到，肥瘦也均匀，个人觉得值得购买</div>
							</li>
							<li><span class="lookAllComment"><a href="#">查看全部评价</a></span></li>

						</ul>
						</c:if>
					</ul>
				<!--  <div class="linellae"></div>	
					<div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
					<div class="linellae1"></div> -->
				</div>
				
			</div>	 
		</div>
<div class="fixDiv">
     <!-- <a href="/cart.shtml">
     <div class="left">
		<span class="${empty cartaCount?'':'cartIcon'}">${empty cartaCount?'':cartaCount}</span>
	 </div>
	 </a> -->
	 <div>
	        <c:if test="${empty productCount||productCount==0||productCount==null}">				
				<c:choose>
      			<c:when test="${result.operateCode==500080||result.operateCode==500072}">
					<a class="btn_buy2" >已结束</a>
	  			</c:when> 
				<c:when test="${document.documentDataMap.get('availableCount').dataValue=='0'&&result.operateCode!=500080&&result.operateCode!=500072}">
					<a class="btn_buy2" >已售罄</a>
	  			</c:when>
				<c:otherwise> 
					 <span style="color: #7da215; display: inline-block; width: 50px; padding-left: 9px;" onclick="collectproduct()"><i class="icon iconfont icon-shoucang1"></i><span style=" display: block;
    font-size: 12px;
    margin-top: -5px;">收藏</span></span>
				    <a class="btn_buy" id="buyNow">加入购物车</a>
	            </c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${!empty productCount&&productCount!=0&&productCount!=null}">
	 	    <c:choose>
      			<c:when test="${result.operateCode==500080||result.operateCode==500072}">
					<a class="btn_buy2" >已结束</a>
	  			</c:when> 
				<c:when test="${document.documentDataMap.get('availableCount').dataValue=='0'&&result.operateCode!=500080&&result.operateCode!=500072}">
					<a class="btn_buy2" >已售罄</a>
	  			</c:when> 
				<c:otherwise> 
				<div class="spinner" >
					<a class="decrease" onClick="decrease(this,'${transaId}')">-</a>
					<input type="text" class="spinner value passive" value="${empty productCount?0:productCount}" id="productcount" maxlength="2" oninput="changevalue(this.value,'${transaId}')"  >
					<a class="increase" onClick="increase(this,'${transaId}')">+</a>
				</div>
	            </c:otherwise>
            </c:choose>
			</c:if>
		<a href="/cart.shtml" style="display: block; width: 100px!important; height: 50px!important;
    line-height: 50px!important;  text-align: center;  color: #fff!important; background-color: #D4B310; float: right;">查看购物车</a>	
	</div>
</div>
<div class="commentBox">
	<div class="box">
		<h1><span>全部评论</span><span class="close"><i class="icon iconfont icon-guanbi" style="font-size: 22px;  position: absolute; top: 5px; right: 2px;color: rgb(125, 162, 21);"></i></span></h1>
	<ul class="commentFirst">
		<li>
			<p>
				<img src="../../../theme/${theme}/image/header.png">
				<span>仁66666</span>
			</p>
			<div>为了好评确认收货，结果确认错了，把给闺女买的没到货的确认了收货?腊肉收到了，挺满意，好吃就是买少了，本以为会和以前吃过的不一样，口感挺好，烟熏味挺大，炒着吃蒸着吃都好吃，上图给你看看有没有食欲呵呵?物流挺快服务很周到，肥瘦也均匀，个人觉得值得购买</div>
			<div class="img">
				<img src="../../../theme/${theme}/image/header.png"><img src="../../../theme/${theme}/image/header.png">
			</div>
		</li>
		<li>
			<p>
				<img src="../../../theme/${theme}/image/header.png">
				<span>仁66666</span>
			</p>
			<div>为了好评确认收货，结果确认错了，把给闺女买的没到货的确认了收货?腊肉收到了，挺满意，好吃就是买少了，本以为会和以前吃过的不一样，口感挺好，烟熏味挺大，炒着吃蒸着吃都好吃，上图给你看看有没有食欲呵呵?物流挺快服务很周到，肥瘦也均匀，个人觉得值得购买</div>
			<div class="img">
				<img src="../../../theme/${theme}/image/header.png"><img src="../../../theme/${theme}/image/header.png">
			</div>
		</li>
		<li>
			<p>
				<img src="../../../theme/${theme}/image/header.png">
				<span>仁66666</span>
			</p>
			<div>为了好评确认收货，结果确认错了，把给闺女买的没到货的确认了收货?腊肉收到了，挺满意，好吃就是买少了，本以为会和以前吃过的不一样，口感挺好，烟熏味挺大，炒着吃蒸着吃都好吃，上图给你看看有没有食欲呵呵?物流挺快服务很周到，肥瘦也均匀，个人觉得值得购买</div>
			<div class="img">
				<img src="../../../theme/${theme}/image/header.png"><img src="../../../theme/${theme}/image/header.png">
			</div>
		</li>
		<li>
			<p>
				<img src="../../../theme/${theme}/image/header.png">
				<span>仁66666</span>
			</p>
			<div>为了好评确认收货，结果确认错了，把给闺女买的没到货的确认了收货?腊肉收到了，挺满意，好吃就是买少了，本以为会和以前吃过的不一样，口感挺好，烟熏味挺大，炒着吃蒸着吃都好吃，上图给你看看有没有食欲呵呵?物流挺快服务很周到，肥瘦也均匀，个人觉得值得购买</div>
			<div class="img">
				<img src="../../../theme/${theme}/image/header.png"><img src="../../../theme/${theme}/image/header.png">
			</div>
		</li>
	</ul>
	</div>
</div>
</body>
<script>
	$(function(){
		$('.chanpincanshu p').on('click',function(){
			$(this).parent().find('.span').toggle();
			if ($(this).parent().find('.span').css('display')=='block') {
				$(this).find('i').addClass('icon-chevron-large-thin-down')
				$(this).find('i').removeClass('icon-miny');
			}else{
				$(this).find('i').remveClass('icon-chevron-large-thin-down');
				$(this).find('i').addClass('icon-miny');
			}
		});
		var nameF = $('.commentFirst li p span').text().substr(0,1);
		var nameL = $('.commentFirst li p span').text().substr(-1);
		$('.commentFirst li p span').text(nameF+'**'+nameL);

		$('.commentBox .box .close').on('click',function(){
			$('.commentBox .box').css('top','100%')
			setTimeout(function(){
				$('.commentBox').css('display','none')
			},100)
			
		})
		$('.lookAllComment').on('click',function(){
			$('.commentBox').css('display','block')
			setTimeout(function(){
				$('.commentBox .box').css('top','13%');
			},100)
			

		})
	})
	
</script>
</html>    