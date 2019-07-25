DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `incoming_statistic_gather`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `incoming_statistic_gather`(begintime DATETIME,endtime DATETIME)
BEGIN
 INSERT INTO incoming_stat(stat_time,total_money,share_cost,gross_Profit) SELECT endtime,SUM(success_money) AS total_money,SUM(csfc)+SUM(success_money*b.share_percent) AS p_fc,SUM(success_money-csfc-success_money*b.share_percent) AS gross_profit FROM
(
SELECT a.uuid,success_money,FLOOR(success_money*b.share_percent) AS csfc,total_money FROM 
(
SELECT a.uuid,SUM(b.`success_money`) AS success_money,SUM(b.`total_money`) AS total_money,b.`product_id`,b.`stat_time` FROM item_stat b 
LEFT JOIN 
(SELECT * FROM childrenlst WHERE LEVEL=2) AS a ON  INSTR(CONCAT(',',a.childrenlst,','),CONCAT(',',b.inviter,','))>0
LEFT JOIN 
partner c ON a.uuid=c.`uuid`
WHERE stat_time>=DATE_FORMAT(begintime,'%Y%m%d%H') AND stat_time<DATE_FORMAT(endtime,'%Y%m%d%H') AND a.uuid IS NOT NULL
GROUP BY a.childrenlst 
) AS a LEFT JOIN share_config b ON a.`product_id`=b.`object_id` AND b.`share_type`='business'  
) AS a LEFT JOIN share_config b ON b.`object_id`=0 AND a.success_money BETWEEN b.`begin_money` AND b.`end_money` AND share_type='channel'  ;   
-- "-------------------------------------------------------------------------------------"
INSERT INTO incoming_stat(stat_time,total_money,share_cost,gross_Profit) 
SELECT endtime,SUM(b.`success_money`) AS success_money,0,0 FROM item_stat b 
LEFT JOIN 
(SELECT * FROM childrenlst WHERE LEVEL=2) AS a ON  INSTR(CONCAT(',',a.childrenlst,','),CONCAT(',',b.inviter,','))>0
LEFT JOIN 
partner c ON a.uuid=c.`uuid`
WHERE stat_time>=DATE_FORMAT(begintime,'%Y%m%d%H') AND stat_time<DATE_FORMAT(endtime,'%Y%m%d%H') AND a.uuid IS NULL
GROUP BY a.childrenlst ;
END$$

DELIMITER ;