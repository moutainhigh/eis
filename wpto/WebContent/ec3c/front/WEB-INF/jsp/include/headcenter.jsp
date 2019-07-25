<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
       
<div class="Top">
    <div class="Top_Left">
        <a href="" class="Logo"></a>
    </div>
    <div class="Top_Right">
        <div id="juncHeader1_plNoLogin">
	
            
            
            <a href="/content/user/login.shtml" target="_blank" class="zhanghu_res">登录</a>
            <a href="/content/user/register.shtml" target="_blank" class="username_res">注册</a>
        
		</div>
        
    </div>
</div>
<script src="/theme/ec1/js/jquery-1.3.2.min.js"></script>
<script >
	function getRootPath(){
	var CurWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = CurWwwPath.indexOf(pathName);
	var localhostPath = CurWwwPath.substring(0,pos);
	return(localhostPath);
}
	function getCookie(name){

			var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
         
            if(arr != null) return decodeURIComponent(arr[2]).replace(/^\"|\"$/g,''); return null;
    }
		$(function(){
			var eis_username=decodeURI(getCookie("eis_username"));
			//console.log(decodeURI(eis_username)!="null");
			if(eis_username!=null&&eis_username!="null"){
				$(".username_res").html('<a href="/content/user/pcenter.shtml" style="text-decoration:underline;color:#B1191A;">'+eis_username+"，您好！</a>");
				$(".zhanghu_res").html("<a href='#' onClick='logout();'>退出</a>");
			}
		});
		
	function logout(){
		$.ajax({
                	type:"get",
                	url:"/user/logout.json",
                	async:true,
                	success:function(data){
             
							alert('退出登录成功');
							document.location.href = getRootPath();               		
                	
                	
                	}
                })
	};
	
</script>



