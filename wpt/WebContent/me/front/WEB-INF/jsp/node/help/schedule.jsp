<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}课程表111</TITLE>
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
                            <ul>
                                <li><a href="">免费开放指弹曲谱/教学（更新与2018年3月8日）</a></li>
                                <li><a href="">岑宁儿《追光者》指弹演奏曲谱</a></li>
                                <li><a href="">Alan Walker《Faded》指弹演奏曲谱</a></li>
                                <li><a href="">《成都》精简版指弹演奏曲谱</a></li>
                                <li><a href="">经典《卡农》高清指弹吉他曲谱/【教学视频】</a></li>
                                <li><a href="">《River Flows In You》指弹演奏曲谱/【教学视频】</a></li>
                                <li><a href="">《千与千寻》指弹演奏曲谱/【教学视频】</a></li>
                                <li><a href="">岸部真明《奇迹的山》指弹演奏曲谱/【教学视频】</a></li>
                                <li><a href="">《神秘园之歌》指弹演奏曲谱/【教学视频】</a></li>
                                <li><a href="">久石让《天空之城》指弹演奏曲谱/【教学视频】</a></li>
                                <li><a href="">七星编配制作付费指弹曲谱/教学（付费后可查看曲谱或教学，升级VIP会员可免费学习</a></li>
                                <li><a href="">《小幸运》指弹演奏曲高清曲谱/【曲谱价格：6元】共3页</a></li>
                                <li><a href="">《你离开的真相》指弹演奏高清曲谱/【曲谱价格：5元】共4页</a></li>
                                <li><a href="">《I Believe》指弹演奏曲免费曲谱/【付费高清教学视频：20元】总2个课程，时长：19分钟</a></li>
                                <li><a href="">《梦中的婚礼》指弹演奏免费教学/【高清曲谱价格：5元】共2页</a></li>
                                <li><a href="">《See You Again》指弹演奏免费教学/【高清曲谱价格：5元】共2页</a></li>
                                <li><a href="">《Kiss The Rain》指弹演奏免费教学/【高清曲谱价格：5元】共2页</a></li>
                                <li><a href="">周杰伦《烟花易冷》指弹精简版教学视频/【高清曲谱价格：5元】共3页</a></li>
                                <li><a href="">Beyond《长城》指弹演奏曲/【高清曲谱价格：3元】共2页</a></li>
                                <li><a href="">VIP会员专享曲谱/课程专区(仅限VIP会员观看)</a></li>
                                <li><a href="">《神话》无死角指弹教学视频【VIP课程】/总3课时、教学时长42分钟</a></li>
                                <li><a href="">《神话》指弹演奏高清曲谱【VIP曲谱】总2页</a></li>
                                <li><a href="">朴树《平凡之路》无死角指弹教学视频【VIP课程】/总3课时、教学时长40分钟</a></li>
                                <li><a href="">朴树《平凡之路》高清曲谱【VIP曲谱】总2页</a></li>
                                <li><a href="">《未闻花名》指弹演奏高清曲谱【VIP曲谱】总3页</a></li>
                                <li><a href="">伍伍慧《等待的风》无死角指弹教学视频【VIP课程】/总4课时/教学时长40分钟</a></li>
                                <li><a href="">伍伍慧《等待的风》指弹演奏高清曲谱【VIP曲谱】总4页</a></li>
                                
                            </ul>
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