<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE>${systemName}</TITLE>
<meta http-equiv="x-ua-compatible" content="ie=7">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name=description 
content=炒卡网主营游戏充值,话费充值,影音娱乐,杀毒软件,软件序列号，在线教育，影票兑换码等产品。，高效快捷，实时充值,安全放心。7*24小时为您提供全面的支付服务。>
<META name=keywords 
content=炒卡网,jcard,炒卡网充值,炒卡网官网,充值,官网,qq卡,q币充值中心,qq充值中心,qq蓝钻,蓝钻豪华版,q币查询,点卡充值,游戏点卡,魔兽世界点卡,魔兽世界,大话西游3,剑网三,九阴真经,神仙传,神仙道,人人豆,>
<LINK rel="Shortcut Icon" href="/favicon.ico">
<LINK rel=Bookmark href="/favicon.ico">

<LINK rel=Stylesheet type=text/css href="/style/UCardTop.css">
<LINK rel=Stylesheet type=text/css href="/style/CarouselAD.css">
<LINK rel=stylesheet type=text/css href="/style/OldUcard.css">
<LINK rel=stylesheet type=text/css href="/style/Web.css">
<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<LINK rel=stylesheet type=text/css href="/style/list.css">
<LINK rel=stylesheet type=text/css href="/style/Footer.css">
<title>用户注册 | ${systemName }</title>
</head>
<script  src="/js/lib/constants.js"></script>
<script  src="/js/ui/jquery-1.7.2.min.js"></script>
<script src="/style/loading/jquery.bgiframe.min.js" type="text/javascript"></script>
<script  src="/style/loading/loading-min.js"></script>
<link href="/style/loading/loading.css" type="text/css" rel="stylesheet">
<script  src="/js/ui/commonui.js"></script>
<script  src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>

<script  src="/js/ui/reg.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js" ></script>


<body>

<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
<div id="mainbody">
    	    <div class="leftbar">
	    	  
                <%@include file="/WEB-INF/jsp/include/common_reinfo.jsp"%>
	  
           
			
        </div>		
        <div class="rightbar">
        
            	
                <div class="position"><span>当前位置: <a href="/" target="_blank">炒卡网首页</a> - 注册帐号</span></div>
                    
               
                <div class="registerbody">
                		<!--<div class="welcomes">欢迎注册${systemName}，请在下面输入您的信息</div>-->
                         <div class="clear"></div>
             <ul class="tab">
    <li class="curr"><a href="">实名注册</a></li>
   
</ul><div class="clear"></div>
				<div class="registerf">
				
                    <span class="regnotifyOk"  style=" font-size:12px;text-align:left;padding-left:50px;margin:0 auto; display:none   ">正在处理中</span>
					<form name="registerForm" id="registerForm" >
				
					<p >
                        <span class="txt">用户名[账号]：</span>
                        <span class="forms"><input type="text"  name="username" id="username"  size="40"/></span>
                        <span class="regok"></span>
                    </p>
					<p>
                        <span class="txt">您的密码：</span>
                        <span class="forms"><input type="password" name="userPassword" id="userPassword"  size="40"/></span>
                        <span class="regok"></span>
                    </p>
					<p><span class="txt">确认密码：</span>
                        <span class="forms"><input type="password" name="oncePassword" id="oncePassword"  size="40"/></span>
                        <span class="regok"></span>
                    </p>
                     <div style="width:80%; margin:0 auto;border-bottom: #bfbfbf 1px dashed ; margin-top:20px; height:1px"></div>
                    <p>
                        <span class="txt">真实姓名：</span>
                        <span class="forms"><input type="text" name="truename" id="truename"  size="40"/></span>
                        <span class="regok"></span>
                    </p>
                    <p>
                        <span class="txt">身份证号：</span>
                        <span class="forms"><input type="text" name="idnumber" id="idnumber"  size="40"/></span>
                        <span class="regok"></span>
                    </p>
					<p class="agree"><input type="checkbox" id="license" checked/>我接受<a href="/about/188.html" target="_blank">${systemName}通行证使用的协议和隐私政策</a></p>
					
					<p style="text-align:center;margin:0 auto; width:80px;"><input  type="button" id="submit" value="提交注册" /></p>
					</form>
				</div>  	
					
				
          
            
          
            
               </div>
    <div class="clear"></div>
    </div>
        <div class="clear"></div>
         </div>
        
 <%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
 <script>
    $('#submit').on('click',function () {
        var username = $('#username').val()
        var userPassword = $('#userPassword').val()
        var oncePassword = $('#oncePassword').val()
        var regPas = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,8}$/
        var truename = $('#truename').val()
        var regNam = /^[\u4e00-\u9fa5]{2,7}$/
        var idnumber = $('#idnumber').val()
        var regId = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
       
            if (username == '') {
                alert('用户名不能为空！')
                $('#username').focus()
            }else if(userPassword=='') {
                alert('您的密码不能为空！')
                $('#userPassword').focus()
            }else if(!regPas.test(userPassword)) {
                alert('请输入6-8位由数字和英文字母组成的密码')
                $('#userPassword').focus()
            }else if(oncePassword=='') {
                alert('确认密码不能为空！')
                $('#oncePassword').focus()
            }else if(oncePassword !== userPassword) {
                alert('确认密码与密码不一致！')
                $('#oncePassword').focus()
            }else if(truename=='') {
                alert('真实姓名不能为空！')
                $('#truename').focus()
            }else if(!regNam.test(truename)) {
                alert('请输入您的真实姓名！')
                $('#truename').focus()
            }else if(idnumber=='') {
                alert('身份证号码不能为空！')
                $('#idnumber').focus()
            }else if(!regId.test(idnumber)) {
                alert('请输入正确的身份证号码！')
                $('#idnumber').focus()
            }else if($('#license').is(':checked')) {
                $.ajax( {  
                     type : "POST",  
                     url : "/user.json",  
                     dataType:"json",
                     data:$('#registerForm').serialize(), 
                     success : function(data) {  
                        alert(data.message.message);
                        if (data.message.message == '登录成功') {
                            window.location.href = 'http://www.chaoka.cn';
                        }
                        
                     },
                     error : function(data) {  
                        alert(data.message.message);
                     }  
                });  
            }else{
            alert('您还没有勾选接受炒卡网通行证使用的协议和隐私政策')
            $('#license').focus()
       }
    })
 </script>
</body></html>