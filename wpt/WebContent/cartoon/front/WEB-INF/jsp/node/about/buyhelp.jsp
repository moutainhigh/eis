<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
    <%@page import="java.io.File"%>
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
                        /*else if (obj.tagName=="BUTTON"){
                        return true;
                        }*/
                        else
                            return false;
                    }
                }
                // End -->
            </script>

        </head>

        <style>
            .widget-content ul li:nth-of-type(odd){
                margin: 20px 0;
                font-size: 18px;
                line-height: 30px;
            }
        </style>

        <body id="top" class="home blog chrome">  
	        <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
            <div id="main-content">
                <div id="content-header">
                    <div itemscope id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <a  href='/content/newbie/index.shtml'><img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">${node.name}</span></a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">正文</span></div>
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
                            <div class="widget-box" role="main" itemscope="" style="border-bottom: 0;">
                                <header id="post-header">
                                    <h1 itemprop="headline" id="list">
                                        购买指南
                                    </h1>
                                </header>
                                <div class="widget-content">
                                    <ul style="width:100%;">
                                        <li>1.购买乐谱/教学，请先注册后登陆！
（为确保您账户的安全及正常使用，依《网络安全法》相关要求，6月1日起会员账户需绑定手机。注册如您还未绑定，请尽快完成，感谢您的理解及支持！）</li>
                                        <li>
                                            <img src="../../../theme/${theme}/images/register.gif" alt="">
                                        </li>
                                        <li>2.点击立即购买或是加入购物车（多份谱子时候可以选择购物车一起提交）</li>
                                        <li>
                                            <img src="../../../theme/${theme}/images/buy.gif" alt="">
                                        </li>
                                        <li>3.购买完成后或下次登陆继续学习/阅读，可以登陆网站---点击管理---点击“已购教学”，中查看或是继续学习！</li>
                                        <li>
                                            <img src="../../../theme/${theme}/images/order.gif" alt="">
                                        </li>
                                        <li>4.购买数量：点击购买单份谱子及教学，默认数量为1，如果点击加入购物车时，网页会给出确认购买提示，然后可以在网页最上面/最右面，购物车内确定商品文件名及数量，确认无误后点击提交订单---付款！</li>
                                        <li>
                                            <img src="../../../theme/${theme}/images/cart.gif" alt="">
                                        </li>
                                        <li>5.购买时候多选/错选：将喜欢的商品加入购物车后如有错误，可以直接在购物车内进行删除，然后重新选择!</li>
                                        <li>
                                            <img src="../../../theme/${theme}/images/error.gif" alt="">
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </section>
             

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