<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>${pageTitle}</title>
<!--<link rel="stylesheet/less" href="/style/mobile/common.less" />
<script src="/js/less.min.js"></script>-->
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/changepassword.css" rel="stylesheet" type="text/css"> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script type="text/javascript" src='../../../theme/${theme}/js/mobile/jqmeter.min.js'></script>
</head>
<style>
    .login{
        box-sizing: border-box;
        padding: 80px 0;
    }
    .wpuf-label{
        width:100%;
        box-sizing: border-box;
        padding: 10px 15px;
        border-bottom: 1px solid #ccc;
    }
    .wpuf-fields-file{
        width:100%;
        box-sizing: border-box;
        padding: 10px 15px;
        border-bottom:1px solid #ccc;
        margin-bottom: 10px;
    }
    .wpuf-fields-submit{
        width:100%;
        box-sizing: border-box;
        padding: 10px 15px;
    }
    #jqmeter-container{
        width:100%;
        box-sizing: border-box;
        padding-right:15px;
    }
    .upload{
        background:  #7da215;
        width: 100%;
        height: 40px;
        color: #fff;
        text-align: center;
        line-height: 40px;
    }
</style>
<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>设置</span>
  </div>
<!-- <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %> -->
<div class="login">
        <div class="wpuf-label">修改头像</div>
        <form class="wpuf-form-add" action=""  enctype="multipart/form-data" method="post" name="fileinfo" id="registerForm">
            <div class="wpuf-fields">
                    <div class="wpuf-fields-file">
                        <a href="javascript:void();" class="file" style="color: #fff;">选择文件
                            <input type="file" name="image" id="fileupload" required onchange="previewImage(this)">
                        </a>
                        <!--<input type="file" name="image" required  onchange="previewImage(this)" />-->
                        <div id="preview" style="margin:10px 0;"></div>
                        <div id="jqmeter-container"></div>
                    </div>
                    <div class="wpuf-fields-submit">
                        <input type="submit" value="上传" class="upload" style="border-radius: 5px;" />
                    </div>
            </div>
        </form>
</div>
<script>                                                 
    //图片上传预览    IE是用了滤镜。
    function previewImage(file)
    {
            var MAXWIDTH  = 260; 
            var MAXHEIGHT = 180;
            var div = document.getElementById('preview'); 
            if (file.files && file.files[0])
            {
                div.innerHTML ='<img id=imghead>';
                var img = document.getElementById('imghead');
                img.onload = function(){
                    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                    img.width  =  rect.width;
                    img.height =  rect.height;
    //                 img.style.marginLeft = rect.left+'px';
                    img.style.marginTop = rect.top+'px';
                }
                var reader = new FileReader();
                reader.onload = function(evt){img.src = evt.target.result;}
                reader.readAsDataURL(file.files[0]);
            }
            else //兼容IE
            {
                var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
                file.select();
                var src = document.selection.createRange().text;
                div.innerHTML = '<img id=imghead>';
                var img = document.getElementById('imghead');
                img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
                div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
            }
    } 
    
    function clacImgZoomParam( maxWidth, maxHeight, width, height ){
        var param = {top:0, left:0, width:width, height:height};
        if( width>maxWidth || height>maxHeight )
        {
            rateWidth = width / maxWidth;
            rateHeight = height / maxHeight;
            
            if( rateWidth > rateHeight )
            {
                param.width =  maxWidth;
                param.height = Math.round(height / rateWidth);
            }else
            {
                param.width = Math.round(width / rateHeight);
                param.height = maxHeight;
            }
        }
        
        param.left = Math.round((maxWidth - param.width) / 2);
        param.top = Math.round((maxHeight - param.height) / 2);
        return param;
    } 

    // 将本地图片上传后台              
    var form = document.forms.namedItem("fileinfo");
    form.addEventListener('submit', function(ev) {

    // var oOutput = document.querySelector("#preview"),
    oData = new FormData(form);

    // oData.append("CustomField", "This is some extra data");

    var oReq = new XMLHttpRequest();
    oReq.open("POST", "/user/changeHeadPic.json", true);
    oReq.onload = function(oEvent) {
    if (oReq.status == 200) {
            $('#jqmeter-container').jQMeter({
                goal:'$1,000',
                raised:'$1000',
                orientation:'vertical',
                width:'100%',
                height:'20px'
            });
            // location.reload();
            window.location.href = '/content/user/setting.shtml';
        } else {
            $('#jqmeter-container').jQMeter({
                goal:'$1,000',
                raised:'$800',
                orientation:'vertical',
                width:'100%',
                height:'20px'
            });
            $('#preview').append('<p>"Error ":' + oReq.status + ' 上传文件失败.'+'</p>');
            }
        };
        oReq.send(oData);
        ev.preventDefault();
    }, false);
</script>
</body>
</html>