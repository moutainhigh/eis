DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `incoming_statistic_gather_by_period`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `incoming_statistic_gather_by_period`(begintime DATETIME,endtime DATETIME)
BEGIN   
  DECLARE begintime1,endtime1 DATETIME;  
  DECLARE tmpName VARCHAR(20) DEFAULT ''; 
  DECLARE curl CURSOR FOR SELECT datestring FROM 2to1 WHERE datestring BETWEEN begintime AND endtime ORDER BY datestring ASC;
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET tmpName = NULL;
  OPEN curl;
  FETCH curl INTO begintime1;
  FETCH curl INTO endtime1;
  UPDATE item_stat SET inviter=300000 WHERE inviter=0;
  REPEAT
  CALL incoming_statistic_gather(begintime1,endtime1)  ;
  SET begintime1=endtime1;    
  FETCH curl INTO endtime1;
  UNTIL tmpName IS NULL
  END REPEAT;
  CLOSE curl;
END$$

DELIMITER ;