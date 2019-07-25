<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<footer>
     <ul class="footer-btn-group" id="footer-btn-group">
		<a href="/content/default/index.shtml"><li>公告</li></a>
		<a href="/content/about/index.shtml"><li>关于我们</li></a>
		<a href="/content/contact/index.shtml"><li>联系我们</li></a>
	 </ul>  
	 <ul class="footer-list">
		<a href="/"><li><img src="../../../theme/${theme}/image/mobile/footer/home.png"><p>首页</p></li></a>
		<a href="javascript:showList()"><li><img src="../../../theme/${theme}/image/mobile/footer/suyuan.png"><p>农青</p></li></a>
		<a href="/cart.shtml"><li><img src="../../../theme/${theme}/image/mobile/footer/cart1.png"><p>购物车</p><!--<span class="${empty cartCount?'':'cartIcon'}">${empty cartCount?'':cartCount}</span>--></li></a>
		<a href="/content/user/pcenter.shtml?favPage=1&favRows=8"><li><img src="../../../theme/${theme}/image/mobile/footer/my.png"><p>账户</p></li></a>
	 </ul>
</footer>  
<script>	
    var title = $('#header span').text();
	if(title.indexOf('个人中心')>-1 || title.indexOf('帮助中心')>-1 || title.indexOf('支付中心')>-1 || title.indexOf('我的评论')>-1 || title.indexOf('我的收藏')>-1 || title.indexOf('我的订单')>-1){
		$('.footer-list li').eq(0).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/homea.png');
		$('.footer-list li').eq(1).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/suyuan.png');
		$('.footer-list li').eq(2).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/cart1.png');
		$('.footer-list li').eq(3).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/mya.png');
	}else if(title.indexOf('公告')>-1 || title.indexOf('关于我们')>-1 || title.indexOf('联系我们')>-1){
		$('.footer-list li').eq(0).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/homea.png');
		$('.footer-list li').eq(1).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/suyuana.png');
		$('.footer-list li').eq(3).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/my.png');
		$('.footer-list li').eq(2).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/cart1.png');
	}else if(title.indexOf('购物车')>-1){
		$('.footer-list li').eq(0).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/homea.png');
		$('.footer-list li').eq(1).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/suyuan.png');
		$('.footer-list li').eq(3).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/my.png');
		$('.footer-list li').eq(2).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/cart.png');
	}else{
		$('.footer-list li').eq(0).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/home.png');
		$('.footer-list li').eq(1).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/suyuan.png');
		$('.footer-list li').eq(3).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/my.png');
		$('.footer-list li').eq(2).find('img').attr('src','../../../theme/${theme}/image/mobile/footer/cart1.png');
	}
</script>