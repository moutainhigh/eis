        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <!--**************************************-->
                 <style>
                     .app-voice-main{
                         margin: 20px 73px;
                         margin-top: 20px;
                     }
                     .app-voice-state-bg{
                         background: #9be855;
                         box-shadow: 3px 3px 3px 3px #f3f3f3;
                         border-radius: 14px;
                         cursor: pointer;
                         float: left;
                         width:80%;
                     }
                     .app-voice-you{
                        /*播放中（不需要过渡动画）*/
                        width:80px;
                         height: 50px;
                        background: url(../../../theme/${theme}/images/voice.png) no-repeat;
                        background-position: 15px 8px;
                        -webkit-animation: voiceplay 1s infinite step-start;
                        -moz-animation: voiceplay 1s infinite step-start;
                        -o-animation: voiceplay 1s infinite step-start;
                        animation: voiceplay 1s infinite step-start;
                        background-size:37px;
                    }
                    .app-voice-he{
                        /*播放中（不需要过渡动画）*/
                        width:80px;
                         height: 50px;
                        background: url(../../../theme/${theme}/images/voice.png) no-repeat;
                        background-position: 15px 8px;
                        background-size:37px;
                    }
                    @-webkit-keyframes voiceplay {
                        0%,
                        100% {
                            width:80px;
                        }
                        20%{
                          width:16px;
                        }
                        40%{
                            width:32px;
                        }
                        60%{
                            width:48px;
                        }
                        80%{
                            width:64px;
                        }
                    }
                    .voiceTime{
                        line-height: 55px;
                        font-size: 22px;
                    }
                 </style>
            <!--**********************************-->

            <script src='../../../theme/${theme}/js/HZRecorder.js'></script>
                    <div class="widget-box">
                        <section class="widget-content" style='padding: 0;'>
                            <!-- You can start editing here. -->
                            <div id="comments">
                                <h3>${document.documentDataMap.get('commentCount').dataValue} 条评论</h3>
                            </div>
                            <div class="comments-loading"><img data-lazy-src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;"> 评论正在加载 ...</div>
                            <ol class="commentlist">
                                <c:choose>
                                        <c:when test="${document.documentDataMap.get('commentCount').dataValue == 0}">
                                                <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;'>
                                                <div id="div-comment-39615" class="comment-body">
                                                    <div class="comment-author vcard ">
                                                        <p style="padding:0px 40%;">还没有人评论！</p>
                                                        <!--width:110px;-->
                                                    </div>
                                                </div>
                                            </li>
                                        </c:when>
                                </c:choose>         
                                <c:forEach var="items" items="${commentList}" varStatus="status">
                                    <c:choose>
                                            <c:when test="${empty items}">
                                                 <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;'>
                                                    <div id="div-comment-39615" class="comment-body">
                                                        <div class="comment-author vcard ">
                                                            <p style="padding:0px 40%;">还没有人评论！</p>
                                                            <!--width:110px;-->
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${items}" varStatus="status">
                                                    <c:choose>
                                                        <c:when test="${item.relatedCommentId == 0}">
                                                            <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;'>
                                                                <div id="div-comment-39615" class="comment-body">
                                                                    <div class="comment-author vcard ">
                                                                        <c:choose>
                                                                            <c:when test="${empty item.data.userHeadPic}">
                                                                                <img data-lazy-src="../../../theme/basic/images/touxiang.png" alt="${item.data.userRealName}" height="40" width="40" class="avatar">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <img data-lazy-src="/file/client/${item.data.userHeadPic}" alt="${item.data.userRealName}" height="40" width="40" class="avatar">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <div class="floor"></div>
                                                                        <span class="c-user"><a href='' rel='external nofollow' class='url'>${item.data.userRealName}</a></span>: 
                                                                    </div>
                                                                    <!--音频显示-->
                                                                    <c:choose>
                                                                        <c:when test="${empty item.data.productGallery}">
                                                                            <!--评论内容-->
                                                                            <p class="text">${item.content}</p>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <div class="app-voice-main">
                                                                                    <div style=" width: 180px;">
                                                                                        <audio class="audio">
                                                                                            <source src="/file/${item.data.productGallery}" type="">
                                                                                        </audio>
                                                                                        <div class="app-voice-state-bg">
                                                                                            <div class="app-voice-he"></div>
                                                                                        </div>
                                                                                        <div class="voiceTime">
                                                                                            
                                                                                        </div>
                                                                                    </div>
                                                                            </div>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <div class="clear"></div>
                                                                    <span class="datetime"><fmt:formatDate value="${item.createTime}" type="both" /></span>
                                                                    <span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span>
                                                                    <input type="hidden" value="${item.commentId}">
                                                                    <input type="hidden" value="${item.objectId}" id="objectId">
                                                                    <input type="hidden" value="${item.commentId}" id="commentId">
                                                                </div>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <!--<ul class="children">-->
                                                                <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;">
                                                                    <div id="div-comment-39615" class="comment-body">
                                                                        <div class="comment-author vcard ">
                                                                            <c:choose>
                                                                                <c:when test="${empty item.data.userHeadPic}">
                                                                                    <img data-lazy-src="../../../theme/basic/images/touxiang.png" alt="${item.data.userRealName}" height="40" width="40" class="avatar">
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <img data-lazy-src="/file/client/${item.data.userHeadPic}" alt="${item.data.userRealName}" height="40" width="40" class="avatar">
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                            <span class="c-user">
                                                                                <a href="#" rel="external nofollow" class="url" >${item.data.userRealName}</a>回复<a class="url">${item.data.replyTo}</a>
                                                                            </span>: 
                                                                        </div>
                                                                         
                                                                         <!--音频显示-->
                                                                        <c:choose>
                                                                            <c:when test="${empty item.data.productGallery}">
                                                                                <!--评论显示-->
                                                                                <p class="text">${item.content}</p>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <div class="app-voice-main">
                                                                                    <div style=" width: 180px;">
                                                                                        <audio class="audio">
                                                                                            <source src="/file/${item.data.productGallery}" type="">
                                                                                        </audio>
                                                                                        <div class="app-voice-state-bg">
                                                                                            <div class="app-voice-he"></div>
                                                                                        </div>
                                                                                        <div class="voiceTime">
                                                                                            
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <div class="clear"></div>
                                                                        <span class="datetime"><fmt:formatDate value="${item.createTime}" type="both" /></span>
                                                                        <span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span>
                                                                        <input type="hidden" value="${item.commentId}">
                                                                        <input type="hidden" value="${item.objectId}" id="objectId">
                                                                        <input type="hidden" value="${item.commentId}" id="commentId">
                                                                    </div>
                                                                </li>
                                                            <!--</ul>-->
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </ol>
                            <c:choose>
                                <c:when test="${empty document.documentDataMap.get('productCode')}">
                                     <input type="hidden" value="document" id="productType">
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" value="product" id="productType">
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${document.documentDataMap.get('commentCount').dataValue == 0}">
                                        
                                </c:when>
                                <c:otherwise>
                                    <div class="page-nav comment-nav">
                                        <c:forEach  var="i" begin="1" end="${totalPages}" >
                                            <c:if test="${i==1}">
                                                <a href="#comments" class='page-numbers current' style='margin-left:15px;' onclick="goPage('${i}')">${i}</a>
                                            </c:if>
                                            <c:if test="${i>1}">
                                                <div style='margin-left:15px;'>
                                                    <a href="#comments" class='page-numbers' onclick="goPage('${i}')">${i}</a>
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
                                        <a href="#comments" class="prev page-numbers"  onclick="nextPage()">下一页  &raquo;</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="clear"></div>
                            <div id="respond" class="comment-respond">
                                <h3 id="reply-title" class="comment-reply-title">发表评论 <small><a rel="nofollow" id="cancel-comment-reply-link" href="/7315.html#respond" style="display:none;">取消回复</a></small></h3>
                                <p class="must-log-in">要发表评论，您必须先<a href="/user/login.shtml" style="color: #31a030;">登录</a>。</p>
                            </div>
                            <div class='smile'></div>
                            <!-- #respond -->
                        </section>
                    </div>




