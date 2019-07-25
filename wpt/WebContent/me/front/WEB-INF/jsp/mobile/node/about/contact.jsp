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
                <div class='userour'>
                    <div class='userour_a'>
                        <h2 style="font-size:18px;">联系我们</h2>
                    </div>
                    <div style="width:100%;height:1px;background:#ddd;margin-bottom:20px;"></div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>七星乐器微信公众号搜索： </h3>
                            </li>
                            <br>
                            <li>公众号名称：<a href="#">七星乐器</a></li>
                            <br>
                            <li><img src="../../../theme/${theme}/images/gzh.JPG" alt="" style="width:100px;height:auto;"></li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>联系方式： </h3>
                            </li>
                            <br>
                            <li>QQ：<a href="#">1664083554</a></li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>邮箱： </h3>
                            </li>
                            <br>
                            <li>电子邮件：<a href="#">qixingwenhuavip@126.com</a></li>
                        </ul>
                    </div>
                    <!--<div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>广告销售：</h3>
                            </li>
                            <li>电子邮件：<a href="#"></a></li>
                            <li>联系电话：</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>市场合作（媒体、公关）：</h3>
                            </li>
                            <li>电子邮件：<a href="#"></a></li>
                            <li>联系电话：</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>联系地址： </h3>
                            </li>
                            <li>公司地址：</li>
                            <li></li>
                        </ul>
                    </div>-->

                </div>

            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>
     </html>