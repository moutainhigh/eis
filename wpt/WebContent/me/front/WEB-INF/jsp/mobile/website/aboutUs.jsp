<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-关于我们</TITLE>
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
                    <img data-lazy-src="../../../theme/${theme}/images/about1.png" alt="" style="width:60%;margin: 0 10%;margin-top: 3%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about2.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about3.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about4.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about5.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about6.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about7.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about8.png" alt="" style="width:60%;margin: 0 10%;">
                    <img data-lazy-src="../../../theme/${theme}/images/about9.png" alt="" style="width:60%;margin: 0 10%;margin-bottom: 3%;">
            </div>
            <script src='../../../theme/${theme}/js/EasyLazyload.min.js'></script>
            <script>
                // 图片懒加载   
                lazyLoadInit();
            </script>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>
     </html>