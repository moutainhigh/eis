(function(){var c={id:"960c6fa14be7bcfa43f6e67089cc180c",dm:["jcard.cn","jcard.com.cn","9jx.com"],etrk:[{id:"NumericalStatementC",eventType:"onclick"}],js:"tongji.baidu.com/hm-web/js/",icon:'/hmt/icon/21|gif|20|20',br:false,ctrk:true,align:1,nv:0,vdur:1800000,age:31536000000,se:[[1,'baidu.com','word|wd|w',1,'news,tieba,zhidao,,image,video,hi,baike,wenku,opendata,jingyan'],[2,'google.com','q',0,'tbm=isch,tbm=vid,tbm=nws|source=newssearch,tbm=blg,tbm=frm'],[4,'sogou.com','query|keyword',1,'news,mp3,pic,v,gouwu,zhishi,blogsearch'],[6,'search.yahoo.com','p',1,'news,images,video'],[7,'yahoo.cn','q',1,'news,image,music'],[8,'soso.com','w|key',1,'image,video,music,sobar,wenwen,news,baike'],[11,'youdao.com','q',1,'image,news,,mp3,video'],[12,'gougou.com','search',1,',movie,,,,,video'],[13,'bing.com','q',2,'images,videos,news'],[14,'so.com','q',1,'video,news'],[15,'jike.com','q',1,'news,image,video'],[16,'qihoo.com','kw',0,''],[17,'etao.com','q',1,'s,8'],[18,'soku.com','keyword',2,'a'],[19,'easou.com','q',0,''],[20,'glb.uc.cn','keyword|word|q',0,'']]};var g=true,h=null,i=false;function k(a,b,d){a.attachEvent?a.attachEvent("on"+b,function(b){d.call(a,b)}):a.addEventListener&&a.addEventListener(b,d,i)};(function(){function a(){if(!a.e){a.e=g;for(var b=0,d=e.length;b<d;b++)e[b]()}}function b(){try{document.documentElement.doScroll("left")}catch(d){setTimeout(b,1);return}a()}var d=i,e=[],f;document.addEventListener?f=function(){document.removeEventListener("DOMContentLoaded",f,i);a()}:document.attachEvent&&(f=function(){document.readyState==="complete"&&(document.detachEvent("onreadystatechange",f),a())});(function(){if(!d)if(d=g,document.readyState==="complete")a.e=g;else if(document.addEventListener)document.addEventListener("DOMContentLoaded",
f,i),window.addEventListener("load",a,i);else if(document.attachEvent){document.attachEvent("onreadystatechange",f);window.attachEvent("onload",a);var e=i;try{e=window.frameElement==h}catch(m){}document.documentElement.doScroll&&e&&b()}})();return function(b){a.e?b():e.push(b)}})().e=i;function l(a,b){if(window.sessionStorage)try{window.sessionStorage.setItem(a,b)}catch(d){}}function n(a){return window.sessionStorage?window.sessionStorage.getItem(a):h};function o(a,b,d){var e;d.i&&(e=new Date,e.setTime(e.getTime()+d.i));document.cookie=a+"="+b+(d.domain?"; domain="+d.domain:"")+(d.path?"; path="+d.path:"")+(e?"; expires="+e.toGMTString():"")+(d.Q?"; secure":"")};function p(a,b){var d=a.match(RegExp("(^|&|\\?|#)("+b+")=([^&#]*)(&|$|#)",""));return d?d[3]:h}function q(a){var b;return(b=(a=a.match(/^(https?:\/\/)?([^\/\?#]*)/))?a[2].replace(/.*@/,""):h,a=b)?a.replace(/:\d+$/,""):a};function r(a,b){var d=new Image,e="mini_tangram_log_"+Math.floor(Math.random()*2147483648).toString(36);window[e]=d;d.onload=d.onerror=d.onabort=function(){d.onload=d.onerror=d.onabort=h;d=window[e]=h;b&&b(a)};d.src=a};var s=/msie (\d+\.\d+)/i.test(navigator.userAgent),t=navigator.cookieEnabled,u=navigator.javaEnabled(),v=navigator.language||navigator.browserLanguage||navigator.systemLanguage||navigator.userLanguage||"",w=window.screen.width+"x"+window.screen.height,x=window.screen.colorDepth;var y;function z(){if(!y)try{y=document.createElement("input"),y.type="hidden",y.style.display="none",y.addBehavior("#default#userData"),document.getElementsByTagName("head")[0].appendChild(y)}catch(a){return i}return g};c.se.push([14,"so.360.cn","q",1,",news"]);c.se.push([14,"v.360.cn","q",1,"so"]);var A=0,C=Math.round(+new Date/1E3),D=window.location.protocol=="https:"?"https:":"http:",E="cc,cf,ci,ck,cl,cm,cp,cw,ds,ep,et,fl,ja,ln,lo,lt,nv,rnd,sb,se,si,st,su,sw,sse,v,cv,lv,u,api".split(",");function F(){if(typeof window["_bdhm_loaded_"+c.id]=="undefined"){window["_bdhm_loaded_"+c.id]=g;var a=this;a.a={};a.b=[];a.H={push:function(){a.t.apply(a,arguments)}};a.f=0;a.j=i;a.I()}}
F.prototype={F:function(){var a="";try{external.twGetVersion(external.twGetSecurityID(window))&&external.twGetRunPath.toLowerCase().indexOf("360se")>-1&&(a=17)}catch(b){}return a},k:function(a,b){var a="."+a.replace(/:\d+/,""),b="."+b.replace(/:\d+/,""),d=a.indexOf(b);return d>-1&&d+b.length==a.length},q:function(a,b){a=a.replace(/^https?:\/\//,"");return a.indexOf(b)==0},d:function(a){for(var b=0;b<c.dm.length;b++)if(c.dm[b].indexOf("/")>-1){if(this.q(a,c.dm[b]))return g}else{var d=q(a);if(d&&this.k(d,
c.dm[b]))return g}return i},o:function(){for(var a=window.location.hostname,b=0,d=c.dm.length;b<d;b++)if(this.k(a,c.dm[b]))return c.dm[b].replace(/(:\d+)?[\/\?#].*/,"");return a},p:function(){for(var a=0,b=c.dm.length;a<b;a++){var d=c.dm[a];if(d.indexOf("/")>-1&&this.q(window.location.href,d))return d.replace(/^[^\/]+(\/.*)/,"$2")+"/"}return"/"},G:function(){if(document.referrer)for(var a=0,b=c.se.length;a<b;a++){if(RegExp("(^|\\.)"+c.se[a][1].replace(/\./g,"\\.")).test(q(document.referrer))){var d=
p(document.referrer,c.se[a][2])||"";if(d||!(c.se[a][0]!=2&&c.se[a][0]!=14&&c.se[a][0]!=17)){c.se[a][0]==1&&document.referrer.indexOf("cpro.baidu.com")>-1&&(d="");this.a.se=c.se[a][0];for(var b=this.a,e=c.se[a],a=0,f=e[3]==2?e[1]+"\\/":"",j=e[3]==1?"\\."+e[1]:"",e=e[4].split(","),m=0,B=e.length;m<B;m++)if(e[m]!==""&&RegExp(f+e[m]+j).test(document.referrer)){a=m+1;break}b.sse=a;this.a.sw=d;return 2}}}else return C-A>c.vdur?1:4;d=i;return(d=this.d(document.referrer)&&this.d(window.location.href)?g:this.k(q(document.referrer)||
"",window.location.hostname))?C-A>c.vdur?1:4:3},getData:function(a){try{var b,d=RegExp("(^| )"+a+"=([^;]*)(;|$)").exec(document.cookie);if(!(b=d?d[2]:h)){var e;if(!(e=n(a)))a:{if(window.localStorage){var f=window.localStorage.getItem(a);if(f){var j=f.indexOf("|"),m=f.substring(0,j)-0;if(m&&m>(new Date).getTime()){e=f.substring(j+1);break a}}}else if(z())try{y.load(window.location.hostname);e=y.getAttribute(a);break a}catch(B){}e=h}b=e}return b}catch(M){}},setData:function(a,b,d){try{if(o(a,b,{domain:this.o(),
path:this.p(),i:d}),d){var e=new Date;e.setTime(e.getTime()+d||31536E6);try{if(window.localStorage)b=e.getTime()+"|"+b,window.localStorage.setItem(a,b);else if(z())y.expires=e.toUTCString(),y.load(window.location.hostname),y.setAttribute(a,b),y.save(window.location.hostname)}catch(f){}}else l(a,b)}catch(j){}},J:function(a){try{if(o(a,"",{domain:this.o(),path:this.p(),i:-1}),window.sessionStorage&&window.sessionStorage.removeItem(a),window.localStorage)window.localStorage.removeItem(a);else if(z())try{y.load(window.location.hostname),
y.removeAttribute(a),y.save(window.location.hostname)}catch(b){}}catch(d){}},N:function(){var a,b,d,e,f;A=this.getData("Hm_lpvt_"+c.id)||0;A.length==13&&(A=Math.round(A/1E3));b=this.G();a=b!=4?1:0;if(d=this.getData("Hm_lvt_"+c.id)){e=d.split(",");for(f=e.length-1;f>=0;f--)e[f].length==13&&(e[f]=""+Math.round(e[f]/1E3));for(;C-e[0]>2592E3;)e.shift();f=e.length<4?2:3;for(a===1&&e.push(C);e.length>4;)e.shift();d=e.join(",");e=e[e.length-1]}else d=C,e="",f=1;this.setData("Hm_lvt_"+c.id,d,c.age);this.setData("Hm_lpvt_"+
c.id,C);d=C==this.getData("Hm_lpvt_"+c.id)?"1":"0";if(c.nv==0&&this.d(window.location.href)&&(document.referrer==""||this.d(document.referrer)))a=0,b=4;this.a.nv=a;this.a.st=b;this.a.cc=d;this.a.lt=e;this.a.lv=f},w:function(){for(var a=[],b=0,d=E.length;b<d;b++){var e=E[b],f=this.a[e];typeof f!="undefined"&&f!==""&&a.push(e+"="+encodeURIComponent(f))}return a.join("&")},O:function(){this.N();this.a.si=c.id;this.a.su=document.referrer;this.a.ds=w;this.a.cl=x+"-bit";this.a.ln=v;this.a.ja=u?1:0;this.a.ck=
t?1:0;this.a.lo=typeof _bdhm_top=="number"?1:0;var a=this.a,b="";if(navigator.plugins&&navigator.mimeTypes.length){var d=navigator.plugins["Shockwave Flash"];d&&d.description&&(b=d.description.replace(/^.*\s+(\S+)\s+\S+$/,"$1"))}else if(window.ActiveXObject)try{if(d=new ActiveXObject("ShockwaveFlash.ShockwaveFlash"))(b=d.GetVariable("$version"))&&(b=b.replace(/^.*\s+(\d+),(\d+).*$/,"$1.$2"))}catch(e){}a.fl=b;this.a.sb=this.F();this.a.v="1.0.38";this.a.cv=decodeURIComponent(this.getData("Hm_cv_"+c.id)||
"");this.a.api=0;a=window.location.href;this.a.cm=p(a,"hmmd")||"";this.a.cp=p(a,"hmpl")||"";this.a.cw=p(a,"hmkw")||"";this.a.ci=p(a,"hmci")||"";this.a.cf=p(a,"hmsr")||""},I:function(){try{this.O(),this.a.nv==0?this.M():this.l(".*"),this.z(),this.B(),this.n&&this.n(),this.m&&this.m(),this.h=new G,k(window,"beforeunload",this.L()),this.C(),this.D(),this.K()}catch(a){var b=[];b.push("si="+c.id);b.push("n="+encodeURIComponent(a.name));b.push("m="+encodeURIComponent(a.message));b.push("r="+encodeURIComponent(document.referrer));
r(D+"//hm.baidu.com/hm.gif?"+b.join("&"))}},K:function(){if(typeof window._bdhm_autoPageview==="undefined"||window._bdhm_autoPageview===g)this.j=g,this.a.et=0,this.a.ep="",this.c()},L:function(){var a=this;return function(){a.a.et=3;a.a.ep=(new Date).getTime()-a.h.r+","+((new Date).getTime()-a.h.g+a.h.s);a.c()}},c:function(){var a=this;a.a.rnd=Math.round(Math.random()*2147483647);a.a.api=a.a.api||a.f?a.a.api+"_"+a.f:"";var b=D+"//hm.baidu.com/hm.gif?"+a.w();a.a.api=0;a.f=0;a.A(b);r(b,function(b){a.l(b)})},
z:function(){if(c.icon!=""){var a,b=c.icon.split("|"),d="http://tongji.baidu.com/hm-web/welcome/ico?s="+c.id;a=(D=="http:"?"http://eiv":"https://bs")+".baidu.com"+b[0]+"."+b[1];switch(b[1]){case "swf":var e=b[2],b=b[3],d="s="+d,f="HolmesIcon"+C;a='<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" id="'+f+'" width="'+e+'" height="'+b+'"><param name="movie" value="'+a+'" /><param name="flashvars" value="'+d+'" /><param name="allowscriptaccess" value="always" /><embed type="application/x-shockwave-flash" name="'+
f+'" width="'+e+'" height="'+b+'" src="'+a+'" flashvars="'+d+'" allowscriptaccess="always" /></object>';break;case "gif":a='<a href="'+d+'" target="_blank"><img border="0" src="'+a+'" width="'+b[2]+'" height="'+b[3]+'"></a>';break;default:a='<a href="'+d+'" target="_blank">'+b[0]+"</a>"}document.write(a)}},B:function(){var a=window.location.hash.substring(1),b=RegExp(c.id),d=document.referrer.indexOf("baidu.com")>-1?g:i;a&&b.test(a)&&d&&(b=document.createElement("script"),b.setAttribute("type","text/javascript"),
b.setAttribute("charset","utf-8"),b.setAttribute("src",D+"//"+c.js+p(a,"jn")+"."+p(a,"sx")+"?"+this.a.rnd),a=document.getElementsByTagName("script")[0],a.parentNode.insertBefore(b,a))},D:function(){if(typeof window._bdhm_account==="undefined"||window._bdhm_account===c.id){window._bdhm_account=c.id;var a=window._hmt;if(a&&a.length)for(var b=0,d=a.length;b<d;b++)this.t(a[b]);window._hmt=this.H}},t:function(a){if(function(a){if(Object.prototype.toString.call(a)!=="[object Array]")return i;for(var b=
a.length-1;b>=0;b--){var d=a[b];if(!("[object Number]"==Object.prototype.toString.call(d)&&isFinite(d))&&"[object String]"!=Object.prototype.toString.call(d)&&d!==g&&d!==i)return i}return g}(a)){var b=function(a){return a.replace?a.replace(/'/g,"'0").replace(/\*/g,"'1").replace(/!/g,"'2"):a};switch(a[0]){case "_trackPageview":if(a.length>1&&a[1].charAt&&a[1].charAt(0)=="/"){this.a.api|=4;this.a.et=0;this.a.ep="";this.j?(this.a.nv=0,this.a.st=4):this.j=g;var b=this.a.u,d=this.a.su;this.a.u=D+"//"+
window.location.host+a[1];this.a.su=window.location.href;this.c();this.a.u=b;this.a.su=d}break;case "_trackEvent":if(a.length>2)this.a.api|=8,this.a.et="4",this.a.ep=b(a[1])+"*"+b(a[2])+(a[3]?"*"+b(a[3]):"")+(a[4]?"*"+b(a[4]):""),this.c();break;case "_setCustomVar":if(a.length<4)break;var d=a[1],e=a[4]||3;if(d>0&&d<6&&e>0&&e<4){this.f++;for(var f=(this.a.cv||"*").split("!"),j=f.length;j<d-1;j++)f.push("*");f[d-1]=e+"*"+b(a[2])+"*"+b(a[3]);this.a.cv=f.join("!");a=this.a.cv.replace(/[^1](\*[^!]*){2}/g,
"*").replace(/((^|!)\*)+$/g,"");a!==""?this.setData("Hm_cv_"+c.id,encodeURIComponent(a),c.age):this.J("Hm_cv_"+c.id)}}}},C:function(){var a=window._hmt;if(a&&a.length)for(var b=0;b<a.length;b++){var d=a[b];switch(d[0]){case "_setAccount":if(d.length>1&&/^[0-9a-z]{32}$/.test(d[1]))this.a.api|=1,window._bdhm_account=d[1];break;case "_setAutoPageview":if(d.length>1&&(d=d[1],i===d||g===d))this.a.api|=2,window._bdhm_autoPageview=d}}},A:function(a){var b=n("Hm_unsent_"+c.id)||"",d=this.a.u?"":"&u="+encodeURIComponent(window.location.href),
b=encodeURIComponent(a.replace(/^https?:\/\//,"")+d)+(b?","+b:"");l("Hm_unsent_"+c.id,b)},l:function(a){var b=n("Hm_unsent_"+c.id)||"";b&&((b=b.replace(RegExp(encodeURIComponent(a.replace(/^https?:\/\//,"")).replace(/([\*\(\)])/g,"\\$1")+"(%26u%3D[^,]*)?,?","g"),"").replace(/,$/,""))?l("Hm_unsent_"+c.id,b):window.sessionStorage&&window.sessionStorage.removeItem("Hm_unsent_"+c.id))},M:function(){var a=this,b=n("Hm_unsent_"+c.id);if(b)for(var b=b.split(","),d=0,e=b.length;d<e;d++)r(D+"//"+decodeURIComponent(b[d]).replace(/^https?:\/\//,
""),function(b){a.l(b)})}};F.prototype.n=function(){k(document,"click",H(this));for(var a=c.etrk.length,b=0;b<a;b++){var d=c.etrk[b],e=document.getElementById(d.id);e&&k(e,d.eventType,I(this))}};function I(a){return function(b){(b.target||b.srcElement).setAttribute("HM_fix",b.clientX+":"+b.clientY);a.a.et=1;a.a.ep="{id:"+this.id+",eventType:"+b.type+"}";a.c()}}
function H(a){return function(b){var d=b.target||b.srcElement,e=d.getAttribute("HM_fix"),f=b.clientX+":"+b.clientY;if(e&&e==f)d.removeAttribute("HM_fix");else if(e=c.etrk.length,e>0){for(f={};d&&d!=document.body;)d.id&&(f[d.id]=""),d=d.parentNode;for(d=0;d<e;d++){var j=c.etrk[d];if(f.hasOwnProperty(j.id))a.a.et=1,a.a.ep="{id:"+j.id+",eventType:"+b.type+"}",a.c()}}}}
F.prototype.m=function(){var a=this;c.ctrk&&(k(document,"mouseup",J(this)),k(window,"beforeunload",function(){K(a)}),setInterval(function(){K(a)},6E5))};
function J(a){return function(b){var d,e;s?(e=Math.max(document.documentElement.scrollTop,document.body.scrollTop),d=Math.max(document.documentElement.scrollLeft,document.body.scrollLeft),d=b.clientX+d,e=b.clientY+e):(d=b.pageX,e=b.pageY);var f=window.innerWidth||document.documentElement.clientWidth||document.body.offsetWidth;switch(c.align){case 1:d-=f/2;break;case 2:d-=f}d="{x:"+d+",y:"+e+",";b=b.target||b.srcElement;if(b.tagName.toLowerCase()!="a")a:{for(e="A";(b=b.parentNode)&&b.nodeType==1;)if(b.tagName==
e)break a;b=h}d+=b?"t:a,u:"+encodeURIComponent(b.href)+"}":"t:b}";b=d;if(""!=b)d=(D+"//hm.baidu.com/hm.gif?"+a.w().replace(/ep=[^&]*/,"ep="+encodeURIComponent("["+b+"]"))).length,d+10>1024||(d+encodeURIComponent(a.b.join(",")+(a.b.length?",":"")).length+10>1024&&K(a),a.b.push(b),(a.b.length>=10||/t:a/.test(b))&&K(a))}}function K(a){if(a.b.length!=0)a.a.et=2,a.a.ep="["+a.b.join(",")+"]",a.c(),a.b=[]}
function G(){this.g=this.r=(new Date).getTime();this.s=0;typeof document.P=="object"?(k(document,"focusin",L(this)),k(document,"focusout",L(this))):(k(window,"focus",L(this)),k(window,"blur",L(this)))}function L(a){return function(b){if(!(b.target&&b.target!=window)){if(b.type=="blur"||b.type=="focusout")a.s+=(new Date).getTime()-a.g;a.g=(new Date).getTime()}}}new F;})();
