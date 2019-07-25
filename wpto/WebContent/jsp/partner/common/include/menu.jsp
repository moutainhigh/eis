<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>
<style type="text/css">
/*搜索框*/
#searchdiv{
	width: 100%;
	position: relative;
}
#searchdiv input{
	border: 1px solid #ddd;
    border-radius: 19px;
    width: 90%;
    margin-left: 5%;
    font-size: 12px;
    padding: 5px 6px;
    padding-right: 25px;
    background-color: #fff;
}
#searchdiv img{
	position: absolute;
    right: 7%;
    top: 5px;
    opacity: 0.5;
    width: 19px;
    cursor: pointer;
}
@media(max-width: 768px){
	.webname{
		display: none;
	}
}
/*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
.scrollbar::-webkit-scrollbar{
    width: 5px;
    height: 5px;
    background-color: #eee;
}
/*定义滚动条的轨道，内阴影及圆角*/
.scrollbar::-webkit-scrollbar-track{
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    border-radius: 10px;
    background-color: #eee;
}
/*定义滑块，内阴影及圆角*/
.scrollbar::-webkit-scrollbar-thumb{
    /*width: 10px;*/
    height: 20px;
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: rgb(43,57,74);
}
input[type=text]:focus{outline:none;}//IE
input[type=text]::-moz-focus-inner{outline:0;}//火狐
</style>
<script>
// 菜单查询
function seachmenu(){
	var url = window.location.host;
	
	var searchV = $('#searchdiv input').val();
	var hanziReg = /^[\u4e00-\u9fa5]+$/;
	var Eng = /[a-z\.]+$/;
		$.ajax({
			type:'get',
			url:'/partnerMenuRoleRelation.json',
			dataType:'json',
			success:function (data) {
				if(hanziReg.test(searchV)){
					if (data.partnerMenuList!=null){
						var urls = '';
						var n=data.partnerMenuList;
						for (var i = 0; i < n.length; i++) {
							if (searchV==n[i].menuName) {
								urls = n[i].subMenuList[0].menuUrl;
								console.log(urls)
							}else if (n[i].subMenuList!=null) {
								var m = n[i].subMenuList;
								for (var ii = 0; ii < m.length; ii++) {
									if (searchV==m[ii].menuName) {
										urls = m[ii].menuUrl;
										console.log(urls)
									}
								}
							}
						}
						//console.log(url+urls)
						if (urls=='') {
							alert('该菜单不存在，请重新搜索！')
						}else{
							window.location.replace('http://'+url+urls);
						}
						
					}
				}
				else if(Eng.test(searchV)){
					if (data.partnerMenuList!=null){
						var urls = '';
						var n=data.partnerMenuList;
						for (var i = 0; i < n.length; i++) {
							if (n[i].subMenuList!=null) {
								var m = n[i].subMenuList;
								if (searchV.indexOf('/')!=-1) {
									var searchVf = searchV.split('/')[0];
								}else{
									var searchVf = searchV.split('.')[0];
								}
								
								console.log(searchVf);
								for (var ii = 0; ii < m.length; ii++) {
									if ("/"+searchVf+'.shtml'==m[ii].menuUrl) {
										urls = '/'+searchV;
										console.log(urls)
									}
								}
							}
						}
						//console.log(url+urls)
						if (urls=='') {
							alert('该地址不存在，请重新搜索！')
						}else{
							window.location.replace('http://'+url+urls);
						}
						
					}
				}else{
					alert('请输入中文菜单名如（“支付统计”）或菜单链接地址如（payStat.shtml）进行搜素')
				}
				
			}

		})
	   
	

}

$.ajax({ 
		    type: "GET", 	
			url: "/partnerMenuRoleRelation.json",
			dataType: "json",
			success: function(data){
			
				//alert(location.pathname)
				if (data.partnerMenuList!=null){
					var n=data.partnerMenuList.length;
					for(var j=0;j<n;j++){
						var subHtml='<li class="menuList">';
						if(j==1){
                            subHtml+='<div class="menuTitle"><span style="cursor:pointer;"><i class="glyphicon "   style="cursor:pointer;"></i></span><a href="javascript:void(0);" style="padding-right: 20px; padding-left: 20px;text-decoration:none;">'+data.partnerMenuList[j].menuName+'</a></div>'
                            getIcon(j,data.partnerMenuList[j].resourceId);
						}else{
                            subHtml+='<div class="menuTitle"><span style="cursor:pointer;"><i class="glyphicon " style="cursor:pointer;"></i></span><a href="javascript:void(0);" style="padding-right: 20px; padding-left: 20px;text-decoration:none;">'+data.partnerMenuList[j].menuName+'</a></div>'
                            getIcon(j,data.partnerMenuList[j].resourceId);
                        }
						if(data.partnerMenuList[j].subMenuList!=null){
							var m=data.partnerMenuList[j].subMenuList.length;
							subHtml+='<ul class="nav nav-sidebar" style="display:none;">';
							for(var k=0;k<m;k++){								
								subHtml+='<li><span><i class="glyphicon glyphicon-leaf"></i></span><a href="#"></a></li>';
                                getIcon(j,k,data.partnerMenuList[j].subMenuList[k].resourceId);
							}
							subHtml+='</ul>';							
						}
						subHtml+='</li>';
						$(".side").append(subHtml);


					};
					var searchhtml = '<div id="searchdiv"><input type="text"placeholder="搜索请输入菜单名或菜单链接地址" title="菜单链接地址格式：template.shtml或template/create.shtml ；菜单名如：“系统模板”"/><img src="/theme/${theme}/images/search.png"></div>';
						$(".side").before(searchhtml);

					$('#searchdiv img').on('click',function(){
						seachmenu();
					})
					 $('#searchdiv input').keypress(function(e) {  
					    // 回车键事件  
					       if(e.which == 13) {  
					   			seachmenu();
					       }  
					   }); 
					for(var i=0;i<n;i++){
						if(data.partnerMenuList[i].subMenuList!=null){
							$(".menuTitle").eq(i).addClass("parent_li");
							var m=data.partnerMenuList[i].subMenuList.length;
							for(var k=0;k<m;k++){
								//alert(data.partnerMenuList[i].subMenuList[k].menuName);
								var timestamp=new Date().getTime();
								$(".menuList").eq(i).find(".nav-sidebar li a").eq(k).html(data.partnerMenuList[i].subMenuList[k].menuName);
								var menuUrl=data.partnerMenuList[i].subMenuList[k].menuUrl;
								if(menuUrl.indexOf("?")==-1){
									menuUrl=menuUrl+"?time="+timestamp;
								}else{
									menuUrl=menuUrl+"&time="+timestamp;
								}
								if(data.partnerMenuList[i].subMenuList[k].parameter != undefined){
									$(".menuList").eq(i).find(".nav-sidebar li a").eq(k).attr({"href":menuUrl,"target":data.partnerMenuList[i].subMenuList[k].parameter});
								} else{
									$(".menuList").eq(i).find(".nav-sidebar li a").eq(k).attr("href",menuUrl);
								}
								var urlHref=window.location.href;									
								if((window.location.pathname.split('/')[1]).split('.')[0]=="activity"){
									if(window.location.href.split("?")[1]&&window.location.href.split("?")[1].split('&')[window.location.href.split("?")[1].split('&').length-1].split('=')[0]=="page"){
										url=urlHref.split('?')[1].split('&');
										urlHref='';
										var urlLen=url.length;
										if(urlLen>=3){
											for(var u=0;u<urlLen-2;u++){
												urlHref+=url[u]+'&';
											}
											urlHref+=url[urlLen-2];
										}								
									}
									var search=urlHref.split('?')[1].split('&');
									var searchStr='';
									var searchLen=search.length
									if(search[search.length-1].split('=')[0]=="time"){
										searchLen--;
									}
									for(var s=0;s<searchLen-1;s++){
										searchStr+=search[s]+'&';
									}
									searchStr+=search[searchLen-1];
									if(data.partnerMenuList[i].subMenuList[k].menuUrl.split('?')[1]==searchStr){
										$(".menuList").eq(i).find(".nav-sidebar li").eq(k).addClass("active");
										$(".menuList").eq(i).find(".nav-sidebar").show('fast');
										$(".menuList").eq(i).children(".menuTitle").find("i").removeClass("glyphicon-folder-close").addClass("glyphicon-folder-open")
									}   
								}else{
									if(data.partnerMenuList[i].subMenuList[k].menuUrl.indexOf('#') == 0){
										$(".menuList").eq(i).find(".nav-sidebar li").eq(k).addClass("active");
										$(".menuList").eq(i).find(".nav-sidebar").show('fast');
										$(".menuList").eq(i).children(".menuTitle").find("i").removeClass("glyphicon-folder-close").addClass("glyphicon-folder-open")
									} else {
										if(((data.partnerMenuList[i].subMenuList[k].menuUrl.split('/')[1]).split('.')[0])==(window.location.pathname.split('.')[0]).split('/')[1]){
											$(".menuList").eq(i).find(".nav-sidebar li").eq(k).addClass("active");
											$(".menuList").eq(i).find(".nav-sidebar").show('fast');
											$(".menuList").eq(i).children(".menuTitle").find("i").removeClass("glyphicon-folder-close").addClass("glyphicon-folder-open")
										}
									}
									
								}
							}														
						}						
					}					 
				}			
				
				$('.parent_li').on('click', function (e) {
					$(this).find('.righticon').toggleClass('down');
					if($(this).find('.righticon').hasClass('down')){
						$(this).parent('li').addClass('activeli');
					}else{
						$(this).parent('li').removeClass('activeli');
					}
					var children = $(this).parent().find(' > ul');
					if (children.is(":visible")) {
                        children.hide('fast');
                        $(this).attr('title', '展开此级');
						
						
					} else {
						children.show('fast');
						$(this).attr('title', '收缩此级');
						
					}
					e.stopPropagation();
				});



                function getIcon(j,k,c){
                    var objIcon = {
                        "staple"     	 : "asterisk",
                        "home"       	 : "home",
                        "bigdata"   	 : "refresh",
                        "analysis"       : "sort",
                        "stat" 		 	 : "stats",
                        "money" 	 	 : "usd",
                        "user" 	 	 	 : "user",
                        "frontUser"  	 : "repeat",
                        "frontUserStat"  : "signal",
                        "weixinButton"   : "envelope",
                        "partner"	     : "lock",
                        "channel" 	     : "retweet",
                        "shareConfig"    : "circle-arrow-up",
                        "site" 		 	 : "list-alt",
                        "node" 		 	 : "list",
                        "template" 	 	 : "th-large",
                        "document" 	 	 : "book",
                        "system" 	 	 : "hdd",
						"cache" 	 	 : "download-alt",
                        "security"       : "asterisk",
                        "config" 		 : "cog",
                        "workflow" 		 : "tasks",
                        "role" 			 : "tower",
                        "pay_method" 	 : "random",
                        "transaction" 	 : "transfer",
                        "product" 		 : "star",
                        "order" 		 : "file",
                        "coupon" 		 : "gift",
                        "entity_shop" 	 : "map-marker",
                        "address_book" 	 : "edit",
                        "message" 		 : "comment",
                        "comment" 		 : "pencil",
                        "operation" 	 : "magnet",
                        "activity" 		 : "glass",
						"finance" 		 : "usd",
						"bank_account"   : "lock",
						"withdraw" 		 : "pushpin",
						"game"			 : "tower",
						"server"         : "cloud" ,
						"theme"          : "tag",
						"exchange"       : "equalizer"
                    };
                    var iconLength=arguments.length;
                    function hello(){

                        if($(".menuList").eq(j).find("i")[0]!=undefined&&iconLength==2){
                            if(objIcon[k]==undefined){
                                $(".menuList").eq(j).find("i")[0].className="glyphicon glyphicon-folder-close";
							}else{
                                $(".menuList").eq(j).find("i")[0].className="glyphicon glyphicon-"+objIcon[k];
                            }
                        } else if($(".menuList").eq(j).find(".nav-sidebar li i")[k]!=undefined&&iconLength==3){
                            if(objIcon[c]==undefined){
                                $(".menuList").eq(j).find(".nav-sidebar li i")[k].className="glyphicon glyphicon-leaf";
							}else{
                                $(".menuList").eq(j).find(".nav-sidebar li i")[k].className= "glyphicon glyphicon-"+objIcon[c];
                            }
                        }



                    }
                    var t1 = window.setTimeout(hello,100);

                }





menuaddicon();

			}
		}); 

		
		
	</script>
<script>
	function menuaddicon(){
	$('ul.side li.menuList').each(function(){
		var index = $(this).index();
		var navsL = $(this).find($(".nav-sidebar"));
		console.log(navsL.length);
		if(navsL.length>0){
			if(navsL.css('display')=='block'){
				$(this).find('.menuTitle').append('<span class="righticon glyphicon glyphicon-menu-right down"></span>');
				$(this).addClass('activeli');
			}else{
				$(this).find('.menuTitle').append('<span class="righticon glyphicon glyphicon-menu-right "></span>');
				$(this).removeClass('activeli');
			}
		}

	})
	};
	
</script>
	
	
<div class="col-sm-3 col-md-2 tree sidebar scrollbar">
	<p style="line-height: 51px;background-color: rgb(43,57,74);text-align: left;color: #FFF; font-size: 17px;font-weight:bold;padding-left:16px;">
		<a href="/" style="color:#fff;" class="webname"><span class="glyphicon glyphicon-list" style="margin-right:5px;"></span>管理菜单</a>
	</p>
	<ul class="side" style="width:100%;padding:16px;">
	
	</ul>
</div>

