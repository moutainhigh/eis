package com.maicard.common.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

public class Sslv3SocketFactory implements ProtocolSocketFactory{

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException,
	UnknownHostException {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket socket = (SSLSocket) factory.createSocket(arg0, arg1);
		socket.setEnabledProtocols(new String[]{"SSLv3"}); // <--- THIS IS THE WORK-AROUND

		return socket;
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket socket = (SSLSocket) factory.createSocket(arg0, arg1);
		socket.setEnabledProtocols(new String[]{"SSLv3"}); // <--- THIS IS THE WORK-AROUND

		return socket;
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2,
			int arg3, HttpConnectionParams arg4) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket socket = (SSLSocket) factory.createSocket(arg0, arg1);
		socket.setEnabledProtocols(new String[]{"SSLv3"}); // <--- THIS IS THE WORK-AROUND

		return socket;
	}

}
