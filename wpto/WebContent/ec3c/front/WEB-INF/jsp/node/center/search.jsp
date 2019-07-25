<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<title>搜索商品</title>
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
当前位置: <a href="/" >首页</a> <code>&gt;</code> <a href="" >${pageTitle}</a> 
</div>
</div>
<div class="blank"></div>
<div class="block clearfix">
  
  <div class="AreaL">
    
  <div id="category_tree">
  <div class="tit">所有商品分类</div>
  <dl class="clearfix" style=" overflow:hidden;" >
   <div class="box1 cate" id="cate">
		<h1 onclick="tab(0)"  style="border-top:none">
		<span class="f_l"><img src="../../../../theme/${theme}/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="/content/kongtiao/index.shtml"  class="  f_l">空调</a>
		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
					<h1 onclick="tab(1)" >
		<span class="f_l"><img src="/theme/${theme}/images/btn_fold.gif"  style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="/content/bingxiang/index.shtml"  class="  f_l">冰箱</a>
		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
					<h1 onclick="tab(2)" >
		<span class="f_l"><img src="/theme/${theme}/images/btn_fold.gif"  style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="/content/dianshi/index.shtml"  class="  f_l">电视</a>
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
      <h3><span>搜索结果</span></h3>
        <div style="padding:20px 0px; text-align:center" class="f5" >无法搜索到您要找的商品！</div>
        </div>
      </div>

    <div id="pager" class="pagebar">
        <span class="f_l " style="margin-right:10px;">总计 <b>0</b>  个记录</span>
      </div>

	       
        <!--</form>-->
        <!-- 页码 -->
        <%@include file="/WEB-INF/jsp/include/page.jsp"%>
  
 </div>
</div>
<div class="blank5"></div>
<script>
    //   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     }  
 })
</script>

  </div>  
  
</div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

</body>
</html>
