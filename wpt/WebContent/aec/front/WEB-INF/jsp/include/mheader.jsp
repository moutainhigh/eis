<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<link rel="stylesheet" href="../../../theme/${theme}/css/iconfont.css">
<style>
	.iconfont{
		font-size: 22px;
	}
</style>
	<div class="header color_bg" id="header" style="text-align:left">
	  <div class="inputBox">
	    <span class="sprite-icon"></span>
			<form target="frameFile" action="." id="headerInput">
	    		<input type="search" placeholder="搜索你喜欢的···" />
					<iframe name='frameFile' style="display: none;"></iframe>
			</form>
	  </div>
	  <a class="hanbao" id="hanbao">
		<i class="icon iconfont icon-kai"></i>
	  </a>
	  <div class="erweimaCode" id="erweimaCode">
	     <img src="/image/QRcode.png">
		 <p><a href="/content/about/20160709160756.shtml">关于我们</a></p>
	  </div>
	</div>
	<script>
			$('#headerInput').on('submit',function(e){
					var title = $(this).children().val();
					window.location.href = '/content/default/20170726115149.shtml?search=search&title='+title;
					document.activeElement.blur();//软键盘收起
			})
	</script>