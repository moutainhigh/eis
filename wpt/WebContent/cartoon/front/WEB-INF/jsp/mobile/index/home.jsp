<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}首页</TITLE>
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
                            <a href="#" title="新人快快来报道" target="_blank" rel="nofollow">
                                <img data-lazy-src="../../../theme/${theme}/images/banner2.png" alt="新人报道">
                            </a>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="home-slider" class="widget-box">
                                <div class="slider-content">
                                    <!--首页轮播图-->
                                    <div class="swiper-container">
                                        <div class="swiper-wrapper">
                                            <c:forEach var="item" items="${indexCrouselFigureList}" varStatus="status">
                                                <c:if test="${status.count <= 3}">
                                                    <div class="swiper-slide">
                                                                    <a href="${item.document.viewUrl}">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(item.document.productSmallImage, ',')}">
                                                                        <c:set var="string4" value="${fn:split(item.document.productSmallImage, ',')}" />
                                                                        <c:forEach var="itema" items="${string4}" varStatus="statusa"> 
                                                                            <c:if test="${statusa.count == 1}">   
                                                                                    <img data-lazy-src="/file/${itema}" alt="" style='width:100%;height:100%;'>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                </c:when>
                                                                <c:otherwise>
                                                                        <img data-lazy-src="/file/${item.document.productSmallImage}" alt="" style='width:100%;height:100%;'>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                    </div>   
                                                </c:if> 
                                            </c:forEach>
                                        </div>
                                        <!-- Add Pagination -->
                                        <div class="swiper-pagination"></div>
                                        <!-- Add Arrows -->
                                        <div class="swiper-button-next"></div>
                                        <div class="swiper-button-prev"></div>
                                    </div>



                                    <!-- END REVOLUTION SLIDER -->
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="widget-box">
                                <div class="widget-content">
                                    <ul class="home-icons" style="display: flex;justify-content: space-between;">
                                        <li>
                                            <a target="_blank" href="/content/newbie/index.shtml" title="新手入门"><img data-lazy-src="../../../theme/${theme}/images/newman.png" alt="新手入门">
                                                <p>入门20课</p>
                                            </a>
                                        </li>
                                        <li>
                                            <a target="_blank" href="/content/finger/index.shtml" title="教学资源"><img data-lazy-src="../../../theme/${theme}/images/learn.png" alt="教学资源">
                                                <p>指弹教学</p>
                                            </a>
                                        </li>
                                        <li>
                                            <a target="_blank" href="/content/vip/index.shtml" title="VIP教学" rel="nofollow"><img data-lazy-src="../../../theme/${theme}/images/vipbig.png" alt="VIP教学">
                                                <p>VIP教学</p>
                                            </a>
                                        </li>
                                        <li>
                                            <a target="_blank" href="/content/friend/all/index.shtml" title="琴友作品"><img data-lazy-src="../../../theme/${theme}/images/pino.png" alt="琴友作品">
                                                <p>琴友作品</p>
                                            </a>
                                        </li>
                                        <!--<li>
                                            <a target="_blank" href="#" title="官方淘宝店" rel="nofollow"><img src="../../../theme/${theme}/images/shop.png" alt="官方淘宝店">
                                                <p>官方淘宝店</p>
                                            </a>
                                        </li>-->
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid home-cats">
                        <section class="span12 pic-box">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/finger/index.shtml" style='color:#31a030;'>更多....</a></span>
                                    <span class="iconclock"></span>
                                    <h2>指弹教学</h2>
                                </div>
                                <div class="widget-content">
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty finger}">
                                                <li class="style-row" style="background: #fff;">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${finger}" varStatus="status">
                                                    <c:if test="${status.index < 10}">
                                                        <li class="style-row">
                                                                <a href="${item.document.viewUrl}" class="post-thumbnail" title="链接到  ${item.document.title}" target="_blank">
                                                                    <c:choose>
                                                                        <c:when test="${empty item.document.productSmallImage}">
                                                                            <img  class="lazy" data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img class="lazy" data-lazy-src="/file/${item.document.productSmallImage}"  alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </a>
                                                                <div class="head_box">
                                                                    <c:choose>
                                                                        <c:when test="${empty item.buy_money}"></c:when>
                                                                        <c:when test="${item.buy_money == '0'}"></c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${item.vipFree == 0 || item.vipFree == 1}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/suo.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                            <c:if test="${item.vipFree == 2}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/vip.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                <a class="row-title" href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                            </div>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                            <!-- .cat-box-content /-->
                        </section>
                    </div>
                    <div class="row-fluid home-cats">
                        <section class="span12 pic-box">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <!--<span class="more"><a target="_blank" href="#" style='color:#31a030;'>更多....</a></span>-->
                                    <span class="icon"></span>
                                    <h2>新鲜出炉</h2>
                                </div>
                                <div class="widget-content">
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty indexNewestList}">
                                                <li class="style-row">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${indexNewestList}" varStatus="status">
                                                    <c:if test="${status.index < 10}">
                                                        <li class="style-row">
                                                            <a href="${item.document.viewUrl}" class="post-thumbnail" title="链接到  ${item.document.title}" target="_blank">
                                                                <c:choose>
                                                                    <c:when test="${empty item.document.productSmallImage}">
                                                                        <img  class="lazy" data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img class="lazy" data-lazy-src="/file/${item.document.productSmallImage}"  alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </a>
                                                                <div class="head_box">
                                                                    <c:choose>
                                                                        <c:when test="${empty item.buy_money}"></c:when>
                                                                        <c:when test="${item.buy_money == '0'}"></c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${item.vipFree == 0 || item.vipFree == 1}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/suo.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                            <c:if test="${item.vipFree == 2}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/vip.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <a class="row-title" href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                                </div>
                                                            </li>
                                                    </c:if>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                            <!-- .cat-box-content /-->
                        </section>
                    </div>
                    <div class="row-fluid">
                        <section class="span4 home-recent">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/newbie/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="iconb"></span>
                                    <h2>入门20课</h2>
                                </div>
                                <div class="widget-content">
                                    <ul class="news-list">
                                        <c:choose>
                                            <c:when test="${empty newbie}">
                                                <li>
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${newbie}" varStatus="status">
                                                    <li>
                                                        <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.document.readCount}</span>
                                                        <a href="${item.document.viewUrl}" title="${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                    </li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                                <!-- .widget-content /-->
                            </div>
                        </section>


                        <section class="span4 home-recent">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/study/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="icone"></span>
                                    <h2>学习文章</h2>
                                </div>
                                <div class="widget-content">
                                    <ul class="news-list">
                                        <c:choose>
                                            <c:when test="${empty study}">
                                                <li>
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${study}" varStatus="status">
                                                    <li>
                                                        <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.document.readCount}</span>
                                                        <a href="${item.document.viewUrl}" title="${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                    </li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                                <!-- .widget-content /-->
                            </div>
                        </section>

                        <section class="span4 home-recent">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/highfinger/song/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="iconc"></span>
                                    <h2>指弹提高</h2>
                                </div>
                                <div class="widget-content">
                                    <ul class="news-list">
                                        <c:choose>
                                            <c:when test="${empty song}">
                                                <li>
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${sing}" varStatus="status">
                                                    <li>
                                                        <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.document.readCount}</span>
                                                        <a href="${item.document.viewUrl}" title="${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                    </li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                                <!-- .widget-content /-->
                            </div>
                        </section>
                    </div>
                    <div class="row-fluid">
                        <section class="span12 pic-box">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/vip/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="icong"></span>
                                    <h2>付费曲谱教学</h2>
                                </div>
                                <div class="widget-content">
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty vip}">
                                                <li class="style-row">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${vip}" varStatus="status">
                                                    <c:if test="${status.index < 10}">
                                                        <li class="style-row">
                                                            <a href="${item.document.viewUrl}" class="post-thumbnail" title="链接到  ${item.document.title}" target="_blank">
                                                                <c:choose>
                                                                    <c:when test="${empty item.document.productSmallImage}">
                                                                        <img  class="lazy" data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img class="lazy" data-lazy-src="/file/${item.document.productSmallImage}" alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </a>
                                                            <div class="head_box">
                                                                <c:choose>
                                                                        <c:when test="${empty item.buy_money}"></c:when>
                                                                        <c:when test="${item.buy_money == '0'}"></c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${item.vipFree == 0 || item.vipFree == 1}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/suo.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                            <c:if test="${item.vipFree == 2}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/vip.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                <a class="row-title" href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                            </div>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="row-fluid">
                        <section class="span12 pic-box">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/onlyvip/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="iconh"></span>
                                    <h2>vip专区</h2>
                                </div>
                                <div class="widget-content">
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty onlyvip}">
                                                <li class="style-row">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${onlyvip}" varStatus="status">
                                                    <c:if test="${status.index < 10}">
                                                        <li class="style-row">
                                                            <a href="${item.document.viewUrl}" class="post-thumbnail" title="链接到  ${item.document.title}" target="_blank">
                                                                <c:choose>
                                                                    <c:when test="${empty item.document.productSmallImage}">
                                                                        <img  class="lazy" data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img class="lazy" data-lazy-src="/file/${item.document.productSmallImage}" alt="${item.document.title}" style="width:100%;height: 145px;" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </a>
                                                            <div class="head_box">
                                                                <c:choose>
                                                                        <c:when test="${empty item.buy_money}"></c:when>
                                                                        <c:when test="${item.buy_money == '0'}"></c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${item.vipFree == 0 || item.vipFree == 1}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/suo.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                            <c:if test="${item.vipFree == 2}">
                                                                                <img data-lazy-src="../../../theme/${theme}/images/vip.png" alt="" style="width: 12px;float: left;">
                                                                            </c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                <a class="row-title" href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank" >${item.document.title}</a>
                                                            </div>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="row-fluid">
                        <div class="row-fluid">
                            <section class="span12 scroll-box">
                                <div class="widget-box">
                                    <div class="widget-title"> <span class="iconi"></span>
                                        <h2>官方旗舰店—热销商品</h2>
                                    </div>
                                    <div class="widget-content pic-list">
                                        <div class="swiper-containera" style='height:300px;'>
                                            <div class="swiper-wrapper">
                                                <c:forEach var="item" items="${indexHotSaleList}" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${empty item.document.productGallery}">    
                                                                <div class="swiper-slide">
                                                                    <a href="/">
                                                                        <img data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="" style="width:100%;height:100%;" />
                                                                    </a>
                                                                </div>
                                                            </c:when>
                                                            <c:when test="${fn:contains(item.document.productGallery, ',')}">
                                                                    <c:set var="string4" value="${fn:split(item.document.productGallery, ',')}" />
                                                                    <c:forEach var="itema" items="${string4}" varStatus="statusa">
                                                                        <c:if test="${status.count == 1}">
                                                                            <div class="swiper-slide">
                                                                                <a href="/">
                                                                                    <img data-lazy-src="/file/${itema}" alt="" style="width:100%;height:100%;" />
                                                                                </a>
                                                                            </div>
                                                                        </c:if>
                                                                    </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                    <div class="swiper-slide">
                                                                        <a href="/">
                                                                            <img data-lazy-src="/file/${item.document.productGallery}" alt="" style='width:100%;height:100%;'>
                                                                        </a>
                                                                    </div> 
                                                            </c:otherwise>
                                                        </c:choose>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </div>
                        <div class="clear"></div>

                        <section class="span4 column2" style="margin-left: 0;">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/friend/sing/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="iconk"></span>
                                    <h2><a href="/content/friend/sing/index.shtml" target="_blank">琴友弹唱推荐</a></h2>
                                </div>
                                <div class="widget-content" style='padding:10px;'>
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty sing}">
                                                <li class="other-posts">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${sing}" varStatus="status">
                                                    <li class="other-posts">
                                                        <a class="post-thumbnail" href="#" title="" target="_blank">
                                                            <c:choose>
                                                                <c:when test="${empty item.userHeadPic}">
                                                                    <img data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="" style="width:45px;height:45px;" class="avatar">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img data-lazy-src="/file/client/${item.userHeadPic}" alt="" style="width:45px;height:45px;" class="avatar">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                        <h3><a href="${item.document.viewUrl}" title="链接到  ${item.title}" rel="bookmark" target="_blank">${item.document.title}</a></h3>
                                                        <p class="post-meta">
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="" target="_blank">
                                                            <c:choose>
                                                                <c:when test="${empty item.userName}">
                                                                    七星乐器
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${item.userName}
                                                                </c:otherwise>
                                                            </c:choose>    
                                                            </a></span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.document.readCount}</span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/pao.png" alt="" class='pao'><a href="${item.document.viewUrl}#comments">${item.document.commentCount}</a></span>
                                                        </p>
                                                        <div class="clear"></div>
                                                    </li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                                <!-- .cat-box-content /-->
                            </div>
                        </section>
                        <!-- Three Columns -->
                        <section class="span4 column2">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/friend/thrum/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="iconk"></span>
                                    <h2><a href="/content/friend/thrum/index.shtml" target="_blank">琴友指弹推荐</a></h2>
                                </div>
                                <div class="widget-content" style='padding:10px;'>
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty thrum}">
                                                <li class="other-posts">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${thrum}" varStatus="status">
                                                    <li class="other-posts">
                                                        <a class="post-thumbnail" href="#" title="" target="_blank">
                                                            <c:choose>
                                                                <c:when test="${empty item.userHeadPic}">
                                                                    <img data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="" style="width:45px;height:45px;" class="avatar">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img data-lazy-src="/file/client/${item.userHeadPic}" alt="" style="width:45px;height:45px;" class="avatar">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                        <h3><a href="${item.document.viewUrl}" title="链接到  ${item.title}" rel="bookmark" target="_blank">${item.document.title}</a></h3>
                                                        <p class="post-meta">
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="" target="_blank">
                                                            <c:choose>
                                                                <c:when test="${empty item.userName}">
                                                                    七星乐器
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${item.userName}
                                                                </c:otherwise>
                                                            </c:choose>    
                                                            </a></span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.document.readCount}</span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/pao.png" alt="" class='pao'><a href="${item.document.viewUrl}#comments">${item.document.commentCount}</a></span>
                                                        </p>
                                                        <div class="clear"></div>
                                                    </li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                                <!-- .cat-box-content /-->
                            </div>
                        </section>
                        <!-- Three Columns -->
                        <section class="span4 column2">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <span class="more"><a target="_blank" href="/content/friend/original/index.shtml" style='color:#31a030;'>更多...</a></span>
                                    <span class="iconk"></span>
                                    <h2><a href="/content/friend/original/index.shtml" target="_blank">琴友原创推荐</a></h2>
                                </div>
                                <div class="widget-content" style='padding:10px;'>
                                    <ul>
                                        <c:choose>
                                            <c:when test="${empty original}">
                                                <li class="other-posts">
                                                    暂时没有内容！
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${original}" varStatus="status">
                                                    <li class="other-posts">
                                                        <a class="post-thumbnail" href="#" title="" target="_blank">
                                                            <c:choose>
                                                                <c:when test="${empty item.userHeadPic}">
                                                                    <img data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="" style="width:45px;height:45px;" class="avatar">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img data-lazy-src="/file/client/${item.userHeadPic}" alt="" style="width:45px;height:45px;" class="avatar">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                        <h3><a href="${item.document.viewUrl}" title="链接到  ${item.title}" rel="bookmark" target="_blank">${item.document.title}</a></h3>
                                                        <p class="post-meta">
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="" target="_blank">
                                                            <c:choose>
                                                                <c:when test="${empty item.userName}">
                                                                    七星乐器
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${item.userName}
                                                                </c:otherwise>
                                                            </c:choose>    
                                                            </a></span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.document.readCount}</span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/pao.png" alt="" class='pao'><a href="${item.document.viewUrl}#comments">${item.document.commentCount}</a></span>
                                                        </p>
                                                        <div class="clear"></div>
                                                    </li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                                <!-- .cat-box-content /-->
                            </div>
                        </section>
                        <!-- Three Columns -->
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
           
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
        </body>

        </html>