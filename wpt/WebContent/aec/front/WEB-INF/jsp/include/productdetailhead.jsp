<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<style>

.detail span {
    background: #fd7a40 none repeat scroll 0 0;
    border: 2px solid #fff;
    border-radius: 20px;
    color: #fff;
    display: inline-block;
    float: right;
    font-size: 12px;
    height: 16px;
    line-height: 16px;
    margin: 5px;
    padding: 0 5px;
    text-align: center;
}
</style>

<div class="header"  id="header"> <a class="detail"
 href="/yxcart/list.shtml"><span style="">${cartCount}</span></a> <a class="dmenu" href="javascript:goBack();"></a><a href="/">${fn:substring(pageTitle ,0,12)}${fn:length(pageTitle)>12?"..":""}</a> </div>
