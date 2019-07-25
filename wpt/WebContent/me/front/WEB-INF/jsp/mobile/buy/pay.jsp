<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-${node.name}</TITLE>
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
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="1106128779" data-redirecturi="http://me.mo4u.cn" charset="utf-8"></script>
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>


            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
                 var timer;
                 $(function(){   
                      var title = '${node.name}';
                      if(title.indexOf('首页')>-1){
                        $('#header li').eq(0).addClass('current-menu-item');               
                      }else if(title.indexOf('入门自学20课')>-1){
                        $('#header li').eq(2).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹教学')>-1||title.indexOf('初级指弹曲')>-1||title.indexOf('中级指弹曲')>-1||title.indexOf('高级指弹曲')>-1){
                        $('#header li').eq(3).addClass('current-menu-item');               
                      }else if(title.indexOf('VIP曲谱*教学')>-1){
                        $('#header li').eq(8).addClass('current-menu-item');               
                      }else if(title.indexOf('歌曲前奏')>-1 ||title.indexOf('Riff练习')>-1 || title.indexOf('双吉他教学')>-1 ){
                        $('#header li').eq(9).addClass('current-menu-item');               
                      }else if(title.indexOf('学习文章')>-1){
                        $('#header li').eq(13).addClass('current-menu-item');               
                      }else if(title.indexOf('金伟')>-1 || title.indexOf('杨猛')>-1 ){
                        $('#header li').eq(14).addClass('current-menu-item');               
                      }else if(title.indexOf('所有作品')>-1 || title.indexOf('我要上传')>-1 ||title.indexOf('弹唱作品')>-1||title.indexOf('指弹作品')>-1||title.indexOf('原创作品')>-1){
                        $('#header li').eq(17).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹铃音')>-1){
                        $('#header li').eq(23).addClass('current-menu-item');               
                      }else if(title.indexOf('在线调弦')>-1){
                        $('#header li').eq(24).addClass('current-menu-item');               
                      }else if(title.indexOf('帮助中心')>-1){
                        $('#header li').eq(25).addClass('current-menu-item');               
                      }

                    //   ajax查询支付状态
                      timer=setInterval("autoQuery('${payTransactionId}')",3e3);
                 })
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
                // End -->
            </script>

        </head>

        <body id="top" class="home blog chrome">  
	        <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
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
                            <div class="widget-box">
                                <div class="wid-80 martop50">
                                        <c:choose> 
                                            <c:when test="${!empty message}">   
                                                <c:if test="${message.operateCode==102167}">
                                                <div class="content">
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
                                                        <p class="fo-20 textIn20">微信支付</p>
                                                        <!--<p class="textAlignCenter red">二维码已过期，<a href="#" class="blue" onclick="refresh()">刷新</a>页面重新获取二维码</p>
                                                        <p class="textAlignCenter">距离二维码过期还剩<span class="red">40</span>秒，过期后请刷新页面重新获取二维码。</p>--> 
                                                        <p class="textAlignCenter"><div id="code"></div></p>
                                                        <p class="textAlignCenter">请用<span class="orange">微信</span>扫一扫</p>
                                                        <p class="textAlignCenter">扫描二维码进行支付</p>
                                                        <!-- <p class="textIn20"><a href="#" class="orange" onclick="changeType()">〈 选用支付宝进行支付</a></p> -->
                                                        </div>
                                                    </div>
                                                    ${message.content}
                                                    <input type="hidden" value="${message.content}" id="qrcode_url"/>
                                                    <script>
                                                            $("#code").qrcode({      
                                                
                                                                            width: 182, //宽度 
                                                                            height:182, //高度 
                                                                            text: "${message.content}"
                                                                            });
                                                        
                                                    </script>
                                                </c:if> 
                                                <c:if test="${message.operateCode==102169}">
                                                    <div class="content">
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
                                                            <!--<p class="textAlignCenter red">二维码已过期，<a href="#" class="blue" onclick="refresh()">刷新</a>页面重新获取二维码</p>
                                                            <p class="textAlignCenter">距离二维码过期还剩<span class="red">40</span>秒，过期后请刷新页面重新获取二维码。</p>-->
                                                            <p class="textAlignCenter"><div id="code"></div></p>
                                                            <p class="textAlignCenter">请用<span class="orange">支付宝</span>扫一扫</p>
                                                            <p class="textAlignCenter">扫描二维码进行支付</p>
                                                            <!-- <p class="textIn20"><a href="#" class="orange" onclick="changeType()">〈 选用支付宝进行支付</a></p> -->
                                                            </div>
                                                        </div>
                                                    
                                                    <input type="text" value="${message.content}" id="qrcode_url"/>  

                                                    <script>   
                                                            alert("${message.message}");
                                                            var str="${message.message}";
                                                            $("#code").qrcode({       
                                                                            width: 182, //宽度 
                                                                            height:182, //高度 
                                                                            text: toUtf8(str)
                                                                            });
                                                            
                                                    </script>
                                            </c:if> 
                                            <c:if test="${message.operateCode!=102167&&message.operateCode!=102169}">
                                                <script>
                                                //alert("${message.message}"+"");
                                                alert(${message.operateCode})
                                                if(${message.operateCode==710010}){
                                                    location.href="/content/notice/paySuccess.shtml" ;
                                                }
                                                </script>
                                            </c:if> 
                                        </c:when> 
                                        <c:otherwise>   
                                                <script>
                                                alert("系统繁忙！");
                                                </script>
                                        </c:otherwise> 
                                    </c:choose>
                                </div>
                        </div>


	                     <%@include file="/WEB-INF/jsp/include/right.jsp" %>
                    </div>
                 </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>