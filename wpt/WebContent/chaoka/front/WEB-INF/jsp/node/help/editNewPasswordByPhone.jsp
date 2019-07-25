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
<LINK rel=stylesheet type=text/css href="/style/OldUcard.css">
<LINK rel=stylesheet type=text/css href="/style/Web.css">
<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<LINK rel=stylesheet type=text/css href="/style/list.css">
<LINK rel=stylesheet type=text/css href="/style/Footer.css">
<title>用户注册 | ${systemName }</title>
</head>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">
<script  src="/js/ui/commonui.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>

<script  src="/js/ui/editnewpasswordbyphone.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>


<body>

<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
<div id="mainbody">
    	    <div class="leftbar">
	    	  
                <%@include file="/WEB-INF/jsp/include/common_reinfo.jsp"%>
	  
           
			
        </div>		
        <div class="rightbar">
        
            	
                <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 输入新密码</span></div>
                    
               
                <div class="forgetbody" style="display:none">
                		<!--<div class="welcomes">欢迎注册${systemName}，请在下面输入您的信息</div>-->
                         <div class="clear"></div>
 <div class="step">
            <ul>
               <li class="one">填写账户名并验证身份</li>
               <li class="cur">设置新密码</li>
               <li class="four">完成</li>
             
            </ul>
        </div>
   
</ul><div class="clear"></div>
				<div class="forgetf">
				
                  
				<form name="findnewpForm" id="findnewpForm" action="" onSubmit="return false;" method="POST" >
			
					<p ><span class="txt">设置新密码：</span> <span class="forms"><input type="password"  name="newPassword" id="newPassword"  size="40" /></span><span class="regok"></span></p>
					
					<p ><span class="txt">重复新密码：</span> <span class="forms"><input type="password"  name="newPassword2" id="newPassword2"  size="40"/></span><span class="regok"></span></p>
                    
                  <input type="hidden"  name="uuid" id="uuid" />
                  <input type="hidden"  name="sign" id="sign"/>
                
					
				
					<p style="text-align:center;margin:30px 150px;">
                    <input  type="submit" id="saveemail" value="保存设置" />
                </p>
					</form>
				</div>  	
					
				
       
            
          
            
               </div>
                <div  id="jieguop" style="font-size:18px; display:none"></div>              
    <div class="clear"></div>
    </div>
        <div class="clear"></div>
         </div>
        
 <%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body></html>