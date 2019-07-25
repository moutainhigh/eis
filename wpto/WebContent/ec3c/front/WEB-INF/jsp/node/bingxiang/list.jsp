<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<title>产品列表页</title>
<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/common.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/global.js" ></script>
<!--<script type="text/javascript" src="/theme/${theme}/js/compare.js" ></script>-->
</head>
<body>
  
<%@include file="/WEB-INF/jsp/include/head.jsp" %> 
 
  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/" />首页</a> <code>&gt;</code> <a href="/">${pageTitle}</a> 
</div>
</div>
<div class="blank"></div>
<div class="block clearfix">
  
  <div class="AreaL">
    
  <div id="category_tree">
  <div class="tit">所有商品分类</div>
  <dl class="clearfix" style=" overflow:hidden;" >

<%@include file="/WEB-INF/jsp/include/productList.jsp" %> 
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
	      <ul class="clearfix">
      <c:forEach items="${browseHistoryList}" var="item" varStatus="status">
          <li class="goodsimg"><a href="${item.doc.viewUrl}" target="_blank"><img src="/file/${item.doc.documentDataMap.productSmallImage.dataValue}"  alt="" class="B_blue" /></a></li>
          <li><a href="${item.doc.viewUrl}" target="_blank" title="${item.doc.title}">${item.doc.title}</a><br />本店售价：<font class="f1">￥${item.buyMoney}元</font><br /></li></ul><ul class="clearfix">
      </c:forEach>
    </ul>
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
  <em class="stylelist" id="lists"><img src="/theme/${theme}/images/display_mode_lists.gif"  alt="" style="top: 4px;cursor: pointer;"></em>
  <em class="stylelist" id="Box"><img src="/theme/${theme}/images/display_mode_Box_act.gif" style="top: 4px;cursor: pointer;"></em>
  &nbsp;&nbsp;  
     <img src="/theme/${theme}/images/goods_id_default.gif" class="sort" datasrc="sortName=publish_time&sortType=asc&page=1" alt="按上架时间排序" dange="publish_time" names="goods_id" sortname="${sortName}" sorttype="${sortType}" datazhi="asc">
     
     <img src="/theme/${theme}/images/last_update_default.gif" class="sort" datasrc="sortName=last_modified&sortType=asc&page=1" lt="按更新时间排序" dange="last_modified" names="last_update" sortname="${sortName}" sorttype="${sortType}" datazhi="asc">
  <!-- <a href="publishTime=desc&price=desc&lastModified=ase"><img src="/theme/${theme}/images/shop_price_default.gif"  alt="按价格排序"></a> -->
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
      <!--<form name="compareForm" action="compare.php.htm" method="post" onSubmit="return compareGoods(this);">-->
      	<div class="clearfix goodsBox productlist">
      	<c:forEach items="${newsList}" var="a">
             <div class="goodsItem" style="padding: 10px 3px 15px 2px;" >
           		<a href="${a.viewUrl}"><img src="/file/${a.documentDataMap.productSmallImage.dataValue}" alt="" class="goodsimg" /></a><br />

           		<p><a href="${a.viewUrl}"  title="${a.title}">${a.title}</a></p>市场价：<font class="market_s">￥${a.documentDataMap.productMarketPrice.dataValue}元</font><br />本店价：<font class="shop_s">￥<em class="moneynumber" style="font-style: normal;">${a.documentDataMap.productBuyMoney.dataValue}</em>元</font><br />
        	 </div>
      	</c:forEach>
      	</div>
      	<div class="goodsList productlist goodslists" style="display:none;">
      	<c:forEach items="${newsList}" var="b">
	          <ul class="clearfix bgcolor">
			    <li class="thumb"><a href="${b.viewUrl}"><img src="/file/${b.documentDataMap.productSmallImage.dataValue}"></a></li>
			    <li class="goodsName">
			    <div class="div_name">
			    <a href="${b.viewUrl}" class="f6">
			                ${b.title}<br>
			              </a>
			          </div>
			     <div>
			     <img class="addcart" src="/theme/${theme}/images/goumai.gif" data-productCode ="${b.documentDataMap.productCode.dataValue}" >
			    <a href="javascript:collect(30);"><img src="/theme/${theme}/images/shoucang.gif"></a> 
			    
			    </div>
			    </li>
		    	<li class="action">
		          市场价格：<font class="market" style="padding-right:10px;">￥${b.documentDataMap.productMarketPrice.dataValue}元</font> <br>
		            本店售价：<font class="shop">￥<em class="moneynumber" style="font-style: normal;">${b.documentDataMap.productBuyMoney.dataValue}</em>元</font> 
		        </li>
    		</ul>
      	</c:forEach>
</div>
            
        <!--</form>-->
        <!-- 页码 -->
        <%@include file="/WEB-INF/jsp/include/page.jsp"%>
  
 </div>
</div>
<div class="blank5"></div>
<script type="text/javascript">
// 本店价格
$('.moneynumber').each(function () {
	var neirongzhi = $(this).text();
	var arry = {};
	arry = neirongzhi.split(';'); 
	if (arry.length>0) {
		var num = arry[0].replace(/[^\d.]/g,"");
	};
	$(this).text(num);
})
// 排列方式选择
	$('.stylelist').on('click',function (){
		var styles = $(this).attr('id');
		var slistyle = $(this).siblings('.stylelist').attr('id');
		$(this).siblings('.stylelist').find('img').attr('src','/theme/basic/images/display_mode_'+slistyle+'.gif');
		$('.productlist').css('display','none')
		$('.goods'+styles).css('display','block');
		$(this).find('img').attr('src','/theme/basic/images/display_mode_'+styles+'_act.gif');


	})
</script>
<script type="Text/Javascript" language="JavaScript">
<!--
function selectPage(sel)
{
  sel.form.submit();
}
//-->
</script>
<script type="text/javascript">
// window.onload = function()
// {
//   Compare.init();
//   fixpng();
// }
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
<!--  <div id="pager" class="pagebar">
  <span class="f_l " style="margin-right:10px;">总计 <b>10</b>  个记录</span>
      
      </div> -->
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
    <script type="text/javascript">
    $('img.addcart').on('click',function(){

    var documentCode = $(this).attr('data-productCode');
  $.ajax({
    type : "POST",  
         url : "/buy/add.json",  
         dataType:"json",
         data:{
          productCode:documentCode,
          count:1,
          directBuy:false
         }, 
         success : function(data) {  
          alert(data.message.message)
         },  
         error : function() {  
          alert(data.message.message)
         }  
        });
})

// 排序
$('img.sort').each(function(){
	var sortname = $(this).attr('sortname');
	var sorttype = $(this).attr('sorttype');
	var dangsrc = $(this).attr('dange');
	var namesort = $(this).attr('names');
	if (sortname == dangsrc) {
		$(this).attr('src','/theme/basic/images/'+namesort+'_'+sorttype.toUpperCase()+'.gif');
		
	}else{
		$(this).attr('src','/theme/basic/images/'+namesort+'_default.gif');
	}
})

$('img.sort').on('click',function(){
	var sortname = $(this).attr('sortname');
	var sorttype = $(this).attr('sorttype');
	var namesort = $(this).attr('names');
	var dangsrc = $(this).attr('dange');
	if (sortname == dangsrc) {
		if (sorttype=='asc') {
			$(this).attr('datazhi','desc');
			$(this).attr('src','/theme/basic/images/'+namesort+'_DESC.gif');
			var htmlshtml = 'sortName='+dangsrc+'&sortType=desc&page=1';
		}else{
			$(this).attr('datazhi','asc');
			$(this).attr('src','/theme/basic/images/'+namesort+'_ASC.gif');
			var htmlshtml = 'sortName='+dangsrc+'&sortType=asc&page=1';
		}
	}else{
		$(this).attr('src','/theme/basic/images/'+namesort+'_ASC.gif');
		var htmlshtml = 'sortName='+dangsrc+'&sortType=asc&page=1';
	}
	//$(this).siblings().attr('src','/theme/basic/images/'+sibnamesort+'_default.gif')
	var url = document.location.href;
	if(url.indexOf('?') == -1){
		//没有任何查询字符串
		url = url + '?' + htmlshtml;
	} else {
		//已经有页码
		if(url.indexOf('sortName') >= 0){
			url = url.replace(/sortName=[a-zA-Z_]+/, 'sortName='+dangsrc);
			url = url.replace(/sortType=[a-zA-Z_]+/, 'sortType='+$(this).attr('datazhi'));
		} else {
			url = url.replace(/page=[0-9]+/, htmlshtml);
		}  
	}
	document.location.href=url;
})


    </script>
</body>
</html>
