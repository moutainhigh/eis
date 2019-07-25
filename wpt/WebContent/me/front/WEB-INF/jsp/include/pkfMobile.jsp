        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <style>
                .ptfcontent{
                        width: 35%;
                        height: auto;
                        position:fixed;
                        bottom:15%;
                        right:20px;
                        z-index:999;
                }
                .ptfclosebook{
                        width: 20px;
                        height: auto;
                        position: relative;
                        left:-16px;
                        top:5px;
                }
                .ptfbook{
                        width: 100%;
                        height: auto;
                }
        </style>
        <div class="ptfcontent">
                <img src="../../../theme/basic/images/closebook.png" alt="" class="ptfclosebook" onclick="closebook()">
                <a href="/content/about/qixingbook.shtml">
                        <img  class="ptfbook" src="../../../theme/basic/images/book.gif" alt=""> 
                </a>
        </div>
        <script src='../../../theme/${theme}/js/EasyLazyload.min.js'></script>
        <script>
        $(function(){
                // 图片懒加载   
                lazyLoadInit();  
        })

        function closebook(){
                $('.ptfcontent').css('display','none');
        }
        </script>