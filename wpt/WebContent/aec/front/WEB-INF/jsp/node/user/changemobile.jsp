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
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../css/pcenter.css">
<link rel="stylesheet" type="text/css" href="../../css/changeInfo.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" src="../../js/jquery.validate.min.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>
<script src="../../js/zyFile.js"></script>
<script src="../../js/zyUpload_header.js"></script>
<script src="../../js/script.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script src="../../js/changeInfo.js"></script>
</head>
<body>
  <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50">
      <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox padbtm50">
	    <div><a href="#">账户中心</a>><a href="#">个人信息</a>><a href="#">修改个人信息</a></div>
		<div class="changeBox">
		   <article>
		      <p >我的头像:</p>
			  <p >
				<c:choose>
					<c:when test="${!empty frontUser.userConfigMap.get('userHeadPic').dataValue}">
						<c:if test="${fn:indexOf(frontUser.userConfigMap.get('userHeadPic').dataValue,'http://')!=-1}">
							<img src="${frontUser.userConfigMap.get('userHeadPic').dataValue}" style="margin-left: 105px">
						</c:if>
						<c:if test="${fn:indexOf(frontUser.userConfigMap.get('userHeadPic').dataValue,'http://')==-1}">
							<img src="/static/userUploadDir/${frontUser.userConfigMap.get('userHeadPic').dataValue}" style="margin-left: 105px; width:90px; height:90px; border-radius:90px;">
						</c:if>
					</c:when>
					<c:otherwise>
						<img src="../../image/header.png" style="margin-left: 105px">
					</c:otherwise>
				</c:choose>
			  </p>
			  <p><a href="#" class="orange box_container_right btnUpload">上传图片</a></p>
			  <div class="box_imgload" style="display:none;" id="fileDragArea">
			    
			  </div>
			   <p><span class="">我的昵称:</span><input type="text" class="myName" value="${frontUser.nickName}"></p>
			   <p><span class="">性别:</span><input type="radio"  name="sex" value="0"/>男&nbsp;&nbsp;<input type="radio" name="sex" value="1"/>女</p>
			   <p><span class="">我的密码:</span><input type="password"  readonly="true" class="myPw" value="******"><a href="#" class="orange fo-14 marleft10" id="changePwBtn">修改</a></p>
			      
			     <form class="box_changePw" style="display:none;">
			     <p><span class='box_container_left'>输入原密码:</span><input type='password' class='oldPw box_container_left' name="oldPw" id="oldPw"/></p>
				 <p><span class="box_container_left">输入新密码:</span><input type="password" class="oldPw box_container_left" name="newPw" id="newPw"/></p>
				 <p><span class="box_container_left">确认新密码:</span><input type="password" class="oldPw box_container_left" name="newPw1" id="newPw1"/></p>
				 <input type="submit" class="btnCon martop30" value="确认"/>
				 </form>
			   
			   <p><span class="">手机号码:</span><input type="text" readonly="true"  class="username" value="${frontUser.username}"><a href="#" class="orange fo-14 marleft10" id="changeMobileBtn">修改</a></p>
			   <form class="box_changeMobile" style="display:none;">
			     <p><span class="box_container_left">输入密码:</span><input type="password" class="oldPw box_container_left" name="userPw" id="userPw"/></p>
				 <p><span class="box_container_left">输入手机号:</span><input type="text" class="oldPw box_container_left" name="userPhone" id="userPhone"/></p>
				 <p><span class="box_container_left">确认手机号:</span><input type="text" class="oldPw box_container_left" name="userPhone1" id="userPhone1"/></p>
				 <input type="submit" class="btnCon martop30" value="确认"/>
			   </form>
			  <!-- <p><span class="">我的生日:</span>
			      <span>
				        <select id="sel_year"></select>年<select id="sel_month"></select>月<select id="sel_day"></select>日
				  </span>
				  <span>
						<select class="sel_year" rel="2000"></select>年<select class="sel_month" rel="2"></select>月<select class="sel_day" rel="14"></select>日
      	         </span>
        
	           </p>-->
	            <input type="submit" class="btnCon martop30" value="保存"/>
		   </article>
		</div>
	 </div>
   </div>
   <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>