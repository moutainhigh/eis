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
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script>                
                $(function(){
                     if(jQuery.cookie('eis_username') == null){
                           $('.entry').empty().append('<div class="wpuf-info" style="margin:23px;">该页面只允许已登录用户访问，请登录/注册后访问。</div>')
                     }else{
                         return;
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
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">VIP订购</span></div>
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
                                    <div class="user-avatar">
                                            <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li><a href="/user/myUploadProducts.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li>
                                            <a href="/user/myFavoriteList.shtml"><span class="icon-heart"></span>我的收藏</a></li>
                                            <!--/content/user/20170515125420.shtml?preview=525940543BC2641DD422CF13501B4C719DA3DCEE9232775527AB4BF88D22B1E8&vPro=purchaseList-->
                                        <li><a href="/user/purchasedList.shtml"><span class="icon-shopping-cart"></span>已购教学</a></li>
                                        <li class="current-menu-item"><a href="/content/user/20170515130841.shtml?preview=8522EDBB6A0A0CA2A9B67FC2577D40F50A85647ECD689EE49012C6DD73057B20&vPro=purchaseList" style='background: url("../../../theme/${theme}/images/greengift.png") no-repeat;
                background-position-y: 4px;
                background-size: 11px;'><span class="icon-gift"></span>VIP订购</a></li>
                                        <li><a href="/content/user/20170515133906.shtml?preview=7991CF14C46EED8461848DB054D7ABEC9B7A9BDB109B333D5E2640B2950E1C95"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/content/user/20170515141440.shtml?preview=FA5F1259136D6FA8994C28370FDC834C232C6A10B52F6FD05056CFC0C721EB65"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope itemtype="http://schema.org/Article">
                                    <div id="post-header">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">VIP订购</h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody">
                                        <div class="wrap">
                                            <form method="post" action="" id="buyForm">
                                                <c:if test="${document.documentDataMap.vipLevel.dataValue == 2}">
                                                    <p style="font-size:16px;color:red;">尊敬的VIP会员！点击查看 <a href="/content/help/FeeCourseService.shtml">VIP用户价格与服务条款</a></p>
                                                </c:if>
                                                <c:if test="${document.documentDataMap.vipLevel.dataValue != 2}">
                                                    <p style="font-size:16px;color:red;">您目前不是本站VIP会员！点击查看 <a href="/content/help/FeeCourseService.shtml">VIP用户价格与服务条款</a></p>
                                                </c:if>
                                                <!--<p>您目前的账户余额为 <strong>0.00</strong>。<a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D">给账户充值</a></p>-->
                                                <h2 style='border-bottom: 1px solid #f2f2f2;margin:15px 0;padding-left:15px;'>购买VIP服务</h2>
                                                <table class="form-table">
                                                    <tr>
                                                        <td valign="top" width="30%" style='padding-left:15px'>VIP类型
                                                        </td>
                                                        <td>
                                                            <!-- <input type="radio" id="userType" name="userType" value="10" checked />终身VIP会员 --- 元<br /> -->
                                                            <input type="radio" class="userType" name="userType" value="VIP_YEAR" />年度VIP会员 --- 168元<br />
                                                            <input type="radio" class="userType" name="userType" value="VIP_QUARTER" />季度VIP会员 --- 48元<br />
                                                            <input type="radio" class="userType" name="userType" value="VIP_MONTH" />月度VIP会员 --- 18元 </td>
                                                    </tr>
                                                    <!--<tr>
                                                        <td valign="top" width="30%" style='padding-left:15px'>可用余额
                                                        </td>
                                                        <td id="moremoney">0.00元</td>
                                                    </tr>-->
                                                    <tr>
                                                        <td colspan="2" style='padding-left:15px'><input type="button"  value="确认购买" onclick="buy()" class="button-primary" />
                                                        <input type="hidden" value="${document.documentDataMap.vipLevel.dataValue}" id="vip">
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                        </div>
                                        <div class="clear"></div>
                                        <div class="wrap">
                                            <h2 style='border-bottom: 1px solid #f2f2f2;margin:15px 0;padding-left:15px;'>VIP订单查询</h2>
                                            <p>共有 <strong>${vipMap.vipPurchaseNum}</strong> 笔交易，总金额：<strong>${vipMap.vipPurchaseMoney}</strong></p>
                                            <table class="widefat">
                                                <thead>
                                                    <tr>
                                                        <th width="20%">购买时间</th>
                                                        <th width="15%">VIP类型</th>
                                                        <th width="15%">价格</th>
                                                        <th width="15%">到期时间</th>
                                                        <th width="15%">状态</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                        <c:choose>
                                                            <c:when test="${empty vipMap.vipPurchaseList}">
                                                                <tr>
                                                                    <td colspan="5" align="center">您还没有购买过VIP服务！</td>
                                                                </tr> 
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach var="item" items="${vipMap.vipPurchaseList}" varStatus="status">
                                                                <tr>
                                                                    <td  align="center" class="buyTime"><fmt:formatDate value="${item.startTime}" type="both"/></td>
                                                                    <td  align="center">${item.name}</td>
                                                                    <td  align="center">${item.realMoney}</td>
                                                                    <td  align="center"><fmt:formatDate value="${item.startTime}" type="both"/></td>
                                                                    <td align="center">已到期</td>
                                                                </tr> 
                                                                </c:forEach>  
                                                            </c:otherwise>
                                                        </c:choose>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="clear"></div>
                                        <p><br />
                                        </p>
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