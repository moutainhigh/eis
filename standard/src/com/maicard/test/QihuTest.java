package com.maicard.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.util.Sslv3SocketFactory;



public class QihuTest {
	public static void main(String[] argv) throws UnsupportedEncodingException{
		String e="__guid";
		String userName="vivian1933";
		String password="test111";
		Protocol.registerProtocol("https", 
				new Protocol("https", new Sslv3SocketFactory(), 443));
		//Cookie cookie = new Cookie(".360.cn", "i360loginName","vivian1933", "/", null,false);
		HttpClient httpclient = new HttpClient();
		//HttpState state = new HttpState();
		//state.addCookie(cookie);
		//httpclient.setState(state);	
		String psw=DigestUtils.md5Hex(password);
        httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		String s="pay.360.cn";
		Date myDate = new Date(System.currentTimeMillis());

		Double aa=(double)System.currentTimeMillis() / 1000;


		BigDecimal a1 = new BigDecimal(aa); 
		long time2=a1.toBigInteger().longValue()+2000;
		Double bb=(double) Math.random();

		BigDecimal b1 = new BigDecimal(bb); 

		Double cc=(double) Math.random();

		BigDecimal c1 = new BigDecimal(cc); 

		Double ff=aa+bb+cc;

		DecimalFormat df = new DecimalFormat("0.000"); 
		String num = df.format(ff); 

		BigDecimal dd = new BigDecimal(ff); 
		String blow="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0";
		long n=(Math.round(Math.random() * 2147483647) ^ jisuan(blow)) * 2147483647;
		String ss =jisuan(s)+"."+n+"."+ num;

		//String ss ="156009789.557321379193028160.1396680191732.4666";
		Double saa2=(double)System.currentTimeMillis();


		BigDecimal urlsa1 = new BigDecimal(saa2); 
		long times=urlsa1.toBigInteger().longValue();
		//String startUrl="https://pay.360.cn/w360/c.html?p=payment&u=https%3A%2F%2Fpay.360.cn%2Findex%2F&id="+ss.toString()+"&guid="+ss.toString()+"&b=firefox&c=3&r=https%3A%2F%2Fpay.360.cn%2Findex&fl=12&t="+times;
		//GetMethod getMethod = new GetMethod(startUrl);
		/*getMethod.addRequestHeader("User-Agent",	blow);	
		try {
			httpclient.executeMethod(getMethod);
			printHeader(getMethod);
			BufferedReader br = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
			String temp = "";
			StringBuffer sb = new StringBuffer(100);
			while((temp = br.readLine()) != null){
				sb.append(temp + "\n");
			}
			System.out.println("urlstart执行结果:"+sb.toString()); 
		}catch(Exception e1){
			e1.printStackTrace();
		}*/
		//String url0 = "https://pay.360.cn/w360/c.html?p=payment&u=https%3A%2F%2Fpay.360.cn%2Findex%2F&id=" + ss.toString()+"&guid="+ss.toString()+"&f=https%3A%2F%2Fpay.360.cn%2Findex%23%23%23&c=%E7%99%BB%E5%BD%95&cId=&t="+a1.toString();
		String url0 = "https://pay.360.cn/w360/s.html?p=payment&u=https%3A%2F%2Fpay.360.cn%2Findex%2F&id=" + ss.toString() + "&guid=" + ss.toString() + "&b=firefox&c=1&r=https%3A%2F%2Fpay.360.cn%2Fdeposit%2Frecharge&fl=11&t=" + a1.toString();
		GetMethod getMethod = new GetMethod(url0);
		getMethod.addRequestHeader("User-Agent",	blow);	
		System.out.println("XXXXXXXXXXXX开始执行url0:" + url0);
		try {
			httpclient.executeMethod(getMethod);
			printHeader(getMethod);
			BufferedReader br = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
			String temp = "";
			StringBuffer sb = new StringBuffer(100);
			while((temp = br.readLine()) != null){
				sb.append(temp + "\n");
			}
			System.out.println("url0执行结果:"+sb.toString()); 
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		String url1 = "https://pay.360.cn/w360/c.html?p=payment&u=https%3A%2F%2Fpay.360.cn%2Findex%2F&id="+ss.toString()+"&guid="+ss.toString()+"&f=https%3A%2F%2Fpay.360.cn%2Findex%23%23%23&c=%E7%99%BB%E5%BD%95&cId=&t="+a1.toString();

		getMethod = new GetMethod(url1);
		getMethod.addRequestHeader("User-Agent",	blow);	
		System.out.println("XXXXXXXXXXXX开始执行url1:" + url1);
		try {
			httpclient.executeMethod(getMethod);
			printHeader(getMethod);
			BufferedReader br = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
			String temp = "";
			StringBuffer sb = new StringBuffer(100);
			while((temp = br.readLine()) != null){
				sb.append(temp + "\n");
			}
			System.out.println("url1执行结果:"+sb.toString()); 
		}catch(Exception e1){
			e1.printStackTrace();
		}
		/*String url2 ="https://pay.360.cn/w360/c.html?p=payment&u=https%3A%2F%2Fpay.360.cn%2Findex%2F&id="+ss.toString()+"&guid="+ss.toString()+"&f=&c=form%3AloginForm&cId=&t="+time2;

		getMethod = new GetMethod(url2);
		getMethod.addRequestHeader("User-Agent",	blow);	
		System.out.println("XXXXXXXXXXXX开始执行url1:" + url1);

		try {
			httpclient.executeMethod(getMethod);
			BufferedReader br = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
			String temp = "";
			StringBuffer sb = new StringBuffer(100);
			while((temp = br.readLine()) != null){
				sb.append(temp + "\n");
			}
			System.out.println("url2执行结果:"+sb.toString()); 
		}catch(Exception e1){
			e1.printStackTrace();
		}*/

		Double url3random=Math.random();
		Double aa2=(double)System.currentTimeMillis();


		BigDecimal url3a1 = new BigDecimal(aa2); 
		long time3=url3a1.toBigInteger().longValue();
		String url3 ="https://login.360.cn/?o=sso&m=getToken&requestScema=https&func=QHPass.loginUtils.tokenCallback&userName="+userName+"&rand="+url3random+"&callback=QiUserJsonP"+time3;
		int ww=jisuan(s);

		//String url = "https://login.360.cn/?o=sso&m=getToken&requestScema=https&func=QHPass.loginUtils.tokenCallback&userName=vivian1933&rand=0.6854330655881165&callback=QiUserJsonP1396505794857";

		getMethod = new GetMethod(url3);
		getMethod.addRequestHeader("User-Agent",	blow);	
		try {
			httpclient.executeMethod(getMethod);
			printHeader(getMethod);
			BufferedReader br = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
			String temp = "";
			StringBuffer sb = new StringBuffer(100);
			while((temp = br.readLine()) != null){
				sb.append(temp + "\n");
			}

			int offset1 = sb.toString().indexOf("{");
			int offset2 = sb.toString().indexOf("}");
			if(offset1 > 0 && offset2 > 0){


				ObjectMapper om = new ObjectMapper();

				String token=om.readTree(sb.toString().substring(offset1, offset2+1)).get("token").asText();
				System.out.println("url3=" + url3);
				System.out.println("url3执行结果:" + sb.toString());

				System.out.println(om.readTree(sb.toString().substring(offset1, offset2+1)).get("token").asText());
				String url4="https://login.360.cn/?o=sso&m=login&requestScema=https&from=pcw_pay&rtype=data&func=QHPass.loginUtils.loginCallback&userName="+userName+"&pwdmethod=1&isKeepAlive=1&token="+token+"&captFlag=1&captId=i360&captCode=&lm=0&validatelm=0&password="+psw+"&r=1396320885199&callback=QiUserJsonP1396320805553";
				System.out.println("url4="+url4); 

				getMethod = new GetMethod(url4);
				getMethod.addRequestHeader("User-Agent",	blow);	
				try {
					httpclient.executeMethod(getMethod);
					printHeader(getMethod);
					BufferedReader br2 = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
					String temp2 = "";
					StringBuffer sb2 = new StringBuffer(1000);
					while((temp2 = br2.readLine()) != null){
						sb2.append(temp2 + "\n");
					}
					System.out.println("url4执行结果:"+sb2.toString()); 
					System.out.println("url4执行结果URLDecode:"+ java.net.URLDecoder.decode(sb2.toString(),"UTF-8")); 

					int offset3 = sb2.toString().indexOf("{");
					int offset4 = sb2.toString().indexOf("}");

					if(offset3 > 0 && offset4 > 0){

						ObjectMapper om2 = new ObjectMapper();
						String stoken=om.readTree(sb2.toString().substring(offset3, offset4+2)).get("s").asText();		
						Double aa3=(double)System.currentTimeMillis();


						BigDecimal url4a1 = new BigDecimal(aa3); 
						long time4=url4a1.toBigInteger().longValue();
						String url5="https://login.360.cn/?o=sso&m=setcookie&requestScema=https&func=QHPass.loginUtils.setCookieCallback&s="+stoken+"&callback=QiUserJsonP"+time4;
						System.out.println("url5="+url5); 
						getMethod = new GetMethod(url5);
						getMethod.addRequestHeader("User-Agent",	blow);	
						httpclient.executeMethod(getMethod);
						printHeader(getMethod);
						br2 = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
						temp2 = "";
						sb2 = new StringBuffer(1000);
						while((temp2 = br2.readLine()) != null){
							sb2.append(temp2 + "\n");
						}
						System.out.println("url5执行结果:" + sb2.toString()); 

						Double aa4=(double)System.currentTimeMillis();


						/*BigDecimal url5a1 = new BigDecimal(aa4); 
						long time5=url5a1.toBigInteger().longValue();
						String url6="https://login.360pay.cn/?o=sso&m=setcookie&requestScema=https&func=QHPass.loginUtils.setCookieCallback&s="+stoken+"&callback=QiUserJsonP"+time5;
						System.out.println("url6="+url6); 
						getMethod = new GetMethod(url6);
						getMethod.addRequestHeader("User-Agent",	blow);	
						httpclient.executeMethod(getMethod);
						printHeader(getMethod);
						br2 = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
						temp2 = "";
						sb2 = new StringBuffer(1000);
						while((temp2 = br2.readLine()) != null){
							sb2.append(temp2 + "\n");
						}
						System.out.println("url6执行结果:" + sb2.toString()); 
*/
						String url7 = "https://pay.360.cn/quser/getLive";
						getMethod = new GetMethod(url7);
						Cookie[] cookies = httpclient.getState().getCookies();
						getMethod.addRequestHeader("User-Agent",	blow);	
						String cookieHeaders = "i360loginName=vivian1933; ";
						for(Cookie cookie : cookies){
							System.out.println("COOKIE:" + cookie.getDomain() + "," + cookie.getPath() + "," + cookie.getName() + "======>" + cookie.getValue());
							if(cookie.getDomain().equals("360pay.cn") || cookie.getDomain().equals(".360.cn") || cookie.getName().equals("__guid")){
								cookieHeaders += cookie.getName() + "=" + cookie.getValue() + "; ";
							}
						}
						cookieHeaders = cookieHeaders.replaceAll("; $","");
						getMethod.addRequestHeader("Cookie", cookieHeaders);
						httpclient.executeMethod(getMethod);
						printHeader(getMethod);
					
						br2 = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
						temp2 = "";
						sb2 = new StringBuffer(1000);
						while((temp2 = br2.readLine()) != null){
							sb2.append(temp2 + "\n");
						}
						System.out.println("url7=" + url7); 
						System.out.println("url7执行结果:"+sb2.toString()); 

						String url8 = "https://pay.360.cn/quser/setLive";
						
						//httpclient.getState().addCookie(cookie);
						PostMethod postMethod = new PostMethod(url8);
						postMethod.addRequestHeader("User-Agent",	blow);	
						postMethod.addRequestHeader("Referer", "https://pay.360.cn/");
						postMethod.addRequestHeader("X-Requested-With", "XMLHttpRequest");
						cookies = httpclient.getState().getCookies();
						//cookieHeaders = "i360loginName=vivian1933; ";
						for(Cookie cookie : cookies){
							System.out.println("COOKIE:" + cookie.getDomain() + "," + cookie.getPath() + "," + cookie.getName() + "======>" + cookie.getValue());
							if(cookie.getDomain().equals("pay.360.cn")){
								cookieHeaders += "; " + cookie.getName() + "=" + cookie.getValue() + "; ";
							}
						}
						cookieHeaders = cookieHeaders.replaceAll("; $","");
						httpclient.getState().clearCookies();
						postMethod.addRequestHeader("Cookie", cookieHeaders);
						httpclient.executeMethod(postMethod);
						printHeader(postMethod);
						br2 = new   BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"UTF-8"));
						temp2 = "";
						sb2 = new StringBuffer(1000);
						while((temp2 = br2.readLine()) != null){
							sb2.append(temp2 + "\n");
						}
						System.out.println("url8=" + url8); 
						System.out.println("url8执行结果:"+sb2.toString()); 

						String url9 = "https://pay.360.cn/quser/getUserInfo";
						getMethod = new GetMethod(url9);
						getMethod.addRequestHeader("User-Agent",	blow);	
						httpclient.getState().clearCookies();
						getMethod.addRequestHeader("Cookie", cookieHeaders);
						httpclient.executeMethod(getMethod);
						printHeader(getMethod);
						br2 = new   BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),"UTF-8"));
						temp2 = "";
						sb2 = new StringBuffer(1000);
						while((temp2 = br2.readLine()) != null){
							sb2.append(temp2 + "\n");
						}
						System.out.println("url9=" + url9); 
						System.out.println("url9执行结果:"+sb2.toString()); 

					}

				}catch(Exception e1){
					e1.printStackTrace();
				}
				finally {

					getMethod.releaseConnection();
				}

			}

		}catch(Exception e1){
			e1.printStackTrace();
		}
		finally {

			getMethod.releaseConnection();
		}


	}
	private static int jisuan(String e) throws UnsupportedEncodingException{


		int t = 0;int n = 0; 
		for (int r = e.length() - 1; r >= 0; r--) {

			int ns=e.charAt(r);


			t = (t << 6 & 268435455) + ns + (ns << 14);
			ns = t & 266338304;
			t = ns != 0 ? t^ns >> 21 : t;



			//  t = (t << 6 & 268435455) + i + (i << 14);
		}

		return t;

	}
	
	private static void printHeader(HttpMethod method){
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(">>>>>>" + method.getName() + " " + method.getRequestHeader("Host").getValue() + 	method.getPath());
		Header[] headerx = method.getRequestHeaders();
				for (int i = 0; i < headerx.length; i++) {
			System.out.println("Name: " + headerx[i].getName() + " : " + headerx[i].getValue());
		}
		System.out.println("* * * * * * * * * * * * * * * * ");
		System.out.println("ResponseHeaders Information :");
		Header[] headers = method.getResponseHeaders();
		headers = method.getResponseHeaders();
		for (int i = 0; i < headers.length; i++) {
			System.out.println("Name: " + headers[i].getName() + " : " + headers[i].getValue());
		}
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
	}
}
