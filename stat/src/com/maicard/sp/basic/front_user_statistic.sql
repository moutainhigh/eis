DELIMITER $$

USE `eis_yeele`$$

DROP PROCEDURE IF EXISTS `front_user_statistic`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `front_user_statistic`(
	stat_start_time VARCHAR(255), 
	stat_end_time VARCHAR(255)
)
BEGIN
	/* 更新于2016-11-09,NetSnake */
    /* 统计注册人数和登录人数 */
	DELETE FROM front_user_stat WHERE stat_time>=stat_start_time AND stat_time <= stat_end_time;
	INSERT INTO front_user_stat(register_count,active_count,inviter,stat_time) SELECT COUNT(*),COUNT(*), inviter, DATE_FORMAT(create_time,"%Y%m%d%H") FROM front_user WHERE DATE_FORMAT(create_time,"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(create_time,"%Y%m%d%H")<=stat_end_time GROUP BY inviter,DATE_FORMAT(create_time,"%Y%m%d%H");
	/* 登录人数 */
	INSERT INTO front_user_stat(login_count,inviter,stat_time) SELECT COUNT(DISTINCT a.object_id) login_count, b.inviter, DATE_FORMAT(a.operate_time,"%Y%m%d%H") FROM operate_log AS a, front_user AS b  WHERE a.`object_id`=b.username AND DATE_FORMAT(a.operate_time,"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(a.operate_time,"%Y%m%d%H")<=stat_end_time AND a.object_type='user' AND a.operate_code='USER_LOGIN' AND a.operate_result=102008 GROUP BY b.inviter, DATE_FORMAT(a.operate_time,"%Y%m%d%H") ON DUPLICATE KEY UPDATE login_count=VALUES(login_count);
    END$$

DELIMITER ;