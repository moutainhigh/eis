
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-cn" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="keywords" content="财付通,支付/付款/汇款,在线/网上,我的钱包/安全/方便/快捷,手机充值/信用卡还款/送红包/酒店/机票/预定/Q币打折/购物/优惠" />
<meta name="description" content="财付通，在线支付专家，带给您安全快捷的网上付款体验。" />
<meta name="unlogin" content="true" />
<title>财付通 会支付 会生活</title>
<link type="text/css" href="https://img.tenpay.com/v2.0/css/global_v2.css?v=20131216" rel="stylesheet" />
<link href="https://img.tenpay.com/zft/css/style_1.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="https://img.tenpay.com/v2.0/css/action_suc.css?v=20150113" type="text/css" />
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/qq_ctrl.js?v=20150122"></script>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/rsa-sha1.js?v=20130404"></script>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/base_control.js?v=20140520"></script>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/nav_control.js?t=20130110"></script>

<script type="text/javascript">

</script>
<style type="text/css">
<!--
.style1 {color: #FF6600}
.bank-notice:after{content:".";display:block;clear:both;height:0;visibility:hidden;}
.bank-notice{display:inline-block;}
.bank-notice{margin-bottom:100px;display:block;margin-left:70px;}
.bank-notice .ico-error{background: url("https://img.tenpay.com/res/tenpay_v1/component/box/box.png") no-repeat;display: inline-block;width:32px;height:32px;float:left;}
.bank-notice .bd{float:left;margin-left:20px;}
.bank-notice .bd p{color:#666;}
-->
</style>
</head>
<script type="text/javascript" src="https://img.tenpay.com/zft/js/GetInfo.js"></script>
<script type="text/javascript" src="https://img.tenpay.com/zft/js/CHttpRequest.js"></script>
<script type="text/javascript" src="https://img.tenpay.com/zft/js/common.js"></script>
<script type="text/javascript" src="https://img.tenpay.com/childacc/script/CSelfUrl.js"></script>
<script type="text/javascript" src="https://img.tenpay.com/childacc/script/CHttpRequest.js"></script>
<script type="text/javascript" src="https://img.tenpay.com/childacc/script/CCookie.js"></script>
<script type="text/javascript" src="https://img.tenpay.com/childacc/script/CXml.js"></script>

<script type="text/javascript" src="https://img.tenpay.com/zft/js/common.js"></script>
<script type="text/javascript">
<!--
var gRedirectUrl="";
var g_xml = null;
function OpenServiceResult(state,sts,str)
{
  switch(state)
  {
  case 0://initting
//    break;
  case 1://reading
//    break;
  case 2://readed
//    break;
  case 3://interact
    break;
  case 4://complete
    switch(sts)
    {
    case 200://success
      var g_xml = new CXml(str);
      var cd = parseInt(g_xml.GetValue("retcode"),10);
      switch(cd)
      {
      case 0:
        document.getElementById("cmbactivity").style.display = "";
          return;
      case 4:
        return;
      case 5:
        document.getElementById("cmbactivity").style.display = "";
        return;
      default:
          return;
      }
      break;
    case 404://notfound
    default:
      break;
    }
  default:
    break;
  }
}

function Init()
{
  document.getElementById("table_paipai_use").style.display = "none";
  if(document.getElementById("td_errinfo").innerHTML == '恭喜您，充值成！' || document.getElementById("td_errinfo").innerHTML == '恭喜您，充值成功！')
  {
    document.getElementById("td_errinfo").innerHTML = '恭喜您，财付通账户充值成功！';

    document.getElementById("manage_icon").style.display = "";

    document.getElementById("youhuizhuanqu").style.display = "";

    var strUin = "";
    strUin = g_CSelfUrl.GetPara("uin");
      if(g_CSelfUrl.GetPara("bankname")=="cmb" && checkUin(strUin, "Q"))
      {
      /*var http = new CHttpRequest(OpenServiceResult,true,2);
      http.Get("/cgi-bin/v1.0/cmbactivity.cgi", "tid=00008020&uin="+strUin);*/
      }
  }
  else if(document.getElementById("td_errinfo").innerHTML == '您没有注册密码保护,请与客服联系.！')
  {
    document.getElementById("td_errinfo").innerHTML = '您未注册密码保护资料，请您登陆<a href="https://www.tenpay.com/zft/cs/cs.shtml?tid=1">申诉系统</a>填写找回支付密码申诉表，谢谢！';
  }else if (document.getElementById("error-info").innerHTML == '交易受限，请确认您有开通此交易权限，详情请咨询财付通') {
    document.getElementById("error-info").innerHTML = '您的操作过于频繁，为保证您的账户安全，建议您稍候再尝试';
  }
}

-->
</script>
<body class="no-nav">
<script type="text/javascript">TFL.header.initLogin();</script>
<!--[if IE 6]>
<script type="text/javascript">
document.execCommand("BackgroundImageCache", false, true);
</script>
<![endif]-->
<div id="header">
  <div class="logo-contianer">
  <div class="logo-new">
    <h1 id="logo" onmousedown="TFL.Ping.clickStat('TENPAY.OLD.DAOHANG.LOGO');">
      <a onclick="TFL.Ping.clickStat('TENPAY.OLD.DAOHANG.LOGO')" href="https://www.tenpay.com/app/v1.0/switch_web.cgi?direct=v2&target=https%3A%2F%2Fwww.tenpay.com%2Fapp%2Fv1.0%2Fcftaccount.cgi" hidefocus="true" title="财付通 - 会支付 会生活">财付通<sub>会支付 会生活</sub></a>
      <div class="tenpay_update">
        <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="https://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="200" height="60">
          <param name="movie" value="https://img.tenpay.com/v2.0/img/tenpay_update_new.swf" />
          <param name="quality" value="high" />
          <param name="wmode" value="opaque" />
          <param name="allowScriptAccess" value="always">
          <embed src="https://img.tenpay.com/v2.0/img/tenpay_update_new.swf" wmode="opaque" quality="high"  pluginspage="https://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="200" height="60" allowScriptAccess="always"></embed>
        </object>
      </div>
    </h1>
  </div>
	<ul class="guide" id="guide">
  <li class="tenpay_new"><a onclick="TFL.Ping.clickStat('TENPAY.OLD.DAOHANG.TIYANNEW')" href="https://www.tenpay.com/app/v1.0/switch_web.cgi?direct=v2&target=https%3A%2F%2Fwww.tenpay.com%2Fapp%2Fv1.0%2Fcftaccount.cgi">体验新版</a></li>
  <li class="drop-trigger first-one life-drop">|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.LIFE')" href="http://life.tenpay.com/" target="_blank">生活好帮手</a>
	<div class="menu-wrap" style="display:none;">
	 <ul>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.SHOUJI')" href="https://www.tenpay.com/cgi-bin/v1.0/mchpassport.cgi?tid=mchpassport_jump&logout_url=http://life.tenpay.com/mobile/&cmdno=91&chnid=00000006&tourl=http://life.tenpay.com/cgi-bin/common/portalloginret.cgi&resulturl=http://life.tenpay.com/mobile/" target="_blank">手机话费充值</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.XINYONKA')" href="http://life.tenpay.com/creditcard/" target="_blank">信用卡还款</a></li>
    <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.CAIPIAO')" href="http://life.tenpay.com/v2/app/detail.shtml?appId=0000000708" target="_blank">彩票卖场</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.JIAOFEI')" href="https://www.tenpay.com/cgi-bin/v1.0/mchpassport.cgi?tid=mchpassport_jump&logout_url=http://portal.tenpay.com/life/jiaofei/&cmdno=91&chnid=00000006&tourl=http://life.tenpay.com/cgi-bin/common/portalloginret.cgi&resulturl=http://portal.tenpay.com/life/jiaofei/" target="_blank">水电煤缴费</a></li>
    <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.COMM')" href="http://life.tenpay.com/v2/app/detail.shtml?appId=0000001264" target="_blank">话费宽带</a></li>
    <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.JIPIAO')" href="http://jipiao.tenpay.com/" target="_blank">机票订购</a></li>
    <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.FANGDAI')" href="https://www.tenpay.com/v2.0/main/mortgage/" target="_blank">还房贷</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.YOUCHU')" href="https://www.tenpay.com/v2/account/pay/postal.shtml" target="_blank">邮政网上汇款</a></li>
    <!--<li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.CHEPIAO')" href="http://chepiao.tenpay.com/" target="_blank">火车票代购</a></li>-->
    <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.JIUDIAN')" href="http://life.tenpay.com/hotel/" target="_blank">特价酒店</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.YOUXI')" href="https://www.tenpay.com/cgi-bin/v1.0/mchpassport.cgi?tid=mchpassport_jump&logout_url=http://life.tenpay.com/game/&cmdno=91&chnid=00000006&tourl=http://life.tenpay.com/cgi-bin/common/portalloginret.cgi&resulturl=http://life.tenpay.com/game/" target="_blank">游戏充值</a></li>	  
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.PAIPAI')" href="http://www.paipai.com/" target="_blank">拍拍购物</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.QBQD')" href="http://life.tenpay.com/v2/app/detail.shtml?appId=0000000911" target="_blank">充值Q币Q点</a></li>
    <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.QQ_FUWU')" href="http://life.tenpay.com/qqservices/" target="_blank">购买QQ服务</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.HONGBAO')" href="http://life.tenpay.com/gift/" target="_blank">发红包</a></li>
	  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.BAOXIAN')" href="https://www.tenpay.com/cgi-bin/v1.0/mchpassport.cgi?tid=mchpassport_jump&logout_url=http://life.tenpay.com/baoxian/&cmdno=91&chnid=00000006&tourl=http://portal.tenpay.com/cgi-bin/common/portalloginret.cgi&resulturl=http://life.tenpay.com/baoxian/" target="_blank">保险超市</a></li>
    <!-- <li><a target="_blank" href="http://life.tenpay.com/v2/?ADTAG=TENPAY.HEADER.DAOHANG.LIFEHOME">更多生活服务</a></li> -->
	 <!-- <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.LINHUAQIAN')" href="http://pay-client.qq.com/cgi-bin/v1.0/qzone/qzone_jump_2.cgi" target="_blank">赚零花钱！</a></li>-->
	 </ul>
	 </div>
	</li>
  <li class="drop-trigger four-char short-drop">|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.MENU.SANGJIAFUWU')" href="http://mch.tenpay.com/" target="_blank">商家</a>
		<div class="menu-wrap" style="display:none;">
		 <ul>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.QIYEBAN')" href="http://mch.tenpay.com/" target="_blank">财付通企业版</a></li>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.JICHENG')" href="http://mch.tenpay.com/market/index.shtml" target="_blank">集成财付通</a></li>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.CANPING_FUWU')" href="http://mch.tenpay.com/market/ps_index.shtml" target="_blank">产品及服务</a></li>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.HANGYE_JIEJUE_FANGAN')" href="http://mch.tenpay.com/market/is_index.shtml" target="_blank">行业解决方案</a></li>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.YINGXIAO_GONGJU')" href="http://mch.tenpay.com/market/st_index.shtml" target="_blank">营销工具</a></li>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.CXSJ')" href="http://union.tenpay.com/trust_mch/" target="_blank">诚信商家</a></li>
		  <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.SANGJIA_TAOCAN')" href="http://union.tenpay.com/set_meal_charge/" target="_blank">商家套餐</a></li>
		 </ul>
		 </div>
	</li>
	<li>|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.LICAI')" href="http://money.tenpay.com/" target="_blank">理财专区</a><span class="icon-span"></span></li>

	<!--<li>|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.WEBWALLET')" href="http://wallet.tenpay.com/web/" target="_blank">QQ钱包</a><span class="icon-span"></span></li>-->
  <li>|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.FANKUI')" href="http://support.qq.com/cgi-bin/beta2/titlelist_simple?pn=0&amp;order=3&amp;fid=127" target="_blank">反馈</a></li>
  <li class="drop-trigger four-help">|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.BANGZHU')" href="http://help.tenpay.com/" target="_blank">帮助</a>
    <div class="menu-wrap" style="display:none;">
     <ul>
      <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.KEFU')" href="http://kf.qq.com/special_auto/tenpay.html" target="_blank">腾讯客服</a></li>
      <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.SHENSU')" href="https://www.tenpay.com/v2/cs/index.shtml" target="_blank">自助申诉</a></li>
      <li><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.BANGZHU')" href="http://help.tenpay.com/" target="_blank">帮助中心</a></li>
     </ul>
    </div>
  </li>
  <li>|&nbsp;&nbsp;<span class="mblog"><!--微博图标--></span><a href="http://t.qq.com/tenpay" target="_blank">官方微博</a></li>
  <!--<li>|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.SHENSU')" href="https://www.tenpay.com/cgi-bin/v1.0/jump.cgi?u1=/zft/cs/index.shtml" target="_blank">申诉</a></li>
  <li>|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.DAOHANG.KEFU')" href="https://www.tenpay.com/v2.0/inc/header_service_link.html" target="_blank">客服</a></li>-->
	<!--li class="last-one">|<a href="http://qbar.qq.com/tenpay/r/?all" target="_blank">论坛</a></li-->
</ul>
<div class="action">
<span id="snNoLog"><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.FUNC.ZHUCE')" href="https://www.tenpay.com/zft/wap/new_register.shtml?cdw_click_link_id=20062001">注册</a>|<a onclick="TFL.Ping.clickStat('TENPAY.HEADER.FUNC.DENGLU')" href="https://www.tenpay.com/zft/login.shtml">登录</a></span>
<span id="snLog">
  <span id="head_true_name"></span><span id="snCert" onclick="TFL.Ping.clickStat('CERTIFICATE.MYACCOUNT.RIGHT')"></span><a onclick="TFL.Ping.clickStat('QUIT.MYACCOUNT.RIGHT')" href="/cgi-bin/v1.0/logout.cgi">退出</a></span>|<a onclick="TFL.Ping.clickStat('SAVE.MYACCOUNT.RIGHT')" class="recharge"  href="https://www.tenpay.com/v2/account/charge/ydt.shtml?ADTAG=CHARGE.TENPAY.HEADER.CHONGZHI" title="">充值</a>
</div>
<div class="action" id="special-action"><span><a href="https://www.tenpay.com/">返回财付通首页</a>|<a href="https://www.tenpay.com/">帮助中心</a></span></div>
  </div>
  <div id="main-nav">
	 <div class="nav-wrapper">
		<ul>
		  <li id="account_g"><a onclick="TFL.Ping.clickStat('TENPAY.OLD.DAOHANG.MYCFT')" href="https://www.tenpay.com/app/v1.0/switch_web.cgi?direct=v2&target=https%3A%2F%2Fwww.tenpay.com%2Fapp%2Fv1.0%2Fcftaccount.cgi"><span>我的财付通</span></a></li>
		  <li id="trade_g"><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.MENU.JIAOYIGUANGLI')" href="https://www.tenpay.com/v2/trade/trade_mgr.shtml"><span>交易管理</span></a></li>
		  <li id="favourable_g"><a onclick="TFL.Ping.clickStat('TENPAY.OLD.DAOHANG.YOUHUI')" href="http://juhui.tenpay.com/"><span>优　惠</span></a></li>
		  <li id="gathering_g"><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.MENU.SHOUKUAN')" href="https://www.tenpay.com/v2/account/receive/index.shtml"><span>收　款</span></a></li>
      <li id="pay_g"><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.MENU.FUKUAN')" href="https://www.tenpay.com/v2/account/pay/paycard.shtml"><span>付　款</span></a></li>
		  <li id="safement_g"><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.MENU.ANQUANZHONGXIN')" href="https://www.tenpay.com/app/v1.0/switch_web.cgi?direct=v2&target=https%3A%2F%2Fwww.tenpay.com%2Fv2%2Fsafe%2Fsafe_check.shtml"><span>安全中心</span></a></li>
		  <li id="service_g" class="lastNav"><a onclick="TFL.Ping.clickStat('TENPAY.HEADER.MENU.ZIZHANGHU')" href="https://www.tenpay.com/v2.0/main/gaccnt.shtml"><span>子账户</span></a></li>
		</ul>
	</div>
  </div>
  <script type="text/javascript">
	TFL.header.initHeader();
	TFL.header.initHeaderDropdown( 'guide','drop-trigger','hovSty' );
  </script>
</div>

    <table width="910" border="0" cellpadding="0" cellspacing="0" align="center" id="big-table">
    <tr>
    <td valign="top">
  <table width="910"><tr><td height="28"></td></tr></table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
  <table width="100%" height="100" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td valign="top">
    <table width="700" border="0" align="left" cellpadding="0" cellspacing="0">
        <tr>
          <td align="center">&nbsp;</td>
          <td align="center">&nbsp;</td>
          <td height="30" class="font_14b" id="td_errinfo2">&nbsp;</td>
        </tr>
        <tr>
          <td width="41" align="center">&nbsp;</td>
          <td width="79" align="center" valign="top"><img src="https://img.tenpay.com/v2.0/img/commonTip.gif"></td>
          <td width="727" class="font_14b" id="td_errinfo" style="padding-top:5px;">
          <div id="error-info">您正在进行的交易存在较大风险，为了您的资金安全，您暂时不能支付此次交易，您可以拨打财付通热线0755-86013860咨询详情。</div>
            <div style="margin-top:15px;">
            <table width="100%" id="tr_balance" style="display:none; font-weight:normal;" >
                <tr>
                    <td align="left" id="td_balance"><a href=/cgi-bin/v1.0/check_detail.cgi class="blue_font_12a">查看充值明细&#187;</a></td>
                </tr>
            </table>

            <table width="100%" id="table_paipai_list" style="display:none; font-weight:normal;" >
                <tr>
                    <td align="left" style="line-height:20px;">
                    <span class="style1">
                        QQ用户成功充值1分钱（仅限1分钱），即可获得68元拍拍购物红包，<a target="_blank" href="http://party.paipai.com/200610/quanhang/tequan.shtml?hb=01?ADTAG=701.4.5" class="blue_font_12a">去红包卖场挑好货&#187;</a>
                  </span>
                    </td>
                </tr>
            </table>

            <table width="100%" id="table_paipai_use" style="display:none; font-weight:normal;" >
                <tr>
                    <td align="left">
                        <span class="style1">
                        红包发送失败，请点击<a href="https://www.tenpay.com/issuer/gwq.shtml" class="blue_font_12a">再次领取&#187;</a>
                        </span>
                    </td>
                </tr>
            </table>

            <table id="manage_icon" style="display:none; font-weight:normal;" width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left"><a href=/cgi-bin/v1.0/manage_icon_page.cgi class="blue_font_12a">点亮我的财付通图标&#187;</a></td>
                </tr>
            </table>

            <table id="youhuizhuanqu" width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" style="display:none; font-weight:normal;">
                <tr>
                  <td style="line-height:20px;color:#333333;" align="left;">
                        <a href="http://bank.qq.com/act/zcft/index.shtml?posid=133&actid=115&opid=13&whoid=10" class="blue_font_12a" target="_blank">财付通优惠专区现已开放，强烈推荐中&#187;</a>
                  </td>
                </tr>
            </table>

            <table id="cmbactivity" width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" style="display:none;font-weight:normal;">
                <tr>
                    <td style="line-height:20px;color:#333333;" align="left">

                        <a href="/issuer/cmbactivity.shtml" class="blue_font_12a" target="_blank">现在招行用户可以免费领取“QQ一重大礼包”，包含QQ会员、QQ红钻及QQ靓号等服务，请点击领取。</a>          </td>
                </tr>
            </table>
            </div>
          </td>
        </tr>
       </table>
    <table width="100%"  border="0" cellspacing="0" cellpadding="6">
        <tr>
          <td align="center"></td>
        </tr>
      </table></td>
    </tr>
  </table>

  </td>
  </tr></table>
  </td>
  </tr>
  </table>
  <div id=CTrans_DJL style="display:none" > </div>


   <!-- 对充值成功页面的特殊处理 -->
  <div id="wrapper">
    <!--充值成功 [[-->
    <div class="action-suc" id="action-suc" style="display:none;">
      <span class="suc-icon"></span>
      <div class="bd">
        <strong>充值成功，资金已转入您的财付通账户。</strong>
      </div>
      <div class="ft">
        <span class="low-blue little-pd"><button onclick="TFL.Ping.clickStat('TENPAY.CHARGE.SUCCESS.ZHANGHUSHOUYE');window.location.href='https://www.tenpay.com/cgi-bin/v1.0/check_balance.cgi';">查看账户余额</button></span>
        <a onclick="TFL.Ping.clickStat('TENPAY.CHARGE.SUCCESS.CHONGZHI')" href="https://www.tenpay.com/cgi-bin/v1.0/show_save.cgi#0">继续给您的账户充值</a>
      </div>
    </div>
    <!--充值成功 ]]-->

    <!--银行维护公告 [[-->
    <div id="notice_bank" class="action-suc bank-notice" style="display:none">
      <span class="ico-error"></span>
      <div class="bd">
        <strong id="notice_title"><span>招商银行</span>系统维护，网上支付服务暂停，请稍候交易或使用其他支付方式。</strong>
        <p id="notice_content">维护时间为<span>7月29日24:00</span>至<span>7月30日4:00</span>，对您造成不便，敬请谅解。</p>
      </div>
    </div>
    <!--银行维护公告 ]]-->
  </div>
  <!-- wrapper -->
 <script type="text/javascript">
 var errinfo = document.getElementById('error-info');
  var errorInfo = (null != errinfo && errinfo.firstChild) ? document.getElementById('error-info').firstChild.nodeValue : "";
  if (errorInfo.indexOf('[1086]') != -1) {
    var bigTable = document.getElementById('big-table');
    var btnWrap = document.getElementById('btn-wrap');
    var rechargeWrap = document.getElementById('wrapper');
    var actionSuc = document.getElementById('action-suc');
    if(bigTable) bigTable.style.display = 'none';
    if(btnWrap) btnWrap.style.display = 'none';
    if (rechargeWrap) rechargeWrap.style.display = 'block';
    if ( actionSuc ) actionSuc.style.display = 'block';
    TFL.Ping.load("/CHARGE/SUCCESS.SHTML", {start:true, tag:"PATH_CHARGE_SUCCESS"});
  }else{
    TFL.Ping.load("/NOTICE/NOTICE.SHTML", {start:true, tag:"PATH_NOTICE"});
  }
  if(/\[20621300\]/.test(errinfo.innerHTML) && /\/cgi-bin\/v1\.0\/bank_repay_callback\.cgi/.test(window.location.href)){
   errinfo.innerHTML = '[20621300]您的还款请求银行正在处理中，请稍后直接查询还款记录。<br /><span style="font-weight:normal; font-size:12px; line-height:18px;">若没有还款记录，请咨询财付通客服电话：0755-86013860。</span><br /><span  class="high-blue pd-10" style="margin-top:10px;"><button onclick="window.location.href=\'https://www.tenpay.com/v2.0/main/creditcard/portal_creditcard.shtml?url=portal_rpm_query.shtml\'" type="button">查询还款记录</button></span>';
  }
  if(/\[87513002\]/.test(errinfo.innerHTML) && /\/cgi-bin\/v1\.0\/bank_repay_callback\.cgi/.test(window.location.href)){
   errinfo.innerHTML = '支付成功，您的信用卡还款申请已提交给发卡银行进行处理。<br />请注意查询到账信息。';
  }
  if(/\[10012\]/.test(errinfo.innerHTML)){
    var flag = false;
    if(typeof noticeList == "undefined"){
        noticeList = {};
    }
    for(banktype in noticeList) {
        if(noticeList.hasOwnProperty(banktype) && errinfo.innerHTML.indexOf("[" + banktype + "]") > 0) {
            flag = true;
            break;
        }
    }
    if(flag){
        TFL.dom.$("big-table").style.display = "none";
        TFL.dom.$("notice_bank").style.display = "block";
        TFL.dom.$("notice_title").innerHTML = noticeList[banktype].title;
        TFL.dom.$("notice_content").innerHTML = noticeList[banktype].content.replace(/\s/g, '&nbsp;');
    }else{
        errinfo.innerHTML = "[10012]您使用的当前银行系统升级维护中，暂时无法提供服务。银行正紧急处理。<br />请选择其他银行支付，或稍候再试。如有不便，敬请谅解。";
    }
  }
  </script>
 <!-- 对充值成功页面的特殊处理 结束 -->
  <div id="inner-footer">
  <div id="foot-wrap">
    <div id="foot-links"><a href="http://help.tenpay.com/cgi-bin/helpcenter/help_center.cgi?id=1&amp;type=0">关于财付通</a>|<a href="http://service.qq.com/category/tenpay.html" target="_blank">客服中心</a>|<a href="http://help.tenpay.com/">帮助中心</a>|<a href="https://www.tenpay.com/zhaopin/" target="_blank">诚征英才</a>|<a href="http://help.tenpay.com/cgi-bin/helpcenter/help_center.cgi?id=2&amp;type=1">账户安全</a>|<a href="http://help.tenpay.com/helpcenter/contactus.shtml">联系我们</a></div>
    <p id="copyright"><span class="copy-info">Copyright &copy; 2005 – <script type="text/javascript">document.write(new Date().getFullYear())</script> Tenpay. All Rights Reserved&nbsp;&nbsp;财付通公司 版权所有</span></p>
  </div>
</div>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/CCookie.js?v=20100225"></script>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/CSelfUrl.js?v=20100225"></script>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/CHttpRequest.js?v=20100225"></script>
<script type="text/javascript" src="https://img.tenpay.com/v2.0/js/CXml.js?v=20100225"></script>
<script type="text/javascript" src="https://img.tenpay.com/helper/cfthelper.js?v=20100225"></script>
<script type="text/javascript">g_CCftHelper.Fetch();</script>

<!--<script type="text/javascript">
  var _onload = window.onload;
  window.onload = function() {
    if (typeof(_onload)=="function") _onload();
	var script = document.createElement("SCRIPT");
	script.type = "text/javascript";
	script.src = "/zft/js/ping_tenpay.ziped.js";
	document.body.appendChild(script);
	var pgvInter = window.setInterval(function() {
		if(typeof(pgvMain) == 'function') {
			pgvMain();
		}
		window.clearInterval(pgvInter);
	}, 100);
  }
</script>-->

<script type="text/javascript">
  function __stat(){
    var script = document.createElement("SCRIPT");
    script.type = "text/javascript";
    script.src ="https://www.tenpay.com/zft/js/ping_tcss_https_src-min.js?v=20120719";// "/zft/js/ping_tenpay.ziped.js?v=201000916";
    document.body.appendChild(script);
    var pgvInter = window.setInterval(function() {
      if(typeof(pgvMain) == 'function') {
        pgvMain();
      }
      if(typeof(pgvWatchClick) == 'function') {
          document.documentElement.onclick = function () {pgvWatchClick({coordinateId: 'menu'})};
      }
      window.clearInterval(pgvInter);
    }, 100);
    if(document.getElementById("nav"))
    {
      var as = document.getElementById("nav").getElementsByTagName("a");
      for (var i=0;i<as.length;i++)
      {
        as[i]._id = "LINK_"+(i+1);
        as[i].onclick = function () {pgvSendClick({hottag:"TENPAY.INDEX.NAV."+this._id});};
      }
    }
  }
  window.attachEvent("onload", __stat);
</script>

</body>
<script type="text/javascript">
  Init();
  GetInfo(document.getElementById("td_errinfo").innerHTML);
</script>
</html>