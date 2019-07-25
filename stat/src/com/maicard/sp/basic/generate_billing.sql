DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `generate_billing`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `generate_billing`(tablename VARCHAR(30),custom_uuid INT,custom_commission FLOAT,begintime DATETIME,endtime DATETIME)
BEGIN
DECLARE tmpName VARCHAR(20) DEFAULT '';
DECLARE inviterlist VARCHAR(1000);
DECLARE totalmoney,sharepercent FLOAT DEFAULT 0;
DECLARE successmoney FLOAT;
DECLARE cur1 CURSOR FOR SELECT UUID FROM user_data_all WHERE data_value='true' AND data_define_id=97;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET tmpName = NULL;
IF custom_uuid>0.0 THEN
   SET inviterlist=getchildlst(custom_uuid); 
    INSERT INTO settlement(billing_begin_time,billing_end_time,billing_handler_time,UUID,share_percent,request_money,commission,success_money,object_id,object_type,current_status) SELECT a.billing_begin_time,a.billing_end_time,billing_handler_time,UUID,b.`share_percent`,request_money,commission,(request_money*b.`share_percent`)+custom_commission success_money,0,'business',710013 FROM (
    SELECT 
      begintime AS billing_begin_time,
      endtime AS billing_end_time,
      NOW() billing_handler_time,
      custom_uuid UUID,
      SUM(FLOOR(success_money)) request_money,
      custom_commission commission,
      0 product_id
      FROM item_stat a  WHERE stat_time>=DATE_FORMAT(begintime,'%Y%m%d%H') 
      AND stat_time<DATE_FORMAT(endtime,'%Y%m%d%H') AND INSTR(CONCAT(',',inviterlist,','),CONCAT(',',a.inviter,','))>0  
      -- GROUP BY uuid
     ) AS a LEFT JOIN share_config b ON a.product_id=b.`object_id` AND b.share_type='channel' WHERE request_money BETWEEN begin_money AND end_money ;
ELSE
  OPEN cur1;
  FETCH cur1 INTO tmpName;
  REPEAT
    SET inviterlist=get_children_lst(tmpName); 
    INSERT INTO settlement(billing_begin_time,billing_end_time,billing_handler_time,UUID,share_percent,request_money,commission,success_money,object_id,object_type,current_status) SELECT a.billing_begin_time,a.billing_end_time,billing_handler_time,UUID,b.`share_percent`,request_money,commission,request_money*b.`share_percent` success_money,0,'business',710013 FROM (
    SELECT 
      begintime AS billing_begin_time,
      endtime AS billing_end_time,
      NOW() billing_handler_time,
      tmpName UUID,
      SUM(FLOOR(success_money)) request_money,
      0 commission,
      0 product_id
      FROM item_stat a  WHERE stat_time>=DATE_FORMAT(begintime,'%Y%m%d%H') 
      AND stat_time<DATE_FORMAT(endtime,'%Y%m%d%H') AND INSTR(CONCAT(',',inviterlist,','),CONCAT(',',a.inviter,','))>0  
      -- GROUP BY uuid
     ) AS a LEFT JOIN share_config b ON a.product_id=b.`object_id` AND b.share_type='channel' WHERE request_money BETWEEN begin_money AND end_money ;
  FETCH cur1 INTO tmpName;
  UNTIL tmpName IS NULL
  END REPEAT;
  CLOSE cur1;
  SELECT 1;
END IF;
END$$

DELIMITER ;