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
             <div id="main-content" style="padding-bottom: 210%;">
                <div class='userour' style="height: auto;">
                    <div class='userour_a'>
                        <h2 style="font-size:18px;">商务合作</h2>
                    </div>
                    <div style="width:100%;height:1px;background:#ddd;margin-bottom:20px;"></div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>社团 琴行合作: </h3>
                            </li>
                            <br>
                            <li>七星乐器书籍《七星指弹曲集》现正式全国推广，优秀吉他社团及琴行均可接受批发业务，一次订货20本以上，均可申请批发价!</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>产品推广： </h3>
                            </li>
                            <br>
                            <li>
                                <p>七星乐器未来更加重视吉他视频编曲及教学水平的提高，高质量就会有高的展现率，如果您是吉他品牌商家并且认同我们的吉他视频质量及教学质量，我们更期待与您的合作申请条件：双向选择，您选择我们的同时我们也要审核您的产品!</p>
                                <br>
                                <img src="../../../theme/${theme}/images/tuiguang.png" alt="" style="width:100%;height:auto;">
                            </li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>签约指弹乐手： </h3>
                            </li>
                            <br>
                            <li>条件: <a href="">有独立编曲能力，能熟练掌握GTP或是MUSE制谱软件</a><br><br>
                                      <p style="padding-left:40px;">
                                        <a href="#">
                                                 有独特编曲风格，有优秀作品的，即使不会使用制谱软件我们可以负责制谱<br><br>
                                                 独立录制视频或全方位讲解视频能力<br><br>
                                                 从事过网络相关工作，有自己系列作品</a>
                                      </p>
                            </li>
                            <br>
                            <li>流程: 审核作品------协商签约细节</li>
                            <br>
                            <li>电子邮件：<a href="#">qixingwenhuavip@126.com</a></li>
                            <br>
                            <li>联系方式：在线qq<a href="#">1664083554</a></li>
                            <br>
                            <li>手机：杨老师<a href="">18736805666</a></li>
                            <br>
                            <li>（来电前请先发短信注明事项，我们主动与您联系）</li>
                            <br>
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