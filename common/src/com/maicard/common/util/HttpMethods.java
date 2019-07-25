package com.maicard.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpMethods {

	protected static final Logger logger = LoggerFactory.getLogger(HttpMethods.class);


	/* 设置标准信息 例如GB2312，不自动跳转等等 */
	private static void setStandardInfo(HttpMethod httpMethod) {
		httpMethod.setFollowRedirects(false);
		httpMethod.getParams().setContentCharset("GB2312");

		httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		//httpMethod.getParams().setSoTimeout(3000);//(HttpMethodParams.SO_TIMEOUT, 3000);
		
		httpMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.2; zh-CN; rv:1.8.0.4) Gecko/20060508 Firefox/1.5.0.4");
		httpMethod.addRequestHeader("Accept",
				"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		httpMethod.addRequestHeader("Accept-Language", "zh-cn,en;q=0.5");
		httpMethod.addRequestHeader("UA-cpu", "x86");
		httpMethod.addRequestHeader("Connection", "Keep-Alive");
		httpMethod.addRequestHeader("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
		httpMethod.addRequestHeader("Accept-Encoding", "deflate");

		// httpMethod.addRequestHeader("Referer", "http://passport.bjmcc.net/passport/M-Zone/mzone_login.jsp");
	}

	/* 打印Http头信息 */
	public static void printHeadersInfo(HttpMethod httpMethod) {
		Header[] headerx = httpMethod.getRequestHeaders();
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("RequestHeaders Information :");
		logger.debug("URL:" + 		httpMethod.getPath());
		for (int i = 0; i < headerx.length; i++) {
			logger.debug("Name: " + headerx[i].getName() + " : " + headerx[i].getValue());
		}
		logger.debug("* * * * * * * * * * * * * * * * ");
		logger.debug("ResponseHeaders Information :");
		Header[] headers = httpMethod.getResponseHeaders();
		for (int i = 0; i < headers.length; i++) {
			logger.debug("Name: " + headers[i].getName() + " : " + headers[i].getValue());
		}
		logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");

	}



	/* get方法获取网页信息 */
	public static String getData(HttpClient httpClient, String getURL, String[][] manuHeader) throws Exception {
		logger.debug("GET: " + getURL);
		GetMethod getMethod = new GetMethod(getURL);
		HttpMethods.setStandardInfo(getMethod);
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				getMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(getMethod);
			printHeadersInfo(getMethod);
			if (statusCode == HttpStatus.SC_OK)
				return getMethod.getResponseBodyAsString();
			Header header = getMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return "";


				GetMethod tempMethod = new GetMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				if(manuHeader != null && manuHeader.length > 0){
					for(int i=0; i < manuHeader.length; i++){
						tempMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
					}
				}
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK) {
						// printHeadersInfo(tempMethod);
						return tempMethod.getResponseBodyAsString();
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("HttpMethods.java中String getData()中抛出Exception错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} // end of while
		} catch (Exception e) {
			logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			//			printHeadersInfo(getMethod);
			getMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}
	/* get方法获取网页信息 */
	public static String[] getLocationAndData(HttpClient httpClient, String getURL, String[][] manuHeader) throws Exception {
		String[] returnValue = new String[2];
		logger.debug("GET: " + getURL);
		GetMethod getMethod = new GetMethod(getURL);
		HttpMethods.setStandardInfo(getMethod);
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				getMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(getMethod);
			// printHeadersInfo(getMethod);
			//Header header0 = getMethod.getResponseHeader("location");
			if (statusCode == HttpStatus.SC_OK){
				returnValue[0]  = "";
				returnValue[1] = getMethod.getResponseBodyAsString();
				return returnValue;
			}
			logger.debug("First try fetch location...");
			Header header = getMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return null;

				logger.debug("location changed to: " + location);
				GetMethod tempMethod = new GetMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				if(manuHeader != null && manuHeader.length > 0){
					for(int i=0; i < manuHeader.length; i++){
						tempMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
					}
				}
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					//header = tempMethod.getResponseHeader("Location");
					if (statusCode == HttpStatus.SC_OK) {
						returnValue[0]  = location;
						returnValue[1] = tempMethod.getResponseBodyAsString();
						return returnValue;
					}
				} catch (Exception e) {
					logger.error("HttpMethods.java中String getData()中抛出Exception错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} // end of while
		}  catch (Exception e) {
			logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			//			printHeadersInfo(getMethod);
			getMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}



	public static String[] getCookieAndData(HttpClient httpClient, String getURL, String[][] manuHeader) throws Exception {
		logger.debug("GET: " + getURL);
		String[] result = new String[2];
		GetMethod getMethod = new GetMethod(getURL);
		HttpMethods.setStandardInfo(getMethod);
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				getMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK){
				if(getMethod.getResponseHeader("Set-Cookie") != null){
					result[0] = getMethod.getResponseHeader("Set-Cookie").getValue();
				}
				result[1] = getMethod.getResponseBodyAsString();
				//	printHeadersInfo(getMethod);		
				return result;
			}
			Header header = getMethod.getResponseHeader("location");
			if(getMethod.getResponseHeader("Set-Cookie") != null){
				logger.debug("Got cookie before location changed.");
				result[0] = getMethod.getResponseHeader("Set-Cookie").getValue();
			}
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null){
					location = header.getValue();

				} else {
					return null;
				}
				logger.debug("Location changed to " + location);
				GetMethod tempMethod = new GetMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK) {
						String cookieContent = "";
						Header cookie = tempMethod.getResponseHeader("Set-Cookie");
						if(cookie != null){
							logger.debug("Got last cookie" + tempMethod.getResponseHeader("Set-Cookie").getValue());
							cookieContent = cookie.getValue();
							result[0] = cookieContent;
						}
						//	printHeadersInfo(tempMethod);		

						result[1] = tempMethod.getResponseBodyAsString();
						return result;						//return tempMethod.getResponseBodyAsString();
					}

				} catch (Exception e) {
					logger.error(e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} // end of while

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			//			printHeadersInfo(getMethod);
			getMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}



	/* get方法获取字节内容 */
	public static byte[] getByteData(HttpClient httpClient, String getByteURL) throws Exception {
		GetMethod getMethod = new GetMethod(getByteURL);
		HttpMethods.setStandardInfo(getMethod);
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(getMethod);
			printHeadersInfo(getMethod);
			if (statusCode == HttpStatus.SC_OK){
				return getMethod.getResponseBody();
			}
			Header header = getMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return null;
				GetMethod tempMethod = new GetMethod(location);
				setStandardInfo(tempMethod);
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK) {
						return tempMethod.getResponseBody();
					}

				} catch (Exception e) {
					logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			} // end of while
		} catch (Exception e) {
			logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			// printHeadersInfo(getMethod);
			getMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}

	/* 以制定头部信息 get方法获取字节内容 */
	public static byte[] getByteDataWithHeader(HttpClient httpClient, String getByteURL, String[][] manuHeader) throws Exception {
		GetMethod getMethod = new GetMethod(getByteURL);
		HttpMethods.setStandardInfo(getMethod);
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				getMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(getMethod);
			// printHeadersInfo(getMethod);
			if (statusCode == HttpStatus.SC_OK)
				return getMethod.getResponseBody();
			Header header = getMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return null;
				GetMethod tempMethod = new GetMethod(location);
				setStandardInfo(tempMethod);
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK) {
						return tempMethod.getResponseBody();
					}
				} catch (Exception e) {
					logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} // end of while
		}  catch (Exception e) {
			logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			// printHeadersInfo(getMethod);
			getMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}
	
	
	/**
	 * @param httpClient Commons HttpClient
	 * @param url	请求地址
	 * @param fileName	保存的文件名称，如果为空则使用UUID生成的文件名并存放到java.io.tmpdir
	 * @return 保存的文件名称
	 * @throws Exception
	 */
	public static String getImage(HttpClient httpClient, String url, String fileName) throws Exception {
		GetMethod getMethod = new GetMethod(url);
		HttpMethods.setStandardInfo(getMethod);
		
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(getMethod);
			printHeadersInfo(getMethod);
			if (statusCode == HttpStatus.SC_OK){
				String imageType = getMethod.getResponseHeader("Content-Type").getValue().split("/")[1].toLowerCase();
				if(fileName == null || fileName.equals("")){
					fileName = System.getProperty("java.io.tmpdir")  + java.util.UUID.randomUUID().toString() + "." + imageType;
				}
				ByteArrayInputStream in = new ByteArrayInputStream(getMethod.getResponseBody());
				
				BufferedImage bi = ImageIO.read(in);
				ImageIO.write(bi, imageType, new File(fileName));
				return fileName;
			}
			Header header = getMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return null;
				GetMethod tempMethod = new GetMethod(location);
				setStandardInfo(tempMethod);
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK) {
						String imageType = getMethod.getResponseHeader("Content-Type").getValue().split("/")[1].toLowerCase();
						if(fileName == null || fileName.equals("")){
							fileName = System.getProperty("java.io.tmpdir") + "/" + java.util.UUID.randomUUID().toString() + "." + imageType;
						}
						ByteArrayInputStream in = new ByteArrayInputStream(getMethod.getResponseBody());
						
						BufferedImage bi = ImageIO.read(in);
						ImageIO.write(bi, imageType, new File(fileName));
						return fileName;
					}

				} catch (Exception e) {
					logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			} // end of while
		} catch (Exception e) {
			logger.error("HttpMethods.java中getByteData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			// printHeadersInfo(getMethod);
			getMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}




	/* post方法提交表单信息 */
	public static String postData(HttpClient httpClient, NameValuePair[] nameValuePair, String postURL, String[][] manuHeader)
			throws Exception {
		logger.debug("POST: " + postURL);
		PostMethod postMethod = new PostMethod(postURL);
		postMethod.addParameters(nameValuePair);
		HttpMethods.setStandardInfo(postMethod);
		String location = "";
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				logger.debug("Add header[" + manuHeader[i][0] + "], value[" + manuHeader[i][1] + "]");
				postMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		postMethod.addRequestHeader("Cookie", "payMethod=directPay");

		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			logger.debug("status: " + statusCode);
			printHeadersInfo(postMethod);
			// logger.info(postMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK){
				return postMethod.getResponseBodyAsString();
				/*
                BufferedReader bf = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
                String tempStr = "";
                String returnStr = "";
                while((tempStr = bf.readLine()) != null){
                	returnStr += tempStr;
                	returnStr += "\n";
                }
                return returnStr;
				 */
			}
			// Only test for GDTELCOM
			if(statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
				return postMethod.getResponseBodyAsString();
			}
			Header header = postMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				if (header != null)
					location = header.getValue();
				else
					return "";
				logger.debug("Location is changed to: " + location);
				//url = java.net.URLEncoder.encode(url, "GB2312");
				PostMethod tempMethod = new PostMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				/*if(manuHeader != null && manuHeader.length > 0){
					for(int i=0; i < manuHeader.length; i++){
						logger.info("Add header[" + manuHeader[i][0] + "], value[" + manuHeader[i][1] + "]");
						tempMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
					}
				}*/try {
					statusCode = httpClient.executeMethod(tempMethod);
					// logger.info(tempMethod.getResponseBodyAsString());
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK){
						return tempMethod.getResponseBodyAsString();
						/*
                        BufferedReader bf = new BufferedReader(new InputStreamReader(tempMethod.getResponseBodyAsStream()));
                        String tempStr = "";
                        String returnStr = "";
                        while((tempStr = bf.readLine()) != null){
                        	returnStr += tempStr;
                        	returnStr += "\n";
                        }
                        return returnStr;
						 */
					}
				}  catch (Exception e) {
					logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
					return location;
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			} // end of while
		}  catch (Exception e) {
			logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
			return location;
		} finally {
			//printHeadersInfo(postMethod);
			postMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}
	
	/* post方法提交表单信息，并返回location地址和页面 */
	public static String[] postDataWithLocationReturn(HttpClient httpClient, NameValuePair[] nameValuePair, String postURL, String[][] manuHeader)
			throws Exception {
		String[] result = new String[2];
		logger.debug("POST: " + postURL);
		PostMethod postMethod = new PostMethod(postURL);
		postMethod.addParameters(nameValuePair);
		HttpMethods.setStandardInfo(postMethod);
		String location = "";
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				logger.debug("Add header[" + manuHeader[i][0] + "], value[" + manuHeader[i][1] + "]");
				postMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}

		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			logger.debug("status: " + statusCode);
			printHeadersInfo(postMethod);
			// logger.info(postMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK){
				result[0] = null;
				result[1] = postMethod.getResponseBodyAsString();
				return result;
				
			}
			
			Header header = postMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				if (header != null)
					location = header.getValue();
				else
					return null;
				logger.debug("Location is changed to: " + location);
				result[0] = location;
				//url = java.net.URLEncoder.encode(url, "GB2312");
				PostMethod tempMethod = new PostMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				/*if(manuHeader != null && manuHeader.length > 0){
					for(int i=0; i < manuHeader.length; i++){
						logger.info("Add header[" + manuHeader[i][0] + "], value[" + manuHeader[i][1] + "]");
						tempMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
					}
				}*/try {
					statusCode = httpClient.executeMethod(tempMethod);
					// logger.info(tempMethod.getResponseBodyAsString());
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK){
						result[1] = tempMethod.getResponseBodyAsString();
						return result;
						/*
                        BufferedReader bf = new BufferedReader(new InputStreamReader(tempMethod.getResponseBodyAsStream()));
                        String tempStr = "";
                        String returnStr = "";
                        while((tempStr = bf.readLine()) != null){
                        	returnStr += tempStr;
                        	returnStr += "\n";
                        }
                        return returnStr;
						 */
					}
				}  catch (Exception e) {
					logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
					return null;
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			} // end of while
		}  catch (Exception e) {
			logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
			return null;
		} finally {
			//printHeadersInfo(postMethod);
			postMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}

	/* post方法提交表单信息，并在302转向时手工提交Cookie */
	public static String postDataWith302Cookie(HttpClient httpClient, NameValuePair[] nameValuePair, String postURL, String[][] manuHeader)
			throws Exception {
		logger.debug("POST: " + postURL);
		PostMethod postMethod = new PostMethod(postURL);
		postMethod.addParameters(nameValuePair);
		HttpMethods.setStandardInfo(postMethod);
		String location = "";
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				logger.debug("Add header[" + manuHeader[i][0] + "], value[" + manuHeader[i][1] + "]");
				postMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			logger.debug("status: " + statusCode);
			// printHeadersInfo(postMethod);
			// logger.info(postMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK){
				return postMethod.getResponseBodyAsString();
				/*
                BufferedReader bf = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
                String tempStr = "";
                String returnStr = "";
                while((tempStr = bf.readLine()) != null){
                	returnStr += tempStr;
                	returnStr += "\n";
                }
                return returnStr;
				 */
			}
			// Only test for GDTELCOM
			if(statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
				return postMethod.getResponseBodyAsString();
			}
			Header header = postMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				if (header != null)
					location = header.getValue();
				else
					return "";
				logger.debug("Location is changed to: " + location);
				//url = java.net.URLEncoder.encode(url, "GB2312");
				PostMethod tempMethod = new PostMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				if(manuHeader != null && manuHeader.length > 0){
					for(int i=0; i < manuHeader.length; i++){
						logger.debug("Add header[" + manuHeader[i][0] + "], value[" + manuHeader[i][1] + "]");
						tempMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
					}
				}try {
					statusCode = httpClient.executeMethod(tempMethod);
					// logger.info(tempMethod.getResponseBodyAsString());
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK){
						return tempMethod.getResponseBodyAsString();
						/*
                        BufferedReader bf = new BufferedReader(new InputStreamReader(tempMethod.getResponseBodyAsStream()));
                        String tempStr = "";
                        String returnStr = "";
                        while((tempStr = bf.readLine()) != null){
                        	returnStr += tempStr;
                        	returnStr += "\n";
                        }
                        return returnStr;
						 */
					}
				} catch (Exception e) {
					logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
					return location;
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			} // end of while
		} catch (Exception e) {
			logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
			return location;
		} finally {
			//printHeadersInfo(postMethod);
			postMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}



	/* post方法提交表单信息 */
	public static String[] postCookieAndData(HttpClient httpClient, NameValuePair[] nameValuePair, String postURL, String[][] manuHeader)
			throws Exception {
		logger.debug("POST: " + postURL);
		PostMethod postMethod = new PostMethod(postURL);
		postMethod.addParameters(nameValuePair);
		HttpMethods.setStandardInfo(postMethod);

		//logger.info("DEBUG: manuHeader.length:" + manuHeader.length);
		if(manuHeader != null && manuHeader.length > 0){
			for(int i=0; i < manuHeader.length; i++){
				logger.debug("Add header " + manuHeader[i][0] + ", value:" + manuHeader[i][1] + ".");
				postMethod.removeRequestHeader(manuHeader[i][0]);
				postMethod.setRequestHeader(manuHeader[i][0], manuHeader[i][1]);
			}
		}
		int statusCode = HttpStatus.SC_OK;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			logger.debug("status: " + statusCode);
			printHeadersInfo(postMethod);
			// logger.info(postMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK){
				String cookie = null;
				if(postMethod.getResponseHeader("Set-Cookie") != null){
					cookie = postMethod.getResponseHeader("Set-Cookie").getValue();
				}
				String body = postMethod.getResponseBodyAsString();
				String[] result = new String[2];
				result[0] = cookie;
				result[1] = body;
				return result;
				/*
                BufferedReader bf = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
                String tempStr = "";
                String returnStr = "";
                while((tempStr = bf.readLine()) != null){
                	returnStr += tempStr;
                	returnStr += "\n";
                }
                return returnStr;
				 */
			}
			Header header = postMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return null;
				logger.debug("Location is changed to: " + location);
				PostMethod tempMethod = new PostMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					// logger.info(tempMethod.getResponseBodyAsString());
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK){
						String cookie = tempMethod.getResponseHeader("Set-Cookie").getValue();
						String body = tempMethod.getResponseBodyAsString();
						String[] result = new String[2];
						result[0] = cookie;
						result[1] = body;
						return result;

						/*
                        BufferedReader bf = new BufferedReader(new InputStreamReader(tempMethod.getResponseBodyAsStream()));
                        String tempStr = "";
                        String returnStr = "";
                        while((tempStr = bf.readLine()) != null){
                        	returnStr += tempStr;
                        	returnStr += "\n";
                        }
                        return returnStr;
						 */
					}
				} catch (Exception e) {
					logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			} // end of while
		} catch (Exception e) {
			logger.error("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			//printHeadersInfo(postMethod);
			postMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}



	/* post方法提交表单信息 
	public static String postData(HttpClient httpClient, byte[] data, String postURL)
	throws Exception {
		PostMethod postMethod = new PostMethod(postURL);
		InputStream is = new ByteArrayInputStream(data);

		postMethod.setRequestBody(is);

		HttpMethods.setStandardInfo(postMethod);
		int statusCode = HttpStatus.SC_OK;
		logger.info("11");
		try {
			statusCode = httpClient.executeMethod(postMethod);
			//           logger.info("Status: " + statusCode);
			// logger.info(postMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK)
				return postMethod.getResponseBodyAsString();
			Header header = postMethod.getResponseHeader("location");
			while (statusCode != HttpStatus.SC_OK) {
				String location;
				if (header != null)
					location = header.getValue();
				else
					return "";
				logger.info("location = " + location);
				PostMethod tempMethod = new PostMethod(location);
				HttpMethods.setStandardInfo(tempMethod);
				try {
					statusCode = httpClient.executeMethod(tempMethod);
					// logger.info(tempMethod.getResponseBodyAsString());
					header = tempMethod.getResponseHeader("location");
					if (statusCode == HttpStatus.SC_OK)
						return tempMethod.getResponseBodyAsString();
				} catch (ConnectException ce) {
					logger.info("HttpMethods.java中postData()中抛出ConnectionException错误，错误如下：" + ce.getMessage());
				} catch (HttpException he) {
					logger.info("HttpMethods.java中postData()中抛出http错误，错误如下：" + he.getMessage());
				} catch (IOException ioe) {
					logger.info("HttpMethods.java中postData()中抛出I/O错误，错误如下：" + ioe.getMessage());
				} catch (Exception e) {
					logger.info("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
				} finally {
					tempMethod.releaseConnection();
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			} // end of while
		} catch (ConnectException ce) {
			logger.info("HttpMethods.java中postData()中抛出ConnectionException错误，错误如下：" + ce.getMessage());
		} catch (HttpException he) {
			logger.info("HttpMethods.java中postData()中抛出http错误，错误如下：" + he.getMessage());
		} catch (IOException ioe) {
			logger.info("HttpMethods.java中postData()中抛出I/O错误，错误如下：" + ioe.getMessage());
		} catch (Exception e) {
			logger.info("HttpMethods.java中postData()中抛出中抛出Exception错误错误，错误如下：" + e.getMessage());
		} finally {
			// printHeadersInfo(postMethod);
			postMethod.releaseConnection();
		}
		throw new Exception("未能取到转向地址或者调用时出现未知异常！");
	}*/
}

