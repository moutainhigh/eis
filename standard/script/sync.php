<?php
/*
 * phpBB 3 Í¬²½³ÌÐò
 *
 * @authoer: NetSnake
 * @date: 2012-08-07
 */
define('IN_PHPBB', true);
$phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : './';
$phpEx = substr(strrchr(__FILE__, '.'), 1);
include($phpbb_root_path . 'common.' . $phpEx);
include($phpbb_root_path . 'includes/functions_sync.' . $phpEx);
include($phpbb_root_path . 'includes/DES.' . $phpEx);

$key = "ALla2lll";

$requestString = $_SERVER['QUERY_STRING'];
try{
	//echo "decoding str: $requestString";
	$crypt = new DES($key);
	$decoding = $crypt->decrypt($requestString);
	
}catch(Exception $e){
	return;

}
//echo "after decoding: $decoding";

$params = split('&',$decoding);
for( $i = 0; $i < count($params); $i++){
	//echo $params[$i];
	if(preg_match("/^action/",$params[$i])){
		list($temp, $action) = split('=',$params[$i]);
		continue;
	}
	if(preg_match("/^username/",$params[$i])){
		list($temp, $username) = split('=',$params[$i]);
		continue;
	}
	if(preg_match("/^password/",$params[$i])){
		list($temp, $password) = split('=',$params[$i]);
		continue;
	}
	
}
//echo $action . '/' . $username . '/' . $password;


if(!$username || !$password || !$action ){
	return;
}

/*
$signSource = $username . $password . $key;
$serverSign = md5($signSource);
echo $serverSign;*/
//return;


if($action == "login"){	
	if(userExist($username)){
		forumLogin($username, $password, false);
	} else {
		forumRegister($username, $password, $username, false);	
	}
	echo "<META HTTP-EQUIV= \"Refresh\" CONTENT=\"0;  URL=/\">"; 

}

if($action == "edit"){
	if(userExist($username)){
		forumEdit($username, $username, $password);

	} else {
		forumRegister($username, $password, $username, false);	
	}
	return;

}
return;

function desEncrypt($str, $key)  {  
   
  $block = mcrypt_get_block_size('des', 'ecb');    
  $pad = $block - (strlen($str) % $block);     
  $str .= str_repeat(chr($pad), $pad);    
  return bin2hex(mcrypt_encrypt(MCRYPT_DES, $key, $str, MCRYPT_MODE_ECB));
   
}

 function desDecrypt($str, $key) {
        $strBin = hex2bin(strtolower($str));
        $str = mcrypt_cbc(MCRYPT_DES, key, $strBin, MCRYPT_DECRYPT, $key);
        return $str;
}

function hex2bin($hexData) {
        $binData = "";
        for($i = 0; $i < strlen ( $hexData ); $i += 2) {
            $binData .= chr ( hexdec ( substr ( $hexData, $i, 2 ) ) );
        }
        return $binData;
}
?>
