package com.maicard.test.serviceTest;

import com.maicard.common.util.ShortMd5;

public class PartnerGenerate {
	public static void main(String[] argv){
		int j = 300015;
		for( int i =2035 ; i < 2065; i++){
			String password = "yeelePA" + (int)(Math.random() * 100000);
			String cryptPassword = com.maicard.common.util.Crypt.passwordEncode(password);
			String username = "ylp" + i;
			String csv = username + "," + password;
			String sql = "insert into partner(uuid,username,user_password,current_status,crypt_key,parent_uuid, root_uuid)values(" + j + ",\"" + username + "\",\"" + cryptPassword + "\",120001,\"" + password + "\",300001,300001);";
			System.out.println(sql);
			
			String inviteCode = "p" + ShortMd5.encode(String.valueOf(j));

			
			sql = "insert into user_config(uuid,config_name,config_description,config_value,current_status)values(" + j + ",\"user.inviteCode\",\"推广码\",\"" + inviteCode + "\",100001);";
			//System.out.println(csv);
			System.out.println(sql);
			j++;
		}
	}

}
