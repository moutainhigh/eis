package com.maicard.test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.maicard.common.util.Crypt;
import com.maicard.common.util.RSAUtils;

public class RSATest {
	public static void main(String[] argv){
		KeyPair kp = RSAUtils.genKeyPair();
		System.out.println(		Crypt.toHexString(kp.getPublic().getEncoded()));

		System.out.println(kp.getPublic().getAlgorithm());

		test();
		return;




	}    

	public static void test(){
		//	KeyPair kp = RSAUtils.genKeyPair();
		String publicKeyStr = "30819f300d06092a864886f70d010101050003818d00308189028181008c2f3ce1d991bd5dc41d2a06c190b7b3498e606ea35bd517b0fa120f7adb3c42dc075268de0a4a2a7c2e58c1b32675ec7a5114b2dbcf45346a1927e6ac3366cb1ed71916d8a164754ab6ba69cddc0c83c5fbd7db4976934b3751c3c218a7135267e2a3cb0817d71bb8410bffa6dfc0a0fb7d09810ab186dd40bf9db234c69e350203010001";
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Crypt.toByteArray(publicKeyStr));
			RSAPublicKey publicKey = (RSAPublicKey)factory.generatePublic(x509EncodedKeySpec);

			publicKey.getModulus().toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test2(){
		KeyPair kp = RSAUtils.genKeyPair();
		String publicModulus = ((RSAPublicKey) kp.getPublic()).getModulus().toString();
		String publicExp = ((RSAPublicKey) kp.getPublic()).getPublicExponent().toString();
		String publicModulus16 = ((RSAPublicKey) kp.getPublic()).getModulus().toString(16);
		String publicExp16 = ((RSAPublicKey) kp.getPublic()).getPublicExponent().toString(16);
		//publicModulus = "97917414082548316782488336626959105940017071702377214264945413013492462653255011544149790159843848949690108826906805688452055562869062497771028887463013660143821377851257710657849445340418149997672596586561283239686957518776469940799546865612127280017578054777843565972789325464832590966211971445250272640299";
		//publicExp = "65537";

		System.out.println("public modules=" + publicModulus);
		System.out.println("public exponent=" + publicExp);
		System.out.println("public modules16=" + publicModulus16);
		System.out.println("public exponent16=" + publicExp16);		
		String privateModulus = ((RSAPrivateKey) kp.getPrivate()).getModulus().toString();
		String privateExp = ((RSAPrivateKey) kp.getPrivate()).getPrivateExponent().toString();

		privateModulus = "91261358685632224714693439089118028671766686511326363584080996545459044422100466061894171771355225022488772814046870380908570830711579868837807590177528924293188800342403837373611678159389767029899899069734408520877802404148523437501743497949151930406613441601897686708847584226298255956881505210908693721557";
		privateExp = "78613130647400424402143879187894917232035590892326754806837489951895034771278204847005105103683230441118470183929719402533110421242832749673765807038500645124974072905422780473392761874925413190259056230045540138073673702414186530008300140377997163554754630675543627039693220562548529544473651473091688629833";

		System.out.println("private modules=" + privateModulus);
		System.out.println("private exponent=" + privateExp);

		String src = "yxzh2015";
		StringBuffer sb = new StringBuffer();
		sb.append(src);
		System.out.println("XX=" + sb.reverse().toString());
		String dst = (RSAUtils.encrypt(src, publicModulus, publicExp));
		System.out.println("dst=" + dst);
		dst = "4a9375a813065705aaa6712a56cb34e906f179463fe2bce4db7993f621f531b2eea36098c97b54491c0e37713877e85d6026f9ec2b88d15ef8b696b0cab0a511dc31c941f87fc5699f5f824cc62bf6467d2d2d7e7289cc33980a839e7f054fc7eb6407cc4e63476e7fdfc597cddb42e92286060fb74acc6fd0f62684b37e342d";
		//dst = "1782c06ad8e21a10fe43275ed53ef1a7f9c1c6d0d7cc5f2076e7e0af5313a3f88fc0e1776acb987d591345240ea74f992f2bcf867d72b7baff230080a186beafbb31cf5a4cdbbe8b89dd0fb5e1827d8bc47b284fc7c437ab9ae382fb004d311f36f277da9c2512928ff29b3a0e69da15e932a063cfa05826e7eef7f17af4b6c8
		src = RSAUtils.decrypt(dst, privateModulus, privateExp);
		System.out.println("src=" + src);
	}
}
