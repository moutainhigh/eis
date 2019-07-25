<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
    <%@page import="java.io.File"%>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-七星书籍</TITLE>
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
                .book-right{
                    flex:1;
                    margin: 15px;
                }
                .book-right-top{
                    padding-bottom: 14px;
                    border-bottom: 1px solid #c0c0c0;
                }
                .book-right-top strong{
                    font-size:22px;
                    color:#000;
                    line-height:28px;
                }
                .book-right-top ul{
                    width:250px;
                    display:flex;
                    display: -webkit-flex;
                    flex-direction:row;   
                    margin-top: 44px;
                    justify-content: space-between;
                }
                .book-right-top ul li{
                    height:25px;
                    background:#f2f2f2;
                    display:flex;
                    display: -webkit-flex;
                    flex-direction:row;
                    justify-content:center;
                    align-items: center;
                    color:gray;
                    border-radius: 4px;
                    font-size: 12px;
                }
                .book-right-top ul li img{
                    width:14px;
                    height:14px;
                    padding-left: 5px;
                }
                .book-right-top ul li span{
                    padding:0 5px;

                }
                .book-right-mid{
                    border-bottom:1px solid #c0c0c0;
                }
                .book-right-mid table{
                    width:100%;
                    /*margin-left: -28px;*/
                    border-spacing:15px;
                    border-collapse: separate;          
                }
                .book-right-mid table tr td:nth-child(1){
                    width:60px;
                    color:gray;
                }
                .book-right-mid table tr td:nth-child(2){
                    color:#000;
                }
                .book-right-foot p{
                    font-size: 14px;
                    color: #000;
                    line-height: 25px;
                    padding-top: 10px;
                }
                .book-right-foot-num{
                    display: flex;
                    display: -webkit-flex;
                    flex-direction: row;
                    font-size: 16px;
                    color: #000;
                    margin-top: 30px;
                }
                .book-right-foot-num span{
                    line-height:38px;
                }
                .book-right-foot-num div{
                    width: 20px;
                    height: 35px;
                    border: 1px solid gray;
                    text-align: center;
                    cursor: pointer;
                    line-height: 33px;
                }
                .book-right-foot-num input{
                    width: 50px;
                    height: 37px;
                    border: 0;  
                    border-top: 1px solid gray;
                    border-bottom: 1px solid gray;
                    text-align: center;

                }
                .book-right-foot button{
                    background: #fff;
                    border: 1px solid #f1881b;
                    padding: 10px 80px;
                    color: #f1881b;
                    margin: 33px 0;
                    cursor:pointer;
                    border-radius: 5px;
                }
                .book-right-foot-buy span{
                    position: relative;
                    top: -8px;
                }
                .book-right-foot-buy img{
                    width: 80px;
                    height: 27px;
                }
                .book-toggle{
                    width:100%;
                }
                .book-toggle ul{
                    display: flex;
                    display: -webkit-flex;
                    flex-direction: row;
                    justify-content:flex-start;
                    margin-left:14px;
                }
                .book-toggle ul li{
                    display: flex;
                    flex-direction: column;
                    color: #f1881b;
                    width: 190px;
                    height: 100px;
                    font-size: 27px;
                    align-items: center;
                    justify-content: center;
                    cursor:pointer;
                }
                .book-toggle ul li span{
                    line-height: 38px;
                }
                .book-video ul li{
                    width: 100%;
                    box-sizing: border-box;
                    padding: 15px;
                    /*margin: 10px 15px 15px 15px;*/
                    float:left;
                }
                .book-video ul li div{
                    width:100%;
                    height:25%;
                }
                .book-video ul li p{
                    width: 100%;
                    height: 32px;
                    font-size: 14px;
                    color: #fff;
                    text-align: center;
                    line-height: 32px;
                    background: #F58B3C;
                }
                .book-images{
                    margin:14px;
                }
                .book-detail img{
                    width:100%;
                    height:auto;
                }
            </style>
        <body id="top" class="home blog chrome">
            <p class="post-title" style="display:none">${document.title}</p>
            <input type="hidden" name="qixingbook" value="${document.documentCode}">
            <input type="hidden" name="objectId" value="${document.udid}" id="udid">  
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
                        <div class="span8" style="width:100%;">
                            <div class="widget-box">
                                <div style="display:flex;  display: -webkit-flex;flex-direction:column;">
                                        <!--实物轮播图-->
                                        <div class="book-left">
                                            <div class="swiper-container" style="height:290px;">
                                                <div class="swiper-wrapper">
                                                    <div class="swiper-slide">
                                                        <img data-lazy-src="../../../theme/basic/images/bookone.png" alt="" style='width:100%;height:100%;'>
                                                    </div>
                                                    <div class="swiper-slide">
                                                        <img data-lazy-src="../../../theme/basic/images/booktwo.png" alt="" style='width:100%;height:100%;'>
                                                    </div>
                                                    <div class="swiper-slide">
                                                        <img data-lazy-src="../../../theme/basic/images/bookthree.png" alt="" style='width:100%;height:100%;'>
                                                    </div>
                                                </div>
                                                <!-- Add Pagination -->
                                                <div class="swiper-pagination"></div>
                                            </div>
                                        </div>
                                        <div class="book-right">
                                            <div class="book-right-top">
                                                <strong>全国包邮 七星指弹吉他曲集 25首经典曲目 精细编配 指弹必备</strong>
                                                <ul>
                                                    <li>
                                                        <img src="../../../theme/${theme}/images/real.png" alt="">
                                                        <span>正品保障</span>
                                                    </li>
                                                    <li>
                                                        <img src="../../../theme/${theme}/images/quick.png" alt="">
                                                        <span>闪电发货</span>
                                                    </li>
                                                    <li>
                                                        <img src="../../../theme/${theme}/images/sevenback.png" alt="">
                                                        <span>7天退换</span>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="book-right-mid">
                                                <table cellspacing="10px">
                                                    <tr>
                                                        <td>优惠价</td>
                                                        <td style="color:red;font-size:33px;"><strong>36.00</strong>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>服务</td>
                                                        <td>由七星指弹教室发货，并提供售后服务</td>
                                                    </tr>
                                                    <tr>
                                                        <td>配送</td>
                                                        <td>韵达 汇通 申通 圆通 快递发货，默认韵达快递！</td>
                                                    </tr>
                                                    <tr>
                                                        <td>评论</td>
                                                        <td><span style="color:red;">${document.documentDataMap.get('commentCount').dataValue}</span>条评论</td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="book-right-foot">
                                                <p>本书汇集多首吉他爱好者以及七星吉他老师的心选曲目，内容丰富曲目优美难易兼备曲谱用心编配合理安排难易递进，并提供了详细的指法安排以及高清完整的视频扫码示范曲，对于学习指弹是一本实用的辅助书籍！</p>
                                                <div class="book-right-foot-num">
                                                    <span>数量：</span>
                                                    <div onclick="popnum()">-</div>
                                                    <input type="text" name="num" value="1">
                                                    <div onclick="addnum()">+</div>
                                                </div>
                                                <button onclick="buyBook()">立即购买</button>
                                                <div class="book-right-foot-buy">
                                                    <span>支付方式：</span>
                                                    <img src="../../../theme/${theme}/images/weixing.png" alt="">
                                                    <img src="../../../theme/${theme}/images/zfb.png" alt="">
                                                </div>
                                            </div>
                                        </div>
                                </div>

                                <div class="book-toggle">
                                    <ul>
                                        <li style="border-bottom:1px solid red;"><span>商品简介</span><span style="font-size: 14px;">DETAIL</span></li>
                                        <li><span>评论(${document.documentDataMap.get('commentCount').dataValue})</span><span style="font-size: 14px;">REVEIEWS</span></li>
                                    </ul>
                                </div>
                                
                                <div style="width:100%;"  class="book-detail">
                                    <div class="book-images">
                                        <img data-lazy-src="../../../theme/${theme}/images/book1.png" alt="">
                                    </div>
                                    <div class="book-video">
                                        <ul>
                                            <li>
                                                <p>《小幸运》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODM3NTUzMg" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            </li>
                                            <li>
                                                <p>《未闻花名》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODM5MTU5Ng" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            </li>
                                            <li>
                                                <p>《遇见》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODM2NTExMg" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            <li>
                                                <p>《演员》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODMxODU0MA" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            <li>
                                                <p>《你离开的真相》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODMyOTU0MA" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            <li>
                                                <p>《记得》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODMzNTQ4NA" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            </li>
                                            <li>
                                                <p>《梦中的婚礼》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODMxODQ2MA" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            <li>
                                                <p>《夜的钢琴曲》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODM0NTA2MA" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            <li>
                                                <p>《平凡之路》</p>
                                                <div>
                                                    <iframe height="100%" width="100%" src="http://player.youku.com/embed/XMjgyODM3OTE5Ng" frameborder=0 allowfullscreen></iframe>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                        <img data-lazy-src="../../../theme/${theme}/images/book2.png" alt="">
                                        <img data-lazy-src="../../../theme/${theme}/images/book3.png" alt="">
                                        <img data-lazy-src="../../../theme/${theme}/images/book4.png" alt="">
                                        <img data-lazy-src="../../../theme/${theme}/images/book5.png" alt="">
                                        <img data-lazy-src="../../../theme/${theme}/images/book6.png" alt="">
                                    </div>
                                </div>
                                <div style="width:100%;display:none" class="book-comment">
	                                <%@include file="/WEB-INF/jsp/include/comment.jsp" %>    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script src='../../../theme/${theme}/js/EasyLazyload.min.js'></script>            
            <script src="../../../theme/${theme}/js/swiper.js"></script>
    
            <script>
                $(function(){
                    var swiper = new Swiper('.swiper-container', {
                        pagination: '.swiper-pagination',
                        paginationClickable: true
                    });
                })

                function addnum(){
                    var num = $('input[name="num"]').val();
                    num++;
                    $('input[name="num"]').val(num);
                }
                
                function popnum(){
                    var num = $('input[name="num"]').val();
                    if(num == 1){
                        return false;
                    }else{
                        num--;
                    }
                    $('input[name="num"]').val(num);
                }

                $('.book-toggle ul li').each(function(){
                    $(this).click(function(){
                        if($(this).css('border-bottom-width') == '0px'){
                            $(this).siblings().css('border-bottom-width','0px');
                            $(this).css({'border-bottom-width':'1px','border-bottom-color':'red','border-bottom-style':'solid'});
                            if($(this).text().indexOf('评论') > -1){
                                $('.book-comment').css('display','block');
                                $('.book-detail').css('display','none');
                            }else{
                                $('.book-comment').css('display','none');
                                $('.book-detail').css('display','block');
                            }
                        }else{
                            return false;
                        }
                    })
                })

                // 图片懒加载   
                lazyLoadInit();
            </script>
	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
            <script src='../../../theme/${theme}/js/commentMobile.js'></script>
        </body>

        </html>