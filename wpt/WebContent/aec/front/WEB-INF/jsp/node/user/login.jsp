<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../css/newCss/main.css">
<link rel="stylesheet" type="text/css" href="../../css/login.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" src="../../js/jquery.form.js"></script>
<script  type="text/javascript" src="../../js/jquery.validate.min.js"></script>

<!--<script  type="text/javascript" src="../../js/jquery-1.11.3.js"></script> -->
<script  type="text/javascript" src="../../js/register.js"></script>
<script  type="text/javascript" src="../../js/login.js"></script>
<script type="text/javascript" src="../../js/forgetpassword.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>
</head>
<body>
   <div class="wid-100">
  <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<nav>
	   <ul class="list_img">
	     <li><img src="../../image/banner/img1.jpg"></li>
		 <li><img src="../../image/banner/img2.jpg"></li>
		 <li><img src="../../image/banner/img3.jpg"></li>
		
	   </ul>
	  </nav>
	   <div class="box_logins" id="divLogin1">
	   <div class="login_font">登录</div>
	       <a href="#" class="btnRegister" id="btnRegister">
	       <span class="box_container_left">立即注册</span ><span class="arrow_right">〉</span>
	        </a> 
			<form id="loginForm" onsubmit="return false;" method="POST"  name="loginForm" >
			 <div class="line_box martop50">
		        <div class="input_border">
			      <input type="text" placeholder="请输入手机号" maxlength=11 id="username" name="username" />
				 
				</div>
		    </div>
			<div class="line_box martop50">
		       <div class="input_border">
			      <input type="password" placeholder="请输入密码" maxlength=16 id="userPassword" name="userPassword"/>
				  </div>
		    </div>
			   
		 <a href="#" class="orange fo-13 box_container_right martop30" id="btnForPsw">忘记密码</a>
			   <input type="hidden" id="loginurl" value="${return_Url}" name="login"> 
			    <div class="line_box">
		    <input type="submit" value="登录" class="btn_login">
		  </div>
			</form>
	   </div>



	   <div class="box_logins"  id="divLogin2">
	   
			<form  method="POST"  name="member_regForm" id="member_regForm" onsubmit="return false;" >
				<div class="login_font">注册</div>
	        <a href="#" class="btnRegister" id="btnLogin1"><span class="box_container_left">登录</span><span class="arrow_right">〉</span></a>
             <div class="content">
                 <div class="panel_login">	 
	             <div class="bg_white">
	          
		 
		   <div class="line_box">
		     <div class="input_border">
			  <input type="text" class="username" id="username" name="username" maxlength=11  placeholder="请输入您的手机号"/>
			 </div>
		  </div>
		  <div class="line_box">
		  <div class="input_border">
			  <input type="password" class="passwd" id="userPassword" name="userPassword" maxlength=16  placeholder="请输入密码"/>
			  </div>
		  </div>
		  	     <div class="line_box">
		  <div class="input_border_a">
		     
			  <input type="text" placeholder="图形验证码" class="passwd" id="eis_captcha" maxlength=4  name="eis_captcha"/>
			 
			  </div>
			  <div class="input_border_b">
			   <img src="/captcha"  id="captcha" onclick="onchangePatchca(this);"/>
			   </div>
		  </div>
		     <div class="line_box">
		  		<div class="input_border_a">
			       <input type="text" placeholder="请输入验证码" id="smsRegisterSign1" name="smsRegisterSign1" maxlength=6  class="passwd" />
			    </div>
			    <div class="input_border_b">
			       <input type="button" class="btn_sendCode" id="btnSendCode" value="发送验证码" onclick="sendMessage();"/>
			   </div>
		     </div>
		
		  <div class="mbie">    
                <div class="checkBox">
  		            <input type="checkbox" value="1" id="checkboxgroup" name=""/>
	  	            <label for="checkboxgroup"></label>
  	             </div>                      
                <a class="mb10" href="/content/about/20170122142130.shtml">同意以先用户注册协议</a>
               
            </div>
			  <div class="line_box">
		       <input type="submit" class="btn_login" value="注册"/>
		      </div>
		</div>
	 </div>
   </div>
   </form>

        <form  method="POST"  name="member_forgetForm" id="member_forgetForm"   onsubmit="return false;" style="display:none;" >
			<div class="login_font">找回密码</div>
	        <a href="#" class="btnRegister" id="btnLogin2"><span class="box_container_left">登录</span><span class="arrow_right">〉</span></a>
            <div class="content">
              <div class="panel_login">
	          <div class="bg_white">
		      <div class="line_box">
		     <div class="input_border">
			  <input type="text" class="username" id="username" name="username" maxlength=11 placeholder="请输入您的手机号"/>
			  </div>
		  </div>
		     <div class="line_box">
		  <div class="input_border_a">
			  <input type="text" placeholder="请输入验证码" id="smsRegisterSign" name="smsRegisterSign" class="passwd" maxlength=6 AutoComplete="off"/>
			  </div>
			  <div class="input_border_b">
			   <input type="button" class="btn_sendCode" id="btnSendCode" value="发送验证码"  onclick="sendMessage3();"/>
			   </div>
		  </div>
		   <div class="line_box">
		  <div class="input_border">		     
			  <input type="password" class="passwd" id="userPassword1" name="userPassword1"   placeholder="设置新密码" autocomplete="off"/>
			  </div>
		  </div>
		  <div class="line_box">
		  <div class="input_border">
			  <input type="password" class="passwd" id="userPassword2" name="userPassword2"  placeholder="确认新密码"   autocomplete="off"/>
			  </div>
		  </div>
		  <div class="line_box martop50 ">
		     <input type="submit" class="btn_login"  value="提交"/>
		  </div>
		 
		</div>
	 </div>
   </div>
   </form>

	   </div>
	</nav>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</div>
</body>
</html>