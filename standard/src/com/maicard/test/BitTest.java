package com.maicard.test;

import java.util.Date;
import java.util.LinkedHashSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.product.domain.Item;
import com.maicard.security.domain.User;
import com.maicard.standard.DataName;
import com.maicard.standard.SecurityStandard.UserExtraType;


public class BitTest {
	public static void main(String[] argv){
		int x = 11111;
		x = x >> 2;
		Crypt crypt = new Crypt();
		String serialnumber="14805091436269891";
		String password="091915378878378895";
		String order_id="kmwcsc_1278907";
		String timestamp=String.valueOf(new Date().getTime() / 1000);
		String sign= DigestUtils.md5Hex("ZGYDK100|8265671417|"+serialnumber+"|"+password+"|"+order_id+"|"+timestamp+"|ND2mAuev");
		//crypt.setDes3Key("x4buyuwggevY9zaC56P1dSA1");
		//serialnumber=crypt.des3Encrypt(serialnumber);
		//password=crypt.des3Encrypt(password);
		System.out.println("number sign:"+sign);
		Part[] requestData=new Part[10];
		requestData[0]=new StringPart("chargeFromAccount","8265671417");
		requestData[1]=new StringPart("content","点卡支付");
		requestData[2]=new StringPart("orderId",order_id);
		requestData[3]=	new StringPart("productCode","ZGYDK100");
		requestData[4]=	new StringPart("productSerialNumber",serialnumber);		
		requestData[5]=new StringPart("productPassword",password);
		requestData[6]=new StringPart("timestamp",timestamp);
		requestData[7]=new StringPart("payNotifyUrl","http://dcsapi.ba.com/order/chaoka_result");
		requestData[8]=new StringPart("cryptMode","NONE");
		requestData[9]=new StringPart("sign",sign);		
		String chargeResult = HttpUtils.postData("http://api.chaoka.cn/sale/v5/submit.xml",requestData);
		System.out.print(chargeResult);
	}


}
