DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `change_inviter`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `change_inviter`(uuids VARCHAR(20000),dest_uuid INT,updatetime VARCHAR(10))
BEGIN
  DECLARE entertime DATETIME;  
  SET entertime=CONCAT(updatetime,' 00:00:00');  
  UPDATE item SET extra_status=0 WHERE charge_from_account IN (SELECT UUID FROM front_user WHERE inviter=0 AND INSTR(CONCAT(',',uuids,','),CONCAT(',',UUID,','))>0) AND enter_time<entertime;
  SELECT ROW_COUNT() AS item;
  UPDATE item_history SET extra_status=0 WHERE charge_from_account IN (SELECT UUID FROM front_user WHERE inviter=0 AND INSTR(CONCAT(',',uuids,','),CONCAT(',',UUID,','))>0) 
  AND enter_time<entertime;
  SELECT ROW_COUNT() AS item_history;  
  UPDATE front_user SET inviter=dest_uuid WHERE inviter = 0  AND INSTR(CONCAT(',',uuids,','),CONCAT(',',UUID,','))>0;
  SELECT ROW_COUNT() itemfordest;   
  CALL item_statistic_gather(DATE_FORMAT(entertime,"%Y%m%d%H"),DATE_FORMAT(SUBDATE(NOW(),INTERVAL 1 HOUR),"%Y%m%d%H"),'',TRUE);
 -- CALL item_statistic_gatherx(DATE_FORMAT(entertime,"%Y%m%d%H"),dest_uuid); 
END$$

DELIMITER ;