<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${systemName}-注册商家账户</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
    <link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="/theme/${theme}/js/sweetalert.min.js"></script>
    <link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css" />
    <link href="/theme/${theme}/style/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
<div class="container-fluid">
    <div class="row">
        <div class="fullScreenBg" style="display:none;opacity:1; background: rgba(0, 0, 0, 0.63);">

            <div class="message" style="display:none;">
                <span class="text"></span>
                <p style="text-align:center;margin-top: 57px;">
                    <a href="<c:url value='/merchant.shtml'/>"  class="completebtn" style=" font-size: 15px;padding: 10px 14px; margin-right: 12px;">返回商户列表</a>
                    <span class="querenbtn" style=" cursor: pointer;font-size: 15px;padding: 10px 14px;">继续添加商户</span></p>
            </div>
        </div>
        <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header"><span>商户注册</span></h2>
            <div class="table-responsive">
                <form:form id="partnerForm"  commandName="partner" name="partnerForm" enctype="multipart/form-data" action="" method="post">
                    <table class="table table-striped" style="">
                        <thead style="display:none;">
                        <tr>
                            <th style="width:20%;text-align:right;"></th>
                            <th style="width:80%;text-align:left;"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>

                            <td style="text-align:right; width:25%;"><span>登录名</span>
                            </td>
                            <td style="text-align:left;">
                                <form:input path="username"/><span style="color:red;font-size:12px;padding:0;margin-left:6px;">*只允许英文字母、数字和下划线</span>
                                <form:errors path="username" cssClass="errorMessage"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="">用户昵称</td>
                            <td style="text-align:left;">
                                <form:input path="nickName"/><span style="color:red;font-size:12px;padding:0;margin-left:6px;">*支持中文</span>
                                <form:errors path="nickName" cssClass="errorMessage"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="">用户密码</td>
                            <td style="text-align:left;">
                                <form:password path="userPassword"/>
                                <input type="password"  name="userPassword2" id="userPassword2" placeholder="确认密码"style="margin-left:20px;"/>
                                <form:errors path="userPassword" cssClass="errorMessage"/>
                            </td>
                        </tr>
                        <tr>
                            <td>二级密码</td>
                            <td style="text-align:left;">
                                <form:password path="authKey"/>
                                <input type="password"  name="authKey2" id="authKey2" placeholder="确认密码"style="margin-left:20px;"/>
                            </td>
                        </tr>

                        <tr>
                            <td>关联角色</td>
                            <td style="text-align:left;">
                                <c:forEach var="r" items="${partnerRoleList}">
                                    <span class="jiaose"><input type="checkbox" name="roleId" value="${r.roleId }">&nbsp;${r.roleId}：<span style="padding-left:24px;display:inline-block;background-size: contain;background-repeat: no-repeat;">${r.roleName}</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:forEach>
                            </td>
                        </tr>

                        <tr  style="border-bottom:2px solid #BDBDBD">
                            <td style="">用户状态</td>
                            <td style="text-align:left;">
                                <form:select path="currentStatus">
                                    <c:forEach items="${statusCodeList}" var="i">
                                        <option value="${i.id}" <c:if test="${i.id == 120001}"> selected </c:if> ><spring:message code="Status.${i.id}" /></option>
                                    </c:forEach>
                                </form:select>
                                <span style="font-size:13px;color:red;margin-left:6px;">*如果不确定请选择 “ 正常 ”</span>
                            </td>
                        </tr>

                        <c:forEach items="${dataDefine}" var="d">
                            <tr class="dataDefine">
                                <td>${d.dataDescription}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${d.inputMethod == 'string'}">
                                            <input class="${d.dataCode}" type="text" name="${d.dataCode}" value="">
                                        </c:when>
                                        <c:when test="${d.inputMethod == 'input'}">
                                            <c:if test="${d.dataCode == 'userInviteCode'}">
                                                <input id="userInviteCode" type="text" name="${d.dataCode}" onblur="upperCase()" value="">&nbsp;&nbsp;
                                                <input id="butt" style="display:none" type="button" value="推广二维码">
                                            </c:if>
                                            <c:if test="${d.dataCode != 'userInviteCode'}">
                                                <input class="${d.dataCode}" type="text" name="${d.dataCode}" value="">
                                            </c:if>
                                        </c:when>

                                        <c:when test="${d.inputMethod == 'select'}">
                                            <input type="radio" name="${d.dataCode}" value="true">是
                                            <input type="radio" name="${d.dataCode}" value="false">否
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <!-- 结束 -->
                        </tbody>
                    </table>

                    <div class="submit"><input type="submit" value="录入商户信息" class="submitinput"></div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="/theme/basic/js/ie10-viewport-bug-workaround.js"></script>
<script src="/theme/basic/js/lib/jquery.cookie.js" type="text/javascript"></script>
<script src="/theme/basic/js/jquery.treeview.js" type="text/javascript"></script>
<script src="/theme/basic/js/jquery.form.3.5.1.js" type="text/javascript"></script>
<script>

    $(function(){
        var length=$("tbody tr").length;
        for(var i=0;i<length;i++){
            $("tbody tr").eq(i).find("td").eq(0).css({"text-align":"right",'background-color': '#E4E9F0'});
            $("tbody tr").eq(i).find("td").eq(0).find('input').css({"text-align":"right",'width':'100%','min-width':'400px'});
        };
        $('td span.jiaose').each(function(){
            var imgsrc = '/theme/basic/images/people.png';
            $(this).find('span').css('background-image','url('+imgsrc+')');
            console.log(imgsrc);
        });
        $('.kzinputtd').each(function(){
            console.log($(this).find("input[type='text']").attr('data-required'));
            if($(this).find("input[type='text']").attr('data-readonly') == 'false'){
                $(this).find("input[type='text']").removeAttr('readonly');
            }else{
                $(this).find("input[type='text']").attr('readonly','readonly');
            }
            if($(this).find("input[type='text']").attr('data-required') == 'true'){
                $(this).find("input[type='text']").addClass('required');

            }else{
                $(this).find("input[type='text']").removeClass('required');

            }
        })
        $('.tdradio').each(function(){
            if($(this).attr('data-readonly')=='false'){
                $(this).addClass('required');
            }
        })
    });

</script>


<script>

    $(function(){
        $(".submitinput").on('click',function(e){
            e.preventDefault();
            var reg=/^[\w]*$/;
            var reg2 =/^[a-zA-Z\u4e00-\u9fa5]{1}[a-zA-Z0-9\u4e00-\u9fa5]{3,9}$/;
            var username=$("#username").val();
            var nickName=$("#nickName").val();
            var userPassword=$("#userPassword").val();
            var userPassword2=$("#userPassword2").val();
            var authKey = $("#authKey").val();
            var authKey2 = $("#authKey2").val();
            console.log(userPassword);
            console.log(userPassword2);

            $('.jiaose input').each(function(){
                if($(this).is(":checked")){
                    $(this).addClass('checkedbox');
                }

            });
            var checkedboxL = $('input.checkedbox').length;

            $('.tdradio').each(function(){
                if($(this).find('input').is(':checked')){
                    $(this).find('input').addClass('radioinput');
                }else{
                    $(this).find('input').removeClass('radioinput');
                }
                var radioinputL = $(this).find('input.radioinput').length;
                if(radioinputL>0){
                    $(this).removeClass('required');
                }

            })

            $('.required').each(function(){
                if ($(this).val()!=='') {
                    $(this).removeClass('required');
                };
            })

            if(username==""){
                alert('请输入登录名！')
                $('#username').focus();
            }else if(!reg.exec(username)){
                alert("请输入正确格式登录名");
                $('#username').focus();
            }else if(nickName==""){
                alert("请输入用户昵称");
                $('#nickName').focus();
            }else if(userPassword==""){
                alert("请输入用户密码");
                $('#userPassword').focus();
            }else if(userPassword2==''){
                alert("请输入用户确认密码");
                $('#userPassword2').focus();
            }else if(userPassword2!==userPassword){
                alert("用户确认密码不正确");
                $('#userPassword2').focus();
            }else if(authKey==''){
                alert("请输入二级密码");
                $('#authKey').focus();
            }else if(authKey2==''){
                alert("请输入二级确认密码");
                $('#authKey2').focus();
            }else if(authKey2!==authKey){
                alert("二级确认密码不正确");
                $('#authKey2').focus();
            }else if(checkedboxL<1){
                alert("请选择关联角色");
                $("input[name='roleId']").eq(0).focus();
            }else if($('.required').length>0){
                console.log($('.required').length);
                var tcz = $('.required').eq(0).parents('tr').find('span').text();
                alert(tcz+'未填写');
                $('.required').eq(0).parents('tr').find('input').focus();
            }
            else{
                $.ajax(
                        {
                            type:"POST",
                            url:"/merchant/plus.json",
                            dataType:"json",
                            data:$('#partnerForm').serialize(),
                            success:function(data) {
                                $(".fullScreenBg").show();$(".message").show();$(".message .text").html(data.message.message+"["+data.message.operateCode+"]");
                            },
                            error:function(XMLResponse){
                                $(".fullScreenBg").show();$(".message").show();$(".message .text").html("操作失败:" +XMLResponse.responseText);
                            },
                        }
                );
            }
        });
        $(".message .querenbtn").click(function(){
            $(".fullScreenBg").hide();$(".message").hide();
        });

    })
</script>
<script>
    $(function(){
        $(".hitarea").click(function(){
            if($(this).hasClass("collapsable-hitarea")){
                $(this).siblings("ul").slideDown();
                $(this).siblings(".folder").children("i").removeClass("fa-folder-o");
                $(this).siblings(".folder").children("i").addClass("fa-folder-open-o");
            }else if($(this).hasClass("expandable-hitarea")){
                $(this).siblings("ul").slideUp();
                $(this).siblings(".folder").children("i").addClass("fa-folder-o");
                $(this).siblings(".folder").children("i").removeClass("fa-folder-open-o");
            }
        })

    })
</script>
</body>
</html>