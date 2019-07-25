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
                    <div itemscope id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">指弹铃音</span></div>
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
                                <header class="archive-head">
                                    <h1 itemprop="headline">指弹铃音介绍
                                        <a class="rss-cat-icon" title="订阅这个分类" href="#">
                                            <p class="icon-rss"></p>
                                        </a>
                                    </h1>

                                    <p>${node.desc}</p>
                                    <div class='line'></div>
                                    <div class='archive-head-one'>
                                        <h1 class='list'>指弹铃音列表</h1>
                                        <span class='list' style='color:#666666;padding-top:23px;'>共300首</span>
                                        <span class='play' style='color:#666666;padding-top:28px;'>播放：38万次</span>
                                    </div>
                                    <div class='archive-head-two' >
                                        <input type="checkbox" onclick="allCheck()" id="allCheck"><span style='color:#666666;margin-top:20px;'>全选</span>
                                        <button style='background:#EDEDED;' onclick="moreLoad()" id="onload">批量下载</button>
                                        <button style='background:#31a030;color:#ffffff;' onclick="allLoad()">全部下载</button>
                                    </div>
                                    <div class='archive-head-three'>
                                        <div class='qumu'>曲目</div>
                                        <div class='author'>作者</div>
                                        <div class='times'>时长</div>
                                        <div class='shoucan'>收藏/播放<span class="gfjx" style='top: 0;'>VIP下载</span> </li>
                                        </div>
                                    </div>
                                </header>
                                <input type="hidden" value="${frontUser.uuid}" name="uuid">
                                <input type="hidden" name="objectType"  value="document">
                                <input type="hidden" name="objectId" value="${newsList[0].udid}"  id="udid">
                                <input type="hidden" name="vipLevel" value="${newsList[0].documentDataMap.vipLevel.dataValue}">
                                <div class="widget-content" style='padding: 0;'>
                                    <div class="comments-loading"><img src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;"> 音乐正在加载 ...</div>
                                    <ul class="commentlist">
                                        <c:forEach var="item" items="${newsList}" varStatus="status">
                                            <li class="archive-head-three-odd">
                                                <Audio class="music"><source src='/file/${item.documentDataMap.subscribeContent.dataValue}'></source></Audio>
                                                <div class='qumua'><input type="checkbox" class='content-input'><span class='content-author'>${item.title}</span></div>
                                                <div class='authora'>${item.author}</div>
                                                <div class='times'></div>
                                                <div class='shoucana'>
                                                    <span><img src="../../../theme/${theme}/images/love.png" alt="" style='margin-left:15px;' onclick="collectMusic()"></span>
                                                    <span><img style="width: 14px;height: 14px;" src="../../../theme/${theme}/images/pause.png" alt="" class="audio_btn"></span>
                                                    <span><input type="hidden" value="${item.documentDataMap.subscribeContent.dataValue}"><img src="../../../theme/${theme}/images/onload.png" alt="" style='margin-right: 15px;' class="onload"></span>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <div class="page-nav">
                                        <!--<span class="pages" style='margin-left:15px;'>1 of 35</span><span class="current">1</span><a href="#" class="page" title="2">2</a><a href="#" class="page" title="3">3</a><a href="#" class="page" title="4">4</a>
                                        <a href="#" class="page" title="5">5</a>
                                        <a href="#">&raquo;</a>
                                        <a href="#" class="page" title="10">10</a><a href="http://www.daweijita.com/guitar_friend_works/page/20" class="page" title="20">20</a><span class="extend">...</span>
                                        <a href="#" class="last" title="尾页 &raquo;">尾页 &raquo;</a>-->
                                        <c:forEach  var="i" begin="1" end="${totalPages}" >
                                                <c:if test="${i==1}">
                                                    <a href="#" class='page-numbers current' style='margin-left:15px;' onclick="goPage('${i}')">${i}</a>
                                                </c:if>
                                                <c:if test="${i>1}">
                                                    <div style='margin-left:15px;'>
                                                        <a href="#" class='page-numbers' onclick="goPage('${i}')">${i}</a>
                                                    </div>
                                                </c:if>
                                                <!--<c:if test="${i>3}">
                                                    <a class='page-numbers' onclick="goPage(1)">1</a>
                                                    <a class='page-numbers' onclick="goPage(2)">2</a>
                                                    <a class='page-numbers' onclick="goPage(3)">3</a>
                                                    <span class='page-numbers' >...</span>
                                                    <a class='page-numbers' >${totalPages}</a>
                                                    <a class="prev page-numbers"  onclick="nextPage()">下一页  &raquo;</a>
                                                </c:if>-->
                                        </c:forEach>
                                        <a href="#" class="prev page-numbers"  onclick="nextPage()">下一页  &raquo;</a>
                                    </div>
                                </div>
                        </section>
	                     <%@include file="/WEB-INF/jsp/include/right.jsp" %>
                        </div>
                    </div>
                </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/ring.js'></script>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>