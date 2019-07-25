DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `item_statistic_gather`$$

CREATE DEFINER=`chaoka`@`%` PROCEDURE `item_statistic_gather`(
	stat_enter_time VARCHAR(255), 
	stat_end_time VARCHAR(255),
        table_suffix VARCHAR(255),	
	save_data BOOLEAN
)
BEGIN		
        DECLARE insert_str VARCHAR(1000); 
        DECLARE total INT;       
        IF save_data=TRUE THEN        
          SET insert_str='insert into item_stat(stat_time,inviter,product_id,success_money,success_count,total_money,total_count,server) ';        
          DELETE FROM item_stat WHERE stat_time >= stat_enter_time AND stat_time <= stat_end_time;
        ELSE
          SET insert_str='';
        END IF;  
        SET @stat_enter_time=stat_enter_time; 
        SET @stat_end_time=stat_end_time;       
        SET @sqlstring1=CONCAT(insert_str,'SELECT a.stat_time,a.charge_from_account,a.product_id,a.success_money,a.success_count,b.total_money,b.total_count,a.product_id FROM (SELECT   charge_from_account,  product_id,  SUM(success_money) AS success_money,  COUNT(*) AS success_count,  DATE_FORMAT(enter_time, "%Y%m%d%H") AS stat_time FROM  item WHERE  success_money>0 AND DATE_FORMAT(enter_time, "%Y%m%d%H")>=? AND DATE_FORMAT(enter_time, "%Y%m%d%H")<=? GROUP BY charge_from_account,  DATE_FORMAT(enter_time, "%Y%m%d%H"),  product_id  ) AS a INNER JOIN (SELECT   charge_from_account,  product_id,  SUM(request_money) total_money,  COUNT(*) AS total_count,  DATE_FORMAT(enter_time, "%Y%m%d%H") AS stat_time FROM  item WHERE  DATE_FORMAT(enter_time, "%Y%m%d%H")>=? AND DATE_FORMAT(enter_time, "%Y%m%d%H")<=? GROUP BY charge_from_account,  DATE_FORMAT(enter_time, "%Y%m%d%H"),  product_id  ) AS b ON a.charge_from_account=b.charge_from_account AND a.product_id=b.product_id and a.stat_time=b.stat_time');
        PREPARE sqlstring FROM @sqlstring1;
	EXECUTE sqlstring USING @stat_enter_time,@stat_end_time,@stat_enter_time,@stat_end_time;	
        SELECT ROW_COUNT() INTO total;
        SET @sqlstring1=CONCAT(insert_str,'SELECT a.stat_time,a.charge_from_account,a.product_id,a.success_money,a.success_count,b.total_money,b.total_count,a.product_id FROM (SELECT   charge_from_account,  product_id,  SUM(success_money) AS success_money,  COUNT(*) AS success_count,  DATE_FORMAT(enter_time, "%Y%m%d%H") AS stat_time FROM  item_history WHERE  success_money>0 AND DATE_FORMAT(enter_time, "%Y%m%d%H")>=? AND DATE_FORMAT(enter_time, "%Y%m%d%H")<=? GROUP BY charge_from_account,  DATE_FORMAT(enter_time, "%Y%m%d%H"),  product_id  ) AS a INNER JOIN (SELECT   charge_from_account,  product_id,  SUM(request_money) total_money,  COUNT(*) AS total_count,  DATE_FORMAT(enter_time, "%Y%m%d%H") AS stat_time FROM  item_history WHERE  DATE_FORMAT(enter_time, "%Y%m%d%H")>=? AND DATE_FORMAT(enter_time, "%Y%m%d%H")<=? GROUP BY charge_from_account,  DATE_FORMAT(enter_time, "%Y%m%d%H"),  product_id  ) AS b ON a.charge_from_account=b.charge_from_account AND a.product_id=b.product_id and a.stat_time=b.stat_time');
        PREPARE sqlstring FROM @sqlstring1;
	EXECUTE sqlstring USING @stat_enter_time,@stat_end_time,@stat_enter_time,@stat_end_time;	
	SELECT ROW_COUNT()+total;
END$$

DELIMITER ;