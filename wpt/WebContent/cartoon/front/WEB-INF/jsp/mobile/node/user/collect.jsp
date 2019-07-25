<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-我的收藏</TITLE>
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
             <script>                
                $(function(){
                     if(jQuery.cookie('eis_username') == null){
                           $('.entry').empty().append('<div class="wpuf-info" style="margin:23px;">该页面只允许已登录用户访问，请登录/注册后访问。</div>')
                     }else{
                         var img = jQuery.cookie('eis_userheadpic');
                        if(img == ''){
                            $('#headImg').attr('src','../../../theme/basic/images/touxiang.png');
                         }else{
                            $('#headImg').attr('src','/file/client/'+img);
                         }
                     }
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
                    <div itemscope itemtype="http://schema.org/WebPage" id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">用户中心</span>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">我的收藏</span></div>
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
                        <div class="span12">

                            <div class="widget-box user-center">
                                <div id="user-left">
                                    <div class="user-avatar myname">
                                        <c:choose>
                                        <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li><a href="/user/myUploadProducts.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li class="current-menu-item">
                                            <a href="/user/myFavoriteList.shtml?vipType=VIP_YEAR&VIP_QUARTER&VIP_MONTH" style='background: url("../../../theme/${theme}/images/oldlovers.png") no-repeat;
                background-position-y: 4px;
                background-position-x: -1px;
                background-size: 13px;'><span class="icon-heart"></span>我的收藏</a></li>
                                        <li><a href="/user/purchasedList.shtml"><span class="icon-shopping-cart"></span>已购教学</a></li>
                                        <li><a href="/user/purchasedList.shtml?flag=vip"><span class="icon-gift"></span>VIP订购</a></li>
                                        <li><a href="/content/user/20170515133906.shtml"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/content/user/20170515141440.shtml"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope itemtype="http://schema.org/Article">
                                    <div id="post-header" style="width: 100%;margin: 0;">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">我的收藏</h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody">
                                        <div class=' wpfp-span '>
                                            <ul>
                                                <c:choose>
                                                    <c:when test="${empty userFavoriteList}">
                                                        <p>您已收藏了 0 篇文章</p>
                                                            <li>
                                                                <p>您还没有收藏任何文章！</p>
                                                            </li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>您已收藏了 ${fn:length(userFavoriteList)} 篇文章</p>
                                                        <c:forEach var="item" items="${userFavoriteList}" varStatus="status">
                                                                <li>
                                                                    <a id='rem_4360 ' class='wpfp-link remove-parent ' href='#' title='取消收藏 ' rel='nofollow '><i class="icon-trash"></i></a>
                                                                    <a href='${item.data.refUrl}' title='${item.data.refTitle}'>${item.data.refTitle}</a> 
                                                                    <input type="hidden" name="userRelationId" value="${item.userRelationId}">
                                                                </li>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </ul>
                                            <div class="page-nav">
                                            </div>
                                            <div class="clear"></div>
                                        </div>
                                        <p></p>
                                    </div>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>