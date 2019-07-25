          <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
                <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
                        <section class="span8 archive-list">
                            <header class="archive-head jieshao">
                                <h1 itemprop="headline" class='title-ro' style='padding: 20px 0px;'>课程概述
                                    <a class="rss-cat-icon" title="订阅这个分类" href="#">
                                        <p class="icon-rss"></p>
                                    </a>
                                </h1>
                                <p style='padding: 15px;'>${node.desc}</p>
                            </header>
                            <div class="widget-box" role="main" itemscope style='border-bottom: 0;'>
                                <header id="post-header">
                                    <h1 itemprop="headline" id="list">
                                        ${node.name}
                                    </h1>
                                </header>
                                <div class="widget-content">
                                    <div class="comments-loading"><img data-lazy-src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;"> 列表正在加载 ...</div>
                                    <ul class="commentlists">
                                        <c:forEach var="item" items="${newsList}" varStatus="status">
                                            <!--<li class="archive-simple" style='margin: 15px 0px;border-bottom: 1px solid #EDEDED;padding-bottom:15px;'>
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
                                                <h2><a href="${item.viewUrl}" title="链接到  ${item.title}" rel="bookmark" target="_blank">${item.title}</a></h2>
                                                <c:choose>
                                                    <c:when test="${node.name == 'vip专区'}">
                                                        <span class="gfjx">vip专享</span>
                                                    </c:when>
                                                    <c:when test="${node.name == '付费曲谱教学'}">
                                                        <span class="gfjx">官方vip</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="gfjx">官方教学</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </li>-->
                                            <li class="video-thumb" style="width:24%;clear:none;height:225px;">
                                                <article>
                                                    <a class="pic" target="_blank" href="${item.viewUrl}" title="链接到  ${item.title}" rel="bookmark">
                                                        <c:choose>
                                                            <c:when test="${empty item.documentDataMap.productSmallImage.dataValue}">
                                                                <img class="lazy" data-lazy-src="../../../theme/${theme}/images/touxiang.png" alt="#" style='width: 100%;height: 125px;' />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img class="lazy" data-lazy-src="/file/${item.documentDataMap.productSmallImage.dataValue}" alt="#" style='width: 100%;height: 125px;' />
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </a>
                                                    <div class="video-info">
                                                        <h2 style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><a target="_blank" href="${item.viewUrl}" title="链接到  ${item.title}" rel="bookmark">${item.title}</a></h2>
                                                        <!--<p>琴友：<a target="_blank" href="#" title="">${item.author}</a></p>
                                                        <p><fmt:formatDate value="${item.publishTime}" type="both"/></p>-->
                                                        <c:choose>
                                                            <c:when test="${node.name == 'vip专区'}">
                                                                <p class="gfjx" style="margin-top:10px;">vip专享</p>
                                                            </c:when>
                                                            <c:when test="${node.name == '付费曲谱教学'}">
                                                                <p class="gfjx" style="margin-top:10px;">官方vip</p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p class="gfjx" style="margin-top:10px;">官方教学</p>
                                                            </c:otherwise>
                                                        </c:choose>
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
                                                            <span><img data-lazy-src="../../../theme/${theme}/images/collectLove.png" alt="" style='width: 14px;height:14px;padding-right: 5px;'>
                                                                <c:choose>
                                                                    <c:when test="${empty item.documentDataMap.favoriteCount}">
                                                                        0
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${item.documentDataMap.favoriteCount.dataValue}
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
                                    <div class="page-nav" style="float: inherit;">
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
                                                <a class='page-numbers current' onclick="goPage(1)" href="#">1</a>
                                                <a class='page-numbers' onclick="goPage(2)" href="#">2</a>
                                                <a class="prev page-numbers"  onclick="nextPage()" href="#">下一页  &raquo;</a>
                                            </div>
                                        </c:if>
                                        <c:if test="${totalPages == 3}">
                                            <div style='margin-left:15px;'>
                                                <a class='page-numbers current' onclick="goPage(1)" href="#">1</a>
                                                <a class='page-numbers' onclick="goPage(2)" href="#">2</a>
                                                <a class='page-numbers' onclick="goPage(3)" href="#">3</a>
                                                <a class="prev page-numbers"  onclick="nextPage()" href="#">下一页  &raquo;</a>
                                            </div>
                                        </c:if>
                                        <c:if test="${totalPages > 3}">
                                            <a class='page-numbers current' onclick="goPage(1)" href="#">1</a>
                                            <a class='page-numbers' onclick="goPage(2)" href="#">2</a>
                                            <a class='page-numbers' onclick="goPage(3)" href="#">3</a>
                                            <span >...</span>
                                            <a class='page-numbers'  onclick="goPage(${totalPages})" href="#">${totalPages}</a>
                                            <a class="prev page-numbers"  onclick="nextPage()" href="#">下一页  &raquo;</a>
                                        </c:if>
                                    </div>
                                </div>
                            </section>