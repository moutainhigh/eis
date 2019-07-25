<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<c:choose>
	<c:when test="${frontUser == null}">
	<div class="login fl">
            	<div class="login_tit"><span class="leftbar_item_tit" name="ajaxInfo" id="ajaxInfo"></span></div>
            	<div class="login_body">
             		<form name="login" name="loginForm" id="loginForm" action="/passport.do?action=login" method="POST" onClick="return false;">
            		<div class="login_input"> 
            			<span class="s1"><input id="username" name="username" value="${eis_username}" onclick="this.value=''" type="text"></span>
            			<span class="s1"><input id="userPassword" name="userPassword" type="password"></span>  
            		</div> 
            		<div class="login_submit">
            			<input id="loginButton" name="loginButton"  type="submit" value="">
            		</div> 
            		<div class="clr"></div> 
            		<div class="login_options">
            			<input id="remember_username" name="remember_username" value="1" checked="checked" type="checkbox">记住我的账号&nbsp;| <a target="_blank" class="login_forget" href="#">忘记密码？</a> 
            		</div> 
            		<input name="refer" id="refer" value="" type="hidden">
            		</form>
            		<a href="/register.do" target="_blank" class="login_reg">注册账号</a>
            	</div>
         </div>
         <div class="clr"></div>         
            
            
	
	</c:when>
	<c:otherwise>
	
	<div class="login fl">
            	<div class="login_tit"><span class="leftbar_item_tit_logined" name="ajaxInfo" id="ajaxInfo"></span></div>
            	<div class="login_body">
			<p style="height:30px; line-height:30px; background-color:#eeeeee; ">您好，<a href="/i.do" target="_blank">
				<c:choose>
						<c:when test="${frontUser.nickname ne null}">${fn:substring(frontUser.nickname ,0,10)}${fn:length(frontUser.nickname)>10?"...":""}</c:when>
						<c:otherwise>${fn:substring(frontUser.username ,0,10)}${fn:length(frontUser.username)>10?"...":""}</c:otherwise>
				</c:choose></a>
			</p>
			<p style="text-indent:24px; ">上次于<fmt:formatDate value="${frontUser.lastLoginTimestamp}" pattern="MM月dd日 HH:mm:ss"/>从IP ${frontUser.lastLoginIp} 登录。</p>
				<!-- <p>
					最近玩过的游戏：
					<ul>
					<c:forEach var="business" items="${recentBusinessList}" end="5">
					<li class="biz">
						<a href="/business.do?business=${business.businessId}" target="_blank">
						${business.businessName}
						
						</a>
					</li> 
					</c:forEach>
					</ul>
				</p>-->
			<p style="text-align: center;">
				<span><a href="/i.do" target="_blank">[我的${systemName}]</a><span>
				<span><a href="javascript:logout();">[安全退出]</a><span>
			</p>
            	</div>
         </div>
         <div class="clr"></div>      
		
	
	</c:otherwise>
</c:choose>