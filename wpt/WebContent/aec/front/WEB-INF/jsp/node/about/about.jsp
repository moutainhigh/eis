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
<link rel="stylesheet" type="text/css" href="../../css/index.css">
<link rel="stylesheet" type="text/css" href="../../css/aboutus.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/common.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
	function suggestSubmit(){
		 if(Cookie.getCookie("eis_username")==null){
        alert("您还没有登录");
        location.href = "/content/user/login.shtml";

    }else{
		if($("#suggestContent").val().length==0){
			return false;
		}
		$.ajax({
            type:"POST",
            url: '/comment/submit.json',
            data:{
				objectType:'document',
				objectId:$("#udid").val(),
				content:$("#suggestContent").val()
			},
            dataType:'json',
            success:function (data) {
               alert("提交成功！");
               window.location.reload(); 
            },
           error:function (data) {
                alert("系统繁忙,请稍后再试");
                return false;
            }
			
        });	
	}
}
</script>

</head>
<body>
   <div class="wid-100" id="wid-100">
    <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="box_container">
	    <h3 class="martop30 orange spacing"><span class="orange">以先</span>介绍</h3>
		<div class="box_intro martop30">
		   ${document.content}
		</div>
	
	<div class="box_container marbottom30">
	   <h3 class="martop30"><span class="orange spacing">联系我们</span></h3>
		<div class="wid-80 box_contactus martop30">
			<div class="box_container_left">
			<form>
				 <input type="hidden"  value="${document.udid}" id="udid"/> 
			   <p><textarea placeholder="有什么想对我们说的话或者建议么？可以在这里直接提交哦！（不能超过400字）" maxlength=400 id="suggestContent"></textarea></p>			  
			  <p><input type="button" value="提交" onclick="suggestSubmit()"/></p>
			   </form> 
			</div>
		    <div class="box_container_right">
				<p class="martop20">联系地址：北京市朝阳区八里庄北里129号院</p>
				<p class="textIn20">保利东郡c座11层</p>
				<p>企业邮箱：yixian@yixian365.com</p>
				<p>传真地址：010-84896048转618</p>
				<p>客服热线：400-000-7713</p>
		    </div>
		</div>
		
	</div>	
	</div>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>