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
            .widget-content ul li{
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
                                        付费课程服务条款
                                    </h1>
                                </header>
                                <div class="widget-content">
                                    <ul style="width:100%;">
                                        <li>                                            
                                          提示：在使用【<a href="www.qixingyueqi.com" style="color:#31a030;">www.qixingyueqi.com</a>】网站课程服务之前，请您先认真阅读本《付费课程服务条款》（以下简称“本条款”），在充分理解并同意遵守本条款各内容再进行本站课程服务的购买和使用。如您对本条款有任何疑问的，应向本站在线客服咨询。本条款只适用于个人用户。
                                        </li>
                                        <li>
                                            <h1>一、条款确认</h1>
                                        </li>
                                        <li>
                                            1、本条款是您(以下简称“您”或“用户”)基于对七星指弹教室官方网站（<a href="www.qixingyueqi.com" style="color:#31a030;">www.qixingyueqi.com</a>）（以下简称“qixingyeuqi”）课程服务方式的认可、学习计划的认同、自身提高吉他演奏的需求，就本站课程服务等相关事宜所订立的契约，双方签订本服务条款。 
                                        </li>
                                        <li>
                                            2、请您仔细阅读本服务条款，当您按照本协议提示填写信息、阅读并同意本协议且完成全部订立程序后，即表示您已充分阅读、理解并接受本条款的全部内容，本条款即构成对双方有约束力的法律文件。您承诺接受并遵守本条款的约定，届时您不应以未阅读本条款的内容或者未获得本站对您问询的解答等理由，主张本条款无效，或要求撤销本条款。
                                        </li>
                                        <li>    
                                            3、如果您在18周岁以下，则只能在父母或监护人的监护参与下才能使用本站。若您不具备该主体资格，则您的监护人应承担因此而导致的一切后果。
                                        </li>
                                        <li>
                                            <h1>二、曲谱/教学付费服务</h1>
                                        </li>
                                        <li>
                                            1、用户在<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>网站上可以通过网银、支付宝、易乐等方式进行曲谱或教学费用的支付，在<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>收到用户款项后立即开通相应的曲谱或视频阅读、学习服务。
                                        </li>
                                        <li>
                                            2、用户在购买VIP时，<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>网站应显示所选付费项目，名称、时限、价格。
                                        </li>
                                        <li>
                                            3、用户支付曲谱或教学费用后，如不可预知的网络或技术原因不能查看相关服务，用户可在线联系老师，并及时帮助解决！
                                        </li>
                                        <li>
                                            4、曲谱或视频当非实物交易时，购买后不退款，请考虑好再购买。
                                        </li>
                                        <li>
                                            <h1>三、VIP付费服务</h1>
                                        </li>
                                        <li>
                                            1、用户在<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>网站上可以通过网银、支付宝、易乐等方式VIP费用的支付，在<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>收到用户款项后立即开通相应的VIP权限阅读、学习服务。
                                        </li>
                                        <li>
                                            2、用户在购买VIP时，<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>网站应显示所选付费项目，名称、时限、价格。
                                        </li>
                                        <li>
                                            3、用户付费后，VIP使用到约定时限<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>有权停止vip权限服务。
                                        </li>
                                        <li>
                                            4、用户支付VIP费用后，如不可预知的网络或技术原因不能查看相关服务，用户可在线联系老师，并及时帮助解决！ 
                                        </li>
                                        <li>
                                            <h1>四、账户管理</h1>
                                        </li>
                                        <li>
                                            1、用户有义务妥善保管其在<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>申请的账号及密码之机密与安全，不得泄漏给任何第三人。用户必须为此组账号及密码之一切行为负责，此账号及密码所做之一切行为即视为用户本身之行为。
                                        </li>
                                        <li>
                                            2、本组账号及密码仅限于您个人使用，非经<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>同意，用户不得出借、移转或让与给其他第三人使用。
                                        </li>
                                        <li>
                                            3、<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>承诺对获得的用户的个人信息主要为其正常的记录和管理客户信息及为本协议之目的使用，除非经过用户同意，<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>对于用户的个人数据不会加以泄漏、转售或与其他机构进行交换。
                                        </li>
                                        <li>
                                            4、本协议自用户完成本服务条款的注册程序且将所选课程的款项支付至<a href="www.qixingyueqi.com" style="color:#31a030;">qixingyueqi</a>账户后生效。
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
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
            <script src='../../../theme/${theme}/js/comment.js'></script>
        </body>

        </html>