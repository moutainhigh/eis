<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE>${systemName}</TITLE>
<meta http-equiv="x-ua-compatible" content="ie=7">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name=description 
content=炒卡网主营游戏充值,话费充值,影音娱乐,杀毒软件,软件序列号，在线教育，影票兑换码等产品。，高效快捷，实时充值,安全放心。7*24小时为您提供全面的支付服务。>
<META name=keywords 
content=炒卡网,jcard,炒卡网充值,炒卡网官网,充值,官网,qq卡,q币充值中心,qq充值中心,qq蓝钻,蓝钻豪华版,q币查询,点卡充值,游戏点卡,魔兽世界点卡,魔兽世界,大话西游3,剑网三,九阴真经,神仙传,神仙道,人人豆,>
<LINK rel="Shortcut Icon" href="/favicon.ico">
<LINK rel=Bookmark href="/favicon.ico">

<LINK rel=Stylesheet type=text/css href="/style/UCardTop.css">
<LINK rel=Stylesheet type=text/css href="/style/CarouselAD.css">

<LINK rel=stylesheet type=text/css href="/style/Web.css">
<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<LINK rel=stylesheet type=text/css href="/style/list.css">
<LINK rel=stylesheet type=text/css href="/style/Footer.css">
<title>炒卡网充值 | ${systemName }</title>
</head>

<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>

<script  src="/js/ui/pay.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/commonui.js"></script>
<body>

<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
<div id="mainbody">
    	    <div class="leftbar" style="height:auto">
	    	    <%@include file="/WEB-INF/jsp/include/common_login.jsp"%>
	  <%@include file="/WEB-INF/jsp/include/common_reinfo.jsp"%>
           
			
        </div>		
        <div class="rightbar" >
        
            	
                <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 选择充值方式</span></div>
                    
               
                <div class="paybody">
                		<div class="paywelcome">${product.productName }</div>
                         <div class="clear"></div>
                
				<div class="paymain">
				  <div id="ajaxInfo" name="ajaxInfo" style="text-align:center;"> </div>
   <div id="pic">
             <ul>
             <c:forEach var="payType" items="${payTypeList}">
                 <li><a href="/${contentPrefix}pay/${payType.payTypeId}">
                     <img src="${payType.logoUrl}" width="106" height="44" border="0"/>${payType.name}</a></li>
                  </c:forEach>   
                
             </ul>
         </div>

					
				</div>  	
					
				
       
            
          
            
               </div>
    <div class="clear"></div>
    </div>
        <div class="clear"></div>
         </div>
        
 <%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body></html>