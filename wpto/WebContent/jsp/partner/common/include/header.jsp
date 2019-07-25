<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>
<style type="text/css">
  .navbar-inverse .navbar-nav>li>a {
    color: #000000;
    display: inline-block;
    padding: 2px 6px;
    border-radius: 4px;
    margin-top: 14px;
    margin-left: 16px;
    background-color: #B0BDD0;
}
.navbar-inverse .navbar-nav>li>a:hover{
  /*background-color: #fff!important;*/
}
/*.navbar-inverse {
    background-color: #0F8A0E;
    border-color: #0F8A0E;
    box-shadow: 1px 1px 5px #5D5D5D;
}
*/
/*.tree  a:hover {
  color: #31A030 ;
  background:transparent !important;
}
.menuTitle:hover  a {
  color: #31A030 !important;
  background:transparent !important;
}
.menuTitle:hover  i {
  color: #31A030 !important;
  background:transparent !important;
}
.menuTitle:hover  span {
  border: 1px solid #31A030 !important;
}
.nav-sidebar > .active > a,
.nav-sidebar > .active > a:hover,
.nav-sidebar > .active > a:focus {
  color: #31A030 !important;
}
.nav-sidebar > .active i,
.nav-sidebar > .active i:hover,
.nav-sidebar > .active i:focus {
  color: #31A030 !important;
  
}
.nav-sidebar > .active span,
.nav-sidebar > .active span:hover,
.nav-sidebar > .active span:focus {
  border: 1px solid #31A030 !important;
  
}

.nav-sidebar > li:hover > a,
.nav-sidebar > li:focus> a {
  color: #31A030 !important;
}

.nav-sidebar > li:hover i,
.nav-sidebar > li:focus i {
  color: #31A030 !important;
  
}

.nav-sidebar > li:hover span,
.nav-sidebar > li:focus span {
  border: 1px solid #31A030 !important;
  
}*/
@media(max-width: 768px){
  .navbar-brand{
    display: block!important;
  }
}

</style>
<nav class="navbar navbar-inverse navbar-fixed-top navbar navbar-inverse navbar-fixed-top col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" style="color:#fff;font-weight:bold; display:none;" href="/">管理菜单</a><span class="welcomeName">欢迎来到<span style="color:rgb(206, 206, 206);">${systemName}管理中心</span>，${welcomeName}！</span>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/config.shtml" target="_blank">设置</a></li>
            <li><a href="/user/logout.shtml">退出</a></li>
            <li><a href="#">关于</a></li>
          </ul>  
          <div class="navbar-right" style="color:#E4E9F0;height:50px;line-height:50px;margin-right:10px;">
			${securityLevelDesc} 
          </div>
        </div>
      </div>
    </nav>