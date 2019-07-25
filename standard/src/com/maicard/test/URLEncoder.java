package com.maicard.test;



public class URLEncoder {
	public static void main(String[] argv){
		try{
		System.out.println(java.net.URLEncoder.encode("http://youbao.changxiu.net/weixin/","UTF-8"));
//		System.out.println(java.net.URLDecoder.decode("p=game_payorder&u=http%3A%2F%2Fpay.wan.360.cn%2F&id=65863720.2425385283805420000.1437225585117.9673&guid=65863720.2425385283805420000.1437225585117.9673&f=http%3A%2F%2Fpay.wan.360.cn%2F%3Fgkey%3Dmir%26skey%3DS68%26plat%3D2%23&c=%E7%A1%AE%E8%AE%A4%E6%8F%90%E4%BA%A4&cId=&t=1437282499776"));
		System.out.println(java.net.URLDecoder.decode("/Charge/UCardChargeInfo.aspx?ts=635771502372748750&current=1&bn=C150906365081425&sn=ff0e8473f48eeb92fb859a630b6f2d67&m=%u5b98%u65b9%u5145%u503c%u51fa%u73b0%u672a%u77e5%u60c5%u51b5%uff0c%u539f%u56e0%uff1a%u8ba2%u5355%u5df2%u63a5%u53d7%uff0c%u6b63%u5728%u5904%u7406%u4e2d","UTF-8"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
