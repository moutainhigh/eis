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
            <link rel="stylesheet" href="../../../theme/${theme}/css/tjdd.css">
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="1106128779" data-redirecturi="http://me.mo4u.cn" charset="utf-8"></script>
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>


            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
                 $(function(){   
                      var title = '${node.name}';
                      if(title.indexOf('首页')>-1){
                        $('#header li').eq(0).addClass('current-menu-item');               
                      }else if(title.indexOf('入门自学20课')>-1){
                        $('#header li').eq(2).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹教学')>-1||title.indexOf('初级指弹曲')>-1||title.indexOf('中级指弹曲')>-1||title.indexOf('高级指弹曲')>-1){
                        $('#header li').eq(3).addClass('current-menu-item');               
                      }else if(title.indexOf('VIP曲谱*教学')>-1){
                        $('#header li').eq(8).addClass('current-menu-item');               
                      }else if(title.indexOf('歌曲前奏')>-1 ||title.indexOf('Riff练习')>-1 || title.indexOf('双吉他教学')>-1 ){
                        $('#header li').eq(9).addClass('current-menu-item');               
                      }else if(title.indexOf('学习文章')>-1){
                        $('#header li').eq(13).addClass('current-menu-item');               
                      }else if(title.indexOf('金伟')>-1 || title.indexOf('杨猛')>-1 ){
                        $('#header li').eq(14).addClass('current-menu-item');               
                      }else if(title.indexOf('所有作品')>-1 || title.indexOf('我要上传')>-1 ||title.indexOf('弹唱作品')>-1||title.indexOf('指弹作品')>-1||title.indexOf('原创作品')>-1){
                        $('#header li').eq(17).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹铃音')>-1){
                        $('#header li').eq(23).addClass('current-menu-item');               
                      }else if(title.indexOf('在线调弦')>-1){
                        $('#header li').eq(24).addClass('current-menu-item');               
                      }else if(title.indexOf('帮助中心')>-1){
                        $('#header li').eq(25).addClass('current-menu-item');               
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
       <c:out value="${adminRoleBeans[fn:trim(itemMap.101122017071211382553180989)].index}"/>
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
                        <div class="span8">
                            <div class="widget-box">
                                <div style="margin-top: 40px;">
                                        <div class="cartNum po_re">
                                            <img src="../../../theme/basic/images/cart_big.png"/>
                                            <span class="po_ab num">0</span>我的购物车
                                        </div>
                                        <ul class="cartlist" id="rongQi">
                                            <c:forEach var="cartStr" items="${itemMap}" varStatus="status">
                                                <c:set var="str" value="${cartStr.value}"></c:set> 
                                                <li>
                                                    <img src="/file/${str.itemDataMap.get('productSmallImage').dataValue}">
                                                    <p class="fo-14">${str.name}</p>
                                                    <p class="fo-14">￥${str.price.money}&nbsp;&nbsp;&nbsp;&nbsp;规格：${str.itemDataMap.get('goodsSpec').dataValue}</p>
                                                    <div class="box_container martop10 divBottom">
                                                    <div class="spinner" ><button class="decrease" onclick="decrease(this,'${str.transactionId}')">-</button><input type="text" class="spinner value passive" value="${str.count}" id="productcount" maxlength="2" onblur="changevalue(this.value,'${str.transactionId}')"  ><button class="increase" onClick="increase(this,'${str.transactionId}')">+</button>
                                                        </div>
                                                        <div class="box_container_right orange fo-12 total subtotal">小计：￥<span class="sumtotal">0</span></div>
                                                    </div>
                                                    <div class="checkBox">
                                                        <div class="checkboxFive">
                                                            <input type="checkbox" id="checkboxFiveInput${str.transactionId}" name="food"  value="${str.transactionId}"/>
                                                            <label for="checkboxFiveInput${str.transactionId}"></label>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                        <div id="gouwuche_bottom">
                                            <div class="box_left_cart">
                                                <div class="checkBox" id="quanxuan_checkBox">
                                                    <input type="checkbox"  id="quanxuan"  class="quanxuan" />
                                                </div>
                                                <p style="height:7px; margin-left:55px;">
                                                <a href="#" class="select_all">全选</a>
                                                <a href="#" id="delgwc" class="btn_delete">删除</a></p>
                                                
                                            </div>
                                            <div class="box_right_cart">
                                                <span class="box_container_left" style="margin-right:20px;">共选商品<span class="orange num">0</span>件</span>
                                                <span>总计(不含运费):￥<span style="color:#ff6400" id="zong1">0</span></span>
                                                <a href="#" class="btnSettle box_container_right marleft10" id="balance" style="background-color: rgb(221, 221, 221);color:#ffffff;">结算</a>
                                            </div>
                                        </div>
                                </div>
                            </div>
                        </div>


	                     <%@include file="/WEB-INF/jsp/include/right.jsp" %>
                    </div>
                 </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>