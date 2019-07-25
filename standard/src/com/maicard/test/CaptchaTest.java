package com.maicard.test;

import java.awt.Color;

import com.maicard.captcha.domain.Captcha;
import com.maicard.captcha.service.impl.generator.PatchcaOrgGeneratorImpl;



public class CaptchaTest {
	public static void main(String[] argv){
		PatchcaOrgGeneratorImpl service = new PatchcaOrgGeneratorImpl();
		Captcha captcha = service.generate(null);
		System.out.println(new Color(51,122,183).getRGB());
		
		//aa

	}

}
