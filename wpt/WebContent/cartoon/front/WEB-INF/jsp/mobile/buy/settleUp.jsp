<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-付款界面</TITLE>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!--<meta http-equiv="Refresh" content="0; url=/content/node/index.shtml" />-->
            <META name=description content=有宝>
            <META name=keywords content=有宝>
            <LINK rel="Shortcut Icon" href="/favicon.ico">
            <LINK rel=Bookmark href="/favicon.ico">


            <link rel="stylesheet" href="../../../theme/${theme}/css/swiper.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/tjdd.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/alert.css">
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script src='../../../theme/${theme}/js/jquery.qrcode.min.js'></script>
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>


            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
                 var times=20;
                 var timer;
                // IE Evitar seleccion de texto
                document.onselectstart = function() {
                    if (event.srcElement.type != "text" && event.srcElement.type != "textarea" && event.srcElement.type != "password")
                        return false
                    else return true;
                };

                // FIREFOX Evitar seleccion de texto
                if (window.sidebar) {
                    document.onmousedown = function(e) {
                        var obj = e.target;
                        if (obj.tagName.toUpperCase() == "INPUT" || obj.tagName.toUpperCase() == "TEXTAREA" || obj.tagName.toUpperCase() == "PASSWORD")
                            return true;
                        else
                            return false;
                    }
                }

                //  ajax查询支付状态
                function autoQuery(payTransactionId){
                        $.ajax({
                            type:"get",
                            url:"/pay/query/"+payTransactionId+".json",           
                            async:false,
                            success:function(data){
                                times--;
                                if(data.message.operateCode==710010){		
                                    alert(data.message.message);
                                    location.href="/user/purchasedList.shtml?flag=product";
                                    return false;
                                }else if(data.message.operateCode==710013&&times>0){
                                    clearInterval(timer);
                                    setTimeout("autoQuery('"+payTransactionId+"')",5000);
                                }else if(times==0){
                                    clearInterval(timer);
                                }					
                            }
                        })
                }	

                function toUtf8(str) {    
                    var out, i, len, c;    
                    out = "";    
                    len = str.length;    
                    for(i = 0; i < len; i++) {    
                        c = str.charCodeAt(i);    
                        if ((c >= 0x0001) && (c <= 0x007F)) {    
                            out += str.charAt(i);    
                        } else if (c > 0x07FF) {    
                            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));    
                            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));    
                            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));    
                        } else {    
                            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));    
                            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));    
                        }    
                    }    
                    return out;    
                } 
                // End -->
            </script>

        </head>

        <div class="box"></div>
        <body id="top" class="home blog chrome">  
	        <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
                        
            <!--弹出窗口-->
            <div class="hint">
                <div class="hint-in1">
                    <div class="hint1"></div>
                    <div class="hint2">订单提交成功，请您尽快付款！订单号：${payTransactionId}</div>
                    <div class="hint3"></div>
                </div>
                <div class="hint-in2"></div>
                <div class="hint-in3"></div>
            </div>
            
            <div id="main-content">
                <div id="content-header">
                    <div itemscope id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <a  href='/content/newbie/index.shtml'><img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">${node.name}</span></a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">正文</span></div>
                </div>
                <div class="container-fluid">
                    <div class="row-fluid gtop">
                        <div class="span6">
                            <a href="#" title="全站免费教学资源集合" target="_blank" rel="nofollow">
                                <img src="../../../theme/${theme}/images/banner1.png" alt="全站资源索引">
                            </a>
                        </div>
                        <div class="span6">
                            <a href="#" title="新人快快来报道" target="_blank" rel="nofollow">
                                <img src="../../../theme/${theme}/images/banner2.png" alt="新人报道">
                            </a>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span8">
                            <div class="widget-box" style="height:1000px;">
                                <div class="wid-80 martop50" id="scanCode">
                                    <c:choose> 
                                        <c:when test="${!empty message}"> 
                                            <c:if test="${message.operateCode == 102124}">
                                                <script>
                                                        $(function(){
                                                            window.location.href = '${message.message}';
                                                        })
                                                </script>
                                                <!--<script>
                                                        $(function(){
                                                            $(".box").css({"display":"block"});
                                                            $(".hint").css({"display":"block"});
                                                            
                                                            $(".hint-in2").qrcode({  
                                                                            width: 182, //宽度 
                                                                            height:182, //高度 
                                                                            text: toUtf8('addd')
                                                                            });
                                                        
                                                            $(".hint-in3").click(function(event) {
                                                                $(".hint").css({"display":"none"});
                                                                $(".box").css({"display":"none"});
                                                            });
                                                            
                                                            
                                                            $(".hint3").click(function(event) {
                                                                $(this).parent().parent().css({"display":"none"});
                                                                $(".box").css({"display":"none"});
                                                            });
                                                        })
                                                    </script>-->
                                            </c:if>  
                                            <c:if test="${message.operateCode==102167}">
                                                <script>
                                                        $(function(){

                                                            //  ajax查询支付状态
                                                            timer=setInterval("autoQuery('${payTransactionId}')",5e3);


                                                            $(".box").css({"display":"block"});
                                                            $(".hint").css({"display":"block"});
                                                            $('#top-bar').css('position','relative');
                                                            
                                                            var str = '${message.message}';
                                                            $(".hint-in2").qrcode({  
                                                                            width: 182, //宽度 
                                                                            height:182, //高度 
                                                                            text: toUtf8(str)
                                                                            });
                                                        
                                                            // $(".hint-in3").click(function(event) {
                                                            //     $(".hint").css({"display":"none"});
                                                            //     $(".box").css({"display":"none"});
                                                            // });
                                                            
                                                            
                                                            $(".hint3").click(function(event) {
                                                                // $(this).parent().parent().css({"display":"none"});
                                                                // $(".box").css({"display":"none"});
                                                                // $('#top-bar').css('position','fixed');
                                                                history.go(-2);
                                                            });
                                                            
                                                            $(".hint-in3").text('保存图片微信扫描二维码!');
                                                        })
                                                    </script>
                                            </c:if> 
                                            <c:if test="${message.operateCode==102169}">
                                                    <!--<div class="content">
                                                        <div class="box_container_left">
                                                            <p>订单提交成功，请您尽快付款！订单号：${order.cartId}</p>
                                                            <p class="fo-13 martop10">请您在提交订单
                                                            <c:choose>
                                                                <c:when test="${!empty order.ttl}">
                                                                    <c:if test="${order.ttl >= 3600}"><fmt:formatNumber type='number' value='${order.ttl/3600}'  maxFractionDigits="0"></fmt:formatNumber>小时</c:if>
                                                                    <c:if test="${order.ttl < 3600}"><fmt:formatNumber type='number' value='${order.ttl/60}'  maxFractionDigits="0"></fmt:formatNumber>分钟</c:if>
                                                                </c:when>
                                                                    <c:otherwise>24小时</c:otherwise>
                                                                </c:choose>
                                                                内完成支付，否则订单会自动取消。</p>
                                                            
                                                            </div>
                                                            <div class="box_container_right" style="text-align:right"><p>应付金额<span class="orange">￥<fmt:formatNumber value="${order.money.chargeMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>元</span></p></div>
                                                            <div class="box_QRcode  martop10">
                                                            <p class="fo-20 textIn20">支付宝支付</p>
                                                            <p class="textAlignCenter red">二维码已过期，<a href="#" class="blue" onclick="refresh()">刷新</a>页面重新获取二维码</p>
                                                            <p class="textAlignCenter">距离二维码过期还剩<span class="red">40</span>秒，过期后请刷新页面重新获取二维码。</p>
                                                            <p class="textAlignCenter"><div id="code"></div></p>
                                                            <p class="textAlignCenter">请用<span class="orange">支付宝</span>扫一扫</p>
                                                            <p class="textAlignCenter">扫描二维码进行支付</p>
                                                             <p class="textIn20"><a href="#" class="orange" onclick="changeType()">〈 选用支付宝进行支付</a></p> 
                                                            </div>
                                                        </div>
                                                    
                                                    <input type="text" value="${message.content}" id="qrcode_url"/>  -->

                                                    <script>   
                                                           $(function(){
                                                               $('#scanCode').append('<div class="iframeDiv"><iframe src="${message.message}"  frameborder="0"  scrolling="no"  hspace="-100" vspace="-150" style="width:100%;height:500px;"></iframe></div>');
                                                                // $(".box").css({"display":"block"});
                                                                // $(".hint").css({"display":"block"});
                                                                
                                                                // var str = '${message.message}';
                                                                // $(".hint-in2").qrcode({  
                                                                //                 width: 182, //宽度 
                                                                //                 height:182, //高度 
                                                                //                 text: toUtf8(str)
                                                                //                 });
                                                            
                                                                // $(".hint-in3").click(function(event) {
                                                                //     $(".hint").css({"display":"none"});
                                                                //     $(".box").css({"display":"none"});
                                                                // });
                                                                
                                                                
                                                                // $(".hint3").click(function(event) {
                                                                //     $(this).parent().parent().css({"display":"none"});
                                                                //     $(".box").css({"display":"none"});
                                                                // });
                                                                // $(".hint-in3").text('请打开手机支付宝扫码支付!');
                                                           })
                                                    </script>
                                            </c:if>
                                            <c:if test="${message.operateCode==102168}">
                                                <script>
                                                    function onBridgeReady(){
                                                        WeixinJSBridge.invoke(
                                                            'getBrandWCPayRequest', {
                                                                "appId":"${message.objectType}",     //公众号名称，由商户传入     
                                                                "timeStamp":"${message.timestamp}",         //时间戳，自1970年以来的秒数     
                                                                "nonceStr":"${message.messageId}", //随机串     
                                                                "package":"prepay_id=${message.message}",     
                                                                "signType":"MD5",         //微信签名方式：     
                                                                "paySign":"${message.sign}" //微信签名 
                                                            },
                                                            function(res){     
                                                                if(res.err_msg == "get_brand_wcpay_request:ok" ) {}     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
                                                            }
                                                        ); 
                                                    }

                                                    $(function(){
                                                        // var wechatInfo = navigator.userAgent.match(/MicroMessenger\\/([\\d\\.]+)/i);
                                                        // if ( wechatInfo[1] < "5.0" ) {
                                                        //     alert("本活动仅支持微信5.0以上版本") ;
                                                        // }else{
                                                        //     alert('开始执行代码');
                                                            if (typeof WeixinJSBridge == "undefined"){
                                                                if( document.addEventListener ){
                                                                    document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                                                                }else if (document.attachEvent){
                                                                    document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
                                                                    document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                                                                }
                                                            }else{
                                                                onBridgeReady();
                                                            }
                                                        // }
                                                    })
                                                </script>
                                            </c:if>  
                                        </c:when> 
                                        <c:otherwise>   
                                                <script>
                                                    $(function(){
                                                        alert("系统繁忙！");
                                                    })
                                                </script>
                                        </c:otherwise> 
                                    </c:choose>
                                </div>
                            </div>
                        </div>
	                     <%@include file="/WEB-INF/jsp/include/right.jsp" %>
                 </div>
            </div>


	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>