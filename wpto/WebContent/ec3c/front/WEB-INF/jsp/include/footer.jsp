<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<c:if test="${theme=='basic'}">
<div class="all-service bgc-f2">
	<div class="container clearfix">
    	<div class="services-rule">
        	<p class="h3-title">售后服务和保修政策</p>
            <p class="p-rule">
                <c:forEach items="${navigation}" var="a" >
                    <a class="a-rule" href="${a.viewUrl}">${a.name}</a>
                    | 
                </c:forEach>
                  
            </p>
        </div>
        <p class="service-online"><a class="a-servicer J_openkf">在线客服</a></p>
        <p class="service-online"><a class="s-wechat J-wechat" onMouseOver="sw_nav('wechat',1)" onMouseOut="sw_nav('wechat',0)">微信客服</a></p>
    </div>
</div>
<div class="all-help bgc-f2">
	<div class="container clearfix" style="overflow:hidden;">
        <dl class="dl-all-help">
            <dt><span>关于配送</span></dt>
            <dd><a href="<c:url value='/content/about/CashOnDelivery.shtml'/>" target="_blank" title="货到付款区域">货到付款区域</a></dd>
            <dd><a href="<c:url value='/content/about/IntelligentInquiry.shtml'/>" target="_blank" title="配送支付智能查询">配送支付智能查询</a></dd>
            </dl>
            <dl class="dl-all-help">
            <dt><span>关于我们</span></dt>
            <dd><a href="<c:url value='/content/about/contact.shtml'/>" target="_blank" title="联系我们">联系我们</a></dd>
            <dd><a href="<c:url value='/content/about/about.shtml'/>" target="_blank" title="公司简介">公司简介</a></dd>
       </dl>
       <dl class="dl-all-help">
            <dt><span>售后服务</span></dt>
            <dd><a href="<c:url value='/content/about/serviceGuarantee.shtml'/>" target="_blank" title="售后服务">售后服务</a></dd>
            <dd><a href="<c:url value='/content/about/ReturnPrinciple.shtml'/>" target="_blank" title="退换货政策">退换货政策</a></dd>
        </dl>
    </div>
</div>
<div class="all-promise bgc-f2">
	<ul class="container clearfix ul-all-promise">
    	<li class="li-all-promise li-all-promise-1">
        	<img class="img-promise" src="../../../../theme/${theme}/images/promise-1.png"  width="82" height="76" alt="24小时限时达">
            <a href="javascript:void(0);" class="a-title" target="_blank"><strong class="strong-title">24小时限时达</strong>全国1288个县支持24小时内送货上门</a>
        </li>
        <li class="li-all-promise li-all-promise-2">
        	<img class="img-promise" src="../../../../theme/${theme}/images/promise-2.png" tppabs="../../../../theme/${theme}/images/promise-2.png" width="82" height="76" alt="送装一体">
            <a href="javascript:void(0);" class="a-title" target="_blank">
            <strong class="strong-title">按约送达 超时免单</strong>约定时间内将货物送到收货地点</a>
        </li>
        <li class="li-all-promise li-all-promise-3">
        	<img class="img-promise" src="../../../../theme/${theme}/images/promise-3.png" tppabs="../../../../theme/${theme}/images/promise-3.png" width="82" height="76" alt="送装一体">
            <a href="javascript:void(0);" class="a-title" target="_blank" rel="nofolow">
            <strong class="strong-title">7天包退换</strong>产品售出7日内可退换货</a>
        </li>
        <li class="li-all-promise li-all-promise-4">
        	<img class="img-promise" src="../../../../theme/${theme}/images/promise-4.png" tppabs="../../../../theme/${theme}/images/promise-4.png" width="82" height="76" alt="货到付款">
            <a href="javascript:void(0);" class="a-title" target="_blank" rel="nofolow">
            	<strong class="strong-title">货到付款</strong>全国2082个区县支持货到付款</a>
        </li>
    </ul>
</div>
</c:if>
<c:if test="${theme=='basic2' || theme=='basic3'}">
<div class="site-footer">
    <div class="ft-container">
        <div class="footer-service">
            <ul class="list-service clearfix">
                <li>
                    <a rel="nofollow" href="javascript:void(0)">
                        <i class="iconfont"></i>1小时快修服务
                    </a>
                </li>
                <li>
                    <a rel="nofollow" href="javascript:void(0)">
                        <i class="iconfont"></i>7天无理由退货
                    </a>
                </li>
                <li>
                    <a rel="nofollow" href="javascript:void(0)">
                        <i class="iconfont"></i>15天免费换货
                    </a>
                </li>
                <li>
                    <a rel="nofollow" href="javascript:void(0)">
                        <i class="iconfont"></i>满150元包邮
                    </a>
                </li>
                <li>
                    <a rel="nofollow" href="javascript:void(0)">
                        <i class="iconfont"></i>520余家售后网点
                    </a>
                </li>
            </ul>
        </div>
        <div class="footer-links clearfix">
            <div class="blank"></div>
     
        <dl class="col-links">
      <dt>新手上路</dt>
            <dd> 
        <a href="<c:url value='/content/about/AfterSalesProcess.shtml'/>" target="_blank" title="售后流程" rel="nofollow">售后流程</a>
      </dd>
            <dd> 
        <a href="<c:url value='/content/about/AfterBuyProcess.shtml'/>" target="_blank" title="购物流程" rel="nofollow">购物流程</a>
      </dd>
       
    </dl>
     
     
        <dl class="col-links">
      <dt>配送与支付</dt>
            <dd> 
        <a href="<c:url value='/content/about/CashOnDelivery.shtml'/>" target="_blank" title="货到付款区域" rel="nofollow">货到付款区域</a>
      </dd>
            <dd> 
        <a href="<c:url value='/content/about/IntelligentInquiry.shtml'/>" target="_blank" title="配送支付智能查询 " rel="nofollow">配送支付智能查询</a>
      </dd>
       
    </dl>
     
     
        <dl class="col-links">
      <dt>服务保证</dt>
            <dd> 
        <a href="<c:url value='/content/about/serviceGuarantee.shtml'/>" target="_blank" title="售后服务保证" rel="nofollow">售后服务保证</a>
      </dd>
            <dd> 
        <a href="<c:url value='/content/about/ReturnPrinciple.shtml'/>" target="_blank" title="退换货原则" rel="nofollow">退换货原则</a>
      </dd>
       
    </dl>
     
     
        <dl class="col-links">
      <dt>关于我们</dt>
            <dd> 
        <a href="<c:url value='/content/about/contact.shtml'/>" target="_blank" title="联系我们" rel="nofollow">联系我们</a>
      </dd>
            <dd> 
        <a href="<c:url value='/content/about/about.shtml'/>" target="_blank" title="公司简介" rel="nofollow">公司简介</a>
      </dd>
       
    </dl>
     
     
             <div class="col-contact">
                <p class="phone">0755-25402759</p>
                <p>周一至周日 8:00-18:00<br>（仅收市话费）</p>
                <a rel="nofollow" class="btn btn-line-primary btn-small" href="http://wpa.qq.com/msgrd?v=3&uin=1289275912&site=qq&menu=yes">
                    <i class="iconfont"></i> 24小时在线客服
                </a>
            </div>
        </div>
    </div>
</div>
<div class="site-info">
    <div class="ft-container clearfix">
        <div class="logo ir">${systemName}</div>
        <div class="info-text">
            <span class="sites">
            © 2005-2017 ${systemName} 版权所有，并保留所有权利。            </span><br>
            <p>
                 地址：深圳市福田区沙头街道泰然六路泰然苍松大厦北座1801-57 Tel: 0755-25402759 E-mail: cyxkja@163.com <br>备案号：粤ICP备17040881号-3            </p>
        </div>
        
    </div>
</div>
</c:if>
<c:if test="${theme=='guohongchangsheng'}">
<div id="footer">
  <div class="ft_footer_service"> 
  <a href="#" target="_blank"><span class="s1"></span>全国包邮</a> 
  <a href="#" target="_blank"><span class="s2"></span>正品保障</a> 
  <a href="#" target="_blank"><span class="s3"></span>售后无忧</a> 
  <a href="#" target="_blank"><span class="s4"></span>准时送达</a> 
  </div>
     
  <p class="ft_footer_link"> 
    
    <a href="<c:url value='/cart.shtml '/>">查看购物车</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/about.shtml'/>">企业简介</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/contact.shtml' />" >联系我们</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/serviceGuarantee.shtml' />" >售后服务保证</a> 
          
          <span> | </span>
          
          <a href="<c:url value='/content/about/AfterBuyProcess.shtml' />" >购物流程</a> 
          <span> | </span>
         <a href="<c:url value='/content/about/ReturnPrinciple.shtml' />" >退换货原则</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/CashOnDelivery.shtml' />" >货到付款区域</a> 
      </p>
  
  <div class="text" style="line-height:20px;">
  <center>
 © 2005-2017 北京国宏长圣科技有限公司 版权所有，并保留所有权利。<br>
 北京市通州区运河核心区IV-07地块绿地大厦1号楼22层22090        Tel: 010-84065106        E-mail: ghcsa@163.com<br>
    ICP备案证书号:<a href="#"  target="_blank">京ICP备17033632号-1</a>
  </center>
 </div>
     
 
</div>
</c:if>
<c:if test="${theme=='beijingjiamei'}">
<div id="VjiaFooter" class="footer">
<div class="block">
<div class="footTop"> 
 
</div> 
  <div class="footer_nav_box">
 <div class="footer_nav">
          <ul style="width:1018px; margin:0 auto;">
         <li>
              
         <a href="<c:url value='/cart.shtml '/>">查看购物车</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/about.shtml'/>">企业简介</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/contact.shtml' />" >联系我们</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/serviceGuarantee.shtml' />" >售后服务保证</a> 
          
          <span> | </span>
          
          <a href="<c:url value='/content/about/AfterBuyProcess.shtml' />" >购物流程</a> 
          <span> | </span>
         <a href="<c:url value='/content/about/ReturnPrinciple.shtml' />" >退换货原则</a> 
          
          <span> | </span>
          
          
         <a href="<c:url value='/content/about/CashOnDelivery.shtml' />" >货到付款区域</a> 
                     </li>
            <li class="integrity">
             
    <a href="#">
     
    </a>
   </li>
        </ul>
          
    </div>
 
    <p style="text-align:center; margin-top:15px; color:#999;">
    
 
 
 © 2005-2017 北京加美海兰科技有限公司 版权所有，并保留所有权利。<br>地址:北京市通州区宋庄文化产业创意产业集聚区公共服务平台674号  <br> 
        Tel: 010-67375667        E-mail: jmhlb@163.com   <br>
       
     ICP备案证书号:<a href="#" tppabs="http://www.miibeian.gov.cn/" target="_blank">京ICP备17033550号-1</a><br>
</p></div>
</div>
<div style=" line-height:22px;">

<br>
        
    
</div>
 <div class="blank"></div>  
 </div>
</c:if>