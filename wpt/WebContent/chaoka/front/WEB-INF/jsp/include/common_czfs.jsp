<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

<div id="czfs"><ul id="adbar" class="mainNav5"> <c:forEach var="payType" items="${payTypeList}">
                 <li style="background: url(${payType.logoUrl}) no-repeat  20px center"><a href="/${contentPrefix}pay/${payType.payTypeId}${pageSuffix}" >
                 ${payType.name}</a></li>
                  </c:forEach> </ul>  </div>
