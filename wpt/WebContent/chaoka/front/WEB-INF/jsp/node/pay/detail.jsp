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
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>
<script  src="/js/ui/pay2.js"></script>
<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">

<script  src="/js/ui/commonui.js"></script>
</head>

<body>

<%@include file="/WEB-INF/jsp/include/common_header_zf.jsp"%>
<div id="mainbody">
    	    <div class="leftbar" style="height:auto">
	    	    
	  <%@include file="/WEB-INF/jsp/include/common_czfs.jsp"%>
           
			
        </div>		
        <div class="rightbar" >
        
            	
                <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 您当前选择的是<div style="color:#ff6600; display:inline">"${payType.name}"</div>的充值方式</span></div>
                    
               
                <div class="paybody">
                		<div class="paywelcome">${product.productName }</div>
                         <div class="clear"></div>
                
				<div class="paymain">
				  <div id="ajaxInfo" name="ajaxInfo" style="text-align:center;"> </div>
                  <form onsubmit="return false" method="POST" action="/pay.json" id="chargeForm" name="chargeForm">
                  			<input type="hidden" value="${payType.payTypeId }" id="payTypeId" name="payTypeId">
                  	<div name="selectAmount" id="selectAmount" style=" margin: 50px; width: 570px; line-height: 40px; display: none;">
                    </div>
                    <div style="margin-top: 40px; display: none;" id="accountlist"><span style="background-color:#fff;margin-left: 20px;">请输入充值金额:</span><span style="text-align:left; text-indent:22px; height:25px; line-height:25px; width:300px;"><input value="0" name="total_fee" id="total_fee"  class="inptu_cz">元</span></div>
                    <div style="margin-left: 20px; width: 670px; display: none; " id="banklist">
                    
                    <div style="background-color:#fff;float:left;height:40px;line-height:40px">请选择充值银行:</div><div style="text-align:left;float:left; line-height:40px; margin:10px 10px; ">
								<input type="radio" value="CMB" name="defaultbank" id="CMB" class="bank"><img src="/images/bank/CMB_OUT.gif">
								<input type="radio" value="ICBC" name="defaultbank" id="ICBC" class="bank"><img src="/images/bank/ICBC_OUT.gif">
								<input type="radio" value="CCB" name="defaultbank" id="CCB" class="bank"><img src="/images/bank/CCB_OUT.gif">
								<input type="radio" value="BOC" name="defaultbank" id="BOC" class="bank"><img src="/images/bank/BOC_OUT.gif">
								<br>
								<input type="radio" value="ABC" name="defaultbank" id="ABC" class="bank"><img src="/images/bank/ABC_OUT.gif">
								<input type="radio" value="BCOM" name="defaultbank" id="BCOM" class="bank"><img src="/images/bank/COMM_OUT.gif">
								<input type="radio" value="SPDB" name="defaultbank" id="SPDB" class="bank"><img src="/images/bank/SPDB_OUT.gif">
								<input type="radio" value="GDB" name="defaultbank" id="GDB" class="bank"><img src="/images/bank/GDB_OUT.gif">
								<br>
								<input type="radio" value="CITIC" name="defaultbank" id="CITIC" class="bank"><img src="/images/bank/CITIC_OUT.gif">
								<input type="radio" value="CEB" name="defaultbank" id="CEB" class="bank"><img src="/images/bank/CEB_OUT.gif">
								<input type="radio" value="CIB" name="defaultbank" id="CIB" class="bank"><img src="/images/bank/CIB_OUT.gif">
								<input type="radio" value="SDB" name="defaultbank" id="SDB" class="bank"><img src="/images/bank/SDB_OUT.gif">
								<br>
								<input type="radio" value="CMBC" name="defaultbank" id="CMBC" class="bank"><img src="/images/bank/CMBC_OUT.gif">
								<input type="radio" value="HZB" name="defaultbank" id="HZB" class="bank"><img src="/images/bank/HZCB_OUT.gif">
								<input type="radio" value="SHB" name="defaultbank" id="SHB" class="bank"><img src="/images/bank/SHBANK_OUT.gif">
								<input type="radio" value="NBCB" name="defaultbank" id="NBCB" class="bank"><img src="/images/bank/NBBANK_OUT.gif">
								<br>
								<input type="radio" value="PAB" name="defaultbank" id="PAB" class="bank"><img src="/images/bank/SPABANK_OUT.gif">
								<input type="radio" value="BOB" name="defaultbank" id="BOB" class="bank"><img src="/images/bank/BJBANK_OUT.gif">
								<input type="radio" value="BJRCB" name="defaultbank" id="BJRCB" class="bank"><img src="/images/bank/BJRCB_OUT.gif">
									<input type="radio" value="PSBC" name="defaultbank" id="PSBC" class="bank"><img src="/images/bank/PSBC_OUT.gif">
								<br></div>
							<div class="clear"></div>
								</div>
			<p style="text-align:center; margin-top:20px;"><input type="submit" value="确认充值" id="submit" style="background:url(/style/bottonbg.gif) no-repeat;
	text-align:center;
	width:74px;
	height:25px;font-family:'微软雅黑', Verdana, sans-serif;
	line-height:27px; font-weight:bold; color:#FFFFFF;
	border:0px"></p>	
            
            
   </form>
<INPUT type="hidden" name="inputType"  id="inputType" value="${payType.inputType }" />
<INPUT type="hidden" name="validAmount" id="validAmount"  value="${payType.validAmount }" />		
<INPUT type="hidden" name="cardSerialnumberLength"  id="cardSerialnumberLength" value="${payType.cardSerialnumberLength }" />		
<INPUT type="hidden" name="cardPasswordLength" id="cardPasswordLength"  value="${payType.cardPasswordLength}" />					
				</div>  	
					
				
       
            
          
            
               </div>
    <div class="clear"></div>
    </div>
        <div class="clear"></div>
         </div>
        
 <%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body></html>