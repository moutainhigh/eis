
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-我的订单</TITLE>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!--<meta http-equiv="Refresh" content="0; url=/content/node/index.shtml" />-->
            <META name=description content=有宝>
            <META name=keywords content=有宝>
            <LINK rel="Shortcut Icon" href="/favicon.ico">
            <LINK rel=Bookmark href="/favicon.ico">


            <link rel="stylesheet" href="../../../theme/${theme}/css/swiper.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/alert.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/tjdd.css">
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cityselect.js'></script>
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>


            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
                var provs ='北京';
                var citys = '东城区';
                var dists = '';
                // 选择城市插件
                function cityselects(provs,citys,dists){
                    $("#citys").citySelect({
                        url:"/theme/${theme}/js/city.min.js",  
                        prov:provs, //省份 
                        city:citys, //城市 
                        dist:dists, //区县 
                        nodata:"none" //当子集无数据时，隐藏select 
                    });
                }
                $(function(){
                    cityselects(provs,citys,dists);
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
            .table_list{
                width:100%;
            }
            .table_list tr th{
                height:30px;
                text-align:center;
                color:#666;
                line-height:30px;
            }
            .table_list tr td{
                height:30px;
                text-align:center;
                color:#666;
                line-height:30px;
                border-left:1px solid #dddddd;
                border-right:1px solid #dddddd;
                border-bottom:1px solid #dddddd;
            }
            .addAddress{
                /*width:590px;*/
                /*height:510px;*/
                background:#fff;
                margin:5% auto;
                border-radius:5px;
                box-shadow:0 0 10px #666;
            }
            .addAddress_head{
                width:100%;
                height:55px;
                border-bottom:1px solid #dddddd;
                text-align:center;
                line-height:55px;
                position:relative;
                color:red;
                font-size:17px;
            }
            .addAddress_head img{
                position:absolute;
                right:10px;
                top:10px;    
                width: 15px;
                height: auto;
                cursor:pointer;
            }
            .modelAddress tr{
                height:40px;    
                margin-top: 20px;
                display: inline-block;
            }
            .modelAddress tr td:nth-child(1){
                text-align:center;
                line-height:40px;
            }
            .modelAddress tr td:nth-child(2){
                text-align:left;
                line-height:40px;
            }
            .modelAddress tr td span{
                color:red;
                padding-left:5px;
            }
            .modelAddress tr td input{
                width: 90%;
                height: 40px;
                padding-left: 18px;
            }
            .modelAddress tr td textarea{
                width: 90%;
                height:60px;
                padding-left: 18px;
                padding-top:10px;
            }
            .modelAddress tr td button{
                width: 200px;
                height:40px;
                background:#F58B3C;
                color:#ffffff;
                text-align:center;
                line-height:40px;
                border-radius:5px;
                font-size:18px;
                border:0;
                cursor:pointer;
            }
            .widget-box{
                box-sizing:border-box;
                padding-right:15px;
            }
        </style>
        <body id="top" class="home blog chrome"> 
            <div class="box">
                <div class="addAddress">
                    <div class="addAddress_head"><span>新增收货地址</span><img src="../../../theme/${theme}/images/close.png" alt="" onclick="closeBox()"></div>
                    <table class="modelAddress">
                        <tr>
                            <td style="width:150px;">收货人</td>
                            <td style="width:440px;"><input type="text" placeholder="长度为2-6个字符" name="model_people"><span>*</span></td>
                        </tr>
                         <tr>
                            <td style="width:150px;">手机号码</td>
                            <td style="width:440px;"><input type="text" placeholder="请输入手机号码" name="model_phone"><span>*</span></td>
                        </tr>
                         <tr>
                            <td style="width:150px;">所在地区</td>
                            <td style="width:440px;"><div id="citys" style="display:inline;"><select class="prov" id="prov"></select><select class="city" id="city" disabled="disabled"></select><select class="dist" id="dist" disabled="disabled"></select></div><span>*</span></td>
                        </tr>
                         <tr>
                            <td style="width:150px;">详细地址</td>
                            <td style="width:440px;">
                                <input type="text"  placeholder="输入5-50字符的详细地址" name="model_address" /><span>*</span>
                            </td>
                        </tr>
                         <tr style="margin-top:0;">
                            <td style="width:150px;"></td>
                            <td style="width:440px;color:red;">不支持港澳台地区的配送</td>
                        </tr>
                         <tr style="margin-top:0;">
                            <td style="width:150px;"></td>
                            <td style="width:440px;"><input style="width:20px;height:20px;margin-right:5px;" type="checkbox" id="setDefaultAdd" datavalue="N" onclick="defaultAddress()">设为默认</td>
                        </tr>
                         <tr style="margin-bottom:15px;">
                            <td style="width:150px;"></td>
                            <td style="width:440px;"><button onclick="newAddress()">保存</button></td>
                        </tr>
                    </table>
                </div>
            </div> 

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
                        <div class="span8" style="width:100%">
                            <div class="widget-box">
                                <div class="wid-80 martop50">
                                        <div class="status_right" style="margin-left: 15px;">
                                            <div class="status"> 
                                                    <span class="text_status" style="color:gray;">1.选择商品套餐</span> 
                                                    <span class="text_status" style="color:#ff6400;">2.确认支付</span> 
                                                    <span class="text_status">3.订单支付成功</span> 
                                                    <span class="text_status">4.订单打包发货</span> 
                                                    <span class="text_status">5.交易完成</span> 
                                                    </div>
                                                    <div>
                                                        <div  style="float:left;width: 45%;display: flex;display:-webkit-flex;flex-direction: row;align-items: center;">
                                                            <div  style="height:2px;background:#cdcdcd;flex:1;"></div>
                                                            <img src="../../../theme/basic/images/jindu.png" alt="" style="width:20px;height:auto;">
                                                        </div>
                                                        <div  style="float:left;width: 35%;display: flex;display:-webkit-flex;flex-direction: row;align-items: center;">
                                                            <div style="flex:1;height:2px;background: -moz-linear-gradient( right,#ff6400,#cdcdcd);background: linear-gradient( right,#ff6400,#cdcdcd);background: -webkit-linear-gradient( right,#ff6400,#cdcdcd);background: -o-linear-gradient( right,#ff6400,#cdcdcd);"></div>                                                           
                                                            <img src="../../../theme/basic/images/orangejindu.png" alt="" style="width:20px;height:auto;">
                                                            <div style="flex:1;height:2px;background: -moz-linear-gradient( left,#ff6400,#cdcdcd);background: linear-gradient( left,#ff6400,#cdcdcd);background: -webkit-linear-gradient( left,#ff6400,#cdcdcd);background: -o-linear-gradient( left,#ff6400,#cdcdcd);"></div>                                                   
                                                        </div>
                                                        <div style="float:left;width: 20%;height:2px;background:#cdcdcd;margin-top:9px;"></div>
                                                    </div>
                                                </div>
                                        
                                                <c:set var="str" value="${cart.value}"></c:set> 
                                                <div class="content">
                                                <!--<div class="blurb orange martop20" style="border:0;border-bottom:1px solid #cdcdcd;color:gray;">
                                                        <ul class="goods_title">
                                                            <li class="li1" style="width:20%;color:gray;">收货人</li>
                                                            <li style="width:20%;color:gray;">手机号码</li>
                                                            <li class="li2" style="color:gray;">所在地区</li>
                                                            <li class="li3" style="color:gray;">详细地址</li>
                                                            <li class="li4" style="color:gray;">操作</li>
                                                        </ul>
                                                </div>
                                                <div class="goods_content1" style="border:0;border-bottom:1px solid #cdcdcd;">
                                                    <c:forEach var="cartStr" items="${cart}" varStatus="status">
                                                            <c:set var="str" value="${cartStr.value}">
                                                            </c:set>
                                                        <ul class="goods_content1_message"  style="color:#000">
                                                            <input type="hidden" class="tid"   name="foods" value="${str.transactionId}"/>
                                                            <input type="hidden" class="cartId"   name="foods" value="${str.cartId}"/>
                                                            <img src="${str.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left"/>
                                                            <li class="li1" style="text-align: center;display: inherit;color:red;width:20%;"><span>${str.name}</span></li>
                                                            <li class="li5" style="color:gray;width:20%;text-align: center;"></li>
                                                            <li class="li2" style="color:gray;">￥${str.price.money}</li>
                                                            <li class="li3" style="color:gray;">${str.count}</li>
                                                            <li class="li4 money">￥<fmt:formatNumber value="${str.requestMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></li>
                                                        </ul>
                                                    </c:forEach>
                                                </div> -->              
                                                <c:choose>
                                                    <c:when test="${str.inOrderId == 'qixingbook'}">
                                                          <div class="shipping_address martop20">
                                                              <span  style="color:#ff6400;padding-left: 15px;">收货地址</span>
                                                          </div> 
                                                          <div class="orange martop20" style="border:0;border-bottom:1px solid #cdcdcd;color:gray;"></div>
                                                          <table class="table_list" cellpadding="5" cellspacing="1" align="center" border="1">
                                                                <tr style="background:#dddddd">
                                                                    <th style="width:10%">收货人</th>
                                                                    <th style="width:20%">所在地区</th>
                                                                    <th style="width:20%">详细地址</th>
                                                                    <th style="width:15%">手机号码</th>
                                                                    <th style="width:35%">操作</th>
                                                                </tr>
                                                                <c:forEach var="cartStr" items="${addressBookList}" varStatus="status">
                                                                    <c:choose>
                                                                        <c:when test="${empty addressBookList}">
                                                                            <tr>您还没有新增收货地址！</tr>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:choose>
                                                                                <c:when test="${cartStr.currentStatus == '100003'}">
                                                                                    <tr>
                                                                                        <td style="width:10%">${cartStr.contact}</td>
                                                                                        <td style="width:20%"><span class="prov">${cartStr.province}</span><span class="city">${cartStr.city}</span><span class="dist">${cartStr.district}</span></td>
                                                                                        <td style="width:20%">${cartStr.address}</td>
                                                                                        <td style="width:15%">${cartStr.mobile}</td>
                                                                                        <td style="width:35%;font-size: 12px;">
                                                                                            <p style="margin-right: 15px;display:inline-block;">
                                                                                                <input type="radio" name="addressBookId" class="addressBookId" value="${cartStr.addressBookId}" checked="checked"><span>配送至该地址</span>
                                                                                                <input type="radio" name="default_address" onclick="one(${cartStr.addressBookId})" class="setDefaultAdd" checked="checked">设为默认
                                                                                            </p>
                                                                                            <p class="modification" dataaddressbookid="${cartStr.addressBookId}" style="cursor:pointer;display:inline-block;">修改</p> | <p style="cursor:pointer;display:inline-block;" class="delete" onclick="deletes(${cartStr.addressBookId})">删除</p>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <tr>
                                                                                        <td style="width:10%">${cartStr.contact}</td>
                                                                                        <td style="width:20%"><span class="prov">${cartStr.province}</span><span class="city">${cartStr.city}</span><span class="dist">${cartStr.district}</span></td>
                                                                                        <td style="width:20%">${cartStr.address}</td>
                                                                                        <td style="width:15%">${cartStr.mobile}</td>
                                                                                        <td style="width:35%">
                                                                                            <p style="margin-right: 15px;display:inline-block;">
                                                                                                <input type="radio" name="addressBookId" class="addressBookId" value="${cartStr.addressBookId}"><span>配送至该地址</span>
                                                                                                <input type="radio" name="default_address" onclick="one(${cartStr.addressBookId})" class="setDefaultAdd">设为默认
                                                                                            </p>
                                                                                            <p class="modification" dataaddressbookid="${cartStr.addressBookId}" style="cursor:pointer;display:inline-block;">修改</p> | <p class="delete" onclick="deletes(${cartStr.addressBookId})" style="cursor:pointer;display:inline-block;">删除</p>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                        </table>
                                                        <div class="btngroup_address martop10">
                                                            <!--<div class="box_container_left">
                                                                <a onclick="getMoreAddress()">更多收货地址</a>
                                                            </div>-->
                                                            <div class="box_container_right">
                                                                <a onclick="addAddress()">新增收货地址</a>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                    </c:otherwise>
                                                </c:choose>
                                                
                                                <div class="martop45 ">
                                                    <span style="color:#ff6400;padding-left: 15px;">购买商品套餐</span>
                                                </div>
                                                <div class="blurb orange martop20" style="border:0;border-bottom:1px solid #cdcdcd;color:gray;">
                                                        <ul class="goods_title">
                                                            <li class="li1" style="width:20%;color:gray;">商品名</li>
                                                            <li style="width:20%;color:gray;">时限</li>
                                                            <li class="li2" style="color:gray;">单价</li>
                                                            <li class="li3" style="color:gray;">数量</li>
                                                            <li class="li4" style="color:gray;">小计</li>
                                                        </ul>
                                                </div>
                                                <div class="goods_content1" style="border:0;border-bottom:1px solid #cdcdcd;">
                                                    <c:forEach var="cartStr" items="${cart}" varStatus="status">
                                                            <c:set var="str" value="${cartStr.value}">
                                                            </c:set>
                                                        <ul class="goods_content1_message"  style="color:#000">
                                                            <input type="hidden" class="tid"   name="foods" value="${str.transactionId}"/>
                                                            <input type="hidden" class="cartId"   name="foods" value="${str.cartId}"/>
                                                            <!--<img src="${str.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left"/>-->
                                                            <li class="li1" style="text-align: center;display: inherit;color:red;width:20%;"><p>${str.name}</p></li>
                                                            <li class="li5" style="color:gray;width:20%;text-align: center;"></li>
                                                            <li class="li2" style="color:gray;">￥${str.price.money}</li>
                                                            <li class="li3" style="color:gray;">${str.count}</li>
                                                            <li class="li4 money">￥<fmt:formatNumber value="${str.requestMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></li>
                                                        </ul>
                                                    </c:forEach>
                                                </div>


                                                <c:choose>
                                                    <c:when test="${str.inOrderId == 'qixingbook'}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div id="vipRemove">
                                                            <div class="martop30" style="margin-bottom: 21px;">
                                                                <span style="color:#ff6400;padding-left: 15px;">马上升级vip免费</span>
                                                            </div>
                                                            <div class="thirdly_box martop10" style="border:0;">
                                                                <ul>
                                                                    <li style="margin-top: 10px;">
                                                                        <input type="radio" value="VIP_YEAR" class="vip"  style="margin-right: 10px;" /><input type="hidden" value="168">年度VIP会员 --- 168元
                                                                    </li>
                                                                    <li style="margin-top: 10px;">
                                                                        <input type="radio" value="VIP_QUARTER"  class="vip"  style="margin-right: 10px;" /><input type="hidden" value="48">季度VIP会员 --- 48元
                                                                    </li>
                                                                    <li style="margin-top: 10px;">
                                                                        <input type="radio" value="VIP_MONTH" class="vip"  style="margin-right: 10px;" /><input type="hidden" value="18">月度VIP会员 --- 18元
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </div> 
                                                    </c:otherwise>
                                                </c:choose>                               

                                                <div class="martop15 last_box"  style="color:#000;border-bottom:1px solid #cdcdcd;height:83px;">
                                                        <ul class="text_shangpinjine" style="float:none;">
                                                            <li style="float:left;">
                                                                <input type="checkbox" name="priceOrder" value="" checked="checked" style="margin-left:11px;margin-bottom:2px;">
                                                                <span>我已阅读并同意</span>
                                                                <a href="/about/termOfService.shtml" style="color:#1ba0e1;">付费商品服务条款</a>
                                                            </li>
                                                            <li style="float:right;"><span class="box_left">订单金额总计：</span>
                                                                <span class="orange">￥</span>
                                                                <span class="orange" id="totalMoney">
                                                                    <c:choose>
                                                                        <c:when test="${empty askUseCouponMoney}">
                                                                            0
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                    <!--还要再减去代金券的值-->
                                                                            0
                                                                        </c:otherwise>
                                                                    </c:choose> 
                                                                </span>
                                                            </li>
                                                            <li style="float:right;"><span class="box_left">商品数量总计算</span>&nbsp
                                                                <span class="box_right"><span id="productMoneyNum" style="color:#ff6400;">0</span>&nbsp份</span>
                                                            </li>
                                                            <!--<li><span class="box_left">代金券抵扣：</span>
                                                                <span class="orange box_right" id="giftmoney">￥ <span id="couponMoney">
                                                                    <c:choose>
                                                                        <c:when test="${empty askUseCouponMoney}">
                                                                            0.00
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ${askUseCouponMoney}
                                                                        </c:otherwise>
                                                                    </c:choose>    
                                                                </span></span>
                                                            </li>-->
                                                        </ul>        	
                                                </div>
                                                
                                                <div class="martop30">
                                                    <span style="color: #ff6400;padding-left: 15px;">支付方式</span>
                                                </div>
                                                <div class="allPay">
                                                    全额支付
                                                    <span>√</span>
                                                </div>
                                                <p style="margin-left:15px;color: #000;margin-top:55px;">应付金额：&nbsp<span style="color:#ff6400;"  id="productMoney"></span>元</p>
                                                <div class="thirdly_box martop10" style="border:0;">
                                                        <ul class="pay">
                                                            <c:forEach var="payType" items="${payTypeList}">
                                                                <c:choose>
                                                                    <c:when test="${payType.payTypeId == 1}">
                                                                        <li style="margin-top: 8px;float: left;"><input type="radio" name="checkit" checked value="${payType.payTypeId}"/></li>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <li style="margin-top: 8px;float: left;clear: both;"><input type="radio" name="checkit" value="${payType.payTypeId}"/></li>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:choose>
                                                                    <c:when test="${payType.payTypeId == 1}">
                                                                        <li><img src="../../../theme/basic/images/weixing.png" class="weixin"></li>
                                                                    </c:when>
                                                                    <c:when test="${payType.payTypeId == 2}">
                                                                        <li><img src="../../../theme/basic/images/zfb.png" class="weixin"></li>
                                                                    </c:when>
                                                                </c:choose>
                                                                <c:choose>
                                                                    <c:when test="${payType.payTypeId == 10}">
                                                                        <li style="margin-top: 9px;">${payType.name}</li>
                                                                    </c:when>
                                                                </c:choose>
                                                            </c:forEach>
                                                         	<!--<li><input type="radio" name="checkit" checked=cheched value="${payType.payTypeId}"/></li>
                                                            <li><img style="margin-top: 3px;" src="../../../theme/${theme}/images/wxlogin.png" class="weixin"></li>
                                                            <li style="margin-top: 3px;">微信支付</li>	-->
                                                        </ul>
                                                    </div>
                                                    <!--<div class="xuanzhe martop15">
                                                        <input type="checkbox" style="width: 15px;height: 15px;" id="isfapiao"/>
                                                        <span>开具发票</span>
                                                        <div class="fapiao martop15" id="box_fapiao" style="display:none">
                                                        发票抬头：
                                                        <input type="text" class="input_style"  id="title" placeholder="某某有限公司"/>
                                                    </div>
                                                    </div>-->
                                                    <!--<div class="xuanzhe martop15">
                                                        <input type="checkbox" style="width: 15px;height: 15px;" id="isdaijinquan"/>
                                                        <span >使用代金券</span> 
                                                        <div id="box_daijinquan"  class="box_daijinquan  martop15" style="display:none;">
                                                        <p>现有代金券金额：<span class="orange"><span class="balance">${totalCouponMoney}</span>元</span></p>
                                                        <p class="martop15">现有代金券张数：<span class="orange">2</span></p>
                                                        <p  class="martop15">请输入使用代金券金额：<span class="orange">￥</span><input type="text" id="couponing" value="0" class="orange" onkeyup="value=value.replace(/[^\d.]/g,'')"/></p>
                                                        
                                                        
                                                
                                                
                                                    <span class="begin  martop15"><a class="orange" id="addCoupon">添加代金券</a></span>
                                                    <div class="box_coupon">
                                                    </div>
                                                        <div class="martop30">
                                                        <input type="hidden" id="giftMoney" value="${giftMoney}">
                                                        </div>
                                                    </div>
                                                    </div>-->
                                                    <!-- <div class="xuanzhe martop15">代金券抵用金额：<span class="orange">￥200</span></div> -->
                                                <!-- 	<div class="xuanzhe martop15">
                                                        <input type="checkbox" style="width: 15px;height: 15px;" id="ischongzhi" autocomplete="off"/>
                                                        <span >储值券充值</span> 
                                                        <div id="box_chongzhi"  class="box_chongzhi  martop15" style="display:none;">
                                                        <a class="orange" id="addChongzhi">添加储值券</a>
                                                        <div class="box_chongzhiquan"></div>
                                                    </div>
                                                    </div> -->
                                            </div>
                                            <div style="">
                                                <c:choose>
                                                    <c:when test="${str.inOrderId == 'qixingbook'}">
                                                        <input type="button" value="确认付款" class="corange present" onclick="submitPayAddress()" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="button" value="确认付款" class="corange present" onclick="submitPay()" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
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