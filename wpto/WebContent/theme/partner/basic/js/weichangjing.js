$(function (argument) {
	function sliderul(){
							var leftli=$(".leftBox .tab ul li").outerWidth();
							var erleftli = $(".leftBox .tabCont ul li").outerWidth();
							$(".leftBox .tab ul").width(leftli*$(".leftBox .tab ul li").length);
							$(".leftBox .tabCont ul").width(erleftli*$(".leftBox .tab ul li").length);
							$(".leftBox .tabCont ul").each(function(){
								var tabContentul = $(this).find('li').length*$(this).find('li').outerWidth();
								$(this).width(tabContentul);
							})
							var onejl = $('.tab .tabbox').width();
							
							console.log(leftli);

							$('.control .left').on('click',function(){
								var $ulthis = $(this).parent().next('.tabbox').find('ul');
								var ulleft = $(this).parent().next('.tabbox').find('ul').position().left;
								var ulwidth = $ulthis.width();
								var yus1 = ulwidth / onejl;
								var num =  Math.floor(yus1);
								if (ulleft==0) {
									ulleft=-(num*onejl);
								}else{
									ulleft=ulleft+onejl;
									console.log(ulleft)
								}
								$ulthis.css('left',ulleft)
							});

							$('.control .right').on('click',function(){
								var $ulthis = $(this).parent().next('.tabbox').find('ul');
								var ulleft = $(this).parent().next('.tabbox').find('ul').position().left;
								var ulwidth = $ulthis.width();
								var yus1 = ulwidth / onejl;
								var num =  Math.floor(yus1);
								if (Math.round(ulleft)==-(num*onejl)) {
									ulleft=0
								}else{
									ulleft=ulleft-onejl;
									console.log(ulleft)
								}
								$ulthis.css('left',ulleft)
							});
						};
})