DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `insert_settlement`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `insert_settlement`(UUID INT ,commission FLOAT,begintime DATETIME,endtime DATETIME)
BEGIN   
  DECLARE begintime1,endtime1 DATETIME;  
  DECLARE tmpName VARCHAR(20) DEFAULT ''; 
  DECLARE curl CURSOR FOR SELECT datestring FROM 2to1 WHERE datestring BETWEEN begintime AND endtime ORDER BY datestring ASC;
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET tmpName = NULL;
  OPEN curl;
  FETCH curl INTO begintime1;
  FETCH curl INTO endtime1;
  REPEAT
  CALL generate_billing ("settlement",UUID,commission,begintime1,endtime1)  ;
  SET begintime1=endtime1;    
  FETCH curl INTO endtime1;
  UNTIL tmpName IS NULL
  END REPEAT;
  CLOSE curl;
END$$

DELIMITER ;