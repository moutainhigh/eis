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

    <title>${systemName}-提现通道详情</title>

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
    width: 95%;
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
        width: 97px;
    padding: 9px;
    background-color: #708AA9;
    color: #fff;
    border-radius: 5px;
    margin-right: 15px;
    display: inline-block;
    font-size: 16px;
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
  select.none{
    display: none!important;
  }
  </style>
  <body>
   <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
	  
	     
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span><spring:message code="提现通道详情" /></span></h2><!-- ConfigCategory.${ct} -->
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
            <tr>
              <th>通道ID</th>
              <td style="text-align:left;"><span>${withdrawMethod.withdrawMethodId}</span></td>
              
              <td></td>
         
            </tr>
            <tr>
                <th style="text-align:left;"> 
                  <span>绑定ID</span>
                </th>   
                <td style="text-align:left;"><input type="text" name="channelId" id="channelId" withdrawMethodId="${withdrawMethod.withdrawMethodId}" value="${withdrawMethod.channelId}" class="styleinput" readonly/></td>
                
                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                <span class="delete" style=" margin-left: 10px;"><img style='width:21px;cursor: pointer;' title='删除' src='/theme/basic/images/delete.png'></span>
                </td>
          
              </tr>
               <tr>
                <th style="text-align:left;"> 
                  <span>名称</span>
                </th>   
                <td style="text-align:left;"><input type="text" name="name" id="name" withdrawMethodId="${withdrawMethod.withdrawMethodId}" value="${withdrawMethod.name}" class="styleinput" readonly/></td>
               
                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                <span class="delete" style=" margin-left: 10px;"><img style='width:21px;cursor: pointer;' title='删除' src='/theme/basic/images/delete.png'></span>
                </td>
           
              </tr>
            <tr>
              <th>对应提现类型</th>
              <td style="text-align:left;">
                <input type="text" name="withdrawTypeId" id="withdrawTypeId" class="styleinput" withdrawMethodId="${withdrawMethod.withdrawMethodId}" readonly withdrawTypeId='${withdrawMethod.withdrawTypeId}' style="width:50%;"/>
                <select id="leixing" class="none" style="width:49%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${withdrawType}">
                    <option value="${a.withdrawTypeId}">${a.withdrawTypeName}</option>
                  </c:forEach>
                </select>
              </td>
             
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              <span class="delete" style=" margin-left: 10px;"><img style='width:21px;cursor: pointer;' title='删除' src='/theme/basic/images/delete.png'></span>
              </td>
            
            </tr>
            <tr>
              <th>处理器</th>
              <td style="text-align:left;">
                <input type="text" name="processClass" id="processClass" value="${withdrawMethod.processClass}" class="styleinput" withdrawMethodId="${withdrawMethod.withdrawMethodId}" readonly style="width:50%;"/>
                <select id="leixing" class="none" style="width:49%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${process}">
                    <option value="${a.processName}">${a.processDesc}</option>
                  </c:forEach>
                </select>  
              </td>
            
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                <span class="delete" style=" margin-left: 10px;"><img style='width:21px;cursor: pointer;' title='删除' src='/theme/basic/images/delete.png'></span>
              </td>
         
            </tr>
            <tr>
                <th style="text-align:left;"> 
                  <span>系统资金账户ID</span>
                </th>   
                <td style="text-align:left;"><input type="text" name="referUuid" id="referUuid" withdrawMethodId="${withdrawMethod.withdrawMethodId}"" value="${withdrawMethod.referUuid}" class="styleinput" readonly/></td>
                
                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                <span class="delete" style=" margin-left: 10px;"><img style='width:21px;cursor: pointer;' title='删除' src='/theme/basic/images/delete.png'></span>
                </td>
            
              </tr>
            
               <!-- 新增 -->
            <c:forEach var="a" items="${configMap}">
              <tr class='tabody'>
              <th style="text-align:left;"> 
                ${a.dataName}
              </th>   
              <td style="text-align:left;"><input type="text" name="${a.dataCode}" id="${a.dataCode}" withdrawMethodId="${a.withdrawMethodId}" value="${a.dataValue}" class="styleinput" readonly/></td>
             
              <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                <span class="delete" style=" margin-left: 10px;"><img style='width:21px;cursor: pointer;' title='删除' src='/theme/basic/images/delete.png'></span>
              </td>
           
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
      <div id="jurisdiction" style="display:none;">
      <span>
        <c:forEach items="${listTag}" var="a">
       ${a.tagName},
        </c:forEach>
      </span>
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
if ($('#jurisdiction>span').length && $('#jurisdiction>span').length>0) {
var jurisdictiontext = $('#jurisdiction>span').text();
if (jurisdictiontext.indexOf('edit')>-1) {
  $('table tr').each(function(){
    $(this).find('td').eq(1).css('display','block');
    $(this).find('th').eq(2).css('display','block');
  })
}else{
  $('table tr').each(function(){
    $(this).find('td').eq(1).css('display','none');
    $(this).find('th').eq(2).css('display','none');
  })
}
}else{
      $('table tr').each(function(){
        $(this).find('td').eq(1).css('display','none');
        $(this).find('th').eq(2).css('display','none');
      })
  }










   
   $('#leixing option').each(function(){
    if($(this).attr('value')==$(this).parents('td').find('input').attr('withdrawTypeId')){
      $(this).parents('td').find('input').val($(this).text());
    }
   })
   // 删除
   $('.delete').on('click',function(){
    var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
    var name = $inputText.attr('name');
    var withdrawMethodId = $inputText.attr('withdrawMethodId');
    var dataarry={};
    var tishititle = $(this).parent().parent().find('th').text();
    
    if($(this).parent().parent().hasClass('tabody')){
        var dataarry = {'withdrawMethodId':withdrawMethodId,'updateDataCode':name};

    }else{
        var dataarry = {'withdrawMethodId':withdrawMethodId,'updateMode':name};
    };
    dataarry[name]=11;
      $.ajax( {  
         type : "POST",  
         url : "/withdrawMethod/delete.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'删除成功！')    
         },  
         error : function() {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'删除失败！')   
         }  
        });  




   })
//弹框点击事件
$('.Popup .btnclose').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  
});

    $('.tools').on('click',function(){
              
              var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
              
              if(typeof($inputText.attr("readonly"))=="undefined"){
                $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                $(this).find('img').attr('title','修改配置');
                $inputText.removeClass('inputclass');
                var key = $inputText.attr('name');
                console.log(key,value);var withdrawMethodId = $inputText.attr('withdrawMethodId');
                 $inputText.attr('readonly','readonly');

                //$inputText.removeClass('styleinput');
                    if($(this).parents('tr').hasClass('tabody')){
                      
                      var tishititle = $(this).parent().parent().find('th').text();
                      var value = $inputText.val();
                      var classname = 'updateDataCode';
                      ajaxfunction(key,value,withdrawMethodId,classname,tishititle);
                    }else{
                      var tishititle = $(this).parent().parent().find('th').text();
                      var value = $inputText.val();
                      var classname = 'updateMode';
                      ajaxfunction(key,value,withdrawMethodId,classname,tishititle);
                    }
                     
               
                }else{
                $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
                $(this).find('img').attr('title','保存配置');
                $inputText.removeAttr('readonly');
                $inputText.addClass('inputclass');
                //$inputText.addClass('styleinput');
                $inputText.focus();
                $(this).parent().parent().find('td').eq(0).find('select').css('display','block');
              }


        });
  $('.selectbtn').on('click',function(){
              var $input = $(this).parent().parent().find('td').eq(0).find('input');
              var key = $input.attr('name');
              var $select = $(this).parent().parent().find('td').eq(0).find('select');
              if($select.hasClass('none')){
                $select.removeClass('none');
                  $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
                  $(this).find('img').attr('title','保存配置');
                  
                  $(this).parent().parent().find('td').eq(0).find('select').css('display','inline-block');
                    
                }else{
                  $select.addClass('none');
                  $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                  $(this).find('img').attr('title','修改配置');
                  $(this).parent().parent().find('td').eq(0).find('select').css('display','none');

                  var withdrawMethodId = $input.attr('withdrawMethodId');
                     var tishititle = $(this).parent().parent().find('th').text();
                      var value1 = $(this).parent().parent().find('td').eq(0).find('select option:selected').text();
                      $input.val(value1);
                      var value = $(this).parent().parent().find('td').eq(0).find('select option:selected').attr('value');
                      var classname = 'updateMode';
                      ajaxfunction(key,value,withdrawMethodId,classname,tishititle);
              }


        });






      //弹框点击事件
       $('.Popup .closespan').on('click',function(){
          $(this).parent().parent().parent().addClass('none');
        });




//提交
function ajaxfunction(key,value,withdrawMethodId,classname,tishititle){
  var dataarry = {'withdrawMethodId':withdrawMethodId};
   dataarry[classname] = key;
   dataarry[key] = value;
   console.log(dataarry);
  $.ajax( {  
         type : "POST",  
         url : "/withdrawMethod/update.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'修改成功！')    
         },  
         error : function() {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'修改失败！')   
         }  
        });  
}
// function ajaxfunction2(key,value,withdrawMethodId,classname,tishititle){
//   var dataarry = {'withdrawMethodId':withdrawMethodId}; 

//   dataarry[classname] = key;
//   dataarry[key] = value; 
//   console.log(dataarry);
//   $.ajax( {  
//          type : "POST",  
//          url : "/withdrawMethod/update.json",  
//          dataType:"json",
//          data:dataarry, 
//          success : function(data) {  
//             $('.outpopup').removeClass('none');
//             $('.outpopup h3').text(tishititle+'修改成功！')    
//          },  
//          error : function() {  
//             $('.outpopup').removeClass('none');
//             $('.outpopup h3').text(tishititle+'修改失败！')   
//          }  
//         });  
// }

  </script>
  </body>
</html>
