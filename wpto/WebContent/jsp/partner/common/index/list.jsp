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

    <title>${systemName}管理中心</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!--<link rel="stylesheet" href="../../../../WebContent/theme/partner/basic/style/dashboard.css">-->
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <style type="text/css">
  
.main .page-header,.sub-header {
  margin-top: 0;
    color: #5D5D5D;
    font-size: 27px;
    border-bottom:1px dashed #5D5D5D;
    padding-bottom :5px;
        margin-bottom: 29px;
}
.main .page-header span,.sub-header span{
  border-left: 4px solid #5D5D5D;
  padding-left:6px;
  display: inline-block;
  font-size: 20px;
}
.main {
  padding: 40px;
  padding-bottom:50px;
}
.table-responsive {
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    overflow: hidden;
}
@media(max-width: 1500px){
  .profile-controller{
    width: 100%;
  }
}
  </style>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header"><span>概况</span></h1>

          <div class="row placeholders placeholderuser">
            <div class="col-xs-6 col-sm-6 col-md-3 placeholder">
              <div class="profile-controller profile-controller1">
                <p class="title">今日新增用户</p>
                
                <div>
                  <h4>${registerUserToday}人</h4>
                </div> 
                <p><a href="" class="btn btn-default">查看详情 <span>→</span></a></p>
                <p class="user-icon"></p>
              </div>
            </div>
            <div class="col-xs-6 col-sm-6 col-md-3 placeholder">
              <div class="profile-controller profile-controller2">
                <p class="title">今日成功订单数</p>
                <div>
                  <h4>${successOrderToday==null?0:successOrderToday}笔</h4>
                </div>
                <p><a href="" class="btn btn-default">查看详情 <span>→</sapn></a></p>
                <p class="order-icon"></p>
              </div>
            </div>
            <div class="col-xs-6 col-sm-6 col-md-3 placeholder">
              <div class="profile-controller profile-controller3">
                <p class="title">今日成功交易金额</p>
                
                <div>
                  <h4><fmt:formatNumber type="number" value="${successMoneyToday==null?0:successMoneyToday}" maxFractionDigits="2"/>元</h4>
                </div>
                <p><a href="" class="btn btn-default">查看详情 <span>→</sapn></a></p>
                <p class="money-icon"></p>
              </div>

            </div>
            <div class="col-xs-6 col-sm-6 col-md-3 placeholder">
              <div class="profile-controller profile-controller4">
                
                <p class="title">账户余额</p>
                <div>
                  <h4><fmt:formatNumber type="number" value="${money.incomingMoney==null?0:money.incomingMoney + money.transitMoney}" maxFractionDigits="2"/>元</h4>
                  <h5 style="color:#37485D;font-size:13px;line-height: 20px;"><span style="margin-right:9px;">可提现：<b><fmt:formatNumber type="number" value="${money.transitMoney==null?0:money.transitMoney}" maxFractionDigits="2"/></b>元</span><br><span>，待结算：<b><fmt:formatNumber type="number" value="${money.incomingMoney==null?0:money.incomingMoney}" maxFractionDigits="2"/></b>元</span></h5>
                </div>
                <p><a href="" class="btn btn-default">查看详情 <span>→</span></a></p>
                <p class="ip-icon"></p>
              </div>

            </div>
          </div>
          
          <!-- <div class="row placeholders">
            <div class="col-xs-6 col-sm-3 placeholder">
              <p>系统版本</p>
              <h4>${systemVersion}</h4>
            </div>
            <div class="col-xs-6 col-sm-3 placeholder">
              <p>安全级别</p>
              <h4>${securityLevelId}级</h4>
              <span class="text-muted">${securityLevel.name}(${securityLevel.description})</span>
            </div>
            <div class="col-xs-6 col-sm-3 placeholder">
              <p>系统性能指数</p>
              <h4>${performanceRate}</h4>
            </div>
            <div class="col-xs-6 col-sm-3 placeholder">
              <p>系统运行时间</p>
              <h4>大于1天</h4>
            </div>
          </div> -->

          <h2 class="sub-header"><span>最新用户消息</span></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>时间</th>
                  <th>发信人</th>
                  <th>用户ID</th>
                  <th>内容</th>
                  <th>类型</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              <c:forEach var="msg" items="${userMessageList}">
                <tr>
                	<td><fmt:formatDate value="${msg.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${msg.senderName}</td>
                  	<td>${msg.senderId}</td>
                  	<td>${msg.content}</td>
                  	<td>${msg.messageType}</td>
                   	<td style="position:relative;">
						<span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
						<ul style="position:absolute; width:50px; padding-right: 10px; padding-left: 10px; left:50%; margin-left:-24px; top:28px; line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
							<c:if test="${msg.operate.get != null }"><a href="${msg.operate.get}"><li class="materialSelect">查看</li></a></c:if>
						</ul>
					</td>
              </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
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
  </body>
  <script>
	$(".tools").click(function(){
		$(this).parent().parent().siblings().find(".toolbtns").hide();
		$(this).siblings(".toolbtns").toggle();
	});
  $(function(){
      // div高度相同
  function aa(){

  var addressspan = [];
 
    $('.placeholder .profile-controller').each(function(){
      var spanH = $(this).outerHeight();
      addressspan.push(spanH);
    })
  
    var max = addressspan[0];

  var spanL = addressspan.length;

  for(var aa=1;aa<spanL;aa++){
    if(addressspan[aa]>max && addressspan[aa]!=0){
      max = addressspan[aa];
    }
  }
  console.log(max);
  $('.placeholder .profile-controller').outerHeight(max+"px")
}
aa();


  })
  </script>
</html>
