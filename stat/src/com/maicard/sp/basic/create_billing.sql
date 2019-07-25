DELIMITER $$

USE `eis_XXX`$$

DROP PROCEDURE IF EXISTS `create_billing`$$

CREATE DEFINER=`XXX`@`%` PROCEDURE `create_billing`(billingId INT, uid BIGINT, begintime DATETIME,endtime DATETIME)
BEGIN
	/* 给指定billingId、用户和时间区间，更新pay中的对应的记录为billingId，更新完成后反过来更新billing表中的对应记录, @version 1, NetSnake, 2017-06-13. */
	DECLARE faceMoney FLOAT;
	DECLARE rowcount INT;
	DECLARE HISTORY_TABLE VARCHAR(20);
	DECLARE sqlText TEXT;
	
	IF DATE_FORMAT(beginTime,"%Y%m%d") > DATE_FORMAT(DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), "%Y%m%d")  THEN
		SET HISTORY_TABLE = 'pay';
	ELSE 
		SET HISTORY_TABLE = CONCAT('pay_',DATE_FORMAT(beginTime,"%m"));
	END IF;
	
	
	SET @sqlText=CONCAT('UPDATE ',HISTORY_TABLE, ' SET billing_id = ', billingId, ' WHERE pay_from_account=', uid, ' AND current_status=710010 AND billing_id=0 AND end_time IS NOT NULL AND end_time >="', beginTime, '" AND end_time <= "', endTime, '"');
	PREPARE exec FROM @sqlText;
    EXECUTE exec;
	
	SELECT ROW_COUNT() INTO rowcount;
	IF rowcount > 0 THEN
    BEGIN
		SET @sqlText = CONCAT('SELECT SUM(real_money) FROM ', HISTORY_TABLE, ' WHERE billing_id=', billingId, ' INTO @faceMoney');
		PREPARE exec FROM @sqlText;
		EXECUTE exec;
		UPDATE billing AS a, share_config AS b SET  a.face_money=@faceMoney, a.commission=b.share_percent, a.real_money=@faceMoney * b.share_percent WHERE a.billing_id=billingId AND a.uuid=b.share_uuid AND b.object_type='pay';
		SELECT ROW_COUNT();
	END;
	END IF;
    END$$

DELIMITER ;