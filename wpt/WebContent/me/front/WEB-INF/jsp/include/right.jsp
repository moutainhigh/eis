<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
         <aside class="span4 sidebar-right hide-sidebar" id="rightindex">
                <!--<div class="widget-box widget ">
                    <div class="widget-content">
                        <a href="#" target="_blank" title="dove新款D系列">
                            <img class="gg430 mt10" src="../../../theme/${theme}/images/a.png" alt="dove新款D系列">
                        </a>
                        <a href="#" target="_blank" title="拿火吉他">
                            <img class="gg430 mt10" src="../../../theme/${theme}/images/b.png" alt="拿火吉他">
                        </a>
                        <a href="#" target="_blank" title="七星独家原创教材">
                            <img class="gg430 mt10" src="../../../theme/${theme}/images/c.png" alt="七星独家原创教材">
                        </a>
                    </div>
                </div>-->
                <div id="news-pic-widget-3" class="widget-box widget widget-pic">
                    <div class="widget-title"><span class="icon"><i class="icon-list"></i></span>
                        <h3>最新教学 </h3>
                    </div>
                    <div class="widget-content" style="padding:0px;">
                        <c:forEach var="item" items="${indexNewestList}" varStatus="status">
                            <c:if test="${status.index%2 == 1}">
                                <div class="new-pic" style="margin-left:5%;margin-right:0;height: 9vw;">
                                    <a class="post-thumbnail" href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark">
                                        <p>
                                            <c:choose>
                                                <c:when test="${empty item.document.productSmallImage}">
                                                    <img class="lazy" data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="${item.document.title}" style="width:100%;height:7vw;" />
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="lazy" data-lazy-src="/file/${item.document.productSmallImage}" alt="${item.document.title}" style="width:100%;height:7vw;" />
                                                </c:otherwise>
                                            </c:choose>
                                            <!--${item.product.content}-->
                                        </p>
                                    </a>
                                    <!--<p class="pic-t">-->
                                    <div class="head_box_right">
                                        <c:choose>
                                            <c:when test="${empty item.buy_money}"></c:when>
                                            <c:when test="${item.buy_money == '0'}"></c:when>
                                            <c:otherwise>
                                                <c:if test="${item.vipFree == 0 || item.vipFree == 1}">
                                                    <img data-lazy-src="../../../theme/${theme}/images/suo.png" alt="" style="width: 20px;float: left;">
                                                </c:if>
                                                <c:if test="${item.vipFree == 2}">
                                                    <img data-lazy-src="../../../theme/${theme}/images/vip.png" alt="" style="width: 20px;float: left;">
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${item.document.viewUrl}" class="head_box_right_a"  title="链接到  ${item.document.title}" style="margin: 3px 5px 5px;">${item.document.title}</a>
                                    <!--</p>-->
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${status.index%2 == 0}">
                                <div class="new-pic" style="margin-left:1%;margin-right:0;height: 9vw;">
                                    <a class="post-thumbnail" href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark">
                                        <p>
                                            <c:choose>
                                                <c:when test="${empty item.document.productSmallImage}">
                                                    <img class="lazy" data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="${item.document.title}" style="width:100%;height:7vw;" />
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="lazy" data-lazy-src="/file/${item.document.productSmallImage}" alt="${item.document.title}" style="width:100%;height:7vw;" />
                                                </c:otherwise>
                                            </c:choose>
                                            <!--${item.product.content}-->
                                        </p>
                                    </a>
                                    <!--<p class="pic-t">-->
                                    <div class="head_box_right">
                                        <c:choose>
                                            <c:when test="${empty item.buy_money}"></c:when>
                                            <c:when test="${item.buy_money == '0'}"></c:when>
                                            <c:otherwise>
                                                <c:if test="${item.vipFree == 0 || item.vipFree == 1}">
                                                    <img data-lazy-src="../../../theme/${theme}/images/suo.png" alt="" style="width: 20px;float: left;">
                                                </c:if>
                                                <c:if test="${item.vipFree == 2}">
                                                    <img data-lazy-src="../../../theme/${theme}/images/vip.png" alt="" style="width: 20px;float: left;">
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${item.document.viewUrl}" class="head_box_right_a" title="链接到  ${item.document.title}" style="margin: 3px 5px 5px;">${item.document.title}</a>
                                    <!--</p>-->
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                        <div class="clear"></div>
                    </div>
                </div>
                <!--<div id="wpfp-most_favorited_posts" class="widget-box widget wpfp_widget_view">
                    <div class="widget-title"><span class="icona"></span>
                        <h3>最受关注教学</h3>
                    </div>
                    <div class="widget-content">
                        <ul>
                            <li class="most-favorited" style='list-style:disc;'><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 374</span><a href='#' title='【V】赵雷《成都》吉他弹唱教学'>【V】赵雷《成都》吉他弹唱教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 288</span><a href='#' title='马頔 《南山南》吉他教学'>马頔 《南山南》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 286</span><a href='#' title='宋冬野《斑马斑马》吉他教学'>宋冬野《斑马斑马》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 220</span><a href='#' title='宋冬野/左立《董小姐》吉他教学'>宋冬野/左立《董小姐》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 214</span><a href='#' title='【V】薛之谦《演员》吉他教学'>【V】薛之谦《演员》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 213</span><a href='#' title='李克勤《月半小夜曲》吉他教学'>李克勤《月半小夜曲》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 205</span><a href='#' title='朴树《平凡之路》吉他教学'>朴树《平凡之路》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 197</span><a href='#' title='【V】田馥甄《小幸运》吉他教学'>【V】田馥甄《小幸运》吉他教学</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 184</span><a href='#' title='逃跑计划乐队/张恒远《夜空中最亮的星》'>逃跑计划乐队/张恒远《夜空中最亮的星》</a></li>
                            <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 163</span><a href='#' title='全站教学资源列表（一直在更新，永远不间断……）'>全站教学资源列表（一直在更新，永远不间断……）</a></li>
                        </ul>
                    </div>
                </div>-->
                <div id="category-posts-widget-3" class="widget-box widget category-posts">
                    <div class="widget-title"><span class="iconb"></span>
                        <h3>热门作品 </h3>
                    </div>
                    <div class="widget-content">
                        <ul>
                            <c:forEach var="item" items="${indexHotSaleList}" varStatus="status">
                                <c:choose>
                                    <c:when test="${item.document.title == 'vipyear'}">

                                    </c:when>
                                    <c:when test="${item.document.title == '季度会员'}">

                                    </c:when>
                                    <c:when test="${item.document.title == '月度会员'}">

                                    </c:when>
                                    <c:otherwise>
                                        <li class="widget-thumb posts-cat">
                                            <a class="post-thumbnail" href="${item.document.viewUrl}" title="">
                                                <c:choose>
                                                    <c:when test="${empty item.document.productSmallImage}">
                                                        <img data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt=""  class="avatar avatar-90" height="90" width="90">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img data-lazy-src="/file/${item.document.productSmallImage}" alt=""  class="avatar avatar-90" height="90" width="90">
                                                    </c:otherwise>
                                                </c:choose>
                                            </a>
                                            <p class="post-t">
                                                <a href="${item.document.viewUrl}">${item.document.title}</a>
                                            </p>
                                            <p class="post-meta">
                                                <span><img data-lazy-src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="">
                                                    <c:choose>
                                                        <c:when test="${empty item.userName}">
                                                            七星乐器
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${item.document.userName}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a></span>
                                                <span><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>
                                                    <c:choose>
                                                        <c:when test="${empty item.readCount}">
                                                            0
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${item.document.readCount}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                                <span><img data-lazy-src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="${item.document.viewUrl}#comments">
                                                    <c:choose>
                                                        <c:when test="${empty item.document.commentCount}">
                                                            0
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${item.document.commentCount}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a></span>
                                            </p>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </ul>
                        <div class="clear"></div>
                    </div>
                </div>
            </aside>