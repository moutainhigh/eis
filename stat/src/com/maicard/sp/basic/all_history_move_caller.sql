DELIMITER $$

USE `eis_tongbao`$$

DROP PROCEDURE IF EXISTS `all_history_move_caller`$$

CREATE DEFINER=`tongbao`@`%` PROCEDURE `all_history_move_caller`()
BEGIN
 Œ
 
 CALL item_move_to_history (CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"00"),CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"05"),DATE_FORMAT(dt,"%m"));
SELECT SLEEP(3);
  CALL item_move_to_history (CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"06"),CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"12"),DATE_FORMAT(dt,"%m"));
SELECT SLEEP(3);
  CALL item_move_to_history (CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"13"),CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"19"),DATE_FORMAT(dt,"%m"));
SELECT SLEEP(3);
  CALL item_move_to_history (CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"20"),CONCAT(DATE_FORMAT(dt,"%Y%m%d"),"24"),DATE_FORMAT(dt,"%m"));
  
  
END$$

DELIMITER ;