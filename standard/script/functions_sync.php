<?php

define('IN_PHPBB', true);

function userExist($user_id){
	global $db, $template, $config, $auth, $phpEx, $phpbb_root_path, $cache, $user;
	
	$sql = 'SELECT user_id FROM ' . USERS_TABLE . " WHERE username = '" . $db->sql_escape($user_id) . "' ";
	$result = $db->sql_query($sql);
	$row = $db->sql_fetchrow($result);
	$db->sql_freeresult($result);   
	$uid = $row['user_id'];
	if($uid){
		return true;
	} 
	return false;
}


//同步登陆论坛
function forumLogin($username, $password, $admin = false)
{
   global $db, $template, $config, $auth, $phpEx, $phpbb_root_path, $cache, $user;
    
   $ss = array();
	$phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : './';
   $phpEx = substr(strrchr(__FILE__, '.'), 1);
   require_once($phpbb_root_path.'common.php');
   require_once($phpbb_root_path.'includes/functions_user.php');
   require_once($phpbb_root_path.'includes/functions_module.php');

   $_REQUEST['username'] = $_POST['username'] = $username;
   $_REQUEST['password'] = $_POST['password'] = $password;   
   $password   = request_var('password', '', true);
   $username   = request_var('username', '', true);
   $user->session_begin();
   $auth->acl($user->data);
   $user->setup();

   $_REQUEST['sid'] = $_POST['sid']= $user->session_id;

   $autologin   = false;
   $viewonline = 1;
   $admin       = ($admin) ? 1 : 0;
   $viewonline = ($admin) ? $user->data['session_viewonline'] : $viewonline;   

   $ss = $auth->login($username, $password, $autologin, $viewonline, $admin);
   return $ss['status'];
   //return //$user->session_id;
 }

//同步退出论坛
function forumLogout()
{
   global $db, $template, $config, $auth, $phpEx, $phpbb_root_path, $cache, $user;

$phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : './';
   $phpEx = substr(strrchr(__FILE__, '.'), 1);

   require_once($phpbb_root_path.'common.php');
   require_once($phpbb_root_path.'includes/functions_user.php');
   require_once($phpbb_root_path.'includes/functions_module.php');

   $user->session_kill();
   $user->session_begin();

}

//同步注册论坛会员
function forumRegister($user_name, $password, $email, $coppa = false)
{
   global $db, $template, $config, $auth, $phpEx, $phpbb_root_path, $cache, $user;

   $_REQUEST['username'] = $_POST['username']=$user_name;
   $_REQUEST['email'] = $_REQUEST['email_confirm'] = $_POST['email']= $_POST['email_confirm']= $email;
   $_REQUEST['new_password'] = $_REQUEST['password_confirm'] = $_POST['new_password']= $_POST['password_confirm']= $password;
   $phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : './';
   $phpEx = substr(strrchr(__FILE__, '.'), 1);

   require_once($phpbb_root_path.'common.php');
   require_once($phpbb_root_path.'includes/functions_user.php');
   require_once($phpbb_root_path.'includes/functions_module.php');

   // Start session management
   $user->session_begin();
   $auth->acl($user->data);
   $user->setup();
	echo "yyyy\n";

   $timezone = date('Z') / 3600;
   $is_dst = date('I');

   if ($config['board_timezone'] == $timezone || $config['board_timezone'] == ($timezone - 1))
   {
      $timezone = ($is_dst) ? $timezone - 1 : $timezone;

      if (!isset($user->lang['tz_zones'][(string) $timezone]))
      {
         $timezone = $config['board_timezone'];
      }
   }
   else
   {
      $is_dst = $config['board_dst'];
      $timezone = $config['board_timezone'];
   }

   $data = array(
      'username'         => utf8_normalize_nfc(request_var('username', '', true)),
      'new_password'      => request_var('new_password', '', true),
      'password_confirm'   => request_var('password_confirm', '', true),
      'email'            => strtolower(request_var('email', '')),
      'email_confirm'      => strtolower(request_var('email_confirm', '')),
      'lang'            => basename(request_var('lang', $user->lang_name)),
      'tz'            => request_var('tz', (float) $timezone),
   );
   $server_url = generate_board_url();
   // Which group by default?
   $group_name = ($coppa) ? 'REGISTERED_COPPA' : 'REGISTERED';

   $sql = 'SELECT group_id
      FROM ' . GROUPS_TABLE . "
      WHERE group_name = '" . $db->sql_escape($group_name) . "'
         AND group_type = " . GROUP_SPECIAL;
   $result = $db->sql_query($sql);
   $row = $db->sql_fetchrow($result);
   $db->sql_freeresult($result);

   $group_id = $row['group_id'];
	echo "group_id=$group_id";
   if (($coppa ||
      $config['require_activation'] == USER_ACTIVATION_SELF ||
      $config['require_activation'] == USER_ACTIVATION_ADMIN) && $config['email_enable'])
   {
      $user_actkey = gen_rand_string(10);
      $key_len = 54 - (strlen($server_url));
      $key_len = ($key_len < 6) ? 6 : $key_len;
      $user_actkey = substr($user_actkey, 0, $key_len);

      $user_type = USER_INACTIVE;
      $user_inactive_reason = INACTIVE_REGISTER;
      $user_inactive_time = time();
   }
   else
   {
      $user_type = USER_NORMAL;
      $user_actkey = '';
      $user_inactive_reason = 0;
      $user_inactive_time = 0;
   }

   $user_row = array(
      'username'            => $data['username'],
      'user_password'         => phpbb_hash($data['new_password']),
      'user_email'         => $data['email'],
      'group_id'            => (int) $group_id,
      'user_timezone'         => (float) $data['tz'],
      'user_dst'            => $is_dst,
      'user_lang'            => $data['lang'],
      'user_type'            => $user_type,
      'user_actkey'         => $user_actkey,
      'user_ip'            => $user->ip,
      'user_regdate'         => time(),
      'user_inactive_reason'   => $user_inactive_reason,
      'user_inactive_time'   => $user_inactive_time,
   );

   // Register user...
   $user_id = user_add($user_row);
   if($user_id){
      return forumLogin($data['username'],$data['new_password'],false);
   }

}

//联动删除论坛会员信息
function forumDelete($user_id)
{
    global $db, $template, $config, $auth, $phpEx, $phpbb_root_path, $cache, $user;   

   $phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : './';
   $phpEx = substr(strrchr(__FILE__, '.'), 1);

   require_once($phpbb_root_path.'common.php');
   require_once($phpbb_root_path.'includes/functions_user.php');
   require_once($phpbb_root_path.'includes/functions_module.php');

   $user->session_begin();
   $auth->acl($user->data);
   $user->setup();

   $sql = 'SELECT user_id FROM ' . USERS_TABLE . " WHERE username = '" . $db->sql_escape($user_id) . "' ";
   $result = $db->sql_query($sql);
   $row = $db->sql_fetchrow($result);
   $db->sql_freeresult($result);   
    $uid = $row[user_id];

   if (is_numeric($uid))
   {
      user_delete("retain",$uid);
   }

}

//联动修改论坛邮箱和密码
function forumEdit($user_id, $email, $password, $cu_password="")
{
    global $db, $template, $config, $auth, $phpEx, $phpbb_root_path, $cache, $user;   

   //$phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : dirname(__FILE__).'./';
	$phpbb_root_path = (defined('PHPBB_ROOT_PATH')) ? PHPBB_ROOT_PATH : './';
   $phpEx = substr(strrchr(__FILE__, '.'), 1);

   require_once($phpbb_root_path.'common.php');
   require_once($phpbb_root_path.'includes/functions_user.php');
   require_once($phpbb_root_path.'includes/functions_module.php');
 
   $user->session_begin();
   $auth->acl($user->data);
   $user->setup();

   $sql = 'SELECT user_id FROM ' . USERS_TABLE . " WHERE username = '" . $db->sql_escape($user_id) . "' ";
   $result = $db->sql_query($sql);
   $row = $db->sql_fetchrow($result);
   $db->sql_freeresult($result);   
   $uid = $row['user_id'];
	
    if(!$uid){
	echo "User not exist.";
      exit();
     }
    if(!$cu_password ){

        $sql_ary1 = $sql_ary2 = array();

      $sql_ary1 = array(
         'user_email'      => $email,
         'user_email_hash'   => crc32($email) . strlen($email)
      );

        if($password){
         $sql_ary2 = array(
            'user_password'      => phpbb_hash($password) ,
            'user_passchg'      => time()
         );
       }

        $sql_ary = array_merge($sql_ary1,$sql_ary2);
      if (sizeof($sql_ary))
      {
         $sql = 'UPDATE ' . USERS_TABLE . '
            SET ' . $db->sql_build_array('UPDATE', $sql_ary) . '
            WHERE user_id = ' . $uid;
         $db->sql_query($sql);
      }

   }
   else{

      if (!$user->data['user_password']){
         forumLogin($user_id,$cu_password,false);
         $user->session_begin();
         $auth->acl($user->data);
         $user->setup();
      }

      if ($user->data['user_password']){

         $_REQUEST['email'] = $_REQUEST['email_confirm'] = $_POST['email']= $_POST['email_confirm']= $email;
         $_REQUEST['new_password'] = $_REQUEST['password_confirm'] = $_POST['new_password']= $_POST['password_confirm']= $password;

         $data = array(
            'email'            => strtolower(request_var('email', $user->data['user_email'])),
            'email_confirm'      => strtolower(request_var('email_confirm', '')),
            'new_password'      => request_var('new_password', '', true),
            'password_confirm'   => request_var('password_confirm', '', true),
         );

         $sql_ary = array(
            'user_email'      => ($auth->acl_get('u_chgemail')) ? $data['email'] : $user->data['user_email'],
            'user_email_hash'   => ($auth->acl_get('u_chgemail')) ? crc32($data['email']) . strlen($data['email']) : $user->data['user_email_hash'],
            'user_password'      => ($auth->acl_get('u_chgpasswd') && $data['new_password']) ? phpbb_hash($data['new_password']) : $user->data['user_password'],
            'user_passchg'      => ($auth->acl_get('u_chgpasswd') && $data['new_password']) ? time() : 0,
         );

         if (sizeof($sql_ary))
         {
            $sql = 'UPDATE ' . USERS_TABLE . '
               SET ' . $db->sql_build_array('UPDATE', $sql_ary) . '
               WHERE user_id = ' . $uid;
            $db->sql_query($sql);
         }

      }
   }
}



?>