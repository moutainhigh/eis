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

    <title>${systemName}-权限控制管理详情</title>

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
  .jiaose.none{
    display: none;
  }
  .xzroles>span{
    margin-right: 14px;
  }
  .jiaose>span{
    margin-right: 14px;
  }
  </style>
  <body>
   <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
	  
	     
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span><spring:message code="权限控制管理详情" /></span></h2><!-- ConfigCategory.${ct} -->
			<div class="table-responsive">
				<div class="table-responsive">
         <table class="table table-striped" style="margin-top:15px;" data-privilegeId="${partnerPrivilege.privilegeId}">
          <thead>
            <tr>
              <th>配置</th>
              <th>属性</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>权限ID</th>
              <td style="text-align:left;"><span>${partnerPrivilege.privilegeId}</span></td>
              <td></td>
            </tr>
            <tr>
              <th>权限名</th>
                <td style="text-align:left;"><input type="text" name="privilegeName" id="privilegeName"  value="${partnerPrivilege.privilegeName}" class="styleinput" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                
                </td>
            </tr>
            <tr>
              <th>权限操作代码</th>
              <td style="text-align:left;">
                <input type="text" name="operateCode" id="operateCode" class="styleinput" readonly style="width:50%;" value="${partnerPrivilege.operateCode}"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${operateCode}">
                    <option value="${a}">${a}</option>
                  </c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              
              </td>
            </tr>
            <tr>
              <th>权限操作二级代码</th>
                <td style="text-align:left;"><input type="text" name="subOperateCode" id="subOperateCode"  value="*" class="styleinput" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                
                </td>
            </tr>
            <tr>
              <th>权限匹配对象代码</th>
              <td style="text-align:left;">
                <input type="text" name="objectTypeCode" id="objectTypeCode" class="styleinput" readonly style="width:50%;" value="${partnerPrivilege.objectTypeCode}"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${objectTypes}">
                    <option value="${a}">${a}</option>
                  </c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              </td>
            </tr>
            <tr>
              <th>权限匹配对象ID列表</th>
                <td style="text-align:left;"><input type="text" name="objectList" id="objectList" value="${partnerPrivilege.objectList}" class="styleinput" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
               
                </td>
            </tr>
            <tr>
              <th data-name="roleIds">关联角色</th>
              <td style="text-align:left;">
                <div class="xzroles">
                  <c:forEach var="a" items="${roles}" varStatus="varStatus">
                    <span dataId="${a.roleId}">${a.roleId}.${a.roleName}</span>
                  </c:forEach>
                </div>
                <div class="jiaose none">
                  <c:forEach var="a" items="${roleList}">
                    <input type="checkbox" value="${a.roleId}" /><span>${a.roleId}.${a.roleName}</span>
                  </c:forEach>
                </div>
              </td>
              <td><span class='jsgls' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              </td>
            </tr>
            <tr>
              <th>权限匹配对象中的属性匹配</th>
                <td style="text-align:left;"><input type="text" name="objectAttributePattern" id="objectAttributePattern"  value="${partnerPrivilege.objectAttributePattern}" class="styleinput" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                
                </td>
            </tr>
            <tr>
              <th>权限的当前状态</th>
              <td style="text-align:left;">
                <input type="text" name="currentStatus" id="currentStatus" class="styleinput"  readonly style="width:50%;" value="<spring:message code='Status.${partnerPrivilege.currentStatus}'/>"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${statusList}">
                    <option value="${a}"><spring:message code="Status.${a}"/></option>
                  </c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              
              </td>
            </tr>
            <tr>
              <th>权限说明</th>
                <td style="text-align:left;"><input type="text" name="privilegeDesc" id="privilegeDesc"  value="${partnerPrivilege.privilegeDesc}" class="styleinput" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                
                </td>
            </tr>  
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
   
//弹框点击事件
$('.Popup .btnclose').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  
});
var payMethodId = $('.table').attr('data-privilegeid');
    $('.tools').on('click',function(){
              
              var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
              
              if(typeof($inputText.attr("readonly"))=="undefined"){
                $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                $(this).find('img').attr('title','修改配置');
                $inputText.removeClass('inputclass');
                var key = $inputText.attr('name');
                $inputText.attr('readonly','readonly');

                console.log(key,value);
                //var payMethodId = $inputText.attr('payMethodId');
                //$inputText.removeClass('styleinput');
                    if($(this).parents('tr').hasClass('tabody')){
                      
                      var tishititle = $(this).parent().parent().find('th').text();
                      var value = $inputText.val();
                      var classname = 'updateDataCode';
                      ajaxfunction(key,value,payMethodId,classname,tishititle);
                    }else{
                      var tishititle = $(this).parent().parent().find('th').text();
                      var value = $inputText.val();
                      var classname = 'updateMode';
                      ajaxfunction(key,value,payMethodId,classname,tishititle);
                    }
                     
               
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

                  //var payMethodId = $input.attr('payMethodId');
                     var tishititle = $(this).parent().parent().find('th').text();
                      var value1 = $(this).parent().parent().find('td').eq(0).find('select option:selected').text();
                      $input.val(value1);
                      var value = $(this).parent().parent().find('td').eq(0).find('select option:selected').attr('value');
                      var classname = 'updateMode';
                      ajaxfunction(key,value,payMethodId,classname,tishititle);
              }


        });

$('.xzroles>span').each(function(){
  var index = $(this).index();
      for (var i = 0; i < $('.jiaose>input').length; i++) {
        if($(this).attr('dataId')==$('.jiaose>input').eq(i).attr('value')){
          $('.jiaose>input').eq(i).attr('checked','checked');
        }
      };
  })
$('.jsgls').on('click',function(){
          var tishititle = $(this).parent().parent().find('th').text();
          if($('.jiaose').hasClass('none')){
            $('.jiaose').removeClass('none');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
          }else{
            
            var jiaosetext = '';
            $('.jiaose input').each(function(){
              if($(this).is(':checked')){
                $(this).addClass('checkedbox');
              }else{
                $(this).removeClass('checkedbox');
              }
            })
            var checketext = '';
            var value = ''; 
            var juesehtmladd = '';
            var key = $(this).parent().parent().find('th').attr('data-name');
            if($('.checkedbox').length>0){
              $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
              $('.jiaose').addClass('none');
              $('.checkedbox').each(function(){
                var indextext = $(this).attr('value');//角色id
                checketext = $(this).next('span').text();//角色name
                var juesehtml = "<span>"+checketext+"</span>"
                juesehtmladd +=juesehtml;
                $(this).parent().parent().find('.xzroles').empty();
                $(this).parent().parent().find('.xzroles').append(juesehtmladd);
                value+=indextext+',';
              });
              var classname = 'updateMode';
               ajaxfunction(key,value,payMethodId,classname,tishititle);
            }else{
              alert('关联角色不能为空！')
            }
              //$('.a-updata').text(jiaosetext);
              //$(this).parent().parent().find('span.kz-updata').text(checketext);
            
            
          }
        });

// 关联角色不能为空




//弹框点击事件
 $('.Popup .closespan').on('click',function(){
    $(this).parent().parent().parent().addClass('none');
  });




//提交
function ajaxfunction(key,value,payMethodId,classname,tishititle){
  var dataarry = {'privilegeId':payMethodId};
   dataarry[classname] = key;
   dataarry[key] = value;
   console.log(dataarry);
  $.ajax( {  
         type : "POST",  
         url : "/partnerPrivilege/update.json",  
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
