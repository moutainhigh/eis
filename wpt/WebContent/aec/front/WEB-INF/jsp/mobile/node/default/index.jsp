<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>农青-公告</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/about.css"/>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<!--<script type="text/javascript" src="../../../theme/${theme}/js/mobile/iscroll-probe.js"></script>-->
<style>
/*上拉刷新  下拉加载*/
#show{
	display:none;	
}
.downContainer{
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right:0;
    box-sizing: border-box;
    padding:50px 0;
}
#wrapper {
    max-width: 640px;
    width:100%;
    height:100%;
	background:#fafafa;
}

#scroller {
	position:absolute; z-index:1;
/*	-webkit-touch-callout:none;*/
	-webkit-tap-highlight-color:rgba(0,0,0,0);
	width:100%;
	padding:0;
}

#scroller ul {
	list-style:none;
	padding:0;
	margin:0;
	width:100%;
	text-align:left;
}

#scroller li {
	padding:0 10px;
	height:40px;
	line-height:40px;
	border-bottom:1px solid #ccc;
	border-top:1px solid #fff;
	background-color:#fafafa;
	font-size:14px;
}


/**
 *
 * Pull down styles
 *
 */
#pullDown, #pullUp {
	background:#fff;
	height:40px;
	line-height:40px;
	padding:5px 10px;
	border-bottom:1px solid #ccc;
	font-weight:bold;
	font-size:14px;
	color:#888;
}
#pullDown .pullDownIcon, #pullUp .pullUpIcon  {
	display:block; float:left;
	width:40px; height:40px;
	background:url("../../../theme/${theme}/image/mobile/pull-icon@2x.png") 0 0 no-repeat;
	-webkit-background-size:40px 80px; background-size:40px 80px;
	-webkit-transition-property:-webkit-transform;
	-webkit-transition-duration:250ms;    
    position: relative;
    left: -2px;	
}
#pullDown .pullDownIcon {
	-webkit-transform:rotate(0deg) translateZ(0);
}
#pullUp .pullUpIcon  {
	-webkit-transform:rotate(-180deg) translateZ(0);
}

#pullDown.flip .pullDownIcon {
	-webkit-transform:rotate(-180deg) translateZ(0);
}

#pullUp.flip .pullUpIcon {
	-webkit-transform:rotate(0deg) translateZ(0);
}

#pullDown.loading .pullDownIcon, #pullUp.loading .pullUpIcon {
	background-position:0 100%;
	-webkit-transform:rotate(0deg) translateZ(0);
	-webkit-transition-duration:0ms;

	-webkit-animation-name:loading;
	-webkit-animation-duration:2s;
	-webkit-animation-iteration-count:infinite;
	-webkit-animation-timing-function:linear;
}

@-webkit-keyframes loading {
	from { -webkit-transform:rotate(0deg) translateZ(0); }
	to { -webkit-transform:rotate(360deg) translateZ(0); }
}
.content{
    display: flex;
    justify-content: space-between;    
    text-align: left;
}
.title{
    display: inline-block;
    width: 40%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.text{
    width: 140px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.time{
    display: inline-block;
    font-size: 12px;
    color: gray;
    width: 60%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    text-align: right;
}
</style>
</head>
<body>
    <div class="header" id="header">
        <a class="back" href="javascript:history.go(-1);"></a><span>${node.name}</span><a class="list2" href="/"></a>
    </div>
    <div class="downContainer">
        <p id="show"></p> 
        <div id="wrapper">
            <div id="scroller">
                <div id="pullDown">
                    <span class="pullDownIcon"></span><span class="pullDownLabel">下拉刷新...</span>
                </div>
                <ul id="thelist">
                    <c:forEach items="${newsList}" var="a" varStatus="status"  begin="0" end="9" >
                        <li>
                            <a href="${a.viewUrl}" class="content">
                            <span class="title">${a.title}</span>
                            <!--<span class="text" >${a.content}</span>-->
                            <span class="time">日期：<fmt:formatDate value="${a.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            </a>
                        </li>
		            </c:forEach> 
                </ul>
                <div id="pullUp" style="display:none;" >
                    <span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载...</span>
                </div>
            </div>
        </div>
    </div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

<script type="text/javascript" src="../../../theme/${theme}/js/mobile/iscroll.js"></script>
<script type="text/javascript">

    var myScroll,
        pullDownEl, 
        pullDownOffset,
        pullUpEl, 
        pullUpOffset,
        generatedCount = 0,
        scrollHeight = 50,
        index =2;

    function pullDownAction () {
        index = 2;
        $.ajax({
            type: "get",
            url: '/content/default/index_10_1.json',
            data: {},
            dataType: 'json',
            success: function(data) {
                $('#thelist').empty();
                var defaultList = data.newsList.map(function(a,b){
                    if(b < 10){
                        return "<li><a href="+a.viewUrl+" class='content'><span class='title'>"+a.title+"</span><span class='time'>日期："+a.publishTime+"</span></a></li>"
                    }
                })
                $('#thelist').append(defaultList);
                document.getElementById("pullUp").style.display="";
                myScroll.refresh();
            },
            error: function(a) {
                alert("系统繁忙,请稍后再试");
                return false;
            }
        })
    }

    function pullUpAction () {
        $.ajax({
            type: "get",
            url: '/content/default/index_10_'+index+'.json',
            data: {},
            dataType: 'json',
            success: function(data) {
                if(data.newsList == ''){                    
                    $('#pullUp').css('top','50%');
                    scrollHeight = 50;
                    alert('没有更多数据了！');
                }else{
                    var defaultList = data.newsList.map(function(a,b){
                        if(b < 10){
                            return "<li><a href="+a.viewUrl+" class='content'><span class='title'>"+a.title+"</span><span class='time'>日期："+a.publishTime+"</span></a></li>"
                        }
                    })
                    index++;
                    $('#thelist').append(defaultList);
                    myScroll.refresh();
                }
            },
            error: function(a) {
                alert("系统繁忙,请稍后再试");
                return false;
            }
        })
    }

    function loaded() {
        pullDownEl = document.getElementById('pullDown');
        pullDownOffset = pullDownEl.offsetHeight;
        pullUpEl = document.getElementById('pullUp');	
        pullUpOffset = 10;
        //pullUpOffset = pullUpEl.offsetHeight;
        myScroll = new iScroll('wrapper', {
            useTransition: true,
            topOffset: pullDownOffset,
            onRefresh: function () {
                //that.maxScrollY = that.wrapperH - that.scrollerH + that.minScrollY;
                //that.minScrollY = -that.options.topOffset || 0;
                //alert(this.wrapperH);
                //alert(this.scrollerH);
                if (pullDownEl.className.match('loading')) {
                    pullDownEl.className = '';
                    pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';}
                if (pullUpEl.className.match('loading')) {
                    pullUpEl.className = '';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载...';
                }
                
                document.getElementById("pullUp").style.display="none";
                document.getElementById("show").innerHTML="onRefresh: up["+pullUpEl.className+"],down["+pullDownEl.className+"],Y["+this.y+"],maxScrollY["+this.maxScrollY+"],minScrollY["+this.minScrollY+"],scrollerH["+this.scrollerH+"],wrapperH["+this.wrapperH+"]";
            },
            onScrollMove: function () {
                document.getElementById("show").innerHTML="onScrollMove: up["+pullUpEl.className+"],down["+pullDownEl.className+"],Y["+this.y+"],maxScrollY["+this.maxScrollY+"],minScrollY["+this.minScrollY+"],scrollerH["+this.scrollerH+"],wrapperH["+this.wrapperH+"]";
                if (this.y > 0) {
                    pullDownEl.className = 'flip';
                    pullDownEl.querySelector('.pullDownLabel').innerHTML = '放开刷新...';
                    this.minScrollY = 0;
                }
                if (this.y < 0 && pullDownEl.className.match('flip')) {
                    pullDownEl.className = '';
                    pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
                    this.minScrollY = -pullDownOffset;
                }
                
                if ( this.scrollerH < this.wrapperH && this.y < (this.minScrollY-pullUpOffset) || this.scrollerH > this.wrapperH && this.y < (this.maxScrollY - pullUpOffset) ) {
                    document.getElementById("pullUp").style.display="";
                    pullUpEl.className = 'flip';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = '放开刷新...';
                } 
                if (this.scrollerH < this.wrapperH && this.y > (this.minScrollY-pullUpOffset) && pullUpEl.className.match('flip') || this.scrollerH > this.wrapperH && this.y > (this.maxScrollY - pullUpOffset) && pullUpEl.className.match('flip')) {
                    document.getElementById("pullUp").style.display="none";
                    pullUpEl.className = '';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载...';
                }
            },
            onScrollEnd: function () {
                document.getElementById("show").innerHTML="onScrollEnd: up["+pullUpEl.className+"],down["+pullDownEl.className+"],Y["+this.y+"],maxScrollY["+this.maxScrollY+"],minScrollY["+this.minScrollY+"],scrollerH["+this.scrollerH+"],wrapperH["+this.wrapperH+"]";
                if (pullDownEl.className.match('flip')) {
                    pullDownEl.className = 'loading';
                    pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...'; 
                    pullDownEl.querySelector('.pullDownLabel').style.position = 'relative'; 
                    pullDownEl.querySelector('.pullDownLabel').style.left = -15+'px'; 
                    $('#pullUp').css('top','50%');
                    scrollHeight = 50;	                   			
                    pullDownAction();	// Execute custom function (ajax call?)
                } 
                if (pullUpEl.className.match('flip')) {
                    pullUpEl.className = 'loading';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Loading...';
                    pullUpEl.querySelector('.pullUpLabel').style.position = 'relative'; 
                    pullUpEl.querySelector('.pullUpLabel').style.left = -15+'px';
                    $('#pullUp').css('top',scrollHeight + '%');
                    scrollHeight = parseInt(scrollHeight) + 20;	
                    pullUpAction();	// Execute custom function (ajax call?)
                }
            }
        });
        
        //setTimeout(function () { document.getElementById('wrapper').style.left = '0'; }, 800);
    }

    document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

    document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
</script>
</body>
</html>