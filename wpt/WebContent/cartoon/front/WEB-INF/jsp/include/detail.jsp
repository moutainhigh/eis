        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
                            <div class="row-fluid" style='background: #ffffff;'>
                                <section class="span4 home-recent related">
                                    <div class="widget-box">
                                        <div class="widget-title">
                                            <h3>随便看看</h3>
                                        </div>
                                        <div class="widget-content">
                                            <ul class="news-list">
                                                <c:forEach var="item" items="${indexNewestList}" varStatus="status">
                                                    <c:choose>
                                                        <c:when test="${empty indexNewestList}">
                                                            <li>还没有任何内容！</li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li>
                                                                <a href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                            </li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </section>



                                <section class="span4 home-recent related">
                                    <div class="widget-box">
                                        <div class="widget-title">
                                            <h3>小编推荐</h3>
                                        </div>
                                        <div class="widget-content">
                                            <ul class="news-list">
                                                <c:forEach var="item" items="${indexRecommendList}" varStatus="status">
                                                    <c:choose>
                                                        <c:when test="${empty indexRecommendList}">
                                                            <li>还没有任何内容！</li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${item.document.title == 'vipyear'}">

                                                                </c:when>
                                                                <c:when test="${item.document.title == '季度会员'}">

                                                                </c:when>
                                                                <c:when test="${item.document.title == '月度会员'}">

                                                                </c:when>
                                                                <c:otherwise>
                                                                    <li>
                                                                        <a href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                                    </li>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </section>

                                <section class="span4 home-recent related">
                                    <div class="widget-box">
                                        <div class="widget-title">
                                            <h3>精品付费教学</h3>
                                        </div>
                                        <div class="widget-content">
                                            <ul class="news-list">
                                                <c:forEach var="item" items="${indexHotSaleList}" varStatus="status">
                                                    <c:choose>
                                                        <c:when test="${empty indexHotSaleList}">
                                                            <li>还没有任何内容！</li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${item.document.title == 'vipyear'}">

                                                                </c:when>
                                                                <c:when test="${item.document.title == '季度会员'}">

                                                                </c:when>
                                                                <c:when test="${item.document.title == '月度会员'}">

                                                                </c:when>
                                                                <c:otherwise>
                                                                    <li>
                                                                        <a href="${item.document.viewUrl}" title="链接到  ${item.document.title}" rel="bookmark" target="_blank">${item.document.title}</a>
                                                                    </li>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </section>
                            </div>