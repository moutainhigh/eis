$(document).ready(function () {

    var urlData = [{ "url": "Index.aspx", "id": "index", },
                    { "url": "ProductList.aspx?categoryID=AAWXSCCPGM", "id": "dnsm", },
                    { "url": "ProductList.aspx?categoryID=AAWXSCJYDQ", "id": "jydq" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCYDHW", "id": "ydhw" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCFSXB", "id": "xbfs" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCGHHZ", "id": "ghhz" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCMYYP", "id": "myyp" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCRYBH", "id": "rybh" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCSPBJ", "id": "spbj" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCLPZB", "id": "jpzb" },
                    { "url": "ProductList.aspx?categoryID=AAWXSCTSYX", "id": "tsyx" }
    ];


    var curUrl = document.location.href;

    $.each(urlData, function (index, data) {
        if (curUrl.toLowerCase().indexOf(data.url.toLowerCase()) != -1) {
            $(".Header li#" + data.id).addClass("Cur").siblings().removeClass("Cur");
            return false;
        }
    });
})

    	$(function(){
		var num=0;
		var len=$('#ban_show').find('li').length;
		var timer="";
		
	$('#bn_xbtn').find('li').hover(function(){
		
			//alert($(this).index());
			//return ;
			//var $this=$(this);
			var $index=$(this).index();
			num=$index;

			$(this).addClass('curr').siblings().removeClass('curr');
			$("#ban_show").find("li").eq($index).addClass("active").siblings().removeClass("active");
		});

	$('.next').click(function(){
			num++;
			if(num>=len){
				num=0;
			}
			main(num);
		});

	$('.pre').click(function(){
			num--;
			if(num<0){
				num=len-1;
			}
			main(num);
		});

	function main(num){
			$('#bn_xbtn').find('li').eq(num).addClass('curr').siblings().removeClass('curr');
			$("#ban_show").find("li").eq(num).addClass("active").siblings().removeClass("active");

			
		};
		
	function autoPlay(){
		timer=setInterval(function(){
			num++;
			if(num>=len){
				num=0;
			}
			main(num);
		},2000);
		}
	autoPlay();

		function clear(obj){
			obj.mouseover(function(){
				clearInterval(timer)
			}).mouseout(function(){
				autoPlay();
			});
		};

		clear($('.next'));
		clear($('.pre'));
		//clear($('#ban_show'));
		clear($('#bn_xbtn'));
	});

