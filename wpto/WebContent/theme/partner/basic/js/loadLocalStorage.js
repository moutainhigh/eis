function loadLocalStorage(a,k,c,d,f,g){
			var phoneHtml='';
			if(a.scene==""){
				$(".editArea").empty().append('<div class="phoneContBg" style="position:absolute;width:100%;height:100%;z-index:1;opacity:0;"></div>').append(phoneHtml);
				$(".editArea").css("background","");
				$(".bgToggle").hide();
				$(".musicToggle").hide();
				$(".styleModal").hide();
				$(".animModal").hide();
				$("#audio_btn").hide();				
				$(".animModal").empty();
				if($(".pagin")){
					$(".pagin").empty();
				}
				return false;
			}else{
				$(".editArea").empty().append('<div class="phoneContBg" style="position:absolute;width:100%;height:100%;z-index:1;opacity:0;"></div>').append(phoneHtml);
				$(".editArea").css("background","");
				$(".bgToggle").hide();
				$(".musicToggle").hide();				
				$(".styleModal").hide();
				$(".animModal").hide();
				$("#audio_btn").hide();
				$(".animModal").empty();
				var sceneData=JSON.parse(a.scene);
				var paginationHtml='';
				var m=sceneData.pageLen;
				for(var i=0;i<m;i++){
					var page=i+1;
					paginationHtml+='<li>第'+page+'页</li>';
				}
				$(".pagin").empty().append(paginationHtml);				
				if(sceneData.index!="-1"){
					$(".pagin li").removeClass("currentPage");
					$(".pagin li").eq(sceneData.index).addClass("currentPage");
				}
			}			
			if(sceneData.materialList!=undefined){
					var b=sceneData.materialList.length;
					for(l=0;l<b;l++){
						var url="";
						phoneHtml+=addContByType(sceneData.materialList[l].materialType,url);
						if(sceneData.materialList[l].materialType=="background-image"){
							var tim = sceneData.background.split('-')[0];
							$(".editArea").css("background","url(/static/open/"+tim+'/'+sceneData.background+") top center no-repeat");
							$(".editArea").css("background",url+" top center 99.5% 100% no-repeat");
							$(".editArea").css("backgroundSize","99.5% 100%");
							$(".bgToggle").show();
						}
					}
					$(".editArea").empty().append('<div class="phoneContBg" style="position:absolute;width:100%;height:100%;z-index:1;opacity:0;"></div>').append(phoneHtml);
					for(l=0;l<b;l++){
						if(sceneData.materialList[l].materialType=="image"){
							$(".editArea").children("li").eq(l).find(".elementBox").attr("src",sceneData.materialList[l].content);
							$("#nr .editWrapper .editArea").children("li").eq(l).attr("style",sceneData.materialList[l].css);
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").attr("style",sceneData.materialList[l].cssExt);
							$(".editArea").children("li").eq(l).find(".materialId").html(sceneData.materialList[l].materialModelId);
							$("#nr .editWrapper .editArea").children("li").eq(l).find("img").css("width","100%").css("height","100%");
						}else if(sceneData.materialList[l].materialType=="music"){
							$(".editArea").children("li").eq(l).find("audio").attr("src",sceneData.materialList[l].content);
							$(".editArea").children("li").eq(l).find(".materialId").html(sceneData.materialList[l].materialModelId);
							$(".musicToggle").show();
							$("#audio_btn").show();
							if($(".editArea").children("li").eq(l).find("audio").attr("autoplay")=="autoplay"){
								$("#audio_btn").removeClass("off").addClass("play_yinfu");
								$("#yinfu").addClass("rotateMusic");								 
							}
						}else if(sceneData.materialList[l].materialType=="video"){
							$(".editArea").children("li").eq(l).find("source").attr("src",sceneData.materialList[l].content);
							$(".editArea").children("li").eq(l).find(".materialId").html(sceneData.materialList[l].materialModelId);
						}else if(sceneData.materialList[l].materialType=="collection"){
							var m=sceneData.materialList[l].content.split(',').length;
							var collection='';
							for(var i=0;i<m;i++){
								collection+='<li style="position:relative;"><a href=""><img src=""  data-description="" /></a></li>';
							}
							$(".editArea").children("li").eq(l).find(".html5zoo-slides").append(collection);
							$("#nr .editWrapper .editArea").children("li").eq(l).attr("style",sceneData.materialList[l].css);
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").attr("style",sceneData.materialList[l].cssExt);
							$(".editArea").children("li").eq(l).find(".materialId").html(sceneData.materialList[l].materialModelId);
							$(".editArea").children("li").eq(l).append('');	
							for(var i=0;i<m;i++){
								$(".editArea").children("li").eq(l).find(".html5zoo-slides li").eq(i).find("img").attr("src",sceneData.materialList[l].content.split(',')[i]);
							}
							variousSlide($(".editArea").children("li").eq(l).attr("id"));
						}else if(sceneData.materialList[l].materialType=="background-image"){
							$(".editArea").children("li").eq(l).find(".materialId").html(sceneData.materialList[l].materialModelId);
						}else if(sceneData.materialList[l].materialType=="text"){
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").html(sceneData.materialList[l].content);
							$("#nr .editWrapper .editArea").children("li").eq(l).attr("style",sceneData.materialList[l].css);
							
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").attr("style",sceneData.materialList[l].cssExt);
							var h=$("#nr .editWrapper .editArea").children("li").eq(l).height();
							var w=$("#nr .editWrapper .editArea").children("li").eq(l).width();
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").css({"width":w,"height":h})
						}else if(sceneData.materialList[l].materialType=="button"){
							$("#nr .editWrapper .editArea").children("li").eq(l).find("button").html(sceneData.materialList[l].content);
							$("#nr .editWrapper .editArea").children("li").eq(l).attr("style",sceneData.materialList[l].css);
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").attr("style",sceneData.materialList[l].cssExt);
							var h=$("#nr .editWrapper .editArea").children("li").eq(l).height();
							var w=$("#nr .editWrapper .editArea").children("li").eq(l).width();
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").css({"width":w,"height":h})
						}else if(sceneData.materialList[l].materialType=="input"){
							//alert(sceneData.materialList[l].content);
							var content=JSON.parse(sceneData.materialList[l].content);
							$("#nr .editWrapper .editArea").children("li").eq(l).find("input").attr("type",content.type);
							$("#nr .editWrapper .editArea").children("li").eq(l).find("input").attr("placeholder",content.placeholder);
							$("#nr .editWrapper .editArea").children("li").eq(l).attr("style",sceneData.materialList[l].css);
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").attr("style",sceneData.materialList[l].cssExt);
							var h=$("#nr .editWrapper .editArea").children("li").eq(l).height();
							var w=$("#nr .editWrapper .editArea").children("li").eq(l).width();
							$("#nr .editWrapper .editArea").children("li").eq(l).find(".elementBox").css({"width":w,"height":h})
						}else{
							
						}
					}
				}
				
				if(sceneData.materialList!=undefined){
					var b=sceneData.materialList.length;
					for(var l=0;l<b;l++){
						//$("#nr .editWrapper .editArea").children("li").eq(l).css({"width":80+'%',"height":50,"left":10+'%',"top":5+'%',"textAlign":"center",});
						//$("#nr .editWrapper .editArea").children("li").eq(l).css({"display":"table-cell","vertical-align":"middle"});
						rotate($("#nr .editWrapper .editArea").children("li").eq(l).find(".bar-rotate"));
						draggable($("#nr .editWrapper .editArea").children("li").eq(l).find(".draggable").get(0));
						resizeW($("#nr .editWrapper .editArea").children("li").eq(l).find(".bar-w").children(".bar-radius"));
						resizeE($("#nr .editWrapper .editArea").children("li").eq(l).find(".bar-e").children(".bar-radius"));
						resizeN($("#nr .editWrapper .editArea").children("li").eq(l).find(".bar-n").children(".bar-radius"));						
						resizeS($("#nr .editWrapper .editArea").children("li").eq(l).find(".bar-s").children(".bar-radius"));						
						var animHtml=writeAnimModal();
						//alert(styleHtml);
						$(".animModal").append(animHtml);
						if(sceneData.materialList[l].animation!=undefined){
							c=JSON.parse(sceneData.materialList[l].animation).length;
						}
						if(c>0){
							var animTabHtml='';
							for(var j=0;j<c;j++){
								var num=j+1;
								animTabHtml+='<li>动画-'+num+'</li>';
							}
							$(".animModal .animModalDrag").eq(l).find(".animTab").empty().append(animTabHtml);
							var animContHtml='';
							for(var j=0;j<c;j++){
								var animation=JSON.parse(sceneData.materialList[l].animation);
								animContHtml+='<li>';
								animContHtml+='<form action="" style="margin:auto 0;">';				
								animContHtml+='<div class="form-item"><label for="time">动画持续:</label><input type="text" class="time" name="time" value="'+animation[j].animationDuration+'" placeholder="请输入动画时间"/></div>';
								animContHtml+='<div class="form-item"><label for="delay">动画延迟:</label><input type="text" class="delay" name="delay" value="'+animation[j].animationDelay+'" placeholder="请输入延迟时间"/></div>';		
								animContHtml+='<div class="form-item"><label for="times">动画次数:</label><input type="text" class="times" name="times" value="'+animation[j].animationIterationCount+'" placeholder="请输入动画次数"/></div>';
								animContHtml+='<div class="form-item"><label for="method">是否循环:</label><label><input name="animCircle" type="radio" value="infinite" />是 </label> <label><input name="animCircle" type="radio" value=""/>否 </label></div>';
								animContHtml+='<div class="form-item"><label for="method">动画方式:</label><select class="animMethod"><option value="fadeIn">淡入 </option> <option value="fadeInRight">从右移入 </option> <option value="fadeInLeft" >从左移入 </option><option value="fadeInUp">从下移入 </option><option value="fadeInDown">从上移入 </option><option value="zoomIn">中心放大</option><option value="wobble">摇摆 </option><option value="rotateIn">旋转 </option><option value="swing">悬摆 </option><option value="flipOutY">翻转消失 </option><option value="fadeOut">淡出 </option> <option value="none">无 </option></select></div>';			
								animContHtml+='</form>';
								animContHtml+='</li>';
							}
							$(".animModal .animModalDrag").eq(l).find(".animCont").empty().append(animContHtml);
							for(var j=0;j<c;j++){								
								if(animation[j].animationIterationCount=="infinite"){
									$(".animModal .animModalDrag").eq(l).find(".animCont li").eq(j).find("input[name=animCircle]").eq(0).get(0).checked=true;
								}else{
									$(".animModal .animModalDrag").eq(l).find(".animCont li").eq(j).find("input[name=animCircle]").eq(1).get(0).checked=true;
								}	  
								var d=$(".animModal .animModalDrag").eq(l).find(".animCont li").eq(j).find(".animMethod option").length;
								if(animation[j].animationName!=undefined){
									for(var md=0;md<d;md++){
										if($(".animModal .animModalDrag").eq(l).find(".animCont").eq(j).find(".animMethod option").eq(md).val()==animation[j].animationName){
											$(".animModal .animModalDrag").eq(l).find(".animCont").eq(j).find(".animMethod option").eq(md)[0].selected=true;
										}
									} 
								}
							}
						}
						draggable($(".animModal .modalHeader"));
					}
									
				}	
			
			if($("#nr .editWrapper .editArea").children("li").length>0){
				var toolbarHtml=writeToolbar();
				var colorPicker;
				var btnToggle=false;
				$(".btn-toolbar").empty().append(toolbarHtml);				
				$(".btn-toolbar").css("position", "absolute");
				$(".btn-toolbar").css("z-index", "99");
				$("body").on("click",".foreColorBtn", function(){								
					$(".dropdown-menu-foreColor").css("display","none");
					$("#picker3").css("display","block");
					$("#picker3").css("top","160px").css("marginLeft",0).css("left",0);
					$("#picker3").css("border","#fff 1px solid");
					$('#picker3').farbtastic(function(data){colorPicker=data;btnToggle=true;document.execCommand("foreColor", 0, colorPicker)})					
				})
				$(".dropdown-toggle-foreColor").click(function(){
					if($(".dropdown-menu-foreColor").css("display")!="none"){
						$(".dropdown-menu-foreColor").css("display","none");
					}else{
						$(".dropdown-menu-foreColor").css("display","block");
					}	
					$("#picker4").css("display","none");
				})
				$("body").on("click",".imgClose",function(){
					$(this).parent().css("display","none");	
				})
				
				$("body").on("click",".backColorBtn", function(){								
					$(".dropdown-menu-backColor").css("display","none");
					$("#picker4").css("display","block");
					$("#picker4").css("top","160px").css("marginLeft",0).css("left",20);
					$("#picker4").css("border","#fff 1px solid");
					$('#picker4').farbtastic(function(data){btnToggle=true;document.execCommand("backColor", 0, data)})					
				})
				$(".dropdown-toggle-backColor").click(function(){
					if($(".dropdown-menu-backColor").css("display")!="none"){
						$(".dropdown-menu-backColor").css("display","none");
					}else{
						$(".dropdown-menu-backColor").css("display","block");
					}	
					$("#picker3").css("display","none");
				})
				$(".btn-group a").click(function(){
					$(this).parent().siblings().find("ul").css("display","none");
				})
				$(".dropdown-toggle-foreSize").click(function(){
					$(this).siblings().toggle();
				})
				var popMenuHtml=writePopMenu();
				$("#popMenu").empty().append(popMenuHtml);				
				$("#popMenu").css("position", "absolute");
				$("#popMenu").css("z-index", "99");
				var styleHtml=writeStyleModal();
				//alert(styleHtml);
				$(".styleModal").empty().append(styleHtml);
				draggable($(".styleModal .modalHeader").get(0));
			}					
			
			$(".style").click(function(){
				var index=$(this).parent().find(".popIndex").html();
				var cssData={};
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth")))){
					borderWidth='';
				}else{
					borderWidth=parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth"));
				}
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius")))){
					borderRadius='';
				}else{
					borderRadius=parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius"));
				}
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing")))){
					letterSpacing="0px";
				}else{
					letterSpacing=$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing");
				}
				cssData={
					borderWidth: borderWidth,
					borderStyle: $(".editArea").children("li").eq(index).find(".elementBox").css("borderTopStyle"),
					borderColor: $(".editArea").children("li").eq(index).find(".elementBox").css("borderTopColor"),
					borderRadius: borderRadius,
					backgroundColor: $(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor"),
					opacity: $(".editArea").children("li").eq(index).find(".elementBox").css("opacity"),
					fontSize:$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize"),
					lineHeight:$(".editArea").children("li").eq(index).find(".elementBox")[0].style.lineHeight,
					letterSpacing:letterSpacing,
				}
				$(".styleModal").find("#opacity").val(cssData.opacity);
				$(".styleModal").find("#borderSize").val(cssData.borderWidth);
				$(".styleModal").find("#borderRadius").val(cssData.borderRadius);
				$(".styleModal").find("#borderColor").val(cssData.borderColor);
				$(".styleModal").find("#color").val(cssData.backgroundColor);
				$(".styleModal").find("#color").css("backgroundColor",cssData.backgroundColor);
				$(".styleModal").find("#borderColor").css("backgroundColor",cssData.borderColor);
				$(".styleModal").find("#fontSize").val(cssData.fontSize);
				$(".styleModal").find("#lineHeight").val(cssData.lineHeight);
				$(".styleModal").find("#letterSpacing").val(cssData.letterSpacing);
				var typeNum=$(".styleModal").find("input[name='borderStyle']").length;
				for(var i=0;i<typeNum;i++){
					if($(".styleModal").find("input[name='borderStyle']").eq(i).val()==cssData.borderStyle){
						$(".styleModal").find("input[name='borderStyle']").eq(i)[0].checked=true;
					}
				}
				$(".styleModal").fadeIn(1000);				
				$(".dragIndex").html($(this).parent().find(".popIndex").html());
				$("#popMenu").hide();
				$(".animModal").hide();
				$(".bar").children(".bar-radius").css("display","none");	
				$(".bar").css("display","none");				
				$(".styleModal .submit").click(function(){
					$(".styleModal").fadeOut(1000);
					setLocalStorage();
				})
				$(".styleModal .modalFooter .cancel").click(function(){
					var index=$(".dragIndex").html();
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderWidth",cssData.borderWidth),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderStyle",cssData.borderStyle),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",cssData.borderColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderRadius",cssData.borderRadius),
					$(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",cssData.backgroundColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("opacity",cssData.opacity),
					$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize",cssData.fontSize),
					$(".editArea").children("li").eq(index).find(".elementBox").css("lineHeight",cssData.lineHeight),
					$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing",cssData.letterSpacing),
					$(".styleModal").fadeOut(1000);
				})
				$(".styleModal .close").click(function(){
					var index=$(".dragIndex").html();
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderWidth",cssData.borderWidth),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderStyle",cssData.borderStyle),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",cssData.borderColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderRadius",cssData.borderRadius),
					$(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",cssData.backgroundColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("opacity",cssData.opacity),
					$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize",cssData.fontSize),
					$(".editArea").children("li").eq(index).find(".elementBox").css("lineHeight",cssData.lineHeight),
					$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing",cssData.letterSpacing),
					$(".styleModal").fadeOut(1000);
				})
				if($('#picker').length>0){
				$('#picker').farbtastic(function(data){$("#color").val(data);$("#color").css("backgroundColor",data); $(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",data);var color=rgbToHsl(data.colorRgb().split(',')[0],data.colorRgb().split(',')[1],data.colorRgb().split(',')[2])[2] > 0.5 ? '#000' : '#fff'; $("#color").css("color",color)});}
				if($('#picker2').length>0){
				$('#picker2').farbtastic(function(data){$("#borderColor").val(data);$("#borderColor").css("backgroundColor",data); $(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",data);var color=rgbToHsl(data.colorRgb().split(',')[0],data.colorRgb().split(',')[1],data.colorRgb().split(',')[2])[2] > 0.5 ? '#000' : '#fff'; $("#borderColor").css("color",color)});}
				})
			$(".anim").click(function(){		
				var index=$(this).parent().find(".popIndex").html();
				$(".animModal").fadeIn();
				$(".animModalDrag").css("display","none");
				$(".animModalDrag").eq(index).find(".animIndex").html(index);				
				$(".animModalDrag").eq(index).fadeIn(1000);
				$("#popMenu").hide();
				$(".styleModal").hide();
				$(".bar").children(".bar-radius").css("display","none");	
				$(".bar").css("display","none");
				var animData=new Array();
				var m=$(".animModalDrag").eq(index).find(".animCont").length;
				for(var i=0;i<m;i++){
					if($(".animModalDrag").eq(index).find(".animCont").eq(i).find("input[name='animCircle']:checked").val()=="infinite"){
						animationIterationCount="infinite";
					}else{
						animationIterationCount=$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val();
					}
					animData[i]={
						animationDuration:$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".time").val(),
						animationDelay:$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".delay").val(),
						animationName:$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod").val(),
						animationIterationCount:animationIterationCount,
					}
				}
				$("body").on("click",".animModal .modalFooter .cancel",function(){
					var animTabHtml='';
					for(var j=0;j<m;j++){
						var num=j+1;
						animTabHtml+='<li>动画-'+num+'</li>';
					}
					$(".animModal .animModalDrag").eq(l).find(".animTab").empty().append(animTabHtml);
					var animContHtml='';
					for(var j=0;j<m;j++){
						animContHtml+='<li>';
						animContHtml+='<form action="" style="margin:auto 0;">';				
						animContHtml+='<div class="form-item"><label for="time">动画持续:</label><input type="text" class="time" name="time" value="" placeholder="请输入动画时间"/></div>';
						animContHtml+='<div class="form-item"><label for="delay">动画延迟:</label><input type="text" class="delay" name="delay" value="" placeholder="请输入延迟时间"/></div>';		
						animContHtml+='<div class="form-item"><label for="times">动画次数:</label><input type="text" class="times" name="times" value="" placeholder="请输入动画次数"/></div>';
						animContHtml+='<div class="form-item"><label for="method">是否循环:</label><label><input name="animCircle" type="radio" value="infinite" />是 </label> <label><input name="animCircle" type="radio" value="" />否 </label></div>';
						animContHtml+='<div class="form-item"><label for="method">动画方式:</label><select class="animMethod"><option value="fadeIn">淡入 </option> <option value="fadeInRight">从右移入 </option> <option value="fadeInLeft" >从左移入 </option><option value="fadeInUp">从下移入 </option><option value="fadeInDown">从上移入 </option><option value="zoomIn">中心放大</option><option value="wobble">摇摆 </option><option value="rotateIn">旋转 </option><option value="swing">悬摆 </option><option value="flipOutY">翻转消失 </option><option value="fadeOut">淡出 </option> <option value="none">无 </option></select></div>';	
						animContHtml+='</form>';
						animContHtml+='</li>';
					}
					$(".animModal .animModalDrag").eq(index).find(".animCont").empty().append(animContHtml);
					for(var i=0;i<m;i++){
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".time").val(animData[i].animationDuration);
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".delay").val(animData[i].animationDelay);
						var n=$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").length;
						for(var j=0;j<n;j++){
							if($(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j).val()==animData[i].animationName){
								$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j)[0].selected=true;
							}
						}
						if(animData[i].animationIterationCount=="infinite"){
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find("input[name='animCircle']").eq(0)[0].checked=true;
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val("");
						}else{
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val(animData[i].animationIterationCount);
						}
					}
					$(".animModal").fadeOut(1000);
				})
				$("body").on("click",".animModal .modalFooter .submit",function(){
					$(".animModal").fadeOut(1000);
					setLocalStorage();
				})
				$("body").on("click",".animModal .close",function(){
					var animTabHtml='';
					for(var j=0;j<m;j++){
						var num=j+1;
						animTabHtml+='<li>动画-'+num+'</li>';
					}
					$(".animModal .animModalDrag").eq(l).find(".animTab").empty().append(animTabHtml);
					var animContHtml='';
					for(var j=0;j<m;j++){
						animContHtml+='<li>';
						animContHtml+='<form action="" style="margin:auto 0;">';				
						animContHtml+='<div class="form-item"><label for="time">动画持续:</label><input type="text" class="time" name="time" value="" placeholder="请输入动画时间"/></div>';
						animContHtml+='<div class="form-item"><label for="delay">动画延迟:</label><input type="text" class="delay" name="delay" value="" placeholder="请输入延迟时间"/></div>';		
						animContHtml+='<div class="form-item"><label for="times">动画次数:</label><input type="text" class="times" name="times" value="" placeholder="请输入动画次数"/></div>';
						animContHtml+='<div class="form-item"><label for="method">是否循环:</label><label><input name="animCircle" type="radio" value="infinite" />是 </label> <label><input name="animCircle" type="radio" value=""  />否 </label></div>';
						animContHtml+='<div class="form-item"><label for="method">动画方式:</label><select class="animMethod"><option value="fadeIn">淡入 </option> <option value="fadeInRight">从右移入 </option> <option value="fadeInLeft" >从左移入 </option><option value="fadeInUp">从下移入 </option><option value="fadeInDown">从上移入 </option><option value="zoomIn">中心放大</option><option value="wobble">摇摆 </option><option value="rotateIn">旋转 </option><option value="swing">悬摆 </option><option value="flipOutY">翻转消失 </option><option value="fadeOut">淡出 </option> <option value="none">无 </option></select></div>';		
						animContHtml+='</form>';
						animContHtml+='</li>';
					}
					$(".animModal .animModalDrag").eq(index).find(".animCont").empty().append(animContHtml);
					for(var i=0;i<m;i++){
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".time").val(animData[i].animationDuration);
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".delay").val(animData[i].animationDelay);
						var n=$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").length;
						for(var j=0;j<n;j++){
							if($(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j).val()==animData[i].animationName){
								$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j)[0].selected=true;
							}
						}
						if(animData[i].animationIterationCount=="infinite"){
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find("input[name='animCircle']").eq(0)[0].checked=true;
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val("");
						}else{
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val(animData[i].animationIterationCount);
						}
					}
					$(".animModal").fadeOut(1000);
				})
			})
			var copyLength;
			$(".copy").click(function(){
				var index=$(this).parent().find(".popIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).clone(false).appendTo("#nr .editWrapper .editArea");
				$("#popMenu").hide();
				$("#fontFamily").val("simHei");
				$(".animModalDrag").eq(index).clone(false).appendTo(".animModal");	
				var select=$(".animModalDrag").eq(index).find(".animMethod")[0];
				draggable($(".animModal .modalHeader"));
				copyLength=$("#nr .editWrapper .editArea").children("li").length;
				var top=parseInt($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).css("top"));
				var height=parseInt($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).height())
				$("#nr .editWrapper .editArea").children("li").eq(copyLength-1).css("top",top+height+5);
				$(".animModalDrag").eq(copyLength-1).find(".animMethod")[0].selectedIndex = select.selectedIndex;
				$(".animModalDrag").eq(copyLength-1).find(".animMethod")[0].value = select.value;	
				rotate($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).find(".bar-rotate"));
				$(".animIndex").eq(copyLength-1).html(copyLength-1);
				draggable($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).children(".draggable").get(0));
				resizeW($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).find(".bar-w").children(".bar-radius"));
				resizeE($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).find(".bar-e").children(".bar-radius"));
				resizeN($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).find(".bar-n").children(".bar-radius"));						
				resizeS($("#nr .editWrapper .editArea").children("li").eq(copyLength-1).find(".bar-s").children(".bar-radius"));				
				setLocalStorage();
				$(".styleModal").hide();
				$(".animModal").hide();
			})
			$(".delete").click(function(){
				var index=$(this).parent().find(".popIndex").html();
				swal({ 
						title: "您确定要删除选中元素么？",  
						showCancelButton: true, 
						cancelButtonText:"取消",
						closeOnConfirm: true, 
						confirmButtonText: "确定", 
						confirmButtonColor: "#428bca" ,
					}, function() {$("#nr .editWrapper .editArea").children("li").eq(index).remove(),$(".animModalDrag").eq(index).remove(),$("#popMenu").hide();setLocalStorage();})
				$("#popMenu").hide();
				
			})			
			
			$("#color").on("input",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("background",$('#color').val());
				$("#color").css("background",$('#color').val());
			})
			$("#borderColor").on("input",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("borderColor",$('#borderColor').val());
				$("#borderColor").css("background",$('#borderColor').val());
			})
			$("#fontSize").on("change",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("fontSize",$('#fontSize').val());
			})
			$("#lineHeight").on("change",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("lineHeight",$('#lineHeight').val());
			})
			$("#letterSpacing").on("change",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox")[0].style.letterSpacing=$('#letterSpacing').val();
			})
			$("input[name='borderStyle']").click(function(){
				var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("borderStyle",$(this).val());
				var h=parseInt($("#nr .editWrapper .editArea").children("li").eq(index).height());
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("height",h);
			})
			$(".animMethod").on("change",function(){
				var index=$(this).parents(".animModalDrag").index();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("animation",""),$("#nr .editWrapper .editArea").children("li").eq(index).get(0).offsetWidth = $("#nr .editWrapper .editArea").children("li").eq(index).get(0).offsetWidth,$("#nr .editWrapper .editArea").children("li").eq(index).css("animation", $(this).val() + " " + $(this).parent().siblings().find(".time").val() + "s ease " + $(this).parent().siblings().find(".delay").val() + "s " + ($(this).parent().siblings().find(".times").val() ? $(this).parent().siblings().find(".times").val() : ""));
				if($(this).parent().siblings().find("input[name='animCircle']:checked").val()=="infinite"){
				$("#nr .editWrapper .editArea").children("li").eq(index).css("animation-iteration-count", "infinite");}
			})
			$("body").on("click",".seeAnim",function(){
				var index=$(this).parents(".animModalDrag").index();
				var m=$(this).siblings(".animCont").children("li").length;
				//alert(m);
				var a=$(this).siblings(".animCont").children("li");
				function preview(i){
					if(i<m){
							b=a.eq(i).children().children("div");
							//alert(b.html());
							$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("animation",""),$("#nr .editWrapper .editArea").children("li").eq(index).get(0).offsetWidth = $("#nr .editWrapper .editArea").children("li").eq(index).get(0).offsetWidth,$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("animation",b.eq(4).find(".animMethod").val() + " " + b.eq(0).find(".time").val() + "s ease " + b.eq(1).find(".delay").val() + "s "+  b.eq(2).find(".times").val()).css("animation-fill-mode", "backwards");
							if(b.eq(3).find("input[name='animCircle']:checked").val()=="infinite"){
							$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("animation-iteration-count", "infinite");};
							$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend", function(){ //动画结束时事件 
								i++,preview(i);
							});						
					}	
				}
				preview(0);
			}) 
			$(".addAnim").click(function(){
				$(this).siblings(".animTab").find("li").removeClass("current");
				$(this).siblings(".animCont").find("li").hide();
				var index=$(this).siblings(".animTab").find("li").length+1;
				$(this).siblings(".animTab").append('<li class="current">动画-'+index+'</li>');
				var animCont='<li>';
				animCont+='<form action="" style="margin:auto 0;">';				
				animCont+='<div class="form-item"><label for="time">动画持续:</label><input type="text" class="time" name="time" value="" placeholder="请输入动画时间"/></div>';
				animCont+='<div class="form-item"><label for="delay">动画延迟:</label><input type="text" class="delay" name="delay" value="" placeholder="请输入延迟时间"/></div>';		
				animCont+='<div class="form-item"><label for="times">动画次数:</label><input type="text" class="times" name="times" value="" placeholder="请输入动画次数"/></div>';
				animCont+='<div class="form-item"><label for="method">是否循环:</label><label><input name="animCircle" type="radio" value="infinite" />是 </label> <label><input name="animCircle" type="radio" value=""/>否 </label></div>';
				animCont+='<div class="form-item"><label for="method">动画方式:</label><select class="animMethod"><option value="fadeIn">淡入 </option> <option value="fadeInRight">从右移入 </option> <option value="fadeInLeft" >从左移入 </option><option value="fadeInUp">从下移入 </option><option value="fadeInDown">从上移入 </option><option value="zoomIn">中心放大</option><option value="wobble">摇摆 </option><option value="rotateIn">旋转 </option><option value="swing">悬摆 </option><option value="flipOutY">翻转消失 </option><option value="fadeOut">淡出 </option> <option value="none">无 </option></select></div>';			
				animCont+='</form>';
				animCont+='</li>';
				$(this).siblings(".animCont").append(animCont);				
			})
			$("body").on("click",".animTab li",function(){
				//alert(1);				
				$(".animTab li").removeClass("current");
				$(".animCont li").css("display","none");				
				//$(this).offsetWidth=$(this).offsetWidth;
				var m=$(this).parent().parent().siblings(".animIndex").html();
				var index=$(".animModalDrag").eq(m).find(".modalBody .animTab li").index(this);
				//alert(index);
				//alert(index);
				$(this).addClass("current");
				$(".animModalDrag").eq(m).find(".modalBody .animCont li").eq(index).css("display","block");
			})
			$(".bgColor").click(function(){
				$("#picker").toggleClass("block");
				$("#picker2").removeClass("block");
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("background",$('#color').val());				
			});
			$(".borderColorBtn").click(function(){
				$("#picker").removeClass("block");
				$("#picker2").toggleClass("block");				
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("borderColor",$('#borderColor').val());	
			});
			$("#borderSize").on("input",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("borderWidth",$('#borderSize').val());	
				var h=parseInt($("#nr .editWrapper .editArea").children("li").eq(index).height());
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("height",h);
			})
			$("#borderRadius").on("input",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("borderRadius" ,$('#borderRadius').val()+'px');
				var h=parseInt($("#nr .editWrapper .editArea").children("li").eq(index).height());
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("height",h);
			})
			$("#opacity").on("input",function(){
				 var index=$(".dragIndex").html();
				$("#nr .editWrapper .editArea").children("li").eq(index).find(".elementBox").css("opacity",$('#opacity').val());				
			})
			$("#nr .editWrapper .editArea").children("li").resize(function(){
				if(window.getSelection){
					window.getSelection().removeAllRanges(); //w3c
				}else  if(document.selection){
					document.selection.empty();//IE
				}				
			})			
			$("body").on("click","#nr .editWrapper .editArea li",function(){
				$(".bar").css("display","none");
				$(".bar").children(".bar-radius").css("display","none");
				$(this).find(".bar").css("display","block");
				$(this).find(".bar").children(".bar-radius").css("display","block");
				$("#popMenu").css("display","none");
				var index=$("#nr .editWrapper .editArea li").index($(this));
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth")))){
					borderWidth='';
				}else{
					borderWidth=parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth"));
				}
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius")))){
					borderRadius='';
				}else{
					borderRadius=parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius"));
				}
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing")))){
					letterSpacing="0px";
				}else{
					letterSpacing=$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing");
				}
				cssData={
					borderWidth: borderWidth,
					borderStyle: $(".editArea").children("li").eq(index).find(".elementBox").css("borderTopStyle"),
					borderColor: $(".editArea").children("li").eq(index).find(".elementBox").css("borderTopColor"),
					borderRadius: borderRadius,
					backgroundColor: $(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor"),
					opacity: $(".editArea").children("li").eq(index).find(".elementBox").css("opacity"),
					fontSize:$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize"),
					lineHeight:$(".editArea").children("li").eq(index).find(".elementBox")[0].style.lineHeight,
					letterSpacing:letterSpacing,
				}
				$(".styleModal").find("#opacity").val(cssData.opacity);
				$(".styleModal").find("#borderSize").val(cssData.borderWidth);
				$(".styleModal").find("#borderRadius").val(cssData.borderRadius);
				$(".styleModal").find("#borderColor").val(cssData.borderColor);
				$(".styleModal").find("#color").val(cssData.backgroundColor);
				$(".styleModal").find("#color").css("backgroundColor",cssData.backgroundColor);
				$(".styleModal").find("#borderColor").css("backgroundColor",cssData.borderColor);
				$(".styleModal").find("#fontSize").val(cssData.fontSize);
				$(".styleModal").find("#lineHeight").val(cssData.lineHeight);
				$(".styleModal").find("#letterSpacing").val(cssData.letterSpacing);
				var typeNum=$(".styleModal").find("input[name='borderStyle']").length;
				for(var i=0;i<typeNum;i++){
					if($(".styleModal").find("input[name='borderStyle']").eq(i).val()==cssData.borderStyle){
						$(".styleModal").find("input[name='borderStyle']").eq(i)[0].checked=true;
					}
				}
				if($('#picker').length>0){
				$('#picker').farbtastic(function(data){$("#color").val(data);$("#color").css("backgroundColor",data); var index=$(".dragIndex").html();$(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",data);var color=rgbToHsl(data.colorRgb().split(',')[0],data.colorRgb().split(',')[1],data.colorRgb().split(',')[2])[2] > 0.5 ? '#000' : '#fff'; $("#color").css("color",color)});}
				if($('#picker2').length>0){
				$('#picker2').farbtastic(function(data){$("#borderColor").val(data);$("#borderColor").css("backgroundColor",data); var index=$(".dragIndex").html();$(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",data);var color=rgbToHsl(data.colorRgb().split(',')[0],data.colorRgb().split(',')[1],data.colorRgb().split(',')[2])[2] > 0.5 ? '#000' : '#fff'; $("#borderColor").css("color",color)});}
				var copyToggle=false;
				document.onkeydown=function(event){
					deleteEle(event,index)
					moveaway(event,$("#nr .editWrapper .editArea li").eq(index).find(".draggable"),index);	
					var fontFamily=$("#fontFamily").val();
				}
				document.onkeyup=function(event){
					copyEle(event,index);
					pasteEle(event);
				}
			})
			$("#nr .editWrapper .editArea .text").dblclick(function(){
					
						$(this).children(".draggable").children(".elementBox").attr("contenteditable","true");
						$(this).children(".draggable").children(".elementBox").attr("id","editor");	
						$(this).children(".draggable").children(".elementBox").css("cursor","text");
						if(typeof userSelect === "string"){      
							return document.documentElement.style[userSelect] = "text";
						}
						document.unselectable  = "off";document.onselectstart = null;					
						e = parseInt($(this).css("top"));
						f=$(".editArea").width();
						g=parseInt($(".editArea").offset().left);
						$(".btn-toolbar").css("display","block");
						$(".btn-toolbar").css("top",e);
						$(".btn-toolbar").css("background","#fff");
						$(".btn-toolbar").css("box-shadow","+0 1px 2px rgba(0,0,0,.5)+");
						$(".btn-toolbar").css("left",-350);					
						$(".btn-toolbar").css("width",f);
						$(this).children().children(".elementBox").wysiwyg();
						$(this).children().children(".elementBox").focus();
					
			})		
				$("#nr .editWrapper .editArea li").dblclick(function(){
				var index=$("#nr .editWrapper .editArea li").index($(this));
				var cssData={};
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth")))){
					borderWidth='';
				}else{
					borderWidth=parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopWidth"));
				}
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius")))){
					borderRadius='';
				}else{
					borderRadius=parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("borderTopLeftRadius"));
				}
				if(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing"))==0||isNaN(parseFloat($(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing")))){
					letterSpacing="0px";
				}else{
					letterSpacing=$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing");
				}
				cssData={
					borderWidth: borderWidth,
					borderStyle: $(".editArea").children("li").eq(index).find(".elementBox").css("borderTopStyle"),
					borderColor: $(".editArea").children("li").eq(index).find(".elementBox").css("borderTopColor"),
					borderRadius: borderRadius,
					backgroundColor: $(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor"),
					opacity: $(".editArea").children("li").eq(index).find(".elementBox").css("opacity"),
					fontSize:$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize"),
					lineHeight:$(".editArea").children("li").eq(index).find(".elementBox")[0].style.lineHeight,
					letterSpacing:letterSpacing,
				}
				$(".styleModal").find("#opacity").val(cssData.opacity);
				$(".styleModal").find("#borderSize").val(cssData.borderWidth);
				$(".styleModal").find("#borderRadius").val(cssData.borderRadius);
				$(".styleModal").find("#borderColor").val(cssData.borderColor);
				$(".styleModal").find("#color").val(cssData.backgroundColor);
				$(".styleModal").find("#color").css("backgroundColor",cssData.backgroundColor);
				$(".styleModal").find("#borderColor").css("backgroundColor",cssData.borderColor);
				$(".styleModal").find("#fontSize").val(cssData.fontSize);
				$(".styleModal").find("#lineHeight").val(cssData.lineHeight);
				$(".styleModal").find("#letterSpacing").val(cssData.letterSpacing);
				var typeNum=$(".styleModal").find("input[name='borderStyle']").length;
				for(var i=0;i<typeNum;i++){
					if($(".styleModal").find("input[name='borderStyle']").eq(i).val()==cssData.borderStyle){
						$(".styleModal").find("input[name='borderStyle']").eq(i)[0].checked=true;
					}
				}
				$(".styleModal").fadeIn(1000);				
				$(".dragIndex").html($("#nr .editWrapper .editArea li").index($(this)));
				$("#popMenu").hide();
				$(".animModal").hide();
				$(".bar").children(".bar-radius").css("display","none");	
				$(".bar").css("display","none");				
				$(".styleModal .submit").click(function(){
					$(".styleModal").fadeOut(1000);
					setLocalStorage();
				})
				$(".styleModal .modalFooter .cancel").click(function(){
					var index=$(".dragIndex").html();
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderWidth",cssData.borderWidth),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderStyle",cssData.borderStyle),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",cssData.borderColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderRadius",cssData.borderRadius),
					$(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",cssData.backgroundColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("opacity",cssData.opacity),
					$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize",cssData.fontSize),
					$(".editArea").children("li").eq(index).find(".elementBox").css("lineHeight",cssData.lineHeight),
					$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing",cssData.letterSpacing),
					$(".styleModal").fadeOut(1000);
				})
				$(".styleModal .close").click(function(){
					var index=$(".dragIndex").html();
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderWidth",cssData.borderWidth),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderStyle",cssData.borderStyle),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",cssData.borderColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("borderRadius",cssData.borderRadius),
					$(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",cssData.backgroundColor),
					$(".editArea").children("li").eq(index).find(".elementBox").css("opacity",cssData.opacity),
					$(".editArea").children("li").eq(index).find(".elementBox").css("fontSize",cssData.fontSize),
					$(".editArea").children("li").eq(index).find(".elementBox").css("lineHeight",cssData.lineHeight),
					$(".editArea").children("li").eq(index).find(".elementBox").css("letterSpacing",cssData.letterSpacing),
					$(".styleModal").fadeOut(1000);
				})
				if($('#picker').length>0){
				$('#picker').farbtastic(function(data){$("#color").val(data);$("#color").css("backgroundColor",data); $(".editArea").children("li").eq(index).find(".elementBox").css("backgroundColor",data);var color=rgbToHsl(data.colorRgb().split(',')[0],data.colorRgb().split(',')[1],data.colorRgb().split(',')[2])[2] > 0.5 ? '#000' : '#fff'; $("#color").css("color",color)});}
				if($('#picker2').length>0){
				$('#picker2').farbtastic(function(data){$("#borderColor").val(data);$("#borderColor").css("backgroundColor",data); $(".editArea").children("li").eq(index).find(".elementBox").css("borderColor",data);var color=rgbToHsl(data.colorRgb().split(',')[0],data.colorRgb().split(',')[1],data.colorRgb().split(',')[2])[2] > 0.5 ? '#000' : '#fff'; $("#borderColor").css("color",color)});}
				
				$(".animModal").fadeIn();
				$(".animModalDrag").css("display","none");
				$(".animModalDrag").eq(index).find(".animIndex").html(index);				
				$(".animModalDrag").eq(index).fadeIn(1000);
				$("#popMenu").hide();
				$(".bar").children(".bar-radius").css("display","none");	
				$(".bar").css("display","none");
				var animData=new Array();
				var m=$(".animModalDrag").eq(index).find(".animCont").length;
				for(var i=0;i<m;i++){
					if($(".animModalDrag").eq(index).find(".animCont").eq(i).find("input[name='animCircle']:checked").val()=="infinite"){
						animationIterationCount="infinite";
					}else{
						animationIterationCount=$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val();
					}
					animData[i]={
						animationDuration:$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".time").val(),
						animationDelay:$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".delay").val(),
						animationName:$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod").val(),
						animationIterationCount:animationIterationCount,
					}
				}
				//alert(animData);
				$("body").on("click",".animModal .modalFooter .cancel",function(){
					var animTabHtml='';
					for(var j=0;j<m;j++){
						var num=j+1;
						animTabHtml+='<li>动画-'+num+'</li>';
					}
					$(".animModal .animModalDrag").eq(l).find(".animTab").empty().append(animTabHtml);
					var animContHtml='';
					for(var j=0;j<m;j++){
						animContHtml+='<li>';
						animContHtml+='<form action="" style="margin:auto 0;">';				
						animContHtml+='<div class="form-item"><label for="time">动画持续:</label><input type="text" class="time" name="time" value="" placeholder="请输入动画时间"/></div>';
						animContHtml+='<div class="form-item"><label for="delay">动画延迟:</label><input type="text" class="delay" name="delay" value="" placeholder="请输入延迟时间"/></div>';		
						animContHtml+='<div class="form-item"><label for="times">动画次数:</label><input type="text" class="times" name="times" value="" placeholder="请输入动画次数"/></div>';
						animContHtml+='<div class="form-item"><label for="method">是否循环:</label><label><input name="animCircle" type="radio" value="infinite" />是 </label> <label><input name="animCircle" type="radio" value="" />否 </label></div>';
						animContHtml+='<div class="form-item"><label for="method">动画方式:</label><select class="animMethod"><option value="fadeIn">淡入 </option> <option value="fadeInRight">从右移入 </option> <option value="fadeInLeft" >从左移入 </option><option value="fadeInUp">从下移入 </option><option value="fadeInDown">从上移入 </option><option value="zoomIn">中心放大</option><option value="wobble">摇摆 </option><option value="rotateIn">旋转 </option><option value="swing">悬摆 </option><option value="flipOutY">翻转消失 </option><option value="fadeOut">淡出 </option> <option value="none">无 </option></select></div>';		
						animContHtml+='</form>';
						animContHtml+='</li>';
					}
					$(".animModal .animModalDrag").eq(index).find(".animCont").empty().append(animContHtml);
					for(var i=0;i<m;i++){
						//alert(animData[i].animationDuration);
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".time").val(animData[i].animationDuration);
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".delay").val(animData[i].animationDelay);
						var n=$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").length;
						for(var j=0;j<n;j++){
							if($(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j).val()==animData[i].animationName){
								$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j)[0].selected=true;
							}
						}
						if(animData[i].animationIterationCount=="infinite"){
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find("input[name='animCircle']").eq(0)[0].checked=true;
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val("");
						}else{
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val(animData[i].animationIterationCount);
						}
					}
					$(".animModal").fadeOut(1000);
				})
				/* $("body").on("click",".animModal .modalFooter .submit",function(){
					$(".animModal").fadeOut(1000);
				}) */
				$(".animModal .modalFooter .submit").click(function(){
					$(".animModal").fadeOut(1000);
					setLocalStorage();
				})
				$("body").on("click",".animModal .close",function(){
					var animTabHtml='';
					for(var j=0;j<m;j++){
						var num=j+1;
						animTabHtml+='<li>动画-'+num+'</li>';
					}
					$(".animModal .animModalDrag").eq(l).find(".animTab").empty().append(animTabHtml);
					var animContHtml='';
					for(var j=0;j<m;j++){
						animContHtml+='<li>';
						animContHtml+='<form action="" style="margin:auto 0;">';				
						animContHtml+='<div class="form-item"><label for="time">动画持续:</label><input type="text" class="time" name="time" value="" placeholder="请输入动画时间"/></div>';
						animContHtml+='<div class="form-item"><label for="delay">动画延迟:</label><input type="text" class="delay" name="delay" value="" placeholder="请输入延迟时间"/></div>';		
						animContHtml+='<div class="form-item"><label for="times">动画次数:</label><input type="text" class="times" name="times" value="" placeholder="请输入动画次数"/></div>';
						animContHtml+='<div class="form-item"><label for="method">是否循环:</label><label><input name="animCircle" type="radio" value="infinite" />是 </label> <label><input name="animCircle" type="radio" value=""  />否 </label></div>';
						animContHtml+='<div class="form-item"><label for="method">动画方式:</label><select class="animMethod"><option value="fadeIn">淡入 </option> <option value="fadeInRight">从右移入 </option> <option value="fadeInLeft" >从左移入 </option><option value="fadeInUp">从下移入 </option><option value="fadeInDown">从上移入 </option><option value="zoomIn">中心放大</option><option value="wobble">摇摆 </option><option value="rotateIn">旋转 </option><option value="swing">悬摆 </option><option value="flipOutY">翻转消失 </option><option value="fadeOut">淡出 </option> <option value="none">无 </option></select></div>';			
						animContHtml+='</form>';
						animContHtml+='</li>';
					}
					$(".animModal .animModalDrag").eq(index).find(".animCont").empty().append(animContHtml);
					for(var i=0;i<m;i++){
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".time").val(animData[i].animationDuration);
						$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".delay").val(animData[i].animationDelay);
						var n=$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").length;
						for(var j=0;j<n;j++){
							if($(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j).val()==animData[i].animationName){
								$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".animMethod option").eq(j)[0].selected=true;
							}
						}
						if(animData[i].animationIterationCount=="infinite"){
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find("input[name='animCircle']").eq(0)[0].checked=true;
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val("");
						}else{
							$(".animModalDrag").eq(index).find(".animCont").eq(i).find(".times").val(animData[i].animationIterationCount);
						}
					}
					$(".animModal").fadeOut(1000);
				})
				
			})		
			$("body").on("click","#popMenu .edit",function(){
				var i=$(this).parent().children(".popIndex").html();
				//alert(i);
				if($("#nr .editWrapper .editArea").children("li").eq(i).hasClass("text")){
					$("#nr .editWrapper .editArea").children("li").eq(i).children(".draggable").children(".elementBox").attr("contenteditable","true");
					$("#nr .editWrapper .editArea").children("li").eq(i).children(".draggable").children(".elementBox").attr("id","editor");	
					$("#nr .editWrapper .editArea").children("li").eq(i).children(".draggable").children(".elementBox").css("cursor","text");
					//$("#nr .editWrapper .editArea").children("li").eq(i).children(".draggable").children(".elementBox").css("display","table-cell");
					if(typeof userSelect === "string"){      
						return document.documentElement.style[userSelect] = "text";
					}
					document.unselectable  = "off";document.onselectstart = null;					
					e = parseInt($("#nr .editWrapper .editArea").children("li").eq(i).css("top"));
					f=$(".editArea").width();
					g=parseInt($(".editArea").offset().left);
					$(".btn-toolbar").css("display","block");
					$(".btn-toolbar").css("top",e);
					$(".btn-toolbar").css("background","#fff");
					$(".btn-toolbar").css("box-shadow","+0 1px 2px rgba(0,0,0,.5)+");
					$(".btn-toolbar").css("left",-350);					
					$(".btn-toolbar").css("width",f);
					//$(".elementBox").wysiwyg_destroy();
					$("#nr .editWrapper .editArea").children("li").eq(i).children().children(".elementBox").wysiwyg();
					var h=$("#nr .editWrapper .editArea").children("li").eq(i).find(".elementBox").height();
					var w=$("#nr .editWrapper .editArea").children("li").eq(i).find(".elementBox").width();
					$("#nr .editWrapper .editArea").children("li").eq(i).css({"width":w,"height":h})
					$("#nr .editWrapper .editArea").children("li").eq(i).children().children(".elementBox").focus();
					$("#popMenu").css("display","none");
				}
				else if($("#nr .editWrapper .editArea").children("li").eq(i).hasClass("input")){
					var type=$("#nr .editWrapper .editArea").children("li").eq(i).find("input").attr("type");
					var placeholder=$("#nr .editWrapper .editArea").children("li").eq(i).find("input").attr("placeholder");
					$(".inputModal .inputModalType").html(i);
					//alert(type);
					for(var j=0;j<2;j++){
						if($(".inputModal").find("input[name='inputType']").eq(j).val()==type){
							$(".inputModal").find("input[name='inputType']").eq(j)[0].checked=true;
							//alert(j);
						}						
					}
					$(".inputModal").find(".inputName").val(placeholder);
					$(".inputmsg").hide();
					$(".inputModal").fadeIn(1000);
					draggable($(".inputModal .modalHeader"));
					$("#popMenu").css("display","none");
				}
				else if($("#nr .editWrapper .editArea").children("li").eq(i).hasClass("button")){
					var name=$("#nr .editWrapper .editArea").children("li").eq(i).find("button").html();
					$(".buttonModal .buttonModalType").html(i);
					$(".buttonModal").find(".buttonName").val(name);
					$(".buttonmsg").hide();
					$(".buttonModal").fadeIn(1000);
					draggable($(".buttonModal .modalHeader"));
					$("#popMenu").css("display","none");
				}
				else if($("#nr .editWrapper .editArea").children("li").eq(i).attr("ctype")=="collection"){
					$(".collectionModal .collectionModalType").html(i);
					$(".collectionModal").fadeIn(1000);
					draggable($(".collectionModal .modalHeader"));
					$("#popMenu").css("display","none");
				}
				
			}) 			
			$("body").on("mousedown","#nr .editWrapper .editArea li",function(e){
				if ( e.which == null && (e.charCode != null || e.keyCode != null) ) { 
				e.which = e.charCode != null ? e.charCode : e.keyCode; 
				} // 1 = 鼠标左键 left; 2 = 鼠标中键; 3 = 鼠标右键
				l=$(".editWrapper").offset().left;
				t=$(".editWrapper").offset().top;
				//alert($(this).children(".draggable").children(".elementBox").attr("contenteditable"));
				if(e.which==3){
					//alert($(this).children(".draggable").children(".elementBox").attr("contenteditable")!="true");
					if($(this).children(".draggable").children(".elementBox").attr("contenteditable")!="true"){							
						$("#popMenu").css("display","block");				
						$("#popMenu").css("top",e.pageY-t);
						$("#popMenu").css("left",e.pageX-l);
						if($(this).attr("ctype")=="image"){
							$("#popMenu .edit").hide();
						}else{
							$("#popMenu .edit").show();
						}
						if($(this).attr("ctype")=="collection"){
							$("#popMenu .copy").hide();
						}else{
							$("#popMenu .copy").show();
						}
						var m=$(this).index()-1;
						$("#popMenu .popIndex").html(m);
					} 
					$(this).bind("contextmenu",function(e){
							if($(this).children(".draggable").children(".elementBox").attr("contenteditable")!="true"){
								return false;
							}
							else{
								return true;
							}
					});				
				}
			})
			
			$(window).resize(function(){
				f=$(".editArea").width();
				$(".btn-toolbar").css("width",f);
			})
			$("#nr .editWrapper .editArea .phoneContBg").click(function(){
				$("#nr .editWrapper .editArea li .bar").css("display","none");
				if($(".btn-toolbar").css("display")=="block"){
					$(".btn-toolbar").css("display","none");
					setLocalStorage();
				}
				$("#nr .editWrapper .editArea li .bar .bar-radius").css("display","none");	
				$(".elementBox").attr("contenteditable","false");
				//$(".elementBox").css("cursor","move").css("display","table-cell");
				$(".elementBox").attr("id","");
				$("#popMenu").css("display","none");
			})
			$("#nr .editWrapper .editArea").children("li").click(function(){
				$(this).siblings("li").find("bar").css("display","none");
				if($(this).find(".elementBox").attr("id")!="editor"&&$(".btn-toolbar").css("display")=="block"){
					$(".btn-toolbar").css("display","none");
					setLocalStorage();
				}				
				$(this).siblings("li").find("bar-radius").css("display","none");				
				$(this).siblings("li").find(".elementBox").attr("contenteditable","false");
				$(this).siblings("li").find(".elementBox").attr("id","");
				$("#popMenu").css("display","none");
			})
			
		}
		function previewAll(){
			var animLength=$(".animModal").find(".animModalDrag").length;
				for(var j=0;j<animLength;j++){
					var m=$(".animModal").find(".animModalDrag").eq(j).find(".animCont li").length;
					function previewEach(i,j){
						if(i<m){
							b=$(".animModal").find(".animModalDrag").eq(j).find(".animCont li").eq(i).children().children("div");
							$("#nr .editWrapper .editArea").children("li").eq(j).find(".elementBox").css("animation",""),$("#nr .editWrapper .editArea").children("li").eq(0).get(0).offsetWidth = $("#nr .editWrapper .editArea").children("li").eq(0).get(0).offsetWidth,$("#nr .editWrapper .editArea").children("li").eq(j).find(".elementBox").css("animation",b.eq(4).find(".animMethod").val() + " " + b.eq(0).find(".time").val() + "s ease " + b.eq(1).find(".delay").val() + "s "+  b.eq(2).find(".times").val()).css("animation-fill-mode", "backwards");
							if(b.eq(3).find("input[name='animCircle']:checked").val()=="infinite"){
								$("#nr .editWrapper .editArea").children("li").eq(j).find(".elementBox").css("animation-iteration-count", "infinite");												
							}	
							$("#nr .editWrapper .editArea").children("li").eq(j).find(".elementBox").one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend", function(){ //动画结束时事件 
								i++,previewEach(i,j);
							});
						}						
					}
					previewEach(0,j);
				}
		}
	