<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-杨猛老师</TITLE>
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
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a> <img src="../../../theme/${theme}/images/right.png" alt="" class='right'> <span class="current"> 阿坤 的文章 </span></div>
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
                        <div class="span12">
                            <div class="widget-box" style='background: none repeat scroll 0 0 #F8F8F8;border-bottom: 0;'>
                                <div id="user-left">
                                    <div class="user-avatar">
                                        <img src="../../../theme/${theme}/images/pino.png" alt="" height="100" width="100" class="avatar">
                                        <p>阿坤</p>
                                        <p class="user-pm">
                                            <a href="/user/bbsInformation.shtml?defaultReceiver=阿坤"><img src="../../../theme/${theme}/images/jianyi.png" alt="" style='width: 12px;height:12px;border: 0;padding: 0;border-radius:0;transform:rotateZ(0);box-shadow: 0 0 0;'> 发送站内信</a>
                                        </p>
                                    </div>
                                    <ul id="user-menu">
                                        <li class="current-menu-item">
                                            <a href="#"><img src="../../../theme/${theme}/images/greenbook.png" alt="" style='width:12px;height:12px;position:relative;top:1px;'> TA的文章</a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="widget-content" id="user-right">
                                    <header id="post-header" style='width:100%;'>
                                        <h1 itemprop="headline">
                                            阿坤 的文章 <a class="rss-cat-icon" title="订阅该作者的文章" href="#"><i class="icon-rss"></i></a>
                                        </h1>
                                    </header>
                                    <div class="post-count"> 阿坤 已发布了<span>${totalResults}</span> 篇文章：</div>
                                    <script>
                                        // $(function(){
                                        //     $('.archive-simple').each(function(x,y){
                                        //           $('.post-count').find('span').text(x+1);
                                        //     })
                                        // })
                                    </script>
                                    <div class="comments-loading"><img src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;"> 列表正在加载 ...</div>
                                    <ul class="commentlists">
                                        <c:forEach var="item" items="${newsList}" varStatus="status">
                                            <li class="archive-simple" style='border-bottom: 1px solid #eee;'>
                                                <h2>
                                                    <a href="${item.viewUrl}" title="链接到  ${item.title}" rel="bookmark" target="_blank"><img src="../../../theme/${theme}/images/greenyuan.png" alt="" class='right'>${item.title}</a>
                                                </h2>
                                                <p class="post-meta">
                                                    <span><img src="../../../theme/${theme}/images/lixian.png" alt="" class='lixian'><fmt:formatDate value="${item.publishTime}" type="both"/></span>
                                                    <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'></i>${item.documentDataMap.readCount.dataValue}</span>
                                                </p>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <div class="page-nav">
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
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/teacher.js'></script>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>