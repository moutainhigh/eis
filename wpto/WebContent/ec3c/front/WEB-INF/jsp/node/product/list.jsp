<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<title>空调_深圳市凯特威武科技有限公司 - Powered by ECShop</title>
<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/theme/${theme}/js/jquery-1.9.1.min.js" tppabs="http://www.ktwwg.top/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" tppabs="http://www.ktwwg.top/js/jquery.json.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/common.js" tppabs="http://www.ktwwg.top/js/common.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/global.js" tppabs="http://www.ktwwg.top/js/global.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/compare.js" tppabs="http://www.ktwwg.top/js/compare.js"></script>
</head>
<body>
  
<%@include file="/WEB-INF/jsp/include/head.jsp" %> 
 
  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="..htm" tppabs="http://www.ktwwg.top/.">首页</a> <code>&gt;</code> <a href="category.php-id=5.htm" tppabs="http://www.ktwwg.top/category.php?id=5">空调</a> 
</div>
</div>
<div class="blank"></div>
<div class="block clearfix">
  
  <div class="AreaL">
    
  <div id="category_tree">
  <div class="tit">所有商品分类</div>
  <dl class="clearfix" style=" overflow:hidden;" >
   <div class="box1 cate" id="cate">
					<h1 onclick="tab(0)"  
		
		
		        style="border-top:none"
         
		 
		
		
		>
		
		 		
		<span class="f_l"><img src="/theme/${theme}/images/btn_fold.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="category.php-id=5.htm" tppabs="http://www.ktwwg.top/category.php?id=5" class="  f_l">空调</a>
		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
					<h1 onclick="tab(1)"  
		
		
		 
		 
		
		
		>
		
		 		
		<span class="f_l"><img src="/theme/${theme}/images/btn_fold.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="category.php-id=11.htm" tppabs="http://www.ktwwg.top/category.php?id=11" class="  f_l">冰箱</a>
		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
					<h1 onclick="tab(2)"  
		
		
		 
		 
		
		
		>
		
		 		
		<span class="f_l"><img src="/theme/${theme}/images/btn_fold.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="category.php-id=12.htm" tppabs="http://www.ktwwg.top/category.php?id=12" class="  f_l">电视</a>
		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
	 
</div>
<div style="clear:both"></div>  
</div>  
 
<div class="blank"></div>
<script type="text/javascript">
obj_h4 = document.getElementById("cate").getElementsByTagName("h4")
obj_ul = document.getElementById("cate").getElementsByTagName("ul")
obj_img = document.getElementById("cate").getElementsByTagName("img")
function tab(id)
{ 
		if(obj_ul.item(id).style.display == "block")
		{
			obj_ul.item(id).style.display = "none"
			obj_img.item(id).src = "/theme/${theme}/images/btn_fold.gif"/*tpa=http://www.ktwwg.top//theme/${theme}/images/btn_fold.gif*/
			return false;
		}
		else(obj_ul.item(id).style.display == "none")
		{
			obj_ul.item(id).style.display = "block"
			obj_img.item(id).src = "/theme/${theme}/images/btn_unfold.gif"/*tpa=http://www.ktwwg.top//theme/${theme}/images/btn_unfold.gif*/
		}
}
</script><div class="box" id='history_div'> <div class="box_1">
 <h3><span>浏览历史</span></h3>
 
  <div class="boxCenterList clearfix" id='history_list'>
      </div>
 </div>
</div>
<div class="blank5"></div>
<script type="text/javascript">
if (document.getElementById('history_list').innerHTML.replace(/\s/g,'').length<1)
{
    document.getElementById('history_div').style.display='none';
}
else
{
    document.getElementById('history_div').style.display='block';
}
function clear_history()
{
Ajax.call('user.php.htm'/*tpa=http://www.ktwwg.top/user.php*/, 'act=clear_history',clear_history_Response, 'GET', 'TEXT',1,1);
}
function clear_history_Response(res)
{
document.getElementById('history_list').innerHTML = '您已清空最近浏览过的商品';
}
</script>
    
  </div>
  
  
  <div class="AreaR">
	 
	  	 
   
<div class="box">
 <div class="box_1">
  <h3>
  <span>商品列表</span>
  <form method="GET" class="sort" name="listform">
  显示方式：
  <a href="javascript:;" onClick="javascript:display_mode('list')"><img src="/theme/${theme}/images/display_mode_list.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/display_mode_list.gif" alt=""></a>
  <a href="javascript:;" onClick="javascript:display_mode('grid')"><img src="/theme/${theme}/images/display_mode_grid_act.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/display_mode_grid_act.gif" alt=""></a>
  <a href="javascript:;" onClick="javascript:display_mode('text')"><img src="/theme/${theme}/images/display_mode_text.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/display_mode_text.gif" alt=""></a>&nbsp;&nbsp;
  
  <a href="category.php-category=5&display=grid&brand=0&price_min=0&price_max=0&filter_attr=0&page=1&sort=goods_id&order=ASC.htm#goods_list" tppabs="http://www.ktwwg.top/category.php?category=5&display=grid&brand=0&price_min=0&price_max=0&filter_attr=0&page=1&sort=goods_id&order=ASC#goods_list"><img src="/theme/${theme}/images/goods_id_DESC.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/goods_id_DESC.gif" alt="按上架时间排序"></a>
  <a href="category.php-category=5&display=grid&brand=0&price_min=0&price_max=0&filter_attr=0&page=1&sort=shop_price&order=ASC.htm#goods_list" tppabs="http://www.ktwwg.top/category.php?category=5&display=grid&brand=0&price_min=0&price_max=0&filter_attr=0&page=1&sort=shop_price&order=ASC#goods_list"><img src="/theme/${theme}/images/shop_price_default.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/shop_price_default.gif" alt="按价格排序"></a>
  <a href="category.php-category=5&display=grid&brand=0&price_min=0&price_max=0&filter_attr=0&page=1&sort=last_update&order=DESC.htm#goods_list" tppabs="http://www.ktwwg.top/category.php?category=5&display=grid&brand=0&price_min=0&price_max=0&filter_attr=0&page=1&sort=last_update&order=DESC#goods_list"><img src="/theme/${theme}/images/last_update_default.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/last_update_default.gif" alt="按更新时间排序"></a>
  <input type="hidden" name="category" value="5" />
  <input type="hidden" name="display" value="grid" id="display" />
  <input type="hidden" name="brand" value="0" />
  <input type="hidden" name="price_min" value="0" />
  <input type="hidden" name="price_max" value="0" />
  <input type="hidden" name="filter_attr" value="0" />
  <input type="hidden" name="page" value="1" />
  <input type="hidden" name="sort" value="goods_id" />
  <input type="hidden" name="order" value="DESC" />
  </form>
  </h3>
      <form name="compareForm" action="compare.php.htm" tppabs="http://www.ktwwg.top/compare.php" method="post" onSubmit="return compareGoods(this);">
            <div class="clearfix goodsBox" style="border:none;">
             <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=30.htm" tppabs="http://www.ktwwg.top/goods.php?id=30"><img src="images/201603/thumb_img/30_thumb_G_1457385750536.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/30_thumb_G_1457385750536.jpg" alt="Gree/格力..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=30.htm" tppabs="http://www.ktwwg.top/goods.php?id=30" title="Gree/格力 KFR-50LW(50551)NhAa-3 大2匹高端柜机定速空调 i酷">Gree/格力...</a></p>
                                    市场价：<font class="market_s">￥7679元</font><br />
                                                                        本店价：<font class="shop_s">￥6399元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=29.htm" tppabs="http://www.ktwwg.top/goods.php?id=29"><img src="images/201603/thumb_img/29_thumb_G_1457385660969.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/29_thumb_G_1457385660969.jpg" alt="Gree/格力..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=29.htm" tppabs="http://www.ktwwg.top/goods.php?id=29" title="Gree/格力 KFR-72LW/(72595)NhAa-3 3匹智能定速立式柜机空调Q雅">Gree/格力...</a></p>
                                    市场价：<font class="market_s">￥7199元</font><br />
                                                                        本店价：<font class="shop_s">￥5999元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=28.htm" tppabs="http://www.ktwwg.top/goods.php?id=28"><img src="images/201603/thumb_img/28_thumb_G_1457385607245.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/28_thumb_G_1457385607245.jpg" alt="Gree/格力..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=28.htm" tppabs="http://www.ktwwg.top/goods.php?id=28" title="Gree/格力 KFR-35GW/(35596)FNAa-A3 变频大1.5P冷暖空调 Q铂">Gree/格力...</a></p>
                                    市场价：<font class="market_s">￥3839元</font><br />
                                                                        本店价：<font class="shop_s">￥3199元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=27.htm" tppabs="http://www.ktwwg.top/goods.php?id=27"><img src="images/201603/thumb_img/27_thumb_G_1457385521535.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/27_thumb_G_1457385521535.jpg" alt="Gree/格力..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=27.htm" tppabs="http://www.ktwwg.top/goods.php?id=27" title="Gree/格力 KFR-72LW/(72551)FNAb-A3 3匹变频柜机水晶白i酷">Gree/格力...</a></p>
                                    市场价：<font class="market_s">￥9599元</font><br />
                                                                        本店价：<font class="shop_s">￥7999元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=26.htm" tppabs="http://www.ktwwg.top/goods.php?id=26"><img src="images/201603/thumb_img/26_thumb_G_1457385434712.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/26_thumb_G_1457385434712.jpg" alt="Gree/格力..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=26.htm" tppabs="http://www.ktwwg.top/goods.php?id=26" title="Gree/格力 KFR-35GW/(35592)FNhDa-A3大1.5匹除甲醛变频空调 品圆">Gree/格力...</a></p>
                                    市场价：<font class="market_s">￥3959元</font><br />
                                                                        本店价：<font class="shop_s">￥3299元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=25.htm" tppabs="http://www.ktwwg.top/goods.php?id=25"><img src="images/201603/thumb_img/25_thumb_G_1457385265331.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/25_thumb_G_1457385265331.jpg" alt="Midea/美..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=25.htm" tppabs="http://www.ktwwg.top/goods.php?id=25" title="Midea/美的 KFR-35GW/WPAA3美的大1.5匹变频内外双静音极速冷暖">Midea/美...</a></p>
                                    市场价：<font class="market_s">￥3479元</font><br />
                                                                        本店价：<font class="shop_s">￥2899元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=24.htm" tppabs="http://www.ktwwg.top/goods.php?id=24"><img src="images/201603/thumb_img/24_thumb_G_1457385193537.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/24_thumb_G_1457385193537.jpg" alt="Midea/美..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=24.htm" tppabs="http://www.ktwwg.top/goods.php?id=24" title="Midea/美的 KFR-51LW/WPAD3大2匹立体远距离送风双重滤网冷暖柜机">Midea/美...</a></p>
                                    市场价：<font class="market_s">￥4919元</font><br />
                                                                        本店价：<font class="shop_s">￥4099元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=23.htm" tppabs="http://www.ktwwg.top/goods.php?id=23"><img src="images/201603/thumb_img/23_thumb_G_1457385136489.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/23_thumb_G_1457385136489.jpg" alt="Midea/美..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=23.htm" tppabs="http://www.ktwwg.top/goods.php?id=23" title="Midea/美的 KFR-35GW/WJBA3@大1.5匹智能云除甲醛极速冷暖变频">Midea/美...</a></p>
                                    市场价：<font class="market_s">￥3359元</font><br />
                                                                        本店价：<font class="shop_s">￥2799元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=22.htm" tppabs="http://www.ktwwg.top/goods.php?id=22"><img src="images/201603/thumb_img/22_thumb_G_1457385088653.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/22_thumb_G_1457385088653.jpg" alt="Midea/美..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=22.htm" tppabs="http://www.ktwwg.top/goods.php?id=22" title="Midea/美的 KFR-35GW/WCBA3@大1.5匹阿里小智智能云变频家用空调">Midea/美...</a></p>
                                    市场价：<font class="market_s">￥3839元</font><br />
                                                                        本店价：<font class="shop_s">￥3199元</font><br />
                        			 
        </div>
                 <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           <a href="goods.php-id=21.htm" tppabs="http://www.ktwwg.top/goods.php?id=21"><img src="images/201603/thumb_img/21_thumb_G_1457384986341.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/21_thumb_G_1457384986341.jpg" alt="Midea/美..." class="goodsimg" /></a><br />
           <p><a href="goods.php-id=21.htm" tppabs="http://www.ktwwg.top/goods.php?id=21" title="Midea/美的 KFR-26GW/WPAD3大1匹壁挂式家用冷暖定速空调送货入户">Midea/美...</a></p>
                                    市场价：<font class="market_s">￥6599元</font><br />
                                                                        本店价：<font class="shop_s">￥5499元</font><br />
                        			 
        </div>
            </div>
        </form>
  
 </div>
</div>
<div class="blank5"></div>
<script type="Text/Javascript" language="JavaScript">
<!--
function selectPage(sel)
{
  sel.form.submit();
}
//-->
</script>
<script type="text/javascript">
window.onload = function()
{
  Compare.init();
  fixpng();
}
var button_compare = '';
var exist = "您已经选择了%s";
var count_limit = "最多只能选择4个商品进行对比";
var goods_type_different = "\"%s\"和已选择商品类型不同无法进行对比";
var compare_no_goods = "您没有选定任何需要比较的商品或者比较的商品数少于 2 个。";
var btn_buy = "购买";
var is_cancel = "取消";
var select_spe = "请选择商品属性";
</script>
<form name="selectPageForm" action="index.htm" tppabs="http://www.ktwwg.top/category.php" method="get">
 <div id="pager" class="pagebar">
  <span class="f_l " style="margin-right:10px;">总计 <b>10</b>  个记录</span>
      
      </div>
</form>
<script type="Text/Javascript" language="JavaScript">
<!--
function selectPage(sel)
{
  sel.form.submit();
}
//-->
</script>
  </div>  
  
</div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    
</body>
</html>
