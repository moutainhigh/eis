<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!--<link rel="stylesheet" href="/theme/${theme}/css/qq.css">-->
 <script type="text/javascript">
var process_request = "正在处理您的请求...";
</script>
<script type="text/javascript">
//设为首页 www.ecmoban.com
function SetHome(obj,url){
    try{
        obj.style.behavior='url(#default#homepage)';
       obj.setHomePage(url);
   }catch(e){
       if(window.netscape){
          try{
              netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
         }catch(e){
              alert("抱歉，此操作被浏览器拒绝！\n\n请在浏览器地址栏输入“about:config”并回车然后将[signed.applets.codebase_principal_support]设置为'true'");
          }
       }else{
        alert("抱歉，您所使用的浏览器无法完成此操作。\n\n您需要手动将【"+url+"】设置为首页。");
       }
  }
}
 
//收藏本站 bbs.ecmoban.com
function AddFavorite(title, url) {
  try {
      window.external.addFavorite(url, title);
  }
catch (e) {
     try {
       window.sidebar.addPanel(title, url, "");
    }
     catch (e) {
         alert("抱歉，您所使用的浏览器无法完成此操作。\n\n加入收藏失败，请使用Ctrl+D进行添加");
     }
  }
}
</script>
<script type="text/javascript">
    //初始化主菜单
      function sw_nav(obj,tag)
      {
          var DisSub = document.getElementById("DisSub_"+obj);
          var HandleLI= document.getElementById("HandleLI_"+obj);
          if(tag==1)
          {
              DisSub.style.display = "block";
          }
          else
          {
              DisSub.style.display = "none";
          }
      }
</script>
<c:choose>
  <c:when test="${theme=='basic'}">  
<div class="ng-toolbar top_nav">
    <div class="ng-toolbar-con block">
        <div class="ng-toolbar-left">
            <a class="a-fav-ehaier" href="javascript:alert('当前浏览器不支持此操作，请使用 Ctrl+D 收藏${systemName}！')">收藏${systemName}</a>
        </div>
        <div class="ng-toolbar-right"> 
            <div class="ng-bar-node reg-bar-node" id="reg-bar-node" style="display: block;"> 
              <!--<c:if test="${!empty frontUser.username}">
                <form id="logout">
                  ${frontUser.username}，欢迎光临本店 <span onclick="secede()" style="background-color:transparent;color: #666;border: none;cursor: pointer;" >退出</span>
                </form>
              </c:if>

              <c:if test="${empty frontUser.username}">
                 嗨，欢迎来到本网站 请<a class="login"  href="/user/login.shtml">登录</a> &nbsp;|&nbsp; <a  href="/user/register.shtml">免费注册</a>
              </c:if>-->
 
            </div>
            <a href="/content/center/center.shtml" style="float:left;background-color: #DADADA;    padding: 0px 9px;    border-radius: 5px;">用户中心</a><i class="ng-line"></i>
             
        </div>
    </div>
</div>

<div class="ng-header">
    <div class="ng-header-con block">
        <div class="ng-header-box">
            <!--<div class="head-promise"><a class="a-promise freeship" href="#" rel="nofollow">全场免运费</a><a class="a-promise deliverinstall" href="#" rel="nofollow">送装同步</a><a class="a-promise satisfy" href="#" rel="nofollow">满意再付款</a></div>-->
            <a href="/" class="logo">
                <%--首页<!-- <img src="../../../../theme/${theme}/images/logo.gif"> -->--%>
                首页 <img src="/theme/${theme}/images/www.xn--5cx06q.com_logo.gif">
            </a>
            <a href="javascript:void(0)" class="slogn">
            </a>    
            <div class="ng-search" onkeydown="keySearch()"> 
                <div class="g-search">
                    <!--<form id="searchForm" name="searchForm" method="get" onSubmit="return checkSearchForm()"  >-->
                    <div class="search-keyword-box">
                    <input name="keywords" type="text" id="keyword" value="【十惠国庆，一促到底】" class="search-keyword" onfocus="javascript:if(this.value=='【十惠国庆，一促到底】'){this.value=''}" onblur="javascript:if(this.value==''){this.value='【十惠国庆，一促到底】'}" />
                    </div>
                    <input name="imageField" type="button" value="搜索"  class="btn-search" style="cursor:pointer;" onclick="search()"/>
                    <div class="clear"></div>
                    <div id="snKeywordNew" class="g-search-hotwords"> 
                    <script type="text/javascript">
                    
                    function checkSearchForm()
                    {
                    if(document.getElementById('keyword').value)
                    {
                    return true;
                    }
                    else
                    {
                    alert("请输入搜索关键词！");
                    return false;
                    }
                    }
                    
                    </script> 
                     
                    </div>
                    <!--</form>-->
                </div>
            </div>
        </div>
    </div>
</div>

<div class="ng-nav-bar">
    <div class="block">
        
        <div class="ng-sort"  onMouseOver="sw_nav('sort_list',1)" onMouseOut="sw_nav('sort_list',0)">  
            <a class="ng-all-hook" href="/" >商品分类</a>
            <div class="ng-sort-list-box " id="DisSub_sort_list" >
                <dl>   
                      <c:forEach items="${navigation}" var="a" >
                          <dt><a class="a" href="${a.viewUrl}">${a.name}</a></dt>
                          <dd></dd>
                        </c:forEach>
                     
                 </dl>    
                 
            </div>
        </div>
        <div class="ng-nav-index">
            <ul class="ng-nav">
                <li><a href="/">首页</a></li>
                <c:forEach items="${navigation}" var="a" >
                          <li class="c"><a href="${a.viewUrl}" >${a.name}</a></li>
                    </c:forEach>
            </ul>
        </div>
        
        
        <div id="ECS_CARTINFO">
            <div class="mini-cart"><a href="/cart.shtml" title="查看购物车">查看购物车<span id="shopping-amount">
              <c:choose>
                <c:when test="${!empty cartCount}">${cartCount}</c:when>
                <c:when test="${!empty cart.totalGoods}">${cart.totalGoods}</c:when>
                <c:otherwise>0</c:otherwise>
              </c:choose>
            </span></a>
        </div>
<script type="text/javascript">
// function deleteCartGoods(rec_id)
// {
//     Ajax.call('http://www.ktwwg.top/delete_cart_goods.php', 'id='+rec_id, deleteCartGoodsResponse, 'POST', 'JSON');
// }

/**
 * 接收返回的信息
 */
function deleteCartGoodsResponse(res)
{
  if (res.error)
  {
    alert(res.err_msg);
  }
  else
  {
      $("#ECS_CARTINFO").html(res.content);
      $("#ECS_CARTINFO2").html(res.content);
  }
}
</script>         </div>
        
    </div>
</div>
</c:when>
<c:when test="${theme=='guohongchangsheng'}">
<div class="hd_global_top_bar" id="global_top_bar">
  <div class="wrap clearfix">
    <div class="hd_topbar_left clearfix">
      <div class="hd_unlogin_wrap" id="global_unlogin">
        <div class="ng-bar-node reg-bar-node" id="reg-bar-node" style="display: block;"> </div>
        
      </div>
    </div>
    <!-- <div class="hd_top_manu clearfix">
      
      <div class="hd_weixin_show"> <i></i>
        <p> 扫描二维码<br>
          关注北京国宏长圣科技有限公司微信 </p>
        <img src="themes/ecmoban_yihaodian/images/suspension-code.png" alt="扫描二维码<br />关注北京国宏长圣科技有限公司微信"> </div>
      </a> </div> -->
  </div>
</div>
<div id="site_header" class="wrap hd_header clearfix">
  <div class="hd_logo_area fl clearfix" id="logo_areaID"> 
  
  <a class="fl" href="/" style="line-height: 59px;font-size: 15px;font-weight: bold;">${systemName}</a>
  
  </div>
  
  <!-- 搜索 -->
     <div class="ng-search hd_head_search" onkeydown="keySearch()"> 
                <div class="g-search clearfix">
                    <!--<form id="searchForm" name="searchForm" method="get" onSubmit="return checkSearchForm()"  >-->
                    <div class="search-keyword-box hd_search_wrap clearfix">
                    <input name="keywords" type="text" id="keyword" value="【十惠国庆，一促到底】" class="search-keyword" onfocus="javascript:if(this.value=='【十惠国庆，一促到底】'){this.value=''}" onblur="javascript:if(this.value==''){this.value='【十惠国庆，一促到底】'}" />
                    </div>
                    <input name="imageField" type="button" value="搜索"  class="btn-search hd_search_btn" style="cursor:pointer;" onclick="search()"/>
                    <div class="clear"></div>
                    <div id="snKeywordNew" class="g-search-hotwords"> 
                    <script type="text/javascript">
                    
                    function checkSearchForm()
                    {
                    if(document.getElementById('keyword').value)
                    {
                    return true;
                    }
                    else
                    {
                    alert("请输入搜索关键词！");
                    return false;
                    }
                    }
                    
                    </script> 
                     
                    </div>
                    <!--</form>-->
                </div>
            </div>

  <div id="hdPrismWrap" class="hd_prism_wrap">
    <div id="hdPrismCoupon" class="hd_prism hd_welfare"> <u id="hdPrismCouponNum" style="display:none;"></u> <a class="hd_prism_tab" target="_blank" href="<c:url value='/content/center/myPacket.shtml' />" > <em></em>
      <p>福利</p>
      <i></i> </a>
      <div id="hdPrismCouponList" class="hd_prism_show global_loading"> </div>
    </div>
    <div id="hdPrismOrder" class="hd_prism hd_order"> <u id="hdPrismOrderNum" style="display:none;"></u> <a class="hd_prism_tab" data-ref="top_prism_order" target="_blank" href="<c:url value='/content/center/myOrders.shtml' />" > <em></em>
      <p>我的订单</p>
      <i></i> </a>
      <div id="hdPrismOrderList" class="hd_prism_show global_loading"> </div>
    </div>
  <div class="hd_mini_cart">
        
        <i></i><span id="in_cart_num"><u id="ECS_CARTINFO"><a href="/cart.shtml"  title="查看购物车">
          <c:choose>
                <c:when test="${!empty cartCount}">${cartCount}</c:when>
                <c:when test="${!empty cart.totalGoods}">${cart.totalGoods}</c:when>
                <c:otherwise>0</c:otherwise>
              </c:choose>
        </a></u></span> <a class="hd_prism_cart" href="/cart.shtml" > <em></em>购物车 </a> 
<div class="ap_shopping_warning"></div>
</div>
    
  </div>
  
</div>

<div class="ng-nav-bar">
    <div class="block">
        
        <div class="ng-sort"  onMouseOver="sw_nav('sort_list',1)" onMouseOut="sw_nav('sort_list',0)">  
            <a class="ng-all-hook" href="/" >商品分类</a>
            <div class="ng-sort-list-box " id="DisSub_sort_list" >
                <dl>   
                      <c:forEach items="${navigation}" var="a" >
                          <dt><a class="a" href="${a.viewUrl}">${a.name}</a></dt>
                          <dd></dd>
                        </c:forEach>
                     
                 </dl>    
            </div>
        </div>
        <div class="ng-nav-index">
            <ul class="ng-nav">
                <li><a href="/">首页</a></li>
            </ul>
        </div>
        
        
        <div id="ECS_CARTINFO">
 
<script type="text/javascript">
// function deleteCartGoods(rec_id)
// {
//     Ajax.call('http://www.ktwwg.top/delete_cart_goods.php', 'id='+rec_id, deleteCartGoodsResponse, 'POST', 'JSON');
// }

/**
 * 接收返回的信息
 */
function deleteCartGoodsResponse(res)
{
  if (res.error)
  {
    alert(res.err_msg);
  }
  else
  {
      $("#ECS_CARTINFO").html(res.content);
      $("#ECS_CARTINFO2").html(res.content);
  }
}
</script>         </div>
        
    </div>
</div>
</c:when>
<c:when test="${theme=='beijingjiamei'}">
<div class="ng-toolbar top_nav">
    <div class="ng-toolbar-con block">
        <div class="ng-toolbar-left" style="float:left;">
            <a class="a-fav-ehaier" href="javascript:alert('当前浏览器不支持此操作，请使用 Ctrl+D 收藏${systemName}！')">收藏${systemName}</a>
        </div>
        <div class="ng-toolbar-right"style="float:right;"> 
            <div class="ng-bar-node reg-bar-node" id="reg-bar-node" style="display: block;"> 

            </div>
        </div>
    </div>
</div>
<div class="block header clearfix">
        <div class="top clearfix">
        <a class="fl" href="/" style="line-height: 59px;font-size: 15px;font-weight: bold;">${systemName}</a>
        <div class="head_r"> 
        <!-- 搜索 -->
     <div class="ng-search hd_head_search" onkeydown="keySearch()"> 
                <div class="g-search clearfix top_search" style="margin-top: 17px;">
                    <!--<form id="searchForm" name="searchForm" method="get" onSubmit="return checkSearchForm()"  >-->
                    <div class="search-keyword-box hd_search_wrap clearfix " style="float: left;">
                    <input name="keywords" type="text" id="keyword" placeholder="搜索商品" class="search-keyword"  style="border: none;    background-color: transparent; height: 24px;"/>                
                  </div>
                    <input name="imageField" type="button" class="btn-search hd_search_btn keyword" style="cursor:pointer;" id='seachbtn' onclick="search()"/>
                    <div class="clear"></div>
                    <div id="snKeywordNew" class="g-search-hotwords"> 
                    <script type="text/javascript">
                    
                    function checkSearchForm()
                    {
                    if(document.getElementById('keyword').value)
                    {
                    return true;
                    }
                    else
                    {
                    alert("请输入搜索关键词！");
                    return false;
                    }
                    }
                    
                    </script> 
                     
                    </div>
                    <!--</form>-->
                </div>
            </div>
         
        </div>
        </div>
    </div>
    <div class="menu_box clearfix"> 
<div class="block"> 
<div class="menu">
  <a href="/" class="cur">首页<span></span></a>
  </div> 
<div class="RightNav f_r">
     <div class="Carta" id="ECS_CARTINFO"><a href="/cart.shtml"  title="购物车" id="shopping-amount">
       <c:choose>
                <c:when test="${!empty cartCount}">${cartCount}</c:when>
                <c:when test="${!empty cart.totalGoods}">${cart.totalGoods}</c:when>
                <c:otherwise>0</c:otherwise>
              </c:choose>
     </a></div>
        <div class="Settlement"><a href="<c:url value='/content/center/myOrders.shtml'/>">我的订单</a></div>
      </div>
</div>
</div>
<!-- 浮窗客服 -->
<div class="QQbox" id="divQQbox" style="width: 170px; top: 1939.5px;">
<div class="Qlist" id="divOnline" onmouseout="hideMsgBox(event);" style="display: none;" onmouseover="OnlineOver();">
    <div class="t"></div>
    <div class="infobox">我们营业的时间<br>9:00-18:00</div>
    <div class="con">
        <ul>
                                                                                  
    
        <li> 服务热线: 010-67375667</li>
         </ul>
    </div>
    <div class="b"></div>
</div>
<div id="divMenu" onmouseover="OnlineOver();" style="display: block;"><img src="/theme/${theme}/images/qq_1.gif" class="press" alt="在线咨询"></div>
</div>
<script type="text/javascript">
//<![CDATA[
var tips; var theTop = 120/*这是默认高度,越大越往下*/; var old = theTop;
function initFloatTips() {
tips = document.getElementById('divQQbox');
moveTips();
};
function moveTips() {
var tt=50;
if (window.innerHeight) {
pos = window.pageYOffset
}
else if (document.documentElement && document.documentElement.scrollTop) {
pos = document.documentElement.scrollTop
}
else if (document.body) {
pos = document.body.scrollTop;
}
pos=pos-tips.offsetTop+theTop;
pos=tips.offsetTop+pos/10;
if (pos < theTop) pos = theTop;
if (pos != old) {
tips.style.top = pos+"px";
tt=10;
//alert(tips.style.top);
}
old = pos;
setTimeout(moveTips,tt);
}
//!]]>
initFloatTips();
function OnlineOver(){
document.getElementById("divMenu").style.display = "none";
document.getElementById("divOnline").style.display = "block";
document.getElementById("divQQbox").style.width = "170px";
}
function OnlineOut(){
document.getElementById("divMenu").style.display = "block";
document.getElementById("divOnline").style.display = "none";
}
if(typeof(HTMLElement)!="undefined")    //给firefox定义contains()方法，ie下不起作用
{   
      HTMLElement.prototype.contains=function(obj)   
      {   
          while(obj!=null&&typeof(obj.tagName)!="undefind"){ //通过循环对比来判断是不是obj的父元素
   　　　　if(obj==this) return true;   
   　　　　obj=obj.parentNode;
   　　}   
          return false;   
      };   
}  
function hideMsgBox(theEvent){ //theEvent用来传入事件，Firefox的方式
　 if (theEvent){
　 var browser=navigator.userAgent; //取得浏览器属性
　 if (browser.indexOf("Firefox")>0){ //如果是Firefox
　　 if (document.getElementById('divOnline').contains(theEvent.relatedTarget)) { //如果是子元素
　　 return; //结束函式
} 
} 
if (browser.indexOf("MSIE")>0){ //如果是IE
if (document.getElementById('divOnline').contains(event.toElement)) { //如果是子元素
return; //结束函式
}
}
}
/*要执行的操作*/
document.getElementById("divMenu").style.display = "block";
document.getElementById("divOnline").style.display = "none";
}
</script>
</c:when>
<c:otherwise>
<div class="top_nav">
  <script type="text/javascript">
          //初始化主菜单
            function sw_nav(obj,tag)
            {
     
            var DisSub = document.getElementById("DisSub_"+obj);
            var HandleLI= document.getElementById("HandleLI_"+obj);
                if(tag==1)
                {
                    DisSub.style.display = "block";
             
                    
                }
                else
                {
                    DisSub.style.display = "none";
                
                }
     
            }
     
    </script>
    <div class="block">     
    
        <ul class="top_bav_l">
        <li class="top_sc">
           <a href="javascript:void(0);" onclick="AddFavorite('我的网站',location.href)">收藏本站</a>
</li>
            <li>关注我们：</li>
            <li style="border:none" class="menuPopup">
            <a id="HandleLI_1" href="javascript:;" title="微博" class="attention"></a> 
            <div id="DisSub_1" class="top_nav_box  top_weibo"> 
            <a href="http://e.weibo.com/ECMBT" target="_blank" title="新浪微博" class="top_weibo"></a>
            <a href="http://e.t.qq.com/ecmoban_com" target="_blank" title="QQ微博" class="top_qq"></a> 
            </div> 
            </li> 
            <li class="menuPopup">
            <a id="HandleLI_2" href="javascript:;" title="微信" class="top_weixin"></a> 
            <div id="DisSub_2" class="weixinBox" style="display: none;"> 
    
            <img src="../../../../theme/${theme}/images/weixin.png" style="width:150px; height:190px;  background:#0000CC" width="150" height="190"> 
            </div> 
            </li>
        </ul>
        <div class="header_r" id="reg-bar-node">
        <!--<font id="ECS_MEMBERZONE">
          <c:if test="${!empty frontUser.username}">
            <form id="logout">
              ${frontUser.username}，欢迎光临本店 <span onclick="secede()" style="background-color:transparent;color: #666;border: none;cursor: pointer;" >退出</span>
            </form>
          </c:if>
          <c:if test="${empty frontUser.username}">
             嗨，欢迎来到本网站 请<a class="login"  href="/user/login.shtml">登录</a> &nbsp;|&nbsp; <a  href="/user/register.shtml">免费注册</a>
          </c:if>
        </font>-->
     
                  
        </div>
    </div>
</div>
<div class=" block header_bg" style="margin-bottom: 0px;">
  <div class="clear_f"></div>
  <div class="header_top logo_wrap"> 
  <a class="logo_new" href="/" style="line-height: 59px;font-size: 15px;font-weight: bold;">${systemName}</a>
    <div class="menu">
      <a href="/" class="cur">首页<span></span></a>
     <!--  <c:if test="${theme=='basic2'}" >
            <a href="${node.viewUrl}">
      手机      </a>
      </c:if> -->
             <a href="/content/about/contact.shtml"  >联系我们</a>
             <a href="/content/about/about.shtml">公司简介</a>
       
           </div>
    <div class="ser_n">
    <div class="g-search" onkeydown="keySearch()">
      <!--<form id="searchForm" class="searchBox" name="searchForm" method="get" action="search.php" onsubmit="return checkSearchForm()">-->
          <div class="search-keyword-box" id="keywrap">
              <input name="keywords" type="text" id="keyword" class="search-keyword" onfocus="searchFocus()" onblur="searchBlur()">
          </div>
          <input type="button" name="imageField" id="searchBtn" class="iconfont search-btn" value=""  onclick="search()">
      <!--</form>-->
      <div id="test" class="g-search-hotwords">
   <script type="text/javascript">
    

    // function checkSearchForm()
    // {
    //     if(document.getElementById('keyword').value)
    //     {
    //         return true;
    //     }
    //     else
    //     {
    //         alert("请输入搜索关键词！");
    //         return false;
    //     }
    // }
  
  function searchFocus(){
    var wrap=document.getElementById("keywrap");
    var ipt=document.getElementById("keyword");
    var hot=document.getElementById("test");
    var btn=document.getElementById("searchBtn");
    if(ipt.value.length==0){
      hot.style.display="none"; 
    }
    wrap.style.borderColor="#ff6700";
    btn.style.borderColor="#ff6700";
  }
  
  function searchBlur(){
    var wrap=document.getElementById("keywrap");
    var ipt=document.getElementById("keyword");
    var hot=document.getElementById("test");
    var btn=document.getElementById("searchBtn");
    if(ipt.value.length==0){
      hot.style.display="";
    }
    wrap.style.borderColor="";
    btn.style.borderColor="";
  }

    
  
    </script>
      </div>
      </div>
      
    </div>
    <div class="home-ann">
    <div class="ann-box">
    <ul class="cart_info">
      <li id="ECS_CARTINFO"><span class="carts_num none_f"><a href="/cart.shtml" title="查看购物车"><span id="shopping-amount">
   
      </span></a></span> <em class="i_cart">&nbsp;</em><a href="/cart.shtml" class="a_cart" >查看购物车</a></li>
    </ul>
    </div>
    </div>
  </div>
</div>
</c:otherwise>
</c:choose>
<div style="clear:both"></div>
<script>
    // 退出按钮
    function secede(){
        $.ajax({  
            type : "get",  
            url : "/user/logout.json",  
            dataType:"json",
            data:'', 
            success : function(data) {  
            if (data.message.operateCode=='102008') {
                window.location.href = "/";
            }else{
                alert(data.message.message);
            }
            },  
            error : function(data) {  
                alert(data.message.message);
            }  
        }); 
    }

    // 获得cookie值
    function getCookie(name){
            var arr = document.cookie.split("; ");
            for(var i=0,len=arr.length;i<len;i++){
            var  item = arr[i].split("=");
            if(item[0]==name){
            return item[1];
            }
        }
        return "";
        }

              
    //商品分类在用户中心页隐藏
    $(function(){
        //  检验是否登录
        if(getCookie('eis_username') == ''){
            $('#reg-bar-node').empty().append('嗨，欢迎来到本网站 请<a class="login"  href="/user/login.shtml">登录</a> &nbsp;|&nbsp; <a  href="/user/register.shtml">免费注册</a>')
        }else{
            $('#reg-bar-node').empty().append('<form id="logout">'+decodeURIComponent(getCookie('eis_username'))+'，欢迎光临本店 <input value="退出" onclick="secede()" type="button" style="background-color:transparent;color: #999;border: none;"><a href="/content/center/center.shtml" style="float:right;color:#999;">用户中心</a></form>')
        }
            


        var sort_list=$("#DisSub_sort_list");
        if(sort_list.find("dl").is(":empty")){
            sort_list.remove();
        } 

        // 商品分类选中
        var url = window.location.href;
        if(url.indexOf('kongtiao') > -1){
            // $('.ng-all-hook').css('background','#333');
            $('.ng-nav-index ul li').eq(1).css('background','#005AA9');
        }else if(url.indexOf('bingxiang') > -1){
            // $('.ng-all-hook').css('background','#333');
            $('.ng-nav-index ul li').eq(2).css('background','#005AA9');
        }else if(url.indexOf('dianshi') > -1){
            // $('.ng-all-hook').css('background','#333');
            $('.ng-nav-index ul li').eq(3).css('background','#005AA9');
        }


        // 显示购物车数量
        $.ajax({  
            type : "get",  
            url : "/cart/count.json",  
            dataType:"json",
            data:{
                currentStatus:710018
            }, 
            success : function(data) { 
                $('#shopping-amount').text(data);
            },  
            error : function(data) {  
                alert(data.message.message);
            }  
        }); 
    });

    // 搜索框回车键
    function keySearch(){
            if(window.event.keyCode == 13){
                    search();
            }
    }


    //  搜索按钮
    function search(){
            var title = $('#keyword').val();
            if(title == ''){
                alert('搜索内容不能为空！');
            }else{
                window.location.href = '/content/search/index.shtml?search=search&title='+title;
            }
    }

</script>

