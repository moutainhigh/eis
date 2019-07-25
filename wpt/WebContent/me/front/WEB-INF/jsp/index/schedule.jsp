<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}课程表</TITLE>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!--<meta http-equiv="Refresh" content="0; url=/content/youbaoshangcheng/index.shtml" />-->
            <META name=description content=有宝>
            <META name=keywords content=有宝>
            <link rel="shortcut icon" href="/favicon.ico"/>
            <link rel="bookmark" href="/favicon.ico"/>


            <link rel="stylesheet" href="../../../theme/${theme}/css/swiper.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <!--<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" charset="utf-8" data-callback="true"></script>-->
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>

            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
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
                    <div id="top-announce"><img data-lazy-src="../../../theme/${theme}/images/tongzhi.png" alt="" class='tongzhi'>全网教学点击500万次，本网站所有曲谱制作，教学制作，由七星老师独立完成，专注指弹教学，持续更新，有问题联系网站右侧老师或加QQ：3268551923！</div>
                </div>
                <div class="container-fluid home-fluid">
                    <div class="row-fluid gtop">
                        <div class="span6">
                            <a href="#" title="全站免费教学资源集合" target="_blank" rel="nofollow">
                                <img data-lazy-src="../../../theme/${theme}/images/banner1.png" alt="全站资源索引">
                            </a>
                        </div>
                        <div class="span6">
                            <a href="/user/purchasedList.shtml?flag=vip" title="VIP订购" target="_blank" rel="nofollow">
                                <img data-lazy-src="../../../theme/${theme}/images/banner2.gif" alt="VIP订购">
                            </a>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            
                        </div>
                    </div>
       
                </div>
            </div>
	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
        
            <!-- Swiper JS -->
            <script src='../../../theme/${theme}/js/EasyLazyload.min.js'></script>
            <script src="../../../theme/${theme}/js/swiper.js"></script>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>

            <!-- Initialize Swiper -->
            <script>
                $(function(){
                    var swiper = new Swiper('.swiper-container', {
                        pagination: '.swiper-pagination',
                        nextButton: '.swiper-button-next',
                        prevButton: '.swiper-button-prev',
                        paginationClickable: true,
                        spaceBetween: 30,
                        centeredSlides: true,
                        autoplay: 4000,
                        autoplayDisableOnInteraction: false
                    });

                    var swipera = new Swiper('.swiper-containera', {
                        // pagination: '.swiper-pagination',
                        // nextButton: '.swiper-button-next',
                        // prevButton: '.swiper-button-prev',
                        paginationClickable: true,
                        spaceBetween: 30,
                        centeredSlides: true,
                        autoplay: 4000,
                        autoplayDisableOnInteraction: false
                    });

                    // 图片懒加载   
                    lazyLoadInit();
                })
            </script>

            <script>
                var right_1 = 1,
                    right_2 = 2;
            </script>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
        </body>

        </html>