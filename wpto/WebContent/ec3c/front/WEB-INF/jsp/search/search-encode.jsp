<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>商品搜索_深圳市凯特威武科技有限公司 - Powered by ECShop</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/jquery-1.9.1.min.js" tppabs="http://www.ktwwg.top/js/jquery-1.9.1.min.js"></script><script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" tppabs="http://www.ktwwg.top/js/jquery.json.js"></script><script type="text/javascript" src="/theme/${theme}/js/utils.js" tppabs="http://www.ktwwg.top/js/utils.js"></script><script type="text/javascript" src="/theme/${theme}/js/common.js" tppabs="http://www.ktwwg.top/js/common.js"></script><script type="text/javascript" src="/theme/${theme}/js/global.js" tppabs="http://www.ktwwg.top/js/global.js"></script><script type="text/javascript" src="/theme/${theme}/js/compare.js" tppabs="http://www.ktwwg.top/js/compare.js"></script></head>
<body>
  <script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" tppabs="http://www.ktwwg.top/js/transport_jquery.js"></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 

 


  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="..htm" tppabs="http://www.ktwwg.top/.">首页</a> <code>&gt;</code> 商品搜索 
</div>
</div>
<div class="blank"></div><div class="block clearfix">
  
  <div class="AreaL">
    
 
    
  <div id="category_tree">
  <div class="tit">所有商品分类</div>
  <dl class="clearfix" style=" overflow:hidden;" >
   <div class="box1 cate" id="cate">
					<h1 onclick="tab(0)"  
		
		
		        style="border-top:none"
         
		 
		
		
		>
		
		 		
		<span class="f_l"><img src="themes/ecmoban_haier2015/images/btn_fold.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="category.php-id=5.htm" tppabs="http://www.ktwwg.top/category.php?id=5" class="  f_l">空调</a>

		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
					<h1 onclick="tab(1)"  
		
		
		 
		 
		
		
		>
		
		 		
		<span class="f_l"><img src="themes/ecmoban_haier2015/images/btn_fold.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="category.php-id=11.htm" tppabs="http://www.ktwwg.top/category.php?id=11" class="  f_l">冰箱</a>

		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
					<h1 onclick="tab(2)"  
		
		
		 
		 
		
		
		>
		
		 		
		<span class="f_l"><img src="themes/ecmoban_haier2015/images/btn_fold.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
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
			obj_img.item(id).src = "themes/ecmoban_haier2015/images/btn_fold.gif"/*tpa=http://www.ktwwg.top/themes/ecmoban_haier2015/images/btn_fold.gif*/
			return false;
		}
		else(obj_ul.item(id).style.display == "none")
		{
			obj_ul.item(id).style.display = "block"
			obj_img.item(id).src = "themes/ecmoban_haier2015/images/btn_unfold.gif"/*tpa=http://www.ktwwg.top/themes/ecmoban_haier2015/images/btn_unfold.gif*/
		}
}
</script>    
    
    
    
    <div class="box" id='history_div'> <div class="box_1">
 <h3><span>浏览历史</span></h3>

 
  <div class="boxCenterList clearfix" id='history_list'>
    <ul class="clearfix"><li class="goodsimg"><a href="goods.php-id=31.htm" tppabs="http://www.ktwwg.top/goods.php?id=31" target="_blank"><img src="images/201603/thumb_img/31_thumb_G_1457385860539.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/31_thumb_G_1457385860539.jpg" alt="Hisense/海信 LED55EC620UA 55吋4K超清14核智能平板液晶电视机50" class="B_blue" /></a></li><li><a href="goods.php-id=31.htm" tppabs="http://www.ktwwg.top/goods.php?id=31" target="_blank" title="Hisense/海信 LED55EC620UA 55吋4K超清14核智能平板液晶电视机50">Hisense...</a><br />本店售价：<font class="f1">￥3999元</font><br /></li></ul><ul class="clearfix"><li class="goodsimg"><a href="goods.php-id=32.htm" tppabs="http://www.ktwwg.top/goods.php?id=32" target="_blank"><img src="images/201603/thumb_img/32_thumb_G_1457386008360.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/32_thumb_G_1457386008360.jpg" alt="Hisense/海信 LED55EC620UA 55吋4K超清14核智能平板液晶电视机50" class="B_blue" /></a></li><li><a href="goods.php-id=32.htm" tppabs="http://www.ktwwg.top/goods.php?id=32" target="_blank" title="Hisense/海信 LED55EC620UA 55吋4K超清14核智能平板液晶电视机50">Hisense...</a><br />本店售价：<font class="f1">￥3999元</font><br /></li></ul><ul class="clearfix"><li class="goodsimg"><a href="goods.php-id=33.htm" tppabs="http://www.ktwwg.top/goods.php?id=33" target="_blank"><img src="images/201603/thumb_img/33_thumb_G_1457386083317.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/33_thumb_G_1457386083317.jpg" alt="Hisense/海信 LED58EC620UA 58英寸14核4K超清智能平板液晶电视机" class="B_blue" /></a></li><li><a href="goods.php-id=33.htm" tppabs="http://www.ktwwg.top/goods.php?id=33" target="_blank" title="Hisense/海信 LED58EC620UA 58英寸14核4K超清智能平板液晶电视机">Hisense...</a><br />本店售价：<font class="f1">￥4599元</font><br /></li></ul><ul class="clearfix"><li class="goodsimg"><a href="goods.php-id=34.htm" tppabs="http://www.ktwwg.top/goods.php?id=34" target="_blank"><img src="images/201603/thumb_img/34_thumb_G_1457386156495.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/34_thumb_G_1457386156495.jpg" alt="Hisense/海信 LED43T11N 43吋智能液晶电视机平板WIFI网络彩电42" class="B_blue" /></a></li><li><a href="goods.php-id=34.htm" tppabs="http://www.ktwwg.top/goods.php?id=34" target="_blank" title="Hisense/海信 LED43T11N 43吋智能液晶电视机平板WIFI网络彩电42">Hisense...</a><br />本店售价：<font class="f1">￥2199元</font><br /></li></ul><ul class="clearfix"><li class="goodsimg"><a href="goods.php-id=35.htm" tppabs="http://www.ktwwg.top/goods.php?id=35" target="_blank"><img src="images/201603/thumb_img/35_thumb_G_1457386209100.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/35_thumb_G_1457386209100.jpg" alt="Hisense/海信 LED32K30JD 32英寸液晶电视高清电视机网络彩电" class="B_blue" /></a></li><li><a href="goods.php-id=35.htm" tppabs="http://www.ktwwg.top/goods.php?id=35" target="_blank" title="Hisense/海信 LED32K30JD 32英寸液晶电视高清电视机网络彩电">Hisense...</a><br />本店售价：<font class="f1">￥1099元</font><br /></li></ul><ul id="clear_history"><a onclick="clear_history()">[清空]</a></ul>  </div>
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
</script>  </div>
  
  
  <div class="AreaR">
  
        <div class="box">
     <div class="box_1">
      <h3>
    
                 <span>搜索结果</span>
                             <form action="http://www.ktwwg.top/search.php" method="post" class="sort" name="listform" id="form">
          显示方式：
          <a href="javascript:;" onClick="javascript:display_mode('list')"><img src="themes/ecmoban_haier2015/images/display_mode_list.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/display_mode_list.gif" alt=""></a>
          <a href="javascript:;" onClick="javascript:display_mode('grid')"><img src="themes/ecmoban_haier2015/images/display_mode_grid_act.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/display_mode_grid_act.gif" alt=""></a>
          <a href="javascript:;" onClick="javascript:display_mode('text')"><img src="themes/ecmoban_haier2015/images/display_mode_text.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/display_mode_text.gif" alt=""></a>&nbsp;&nbsp;
               <select name="sort">
              <option value="goods_id" selected>按上架时间排序</option><option value="shop_price">按价格排序</option><option value="last_update">按更新时间排序</option>              </select>
              <select name="order">
              <option value="DESC" selected>倒序</option><option value="ASC">正序</option>              </select>
              <input type="image" name="imageField" src="themes/ecmoban_haier2015/images/bnt_go.gif" tppabs="http://www.ktwwg.top/themes/ecmoban_haier2015/images/bnt_go.gif" alt="go" style="vertical-align: -2px; *vertical-align:1px;"/>
              <input type="hidden" name="page" value="1" />
              <input type="hidden" name="display" value="grid" id="display" />
                                                              <input type="hidden" name="keywords" value="" />
                                                                                            <input       <form action="compare.php.htm" tppabs="http://www.ktwwg.top/compare.php" method="post" name="compareForm" id="compareForm" onsubmit="return compareGoods(this);">
                   
              <div class="clearfix goodsBox" style="border:none; ">
                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=40.htm" tppabs="http://www.ktwwg.top/goods.php?id=40"><img src="images/201603/thumb_img/40_thumb_G_1457386850600.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/40_thumb_G_1457386850600.jpg" alt="Haier/海..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=40.htm" tppabs="http://www.ktwwg.top/goods.php?id=40" title="">Haier/海...</a></p>
                                                                       本店价<font class="shop_s">￥3699元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(40)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(40);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=39.htm" tppabs="http://www.ktwwg.top/goods.php?id=39"><img src="images/201603/thumb_img/39_thumb_G_1457386763596.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/39_thumb_G_1457386763596.jpg" alt="Haier/海..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=39.htm" tppabs="http://www.ktwwg.top/goods.php?id=39" title="">Haier/海...</a></p>
                                                                       本店价<font class="shop_s">￥1399元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(39)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(39);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=38.htm" tppabs="http://www.ktwwg.top/goods.php?id=38"><img src="images/201603/thumb_img/38_thumb_G_1457386691571.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/38_thumb_G_1457386691571.jpg" alt="Haier/海..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=38.htm" tppabs="http://www.ktwwg.top/goods.php?id=38" title="">Haier/海...</a></p>
                                                                       本店价<font class="shop_s">￥949元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(38)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(38);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=37.htm" tppabs="http://www.ktwwg.top/goods.php?id=37"><img src="images/201603/thumb_img/37_thumb_G_1457386626746.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/37_thumb_G_1457386626746.jpg" alt="Haier/海..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=37.htm" tppabs="http://www.ktwwg.top/goods.php?id=37" title="">Haier/海...</a></p>
                                                                       本店价<font class="shop_s">￥1099元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(37)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(37);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=36.htm" tppabs="http://www.ktwwg.top/goods.php?id=36"><img src="images/201603/thumb_img/36_thumb_G_1457386449467.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/36_thumb_G_1457386449467.jpg" alt="Haier/海..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=36.htm" tppabs="http://www.ktwwg.top/goods.php?id=36" title="">Haier/海...</a></p>
                                                                       本店价<font class="shop_s">￥3499元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(36)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(36);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=35.htm" tppabs="http://www.ktwwg.top/goods.php?id=35"><img src="images/201603/thumb_img/35_thumb_G_1457386209100.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/35_thumb_G_1457386209100.jpg" alt="Hisense..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=35.htm" tppabs="http://www.ktwwg.top/goods.php?id=35" title="">Hisense...</a></p>
                                                                       本店价<font class="shop_s">￥1099元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(35)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(35);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=34.htm" tppabs="http://www.ktwwg.top/goods.php?id=34"><img src="images/201603/thumb_img/34_thumb_G_1457386156495.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/34_thumb_G_1457386156495.jpg" alt="Hisense..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=34.htm" tppabs="http://www.ktwwg.top/goods.php?id=34" title="">Hisense...</a></p>
                                                                       本店价<font class="shop_s">￥2199元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(34)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(34);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=33.htm" tppabs="http://www.ktwwg.top/goods.php?id=33"><img src="images/201603/thumb_img/33_thumb_G_1457386083317.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/33_thumb_G_1457386083317.jpg" alt="Hisense..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=33.htm" tppabs="http://www.ktwwg.top/goods.php?id=33" title="">Hisense...</a></p>
                                                                       本店价<font class="shop_s">￥4599元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(33)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(33);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=32.htm" tppabs="http://www.ktwwg.top/goods.php?id=32"><img src="images/201603/thumb_img/32_thumb_G_1457386008360.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/32_thumb_G_1457386008360.jpg" alt="Hisense..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=32.htm" tppabs="http://www.ktwwg.top/goods.php?id=32" title="">Hisense...</a></p>
                                                                       本店价<font class="shop_s">￥3999元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(32)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(32);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                                 <div class="goodsItem" style="padding: 10px 4px 15px 1px;">
                       <a href="goods.php-id=31.htm" tppabs="http://www.ktwwg.top/goods.php?id=31"><img src="images/201603/thumb_img/31_thumb_G_1457385860539.jpg" tppabs="http://www.ktwwg.top/images/201603/thumb_img/31_thumb_G_1457385860539.jpg" alt="Hisense..." class="goodsimg" /></a><br />
                       <p><a href="goods.php-id=31.htm" tppabs="http://www.ktwwg.top/goods.php?id=31" title="">Hisense...</a></p>
                                                                       本店价<font class="shop_s">￥3999元</font><br />
                        						
						<!-- 
                        <a href="javascript:addToCart(31)" ><img src="themes/ecmoban_haier2015/images/goumai.gif"></a> &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:collect(31);"><img src="themes/ecmoban_haier2015/images/shoucang.gif"></a> 
	-->
	
                    </div>
                                                </div>
               
                       </form>
          <script type="text/javascript">
                var button_compare = "比较选定商品";
                var exist = "您已经选择了%s";
                var count_limit = "最多只能选择4个商品进行对比";
                var goods_type_different = "\"%s\"和已选择商品类型不同无法进行对比";
        
				                var button_compare = '';
                                var exist = "您已经选择了%s";
                                var count_limit = "最多只能选择4个商品进行对比";
                                var goods_type_different = "\"%s\"和已选择商品类型不同无法进行对比";
                

        var compare_no_goods = "您没有选定任何需要比较的商品或者比较的商品数少于 2 个。";
        window.onload = function()
        {
          Compare.init();
          fixpng();
        }
	var btn_buy = "购买";
	var is_cancel = "取消";
	var select_spe = "请选择商品属性";
        </script>
                </div>
      </div>
      <div class="blank"></div>
      
<form name="selectPageForm" action="search.php-encode=YToxOntzOjE4OiJzZWFyY2hfZW5jb2RlX3RpbWUiO2k6MTUwMTczODA3NTt9.htm" tppabs="http://www.ktwwg.top/search.php" method="get">


 <div id="pager" class="pagebar">
  <span class="f_l " style="margin-right:10px;">总计 <b>20</b>  个记录</span>
                      <span class="page_now">1</span>
                      <a href="fi000001.php-encode=ytoxndp7czo4oijrzxl3b3jkcyi7czowoiiio3m6odoiy2f0zwdvcnkio3m6mtoimci7czo1oijicmfuzci7czoxoiiwijtzojq6innvcnqio3m6odoiz29vzhnfawqio3m6ntoib3jkzxiio3m6ndoirevtqyi7czo5oijtaw5fchjpy2uio3m6mtoimci7czo5oijtyxhfchjpy2uio3m6mtoimci7czo2oijhy3rpb24io3m6mdoiijtzoju6imludhjvijtzoja6iii7czoxmdoiz29vzhnfdhlwzsi7czoxoiiwijtzoju6innjx2rzijtzoje6ijaio3m6odoib3v0c3rvy2sio3m6mtoimci7czo0oijwywdlijtzoje6ijiio3m6mtg6innlyxjjaf9lbmnvzgvfdgltzsi7atoxntaxnzm4mdc4o30=.htm" tppabs="http://www.ktwwg.top/search.php?keywords=&category=0&brand=0&sort=goods_id&order=DESC&min_price=0&max_price=0&action=&intro=&goods_type=0&sc_ds=0&outstock=0&page=2">[2]</a>
            
  <a class="next" href="fi000001.php-encode=ytoxndp7czo4oijrzxl3b3jkcyi7czowoiiio3m6odoiy2f0zwdvcnkio3m6mtoimci7czo1oijicmfuzci7czoxoiiwijtzojq6innvcnqio3m6odoiz29vzhnfawqio3m6ntoib3jkzxiio3m6ndoirevtqyi7czo5oijtaw5fchjpy2uio3m6mtoimci7czo5oijtyxhfchjpy2uio3m6mtoimci7czo2oijhy3rpb24io3m6mdoiijtzoju6imludhjvijtzoja6iii7czoxmdoiz29vzhnfdhlwzsi7czoxoiiwijtzoju6innjx2rzijtzoje6ijaio3m6odoib3v0c3rvy2sio3m6mtoimci7czo0oijwywdlijtzoje6ijiio3m6mtg6innlyxjjaf9lbmnvzgvfdgltzsi7atoxntaxnzm4mdc4o30=.htm" tppabs="http://www.ktwwg.top/search.php?keywords=&category=0&brand=0&sort=goods_id&order=DESC&min_price=0&max_price=0&action=&intro=&goods_type=0&sc_ds=0&outstock=0&page=2">下一页</a>    </div>


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
