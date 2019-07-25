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

    <title>${systemName}-商户配置</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	  <link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
  </head>
  <style type="text/css">
  input[type='text']{
    width: 100%;
  }
  input{
    
    border:none;
    background-color: transparent;
  }
  .Tinput{
    border-radius: 5px;
    width: 80%;
    border:none;
    padding: 3px 5px;
    background-color: transparent;
    text-align: center;
  }
  td{
    padding: 7px 0;
  }
  .TinputBorder{
    border:1px solid #ddd;
  }
  .styleinput{
        border-radius: 5px;
    border: 1px solid #989898;
    padding: 1px 3px;
    background-color: #E6E6E6;
  }
  .Popup{
    position: fixed;top: 0;left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 99999;
  } 
  .Popup.none{
    display: none;
  }
  .Popup .box{
    position: absolute;
    top: 50%;
    width: 300px;
    height: 182px;
    left: 50%;
    margin-left: -150px;
    margin-top: -140px;
    background-color: #fff;
    border-radius: 5px;
    padding: 20px;
  }
  .Popup .box h3{
    font-size: 20px;
    text-align: center;
    margin-bottom: 36px;
    color: #A52222;
    margin-top: 29px;
  }
  .Popup .box p{
    text-align: center;    margin-top: 18px;
  }
  .Popup .box p.tsp{
    font-size: 12px;
    color: red;
    margin: 0;
    text-align: left;
    margin-top: 5px;
  }
  .Popup .box p.tsp.none{
    display: none;
  }
  .Popup .box input[type="text"]{
    background-color: #eee;
    border:1px solid #ddd;
    border-radius: 5px;
    padding: 3px 5px;
  }
  .Popup .box input[type="button"]{
    width: 120px;
    padding: 5px;
    background-color: #506A9C;
    color: #fff;
    border-radius: 5px;
    margin-right: 15px;
    display: inline-block;
  }
  .Popup .none{
      display: none;
  }
  .Popup .closespan{
    display: inline-block;
    width: 120px;
    padding: 6px;
    background-color: #b1b1b1;
    color: #fff;
    border-radius: 5px;
    cursor: pointer;
  }
  </style>
  <body>
   <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
	  
	     
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span><spring:message code="商户配置" /></span></h2><!-- ConfigCategory.${ct} -->
			<div class="table-responsive">
				<div class="table-responsive">
         <table class="table table-striped" style="margin-top:15px;" data-uuid="${partner.uuid}">
          <thead>
            <tr>
                                
              <th>配置</th>
                                
              <th>属性</th>
                                
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr data-required="true">
              <th>昵称</th>
              <td><input type="text" name="nickName" id="nickName" readonly value="${partner.nickName}"/ class="styleinput"></td>
              <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
            </tr>
            <tr  data-required="true">
              <th>密码</th>
              <td>
                <input type="text" name="userPassword" id="userPassword" class="styleinput" value="******" readonly />
              </td>  
              <td><span class='tools password'  style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                <div class="Popup none">
                  <div class="box">
                  <p>请再一次输入密码</p>
                  <input type="text" placeholder="确认密码"/>
                  <p class='tsp none'>确认密码不正确！请重新输入</p>
                  <p><input type="button" value="确 认"><span class="closespan">取 消</span></p>
                  </div>
                </div>
              </td>
            </tr>
            <tr  data-required="true">
              <th>二级密码</th>
              <td>
                <input type="text" name="authKey" id="authKey" class="styleinput" value="******" readonly />
              </td>
              <td><span class='tools password' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                  <div class="Popup none">
                  <div class="box">
                  <p>请再一次输入密码</p>
                  <input type="text" placeholder="确认密码"/>
                  <p class='tsp none'>确认密码不正确！请重新输入</p>
                  <p><input type="button" value="确 认"><span class="closespan">取 消</span></p>
                  </div>
                </div>
              </td>
            </tr>
               <!-- 新增 -->
            <c:forEach var="a" items="${configMap}">
            <tr class='tabody' data-readonly="${a.value.readonly}" data-required="${a.value.required}">
            <th style="text-align:left;" data='${a.value.dataValue}'> 
              <input type="text" value="${a.value.dataCode}" readonly  title="${a.value.dataDescription}" />
            </th>   
              <c:choose>
               <c:when test="${a.value.dataType=='boolean'}">
                <td class="tdradio" style='text-align:left;' data-readonly="${a.value.readonly}" data-required="${a.value.required}">
                  <span class="kz-updata">${a.value.dataValue}</span>
                  <span class="truefalse hide">
                  <input type="radio" name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" value="true" /><span style="margin-right:6px;">true</span>
                  <input type="radio" name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" value="false"/><span>false</span></span>
                </td>  
              </c:when>
              <c:when test="${!empty a.value.dataValue}">
                <td class="kzinputtd" style='text-align:left;'>
                  <input name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" value="${a.value.dataValue}" type="text" data-readonly="${a.value.readonly}" readonly data-required="${a.value.required}" class="styleinput" />
                </td>
              </c:when>
              <c:otherwise>
                <td class="kzinputtd tadata" style='text-align:left;'>
                  <input name="data.${a.value.dataCode}" id="data.${a.value.dataCode}"  type="text"  value=""  style="min-width:50%;"  readonly class="styleinput" class="styleinput" />
                </td>
              </c:otherwise>
            </c:choose>
           
            </tr>
            </c:forEach>
            <!-- 结束 -->   
          </tbody>
        </table>  
      </div>


			</div>
        </div>
        
       
        
      </div>
    </div>
<!-- 弹窗 -->
    <div class="Popup outpopup none">
      <div class="box">
      <h3></h3>
      <p><input type="button" class="btnclose" value="确 认"></p>
      </div>
    </div>
    
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <!--<script src="/theme/${theme}/js/vendor/holder.min.js"></script>-->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	 <script> 
$('.kzinputtd').each(function(){
    if($(this).parent().attr('data-readonly')=='false'){
      console.log('tatat');
      $(this).parent().append("<td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>")
    }else{
      $(this).parent().append("<td></td>");console.log('bbbb');
    }
  });

 //扩展数据
   $('.tdradio').each(function(){
      if($(this).attr('data-readonly')=='false'){
        $(this).parent().append("<td><span class='radiobtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>")
        //$(this).addClass('required');
      }else{
        $(this).parent().append("<td></td>");
      }
    });
//弹框点击事件
$('.Popup .btnclose').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  
});

var uuid = $('.table-striped').attr('data-uuid');
    $('.tools').on('click',function(){
              
              var $inputText = $(this).parent().parent().find('td').eq(0).find('input');

              if(typeof($inputText.attr("readonly"))=="undefined"){
                $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                $(this).find('img').attr('title','修改配置');
                $inputText.attr('readonly','readonly');
                $inputText.removeClass('inputclass');
                var key = $inputText.attr('name');
                var value = $inputText.val();
                console.log(key,value);
                //$inputText.removeClass('styleinput');
                if($(this).parents('tr').attr('data-required')=='true' && $.trim(value)==''){
                  alert($(this).parents('tr').find('th').text()+'不能为空！');
                  $inputText.focus();
                  $inputText.removeAttr('readonly');
                }else{
                  if($(this).hasClass('password')){
                    var $Popup = $(this).parent().find('.Popup');
                    $Popup.removeClass('none');
                     $Popup.find("input[type='text']").val('');
                     var placeholdertext = $(this).parents('tr').find('th').text();
                     $Popup.find("input[type='text']").attr('placeholder',placeholdertext);
                     
                      //return false;
                  }else{
                    if($(this).parents('tr').hasClass('tabody')){
                      var tishititle = $(this).parent().parent().find('th input').attr('value');
                      
                      var classname = 'updateDataCode';
                      ajaxfunction(key,value,uuid,classname,tishititle);
                    }else{
                      var tishititle = $(this).parent().parent().find('th').text();
                      var classname = 'updateMode';
                      ajaxfunction(key,value,uuid,classname,tishititle);
                    }
                     
                     
                  }
               
                }
             }else{
                $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
                $(this).find('img').attr('title','保存配置');
                $inputText.removeAttr('readonly');
                $inputText.addClass('inputclass');
                //$inputText.addClass('styleinput');
                $inputText.focus();
              }


        });

      //弹框点击事件
       $('.Popup .closespan').on('click',function(){
          $(this).parent().parent().parent().addClass('none');
          var $shuruinput = $(this).parent().parent().parent().parent().parent().find('td').eq(0).find('input');
          $shuruinput.removeAttr('readonly');$shuruinput.focus();
        });

        $('.Popup input[type="button"]').on('click',function(){
          var tishititle = $(this).parent().parent().parent().parent().parent().find('th').text();
         var $shuruinput = $(this).parent().parent().parent().parent().parent().find('td').eq(0).find('input');
          var querenmima = $(this).parent().parent().find('input[type="text"]').val();
          var key = $shuruinput.attr('name');
          var value = $shuruinput.val();
          console.log(value);
          if($.trim(querenmima)==''||querenmima !== value) {
            $(this).parent().parent().find('p.tsp').removeClass('none');
          }else{
            $(this).parent().parent().find('p.tsp').addClass('none');
            $(this).parent().parent().parent().addClass('none');
              var classname = 'updateMode';
            ajaxfunction(key,value,uuid,classname,tishititle);
          }
        });


        //true false选择
        $('.radiobtn').on('click',function(){
          var tishititle = $(this).parent().parent().find('th').text();
          var $ycspan = $(this).parent().parent().find($('.truefalse'));
          if($ycspan.hasClass('hide')){
            $ycspan.removeClass('hide');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
          }else{
            $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
            $ycspan.addClass('hide');
            var jiaosetext = '';
            var $input = $ycspan.find("input[type='radio']");
            $input.each(function(){
              if($(this).is(':checked')){
                jiaosetext = $(this).next('span').text();
                $(this).parent().parent().find('span.kz-updata').text(jiaosetext);
              }

            })
            var classname = 'updateDataCode';
            var key = $(this).parent().parent().find("input[type='radio']").eq(0).attr('name');
            ajaxfunction(key,$(this).parent().parent().find('span.kz-updata').text(),uuid,classname,tishititle);
          }
        });




//提交
function ajaxfunction(key,value,uuid,kuozhan,tishititle){
  var dataarry = {'uuid':uuid};
   dataarry[kuozhan] = key;
   dataarry[key] = value;
   console.log(dataarry);
  $.ajax( {  
         type : "POST",  
         url : "/partner/update.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+data.message.message)    
         },  
         error : function() {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'修改失败！')   
         }  
        });  
}


  </script>
  </body>
</html>
