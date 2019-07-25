<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-在线调弦</TITLE>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!--<meta http-equiv="Refresh" content="0; url=/content/node/index.shtml" />-->
            <META name=description content=有宝>
            <META name=keywords content=有宝>
            <LINK rel="Shortcut Icon" href="/favicon.ico">
            <LINK rel=Bookmark href="/favicon.ico">


            <link rel="stylesheet" href="../../../theme/${theme}/css/chord.css">
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
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">在线调弦</span></div>
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
                            <header class="archive-head jieshao">
                                <h1 itemprop="headline" class='title-ro' style='padding: 20px 0px;'>课程概述
                                    <a class="rss-cat-icon" title="订阅这个分类" href="#">
                                        <p class="icon-rss"></p>
                                    </a>
                                </h1>

                                <p style='padding: 15px;'>${node.desc}</p>
                            </header>
                            <div class="widget-box">
                                <article id="post-7315" class="widget-content single-post" itemscope style='padding: 0;'>

                                    <header id="post-header">
                                        <div class='wpfp-span'><img src='../../../theme/${theme}/images/lovera.png' alt='Loading' title='Loading' class='wpfp-hide wpfp-img' /><span title="只有已登录用户才能收藏文章，请先登录/注册。"><i class="icon-heart add"></i> <div class="poptip"><span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span>
                                            <c:choose>
                                                <c:when test="${empty favoriteCount}">
                                                    0
                                                </c:when>
                                                <c:otherwise>
                                                    ${favoriteCount}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                        </div>
                                        </span>
                            </div>
                            <h1 class="post-title" itemprop="headline">在线调弦</h1>
                            <div class="clear"></div>
                            <p class="post-meta">
                                <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="">  
                                    <c:choose>
                                        <c:when test="${empty uploadAuthor}">
                                            七星乐器
                                        </c:when>
                                        <c:otherwise>
                                            ${uploadAuthor.userName}
                                        </c:otherwise>
                                    </c:choose></a>
                                </span>
                                <span class="time"><img src="../../../theme/${theme}/images/lixian.png" alt="" class='lixian' style='padding-left:5px;'>2015-06-12</span>
                                <span class="cat"><img src="../../../theme/${theme}/images/excrse.png" alt="" class='ku'><a href="#" rel="category tag" style='padding-left:5px;'>${node.name}</a></span>
                                <!--<span class="eye"><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>${readCount}</span>-->
                                <!--<span class="comm"><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="#">${document.documentDataMap.get('commentCount').dataValue}</a></span>-->
                            </p>
                            <div class="clear"></div>
                            </header>
                            <div class="entrys" itemprop="articleBody">
                                <div class="myplayer">
                                    <table cellpadding="0" cellspacing="0">
                                        <thead>
                                             <tr>
                                                 <td>听力调音</td>
                                                 <td>四三拍节拍器</td>
                                                 <td>四四拍常用节拍</td>
                                                 <td>八六拍常用节拍</td>
                                             </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                               <td class="tdHead">
                                                   <span>1、常用标准音调弦</span>
                                                   <p class="music">
                                                        <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/change1.mp3"></audio>
                                                        <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                        <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                        <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                        <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                   </p>
                                                </td>
                                               <td>
                                                   <span>1、四三拍---每分钟70拍</span>
                                                   <p class="music">
                                                        <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four370.mp3"></audio>
                                                        <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                        <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                        <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                        <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                   </p>
                                                </td>
                                                <td>
                                                    <span>1、四四拍---每分钟50拍</span>
                                                    <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four450.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>1、八六拍---每分钟80拍</span>
                                                    <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/eight680.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                               <td class="tdHead">
                                                   <span>2、降半音调弦</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/change2.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                               <td>
                                                   <span>2、四三拍---每分钟75拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four375.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>2、四四拍---每分钟55拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four455.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>2、八六拍---每分钟85拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/eight685.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                               <td class="tdHead">
                                                   <span>3、特殊调弦 1D 2A 3G 4D 5A 6D</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/change3.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                               <td>
                                                   <span>3、四三拍---每分钟80拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four380.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>3、四四拍---每分钟60拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four460.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>3、八六拍---每分钟90拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/eight690.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead">
                                                    <span>4、特殊调弦 1D 2A 3#F 4D 5A 6D</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/change4.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>4、四三拍---每分钟90拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four390.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>4、四四拍---每分钟65拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four465.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>4、八六拍---每分钟95拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/eight695.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead">
                                                    <span>5、特殊调弦 1D 2B 3G 4D 5G 6C</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/change5.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>5、四三拍---每分钟100拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3100.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>5、四四拍---每分钟70拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four470.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>5、八六拍---每分钟100拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/eight6100.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead">
                                                    <span>6、特殊调弦 1D 2bB 3G 4D 5G 6C</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/change6.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>6、四三拍---每分钟110拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3110.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>6、四四拍---每分钟75拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four475.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead"></td>
                                                <td>
                                                    <span>7、四三拍---每分钟120拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3120.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>7、四四拍---每分钟80拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four480.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead"></td>
                                                <td>
                                                    <span>8、四三拍---每分钟130拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3130.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>8、四四拍---每分钟85拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four485.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead"></td>
                                                <td>
                                                    <span>9、四三拍---每分钟140拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3140.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>9、四四拍---每分钟90拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four490.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead"></td>
                                                <td>
                                                    <span>10、四三拍---每分钟150拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3150.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>10、四四拍---每分钟95拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four495.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td class="tdHead"></td>
                                                <td>
                                                    <span>11、四三拍---每分钟160拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four3160.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span><input type="range" min=0 max=100 class='range' value=0><span class='max'></span>
                                                    </p>
                                                </td>
                                                <td>
                                                    <span>11、四四拍---每分钟100拍</span>
                                                   <p class="music">
                                                            <audio id="ao" class="ao"><source src="../../../theme/${theme}/mp3/four4100.mp3"></audio>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/plays.png" alt="" class="plays"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/pasues.png" alt="" class="pasues"></span>
                                                            <span><img style="width: 14px;height: 14px;opacity: 0.8;" src="../../../theme/basic/images/oneAgain.png" alt="" class="oneAgain"></span>
                                                            <span class='cur'></span>
                                                            <input type="range" min=0 max=100 class='range' value=0>
                                                            <span class='max'></span>
                                                    </p>
                                                </td>
                                                <td></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #ffffff;background: #ff8200;display: inline-block;width: 100px;height: 26px;text-align: center;'>七星指弹教室</a>  的吉他谱和教学视频！</div>
                            </div>
                            <footer class="entry-meta">
                                <div class='wpfp-span'><img src='../../../theme/${theme}/images/lovera.png' alt='Loading' title='Loading' class='wpfp-hide wpfp-img' /><span title="只有已登录用户才能收藏文章，请先登录/注册。"><i class="icon-heart add"></i> <div class="poptip"><span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span>
                                    <c:choose>
                                        <c:when test="${empty favoriteCount}">
                                            0
                                        </c:when>
                                        <c:otherwise>
                                            ${favoriteCount}
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                </div>
                                </span>
                        </div>
                        <!--<div class="bdsharebuttonbox post-share">
                            <a href="#" data-cmd="qzone" title="分享到QQ空间">
                                <div class="bds_qzone"></div>
                            </a>
                            <a href="#" data-cmd="tsina" title="分享到新浪微博">
                                <div class="bds_tsina"></div>
                            </a>
                            <a href="#" data-cmd="tqq" title="分享到腾讯微博">
                                <div class="bds_tqq"></div>
                            </a>
                            <a href="#" data-cmd="renren" title="分享到人人网">
                                <div class="bds_renren"></div>
                            </a>
                            <a href="#" data-cmd="weixin" title="分享到微信">
                                <div class="bds_weixin"></div>
                            </a>
                            <a href="#" data-cmd="more">
                                <div class="bds_more"></div>
                            </a>
                            <a data-cmd="count">
                                <div class="bds_count"></div>
                            </a>
                        </div>
                        <div class="gpost-below">
                            <a href="#" rel="nofollow" title="官方淘宝店" target="_blank">
                                <img src="../../../theme/${theme}/images/bannerb.png" alt="官方淘宝店" style='width: 96%;margin:0 15px;' />
                            </a>
                        </div>-->
                        <div id="author-box">
                            <c:choose>
                                <c:when test="${empty uploadAuthor}">
                                    <h3>作者： 七星乐器</h3>
                                </c:when>
                                <c:otherwise>
                                    <h3>作者： ${uploadAuthor.userName}</h3>
                                </c:otherwise>
                            </c:choose>
                            <div class="author-info">
                                <div class="author-avatar">
                                    <c:choose>
                                        <c:when test="${empty uploadAuthor}">
                                            <img src="../../../theme/${theme}/images/LOGO.png" alt="" height="64" width="64" class="avatar"> </div>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="/file/client/${uploadAuthor.userHeadPic}" alt="" height="64" width="64" class="avatar"> </div>
                                        </c:otherwise>
                                    </c:choose>
                                <div class="author-description">
                                    <c:choose>
                                        <c:when test="${empty uploadAuthor}">
                                            <p>该用户很懒，还没有介绍自己。</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p>${uploadAuthor.userDescription}</p>
                                        </c:otherwise>
                                    </c:choose>
                                    <ul class="author-social follows nb">
                                        <li class="archive">
                                            <a target="_blank" href="#" title="阅读 大伟 的其他文章">阅读 金伟 的其他文章</a>
                                        </li>
                                        <li class="email">
                                            <a target="_blank" rel="nofollow" href="#" title="给 金伟 发送站内信">给 金伟 发送站内信</a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        </footer>
                        </article>
                    </div>
                    <%@include file="/WEB-INF/jsp/include/detail.jsp" %> 

                </div>


                <%@include file="/WEB-INF/jsp/include/right.jsp" %>
            </div>
            </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
            <script src='../../../theme/${theme}/js/comment.js'></script>
        </body>

        </html>