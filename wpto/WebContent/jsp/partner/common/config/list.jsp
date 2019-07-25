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

    <title>${systemName}-系统配置</title>

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
  <script src="/theme/${theme}/js/sweetalert.min.js"></script>
  <link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
  </head>
  <style type="text/css">
  .jiaose.none{
    display: none;
  }
  #leixing.none{
    display: none;
  }
  input[type="text"]{
    width: 700px;
    border-radius: 5px;
    border:1px solid #333;
    padding: 3px 5px;
  }
  table,tbody,tr{
    display: inline-block;
    width: 100%;
  }
  tr{
    border-bottom: 1px solid #ddd;
  }
  td{
    border-top:none!important;
  }
  .jiaose>span{
    display: inline-block;
  }
  </style>
  <body>
  <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
    <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
    
      <c:forEach var="ct" items="${categoryList}">
    
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      <h2 class="sub-header"><span><spring:message code="ConfigCategory.${ct}" /></span></h2>
      <div class="table-responsive">
        <table class="table table-striped">
                 
          <tbody>
          <c:forEach var="i" items="${rows}">
            <c:if test="${i.category == ct}">
            <tr >
              <th width="250px"style="text-align:right;width:250px!important;border-top:none;overflow:hidden;"><span title="${i.configDescription}(${i.configName})" style="width:180px;display:block;white-space:nowrap;text-overflow:ellipsis; -o-text-overflow:ellipsis; overflow: hidden;text-align:right;float:right;">${i.configDescription}</span></th>
              <td style="text-align:left;border-top:none;padding-left: 50px;text-overflow:ellipsis; -o-text-overflow:ellipsis; overflow: hidden;width:80%;" title="${i.configValue}" class="amend">
                
                <c:choose>
   
                  <c:when test="${i.configName == 'siteNodeProcessorList'}">
                
                    <div class="xzroles">
                        <span dataId="${i.configValue}">
                          ${i.configValue}
                        </span>
                    </div>
                    <div class="jiaose none">
                      <c:forEach var="a" items="${siteNodeProcessorList}">
                      <span><input type="checkbox" name="siteNodeProcessorList"  value="${a}"><span style="display:inline-block;background-size: contain;background-repeat: no-repeat;">${a}</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
                      </c:forEach>
                    </div>
                  </td>
                  <td class="operating" data-category="${i.category}" data-categoryDescription="${i.categoryDescription}" data-configDescription="${i.configDescription}" data-configId="${i.configId}" data-currentStatus="${i.currentStatus}" data-configName="${i.configName}">
                    <span class='jsgls' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                  </td>
                  </c:when>
                  
                  <c:when test="${i.configName == 'displayNodeInFirstPageConfig'}">
                    <div class="xzroles">
                        <span dataId="${i.configValue}">
                          ${i.configValue}
                        </span>
                    </div>
                    <div class="jiaose none duoxuan">
                      <c:forEach var="a" items="${displayNodeInFirstPageConfig}">
                      <span><input type="checkbox" name="siteNodeProcessorList"  value="${a.nodeId}"><span style="display:inline-block;background-size: contain;background-repeat: no-repeat;">${a.alias}</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
                      </c:forEach>
                    </div>
                    </td>
                    <td class="operating" data-category="${i.category}" data-categoryDescription="${i.categoryDescription}" data-configDescription="${i.configDescription}" data-configId="${i.configId}" data-currentStatus="${i.currentStatus}"data-configName="${i.configName}">
                      <span class='jsgls' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                    </td>
                  </c:when>

                  <c:when test="${i.configName == 'partnerDefaultRoleid'}">
                    <div class="xzroles">
                        <span dataId="${i.configValue}">
                          ${i.configValue}
                        </span>
                    </div>
                    <div class="jiaose none duoxuan">
                      <c:forEach var="a" items="${partnerDefaultRoleid}">
                      <span><input type="checkbox" name="siteNodeProcessorList"  value="${a.roleId}"><span style="display:inline-block;background-size: contain;background-repeat: no-repeat;" data-value="${a.roleId}">${a.roleId}.${a.roleName}</span></span>&nbsp;&nbsp;&nbsp;&nbsp;
                      </c:forEach>
                    </div>
                    </td>
                    <td class="operating" data-category="${i.category}" data-categoryDescription="${i.categoryDescription}" data-configDescription="${i.configDescription}" data-configId="${i.configId}" data-currentStatus="${i.currentStatus}"data-configName="${i.configName}">
                      <span class='jsgls' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                    </td>
                  </c:when>                  
                  <c:when test="${i.configName == 'siteDefaultNodeProcessor'}">
                     <input type="text" name="siteDefaultNodeProcessor" class="styleinput"  readonly style="width:300px" value="${i.configValue}"/>
                      <select id="leixing" class="none" style="width:330px;padding: 1px;border-radius: 5px;">
                        <c:forEach var="a" items="${siteNodeProcessorList}">
                          <option value="${a}">${a}</option>
                        </c:forEach>
                      </select>
                      </td>
                      <td class="operating" data-category="${i.category}" data-categoryDescription="${i.categoryDescription}" data-configDescription="${i.configDescription}" data-configId="${i.configId}" data-currentStatus="${i.currentStatus}"data-configName="${i.configName}">
                        <span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                      </td>
                  </c:when>
                  
                  <c:otherwise>
                    
                    <input type="text" value='${i.configValue}' name="${i.configName}" readonly/>
                  </td>
                    <td class="operating" data-category="${i.category}" data-categoryDescription="${i.categoryDescription}" data-configDescription="${i.configDescription}" data-configId="${i.configId}" data-currentStatus="${i.currentStatus}" data-configName="${i.configName}">
                        <span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>

                    </td>
                  </c:otherwise>
                </c:choose>
              
              
            </tr>
            </c:if>
          </c:forEach>
          </tbody>
        </table>
      </div>
        </div>
        
        </c:forEach>
        
      </div>
    </div>
  <%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
  <script>
    $(function(){
      $("h2").eq(0).append('<i class="fa fa-user" style="margin-left:20px;"></i>');
      $("h2").eq(1).append('<i class="fa fa-wrench" style="margin-left:20px;"></i>'); 
      $("h2").eq(2).append('<i class="fa fa-sitemap" style="margin-left:20px;"></i>');  
      $("h2").eq(3).append('<i class="fa fa-shopping-cart" style="margin-left:20px;"></i>');
    })
$('td.amend').each(function(){
  if($(this).attr('title')=='true' || $(this).attr('title')=='false'){
    $(this).addClass('aaaaa');
    $(this).empty();
    $(this).append('<div class="xzroles danxuan"><span></span></div><div class="jiaose none radiobtn"><input type="radio" value="true"/><span>true</span><input type="radio" value="false"/><span>false</span></div>');
    $(this).find('.xzroles>span').attr('dataId',$(this).attr('title'));
    $(this).find('.xzroles>span').text($(this).attr('title'));
    $(this).parent().find('td').eq(1).find('span').addClass('jsgls');
    $(this).parent().find('td').eq(1).find('span').removeClass('tools');
  };


}) 


$(".jiaose>span>input").each(function(){
  var text = $(this).attr('value');
  
  var spantext = $(this).parent().parent().parent().find('.xzroles>span').attr('dataId');
  if (spantext.indexOf(text) <0) {
    }else{
      $(this).attr('checked',true);   
    };
})


  
// true  false
$('.danxuan>span').each(function(){
  var inputname = $(this).parents('tr').find('.operating').attr('data-configname');
  
  var input = $(this).parents('td').find($("input[type='radio']"));
  input.attr('name',inputname);
      for (var i = 0; i < input.length; i++) {
        if($(this).attr('dataId') == input.eq(i).attr('value')){
          var $inputche = input.eq(i);
          $inputche.attr('checked',true);
        }
      };
  })



  //var payMethodId = $('.table').attr('data-shareConfigId');
  // 输入框
$('.tools').on('click',function(){
var category = $(this).parent().attr('data-category');
var categoryDescription = $(this).parent().attr('data-categoryDescription');
var configDescription = $(this).parent().attr('data-configDescription');
var configId = $(this).parent().attr('data-configId');
var currentStatus = $(this).parent().attr('data-currentStatus');

        var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
        if(typeof($inputText.attr("readonly"))=="undefined"){
          $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
          $(this).find('img').attr('title','修改配置');
          $inputText.removeClass('inputclass');
          var key = $inputText.attr('name');  
          $inputText.attr('readonly','readonly');
          var tishititle = $(this).parent().parent().find('th span').text();
          var value = $inputText.val();
          ajaxfunction(key,value,tishititle,category,categoryDescription,configDescription,configId,currentStatus);
      }
       else{
          $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
          $(this).find('img').attr('title','保存配置');
          $inputText.removeAttr('readonly');
          $inputText.addClass('inputclass');
          //$inputText.addClass('styleinput');
          $inputText.focus();
          $(this).parent().parent().find('td').eq(0).find('select').css('display','block');
        }


  }); 

// 下拉框
$('.selectbtn').on('click',function(){
var category = $(this).parent().attr('data-category');
var categoryDescription = $(this).parent().attr('data-categoryDescription');
var configDescription = $(this).parent().attr('data-configDescription');
var configId = $(this).parent().attr('data-configId');
var currentStatus = $(this).parent().attr('data-currentStatus');

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
            
            //var payMethodId = $input.attr('payMethodId');
               var tishititle = $(this).parent().parent().find('th').text();
                var value1 = $(this).parent().parent().find('td').eq(0).find('select option:selected').text();
                $input.val(value1);
                var value = $(this).parent().parent().find('td').eq(0).find('select option:selected').attr('value');
                ajaxfunction(key,value,tishititle,category,categoryDescription,configDescription,configId,currentStatus);
        }
  });
// 单选多选框
$('.jsgls').on('click',function(){
var category = $(this).parent().attr('data-category');
var categoryDescription = $(this).parent().attr('data-categoryDescription');
var configDescription = $(this).parent().attr('data-configDescription');
var configId = $(this).parent().attr('data-configId');
var currentStatus = $(this).parent().attr('data-currentStatus');
var key = $(this).parent().attr('data-configName');
        var tishititle = $(this).parent().parent().find('th span').text();
        var jiaose = $(this).parents('tr').find('.jiaose');
        var jiaoseinput = jiaose.find('input');
        if(jiaose.hasClass('none')){
          jiaose.removeClass('none');
          $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
        }else{
          
          var jiaosetext = '';
          jiaoseinput.each(function(){
            if($(this).is(':checked')){
              $(this).addClass('checkedbox');
            }else{
              $(this).removeClass('checkedbox');
            } 
          })
          var checketext = '';
          var value = ''; 
          var juesehtmladd = '';
          
          var checkedbox = $(this).parents('tr').find($('.checkedbox'));
          if(checkedbox.length>0){
            $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
            jiaose.addClass('none');
            checkedbox.each(function(){
              var indextext = $(this).attr('value');//角色id
              if ($(this).parent().hasClass('radiobtn')) {
                juesehtmladd +=indextext;
                value+=indextext;
              }else{
                juesehtmladd +=indextext+',';
                value+=indextext+',';
              }  
              
              $(this).parent().parent().parent().find('.xzroles span').text(juesehtmladd);
              
              //console.log(v)
            });
             ajaxfunction(key,value,tishititle,category,categoryDescription,configDescription,configId,currentStatus);
          }else{
            alert(tishititle+'不能为空！')
          }
            //$('.a-updata').text(jiaosetext);
            //$(this).parent().parent().find('span.kz-updata').text(checketext);
        }
      });

//提交
function ajaxfunction(key,value,tishititle,category,categoryDescription,configDescription,configId,currentStatus){
  var dataarry = {'category':category,'categoryDescription':categoryDescription,'configDescription':configDescription,'configId':configId,'currentStatus':currentStatus,'configName':key,'configValue':value};
  
   console.log(dataarry);
  $.ajax( {   
         type : "POST",  
         url : "/config/update.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) {  
              swal(tishititle+'修改成功！')
         },  
         error : function() {  
            swal(tishititle+'修改失败！')
         }  
        });  
}





  </script>
  </body>
</html>
