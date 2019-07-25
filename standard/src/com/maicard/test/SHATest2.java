package com.maicard.test;

import java.util.Date;

import com.maicard.common.util.Crypt;
import com.maicard.standard.CommonStandard;

public class SHATest2 {

	public static void main(String[] argv){
		String src = "46EE16AFC00E632A78B0F50CC1465CDC4FB73B13875F2034CF0D37476BBB194B009CDE23878F092E088C64E4C406DA4826D2A99FBE89C27E40C893399C00EFDDF5E287710EF8BF70D63A3021AB62AB1992A24B2D5F67824322BB9BBB1AF9E48734597BC7B3AEC1C51FBD6917BD0B06E8118E5039D9B77E92B5A3E1F54145A65C2BF21A8664DB784C48B055835D293F34";
		Crypt crypt = new Crypt();
	//	crypt.setAesKey(CommonStandard.cookieAesKey);
		String token = crypt.aesDecrypt(src);
		String[] data = token.split("\\|");
		for(String d1 : data){
			System.out.println(d1);
		}

		String[] data2 = Crypt.base64Decode(data[0]).split("\\|");
		for(String d1 : data2){
			System.out.println(d1);
		}
		long time = 1373615844940l;
		System.out.println(new Date(time));

	}
}
