// JavaScript Document
        var a;
		function getText(e){
			$.ajax({ 
				type: "GET", 	
				dataType: "json",
				url:"/content/collection/index.json?tags="+$("#provinceName"+e).val(), 
				success: function(data){	
				    $('#product'+e).html("");
					$('#smallImage'+e).html("");
					$(data.relationTagSet).each(function(i,m) {						
						$('#product'+e).append("<li><a style='color:#000' href='"+m.messageId+"'>"+m.tagName+"</a></li>");
					});
					$(data.newsList).each(function(i,m) {	
												console.log(i)
						$(m.documentDataMap).each(function(i,n) {  
							
							$('#smallImage'+e).append("<li><a href='"+m.viewUrl+"'><img src='"+n.productSmallImage.dataValue+"' /></a></li>");
							
							
						});							
												
					}); 
					//displayMarquee($('#smallImage'+e));
				},
				error: function(data) {
					alert("系统繁忙");
				}, 
				
				
			}); 

		}
		function scroll(obj) {
			 if(obj[0]){
			 	var tmp = (obj.scrollLeft)++;
			//当滚动条到达右边顶端时
			if (obj.scrollLeft==tmp) obj.innerHTML += obj.innerHTML;
			//当滚动条滚动了初始内容的宽度时滚动条回到最左端
			if (obj.scrollLeft>=obj[0].firstChild.offsetWidth) obj.scrollLeft=0;
			 }
		}
		function _scroll(_obj){
       			return function(){
             scroll(_obj);
       		}
		}
	
		
        function displayMarquee(obj){
             obj.before("<MARQUEE behavior='scroll' contenteditable='true' onstart='this.firstChild.innerHTML+=this.firstChild.innerHTML;' scrollamount='3' width='100'><SPAN unselectable='on'></SPAN></MARQUEE>")
             a =setInterval(scroll(obj),20);
        }
        $(".imgList").mouseover(function(){
            clearInterval(a);
        })
        $(".imgList").mouseout(function(){
        	  var obj=this;
              a =	setInterval("scroll(obj)",20);
        })

		
		