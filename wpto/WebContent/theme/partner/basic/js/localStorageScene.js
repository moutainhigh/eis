function setLocalStorage(){
		document.onkeyup=function(event){
			if(event.ctrlKey==1){
				keyDown = event.keyCode||event.which;
				if(keyDown==90){
					undo();
				}else if(keyDown==89){
					repeat();
				};
			};
		}
		var storage = window.localStorage;
		var index=$(".pagin .currentPage").index();
		var pageLen=$(".pagin li").length;
		var materialList=new Array();				
		var	scene=
					{
						currentStatus:100001,
						materialList:materialList,
						title:"",
						background:"",
						index:index,
						pageLen:pageLen,
						tags:new Array(),
						sceneId:parseInt($(".sceneId").val()),
					}		
					var m=$(".editWrapper .editArea").children("li").length;
					scene.title=$(".sceneName").val();
					var tagL=$(".dowebok input:checked").length;
					for(var i=0;i<tagL;i++){
						scene.tags[i]=$(".dowebok input:checked")[i].value;
					}
					if($(".otherTagName").val()!=""){
						scene.tags[tagL]=$(".otherTagName").val();
					}
					for(var j=0;j<m;j++){
						scene.materialList[j]={
							css:new String(),
							cssExt: new String(),
							materialType: $(".editWrapper ul").children("li").eq(j).attr("ctype"),
							materialModelId:$(".editWrapper ul").children("li").eq(j).children(".materialId").html(),
							content: "",
							id:j+1,
							index:j,
							animation:new Array(),
						}
						if (scene.materialList[j].materialType=="background-image"){var background=$(".editWrapper ul").css("backgroundImage").split("/");var bL=background.length;var background=background[bL-1];scene.background=background.split(")")[0].split('"')[0];}						
						else if(scene.materialList[j].materialType=="text"){
							scene.materialList[j].content=$(".editWrapper ul").children("li").eq(j).find(".elementBox").html();
						}
						else if(scene.materialList[j].materialType=="button"){
							scene.materialList[j].content=$(".editWrapper ul").children("li").eq(j).find("button").html();
						}
						else if(scene.materialList[j].materialType=="input"){
							scene.materialList[j].content=JSON.stringify({type:$(".editWrapper ul").children("li").eq(j).find("input").attr("type"),placeholder:$(".editWrapper ul").children("li").eq(j).find("input").attr("placeholder")})
						}
						else if(scene.materialList[j].materialType=="image"){
							scene.materialList[j].content=$(".editWrapper ul").children("li").eq(j).find("img").attr("src");
						}
						else if(scene.materialList[j].materialType=="music"){
							scene.materialList[j].content=$(".editWrapper ul").children("li").eq(j).find("audio").attr("src");
						}
						else if(scene.materialList[j].materialType=="video"){
							scene.materialList[j].content=$(".editWrapper ul").children("li").eq(j).find("source").attr("src");
						}
						else if(scene.materialList[j].materialType=="collection"){
							var s=$(".editWrapper ul").children("li").eq(j).find(".html5zoo-slides img").length;
							var src=$(".editWrapper ul").children("li").eq(j).find(".html5zoo-slides img").eq(0).attr("src");
							for(var k=1;k<s;k++){
								src+=','+$(".editWrapper ul").children("li").eq(j).find(".html5zoo-slides img").eq(k).attr("src");
							}
							var colType='';
							if($(".editWrapper ul").children("li").eq(j).attr("id")!=undefined){colType=$(".editWrapper ul").children("li").eq(j).attr("id");}
							scene.materialList[j].content=JSON.stringify({src:src,colType:colType})
						}
						var h=$(".editWrapper ul").height();
						var w=$(".editWrapper ul").width();
						var height=$(".editWrapper ul").children("li").eq(j).height();
						var width=$(".editWrapper ul").children("li").eq(j).width();
						var left=$(".editWrapper ul").children("li").eq(j).css("left");
						var top=$(".editWrapper ul").children("li").eq(j).css("top");
						if(hasPercent(left)){left=left}else{left=(Math.round(parseInt(left)*100/w)+'%')};
						if(hasPercent(top)){top=top}else{top=(Math.round(parseInt(top)*100/h)+'%')};
						if(hasPercent(height)){height=height}else{height=(Math.round(height*100/h)+'%')};
						if(hasPercent(width)){width=width}else{width=(Math.round(width*100/w)+'%')};
						var borderRadius=$(".editWrapper ul").children("li").eq(j).find(".elementBox").css("borderTopLeftRadius");
						if (borderRadius!=undefined){
							if(hasRem(borderRadius)){borderRadius=borderRadius}else{borderRadius=parseInt(borderRadius)*10/w+'rem'};
						}		
						var rotDeg=$(".editWrapper ul").children("li").eq(j)[0].style.webkitTransform;
						scene.materialList[j].css=
						"transform:"+rotDeg+";"+"left:"+ left+";top:"+ top+";height:"+ height+";width:"+width+";";
						scene.materialList[j].cssExt="border-width:"+$(".editWrapper ul").children("li").eq(j).find(".elementBox").css("borderTopWidth")+";border-style:"+$(".editWrapper ul").children("li").eq(j).find(".elementBox").css("borderTopStyle")+";border-color:"+removeSpace($(".editWrapper ul").children("li").eq(j).find(".elementBox").css("borderTopColor"))+";border-radius:"+ borderRadius+";background-color:"+removeSpace($(".editWrapper ul").children("li").eq(j).find(".elementBox").css("backgroundColor"))+";opacity:"+ $(".editWrapper ul").children("li").eq(j).find(".elementBox").css("opacity")+";font-size:"+$(".editWrapper ul").children("li").eq(j).find(".elementBox").css("fontSize");
						var l=$(".animModal").children(".animModalDrag").eq(j).find(".animCont li").length;
						for(var k=0;k<l;k++){
							scene.materialList[j].animation[k]={
							animationName: $(".animModal").children(".animModalDrag").eq(j).find(".animCont li").eq(k).find(".animMethod").val(),
							animationDuration: $(".animModal").children(".animModalDrag").eq(j).find(".animCont li").eq(k).find(".time").val(),
							animationDelay: $(".animModal").children(".animModalDrag").eq(j).find(".animCont li").eq(k).find(".delay").val(),
							animationIterationCount:null,
							}
							 if($(".animModal").children(".animModalDrag").eq(j).find(".animCont li").eq(k).find("input[name=animCircle]:checked").val()=="infinite"){
								scene.materialList[j].animation[k].animationIterationCount="infinite";}
							else{
								scene.materialList[j].animation[k].animationIterationCount=$(".animModal").children(".animModalDrag").eq(j).find(".animCont li").eq(k).find(".times").val();
							}
						}
						scene.materialList[j].animation=JSON.stringify(scene.materialList[j].animation);
		 
					}
		data=JSON.stringify({"scene":JSON.stringify(scene)});
		var l=storage.length;
		var num=$(".undoInit").val();
		var j=l-num;
		var dataArr=new Array();
		for(var i=0;i<num;i++){
			var dataKey=storage.getItem(storage.key(l-num+i));
			dataArr[i]=dataKey;
		}
		var keyCur="key"+j;
		try{
			localStorage.setItem(keyCur,data);
			}catch(oException){
			if(oException.name == 'QuotaExceededError'){
			console.log('超出本地存储限额！');
			//如果历史信息不重要了，可清空后再设置
			localStorage.clear();
			localStorage.setItem(keyCur,data);
			}
		}
		for(var i=0;i<num;i++){
			var num1=l-num+1+i;
			var key="key"+num1;
			try{
				localStorage.setItem(key,dataArr[i]);
				}catch(oException){
				if(oException.name == 'QuotaExceededError'){
				console.log('超出本地存储限额！');
				//如果历史信息不重要了，可清空后再设置
				localStorage.clear();					
				localStorage.setItem(key,dataArr[i]);
				}
			}
		}
	}
	function getLocalStorage(i){
		var storage = window.localStorage;
		key="key"+i;
		var data=JSON.parse(storage.getItem(storage.key(i)));
		loadLocalStorage(data,0,0,0,"");
		
	}
	
	 function getmatrix(a,b,c,d,e,f){  
        var aa=Math.round(180*Math.asin(a)/ Math.PI);  
        var bb=Math.round(180*Math.acos(b)/ Math.PI);  
        var cc=Math.round(180*Math.asin(c)/ Math.PI);  
        var dd=Math.round(180*Math.acos(d)/ Math.PI);  
        var deg=0;  
        if(aa==bb||-aa==bb){  
            deg=dd;  
        }else if(-aa+bb==180){  
            deg=180+cc;  
        }else if(aa+bb==180){  
            deg=360-cc||360-dd;  
        }  
        return deg>=360?0:deg;  
        //return (aa+','+bb+','+cc+','+dd);  
		
    } 