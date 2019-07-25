                <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
                <%@include file="/WEB-INF/jsp/include/tags.jsp" %>

                <link rel="stylesheet" href="../../../theme/${theme}/css/viewer.min.css">
                <style>
                    .imgMargi{
                        padding: 0;
                    }
                    .imgMargin a{
                        background-color: #eee;
                        margin: 2.5px;
                    }
                </style>
                            <div class="widget-box">
                                <article id="post-7315" class="widget-content single-post" itemscope style='padding: 0;'>
                                   <header id="post-header" style='width:100%;background: #f8f8f8;'>
                                        <div class='wpfp-span'>
                                            <img data-lazy-src='../../../theme/${theme}/images/lovera.png' alt='Loading' title='Loading' class='wpfp-hide wpfp-img' onclick="collect()"/>
                                            <span title="只有已登录用户才能收藏文章，请先登录/注册。">
                                                <i class="icon-heart add"></i> 
                                                <div class="poptip">
                                                    <span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span>
                                                    <c:choose>
                                                        <c:when test="${empty favoriteCount}">
                                                            0
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${favoriteCount}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                        </div>
                                        <h1 class="post-title" itemprop="headline" style='width:80%;padding-left:15px;'>  ${document.title}</h1>
                                        <div class="clear"></div>
                                        <p class="post-meta" style='margin-left:15px;'>
                                            <span><img data-lazy-src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="">
                                                <c:choose>
                                                    <c:when test="${empty uploadAuthor}">
                                                        七星乐器
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${uploadAuthor.userName}
                                                    </c:otherwise>
                                                </c:choose></a>
                                            </span>
                                            <span class="time"><img data-lazy-src="../../../theme/${theme}/images/lixian.png" alt="" class='lixian' style='padding-left:5px;'><fmt:formatDate value="${document.publishTime}" type="both"/></span>
                                            <span class="cat"><img data-lazy-src="../../../theme/${theme}/images/excrse.png" alt="" class='ku'><a href="${node.viewUrl}" rel="category tag" style='padding-left:5px;'>${node.name}</a></span>
                                            <span class="eye"><img data-lazy-src="../../../theme/${theme}/images/video.png" alt="" class='video'>${readCount}</span>
                                            <span class="comm"><img data-lazy-src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="#">${document.documentDataMap.get('commentCount').dataValue}</a></span>
                                        </p>
                                        <div class="clear"></div>
                                    </header>
                                    <div class="entry" itemprop="articleBody" id="dowebok" >
                                    <!--<p>
                                        <a href="../../../theme/${theme}/images/learnor.png" rel="lightbox" title="" class="highslide-image">
                                            <img style="width:100%;height: auto;margin: 0 15px;border:0;padding: 11px;"  rel="lightbox" title="" class="alignnone size-full wp-image-7140" alt="learnor" src="../../../theme/${theme}/images/learnor.png" />
                                        </a>
                                    </p>-->
                                    <p style="cursor: pointer">
                                        <c:set var="theString" value="${document.documentDataMap.productGallery.dataValue}"/>
                                        <c:choose>
                                            <c:when test="${fn:contains(theString, ',')}">
                                                <c:set value="${fn:split(theString, ',') }" var="names" />
                                                <c:forEach items="${names}" var="name">
                                                        <c:if test="${fn:contains(name, '.mp4')}">
                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                <video width="90%" height="" controls style="margin: 0 auto;display: block;" poster="../../../theme/${theme}/images/voiceBg.png">
                                                                    <source src="/file/${name}"  type="video/mp4">
                                                                </video>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${fn:contains(name, '.f4v')}">
                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                <!--<video width="90%" height="500" controls style="margin: 0 auto;display: block;">
                                                                    <source src="/file/${name}"  type="video">
                                                                </video>-->
                                                                <video   controls width="100%" height="" poster="../../../theme/${theme}/images/voiceBg.png">
                                                                    <source src="/file/${name}" />
                                                                    <embed src="/file/${name}"></embed>
                                                                </video>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${fn:contains(name, '.jpg')}">
                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                <!--<a href="/file/${name}" rel="lightbox" style="margin: 0 auto;">-->
                                                                    <img style="width:90%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${name}" data-original="/file/${name}" alt="" rel="lightbox">
                                                                <!--</a>-->
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${fn:contains(name, '.png')}">
                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                <!--<a href="/file/${name}" rel="lightbox" style="margin: 0 auto;">-->
                                                                    <img style="width:90%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${name}"  data-original="/file/${name}" alt="" rel="lightbox">
                                                                <!--</a>-->
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${fn:contains(name, '.gif')}">
                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                <!--<a href="/file/${name}" rel="lightbox" style="margin: 0 auto;">-->
                                                                    <img style="width:90%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${name}"  alt="" rel="lightbox">
                                                                <!--</a>-->
                                                            </div>
                                                        </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${fn:contains(theString, '.mp4')}">
                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                        <video width="100%" height="" controls  style="margin: 0 auto;display: block;" poster="../../../theme/${theme}/images/voiceBg.png">
                                                            <source src="/file/${document.documentDataMap.productGallery.dataValue}"  type="video/mp4">
                                                        </video>
                                                    </div>
                                                </c:if>
                                                <c:if test="${fn:contains(theString, '.f4v')}">
                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                        <!--<video width="100%" height="500" controls  style="margin: 0 auto;display: block;">
                                                            <source src="/file/${document.documentDataMap.productGallery.dataValue}"  type="video">
                                                        </video>-->
                                                        <video   controls width="100%" height="" poster="../../../theme/${theme}/images/voiceBg.png">
                                                            <source src="/file/${document.documentDataMap.productGallery.dataValue}" />
                                                            <embed src="/file/${document.documentDataMap.productGallery.dataValue}"></embed>
                                                            <!--</object>-->
                                                        </video>
                                                    </div>
                                                </c:if>
                                                <c:if test="${fn:contains(theString, '.jpg')}">
                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                        <!--<a href="/file/${document.documentDataMap.productGallery.dataValue}" rel="lightbox" style="margin: 0 auto;">-->
                                                            <img style="width:100%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${document.documentDataMap.productGallery.dataValue}" data-original="/file/${document.documentDataMap.productGallery.dataValue}" alt="" rel="lightbox">
                                                        <!--</a>-->
                                                    </div>
                                                </c:if>
                                                <c:if test="${fn:contains(theString, '.png')}">
                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                        <!--<a href="/file/${document.documentDataMap.productGallery.dataValue}" rel="lightbox" style="margin: 0 auto;">-->
                                                            <img style="width:100%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${document.documentDataMap.productGallery.dataValue}" data-original="/file/${document.documentDataMap.productGallery.dataValue}"  alt="" rel="lightbox">
                                                        <!--</a>-->
                                                    </div>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <c:choose>
                                        <c:when test="${document.documentDataMap.payFlag.dataValue == 'allPurchase'}">
                                            <!--<div class="entry" itemprop="articleBody">-->
                                                <!--<p>
                                                    <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                </p>-->
                                                <p style="margin: 0 15px;">${document.content}</p>
                                                <div style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;" >
                                                    <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">内容收费</div>
                                                     <!--<a href="/user/login.shtml" id="dltz">登录</a> <span id="and">且</span>-->
                                                    亲，您必须 <a href="#paydown"  style='color: #ffffff;background: #ff8200;display: inline-block;width: 50px;height: 26px;text-align: center;border-radius: 4px;'>购买</a> 后<b>（此内容vip、普通用户均需购买）</b>才可以阅读收费内容！
                                                </div>  
                                                <div class="paydown" id="paydown" style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;background:#ffffff;">
                                                    <!--<div class="down-title">资<br>源<br>购<br>买</div>-->
                                                    <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">内容详情</div>
                                                    <div class="down-detail" style="border:0;">
                                                        <p class="down-price" style="color:#fdb92d;">价格：<span><c:set var="money"  value="${fn:substringBefore(document.documentDataMap.PRICE_PRICE_STANDARD.dataValue,'&coin')}"/>${fn:substringAfter(money,'money=')}</span>&nbsp;元</p>
                                                        <c:choose>
                                                            <c:when test="${document.documentDataMap.vipLevel.dataValue == 2}">
                                                                <p class="down-ordinary"  style="color:#fdb92d;"><u>普通购买通道(原价 ${document.documentDataMap.productMarketPrice.dataValue}元)</u>
                                                                    <input type="hidden" name="" value="${documentCode}" id="documentCode">
                                                                    <a onclick='buy_now()'  style='color: #ffffff;font-weight: 100;background: #ff8200;display: inline-block;width: 50px;height: 26px;text-align: center;border-radius: 4px;'>购买</a>
                                                                    <input type="button" value="加入购物车" id="cart" onclick="cart()" style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 80px;height: 26px;text-align: center;border: 0;margin-bottom: 3px;'>
                                                                </p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p class="down-ordinary"  style="color:#fdb92d;"><u>普通购买通道(原价 ${document.documentDataMap.productMarketPrice.dataValue}元)</u>
                                                                    <input type="hidden" name="" value="${documentCode}" id="documentCode">
                                                                    <a onclick='buy_now()' style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 50px;height: 26px;text-align: center;border-radius: 4px;'>购买</a>
                                                                    <input type="button" value="加入购物车" id="cart" onclick="cart()" style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 85px;height: 26px;text-align: center;border: 0;margin-bottom: 3px;'>
                                                                </p>
                                                                <p class="down-vip"  style="color:#fdb92d;text-decoration:underline;">亲,升级VIP会员可免费阅读此课程内容 <a href="/user/purchasedList.shtml?flag=vip" style='font-weight: 100;color: #ffffff; background:url(../../../theme/${theme}/images/headvip.png) no-repeat #ff8200;display: inline-block;width: 140px;height: 26px;text-align: center;border-radius: 4px;background-position-x: 5px;background-position-y:5px;background-size:20px;'>马上升级VIP</a>还有VIP会员专享课程哦！<a href="http://me.lerongyun.com/content/onlyvip/index.shtml" class="vipkc">VIP课程</a></p>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
                                                        <!--<p class="down-tip">提示：内容制作不易，适当收费请理解！此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="#" style="color:#ff8200;">点此查看购买指南</a></p>
                                                        <p class="down-tip">要联系在线老师， <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}" style="color:#ff8200;">请点此给TA发站内信</a></p>-->
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                                <p class="down-tip" style="padding:5px 15px;">提示：内容制作不易，适当收费请理解！此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="/about/purchaseGuide.shtml" style="color:f47c00;"><b>点此查看购买指南</b></a> 要联系作者，
                                                    <!-- <a target="_blank" rel="nofollow" href="" ><b>请点此给TA发站内信</b></a> -->
                                                    <a class="qqzxzx" href="tencent://message/?uin=3268551923&Site=http://vps.shuidazhe.com&Menu=yes" style="color:f47c00;">
                                                        <img src="../../../theme/${theme}/images/qq.PNG" style="width: 32px; margin: 0; padding: 0; vertical-align: bottom;"><b>在线咨询</b>
                                                    </a>    
                                                </p>
                                                <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #ffffff;background: #4e4e4e;display: inline-block;width: 100px;height: 26px;text-align: center;'>七星指弹教室</a> 的吉他谱和教学视频！</div>
                                            <!--</div>     -->
                                        </c:when>
                                        <c:when test="${document.documentDataMap.payFlag.dataValue == 'vipOnly'}">
                                            <!--<div class="entry" itemprop="articleBody">-->
                                                <!--<p>
                                                    <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                </p>  -->
                                                <p style="margin: 0 15px;">${document.content}</p>
                                                <div style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;">
                                                    <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">内容收费</div>
                                                    <!--<a href="/user/login.shtml" id="dltz">登录</a> <span id="and">且</span>-->
                                                    亲，升级VIP会员可免费阅读此课内容  <a href="/user/purchasedList.shtml?flag=vip" style="font-weight: 100;display: inline-block;width:153px;height: 27px;text-align: center;background: url(../../../theme/${theme}/images/headvip.png) no-repeat #ff8200;color: #ffffff;border-radius: 4px;background-position-x: 5px;background-position-y:5px;background-size:20px;">马上升级为VIP</a> 还有VIP会员专享课程哦！<a href="http://me.lerongyun.com/content/onlyvip/index.shtml" class="vipkc">VIP课程</a>
                                                </div>  
                                                <!--<div class="paydown" id="paydown">
                                                    <div class="down-title">资<br>源<br>购<br>买</div>
                                                    <div class="down-detail">
                                                        <p class="down-price">价格：<span>${document.documentDataMap.productMarketPrice.dataValue}</span>&nbsp;元</p>
                                                        <p class="down-vip">亲，VIP会员免费，您还不是VIP会员，<a href="/content/user/20170515130841.shtml">马上升级VIP</a></p>-->
                                                        <!--<p class="down-ordinary">亲，这是普通购买通道（原价 ${document.documentDataMap.productMarketPrice.dataValue}元），
                                                            <input type="hidden" name="" value="${documentCode}" id="documentCode">
                                                            <a onclick='buy_now()'>立即购买</a>
                                                            <input type="button" value="加入购物车" id="cart" onclick="cart()">
                                                        </p> -->
                                                        <!--<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
                                                        <p class="down-tip">提示：此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="#">点此查看购买指南</a></p>
                                                        <p class="down-tip">要联系作者，请 <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}">点此给TA发站内信</a></p>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>-->
                                                <p class="down-tip" style="padding:5px 15px;">提示：内容制作不易，适当收费请理解！此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="/about/purchaseGuide.shtml" style="color:f47c00;"><b>点此查看购买指南</b></a> 要联系作者，
                                                    <!-- <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}" style="color:f47c00;"><b>请点此给TA发站内信</b></a> -->
                                                    <a class="qqzxzx" href="tencent://message/?uin=3268551923&Site=http://vps.shuidazhe.com&Menu=yes" style="color:f47c00;">
                                                        <img src="../../../theme/${theme}/images/qq.PNG" style="width: 32px; margin: 0; padding: 0; vertical-align: bottom;"><b>在线咨询</b>
                                                    </a>   
                                                </p>     
                                                <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #ffffff;background: #4e4e4e;display: inline-block;width: 100px;height: 26px;text-align: center;'>七星指弹教室</a> 的吉他谱和教学视频！</div>
                                            <!--</div>-->
                                        </c:when> 
                                        <c:when test="${document.documentDataMap.payFlag.dataValue == 'vip'}">
                                            <!--<div class="entry" itemprop="articleBody">-->
                                                <!--<p>
                                                    <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                </p> -->
                                                <p  style="margin: 0 15px;">${document.content}</p>
                                                <div style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;">
                                                    <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">收费内容</div>
                                                    <!--<a href="/user/login.shtml" id="dltz">登录</a> <span id="and">且</span>-->
                                                    亲，升级VIP会员可免费阅读此课内容 <a href="/user/purchasedList.shtml?flag=vip" style="display: inline-block;width:153px;height: 27px;text-align: center;background: url(../../../theme/${theme}/images/headvip.png) no-repeat #ff8200;color: #ffffff;background-position-x: 5px;background-position-y:5px;background-size:20px;">马上升级为VIP</a> 还有VIP会员专享课程哦！<a href="http://me.lerongyun.com/content/onlyvip/index.shtml" class="vipkc">VIP课程</a>
                                                </div>
                                                <div class="paydown" id="paydown" style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;background:#ffffff;">
                                                    <!--<div class="down-title">资<br>源<br>购<br>买</div>-->
                                                    <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">内容详情</div>
                                                    <div class="down-detail" style="border:0;">
                                                        <p class="down-price"  style="color:#fdb92d;">价格：<span><c:set var="money"  value="${fn:substringBefore(document.documentDataMap.PRICE_PRICE_STANDARD.dataValue,'&coin')}"/>${fn:substringAfter(money,'money=')}</span>&nbsp;元</p>
                                                        <p class="down-ordinary"  style="color:#fdb92d;"><u>普通购买通道(原价 ${document.documentDataMap.productMarketPrice.dataValue}元)</u>
                                                            <input type="hidden" name="" value="${documentCode}" id="documentCode">
                                                            <a onclick='buy_now()' style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 50px;height: 26px;text-align: center;border-radius: 4px;'>购买</a>
                                                            <input type="button" value="加入购物车" id="cart" onclick="cart()" style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 80px;height: 26px;text-align: center;border: 0;margin-bottom: 3px;'>
                                                        </p>
                                                        <p class="down-vip"  style="color:#fdb92d;text-decoration:underline;">亲，升级VIP会员可免费阅读此课内容 <a href="/user/purchasedList.shtml?flag=vip" style='font-weight: 100;color: #ffffff;background: url(../../../theme/${theme}/images/headvip.png) no-repeat #ff8200;display: inline-block;width: 140px;height: 26px;text-align: center;border-radius: 4px;background-position-x: 5px;background-position-y:5px;background-size:20px;'>马上升级VIP</a> 还有VIP会员专享课程哦！<a href="http://me.lerongyun.com/content/onlyvip/index.shtml" class="vipkc">VIP课程</a></p>
                                                        <input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">   
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                                <p class="down-tip" style="padding:5px 15px;">提示：内容制作不易，适当收费请理解！此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="/about/purchaseGuide.shtml" style="color:f47c00;"><b>点此查看购买指南</b></a>要联系作者，
                                                    <!-- <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}" style="color:f47c00;"><b>请点此给TA发站内信</b></a> -->
                                                    <a class="qqzxzx" href="tencent://message/?uin=3268551923&Site=http://vps.shuidazhe.com&Menu=yes" style="color:f47c00;">
                                                        <img src="../../../theme/${theme}/images/qq.PNG" style="width: 32px; margin: 0; padding: 0; vertical-align: bottom;"><b>在线咨询</b>
                                                    </a>   
                                                </p>
                                             <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #ffffff;background: #4e4e4e;display: inline-block;width: 100px;height: 26px;text-align: center;'>七星指弹教室</a> 的吉他谱和教学视频！</div>
                                            <!--</div>-->
                                        </c:when>
                                        <c:when test="${document.documentDataMap.payFlag.dataValue == 'notLogin'}">
                                            <!--<div class="entry" itemprop="articleBody">-->
                                                <!--<p>
                                                    <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                </p>  -->
                                                <p  style="margin: 0 15px;">${document.content}</p>
                                                <c:choose>
                                                    <c:when test="${empty document.documentDataMap.productMarketPrice}">

                                                    </c:when>
                                                    <c:when test="${document.documentDataMap.productMarketPrice.dataValue == '0'}">

                                                    </c:when>
                                                    <c:when test="${document.documentDataMap.productMarketPrice.dataValue == '1.0'}">
                                                    <!--<div class="entry" itemprop="articleBody">-->
                                                        <!--<p>
                                                            <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                        </p>  -->
                                                        <p style="margin: 0 15px;">${document.content}</p>
                                                        <div style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;">
                                                            <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">内容收费</div>
                                                            <!--<a href="/user/login.shtml" id="dltz">登录</a> <span id="and">且</span>-->
                                                            亲，升级VIP会员可免费阅读此课内容  <a href="/user/purchasedList.shtml?flag=vip" style="font-weight: 100;display: inline-block;width:153px;height: 27px;text-align: center;background: url(../../../theme/${theme}/images/headvip.png) no-repeat #ff8200;color: #ffffff;border-radius: 4px;background-position-x: 5px;background-position-y:5px;background-size:20px;">马上升级为VIP</a> 还有VIP会员专享课程哦！<a href="http://me.lerongyun.com/content/onlyvip/index.shtml" class="vipkc">VIP课程</a>
                                                        </div>  
                                                        <!--<div class="paydown" id="paydown">
                                                            <div class="down-title">资<br>源<br>购<br>买</div>
                                                            <div class="down-detail">
                                                                <p class="down-price">价格：<span>${document.documentDataMap.productMarketPrice.dataValue}</span>&nbsp;元</p>
                                                                <p class="down-vip">亲，VIP会员免费，您还不是VIP会员，<a href="/content/user/20170515130841.shtml">马上升级VIP</a></p>-->
                                                                <!--<p class="down-ordinary">亲，这是普通购买通道（原价 ${document.documentDataMap.productMarketPrice.dataValue}元），
                                                                    <input type="hidden" name="" value="${documentCode}" id="documentCode">
                                                                    <a onclick='buy_now()'>立即购买</a>
                                                                    <input type="button" value="加入购物车" id="cart" onclick="cart()">
                                                                </p> -->
                                                                <!--<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
                                                                <p class="down-tip">提示：此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="#">点此查看购买指南</a></p>
                                                                <p class="down-tip">要联系作者，请 <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}">点此给TA发站内信</a></p>
                                                            </div>
                                                            <div class="clear"></div>
                                                        </div>-->
                                                        <p class="down-tip" style="padding:5px 15px;">提示：内容制作不易，适当收费请理解！此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="/about/purchaseGuide.shtml" style="color:f47c00;"><b>点此查看购买指南</b></a> 要联系作者，
                                                            <!-- <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}" style="color:f47c00;"><b>请点此给TA发站内信</b></a> -->
                                                            <a class="qqzxzx" href="tencent://message/?uin=3268551923&Site=http://vps.shuidazhe.com&Menu=yes" style="color:f47c00;">
                                                                <img src="../../../theme/${theme}/images/qq.PNG" style="width: 32px; margin: 0; padding: 0; vertical-align: bottom;"><b>在线咨询</b>
                                                            </a>   
                                                        </p>     
                                                     <!--</div>-->
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="paydown" id="paydown" style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;background:#ffffff;">
                                                            <!--<div class="down-title">资<br>源<br>购<br>买</div>-->
                                                            <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#fdb92d;">内容详情</div>
                                                            <div class="down-detail" style="border:0;">
                                                                <p class="down-price"  style="color:#fdb92d;">价格：<span><c:set var="money"  value="${fn:substringBefore(document.documentDataMap.PRICE_PRICE_STANDARD.dataValue,'&coin')}"/>${fn:substringAfter(money,'money=')}</span>&nbsp;元</p>
                                                                <p class="down-ordinary"  style="color:#fdb92d;"><u>普通购买通道(原价 ${document.documentDataMap.productMarketPrice.dataValue}元)</u>
                                                                    <input type="hidden" name="" value="${documentCode}" id="documentCode">
                                                                    <a onclick='buy_now()' style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 50px;height: 26px;text-align: center;border-radius: 4px;'>购买</a>
                                                                    <input type="button" value="加入购物车" id="cart" onclick="cart()" style='font-weight: 100;color: #ffffff;background: #ff8200;display: inline-block;width: 80px;height: 26px;text-align: center;border: 0;margin-bottom: 3px;'>
                                                                </p>
                                                                <p class="down-vip"  style="color:#fdb92d;text-decoration:underline;">亲，升级VIP会员可免费阅读此课内容 <a href="/user/purchasedList.shtml?flag=vip" style='font-weight: 100;color: #ffffff;background: url(../../../theme/${theme}/images/headvip.png) no-repeat #ff8200;display: inline-block;width: 140px;height: 26px;text-align: center;border-radius: 4px;background-position-x: 5px;background-position-y:5px;background-size:20px;'>马上升级VIP</a>还有VIP会员专享课程哦！<a href="http://me.lerongyun.com/content/onlyvip/index.shtml" class="vipkc">VIP课程</a></p>
                                                                <input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">   
                                                            </div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <p class="down-tip" style="padding:5px 15px;">提示：内容制作不易，适当收费请理解！此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="/about/purchaseGuide.shtml" style="color:f47c00;"><b>点此查看购买指南</b></a>要联系作者，
                                                            <!-- <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?defaultReceiver=${document.author}" style="color:f47c00;"><b>请点此给TA发站内信</b></a> -->
                                                            <a class="qqzxzx" href="tencent://message/?uin=3268551923&Site=http://vps.shuidazhe.com&Menu=yes" style="color:f47c00;">
                                                                <img src="../../../theme/${theme}/images/qq.PNG" style="width: 32px; margin: 0;    padding: 0; vertical-align: bottom;"><b>在线咨询</b>
                                                            </a>   
                                                        </p>
                                                    </c:otherwise>
                                                </c:choose>
                                                <!--<div style="text-align:center;border:1px solid #fdb92d;padding:8px;margin:30px 15px;color:#fdb92d;">-->
                                                    <!--<div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#FF6666;">收费内容</div>-->
                                                    <!--亲，您必须 <a href="/user/login.shtml" id="dltz"  style="display: inline-block;width:50px;height: 27px;text-align: center;background: #ff8200;color: #ffffff;border-radius: 4px;">登录</a> 后才可以阅读内容！
                                                </div>-->
                                                <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #ffffff;background: #4e4e4e;display: inline-block;width: 100px;height: 26px;text-align: center;'>七星指弹教室</a> 的吉他谱和教学视频！</div>
                                            <!--</div>-->
                                        </c:when>
                                        <c:otherwise>
                                            <!--<div class="entry" itemprop="articleBody">-->
                                                    <!--<p>
                                                        <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                    </p>-->
                                                    <p  style="margin: 0 15px;">${document.content}</p>
                                                    <p style="cursor: pointer">
                                                        <c:set var="theString" value="${document.documentDataMap.subscribeContent.dataValue}"/>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(theString, ',')}">
                                                                <c:set value="${fn:split(theString, ',') }" var="names" />
                                                                <c:forEach items="${names}" var="name">
                                                                        <c:if test="${fn:contains(name, '.mp4')}">
                                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                                <video width="100%" height="" controls   style="margin: 0 auto;display: block;" poster="../../../theme/${theme}/images/voiceBg.png">
                                                                                    <source src="/file/${name}"  type="video/mp4">
                                                                                </video>
                                                                            </div>
                                                                        </c:if>
                                                                        <c:if test="${fn:contains(name, '.f4v')}">
                                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                                <!--<video width="100%" height="500" controls  style="margin: 0 auto;display: block;">
                                                                                    <source src="/file/${name}"  type="video">
                                                                                </video>-->
                                                                                <video   controls width="100%" height="" poster="../../../theme/${theme}/images/voiceBg.png">
                                                                                    <source src="/file/${name}" />
                                                                                    <embed src="/file/${name}"></embed>
                                                                                    <!--</object>-->
                                                                                </video>
                                                                            </div>
                                                                        </c:if>
                                                                        <c:if test="${fn:contains(name, '.jpg')}">
                                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                                <!--<a href="/file/${name}" rel="lightbox" style="margin: 0 auto;">-->
                                                                                    <img style="width:100%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${name}"  data-original="/file/${name}" alt="" rel="lightbox">
                                                                                <!--</a>-->
                                                                            </div>
                                                                        </c:if>
                                                                        <c:if test="${fn:contains(name, '.png')}">
                                                                            <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                                <!--<a href="/file/${name}" rel="lightbox" style="margin: 0 auto;">-->
                                                                                    <img style="width:100%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${name}"  data-original="/file/${name}"  alt="" rel="lightbox">
                                                                                <!--</a>-->
                                                                            </div>
                                                                        </c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${fn:contains(theString, '.mp4')}">
                                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                        <video width="100%" height="" controls  style="margin: 0 auto;display: block;" poster="../../../theme/${theme}/images/voiceBg.png">
                                                                            <source src="/file/${document.documentDataMap.subscribeContent.dataValue}"  type="video/mp4">
                                                                        </video>
                                                                    </div>
                                                                </c:if>
                                                                <c:if test="${fn:contains(theString, '.f4v')}">
                                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                        <!--<video width="100%" height="500" controls  style="margin: 0 auto;display: block;">
                                                                            <source src="/file/${document.documentDataMap.subscribeContent.dataValue}"  type="video">
                                                                        </video>-->
                                                                                <video   controls width="100%" height="" poster="../../../theme/${theme}/images/voiceBg.png">
                                                                                    <source src="/file/${document.documentDataMap.subscribeContent.dataValue}" />
                                                                                    <embed src="/file/${document.documentDataMap.subscribeContent.dataValue}"></embed>
                                                                                    <!--</object>-->
                                                                                </video>
                                                                    </div>
                                                                </c:if>
                                                                <c:if test="${fn:contains(theString, '.jpg')}">
                                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                        <!--<a href="/file/${document.documentDataMap.subscribeContent.dataValue}" rel="lightbox" style="margin: 0 auto;">-->
                                                                            <img style="width:100%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${document.documentDataMap.subscribeContent.dataValue}" data-original="/file/${document.documentDataMap.productGallery.dataValue}"  alt="">
                                                                        <!--</a>-->
                                                                    </div>
                                                                </c:if>
                                                                <c:if test="${fn:contains(theString, '.png')}">
                                                                    <div style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                                                        <!--<a href="/file/${document.documentDataMap.subscribeContent.dataValue}" rel="lightbox" style="margin: 0 auto;">-->
                                                                            <img style="width:100%;height: auto;margin: 0 auto;display: block;" data-lazy-src="/file/${document.documentDataMap.subscribeContent.dataValue}" data-original="/file/${document.documentDataMap.productGallery.dataValue}"  alt="">
                                                                        <!--</a>-->
                                                                    </div>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <c:choose>
                                                        <c:when test="${node.name == '所有作品' || node.name == '弹唱作品' || node.name == '指弹作品' || node.name == '原创作品' }">
                                                            <div class="ding" onclick="zan()">
                                                                <a href="javascript:;"  class="recommend favorite" title="感觉作品不错，赞一下他吧！">
                                                                    <i class="icon-thumbs-up-alt"></i> 点赞 <span class="count">
                                                                        <c:choose>
                                                                            <c:when test="${empty praiseCount}">
                                                                                0
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                ${praiseCount}
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </span>
                                                                </a>
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>

                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #ffffff;background: #4e4e4e;display: inline-block;width: 100px;height: 26px;text-align: center;'>七星指弹教室</a> 的吉他谱和教学视频！</div>
                                            <!--</div>-->
                                        </c:otherwise>    
                                    </c:choose>
                                    </div>
                                    
                                    <div  style="border:1px dashed #cdcdcd;width: 95%;" class="imgMargin">
                                            <!-- <a href="/content/about/qixingbook.shtml"><img src="../../../theme/${theme}/images/detailBook.gif" alt="" style="width:100%;height:auto;"></a> -->  
                                            <a href="/content/newbie/index.shtml" style="display: inline-block;width: 32.5%;font-size: 23px;line-height: 130px;text-align: center;">
                                                <b>入门课程</b>
                                            </a><a href="/content/finger/index.shtml" style="display: inline-block;width: 32.5%;font-size: 23;line-height:130px;text-align: center;">
                                                <b>免费指弹</b>
                                            </a><a href="/content/vip/index.shtml" style="display: inline-block;width: 32.5%;font-size: 23;line-height: 130px;text-align: center;position: relative;">
                                                <b>付费指弹</b>
                                                <span style="position: absolute;top: 50%;width: 100%;display: inline-block;text-align: center;line-height: 60px;font-size: 18px;left: 0;">vip会员免费</span>
                                            </a><a href="/content/onlyvip/index.shtml" style="display: inline-block;width: 32.5%;font-size: 23;line-height: 130px;text-align: center;position: relative;">
                                                <b>VIP专区</b>
                                                <span style="position: absolute;top: 50%;width: 100%;display: inline-block;text-align: center;line-height: 60px;font-size: 18px;left: 0;">vip会员课程</span>
                                            </a><a href="/content/study/index.shtml" style="display: inline-block;width: 32.5%;font-size: 23;line-height: 130px;text-align: center;">
                                                <b>学习文章</b>
                                            </a><a href="/content/highfinger/song/index.shtml" style="display: inline-block;width: 32.5%;font-size: 23;line-height: 130px;text-align: center;">
                                                <b>指弹练习</b>
                                            </a>
                                    </div>
                                    <input type="hidden" name="uuid" value="${frontUser.uuid}">
                                    <input type="hidden" name="objectType"  value="document">
                                    <input type="hidden" name="objectId" value="${document.udid}"  id="udid">
                                    <footer class="entry-meta">
                                    <div class='wpfp-span'>
                                        <img data-lazy-src='../../../theme/${theme}/images/lovera.png' alt='Loading' title='Loading' class='wpfp-hide wpfp-img' />
                                        <span title="只有已登录用户才能收藏文章，请先登录/注册。"><i class="icon-heart add"></i> 
                                            <div class="poptip">
                                            <span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span>
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
                                </div>-->
                                <!--<div class="gpost-below">
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
                                                    <img data-lazy-src="../../../theme/${theme}/images/LOGO.png" alt="" height="64" width="64" class="avatar"> </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <img data-lazy-src="/file/client/${uploadAuthor.userHeadPic}" alt="" height="64" width="64" class="avatar"> </div>
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

                    
                <!-- 引入picbox.css -->
                <!--<link rel="stylesheet" href="../../../theme/${theme}/css/picbox.css" type="text/css" media="screen">-->
                <!-- 引入picbox.js -->
                <!--<script type="text/javascript" src="../../../theme/${theme}/js/picbox.js"></script>-->
                <!--view.min.js引入-->
                <script type="text/javascript" src="../../../theme/${theme}/js/viewer.min.js"></script>

                <script>
                    $(function(){
                        // 禁止图片拖动跳转
                        var $img = $(".entry img");
                        var moving = function(event){
                            //something
                        }

                        //IE下需要在document的mousemove里面取消默认事件;要用原生JS的事件不能用JQuery
                        document.onmousemove = function(e){
                            var ev = e || event;
                            ev.cancelBubble=true;
                            ev.returnValue = false;
                        }
                        
                        $img.mousedown(function(event){
                            //FF下需要在mousedown取消默认操作;
                            event.preventDefault();
                            event.stopPropagation();
                            $(this).bind("mousemove",moving);    
                        })

                        // 点击图片放大逻辑
                        var viewer = new Viewer(document.getElementById('dowebok'), {
                            url: 'data-original',
                            zIndex:999999,
                            zoomable:false,
                            movable:false,
                             show: function() {
                            var x = 0;
                            $('#top-bar').css('z-index','0');
                            $('#rightindex').css('z-index','0');
                            $('#asid_share').css('z-index','0');
                            $('#main-nav').css('z-index','1');
                            var a = false;
                            // if(a){                              
                            //     $('.viewer-toolbar li').each(function(a,b){
                            //         if(a==4 || a == 5 || a == 6){

                            //         }else{
                            //             $(this).one().remove();
                            //         }
                            //     })
                            //     a = false;
                            // }
                             // 手机浏览 图片点击缩小
                            if($(window).width()<768) {  
                                console.log('手机')
                                $('.viewer-button').css({'opacity':'0','top':'0','right':'0','width':'100%','height':'80%','border-radius':'0'})
                            }  
                            // 加上一个下载图标
                            if ($('.viewer-toolbar li').length==11) {
                                $('.viewer-toolbar').append(`<li class='xiazaiTu'><img style='width:18px;height:18px;margin:2px auto;' src='../../../theme/${theme}/images/xiazai.png' /></li>`);
                            }
                            
                            $('.xiazaiTu').on('click',function(){
                                var src = $(this).parent().parent().parent().find('.viewer-canvas').find('img').attr('src');
                                // console.log(src)
                                download(src)
                            })

                                var scrollFunc = function (e) {  
                                e = e || window.event;  
                                if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件               
                                    if (e.wheelDelta > 0) { //当滑轮向上滚动时  
                                        x = x - 5 
                                        $('.viewer-canvas').scrollTop(x)
                                    }  
                                    if (e.wheelDelta < 0) { //当滑轮向下滚动时  
                                        x = x + 5 
                                        $('.viewer-canvas').scrollTop(x)
                                    }  
                                } else if (e.detail) {  //Firefox滑轮事件  
                                    if (e.detail> 0) { //当滑轮向上滚动时 
                                        x = x - 5  
                                        $('.viewer-canvas').scrollTop(x--) 
                                    }  
                                    if (e.detail< 0) { //当滑轮向下滚动时 
                                        x = x + 5 
                                        $('.viewer-canvas').scrollTop(x) 
                                    }  
                                }  
                            }  
                            //给页面绑定滑轮滚动事件  
                            if (document.addEventListener) {//firefox  
                                document.addEventListener('DOMMouseScroll', scrollFunc, false);  
                            }  
                            //滚动滑轮触发scrollFunc方法  //ie 谷歌  
                            window.onmousewheel = document.onmousewheel = scrollFunc;   
                        }
                       
                        });
                        // var a = true;
                        // $('#dowebok').viewer({
                        //     url: 'data-original',
                        //     zIndex:999999,
                        //     zoomable:false,
                        //     movable:false,
                        //     show: function() {
                        //         var x = 0;
                        //         $('#top-bar').css('z-index','0');
                        //         $('#rightindex').css('z-index','0');
                        //         $('#asid_share').css('z-index','0');
                        //         $('#main-nav').css('z-index','1');
                        //         if(a){                              
                        //             $('.viewer-toolbar li').each(function(a,b){
                        //                 if(a==4 || a == 5 || a == 6){

                        //                 }else{
                        //                     $(this).one().remove();
                        //                 }
                        //             })
                        //             a = false;
                        //         }
                        //          // 手机浏览 图片点击缩小
                        //         if($(window).width()<768) {  
                        //             console.log('手机')
                        //             $('.viewer-button').css({'opacity':'0','top':'0','right':'0','width':'100%','height':'80%','border-radius':'0'})
                        //         }  
                        //         // 加上一个下载图标
                        //         if ($('.viewer-toolbar li').length==3) {
                        //             $('.viewer-toolbar').append(`<li class='xiazaiTu'><img style='width:18px;height:18px;margin:2px auto;' src='../../../theme/${theme}/images/xiazai.png' /></li>`);
                        //         }
                                
                        //         $('.xiazaiTu').on('click',function(){
                        //             var src = $(this).parent().parent().parent().find('.viewer-canvas').find('img').attr('src');
                        //             download(src)
                        //         })

                        //             var scrollFunc = function (e) {  
                        //             e = e || window.event;  
                        //             if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件               
                        //                 if (e.wheelDelta > 0) { //当滑轮向上滚动时  
                        //                     x = x - 5 
                        //                     $('.viewer-canvas').scrollTop(x)
                        //                 }  
                        //                 if (e.wheelDelta < 0) { //当滑轮向下滚动时  
                        //                     x = x + 5 
                        //                     $('.viewer-canvas').scrollTop(x)
                        //                 }  
                        //             } else if (e.detail) {  //Firefox滑轮事件  
                        //                 if (e.detail> 0) { //当滑轮向上滚动时 
                        //                     x = x - 5  
                        //                     $('.viewer-canvas').scrollTop(x--) 
                        //                 }  
                        //                 if (e.detail< 0) { //当滑轮向下滚动时 
                        //                     x = x + 5 
                        //                     $('.viewer-canvas').scrollTop(x) 
                        //                 }  
                        //             }  
                        //         }  
                        //         //给页面绑定滑轮滚动事件  
                        //         if (document.addEventListener) {//firefox  
                        //             document.addEventListener('DOMMouseScroll', scrollFunc, false);  
                        //         }  
                        //         //滚动滑轮触发scrollFunc方法  //ie 谷歌  
                        //         window.onmousewheel = document.onmousewheel = scrollFunc;   
                        //     },
                           
                        //     shown:function(){
                        //         $('.viewer-container').css('z-index','999999');
                        //         $('.viewer-canvas').css('z-index','0');
                        //         //$('.viewer-canvas img').css('margin-top','0!important');
                        //         $('.viewer-canvas>img').css({'max-width':'100%!important','max-height':'100%!important','transform': 'rotate(0deg) scale(1, 1) translate(-50%,-50%)','position':'absolute','top':'50%','left':'50%','width':'auto!important','height':'auto!important','margin':'0','z-index':'0'});
                                
                                
                        //     },
                        //     hide:function(){
                        //         $('.viewer-canvas>img').css({'z-index':'-99999','transform': 'rotate(0deg) scale(0, 0) translate(100%,100%)'});
                        //         $('.viewer-container').css('z-index','-99999');
                        //         $('.viewer-canvas').css('z-index','-99999');
                        //         $('#top-bar').css('z-index','99');
                        //         $('#asid_share').css('z-index','2');
                        //         $('#main-nav').css('z-index','2');
                                
                        //     },
                        //     viewed:function(){
                        //         $('.viewer-container').css('z-index','9999999');
                        //         $('.viewer-canvas').css('z-index','0');
                        //         //$('.viewer-canvas img').css('margin-top','0!important');
                        //         $('.viewer-canvas>img').css({'max-width':'100%!important','max-height':'100%!important','transform': 'rotate(0deg) scale(1, 1) translate(-50%,-50%)','position':'absolute','top':'50%','left':'50%','width':'auto!important','height':'auto!important','margin':'0','z-index':'0'});
                                
                        //     }
                        // });
                        
       
                    //})
                function download(src) {
                    var $a = $("<a></a>").attr("href", src).attr("download", "img.png");
                    $a[0].click();
                }
            })
                </script>