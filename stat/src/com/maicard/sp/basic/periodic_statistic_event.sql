DELIMITER $$

-- SET GLOBAL event_scheduler = ON$$     -- required for event to execute but not create    

CREATE	/*[DEFINER = { user | CURRENT_USER }]*/	EVENT `hour_statistic_event`
ON SCHEDULE EVERY 5 MINUTE  STARTS '2016-10-14 22:10:00' ON COMPLETION PRESERVE ENABLE DO BEGIN
	/* 更新于2016-11-09,NetSnake */
	/* 每几分钟执行一次，用于执行那些需要执行上一小时统计的存储过程 */
	DECLARE df1 VARCHAR(255);
	DECLARE df2 VARCHAR(255);

	SET df1=DATE_FORMAT(DATE_SUB(CURRENT_TIMESTAMP(),INTERVAL 1 HOUR),"%Y%m%d%H");
	SET df2=DATE_FORMAT(CURRENT_TIMESTAMP(),"%Y%m%d%H");

	CALL pay_statistic(df1,df2);
	CALL front_user_statistic(df1,df2);

	END$$

DELIMITER ;