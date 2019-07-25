<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-${pageTitle}</TITLE>
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
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
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
                    <div itemscope itemtype="http://schema.org/WebPage" id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">琴友作品</span>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">原创作品</span></div>
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
                        <header class="archive-head jieshao">
                            <h1 itemprop="headline" class='title-ro' style='padding: 20px 0px;'>课程概述
                                <a class="rss-cat-icon" title="订阅这个分类" href="#">
                                    <p class="icon-rss"></p>
                                </a>
                            </h1>

                            <p style='    padding: 15px 15px 15px 28px;'>${node.desc}</p>
                        </header>
                        <section class="span12">
                            <div class="widget-box" role="main" itemscope style='border-bottom: 0;'>
                                <header id="post-header">
                                    <h1 itemprop="headline">
                                        原创作品
                                    </h1>
                                </header>
                                <div class="widget-content">

                                    <!--<ul class="post-sort">
                                        <li class="sort-item "><a href="#" rel="nofollow">按人气排序</a></li>
                                        <li class="sort-item "><a href="#" rel="nofollow">男生</a></li>
                                        <li class="sort-item "><a href="#" rel="nofollow">女生</a></li>
                                        <li class="sort-item "><a href="#" rel="nofollow">组合</a></li>
                                        <li class="sort-item "><a href="#" rel="nofollow">乐队</a></li>
                                        <li class="sort-item "><a href="#" rel="nofollow">优秀作品</a></li>
                                    </ul>-->
                                    <div class="clear"></div>
                                    <div class="comments-loading"><img data-lazy-src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;"> 列表正在加载 ...</div>
                                    <ul class="commentlists">
                                        <c:forEach var="item" items="${newsList}" varStatus="status">
                                            <li class="video-thumb">
                                                <article>
                                                    <a class="pic" target="_blank" href="${item.viewUrl}" title="链接到  ${item.title}" rel="bookmark">
                                                        <img class="lazy" data-lazy-src="${item.documentDataMap.productSmallImage.dataValue}" alt="#" style='width: 100%;height: 125px;' />
                                                    </a>
                                                    <div class="video-info">
                                                        <h2><a target="_blank" href="${item.viewUrl}" title="链接到  ${item.title}" rel="bookmark">${item.title}</a></h2>
                                                        <p>琴友：<a target="_blank" href="#" title="">${item.author}</a></p>
                                                        <p><fmt:formatDate value="${item.publishTime}" type="both"/></p>
                                                        <p class="post-meta">
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" style='width:11px;height: 11px;padding-right: 5px;'>
                                                                <c:choose>
                                                                    <c:when test="${empty item.documentDataMap.readCount}">
                                                                        0
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${item.documentDataMap.readCount.dataValue}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/zan.png" alt="" style='width: 14px;height:14px;padding-right: 5px;'>
                                                                <c:choose>
                                                                    <c:when test="${empty item.documentDataMap.praiseCount}">
                                                                        0
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${item.documentDataMap.praiseCount.dataValue}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/pao.png" alt="" style='width:11px;height: 11px;padding-right: 5px;'><a href="#">
                                                                <c:choose>
                                                                    <c:when test="${empty item.documentDataMap.commentCount}">
                                                                        0
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${item.documentDataMap.commentCount.dataValue}
                                                                    </c:otherwise>
                                                                </c:choose></a>
                                                            </span>
                                                        </p>
                                                    </div>
                                                    <div class="clear"></div>
                                                </article>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <div class="page-nav comment-nav">
                                        <!--<span class="pages" style='margin-left:15px;'>1 of 35</span><span class="current">1</span><a href="#" class="page" title="2">2</a><a href="#" class="page" title="3">3</a><a href="#" class="page" title="4">4</a>
                                        <a href="#" class="page" title="5">5</a>
                                        <a href="#">&raquo;</a>
                                        <a href="#" class="page" title="10">10</a><a href="#" class="page" title="20">20</a><span class="extend">...</span>
                                        <a href="#" class="last" title="尾页 &raquo;">尾页 &raquo;</a>-->
                                        <c:if test="${totalPages == 1}">
                                            <a href="#" class='page-numbers current' style='margin-left:15px;' onclick="goPage('1')">1</a>
                                            <a href="#" class="prev page-numbers"  onclick="nextPage()">下一页  &raquo;</a>
                                        </c:if>
                                        <c:if test="${totalPages == 2}">
                                            <div style='margin-left:15px;'>
                                                <a class='page-numbers current' onclick="goPage(1)" href="#list">1</a>
                                                <a class='page-numbers' onclick="goPage(2)" href="#list">2</a>
                                                <a class="prev page-numbers"  onclick="nextPage()" href="#list">下一页  &raquo;</a>
                                            </div>
                                        </c:if>
                                        <c:if test="${totalPages == 3}">
                                            <div style='margin-left:15px;'>
                                                <a class='page-numbers current' onclick="goPage(1)" href="#list">1</a>
                                                <a class='page-numbers' onclick="goPage(2)" href="#list">2</a>
                                                <a class='page-numbers' onclick="goPage(3)" href="#list">3</a>
                                                <a class="prev page-numbers"  onclick="nextPage()" href="#list">下一页  &raquo;</a>
                                            </div>
                                        </c:if>
                                        <c:if test="${totalPages > 3}">
                                            <a class='page-numbers current' onclick="goPage(1)" href="#list">1</a>
                                            <a class='page-numbers' onclick="goPage(2)" href="#list">2</a>
                                            <a class='page-numbers' onclick="goPage(3)" href="#list">3</a>
                                            <span class='page-numbers' >...</span>
                                            <a class='page-numbers'  onclick="goPage(${totalPages})" href="#list">${totalPages}</a>
                                            <a class="prev page-numbers"  onclick="nextPage()" href="#list">下一页  &raquo;</a>
                                        </c:if>
                                    </div>
                                    <div class="clear"></div>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/product.js'></script>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>