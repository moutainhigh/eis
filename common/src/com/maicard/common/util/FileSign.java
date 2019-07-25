package com.maicard.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

public class FileSign {
	
	private static final int BUFFER_SIZE = 8192;
	private static final String SIGN_TYPE="MD5";

	public static String getFileSign(String fileName) throws NoSuchAlgorithmException, IOException{
		File file = new File(fileName);
		InputStream ins = null;
		ins = new FileInputStream(file);
		return getFileSign(ins);


	}
	
	public static String getFileSign(byte[] content)throws NoSuchAlgorithmException, IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(content);
		return getFileSign(bis);

	}
	
	public static String getFileSign(InputStream is)throws NoSuchAlgorithmException, IOException{
		
		byte[] buffer = new byte[BUFFER_SIZE];
		MessageDigest md5 = MessageDigest.getInstance(SIGN_TYPE);

		int len;
		while((len = is.read(buffer)) != -1){
			md5.update(buffer, 0, len);
		}
		is.close();
		return DigestUtils.md5Hex(md5.digest());
		
	}
	
	public static void main(String[] argv){
		String src = "-----BEGIN CERTIFICATE-----MIICkDCCAfmgAwIBAgIJAN0YHVICEuwoMA0GCSqGSIb3DQEBCwUAMGExCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxFDASBgNVBAoMC01haWNhcmQuY29tMQswCQYDVQQLDAJUQzEVMBMGA1UEAwwMY2FAY2hhb2thLmNuMB4XDTE1MTIzMDE0MjQ1N1oXDTI1MTIyNzE0MjQ1N1owYTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEUMBIGA1UECgwLTWFpY2FyZC5jb20xCzAJBgNVBAsMAlRDMRUwEwYDVQQDDAxjYUBjaGFva2EuY24wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANSSeZINXOIWJAUtKD6p5xYeAFqe0YvV8psvVC2AEYjrMpgFikzU87uDniLjyJQmYbNAQ4hLR/wEdXyfFo3SnvvC7+A9axUCb/AMhEhT0y3u2+ceQ82XTFIcw2stjC4pwdlyhiL/ZlxG9e8MUJIgDPC+AhXXE95AVXi7wKfmI9lbAgMBAAGjUDBOMB0GA1UdDgQWBBSx5xp+ilkIt/lsfw0qjmrN/ZQW2TAfBgNVHSMEGDAWgBSx5xp+ilkIt/lsfw0qjmrN/ZQW2TAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4GBAK9je6qk9Y3qGHGXqXgBMFtJ0Jbwv73RwcUAhZULgd7u3d00ct8oWHDBEl9aqkZPtQAR/DO4Qu9vFrtDg5OW5M5IXBcGhUYBC8OgQpEJkxOJzqse12rDD/u/etDkW/+O4N4PfWjAs9r4nWXb/cEU6sF/Y8jANxYJUPucCb6mpN7N-----END CERTIFICATE-----";
		
		String fileName = "d:/work/ca_test.txt";
		try {
			System.out.println(getFileSign(src.getBytes()));
			System.out.println(getFileSign(fileName));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	


}
