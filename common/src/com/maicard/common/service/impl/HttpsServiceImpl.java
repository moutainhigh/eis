package com.maicard.common.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Service;

import com.maicard.common.service.HttpsService;
import com.maicard.common.service.TrustAnyHostnameVerifier;

@Service
public class HttpsServiceImpl implements HttpsService{
	public String httpsPost(String url,String memo) throws Exception{
		String resultString="";
		try{
			URL paramContext = new URL(url);
			String paramString3=memo;
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new X509TrustManager[] { new X509TrustManager() {  
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
				}  

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
				}  

				public X509Certificate[] getAcceptedIssuers() {  
					return new X509Certificate[0];  
				}  
			} }, new SecureRandom());  
			if (url != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			}
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());  
			HttpsURLConnection cn = (HttpsURLConnection)paramContext.openConnection();
			cn.setDoOutput(true);
			cn.setDoInput(true);
			cn.setRequestMethod("POST");
			cn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
			cn.setConnectTimeout(30000);
			cn.setReadTimeout(18000);
			cn.setRequestProperty("Content-Length", String.valueOf(paramString3.length()));
			cn.connect();
			OutputStream o = cn.getOutputStream();
			o.write(paramString3.getBytes("UTF-8"));
			o.flush();
			o.close();
			InputStreamReader i = new InputStreamReader(cn.getInputStream());
			int respInt = i.read();
			while (respInt != -1) {
				resultString=resultString+((char)respInt);
				respInt = i.read();
			}
		}
		catch(Exception e){System.out.print("出错");}
		//System.out.print(resultString);
		return resultString;
	}	
	@Override
	public String httpsGet(String httpsURL) throws Exception{
		URL myurl = new URL(httpsURL);  
		HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();  
		con.setConnectTimeout(2 * 1000);
		InputStream ins = con.getInputStream();  
		InputStreamReader isr = new InputStreamReader(ins,"UTF-8");  
		BufferedReader in = new BufferedReader(isr);  
		String inputLine; 
		String result="";
		boolean flag=false;
		while ((inputLine = in.readLine()) != null) {  
			if (inputLine.startsWith("{")){
				flag=true;
			}
			if (flag){
				result+=inputLine;
			}
			if (inputLine.endsWith("}")){
				flag=false;
			}
		}
		in.close();  
		return result;
	}
	public static KeyStore getKeyStore(String password, String keyStorePath)  
            throws Exception {  
        // 实例化密钥库  
        KeyStore ks = KeyStore.getInstance("JKS");  
        // 获得密钥库文件流  
        FileInputStream is = new FileInputStream(keyStorePath);  
        // 加载密钥库  
        ks.load(is, password.toCharArray());  
        // 关闭密钥库文件流  
        is.close();  
        return ks;  
    }  
	   public static SSLContext getSSLContext(String password,  
	            String keyStorePath, String trustStorePath) throws Exception {  
	        // 实例化密钥库  
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory  
	                .getInstance(KeyManagerFactory.getDefaultAlgorithm());  
	        // 获得密钥库  
	        KeyStore keyStore = getKeyStore(password, keyStorePath);  
	        // 初始化密钥工厂  
	        keyManagerFactory.init(keyStore, password.toCharArray());  
	  
	        // 实例化信任库  
	        TrustManagerFactory trustManagerFactory = TrustManagerFactory  
	                .getInstance(TrustManagerFactory.getDefaultAlgorithm());  
	        // 获得信任库  
	        KeyStore trustStore = getKeyStore(password, trustStorePath);  
	        // 初始化信任库  
	        trustManagerFactory.init(trustStore);  
	        // 实例化SSL上下文  
	        SSLContext ctx = SSLContext.getInstance("TLS");  
	        // 初始化SSL上下文  
	        ctx.init(keyManagerFactory.getKeyManagers(),  
	                trustManagerFactory.getTrustManagers(), null);  
	        // 获得SSLSocketFactory  
	        return ctx;  
	    }  
	public static void initHttpsURLConnection(String password,  
            String keyStorePath, String trustStorePath) throws Exception {  
        // 声明SSL上下文  
        SSLContext sslContext = null;  
        // 实例化主机名验证接口  
        HostnameVerifier hnv = new TrustAnyHostnameVerifier();  
        try {  
            sslContext = getSSLContext(password, keyStorePath, trustStorePath);  
        } catch (GeneralSecurityException e) {  
            e.printStackTrace();  
        }  
        if (sslContext != null) {  
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext  
                    .getSocketFactory());  
        }  
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);  
    }  
/*	public  String httpsPost(String httpsUrl, String xmlStr) {  
        HttpsURLConnection urlCon = null;  
        String line="";
        try {  
            urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();  
            urlCon.setDoInput(true);  
            urlCon.setDoOutput(true);  
            urlCon.setRequestMethod("POST");  
            urlCon.setRequestProperty("Content-Length",  
                    String.valueOf(xmlStr.getBytes().length));  
            urlCon.setUseCaches(false);  
            //设置为gbk可以解决服务器接收时读取的数据中文乱码问题  
            urlCon.getOutputStream().write(xmlStr.getBytes("gbk"));  
            urlCon.getOutputStream().flush();  
            urlCon.getOutputStream().close();  
            BufferedReader in = new BufferedReader(new InputStreamReader(  
                    urlCon.getInputStream()));    
            while ((line = in.readLine()) != null) {  
                line+=line;
            }  
            
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return line;  
    } */   
	}	
