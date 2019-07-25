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
            <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/register.css">
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
                    <div itemscope id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current"> 的搜索结果</span></div>
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
                        <section class="span8 archive-list">
                            <div class="widget-box" role="main" itemscope style='border-bottom: 0;'>
                                <header id="post-header">
                                    <h1 itemprop="headline">
                                        搜索:
                                    </h1>
                                </header>
                                <div class="widget-content">
                                    <ul>
                                        <c:forEach var="item" items="${newsList}" varStatus="status">
                                            <li class="archive-simple" style='margin: 15px 0px;border-bottom: 1px solid #EDEDED;padding-bottom:15px;'>
                                                <p class="post-meta">
                                                    <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="/content/teacher/jinwei/index.shtml" title="">${item.author}</a></span>
                                                    <c:choose>
                                                        <c:when test="${empty item.documentDataMap}">
                                                            <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>0</span>
                                                            <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="${item.viewUrl}#comments">0</a></span>
                                                            </p>
                                                        </c:when>
                                                        <c:when test="${empty item.documentDataMap.readCount}">
                                                            <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>0</span>
                                                            <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="${item.viewUrl}#comments">${item.documentDataMap.commentCount.dataValue}</a></span>
                                                            </p>
                                                        </c:when>
                                                        <c:when test="${empty item.documentDataMap.commentCount}">
                                                            <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.documentDataMap.readCount.dataValue}</span>
                                                            <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="${item.viewUrl}#comments">0</a></span>
                                                            </p>
                                                        </c:when> 
                                                        <c:otherwise>
                                                            <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>${item.documentDataMap.readCount.dataValue}</span>
                                                            <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="${item.viewUrl}#comments">${item.documentDataMap.commentCount.dataValue}</a></span>
                                                            </p>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empty item.documentDataMap.productSmallImage.dataValue}">
                                                            <a class="post-thumbnail" href="/content/teacher/jinwei/index.shtml" title="">
                                                                <img src='../../../theme/basic/images/touxiang.png' alt="" height="45" width="45" class="avatar">
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="post-thumbnail" href="/content/teacher/jinwei/index.shtml" title="">
                                                                <img src='/file/${item.documentDataMap.productSmallImage.dataValue}' alt="" height="45" width="45" class="avatar">
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                <h2><a href="${item.viewUrl}" title="链接到  入门教学01【吉他的发声原理与如何调音】" rel="bookmark" target="_blank">${item.title}</a></h2>
                                                <span class="gfjx">官方教学</span>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </section>

	                     <%@include file="/WEB-INF/jsp/include/right.jsp" %>
                    </div>
                </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/register.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>