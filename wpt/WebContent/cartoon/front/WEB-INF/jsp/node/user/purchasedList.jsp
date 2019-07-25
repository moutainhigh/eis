<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <c:choose>
                <c:when test="${flag == 'vip'}">
                    <TITLE>${systemName}-vip教学</TITLE>
                </c:when>
                <c:otherwise>
                    <TITLE>${systemName}-已购教学</TITLE>
                </c:otherwise>
            </c:choose> 
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
                           $('.entry').empty().append('<div class="wpuf-info" style="margin:23px;">该页面只允许已登录用户访问，请登录/注册后访问。</div>');
                     }else{
                         var img = jQuery.cookie('eis_userheadpic');
                        if(img == ''){
                            $('#headImg').attr('src','../../../theme/basic/images/touxiang.png');
                         }else{
                            $('#headImg').attr('src','/file/client/'+img);
                         }
                         
                        // 点击分页
                        var totalPages = $('input[name="totalPage"]').val();
                        console.log(totalPages)
                        var currentPage = 1;
                        // 点击首页
                        $('.pages ul li').eq(2).click(function(){
                            if(currentPage == 1){
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/user/purchasedList.json',
                                    data:{
                                        // rows:  行数   
                                        page:1
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        console.log(data);
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="${item.cartId}"><span class="seeOrder"><input type="hidden" value="${item.deliveryed}">查看订单</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                                currentPage = 1;
                            }
                        })
                        // 点击尾页
                        $('.pages ul li').eq(1).click(function(){
                            if(currentPage == totalPages ){
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/user/purchasedList.json',
                                    data:{
                                        // rows:  行数   
                                        page:totalPages
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="${item.cartId}"><span class="seeOrder"><input type="hidden" value="${item.deliveryed}">查看订单</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                                currentPage = totalPages;
                            }
                        })
                        // 点击下一页
                        $('.pages ul li').eq(0).click(function(){                            
                            currentPage++;
                            console.log(currentPage);
                            if(currentPage > totalPages){
                                alert('已经到了最后一页了！');
                                currentPage = totalPages;
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/user/purchasedList.json',
                                    data:{
                                        // rows:  行数   
                                        page:currentPage
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="${item.cartId}"><span class="seeOrder"><input type="hidden" value="${item.deliveryed}">查看订单</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                            }
                        })
                        // 点击上一页
                        $('.pages ul li').eq(3).click(function(){
                            currentPage--;
                            console.log(currentPage);
                            if(currentPage == 0){
                                alert('已经到了第一页了！');
                                currentPage = 1;
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/user/purchasedList.json',
                                    data:{
                                        // rows:  行数   
                                        page:currentPage
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="${item.cartId}"><span class="seeOrder"><input type="hidden" value="${item.deliveryed}">查看订单</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                            }
                        })
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
        <style>
                .wrap iframe{
                    width: 100%;
                    height: 50%;
                    border: 0;
                    border-bottom: 1px solid #E1E1E1;    
                    margin-top: 10px;
                }
                .my_message{
                    margin: 20px 0;
                    color:#000;
                }
                .my_product{
                    color:#000;
                    width: 100%;
                    border: 1px solid #e1e1e1;
                }
                .my_productmessage{
                    display: flex;
                    justify-content: space-between;
                }
                .my_productmessage_left{
                    display: flex;
                    align-items: center;
                }
                .my_productmessage_left img{
                    width: 100px;
                    height: auto;
                    border: 0;
                }
                .my_productmessage_right{
                    display: flex;
                    align-items: center;
                    margin-right: 5px;
                }
                .my_productmessage_right span{
                        margin-right: 10px;
                }
                .my_productmoney{
                    text-align: right;
                    border-top: 1px solid #e1e1e1;
                    line-height: 70px;
                }
                    .my_productmoney span{
                        margin-right: 15px;
                }
                .comfirmProduct{
                    float: right;
                    /* margin-right: 15px; */
                    margin-top: 10px;
                    border: 0;
                    background: #31a030;
                    color: #fff;
                    padding: 5px;
                    border-radius: 3px;
                    cursor: pointer;
                }
                .comfirmProduct:hover{
                    background:#8463A9;
                }
                .pages{
                    width: 95%;
                    margin: 0 auto;
                }
                .pages ul li{
                    float: right;
                    cursor: pointer;    
                    background: #eee;
                    padding: 0 10px;
                    border-radius: 3px;
                }
        </style>
        <body id="top" class="home blog chrome">
	        <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
            <input type="hidden" name="totalPage" value="${paging.totalPage}">
            <div id="main-content">
                <div id="content-header">
                    <div itemscope itemtype="http://schema.org/WebPage" id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">用户中心</span>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">已购教学</span></div>
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
                                        <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" id="headImg" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <li><a href="/user/myUploadProducts.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li>
                                            <a href="/user/myFavoriteList.shtml"><span class="icon-heart"></span>我的收藏</a></li>
                                        <c:choose>
                                            <c:when test="${flag == 'vip'}">
                                                <li><a href="/user/purchasedList.shtml"><span class="icon-shopping-cart"></span>已购教学</a></li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="current-menu-item"><a href="/user/purchasedList.shtml?flag=product" style='background: url("../../../theme/${theme}/images/greenshop.png") no-repeat; 
                                                background-position-y: 4px;
                                                background-size: 12px;'><span class="icon-shopping-cart"></span>已购教学</a><img  class="down_list" src="../../../theme/${theme}/images/shop-right.png" alt="">
                                                <ul class="comment_down_list">
                                                    <li>
                                                        <img style="left: 12px;" src="../../../theme/${theme}/images/shop-righta.png" alt="">
                                                        <span style="margin-left: 7px;"><a style="background: #fff;color:gray;" href="/comment/commentListOnPage.shtml?flag=my">我的评论</a></span>
                                                    </li>
                                                    <li>
                                                        <img style="left: 25px;" src="../../../theme/${theme}/images/shop-righta.png" alt="">    
                                                        <span style="margin-left: 21px;"><a style="background: #fff;color:gray;" href="/order/item.shtml?status=710052">待评论商品</a></span>
                                                    </li>
                                                </ul>
                                                </li>
                                            </c:otherwise>
                                        </c:choose> 
                                        <c:choose>
                                            <c:when test="${flag == 'vip'}">
                                                <li class="current-menu-item"><a href="/user/purchasedList.shtml?flag=vip" style='background: url("../../../theme/${theme}/images/greengift.png") no-repeat;
                            background-position-y: 4px;
                            background-size: 11px;'><span class="icon-gift"></span>VIP订购</a></li>
                                            </c:when>
                                            <c:otherwise>
                                                <li><a href="/user/purchasedList.shtml?flag=vip"><span class="icon-gift"></span>VIP订购</a></li>
                                            </c:otherwise>
                                        </c:choose>  
                                        
                                        <li><a href="/user/bbsInformation.shtml"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/user/modifyPersonalInfo.shtml"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope>
                                    <div id="post-header">
                                        <div class="feedback"><a href="/user/bbsInformation.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">
                                            <c:choose>
                                                <c:when test="${flag == 'vip'}">
                                                    vip订购
                                                </c:when>
                                                <c:otherwise>
                                                    已购教学
                                                </c:otherwise>
                                            </c:choose>            
                                     </h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody">
                                        <div class="wrap">
                                            <c:choose>
										<c:when test="${flag == 'vip'}">
											<form method="post" action="" id="buyForm">
												<c:choose>
													<c:when test="${vipValid == true}">
                                                    <p style="font-size: 16px; color: red;">
														尊敬的VIP会员！点击查看 <a href="/about/termOfService.shtml">VIP用户价格与服务条款</a>
													</p>
                                                	</c:when>
													<c:otherwise>
                                                    <p style="font-size: 16px; color: red;">
														您目前不是本站VIP会员！点击查看 <a href="/about/termOfService.shtml">VIP用户价格与服务条款</a>
													</p>
                                                	</c:otherwise>
												</c:choose>
												
												<!--<p>您目前的账户余额为 <strong>0.00</strong>。<a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D">给账户充值</a></p>-->
												<h2
													style='border-bottom: 1px solid #f2f2f2; margin: 15px 0; padding-left: 15px;'>购买VIP服务</h2>
												<table class="form-table">
													<tr>
														<td valign="top" width="30%" style='padding-left: 15px'>VIP类型
														</td>
														<td>
															<!-- <input type="radio" id="userType" name="userType" value="10" checked />终身VIP会员 --- 元<br /> -->
															<input type="radio" class="userType" name="userType"
															value="VIP_YEAR" />年度VIP会员 --- 168元<br /> <input
															type="radio" class="userType" name="userType"
															value="VIP_QUARTER" />季度VIP会员 --- 48元<br /> <input
															type="radio" class="userType" name="userType"
															value="VIP_MONTH" />月度VIP会员 --- 18元
														</td>
													</tr>
													<!--<tr>
                                                                <td valign="top" width="30%" style='padding-left:15px'>可用余额
                                                                </td>
                                                                <td id="moremoney">0.00元</td>
                                                            </tr>-->
													<tr>
														<td colspan="2" style='padding-left: 15px'><input
															type="button" value="确认购买" onclick="buy()"
															class="button-primary" /> <input type="hidden"
															value="${document.documentDataMap.vipLevel.dataValue}"
															id="vip"></td>
													</tr>
												</table>
											</form>
											<div class="clear"></div>
											<div class="wrap">
												<h2
													style='border-bottom: 1px solid #f2f2f2; margin: 15px 0; padding-left: 15px;'>VIP订单查询</h2>
												<p>
													共有 <strong>${vipMap.vipPurchaseNum}</strong> 笔交易，总金额：<strong>${vipMap.vipPurchaseMoney}</strong>
												</p>
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
																<c:forEach var="item" items="${vipMap.vipPurchaseList}"
																	varStatus="status">
																	<tr>
																		<td align="center" class="buyTime"><fmt:formatDate
																				value="${item.enterTime}" type="both" /></td>
																		<td align="center">${item.name}</td>
																		<td align="center">${item.labelMoney}</td>
																		<td align="center"><fmt:formatDate
																				value="${item.enterTime}" type="both" /></td>
																		<td align="center">已到期</td>
																	</tr>
																</c:forEach>
															</c:otherwise>
														</c:choose>
													</tbody>
												</table>
											</div>
										</c:when>
										<c:otherwise>
                                                    <c:choose>
                                                            <c:when test="${empty productMap.productPurchaseList}">
                                                                <p>您目前还没有购买过任何商品！</p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p style="margin-left: 15px;">共有 <strong>${productMap.productPurchaseNum}</strong> 笔交易</p>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <!--<p>您目前的账户余额为 <strong>0.00</strong>。<a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D">给账户充值</a></p>-->
                                                        <table class="widefat">
                                                            <thead>
                                                                <tr>
                                                                    <!--<th width="15%">订单编号</th>-->
                                                                    <th width="15%">购买时间</th>
                                                                    <th width="25%">商品名称</th>
                                                                    <!-- <th width="10%">分类</th> -->
                                                                    <!--<th width="12%">作者</th>                                                                                  -->
                                                                    <!--<th width="8%">价格</th>-->
                                                                    <th width="15%">操作</th>
                                                                    <th width="15%">订单查询</th>
                                                                    <!--<th width="15%">订单状态</th>-->
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <c:choose>
                                                                        <c:when test="${empty productMap.productPurchaseList}">
                                                                            <tr>
                                                                                <td colspan="7" align="center">您目前还没有商品订单！！</td>
                                                                            </tr> 
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:forEach var="item" items="${productMap.productPurchaseList}" varStatus="status">
                                                                            <tr>
                                                                                <!--<td  align="center">${item.payInfo.transactionId}</td>-->
                                                                                <td  align="center"><fmt:formatDate value="${item.startTime}" type="both"/></td>
                                                                                <td  align="center">${item.name}</td>
                                                                                <!--<td  align="center">${item.author}</td>-->
                                                                                <!--<td  align="center">${item.payInfo.realMoney}</td>-->
                                                                                <td  align="center"><a href="${item.refUrl}">继续阅读</a></td>
                                                                                <td  align="center" style="cursor:pointer;"><input type="hidden" value="${item.cartId}"><span class="seeOrder"><input type="hidden" value="${item.deliveryed}">查看订单</span></td>
                                                                                <!--<td  align="center">待发货</td>-->
                                                                            </tr> 
                                                                            </c:forEach>  
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                        <div class="pages">
                                                            <ul>
                                                                <li>下一页</li>
                                                                <li>尾页</li>
                                                                <li>首页</li>
                                                                <li>上一页</li>
                                                            </ul>
                                                        </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>
            
        </html>